// Zack Ross
// zmr462
// 11215196

package dev.zacharyross.mancala.core.player

import dev.zacharyross.mancala.core.game.State


/**
  * `Player` Interface
  * 
  * Defines common features between different players of a game.
  */
abstract class Player(val name: String, val index: Int) {
  /**
    * Perform a move chosen by the player and returns the new state resulting
    * from the move.
    *
    * @param state the origin state
    * @return the new state
    */
  def move(state: State): State

  /**
    * Converts this player to a string
    *
    * @return "<player>"
    */
  override def toString = s"$name ($index)"
}