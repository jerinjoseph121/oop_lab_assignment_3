package common;

import java.util.AbstractMap;
import java.util.Scanner;

/** Generic abstract board class for all games. */
public abstract class Board<T> {
    public static AbstractMap.SimpleEntry<Integer, Integer> setupBoardDimensions(Scanner sc) {
        System.out.print("Rows: ");
        int rows = sc.nextInt();
        while (rows < 2 || rows > 10) {
            System.out.println("Rows value should be between 2 and 10");
            System.out.print("Rows: ");
            rows = sc.nextInt();
        }
        System.out.print("Cols: ");
        int cols = sc.nextInt();
        while (cols < 2 || cols > 10) {
            System.out.println("Cols value should be between 2 and 10");
            System.out.print("Cols: ");
            cols = sc.nextInt();
        }
        sc.nextLine(); // clear newline
        return new AbstractMap.SimpleEntry<>(rows, cols);
    }

    public abstract boolean isSolved();
    public abstract T displayBoard();
}
