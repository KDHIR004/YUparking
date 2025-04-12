package test;

import yuparking.models.visitor;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VisitorTest {

    // Test 1: user type should be Visitor
    @Test
    void testGetUserType() {
        visitor v = new visitor(1, "v1@gmail.com", "Pass123!");
        assertEquals("Visitor", v.getUserType());
    }

    // Test 2: email should be stored correctly
    @Test
    void testEmailStoredCorrectly() {
        visitor v = new visitor(2, "v2@gmail.com", "SomePass456!");
        assertEquals("v2@gmail.com", v.getEmail());
    }

    // Test 3: password should be same
    @Test
    void testPasswordStoredCorrectly() {
        visitor v = new visitor(3, "v3@gmail.com", "MyPass789!");
        assertEquals("MyPass789!", v.getPassword());
    }

    // Test 4: userID should be saved properly
    @Test
    void testUserIDStoredCorrectly() {
        visitor v = new visitor(4, "v4@gmail.com", "ThatPass321!");
        assertEquals(4, v.getUserID());
    }

    // Test 5: visitor should be verified by default in SignupService, but model is false
    @Test
    void testDefaultIsVerifiedIsFalse() {
        visitor v = new visitor(5, "v5@gmail.com", "Pass#321");
        assertFalse(v.isVerified());
    }

    // Test 6: after click it should be true
    @Test
    void testClickVerificationLink() {
        visitor v = new visitor(6, "v6@gmail.com", "Secure@123");
        v.clickVerificationLink();
        assertTrue(v.isVerified());
    }

    // Test 7: email can be long
    @Test
    void testLongEmail() {
        String email = "reallylongemailaddressformanagementtestingplus1@gmail.com";
        visitor v = new visitor(7, email, "Loooooong123!");
        assertEquals(email, v.getEmail());
    }

    // Test 8: password with special chars should be stored fine
    @Test
    void testSpecialCharacterPassword() {
        visitor v = new visitor(8, "v8@gmail.com", "!@#$$Pass123");
        assertEquals("!@#$$Pass123", v.getPassword());
    }

    // Test 9: email with uppercase should stay as-is
    @Test
    void testEmailCasePreserved() {
        visitor v = new visitor(9, "VISITOR@GMAIL.COM", "PassABC123!");
        assertEquals("VISITOR@GMAIL.COM", v.getEmail());
    }

    // Test 10: making two visitors with different info
    @Test
    void testMultipleVisitors() {
        visitor v1 = new visitor(10, "a@a.com", "1234A!");
        visitor v2 = new visitor(11, "b@b.com", "5678B!");

        assertNotEquals(v1.getUserID(), v2.getUserID());
        assertNotEquals(v1.getEmail(), v2.getEmail());
        assertNotEquals(v1.getPassword(), v2.getPassword());
    }
}
