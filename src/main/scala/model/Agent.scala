package  me.mtrupkin.game.model

import java.lang.Math._

import me.mtrupkin.console.{Point, ScreenChar}

/**
 * Created by mtrupkin on 12/19/2014.
 */
abstract class Agent(val name: String,
  val sc: ScreenChar,
  var position: Point,
  val stats: Stats = new Stats,
  var currentHP: Option[Int] = None) extends Entity {
  var hp: Int = currentHP.getOrElse(maxHP)
  def takeDamage(amount: Int) = hp -= amount

  def move(direction: Point) = { position = position + direction }

  import stats._
  def maxHP = (str + dex + int) * 10

  def act(world: World): Unit

  def melee: Combat = Combat((str + floor(dex/2) + floor(int/3)).toInt)
  def ranged: Combat = Combat((dex + floor(str/2) + floor(int/3)).toInt)

  def defense: Int = floor((str + dex + int) / 3).toInt
}
