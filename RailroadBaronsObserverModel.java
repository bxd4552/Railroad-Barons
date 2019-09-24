package student;

import model.Player;
import model.RailroadBarons;
/**
 * Interface for a class that should be notified whenever the state of a Railroad Barons game changes.
 * @author Lauren Baldino
 */

public class RailroadBaronsObserverModel implements model.RailroadBaronsObserver {
    @Override
    public void turnStarted(RailroadBarons game, Player player) {
        /**
         * Called when a player's turn has started.
         * @param game The RailroadBarons game for which a new turn has started.
         * @param player The Player that has just started a turn.
         */

    }

    @Override
    public void turnEnded(RailroadBarons game, Player player) {
        /**
         * Called when a player's turn has ended.
         * @param game The RailroadBarons game for which the current turn has ended.
         * @param player The Player whose turn has ended.
         */

    }

    @Override
    public void gameOver(RailroadBarons game, Player winner) {
        /**
         * Called when the Railroad Barons game is over.
         * @param game The RailroadBarons game that has ended.
         * @param winner The winning Player.
         */

    }
}
