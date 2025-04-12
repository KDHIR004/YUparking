package yuparking.factory;

import yuparking.models.*;

public class UserFactory {
    public static User createUser(int userID, String email, String password, String userType) {
        switch (userType.toLowerCase()) {
            case "student":
                return new student(userID, email, password);
            case "faculty":
                return new faculty(userID, email, password);
            case "visitor":
                return new visitor(userID, email, password);
            case "staff":
                return new staff(userID, email, password);
            case "manager":
                return new manager(userID, email, password);
            case "super_manager":
                return new super_manager(userID, email, password);
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }
}
