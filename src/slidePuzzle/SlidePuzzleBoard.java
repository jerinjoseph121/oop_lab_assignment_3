package slidePuzzle;

import common.Board;
import java.util.*;

/**
 * Internal logic of the Slide Puzzle board.
 * Now uses SlideTile[] to satisfy "Tile/Space" usage without changing behavior.
 */
public class SlidePuzzleBoard extends Board<String> {
    private final int rows;
    private final int cols;
    private final SlideTile[] tiles; // 0-value = blank tile

    public SlidePuzzleBoard(int rows, int cols) {
        if (rows < 2 || cols < 2)
            throw new IllegalArgumentException("Minimum puzzle size is 2x2.");
        this.rows = rows;
        this.cols = cols;
        this.tiles = new SlideTile[rows * cols];
        // initialize solved state
        for (int i = 0; i < tiles.length - 1; i++) tiles[i] = new SlideTile(i / cols, i % cols, i + 1);
        tiles[tiles.length - 1] = new SlideTile((tiles.length - 1)/cols, (tiles.length - 1)%cols, 0);
    }

    /** Copy constructor */
    public SlidePuzzleBoard(SlidePuzzleBoard other) {
        this.rows = other.rows;
        this.cols = other.cols;
        this.tiles = new SlideTile[other.tiles.length];
        for (int i = 0; i < other.tiles.length; i++) {
            SlideTile t = other.tiles[i];
            tiles[i] = new SlideTile(t.getRow(), t.getCol(), t.getValue());
        }
    }

    public int rows() { return rows; }
    public int cols() { return cols; }

    @Override
    public boolean isSolved() {
        for (int i = 0; i < tiles.length - 1; i++)
            if (tiles[i].getValue() != i + 1) return false;
        return tiles[tiles.length - 1].getValue() == 0;
    }

    public Position blankPos() {
        int b = indexOf(0);
        return Position.of(b / cols, b % cols);
    }

    public boolean slide(Direction dir) {
        Position blank = blankPos();
        int nr = blank.row + dir.dRow;
        int nc = blank.col + dir.dCol;
        if (!inBounds(nr, nc)) return false;
        swap(idx(blank.row, blank.col), idx(nr, nc));
        return true;
    }

    public boolean slide(int tileValue) {
        if (!canSlide(tileValue)) return false;
        int t = indexOf(tileValue);
        int b = indexOf(0);
        swap(t, b);
        return true;
    }

    public boolean canSlide(int tileValue) {
        if (tileValue <= 0 || tileValue >= rows * cols) return false;
        int tileIdx = indexOf(tileValue);
        int blankIdx = indexOf(0);
        int tr = tileIdx / cols, tc = tileIdx % cols;
        int br = blankIdx / cols, bc = blankIdx % cols;
        return Math.abs(tr - br) + Math.abs(tc - bc) == 1;
    }

    public void shuffleSolvable(int steps, Random rng) {
        for (int s = 0; s < steps; s++) {
            List<Direction> moves = legalBlankMoves();
            slide(moves.get(rng.nextInt(moves.size())));
        }
    }

    private List<Direction> legalBlankMoves() {
        List<Direction> ds = new ArrayList<>(4);
        Position b = blankPos();
        if (inBounds(b.row - 1, b.col)) ds.add(Direction.UP);
        if (inBounds(b.row + 1, b.col)) ds.add(Direction.DOWN);
        if (inBounds(b.row, b.col - 1)) ds.add(Direction.LEFT);
        if (inBounds(b.row, b.col + 1)) ds.add(Direction.RIGHT);
        return ds;
    }

    private void swap(int i, int j) {
        int vi = tiles[i].getValue();
        tiles[i].setValue(tiles[j].getValue());
        tiles[j].setValue(vi);
    }

    private int idx(int r, int c) { return r * cols + c; }

    private int indexOf(int value) {
        for (int i = 0; i < tiles.length; i++)
            if (tiles[i].getValue() == value) return i;
        return -1;
    }

    private boolean inBounds(int r, int c) { return r >= 0 && r < rows && c >= 0 && c < cols; }

    @Override
    public String displayBoard() {
        StringBuilder sb = new StringBuilder();
        int width = String.valueOf(rows * cols - 1).length() + 1;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int v = tiles[idx(r, c)].getValue();
                if (v == 0) sb.append(String.format("%" + width + "s", "_"));
                else       sb.append(String.format("%" + width + "d", v));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
