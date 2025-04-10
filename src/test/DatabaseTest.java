package test;

import yuparking.database.Database;
import org.junit.jupiter.api.*;

import java.io.*;
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
    }

    // Test 2: write a new user row to users.csv
    @Test
    void testWriteToUsersCsv() {
        List<String[]> users = db.retrieveData("users");

        int nextId = users.size();
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
        assertEquals(users.size(), updated.size());
    }

    // Test 3: read from payments.csv
    @Test
    void testReadPaymentsCsv() {
        List<String[]> data = db.retrieveData("payments");
        assertNotNull(data);
    }

    // Test 4: write a payment record to payments.csv
    @Test
    void testWriteToPaymentsCsv() {
        List<String[]> payments = db.retrieveData("payments");

        int nextId = payments.size();
        String[] newPayment = new String[]{
                String.valueOf(nextId),
                "ParkingFee",
                "15.00",
                "credit"
        };

        payments.add(newPayment);
        db.confirmUpdate("payments", payments);

        List<String[]> updated = db.retrieveData("payments");
        assertEquals(payments.size(), updated.size());
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
    }

    // Test 7: write to bookings.csv
    @Test
    void testWriteToBookingsCsv() {
        List<String[]> bookings = db.retrieveData("bookings");

        int next = bookings.size();
        String[] booking = new String[]{
                String.valueOf(next),
                "user" + next,
                "A1",
                "2025-04-10T12:00",
                "2025-04-10T13:00"
        };

        bookings.add(booking);
        db.confirmUpdate("bookings", bookings);

        List<String[]> updated = db.retrieveData("bookings");
        assertEquals(bookings.size(), updated.size());
    }

    // Test 8: write to parkinglots.csv
    @Test
    void testWriteToParkingLotsCsv() {
        List<String[]> lots = db.retrieveData("parkinglots");

        String[] lot = new String[]{
                "LotX",
                "North Campus",
                "Enabled"
        };

        lots.add(lot);
        db.confirmUpdate("parkinglots", lots);

        List<String[]> updated = db.retrieveData("parkinglots");
        assertEquals(lots.size(), updated.size());
    }

    // Test 9: write to parkingspaces.csv
    @Test
    void testWriteToParkingSpacesCsv() {
        List<String[]> spaces = db.retrieveData("parkingspaces");

        String[] space = new String[]{
                "S101",
                "LotX",
                "Available"
        };

        spaces.add(space);
        db.confirmUpdate("parkingspaces", spaces);

        List<String[]> updated = db.retrieveData("parkingspaces");
        assertEquals(spaces.size(), updated.size());
    }

    // Test 10: invalid table name should not crash
    @Test
    void testInvalidTableDoesNothing() {
        List<String[]> result = db.retrieveData("this_table_does_not_exist");
        assertTrue(result.isEmpty());
    }
}
