package me.mtrupkin.game.model

import me.mtrupkin.console.{Point, ScreenChar}


/**
 * Created by mtrupkin on 12/19/2014.
 */
class Encounter(val world: World, val agents: Seq[Agent]) {
  import world.player

  var round: Int = 0

  def nextTurn(): Unit = {
    for(agent <- activeAgents) {
      agent.act(world)
    }
    round += 1
  }

  def activeAgents: Seq[Agent] = agents.filter(a => a.hp >= 0)

}

object Encounter {
  def toAgent(sc: ScreenChar, p: Point): Option[Agent] = {
    sc.c match {
      case 'T' => Some(new Agent("turret", 'T', p) {
        def act(world: World): Unit = Combat.attack(ranged, world.player)
      })
      case _ => None
    }
  }

  def toAgents(matrix: Seq[Seq[ScreenChar]]): Seq[Agent] = {
    val agents = for {
      (i, x) <- matrix.zipWithIndex
      (t, y) <- i.zipWithIndex
    } yield toAgent(t, Point(x, y))

    agents.flatten
  }

}

