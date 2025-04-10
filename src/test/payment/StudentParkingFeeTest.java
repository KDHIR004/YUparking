package test.payment;

import yuparking.services.payment.StudentParkingFee;
import yuparking.services.ParkingFeeStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentParkingFeeTest {

    ParkingFeeStrategy feeStrategy = new StudentParkingFee();

    // Test 1: 1 hour = $5.0
    @Test
    void testOneHourFee() {
        double fee = feeStrategy.calculateFee(1.0);
        assertEquals(5.0, fee);
    }

    // Test 2: 2.5 hours = $12.5
    @Test
    void testPartialHourFee() {
        double fee = feeStrategy.calculateFee(2.5);
        assertEquals(12.5, fee);
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


    // Test 5: small decimal like 0.2 = $1.0
    @Test
    void testSmallDecimalFee() {
        double fee = feeStrategy.calculateFee(0.2);
        assertEquals(1.0, fee);
    }

    // Test 6: large value = 500 * $5 = $2500
    @Test
    void testLargeHourFee() {
        double fee = feeStrategy.calculateFee(500);
        assertEquals(2500.0, fee);
    }
}
