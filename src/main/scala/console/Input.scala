package me.mtrupkin.console

import me.mtrupkin.console.Key._


/**
 * Created by mtrupkin on 12/14/2014.
 */
case class Modifier(shift: Boolean = false, control: Boolean = false, alt: Boolean = false)

case class ConsoleKey(key: Key, modifier: Modifier)

object Modifiers {
  val Shift = Modifier(shift = true)
  val Control = Modifier(control = true)
  val Alt = Modifier(alt = true)
}

case class Input(
    key: Option[ConsoleKey] = None,
    mouseMove: Option[Point] = None,
    mouseClick: Option[Point] = None,
    mouseExit: Option[Point] = None)
