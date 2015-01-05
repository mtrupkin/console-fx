package me.mtrupkin.game.model

import core.AStarPathFinder
import me.mtrupkin.console.{Colors, RGB, ScreenChar, Screen}
import me.mtrupkin.core.Point

/**
 * Created by mtrupkin on 12/19/2014.
 */
class CombatTracker(val world: World) {
  import world._

  var round: Int = 0

  var currentAgent: Option[Int] = None

  def render(screen: Screen): Unit = {
    currentAgent match {
      case None => renderValidMove(screen, player)
    }
  }

  def nextTurn(): Unit = {
    for(agent <- agents) {
      agent.act(world)
    }
    round += 1
  }

  val finder = new AStarPathFinder(tileMap)

  def renderValidMove(screen: Screen, agent: Agent): Unit = {
    val move = agent.move

    val c: ScreenChar = new ScreenChar('.', RGB(31, 31, 31), Colors.Black)
    for(x <- (-move to move)) {
      for (y <- (-move to move)) {
        val p = agent.position + (x, y)
        if (screen.size.inBounds(p)) {
          val path = finder.search(agent, p, move+1)
          // path list includes starting position
          val cost = calcMove(0, path)
          if ((cost > 0) && (cost <= move)) screen(p) = c
        }
      }
    }
  }


  def calcMove(acc: Double, path: Seq[Point]): Double = {
    path match {
      case p :: Nil => acc
      case p :: ps => calcMove(acc + dist2(p, ps.head), ps)
      case _ => acc
    }
  }

  def dist2(p0: Point, p1: Point): Double = {
    val p = p1 - p0
    Math.sqrt(p.x*p.x + p.y*p.y)
  }
}


