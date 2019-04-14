
import java.util.*;
public class Game {
    private SorryDeck deck;

    public Game(){
        deck = new SorryDeck();
        deck.shuffle();
        deck.cardsRemaining();
    }

    public boolean outOfCards(){
        return deck.isEmpty();
    }

    public int cardsLeft(){
        return deck.cardsRemaining();
    }

    public SorryCard getTopCard(){
        return deck.getTopCard();
    }
}
