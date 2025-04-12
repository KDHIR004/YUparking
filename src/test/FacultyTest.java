package test;

import org.junit.jupiter.api.Test;

import yuparking.models.faculty;

import static org.junit.jupiter.api.Assertions.*;

public class FacultyTest {

    // Test 1: test if the usertype is faculty
    @Test
    void testGetUserType() {
        faculty f = new faculty(1, "prof@yorku.ca", "StrongPass1!");
        assertEquals("Faculty", f.getUserType());
    }
    // Test 2: checking if email is same as passed in constructor
    @Test
    void testEmailIsStoredCorrectly() {
        faculty f = new faculty(2, "janedoe@yorku.ca", "MyPass123!");
        assertEquals("janedoe@yorku.ca", f.getEmail());
    }

    // Test 3: test if password is stored right
    @Test
    void testPasswordIsStoredCorrectly() {
        faculty f = new faculty(3, "johnsmith@yorku.ca", "P@ssw0rd");
        assertEquals("P@ssw0rd", f.getPassword());
    }

    // Test 4: checking user id
    @Test
    void testUserIDIsStoredCorrectly() {
        faculty f = new faculty(4, "uid@yorku.ca", "Password123!");
        assertEquals(4, f.getUserID());
    }

    // Test 5: default verified is false i think
    @Test
    void testDefaultVerificationIsFalse() {
        faculty f = new faculty(5, "unverified@yorku.ca", "Password123!");
        assertFalse(f.isVerified());
    }

    // Test 6: after click it should be true right
    @Test
    void testClickVerificationLink() {
        faculty f = new faculty(6, "verifyme@yorku.ca", "MyPass#123");
        f.clickVerificationLink();
        assertTrue(f.isVerified());
    }

    // Test 7: test if email with caps still same
    @Test
    void testEmailCaseIsPreserved() {
        faculty f = new faculty(7, "UPPERCASE@yorku.ca", "StrongPass1!");
        assertEquals("UPPERCASE@yorku.ca", f.getEmail());
    }

    // Test 8: password has symbols just testing if works
    @Test
    void testSpecialCharacterPassword() {
        faculty f = new faculty(8, "spec@yorku.ca", "@Speci@l#123");
        assertEquals("@Speci@l#123", f.getPassword());
    }

    // Test 9: long password test just to see
    @Test
    void testLongPasswordHandling() {
        String longPass = "ThisIsAVeryStrongPassword123!@#WithSymbols";
        faculty f = new faculty(9, "longpass@yorku.ca", longPass);
        assertEquals(longPass, f.getPassword());
    }

    // Test 10: making 2 objects and seeing if they are different
    @Test
    void testMultipleFacultyInstances() {
        faculty f1 = new faculty(10, "a@yorku.ca", "Pass123!");
        faculty f2 = new faculty(11, "b@yorku.ca", "DiffPass456!");

        assertNotEquals(f1.getUserID(), f2.getUserID());
        assertNotEquals(f1.getEmail(), f2.getEmail());
        assertNotEquals(f1.getPassword(), f2.getPassword());
    }
}
