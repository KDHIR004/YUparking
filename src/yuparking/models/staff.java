package yuparking.models;

public class staff extends User {
    public staff(int userID, String email, String password) {
        super(userID, email, password);
    }

    @Override
    public String getUserType() {
        return "Staff";
    }
}
