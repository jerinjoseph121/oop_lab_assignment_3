package quoridor;

import common.Board;
import java.util.*;

/**
 * Quoridor board (9x9) – Java 8 compatible, column numbers centered.
 * Uses PawnPiece for pawns and WallTile for wall anchors.
 */
public class QuoridorBoard extends Board<String> {

    public static final int N = 9;

    private final PawnPiece[] pawns = new PawnPiece[2]; // [0]=A at (1,5), [1]=B at (9,5)
    private final Set<String> blocked = new HashSet<>(); // blocked edges
    private final Set<String> hWalls = new HashSet<>();
    private final Set<String> vWalls = new HashSet<>();
    private final int[] wallsLeft = new int[]{10, 10};

    // ANSI (safe in most terminals)
    private static final String RESET = "\u001B[0m";
    private static final String RED   = "\u001B[31m";
    private static final String BLUE  = "\u001B[34m";
    private static final String GRAY  = "\u001B[37m";

    private static final int CELL_W = 5;
    private static final String H_EMPTY = "-----";
    private static final String H_WALL  = "=====";

    public QuoridorBoard() {
        // default names/symbols supplied by Quoridor.java when printing
        pawns[0] = new PawnPiece(null, 1, 5, "A");
        pawns[1] = new PawnPiece(null, 9, 5, "B");
    }

    public int[] getWallsLeft() { return Arrays.copyOf(wallsLeft, 2); }

    @Override
    public boolean isSolved() {
        return pawns[0].getRow() == N || pawns[1].getRow() == 1;
    }

    // ---- gameplay API -------------------------------------------------------

    /** one-step move; blocked by walls/edges */
    public boolean movePawn(int pid, int targetR, int targetC) {
        if (!inBounds(targetR, targetC)) return false;
        int cr = pawns[pid].getRow();
        int cc = pawns[pid].getCol();
        int dr = Math.abs(targetR - cr);
        int dc = Math.abs(targetC - cc);
        if (dr + dc != 1) return false;               // exactly 1 step
        if (dr == 1 && isBlocked(cr, cc, targetR, targetC)) return false;
        if (dc == 1 && isBlocked(cr, cc, targetR, targetC)) return false;

        pawns[pid].setPosition(targetR, targetC);
        return true;
    }

    /** place a 2-cell wall (H or V). must keep both players with a path */
    public boolean placeWall(int pid, char orientation, int r, int c) {
        if (wallsLeft[pid] <= 0) return false;
        if (orientation != 'H' && orientation != 'V') return false;
        if (!(1 <= r && r < N && 1 <= c && c < N)) return false;

        if (orientation == 'H') {
            String key = key(r, c);
            if (hWalls.contains(key)) return false;
            if (vWalls.contains(key) || vWalls.contains(key(r, c + 1))) return false;

            List<String> newEdges = hEdgesAt(r, c);
            if (wouldBlock(newEdges)) return false;
            for (String e : newEdges) blocked.add(e);
            hWalls.add(key);
            if (!pathsRemain()) { // rollback if blocks someone's path
                for (String e : newEdges) blocked.remove(e);
                hWalls.remove(key);
                return false;
            }
            wallsLeft[pid]--;
            return true;
        } else {
            String key = key(r, c);
            if (vWalls.contains(key)) return false;
            if (hWalls.contains(key) || hWalls.contains(key(r + 1, c))) return false;

            List<String> newEdges = vEdgesAt(r, c);
            if (wouldBlock(newEdges)) return false;
            for (String e : newEdges) blocked.add(e);
            vWalls.add(key);
            if (!pathsRemain()) {
                for (String e : newEdges) blocked.remove(e);
                vWalls.remove(key);
                return false;
            }
            wallsLeft[pid]--;
            return true;
        }
    }

    // ---- path / geometry helpers -------------------------------------------

    private boolean wouldBlock(List<String> edges) {
        for (String e : edges) if (blocked.contains(e)) return true;
        return false;
    }

    private List<String> hEdgesAt(int r, int c) {
        List<String> res = new ArrayList<>(2);
        res.add(edge(r, c, r + 1, c));
        res.add(edge(r, c + 1, r + 1, c + 1));
        return res;
    }
    private List<String> vEdgesAt(int r, int c) {
        List<String> res = new ArrayList<>(2);
        res.add(edge(r, c, r, c + 1));
        res.add(edge(r + 1, c, r + 1, c + 1));
        return res;
    }

