import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;

/**
 * A complete Sorry board for one player
 * Multiple instances needed for more than one player
 * Communications between boards facilitated through the
 * rotation of the board.
 */
public class PlayerBoard {
    // ints storing the number of pawns on start and home
    private int homePawns = 0;
    private int startPawns = 4;

    // the array of tiles to create the board
    private BoardTile[] boardTiles;

    // int storing the rotation of the board
    // 0, 1, 2 and 3 corresponds to having the start/home circles
    // on the top, right, bottom, and left for the player/board
    private int rotation = 0;

    // int storing the color of the player
    private Color playerColor = Color.RED;

    /**
     * Default constructor
     * Creates the 65 tiles for the Sorry board,
     * as well as a tile for the home space.
     * Orders them in an array, and links them to
     * the next and last tiles moved to.
     */
    public PlayerBoard() {
        this.boardTiles = new BoardTile[66];

        boardTiles[0] = new BoardTile();
        boardTiles[0].setTileID(0);

        for (int i = 1; i < 66; i++) {
            boardTiles[i] = new BoardTile(i, boardTiles[i - 1]);
        }

        for (int i = 0; i < 65; i++) {
            boardTiles[i].setNextTile(boardTiles[i + 1]);
        }

        boardTiles[0].setLastTile(boardTiles[59]);

        boardTiles[0].addPawn();
    }

    /**
     * Constructs the board but also sets the
     * rotation and color.
     */
    public PlayerBoard(int rotation, Color playerColor) {
        this.rotation = rotation;
        this.boardTiles = new BoardTile[66];
        this.playerColor = playerColor;

        boardTiles[0] = new BoardTile();
        boardTiles[0].setTileID(0);

        for (int i = 1; i < 66; i++) {
            boardTiles[i] = new BoardTile(i, boardTiles[i - 1]);
        }

        for (int i = 0; i < 65; i++) {
            boardTiles[i].setNextTile(boardTiles[i + 1]);
        }

        boardTiles[0].setLastTile(boardTiles[59]);
   }

