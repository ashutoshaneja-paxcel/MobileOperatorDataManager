package net.paxcel.ashutoshaneja;
import java.sql.*;

public class DatabaseConnection {
	void insertData(long number) throws SQLException {

		try {

			String url = "jdbc:mysql://localhost:3306/OPERATOR_DB";
			String user = "root";
			String password = "root";

			String sql = "insert into NUMBER_DETAILS VALUES(?,?,?)";

			Class.forName("com.mysql.jdbc.Driver");

			Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedstatement = connection.prepareStatement(sql);

			preparedstatement.setLong(1, number);
			preparedstatement.setString(2,"Idea");
			preparedstatement.setString(3,"Punjab");


			System.out.println(preparedstatement.executeUpdate()+" record inserted!");
			LoadResources.logger.info(preparedstatement.executeUpdate()+" record inserted!");

			connection.close();
		}
		catch(Exception exception) {
			System.out.println("\nException while inserting data: "+exception.getMessage());
			LoadResources.logger.error("Exception found in DB Connection.\nStack Trace: "+exception.getStackTrace(), exception);
		}
	}
}
