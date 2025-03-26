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
        for (int i = 1; i < lots.size(); i++) {
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
        List<String[]> lots = db.retrieveData("parkinglots");
        boolean lotExistsAndActive = false;

        for (int i = 1; i < lots.size(); i++) {
            String[] row = lots.get(i);
            if (Integer.parseInt(row[0]) == lotID && row[3].equalsIgnoreCase("active")) {
                lotExistsAndActive = true;
                break;
            }
        }

        if (!lotExistsAndActive) {
            System.out.println("Lot ID " + lotID + " either does not exist or is not active.");
            return;
        }

        List<String[]> spaces = db.retrieveData("parkingspaces");
        int nextSpaceId = spaces.stream()
                .skip(1)
                .mapToInt(row -> Integer.parseInt(row[0]))
                .max()
                .orElse(100) + 1;

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

    //Super manager privilege: Add new parking lot
    public void addNewParkingLot(int lotID, String location, int capacity) {
        List<String[]> lots = db.retrieveData("parkinglots");

        for (int i = 1; i < lots.size(); i++) {
            String[] row = lots.get(i);
            if (Integer.parseInt(row[0]) == lotID) {
                System.out.println("Lot with ID " + lotID + " already exists.");
                return;
            }
        }

        String[] newLot = new String[]{
                String.valueOf(lotID),
                location,
                String.valueOf(capacity),
                "active"
        };

        lots.add(newLot);
        db.confirmUpdate("parkinglots", lots);
        System.out.println("New parking lot added with ID: " + lotID);
    }

    //Super manager privilege: Remove parking lot
    public void removeParkingLot(int lotID) {
        List<String[]> lots = db.retrieveData("parkinglots");
        boolean found = false;

        for (int i = 1; i < lots.size(); i++) {
            String[] row = lots.get(i);
            if (Integer.parseInt(row[0]) == lotID) {
                lots.remove(i);
                found = true;
                break;
            }
        }

        if (found) {
            db.confirmUpdate("parkinglots", lots);
            System.out.println("Parking lot " + lotID + " removed.");
        } else {
            System.out.println("Lot not found.");
        }
    }


    //Update space occupancy status manually (occupied, vacant, maintenance)
    public void updateSpaceStatus(int spaceID, String newStatus) {
        List<String[]> spaces = db.retrieveData("parkingspaces");
        boolean found = false;
        for (int i = 1; i < spaces.size(); i++) {
            String[] row = spaces.get(i);
            if (Integer.parseInt(row[0]) == spaceID) {
                switch (newStatus.toLowerCase()) {
                    case "occupied":
                        row[2] = "true";
                        break;
                    case "vacant":
                        row[2] = "false";
                        break;
                    case "maintenance":
                        row[2] = "maintenance";
                        break;
                    default:
                        System.out.println("Invalid status. Use occupied, vacant, or maintenance.");
                        return;
                }
                found = true;
                break;
            }
        }

        if (found) {
            db.confirmUpdate("parkingspaces", spaces);
            System.out.println("Space " + spaceID + " updated to status: " + newStatus);
        } else {
            System.out.println("Space ID not found.");
        }
    }
}
