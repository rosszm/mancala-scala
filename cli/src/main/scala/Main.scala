// Zack Ross
// zmr462
// 11215196

package dev.zacharyross.mancala.cli

import dev.zacharyross.mancala.cli.player.PlayerCLI
import dev.zacharyross.mancala.core.game.{State, Result}
import dev.zacharyross.mancala.core.player.{Player, PlayerAI}
import dev.zacharyross.mancala.core.player.util.{MoveMonitor, FileLogger}


/**
  * Runs the cli implementation of mancala.
  *
  * @param args the program arguments
  */
@main
def main(args: String*) = {
  var players = getPlayersFromArgs(args)
  var state: State = State() // create initial game state

  while (!state.isEnd) { // EVENT LOOP: perform a move for the current player
    println(s"$state\n")
    state = players(state.currentPlayer).move(state)
  }

  // display end results
  println(state)
  val result: Result = state.results.get // get works because the game is over
  if (result.isDraw)
    println("Game ends: Draw")
  else
    println(s"Game ends: ${players(state.results.get.winner.get)} wins over " +
      s"${players(state.results.get.loser.get)}")
}

/**
  * Attempt to get the players from an array of arguments.
  * If `args` is empty, use the default player configuration.
  *
  * Post-conditions:
  * - exit the program if an argument is invalid.
  *
  * @param args an array of string arguments
  * @return vector containing the players
  */
private def getPlayersFromArgs(args: Seq[String]): Vector[Player] = {
  args.length match {
    case 0 => defaultPlayers
    case 1 => {
      if (args(0) == "-h") displayUsage
      else exitOnError("must provide 0 or 2 arguments")
    }
    case 2 => {
      args.zipWithIndex.map((arg, i) => {
        if (arg == "-h") displayUsage
        try {
          val argInt = arg.toInt
          if (argInt < 0 || argInt > 9)
            exitOnError(s"invalid difficulty, ${argInt}")
          PlayerAI(arg.toInt, i)
        }
        catch case e: NumberFormatException => PlayerCLI(arg, i)
      }).toVector
    }
    case _ => exitOnError("too many arguments")
  }
}

/** The default player configuration. */
private def defaultPlayers: Vector[Player] = {
  Vector(PlayerCLI("Player", 0), PlayerAI(2, 1))
}

/**
  * Exits prints the error message and exits the program.
  */
private def exitOnError(message: String) = {
  Console.err.println(message)
  sys.exit(1)
}

/**
  * Prints the usage instructions and exits the program.
  */
private def displayUsage = {
  println(usage)
  sys.exit(0)
}

/**
  * Return the usage instructions for the application.
  *
  * @return the usage instructions
  */
private def usage = s"""Usage:
  |  [-h] [-p] <player A> <player B>
  |Arguments:
  |  <player A>    the first player name or difficulty
  |  <player B>    the second player name or difficulty
  |Options:
  |  -h            displays the usage
  |Notes:
  |  The program excepts 0 or 2 arguments:
  |    0: starts a default human vs computer game
  |    2: uses each arg for the players
  |  Human players are specified by a name as a string argument
  |  Computer players are specified by their difficulty an integer argument
  |  Valid difficulties including integers from 0 (easiest) to 9 (hardest)
  |Example:
  |  mancala Bob 5""".stripMargin
