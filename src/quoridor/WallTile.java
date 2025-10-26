package quoridor;

import common.tiles.Tile;

/**
 * Represents a wall segment (a part of a two-cell wall).
 * Each wall occupies two adjacent grid spaces either horizontally or vertically.
 * Extends the Tile class to maintain consistency with the shared board architecture.
 */
public class WallTile extends Tile {
    private final boolean horizontal;  // true = horizontal wall, false = vertical wall

    public WallTile(int row, int col, boolean horizontal) {
        super(row, col);
        this.horizontal = horizontal;
    }
    /** Returns true if this wall tile is horizontal. */
    public boolean isHorizontal() { return horizontal; }
    /** Returns a short text label ("H" or "V") for debugging or text visualization. */
    @Override
    public String toString() {
        return horizontal ? "H" : "V";
    }
}
