import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegressionTest0 {

    public static boolean debug = false;

    public void assertBooleanArrayEquals(boolean[] expectedArray, boolean[] actualArray) {
        if (expectedArray.length != actualArray.length) {
            throw new AssertionError("Array lengths differ: " + expectedArray.length + " != " + actualArray.length);
        }
        for (int i = 0; i < expectedArray.length; i++) {
            if (expectedArray[i] != actualArray[i]) {
                throw new AssertionError("Arrays differ at index " + i + ": " + expectedArray[i] + " != " + actualArray[i]);
            }
        }
    }

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        yuparking.factory.UserFactory userFactory0 = new yuparking.factory.UserFactory();
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        yuparking.models.faculty faculty3 = new yuparking.models.faculty((int) (short) -1, "hi!", "");
        java.lang.String str4 = faculty3.getUserType();
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "Faculty" + "'", str4, "Faculty");
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        yuparking.services.ManagementService managementService0 = new yuparking.services.ManagementService();
        int int1 = managementService0.getNextUserId();
        int int2 = managementService0.getNextUserId();
        int int3 = managementService0.getNextUserId();
// flaky "1) test03(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int1 + "' != '" + 11 + "'", int1 == 11);
// flaky "1) test03(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int2 + "' != '" + 11 + "'", int2 == 11);
// flaky "1) test03(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int3 + "' != '" + 11 + "'", int3 == 11);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        yuparking.models.student student3 = new yuparking.models.student((int) (short) 1, "", "");
        int int4 = student3.getUserID();
        int int5 = student3.getUserID();
        org.junit.Assert.assertTrue("'" + int4 + "' != '" + 1 + "'", int4 == 1);
        org.junit.Assert.assertTrue("'" + int5 + "' != '" + 1 + "'", int5 == 1);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        yuparking.services.ManagementService managementService0 = new yuparking.services.ManagementService();
        int int1 = managementService0.getNextUserId();
        int int2 = managementService0.getNextUserId();
        managementService0.generateManagerAccount();
// flaky "2) test05(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int1 + "' != '" + 11 + "'", int1 == 11);
// flaky "2) test05(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int2 + "' != '" + 11 + "'", int2 == 11);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        yuparking.services.payment.VisitorParkingFee visitorParkingFee0 = new yuparking.services.payment.VisitorParkingFee();
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        yuparking.services.payment.StudentParkingFee studentParkingFee0 = new yuparking.services.payment.StudentParkingFee();
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        // The following exception was thrown during execution in test generation
        try {
            yuparking.models.User user4 = yuparking.factory.UserFactory.createUser((int) (byte) 100, "hi!", "", "");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unknown user type: ");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        yuparking.services.LoginService loginService0 = new yuparking.services.LoginService();
        loginService0.updateVerificationInCSV((-1));
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        yuparking.models.student student3 = new yuparking.models.student(0, "hi!", "");
        student3.clickVerificationLink();
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        yuparking.services.LoginService loginService0 = new yuparking.services.LoginService();
        yuparking.models.User user3 = loginService0.login("Visitor", "hi!");
        org.junit.Assert.assertNull(user3);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        yuparking.services.ManagementService managementService0 = new yuparking.services.ManagementService();
        int int1 = managementService0.getNextUserId();
        int int2 = managementService0.getNextUserId();
        java.lang.Class<?> wildcardClass3 = managementService0.getClass();
// flaky "3) test12(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int1 + "' != '" + 13 + "'", int1 == 13);
// flaky "3) test12(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int2 + "' != '" + 13 + "'", int2 == 13);
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        yuparking.models.faculty faculty3 = new yuparking.models.faculty((int) 'a', "Visitor", "super_manager");
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        yuparking.services.LoginService loginService0 = new yuparking.services.LoginService();
        java.util.List<yuparking.models.User> userList1 = loginService0.getAllUsers();
        yuparking.models.User user4 = loginService0.login("", "");
        org.junit.Assert.assertNotNull(userList1);
        org.junit.Assert.assertNull(user4);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        yuparking.services.ParkingLotService parkingLotService0 = new yuparking.services.ParkingLotService();
        parkingLotService0.updateSpaceStatus((int) (byte) 1, "Faculty");
        parkingLotService0.viewParkingLots();
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        yuparking.models.super_manager super_manager3 = new yuparking.models.super_manager((int) (short) 10, "Faculty", "");
        java.lang.String str4 = super_manager3.getUserType();
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "super_manager" + "'", str4, "super_manager");
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        yuparking.services.UserBookingService userBookingService0 = new yuparking.services.UserBookingService();
        yuparking.services.BookingService bookingService1 = new yuparking.services.BookingService();
        yuparking.models.student student5 = new yuparking.models.student(0, "hi!", "");
        double double7 = bookingService1.calculateFeeForBooking((yuparking.models.User) student5, (double) (byte) 10);
        // The following exception was thrown during execution in test generation
        try {
            userBookingService0.modifyUserBooking((yuparking.models.User) student5, (int) (byte) 10, "Faculty", "super_manager");
            org.junit.Assert.fail("Expected exception of type java.time.format.DateTimeParseException; message: Text 'Faculty' could not be parsed at index 0");
        } catch (java.time.format.DateTimeParseException e) {
            // Expected exception.
        }
        org.junit.Assert.assertTrue("'" + double7 + "' != '" + 50.0d + "'", double7 == 50.0d);
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test18");
        yuparking.models.visitor visitor3 = new yuparking.models.visitor((-1), "hi!", "");
        java.lang.String str4 = visitor3.getUserType();
        java.lang.String str5 = visitor3.getUserType();
        java.lang.String str6 = visitor3.getPassword();
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "Visitor" + "'", str4, "Visitor");
        org.junit.Assert.assertEquals("'" + str5 + "' != '" + "Visitor" + "'", str5, "Visitor");
        org.junit.Assert.assertEquals("'" + str6 + "' != '" + "" + "'", str6, "");
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test19");
        yuparking.services.SignupService signupService0 = new yuparking.services.SignupService();
        int int1 = signupService0.getNextUserId();
