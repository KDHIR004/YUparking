package yuparking.services.payment;

import yuparking.services.ParkingFeeStrategy;

public class StaffParkingFee implements ParkingFeeStrategy{
    @Override
    public double calculateFee(int hours) {
        return hours * 10.0;
    }
}
