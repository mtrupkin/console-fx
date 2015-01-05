package core

import me.mtrupkin.core.Point
import me.mtrupkin.game.model.TileMap

import scala.Array._
import scala.collection.mutable

/**
 * Created by mtrupkin on 1/2/2015.
 */
class Dijkstra(val tileMap: TileMap) {

  class Node(val p: Point, val weight: Int = 1) extends Ordered[Node] {
    var minDist = Double.MaxValue

    def setMinDist(d: Double): Node = {
      minDist = d
      this
    }

    override def compare(o: Node): Int = (o.minDist-minDist).toInt

    override def toString: String = {
      s"$p minDist: $minDist "
    }
  }

  val size = tileMap.size
  val nodes = ofDim[Node](size.width, size.height)
  def nodes(p: Point): Node = nodes(p.x)(p.y)
  size.foreach((x, y) => nodes(x)(y) = new Node(Point(x, y)))

  // Implementation of dijkstra's algorithm using a binary heap.
  def dijkstra(p: Point, r: Int): Array[Array[Double]] = {
    var q = new mutable.PriorityQueue[Node]()
    size.foreach((x, y) => nodes(x)(y).minDist = Double.MaxValue)

    q += nodes(p).setMinDist(0)

    while (!q.isEmpty) {
      val u = q.dequeue() // vertex with shortest distance (first iteration will return source)

      //look at distances to each neighbour
      for (v <- neighbours(u.p).map(nodes(_))) {
        val newMinDist = u.minDist + v.weight
        if (newMinDist < v.minDist && newMinDist < r) { // shorter path to neighbour found
          q += v.setMinDist(newMinDist)
        }
      }
    }

    ???
  }

  def neighbours(p: Point, r: Int = 1): Seq[Point] = {
    for {
      x <- -r to r
      y <- -r to r
      if !((x == 0) && (y == 0))
    } yield p + (x, y)
  }
}
