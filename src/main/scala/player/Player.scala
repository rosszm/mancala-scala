// Zack Ross
// zmr462
// 11215196

package mancala.player
import mancala.game.State


/**
  * `Player` Interface
  * 
  * Defines common features between different players of a game.
  */
abstract class Player {

  /** The name of the player */
  def name: String
  /** The id number of the player */
  def id: Int
 
  /**
    * Returns the move chosen by the player.
    *
    * @param state the origin state
    * @return the move
    */
  def move(state: State): State

  /**
    * Converts this player to a string
    *
    * @return "<player>"
    */
  override def toString = s"$name ($id)"
}