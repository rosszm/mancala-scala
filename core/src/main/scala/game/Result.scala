// Zack Ross
// zmr462
// 11215196

package dev.zacharyross.mancala.core.game


/**
  * The Result class represents the end result of a game with a given score.
  * There are 3 different results. Player A wins, Player B wins, and Draw.
  *
  * @param a the store of player A
  * @param b the store of player B
  */
class Result(a: Int, b: Int) {
  
  /** The winner */
  def winner: Option[Int] = {
    if (a > b) Some(0)
    else if (b > a) Some(1)
    else None
  }
  /** The loser */
  def loser: Option[Int] = {
    if (a < b) Some(0)
    else if (b < a) Some(1)
    else None
  }
  /** Whether the result of the game is a draw */
  def isDraw: Boolean = if (a == b) true else false

  /**
    * Converts this result to a string.
    *
    * @return "<result>"
    */
  override def toString = {
    if (this.isDraw)
        "Draw"
    else {
        val w = this.winner.get
        val l = this.loser.get
        s"Player ${w} Wins over Player ${l}"
    }
  }
}
