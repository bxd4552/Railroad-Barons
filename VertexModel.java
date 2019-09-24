package student;

import model.Station;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that would have been used for graph implementation, unfinished
 * @author Ben Donahue
 */
public class VertexModel {
    private List<Station> neighbors;
    private Station station;

    public VertexModel(Station station) {
        this.neighbors = new ArrayList<>();
        this.station = station;
    }
    public Station getStation() {
        return this.station;
    }

    public List<Station> getNeighbors() {
        return this.neighbors;
    }

    public void addNeighbor(Station station) {
        this.neighbors.add(station);
    }
}
