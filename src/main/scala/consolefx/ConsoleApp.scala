package consolefx

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.scene.control.Label

class ConsoleApp extends Application {
  override def start(primaryStage: Stage) {
    primaryStage.setTitle("Console FX")

    val root = new StackPane
    root.getChildren.add(new Label("Hello world!"))

    primaryStage.setScene(new Scene(root, 300, 300))
    primaryStage.show()
  }
}

object ConsoleApp extends App {
	Application.launch(classOf[ConsoleApp], args: _*)
}