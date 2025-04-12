package test.payment;

import yuparking.services.payment.StaffParkingFee;
import yuparking.services.ParkingFeeStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestStaffParkingFee {

    ParkingFeeStrategy feeStrategy = new StaffParkingFee();

    // Test 1: 1 hour = $10.0
    @Test
    void testOneHourFee() {
        double fee = feeStrategy.calculateFee(1.0);
        assertEquals(10.0, fee);
    }

    // Test 2: 2.5 hours = $25.0
    @Test
    void testPartialHourFee() {
        double fee = feeStrategy.calculateFee(2.5);
        assertEquals(25.0, fee);
    }

    // Test 3: 0 hours = $0.0
    @Test
    void testZeroHourFee() {
        double fee = feeStrategy.calculateFee(0.0);
        assertEquals(0.0, fee);
    }

    // Test 4: negative hours should throw exception
    @Test
    void testNegativeHourThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            feeStrategy.calculateFee(-2.0);
        });
    }

    // Test 5: very small decimal like 0.1 hour = $1.0
    @Test
    void testSmallDecimalFee() {
        double fee = feeStrategy.calculateFee(0.1);
        assertEquals(1.0, fee);
    }

    // Test 6: large value (1000 hours) = $10,000.0
    @Test
    void testLargeHourFee() {
        double fee = feeStrategy.calculateFee(1000);
        assertEquals(10000.0, fee);
    }
}
