package consolefx

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

import me.mtrupkin.console.Screen

import me.mtrupkin.game.TileMap
import rexpaint.RexPaintImage


class ConsoleApp extends Application {
  override def start(primaryStage: Stage) {
    primaryStage.setTitle("Console FX")
    val levelName = "layers-1"
    val is = getClass.getResourceAsStream(s"/levels/$levelName.xp")
    val image = RexPaintImage.read(levelName, is)

    val tileMap = TileMap.load(image.size, image.layers.head.matrix)
    val console = new ConsoleFx(image.size)

    val screen = Screen(image.size)
    tileMap.render(screen)

    console.render(screen)

    val pixelSize = console.pixelSize

    primaryStage.setScene(new Scene(console, pixelSize.width, pixelSize.height))
    primaryStage.show()
  }
}

object ConsoleApp extends App {
	Application.launch(classOf[ConsoleApp], args: _*)
}