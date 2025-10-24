package common.player;

/* Basic player model shared by all games. */
public class Player {
    private final String name;
    private final int id;
    private int score;
    private boolean isWinner;

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        this.score = 0;
        this.isWinner = false;
    }

    public String getName() { return name; }
    public int getId() { return id; }
    public int getScore() { return score; }
    public void incrementScore() { score++; }
    public boolean isWinner() { return isWinner; }
    public void makeWinner() { isWinner = true; }

    /** A / B symbol by id=1/2 (used in Quoridor). */
    public String getSymbol() { return id == 1 ? "A" : "B"; }
}
