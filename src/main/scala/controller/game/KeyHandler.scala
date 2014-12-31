package me.mtrupkin.controller.game

import javafx.event.EventHandler
import javafx.scene.input.KeyEvent

import me.mtrupkin.console._

/**
 * Created by mtrupkin on 12/30/2014.
 */
trait KeyHandler extends EventHandler[KeyEvent] {
  def keyCodeToConsoleKey(event: KeyEvent): ConsoleKey = {
    val modifier = Modifier(event.isShiftDown, event.isControlDown, event.isAltDown)
    val jfxName = event.getCode.getName
    val key = Key.withName(jfxName)
    ConsoleKey(key, modifier)
  }
}
