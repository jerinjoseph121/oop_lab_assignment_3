package quoridor;

import common.Board;
import java.util.*;

/**
 * Quoridor Board (9x9)
 * ✅ Horizontal wall (H): blocks LEFT–RIGHT movement across TWO cells
 * ✅ Vertical wall (V): blocks UP–DOWN movement across TWO cells
 * - Automatically validates paths (no full block)
 * - Rolls back illegal walls
 * - Java 8 compatible (no String.repeat)
 */
public class QuoridorBoard extends Board<String> {

    public static final int N = 9;

    private final PawnPiece[] pawns = new PawnPiece[2]; // A and B
    private final Set<String> blocked = new HashSet<>(); // stores blocked edges
    private final Set<String> hWalls = new HashSet<>();  // horizontal wall anchors
    private final Set<String> vWalls = new HashSet<>();  // vertical wall anchors
    private final int[] wallsLeft = new int[]{10, 10};

    // ANSI colors
    private static final String RESET = "\u001B[0m";
    private static final String RED   = "\u001B[31m";
    private static final String BLUE  = "\u001B[34m";
    private static final String GRAY  = "\u001B[37m";
    private static final String YEL   = "\u001B[33m";

    // board drawing
    private static final int CELL_W = 5;
    private static final String H_EMPTY = "-----";
    private static final String H_WALL  = "=====";

    public QuoridorBoard() {
        pawns[0] = new PawnPiece(null, 1, 5, "A");
        pawns[1] = new PawnPiece(null, 9, 5, "B");
    }

    public int[] getWallsLeft() { return Arrays.copyOf(wallsLeft, 2); }

    @Override
    public boolean isSolved() {
        return pawns[0].getRow() == N || pawns[1].getRow() == 1;
    }

    // ------------------- Move Pawn -------------------
    public boolean movePawn(int pid, int r, int c) {
        if (!inBounds(r, c)) return false;
        int cr = pawns[pid].getRow(), cc = pawns[pid].getCol();
        int dr = Math.abs(r - cr), dc = Math.abs(c - cc);
        if (dr + dc != 1) return false;
        if (isBlocked(cr, cc, r, c)) return false;
        pawns[pid].setPosition(r, c);
        return true;
    }

    // ------------------- Place Wall -------------------
    public boolean placeWall(int pid, char orientation, int r, int c) {
        if (wallsLeft[pid] <= 0) return false;
        if (!(orientation == 'H' || orientation == 'V')) return false;
        if (!(1 <= r && r < N && 1 <= c && c < N)) return false;

        List<String> newEdges = new ArrayList<>();
        List<String> newAnchors = new ArrayList<>();

        if (orientation == 'H') {
            // Horizontal wall blocks LEFT–RIGHT across two cells
            if (hWalls.contains(key(r, c)) || hWalls.contains(key(r, c + 1))) return false;
            if (vWalls.contains(key(r, c)) || vWalls.contains(key(r, c + 1))) return false; // avoid crossing

            newAnchors.add(key(r, c));
            newAnchors.add(key(r, c + 1));

            // Blocks (r - 1, c)-(r, c) and (r - 1, c + 1)-(r, c + 1)
            newEdges.add(edge(r - 1, c, r, c));
            newEdges.add(edge(r - 1, c + 1, r, c + 1));

            hWalls.addAll(newAnchors);
        } else {
            // Vertical wall blocks UP–DOWN across two cells
            if (vWalls.contains(key(r, c)) || vWalls.contains(key(r + 1, c))) return false;
            if (hWalls.contains(key(r, c)) || hWalls.contains(key(r + 1, c))) return false; // avoid crossing

            newAnchors.add(key(r, c));
            newAnchors.add(key(r + 1, c));

            // Blocks (r, c - 1)-(r, c) and (r + 1, c - 1)-(r + 1, c)
            newEdges.add(edge(r, c - 1, r, c));
            newEdges.add(edge(r + 1, c - 1, r + 1, c));

            vWalls.addAll(newAnchors);
        }

        // avoid overlapping same edge twice
        for (String e : newEdges)
            if (blocked.contains(e)) { rollbackAnchors(orientation, newAnchors); return false; }

        blocked.addAll(newEdges);

        // rollback if it causes total block
        if (!pathsRemain()) {
            blocked.removeAll(newEdges);
            rollbackAnchors(orientation, newAnchors);
            return false;
        }

        wallsLeft[pid]--;
        return true;
    }

