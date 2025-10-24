package slidePuzzle;

import common.Board;
import common.Game;
import common.gameUtils.Score;
import common.gameUtils.Timer;
import common.player.Player;
import javafx.util.Pair;

import java.util.Random;
import java.util.Scanner;

/*
 Slide Puzzle implements the Game interface.
 Supports moving by direction or by tile ID.
 */
public class SlidePuzzle extends Game {
    private SlidePuzzleBoard board;
    private final int rows, cols;
    private final Random rng;

    public SlidePuzzle(int rows, int cols, long seed) {
        this.rows = rows;
        this.cols = cols;
        this.rng = new Random(seed);
        reset();
    }

    /* Launches Slide Puzzle with common.player and timer */
    public static void launchSlidePuzzle(Scanner sc, Score score) {
        System.out.print("Enter player name: ");
        String playerName = sc.nextLine();

        Player player = new Player(playerName, 0);

        Pair<Integer, Integer> dimensions = Board.setupBoardDimensions(sc);
        int rows = dimensions.getKey();
        int cols = dimensions.getValue();

        long start = System.currentTimeMillis();

        SlidePuzzle game = new SlidePuzzle(rows, cols, System.currentTimeMillis());
        game.start();

        long end = System.currentTimeMillis();
        long totalSec = (end - start) / 1000;
        String timer = Timer.formatTime(totalSec);

        score.saveScore(player.getName(), "Slide Puzzle", timer, player.getName());
        System.out.println("Game finished in " + timer);
    }

    /* Starts the puzzle */
    @Override
    public void start() {
        Scanner sc = new Scanner(System.in);
        System.out.println(state());

        while (!isSolved()) {
            System.out.print("Move (U/D/L/R or tile <id> or EXIT): ");
            String input = sc.nextLine().trim().toUpperCase();
            if (input.isEmpty()) continue;
            if (input.equals("EXIT")) break;

            String[] parts = input.split("\\s+");
            String cmd = parts[0];
            boolean valid = false;

            // Handle direction moves
            if (cmd.equals("MOVE") || cmd.equals("M") ||
                    cmd.equals("U") || cmd.equals("D") ||
                    cmd.equals("L") || cmd.equals("R")) {

                String dir = (cmd.equals("MOVE") && parts.length > 1) ? parts[1] : cmd;
                Direction direction = null;

                switch (dir) {
                    case "U": direction = Direction.UP; break;
                    case "D": direction = Direction.DOWN; break;
                    case "L": direction = Direction.LEFT; break;
                    case "R": direction = Direction.RIGHT; break;
                    default:
                        System.out.println("Invalid direction.");
                }

                if (direction != null) {
                    valid = move(direction);
                    if (!valid) System.out.println("Illegal move!");
                }
            }

            // Handle tile number moves
            else if (cmd.equals("TILE") || cmd.matches("\\d+")) {
                int tileId;
                try {
                    tileId = cmd.equals("TILE") ? Integer.parseInt(parts[1]) : Integer.parseInt(cmd);
                    valid = moveTile(tileId);
                    if (!valid) System.out.println("Illegal move!");
                } catch (Exception e) {
                    System.out.println("Invalid tile number.");
                }
            }

            // Handle invalid input
            else {
                System.out.println("Invalid command.");
            }

            System.out.println(state());
        }
        if (isSolved())
            System.out.println("🎉 Puzzle Solved!");
        else
            System.out.println("Game exited.");
    }

    /* Resets puzzle with shuffled board */
    @Override
    public void reset() {
        board = new SlidePuzzleBoard(rows, cols);
        board.shuffleSolvable(rows * cols * 30, rng);
    }

    /*Prints current state */
    @Override
    public String state() {
        return board.displayBoard();
    }

    /* Moves the blank tile in a direction (U/D/L/R) */
    public boolean move(Direction d) {
        return board.slide(d);
    }

    /*Moves a specific tile number (if adjacent to blank) */
    public boolean moveTile(int tileId) {
        if (tileId <= 0) return false;
        return board.slide(tileId);
    }

    /*Checks if puzzle solved */
    public boolean isSolved() {
        return board.isSolved();
    }
}
