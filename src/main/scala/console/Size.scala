package me.mtrupkin.console

/**
 * Created by mtrupkin on 12/14/2014.
 */
case class Size(width: Int, height: Int) {
  def add(s: Size): Size = Size(this.width + s.width, this.height + s.height)
  def subtract(s: Size): Size = Size(this.width - s.width, this.height - s.height)

  def inBounds(p: Point): Boolean = (p.x >= 0 && p.y >= 0 && p.x < width && p.y < height)

  def foreach(f: (Int, Int) => Unit): Unit = for ( x <- 0 until width; y <- 0 until height ) f(x, y)
}

object Size {
  implicit def DoubleTupleToSize(t: (Double, Double)): Size = Size(t._1.toInt, t._2.toInt)
  implicit def TupleToSize(t: (Int, Int)): Size = Size(t._1, t._2)
  implicit def SizeToTuple(s: Size): (Int, Int) = (s.width, s.height)
}

object Sizes {
  val ZERO = new Size(0, 0)
  val ONE = new Size(1, 1)
}
