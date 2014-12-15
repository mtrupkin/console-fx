package me.mtrupkin.console

import me.mtrupkin.console.Colors._
/**
 * Created by mtrupkin on 12/13/2014.
 */

class Screen(val size: Size) {
  val blank: ScreenChar = ScreenChar(' ')
  var fg: RGB = White
  var bg: RGB = Black

  val buffer = Array.ofDim[ScreenChar](size.width, size.height)
  clear()

  def apply(x: Int, y: Int): ScreenChar = buffer(x)(y)
  def update(x: Int, y: Int, sc: ScreenChar ): Unit = buffer(x)(y) = sc

  def clear() = foreach((x, y, s) => this(x, y) = blank)
  def clear(x: Int, y: Int) = this(x, y) = blank

  def foreach(f: (Int, Int, ScreenChar) => Unit ): Unit = {
    for (
      i <- 0 until size.width;
      j <- 0 until size.height
    ) f(i, j, this(i, j))
  }

  def transform(f: (ScreenChar) => ScreenChar ): Unit = {
    for (
      i <- 0 until size.width;
      j <- 0 until size.height
    ) this(i, j) = f(this(i, j))
  }

  def write(p: (Int, Int), c: Char): Unit = {
    write(p._1, p._2, c)
  }

  def write(x: Int, y: Int, c: Char): Unit = {
    this(x, y) = ScreenChar(c, fg, bg)
  }

  def write(x: Int, y: Int, sc: ScreenChar): Unit = {
    this(x, y) = sc
  }

  def write(s: String): Unit = write(0, 0, s)

  def write(x: Int, y: Int, s: String): Unit = {
    var pos = x
    s.foreach( c => { write(pos, y, c); pos += 1 } )
  }
}

object Screen {
  def apply(size: Size): Screen = new Screen(size)
}

class SubScreen(val origin: Point, val screen: Screen, size: Size) extends Screen(size) {
  override def update(x: Int, y: Int, sc: ScreenChar) = {
    screen.update(origin.x + x, origin.y + y, sc)
  }

  override def apply(x: Int, y: Int): ScreenChar = {
    screen(origin.x + x, origin.y + y)
  }
}
