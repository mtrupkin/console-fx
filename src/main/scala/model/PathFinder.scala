package me.mtrupkin.game.model

import me.mtrupkin.core.Point
import org.newdawn.slick.util.pathfinding._

/**
 * Created by mtrupkin on 12/31/2014.
 */

class PathFinder(agent: Agent, map: TileMap, depth: Int) {
  protected val finder = new AStarPathFinder(new TileMapWrapper(map), depth, true)
  protected val mover = MoverWrapper(agent)

  def findPath(p: Point): Seq[Point] = {
    val p0 = agent.position
    val stepsOpt: Option[Path] = Option(finder.findPath(mover, p0.x, p0.y, p.x, p.y))

    val path = for {
      steps <- stepsOpt.toSeq
      i <- 0 until steps.getLength
    } yield Point(steps.getX(i), steps.getY(i))

    path.toSeq
  }
}

case class MoverWrapper(agent: Agent) extends Mover

class TileMapWrapper(val map: TileMap) extends TileBasedMap {
  def blocked(ctx: PathFindingContext, x: Int, y: Int): Boolean = {
    !map.apply(x, y).move
  }

  def getCost(ctx: PathFindingContext, x: Int, y: Int): Float = {
    val (dx, dy) = (x - ctx.getSourceX, y - ctx.getSourceY)
    dx*dx + dy*dy
  }

  def getHeightInTiles(): Int = {
    map.size.height
  }

  def getWidthInTiles(): Int = {
    map.size.width
  }

  def pathFinderVisited(x: Int, y: Int) {}
}