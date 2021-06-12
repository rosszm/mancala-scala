# Mancala Scala

A command-line based version of the strategy board game, Mancala.
This program supports Player vs Player games, as well as Player vs CPU games
with varying difficulty.

Created as part of CMPT 470 Winter 2021 Term, exercises 6 through 10.

## Usage

`sbt "run <player A> <player B>"`

### Arguments

- `<player A>` --- the first player file or name
- `<player B>` --- the second player file or name

### Options

- `-h` --- displays the usage
- `-p` --- start a human vs human game without input files

## Controls

While the program is running the following actions may be made by the player:

- `h` --- prints out the game's help message
- `q` --- quits the game
- `a1` to `a6` --- performs the move for player A if it is valid
- `b1` to `b6` --- performs the move for player B if it is valid

## File Format

Examples of the file format may be found in `cpu.txt` and `human.txt`. The player
files must be of the following format:

`"h <name>"`

Where `'h'` means that the player is human and `<name>` is the name of the player.

or

`"c <depth>"`

Where `'c'` means the player is a computer player and `<depth>` is the search depth
limit of the CPU player.

**NOTE** -- default human and CPU player files are found in the `./` directory.

## Project Contents

`./src/main/scala/Main.scala` --- The main program

`./src/main/scala/game/` --- Directory containing code relating to the game state

`./src/main/scala/player/` --- Directory containing code relating to players
