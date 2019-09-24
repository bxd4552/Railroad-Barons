package student;

import model.Player;
/**
 * Implemented by classes that should be notified whenever a Player of interest changes in some way.
 * @author Lauren Baldino
 */
public class PlayerObserverModel implements model.PlayerObserver {
    //private Player player;

    @Override
    public void playerChanged(Player player) {
        /**
         * Called whenever the player of interest has changed in some way.
         * @param player The player of interest
         */
        //this.player = player;
        //player.addPlayerObserver(this);

    }
}
