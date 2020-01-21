package net.paxcel.ashutoshaneja;
import java.sql.*;
import java.util.*;


/**This class establishes all the database connections and executes queries to fetch data
 * @author Ashutosh
 *
 */
public class DatabaseConnection {
	Connection connection;

	// This method populates the tables in Database with Random Data
	void insertData() throws SQLException {

		try {

			int recordCount = 0;

			final String operatorInsertionSQL = "INSERT INTO OPERATOR(OPERATOR_NAME) VALUES(?)";
			final String regionInsertionSQL = "INSERT INTO REGION(REGION_NAME) VALUES(?)";
			final String rangeInsertionSQL = "INSERT INTO OPERATOR_RANGE VALUES(?,?,?)";
			final String numberInsertionSQL = "INSERT INTO NUMBER_DETAIL(NUMBER,OPERATOR_ID,REGION_ID) VALUES(?,?,?)";
			final String messageInsertionSQL = "INSERT INTO MESSAGE_DETAIL(FROM_NUMBER,TO_NUMBER,SENT_TIME,RECEIVED_TIME,STATUS,MESSAGE_CONTENT) VALUES (?,?,?,?,?,?)";

			Class.forName("com.mysql.jdbc.Driver");

			connection = DriverManager.getConnection(LoadResources.configProperties.getProperty("connectionURL"),
					LoadResources.configProperties.getProperty("username"),
					LoadResources.configProperties.getProperty("password"));

			final PreparedStatement operatorInsertionPS = connection.prepareStatement(operatorInsertionSQL);
			final PreparedStatement regionInsertionPS = connection.prepareStatement(regionInsertionSQL);
			final PreparedStatement rangeInsertionPS = connection.prepareStatement(rangeInsertionSQL);
			final PreparedStatement numberInsertionPS = connection.prepareStatement(numberInsertionSQL);
			final PreparedStatement messageInsertionPS = connection.prepareStatement(messageInsertionSQL);

			// Add Operator List
			final List<String> operatorList = GenerateData.getOperatorList();
			final int operatorListSize = operatorList.size();

			for (int i = 0; i < operatorListSize; i++) {
				final String operator = operatorList.get(i);
				operatorInsertionPS.setString(1, operator);

				operatorInsertionPS.executeUpdate();
			}

			// Add Region List
			final List<String> regionList = GenerateData.getRegionList();
			final int regionListSize = regionList.size();

			for (int i = 0; i < regionListSize; i++) {
				regionInsertionPS.setString(1, regionList.get(i));

				regionInsertionPS.executeUpdate();
			}

			// Insert Range
			final List<String> rangeList = GenerateData.getRangeList();
			final int rangeListSize = rangeList.size();

			for (int i = 0; i < rangeListSize; i++) {
				rangeInsertionPS.setString(1, rangeList.get(i));
				rangeInsertionPS.setInt(2, GenerateData.findOperator(rangeList.get(i)));
				rangeInsertionPS.setInt(3, GenerateData.findRegion(rangeList.get(i)));

				rangeInsertionPS.executeUpdate();
			}

			// Insert all the Numbers
			final List<Long> totalNumberList = GenerateData.getNumberList();
			final int numbersListSize = totalNumberList.size();

			final List<Long> messageFrom = new ArrayList<>(totalNumberList.subList(0, (numbersListSize / 2)));
			final List<Long> messageTo = new ArrayList<>(
					totalNumberList.subList((numbersListSize / 2), numbersListSize));

			final Set<Long> withoutDuplicateNumber = new LinkedHashSet<>();
			withoutDuplicateNumber.addAll(totalNumberList);
			totalNumberList.clear();
			totalNumberList.addAll(withoutDuplicateNumber);

			for (int i = 0; i < totalNumberList.size(); i++) {

				final long number = totalNumberList.get(i);

				numberInsertionPS.setLong(1, number);
				numberInsertionPS.setInt(2, GenerateData.findOperator(String.valueOf(number)));
				numberInsertionPS.setInt(3, GenerateData.findRegion(String.valueOf(number)));

				recordCount += numberInsertionPS.executeUpdate();
			}

			System.out.println(recordCount + " records added!");

			// Insert into MESSAGE_DETAILS;

			for (int j = 0; j < (messageFrom.size()); j++) { // add even number of senders and receivers

				final long fromNumber = messageFrom.get(j);
				final long toNumber = messageTo.get(j);

				messageInsertionPS.setLong(1, fromNumber);
				messageInsertionPS.setLong(2, toNumber);
				messageInsertionPS.setString(3, GenerateData.getTime());
				messageInsertionPS.setString(4, GenerateData.getTime());
				messageInsertionPS.setString(5, GenerateData.getStatus());
				messageInsertionPS.setString(6, GenerateData.getMessage());

				recordCount += messageInsertionPS.executeUpdate();
			}

			LoadResources.logger.info(recordCount + " record inserted!");

			// connection.close();
		} catch (final Exception exception) {
			System.out.println("\nException while inserting data: " + exception.getMessage());
			LoadResources.logger.error("Exception found in DB Connection.\nStack Trace: " + exception.getStackTrace(),
					exception);
		} finally {
			connection.close();
		}
	}

