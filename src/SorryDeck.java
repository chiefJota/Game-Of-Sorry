import java.util.*;
public class SorryDeck {

    //Constant to have number of cards in the deck
    private final int CARDS_IN_DECK = 45;

    //description variable
    private String description;

    private ArrayList <SorryCard> deck;

    public SorryDeck() {

        //deck has 45 cards
        deck = new ArrayList<>(CARDS_IN_DECK);

        //first add five cards of ones to the deck
        for (int i = 0; i < 5; ++i) {
            deck.add(new SorryCard(1, "Move a pawn onto the outer starting circle, or move a pawn one space forward."));
        }

        //iterate four times the rest of the cards
        int numFour = 0;
        while (numFour < 4) {
            //then add 4 of each card 2-5
            for (int j = 2; j <= 5; ++j) {
                if (j == 2) {
                    description = "Move a pawn onto the outer starting circle, or move a pawn two spaces forward. Draw again, even if you could not move a pawn.";
                    deck.add(new SorryCard(j, description));
                } else if (j == 3) {
                    description = "Move a pawn forward three spaces";
                    deck.add(new SorryCard(j, description));
                } else if (j == 4) {
                    description = "Move a pawn packward four spaces";
                    deck.add(new SorryCard(j, description));
                } else {
                    description = "Move a pawn forward five spaces";
                    deck.add(new SorryCard(j, description));
                }
            }

            for(int l = 7; l <= 8; ++l) {
                if (l == 7) {
                    description = "Move a pawn forward seven spaces, or split the movement between two pawns. 7’s can’t be used to start a pawn. If a 7 is used to move on pawn home, " +
                            "the remainder of the movement total" +
                            " must be used exactly by another " +
                            "pawn or it is not a legal move.";
                    deck.add(new SorryCard(l, description));
                }
                else{
                    description = "Move a pawn forward eight spaces";
                    deck.add(new SorryCard(l, description));

                }
            }

            for(int m = 10; m <= 12; ++m) {
                if (m == 10) {
                    description = "Move a pawn forward 10 spaces, or move a pawn backward one space.";
                    deck.add(new SorryCard(m, description));
                }
                else if(m == 11){
                    description = "Move a pawn forward 11 spaces, or switch the position of any one of your " +
                            "pawns with any one of your opponents pawns. Note, " +
                            "you do not have to switch positions if there is no " +
                            "other legal play. This is the exception to the aforementioned rule." +
                            " Also, you cannot switch with pawns in the “START”, “HOME”, or “SAFETY ZONE” areas. ";
                    deck.add(new SorryCard(m, description));

                }
                else{
                    description = "Move a pawn forward 12 spaces";
                    deck.add(new SorryCard(m, description));
                }
            }

            //insert Sorry! cards
            description = "Take one pawn from your “START” position" +
                    " and move it to any legal space occupied by an opponent (" +
                    "no “HOME”, “START”, or “SAFETY ZONE” spaces), and " +
                    "bump the opponent back to their “START” space. If you have no" +
                    " pawns in the “START” space, or " +
                    "there are no legal pawns to bump, end your turn.";
            int number = 0;
            deck.add(new SorryCard(number, description));

            //increment numFour
            numFour++;
        }
    }

    /**
     * shuffle method takes no arguments
     * uses the random library and runs through
     * the deck and assigns the randNum variable to
     * generate a random int from the deck
     * then sets the deck to get randNum and has
     * the deck set randNum to be the newly shuffled deck
     *
     */
    public void shuffle(){

        int randNum;
        SorryCard temp;
        Random rand = new Random();
        for(int i = 0; i < deck.size(); i++){
            randNum = rand.nextInt(deck.size());
            temp = deck.get(i);
            deck.set(i,deck.get(randNum));
            deck.set(randNum,temp);
        }
    }

    /**
     * getTopCard method takes the deck and uses
     * the remove method with the cards remaining()-1
     * and then returns
     * @return "s" as the top card of the deck
     */
    public SorryCard getTopCard(){
        SorryCard sorryCard = deck.remove(cardsRemaining()-1);
        return sorryCard;
    }

    /**
     * returns the current state of the deck
     * regarding how many cards are left
     * @return deck.size()
     */
    public int cardsRemaining(){

        return deck.size();
    }

    /**
     * boolean isEmpty method determines whether
     * the deck is finished or cards are left in
     * the deck...true if no cards remain..false otherwise
     * @return deck.size() ==0
     */
    public boolean isEmpty(){

        return (deck.size() == 0);
    }

    /**
     * toString method converts the deck
     * to a string
     * @return deck.toString()
     */
    @Override
    public String toString() {

        return deck.toString();
    }
}
