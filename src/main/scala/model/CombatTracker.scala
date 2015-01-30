package me.mtrupkin.game.model

import me.mtrupkin.console.{Colors, RGB, ScreenChar, Screen}
import me.mtrupkin.core.{StateMachine, Size, Point}
import me.mtrupkin.pathfinding.{Dijkstra, AStar}

import scala.collection.immutable.List


/**
 * Created by mtrupkin on 12/19/2014.
 * World controller
 */
class CombatTracker(val world: World) extends StateMachine  {
  type StateType = ActionState
  def initialState: StateType = new InputAction()

  trait ActionState extends State {
    def update(elapsed: Int): Unit
    def render(screen: Screen): Unit
    def target(p: Point): Unit = {}
  }

  def end: Boolean = {
    (agents == Nil) || (player.hp < 0)
  }

  var round: Int = 0
  var actionsLeft: Int = 2

  var mouse: Option[Point] = None

  val pathFinder = new Dijkstra(world.tileMap)

  def agents: Seq[Agent] = world.agents.filter(_.hp >= 0)
  def player = world.player

  def update(elapsed: Int) = {
    world.update(elapsed)
    currentState.update(elapsed)
  }

  def render(screen: Screen) = {
    world.render(screen)
    renderAgents(screen)
    currentState.render(screen)
  }

  def renderAgents(screen: Screen) = {
    for (a <- agents) {
      // TODO: display range
      if (lineOfSight(a.position, world.player.position) != Nil)
        world.renderAgent(screen, a)
      else
        screen.write(a.position, a.sc.copy(fg = RGB(123, 123, 123)))
    }
  }

  def target(p: Point): Unit = currentState.target(p)

  def nextAction(): ActionState = {
    if (actionsLeft > 0) new InputAction
    else {
      actionsLeft = 2
      round += 1
      enemyAction()
    }
  }

  def enemyAction(): ActionState = {
    for(agent <- agents) {
      agent.act(this)
    }

    ???
  }

  def attack(attacker: Agent, defender: Agent, resolution: (Combat, Int) => Int = Combat.attack): Boolean = {
    val lineOfSight = this.lineOfSight(attacker.position, defender.position)
    if (lineOfSight != Nil) {
      Combat.attack(attacker.melee, defender, resolution)
      true
    } else false
  }

  // uses neighbors in addition to current tile
  def lineOfSight(p: Point, p0: Point): Seq[Point] = {
    for( n <- p :: world.tileMap.size.neighbors(p, 1).toList ) {
      val line = lineOfSightSingle(n, p0)
      if (line != Nil) return line
    }
    Nil
  }

  // uses current tile only
  protected def lineOfSightSingle(p: Point, p0: Point): Seq[Point] = {
    // TODO: optimization candidate
    val line = CombatTracker.bresenham(p, p0).toSeq
    if (line.forall(world.tileMap(_).move)) line else Nil
  }

  val actions = List(new AttackAction(this), new MoveAction(this))

  def getAction(p: Point): Option[Action] = {
    def getAction(target: Point, actions: List[Action]): Option[Action] = {
      actions match {
        case action :: xs => if (action.canAct(target)) Some(action) else getAction(target, xs)
        case Nil => None
      }
    }
    getAction(p, actions)
  }

  class InputAction extends ActionState {
    import InputAction._

    override def target(p: Point): Unit = {
      for( action <- getAction(p) ) {
        action match {
          case move: MoveAction => changeState(new MoveState(world.player, p))
          case attack: AttackAction =>
            attack.act(p)
            changeState(new AttackState(world.player, p))
        }
        actionsLeft -= 1
      }
    }

    def update(elapsed: Int): Unit = {}

    def render(screen: Screen): Unit = {
      renderValidMoves(screen)
      renderValidPath(screen)
    }

    def renderValidPath(screen: Screen): Unit = {
      for (m <- mouse) {
        getAction(m) match {
          case Some(action: MoveAction) => renderPath(screen, m)
          case _ =>
        }
      }
    }

    def renderValidMoves(screen: Screen): Unit = {
      val move = world.player.move
      val p0 = world.player.position

      for {
        x <- -move to move
        y <- -move to move
        p = p0 + (x, y)
        moves = pathFinder.moveCount(p, p0, move)
        if ((moves > 0) && (moves <= move))
        if agents.forall(_.position != p)
      } screen(p) = moveChar
    }

    def renderPath(screen: Screen, target: Point): Unit = {
      val move = world.player.move
      val p0 = world.player.position


      val moves = pathFinder.moveCount(target, p0, move)
      if ((moves > 0) && (moves <= move)) {
        val path = pathFinder.path(target, p0)
        val smoothPoints = smoothPathPoints(p0, path, Nil)

        smoothPoints.foreach(screen(_) = pathChar)
      }
    }
  }

