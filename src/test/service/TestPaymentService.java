package test.service;

import yuparking.services.PaymentService;
import yuparking.database.Database;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Needed for @AfterAll to use non-static
public class TestPaymentService {

    private PaymentService paymentService;
    private Database db;
    private final int baseBookingId = 9990;

    @BeforeEach
    void setup() {
        paymentService = new PaymentService();
        db = new Database();
    }

    @AfterAll
    void cleanupGeneratedPayments() {
        System.out.println("Cleaning up test payments...");
        List<String[]> payments = db.retrieveData("payments");

        List<String[]> cleaned = payments.stream()
                .filter(row -> {
                    try {
                        int bookingId = Integer.parseInt(row[1]);
                        return bookingId < baseBookingId;
                    } catch (NumberFormatException e) {
                        return true;
                    }
                })
                .collect(Collectors.toList());

        db.confirmUpdate("payments", cleaned);
    }

    @Test
    void testProcessPaymentAddsRow() {
        int before = db.retrieveData("payments").size();
        paymentService.processPayment(baseBookingId, 10.00, "Credit");
        int after = db.retrieveData("payments").size();
        assertEquals(before + 1, after);
    }

    @Test
    void testCorrectValuesInNewPayment() {
        paymentService.processPayment(baseBookingId + 1, 15.50, "Debit");
        List<String[]> payments = db.retrieveData("payments");
        String[] last = payments.get(payments.size() - 1);
        assertEquals(String.valueOf(baseBookingId + 1), last[1]);
        assertEquals("15.50", last[2]);
        assertEquals("Debit", last[3]);
        assertEquals("Completed", last[4]);
    }

    @Test
    void testMultiplePaymentsDifferentBookingIDs() {
        paymentService.processPayment(baseBookingId + 2, 12.00, "Online");
        paymentService.processPayment(baseBookingId + 3, 13.00, "Debit");
        List<String[]> payments = db.retrieveData("payments");

        String[] last1 = payments.get(payments.size() - 2);
        String[] last2 = payments.get(payments.size() - 1);

        assertEquals(String.valueOf(baseBookingId + 2), last1[1]);
        assertEquals(String.valueOf(baseBookingId + 3), last2[1]);
    }

    @Test
    void testRefundCompletedPayment() {
        int id = baseBookingId + 4;
        paymentService.processPayment(id, 25.00, "Credit");
        paymentService.refundPayment(id);

        List<String[]> payments = db.retrieveData("payments");
        String[] target = payments.stream().filter(p -> p[1].equals(String.valueOf(id))).findFirst().orElse(null);
        assertNotNull(target);
        assertEquals("Refunded", target[4]);
    }

    @Test
    void testRefundDoesNotAffectOthers() {
        int id1 = baseBookingId + 5;
        int id2 = baseBookingId + 6;
        paymentService.processPayment(id1, 20.00, "Online");
        paymentService.processPayment(id2, 22.00, "Credit");

        paymentService.refundPayment(id1);

        List<String[]> payments = db.retrieveData("payments");
        String[] other = payments.stream().filter(p -> p[1].equals(String.valueOf(id2))).findFirst().orElse(null);
        assertNotNull(other);
        assertEquals("Completed", other[4]);
    }

    @Test
    void testRefundOnNonexistentBooking() {
        int before = db.retrieveData("payments").size();
        paymentService.refundPayment(123456);
        int after = db.retrieveData("payments").size();
        assertEquals(before, after);
    }

    @Test
    void testRefundOnAlreadyRefundedPayment() {
        int id = baseBookingId + 7;
        paymentService.processPayment(id, 30.00, "Debit");
        paymentService.refundPayment(id);
        paymentService.refundPayment(id);

        List<String[]> payments = db.retrieveData("payments");
        long count = payments.stream()
                .filter(p -> p[1].equals(String.valueOf(id)) && p[4].equals("Refunded"))
                .count();
        assertEquals(1, count);
    }

    @Test
    void testPaymentIDIsAutoIncremented() {
        int before = db.retrieveData("payments").size();
        paymentService.processPayment(baseBookingId + 8, 18.00, "Online");

        List<String[]> payments = db.retrieveData("payments");
        String[] last = payments.get(payments.size() - 1);
        assertEquals(String.valueOf(before), last[0]);
    }

    @Test
    void testRefundDoesNotAddRows() {
        int id = baseBookingId + 9;
        paymentService.processPayment(id, 19.00, "Credit");

        int before = db.retrieveData("payments").size();
        paymentService.refundPayment(id);
        int after = db.retrieveData("payments").size();

        assertEquals(before, after);
    }

    @Test
    void testMultipleRefundsWorkInIsolation() {
        int id1 = baseBookingId + 10;
        int id2 = baseBookingId + 11;
        paymentService.processPayment(id1, 12.00, "Debit");
        paymentService.processPayment(id2, 14.00, "Online");

        paymentService.refundPayment(id2);

        List<String[]> payments = db.retrieveData("payments");
        String[] first = payments.stream().filter(p -> p[1].equals(String.valueOf(id1))).findFirst().orElse(null);
        String[] second = payments.stream().filter(p -> p[1].equals(String.valueOf(id2))).findFirst().orElse(null);

        assertNotNull(first);
        assertNotNull(second);
        assertEquals("Completed", first[4]);
        assertEquals("Refunded", second[4]);
    }
}
