package student;

import model.Baron;
import model.Orientation;
import model.Space;
import model.Station;
import model.Track;

import java.util.*;

/**
 * Representation of a route connecting two stations on the board
 * @author Ben Donahue
 */
public class RouteModel implements model.Route {
    private Baron baron;
    private int length;
    private Station origin;
    private Station destination;
    private Orientation orientation;
    private List<Track> tracks;
    private boolean easternMost;
    private boolean southernMost;
    private boolean northernMost;
    private boolean westernMost;

    /**
     * Constructor for a route
     * @param length the length of the route
     * @param origin the station at the start of the route
     * @param destination the station at the end of the route
     * */
    public RouteModel(int length, Station origin, Station destination) {
        this.length = length;
        this.origin = origin;
        this.destination = destination;
        this.baron = Baron.UNCLAIMED;
        this.tracks = new ArrayList<>();

        if(this.origin.getCol() == this.destination.getCol()) {
            this.orientation = Orientation.VERTICAL;
        }
        else if (this.origin.getRow() == this.destination.getRow()){
            this.orientation = Orientation.HORIZONTAL;
        }
        for(int i = 1; i < this.length + 1; i++) {
            if(this.orientation == Orientation.VERTICAL) {
                TrackModel track = new TrackModel(this.orientation, null,
                        this, i + this.origin.getRow(), this.origin.getCol());
                tracks.add(track);
            }

            else if(this.orientation == Orientation.HORIZONTAL) {
                TrackModel track = new TrackModel(this.orientation, null,
                        this, this.origin.getRow(), i + this.origin.getCol());
                tracks.add(track);
            }
        }
    }

    /**
     * Accessor for the route owner
     * @return the owner of the route, if any
     */
    @Override
    public Baron getBaron() {
        return this.baron;
    }

    /**
     * Accessor for the route origin
     * @return the origin of the route
     */
    @Override
    public Station getOrigin() {
        return this.origin;
    }

    /**
     * Accessor for the route destination
     * @return the destination of the route
     */
    @Override
    public Station getDestination() {
        return this.destination;
    }

    /**
     * Accessor for the orientation of the route
     * @return the orientation of the route
     */
    @Override
    public Orientation getOrientation() {
        return this.orientation;
    }

    /**
     * Accessor for the track objects on the route
     * @return the list of tracks on the route
     */
    @Override
    public List<Track> getTracks() {
        return this.tracks;
    }

    /**
     * Accessor for the length of the route
     * @return the length of the route
     */
    @Override
    public int getLength() {
        return this.length;
    }

    /**
     * Set the amount of points per route and return the point value on the route.
     * @return how many points the route is worth
     */
    @Override
    public int getPointValue() {
        int points = 0;
        switch(this.length) {
            case 1:
                points = 1;
                break;
            case 2:
                points = 2;
                break;
            case 3:
                points = 4;
                break;
            case 4:
                points = 7;
                break;
            case 5:
                points = 10;
                break;
            case 6:
                points = 15;
                break;
            default:
                points = (5 * (this.length - 3));
        }
        return points;
    }

    /**
     * Checks to see if the specified space is on the route.
     * @param space The {@link Space} that may be in this route.
     *
     * @return whether or not the space is on the route
     */
    @Override
    public boolean includesCoordinate(Space space) {
        for(Track track: tracks) {
            if((space.getCol() == track.getCol()) && (space.getRow() == track.getRow())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if the route has been claimed. If no baron has claimed the route, the specified baron
     * claims it.
     * @param claimant The {@link Baron} attempting to claim the route. Must
     *                 not be null or {@link Baron#UNCLAIMED}.
     * @return whether or not the route has been claimed.
     */
    @Override
    public boolean claim(Baron claimant) {
        if(this.baron.equals(Baron.UNCLAIMED)) {
            this.baron = claimant;
            for(Track track: this.tracks) {
                TrackModel t = (TrackModel)track;
                t.setBaron(claimant);

            }
            return true;
        }

        return false;
    }

    public void setAsDirectionMost(String direction) {
        switch(direction) {
            case "s":
                this.southernMost = true;
                break;

            case "e":
                this.easternMost = true;
                break;

            case "n":
                this.northernMost = true;
                break;

            case "w":
                this.westernMost = true;
                break;

            default:
                break;
        }
    }
}
