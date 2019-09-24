package student;

import model.RailroadBaronsException;
import model.RailroadMap;
import model.Route;
import model.Station;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Interface for a class that can load and save maps.
 * @author Lauren Baldino, Ben Donahue
 */

public class MapMakerModel implements model.MapMaker {

    private int easternMost;
    private int southernMost;
    private HashMap<Integer, Station> stations;
    private List<Route> routes;

    /**
     * Default Constructor for a MapMaker, sets all values to their starting values
     */
    public MapMakerModel() {
        this.easternMost = 0;
        this.southernMost = 0;
        this.stations = new HashMap<>();
        this.routes = new ArrayList<>();
    }
    @Override
    /**
     * Loads a map using the data in the given input stream.
     * @param in The InputStream used to read the map data.
     * @return The map read from the given InputStream.
     * @throws RailroadBaronsException If there are any problems reading the data from the InputStream.
     */
    public RailroadMap readMap(InputStream in) throws RailroadBaronsException {
        boolean routesRead = false;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = "test";
            String lineTokens[];

            //read in the stations
            while(!line.equals(null)) {
                line = br.readLine();

                if(line.equals("##ROUTES##")) {
                    routesRead = true;
                    continue;
                }
                if(!routesRead) {
                    lineTokens = line.split("\\s+");
                    if (Integer.parseInt(lineTokens[2]) > this.easternMost) {
                        this.easternMost = Integer.parseInt(lineTokens[2]);
                    }

                    if (Integer.parseInt(lineTokens[1]) > this.southernMost) {
                        this.southernMost = Integer.parseInt(lineTokens[1]);
                    }

                    //can't put stationModel here, needs a station object
                    stations.put(Integer.parseInt(lineTokens[0]),
                            new StationModel(Integer.parseInt(lineTokens[1]),
                                    Integer.parseInt(lineTokens[2]), lineTokens[3]));
                }

                //read in the routes
                else {
                    lineTokens = line.split("\\s+");
                    Station origin = findStation(Integer.parseInt(lineTokens[0]));
                    Station destination = findStation(Integer.parseInt(lineTokens[1]));
                    int length = this.setLength(origin, destination);
                    routes.add(new RouteModel(length, origin, destination));
                }
            }
            return new RailroadBaronsMapModel(this.southernMost + 1, this.easternMost + 1, this.routes, this.stations);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        //used to return the map when the bottom of the file is reached
        catch (NullPointerException npe) {
            return new RailroadBaronsMapModel(this.southernMost + 1, this.easternMost + 1, this.routes, this.stations);
        }
        return null;
    }

    @Override
    /**
     * Writes the specified map in the Railroad Barons map file format to the given output stream.
     * The written map should include an accurate record of any routes that have been claimed, and by which Baron.
     * @param map The map to write out to the OutputStream.
     * @param out The OutputStream to which the map data should be written.
     * @throws RailroadBaronsException If there are any problems writing the data to the OutputStream.
     */
    public void writeMap(RailroadMap map, OutputStream out) throws RailroadBaronsException {
        PrintWriter printer = new PrintWriter(out);

        //write the stations out to the file
        for(Integer key: stations.keySet()) {
            printer.println(key + " " + stations.get(key).getRow() + " " +
                    stations.get(key).getCol() + " " + stations.get(key).getName());
        }
        printer.println("##ROUTES##");

        //write the routes out to the file
        for(Route route: routes) {
            int originNum = 0;
            int destinationNum = 0;
            for(Integer key: stations.keySet()) {
                if(stations.get(key) == route.getOrigin()) {
                    originNum = key;
                }

                else if(stations.get(key) == route.getDestination()) {
                    destinationNum = key;
                }
            }
            printer.println(originNum + " " + destinationNum + " " + route.getBaron());
        }
    }

    /**
     *
     * @param stationNum the number associated with the station to retrieve
     * @return the station associated with the station num
     */
    public Station findStation(int stationNum) {
        for(Integer key: stations.keySet()) {
            if(key == stationNum) {
                return stations.get(key);
            }
        }
        return null;
    }

    /**
     * Sets the length of the route based off of the location of the origin and destination.
     * @param origin the station at the origin of the route
     * @param destination the station at the end of the route
     * @return
     */
    public int setLength(Station origin, Station destination) {
        int length = 0;
        if(origin.getCol() != destination.getCol()) {
            length = Math.abs(origin.getCol() - destination.getCol());
        }

        else if(origin.getRow() != destination.getRow()) {
            length = Math.abs(origin.getRow() - destination.getRow());
        }

        return length - 1;
    }
}
