package me.mtrupkin.controller

import javafx.beans.binding.{Bindings, StringBinding}
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout._

import consolefx.ConsoleFx
import me.mtrupkin.console.{Size, Input, Screen}
import me.mtrupkin.game.model.World
import me.mtrupkin.game.{ConsoleController, TileMap}
import rexpaint.RexPaintImage

import scalafx.animation.AnimationTimer

/**
 * Created by mtrupkin on 12/15/2014.
 */
trait Game { self: Controller =>
  class GameController(val world: World) extends ControllerState {
    val name = "Game"

    @FXML var str: Label = _
    @FXML var dex: Label = _
    @FXML var int: Label = _
    @FXML var pane: Pane = _

    var console: ConsoleFx = _
    var screen: Screen = _

    def initialize(): Unit = {
      println("init")

      console = new ConsoleFx(world.tileMap.size)
      console.setStyle("-fx-border-color: white")
      screen = Screen(world.tileMap.size)
      pane.getChildren.clear()
      pane.getChildren.add(console)

      timer.start()
    }

    def update(elapsed: Int): Unit = {
      import world.player._
      str.setText(stats.str.toString)
      dex.setText(stats.dex.toString)
      int.setText(stats.int.toString)

      world.render(screen)
      console.draw(screen)
    }
  }
}
