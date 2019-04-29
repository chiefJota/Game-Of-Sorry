import java.util.Arrays;

public class ComputerPlayer {
    int playerRotation = 0;
    int startPawns = 4;
    int homePawns = 0;
    int lastPawnID = 0;
    int firstPawnID = 0;
    int[] myPawns = new int[65];
    int[] otherPawns = new int[65];
    int players = 2;

    public ComputerPlayer(){}

    public ComputerPlayer(int playerRotation) {
        this.playerRotation = playerRotation;
    }

    public boolean doTurn (PlayerBoard[] boards, int card, int players){
        this.players = players;
        boolean hasMoved = false;
        checkBoard(boards);

        System.out.println("card");
        System.out.println(card);

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
            int maxValue = 11;
            move:
            while (!hasMoved) {
                for (int i = 64; i > -1; i--) {
                    if (boards[playerRotation].canMovePawn(i, card)) {
                        //checks the value of the move
                        if (moveValue(i, card) == maxValue) {
                            doMove(boards, i, card);
                            boards[playerRotation].moveToHome();
                            hasMoved = true;
                            break move;
                        }
                    }
                }
                maxValue--;
                if (maxValue < -8) {
                    break;
                }
            }
        }

        return hasMoved;
    }

    /**
     * determines the value of a move, for every pawn that's not yours bumped, value is incremented
     * for every pawn that is yours bumped, value is decremented
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
        return -10;
    }

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

    private boolean doEleven(PlayerBoard[] boards){
        boolean hasMoved = false;
        if (hasElement(myPawns,1)) {
            int maxValue = 11;
            moved:
            while (!hasMoved) {
                for (int i = 0; i < 64; i++) {
                    if (boards[playerRotation].hasPawnAt(i)) {
                        for (int j = 64; j > -1; j--) {
                            if (hasPawnOther(j)) {
                                int distance = j - i;
                                if ((distance > 11 && moveValue(i, distance) > -1) && !onSlide(boards, i)
                                        || maxValue == moveValue(i, distance) && !onSlide(boards, i)){
                                    System.out.println("in");
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
                    for (int i = 0; i < 64; i++) {
                        if (boards[playerRotation].hasPawnAt(i)) {
                            for (int j = 64; j > -1; j--) {
                                if (hasPawnOther(j)) {
                                    int distance = j - i;
                                    if ((distance > 11 && moveValue(i, distance) > -1)  && i < 60
                                            || maxValue == moveValue(i, distance) && i < 60){
                                        System.out.println("in");
                                        System.out.println(i);
                                        System.out.println(j);
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

                                        System.out.println(i);
                                        System.out.println(j);
                                        System.out.println(distance);
                                        System.out.println(move1);
                                        System.out.println(move2);

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

    private boolean onSlide(PlayerBoard[] boards, int tileID) {
        if (tileID == 21 || tileID == 36 || tileID == 51) {
            return true;
        } else if (tileID == 13 || tileID == 28 || tileID == 43) {
            return true;
        }
        return false;
    }

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

    boolean hasPawnOther(int tileID){
        if (otherPawns[tileID] == 1){
            return true;
        }
        return false;
    }

    private boolean hasElement (int[] array, int element){
        for (int i : array) {
            if (i == element) {
                return true;
            }
        }
        return false;
    }

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
