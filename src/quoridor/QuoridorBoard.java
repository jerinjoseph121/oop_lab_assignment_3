package quoridor;

import common.Board;
import java.util.*;

/**
 * Quoridor board (9x9).
 * - Uses PawnPiece for players
 * - Uses WallTile (extends Tile) for walls
 * - Each wall covers TWO cells (horizontal or vertical)
 * - Automatically checks if wall blocks all paths using BFS
 * - Rolls back illegal placements
 */
public class QuoridorBoard extends Board<String> {

    public static final int N = 9;

    private final PawnPiece[] pawns = new PawnPiece[2];
    private final Set<String> blocked = new HashSet<>();
    private final List<WallTile> walls = new ArrayList<>();
    private final int[] wallsLeft = {10, 10};

    private static final String RESET = "\u001B[0m";
    private static final String RED   = "\u001B[31m";
    private static final String BLUE  = "\u001B[34m";
    private static final String YEL   = "\u001B[33m";
    private static final String GRAY  = "\u001B[37m";

    private static final String H_WALL = "=====";
    private static final String H_EMPTY = "-----";
    private static final int CELL_W = 5;

    public QuoridorBoard() {
        pawns[0] = new PawnPiece(null, 1, 5, "A");
        pawns[1] = new PawnPiece(null, 9, 5, "B");
    }

    @Override
    public boolean isSolved() {
        return pawns[0].getRow() == N || pawns[1].getRow() == 1;
    }

    //  Move pawn
    public boolean movePawn(int pid, int r, int c) {
        if (!inBounds(r, c)) return false;
        int cr = pawns[pid].getRow(), cc = pawns[pid].getCol();
        int dr = Math.abs(r - cr), dc = Math.abs(c - cc);
        if (dr + dc != 1) return false;
        if (isBlocked(cr, cc, r, c)) return false;
        pawns[pid].setPosition(r, c);
        return true;
    }

    //  Place wall (uses WallTile)
    public boolean placeWall(int pid, char orientation, int r, int c) {
        if (wallsLeft[pid] <= 0) return false;
        if (!(orientation == 'H' || orientation == 'V')) return false;
        // In these two condition the second placed wall will be out of bound
        if (orientation == 'H' && c == N) return false;
        if (orientation == 'V' && r == N) return false;

        // Prevention of out of bound wall placements
        if (!(1 <= r && r <= N && 1 <= c && c <= N)) return false;

        List<String> newEdges = new ArrayList<>();
        List<WallTile> newTiles = new ArrayList<>();

        if (orientation == 'H') {
            for (WallTile wall : walls) {
                if (wall.isHorizontal()) {
                    if (wall.getRow() == r && wall.getCol() == c)
                        return false;
                    if (wall.getRow() == r && wall.getCol() == c + 1)
                        return false;
                }
                else {
                    // All conditions to prevent horizontal wall from touching any vertical walls
                    if (wall.getRow() == r - 1 && wall.getCol() == c)
                        return false;
                    if (wall.getRow() == r && wall.getCol() == c)
                        return false;
                    if (wall.getRow() == r - 1 && wall.getCol() == c + 1)
                        return false;
                    if (wall.getRow() == r && wall.getCol() == c + 1)
                        return false;
                    if (wall.getRow() == r - 1 && wall.getCol() == c + 2)
                        return false;
                    if (wall.getRow() == r && wall.getCol() == c + 2)
                        return false;
                }
            }
            // Horizontal wall blocks up-down between (r-1,c)-(r,c) and (r-1,c+1)-(r,c+1)
            newEdges.add(edge(r - 1, c, r, c));
            newEdges.add(edge(r - 1, c + 1, r, c + 1));
            newTiles.add(new WallTile(r, c, true));
            newTiles.add(new WallTile(r, c + 1, true));
        } else {
            for (WallTile wall : walls) {
                if (wall.isHorizontal()) {
                    // All conditions to prevent vertical wall from touching any horizontal walls
                    if (wall.getRow() == r && wall.getCol() == c)
                        return false;
                    if (wall.getRow() == r && wall.getCol() == c - 1)
                        return false;
                    if (wall.getRow() == r + 1 && wall.getCol() == c)
                        return false;
                    if (wall.getRow() == r + 1 && wall.getCol() == c - 1)
                        return false;
                    if (wall.getRow() == r + 2 && wall.getCol() == c)
                        return false;
                    if (wall.getRow() == r + 2 && wall.getCol() == c - 1)
                        return false;
                }
                else {
                    if (wall.getRow() == r && wall.getCol() == c)
                        return false;
                    if (wall.getRow() == r + 1 && wall.getCol() == c)
                        return false;
                }
            }
            // Vertical wall blocks left-right between (r,c-1)-(r,c) and (r+1,c-1)-(r+1,c)
            newEdges.add(edge(r, c - 1, r, c));
            newEdges.add(edge(r + 1, c - 1, r + 1, c));
            newTiles.add(new WallTile(r, c, false));
            newTiles.add(new WallTile(r + 1, c, false));
        }

        for (String e : newEdges)
            if (blocked.contains(e)) return false;
        blocked.addAll(newEdges);

        if (!pathsRemain()) {
            blocked.removeAll(newEdges);
            return false;
        }

        walls.addAll(newTiles);
        wallsLeft[pid]--;
        return true;
    }

