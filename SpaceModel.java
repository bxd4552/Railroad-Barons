package student;

/**
 * Representation of a space on the board
 * @author Ben Donahue
 */
public class SpaceModel implements model.Space {
    private int row;
    private int col;

    /**
     * Constructor for a space
     * @param row the row the space is on
     * @param col the column the space is on
     */
    public SpaceModel(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Get which row the space is on
     * @return the row of the space
     */
    @Override
    public int getRow() {
        return this.row;
    }

    /**
     * Get which column the space is on
     * @return the column of the space
     */
    @Override
    public int getCol() {
        return this.col;
    }

    /**
     * Checks to see if a space is already taken
     * @param other The other space to which this space is being compared for
     *              collocation.
     * @return whether or not the space has been taken
     */
    @Override
    public boolean collocated(model.Space other) {
        if((this.row == other.getRow() && (this.col == other.getCol()))) {
            return true;
        }
        return false;
    }
}
