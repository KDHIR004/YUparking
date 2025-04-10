package test.service;

import yuparking.services.ManagementService;
import yuparking.database.Database;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestManagementService {

    ManagementService manager;
    Database db;
    final int TEST_BOOKING_ID = 99;

    @BeforeEach
    void setup() {
        manager = new ManagementService();
        db = new Database();

        // Ensure test row exists
        List<String[]> bookings = db.retrieveData("bookings");
        boolean exists = bookings.stream().anyMatch(row -> row[0].equals(String.valueOf(TEST_BOOKING_ID)));

        if (!exists) {
            String[] testRow = new String[]{
                String.valueOf(TEST_BOOKING_ID),
                "5", "101", "2025-04-25T09:00",
                "2025-04-25T10:00", "Booked"
            };
            bookings.add(testRow);
            db.confirmUpdate("bookings", bookings);
        }
    }

    private String[] getBookingRow() {
        return db.retrieveData("bookings").stream()
                .filter(row -> row[0].equals(String.valueOf(TEST_BOOKING_ID)))
                .findFirst()
                .orElse(null);
    }

    @AfterEach
    void resetBookingRow() {
        List<String[]> bookings = db.retrieveData("bookings");
        for (String[] row : bookings) {
            if (row[0].equals(String.valueOf(TEST_BOOKING_ID))) {
                row[3] = "2025-04-25T09:00";
                row[4] = "2025-04-25T10:00";
                row[5] = "Booked";
                break;
            }
        }
        db.confirmUpdate("bookings", bookings);
    }

    // Test 1: Modify booking with valid time
    @Test
    void testModifyBookingValid() {
        manager.modifyAnyBooking(TEST_BOOKING_ID, "2025-04-30T10:00", "2025-04-30T11:00");
        String[] row = getBookingRow();
        assertEquals("2025-04-30T10:00", row[3]);
        assertEquals("2025-04-30T11:00", row[4]);
        assertEquals("Modified by Manager", row[5]);
    }

    // Test 2: Modify booking with invalid time (end before start)
    @Test
    void testModifyBookingInvalidTime() {
        String[] before = getBookingRow();
        manager.modifyAnyBooking(TEST_BOOKING_ID, "2025-04-30T12:00", "2025-04-30T11:00");
        String[] after = getBookingRow();
        assertArrayEquals(before, after);
    }

    // Test 3: Cancel a valid booking
    @Test
    void testCancelBookingValid() {
        manager.cancelAnyBooking(TEST_BOOKING_ID);
        String[] row = getBookingRow();
        assertEquals("Cancelled by Manager", row[5]);
    }

    // Test 4: Cancel already cancelled booking
    @Test
    void testCancelAlreadyCancelledBooking() {
        manager.cancelAnyBooking(TEST_BOOKING_ID);
        manager.cancelAnyBooking(TEST_BOOKING_ID);
        String[] row = getBookingRow();
        assertEquals("Cancelled by Manager", row[5]);
    }

    // Test 5: Modify booking after it has been cancelled
    @Test
    void testModifyAfterCancelBooking() {
        manager.cancelAnyBooking(TEST_BOOKING_ID);
        manager.modifyAnyBooking(TEST_BOOKING_ID, "2025-05-01T08:00", "2025-05-01T09:00");
        String[] row = getBookingRow();
        assertEquals("Modified by Manager", row[5]);
        assertEquals("2025-05-01T08:00", row[3]);
    }

    // Test 6: Try modifying a non-existent booking ID
    @Test
    void testModifyNonExistentBooking() {
        assertDoesNotThrow(() ->
            manager.modifyAnyBooking(9999, "2025-06-01T08:00", "2025-06-01T09:00")
        );
    }

    // Test 7: Try cancelling a non-existent booking ID
    @Test
    void testCancelNonExistentBooking() {
        assertDoesNotThrow(() ->
            manager.cancelAnyBooking(9999)
        );
    }

    // Test 8: Generate manager account increases user count
    @Test
    void testGenerateManagerAccountIncrementsUserID() {
        int before = manager.getNextUserId();
        manager.generateManagerAccount();
        int after = manager.getNextUserId();
        assertEquals(before + 1, after);
    }

    // Test 9: Generated password is not null and strong enough
    @Test
    void testGeneratedManagerPasswordIsStrong() {
        manager.generateManagerAccount();
        String password = manager.getLastGeneratedPassword();
        assertNotNull(password);
        assertTrue(password.length() >= 8);
    }

    // Test 10: Attempt to modify booking to have same start and end time (invalid)
    @Test
    void testModifyBookingSameStartEndTime() {
        String[] before = getBookingRow();
        manager.modifyAnyBooking(TEST_BOOKING_ID, "2025-06-01T08:00", "2025-06-01T08:00");
        String[] after = getBookingRow();

        // Booking should not be updated
        assertArrayEquals(before, after);
    }

}
