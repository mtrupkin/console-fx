package consolefx

import javafx.scene.control.Label
import javafx.scene.layout.{StackPane, Pane}
import javafx.scene.paint.Color
import javafx.scene.text.{FontWeight, Font}


import me.mtrupkin.console.{Input, Screen, Size, Console, ScreenChar}

import scala.Array._
import Math._

/**
 * Created by mtrupkin on 12/13/2014.
 */
class ConsoleFx(val size: Size) extends Pane with Console {
  setStyle("-fx-background-color: black;")
  val font = Font.font("Consolas", FontWeight.NORMAL, 19)
  val charBounds = ConsoleFx.charBounds(font)
  val stacks = ofDim[StackPane](size.width, size.height)
  val labels = ofDim[Label](size.width, size.height)
  val (prefWidth, prefHeight) = toPixel(size)
  setPrefSize(prefWidth, prefHeight)
  setMinSize(prefWidth, prefHeight)

  size.foreach(init)

  def init(x: Int, y: Int): Unit = {
    val s = new StackPane()
    val l = new Label()


    l.setTextFill(Color.WHITE)
    l.setFont(font)

    s.getChildren.addAll(l)

    stacks(x)(y) = s
    labels(x)(y) = l

    val (px, py) = toPixel(x, y)
    s.relocate(px, py)
    getChildren.add(s)
  }

  def apply(x: Int, y: Int): Label = labels(x)(y)

  def input(): Option[Input] = None

  // draw screen to window
  def draw(screen: Screen): Unit = screen.foreach(draw)

  def draw(x: Int, y: Int, s: ScreenChar): Unit = {
    this(x, y).setText(s)
  }

  def toPixel(p: (Int, Int)): (Double, Double) = {
    val (x, y) = p
    val (width, height) = charBounds

    (x * width, y * height)
  }

  def pixelSize(): Size = toPixel(size)
}

object ConsoleFx {
  def charBounds(f: Font): (Double, Double) = {
    val metrics = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().getFontMetrics(f)
    val fontWidth = com.sun.javafx.tk.Toolkit.getToolkit().getFontLoader().computeStringWidth(" ", f)
    (floor(fontWidth), floor(metrics.getLineHeight))
  }
}
