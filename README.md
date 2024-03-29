# Mancala Scala

An implementation of the strategy board game, Mancala.
This program supports Player vs Player games, as well as Player vs CPU games
with varying levels difficulty. Move logging is also supported, however, it is
disabled within the code.

Originally created as part of CMPT 470.

# Usage

## CLI Usage
CLI interface usage:
```sh
sbt "cli/run <player A> <player B>"
```

### Arguments

- `<player A>` : the first player name or computer difficulty
- `<player B>` : the second player name or computer difficulty

String arguments represent a human player while integer arguments represent a
computer player. The integer argument for computer players must be between 0 
(easiest) and 9 (hardest) inclusive.

### Options

- `-h` : displays the usage

### Controls

While the program is running the following actions may be made by the player:

- `h` or `help` : prints out the game's help message
- `q` or `quit` : quits the game
- `d` or `display` : displays the current game state
- `a1` to `a6` : performs the move for player A if it is valid
- `b1` to `b6` : performs the move for player B if it is valid

## Server Usage
Server interface usage:

start the websocket server:
```sh
sbt "server/run"
```