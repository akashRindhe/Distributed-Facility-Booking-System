package shared.services;

import java.sql.*;

public class DatabaseConnection {
	public static Connection connectToDB() throws ClassNotFoundException, SQLException{
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
}
