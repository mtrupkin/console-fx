package me.mtrupkin.game

import javafx.animation.AnimationTimer

import me.mtrupkin.console.Console


/**
 * Created by mtrupkin on 12/15/2014.
 */


class GameEngine2(controller: ConsoleController, terminal: Console)  {
  val updatesPerSecond = 100
  val framesPerSecond = 200

  val updateRate = (1f / updatesPerSecond) * 1000
  val renderRate = (1f / framesPerSecond) * 1000
  var completed = false

  def close() = completed = true

  def render() {
    if (!completed) {
      val screen = controller.render
      terminal.draw(screen)
    }
  }

  def processInput() {
    for(input <- terminal.input()) {
      controller.handle(input)
    }
  }

  def gameLoop() {
    var lastUpdateTime = System.currentTimeMillis()
    var fpsTimer = System.currentTimeMillis()
    var updateAccumulator = 0.0
    var renderAccumulator = 0.0
    var frames = 0
    var updates = 0

    while (!completed) {
      val currentTime = System.currentTimeMillis()
      val elapsedTime = currentTime - lastUpdateTime
      lastUpdateTime = currentTime

      updateAccumulator += elapsedTime

      while (updateAccumulator >= updateRate) {
        updates += 1
        processInput
        controller.update(updateRate.toInt)

        updateAccumulator -= updateRate
      }

      while (renderAccumulator >= renderRate) {
        render
        frames += 1

        renderAccumulator -= renderRate
      }

      if ( currentTime - fpsTimer > 10000 ) {
        fpsTimer = System.currentTimeMillis()
        println(s"frames: $frames updates: $updates")
        frames = 0
        updates = 0
      }
    }

  }
}
