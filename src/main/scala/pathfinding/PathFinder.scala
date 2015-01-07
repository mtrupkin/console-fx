package me.mtrupkin.pathfinding

import me.mtrupkin.core.Point

/**
 * Created by mtrupkin on 1/7/2015.
 */
trait PathFinder {
  // number of moves from p0 to p
  def moves(p: Point, p0: Point, r: Int): Int

  // shortest path from p0 to p
  def path(p: Point, p0: Point): List[Point]
}
