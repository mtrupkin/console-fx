package me.mtrupkin.controller

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Label

import me.mtrupkin.console.Point
import me.mtrupkin.game.TileMap
import me.mtrupkin.game.model.{Encounter, Stats, Agent, World}
import rexpaint.RexPaintImage


/**
 * Created by mtrupkin on 12/15/2014.
 */


trait Intro { self: Controller =>

  class IntroController extends ControllerState {
    val name = "Intro"

    def update(elapsed: Int): Unit = ???

    def handleNewGame(event: ActionEvent) = {
      val levelName = "layers-1"
      val is = getClass.getResourceAsStream(s"/levels/$levelName.xp")
      val image = RexPaintImage.read(levelName, is)

      val tileMap = TileMap.load(image.size, image.layers.head.matrix)

      val player = new Agent("Player", '@', Point(5, 5), Stats(str = 1)) {
        def act(world: World): Unit = ???
      }
      val world = new World(player, tileMap)

      val agents = Encounter.toAgents(image.layers(1).matrix)

      world.encounter = new Encounter(world, agents)
      changeState(new GameController(world))
    }
    def handleLoadGame(event: ActionEvent) = {
      println("handled")
    }

    def handleOptions(event: ActionEvent) = println("handled")
    def handleExit(event: ActionEvent) = println("handled")
  }

}
