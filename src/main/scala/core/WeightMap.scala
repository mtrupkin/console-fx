package core

import me.mtrupkin.core.{Point, Size}

import scala.Array._

/**
 * Created by mtrupkin on 12/31/2014.
 */
class WeightMap(val radius: Int) {
  val diameter = radius * 2 + 1
  val weights = ofDim[Double](diameter, diameter)

  def apply(p: Point): Double = weights(p.x + radius)(p.y + radius)
}

