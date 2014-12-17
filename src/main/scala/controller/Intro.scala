package me.mtrupkin.controller

import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.{Node, Parent}
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent

/**
 * Created by mtrupkin on 12/15/2014.
 */

trait Intro { self: Controller =>
  class IntroController extends ControllerState {
    def root: Node = {
      val location = getClass.getResource(s"/controller/intro.fxml")

      FXMLLoader.load[Parent](location)
    }

    def update(elapsed: Int): Unit = ???
  }
}
