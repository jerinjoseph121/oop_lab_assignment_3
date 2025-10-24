package dotsAndBoxes;

import common.Board;
import common.player.Player;

/** Grid & rules for Dots and Boxes (unchanged logic, now uses DotTile). */
public class DotsAndBoxesBoard extends Board<Void> {
    private final int rows, cols;   // number of boxes
    private final int[][] hEdges;   // 0 free, 1/2 owner id
    private final int[][] vEdges;
    private final String[][] boxes; // completed box labels
    private final String[] playerSymbols;
    private final int[] playerBoxCount;
    private final DotTile[][] dots; // ★ use Tile instances

    public DotsAndBoxesBoard(int rows, int cols, String p1Name, String p2Name) {
        this.rows = rows;
        this.cols = cols;
        this.hEdges = new int[rows + 1][cols];
        this.vEdges = new int[rows][cols + 1];
        this.boxes  = new String[rows][cols];
        this.playerSymbols = new String[3];
        this.playerBoxCount = new int[3];
        this.dots = new DotTile[rows + 1][cols + 1];
        for (int r = 0; r <= rows; r++)
            for (int c = 0; c <= cols; c++)
                dots[r][c] = new DotTile(r, c);

        playerSymbols[1] = p1Name.substring(0, 1).toUpperCase();
        playerSymbols[2] = p2Name.substring(0, 1).toUpperCase();
    }

    @Override
    public Void displayBoard() {
        for (int r = 0; r < rows; r++) {
            // top edge row
            for (int c = 0; c < cols; c++) {
                System.out.print("+");
                System.out.print(hEdgeString(hEdges[r][c]));
            }
            System.out.println("+");
            // vertical edges + box labels
            for (int c = 0; c < cols; c++) {
                System.out.print(vEdgeString(vEdges[r][c]));
                System.out.print(boxes[r][c] == null ? "   " : String.format("%3s", boxes[r][c]));
            }
            System.out.println(vEdgeString(vEdges[r][cols]));
        }
        // bottom line
        for (int c = 0; c < cols; c++) {
            System.out.print("+");
            System.out.print(hEdgeString(hEdges[rows][c]));
        }
        System.out.println("+");
        return null;
    }

    private String hEdgeString(int owner) {
        if (owner == 1) return "===";
        if (owner == 2) return "---";
        return "   ";
    }
    private String vEdgeString(int owner) {
        return (owner == 1 || owner == 2) ? "|" : " ";
    }

    private int[] pointToCoord(int point) {
        int r = (point - 1) / (cols + 1);
        int c = (point - 1) % (cols + 1);
        return new int[]{r, c};
    }

    public boolean claimEdgeByPoints(int p1, int p2, Player p) {
        int[] a = pointToCoord(p1);
        int[] b = pointToCoord(p2);
        int r1 = a[0], c1 = a[1];
        int r2 = b[0], c2 = b[1];
        int id = p.getId();

        if (r1 == r2 && Math.abs(c1 - c2) == 1) {            // horizontal
            int r = r1, c = Math.min(c1, c2);
            if (hEdges[r][c] != 0) throw new IllegalArgumentException("Edge already taken");
            hEdges[r][c] = id;
            return checkBoxesAfterMove(r, c, 'H', p);
        } else if (c1 == c2 && Math.abs(r1 - r2) == 1) {     // vertical
            int r = Math.min(r1, r2), c = c1;
            if (vEdges[r][c] != 0) throw new IllegalArgumentException("Edge already taken");
            vEdges[r][c] = id;
            return checkBoxesAfterMove(r, c, 'V', p);
        } else {
            throw new IllegalArgumentException("Points are not adjacent.");
        }
    }

    private boolean checkBoxesAfterMove(int r, int c, char orient, Player p) {
        boolean scored = false;
        if (orient == 'H') {
            if (r < rows && isBoxCompleted(r, c)) { markBox(r, c, p); scored = true; }
            if (r > 0 && isBoxCompleted(r - 1, c)) { markBox(r - 1, c, p); scored = true; }
        } else {
            if (c < cols && isBoxCompleted(r, c)) { markBox(r, c, p); scored = true; }
            if (c > 0 && isBoxCompleted(r, c - 1)) { markBox(r, c - 1, p); scored = true; }
        }
        return scored;
    }

    private void markBox(int r, int c, Player p) {
        int id = p.getId();
        playerBoxCount[id]++;
        boxes[r][c] = playerSymbols[id] + playerBoxCount[id]; // e.g. A1/B2
        p.incrementScore();
    }

    private boolean isBoxCompleted(int r, int c) {
        return hEdges[r][c] != 0 && hEdges[r + 1][c] != 0 &&
                vEdges[r][c] != 0 && vEdges[r][c + 1] != 0;
    }

    @Override
    public boolean isSolved() {
        for (String[] row : boxes)
            for (String cell : row)
                if (cell == null) return false;
        return true;
    }
}

