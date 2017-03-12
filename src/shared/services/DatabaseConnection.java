package shared.services;

import java.sql.*;

public class DatabaseConnection {
	public void connectToDB(){
		try {
			String driverName = "com.mysql.jdbc.Driver";
			String address = "35.185.136.252";
			String user = "root";
			String userPwd = "team_Scandi";
			String db = "dist_systems";
			String instanceUrl = "jdbc:mysql://" + address + '/' + db;
			
			Class.forName(driverName);
			Connection connection = DriverManager.getConnection(instanceUrl, user, userPwd);
			Statement stmt = connection.createStatement();
			ResultSet rstSet = stmt.executeQuery("");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection error");
		}
	}
}
