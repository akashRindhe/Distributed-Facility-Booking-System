package shared.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import shared.services.DatabaseConnection;

public class Facility implements DataModel {
	
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static Facility fetchFacility(int id)
	{
		Facility facility = new Facility ();
		try {
			Connection connection = DatabaseConnection.connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM Facility WHERE id = " + id;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				facility.id = rs.getInt("id");
				facility.name = rs.getString("name");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
			
		return facility;
	}

}
