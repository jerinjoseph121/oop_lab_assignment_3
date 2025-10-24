package dotsAndBoxes;

import common.pieces.Piece;
import common.player.Player;

/** Claimed edge segment ('H' or 'V') owned by a player. */
public class LinePiece extends Piece {
    private final char type; // 'H' or 'V'

    public LinePiece(Player owner, int row, int col, char type) {
        super(owner, row, col);
        this.type = type;
    }

    public char getType() { return type; }

    @Override
    public String toString() {
        return type == 'H' ? "===" : "|";
    }
}