    /**
     * A function that returns a Javafx Group that contains
     * all of the pawns displayed, in the rotation of the
     * player/board
     * @return playerDisplay
     */
    public Group displayPawns() {
        Group playerDisplay = new Group();

        // adds pawns on board
        for (int i = 0; i < 65; i++) {
            if (boardTiles[i].getHasPawn()) {
                int[] coords = getLocation(i);
                int x = coords[0];
                int y = coords[1];
                Circle pawn = new Circle(x, y, 15, playerColor);
                pawn.setStroke(Color.BLACK);
                pawn.setStrokeWidth(2.0);

                playerDisplay.getChildren().add(pawn);
            }
        }

        // adds pawns on home and start
        int[] coord1 = new int[]{305, 130};
        int x1 = coordinateRotation(coord1, 0, rotation)[0];
        int y1 = coordinateRotation(coord1, 0, rotation)[1];
        Circle pawn1 = new Circle(x1, y1, 15, playerColor);
        pawn1.setStroke(Color.BLACK);
        pawn1.setStrokeWidth(2.0);

        int[] coord2 = new int[]{345, 130};
        int x2 = coordinateRotation(coord2, 0, rotation)[0];
        int y2 = coordinateRotation(coord2, 0, rotation)[1];
        Circle pawn2 = new Circle(x2, y2, 15, playerColor);
        pawn2.setStroke(Color.BLACK);
        pawn2.setStrokeWidth(2.0);

        int[] coord3 = new int[]{305, 170};
        int x3 = coordinateRotation(coord3, 0, rotation)[0];
        int y3 = coordinateRotation(coord3, 0, rotation)[1];
        Circle pawn3 = new Circle(x3, y3, 15, playerColor);
        pawn3.setStroke(Color.BLACK);
        pawn3.setStrokeWidth(2.0);

        int[] coord4 = new int[]{345, 170};
        int x4 = coordinateRotation(coord4, 0, rotation)[0];
        int y4 = coordinateRotation(coord4, 0, rotation)[1];
        Circle pawn4 = new Circle(x4, y4, 15, playerColor);
        pawn4.setStroke(Color.BLACK);
        pawn4.setStrokeWidth(2.0);

        switch(startPawns) {
            case 1:
                playerDisplay.getChildren().add(pawn1);
                break;
            case 2:
                playerDisplay.getChildren().add(pawn1);
                playerDisplay.getChildren().add(pawn2);
                break;
            case 3:
                playerDisplay.getChildren().add(pawn1);
                playerDisplay.getChildren().add(pawn2);
                playerDisplay.getChildren().add(pawn3);
                break;
            case 4:
                playerDisplay.getChildren().add(pawn1);
                playerDisplay.getChildren().add(pawn2);
                playerDisplay.getChildren().add(pawn3);
                playerDisplay.getChildren().add(pawn4);
                break;
        }

        int[] coord5 = new int[]{205, 380};
        int x5 = coordinateRotation(coord5, 0, rotation)[0];
        int y5 = coordinateRotation(coord5, 0, rotation)[1];
        Circle pawn5 = new Circle(x5, y5, 15, playerColor);
        pawn5.setStroke(Color.BLACK);
        pawn5.setStrokeWidth(2.0);

        int[] coord6 = new int[]{205, 420};
        int x6 = coordinateRotation(coord6, 0, rotation)[0];
        int y6 = coordinateRotation(coord6, 0, rotation)[1];
        Circle pawn6 = new Circle(x6, y6, 15, playerColor);
        pawn6.setStroke(Color.BLACK);
        pawn6.setStrokeWidth(2.0);

        int[] coord7 = new int[]{245, 380};
        int x7 = coordinateRotation(coord7, 0, rotation)[0];
        int y7 = coordinateRotation(coord7, 0, rotation)[1];
        Circle pawn7 = new Circle(x7, y7, 15, playerColor);
        pawn7.setStroke(Color.BLACK);
        pawn7.setStrokeWidth(2.0);

        int[] coord8 = new int[]{245, 420};
        int x8 = coordinateRotation(coord8, 0, rotation)[0];
        int y8 = coordinateRotation(coord8, 0, rotation)[1];
        Circle pawn8 = new Circle(x8, y8, 15, playerColor);
        pawn8.setStroke(Color.BLACK);
        pawn8.setStrokeWidth(2.0);

        switch(homePawns) {
            case 1:
                playerDisplay.getChildren().add(pawn5);
                break;
            case 2:
                playerDisplay.getChildren().add(pawn5);
                playerDisplay.getChildren().add(pawn6);
                break;
            case 3:
                playerDisplay.getChildren().add(pawn5);
                playerDisplay.getChildren().add(pawn6);
                playerDisplay.getChildren().add(pawn7);
                break;
            case 4:
                playerDisplay.getChildren().add(pawn5);
                playerDisplay.getChildren().add(pawn6);
                playerDisplay.getChildren().add(pawn7);
                playerDisplay.getChildren().add(pawn8);
                break;
        }

        return playerDisplay;
    }

    /**
     * Checks if there is a pawn at the tileID,
     * returns true/false
     * @param tileID
     */
    public Boolean hasPawnAt(int tileID) {
        return boardTiles[tileID].getHasPawn();
    }

    /**
     * Checks if there is a pawn at the tileID when inquired by
     * another board. Takes the tileID as well as the rotation
     * of the board the inquiry came from.
     * @param tileID, initRot
     */
    public Boolean hasPawnAt(int tileID, int initRot) {
        return boardTiles[tileIDRotation(tileID, initRot, rotation)].getHasPawn();
    }

    /**
     * Checks if the pawn at a tile ID can move a certain number of spaces.
     * Checks to avoid bumping own tile and moving off the board.
     * @param tileID
     * @param moves
     * @return
     */
    public Boolean canMovePawn(int tileID, int moves) {
        if (tileID + moves > 65 || !boardTiles[tileID].getHasPawn()) {
            return false;
        } else {
            BoardTile activeTile = boardTiles[tileID];

            if (moves > 0) {
                for (int i = 0; i < moves; i++) {
                    activeTile = activeTile.getNextTile();
                }
            } else {
                for (int i = 0; i > moves; i--) {
                    activeTile = activeTile.getLastTile();
                }
            }

            return !activeTile.getHasPawn();
        }
    }

