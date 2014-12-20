package me.mtrupkin.game.model

import me.mtrupkin.console.Screen
import me.mtrupkin.game.TileMap


/**
 * Created by mtrupkin on 12/19/2014.
 */
class World (
  val player: Agent,
  var tileMap: TileMap,
  var time: Long = 0) {

  var encounter: Encounter = _
  def update(elapsed: Int) {
    time += elapsed
  }


  def renderAgent(screen: Screen, agent: Agent): Unit ={
    val p = agent.position
    screen.write(p.x, p.y, agent.sc)
  }

  def render(screen: Screen): Unit ={
    tileMap.render(screen)

    renderAgent(screen, player)
    for (a <- encounter.activeAgents) {
      renderAgent(screen, a)
    }

  }
}
