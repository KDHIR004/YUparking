package yuparking.services.payment;

import yuparking.services.ParkingFeeStrategy;

public class VisitorParkingFee implements ParkingFeeStrategy{
    @Override
    public double calculateFee(int hours) {
        return hours * 15.0;
    }
}
