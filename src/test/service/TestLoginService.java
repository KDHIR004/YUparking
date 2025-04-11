package test.service;

import yuparking.services.LoginService;
import yuparking.database.Database;
import yuparking.models.User;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestLoginService {

    private LoginService loginService;
    private Database db;

    private final int TEST_USER_ID = 88;
    private final String TEST_EMAIL = "login88@my.yorku.ca";
    private final String TEST_PASSWORD = "StrongP@ss1";
    private final String TEST_TYPE = "student";

    @BeforeEach
    void setup() {
        loginService = new LoginService();
        db = new Database();

        // Ensure test user is in users.csv
        List<String[]> users = db.retrieveData("users");
        boolean exists = users.stream().anyMatch(row -> row[0].equals(String.valueOf(TEST_USER_ID)));

        if (!exists) {
            String[] testUser = new String[]{
                    String.valueOf(TEST_USER_ID),
                    TEST_EMAIL,
                    TEST_PASSWORD,
                    TEST_TYPE,
                    "false"
            };
            users.add(testUser);
            db.confirmUpdate("users", users);
        }
    }
    @AfterEach
    void cleanupTestUser() {
        System.out.println("🧹 Cleaning up test user...");

        List<String[]> users = db.retrieveData("users");
        users.removeIf(row -> row[0].equals(String.valueOf(TEST_USER_ID)));
        db.confirmUpdate("users", users);
    }



    // Test 1: Successful login
    @Test
    void testLoginSuccess() {
        User user = loginService.login(TEST_EMAIL, TEST_PASSWORD);
        assertNotNull(user);
        assertEquals(TEST_EMAIL, user.getEmail());
    }

    // Test 2: Wrong password
    @Test
    void testLoginWrongPassword() {
        assertNull(loginService.login(TEST_EMAIL, "WrongP@ss1"));
    }

    // Test 3: Wrong email
    @Test
    void testLoginWrongEmail() {
        assertNull(loginService.login("unknown@yorku.ca", TEST_PASSWORD));
    }

    // Test 4: Case-insensitive email
    @Test
    void testEmailCaseInsensitive() {
        User user = loginService.login(TEST_EMAIL.toUpperCase(), TEST_PASSWORD);
        assertNotNull(user);
        assertEquals(TEST_EMAIL, user.getEmail());
    }

    // Test 5: Email not found in CSV
    @Test
    void testEmailNotFoundInCSV() {
        assertNull(loginService.login("ghost@my.yorku.ca", "Fake123!"));
    }

    // Test 6: Update verification to true
    @Test
    void testSetVerificationTrue() {
        loginService.updateVerificationInCSV(TEST_USER_ID);
        List<String[]> users = db.retrieveData("users");
        String[] row = users.stream().filter(r -> r[0].equals(String.valueOf(TEST_USER_ID))).findFirst().orElse(null);
        assertNotNull(row);
        assertEquals("true", row[4]);
    }

    // Test 7: Update already-verified user (should stay true)
    @Test
    void testReUpdateVerification() {
        loginService.updateVerificationInCSV(TEST_USER_ID); // run twice
        loginService.updateVerificationInCSV(TEST_USER_ID);
        List<String[]> users = db.retrieveData("users");
        String[] row = users.stream().filter(r -> r[0].equals(String.valueOf(TEST_USER_ID))).findFirst().orElse(null);
        assertNotNull(row);
        assertEquals("true", row[4]);
    }

    // Test 8: Try updating verification for invalid user ID
    @Test
    void testUpdateInvalidUserID() {
        loginService.updateVerificationInCSV(99999); // should not crash
        List<String[]> users = db.retrieveData("users");
        boolean exists = users.stream().anyMatch(row -> row[0].equals("99999"));
        assertFalse(exists);
    }

    // Test 9: All loaded users have valid emails
    @Test
    void testAllUsersHaveEmail() {
        List<User> all = loginService.getAllUsers();
        for (User u : all) {
            assertTrue(u.getEmail().contains("@"));
        }
    }

    // Test 10: All users have 5 fields (CSV structure)
    @Test
    void testUserCSVStructure() {
        List<String[]> users = db.retrieveData("users");
        for (String[] row : users) {
            assertEquals(5, row.length);
        }
    }
}
