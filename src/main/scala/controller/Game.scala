package me.mtrupkin.controller

import javafx.collections.FXCollections._
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView}
import javafx.scene.layout.Pane
import me.mtrupkin.control.ConsoleFx
import me.mtrupkin.console._
import me.mtrupkin.core.{Point, Points}
import me.mtrupkin.controller.game.{AgentBean}
import me.mtrupkin.game.model.World

import scalafx.scene.{control => sfxc}
import scalafx.scene.{layout => sfxl}
import scalafx.scene.{input => sfxi}

import scala.collection.JavaConversions._
import scalafx.Includes._

/**
 * Created by mtrupkin on 12/15/2014.
 */
trait Game { self: Controller =>
  class GameController(val world: World) extends ControllerState {
    implicit def itos(int: Int): String = int.toString
    val name = "Game"

    @FXML var trackerTbl: TableView[AgentBean] = _
    @FXML var nameCol:  TableColumn[AgentBean, String] = _
    @FXML var hpCol:  TableColumn[AgentBean, String] = _
    @FXML var strText: Label = _
    @FXML var dexText: Label = _
    @FXML var intText: Label = _
    @FXML var moveText: Label = _
    @FXML var hpText: Label = _
    @FXML var infoText: Label = _
    @FXML var infoDescText: Label = _
    @FXML var infoPosText: Label = _
    @FXML var pane: Pane = _

    var console: ConsoleFx = _
    var screen: Screen = _

    def initialize(): Unit = {
      console = new ConsoleFx(world.tileMap.size)
      console.setStyle("-fx-border-color: white")
      new sfxl.Pane(console) {
        onMouseClicked = (e: sfxi.MouseEvent) => handleMouseClicked(e)
        onMouseMoved = (e: sfxi.MouseEvent) => handleMouseMove(e)
        onMouseExited = (e: sfxi.MouseEvent) => handleMouseExit(e)
      }

      screen = Screen(world.tileMap.size)
      pane.getChildren.clear()
      pane.getChildren.add(console)
      pane.setFocusTraversable(true)
      new sfxl.Pane(pane) {
        onKeyPressed = (e: sfxi.KeyEvent) => handleKeyPressed(e)
      }

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

      val agentModel = observableArrayList[AgentBean](world.agents.map(a => new AgentBean(a)))
      trackerTbl.setItems(agentModel)

      strText.setText(stats.str)
      dexText.setText(stats.dex)
      intText.setText(stats.int)
      moveText.setText(move)
      hpText.setText(hp)

      world.render(screen)
      console.draw(screen)
    }

    implicit def pointToString(p: Point): String = {
      s"[${p.x}, ${p.y}]"
    }

    def handleMouseClicked(mouseEvent: sfxi.MouseEvent): Unit = {}

    def handleMouseMove(mouseEvent: sfxi.MouseEvent): Unit = {
      val (x, y) = (mouseEvent.x, mouseEvent.y)
      for( s <- console.toScreen(x, y)) {
        val p: Point = s
        infoPosText.setText(p)

        val target = world.agents.find(a => a.position == p)
        target match {
          case Some(t) => {
            infoText.setText(t.name)
            infoDescText.setText(t.hp)
          }
          case None => {
            infoText.setText(world.tileMap(p).name)
          }
        }
      }
    }

    def handleMouseExit(mouseEvent: sfxi.MouseEvent): Unit = {
      infoText.setText("")
      infoDescText.setText("")
      infoPosText.setText("")
    }

    def handleKeyPressed(event: sfxi.KeyEvent): Unit = {
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

  def keyCodeToConsoleKey(event: sfxi.KeyEvent): ConsoleKey = {
    val modifier = Modifier(event.shiftDown, event.controlDown, event.altDown)
    val jfxName = event.code.name
    val key = Key.withName(jfxName)
    ConsoleKey(key, modifier)
  }
}

