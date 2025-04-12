package yuparking.models;

public class faculty extends User {
    public faculty(int userID, String email, String password) {
        super(userID, email, password);
    }

    @Override
    public String getUserType() {
        return "Faculty";
    }
}
