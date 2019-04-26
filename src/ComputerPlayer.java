import java.util.Arrays;

public class ComputerPlayer {
    int rotation = 0;
    int startPawns = 4;
    int homePawns = 0;
    int[] myPawns = new int[60];
    int[] otherPawns = new int[60];

    public ComputerPlayer(int rotation) {
        this.rotation = rotation;
    }

    public void doTurn (PlayerBoard[] boards, int turn, int card){
        checkBoard(boards);

        boolean canMovePawns = canMove(boards, card);
    }

    private void checkBoard(PlayerBoard[] boards){
        Arrays.fill(myPawns, 0);
        Arrays.fill(otherPawns, 0);

        startPawns = boards[rotation].getStartPawns();
        homePawns = boards[rotation].getHomePawns();

        for (int i = 0; i < myPawns.length; i++) {
            if (boards[rotation].hasPawnAt(i)) {
                myPawns[i] = 1;
            }
        }

        for (int i = 0; i < myPawns.length; i++) {
            for (PlayerBoard board : boards) {
                if (!(board.getRotation() == rotation)) {
                    if (board.hasPawnAt(i,rotation)){
                        otherPawns[i] = 1;
                    }
                }
            }
        }
    }

    private boolean canMove(PlayerBoard[] boards, int card){
        boolean canMovePawn = true;

        int pawnsOut = 4 - homePawns - startPawns;

        int lastPawnID = 0;

        for (int i = 0; i < myPawns.length; i++) {
            if (boards[rotation].hasPawnAt(i)) {
                lastPawnID = i;
            }
        }

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
                        canMovePawn = Arrays.asList(otherPawns).contains(1);
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
                        if (Arrays.asList(otherPawns).contains(1)) {
                            canMovePawn = true;
                        } else {
                            canMovePawn = false;
                        }
                    }
                    break;
                default:
                    canMovePawn = Arrays.asList(myPawns).contains(1);
                    break;
            }
        }

        return canMovePawn;
    }
}
