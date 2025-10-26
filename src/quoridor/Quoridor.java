package quoridor;

import common.Game;
import common.gameUtils.Score;
import common.gameUtils.Timer;
import common.player.Player;
import java.util.Scanner;

/** Terminal Quoridor gameplay loop (compatible with your infrastructure). */
public class Quoridor extends Game {
    private QuoridorBoard board;
    private Player p1, p2;
    private Player current;

    public Quoridor() { reset(); }

    @Override
    public void start() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Quoridor ===");
        System.out.print("Player A name: ");
        String n1 = sc.nextLine().trim();
        if (n1.isEmpty()) n1 = "PlayerA";
        System.out.print("Player B name: ");
        String n2 = sc.nextLine().trim();
        if (n2.isEmpty()) n2 = "PlayerB";
        // Adds player names
        p1 = new Player(n1, 1);
        p2 = new Player(n2, 2);
        current = p1;

        Score score = new Score();
        long startTime = System.currentTimeMillis();

        while (true) {
            System.out.print(board.displayBoard());
            System.out.printf("A -> %s\n", p1.getName());
            System.out.printf("B -> %s\n\n", p2.getName());
            System.out.println("Note: Horizontal and Vertical walls cannot cross or touch each other. Players can pass each other.");
            System.out.printf("[%s] turn. Enter a command :\n", current.getName());
            System.out.println("M r c -> Move to row r and column c");
            System.out.println("H r c -> Place horizontal wall top and top-right of box [r,c]");
            System.out.println("V r c -> Place vertical wall left and bottom-left of box [r,c]");
            System.out.println("S -> Show board");
            System.out.println("Q -> Q to Main Menu");
            String cmd = sc.next().toUpperCase();
            if ("Q".equals(cmd) || "QUIT".equals(cmd)) {
                System.out.println("Exiting Quoridor...");
                break;
            } else if ("S".equals(cmd) || "SHOW".equals(cmd)) {
                continue;
            } else if ("M".equals(cmd)) {
                int r = sc.nextInt(), c = sc.nextInt();
                sc.nextLine();
                if (board.movePawn(current.getId() - 1, r, c)) {
                    if (board.isSolved()) {
                        long endTime = System.currentTimeMillis();
                        long totalSec = (endTime - startTime) / 1000;
                        String timer = Timer.formatTime(totalSec);

                        System.out.println(board.displayBoard());
                        System.out.printf("Winner: %s ! Time: %s%n", current.getName(), timer);

                        current.makeWinner();
                        score.saveScore(current.getName(), "Quoridor", timer, current.getName());
                        System.out.println("Game finished in " + timer);
                        break;
                    }
                    current = (current == p1) ? p2 : p1;
                } else {
                    System.out.println("Illegal move.");
                }
            } else if ("H".equals(cmd) || "V".equals(cmd)) {
                char ori = cmd.charAt(0);
                int r = sc.nextInt(), c = sc.nextInt();
                sc.nextLine();
                if (board.placeWall(current.getId() - 1, ori, r, c)) {
                    current = (current == p1) ? p2 : p1;
                } else {
                    System.out.println("Illegal wall placement.");
                }
            } else {
                System.out.println("Unknown command.");
                sc.nextLine();
            }
        }
    }

    @Override
    public void reset() { board = new QuoridorBoard(); }

    @Override
    public String state() { return board.displayBoard(); }
}
