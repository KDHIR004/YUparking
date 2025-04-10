package yuparking.services.payment;

import yuparking.services.ParkingFeeStrategy;

public class StaffParkingFee implements ParkingFeeStrategy {
    private static final double HOURLY_RATE = 10.0;

    @Override
    public double calculateFee(double hours) {
        if (hours < 0) throw new IllegalArgumentException("Hours must be non-negative.");
        return HOURLY_RATE * hours;
    }

}
