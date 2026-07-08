import java.util.*;

class Station {
    String id;
    String name;
    String line;

    public Station(String id, String name, String line) {
        this.id = id;
        this.name = name;
        this.line = line;
    }
}

class MetroEngineService {
    private static final double BASE_FARE = 10.0;
    private static final double PER_STATION_FARE = 2.5;

    private final Map<String, List<String>> networkGraph = new HashMap<>();
    private final Map<String, Station> stations = new HashMap<>();

    public void addStation(Station station) {
        stations.put(station.id, station);
        networkGraph.putIfAbsent(station.id, new ArrayList<>());
    }

    public void connectStations(String sourceId, String destId) {
        if (networkGraph.containsKey(sourceId) && networkGraph.containsKey(destId)) {
            networkGraph.get(sourceId).add(destId);
            networkGraph.get(destId).add(sourceId);
        }
    }

    public int calculateShortestDistance(String sourceId, String destId) {
        if (sourceId.equals(destId)) return 0;
        if (!networkGraph.containsKey(sourceId) || !networkGraph.containsKey(destId)) return -1;

        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> distanceMap = new HashMap<>();

        queue.add(sourceId);
        distanceMap.put(sourceId, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDist = distanceMap.get(current);

            if (current.equals(destId)) {
                return currentDist;
            }

            for (String neighbor : networkGraph.get(current)) {
                if (!distanceMap.containsKey(neighbor)) {
                    distanceMap.put(neighbor, currentDist + 1);
                    queue.add(neighbor);
                }
            }
        }
        return -1;
    }

    public double calculateFare(String sourceId, String destId) {
        int stationsCount = calculateShortestDistance(sourceId, destId);
        if (stationsCount == -1) {
            throw new IllegalArgumentException("Route not found between the given stations.");
        }
        return BASE_FARE + (stationsCount * PER_STATION_FARE);
    }
}

public class MetroEngineApplication {
    public static void main(String[] args) {
        MetroEngineService metroEngine = new MetroEngineService();

        Station s1 = new Station("A1", "Samaypur Badli", "Yellow");
        Station s2 = new Station("A2", "Azadpur", "Yellow");
        Station s3 = new Station("A3", "Rajiv Chowk", "Yellow/Blue");
        Station s4 = new Station("B1", "Laxmi Nagar", "Blue");
        Station s5 = new Station("B2", "Preet Vihar", "Blue");

        metroEngine.addStation(s1);
        metroEngine.addStation(s2);
        metroEngine.addStation(s3);
        metroEngine.addStation(s4);
        metroEngine.addStation(s5);

        metroEngine.connectStations("A1", "A2");
        metroEngine.connectStations("A2", "A3");
        metroEngine.connectStations("A3", "B1");
        metroEngine.connectStations("B1", "B2");

        String startStation = "A1";
        String endStation = "B2";

        try {
            int totalStations = metroEngine.calculateShortestDistance(startStation, endStation);
            double totalFare = metroEngine.calculateFare(startStation, endStation);

            System.out.println("=== METRO ENGINE ROUTE DETAILS ===");
            System.out.println("Source Station ID: " + startStation);
            System.out.println("Destination Station ID: " + endStation);
            System.out.println("Total Stations in between: " + totalStations);
            System.out.println("Calculated Fare: ₹" + totalFare);
            System.out.println("==================================");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}