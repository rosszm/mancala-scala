// Zack Ross
// zmr462
// 11215196

package dev.zacharyross.mancala.core.player.util

import dev.zacharyross.mancala.core.game.State
import java.io.{File, FileWriter, BufferedWriter}
import java.time.LocalDateTime


/**
  * `FileLogger` Trait
  */
trait FileLogger extends MoveMonitor {
  /**
    * Log the move to a log.txt file.
    *
    * @param state the origin state
    * @return the new state
    */
  abstract override def move(state: State): State = {
    // perform the move
    val newState: State = super.move(state)

    // format the log message
    val message = s"""
      |${ if (state.isStart)
          s"""===================================
            |Game: ${LocalDateTime.now()}
            |""".stripMargin
        else ""
      }
      |------------------------------
      |${super.toString()} Move:
      |From
      |$state
      |To
      |$newState""".stripMargin

    // log the move to the log.txt file
    val file = new File("./log.txt")
    val writer = new BufferedWriter(new FileWriter(file, true))
    writer.write(message)
    writer.close
    newState
  }
}