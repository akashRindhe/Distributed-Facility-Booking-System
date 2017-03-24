package shared.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shared.services.DatabaseConnection;

public class Booking implements DataModel {
	
	private int id;
	private int facilityId;
	private String userId;
	private Date bookingStart;
	private Date bookingEnd;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getBookingStart() {
		return bookingStart;
	}

	public void setBookingStart(Date bookingStart) {
		this.bookingStart = bookingStart;
	}

	public Date getBookingEnd() {
		return bookingEnd;
	}

	public void setBookingEnd(Date bookingEnd) {
		this.bookingEnd = bookingEnd;
	}
	public static Booking fetchBookingInfo(int id)
	{
		Booking booking = new Booking();
		try {
			Connection connection = DatabaseConnection.connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Booking WHERE id = " + id;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				booking.id = rs.getInt("id");
				booking.facilityId = rs.getInt("facilityId");
				booking.userId = rs.getString("userId");
				booking.bookingStart = rs.getTimestamp("bookingStart");
				booking.bookingEnd = rs.getTimestamp("bookingEnd");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
		return booking;
	}
	
	public static List<Booking> fetchBookingsByUser(String id)
	{
		List<Booking> bookings = new ArrayList<Booking>();
		try {
			Connection connection = DatabaseConnection.connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Booking WHERE userId = '" + id + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Booking booking = new Booking();
				booking.id = rs.getInt("id");
				booking.facilityId = rs.getInt("facilityId");
				booking.userId = rs.getString("userId");
				booking.bookingStart = rs.getTimestamp("bookingStart");
				booking.bookingEnd = rs.getTimestamp("bookingEnd");
				bookings.add(booking);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
		return bookings;
	}
	
	public static List<Booking> fetchBookingsByFacility(int id)
	{
		List<Booking> bookings = new ArrayList<Booking>();
		try {
			Connection connection = DatabaseConnection.connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Booking WHERE facilityId = " + id;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				Booking booking = new Booking();
				booking.id = rs.getInt("id");
				booking.facilityId = rs.getInt("facilityId");
				booking.userId = rs.getString("userId");
				booking.bookingStart = rs.getTimestamp("bookingStart");
				booking.bookingEnd = rs.getTimestamp("bookingEnd");
				bookings.add(booking);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
		return bookings;
	}
}