    private void rollbackAnchors(char ori, List<String> anchors) {
        if (ori == 'H') hWalls.removeAll(anchors);
        else vWalls.removeAll(anchors);
    }

    // ------------------- Path Validation (BFS) -------------------
    private boolean pathsRemain() { return hasPath(0) && hasPath(1); }

    private boolean hasPath(int pid) {
        int goal = (pid == 0) ? N : 1;
        boolean[][] vis = new boolean[N + 1][N + 1];
        ArrayDeque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{pawns[pid].getRow(), pawns[pid].getCol()});
        vis[pawns[pid].getRow()][pawns[pid].getCol()] = true;

        while (!q.isEmpty()) {
            int[] u = q.poll();
            if (u[0] == goal) return true;
            for (int[] v : neighbors(u[0], u[1])) {
                if (!vis[v[0]][v[1]] && !isBlocked(u[0], u[1], v[0], v[1])) {
                    vis[v[0]][v[1]] = true;
                    q.add(v);
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

    // ------------------- Drawing -------------------
    @Override
    public String displayBoard() {
        StringBuilder sb = new StringBuilder();
        int[] w = getWallsLeft();

        sb.append("    ");
        for (int c = 1; c <= N; c++)
            sb.append(center(Integer.toString(c), CELL_W + 1));
        sb.append("\n");

        for (int r = 1; r <= N; r++) {
            // top walls
            sb.append("   ").append(GRAY).append("+").append(RESET);
            for (int c = 1; c <= N; c++) {
                if (hWalls.contains(key(r, c))) sb.append(YEL).append(H_WALL).append(RESET);
                else sb.append(GRAY).append(H_EMPTY).append(RESET);
                sb.append(GRAY).append("+").append(RESET);
            }
            sb.append("\n");

            // row cells
            sb.append(String.format("%2d ", r));
            for (int c = 1; c <= N; c++) {
                if (vWalls.contains(key(r, c))) sb.append(YEL).append("\\").append(RESET);
                else sb.append(GRAY).append("|").append(RESET);

                String cell = " ";
                if (pawns[0].getRow() == r && pawns[0].getCol() == c){
                    if (pawns[0].getRow() == pawns[1].getRow() && pawns[0].getCol() == pawns[1].getCol())
                        cell = RED + "A" + RESET + " & " + BLUE + "B" + RESET;
                    else
                        cell = RED + "A" + RESET;
                }
                else if (pawns[1].getRow() == r && pawns[1].getCol() == c) cell = BLUE + "B" + RESET;

                sb.append(center(cell, CELL_W));
            }
            sb.append(GRAY).append("|").append(RESET).append("\n");
        }

        // bottom border
        sb.append("   ").append(GRAY).append("+").append(RESET);
        for (int c = 1; c <= N; c++)
            sb.append(GRAY).append(H_EMPTY).append("+").append(RESET);
        sb.append("\n");

        sb.append("Walls left → ")
                .append(RED).append("A: ").append(w[0]).append(RESET)
                .append(" | ")
                .append(BLUE).append("B: ").append(w[1]).append(RESET)
                .append("\n");

        return sb.toString();
    }

    // ---- alignment helper ----
    private static String center(String s, int width) {
        int len = s.replaceAll("\\u001B\\[[;\\d]*m", "").length();
        if (len >= width) return s;
        int left = (width - len) / 2, right = width - len - left;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) sb.append(' ');
        sb.append(s);
        for (int i = 0; i < right; i++) sb.append(' ');
        return sb.toString();
    }
}
