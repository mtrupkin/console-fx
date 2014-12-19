package me.mtrupkin.controller

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label




/**
 * Created by mtrupkin on 12/15/2014.
 */


trait Intro { self: Controller =>

  class IntroController extends ControllerState {
    val name = "Intro"

    def update(elapsed: Int): Unit = ???

    def handleNewGame(event: ActionEvent) = changeState(new GameController)
    def handleLoadGame(event: ActionEvent) = {
      println("handled")
    }

    def handleOptions(event: ActionEvent) = println("handled")
    def handleExit(event: ActionEvent) = println("handled")
  }

}
