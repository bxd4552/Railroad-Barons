package student;

import model.Station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that would have been used for graph implementation, unfinished
 * @author Ben Donahue
 */
public class Graph {
    private Map<Station, List<Station>> neighbors;

    public Graph() {
        this.neighbors = new HashMap<>();
    }

    public void addStation(Station station) {
    }

    public List<Station> getVertex(Station station) {
        for(Station node: this.neighbors.keySet()) {
            if(node.equals(station)) {
                return this.neighbors.get(node);
            }
        }
        return null;
    }

    public Map<Station, List<Station>> getVertices() {
        return this.neighbors;
    }

    public void connect(Station a, Station b) {

    }
}

