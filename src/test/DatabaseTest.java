package test;

import yuparking.database.Database;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    Database db;

    @BeforeEach
    void setUp() {
        db = new Database();
    }

    // Test 1: users.csv file should return some rows
    @Test
    void testReadFromUsersCsv() {
        List<String[]> data = db.retrieveData("users");
        assertNotNull(data);
        assertTrue(data.size() > 0); // Make sure file isn't empty
    }

    // Test 2: write a new user row to users.csv
    @Test
    void testWriteToUsersCsv() {
        List<String[]> users = db.retrieveData("users");

        int nextId = users.stream().skip(1) // skip header
                .mapToInt(row -> Integer.parseInt(row[0]))
                .max().orElse(5) + 1;

        String[] newUser = new String[]{
                String.valueOf(nextId),
                "testuser" + nextId + "@my.yorku.ca",
                "TestPass123!",
                "student",
                "false"
        };

        users.add(newUser);
        db.confirmUpdate("users", users);

        List<String[]> updated = db.retrieveData("users");
        assertTrue(updated.stream().anyMatch(r -> r[0].equals(String.valueOf(nextId))));
    }

    // Test 3: read from payments.csv
    @Test
    void testReadPaymentsCsv() {
        List<String[]> data = db.retrieveData("payments");
        assertNotNull(data);
        assertTrue(data.size() > 0);
    }

    // Test 4: write a payment record to payments.csv
    @Test
    void testWriteToPaymentsCsv() {
        List<String[]> payments = db.retrieveData("payments");

        int nextId = payments.stream().skip(1)
                .mapToInt(row -> Integer.parseInt(row[0]))
                .max().orElse(5) + 1;

        String[] newPayment = new String[]{
                String.valueOf(nextId),
                "3",              // bookingID must exist in bookings
                "15.00",
                "Credit",
                "Completed"
        };

        payments.add(newPayment);
        db.confirmUpdate("payments", payments);

        List<String[]> updated = db.retrieveData("payments");
        assertTrue(updated.stream().anyMatch(r -> r[0].equals(String.valueOf(nextId))));
    }

    // Test 5: case-insensitive table name
    @Test
    void testCaseInsensitiveRead() {
        List<String[]> lower = db.retrieveData("users");
        List<String[]> upper = db.retrieveData("USERS");
        assertEquals(lower.size(), upper.size());
    }

    // Test 6: read from bookings.csv
    @Test
    void testReadBookingsCsv() {
        List<String[]> bookings = db.retrieveData("bookings");
        assertNotNull(bookings);
        assertTrue(bookings.size() > 0);
    }

    // Test 7: write to bookings.csv
    @Test
    void testWriteToBookingsCsv() {
        List<String[]> bookings = db.retrieveData("bookings");

        int nextId = bookings.stream().skip(1)
                .mapToInt(row -> Integer.parseInt(row[0]))
                .max().orElse(3) + 1;

        String[] booking = new String[]{
                String.valueOf(nextId),
                "5",  // userID
                "102",  // spaceID
                "2025-04-10T12:00",
                "2025-04-10T13:00",
                "Booked"
        };

        bookings.add(booking);
        db.confirmUpdate("bookings", bookings);

        List<String[]> updated = db.retrieveData("bookings");
        assertTrue(updated.stream().anyMatch(r -> r[0].equals(String.valueOf(nextId))));
    }

    // Test 8: write to parkinglots.csv
    @Test
    void testWriteToParkingLotsCsv() {
        List<String[]> lots = db.retrieveData("parkinglots");

        int nextId = lots.stream().skip(1)
                .mapToInt(row -> Integer.parseInt(row[0]))
                .max().orElse(3) + 1;

        String[] lot = new String[]{
                String.valueOf(nextId),
                "Test Lot",
                "25",
                "active"
        };

        lots.add(lot);
        db.confirmUpdate("parkinglots", lots);

        List<String[]> updated = db.retrieveData("parkinglots");
        assertTrue(updated.stream().anyMatch(r -> r[0].equals(String.valueOf(nextId))));
    }

    // Test 9: write to parkingspaces.csv
    @Test
    void testWriteToParkingSpacesCsv() {
        List<String[]> spaces = db.retrieveData("parkingspaces");

        int nextId = spaces.stream().skip(1)
                .mapToInt(row -> Integer.parseInt(row[0]))
                .max().orElse(106) + 1;

        String[] space = new String[]{
                String.valueOf(nextId),
                "1",        // valid lotID
                "false"     // isOccupied
        };

        spaces.add(space);
        db.confirmUpdate("parkingspaces", spaces);

        List<String[]> updated = db.retrieveData("parkingspaces");
        assertTrue(updated.stream().anyMatch(r -> r[0].equals(String.valueOf(nextId))));
    }

    // Test 10: invalid table name should not crash
    @Test
    void testInvalidTableDoesNothing() {
        List<String[]> result = db.retrieveData("this_table_does_not_exist");
        assertTrue(result.isEmpty());
    }
}
