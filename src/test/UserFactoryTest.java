package test;

import yuparking.factory.UserFactory;
import yuparking.models.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserFactoryTest {

    // Test 1: create student and check type
    @Test
    void testCreateStudent() {
        User u = UserFactory.createUser(1, "s@yorku.ca", "Pass@1", "student");
        assertTrue(u instanceof student);
        assertEquals("Student", u.getUserType());
    }

    // Test 2: create faculty and check type
    @Test
    void testCreateFaculty() {
        User u = UserFactory.createUser(2, "f@yorku.ca", "Pass@2", "faculty");
        assertTrue(u instanceof faculty);
        assertEquals("Faculty", u.getUserType());
    }

    // Test 3: create visitor and check type
    @Test
    void testCreateVisitor() {
        User u = UserFactory.createUser(3, "v@gmail.com", "Pass@3", "visitor");
        assertTrue(u instanceof visitor);
        assertEquals("Visitor", u.getUserType());
    }

    // Test 4: create staff and check type
    @Test
    void testCreateStaff() {
        User u = UserFactory.createUser(4, "t@yorku.ca", "Pass@4", "staff");
        assertTrue(u instanceof staff);
        assertEquals("Staff", u.getUserType());
    }

    // Test 5: create manager and check type
    @Test
    void testCreateManager() {
        User u = UserFactory.createUser(5, "m@yorku.ca", "Pass@5", "manager");
        assertTrue(u instanceof manager);
        assertEquals("manager", u.getUserType());
    }

    // Test 6: create super_manager and check type
    @Test
    void testCreateSuperManager() {
        User u = UserFactory.createUser(6, "sm@yorku.ca", "Pass@6", "super_manager");
        assertTrue(u instanceof super_manager);
        assertEquals("super_manager", u.getUserType());
    }

    // Test 7: check if user type is case-insensitive
    @Test
    void testCreateUserCaseInsensitive() {
        User u = UserFactory.createUser(7, "s2@yorku.ca", "Pass@7", "StUdEnt");
        assertTrue(u instanceof student);
    }

    // Test 8: invalid user type throws exception
    @Test
    void testCreateInvalidUserThrows() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            UserFactory.createUser(8, "x@yorku.ca", "Pass@8", "alien");
        });
        assertTrue(e.getMessage().contains("Unknown user type"));
    }

    // Test 9: null user type throws exception
    @Test
    void testCreateWithNullTypeThrows() {
        assertThrows(NullPointerException.class, () -> {
            UserFactory.createUser(9, "null@yorku.ca", "Pass@9", null);
        });
    }

    // Test 10: empty user type throws exception
    @Test
    void testCreateWithEmptyTypeThrows() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            UserFactory.createUser(10, "e@yorku.ca", "Pass@10", "");
        });
        assertTrue(e.getMessage().contains("Unknown user type"));
    }
}
