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

    private int testBookingId;
    final int TEST_USER_ID = 77;
    final int TEST_SPACE_ID = 101;
    final String DEFAULT_START = "2025-05-01T10:00";
    final String DEFAULT_END = "2025-05-01T11:00";

    @BeforeEach
    void setup() {
        bookingService = new UserBookingService();
        db = new Database();

        testUser = new student(TEST_USER_ID, "student@my.yorku.ca", "Strong1!");
        testUser.clickVerificationLink();

        List<String[]> bookings = db.retrieveData("bookings");
        testBookingId = bookings.size();

        String[] testRow = new String[]{
                String.valueOf(testBookingId),
                String.valueOf(TEST_USER_ID),
                String.valueOf(TEST_SPACE_ID),
                DEFAULT_START,
                DEFAULT_END,
                "Booked"
        };
        bookings.add(testRow);
        db.confirmUpdate("bookings", bookings);
    }

    private String[] getBookingRow() {
        return db.retrieveData("bookings").stream()
                .filter(row -> row[0].equals(String.valueOf(testBookingId)))
                .findFirst()
                .orElse(null);
    }

    @AfterEach
    void cleanup() {
        List<String[]> bookings = db.retrieveData("bookings");
        bookings.removeIf(row -> row[0].equals(String.valueOf(testBookingId)));
        db.confirmUpdate("bookings", bookings);
    }

    @Test
    void testCreateValidBooking() {
        int countBefore = db.retrieveData("bookings").size();
        bookingService.createUserBooking(testUser, 102, "2025-06-01T08:00", "2025-06-01T09:00");
        int countAfter = db.retrieveData("bookings").size();
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    void testUnverifiedUserBookingFails() {
        User unverified = new student(88, "noverify@my.yorku.ca", "Test123!");
        int countBefore = db.retrieveData("bookings").size();
        bookingService.createUserBooking(unverified, 102, "2025-06-01T08:00", "2025-06-01T09:00");
        int countAfter = db.retrieveData("bookings").size();
        assertEquals(countBefore, countAfter);
    }

    @Test
    void testCreateBookingInvalidTime() {
        int countBefore = db.retrieveData("bookings").size();
        bookingService.createUserBooking(testUser, 102, "2025-06-01T08:00", "2025-06-01T08:00");
        int countAfter = db.retrieveData("bookings").size();
        assertEquals(countBefore, countAfter);
    }

    @Test
    void testModifyBookingValid() {
        bookingService.modifyUserBooking(testUser, testBookingId, "2025-05-01T12:00", "2025-05-01T13:00");
        String[] row = getBookingRow();
        assertEquals("2025-05-01T12:00", row[3]);
        assertEquals("2025-05-01T13:00", row[4]);
        assertEquals("Modified", row[5]);
    }

    @Test
    void testModifyBookingInvalidTime() {
        String[] before = getBookingRow();
        bookingService.modifyUserBooking(testUser, testBookingId, "2025-05-01T13:00", "2025-05-01T12:00");
        String[] after = getBookingRow();
        assertArrayEquals(before, after);
    }

    @Test
    void testCancelBookingValid() {
        bookingService.cancelUserBooking(testUser, testBookingId);
        String[] row = getBookingRow();
        assertEquals("Cancelled", row[5]);
    }

    @Test
    void testCancelAlreadyCancelled() {
        bookingService.cancelUserBooking(testUser, testBookingId);
        bookingService.cancelUserBooking(testUser, testBookingId);
        String[] row = getBookingRow();
        assertEquals("Cancelled", row[5]);
    }

    @Test
    void testModifyCancelledBooking() {
        bookingService.cancelUserBooking(testUser, testBookingId);
        bookingService.modifyUserBooking(testUser, testBookingId, "2025-06-01T08:00", "2025-06-01T09:00");
        String[] row = getBookingRow();
        assertEquals("Cancelled", row[5]);
    }

    @Test
    void testModifyOtherUsersBooking() {
        User stranger = new student(999, "stranger@my.yorku.ca", "Strange1!");
        String[] before = getBookingRow();
        bookingService.modifyUserBooking(stranger, testBookingId, "2025-06-01T08:00", "2025-06-01T09:00");
        String[] after = getBookingRow();
        assertArrayEquals(before, after);
    }
}
