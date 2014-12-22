package me.mtrupkin.controller


import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.input.KeyEvent
import javafx.scene.layout._

import consolefx.ConsoleFx
import me.mtrupkin.console._
import me.mtrupkin.game.model.World
import org.apache.commons.lang3.time.StopWatch

/**
 * Created by mtrupkin on 12/15/2014.
 */
trait Game { self: Controller =>
  class GameController(val world: World) extends ControllerState {
    val name = "Game"

    @FXML var strText: Label = _
    @FXML var dexText: Label = _
    @FXML var intText: Label = _
    @FXML var hpText: Label = _
    @FXML var pane: Pane = _

    var console: ConsoleFx = _
    var screen: Screen = _

    def initialize(): Unit = {
      console = new ConsoleFx(world.tileMap.size)
      console.setStyle("-fx-border-color: white")

      screen = Screen(world.tileMap.size)
      pane.getChildren.clear()
      pane.getChildren.add(console)
      pane.setFocusTraversable(true)

      pane.setOnKeyReleased(new EventHandler[KeyEvent] {
        override def handle(event: KeyEvent): Unit = {
          import Key._
          val key = keyCodeToConsoleKey(event)
          key match {
            case ConsoleKey(Q, Modifiers.Control) => {
              changeState(new IntroController)
            }
            case ConsoleKey(X, Modifiers.Control) => {
              //closed = true
            }
            case ConsoleKey(k, _) => k match {
              case W | Up => world.act(Points.Up)
              case A | Left => world.act(Points.Left)
              case S | Down => world.act(Points.Down)
              case D | Right => world.act(Points.Right)
              case Enter => ???
              //case Escape => flipState(new EscapeMenuController(world))
              case _ =>
            }
            case _ =>
          }
        }
      })

      timer.start()
    }

    def keyCodeToConsoleKey(event: KeyEvent): ConsoleKey = {
      val modifier = Modifier(event.isShiftDown, event.isControlDown, event.isAltDown)
      val jfxName = event.getCode.getName
      val key = Key.withName(jfxName)
      ConsoleKey(key, modifier)
    }

    def update(elapsed: Int): Unit = {
      implicit def toString(int: Int): String = int.toString
      import world.player._

      strText.setText(stats.str)
      dexText.setText(stats.dex)
      intText.setText(stats.int)
      hpText.setText(hp)

      world.render(screen)
      console.draw(screen)
    }
  }
}
