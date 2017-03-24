package shared.model;

import shared.services.DatabaseConnection;
import java.sql.*;

public class User implements DataModel{
	
	private String id;
	private String userName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public static User fetchUser(String id)
	{
		User user = new User();
		try {
			Connection connection = DatabaseConnection.connectToDB();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM User WHERE id = '" + id + "'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				user.id = rs.getString("id");
				user.userName = rs.getString("userName");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Error");
		}
			
		return user;
	}
}
