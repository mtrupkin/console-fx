package me.mtrupkin.game.model

import me.mtrupkin.console.Screen
import me.mtrupkin.core.{Point, Size}

import scala.collection.mutable.ListBuffer


/**
 * Created by mtrupkin on 12/19/2014.
 */
class World (
  val agents0: Seq[Agent],
  val player: Agent,
  var tileMap: TileMap,
  var time: Long = 0) {

  def agents: Seq[Agent] = agents0.filter(_.hp >= 0)

  val tracker: CombatTracker = new CombatTracker(this)

  def update(elapsed: Int) {
    time += elapsed
  }

  def renderAgent(screen: Screen, agent: Agent): Unit ={
    val p = agent.position
    screen.write(p.x, p.y, agent.sc)
  }

  def render(screen: Screen): Unit ={
    tileMap.render(screen)
    tracker.render(screen)

    renderAgent(screen, player)
    for (a <- agents) {
      renderAgent(screen, a)
    }
  }

  def act(direction: Point): Boolean = {
    val action = tryAct(direction)
    if (action) {
      tracker.nextTurn()
    }

    action
  }

  def tryAct(direction: Point): Boolean = {
    val p = player.position + direction
    for (a <- agents.find(a => a.position == p) ) {
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
