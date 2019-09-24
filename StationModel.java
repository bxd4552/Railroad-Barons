package student;

import model.Space;

/**
 * Representation of a station on the board
 * @author Ben Donahue
 */
public class StationModel implements model.Station {
    private String name;
    private int col;
    private int row;


    /**
     * Constructor for a StationModel
     * @param name the name of the station
     * @param row the row the station is on
     * @param col the column the station is on
     */
    public StationModel(int row, int col, String name) {
        this.col = col;
        this.row = row;
        this.name = name;
    }

    /**
     * Get the name of the station
     * @return the name of the station
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Get which row the station is on
     * @return the row the station is on
     */
    @Override
    public int getRow() {
        return this.row;
    }

    /**
     * Get which column the station is on
     * @return the column the station is on
     */
    @Override
    public int getCol() {
        return this.col;
    }

    /**
     * Check to see if a space is already taken.
     * @param other The other space to which this space is being compared for
     *              collocation.
     * @return whether or not the space is already taken
     */
    @Override
    public boolean collocated(Space other) {
        if((this.row == other.getRow()) && (this.col == other.getCol())) {
            return true;
        }
        return false;
    }
}
