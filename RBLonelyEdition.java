package student;

import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Lonely edition of railroad barons, runs a game with one player and 3 computer players.
 * @author Ben Donahue
 */
public class RBLonelyEdition implements RailroadBarons {
    private List<Player> players;
    private List<Route> claimedRoutes;
    protected List<RailroadBaronsObserver> observers;
    private Deck deck;
    private Player currentPlayer;
    private RailroadMap map;

    /**
     * Constructor for Lonely Edition, starts the human player before any of the computer players.
     */
    public RBLonelyEdition() {
        this.players = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.claimedRoutes = new ArrayList<>();
        this.deck = new DeckModel();
        PlayerModel player1 = new PlayerModel(Baron.RED);
        players.add(player1);
        this.currentPlayer = player1;
        this.map = null;
    }

    /**
     * Adds a new observer to the collection of observers that will be notified when the state of the game changes.
     * @param observer The {@link RailroadBaronsObserver} to add to the collection of observers.
     */
    @Override
    public void addRailroadBaronsObserver(RailroadBaronsObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Removes the observer from the collection of observers that will be notified when the state of the game changes.
     * @param observer The {@link RailroadBaronsObserver} to remove.
     */
    @Override
    public void removeRailroadBaronsObserver(RailroadBaronsObserver observer) {
        this.observers.remove(observer);
    }

    /**
     * Starts a new Railroad Barons game with 3 computer players and
     * the specified map and a default deck of cards.
     * If a game is currently in progress, the progress is lost.
     * @param map The {@link RailroadMap} on which the game will be played.
     */
    @Override
    public void startAGameWith(RailroadMap map) {
        this.map = map;
        ComputerModel player2 = new ComputerModel(Baron.BLUE, this.map, this);
        ComputerModel player3 = new ComputerModel(Baron.YELLOW, this.map, this);
        ComputerModel player4 = new ComputerModel(Baron.GREEN, this.map, this);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        for(Player player: this.players) {
            player.reset(this.getStartHand());
        }
        this.claimedRoutes = new ArrayList<>();
        Pair pair = new PairModel(deck.drawACard(), deck.drawACard());
        this.currentPlayer.startTurn(pair);
        for(RailroadBaronsObserver o: this.observers) {
            o.turnStarted(this, this.currentPlayer);
        }
    }

    /**
     * Starts a game with 3 computer players and the specified deck and map.
     * @param map The {@link RailroadMap} on which the game will be played.
     * @param deck The {@link Deck} of cards used to play the game. This may
     *             be ANY implementation of the {@link Deck} interface,
     *             meaning that a valid implementation of the
     *             {@link RailroadBarons} interface should use only the
     */
    @Override
    public void startAGameWith(RailroadMap map, Deck deck) {
        this.map = map;
        ComputerModel player2 = new ComputerModel(Baron.BLUE, this.map, this);
        ComputerModel player3 = new ComputerModel(Baron.YELLOW, this.map, this);
        ComputerModel player4 = new ComputerModel(Baron.GREEN, this.map, this);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        for(Player player: this.players) {
            player.reset(this.getStartHand());
        }
        this.claimedRoutes = new ArrayList<>();
        this.deck = deck;
        Pair pair = new PairModel(deck.drawACard(), deck.drawACard());
        this.currentPlayer.startTurn(pair);
        for(RailroadBaronsObserver o: this.observers) {
            o.turnStarted(this, this.currentPlayer);
        }
    }

    /**
     * Accessor for the game's map to be used by the computer player.
     * @return the map.
     */
    @Override
    public RailroadMap getRailroadMap() {
        return this.map;
    }

    /**
     * Accessor for the current size of the deck.
     * @return the number of cards remaining in the deck.
     */
    @Override
    public int numberOfCardsRemaining() {
        return this.deck.numberOfCardsRemaining();
    }

    /**
     *
     * @param row The row of a {@link Track} in the {@link Route} to check.
     * @param col The column of a {@link Track} in the {@link Route} to check.
     * @return
     */
    @Override
    public boolean canCurrentPlayerClaimRoute(int row, int col) {
        return (this.currentPlayer.canClaimRoute(this.map.getRoute(row, col)));
    }

    /**
     *
     * @param row The row of a {@link Track} in the {@link Route} to claim.
     * @param col The column of a {@link Track} in the {@link Route} to claim.
     * @throws RailroadBaronsException
     */
    @Override
    public void claimRoute(int row, int col) throws RailroadBaronsException {
        this.currentPlayer.claimRoute(this.map.getRoute(row, col));
        this.map.routeClaimed(this.map.getRoute(row, col));
        this.claimedRoutes.add(map.getRoute(row, col));
        this.endTurn();
    }

    /**
     * Called when the current player ends their turn
     */
    @Override
    public void endTurn() {
        this.players.remove(this.currentPlayer);
        this.players.add(this.currentPlayer);

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
            for(RailroadBaronsObserver o: this.observers) {
                o.turnStarted(this, this.currentPlayer);
            }
            this.currentPlayer.startTurn(new PairModel(deck.drawACard(), deck.drawACard()));
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
     * Accessor for current player
     * @return the current player whose turn it is
     */
    @Override
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Accessor for players
     * @return the list of players
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
