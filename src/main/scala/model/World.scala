package me.mtrupkin.game.model

import me.mtrupkin.console.{Point, Screen}
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

  def act(direction: Point): Boolean = {
    val action = tryAct(direction)
    if (action) {
      encounter.nextTurn()
    }

    action
  }
  def tryAct(direction: Point): Boolean = {
    val p = player.position + direction
    for (a <- encounter.activeAgents.find(a => a.position == p) ) {
      attack(a)
      return true
    }

    if (tileMap.move(p.x, p.y)) {
      player.move(direction)
      true
    } else false
  }

  def attack(a: Agent): Unit = {
    Combat.attack(player.melee, a)
  }
}
