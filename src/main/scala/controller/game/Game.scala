package me.mtrupkin.controller.game

import javafx.collections.FXCollections._
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView}
import javafx.scene.input.{MouseEvent, KeyEvent}
import javafx.scene.layout.Pane

import consolefx.ConsoleFx
import me.mtrupkin.console._
import me.mtrupkin.controller.Controller
import me.mtrupkin.game.model.World

import scalafx.scene.{control => sfxc}
import scala.collection.JavaConversions._


/**
 * Created by mtrupkin on 12/15/2014.
 */
trait Game { self: Controller =>
  class GameController(val world: World) extends ControllerState
  with KeyHandler {
    implicit def itos(int: Int): String = int.toString
    val name = "Game"

    @FXML var trackerTbl: TableView[AgentBean] = _
    @FXML var nameCol:  TableColumn[AgentBean, String] = _
    @FXML var hpCol:  TableColumn[AgentBean, String] = _
    @FXML var strText: Label = _
    @FXML var dexText: Label = _
    @FXML var intText: Label = _
    @FXML var hpText: Label = _
    @FXML var infoText: Label = _
    @FXML var infoDescText: Label = _
    @FXML var pane: Pane = _

    var console: ConsoleFx = _
    var screen: Screen = _

    def initialize(): Unit = {
      console = new ConsoleFx(world.tileMap.size)
      console.setStyle("-fx-border-color: white")
      console.setOnMouseMoved(new EventHandler[MouseEvent] {
        override def handle(event: MouseEvent): Unit = handleMouse(event)
      } )

      screen = Screen(world.tileMap.size)
      pane.getChildren.clear()
      pane.getChildren.add(console)
      pane.setFocusTraversable(true)
      pane.setOnKeyPressed(this)

      trackerTbl.setPlaceholder(new Label)
      trackerTbl.setMouseTransparent(true)

      nameCol.prefWidthProperty().bind(trackerTbl.widthProperty().multiply(0.75))
      hpCol.prefWidthProperty().bind(trackerTbl.widthProperty().multiply(0.20))

      new sfxc.TableColumn(nameCol).cellValueFactory = { _.value.name }
      new sfxc.TableColumn(hpCol).cellValueFactory = { _.value.hp }

      timer.start()
    }

    def update(elapsed: Int): Unit = {
      import world.player._

      val agentModel = observableArrayList[AgentBean](world.encounter.activeAgents.map(a => new AgentBean(a)))
      trackerTbl.setItems(agentModel)

      strText.setText(stats.str)
      dexText.setText(stats.dex)
      intText.setText(stats.int)
      hpText.setText(hp)

      world.render(screen)
      console.draw(screen)
    }


    def handleMouse(mouseEvent: MouseEvent): Unit = {
      val (px, py) = (mouseEvent.getX, mouseEvent.getY)
      println(s"mouse moved $px $py")

      val p = Point.TupleToPoint(console.toScreen(px, py))
      println(s"mouse moved $p")
      val target = world.encounter.activeAgents.find(a => a.position == p )
      target match {
        case Some(t) => {
          infoText.setText(t.name)
          infoDescText.setText(t.hp)
        }
        case None => {
          infoText.setText(p.toString)
          infoDescText.setText("")
        }
      }
      for (t <- target) {
        infoText.setText(t.name)
        infoDescText.setText(t.hp)
      }
      world.tileMap(p.x, p.y)
    }

    override def handle(event: KeyEvent): Unit = {
      import me.mtrupkin.console.Key._
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
  }
}
