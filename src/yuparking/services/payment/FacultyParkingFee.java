package yuparking.services.payment;

import yuparking.services.ParkingFeeStrategy;

public class FacultyParkingFee implements ParkingFeeStrategy{
    @Override
    public double calculateFee(int hours) {
        return hours * 8.0;
    }
}