package student;

import model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Model for the Railroad map and its observers.
 * @author Ben Donahue
 */
public class RailroadBaronsMapModel implements RailroadMap {
    private int rows;
    private int cols;
    private List<RailroadMapObserver> observers;
    private List<Route> routes;
    private List<Route> southernMostRoutes;
    private List<Route> easternMostRoutes;
    private List<Route> northernMostRoutes;
    private List<Route> westernMostRoutes;
    private HashMap<Integer, Station> stations;

    /**
     * Builds a railroad map based of an amount of rows and columns
     * @param rows the amount of rows the board will have
     * @param cols the amount of columns the board will have
     */
    public RailroadBaronsMapModel(int rows, int cols, List<Route> routes, HashMap<Integer, Station> stations) {
        this.observers = new ArrayList<>();
        this.stations = stations;
        this.routes = routes;
        this.easternMostRoutes = new ArrayList<>();
        this.southernMostRoutes = new ArrayList<>();
        this.rows = rows;
        this.cols = cols;

        for(Route route: this.routes) {
            if(route.getDestination().getRow() == this.rows - 1) {
                this.southernMostRoutes.add(route);
            }

            if(route.getDestination().getCol() == this.cols - 1) {
                this.easternMostRoutes.add(route);
            }
        }
    }

    /**
     * Adds an observer to the list of observers.
     * @param observer The {@link RailroadMapObserver} being added to the map.
     */
    @Override
    public void addObserver(RailroadMapObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     * @param observer The observer to remove from the collection of
     *                 registered observers that will be notified of
     */
    @Override
    public void removeObserver(RailroadMapObserver observer) {
        for(int i = 0; i < observers.size(); i++) {
            if(observers.get(i).equals(observer)) {
                observers.remove(i);
            }
        }
    }

    /**
     * Accessor for the amount of rows on the map
     * @return the amount of rows on the map
     */
    @Override
    public int getRows() {
        return this.rows;
    }

    /**
     * Accessor for the amount of columns on the map
     * @return the amount of columns on the map
     */
    @Override
    public int getCols() {
        return this.cols;
    }

    /**
     * Returns the track value at a specific coordinate, returns null if there is
     * no track there.
     * @param row The row of the desired {@link Space}.
     * @param col The column of the desired {@link Space}.
     *
     * @return the value of the track at the space.
     */
    @Override
    public Space getSpace(int row, int col) {
        for(Route route: this.routes) {
            for(Track track: route.getTracks()) {
                if(track.getCol() == col && track.getRow() == row) {
                    return track;
                }
            }
        }
        Station station;
        for(Integer key: this.stations.keySet()) {
            station = this.stations.get(key);
            if(station.getRow() == row && station.getCol() == col) {
                return station;
            }
        }
        return null;
    }

    /**
     * Gets the route a specific track is part of at the specified coordinates.
     * @param row The row of the location of one of the {@link Track tracks}
     *            in the route.
     * @param col The column of the location of one of the
     * {@link Track tracks} in the route.
     *
     * @return the route the track belongs to at the specified coordinates.
     */
    @Override
    public Route getRoute(int row, int col) {
        for(Route route: routes) {
            for (Track track : route.getTracks()) {
                if (track.getRow() == row && track.getCol() == col) {
                    return track.getRoute();
                }
            }
        }
        return null;
    }

    /**
     * Lets all observers know that a route has been claimed.
     * @param route The {@link Route} that has been claimed.
     */
    @Override
    public void routeClaimed(Route route) {
        for(RailroadMapObserver o: observers) {
            o.routeClaimed(this, route);
        }
    }

    /**
     * Checks the length of the shortest unclaimed route to later be used to determine if a player
     * can continue playing.
     * @return the length of the smallest route
     */
    @Override
    public int getLengthOfShortestUnclaimedRoute() {
        if(routes.size() > 0) {
            Route smallest = routes.get(0);
            for (Route route : routes) {
                if (route.getLength() < smallest.getLength() && route.getBaron().equals(Baron.UNCLAIMED)) {
                    smallest = route;
                }
            }
            return smallest.getLength();
        }
        return -1;
    }

    /**
     * Accessor for the list of routes
     * @return the list of routes
     */
    @Override
    public Collection<Route> getRoutes() {
        return this.routes;
    }
}
