package me.mtrupkin.controller

import javafx.beans.binding.{Bindings, StringBinding}
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.{VBox, HBox, BorderPane}

import consolefx.ConsoleFx
import me.mtrupkin.console.Screen
import me.mtrupkin.game.TileMap
import rexpaint.RexPaintImage

/**
 * Created by mtrupkin on 12/15/2014.
 */
trait Game { self: Controller =>
  class GameController extends ControllerState {
    val name = "Game"

    def root2: Parent = {
      val levelName = "layers-1"
      val is = getClass.getResourceAsStream(s"/levels/$levelName.xp")
      val image = RexPaintImage.read(levelName, is)

      val tileMap = TileMap.load(image.size, image.layers.head.matrix)
      val console = new ConsoleFx(image.size)

      val screen = Screen(image.size)
      tileMap.render(screen)
      console.draw(screen)

      val pixelSize = console.pixelSize
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

    def update(elapsed: Int): Unit = ???
  }
}
