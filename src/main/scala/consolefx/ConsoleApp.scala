package consolefx

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.stage.Stage
import me.mtrupkin.console.{Size, Screen, Input}
import me.mtrupkin.controller.Controller
import me.mtrupkin.game.{ConsoleController}


class ConsoleApp extends Application {
  override def start(primaryStage: Stage) {
    primaryStage.setTitle("Danger Room")
    val img16 = new Image("icons/icon-16.png")
    val img32 = new Image("icons/icon-32.png")
    val img64 = new Image("icons/icon-64.png")
    primaryStage.getIcons.addAll(img16, img32, img64)

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