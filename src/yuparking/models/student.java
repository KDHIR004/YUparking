package yuparking.models;

public class student extends User {
    public student(int userID, String email, String password) {
        super(userID, email, password);
    }

    @Override
    public String getUserType() {
        return "Student";
    }
}
