package me.mtrupkin.game

import me.mtrupkin.console.{Screen, Input}

/**
 * Created by mtrupkin on 12/15/2014.
 */
trait ConsoleController {
  def handle(input: Input)
  def render: Screen
  def update(elapsed: Int)
}
