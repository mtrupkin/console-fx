package me.mtrupkin.controller

import javafx.event.EventHandler
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent

/**
 * Created by mtrupkin on 12/15/2014.
 */

trait Intro { self: Controller =>
  class IntroController extends ControllerState {
    def root: Parent = {
      val l = new Label("Hello")
        l.setOnMouseClicked(new EventHandler[MouseEvent] {
          override def handle(event: MouseEvent): Unit = changeState(new GameController)
        })
        l
    }

    def update(elapsed: Int): Unit = ???
  }
}
