package test;


import yuparking.models.manager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {

    // Test 1: user type should be manager
    @Test
    void testGetUserType() {
        manager m = new manager(1, "manager@yorku.ca", "Pass@123");
        assertEquals("manager", m.getUserType());
    }

    // Test 2: email should be stored
    @Test
    void testEmailStoredCorrectly() {
        manager m = new manager(2, "m2@yorku.ca", "Abcd1234!");
        assertEquals("m2@yorku.ca", m.getEmail());
    }

    // Test 3: password stored correctly
    @Test
    void testPasswordStoredCorrectly() {
        manager m = new manager(3, "m3@yorku.ca", "Test@123");
        assertEquals("Test@123", m.getPassword());
    }

    // Test 4: userID is correct
    @Test
    void testUserIDStoredCorrectly() {
        manager m = new manager(4, "m4@yorku.ca", "Hello@123");
        assertEquals(4, m.getUserID());
    }

    // Test 5: verified is false by default
    @Test
    void testDefaultVerifiedIsFalse() {
        manager m = new manager(5, "m5@yorku.ca", "Random@123");
        assertFalse(m.isVerified());
    }

    // Test 6: click verification should turn true
    @Test
    void testClickVerificationLink() {
        manager m = new manager(6, "m6@yorku.ca", "Check@123");
        m.clickVerificationLink();
        assertTrue(m.isVerified());
    }

    // Test 7: empty password accepted
    @Test
    void testEmptyPassword() {
        manager m = new manager(7, "m7@yorku.ca", "");
        assertEquals("", m.getPassword());
    }

    // Test 8: email is stored even if empty
    @Test
    void testEmptyEmail() {
        manager m = new manager(8, "", "Valid@123");
        assertEquals("", m.getEmail());
    }

    // Test 9: long email is stored fine
    @Test
    void testLongEmailStored() {
        String email = "averylongemailmanagertestcase@example.com";
        manager m = new manager(9, email, "Pass@Word1");
        assertEquals(email, m.getEmail());
    }

    // Test 10: different manager objects
    @Test
    void testMultipleManagers() {
        manager m1 = new manager(10, "m1@yorku.ca", "Pass1!");
        manager m2 = new manager(11, "m2@yorku.ca", "Pass2!");

        assertNotEquals(m1.getUserID(), m2.getUserID());
        assertNotEquals(m1.getEmail(), m2.getEmail());
        assertNotEquals(m1.getPassword(), m2.getPassword());
    }
}
