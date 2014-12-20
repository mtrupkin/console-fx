package me.mtrupkin.console

import scala.util.Random

/**
 * Created by mtrupkin on 12/19/2014.
 */
object Die {
  // 3d6
  def apply(): Int = this(3, 6)

  // roll one x sided die
  def apply(sides: Int): Int = Random.nextInt(sides) + 1

  // roll one x sided die, n times
  def apply(times: Int, sides: Int): Int = {
    val rolls = for {
      i <- 0 until times
    } yield this(sides)

    rolls.sum
  }
}
