package yuparking.models;

public class manager extends User{
    public manager(int userID, String email, String password) {
        super(userID, email, password);
    }

    @Override
    public String getUserType() {
        return "manager";
    }
}
