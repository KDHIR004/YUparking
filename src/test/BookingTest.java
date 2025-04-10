package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import yuparking.models.Booking;

public class BookingTest {

    // Test 1
	@Test
    void testConstructorInitialization() {
        Booking booking = new Booking(1, 100, 200, "2025-04-01T08:00", "2025-04-01T10:00", "Booked");

        assertEquals(1, booking.getBookingID());
        assertEquals(100, booking.getUserID());
        assertEquals(200, booking.getSpaceID());
        assertEquals("2025-04-01T08:00", booking.getStartTime());
        assertEquals("2025-04-01T10:00", booking.getEndTime());
        assertEquals("Booked", booking.getStatus());
    }

    // Test 2
    @Test
    void testModifyBooking() {
        Booking booking = new Booking(2, 101, 201, "2025-04-01T09:00", "2025-04-01T11:00", "Booked");
        booking.modifyBooking("2025-04-01T10:00", "2025-04-01T12:00");

        assertEquals("2025-04-01T10:00", booking.getStartTime());
        assertEquals("2025-04-01T12:00", booking.getEndTime());
    }

    // Test 3
    @Test
    void testCancelBooking() {
        Booking booking = new Booking(3, 102, 202, "2025-04-01T10:00", "2025-04-01T12:00", "Booked");
        booking.cancelBooking();

        assertEquals("Cancelled", booking.getStatus());
    }

    // Test 4: Confirm booking changes prints something (not testable, just call it)
    @Test
    void testConfirmBookingChangesDoesNotThrow() {
        Booking booking = new Booking(4, 103, 203, "2025-04-01T11:00", "2025-04-01T13:00", "Booked");

        assertDoesNotThrow(() -> booking.confirmBookingChanges());
    }

    // Test 5: Check unique booking IDs don't clash
    @Test
    void testUniqueBookingIDs() {
        Booking b1 = new Booking(5, 104, 204, "08:00", "09:00", "Booked");
        Booking b2 = new Booking(6, 105, 205, "09:00", "10:00", "Booked");

        assertNotEquals(b1.getBookingID(), b2.getBookingID());
    }

    // Test 6: Status remains as initialized unless changed
    @Test
    void testStatusPersistence() {
        Booking booking = new Booking(7, 106, 206, "10:00", "12:00", "Booked");
        assertEquals("Booked", booking.getStatus());
    }

    // Test 7: 
    @Test
    void testDoubleCancel() {
        Booking booking = new Booking(8, 107, 207, "12:00", "14:00", "Booked");
        booking.cancelBooking();
        booking.cancelBooking();

        assertEquals("Cancelled", booking.getStatus());
    }

    // Test 8: 
    @Test
    void testRedundantModify() {
        Booking booking = new Booking(9, 108, 208, "13:00", "15:00", "Booked");
        booking.modifyBooking("13:00", "15:00");

        assertEquals("13:00", booking.getStartTime());
        assertEquals("15:00", booking.getEndTime());
    }

    // Test 9: 
    @Test
    void testCustomStatus() {
        Booking booking = new Booking(10, 109, 209, "14:00", "16:00", "Checked-In-Confirmed-And-Signed-Off");

        assertEquals("Checked-In-Confirmed-And-Signed-Off", booking.getStatus());
    }

    // Test 10: 
    @Test
    void testEmptyTimeStrings() {
        Booking booking = new Booking(11, 110, 210, "", "", "Booked");

        assertEquals("", booking.getStartTime());
        assertEquals("", booking.getEndTime());
    }
}
