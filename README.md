# CS 611 - Object Oriented Principles and Practices: Assignment 3

### Joint Peer Project done by:
**Name:** Mingxing Wang\
**BU ID:** U81767669\
**Name:** Jerin Joseph\
**BU ID:** U11191999

## Main File Contents

1. **Src Folder**\
  This folder contains all the important .java files which are used to execute the program. It contains 4 folders at the moment.
    1. **common:** Used to store all the classes and interfaces which are used again for other games.
    2. **main:** Contains the GameApplication class which is used to run the application.
    3. **slidePuzzle:** Contains all the classes which are used specifically for the slidePuzzle game. It includes SlidePuzzle, SlidePuzzleBoard, SlidePiece, SlideTile, Position and Direction class.
    4. **dotsAndBoxes:** Contains all the classes which are used for the dots and boxes game. It includes DotsAndBoxes, DotsAndBoxesBoard, LinePiece and DotTile.
    5. **quoridor:** Contains all the classes which are used for the quoridor game. It includes Quoridor, QuoridorBoard, PawnPiece and WallTile.
2. **READ_ME file**\
  Contains all the important information about the project
3. **Assignment_3_UML_Diagram image**\
  This is an image file which shows the current inheritance structure of all classes. It also shows how classes are divided based on their logic.\
For all classes, the data members and methods are only mentioned without the datatype and arguments. This is to avoid over cramming of details of a class.
4. **Design_Document_Assignment_3**\
   This is a document which explains the initial design, what has been added to create the new game, how to project supports scalability and the role breakdown.

## Run steps
- This entire project is an IntelliJ IDEA project, so download the project and run GameApplication.java file which is located in the main folder
- Note: Make sure java version 8 is being used since this project runs on that version.
- For the run application configuration. Make sure the java version selected is java 8 (We use corretto 1.8).
- Make sure the main class selected is **main.GameApplication**

## Scalability and Extendibility
- Created abstract classes like Board and Game which will be extended to the classes used in different games.
- Created separate classes for timer and score which are utilized in every game.
- Created other classes like Tile and Piece which would be used in future games. 
- Made sure that all the common repetitive methods are added in the common classes which could be utilized in other games in the future.

## Role Assignment:
**Mengxing:** Implemented the logic for the movement of the players and added the foundational code for running the game like creation of the map, able to place walls and added win condition to game.

**Jerin:** Tested the working of the game, added the logic for wall placement, player movement blocking logic and close path prevention logic. Updated the game to include colors for the wall and created the UML diagram, README and Design document.

## I/O Example:
<pre>
=== CS611 Game Hub ===
1) Slide Puzzle
2) Dots and Boxes
3) Quoridor (NEW)
4) View Scores
0) Exit
Select: 3
=== Quoridor ===
Player A name: Jerin
Player B name: Mengxing
      1     2     3     4     5     6     7     8     9   
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 1 |     |     |     |     |  A  |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 2 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 3 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 4 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 5 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 6 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 7 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 8 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 9 |     |     |     |     |  B  |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
Walls left → A: 10 | B: 10
A -> Jerin
B -> Mengxing

Note: Horizontal and Vertical walls cannot cross or touch each other. Players can pass each other.
[Jerin] turn. Enter a command :
M r c -> Move to row r and column c
H r c -> Place horizontal wall top and top-right of box [r,c]
V r c -> Place vertical wall left and bottom-left of box [r,c]
S -> Show board
Q -> Q to Main Menu
M 2 5

      1     2     3     4     5     6     7     8     9   
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 1 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 2 |     |     |     |     |  A  |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 3 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 4 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 5 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 6 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 7 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 8 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 9 |     |     |     |     |  B  |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
Walls left → A: 10 | B: 10
A -> Jerin
B -> Mengxing

Note: Horizontal and Vertical walls cannot cross or touch each other. Players can pass each other.
[Mengxing] turn. Enter a command :
M r c -> Move to row r and column c
H r c -> Place horizontal wall top and top-right of box [r,c]
V r c -> Place vertical wall left and bottom-left of box [r,c]
S -> Show board
Q -> Q to Main Menu
h 3 5
      1     2     3     4     5     6     7     8     9   
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 1 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 2 |     |     |     |     |  A  |     |     |     |     |
   +-----+-----+-----+-----+=====+=====+-----+-----+-----+
 3 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 4 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 5 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 6 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 7 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 8 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 9 |     |     |     |     |  B  |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
Walls left → A: 10 | B: 9
A -> Jerin
B -> Mengxing

Note: Horizontal and Vertical walls cannot cross or touch each other. Players can pass each other.
[Jerin] turn. Enter a command :
M r c -> Move to row r and column c
H r c -> Place horizontal wall top and top-right of box [r,c]
V r c -> Place vertical wall left and bottom-left of box [r,c]
S -> Show board
Q -> Q to Main Menu
v 5 5
      1     2     3     4     5     6     7     8     9   
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 1 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 2 |     |     |     |     |  A  |     |     |     |     |
   +-----+-----+-----+-----+=====+=====+-----+-----+-----+
 3 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 4 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 5 |     |     |     |     \     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 6 |     |     |     |     \     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 7 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 8 |     |     |     |     |     |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
 9 |     |     |     |     |  B  |     |     |     |     |
   +-----+-----+-----+-----+-----+-----+-----+-----+-----+
Walls left → A: 9 | B: 9
A -> Jerin
B -> Mengxing

Note: Horizontal and Vertical walls cannot cross or touch each other. Players can pass each other.
[Mengxing] turn. Enter a command :
M r c -> Move to row r and column c
H r c -> Place horizontal wall top and top-right of box [r,c]
V r c -> Place vertical wall left and bottom-left of box [r,c]
S -> Show board
Q -> Q to Main Menu
q
Exiting Quoridor...

=== CS611 Game Hub ===
1) Slide Puzzle
2) Dots and Boxes
3) Quoridor (NEW)
4) View Scores
0) Exit
Select: 0
Goodbye!
</pre>
