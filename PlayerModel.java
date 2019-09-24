package student;

import model.*;
import model.Pair;
import model.Route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Model for a player, handles all of a players actions/whether they can do some of their
 * actions.
 * @author Ben Donahue
 */
public class PlayerModel implements model.Player {
    private List<Route> claimedRoutes;
    private List<Card> hand;
    private List<PlayerObserver> observers;
    private Baron baron;
    private Graph graph;
    protected int score;
    protected  int numPieces;
    private Card[] colors =  {Card.BLACK, Card.BLUE, Card.GREEN, Card.ORANGE, Card.PINK, Card.RED, Card.WHITE,
            Card.YELLOW, Card.WILD};


    /**
     * Constructor for the player. The player is given a baron to play as, and has their
     * score, claimed routes, and number of pieces all set to their staring values.
     * @param baron the Baron that will be set to the player
     */
    public PlayerModel(Baron baron) {
        this.baron = baron;
        this.claimedRoutes = new ArrayList<>();
        this.score = 0;
        this.numPieces = 45;
        this.hand = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.graph = new student.Graph();
    }

    /**
     * Deal a hand of cards to the player.
     * @param dealt The hand of {@link Card cards} dealt to the player at the
     */
    @Override
    public void reset(Card... dealt) {
        //card... is an array, so iterate over dealt and add each card to hand
        this.hand = new ArrayList<>();
        int i;
        for(i = 0; i < dealt.length; i++) {
            this.hand.add(dealt[i]);
        }
        this.notifyObservers();
    }