// flaky "4) test19(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int1 + "' != '" + 13 + "'", int1 == 13);
    }

    @Test
    public void test20() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test20");
        yuparking.services.ManagementService managementService0 = new yuparking.services.ManagementService();
        int int1 = managementService0.getNextUserId();
        managementService0.generateManagerAccount();
        managementService0.viewDetailedHistory();
// flaky "5) test20(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int1 + "' != '" + 13 + "'", int1 == 13);
    }

    @Test
    public void test21() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test21");
        yuparking.services.payment.FacultyParkingFee facultyParkingFee0 = new yuparking.services.payment.FacultyParkingFee();
    }

    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test22");
        // The following exception was thrown during execution in test generation
        try {
            yuparking.models.User user4 = yuparking.factory.UserFactory.createUser((-1), "hi!", "super_manager", "hi!");
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: Unknown user type: hi!");
        } catch (java.lang.IllegalArgumentException e) {
            // Expected exception.
        }
    }

    @Test
    public void test23() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test23");
        yuparking.services.PaymentService paymentService0 = new yuparking.services.PaymentService();
        paymentService0.refundPayment((int) (byte) -1);
        paymentService0.refundPayment((int) (short) 1);
    }

    @Test
    public void test24() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test24");
        yuparking.models.Booking booking6 = new yuparking.models.Booking(100, (int) (short) 0, 0, "", "", "Visitor");
        java.lang.String str7 = booking6.getStartTime();
        booking6.confirmBookingChanges();
        org.junit.Assert.assertEquals("'" + str7 + "' != '" + "" + "'", str7, "");
    }

    @Test
    public void test25() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test25");
        yuparking.models.student student3 = new yuparking.models.student((int) '4', "", "Visitor");
    }

    @Test
    public void test26() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test26");
        yuparking.services.ParkingLotService parkingLotService0 = new yuparking.services.ParkingLotService();
        parkingLotService0.removeSpace((int) (byte) 10);
    }

    @Test
    public void test27() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test27");
        yuparking.models.Booking booking6 = new yuparking.models.Booking(100, (int) (short) 0, 0, "", "", "Visitor");
        java.lang.String str7 = booking6.getEndTime();
        java.lang.String str8 = booking6.getStartTime();
        booking6.cancelBooking();
        org.junit.Assert.assertEquals("'" + str7 + "' != '" + "" + "'", str7, "");
        org.junit.Assert.assertEquals("'" + str8 + "' != '" + "" + "'", str8, "");
    }

    @Test
    public void test28() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test28");
        yuparking.models.visitor visitor3 = new yuparking.models.visitor((-1), "hi!", "");
        java.lang.String str4 = visitor3.getUserType();
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "Visitor" + "'", str4, "Visitor");
    }

    @Test
    public void test29() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test29");
        yuparking.services.ManagementService managementService0 = new yuparking.services.ManagementService();
        int int1 = managementService0.getNextUserId();
        managementService0.generateManagerAccount();
        int int3 = managementService0.getNextUserId();
        managementService0.viewDetailedHistory();
        managementService0.generateManagerAccount();
