package slidePuzzle;

import common.tiles.Tile;

/** A single tile slot in the slide puzzle grid (stores a value). */
public class SlideTile extends Tile {
    private int value; // 0 = blank

    public SlideTile(int row, int col, int value) {
        super(row, col);
        this.value = value;
    }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    @Override
    public String toString() {
        return value == 0 ? "_" : Integer.toString(value);
    }
}
