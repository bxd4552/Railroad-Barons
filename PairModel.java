package student;

import model.Card;
import model.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Cards in a Railroad Barons game are dealt to each Player in pairs. This class is used to hold one such pair of cards.
 * @author Lauren Baldino
 */
public class PairModel implements model.Pair {
    private List<Card> cards;
    private Card firstCard;
    private Card secondCard;

    /**
     * Constructor for a pair
     * @param firstCard the first card in the pair
     * @param secondCard the second card in the pair
     * */
    public PairModel(Card firstCard, Card secondCard) {
        this.cards = new ArrayList<>();
        this.firstCard = firstCard;
        this.secondCard = secondCard;
        cards.add(firstCard);
        cards.add(secondCard);
    }

    @Override
    public Card getFirstCard() {
        /**
         * Accessor for the first card in the pair
         * @return Returns the first card in the pair. Note that, if the game deck is empty, the value of this card may be Card.NONE.
         */
        if(cards.size() == 0) {
            return Card.NONE;
        }
        else {
            return this.firstCard;
        }
    }

    @Override
    public Card getSecondCard() {
        /**
         * Accessor for the second card in the pair
         * @return Returns the second card in the pair. Note that, if the game deck is empty, the value of this card may be Card.NONE.
         */
        if(cards.size() == 0) {
            return Card.NONE;
        }
        else {
            return this.secondCard;
        }
    }
}
