package yuparking.models;

public class UserFactory {
    public static User createUser(String userType, int userID, String email, String password) {
        switch (userType.toLowerCase()) {
            case "student":
                return new student(userID, email, password);
            case "faculty":
                return new faculty(userID, email, password);
            case "staff":
                return new staff(userID, email, password);
            case "visitor":
                return new visitor(userID, email, password);
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
}
