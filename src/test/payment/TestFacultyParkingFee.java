package test.payment;

import yuparking.services.payment.FacultyParkingFee;
import yuparking.services.ParkingFeeStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestFacultyParkingFee {

    ParkingFeeStrategy feeStrategy = new FacultyParkingFee();

    // Test 1: 1 hour = $8.0
    @Test
    void testOneHourFee() {
        double fee = feeStrategy.calculateFee(1.0);
        assertEquals(8.0, fee);
    }

    // Test 2: 2.5 hours = $20.0
    @Test
    void testPartialHourFee() {
        double fee = feeStrategy.calculateFee(2.5);
        assertEquals(20.0, fee);
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
            feeStrategy.calculateFee(-1.5);
        });
    }

    // Test 5: very small decimal like 0.05 = $0.4
    @Test
    void testSmallDecimalFee() {
        double fee = feeStrategy.calculateFee(0.05);
        assertEquals(0.4, fee);
    }

    // Test 6: large hour input = $8000.0
    @Test
    void testLargeHourFee() {
        double fee = feeStrategy.calculateFee(1000);
        assertEquals(8000.0, fee);
    }
}
