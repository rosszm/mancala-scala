// Zack Ross
// zmr462
// 11215196

package mancala.player
import mancala.game.State
import scala.io.StdIn.readLine


/**
  * The HumanPlayer class represents a player controlled by a real person.
  */
class HumanPlayer(n: String, i: Int) extends Player {
  /** The name of the player */
  def name = n
  /** The id of the player */
  def id: Int = i

  /**
    * Perform a move chosen by the player and returns the new state resulting
    * from the move.
    *
    * @return the new state
    */
  def move(state: State): State = {
    var nextState = state
    var valid = false
    while (!valid) {
      readMove(state) match {
        case Some(m) => m.toLowerCase match {
            case "h" => println(state.help)   // print help message
            case "q" => quit()               // quit the application
            case _ => {                     // perform some valid move
              nextState = state.move(m)
              valid = true
            } 
          }
        case None => // invalid move: do nothing
      }
    }
    nextState
  }

  /**
    * Read a move from the command line and return the move if it is valid.
    *
    * @param state the game state
    * @return Some move if the move is valid, `None` otherwise
    */
  protected def readMove(state: State): Option[String] = {
    print(s"$toString move: ")
    val move = readLine().toLowerCase()
    if (state.moves(id).contains(move)|| move == "h" || move == "q") 
      Some(move) // move is valid
    else 
      None  // move is not valid
  }
  
  /**
    * Exits the application from the player.
    *
    * @return Nothing
    */
  def quit() = {
    println("!Quitting Game...")
    sys.exit(0)
  }

}

/** Constructor object for human players */
object HumanPlayer {
  def apply(name: String, id: Int) = new HumanPlayer(name, id)
}