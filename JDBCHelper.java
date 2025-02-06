package projectDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class JDBCHelper {
	
	
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		System.out.println(c.get(1));
				
	}
	

	// Establishing connection
	public static Connection getConnection() throws SQLException {
		
		String URL = "jdbc:mysql://localhost:3306/Task"; // Ensure 'db' exists
		String UID = "root";
		String PSWD = "root";
		
		return DriverManager.getConnection(URL, UID, PSWD);

	}

	// Closing ResultSet resources
	public static void close(ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	// Closing any Statement resources
	public static void close(Statement st) {
		if (st != null)
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	// Closing the Connection established
	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
