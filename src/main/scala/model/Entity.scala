package me.mtrupkin.game.model

import me.mtrupkin.console.ScreenChar
import me.mtrupkin.core.Point
/**
 * Created by mtrupkin on 12/19/2014.
 */
trait Entity {
  def name: String
  def sc: ScreenChar
  def position: Point
  def hp: Int
  def maxHP: Int
  def defense: Int

  def takeDamage(amount: Int)
}

case class Stats(str: Int = 0, dex: Int = 0, int: Int = 0)

