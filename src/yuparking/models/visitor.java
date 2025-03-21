package yuparking.models;

public class visitor extends User {
    public visitor(int userID, String email, String password) {
        super(userID, email, password);
    }

    @Override
    public String getUserType() {
        return "Visitor";
    }
}
