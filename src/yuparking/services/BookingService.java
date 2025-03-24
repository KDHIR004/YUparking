package yuparking.services;
import yuparking.database.Database;
import yuparking.models.Booking;
import yuparking.models.User;

//import java.util.ArrayList;
import java.util.List;


public class BookingService {
    private Database db;
    private List<User> users;

    public BookingService(List<User> users2) {
        db = new Database();
        this.users = users;
    }
    private User getUserById(int userID) {
        for (User user : users) {
            if (user.getUserID() == userID) {
                return user;
            }
        }
        return null;
    }

    public void createBooking(int userID, int spaceID, String startTime, String endTime) {
        User user = getUserById(userID);
        if (user == null) {
            System.out.println("User with ID " + userID + " not found! Cannot create booking.");
            return;
        }
        List<String[]> bookings = db.retrieveData("bookings");
        int nextBookingID = bookings.size(); // simple incremental
        Booking booking = new Booking(nextBookingID, userID, spaceID, startTime, endTime, "Booked");

        String[] newBookingRow = new String[] {
                String.valueOf(booking.getBookingID()),
                String.valueOf(booking.getUserID()),
                String.valueOf(booking.getSpaceID()),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus()
        };

        bookings.add(newBookingRow);
        db.confirmUpdate("bookings", bookings);
        System.out.println("Booking created user: " + user.getEmail() + " with booking ID: " + nextBookingID);
    }
}