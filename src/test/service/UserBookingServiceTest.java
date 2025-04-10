package test;

import yuparking.services.UserBookingService;
import yuparking.database.Database;
import yuparking.models.User;
import yuparking.factory.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

class UserBookingServiceTest{

    private UserBookingService userBookingService;
    private Database mockDatabase;
    private User mockUser;

    @BeforeEach
    void setUp(){

        mockDatabase = new Database();
        userBookingService = new UserBookingService();
 
        mockUser = UserFactory.createUser(1001, "user@example.com", "password123", "student"); 
    }

    @Test
    void testCreateBookingValid() {
        String startTime = "2025-04-10T09:00:00";
        String endTime = "2025-04-10T11:00:00";
    
        String[] bookingData ={
            "1", "1001", "1", startTime, endTime, "Booked"
        };
        List<String[]> existingBookings = mockDatabase.retrieveData("bookings");
        existingBookings.add(bookingData); 
        mockDatabase.confirmUpdate("bookings", existingBookings);  

        
        List<String[]> bookings = mockDatabase.retrieveData("bookings");
        assertEquals(1, bookings.size());
        assertEquals("Booked", bookings.get(0)[5]);
    }

    @Test
    void testCreateBookingInvalidTime(){
        String startTime = "2025-04-10T11:00:00";
        String endTime = "2025-04-10T09:00:00";

        
        userBookingService.createUserBooking(mockUser, 1, startTime, endTime);

       
        List<String[]> bookings = mockDatabase.retrieveData("bookings");
        assertEquals(0, bookings.size());
    }

    @Test
    void testCreateBookingUserNotVerified(){
    
        mockUser = UserFactory.createUser(1002, "unverified@example.com", "password123", "visitor"); 
        String startTime = "2025-04-10T09:00:00";
        String endTime = "2025-04-10T11:00:00";
        userBookingService.createUserBooking(mockUser, 1, startTime, endTime);
        List<String[]> bookings = mockDatabase.retrieveData("bookings");
        assertEquals(0, bookings.size());
    }

    @Test
    void testModifyBookingValid(){
        String[] bookingData = {
            "1", "1001", "1", "2025-04-10T09:00:00", "2025-04-10T11:00:00", "Booked"
        };
        List<String[]> existingBookings = mockDatabase.retrieveData("bookings");
        existingBookings.add(bookingData);  
        mockDatabase.confirmUpdate("bookings", existingBookings);  

        String newStartTime = "2025-04-10T10:00:00";
        String newEndTime = "2025-04-10T12:00:00";

        userBookingService.modifyUserBooking(mockUser, 1, newStartTime, newEndTime);

        List<String[]> bookings = mockDatabase.retrieveData("bookings");
        assertEquals("2025-04-10T10:00:00", bookings.get(0)[3]); 
        assertEquals("2025-04-10T12:00:00", bookings.get(0)[4]); 
    }

    @Test
    void testModifyBookingInvalidTime() {
        String[] bookingData = {
            "1", "1001", "1", "2025-04-10T09:00:00", "2025-04-10T11:00:00", "Booked"
        };
        List<String[]> existingBookings = mockDatabase.retrieveData("bookings");
        existingBookings.add(bookingData);  
        mockDatabase.confirmUpdate("bookings", existingBookings); 
        String newStartTime = "2025-04-10T12:00:00";
        String newEndTime = "2025-04-10T11:00:00";

      
        userBookingService.modifyUserBooking(mockUser, 1, newStartTime, newEndTime);

        List<String[]> bookings = mockDatabase.retrieveData("bookings");
        assertEquals("2025-04-10T09:00:00", bookings.get(0)[3]);  
    }

    @Test
    void testModifyBookingNotActive(){
        String[] bookingData = {
            "1", "1001", "1", "2025-04-10T09:00:00", "2025-04-10T11:00:00", "Cancelled"
        };
        List<String[]> existingBookings = mockDatabase.retrieveData("bookings");
        existingBookings.add(bookingData); 
        mockDatabase.confirmUpdate("bookings", existingBookings);  

        String newStartTime = "2025-04-10T10:00:00";
        String newEndTime = "2025-04-10T12:00:00";


        userBookingService.modifyUserBooking(mockUser, 1, newStartTime, newEndTime);


        List<String[]> bookings = mockDatabase.retrieveData("bookings");
        assertEquals("2025-04-10T09:00:00", bookings.get(0)[3]);  
    }

    @Test
    void testCancelBookingValid(){
        String[] bookingData = {
            "1", "1001", "1", "2025-04-10T09:00:00", "2025-04-10T11:00:00", "Booked"
        };
        List<String[]> existingBookings = mockDatabase.retrieveData("bookings");
        existingBookings.add(bookingData); 
        mockDatabase.confirmUpdate("bookings", existingBookings);  

        userBookingService.cancelUserBooking(mockUser, 1);

        List<String[]> bookings = mockDatabase.retrieveData("bookings");
        assertEquals("Cancelled", bookings.get(0)[5]);  
    }

    @Test
    void testCancelBookingAlreadyCancelled(){
        String[] bookingData = {
            "1", "1001", "1", "2025-04-10T09:00:00", "2025-04-10T11:00:00", "Cancelled"
        };
        List<String[]> existingBookings = mockDatabase.retrieveData("bookings");
        existingBookings.add(bookingData); 
        mockDatabase.confirmUpdate("bookings", existingBookings); 

        userBookingService.cancelUserBooking(mockUser, 1);

        List<String[]> bookings = mockDatabase.retrieveData("bookings");
        assertEquals("Cancelled", bookings.get(0)[5]); 
    }

    @Test
    void testCancelBookingNotFound() {

        userBookingService.cancelUserBooking(mockUser, 1);

      
        List<String[]> bookings = mockDatabase.retrieveData("bookings");
        assertEquals(0, bookings.size());
    }

    @Test
    void testShowUserBookingsNoBookings() {
        userBookingService.showUserBookings(mockUser);


        List<String[]> bookings = mockDatabase.retrieveData("bookings");
        assertEquals(0, bookings.size());
    }
}