    private boolean pathsRemain() {
        return hasPathToGoal(0) && hasPathToGoal(1);
    }

    private boolean hasPathToGoal(int pid) {
        int targetRow = (pid == 0) ? N : 1;
        int sr = pawns[pid].getRow(), sc = pawns[pid].getCol();
        boolean[][] vis = new boolean[N + 1][N + 1];
        ArrayDeque<int[]> dq = new ArrayDeque<>();
        dq.add(new int[]{sr, sc});
        vis[sr][sc] = true;
        while (!dq.isEmpty()) {
            int[] u = dq.poll();
            if (u[0] == targetRow) return true;
            for (int[] v : neighbors(u[0], u[1])) {
                if (!vis[v[0]][v[1]] && !isBlocked(u[0], u[1], v[0], v[1])) {
                    vis[v[0]][v[1]] = true;
                    dq.add(v);
                }
            }
        }
        return false;
    }

    private List<int[]> neighbors(int r, int c) {
        List<int[]> out = new ArrayList<>(4);
        if (inBounds(r - 1, c)) out.add(new int[]{r - 1, c});
        if (inBounds(r + 1, c)) out.add(new int[]{r + 1, c});
        if (inBounds(r, c - 1)) out.add(new int[]{r, c - 1});
        if (inBounds(r, c + 1)) out.add(new int[]{r, c + 1});
        return out;
    }

    private boolean inBounds(int r, int c) { return 1 <= r && r <= N && 1 <= c && c <= N; }
    private String key(int r, int c) { return r + "," + c; }

    private String edge(int r1, int c1, int r2, int c2) {
        if (r1 > r2 || (r1 == r2 && c1 > c2)) {
            int tr = r1, tc = c1; r1 = r2; c1 = c2; r2 = tr; c2 = tc;
        }
        return r1 + "," + c1 + "-" + r2 + "," + c2;
    }
    private boolean isBlocked(int r1, int c1, int r2, int c2) {
        return blocked.contains(edge(r1, c1, r2, c2));
    }

    // ---- drawing ------------------------------------------------------------

    @Override
    public String displayBoard() {
        StringBuilder sb = new StringBuilder();
        int[] w = getWallsLeft();

        // column numbers centered
        sb.append("    ");
        for (int c = 1; c <= N; c++) sb.append(center(Integer.toString(c), CELL_W + 1));
        sb.append("\n");

        for (int r = 1; r <= N; r++) {
            // horizontal line with walls
            sb.append("   ").append(GRAY).append("+").append(RESET);
            for (int c = 1; c <= N; c++) {
                boolean aSeg = hWalls.contains(key(r, c));
                if (aSeg) sb.append(H_WALL);
                else sb.append(H_EMPTY);
                sb.append(GRAY).append("+").append(RESET);
            }
            sb.append("\n");

            // row label + cells with vertical walls
            sb.append(String.format("%2d ", r));
            for (int c = 1; c <= N; c++) {
                boolean vSeg = vWalls.contains(key(r, c));
                if (vSeg) sb.append("|"); else sb.append("|"); // same glyph; color留白

                String cell = " ";
                if (pawns[0].getRow() == r && pawns[0].getCol() == c) cell = RED + "A" + RESET;
                if (pawns[1].getRow() == r && pawns[1].getCol() == c) cell = BLUE + "B" + RESET;
                sb.append(center(cell, CELL_W));
            }
            sb.append("|").append("\n");
        }

        // bottom line
        sb.append("   ").append(GRAY).append("+").append(RESET);
        for (int c = 1; c <= N; c++) sb.append(H_EMPTY).append(GRAY).append("+").append(RESET);
        sb.append("\n");

        sb.append("Walls left \u2192 ")
                .append("A: ").append(w[0]).append(" | ")
                .append("B: ").append(w[1]).append("\n");

        return sb.toString();
    }

    // alignment helpers (no String.repeat, Java 8 OK)
    private static String center(String s, int width) {
        int len = s.replaceAll("\\u001B\\[[;\\d]*m", "").length();
        if (len >= width) return s;
        int left = (width - len) / 2;
        int right = width - len - left;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) sb.append(' ');
        sb.append(s);
        for (int i = 0; i < right; i++) sb.append(' ');
        return sb.toString();
    }
}
