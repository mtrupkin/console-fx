package me.mtrupkin.controller


import java.io.{FileWriter, Writer}
import java.nio.file.{StandardOpenOption, OpenOption, Paths, Files}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button

import me.mtrupkin.game.model.TileMap._
import me.mtrupkin.game.model._
import rexpaint.RexPaintImage
import me.mtrupkin.core.{Point, Size}

/**
 * Created by mtrupkin on 12/15/2014.
 */


trait Intro { self: Controller =>

  class IntroController extends ControllerState {
    val name = "Intro"

    @FXML var continueGameButton: Button = _

    def initialize(): Unit = {
      continueGameButton.setDisable(!World.exists())
    }

    def handleContinueGame(event: ActionEvent) = {
      val world = World.read()

      changeState(new GameController(new CombatTracker(world)))
    }

    def handleNewGame(event: ActionEvent) = {
      val levelName = "layers-1"

      // create world

      val is = getClass.getResourceAsStream(s"/levels/$levelName.xp")
      val image = RexPaintImage.read(levelName, is)
      val tileMap = TileMap.load(levelName, image.size, image.layers.head.matrix)

      val (player, agents) = Agent.toAgents(image.layers(1).matrix)
      val world = new World(agents, player, tileMap)

      World.write(world)


      changeState(new GameController(new CombatTracker(world)))
    }
    def handleOptions(event: ActionEvent) = println("handled")
    def handleExit(event: ActionEvent) = stage.close()
  }

}
