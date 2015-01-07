package me.mtrupkin.game.model

import me.mtrupkin.console.{Colors, RGB, ScreenChar, Screen}
import me.mtrupkin.core.{Size, Point}
import me.mtrupkin.pathfinding.{PathFinder, Dijkstra, AStar}


/**
 * Created by mtrupkin on 12/19/2014.
 */
class CombatTracker(val world: World) {
  import world._
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
    round += 1
  }

  val pathFinder: PathFinder = new Dijkstra(tileMap)

  val pathChar = new ScreenChar('*', RGB(123, 123, 123), Colors.Black)
  def pathChar(i: Int) = new ScreenChar(i.toString.charAt(0), RGB(123, 123, 123), Colors.Black)
  val validMoveChar = new ScreenChar('.', RGB(31, 31, 31), Colors.Black)
  def validMoveChar(i: Int) = new ScreenChar(i.toString.charAt(0), RGB(31, 31, 31), Colors.Black)

  def renderPath(screen: Screen, target: Point, agent: Agent): Unit = {
    val moves = pathFinder.moves(target, agent.position, agent.move)
    if ((moves > 0) && (moves <= agent.move))
    for {
      n <- pathFinder.path(target, agent.position)
    } screen(n) = pathChar
  }

  def renderValidMove(screen: Screen, agent: Agent): Unit = {
    val move = agent.move
    val p0 = agent.position

    for {
      x <- -move to move
      y <- -move to move
      p = p0 + (x, y)
      moves = pathFinder.moves(p, p0, move)
      if ((moves > 0) && (moves <= move))
    } screen(p) = validMoveChar(moves)
  }
}


