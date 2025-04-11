package test.service;

import yuparking.services.BookingService;
import yuparking.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestBookingService {

    BookingService bookingService = new BookingService();

    // Dummy user for testing fee strategies
    static class DummyUser extends User {
        private final String userType;
        public DummyUser(int id, String email, String password, String userType) {
            super(id, email, password);
            this.userType = userType;
        }
        @Override
        public String getUserType() {
            return userType;
        }
    }

    // Test 1: Student pays $5/hour
    @Test
    void testStudentFee() {
        User student = new DummyUser(1, "stud@my.yorku.ca", "pass", "student");
        double fee = bookingService.calculateFeeForBooking(student, 2);
        assertEquals(10.0, fee, 0.001);
    }

    // Test 2: Faculty pays $8/hour
    @Test
    void testFacultyFee() {
        User faculty = new DummyUser(2, "prof@yorku.ca", "pass", "faculty");
        double fee = bookingService.calculateFeeForBooking(faculty, 1.5);
        assertEquals(12.0, fee, 0.001);
    }

    // Test 3: Staff pays $10/hour
    @Test
    void testStaffFee() {
        User staff = new DummyUser(3, "staff@yorku.ca", "pass", "staff");
        double fee = bookingService.calculateFeeForBooking(staff, 3);
        assertEquals(30.0, fee, 0.001);
    }

    // Test 4: Visitor pays $15/hour
    @Test
    void testVisitorFee() {
        User visitor = new DummyUser(4, "guest@gmail.com", "pass", "visitor");
        double fee = bookingService.calculateFeeForBooking(visitor, 1);
        assertEquals(15.0, fee, 0.001);
    }

    // Test 5: Default fallback user type treated as visitor
    @Test
    void testUnknownUserTypeDefaultsToVisitor() {
        User unknown = new DummyUser(5, "x@unknown.com", "pass", "alien");
        double fee = bookingService.calculateFeeForBooking(unknown, 2);
        assertEquals(30.0, fee, 0.001); // 2 hrs * $15
    }

    // Test 6: Case-insensitive userType
    @Test
    void testCaseInsensitiveUserType() {
        User faculty = new DummyUser(6, "f@yorku.ca", "pass", "FACULTY");
        double fee = bookingService.calculateFeeForBooking(faculty, 1);
        assertEquals(8.0, fee, 0.001);
    }

    // Test 7: 0 hours = $0
    @Test
    void testZeroDurationFee() {
        User staff = new DummyUser(7, "s@yorku.ca", "pass", "staff");
        double fee = bookingService.calculateFeeForBooking(staff, 0);
        assertEquals(0.0, fee, 0.001);
    }

    // Test 8: Negative duration should throw exception
    @Test
    void testNegativeDurationThrows() {
        User student = new DummyUser(8, "s@my.yorku.ca", "pass", "student");
        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.calculateFeeForBooking(student, -2);
        });
    }

    // Test 9: Long duration (24 hrs)
    @Test
    void testFullDayVisitor() {
        User visitor = new DummyUser(9, "vis@yahoo.com", "pass", "visitor");
        double fee = bookingService.calculateFeeForBooking(visitor, 24);
        assertEquals(360.0, fee, 0.001);
    }

    // Test 10: Rounding precision test
    @Test
    void testFloatingPointRounding() {
        User student = new DummyUser(10, "s@my.yorku.ca", "pass", "student");
        double fee = bookingService.calculateFeeForBooking(student, 1.333);
        assertEquals(6.665, fee, 0.001);
    }
}
