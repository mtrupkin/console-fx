package sample

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button

import me.mtrupkin.controller.Controller

/**
 * Created by mtrupkin on 12/15/2014.
 */


trait IntroTest { self: TestController =>

  class IntroController extends ControllerState {
    val name = "IntroTest"

    @FXML var continueGameButton: Button = _

    def handleContinueGame(event: ActionEvent) = {
      changeState(new ControlController)
    }

    def handleExit(event: ActionEvent) = stage.close()
  }

}
