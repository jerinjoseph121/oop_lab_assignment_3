package quoridor;

import common.pieces.Piece;
import common.player.Player;

/**
 * Represents a pawn in the Quoridor game.
 * Each player (A/B) has one pawn piece on the board.
 * Extends the generic Piece class to inherit position and owner properties.
 */
public class PawnPiece extends Piece {
    private final String symbol;   // The pawn symbol, e.g., "A" or "B"

    public PawnPiece(Player owner, int row, int col, String symbol) {
        super(owner, row, col);
        this.symbol = symbol;
    }

    @Override
    public String toString() { return symbol; }
}
