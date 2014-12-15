package me.mtrupkin.game

import me.mtrupkin.console.{Screen, ScreenChar, Size}
import me.mtrupkin.console.Colors._
import scala.Array._

/**
 * Created by mtrupkin on 12/14/2014.
 */
trait Tile {
  def sc: ScreenChar
  def update(elapsed: Int)
  def move: Boolean
}

class TileMap(val size: Size) {
  val tiles = ofDim[Tile](size.width, size.height)

  def apply(x: Int, y: Int): Tile = tiles(x)(y)
  def foreach(f: (Int, Int, Tile) => Unit ) = size.foreach((x,y) => f(x, y, this(x, y)))

  def move(x: Int, y: Int): Boolean = size.inBounds((x,y)) && this(x, y).move
  def update(elapsed: Int): Unit = size.foreach((x, y) => this(x, y).update(elapsed))

  def render(screen: Screen): Unit = foreach((x, y, t) => screen(x, y) = t.sc)
}

class Floor extends Tile {
  val move = true
  var sc = ScreenChar(' ', fg = LightGrey)
  def update(elapsed: Int) = {}
}

class Wall(val sc: ScreenChar) extends Tile {
  val move = false
  def update(elapsed: Int) = {}
}

object Tile {
  implicit def toTile(s: ScreenChar): Tile = {
    s.c match {
      case ' ' | '.' => new Floor
      case _ => new Wall(s)
    }
  }
}

object TileMap {
  def load(size: Size, matrix: Seq[Seq[ScreenChar]]): TileMap = {
    val tileMap = new TileMap(size)
    for((i, x) <- matrix.zipWithIndex) {
      for((t, y) <- i.zipWithIndex) {
        tileMap.tiles(x)(y) = t
      }
    }

    tileMap
  }
}


