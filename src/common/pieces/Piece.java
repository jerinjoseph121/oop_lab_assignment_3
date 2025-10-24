package common.pieces;

import common.player.Player;

/** Generic movable/owned element placed on a Tile. */
public abstract class Piece {
    protected final Player owner;
    protected int row;
    protected int col;

    public Piece(Player owner, int row, int col) {
        this.owner = owner;
        this.row = row;
        this.col = col;
    }

    public Player getOwner() { return owner; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public void setPosition(int r, int c) { this.row = r; this.col = c; }

    @Override
    public String toString() {
        return owner == null ? "?" : owner.getSymbol();
    }
}