    /**
     * Moves the pawn at a tile ID a certain number of spaces.
     * Also returns the tile ID of the tile the pawn lands on.
     * @param tileID
     * @param moves
     * @return
     */
    public int movePawn(int tileID, int moves) {
        BoardTile activeTile = this.boardTiles[tileID];

        if (activeTile.getHasPawn()) {
            activeTile.removePawn();

            if (moves > 0) {
                for (int i = 0; i < moves; i++) {
                    activeTile = activeTile.getNextTile();
                }
            } else {
                for (int i = 0; i > moves; i--) {
                    activeTile = activeTile.getLastTile();
                }
            }

            activeTile.addPawn();
            return activeTile.getTileID();
        }
        return -1;
    }

    /**
     * Moves the pawn at a tile ID for a player with different rotation
     * a certain number of spaces
     * Also returns the tile ID of the tile the pawn lands on.
     * @param tileID
     * @param moves
     * @return
     */
    public int movePawn(int tileID, int moves, int initRot) {
        int rotatedID = tileIDRotation(tileID, initRot , rotation);
        BoardTile activeTile = this.boardTiles[rotatedID];

        if (activeTile.getHasPawn()) {
            activeTile.removePawn();

            if (moves > 0) {
                for (int i = 0; i < moves; i++) {
                    activeTile = activeTile.getNextTile();
                }
            } else {
                for (int i = 0; i > moves; i--) {
                    activeTile = activeTile.getLastTile();
                }
            }

            activeTile.addPawn();
            return activeTile.getTileID();
        }
        return -1;
    }

    /**
     * Checks if any pawn is on a tile that is the start of a slide
     * bumps all of your own slides on the slide.
     * returns the tile ID of the tiles that was slid over.
     * @return
     */
    public int[] checkSlide() {
        for (int i = 0; i < 64; i++) {
            if (boardTiles[i].getHasPawn()){
                if (i == 21 || i == 36 || i == 51) {
                    int[] bumpedTiles = new int[]{i, i+1, i+2, i+3, i+4};
                    this.bump(Arrays.copyOfRange(bumpedTiles, 1,5), rotation);
                    this.movePawn(i, 4);
                    return bumpedTiles;
                } else if (i == 13 || i == 28 || i == 43) {
                    int[] bumpedTiles = new int[]{i, i+1, i+2, i+3};
                    this.bump(Arrays.copyOfRange(bumpedTiles, 1,4), rotation);
                    this.movePawn(i, 3);
                    return bumpedTiles;
                }
            }
        }
        return new int[0];
    }

    // Moves pawn from start to the tile in front of start
    public void moveFromStart() {
        boardTiles[1].addPawn();
        startPawns--;
    }

    // Moves a pawn from start to a certain tile ID
    public void movePawnTo(int tileID) {
        boardTiles[tileID].addPawn();
        startPawns--;
    }

    /**
     * Checks if there is a pawn on the tile for home,
     * removes it and increment the homePawn number
     */
    public void moveToHome() {
        if (boardTiles[65].getHasPawn()) {
            boardTiles[65].removePawn();
            homePawns++;
        }
    }

