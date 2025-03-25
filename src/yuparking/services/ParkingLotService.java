package yuparking.services;
import yuparking.database.Database;

import java.util.List;

public class ParkingLotService {
    private final Database db;

    public ParkingLotService() {
        this.db = new Database();
    }

    public void viewParkingLots() {
        List<String[]> lots = db.retrieveData("parkinglots");
        System.out.println("\n--- Parking Lots ---");
        for (int i = 1; i < lots.size(); i++) { // skip header
            String[] row = lots.get(i);
            System.out.println("LotID: " + row[0] + " | Location: " + row[1] +
                    " | Capacity: " + row[2] + " | Status: " + row[3]);
        }
    }

    public void enableLot(int lotID) {
        List<String[]> lots = db.retrieveData("parkinglots");
        for (int i = 1; i < lots.size(); i++) {
            String[] row = lots.get(i);
            if (Integer.parseInt(row[0]) == lotID) {
                row[3] = "active";
                db.confirmUpdate("parkinglots", lots);
                System.out.println("Lot " + lotID + " enabled.");
                return;
            }
        }
        System.out.println("Lot not found.");
    }

    public void disableLot(int lotID) {
        List<String[]> lots = db.retrieveData("parkinglots");
        for (int i = 1; i < lots.size(); i++) {
            String[] row = lots.get(i);
            if (Integer.parseInt(row[0]) == lotID) {
                row[3] = "disabled";
                db.confirmUpdate("parkinglots", lots);
                System.out.println("Lot " + lotID + " disabled.");
                return;
            }
        }
        System.out.println("Lot not found.");
    }

    public void addSpace(int lotID, int numberOfSpaces) {
        List<String[]> spaces = db.retrieveData("parkingspaces");
        int nextSpaceId = spaces.size() + 100; // spaceID generation logic start
    
        for (int i = 0; i < numberOfSpaces; i++) {
            String[] newSpace = new String[]{
                    String.valueOf(nextSpaceId + i),
                    String.valueOf(lotID),
                    "false"
            };
            spaces.add(newSpace);
        }
    
        db.confirmUpdate("parkingspaces", spaces);
        System.out.println(numberOfSpaces + " space(s) added to lot " + lotID);
    }
    
    

    public void removeSpace(int spaceID) {
        List<String[]> spaces = db.retrieveData("parkingspaces");
        boolean removed = spaces.removeIf(row -> row[0].equals(String.valueOf(spaceID)));
        if (removed) {
            db.confirmUpdate("parkingspaces", spaces);
            System.out.println("Space " + spaceID + " removed.");
        } else {
            System.out.println("Space not found.");
        }
    }
}