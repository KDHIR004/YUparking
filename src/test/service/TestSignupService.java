package test.service;

import yuparking.services.SignupService;
import org.junit.jupiter.api.*;
import java.io.*;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

public class TestSignupService {

    SignupService signup;

    @BeforeEach
    void setup() {
        signup = new SignupService();
    }
    
    @AfterEach
    void cleanTestUsers() {
        List<String[]> cleaned = new ArrayList<>();
        cleaned.add(new String[]{"id", "email", "password", "usertype", "verified"}); // header

        // real users only
        cleaned.add(new String[]{"1", "manager1@yorku.ca", "man123", "manager", "true"});
        cleaned.add(new String[]{"2", "supermanager1@yorku.ca", "superman123", "super_manager", "true"});
        cleaned.add(new String[]{"3", "faculty1@yorku.ca", "facpass1", "faculty", "true"});
        cleaned.add(new String[]{"4", "staff1@yorku.ca", "staffpass1", "staff", "true"});
        cleaned.add(new String[]{"5", "student1@my.yorku.ca", "studpass1", "student", "true"});

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/yuparking/data/users.csv"))) {
            for (String[] row : cleaned) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    
    //Test 7: short password
    @Test
    void testShortPasswordFailsSignup() {
        boolean result = signup.signup("shortpass@my.yorku.ca", "Sh1@", "student");
        assertFalse(result, "Signup should fail for passwords shorter than 8 characters");
    }

    // Test 10: Case-insensitive user type
    @Test
    void testMixedCaseUserType() {
        boolean result = signup.signup("casetest@my.yorku.ca", "Strong1@", "StUdEnT");
        assertTrue(result);
    }
}
