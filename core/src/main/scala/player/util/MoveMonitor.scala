// Zack Ross
// zmr462
// 11215196

package dev.zacharyross.mancala.core.player.util

import dev.zacharyross.mancala.core.game.State
import dev.zacharyross.mancala.core.player.Player


/**
  * `MoveMonitor` Trait
  */
trait MoveMonitor extends Player {
  /**
    * The move function to monitor.
    *
    * @param state the origin state
    * @return the new state
    */
  def move(state: State): State
}