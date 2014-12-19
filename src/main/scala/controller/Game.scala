package me.mtrupkin.controller

import javafx.beans.binding.{Bindings, StringBinding}
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.{StackPane, VBox, HBox, BorderPane}

import consolefx.ConsoleFx
import me.mtrupkin.console.{Size, Input, Screen}
import me.mtrupkin.game.{ConsoleController, TileMap}
import rexpaint.RexPaintImage

import scalafx.animation.AnimationTimer

/**
 * Created by mtrupkin on 12/15/2014.
 */
trait Game { self: Controller =>
  class GameController extends ControllerState {
    val name = "Game"

    @FXML var str: Label = _
    @FXML var dex: Label = _
    @FXML var int: Label = _
    @FXML var stack: StackPane = _

    var tileMap: TileMap = _
    var console: ConsoleFx = _
    var screen: Screen = _

    def initialize(): Unit = {
      println("init")

      val levelName = "layers-1"
      val is = getClass.getResourceAsStream(s"/levels/$levelName.xp")
      val image = RexPaintImage.read(levelName, is)

      tileMap = TileMap.load(image.size, image.layers.head.matrix)
      console = new ConsoleFx(image.size)
      screen = Screen(image.size)
      stack.getChildren.add(console)

      timer.start()
    }

    def update(elapsed: Int): Unit = {
//      dex.setText(elapsed.toString)

      tileMap.render(screen)
      console.draw(screen)
    }


    def root2: Parent = {
      val levelName = "layers-1"
      val is = getClass.getResourceAsStream(s"/levels/$levelName.xp")
      val image = RexPaintImage.read(levelName, is)

      val tileMap = TileMap.load(image.size, image.layers.head.matrix)
      val console = new ConsoleFx(image.size)
      val screen = Screen(image.size)

      tileMap.render(screen)
      console.draw(screen)

      val border = new BorderPane()
      border.setLeft(console)
      val masterPane = new HBox()
      val l = new Label("Hello")
      masterPane.getChildren.addAll(l)
      border.setCenter(masterPane)

      val statusPane = new VBox()
      val l2 = new Label("Hello")
      statusPane.getChildren.addAll(l2)
      border.setBottom(statusPane)

      border
    }
  }
}