	void searchMsgFromOneNumberToOther(final long primaryNo, final long secondaryNo) throws SQLException {
		try {
			final String searchMsgSQL = "SELECT MESSAGE_CONTENT FROM MESSAGE_DETAIL WHERE FROM_NUMBER=? AND TO_NUMBER=?";

			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(LoadResources.configProperties.getProperty("connectionURL"),
					LoadResources.configProperties.getProperty("username"),
					LoadResources.configProperties.getProperty("password"));

			final PreparedStatement searchpreparedstmt = connection.prepareStatement(searchMsgSQL);

			searchpreparedstmt.setLong(1, primaryNo);
			searchpreparedstmt.setLong(2, secondaryNo);
			final ResultSet resultset = searchpreparedstmt.executeQuery();

			System.out.println();

			while (resultset.next()) {
				System.out.println(resultset.getString(1) + "\n");
			}

		} catch (final Exception e) {
			throw new SQLException();
		} finally {
			connection.close();
		}
	}

	void searchMsgReceivedBy(final long primaryNo, final String region) throws SQLException {
		try {

			String searchMsgSQL = "";

			if (region.equalsIgnoreCase("anyRegion")) {
				searchMsgSQL = "SELECT MESSAGE_CONTENT FROM MESSAGE_DETAIL WHERE TO_NUMBER=?";
			}

			else if (region.equalsIgnoreCase("punjabRegion")) {
				searchMsgSQL = "SELECT * FROM MESSAGE_DETAIL INNER JOIN NUMBER_DETAIL WHERE NUMBER_DETAIL.REGION_ID=? AND MESSAGE_DETAIL.FROM_NUMBER=NUMBER_DETAIL.NUMBER AND MESSAGE_DETAIL.TO_NUMBER=?;";
			}

			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(LoadResources.configProperties.getProperty("connectionURL"),
					LoadResources.configProperties.getProperty("username"),
					LoadResources.configProperties.getProperty("password"));

			final PreparedStatement searchpreparedstmt = connection.prepareStatement(searchMsgSQL);

			if (region.equalsIgnoreCase("anyRegion")) {
				searchpreparedstmt.setLong(1, primaryNo);
			}

			else if (region.equalsIgnoreCase("punjabRegion")) {
				searchpreparedstmt.setInt(1, 2); //2 is Region id for Punjab
				searchpreparedstmt.setLong(2, primaryNo);
			}
			final ResultSet resultset = searchpreparedstmt.executeQuery();

			System.out.println();

			while (resultset.next()) {
				System.out.println(resultset.getString("MESSAGE_CONTENT") + "\n");
			}

		} catch (final Exception e) {
			throw new SQLException();
		} finally {
			connection.close();
		}
	}

