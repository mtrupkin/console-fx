package me.mtrupkin.game.model

import java.io._
import java.nio.file.{StandardOpenOption, Path, Paths, Files}

import me.mtrupkin.console.Screen
import me.mtrupkin.core.{Point, Size}
import play.api.libs.json._
import rexpaint.RexPaintImage


/**
 * Created by mtrupkin on 12/19/2014.
 */
class World (
  val agents: Seq[Agent],
  val player: Agent,
  var tileMap: TileMap,
  var time: Long = 0)  {

  def update(elapsed: Int) {
    time += elapsed
  }

  def render(screen: Screen): Unit = {
    tileMap.render(screen)

    renderAgent(screen, player)
  }

  def renderAgent(screen: Screen, agent: Agent): Unit = screen.write(agent.position, agent.sc)
}

case class AgentJS(name: String, position: Point, hp: Int)
case class WorldJS(levelName: String, agents: Seq[AgentJS], player: AgentJS, time: Long)

object World {
  val saveDirectory = Paths.get("./save")
  val savePath = saveDirectory.resolve("game.json")

  import scala.collection.JavaConversions._
  implicit val formatAgent = Json.format[AgentJS]
  implicit val formatWorld = Json.format[WorldJS]

  def read(): World = {
    val is = Files.newInputStream(savePath)
    val json = Json.parse(is)
    val worldJS = Json.fromJson[WorldJS](json).get
    val tileMap = TileMap.load(worldJS.levelName)
    new World(worldJS.agents.map(toAgent(_)), toAgent(worldJS.player), tileMap, worldJS.time)
  }

  def write(world: World): Unit = {
    val worldJS = WorldJS(world.tileMap.levelName, world.agents.map(toAgentJS(_)), toAgentJS(world.player), world.time)
    val json = Json.toJson(worldJS)

    Files.write(savePath, Seq(Json.prettyPrint(json)), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
  }

  def exists(): Boolean = Files.exists(savePath)

  def delete(): Unit = Files.delete(savePath)

  protected def toAgentJS(agent: Agent): AgentJS = {
    AgentJS(agent.name, agent.position, agent.hp)
  }

  protected def toAgent(agentJS: AgentJS): Agent = {
    agentJS.name match {
      case "Turret" => new Agent (agentJS.name, 'T', agentJS.position, currentHP = Some(agentJS.hp) ) {
        def act (tracker: CombatTracker): Unit = tracker.attack (this, tracker.world.player) //Combat.attack(ranged, world.player)
      }
      case _ =>  new Agent(agentJS.name, '@', agentJS.position, Stats(str = 1), Some(agentJS.hp)) {
        def act(tracker: CombatTracker): Unit = ???
      }
    }
  }
}
