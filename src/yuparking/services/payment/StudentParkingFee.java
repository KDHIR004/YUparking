package yuparking.services.payment;

import yuparking.services.ParkingFeeStrategy;

public class StudentParkingFee implements ParkingFeeStrategy {
    @Override
    public double calculateFee(int hours) {
        return hours * 5.0;
    }
}
