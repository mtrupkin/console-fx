package  me.mtrupkin.game.model


import me.mtrupkin.console.{ScreenChar}
import me.mtrupkin.core.{Point, Size}

/**
 * Created by mtrupkin on 12/19/2014.
 */
abstract class Agent(val name: String,
  val sc: ScreenChar,
  var position: Point,
  val stats: Stats = new Stats,
  var currentHP: Option[Int] = None) extends Entity {
  import stats._
  def floor(d: Double): Int = Math.floor(d).toInt

  var hp: Int = currentHP.getOrElse(maxHP)
  def move: Int = 4 + floor(dex / 3)

  def takeDamage(amount: Int) = hp -= amount

  def move(direction: Point) = { position = position + direction }

  def maxHP = (str + dex + int) * 10

  def act(tracker: CombatTracker): Unit

  def melee: Combat = Combat((str + floor(dex/2) + floor(int/3)))
  def ranged: Combat = Combat((dex + floor(str/2) + floor(int/3)))

  def defense: Int = floor((str + dex + int) / 3)
}

object Agent {
  def toAgent(sc: ScreenChar, p: Point): Option[Agent] = {
    sc.c match {
      case 'T' => Some(new Agent("Turret", 'T', p) {
        def act(tracker: CombatTracker): Unit = tracker.attack(this, tracker.world.player)//Combat.attack(ranged, world.player)
      })
      case '@' => Some(new Agent("Player", '@', p, Stats(str = 1)) {
        def act(tracker: CombatTracker): Unit = ???
      })
      case _ => None
    }
  }

  def toAgents(matrix: Seq[Seq[ScreenChar]]): (Agent, Seq[Agent]) = {
    val agents = for {
      (i, x) <- matrix.zipWithIndex
      (t, y) <- i.zipWithIndex
    } yield toAgent(t, Point(x, y))

    val player = agents.flatten.find((p:Agent) => p.name == "Player").getOrElse(new Agent("Player", '@', Point(1, 1), Stats(str = 1)){
      def act(tracker: CombatTracker): Unit = ???
    })
    (player, agents.flatten.filter((p:Agent) => p.name != "Player"))
  }
}