import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayerBoard {
    private int homePawns = 0;
    private int startPawns = 4;

    private BoardTile[] boardTiles;

    private BoardTile startTile;
    private BoardTile endTile;


    public PlayerBoard() {
        this.boardTiles = new BoardTile[65];

        boardTiles[0] = new BoardTile();
        boardTiles[0].setTileID(0);

        for (int i = 1; i < 65; i++) {
            boardTiles[i] = new BoardTile(i, boardTiles[i - 1]);
        }

        this.startTile = boardTiles[0];
        this.endTile = boardTiles[64];

        for (int i = 0; i < 64; i++) {
            boardTiles[i].setNextTile(boardTiles[i + 1]);
        }

        boardTiles[0].setLastTile(boardTiles[59]);

        boardTiles[0].addPawn();
    }

    public Group displayPawns() {
        Group playerDisplay = new Group();

        for (int i = 0; i < 65; i++) {
            if (boardTiles[i].getHasPawn()) {
                int[] coords = getLocation(i);
                int x = coords[0];
                int y = coords[1];
                Circle pawn = new Circle(x, y, 15, Color.RED);

                playerDisplay.getChildren().add(pawn);
            }
        }

        return playerDisplay;
    }

    // checks if there is a pawn at the tile
    public Boolean hasPawnAt(int tileID) {
        return boardTiles[tileID].getHasPawn();
    }

    // Takes the id of the tile and if there's a pawn on it move it a certain amount foward
    // cannot currently move backwards, but will not move tile off board(that will actually cause a crash)
    public void movePawn(int tileID, int moves) {
        if (boardTiles[tileID].getHasPawn() && tileID + moves < 65) {

            BoardTile activeTile = this.startTile;

            for (int i = 0; i < tileID; i++) {
                activeTile = activeTile.getNextTile();
            }

            activeTile.removePawn();

            for (int i = 0; i < moves; i++) {
                activeTile = activeTile.getNextTile();
            }

            activeTile.addPawn();

        }
    }

    public void checkSlide() {
        int[] longSlides = new int[]{21, 36, 51};
        int[] shortSlides = new int[]{13, 28, 43};

        for (int i = 0; i < 64; i++) {
            if (boardTiles[i].getHasPawn()) {

            }
        }
    }

    // this includes the x and y coordinates of the CENTER of the tiles ordered from 0 to 64
    // remember that if the pane is square it will start from the top left of the pane
    // you would want to subtract 25 from all values in these two lists to get the coords of the
    // left top corner
    private int[] getLocation(int i) {
        int[] coordinates = new int[2];

        int[] xCoords = new int[]{275, 325, 375, 425, 475, 525, 575, 625, 675, 725, 775, 825, 875, 875, 875,
                875, 875, 875, 875, 875, 875, 875, 875, 875, 875, 875, 875, 875, 825, 775, 725, 675, 625,
                575, 525, 475, 425, 375, 325, 275, 225, 175, 125, 125, 125, 125, 125, 125, 125, 125, 125,
                125, 125, 125, 125, 125, 125, 125, 175, 225, 225, 225, 225, 225, 225};

        int[] yCoords = new int[]{75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 125, 175, 225, 275, 325,
                375, 425, 475, 525, 575, 625, 675, 725, 775, 825, 825, 825, 825, 825, 825, 825, 825, 825,
                825, 825, 825, 825, 825, 825, 825, 775, 725, 675, 625, 575, 525, 475, 425, 375, 325, 275, 225,
                175, 125, 75, 75, 75, 125, 175, 225, 275, 325};


        coordinates[0] = xCoords[i];
        coordinates[1] = yCoords[i];

        return coordinates;
    }
    public int getTileID(int x, int y){
        double count = 0;
        if (y == 50 || y == 75) {
            if (x >= 250 && x <= 875) {
                for (int i = 250; i <= 875; i += 25) {
                    if (x == i){
                        return (int)count;
                    }
                    count += .5;
                }
            }
        }

        count = 13;
        if (x == 850 || x == 875) {
            for (int i = 100; i <= 775; i += 25) {
                if (y == i){
                    return (int)count;
                }
                count += .5;
            }
        }


        count = 27;
        if (y == 800 || y == 825) {
            for (int i = 875; i >= 100; i -= 25) {
                if (x == i){
                    return (int)count;
                }
                count += .5;
            }
        }

        count = 43;
        if (x == 100 || x == 125) {
            for (int i = 775; i >= 50; i -= 25) {
                if (y == i){
                    return (int)count;
                }
                count += .5;
            }
        }

        if ((x == 150 || x == 175) && (y == 50 || y == 75)){
            return 58;
        }

        count = 59;
        if (x == 200 || x == 225) {
            for (int i = 50; i <= 325; i += 25) {
                if (y == i){
                    return (int)count;
                }
                count += .5;
            }
        }

        return 0;
    }

}
