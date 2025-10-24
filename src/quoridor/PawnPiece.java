package quoridor;

import common.pieces.Piece;
import common.player.Player;

/** Pawn for Quoridor player A/B. */
public class PawnPiece extends Piece {
    private final String symbol;

    public PawnPiece(Player owner, int row, int col, String symbol) {
        super(owner, row, col);
        this.symbol = symbol;
    }
    @Override public String toString() { return symbol; }
}
