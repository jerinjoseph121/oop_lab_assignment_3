package common.gameUtils;

import java.io.*;

public class Score {
    private static final String SCORE_FILE = "scores.txt";

    /* Saves one result row to file */
    public void saveScore(String player, String game, String timer, String winner) {
        try (FileWriter fw = new FileWriter(SCORE_FILE, true)) {
            fw.write(String.format("%-20s %-20s %-12s %-15s%n", player, game, timer, winner));
        } catch (IOException e) {
            System.out.println("Error saving score: " + e.getMessage());
        }
    }

    /** Displays saved scores as a formatted table */
    public void showScores() {
        System.out.println("\n-----------------------------------------------------------");
        System.out.printf("%-20s %-20s %-12s %-15s%n", "Player", "Game", "Timer", "Winner");
        System.out.println("-----------------------------------------------------------");

        try (BufferedReader br = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line;
            boolean any = false;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                any = true;
            }
            if (!any) System.out.println("No records yet.");
        } catch (IOException e) {
            System.out.println("No saved scores yet.");
        }

        System.out.println("-----------------------------------------------------------\n");
    }
}
