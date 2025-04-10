package test.service;

import yuparking.services.SignupService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestSignupService {

    SignupService signup;

    @BeforeEach
    void setup() {
        signup = new SignupService();
    }

    // Test 1: Valid student signup
    @Test
    void testValidStudentSignup() {
        boolean result = signup.signup("teststudent@my.yorku.ca", "Strong1@", "student");
        assertTrue(result);
    }

    // Test 2: Valid faculty signup
    @Test
    void testValidFacultySignup() {
        boolean result = signup.signup("testfaculty@yorku.ca", "GoodPass1@", "faculty");
        assertTrue(result);
    }

    // Test 3: Valid visitor signup (any domain)
    @Test
    void testValidVisitorSignup() {
        boolean result = signup.signup("visitor@gmail.com", "Visit0r@", "visitor");
        assertTrue(result);
    }

    // Test 4: Invalid user type
    @Test
    void testInvalidUserType() {
        boolean result = signup.signup("random@yorku.ca", "Some1@", "alien");
        assertFalse(result);
    }

    // Test 5: Invalid email domain for student
    @Test
    void testInvalidStudentEmailDomain() {
        boolean result = signup.signup("student@gmail.com", "Strong1@", "student");
        assertFalse(result);
    }

    // Test 6: Invalid email domain for faculty
    @Test
    void testInvalidFacultyEmailDomain() {
        boolean result = signup.signup("fac@hotmail.com", "Test1@", "faculty");
        assertFalse(result);
    }

    // Test 7: Weak password (missing symbol)
    @Test
    void testWeakPasswordNoSymbol() {
        boolean result = signup.signup("weakstudent@my.yorku.ca", "Weakpass1", "student");
        assertFalse(result);
    }

    // Test 8: Duplicate email attempt (already exists)
    @Test
    void testDuplicateEmailSignup() {
        signup.signup("dupe@my.yorku.ca", "Strong1@", "student");
        boolean result = signup.signup("dupe@my.yorku.ca", "Strong1@", "student");
        assertFalse(result);
    }

    // Test 9: Visitor should auto-verify (indirect, check success)
    @Test
    void testVisitorAutoVerified() {
        boolean result = signup.signup("vnewuser@yahoo.com", "Valid1@", "visitor");
        assertTrue(result);
    }

    // Test 10: Case-insensitive user type
    @Test
    void testMixedCaseUserType() {
        boolean result = signup.signup("casetest@my.yorku.ca", "Strong1@", "StUdEnT");
        assertTrue(result);
    }
}
