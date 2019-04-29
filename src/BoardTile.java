/**
 * The tiles of the Sorry Board
 * Basically a linked list.
 * Has an Int ID, as well as a boolean indicating
 * if there is a pawn on the tile.
 */
public class BoardTile {

    private int tileID = 0;
    private Boolean hasPawn = false;

    private BoardTile nextTile;
    private BoardTile lastTile;

    // default constructor
    public BoardTile() {
    }

    // constructor with the ID and last tile
    public BoardTile(int tileID, BoardTile lastTile){
        this.tileID = tileID;
        this.lastTile = lastTile;
    }

    // constructor with the ID, last tile, and next tile
    public BoardTile(int tileID, BoardTile lastTile, BoardTile nextTile){
        this.tileID = tileID;
        this.lastTile = lastTile;
        this.nextTile = nextTile;
    }

    // getters and setters for every parameter
    public void setNextTile(BoardTile nextTile) {
        this.nextTile = nextTile;
    }

    public void setLastTile(BoardTile lastTile) {
        this.lastTile = lastTile;
    }

    public void setTileID(int tileID) {
        this.tileID = tileID;
    }

    public void addPawn() {
        hasPawn = true;
    }

    public void removePawn() {
        hasPawn = false;
    }

    public Boolean getHasPawn() {
        return hasPawn;
    }

    public BoardTile getNextTile() {
        return nextTile;
    }

    public BoardTile getLastTile() {
        return lastTile;
    }

    public int getTileID() {
        return tileID;
    }
}

