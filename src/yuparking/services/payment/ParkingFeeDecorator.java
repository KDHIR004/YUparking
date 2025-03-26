package yuparking.services.payment;

import yuparking.services.ParkingFeeStrategy;

public abstract class ParkingFeeDecorator implements ParkingFeeStrategy {
    protected ParkingFeeStrategy decoratedStrategy;

    public ParkingFeeDecorator(ParkingFeeStrategy decoratedStrategy) {
        this.decoratedStrategy = decoratedStrategy;
    }
}
//    @Override
//    public double calculateFee(int hours) {
//        return decoratedStrategy.calculateFee(hours);
//    }
//    public static class EarlyBirdDiscount extends ParkingFeeDecorator {
//
//        public EarlyBirdDiscount(ParkingFeeStrategy decoratedStrategy) {
//            super(decoratedStrategy);
//        }
//
//        @Override
//        public double calculateFee(double hours) {
//            double baseFee = decoratedStrategy.calculateFee(hours);
//            double discountedFee = baseFee * 0.8;  // Apply 20% early bird discount
//            System.out.println("Early bird discount applied: Original $" + baseFee + " → Discounted $" + discountedFee);
//            return discountedFee;
//        }
//    }
//}