// flaky "6) test29(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int1 + "' != '" + 14 + "'", int1 == 14);
// flaky "4) test29(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int3 + "' != '" + 15 + "'", int3 == 15);
    }

    @Test
    public void test30() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test30");
        yuparking.services.ParkingSensorService parkingSensorService0 = new yuparking.services.ParkingSensorService();
        parkingSensorService0.simulateOccupancyUpdate();
        parkingSensorService0.triggerOccupancyAlerts();
    }

    @Test
    public void test31() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test31");
        yuparking.database.Database database0 = new yuparking.database.Database();
        java.util.List<java.lang.String[]> strArrayList2 = database0.retrieveData("Faculty");
        java.lang.Class<?> wildcardClass3 = strArrayList2.getClass();
        org.junit.Assert.assertNotNull(strArrayList2);
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test32() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test32");
        yuparking.models.student student3 = new yuparking.models.student(11, "super_manager", "hi!");
    }

    @Test
    public void test33() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test33");
        yuparking.models.student student3 = new yuparking.models.student((int) 'a', "Faculty", "Faculty");
    }

    @Test
    public void test34() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test34");
        yuparking.services.UserBookingService userBookingService0 = new yuparking.services.UserBookingService();
        yuparking.models.super_manager super_manager4 = new yuparking.models.super_manager((int) (short) 10, "Faculty", "");
        java.lang.String str5 = super_manager4.getUserType();
        userBookingService0.showUserBookings((yuparking.models.User) super_manager4);
        org.junit.Assert.assertEquals("'" + str5 + "' != '" + "super_manager" + "'", str5, "super_manager");
    }

    @Test
    public void test35() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test35");
        yuparking.models.Booking booking6 = new yuparking.models.Booking(100, (int) '4', (int) '#', "Faculty", "hi!", "Faculty");
    }

    @Test
    public void test36() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test36");
        yuparking.models.super_manager super_manager3 = new yuparking.models.super_manager(0, "Visitor", "super_manager");
    }

    @Test
    public void test37() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test37");
        yuparking.models.super_manager super_manager3 = new yuparking.models.super_manager((int) (short) 10, "Faculty", "");
        java.lang.String str4 = super_manager3.getUserType();
        java.lang.String str5 = super_manager3.getPassword();
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "super_manager" + "'", str4, "super_manager");
        org.junit.Assert.assertEquals("'" + str5 + "' != '" + "" + "'", str5, "");
    }

    @Test
    public void test38() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test38");
        yuparking.services.ManagementService managementService0 = new yuparking.services.ManagementService();
        int int1 = managementService0.getNextUserId();
        managementService0.generateManagerAccount();
        int int3 = managementService0.getNextUserId();
        managementService0.viewDetailedHistory();
        // The following exception was thrown during execution in test generation
        try {
            managementService0.modifyAnyBooking(13, "", "Faculty");
            org.junit.Assert.fail("Expected exception of type java.time.format.DateTimeParseException; message: Text '' could not be parsed at index 0");
        } catch (java.time.format.DateTimeParseException e) {
            // Expected exception.
        }
