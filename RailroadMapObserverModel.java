package student;

import model.RailroadMap;
import model.Route;
/**
 * The interface that should be implemented by a class that observes a Railroad Barons map for significant events.
 * @author Lauren Baldino
 */
public class RailroadMapObserverModel implements model.RailroadMapObserver {
    @Override
    public void routeClaimed(RailroadMap map, Route route) {
        /**
         * Called when a route is successfully claimed on the map.
         * @param map The RailroadMap on which the Route has been claimed.
         * @param route The Route that has been claimed.
         */

    }
}
