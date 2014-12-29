package me.mtrupkin.controller


import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Pane
import javafx.scene.control.Label



import consolefx.ConsoleFx
import me.mtrupkin.console._
import me.mtrupkin.game.model.{Entity, Agent, World}

import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{TableColumn, TableView}



class AgentBean(agent: Agent) {
  val name = new StringProperty(this, "name", agent.name)
  val hp = new StringProperty(this, "hp", agent.hp.toString)
}

/**
 * Created by mtrupkin on 12/15/2014.
 */
trait Game { self: Controller =>
  class GameController(val world: World) extends ControllerState {
    val name = "Game"

//    @FXML var encounterTable:  javafx.scene.control.TableView[AgentBean] = _
//    @FXML var hpCol:  javafx.scene.control.TableColumn[AgentBean, Integer] = _
//    @FXML var nameCol:  javafx.scene.control.TableColumn[AgentBean, String] = _
    @FXML var strText: Label = _
    @FXML var dexText: Label = _
    @FXML var intText: Label = _
    @FXML var hpText: Label = _
    @FXML var pane: Pane = _
    @FXML var tablePane: Pane = _

    var console: ConsoleFx = _
    var screen: Screen = _

    def initialize(): Unit = {
      console = new ConsoleFx(world.tileMap.size)
      console.setStyle("-fx-border-color: white")

      screen = Screen(world.tileMap.size)
      pane.getChildren.clear()
      pane.getChildren.add(console)
      pane.setFocusTraversable(true)

      pane.setOnKeyReleased(new EventHandler[KeyEvent] {
        override def handle(event: KeyEvent): Unit = {
          import Key._
          val key = keyCodeToConsoleKey(event)
          key match {
            case ConsoleKey(Q, Modifiers.Control) => {
              changeState(new IntroController)
            }
            case ConsoleKey(X, Modifiers.Control) => {
              //closed = true
            }
            case ConsoleKey(k, _) => k match {
              case W | Up => world.act(Points.Up)
              case A | Left => world.act(Points.Left)
              case S | Down => world.act(Points.Down)
              case D | Right => world.act(Points.Right)
              case Enter => ???
              //case Escape => flipState(new EscapeMenuController(world))
              case _ =>
            }
            case _ =>
          }
        }
      })

      //val sEncounterTable = new scalafx.scene.control.TableView(encounterTable)
//      val list = FXCollections.observableArrayList(seqAsJavaList(world.encounter.activeAgents.map(a => new AgentBean(a.name, a.hp))))
//      nameCol.setCellValueFactory(new PropertyValueFactory[AgentBean, String]("name"))
//      val nameCol2 = new control.TableColumn[AgentBean, String](nameCol)
      val beans: Seq[AgentBean] = world.encounter.activeAgents.map(a => new AgentBean(a))
      val agentModel: ObservableBuffer[AgentBean] = scalafx.collections.ObservableBuffer[AgentBean](beans)
      import scalafx.scene.control.TableColumn._
      val content: TableView[AgentBean] = new TableView[AgentBean](agentModel) {
        columns ++= List(
          new TableColumn[AgentBean, String] {
            text = "First Name"
            cellValueFactory = {_.value.name}
            prefWidth = 180
          },
          new TableColumn[AgentBean, String]() {
            text = "Last Name"
            cellValueFactory = {  _.value.hp}
            prefWidth = 180
          }
        )
      }

      tablePane.getChildren.clear()
      tablePane.getChildren.add(content)
      tablePane.setFocusTraversable(false)
      content.focusTraversable = false

      timer.start()
    }

    def keyCodeToConsoleKey(event: KeyEvent): ConsoleKey = {
      val modifier = Modifier(event.isShiftDown, event.isControlDown, event.isAltDown)
      val jfxName = event.getCode.getName
      val key = Key.withName(jfxName)
      ConsoleKey(key, modifier)
    }

    def update(elapsed: Int): Unit = {
      implicit def toString(int: Int): String = int.toString
      import world.player._

      strText.setText(stats.str)
      dexText.setText(stats.dex)
      intText.setText(stats.int)
      hpText.setText(hp)

      world.render(screen)
      console.draw(screen)
    }
  }
}
