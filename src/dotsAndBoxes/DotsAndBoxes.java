package dotsAndBoxes;

import common.Board;
import common.Game;
import common.gameUtils.Score;
import common.gameUtils.Timer;
import common.player.Player;
import javafx.util.Pair;

import java.util.Scanner;

/*Full interactive Dots and Boxes game.*/
public class DotsAndBoxes extends Game {
    private DotsAndBoxesBoard board;
    private static Player p1;
    private static Player p2;

    /* Launches Dots and Boxes */
    public static void launchDotsAndBoxes(Scanner sc, Score score) {
        // setup players
        System.out.print("Enter Player 1 name: ");
        p1 = new Player(sc.nextLine(), 1);
        System.out.print("Enter Player 2 name: ");
        p2 = new Player(sc.nextLine(), 2);

        long start = System.currentTimeMillis();

        Game game = new DotsAndBoxes();
        game.start();

        long end = System.currentTimeMillis();
        long totalSec = (end - start) / 1000;
        String timer = Timer.formatTime(totalSec);

        String playerNames = p1.getName() + " and " + p2.getName();
        String winner = printWinner();
        score.saveScore(playerNames, "Dots and Boxes", timer, winner);
        System.out.println("Game finished in " + timer);
    }

    /** Start the game and handle user input loop */
    @Override
    public void start() {
        Scanner sc = new Scanner(System.in);

        Pair<Integer, Integer> dimensions = Board.setupBoardDimensions(sc);
        int rows = dimensions.getKey();
        int cols = dimensions.getValue();

        // initialize board
        board = new DotsAndBoxesBoard(rows, cols, p1.getName(), p2.getName());
        Player current = p1;
        //  print instructions
        int totalPoints = (rows + 1) * (cols + 1);
        System.out.println("Points numbered 1.." + totalPoints + ".");
        System.out.println("Enter two adjacent point numbers per move. Type 'quit' to exit.");
        //  main gameplay loop
        while (!board.isSolved()) {
            board.displayBoard();
            System.out.println(current.getName() + "'s turn:");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Game aborted.");
                return;
            }

            String[] parts = input.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Invalid input.");
                continue;
            }

            try {
                // parse two point numbers
                int pA = Integer.parseInt(parts[0]);
                int pB = Integer.parseInt(parts[1]);
                // claim the edge and check if common.player scored a box
                boolean scored = board.claimEdgeByPoints(pA, pB, current);
                // if no box scored, switch to the other common.player
                if (!scored) current = (current == p1) ? p2 : p1;
            } catch (Exception e) {
                System.out.println("Invalid move: " + e.getMessage());
            }
        }
        //  when all boxes filled
        end();
    }
    /*Reset the game state */
    @Override public void reset() { board = null; }
    /*Return simple text summary */
    @Override public String state() { return "Dots and Boxes finished."; }

    /*Show final scores and winner */
    private void end() {
        board.displayBoard();
        System.out.print("Final Scores:- ");
        System.out.print(p1.getName() + ": " + p1.getScore());
        System.out.print(" | ");
        System.out.println(p2.getName() + ": " + p2.getScore());
        // compare scores
        if (p1.getScore() > p2.getScore()) {
            System.out.println(p1.getName() + " wins!");
            p1.makeWinner();
        }
        else if (p2.getScore() > p1.getScore()) {
            System.out.println(p2.getName() + " wins!");
            p2.makeWinner();
        }
        else
            System.out.println("It's a tie!");
    }


    static private String printWinner() {
        if (p1.isWinner() && p2.isWinner()) {
            return p1.getName() + " and " + p2.getName();
        }
        else if (p1.isWinner()) {
            return p1.getName();
        }
        else if (p2.isWinner()) {
            return p2.getName();
        }
        else  {
            return "Tie";
        }
    }
}
