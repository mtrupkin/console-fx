package me.mtrupkin.pathfinding

import me.mtrupkin.core.Point
import me.mtrupkin.game.model.TileMap

import scala.Array._
import scala.collection.mutable

/**
 * Created by mtrupkin on 1/2/2015.
 */
class Dijkstra(val tileMap: TileMap) {
  case class Node(p: Point, weight: Int = 1, dist: Double = Double.MaxValue) extends Ordered[Node] {
    override def compare(o: Node): Int = (o.dist-dist).toInt
    override def toString: String = s"$p dist: $dist"
  }

  val size = tileMap.size
  val nodes = ofDim[Node](size.width, size.height)
  def nodes(p: Point): Node = nodes(p.x)(p.y)
  size.foreach((x, y) => nodes(x)(y) = new Node(Point(x, y)))

  // dijkstra's algorithm using a binary heap.
  def search(p: Point, r: Int): Array[Array[Double]] = {
    var q = new mutable.PriorityQueue[Node]()
    size.foreach((x, y) => nodes(x)(y) = nodes(x)(y).copy(dist = Double.MaxValue))

    q += nodes(p).copy(dist = 0)

    while (!q.isEmpty) {
      val u = q.dequeue() // vertex with shortest distance (first iteration will return source)

      // look at distances to each neighbour
      for (v <- legalNeighbours(u.p).map(nodes(_))) {
        val newDist = u.dist + v.weight
        if ((newDist < v.dist) && (newDist <= r)) {
          // shorter path to neighbour found
          val newNode = v.copy(dist = newDist)
          nodes(v.p.x)(v.p.y) = newNode
          q += newNode
        }
      }
    }

    val d = 2*r+1
    val m = ofDim[Double](d, d)

    for {
      n <- neighbours(r)
      p0 = n + p
      if size.inBounds(p0)
    } {
      m(n.x+r)(n.y+r) = nodes(p0).dist
    }

    m
  }

  def neighbours(r: Int = 1): Seq[Point] = {
    for {
      x <- -r to r
      y <- -r to r
      if !((x == 0) && (y == 0))
    } yield Point(x, y)
  }

  def legalNeighbours(p: Point, r: Int = 1): Seq[Point] = {
    neighbours(r).map(_ + p).filter(size.inBounds(_)).filter(tileMap.move(_))
  }
}
