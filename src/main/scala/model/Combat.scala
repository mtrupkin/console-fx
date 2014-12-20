package me.mtrupkin.game.model

import me.mtrupkin.console.Die

/**
 * Created by mtrupkin on 12/19/2014.
 */
trait Combat {
  def attack: Int
  def damage: Int
}

object Combat {
  def apply(f: => Int): Combat = new Combat {
    def attack: Int = f
    def damage: Int = f
  }

  private def attack(attacker: Combat, defense: Int): Int = {
    val attack = attacker.attack + Die()

    val effect = attack - (defense + Die())
    val damage = Math.max(attacker.damage, effect)

    println(s"(damage, base-damage, effect): ($damage, ${attacker.damage}, $effect)")

    damage
  }

  def attackRanged(attacker: Agent, defender: Entity): Unit = {
    val damage = attack(attacker.ranged, defender.defense)
    defender.takeDamage(damage)
  }
}
