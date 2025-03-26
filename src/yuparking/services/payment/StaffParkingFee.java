package yuparking.services.payment;

import yuparking.services.ParkingFeeStrategy;

public class StaffParkingFee implements ParkingFeeStrategy {
    private static final double HOURLY_RATE = 10.0;

    @Override
    public double calculateFee(double hours) {
        return HOURLY_RATE * hours;
    }
}
