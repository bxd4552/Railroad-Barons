package student;

import model.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model for a deck of cards.
 * @author Ben Donahue
 */
public class DeckModel implements model.Deck {
    private List<Card> cards;

    /**
     * Constructor for DeckModel. Sets the deck of cards as null, then resets the deck
     * to add and shuffle cards.
     */
    public DeckModel() {
        this.cards = new ArrayList<>();
        this.reset();
    }

    /**
     * Reset the deck of cards after there are no more cards to draw from,
     * then shuffle the deck.
     */
    @Override
    public void reset() {
        for(int i = 0; i < 20; i++) {
            cards.add(Card.RED);
            cards.add(Card.BLACK);
            cards.add(Card.BLUE);
            cards.add(Card.GREEN);
            cards.add(Card.ORANGE);
            cards.add(Card.PINK);
            cards.add(Card.WHITE);
            cards.add(Card.WILD);
            cards.add(Card.YELLOW);
        }
        Collections.shuffle(cards);
    }

    /**
     * Draw a card from the deck by removing and returning it. If there
     * are no cards in the deck, reset it.
     * @return the card at the top of the deck.
     */
    @Override
    public Card drawACard() {
        if(cards.size() == 0) {
            this.reset();
        }
        Card card = cards.remove(0);
        return card;
    }

    /**
     * Returns the amount of cards remaining in the deck.
     * @return the size of the remaining deck.
     */
    @Override
    public int numberOfCardsRemaining() {
        return cards.size();
    }
}
