package test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yuparking.database.Database;
import yuparking.services.OccupancySimulator;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class OccupancySimulatorTest {

    private OccupancySimulator simulator;
    private Database db;

    @BeforeEach
    public void setUp(){
        db = new Database();
        simulator = new OccupancySimulator();
    }

    @Test
    public void testOccupancyUpdateDoesNotThrow(){
        assertDoesNotThrow(() -> simulator.simulateOccupancyUpdate());
    }

    @Test
    public void testOccupancyDataSizeRemainsTheSame(){
        List<String[]> before = db.retrieveData("parkingspaces");
        simulator.simulateOccupancyUpdate();
        List<String[]> after = db.retrieveData("parkingspaces");
        assertEquals(before.size(), after.size());
    }

    @Test
    public void testOccupancyColumnIsBoolean(){
        simulator.simulateOccupancyUpdate();
        List<String[]> data = db.retrieveData("parkingspaces");

        for (int i = 1;i<data.size(); i++){
            String occupied = data.get(i)[2];
            assertTrue(occupied.equals("true")||occupied.equals("false"));
        }
    }

    @Test
    public void testAtLeastOneOccupancyChange(){
        List<String[]> before = db.retrieveData("parkingspaces");
        simulator.simulateOccupancyUpdate();
        List<String[]> after = db.retrieveData("parkingspaces");

        boolean changed = false;
        for (int i = 1; i < before.size(); i++){
            if (!before.get(i)[2].equals(after.get(i)[2])){
                changed = true;
                break;
            }
        }

        assertTrue(changed);
    }

    @Test
    public void testMultipleUpdatesProduceDifferentResults(){
        simulator.simulateOccupancyUpdate();
        List<String[]> after1 = db.retrieveData("parkingspaces");

        simulator.simulateOccupancyUpdate();
        List<String[]> after2 = db.retrieveData("parkingspaces");

        boolean changed = false;
        for (int i = 1; i < after1.size(); i++){
            if (!after1.get(i)[2].equals(after2.get(i)[2])){
                changed = true;
                break;
            }
        }

        assertTrue(changed);
    }

    @Test
    public void testNoNullValuesAfterUpdate(){
        simulator.simulateOccupancyUpdate();
        List<String[]> data = db.retrieveData("parkingspaces");

        for (int i = 1; i < data.size(); i++){
            assertNotNull(data.get(i)[2]);
        }
    }

    @Test
    public void testSimulateMultipleTimesDoesNotCrash(){
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> simulator.simulateOccupancyUpdate());
        }
    }

    @Test
    public void testHeaderRowNotModified(){
        List<String[]> before = db.retrieveData("parkingspaces");
        String[] headerBefore = before.get(0).clone();

        simulator.simulateOccupancyUpdate();
        List<String[]> after = db.retrieveData("parkingspaces");

        assertArrayEquals(headerBefore, after.get(0));
    }

    @Test
    public void testCorrectOccupancyColumnIndex(){
        simulator.simulateOccupancyUpdate();
        List<String[]> data = db.retrieveData("parkingspaces");

        for (int i = 1; i < data.size(); i++) {
            assertEquals(3, data.get(i).length);
        }
    }

    @Test
    public void testOccupancySimulatorInstantiation(){
        OccupancySimulator sim = new OccupancySimulator();
        assertNotNull(sim);
    }
}


