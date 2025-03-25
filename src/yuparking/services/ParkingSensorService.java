package yuparking.services;

import yuparking.database.Database;
import java.util.List;
import java.util.Random;

public class ParkingSensorService {
    private final Database db;
    private final Random random = new Random();

    public ParkingSensorService() {
        this.db = new Database();
    }

    public void simulateOccupancyUpdate() {
        List<String[]> spaces = db.retrieveData("parkingspaces");

        for (int i = 1; i < spaces.size(); i++) {
            String[] row = spaces.get(i);

            // Randomly decide if this space is occupied or not
            boolean newStatus = random.nextBoolean();
            row[2] = String.valueOf(newStatus);
        }

        db.confirmUpdate("parkingspaces", spaces);
        System.out.println("Occupancy statuses updated from simulated sensors.");
        triggerOccupancyAlerts();
    }

    public void triggerOccupancyAlerts() {
        List<String[]> lots = db.retrieveData("parkinglots");
        List<String[]> spaces = db.retrieveData("parkingspaces");

        for (int i = 1; i < lots.size(); i++) {
            String[] lotRow = lots.get(i);
            int lotId = Integer.parseInt(lotRow[0]);
            long totalSpaces = spaces.stream().skip(1)
                    .filter(row -> Integer.parseInt(row[1]) == lotId).count();
            long occupiedSpaces = spaces.stream().skip(1)
                    .filter(row -> Integer.parseInt(row[1]) == lotId && row[2].equals("true")).count();

            double occupancyRate = (occupiedSpaces * 100.0) / totalSpaces;

            if (occupancyRate >= 90) {
                System.out.println("ALERT: Lot " + lotId + " occupancy at " + (int)occupancyRate + "%");
            }
        }
    }
}
