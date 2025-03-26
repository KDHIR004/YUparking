package yuparking.services;
import java.util.List;

import yuparking.database.Database;

public class PaymentService {
    private final Database db;

    public PaymentService() {
        this.db = new Database();
    }

    public void processPayment(int bookingID, double amount, String paymentMethod) {
        List<String[]> payments = db.retrieveData("payments");
        int nextPaymentID = payments.size(); 

        String[] newPayment = new String[]{
                String.valueOf(nextPaymentID),
                String.valueOf(bookingID),
                String.format("%.2f", amount),
                paymentMethod,
                "Completed"
        };

        payments.add(newPayment);
        db.confirmUpdate("payments", payments);

        System.out.println("Payment of $" + amount + " processed for booking " + bookingID +
                " using " + paymentMethod + ".");
    }

    public void refundPayment(int bookingID) {
        List<String[]> payments = db.retrieveData("payments");
        boolean refunded = false;

        for (String[] row : payments) {
            if (row[1].equals(String.valueOf(bookingID)) && row[4].equalsIgnoreCase("Completed")) {
                row[4] = "Refunded";
                refunded = true;
                break;
            }
        }

        if (refunded) {
            db.confirmUpdate("payments", payments);
            System.out.println("Payment for booking " + bookingID + " has been refunded.");
        } else {
            System.out.println("No completed payment found for booking " + bookingID + ".");
        }
    }
}
