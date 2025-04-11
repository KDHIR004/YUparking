package test.service;

import yuparking.services.ParkingSensorService;
import yuparking.database.Database;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestParkingSensorService {

    private ParkingSensorService sensorService;
    private Database db;

    @BeforeEach
    void setUp() {
        sensorService = new ParkingSensorService();
        db = new Database();
    }

    // Test 1: Simulation runs without errors
    @Test
    void testSimulationRuns() {
        assertDoesNotThrow(() -> sensorService.simulateOccupancyUpdate());
    }

    // Test 2: Alert trigger runs without errors
    @Test
    void testAlertRuns() {
        assertDoesNotThrow(() -> sensorService.triggerOccupancyAlerts());
    }

    // Test 3: After simulation, each row in parkingspaces has exactly 3 columns
    @Test
    void testStructureAfterSimulation() {
        sensorService.simulateOccupancyUpdate();
        List<String[]> spaces = db.retrieveData("parkingspaces");

        for (String[] row : spaces) {
            assertEquals(3, row.length, "Each space row must have 3 columns");
        }
    }

    // Test 4: Occupancy values should be either true or false
    @Test
    void testOccupancyValuesValid() {
        sensorService.simulateOccupancyUpdate();
        List<String[]> spaces = db.retrieveData("parkingspaces");

        for (int i = 1; i < spaces.size(); i++) {
            String value = spaces.get(i)[2];
            assertTrue(value.equals("true") || value.equals("false"),
                    "isOccupied must be 'true' or 'false'");
        }
    }

    // Test 5: Occupancy percentage per lot should be between 0 and 100
    @Test
    void testOccupancyRateWithinBounds() {
        sensorService.simulateOccupancyUpdate();
        List<String[]> lots = db.retrieveData("parkinglots");
        List<String[]> spaces = db.retrieveData("parkingspaces");

        for (int i = 1; i < lots.size(); i++) {
            int lotId = Integer.parseInt(lots.get(i)[0]);
            long total = spaces.stream().skip(1).filter(s -> Integer.parseInt(s[1]) == lotId).count();
            long occupied = spaces.stream().skip(1)
                    .filter(s -> Integer.parseInt(s[1]) == lotId && s[2].equals("true")).count();

            double rate = total == 0 ? 0 : (occupied * 100.0) / total;
            assertTrue(rate >= 0 && rate <= 100, "Occupancy percentage must be between 0 and 100");
        }
    }
    // Test 6: Each parking lot must have at least one parking space
    @Test
    void testEachLotHasSpaces() {
        List<String[]> lots = db.retrieveData("parkinglots");
        List<String[]> spaces = db.retrieveData("parkingspaces");

        for (int i = 1; i < lots.size(); i++) { // start from 1 to skip header
            int lotId = Integer.parseInt(lots.get(i)[0]);
            boolean found = spaces.stream()
                .skip(1) // also skip header here
                .anyMatch(s -> Integer.parseInt(s[1]) == lotId);
            assertTrue(found, "Lot " + lotId + " should have at least one space");
        }
    }
    

    // Test 7: Whole update and alert pipeline should not throw errors
    @Test
    void testPipelineSimulationAndAlert() {
        assertDoesNotThrow(() -> {
            sensorService.simulateOccupancyUpdate();
            sensorService.triggerOccupancyAlerts();
        });
    }
}