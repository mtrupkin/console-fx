package me.mtrupkin.game.model

import me.mtrupkin.console.{Colors, RGB, ScreenChar, Screen}
import me.mtrupkin.core.{Size, Point}
import me.mtrupkin.pathfinding.{Dijkstra, AStar}


/**
 * Created by mtrupkin on 12/19/2014.
 */
class CombatTracker(val world: World) {
  import world._
  type Matrix = Array[Array[Double]]
  var round: Int = 0

  def render(screen: Screen): Unit = {
    renderValidMove(screen, player)
    for (m <- world.mouse) {
      renderPath(screen, m, player)
    }
  }

  def nextTurn(): Unit = {
    for(agent <- agents) {
      agent.act(world)
    }
    dijkstraMap = validMoves(player)
    round += 1
  }

  val dijkstraPathFinder = new Dijkstra(tileMap)
  var dijkstraMap = validMoves(player)

  val pathChar = new ScreenChar('*', RGB(123, 123, 123), Colors.Black)
  def pathChar(i: Int) = new ScreenChar(i.toString.charAt(0), RGB(123, 123, 123), Colors.Black)
  val validMoveChar = new ScreenChar('.', RGB(31, 31, 31), Colors.Black)
  def validMoveChar(i: Int) = new ScreenChar(i.toString.charAt(0), RGB(31, 31, 31), Colors.Black)

  def renderPath(screen: Screen, target: Point, agent: Agent): Unit = {
    val p = target - agent.position
    for {

      n <- path(p, Nil)
    } {
      screen(n + agent.position) = pathChar
    }
  }

  def path(p: Point, acc: List[Point]): Seq[Point] = {
    val bounds = Size(player.maxMove, player.maxMove)
    if (bounds.inBounds(p)) {
      val cost = dijkstraMap(p.x)(p.y).toInt
      cost match {
        case 0 => acc
        case _ => {
          val n = for {
            x <- -1 to 1
            y <- -1 to 1
            p0 = p + (x, y)
            if bounds.inBounds(p0)
            if (dijkstraMap(p0.x)(p0.y) < cost)
          } yield p0
          path(n.head, p :: acc)
        }
      }
    } else Nil
  }

  def renderValidMove(screen: Screen, agent: Agent): Unit = {
    val move = agent.move

    for(x <- (-move to move)) {
      for (y <- (-move to move)) {
        val p = agent.position + (x, y)
        if (screen.size.inBounds(p)) {
            val cost = dijkstraMap(x+move)(y+move).toInt
            if ((cost > 0) && (cost <= move)) screen(p) = validMoveChar(cost)
        }
      }
    }
  }

  def validMoves(agent: Agent): Matrix = dijkstraPathFinder.search(agent.position, agent.maxMove)
}


