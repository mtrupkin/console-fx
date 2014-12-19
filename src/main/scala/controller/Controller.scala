package me.mtrupkin.controller

import javafx.animation._
import javafx.event.{ActionEvent, EventHandler}
import javafx.fxml.FXMLLoader
import javafx.scene.layout.StackPane
import javafx.scene.{Node, Parent, Scene}
import javafx.stage.Stage
import javafx.util.Duration
import me.mtrupkin.game.StateMachine

import scalafx.animation.AnimationTimer

/**
 * Created by mtrupkin on 12/15/2014.
 */

trait Controller extends StateMachine
  with Intro
  with Game {
  type StateType = ControllerState

  def stage: Stage
  def css: String

//  val stackPane: StackPane = new StackPane
//  stackPane.getChildren.add(initialState.root)

  val scene = new Scene(initialState.root)
  val cssLocation = getClass.getResource(css).toString
  scene.getStylesheets.add(cssLocation)

  stage.setScene(scene)

  trait ControllerState extends State {
    def name: String
    def templateName: String = s"/views/$name.fxml"

    val timer = AnimationTimer.apply(handle)
    var lastPulse: Long = _
    def handle(now: Long): Unit = {
      update((now-lastPulse).toInt)
      lastPulse = now
    }

    def root: Parent = {
      val is = getClass.getResourceAsStream(templateName)
      val loader = new FXMLLoader()

      loader.setController(this)
      loader.load[Parent](is)
    }

    override def onEnter(): Unit = {
      // method A
//      val scene = new Scene(root)
//      stage.setScene(scene)

      // method B
      scene.setRoot(root)

      // method C
//      val previous = stackPane.getChildren.get(0)
//      val next = root
//      stackPane.getChildren.add(next)
//
//      fade(previous, next)
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