    private boolean pathsRemain() { return hasPath(0) && hasPath(1); }

    private boolean hasPath(int pid) {
        int goal = (pid == 0) ? N : 1;
        boolean[][] vis = new boolean[N + 1][N + 1];
        Queue<int[]> q = new ArrayDeque<>();
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
        List<int[]> res = new ArrayList<>(4);
        if (inBounds(r - 1, c)) res.add(new int[]{r - 1, c});
        if (inBounds(r + 1, c)) res.add(new int[]{r + 1, c});
        if (inBounds(r, c - 1)) res.add(new int[]{r, c - 1});
        if (inBounds(r, c + 1)) res.add(new int[]{r, c + 1});
        return res;
    }

    private boolean inBounds(int r, int c) { return 1 <= r && r <= N && 1 <= c && c <= N; }
    private String edge(int r1, int c1, int r2, int c2) {
        if (r1 > r2 || (r1 == r2 && c1 > c2)) {
            int tr = r1, tc = c1; r1 = r2; c1 = c2; r2 = tr; c2 = tc;
        }
        return r1 + "," + c1 + "-" + r2 + "," + c2;
    }
    private boolean isBlocked(int r1, int c1, int r2, int c2) {
        return blocked.contains(edge(r1, c1, r2, c2));
    }

    //  Display
    @Override
    public String displayBoard() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        for (int c = 1; c <= N; c++)
            sb.append(center(Integer.toString(c), CELL_W + 1));
        sb.append("\n");

        for (int r = 1; r <= N; r++) {
            sb.append("   ").append(GRAY).append("+").append(RESET);
            for (int c = 1; c <= N; c++) {
                boolean h = hasWall(r, c, true);
                sb.append(h ? YEL + H_WALL + RESET : GRAY + H_EMPTY + RESET);
                sb.append(GRAY).append("+").append(RESET);
            }
            sb.append("\n");

            sb.append(String.format("%2d ", r));
            for (int c = 1; c <= N; c++) {
                boolean v = hasWall(r, c, false);
                sb.append(v ? YEL + "/" + RESET : GRAY + "|" + RESET);

                String cell = " ";
                if (pawns[0].getRow() == r && pawns[0].getCol() == c){
                    // When both A & B are in the same cell
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

        sb.append("   ").append(GRAY).append("+").append(RESET);
        for (int c = 1; c <= N; c++)
            sb.append(GRAY).append(H_EMPTY).append("+").append(RESET);
        sb.append("\n");

        sb.append("Walls left → A: ").append(wallsLeft[0])
                .append(" | B: ").append(wallsLeft[1]).append("\n");

        return sb.toString();
    }

    private boolean hasWall(int r, int c, boolean horiz) {
        for (WallTile w : walls)
            if (w.getRow() == r && w.getCol() == c && w.isHorizontal() == horiz)
                return true;
        return false;
    }

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
