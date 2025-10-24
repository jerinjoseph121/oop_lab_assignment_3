package common.tiles;

import common.pieces.Piece;

/** Represents a logical cell/space on a game board. */
public class Tile {
    private final int row;
    private final int col;
    private Piece occupant; // may be null

    public Tile(int row, int col) {
        this.row = row;
        this.col = col;
        this.occupant = null;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public boolean isEmpty() { return occupant == null; }
    public Piece getOccupant() { return occupant; }
    public void setOccupant(Piece p) { this.occupant = p; }

    @Override
    public String toString() {
        return occupant == null ? "." : occupant.toString();
    }
}
