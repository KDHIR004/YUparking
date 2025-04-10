package test;

import org.junit.jupiter.api.Test;

import yuparking.models.staff;

import static org.junit.jupiter.api.Assertions.*;

public class Staff {

    // Test 1: check user type is Staff
    @Test
    void testGetUserType() {
        staff s = new staff(1, "staff1@yorku.ca", "StrongPass1!");
        assertEquals("Staff", s.getUserType());
    }

    // Test 2: email should be same as given
    @Test
    void testEmailStoredCorrectly() {
        staff s = new staff(2, "staff2@yorku.ca", "Password123!");
        assertEquals("staff2@yorku.ca", s.getEmail());
    }

    // Test 3: password is stored same
    @Test
    void testPasswordStoredCorrectly() {
        staff s = new staff(3, "staff3@yorku.ca", "Test@123");
        assertEquals("Test@123", s.getPassword());
    }

    // Test 4: userID should be correct
    @Test
    void testUserIDStoredCorrectly() {
        staff s = new staff(4, "staff4@yorku.ca", "Random@1");
        assertEquals(4, s.getUserID());
    }

    // Test 5: default isVerified should be false
    @Test
    void testDefaultVerificationIsFalse() {
        staff s = new staff(5, "staff5@yorku.ca", "Pass@555");
        assertFalse(s.isVerified());
    }

    // Test 6: after clicking link isVerified should be true
    @Test
    void testClickVerificationLink() {
        staff s = new staff(6, "staff6@yorku.ca", "CheckMe123!");
        s.clickVerificationLink();
        assertTrue(s.isVerified());
    }

    // Test 7: test empty email is accepted (bad, but model allows)
    @Test
    void testEmptyEmailAcceptedInModel() {
        staff s = new staff(7, "", "AnyPassword1!");
        assertEquals("", s.getEmail());
    }

    // Test 8: test empty password is accepted (again model allows)
    @Test
    void testEmptyPasswordAcceptedInModel() {
        staff s = new staff(8, "staff8@yorku.ca", "");
        assertEquals("", s.getPassword());
    }

    // Test 9: test long email is stored fine
    @Test
    void testLongEmail() {
        String longEmail = "thisisaverylongemailaddressusedforstafftestingpurposes@yorku.ca";
        staff s = new staff(9, longEmail, "ValidPass123!");
        assertEquals(longEmail, s.getEmail());
    }

    // Test 10: test 2 objects have different details
    @Test
    void testMultipleStaffObjects() {
        staff s1 = new staff(10, "a@yorku.ca", "OnePass123!");
        staff s2 = new staff(11, "b@yorku.ca", "TwoPass123!");

        assertNotEquals(s1.getUserID(), s2.getUserID());
        assertNotEquals(s1.getEmail(), s2.getEmail());
        assertNotEquals(s1.getPassword(), s2.getPassword());
    }
}
