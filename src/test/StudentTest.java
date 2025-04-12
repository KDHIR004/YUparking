package test;

import org.junit.jupiter.api.Test;

import yuparking.models.student;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    // 1. Constructor sets user ID correctly
    @Test
    void testUserID() {
        student s = new student(1, "a@yorku.ca", "pass");
        assertEquals(1, s.getUserID());
    }

    // 2. Constructor sets email correctly
    @Test
    void testEmail() {
        student s = new student(2, "b@yorku.ca", "pass");
        assertEquals("b@yorku.ca", s.getEmail());
    }

    // 3. Constructor sets password correctly
    @Test
    void testPassword() {
        student s = new student(3, "c@yorku.ca", "secret");
        assertEquals("secret", s.getPassword());
    }

    // 4. New student is not verified by default
    @Test
    void testDefaultVerificationStatus() {
        student s = new student(4, "d@yorku.ca", "pass");
        assertFalse(s.isVerified());
    }

    // 5. clickVerificationLink() sets verified to true
    @Test
    void testClickVerificationLink() {
        student s = new student(5, "e@yorku.ca", "pass");
        s.clickVerificationLink();
        assertTrue(s.isVerified());
    }

    // 6. getUserType returns "Student"
    @Test
    void testUserTypeIsStudent() {
        student s = new student(6, "f@yorku.ca", "pass");
        assertEquals("Student", s.getUserType());
    }

    // 7. Different students have unique userIDs
    @Test
    void testMultipleStudentsUniqueIDs() {
        student s1 = new student(10, "a@yorku.ca", "p1");
        student s2 = new student(20, "b@yorku.ca", "p2");
        assertNotEquals(s1.getUserID(), s2.getUserID());
    }

    // 8. Email is case-sensitive (unless you plan otherwise)
    @Test
    void testEmailCaseSensitivity() {
        student s = new student(7, "CASE@yorku.ca", "pass");
        assertEquals("CASE@yorku.ca", s.getEmail());
        assertNotEquals("case@yorku.ca", s.getEmail());
    }

    // 9. Password is not empty (basic sanity check)
    @Test
    void testNonEmptyPassword() {
        student s = new student(8, "g@yorku.ca", "nonempty");
        assertFalse(s.getPassword().isEmpty());
    }

    // 10. Changing verification flag works only via clickVerificationLink()
    @Test
    void testVerificationOnlyViaMethod() {
        student s = new student(9, "h@yorku.ca", "pass");
        assertFalse(s.isVerified());
        s.clickVerificationLink();
        assertTrue(s.isVerified());
    }
}