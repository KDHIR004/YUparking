package test;

import yuparking.models.super_manager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Super_managerTest {

    // Test 1: user type should be super_manager
    @Test
    void testGetUserType() {
        super_manager sm = new super_manager(1, "sm1@yorku.ca", "Pass@123");
        assertEquals("super_manager", sm.getUserType());
    }

    // Test 2: email should be saved
    @Test
    void testEmailStoredCorrectly() {
        super_manager sm = new super_manager(2, "sm2@yorku.ca", "Admin@123");
        assertEquals("sm2@yorku.ca", sm.getEmail());
    }

    // Test 3: password should be stored
    @Test
    void testPasswordStoredCorrectly() {
        super_manager sm = new super_manager(3, "sm3@yorku.ca", "Root@456");
        assertEquals("Root@456", sm.getPassword());
    }

    // Test 4: userID should be same as given
    @Test
    void testUserIDStoredCorrectly() {
        super_manager sm = new super_manager(4, "sm4@yorku.ca", "MegaPass123");
        assertEquals(4, sm.getUserID());
    }

    // Test 5: should not be verified by default
    @Test
    void testDefaultIsVerifiedFalse() {
        super_manager sm = new super_manager(5, "sm5@yorku.ca", "TryThis@1");
        assertFalse(sm.isVerified());
    }

    // Test 6: clicking verification makes it true
    @Test
    void testClickVerificationLink() {
        super_manager sm = new super_manager(6, "sm6@yorku.ca", "Okay@123");
        sm.clickVerificationLink();
        assertTrue(sm.isVerified());
    }

    // Test 7: test with empty password
    @Test
    void testEmptyPasswordAllowed() {
        super_manager sm = new super_manager(7, "sm7@yorku.ca", "");
        assertEquals("", sm.getPassword());
    }

    // Test 8: empty email is still saved
    @Test
    void testEmptyEmail() {
        super_manager sm = new super_manager(8, "", "EmptyMail@1");
        assertEquals("", sm.getEmail());
    }

    // Test 9: email with uppercase stays same
    @Test
    void testEmailCasePreserved() {
        super_manager sm = new super_manager(9, "SUPER@YORKU.CA", "Super@Case");
        assertEquals("SUPER@YORKU.CA", sm.getEmail());
    }

    // Test 10: two different super managers
    @Test
    void testMultipleSuperManagers() {
        super_manager sm1 = new super_manager(10, "one@yorku.ca", "One@123");
        super_manager sm2 = new super_manager(11, "two@yorku.ca", "Two@456");

        assertNotEquals(sm1.getUserID(), sm2.getUserID());
        assertNotEquals(sm1.getEmail(), sm2.getEmail());
        assertNotEquals(sm1.getPassword(), sm2.getPassword());
    }
}
