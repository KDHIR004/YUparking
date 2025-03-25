package yuparking.models;

public class super_manager extends User{
    public super_manager(int userID, String email, String password) {
        super(userID, email, password);
    }

    @Override
    public String getUserType() {
        return "super_manager";
    }
}
