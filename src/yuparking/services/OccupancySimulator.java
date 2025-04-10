package yuparking.services;

import yuparking.database.Database;
import java.util.List;
import java.util.Random;

public class OccupancySimulator {
    private final Database db;
    private final Random random;

    public OccupancySimulator() {
        this.db = new Database();
        this.random = new Random();
    }

    public void simulateOccupancyUpdate() {
        List<String[]> spaces = db.retrieveData("parkingspaces");

        for (int i = 1; i < spaces.size(); i++) {  // Skip header
            String[] row = spaces.get(i);

            boolean occupied = random.nextBoolean();
            row[2] = String.valueOf(occupied);
        }

        db.confirmUpdate("parkingspaces", spaces);
    }
}
