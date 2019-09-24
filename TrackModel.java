package student;

import model.Baron;
import model.Orientation;
import model.Route;
import model.Space;

/**
 * Representation of a segment of a track on the map. Tracks combine to form routes.
 * @author Lauren Baldino
 */

public class TrackModel implements model.Track {
    private Orientation orientation;
    private Baron baron;
    private Route route;
    private int row;
    private int column;

    /**
     * Constructor for a track
     * @param orientation the orientation of the track
     * @param baron the owner of the track
     * @param route the route that the track belongs to
     * @param row the row number that the track is in
     * @param column the column number that the track is in
     * */
    public TrackModel(Orientation orientation, Baron baron, Route route, int row, int column) {
        this.orientation = orientation;
        this.baron = Baron.UNCLAIMED;
        this.route = route;
        this.row = row;
        this.column = column;
    }

    @Override
    /**
     * Accessor for the track orientation
     * @return the track orientation
     */
    public Orientation getOrientation() {
        return this.orientation;
    }

    @Override
    /**
     * Accessor for the route baron
     * @return the baron of the route
     */
    public Baron getBaron() {
        return this.baron;
    }

    @Override
    /**
     * Accessor for the route
     * @return the route
     */
    public Route getRoute() {
        return this.route; }

    @Override
    /**
     * Accessor for the row of the track
     * @return the row number for the track
     */
    public int getRow() {
        return this.row;
    }

    @Override
    /**
     * Accessor for the column of the track
     * @return the column number for the track
     */
    public int getCol() {
        return this.column;
    }

    @Override
    /**
     * Accessor for track that is co-located in a row and column
     * @return true if they are co-located, false if not
     */
    public boolean collocated(Space other) {
        if ((this.row == other.getRow() && (this.column == other.getCol()))){
            return true;
        }
        return false;
    }

    public void setBaron(Baron baron) {
        this.baron = baron;
    }

}

