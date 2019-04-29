import java.util.Arrays;

/**
 * A class that allows a computer to play Sorry
 */
public class ComputerPlayer {
    // ints storing the rotation, pawns on start and home,
    // and the tile ID for the first and last pawns.
    int playerRotation = 0;
    int startPawns = 4;
    int homePawns = 0;
    int lastPawnID = 0;
    int firstPawnID = 0;

    // Int arrays representing the location of pawns on the board
    // the index corresponds to the tile ID.
    // 0 for no pawn, 1 for pawn.
    // separated into two arrays for the computer players pawns and
    // the pawns for all other players
    int[] myPawns = new int[65];
    int[] otherPawns = new int[65];

    // int storing the number of players
    int players = 2;

    // default constructor
    public ComputerPlayer(){}

    // constructor with rotation
    public ComputerPlayer(int playerRotation) {
        this.playerRotation = playerRotation;
    }

    /**
     * Function that executes a turn for the computer player
     * returns if a move is done in the turn.
     * @param boards
     * @param card
     * @param players
     * @return
     */
    public boolean doTurn (PlayerBoard[] boards, int card, int players){
        this.players = players;
        boolean hasMoved = false;
        checkBoard(boards);

        //System.out.println("card");
        //System.out.println(card);

        if(canMove(boards, card)){
            switch (card) {
                case 0:
                    hasMoved = doSorry(boards);
                    break;
                case 1:
                    hasMoved = doOne(boards);
                    break;
                case 2:
                    doTwo(boards);
                    break;
                case 4:
                    hasMoved = doFour(boards);
                    break;
                case 7:
                    hasMoved = doSeven(boards);
                    break;
                case 10:
                    hasMoved = doTen(boards);
                    break;
                case 11:
                    hasMoved = doEleven(boards);
                    break;
                default:
                    //hasMoved = doEleven(boards);
                    hasMoved = doForwardMove(boards, card);
                    break;
            }
        }
        checkPawnNum(boards);
        return hasMoved;
    }

