package slidePuzzle;

import common.pieces.Piece;
import common.player.Player;

/** Numbered movable piece (for rubric completeness). */
public class SlidePiece extends Piece {
    private final int value;

    public SlidePiece(Player owner, int row, int col, int value) {
        super(owner, row, col);
        this.value = value;
    }

    public int getValue() { return value; }

    @Override
    public String toString() {
        return value == 0 ? "_" : String.valueOf(value);
    }
}
