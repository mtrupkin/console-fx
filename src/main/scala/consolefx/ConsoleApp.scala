package consolefx

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.stage.Stage
import me.mtrupkin.console.{Size, Screen, Input}
import me.mtrupkin.controller.Controller
import me.mtrupkin.game.{ConsoleController, GameEngine}


class ConsoleApp extends Application {
  override def start(primaryStage: Stage) {
    primaryStage.setTitle("Console FX")

    object Controller extends Controller {
      lazy val css = "/views/Console.css"
      lazy val initialState: ControllerState = new IntroController
      lazy val stage = primaryStage
    }

    Controller.stage.show()


  }
}

object ConsoleApp extends App {
	Application.launch(classOf[ConsoleApp], args: _*)
}