	void searchMsgSentBy(final String primaryNo) throws Exception {
		try {

			Class.forName("com.mysql.jdbc.Driver");

			connection = DriverManager.getConnection(LoadResources.configProperties.getProperty("connectionURL"),
					LoadResources.configProperties.getProperty("username"),
					LoadResources.configProperties.getProperty("password"));

			if (primaryNo.contains("**")) {
				final String searchMsgSQL = "SELECT MESSAGE_CONTENT FROM MESSAGE_DETAIL WHERE FROM_NUMBER LIKE \"99175_____\"";
				final PreparedStatement searchpreparedstmt = connection.prepareStatement(searchMsgSQL);
				final ResultSet resultset = searchpreparedstmt.executeQuery();

				System.out.println();

				while (resultset.next()) {
					System.out.println(resultset.getString(1) + "\n");
				}
			} else {
				final String searchMsgSQL = "SELECT MESSAGE_CONTENT FROM MESSAGE_DETAIL WHERE FROM_NUMBER=?";

				final PreparedStatement searchpreparedstmt = connection.prepareStatement(searchMsgSQL);

				searchpreparedstmt.setLong(1, Long.parseLong(primaryNo));
				final ResultSet resultset = searchpreparedstmt.executeQuery();

				System.out.println();

				while (resultset.next()) {
					System.out.println(resultset.getString(1) + "\n");
				}
			}

		} catch (final Exception e) {
			LoadResources.logger.error(e.getMessage());
			throw new SQLException();
		} finally {
			connection.close();
		}
	}

	void searchMsgByOperatorAndRegion(final long primaryNo) throws SQLException {
		try {

			final String searchMsgSQL = "SELECT * FROM MESSAGE_DETAIL INNER JOIN NUMBER_DETAIL WHERE NUMBER_DETAIL.REGION_ID=? AND MESSAGE_DETAIL.FROM_NUMBER=NUMBER_DETAIL.NUMBER AND MESSAGE_DETAIL.TO_NUMBER=? AND NUMBER_DETAIL.OPERATOR_ID=?;";

			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(LoadResources.configProperties.getProperty("connectionURL"),
					LoadResources.configProperties.getProperty("username"),
					LoadResources.configProperties.getProperty("password"));

			final PreparedStatement searchpreparedstmt = connection.prepareStatement(searchMsgSQL);

			searchpreparedstmt.setInt(1, 2); // 2 is Region id for Punjab
			searchpreparedstmt.setLong(2, primaryNo);
			searchpreparedstmt.setInt(3, 4); // 4 is Operator id for Jio
			final ResultSet resultset = searchpreparedstmt.executeQuery();

			System.out.println();

			while (resultset.next()) {
				System.out.println(resultset.getString("MESSAGE_CONTENT") + "\n");
			}

		} catch (final Exception e) {
			throw new SQLException();
		} finally {
			connection.close();
		}
	}

	void searchFailedMsg() throws SQLException {
		try {

			final String searchMsgSQL = "SELECT * FROM MESSAGE_DETAIL INNER JOIN NUMBER_DETAIL WHERE NUMBER_DETAIL.REGION_ID=? AND MESSAGE_DETAIL.FROM_NUMBER=NUMBER_DETAIL.NUMBER AND MESSAGE_DETAIL.STATUS='F';";

			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(LoadResources.configProperties.getProperty("connectionURL"),
					LoadResources.configProperties.getProperty("username"),
					LoadResources.configProperties.getProperty("password"));

			final PreparedStatement searchpreparedstmt = connection.prepareStatement(searchMsgSQL);

			searchpreparedstmt.setInt(1, 2); // 2 is Region id for Punjab
			final ResultSet resultset = searchpreparedstmt.executeQuery();

			System.out.println();

			while (resultset.next()) {
				System.out.println(resultset.getString("MESSAGE_CONTENT") + "\n");
			}

		} catch (final Exception e) {
			throw new SQLException();
		}
		finally {
			connection.close();
		}
	}
}
