package test.service;

import yuparking.services.ManagementService;
import yuparking.database.Database;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestManagementService {

    ManagementService manager;
    Database db;
    final int TEST_BOOKING_ID = 3; // 🟢 Use existing row from CSV

    final String ORIGINAL_START = "2025-03-26T08:00:00";
    final String ORIGINAL_END = "2025-03-26T09:30:00";

    @BeforeEach
    void setup() {
        manager = new ManagementService();
        db = new Database();
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
                row[3] = ORIGINAL_START;
                row[4] = ORIGINAL_END;
                row[5] = "Booked";
                break;
            }
        }
        db.confirmUpdate("bookings", bookings);
    }

    // Test 1: Modify booking with valid time
    @Test
    void testModifyBookingValid() {
        manager.modifyAnyBooking(TEST_BOOKING_ID, "2025-05-01T08:00", "2025-05-01T09:00");
        String[] row = getBookingRow();
        assertEquals("2025-05-01T08:00", row[3]);
        assertEquals("2025-05-01T09:00", row[4]);
        assertEquals("Modified by Manager", row[5]);
    }

    // Test 2: Modify booking with invalid time
    @Test
    void testModifyBookingInvalidTime() {
        String[] before = getBookingRow();
        manager.modifyAnyBooking(TEST_BOOKING_ID, "2025-05-01T10:00", "2025-05-01T09:00");
        String[] after = getBookingRow();
        assertArrayEquals(before, after);
    }

    // Test 3: Cancel booking
    @Test
    void testCancelBooking() {
        manager.cancelAnyBooking(TEST_BOOKING_ID);
        String[] row = getBookingRow();
        assertEquals("Cancelled by Manager", row[5]);
    }

    // Test 4: Cancel already cancelled
    @Test
    void testCancelTwice() {
        manager.cancelAnyBooking(TEST_BOOKING_ID);
        manager.cancelAnyBooking(TEST_BOOKING_ID);
        String[] row = getBookingRow();
        assertEquals("Cancelled by Manager", row[5]);
    }

    // Test 5: Modify after cancel
    @Test
    void testModifyAfterCancel() {
        manager.cancelAnyBooking(TEST_BOOKING_ID);
        manager.modifyAnyBooking(TEST_BOOKING_ID, "2025-05-02T08:00", "2025-05-02T09:00");
        String[] row = getBookingRow();
        assertEquals("Modified by Manager", row[5]);
        assertEquals("2025-05-02T08:00", row[3]);
    }

    // Test 6: Booking not found
    @Test
    void testModifyNonExistentBooking() {
        assertDoesNotThrow(() -> manager.modifyAnyBooking(9999, "2025-06-01T10:00", "2025-06-01T11:00"));
    }

    // Test 7: Cancel non-existent
    @Test
    void testCancelNonExistentBooking() {
        assertDoesNotThrow(() -> manager.cancelAnyBooking(9999));
    }

    // Test 8: Generate manager account increases user count
    @Test
    void testGenerateManagerIncreasesUserCount() {
        int before = manager.getNextUserId();
        manager.generateManagerAccount();
        int after = manager.getNextUserId();
        assertEquals(before + 1, after);
    }

    // Test 9: Generated password check
    @Test
    void testGeneratedPasswordNotNull() {
        manager.generateManagerAccount();
        String password = manager.getLastGeneratedPassword();
        assertNotNull(password);
        assertTrue(password.length() >= 8);
    }

    // Test 10: Same start/end time = invalid modify
    @Test
    void testInvalidSameTime() {
        String[] before = getBookingRow();
        manager.modifyAnyBooking(TEST_BOOKING_ID, "2025-06-01T08:00", "2025-06-01T08:00");
        String[] after = getBookingRow();
        assertArrayEquals(before, after);
    }
}
