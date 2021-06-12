// Zack Ross
// zmr462
// 11215196

package mancala.game


/**
  * The State class represents a state of a mancala board.
  *
  * @param nPots the number of pots for each player
  * @param nPebbles the number of pebbles each pot starts with
  */
class State(nPots: Int, nPebbles: Int) {
  /** The pots of the board */
  protected var pots = Vector.tabulate(2, nPots)((p, h) => nPebbles)

  /** The store for each player*/
  protected var store = Vector[Int](0, 0)

  /** The player whose turn it is */
  protected var turn: Int = 0

  /**
    * Constructor for creating a copy of the state.
    *
    * @param s the state
    * @return the copy
    */
  protected def this(s: State) {
    this(s.numPots, s.numPebbles)
    pots = s.pots
    store = s.store
    turn = s.turn
  }

  /**
    * Return the number of starting pebbles in each pot.
    *
    * @return the number of pebbles
    */
  protected def numPebbles: Int = nPebbles

  /**
    * Return the number of pots in each row of the state.
    *
    * @return the number of pots
    */
  def numPots: Int = nPots

  /**
    * Return the index of the current player
    *
    * @return
    */
  def currentPlayer: Int = turn

  /**
    * Return the the value of the player stores
    *
    * @return the player stores
    */
  def score: Vector[Int] = store

  /**
    * Returns a help message that describes how to read the string
    * representation of the state.
    *
    * @return the help message
    */
  def help: String = {
    s"""The mancala board is represented by 2 lines. The first line is the row
      |for the first player, Player A. The second line is the row for the second 
      |player, Player B. There are 6 pots labelled 1-6 for each player that have 
      |numbers representing how many pebbles are in each pot.
      |    
      |    store  a6 a5 a4 a3 a2 a1
      |
      |        0   4  4  4  4  4  4       <- Player A
      |            4  4  4  4  4  4   0   <- Player B
      |
      |           b6 b5 b4 b3 b2 b1  store""".stripMargin
  }

  /**
    * Sow a pebble in the given pot. This function is protected because it
    * directly modifies `this`; therefore, it should not be called directly,
    * but on a new `State` within `this`.
    *
    * @param pot the pot
    */
  protected def sow(pot: (Int, Int)) {
    if (pot._2 == -1) // sow store
      store = store.updated(pot._1, store(pot._1) + 1)
    else // sow pot
      pots = pots.updated(pot._1, pots(pot._1).updated(pot._2, pots(pot._1)(pot._2) + 1))
  }

  /**
    * Capture the pebbles in the opposite pot and move them to a given player's
    * store. This function is protected because it directly `this`; therefore, 
    * it should not be called directly, but on a new `State` within `this`.
    *
    * @param pot the pot whose opposite will be taken
    */
  protected def capture(pot: (Int, Int)) = {
    val opposite = (pot._1 ^ 1, (nPots - 1) - pot._2) 
    store = store.updated(turn, store(turn) + take(opposite))
  }

  /**
    * Returns the number of pebbles in a given pot. This function is protected 
    * because it modifies `this`; therefore, it should not be called directly,
    * but on a new `State` within `this`.
    *
    * @param pot the pot
    * @return the number of pebbles
    */
  protected def take(pot: (Int, Int)): Int = {
    val pebbles = pots(pot._1)(pot._2)
    pots = pots.updated(pot._1, pots(pot._1).updated(pot._2, 0))
    pebbles
  }

  /**
    * Return the moves that a given player can perform in this state.
    *
    * @param player the player
    * @return the valid moves of the player
    */
  def moves(player: Int): Vector[String] = {
    val letter = player match { 
      case 0 => 'a' // player A
      case 1 => 'b' // player B
      case _ => Vector[String]() // Not a valid player
    }
    if (turn != player) Vector[String]() // player cannot move when it is not their turn
    
    pots(player).toVector.zipWithIndex.filter{case (p, i) => p > 0}
            .map(p => s"$letter${p._2 + 1}")
  }

  /**
    * Perform a move on this board state and return the next board state based
    * on the given move.
    *
    * @param m the move
    * @return the next board state
    */
  def move(m: String): State = {
    require(moves(currentPlayer).contains(m)) // player must have moves to move
    var pot = m(0) match {
      case 'a' => (0, m(1).toString.toInt - 1) 
      case 'b' => (1, m(1).toString.toInt - 1) 
    }
    val state = new State(this)
    val pebbles = state.take(pot)
    val potsToSow = (1 to pebbles).map(pebble => potAt(pot, pebble))
    potsToSow.foreach(state.sow)
    val lastPot = potsToSow.last
    if (lastPot._1 == turn) {
      if (lastPot._2 != -1) {
        state.capture(potsToSow.last)
        state.turn = turn ^ 1 // switch player turn 
      }
      else state.turn = turn // remain same player turn
    }
    else state.turn = turn ^ 1 // switch player turn 
    state
  }

  /**
    * Returns the pot that is a given number of pebbles away from some starting
    * pot.
    *
    * @param start the starting pot
    * @param pebble the number of the pebble
    * @return the pot
    */
  protected def potAt(start: (Int, Int), pebble: Int): (Int, Int) = {
    val p = start._2 + pebble // the un-bounded pot location
    val player = (start._1 + (p / nPots)) % 2 // calculate the player row
    var pot = (p % nPots) // calculate the pot
    if (player != start._1) {
      if (pot == 0) 
        (start._1, -1) // case when the pot is the player store 
      else 
        (player, pot - 1) // have to account player store here
    }
    else
      (player, pot)
  }

  /**
    * Returns the results if the state is an end state.
    *
    * @return `Some[mancala.game.Result]` if the state is an end state, 
    * `None` otherwise
    */
  def results: Option[Result] = {
    if (isEnd)
      Some(new Result(store(0), store(1)))
    else None
  }

  /** 
    * Tests whether the state is the start state.
    *
    * @return `true` if the state is the start state, `false` otherwise
    */
  def isStart: Boolean = {
    pots.foreach(row => {
      if (row.exists(p => p != numPebbles)) 
        return false
    })
    true
  }

  /** 
    * Tests whether the state is an end state.
    *
    * @return `true` if the state is an end state, `false` otherwise
    */
  def isEnd: Boolean = {
    val existsA = pots(0).exists(p => p > 0)
    val existsB = pots(1).exists(p => p > 0)
    !(existsA && existsB)
  }

  /**
    * Return the string representation of this state.
    *
    * @return the state as a string
    */
  override def toString(): String = {
    val p1 = new StringBuilder().append(s" ${store(0)}  ")
    pots(0).reverse.foreach(p => {
      if ( p > 9) p1.append(s"$p ") else p1.append(s"$p  ")
    })
    
    val p2 = new StringBuilder().append(" "+" " * (1+(store(0)/10)))
    pots(1).foreach(p => {
      if ( p > 9) p2.append(s" $p") else p2.append(s"  $p")
    })
    p2.append(s"  ${store(1)}")

    p1.toString() + "\n" + p2.toString()
  }

}


/**
  * The State object allows for simple construction of new State Instances
  */
object State {
  /**
    * Create an initial mancala board state.
    *
    * @param nPots the number of pots for each player
    * @param nPebbles the number of pebbles each pot starts with
    * @return the board state
    */
  def apply(nPots: Int, nPebbles: Int):State = new State(nPots, nPebbles)

  /**
    * Create an initial mancala board state with the default settings of 6 
    * pots per player and 4 pebbles in each pot.
    */
  def apply():State = apply(6, 4)
}
