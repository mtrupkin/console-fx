package  me.mtrupkin.game.model


import me.mtrupkin.console.{ScreenChar}
import me.mtrupkin.core.{Point, Size}

/**
 * Created by mtrupkin on 12/19/2014.
 */
abstract class Agent(val name: String,
  val sc: ScreenChar,
  var position: Point,
  val stats: Stats = new Stats,
  var currentHP: Option[Int] = None,
  var currentMove: Option[Int] = None) extends Entity {
  import stats._
  def floor(d: Double): Int = Math.floor(d).toInt

  var hp: Int = currentHP.getOrElse(maxHP)
  var move: Int = currentMove.getOrElse(maxMove)

  def takeDamage(amount: Int) = hp -= amount

  def move(direction: Point) = { position = position + direction }

  def maxMove: Int = 5 + floor(dex / 3)
  def maxHP = (str + dex + int) * 10

  def act(world: World): Unit

  def melee: Combat = Combat((str + floor(dex/2) + floor(int/3)))
  def ranged: Combat = Combat((dex + floor(str/2) + floor(int/3)))

  def defense: Int = floor((str + dex + int) / 3)
}

object Agent {
  def toAgent(sc: ScreenChar, p: Point): Option[Agent] = {
    sc.c match {
      case 'T' => Some(new Agent("Turret", 'T', p) {
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