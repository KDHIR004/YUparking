package yuparking;

import yuparking.models.*;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting YorkU Parking System...");

        User student = new student(1, "student@yorku.ca", "pass123");
        student.enterRegistrationDetails(); // calls method from abstract User
        System.out.println(student);

        User visitor = UserFactory.createUser("visitor", 2, "visitor@yorku.ca", "visit@123");
        visitor.clickVerificationLink();
        System.out.println(visitor);
    }
}