  def hasLineOfMovement(p: Point, p0: Point): Boolean = {
    CombatTracker.bresenham(p, p0).forall(world.tileMap(_).move)
  }

  // returns smooth path up to point p0
  // finds the line from p0 to a point pn previousPath that is not blocked
  // returns the line and the rest of the path
  // param next is the last point that had a line of sight to p0
  def smoothPreviousPath(p0: Point, next: Point, previousPath: List[Point]): List[Point] = {
    previousPath match {
      case p :: ps => if (hasLineOfMovement(p, p0))
        smoothPreviousPath(p0, p, ps)
      else
        p0 :: (CombatTracker.bresenham(next, p0) ++ previousPath).toList
      case Nil => p0 :: CombatTracker.bresenham(next, p0).toList
    }
  }

  def smoothPathPoints(p0: Point, path: Seq[Point], smoothed: List[Point]): Seq[Point] = {
    path match {
      case p :: ps => smoothPathPoints(p, ps, smoothPreviousPath(p, p0, smoothed))
      // XXX: try to remove reverse and tail call
      case Nil => smoothed.reverse.tail
    }
  }

  class MoveState(val agent: Agent, val p: Point) extends ActionState {
    val p0 = agent.position
    val path = pathFinder.path(p, p0)
    var smoothPath = smoothPathPoints(p0, path, Nil)

    def update(elapsed: Int): Unit = {
      smoothPath match {
        case x::xs =>
          agent.position = x
          smoothPath = xs
        case Nil => changeState(nextAction())
      }
    }

    def render(screen: Screen): Unit = {}
  }

  class AttackState(val agent: Agent, val p: Point) extends ActionState {
    var path = lineOfSight(p, agent.position).toList

    var time = 0
    val rate = 300
    def update(elapsed: Int): Unit = {
      time += elapsed

      path match {
        case p::ps => if (time > rate) {
          time -= rate
          path = ps
        }
        case Nil => changeState(nextAction())
      }
    }

    def render(screen: Screen): Unit = {
      path match {
        case p::ps => screen.write(p, 'Q')
        case Nil =>
      }
    }
  }

  class WaitState extends ActionState {
    def update(elapsed: Int): Unit = {}
    def render(screen: Screen): Unit = {}
  }

  class CompositeActionState(val actions: Seq[ActionState]) extends ActionState {
    def update(elapsed: Int): Unit = actions.foreach(_.update(elapsed))
    def render(screen: Screen): Unit = actions.foreach(_.render(screen))
  }
}

object InputAction {
  import Colors._

  val moveChar = ScreenChar('.', RGB(61, 61, 61), Black)
  val pathChar = ScreenChar('.', White, Black)
}


object Line {
  /**
   * Uses the Bresenham Algorithm to calculate all points on a line from p0 to p1.
   * The iterator returns all points in the interval [start, end].
   * @return the iterator containing all points on the line
   */
  protected def bresenham(p1: Point, p0: Point): Iterator[Point] = {
    import scala.math.abs
    val d = Point(abs(p1.x - p0.x), abs(p1.y - p0.y))

    val sx = if (p0.x < p1.x) 1 else -1
    val sy = if (p0.y < p1.y) 1 else -1

    new Iterator[Point] {
      var p = p0
      var err = d.x - d.y

      def next = {
        val e2 = 2 * err
        if (e2 > -d.y) {
          err -= d.y
          p = p.copy(x = p.x + sx)
        }
        if (e2 < d.x) {
          err += d.x
          p = p.copy(y = p.y + sy)
        }
        p
      }
      def hasNext = !(p == p1)
    }
  }
}
object CombatTracker {

  /**
   * Uses the Bresenham Algorithm to calculate all points on a line from p0 to p1.
   * The iterator returns all points in the interval [start, end).
   * @return the iterator containing all points on the line
   */
  protected def bresenham(p1: Point, p0: Point): Iterator[Point] = {
    import scala.math.abs
    val d = Point(abs(p1.x - p0.x), abs(p1.y - p0.y))

    val sx = if (p0.x < p1.x) 1 else -1
    val sy = if (p0.y < p1.y) 1 else -1

    new Iterator[Point] {
      var p = p0
      var err = d.x - d.y

      def next = {
        val e2 = 2 * err
        if (e2 > -d.y) {
          err -= d.y
          p = p.copy(x = p.x + sx)
        }
        if (e2 < d.x) {
          err += d.x
          p = p.copy(y = p.y + sy)
        }
        p
      }
      def hasNext = !(p == p1)
    }
  }
}


