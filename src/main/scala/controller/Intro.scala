package me.mtrupkin.controller

import javafx.event.ActionEvent

import me.mtrupkin.game.model.{Stats, Agent, World}
import me.mtrupkin.game.model.TileMap
import rexpaint.RexPaintImage
import me.mtrupkin.core.{Point, Size}

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

      // create world
      val player = new Agent("Player", '@', Point(5, 5), Stats(str = 1)) {
        def act(world: World): Unit = ???
      }
      val agents = Agent.toAgents(image.layers(1).matrix)
      val world = new World(agents, player, tileMap)

//      world.encounter = new CombatTracker(world)

      changeState(new GameController(world))
    }
    def handleLoadGame(event: ActionEvent) = println("handled")
    def handleOptions(event: ActionEvent) = println("handled")
    def handleExit(event: ActionEvent) = stage.close()
  }

}
