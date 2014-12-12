package consolefx

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout._
import javafx.scene.paint.Color
import javafx.scene.text.{Text, Font}
import javafx.stage.Stage

import javafx.scene.control.Label
import javax.swing.BorderFactory
import scala.collection.JavaConversions._

class ConsoleApp extends Application {
  override def start(primaryStage: Stage) {
    primaryStage.setTitle("Console FX")

    val root = new FlowPane



    val b = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, null))
    val b2 = new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null, null))
    val DOUBLE_HLINE: Char = 0x2550
    val DOUBLE_VLINE: Char = 0x2551
    //for( c <- "ab" + DOUBLE_HLINE + DOUBLE_HLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE) {
    for( c <- 1 until 1000) {
      //
      // val l = new Label(c.toString)
      //val l = new Text(c.toString)
      val l2 = new Label(DOUBLE_VLINE.toString)

      //l.setFont(Font.font("Consolas", 20))
      l2.setFont(Font.font("Consolas", 20))
      //l2.setFont(Font.font("Monospaced", 20))

      //l.setBorder(b)
      //l2.setBorder(b2)


      //root.getChildren.add(l)
      root.getChildren.add(l2)
    }

    primaryStage.setScene(new Scene(root, 300, 300))
    primaryStage.show()


//    println(root.getBoundsInLocal)
//    println(root.getBoundsInParent)
    root.getChildren.toList.foreach(n => println(n.getBoundsInParent))

  }
}

object ConsoleApp extends App {
	Application.launch(classOf[ConsoleApp], args: _*)
}