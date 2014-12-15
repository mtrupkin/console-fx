package me.mtrupkin.console

/**
 * Created by mtrupkin on 12/13/2014.
 */

trait Console {
  def size: Size

  def input(): Option[Input]
  def render(screen: Screen)
}

