package me.mtrupkin.controller

import javafx.animation._
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.layout.StackPane
import javafx.scene.{Node, Parent, Scene}
import javafx.stage.Stage
import javafx.util.Duration
import me.mtrupkin.game.StateMachine

/**
 * Created by mtrupkin on 12/15/2014.
 */

trait Controller extends StateMachine
  with Intro
  with Game {
  type StateType = ControllerState

  val stage: Stage
  val stackPane: StackPane = new StackPane
  stackPane.getChildren.add(initialState.root)
  val scene = new Scene(stackPane)
  stage.setScene(scene)


  trait ControllerState extends State {
    def root: Node

    override def onEnter(): Unit = {
      // method A
      //val scene = new Scene(root)
      //stage.setScene(scene)

      // method B
      //scene.setRoot(root)

      // method C
      val previous = stackPane.getChildren.get(0)
      val next = root
      stackPane.getChildren.add(next)
      fade(previous, next)
    }

    def fade(previous: Node, next: Node): Unit = {
      val fadePrevious = new FadeTransition(Duration.millis(3000), previous)
      fadePrevious.setFromValue(1.0)
      fadePrevious.setToValue(0.1)
      fadePrevious.setCycleCount(Animation.INDEFINITE)
      fadePrevious.setAutoReverse(true)
      fadePrevious.play()

      val fadeNext = new FadeTransition(Duration.millis(3000), next)
      fadeNext.setFromValue(0.1)
      fadeNext.setToValue(1.0)
      fadeNext.setCycleCount(Animation.INDEFINITE)
      fadeNext.setAutoReverse(true)
      fadeNext.play()

    }
  }
}