    /**
     * Add a new observer to the list of player observers.
     * @param observer The new {@link PlayerObserver}.
     */
    @Override
    public void addPlayerObserver(PlayerObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove an observer from the list of player observers.
     * @param observer The {@link PlayerObserver} to remove.
     */
    @Override
    public void removePlayerObserver(PlayerObserver observer) {
        int i = 0;
        for(PlayerObserver o: observers) {
            if(o.equals(observer)) {
                observers.remove(i);
            }
            i++;
        }
    }

    /**
     * Accessor for the baron the player is playing as
     * @return the baron they are playing as
     */
    @Override
    public Baron getBaron() {
        return this.baron;
    }

    /**
     * Start a player's turn by dealing two cards to them.
     * @param dealt a {@linkplain Pair pair of cards} to the player. Note that
     */
    @Override
    public void startTurn(Pair dealt) {
        hand.add(dealt.getFirstCard());
        hand.add(dealt.getSecondCard());
        this.notifyObservers();
    }

    /**
     * Return the last pair the player was dealt by retrieving the last two cards
     * from their hand.
     * @return the last pair the player
     */
    @Override
    public Pair getLastTwoCards() {
        Pair lastPair;
        try {
            if (this.hand.size() >= 2) {
                Card secondToLast = this.hand.get(this.hand.size() - 1);
                Card last = this.hand.get(this.hand.size() - 2);
                lastPair = new PairModel(secondToLast, last);
            } else {
                Card secondToLast = this.hand.get(0);
                Card last = this.hand.get(this.hand.size());
                lastPair = new PairModel(secondToLast, last);
            }
        }

        //used to handle the event that a player uses all of their cards in one turn, and similar issues
        //this is mostly used to catch an inconsistent bug in claiming a route
        catch (IndexOutOfBoundsException ioobe) {
            try{
                return new PairModel(this.hand.get(0), Card.NONE);
            }
            catch (IndexOutOfBoundsException ioobe2) {
                return new PairModel(Card.NONE, Card.NONE);
            }

        }
        return lastPair;
    }

    /**
     * Count the amount cards of some color in a player's hand.
     * @param card The {@link Card} of interest.
     * @return the amount of cards of the specified color in the player's hand.
     */
    @Override
    public int countCardsInHand(Card card) {
        int i = 0;
        for(Card match: hand) {
            if(match.equals(card)) {
                i++;
            }
        }
        return i;
    }

    /**
     * Accessor for the remaining number of pieces the player has.
     * @return the number of pieces the player has.
     */
    @Override
    public int getNumberOfPieces() {
        return this.numPieces;
    }

    /**
     * Checks if the player can claim a route based on their remaining amount of pieces.
     * @param route The {@link Route} being tested to determine whether or not
     *              the player is able to claim it.
     * @return whether or not the player can claim the route
     */
    @Override
    public boolean canClaimRoute(Route route) {
        int i;

        //iterate through every card color to see if the player has enough cards of one color
        //to claim the route
        for(i = 0; i < colors.length - 1; i++) {
            //check for same amount of one color
            if((countCardsInHand(colors[i]) >= route.getLength() ||
                    //check for color and wild cards
                    (countCardsInHand(colors[i]) >= route.getLength() - 1 && countCardsInHand(colors[8]) >= 1))
                    //check number of pieces
                    && this.numPieces >= route.getLength() && route.getBaron().equals(Baron.UNCLAIMED)) {
                return true;
            }
        }
        return false;
    }

    /**
     * The player claim's a route on the board; their score is updated depending
     * on how many points the route was worth, and their pieces are updated based
     * on the length of the route.
     * @param route The {@link Route} to claim.
     *
     * @throws RailroadBaronsException
     */
    @Override
    public void claimRoute(Route route) throws RailroadBaronsException {
        if(this.canClaimRoute(route)) {
            route.claim(this.baron);
            int cardChoice = chooseCard(route.getLength());
            int i;
            int j = countCardsInHand(colors[cardChoice]);

            //use all of one color
            if(j == route.getLength()) {
                for (i = 1; i <= route.getLength(); i++) {
                    this.hand.remove(colors[cardChoice]);
                }
            }

            //use all of one color plus one wild card
            else if(j < route.getLength()) {
                for(i = 1; i <= route.getLength() - 1; i++) {
                    this.hand.remove(colors[cardChoice]);
                }
                this.hand.remove(colors[8]);
            }

            this.claimedRoutes.add(route);
            this.score += route.getPointValue();
            this.numPieces -= route.getLength();
            this.graph.addStation(route.getOrigin());
            this.graph.addStation(route.getDestination());
            this.notifyObservers();
        }
    }

    /**
     * Accessor for the routes this player has claimed
     * @return the routes this player has claimed
     */
    @Override
    public Collection<Route> getClaimedRoutes() {
        return this.claimedRoutes;
    }

    /**
     * Accessor for the player's score
     * @return the player's score
     */
    @Override
    public int getScore() {
        return this.score;
    }

    /**
     * Checks whether or not the player can continue playing by comparing their remaining
     * number of pieces and the shortest unclaimed route on the board.
     * @param shortestUnclaimedRoute The length of the shortest unclaimed
     *                               {@link Route} in the current game.
     * @return whether or not the player can continue playing
     */
    @Override
    public boolean canContinuePlaying(int shortestUnclaimedRoute) {
        if(shortestUnclaimedRoute > this.numPieces || shortestUnclaimedRoute < 0) {
            return false;
        }
        return true;
    }

    /**
     * Notifies player observers of a change made to the player's state.
     */
    public void notifyObservers() {
        for(PlayerObserver o: observers) {
            o.playerChanged(this);
        }
    }

    /**
     * Used to choose the card that can be used to claim the route.
     * @param routeLength the amount of cards needed to claim the route.
     * @return the index of the card color in an array of card colors to choose.
     */
    public int chooseCard(int routeLength) {
        int i;
        int[] cardNums = new int[9];

        //iterate through the deck and count how many of each type of card the player has
        for(i = 0; i < colors.length; i++) {
            cardNums[i] = countCardsInHand(colors[i]);
        }

        //if the player has enough cards of one color, use that card color
        //-1 is added to prevent using all wild cards
        for(i = 0; i < colors.length - 1; i++) {
            if(cardNums[i] >= routeLength) {
                return i;
            }
        }

        //if the player has enough cards of one color plus one wild card, use that card color
        for(i = 0; i < colors.length - 1; i++) {
            if(cardNums[i] == routeLength - 1 && cardNums[8] >= 1 ) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Return the color of the player to be used by the GUI.
     * @return the color of the player
     */
    public String toString() {
        return baron.toString();
    }
}
