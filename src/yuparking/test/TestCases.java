package yuparking.test;

import yuparking.models.User;
import yuparking.factory.UserFactory;
import yuparking.services.*;
import yuparking.database.Database;

import java.time.LocalDateTime;
import java.util.List;

public class TestCases {

    public static void main(String[] args) {
        Database db = new Database();
        PaymentService paymentService = new PaymentService();
        BookingService bookingService = new BookingService();
        UserBookingService userBookingService = new UserBookingService();
        ManagementService managementService = new ManagementService();
        ParkingLotService parkingLotService = new ParkingLotService();

        System.out.println("✅ TEST 1: Login as super manager & add parking lot");
        User superManager = UserFactory.createUser(2, "supermanager@yorku.ca", "superman123", "manager");
        parkingLotService.addNewParkingLot(4, "West Lot", 25);
        parkingLotService.removeParkingLot(4);

        System.out.println("\n✅ TEST 2: Faculty creates booking and pays");
        User faculty = UserFactory.createUser(3, "faculty1@yorku.ca", "fac123", "faculty");
        userBookingService.createUserBooking(faculty, 101, "2024-04-05T09:00", "2024-04-05T12:00");
        paymentService.processPayment(3, 24.0, "Credit");  // Assume booking ID = 3 for demonstration
        userBookingService.cancelUserBooking(faculty, 3); // Triggers refund

        System.out.println("\n✅ TEST 3: Manager modifies & cancels another user’s booking");
        User manager = UserFactory.createUser(1, "manager1@yorku.ca", "man123", "manager");
        managementService.modifyAnyBooking(0, "2024-04-06T08:00", "2024-04-06T10:00"); // Modifies booking ID 0
        managementService.cancelAnyBooking(0); // Cancels booking ID 0

        System.out.println("\n✅ TEST 4: Student views only their bookings");
        User student = UserFactory.createUser(5, "student1@my.yorku.ca", "stud123", "student");
        userBookingService.showUserBookings(student);

        System.out.println("\n✅ TEST 5: Simulating Occupancy Status Updates");
        OccupancySimulator simulator = new OccupancySimulator();
        simulator.simulateOccupancyUpdate();

        System.out.println("\n✅ All test cases ran successfully.");
    }
}
