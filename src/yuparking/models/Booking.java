package yuparking.models;

public class Booking {
    private int bookingID;
    private int userID;
    private int spaceID;
    private String startTime;
    private String endTime;
    private String status; // Booked, Cancelled, Completed

    public Booking(int bookingID, int userID, int spaceID, String startTime, String endTime, String status) {
        this.bookingID = bookingID;
        this.userID = userID;
        this.spaceID = spaceID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public int getBookingID() { return bookingID; }
    public int getUserID() { return userID; }
    public int getSpaceID() { return spaceID; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStatus() { return status; }

    public void modifyBooking(String newStartTime, String newEndTime) {
        this.startTime = newStartTime;
        this.endTime = newEndTime;
    }

    public void cancelBooking() {
        this.status = "Cancelled";
    }

    public void confirmBookingChanges() {
        System.out.println("Booking changes confirmed for booking ID: " + bookingID);
    }
}
