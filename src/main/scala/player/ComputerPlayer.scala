// Zack Ross
// zmr462
// 11215196

package mancala.player
import mancala.game.State


/**
  * The ComputerPlayer class represents a player controlled by a computer.
  */
class ComputerPlayer(d: Int, i: Int) extends Player {
  /** The name of the player */
  def name: String = "Computer"
  /** The id of the player */
  def id: Int = i
  
  /**
      * The search depth of the CPU player
      *
      * @return the depth
      */
  def depth = d

  /**
    * Perform a move chosen by the player and returns the new state resulting
    * from the move.
    *
    * @param state the origin state
    * @return the new state
    */
  def move(state: State): State = {
    state.moves(state.currentPlayer).map(state.move)
      .map(m => (m, search(m, 0)))
      .map(m => (m._1, estimate(m._2, id, d)))
      .reduceLeft((m1, m2) => if (m1._2._2 > m2._2._2) m1 else m2)._1
  }

  /**
    * Search the moves of a given state for the best resulting state up to a 
    * given depth.
    *
    * @param state the start state
    * @param d the depth
    */
  protected def search(state: State, d: Int): State = {
    // return state when the game is over or when the depth limit is reached
    if (state.isEnd || d == depth) state
    else {
      // recurse over all possible moves of the state, then map the estimate
      state.moves(state.currentPlayer).map(state.move)
        .map(search(_, d + 1))
        .map(estimate(_, state.currentPlayer, d))
        .reduceLeft(best)._1
    }
  }

  /**
    * Compare two state estimates and return the state with the better estimate.
    *
    * @param e1 the first state and its estimate
    * @param e2 the second state and its estimate
    * @return the better state
    */
  protected def best(e1: (State, Int), e2: (State, Int)): (State, Int) = {
      if (e1._2 >= e2._2) e1 else e2
  }

  /**
    * Assign a numerical value to a state that represent how "good" the state
    * is for some player. The depth informs the priority of win and loss states.
    * For example, a loss in 3 turns is better than a loss in 1 turn. Likewise,
    * a win in 1 turn is better than a win in 3 turns.
    *
    * @param state the state
    * @param p the player
    * @param d the depth
    * @return the state and its "goodness"
    */
  protected def estimate(state: State, p: Int, d: Int): (State, Int) = {
    if (state.isEnd) {  // if game is over
      val est = state.results.get.winner match {
        case Some(winner) => { 
          if (p == winner) 
            Int.MaxValue - d // a win gives the highest possible weight
          else 
            Int.MinValue + depth - d // a loss gives the lowest possible weight
        }
        case None => 0 // draw gives a value of 0
      }
      (state, est)
    }
    // if the state is not an end state, compare the scores of the players
    val other = p ^ 1 // get other player id
    (state, state.score(p) - state.score(other))
  }
}
