package server;

import java.sql.*;
import java.sql.Date;
import shared.model.*;
import java.util.*;

/**
 * This class encapsulates the interactions of the server with the MySQL database.
 * It contains various helper functions to get/update/delete data from the
 * database.
 */
public class DatabaseAccess {
	
	private static Connection connectToDB() throws ClassNotFoundException, SQLException{
		String driverName = "com.mysql.jdbc.Driver";
		String address = "35.185.136.252";
		String user = "root";
		String userPwd = "team_Scandi";
		String db = "dist_systems";
		String instanceUrl = "jdbc:mysql://" + address + '/' + db + "?useSSL=false";
		
		Class.forName(driverName);
		Connection connection = DriverManager.getConnection(instanceUrl, user, userPwd);
	
		return connection;
	}
	
	public static List<Facility> fetchAllFacilities()
	{
		List<Facility> facilities = new ArrayList<Facility>();
		try {
			Connection connection = connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Facility";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Facility facility = new Facility();
				facility.setId(rs.getInt("id"));
				facility.setName(rs.getString("name"));
				facilities.add(facility);
			}
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
		return facilities;
	}
	
	public static Facility fetchFacilityById(int id)
	{
		Facility facility = new Facility ();
		try {
			Connection connection = connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Facility WHERE id = " + id;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				facility.setId(rs.getInt("id"));
				facility.setName(rs.getString("name"));
			}
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}	
		return facility;
	}
	
	public static User fetchUserById(String id)
	{
		User user = null;
		try {
			Connection connection = connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM User WHERE id = '" + id + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				user = new User();
				user.setId(rs.getString("id"));
				user.setUserName(rs.getString("userName"));
			}
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}		
		return user;
	}
	
	public static Booking fetchBookingById(int id)
	{
		Booking booking = null;
		try {
			Connection connection = connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Booking WHERE id = " + id;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				booking = new Booking();
				booking.setId(rs.getInt("id"));
				booking.setFacilityId(rs.getInt("facilityId"));
				booking.setUserId(rs.getString("userId"));
				booking.setBookingStart(rs.getTimestamp("bookingStart"));
				booking.setBookingEnd(rs.getTimestamp("bookingEnd"));
			}
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
		return booking;
	}
	
	public static List<Booking> fetchBookingsByUserId(String id)
	{
		List<Booking> bookings = new ArrayList<Booking>();
		try {
			Connection connection = connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Booking WHERE userId = '" + id + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Booking booking = new Booking();
				booking.setId(rs.getInt("id"));
				booking.setFacilityId(rs.getInt("facilityId"));
				booking.setUserId(rs.getString("userId"));
				booking.setBookingStart(rs.getTimestamp("bookingStart"));
				booking.setBookingEnd(rs.getTimestamp("bookingEnd"));
				bookings.add(booking);
			}
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
		return bookings;
	}
	
	public static List<Booking> fetchBookingsByFacilityId(int id)
	{
		List<Booking> bookings = new ArrayList<Booking>();
		try {
			Connection connection = connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Booking WHERE facilityId = " + id;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Booking booking = new Booking();
				booking.setId(rs.getInt("id"));
				booking.setFacilityId(rs.getInt("facilityId"));
				booking.setUserId(rs.getString("userId"));
				booking.setBookingStart(rs.getTimestamp("bookingStart"));
				booking.setBookingEnd(rs.getTimestamp("bookingEnd"));
				bookings.add(booking);
			}
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
		return bookings;
	}
	
	public static List<Booking> fetchBookings(int id, Date date)
	{
		List<Booking> bookings = new ArrayList<Booking>();
		try {
			Connection connection = connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Booking WHERE facilityId = " + id + " AND DATE(bookingStart) = '" + date + "' ORDER BY bookingStart";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Booking booking = new Booking();
				booking.setId(rs.getInt("id"));
				booking.setFacilityId(rs.getInt("facilityId"));
				booking.setUserId(rs.getString("userId"));
				booking.setBookingStart(rs.getTimestamp("bookingStart"));
				booking.setBookingEnd(rs.getTimestamp("bookingEnd"));
				bookings.add(booking);
			}
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
		return bookings;
	}
	
	public static List<Booking> fetchBookings(int facilityId, Timestamp start, Timestamp end)
	{
		List<Booking> bookings = new ArrayList<Booking>();
		try {
			Connection connection = connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Booking WHERE "
					+ "facilityId = " + facilityId + " AND ("
					+ "bookingStart < '" + start + "' AND  bookingEnd > '" + start
					+ "') OR ("
					+ "bookingStart < '" + end + "' AND  bookingEnd > '" + end
					+ "') OR ("
					+ "bookingStart >'" + start + "' AND bookingEnd < '" + end
					+ "') OR (bookingStart = '" + start + "') OR (bookingEnd = '"
					+ end + "')";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Booking booking = new Booking();
				booking.setId(rs.getInt("id"));
				booking.setFacilityId(rs.getInt("facilityId"));
				booking.setUserId(rs.getString("userId"));
				booking.setBookingStart(rs.getTimestamp("bookingStart"));
				booking.setBookingEnd(rs.getTimestamp("bookingEnd"));
				bookings.add(booking);
			}
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
		return bookings;
	}
	
	public static Booking fetchLatestBooking() {
		List<Booking> bookings = new ArrayList<Booking>();
		try {
			Connection connection = connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Booking ORDER BY id DESC LIMIT 1";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Booking booking = new Booking();
				booking.setId(rs.getInt("id"));
				booking.setFacilityId(rs.getInt("facilityId"));
				booking.setUserId(rs.getString("userId"));
				booking.setBookingStart(rs.getTimestamp("bookingStart"));
				booking.setBookingEnd(rs.getTimestamp("bookingEnd"));
				bookings.add(booking);
			}
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
			e.printStackTrace();
		}
		return bookings.get(0);
	}
	
	public static int addBooking (Booking booking)
	{
		try {
			Connection connection = connectToDB();
			String sql = "INSERT INTO Booking (facilityId, userId, bookingStart, bookingEnd)"
					+ " values (?, ?, ?, ?)";
			PreparedStatement preparedStmt = connection.prepareStatement(sql); 
			preparedStmt.setInt(1, booking.getFacilityId());
			preparedStmt.setString(2, booking.getUserId());
			preparedStmt.setTimestamp(3, booking.getBookingStart());
			preparedStmt.setTimestamp(4, booking.getBookingEnd());
			
			preparedStmt.execute();
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
			e.printStackTrace();
		}
		return fetchLatestBooking().getId();
	}
	
	public static void changeBooking (Booking booking)
	{
		try {
			Connection connection = connectToDB();
			String sql = "UPDATE Booking SET bookingStart = ?, bookingEnd = ? WHERE id = ?";
			PreparedStatement preparedStmt = connection.prepareStatement(sql); 
			preparedStmt.setTimestamp(1, booking.getBookingStart());
			preparedStmt.setTimestamp(2, booking.getBookingEnd());
			preparedStmt.setInt(3,booking.getId());
			
			preparedStmt.execute();
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
	}
	
	public static void changeBookingUserId(int bookingId, String newUserId) {
		try {
			Connection connection = connectToDB();
			String sql = "UPDATE Booking SET userId = ? WHERE id = ?";
			PreparedStatement preparedStmt = connection.prepareStatement(sql); 
			preparedStmt.setString(1, newUserId);
			preparedStmt.setInt(2, bookingId);
			
			preparedStmt.execute();
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
	}
	
	public static void deleteBooking (Booking booking)
	{
		try {
			Connection connection = connectToDB();
			String sql = "DELETE FROM Booking WHERE id = ?";
			PreparedStatement preparedStmt = connection.prepareStatement(sql); 
			preparedStmt.setInt(1, booking.getId());
			
			preparedStmt.execute();
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
	}
}