    /**
     * A function that updates the computer player to the state of the game
     * takes the full array of boards and updates the parameter of the
     * computer player
     * @param boards
     */
    private void checkBoard(PlayerBoard[] boards){
        Arrays.fill(myPawns, 0);
        Arrays.fill(otherPawns, 0);

        for (int i = 0; i < myPawns.length; i++) {
            if (boards[playerRotation].hasPawnAt(i)) {
                lastPawnID = i;
            }
        }

        for (int i = myPawns.length; i > 0; i--) {
            if (boards[playerRotation].hasPawnAt(i)) {
                firstPawnID = i;
            }
        }

        startPawns = boards[playerRotation].getStartPawns();
        homePawns = boards[playerRotation].getHomePawns();

        for (int i = 0; i < myPawns.length; i++) {
            if (boards[playerRotation].hasPawnAt(i)) {
                myPawns[i] = 1;
            }
        }

        for (int i = 0; i < 60; i++) {
            for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                if (!(board.getRotation() == playerRotation)) {
                    if (board.hasPawnAt(i,playerRotation)){
                        otherPawns[i] = 1;
                    }
                }
            }
        }
    }

    /**
     * Checks if the computer can execute the card given for the board.
     * @param boards
     * @param card
     * @return
     */
    private boolean canMove(PlayerBoard[] boards, int card){
        boolean canMovePawn = true;

        int pawnsOut = 4 - homePawns - startPawns;

        // checks for the specific case where we have pawns lined up that can't move to home
        // and can't move foward
        if (lastPawnID > 0 && !(card == 10)) {
            if (card > 65 - lastPawnID) {
                if (pawnsOut == 1) {
                    if (myPawns[lastPawnID] == 1) {
                        canMovePawn = false;
                    }
                } else if (pawnsOut == 2) {
                    if (myPawns[lastPawnID] == 1 && myPawns[lastPawnID - card] == 1) {
                        canMovePawn = false;
                    }
                } else if (pawnsOut == 3) {
                    if (myPawns[lastPawnID] == 1 && myPawns[lastPawnID - card] == 1 &&
                            myPawns[lastPawnID - 2 * card] == 1) {
                        canMovePawn = false;
                    }
                } else if (pawnsOut == 4) {
                    if (myPawns[lastPawnID] == 1 && myPawns[lastPawnID - card] == 1 &&
                            myPawns[lastPawnID - card] == 1 && myPawns[lastPawnID - card] == 1) {
                        canMovePawn = false;
                    }
                }
            }
        }

        if (!canMovePawn) {
            switch (card) {
                case 0:
                    if (homePawns > 0) {
                        canMovePawn = hasElement(otherPawns, 1);
                    } else {
                        canMovePawn = false;
                    }
                    break;
                case 1:
                    canMovePawn = true;
                case 2:
                    if (startPawns > 1 && myPawns[1] == 0){
                        canMovePawn = true;
                    }
                    break;
                case 7:
                    if (pawnsOut == 0) {
                        canMovePawn = false;
                    } else if (pawnsOut == 1 && lastPawnID + card > 65) {
                        canMovePawn = false;
                    } else {
                        canMovePawn = true;
                    }
                    break;
                case 11:
                    if (pawnsOut == 1 && lastPawnID + card > 65) {
                        if (hasElement(otherPawns, 1)) {
                            canMovePawn = true;
                        } else {
                            canMovePawn = false;
                        }
                    }
                    break;
                default:
                    canMovePawn = hasElement(myPawns, 1);
                    break;
            }
        }

        return canMovePawn;
    }

    /**
     * Function to move a pawn a certain card number forward.
     * First checks if the pawn can be moved into home or the safe spaces.
     * Then moves the pawn that gives the most valuable move, if two pawns
     * have the same move value, moves the pawn closest to your home.
     * @param boards
     * @param card
     * @return
     */
    private boolean doForwardMove(PlayerBoard[] boards, int card){
        // bool to ensure only one move is done
        boolean hasMoved = false;

        if (hasElement(myPawns,1)) {

            for (int i = 64; i > -1; i--) {
                // check if pawn can move to home or to safe space
                if (boards[playerRotation].canMovePawn(i, card) && i + card == 65) {
                    boards[playerRotation].movePawn(i, card);
                    boards[playerRotation].moveToHome();
                    hasMoved = true;
                    break;
                } else if (boards[playerRotation].canMovePawn(i, card) && i + card > 59) {
                    boards[playerRotation].movePawn(i, card);
                    hasMoved = true;
                    break;
                }
            }

            // next do the moves with the maximum value possible
            // sets maximum possible value of the move
            int maxValue = 11;
            move:
            while (!hasMoved) {
                // for every tile
                for (int i = 64; i > -1; i--) {
                    // checks if the move can be completed
                    if (boards[playerRotation].canMovePawn(i, card)) {
                        //checks if the value of the move matches the max
                        if (moveValue(i, card) == maxValue) {
                            // executes the move and exits loop
                            doMove(boards, i, card);
                            boards[playerRotation].moveToHome();
                            hasMoved = true;
                            break move;
                        }
                    }
                }
                // if move not done decrement max value until we hit the minimum possible value
                maxValue--;
                if (maxValue < -8) {
                    break;
                }
            }
        }

        return hasMoved;
    }

    /**
     * determines the value of a move, for every pawn that's not yours bumped, value is increased by 2
     * for every pawn that is yours bumped, value is decreased by 2.
     * Moving a pawn off start if worth 4 while moving a pawn into home is worth 10.
     * @param startID, moves
     * @return value
     */
    private int moveValue(int startID, int moves) {
        if (moves == 0) {
            return 0;
        }
        int movedTo = startID + moves;
        if (movedTo < 65) {
            int value = 0;
            if (movedTo == 21 || movedTo == 36 || movedTo == 51) {
                for (int i = movedTo; i < movedTo + 5; i++) {
                    if (otherPawns[i] == 1) {
                        value += 2;
                    } else if (myPawns[i] == 1) {
                        value -= 2;
                    }
                }
                value++;
            } else if (movedTo == 13 || movedTo == 28 || movedTo == 43) {
                for (int i = movedTo; i < movedTo + 4; i++) {
                    if (otherPawns[i] == 1) {
                        value += 2;
                    } else if (myPawns[i] == 1) {
                        value -= 2;
                    }
                }
                value++;
            } else if (otherPawns[movedTo] == 1) {
                value += 2;

            }
            if (startID == 1) {
                value += 4;
            }
            if (startID + value == 65) {
                value += 10;
            }
            return value;
        }
        return -100;
    }

    /**
     * Executes the sorry card, if there is a pawn in start,
     * move it to the location of the enemy pawn closes to your home and
     * bump that pawn.
     * @param boards
     * @return
     */
    private boolean doSorry(PlayerBoard[] boards){
        boolean hasMoved = false;
        boolean hasOtherPawn = false;

        if (!(startPawns == 0)) {
            for (int i = 59; i > -1; i--) {
                for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                    if (!(board.getRotation() == playerRotation)) {
                        if (board.hasPawnAt(i, playerRotation)) {
                            hasOtherPawn = true;
                        }
                    }
                }

                if (hasOtherPawn) {
                    boards[playerRotation].movePawnTo(i);

                    for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                        if (!(board.getRotation() == playerRotation)) {
                            board.bump(i, playerRotation);
                        }
                    }

                    hasMoved = true;
                    break;
                }
            }
        }
        return hasMoved;
    }

    /**
     * Move pawn from start if possible, or move pawn forward one.
     * @param boards
     * @return
     */
    private boolean doOne(PlayerBoard[] boards){
        boolean hasMoved = false;
        if (startPawns > 0 && !boards[playerRotation].hasPawnAt(1)) {
            boards[playerRotation].moveFromStart();

            for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                if (!(board.getRotation() == playerRotation)) {
                    board.bump(1, playerRotation);
                }
            }
            hasMoved = true;
        } else {
            hasMoved = doForwardMove(boards, 1);
        }
        return hasMoved;
    }

    /**
     * Move pawn from start if possible, or move pawn forward two.
     * @param boards
     * @return
     */
    private boolean doTwo(PlayerBoard[] boards){
        boolean hasMoved = false;
        if (startPawns > 0 && !boards[playerRotation].hasPawnAt(1)) {
            boards[playerRotation].moveFromStart();

            for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                if (!(board.getRotation() == playerRotation)) {
                    board.bump(1, playerRotation);
                }
            }
            hasMoved = true;
        } else {
            hasMoved = doForwardMove(boards, 2);
        }
        return hasMoved;
    }

    // Move pawn backwards 4, moves the pawn closest to the start
    private boolean doFour(PlayerBoard[] boards){
        boolean hasMoved = false;
        if (hasElement(myPawns,1)) {
            move:
            for (int i = 0; i < 65; i++) {
                // check if pawn can move to home or to safe space
                if (boards[playerRotation].canMovePawn(i, -4)) {
                    doMove(boards, i, -4);
                    break move;
                }
            }
        }
        return hasMoved;
    }

    /**
     * Moves pawn forward 7 if there is only 1 pawn.
     * if there is more than 1 pawn, for every possible combination of 2 pawns on the board,
     * checks the value of every distribution of seven moves between then.
     * Sum the value and take the most valuable combination of moves.
     * @param boards
     * @return
     */
    private boolean doSeven(PlayerBoard[] boards){
        boolean hasMoved = false;
        if (hasElement(myPawns,1)) {
            int pawnsOut = 4 - homePawns - startPawns;

            if (pawnsOut == 1) {
                hasMoved = doForwardMove(boards, 7);
            } else {
                int maxValue = 22;
                // turn back while you have the chance
                moved:
                while (!hasMoved) {
                    for (int i = 64; i > -1; i--) {
                        if (boards[playerRotation].hasPawnAt(i)) {
                            for (int j = 64; j > -1; j--) {
                                if (!(i == j) && boards[playerRotation].hasPawnAt(j)) {
                                    for (int k = 0; k < 7; k++) {
                                        if(!(i + 7 - k == j + k)) {
                                            int value1 = moveValue(i, 7 - k);
                                            int value2 = moveValue(j, k);

                                            if (boards[playerRotation].canMovePawn(i, 7 - k)
                                                && boards[playerRotation].canMovePawn(j, k)) {
                                                if (maxValue == value1 + value2) {
                                                    int bump1 = boards[playerRotation].movePawn(i, 7 - k);

                                                    for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                                                        if (!(board.getRotation() == playerRotation)) {
                                                            board.bump(bump1, playerRotation);
                                                        }
                                                    }

                                                    int bump2 = boards[playerRotation].movePawn(j, k);

                                                    for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                                                        if (!(board.getRotation() == playerRotation)) {
                                                            board.bump(bump2, playerRotation);
                                                        }
                                                    }

                                                    int[] bumped1 = boards[playerRotation].checkSlide();

                                                    for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                                                        if (!(board.getRotation() == playerRotation)) {
                                                            board.bump(bumped1, playerRotation);
                                                        }
                                                    }

                                                    int[] bumped2 = boards[playerRotation].checkSlide();

                                                    for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                                                        if (!(board.getRotation() == playerRotation)) {
                                                            board.bump(bumped2, playerRotation);
                                                        }
                                                    }

                                                    hasMoved = true;
                                                    boards[playerRotation].moveToHome();
                                                    break moved;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    maxValue--;
                    if (maxValue < -16){
                        break;
                    }
                }
            }
        }
        return hasMoved;
    }

    /**
     * Moves pawn backwards 1 if pawn is on the first two tile
     * else move the pawn forward 10.
     * @param boards
     * @return
     */
    private boolean doTen(PlayerBoard[] boards) {
        boolean hasMoved = false;

        if (hasElement(myPawns,1)) {

            if (firstPawnID < 2) {
                move:
                for (int i = 0; i < 2; i++) {
                    // check if pawn can move to home or to safe space
                    if (boards[playerRotation].canMovePawn(i, -1)) {
                        doMove(boards, i, -1);
                        hasMoved = true;
                        break move;
                    }
                }
            }
            if (!hasMoved) {
                hasMoved = doForwardMove(boards, 10);
            }
            if (!hasMoved) {
                move2:
                for (int i = 0; i < 65; i++) {
                    // check if pawn can move to home or to safe space
                    if (boards[playerRotation].canMovePawn(i, -1)) {
                        doMove(boards, i, -1);
                        hasMoved = true;
                        break move2;
                    }
                }
            }
        }
        return hasMoved;
    }

    /**
     * Checks of swap results in a move of greater than 11 or a value of greater than 4
     * Else move pawn forward 10
     * Swaps pawn backwards if there is no other legal move.
     * @param boards
     * @return
     */
    private boolean doEleven(PlayerBoard[] boards){
        boolean hasMoved = false;
        if (hasElement(myPawns,1)) {
            int maxValue = 11;
            moved:
            while (!hasMoved) {
                for (int i = 0; i < 60; i++) {
                    if (boards[playerRotation].hasPawnAt(i)) {
                        for (int j = 60; j > -1; j--) {
                            if (hasPawnOther(j)) {
                                int distance = j - i;
                                if ((distance > 11 && moveValue(i, distance) > -1) && !onSlide(boards, i)
                                        || maxValue == moveValue(i, distance) && !onSlide(boards, i)){
                                    int[] bumped1;
                                    int[] bumped2 = new int[0];

                                    boards[playerRotation].movePawn(i, distance);
                                    bumped1 = boards[playerRotation].checkSlide();

                                    for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                                        if (!(board.getRotation() == playerRotation)) {
                                            if (board.hasPawnAt(j, playerRotation)) {
                                                board.movePawn(j,  -distance, playerRotation);
                                            }
                                            board.bump(bumped1, playerRotation);

                                        }
                                    }
                                    hasMoved = true;
                                    break moved;
                                }
                            }
                        }
                    }
                }
                maxValue--;
                if (maxValue == 4){
                    break;
                }
            }
            if (!hasMoved) {
                doForwardMove(boards, 11);
            }
            if (!hasMoved) {
                moved:
                while (!hasMoved) {
                    for (int i = 0; i < 60; i++) {
                        if (boards[playerRotation].hasPawnAt(i)) {
                            for (int j = 60; j > -1; j--) {
                                if (hasPawnOther(j)) {
                                    int distance = j - i;
                                    if ((distance > 11 && moveValue(i, distance) > -1)  && i < 60
                                            || maxValue == moveValue(i, distance) && i < 60){
                                        int[] bumped1;
                                        int[] bumped2 = new int[0];

                                        int move1 = boards[playerRotation].movePawn(i, distance);
                                        bumped1 = boards[playerRotation].checkSlide();

                                        int otherRotation = -1;

                                        int move2 = 1;

                                        for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                                            if (!(board.getRotation() == playerRotation)) {
                                                if (board.hasPawnAt(j, playerRotation)) {
                                                    move2 = board.movePawn(j,  -distance - 60, playerRotation);
                                                    otherRotation = board.getRotation();
                                                    bumped2 = board.checkSlide();
                                                }
                                            }
                                        }

                                        /*
                                        System.out.println(i);
                                        System.out.println(j);
                                        System.out.println(distance);
                                        System.out.println(move1);
                                        System.out.println(move2);
                                        */

                                        for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
                                            if (!(board.getRotation() == playerRotation)) {
                                                board.bump(bumped1, playerRotation);
                                            }
                                            if (!(board.getRotation() == otherRotation)) {
                                                board.bump(bumped2, otherRotation);
                                            }
                                        }
                                        hasMoved = true;
                                        break moved;
                                    }
                                }
                            }
                        }
                    }
                    maxValue--;
                    if (maxValue == -10){
                        break;
                    }
                }
            }

        }
        return hasMoved;
    }

    /**
     * Checks if a tileID is on the start of a slide
     * @param boards
     * @param tileID
     * @return
     */
    private boolean onSlide(PlayerBoard[] boards, int tileID) {
        if (tileID == 21 || tileID == 36 || tileID == 51) {
            return true;
        } else if (tileID == 13 || tileID == 28 || tileID == 43) {
            return true;
        }
        return false;
    }

    /**
     * Executes a move, moves the pawn on tile ID a certain number of moves,
     * bump the pawns on resulting tile. Checks if the tile is on the start
     * of a slide, executes the slide and bumps all pawns on the slide.
     * @param boards
     * @param tileID
     * @param moves
     */
    private void doMove(PlayerBoard[] boards, int tileID, int moves){
        int bump = boards[playerRotation].movePawn(tileID, moves);

        for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
            if (!(board.getRotation() == playerRotation)) {
                board.bump(bump, playerRotation);
            }
        }

        int[] bumped = boards[playerRotation].checkSlide();

        for (PlayerBoard board : Arrays.copyOfRange(boards, 0, players)) {
            if (!(board.getRotation() == playerRotation)) {
                board.bump(bumped, playerRotation);
            }
        }
    }

    /**
     * Checks if there is a pawn from another board on the tile ID
     * @param tileID
     * @return
     */
    boolean hasPawnOther(int tileID){
        if (otherPawns[tileID] == 1){
            return true;
        }
        return false;
    }

    /**
     * Uitility function, checks if an array contains an element
     * @param array
     * @param element
     * @return
     */
    private boolean hasElement (int[] array, int element){
        for (int i : array) {
            if (i == element) {
                return true;
            }
        }
        return false;
    }

    /**
     * Patch for strange behavior of 11 when a pawn gets deleted
     * Checks if there are 4 pawns total, if there are less, adds the missing
     * pawns to start.
     * @param boards
     */
    private void checkPawnNum (PlayerBoard[] boards) {
        for (PlayerBoard board : boards) {
            int numPawns = 0;

            numPawns += board.getHomePawns();
            numPawns += board.getStartPawns();

            for (int i = 0; i < 65; i++){
                if (board.hasPawnAt(i)) {
                    numPawns ++;
                }
            }
            if (numPawns < 4) {
                while (!(numPawns == 4)) {
                    board.addStartPawns();
                    numPawns++;
                }
            }
        }


    }
}
