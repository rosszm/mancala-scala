// Zack Ross
// zmr462
// 11215196

package mancala
import mancala.game.{State, Result}
import mancala.player.{Player, HumanPlayer, ComputerPlayer}
import mancala.player.util.{MoveMonitor, FileLogger}
import scala.io.Source


/**
  * Main application for the mancala game.
  */
object Main extends App {
  if (args.length == 0) {
    Console.err.println(s"Missing Arguments")
    sys.exit(1)
  }
  // player structures
  var playerFiles: Vector[String] = Vector[String]()
  var players: Vector[Player] = Vector[Player]()

  // handle the program arguments and options
  args.foreach(handleArg)

  // if not quick-play, read players from their files
  if (players.isEmpty)
    players = playerFiles.take(2).zipWithIndex
      .map(p => readPlayer(p._1, p._2))
  
  // create initial game state
  var state: State = State()
  
  // EVENT LOOP: perform a move for the current player
  while (!state.isEnd) {
    println(s"$state\n")
    state = players(state.currentPlayer).move(state)
  }
  // print game results
  println(state)
  val result: Result = state.results.get // get works because the game is over
  if (result.isDraw)
    println("Game ends: Draw")
  else
    println(s"Game ends: ${players(state.results.get.winner.get)} wins over " +
      s"${players(state.results.get.loser.get)}") 
  

  /**
    * Read a player from a file
    *
    * @param path the file path
    * @param id the player id
    * @return the Player
    */
  def readPlayer(path: String, id: Int): Player = {
    val src = try {
      Source.fromFile(path)
    } catch {
      case e: Exception => {
        Console.err.println(s"Could not read player $id: $e")
        sys.exit(1)
      }
    }
    val lines = src.getLines
    if (!lines.hasNext) {
      Console.err.println(s"Could not read player $id: empty file")
      sys.exit(1)
    }
    val args = lines.next.split(" ")
    val player = args(0) match {
      case "h" => HumanPlayer(args(1), id)
      case "c" => {
        // Create computer players with a file logger
        new ComputerPlayer(args(1).toInt, id) with MoveMonitor with FileLogger
      }
      case _ => {
        Console.err.println(s"Could not read player $id: Invalid file format")
        sys.exit(1)
      }
    }
    src.close
    player
  }
  
  /**
    * Handle the command-line arguments given to the program.
    */
  def handleArg(arg: String) {
    arg match {
      case "-h" => { // print the usage message
        println(usage) 
        sys.exit(0)
      }
      case "-p" => players = Vector( // quick-play option
        HumanPlayer("Player A", 0), 
        HumanPlayer("Player B", 1)
      )
      case _ => playerFiles = playerFiles :+ arg
    }
  }

  /**
    * Return the usage instructions for the application.
    *
    * @return the usage instructions
    */
  def usage = s"""Usage:
    |  sbt run [-h] [-p] <player A> <player B>
    |Arguments:
    |  <player A>    the first player file or name
    |  <player B>    the second player file or name
    |Options:
    |  -h            displays the usage
    |  -p            start a human vs human game without input files""".stripMargin
}