package sample

import javafx.fxml.FXML
import javafx.scene.layout.Pane

import me.mtrupkin.console._
import me.mtrupkin.control.ConsoleFx
import me.mtrupkin.controller.Controller
import me.mtrupkin.core.{Point, Size}

import scalafx.Includes._
import scalafx.scene.{control => sfxc, input => sfxi, layout => sfxl}

/**
 * Created by mtrupkin on 12/15/2014.
 */
trait ControlTest { self: TestController =>
  class ControlController extends ControllerState {
    val name = "ControlTest"

    @FXML var pane: Pane = _

    var console: ConsoleFx = _
    var screen: Screen = _

    def initialize(): Unit = {
      val consoleSize = Size(20, 20)
      console = new ConsoleFx(consoleSize)
      console.setStyle("-fx-border-color: white")
      new sfxl.Pane(console) {
        onMouseClicked = (e: sfxi.MouseEvent) => handleMouseClicked(e)
        onMouseMoved = (e: sfxi.MouseEvent) => handleMouseMove(e)
        onMouseExited = (e: sfxi.MouseEvent) => handleMouseExit(e)
      }

      screen = Screen(consoleSize)
      pane.getChildren.clear()
      pane.getChildren.add(console)
      pane.setFocusTraversable(true)
      new sfxl.Pane(pane) {
        onKeyPressed = (e: sfxi.KeyEvent) => handleKeyPressed(e)
      }

      timer.start()
    }

    implicit def pointToString(p: Point): String = {
      s"[${p.x}, ${p.y}]"
    }

    def handleMouseClicked(mouseEvent: sfxi.MouseEvent): Unit = {
      for( p <- mouseToPoint(mouseEvent)) {
      }
    }

    def handleMouseMove(mouseEvent: sfxi.MouseEvent): Unit = {
      for( p <- mouseToPoint(mouseEvent)) {
      }
    }

    def handleMouseExit(mouseEvent: sfxi.MouseEvent): Unit = {
    }

    def mouseToPoint(mouseEvent: sfxi.MouseEvent): Option[Point] = console.toScreen(mouseEvent.x, mouseEvent.y)

    def handleKeyPressed(event: sfxi.KeyEvent): Unit = {
      import me.mtrupkin.console.Key._
      val key = keyCodeToConsoleKey(event)
      key match {
        case ConsoleKey(Q, Modifiers.Control) => {
          changeState(new IntroController)
        }
        case ConsoleKey(X, Modifiers.Control) => {
          //closed = true
        }
        case ConsoleKey(k, _) => k match {
//          case W | Up => world.act(Points.Up)
//          case A | Left => world.act(Points.Left)
//          case S | Down => world.act(Points.Down)
//          case D | Right => world.act(Points.Right)
          case Enter => ???
          //case Escape => flipState(new EscapeMenuController(world))
          case _ =>
        }
        case _ =>
      }
    }
  }

  def keyCodeToConsoleKey(event: sfxi.KeyEvent): ConsoleKey = {
    val modifier = Modifier(event.shiftDown, event.controlDown, event.altDown)
    val jfxName = event.code.name
    val key = Key.withName(jfxName)
    ConsoleKey(key, modifier)
  }
}

