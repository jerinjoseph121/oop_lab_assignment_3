package slidePuzzle;

import java.util.Objects;
/**
 * Represents a tile position (row, col) in the board.
 */

public final class Position {
    public final int row;
    public final int col;

    private Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Position of(int row, int col) {
        return new Position(row, col);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position that = (Position) o;
        return row == that.row && col == that.col;
    }

    @Override public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override public String toString() {
        return "(" + row + "," + col + ")";
    }
}
