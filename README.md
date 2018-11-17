Deadwood
By Kris and Jared

How to compile:
- javac Deadwood.java Board.java Player.java Role.java Room.java Scene.java XMLParse.java


How to run the code:
- java Deadwood <num_players>


Instructions to play the game:
- help: Prints a list of all user commands.
- move room_name: Moves active player to room specified by room_name
- list rooms: Prints all rooms
- list neighbors: Prints all neighboring rooms
- desc room [room_name]: Prints out attributes of the room specified by room_name (defaults to current room)
- desc scene: Prints out attributes of the current room's scene
- desc player [player_name]: Prints out attributes of the player specified by player_name (defaults to active player)
- take role role_name: Takes the role specified by role_name
- act: Acts under the current role
- rehearse: Rehearses under the current role
- upgrade rank_num: Upgrades the player's rank to rank_num and deducts the appropriate amount of currency
- end: ends the active player's turn
