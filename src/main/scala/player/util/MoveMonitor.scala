// Zack Ross
// zmr462
// 11215196

package mancala.player.util
import mancala.game.State
import mancala.player.Player


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