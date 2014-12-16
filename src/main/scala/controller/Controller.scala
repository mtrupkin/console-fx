package me.mtrupkin.controller

import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import me.mtrupkin.game.StateMachine

/**
 * Created by mtrupkin on 12/15/2014.
 */

trait Controller extends StateMachine
  with Intro
  with Game {
  type StateType = ControllerState

  val stage: Stage
  val scene = new Scene(initialState.root)
  stage.setScene(scene)


  trait ControllerState extends State {
    def root: Parent

    override def onEnter(): Unit = {
      val scene = new Scene(root)
      stage.setScene(scene)
      //scene.setRoot(root)
    }
  }
}
