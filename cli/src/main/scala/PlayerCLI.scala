// Zack Ross
// zmr462
// 11215196

package dev.zacharyross.mancala.cli.player

import scala.io.StdIn.readLine
import dev.zacharyross.mancala.core.game.State
import dev.zacharyross.mancala.core.player.Player


/**
  * The HumanPlayer class represents a player controlled by a real person.
  */
class PlayerCLI(name: String, index: Int) extends Player(name, index) {
  override def move(state: State): State = {
    var nextState = state
    var valid = false
    while (!valid) {
      print(s"$toString move: ")
      readLine().toLowerCase() match {
        case "h" | "help" => println(help) // print help message
        case "q" | "quit" => quit()             // quit the application
        case "d" | "display" => println(state) // displays the current state
        case mv => {                          // perform a move
          if (state.moves(index).contains(mv)) {
            nextState = state.move(mv)
            valid = true
          }
          else valid = false
        }
      }
    }
    nextState
  }

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
    * Exits the application from the player.
    *
    * @return Nothing
    */
  def quit() = {
    println("!Quitting Game...")
    sys.exit(0)
  }

}
