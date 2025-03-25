package yuparking.services;

import yuparking.database.Database;
import yuparking.models.Booking;
import yuparking.models.User;

import java.util.List;

public class BookingService {
    private Database db;
    private List<User> users;

    public BookingService(List<User> users) {
        this.db = new Database();
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

    public void createBooking(User user, int spaceID, String startTime, String endTime) {
        if (!user.isVerified()) {
            System.out.println("User " + user.getEmail() + " is not verified. Cannot create booking.");
            return;
        }

        List<String[]> bookings = db.retrieveData("bookings");
        int nextBookingID = bookings.size();

        Booking booking = new Booking(nextBookingID, user.getUserID(), spaceID, startTime, endTime, "Booked");

        String[] bookingRow = {
                String.valueOf(booking.getBookingID()),
                String.valueOf(booking.getUserID()),
                String.valueOf(booking.getSpaceID()),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus()
        };

        bookings.add(bookingRow);
        db.confirmUpdate("bookings", bookings);

        System.out.println("Booking created for user: " + user.getEmail() +
                " | Booking ID: " + nextBookingID +
                " | Space ID: " + spaceID);
    }

    public void modifyBooking(int bookingID, String newStartTime, String newEndTime) {
        List<String[]> bookings = db.retrieveData("bookings");
        for (String[] row : bookings) {
            if (row[0].equals(String.valueOf(bookingID)) && row[5].equalsIgnoreCase("Booked")) {
                row[3] = newStartTime;
                row[4] = newEndTime;
                row[5] = "Modified";
                db.confirmUpdate("bookings", bookings);
                System.out.println("Booking " + bookingID + " modified successfully.");
                return;
            }
        }
        System.out.println("Booking ID not found or not modifiable.");
    }

    public void cancelBooking(int bookingID) {
        List<String[]> bookings = db.retrieveData("bookings");
        for (String[] row : bookings) {
            if (row[0].equals(String.valueOf(bookingID)) && !row[5].equalsIgnoreCase("Cancelled")) {
                row[5] = "Cancelled";
                db.confirmUpdate("bookings", bookings);
                System.out.println("Booking " + bookingID + " cancelled.");
                return;
            }
        }
        System.out.println("Booking ID not found or already cancelled.");
    }

    public void showAllBookings() {
        List<String[]> bookings = db.retrieveData("bookings");
        System.out.println("\nAll Bookings:");
        for (int i = 1; i < bookings.size(); i++) {
            String[] row = bookings.get(i);
            int userId = Integer.parseInt(row[1]);
            User user = getUserById(userId);
            String userInfo = (user != null) ? user.getEmail() + " (" + user.getUserType() + ")" : "Unknown User";

            System.out.println("BookingID: " + row[0] + " | User: " + userInfo +
                    " | SpaceID: " + row[2] + " | Start: " + row[3] +
                    " | End: " + row[4] + " | Status: " + row[5]);
        }
    }
    
}
