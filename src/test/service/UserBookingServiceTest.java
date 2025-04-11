package test.service;

import yuparking.services.UserBookingService;
import yuparking.database.Database;
import yuparking.models.User;
import yuparking.models.student;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserBookingServiceTest {

    private UserBookingService bookingService;
    private Database db;
    private User testUser;

    final int TEST_USER_ID = 77;
    final int TEST_BOOKING_ID = 98;
    final int TEST_SPACE_ID = 101;
    final String DEFAULT_START = "2025-05-01T10:00";
    final String DEFAULT_END = "2025-05-01T11:00";

    @BeforeEach
    void setup() {
        bookingService = new UserBookingService();
        db = new Database();

        testUser = new student(TEST_USER_ID, "student@my.yorku.ca", "Strong1!");
        testUser.clickVerificationLink(); // Verified user

        // Insert test booking if not present
        List<String[]> bookings = db.retrieveData("bookings");
        boolean exists = bookings.stream().anyMatch(row -> row[0].equals(String.valueOf(TEST_BOOKING_ID)));

        if (!exists) {
            String[] newRow = new String[]{
                    String.valueOf(TEST_BOOKING_ID),
                    String.valueOf(TEST_USER_ID),
                    String.valueOf(TEST_SPACE_ID),
                    DEFAULT_START,
                    DEFAULT_END,
                    "Booked"
            };
            bookings.add(newRow);
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
                row[3] = DEFAULT_START;
                row[4] = DEFAULT_END;
                row[5] = "Booked";
                break;
            }
        }
        db.confirmUpdate("bookings", bookings);
    }

    // Test 1: Create a valid booking
    @Test
    void testCreateValidBooking() {
        int previousCount = db.retrieveData("bookings").size();
        bookingService.createUserBooking(testUser, 102, "2025-06-01T08:00", "2025-06-01T09:00");
        int newCount = db.retrieveData("bookings").size();
        assertEquals(previousCount + 1, newCount);
    }

    // Test 2: Try to book with unverified user
    @Test
    void testUnverifiedUserBookingFails() {
        User fakeUser = new student(88, "new@my.yorku.ca", "Test123!");
        int previousCount = db.retrieveData("bookings").size();
        bookingService.createUserBooking(fakeUser, 102, "2025-06-01T08:00", "2025-06-01T09:00");
        int newCount = db.retrieveData("bookings").size();
        assertEquals(previousCount, newCount);
    }

    // Test 3: Booking with invalid time range
    @Test
    void testCreateBookingInvalidTime() {
        int previousCount = db.retrieveData("bookings").size();
        bookingService.createUserBooking(testUser, 102, "2025-06-01T08:00", "2025-06-01T08:00");
        int newCount = db.retrieveData("bookings").size();
        assertEquals(previousCount, newCount);
    }

    // Test 4: Modify booking with valid time
    @Test
    void testModifyBookingValid() {
        bookingService.modifyUserBooking(testUser, TEST_BOOKING_ID, "2025-05-01T12:00", "2025-05-01T13:00");
        String[] row = getBookingRow();
        assertEquals("2025-05-01T12:00", row[3]);
        assertEquals("2025-05-01T13:00", row[4]);
        assertEquals("Modified", row[5]);
    }

    // Test 5: Modify booking with invalid time range
    @Test
    void testModifyBookingInvalidTime() {
        String[] before = getBookingRow();
        bookingService.modifyUserBooking(testUser, TEST_BOOKING_ID, "2025-05-01T13:00", "2025-05-01T12:00");
        String[] after = getBookingRow();
        assertArrayEquals(before, after);
    }

    // Test 6: Cancel booking successfully
    @Test
    void testCancelBookingValid() {
        bookingService.cancelUserBooking(testUser, TEST_BOOKING_ID);
        String[] row = getBookingRow();
        assertEquals("Cancelled", row[5]);
    }

    // Test 7: Cancel already cancelled booking
    @Test
    void testCancelAlreadyCancelled() {
        bookingService.cancelUserBooking(testUser, TEST_BOOKING_ID); // first
        bookingService.cancelUserBooking(testUser, TEST_BOOKING_ID); // again
        String[] row = getBookingRow();
        assertEquals("Cancelled", row[5]);
    }

    // Test 8: Modify cancelled booking should fail
    @Test
    void testModifyCancelledBooking() {
        bookingService.cancelUserBooking(testUser, TEST_BOOKING_ID);
        bookingService.modifyUserBooking(testUser, TEST_BOOKING_ID, "2025-06-01T08:00", "2025-06-01T09:00");
        String[] row = getBookingRow();
        assertEquals("Cancelled", row[5]);
    }

    // Test 9: Try modifying booking that isn’t theirs
    @Test
    void testModifyOtherUsersBooking() {
        User stranger = new student(999, "someone@my.yorku.ca", "Test123!");
        String[] before = getBookingRow();
        bookingService.modifyUserBooking(stranger, TEST_BOOKING_ID, "2025-06-01T08:00", "2025-06-01T09:00");
        String[] after = getBookingRow();
        assertArrayEquals(before, after);
    }

    // Test 10: View user bookings runs without crash
    void testShowUserBookingsRuns() {
        assertDoesNotThrow(() -> bookingService.showUserBookings(testUser));
    }    
}