// flaky "7) test38(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int1 + "' != '" + 16 + "'", int1 == 16);
// flaky "5) test38(RegressionTest0)":         org.junit.Assert.assertTrue("'" + int3 + "' != '" + 17 + "'", int3 == 17);
    }

    @Test
    public void test39() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test39");
        yuparking.models.staff staff3 = new yuparking.models.staff((int) (byte) 10, "hi!", "Visitor");
        java.lang.String str4 = staff3.getUserType();
        org.junit.Assert.assertEquals("'" + str4 + "' != '" + "Staff" + "'", str4, "Staff");
    }

    @Test
    public void test40() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test40");
        yuparking.models.staff staff3 = new yuparking.models.staff(100, "", "");
    }

    @Test
    public void test41() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test41");
        yuparking.models.Booking booking6 = new yuparking.models.Booking(100, (int) (short) 0, 0, "", "", "Visitor");
        booking6.confirmBookingChanges();
        int int8 = booking6.getSpaceID();
        org.junit.Assert.assertTrue("'" + int8 + "' != '" + 0 + "'", int8 == 0);
    }

    @Test
    public void test42() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test42");
        yuparking.services.ParkingLotService parkingLotService0 = new yuparking.services.ParkingLotService();
        parkingLotService0.updateSpaceStatus((int) (byte) 10, "Faculty");
        parkingLotService0.removeSpace(0);
        parkingLotService0.addNewParkingLot(100, "super_manager", (int) (byte) 10);
    }

    @Test
    public void test43() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test43");
        yuparking.models.student student3 = new yuparking.models.student((int) (short) 1, "", "");
        boolean boolean4 = student3.isVerified();
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
    }

    @Test
    public void test44() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test44");
        yuparking.services.ParkingLotService parkingLotService0 = new yuparking.services.ParkingLotService();
        parkingLotService0.updateSpaceStatus((int) (byte) 10, "Faculty");
        parkingLotService0.removeParkingLot((int) (short) 1);
        parkingLotService0.enableLot((int) '#');
    }

    @Test
    public void test45() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test45");
        yuparking.models.Booking booking6 = new yuparking.models.Booking(100, (int) (short) 0, 0, "", "", "Visitor");
        booking6.modifyBooking("Faculty", "Faculty");
        java.lang.String str10 = booking6.getStartTime();
        java.lang.String str11 = booking6.getEndTime();
        org.junit.Assert.assertEquals("'" + str10 + "' != '" + "Faculty" + "'", str10, "Faculty");
        org.junit.Assert.assertEquals("'" + str11 + "' != '" + "Faculty" + "'", str11, "Faculty");
    }

    @Test
    public void test46() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test46");
        yuparking.services.ParkingLotService parkingLotService0 = new yuparking.services.ParkingLotService();
        parkingLotService0.updateSpaceStatus((int) (byte) 1, "Faculty");
        parkingLotService0.disableLot(11);
        parkingLotService0.updateSpaceStatus(12, "Visitor");
    }

    @Test
    public void test47() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test47");
        yuparking.services.payment.StaffParkingFee staffParkingFee0 = new yuparking.services.payment.StaffParkingFee();
        double double2 = staffParkingFee0.calculateFee((double) ' ');
        org.junit.Assert.assertTrue("'" + double2 + "' != '" + 320.0d + "'", double2 == 320.0d);
    }

    @Test
    public void test48() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test48");
        yuparking.database.Database database0 = new yuparking.database.Database();
        java.util.List<java.lang.String[]> strArrayList2 = database0.retrieveData("");
        java.util.List<java.lang.String> strList3 = database0.getDataTables();
        org.junit.Assert.assertNotNull(strArrayList2);
        org.junit.Assert.assertNotNull(strList3);
    }

    @Test
    public void test49() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test49");
        yuparking.models.Booking booking6 = new yuparking.models.Booking(100, (int) (short) 0, 0, "", "", "Visitor");
        java.lang.String str7 = booking6.getStatus();
        java.lang.String str8 = booking6.getStatus();
        java.lang.String str9 = booking6.getStatus();
        int int10 = booking6.getUserID();
        java.lang.String str11 = booking6.getStatus();
        org.junit.Assert.assertEquals("'" + str7 + "' != '" + "Visitor" + "'", str7, "Visitor");
        org.junit.Assert.assertEquals("'" + str8 + "' != '" + "Visitor" + "'", str8, "Visitor");
        org.junit.Assert.assertEquals("'" + str9 + "' != '" + "Visitor" + "'", str9, "Visitor");
        org.junit.Assert.assertTrue("'" + int10 + "' != '" + 0 + "'", int10 == 0);
        org.junit.Assert.assertEquals("'" + str11 + "' != '" + "Visitor" + "'", str11, "Visitor");
    }

    @Test
    public void test50() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test50");
        yuparking.services.ParkingLotService parkingLotService0 = new yuparking.services.ParkingLotService();
        parkingLotService0.updateSpaceStatus((int) (byte) 1, "Faculty");
        parkingLotService0.disableLot(11);
        parkingLotService0.viewParkingLots();
    }

    @Test
    public void test51() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test51");
        yuparking.services.BookingService bookingService0 = new yuparking.services.BookingService();
        yuparking.models.student student4 = new yuparking.models.student(0, "hi!", "");
        double double6 = bookingService0.calculateFeeForBooking((yuparking.models.User) student4, (double) (byte) 10);
        yuparking.services.BookingService bookingService7 = new yuparking.services.BookingService();
        yuparking.models.super_manager super_manager11 = new yuparking.models.super_manager((int) (short) 10, "Faculty", "");
        double double13 = bookingService7.calculateFeeForBooking((yuparking.models.User) super_manager11, (double) 15);
        double double15 = bookingService0.calculateFeeForBooking((yuparking.models.User) super_manager11, (double) 0.0f);
        org.junit.Assert.assertTrue("'" + double6 + "' != '" + 50.0d + "'", double6 == 50.0d);
        org.junit.Assert.assertTrue("'" + double13 + "' != '" + 225.0d + "'", double13 == 225.0d);
        org.junit.Assert.assertTrue("'" + double15 + "' != '" + 0.0d + "'", double15 == 0.0d);
    }

    @Test
    public void test52() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test52");
        yuparking.services.SignupService signupService0 = new yuparking.services.SignupService();
        boolean boolean4 = signupService0.signup("Staff", "hi!", "Visitor");
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
    }
}
