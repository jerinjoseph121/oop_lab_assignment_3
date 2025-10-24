package quoridor;

import common.tiles.Tile;

/** Wall anchor location (logical). Orientation true=H, false=V. */
public class WallTile extends Tile {
    private final boolean horizontal;
    public WallTile(int row, int col, boolean horizontal) {
        super(row, col);
        this.horizontal = horizontal;
    }
    public boolean isHorizontal() { return horizontal; }
}
