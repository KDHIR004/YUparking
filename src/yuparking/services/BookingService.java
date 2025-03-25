package yuparking.services;

import yuparking.models.User;
import yuparking.services.payment.*;

public class BookingService {

    // ✅ Calculate fee based on user type and booking duration
    public double calculateFeeForBooking(User user, int hours) {
        ParkingFeeStrategy strategy;
        switch (user.getUserType().toLowerCase()) {
            case "student":
                strategy = new StudentParkingFee();
                break;
            case "faculty":
                strategy = new FacultyParkingFee();
                break;
            case "staff":
                strategy = new StaffParkingFee();
                break;
            case "visitor":
            default:
                strategy = new VisitorParkingFee();
                break;
        }
        return strategy.calculateFee(hours);
    }
}
