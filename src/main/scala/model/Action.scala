package me.mtrupkin.game.model

import me.mtrupkin.core.Point
import me.mtrupkin.pathfinding.{Dijkstra}

/**
 * Created by mtrupkin on 1/17/2015.
 */
trait Action {
  def name: String
  def canAct(target: Point): Boolean
}

class MoveAction(val tracker: CombatTracker, val name: String = "Move") extends Action {
  import tracker._

  def canAct(target: Point): Boolean = {
    val move = player.move
    val moves = tracker.pathFinder.moveCount(target, player.position, move)
    val path = tracker.pathFinder.path(target, player.position)
    if ((moves > 0) && (moves <= move) && (path != Nil) && agents.forall(_.position != target))
      true
    else false
  }
}

class AttackAction(val tracker: CombatTracker, val name: String = "Attack") extends Action {
  import tracker._

  def act(target: Point): Unit =  {
    for {
      a <- agents.find(a => a.position == target)
    } Combat.attack(player.melee, a, Combat.playerAttack)
  }

  def canAct(target: Point): Boolean = {
    for {
      a <- agents.find(a => a.position == target)
    } {
      val lineOfSight = tracker.lineOfSight(a.position, player.position)
      return (lineOfSight != Nil)
    }
    false
  }
}