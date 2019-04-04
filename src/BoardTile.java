public class BoardTile {
    private int tileID = 0;
    private Boolean hasPawn = false;

    private BoardTile nextTile;
    private BoardTile lastTile;

    public BoardTile() {
    }

    public BoardTile(int tileID, BoardTile lastTile){
        this.tileID = tileID;
        this.lastTile = lastTile;
    }

    public BoardTile(int tileID, BoardTile lastTile, BoardTile nextTile){
        this.tileID = tileID;
        this.lastTile = lastTile;
        this.nextTile = nextTile;
    }

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

