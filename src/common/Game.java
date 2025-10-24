package common;

import common.gameUtils.Score;
import dotsAndBoxes.DotsAndBoxes;
import slidePuzzle.SlidePuzzle;
import quoridor.Quoridor;

import java.util.Scanner;

public abstract class Game {

    public static void launchGame() {
        Score score = new Score();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== CS611 Game Hub ===");
            System.out.println("1) Slide Puzzle");
            System.out.println("2) Dots and Boxes");
            System.out.println("3) Quoridor (NEW)");
            System.out.println("4) View Scores");
            System.out.println("0) Exit");
            System.out.print("Select: ");

            String opt = sc.nextLine().trim();
            switch (opt) {
                case "1": SlidePuzzle.launchSlidePuzzle(sc, score); break;
                case "2": DotsAndBoxes.launchDotsAndBoxes(sc, score); break;
                case "3": new Quoridor().start(); break;
                case "4": score.showScores(); break;
                case "0": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    public abstract void start();
    public abstract void reset();
    public abstract String state();
}
