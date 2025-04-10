package test.service;
import yuparking.services.SignupService;
import yuparking.database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class SignupServiceTest {
   private SignupService signupService;
   @BeforeEach
   void setUp() {
       signupService = new SignupService();
   }
   @Test
   void testSignupValidFaculty() {
       boolean result = signupService.signup("faculty@yorku.ca", "StrongPass123!", "faculty");
       assertTrue(result);
   }
   @Test
   void testSignupValidStudent() {
       boolean result = signupService.signup("student@my.yorku.ca", "StudentPass!2023", "student");
       assertTrue(result);
   }
   @Test
   void testSignupValidVisitor() {
       boolean result = signupService.signup("visitor123@gmail.com", "VisitorPass#123", "visitor");
       assertTrue(result);
   }
   @Test
   void testSignupInvalidUserType() {
       boolean result = signupService.signup("invalid@yorku.ca", "Password123!", "admin");
       assertFalse(result);
   }
   @Test
   void testSignupInvalidEmailFormatForStudent() {
       boolean result = signupService.signup("student@yorku.ca", "Password123!", "student");
       assertFalse(result);
   }
   @Test
   void testSignupInvalidEmailFormatForFaculty() {
       boolean result = signupService.signup("faculty@my.yorku.ca", "Password123!", "faculty");
       assertFalse(result);
   }
   @Test
   void testSignupShortPassword() {
       boolean result = signupService.signup("staff@yorku.ca", "Short1!", "staff");
       assertFalse(result);
   }
   @Test
   void testSignupWeakPassword() {
       boolean result = signupService.signup("faculty@yorku.ca", "Password123", "faculty");
       assertFalse(result);
   }
   @Test
   void testSignupEmailAlreadyExists() {
       signupService.signup("faculty@yorku.ca", "StrongPass123!", "faculty");
       boolean result = signupService.signup("faculty@yorku.ca", "AnotherPassword123!", "faculty");
       assertFalse(result);
   }
   @Test
   void testSignupForVisitorWithInvalidEmail() {
       boolean result = signupService.signup("visitor@yorku.ca", "VisitorPass#123", "visitor");
       assertFalse(result);
   }
}
