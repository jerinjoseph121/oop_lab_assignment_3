package quoridor;

import common.Game;
import common.player.Player;
import common.gameUtils.Score;
import common.gameUtils.Timer;
import java.util.Scanner;

/**
 * Game loop for Quoridor.
 * - Handles players, turns, inputs
 * - Uses QuoridorBoard for logic
 */
public class Quoridor extends Game {
    private QuoridorBoard board;
    private Player p1, p2, current;

    public Quoridor() { reset(); }

    @Override
    public void start() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Quoridor ===");
        System.out.print("Enter Player A name: ");
        String n1 = sc.nextLine().trim();
        if (n1.isEmpty()) n1 = "PlayerA";
        System.out.print("Enter Player B name: ");
        String n2 = sc.nextLine().trim();
        if (n2.isEmpty()) n2 = "PlayerB";

        p1 = new Player(n1, 1);
        p2 = new Player(n2, 2);
        current = p1;
        Score score = new Score();
        long start = System.currentTimeMillis();

        while (true) {
            System.out.println(board.displayBoard());
            System.out.printf("[%s] Enter move (M r c | H r c | V r c | Q): ", current.getName());
            String cmd = sc.next().toUpperCase();

            if (cmd.equals("Q")) break;

            if (cmd.equals("M")) {
                int r = sc.nextInt(), c = sc.nextInt();
                if (board.movePawn(current.getId() - 1, r, c)) {
                    if (board.isSolved()) {
                        long time = (System.currentTimeMillis() - start) / 1000;
                        String formatted = Timer.formatTime(time);
                        System.out.println("Winner: " + current.getName() + " in " + formatted);
                        score.saveScore(current.getName(), "Quoridor", formatted, current.getName());
                        break;
                    }
                    current = (current == p1) ? p2 : p1;
                } else System.out.println("Illegal move.");
            } else if (cmd.equals("H") || cmd.equals("V")) {
                char ori = cmd.charAt(0);
                int r = sc.nextInt(), c = sc.nextInt();
                if (board.placeWall(current.getId() - 1, ori, r, c))
                    current = (current == p1) ? p2 : p1;
                else System.out.println("Invalid wall placement.");
            }
        }
    }

    @Override
    public void reset() { board = new QuoridorBoard(); }

    @Override
    public String state() { return board.displayBoard(); }
}
