package test.service;
import yuparking.services.*;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
public class ParkingLotServiceTest {
   private ParkingLotService service;
   private final Path parkinglotsPath = Paths.get("src/yuparking/data/parkinglots.csv");
   private final Path parkingspacesPath = Paths.get("src/yuparking/data/parkingspaces.csv");
   private final Path parkinglotsBackup = Paths.get("src/yuparking/data/parkinglots_backup.csv");
   private final Path parkingspacesBackup = Paths.get("src/yuparking/data/parkingspaces_backup.csv");
   @BeforeEach
   public void setUp() throws IOException{
       Files.copy(parkinglotsPath, parkinglotsBackup, StandardCopyOption.REPLACE_EXISTING);
       Files.copy(parkingspacesPath, parkingspacesBackup, StandardCopyOption.REPLACE_EXISTING);
       Files.write(parkinglotsPath, List.of(
               "LotID,Location,Capacity,Status",
               "201,Main Lot,100,active",
               "202,Overflow,50,disabled"
       ));
       Files.write(parkingspacesPath, List.of(
               "SpaceID,LotID,Occupied",
               "300,201,false"
       ));
       service = new ParkingLotService();
   }
   @AfterEach
   public void tearDown() throws IOException{
       Files.move(parkinglotsBackup, parkinglotsPath, StandardCopyOption.REPLACE_EXISTING);
       Files.move(parkingspacesBackup, parkingspacesPath, StandardCopyOption.REPLACE_EXISTING);
   }
   @Test
   public void testEnableLot(){
       service.enableLot(202);
       List<String> lines = readLines(parkinglotsPath);
       assertTrue(lines.stream().anyMatch(line -> line.contains("202") && line.contains("active")));
   }
   @Test
   public void testDisableLot(){
       service.disableLot(201);
       List<String> lines = readLines(parkinglotsPath);
       assertTrue(lines.stream().anyMatch(line -> line.contains("201") && line.contains("disabled")));
   }
   @Test
   public void testAddSpace(){
       service.addSpace(201, 2);
       List<String> lines = readLines(parkingspacesPath);
       assertEquals(4, lines.size());
   }
   @Test
   public void testRemoveSpace(){
       service.removeSpace(300);
       List<String> lines = readLines(parkingspacesPath);
       assertEquals(1, lines.size());
   }
   @Test
   public void testAddNewParkingLot(){
       service.addNewParkingLot(203, "Garage", 200);
       List<String> lines = readLines(parkinglotsPath);
       assertTrue(lines.stream().anyMatch(line -> line.contains("203") && line.contains("Garage")));
   }
   @Test
   public void testUpdateSpaceStatusOccupied(){
       service.updateSpaceStatus(300, "occupied");
       List<String> lines = readLines(parkingspacesPath);
       assertTrue(lines.get(1).contains("true"));
   }
   @Test
   public void testUpdateSpaceStatusVacant() {
       service.updateSpaceStatus(300, "vacant");
       List<String> lines = readLines(parkingspacesPath);
       assertTrue(lines.get(1).contains("false"));
   }
   @Test
   public void testUpdateSpaceStatusMaintenance() {
       service.updateSpaceStatus(300, "maintenance");
       List<String> lines = readLines(parkingspacesPath);
       assertTrue(lines.get(1).contains("maintenance"));
   }
   @Test
   public void testAddNewParkingLotWithSameID() {
       service.addNewParkingLot(201, "Main Lot", 100);
       List<String> lines = readLines(parkinglotsPath);
       assertEquals(3, lines.size());
   }
   private List<String> readLines(Path path) {
       try {
           return Files.readAllLines(path);
       } catch (IOException e){
           fail("Could not read file: " + path);
           return Collections.emptyList();
       }
   }
   @Test
   public void testRemoveParkingLot() {
       service.addNewParkingLot(204, "Test Lot", 150);
       service.removeParkingLot(204);
   
       List<String> lines = readLines(parkinglotsPath);
       assertFalse(lines.stream().anyMatch(line -> line.contains("204") && line.contains("Test Lot")));
   }
}