    /**
     * Returns if there are 4 pawns in Home
     */
    public boolean hasWon() {
        if (homePawns == 4) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes the pawn on tileID, add it back to startPawns,
     * taking the rotation of the board
     * @param tileID
     * @param initRot
     */
    public void bump(int tileID, int initRot) {
        int rotatedID = tileIDRotation(tileID, initRot, rotation);
        if (boardTiles[rotatedID].getHasPawn()) {
            boardTiles[rotatedID].removePawn();

            startPawns++;
        }
    }

    /**
     * Removes the pawn on multiple  tileIDs of a different player
     * add it back to startPawns
     */
    public void bump(int[] tileIDs, int initRot) {
        int[] rotatedIDs = tileIDRotation(tileIDs, initRot, rotation);

        for (int rotatedID : rotatedIDs) {
            if (boardTiles[rotatedID].getHasPawn()) {
                boardTiles[rotatedID].removePawn();

                startPawns++;
            }
        }
    }

    /**
     * returns the x and y coordinates of the CENTER of the tiles ordered from 0 to 64
     */
    private int[] getLocation(int tileID) {
        int[] coordinates = new int[2];

        int[] xCoords = new int[]{275, 325, 375, 425, 475, 525, 575, 625, 675, 725, 775, 825, 875, 875, 875,
                875, 875, 875, 875, 875, 875, 875, 875, 875, 875, 875, 875, 875, 825, 775, 725, 675, 625,
                575, 525, 475, 425, 375, 325, 275, 225, 175, 125, 125, 125, 125, 125, 125, 125, 125, 125,
                125, 125, 125, 125, 125, 125, 125, 175, 225, 225, 225, 225, 225, 225};

        int[] yCoords = new int[]{75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 75, 125, 175, 225, 275, 325,
                375, 425, 475, 525, 575, 625, 675, 725, 775, 825, 825, 825, 825, 825, 825, 825, 825, 825,
                825, 825, 825, 825, 825, 825, 825, 775, 725, 675, 625, 575, 525, 475, 425, 375, 325, 275, 225,
                175, 125, 75, 75, 75, 125, 175, 225, 275, 325};


        coordinates[0] = xCoords[tileID];
        coordinates[1] = yCoords[tileID];

        int[] rotatedCoordinates = coordinateRotation(coordinates, 0, rotation);

        return rotatedCoordinates;
    }

    /**
     * Crucial function.
     * All coords are programed for a player/Board with the star and home on top of the Sorry board
     * However, this function allows us to rotate the around the center of the board.
     * Thus allowing us to get the coords of the tile, etc. of a different rotation
     * by calling this function.
     * Takes initial coordinates, and rotates it from the initial rotation to the final rotation.
     * The rotations are 0 for start/home pointing up, 1 for right, 2 for down, and 3 for left.
     * Returns the coordinates rotated around the center.
     * @param initCoords
     * @param initRot
     * @param finalRot
     * @return
     */
    public int[] coordinateRotation(int[] initCoords, int initRot, int finalRot) {
        int initX = initCoords[0];
        int initY = initCoords[1];

        int rotation = (finalRot - initRot + 4)%4;

        initX -= 500;
        initY -= 450;

        int finalX = 0;
        int finalY = 0;

        if (rotation == 0) {
            finalX = initX;
            finalY = initY;
        } else if (rotation == 1) {
            finalX = -initY;
            finalY = initX;
        } else if (rotation == 2) {
            finalX = -initX;
            finalY = -initY;
        } else if (rotation == 3) {
            finalX = initY;
            finalY = -initX;
        }

        finalX += 500;
        finalY += 450;

        return new int[]{finalX, finalY};
    }

    /**
     * Crucial function.
     * For the 60 shared tiles around the outside of the board.
     * Allows us to, knowing the rotation of two players
     * and the tile ID for the first player,
     * to know what the tile ID is for the second player
     *
     * @param initTileID
     * @param initRot
     * @param finalRot
     * @return
     */
    private int tileIDRotation(int initTileID, int initRot, int finalRot) {
        int[] initTileIDs = new int[]{initTileID};
        int[] rotatedIDs = tileIDRotation(initTileIDs, initRot, finalRot);
        return rotatedIDs[0];
    }

    /**
     * tileIDRotation for multiple tiles
     * @param initTileID
     * @param initRot
     * @param finalRot
     * @return
     */
    private int[] tileIDRotation(int[] initTileIDs, int initRot, int finalRot) {
        int rotation = (finalRot - initRot + 4)%4;

        int finalTileIDs[] = new int[initTileIDs.length];

        for (int i = 0; i < initTileIDs.length; i++) {
            finalTileIDs[i] = (initTileIDs[i] + 15 * (4 - rotation)) % 60;
        }

        return finalTileIDs;
    }

    /**
     * Gets the tile id for a click on the screen.
     * @param x
     * @param y
     * @return
     */
    public int getTileID(int x, int y){

        int[] coords = new int[]{x, y};

        int[] rotatedCoords = coordinateRotation(coords, rotation, 0);

        x = rotatedCoords[0];
        y = rotatedCoords[1];

        int subtract;

        subtract = x%25;
        x = x - subtract;
        subtract = y%25;
        y = y - subtract;



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

        return -1;
    }

    // Get the number of pawns on Start
    public int getStartPawns() {
        return startPawns;
    }

    // Get the number of pawns on Home
    public int getHomePawns(){return homePawns; }

    // Increment the number of pawns on Start
    // Not used normally, only for patch
    public void addStartPawns() {
        startPawns++;
    }

    // Gets the rotation of the board.
    public int getRotation(){
        return rotation;
    }
}
