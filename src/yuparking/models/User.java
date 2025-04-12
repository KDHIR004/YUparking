package yuparking.models;

public abstract class User {
    protected int userID;
    protected String email;
    protected String password;
    protected boolean verified = false;

    public User(int userID, String email, String password) {
        this.userID = userID;
        this.email = email;
        this.password = password;
    }

    public abstract String getUserType();

    public int getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public void clickVerificationLink() {
        this.verified = true;
        System.out.println(email + " has verified their email.");
    }

    public boolean isVerified() {
        return verified;
    }
}
