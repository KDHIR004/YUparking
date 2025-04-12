package test.payment;

import yuparking.services.payment.VisitorParkingFee;
import yuparking.services.ParkingFeeStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestVisitorParkingFee {

    ParkingFeeStrategy feeStrategy = new VisitorParkingFee();

    // Test 1: 1 hour = $15
    @Test
    void testOneHourFee() {
        double fee = feeStrategy.calculateFee(1.0);
        assertEquals(15.0, fee);
    }

    // Test 2: 3.5 hours = $52.5
    @Test
    void testPartialHourFee() {
        double fee = feeStrategy.calculateFee(3.5);
        assertEquals(52.5, fee);
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
            feeStrategy.calculateFee(-1.0);
        });
    }


    // Test 5: very small decimal (0.05 hours = $0.75)
    @Test
    void testSmallDecimalHour() {
        double fee = feeStrategy.calculateFee(0.05);
        assertEquals(0.75, fee);
    }

    // Test 6: large hour value
    @Test
    void testLargeHourFee() {
        double fee = feeStrategy.calculateFee(200);
        assertEquals(3000.0, fee);
    }
}
