package me.mtrupkin.controller.game

import me.mtrupkin.game.model.Agent
import scalafx.beans.property.StringProperty

/**
 * Created by mtrupkin on 12/30/2014.
 */
class AgentBean(agent: Agent) {
  val name = new StringProperty(this, "name", agent.name)
  val hp = new StringProperty(this, "hp", agent.hp.toString)
}
