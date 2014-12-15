package me.mtrupkin.console

import play.api.libs.json.Json

/**
 * User: mtrupkin
 * Date: 7/5/13
 */

case class Point(x: Int, y: Int) {
  def +(p: Point): Point = Point(x + p.x, y + p.y)
  def -(p: Point): Point = Point(x - p.x, y - p.y)
}

object Point {
  implicit def TupleToPoint(t: (Int, Int)): Point = Point(t._1, t._2)
  implicit def PointToTuple(p: Point): (Int, Int) = (p.x, p.y)

  implicit val format = Json.format[Point]
}

object Points {
  val Origin: Point = Point(0, 0)
  val Up: Point = Point(0, -1)
  val Down: Point = Point(0, 1)
  val Left: Point = Point(-1, 0)
  val Right: Point = Point(1, 0)
}
