package consolefx

import javafx.application.Application
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.input.{MouseEvent, KeyEvent}
import javafx.scene.layout._
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.{FontWeight, Text, Font}
import javafx.stage.Stage

import javafx.scene.control.Label
import javax.swing.BorderFactory
import scala.collection.JavaConversions._

class ConsoleApp extends Application {
  override def start(primaryStage: Stage) {
    primaryStage.setTitle("Console FX")

    val root = new GridPane
    root.setStyle("-fx-background-color: black;")

    val b3 = new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null, null))
    val b2 = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, null, null))
    val b = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null))
    val DOUBLE_HLINE: Char = 0x2550
    val DOUBLE_VLINE: Char = 0x2551
    val HLINE: Char = 0x2500
    val VLINE: Char = 0x2502
    val SINGLE_LINE_CROSS: Char = 0x253C
    val DOUBLE_LINE_CROSS: Char = 0x256C

    val f = Font.font("Consolas", FontWeight.NORMAL, 20)

    //for( c <- "ab" + DOUBLE_HLINE + DOUBLE_HLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE + DOUBLE_VLINE) {
    val metrics = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().getFontMetrics(f)
    val fontWidth = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth(HLINE.toString, f)
    val bounds = (Math.floor(fontWidth), Math.floor(metrics.getLineHeight))
    val terminalBounds = (50, 25)

    //println(bounds)
    for( x <- 0 until terminalBounds._1) {
      for (y <- 0 until terminalBounds._2 ) {
//        val l = new Text("@")
        //val l1 = new Text(DOUBLE_VLINE.toString)
        val stack = new StackPane()
        //stack.setBorder(b)


//        val c = x%2 match {
//          case 0 => DOUBLE_HLINE.toString
//          case 1 => DOUBLE_VLINE.toString
//          case 2 => " "
//        }
        val c = SINGLE_LINE_CROSS.toString

        val l = new Label(c)
        val r = new Rectangle(bounds._1-1, bounds._2-1)

        l.setTextFill(Color.WHITE)
        //l1.setStroke(Color.WHITE)
        //r.setStroke(Color.TRANSPARENT)
        r.setStroke(Color.BLUE)
        r.setFill(Color.TRANSPARENT)
        //r1.setStroke(Color.BLACK)
        //r1.setStroke(Color.TRANSPARENT)

        l.setFont(f)
        //l1.setTextFill(Color.WHITE)
        stack.getChildren.addAll(l, r)

        //l2.setFont(Font.font("Consolas", 20))
        //l2.setFont(Font.font("Monospaced", 20))

        //l.setBorder(b)
        //l2.setBorder(b)


        //root.add(stack, x, y)
        root.add(stack, x, y)
//        println(l1.getLayoutBounds)

      }
    }

    primaryStage.setScene(new Scene(root, terminalBounds._1 * bounds._1, terminalBounds._2 * bounds._2))
    primaryStage.show()


//    println(root.getBoundsInLocal)
//    println(root.getBoundsInParent)
//    root.getChildren.toList.foreach(n => println(n.getLayoutBounds))

    var borderOn = false
    root.setOnKeyTyped(new EventHandler[KeyEvent] {
      override def handle(event: KeyEvent): Unit = {
        borderOn = !borderOn
        root.setGridLinesVisible(borderOn)
      }
    })

    root.setOnMouseClicked(new EventHandler[MouseEvent] {
      override def handle(event: MouseEvent): Unit = {
        borderOn = !borderOn
        root.setGridLinesVisible(borderOn)
      }
    })

  }
}

object ConsoleApp extends App {
	Application.launch(classOf[ConsoleApp], args: _*)
}