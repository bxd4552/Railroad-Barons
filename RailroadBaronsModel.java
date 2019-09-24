package student;

import model.*;

import java.util.*;

/**
 * The interface for a Railroad Barons game. The main entry point into the model for the entire game.
 * @author Lauren Baldino, Ben Donahue
 */
public class RailroadBaronsModel implements model.RailroadBarons {

    private List<Player> players;
    private List<Route> claimedRoutes;
    private List<RailroadBaronsObserver> observers;
    private Deck deck;
    private Player currentPlayer;
    private RailroadMap map;

    /**
     * Constructor for the railroad barons game, 4 players are used every time with
     * the red player always going first.
     */
    public RailroadBaronsModel() {
        this.players = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.claimedRoutes = new ArrayList<>();
        this.deck = new DeckModel();
        PlayerModel player1 = new PlayerModel(Baron.RED);
        PlayerModel player2 = new PlayerModel(Baron.BLUE);
        PlayerModel player3 = new PlayerModel(Baron.YELLOW);
        PlayerModel player4 = new PlayerModel(Baron.GREEN);
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        for(Player player: this.players) {
            player.reset(this.getStartHand());
        }
        this.currentPlayer = player1;
        this.map = null;
    }

    /**
     * Adds a new observer to the collection of observers that will be notified when the state of the game changes.
     * @param observer The RailroadBaronsObserver to add to the Collection of observers.
     */
    @Override
    public void addRailroadBaronsObserver(RailroadBaronsObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes the observer from the collection of observers that will be notified when the state of the game changes.
     * @param observer The RailroadBaronsObserver to remove.
     */
    @Override
    public void removeRailroadBaronsObserver(RailroadBaronsObserver observer) {
        int i;
        for(i = 0; i < observers.size(); i++) {
            if(observers.get(i).equals(observer)) {
                observers.remove(i);
            }
        }
    }

    /**
     * Starts a new Railroad Barons game with the specified map and a default deck of cards.
     * If a game is currently in progress, the progress is lost.
     * @param map The RailroadMap on which the game will be played.
     */
    @Override
    public void startAGameWith(RailroadMap map) {
        this.map = map;
        this.claimedRoutes = new ArrayList<>();
        Pair pair = new PairModel(deck.drawACard(), deck.drawACard());
        this.currentPlayer.startTurn(pair);
        for(RailroadBaronsObserver o: this.observers) {
            o.turnStarted(this, this.currentPlayer);
        }
    }

    /**
     * Starts a new Railroad Barons game with the specified map and deck of cards.
     * @param map The RailroadMap on which the game will be played.
     * @param deck  The Deck of cards used to play the game.
     */
    @Override
    public void startAGameWith(RailroadMap map, Deck deck) {
        this.map = map;
        this.claimedRoutes = new ArrayList<>();
        this.deck = deck;
        Pair pair = new PairModel(deck.drawACard(), deck.drawACard());
        this.currentPlayer.startTurn(pair);
        for(RailroadBaronsObserver o: this.observers) {
            o.turnStarted(this, this.currentPlayer);
        }
    }

    /**
     * Returns the map currently being used for play. If a game is not in progress, this may be null!
     * @return RailroadMap being used for play.
     */
    @Override
    public RailroadMap getRailroadMap() {
        return this.map;
    }

    /**
     * Returns the number of cards that remain to be dealt in the current game's deck.
     * @return The number of cards that have not yet been dealt in the game's Deck.
     */
    @Override
    public int numberOfCardsRemaining() {
        return this.deck.numberOfCardsRemaining();
    }

    /**
     * Returns true iff the current player can claim the route at the specified location.
     * @param row The row of a Track in the Route to check.
     * @param col The column of a Track in the Route to check.
     * @return True iff the Route can be claimed by the current player.
     */
    @Override
    public boolean canCurrentPlayerClaimRoute(int row, int col) {
        return this.currentPlayer.canClaimRoute(this.map.getRoute(row, col));
    }

    /**
     * Attempts to claim the route at the specified location on behalf of the current player.
     * @param row  The row of a Track in the Route to claim.
     * @param col The column of a Track in the Route to claim.
     * @throws RailroadBaronsException If the Route cannot be claimed by the current player.
     */
    @Override
    public void claimRoute(int row, int col) throws RailroadBaronsException {
        this.currentPlayer.claimRoute(this.map.getRoute(row, col));
        this.map.routeClaimed(this.map.getRoute(row, col));
        this.claimedRoutes.add(map.getRoute(row, col));
        this.endTurn();
    }

    /**
     * Called when the current player ends their turn.
     */
    @Override
    public void endTurn() {
        this.players.remove(this.currentPlayer);
        this.players.add(this.currentPlayer);
        for(RailroadBaronsObserver o: this.observers) {
            o.turnEnded(this, this.currentPlayer);
        }
        if(players.get(0).canContinuePlaying(this.map.getLengthOfShortestUnclaimedRoute())) {
            this.currentPlayer = players.get(0);
        }

        //move player to back of list if they can't continue playing
        else {
            Player playerFinished = this.players.remove(0);
            this.players.add(playerFinished);
            this.currentPlayer = this.players.get(0);
        }

        //loop deck after it is emptied
        if(this.deck.numberOfCardsRemaining() <= 0) {
            deck.reset();
            for(Player player: this.players) {
                player.reset(getStartHand());
            }
        }

        //continue game
        if(!gameIsOver()) {
            this.currentPlayer.startTurn(new PairModel(deck.drawACard(), deck.drawACard()));
            for (RailroadBaronsObserver o : observers) {
                o.turnStarted(this, getCurrentPlayer());
            }
        }

        //end game and decide winner
        else {
            Player winner = this.currentPlayer;
            for(Player player: players) {
                if(player.getScore() > winner.getScore()) {
                    winner = player;
                }
            }
            for(RailroadBaronsObserver o: observers) {
                o.gameOver(this, winner);
            }
        }
    }

    /**
     * Accessor for the current player whose turn it is.
     * @return the current player
     */
    @Override
    public Player getCurrentPlayer() {
        /**
         * Returns the player whose turn it is.
         * @return The Player that is currently taking a turn.
         */
        return this.currentPlayer;
    }

    /**
     * Returns all of the players currently playing the game.
     * @return The Players currently playing the game.
     */
    @Override
    public Collection<Player> getPlayers() {
        return this.players;
    }

    /**
     * Indicates whether or not the game is over. This occurs when no more plays can be made.
     * @return True if the game is over, false otherwise.
     */
    @Override
    public boolean gameIsOver() {
        boolean over = true;
        for(Route route: this.getRailroadMap().getRoutes()) {
            if(route.getBaron().equals(Baron.UNCLAIMED)) {
                over =  false;
            }
        }

        return over;
    }

    /**
     * Create a starting hand of 4 cards for the player
     * @return the player's starting hand
     */
    public Card[] getStartHand() {
        Card[] startHand = {this.deck.drawACard(), this.deck.drawACard(), this.deck.drawACard(),
                this.deck.drawACard()};
        return startHand;
    }
}