package me.mtrupkin.game.app

import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage

import me.mtrupkin.controller.Controller
import sample.TestController

import scala.collection.JavaConversions._

abstract class ConsoleAppBase extends Application {
  def title: String
  def controller(primaryStage: Stage): Controller

  override def start(primaryStage: Stage) {
    primaryStage.setTitle(title)

    val rs = List("icon-16", "icon-32", "icon-64")
    val icons = for(s <- rs) yield new Image(s"/icons/$s.png")
    primaryStage.getIcons.addAll(icons)

    controller(primaryStage).stage.show()
  }
}

class ConsoleApp extends ConsoleAppBase {
  val title = "Console App"

  def controller(primaryStage: Stage) = new TestController {
    lazy val initialState: ControllerState = new IntroController
    lazy val stage = primaryStage
  }

}

object ConsoleApp extends App {
	Application.launch(classOf[ConsoleApp], args: _*)
}