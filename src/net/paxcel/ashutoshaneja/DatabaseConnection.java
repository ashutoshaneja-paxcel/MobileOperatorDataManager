package net.paxcel.ashutoshaneja;
import java.sql.*;
import java.util.*;
import static net.paxcel.ashutoshaneja.LoadResources.*;

/**This class establishes all the database connections and executes queries to fetch data
 * @author Ashutosh
 *
 */
public class DatabaseConnection {

	private static final String DB_DRIVER_CLASS = dbProperties.getProperty("driver.class.name");
	private static final String DB_URL = dbProperties.getProperty("db.url");
	private static final String DB_USER = dbProperties.getProperty("db.user");
	private static final String DB_PASSWORD = dbProperties.getProperty("db.password");
	static List<String> messageContent;

	static boolean createConnectionPool() throws ClassNotFoundException, SQLException {
		boolean isConnectionPoolAvailable = ConnectionPool.createInitialPool(DB_DRIVER_CLASS, DB_URL, DB_USER, DB_PASSWORD);
		//Create Initial Connection Pool

		return isConnectionPoolAvailable;
	}




	// ------------------ This method populates the tables in Database with Random Data ------------------ 
	static void insertData() throws SQLException {

		try{

			Connection connection=ConnectionPool.getConnection();
			//Fetch Connection from Connection Pool

			logger.info("Connection allocated, Pool Size: "+ConnectionPool.getSize());

			int recordCount = 0;

			final String operatorInsertionSQL = "INSERT INTO OPERATOR(OPERATOR_NAME) VALUES(?)";
			final String regionInsertionSQL = "INSERT INTO REGION(REGION_NAME) VALUES(?)";
			final String rangeInsertionSQL = "INSERT INTO OPERATOR_RANGE VALUES(?,?,?)";
			final String numberInsertionSQL = "INSERT INTO NUMBER_DETAIL(NUMBER,OPERATOR_ID,REGION_ID) VALUES(?,?,?)";
			final String messageInsertionSQL = "INSERT INTO MESSAGE_DETAIL(FROM_NUMBER,TO_NUMBER,SENT_TIME,RECEIVED_TIME,STATUS,MESSAGE_CONTENT) VALUES (?,?,?,?,?,?)";


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

			logger.info(recordCount + " record inserted!");


			boolean releaseOutcome=ConnectionPool.releaseConnection(connection);

			logger.info("Status of Connection Release: "+releaseOutcome+", Pool size:"+ConnectionPool.getSize());

		} catch (final Exception exception) {
			System.out.println("\nException while inserting data: " + exception.getMessage());
			logger.error("Exception found in DB Connection.\nStack Trace: " + exception.getStackTrace(), exception);
		}
	}




	// ------------------ This method searches for messages sent by a number ------------------ 
	static List<String> searchMsgSentBy(final String primaryNo) throws Exception {

		try {

			Connection connection=ConnectionPool.getConnection();
			//Fetch Connection from Connection Pool

			logger.info("Connection allocated, Pool Size: "+ConnectionPool.getSize());
			
			

			if (primaryNo.contains("**")) {
				final String searchMsgSQL = "SELECT MESSAGE_CONTENT FROM MESSAGE_DETAIL WHERE FROM_NUMBER LIKE \"99175_____\"";
				final PreparedStatement searchpreparedstmt = connection.prepareStatement(searchMsgSQL);
				final ResultSet resultset = searchpreparedstmt.executeQuery();

				System.out.println();
				 messageContent = new ArrayList<String>();
				 
				while (resultset.next()) {
					messageContent.add(resultset.getString(1));
				}
			} else {
				final String searchMsgSQL = "SELECT MESSAGE_CONTENT FROM MESSAGE_DETAIL WHERE FROM_NUMBER=?";

				final PreparedStatement searchpreparedstmt = connection.prepareStatement(searchMsgSQL);

				searchpreparedstmt.setLong(1, Long.parseLong(primaryNo));
				final ResultSet resultset = searchpreparedstmt.executeQuery();
				
				messageContent = new ArrayList<String>();
				System.out.println();

				while (resultset.next()) {
					messageContent.add(resultset.getString(1));
				}
			}


			boolean releaseOutcome=ConnectionPool.releaseConnection(connection);

			logger.info("Status of Connection Release: "+releaseOutcome+", Pool size:"+ConnectionPool.getSize());
			
			return messageContent;
		} catch (final Exception e) {
			logger.error(e.getMessage());
			throw new SQLException();
		} 
	}




	// ------------------ This method searches for messages received by a number ------------------ 
	static List<String> searchMsgReceivedBy(final long primaryNo, final String region) throws SQLException {
		try{

			Connection connection=ConnectionPool.getConnection();
			//Fetch connection from Connection Pool

			logger.info("Connection allocated, Pool Size: "+ConnectionPool.getSize());

			String searchMsgSQL = "";

			if (region.equalsIgnoreCase("anyRegion")) {
				searchMsgSQL = "SELECT MESSAGE_CONTENT FROM MESSAGE_DETAIL WHERE TO_NUMBER=?";
			}

			else if (region.equalsIgnoreCase("punjabRegion")) {
				searchMsgSQL = "SELECT * FROM MESSAGE_DETAIL INNER JOIN NUMBER_DETAIL WHERE NUMBER_DETAIL.REGION_ID=? AND MESSAGE_DETAIL.FROM_NUMBER=NUMBER_DETAIL.NUMBER AND MESSAGE_DETAIL.TO_NUMBER=?;";
			}

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
			messageContent = new ArrayList<String>();

			while (resultset.next()) {
				messageContent.add(resultset.getString("MESSAGE_CONTENT"));
			}


			boolean releaseOutcome=ConnectionPool.releaseConnection(connection);

			logger.info("Status of Connection Release: "+releaseOutcome+", Pool size:"+ConnectionPool.getSize());
			
			return messageContent;
			
		} catch (final Exception e) {
			logger.error(e.getMessage());
			throw new SQLException();
		}
	}




	// ------------------ This method searches for messages sent from one number to other number ------------------ 
	static List<String> searchMsgFromOneNumberToOther(final long primaryNo, final long secondaryNo) throws SQLException {
		try {

			Connection connection=ConnectionPool.getConnection();
			//fetch connection from Connection Pool

			logger.info("Connection allocated, Pool Size: "+ConnectionPool.getSize());

			final String searchMsgSQL = "SELECT MESSAGE_CONTENT FROM MESSAGE_DETAIL WHERE FROM_NUMBER=? AND TO_NUMBER=?";

			final PreparedStatement searchpreparedstmt = connection.prepareStatement(searchMsgSQL);

			searchpreparedstmt.setLong(1, primaryNo);
			searchpreparedstmt.setLong(2, secondaryNo);
			final ResultSet resultset = searchpreparedstmt.executeQuery();

			System.out.println();
			messageContent = new ArrayList<String>();

			while (resultset.next()) {
				messageContent.add(resultset.getString(1));
			}


			boolean releaseOutcome=ConnectionPool.releaseConnection(connection);

			logger.info("Status of Connection Release: "+releaseOutcome+", Pool size:"+ConnectionPool.getSize());

			return messageContent;
			
		} catch (final Exception e) {
			logger.error(e.getMessage());
			throw new SQLException();
		}
	}




	// ------------------ This method searches for messages sent by a particular operator and region ------------------ 
	static List<String> searchMsgByOperatorAndRegion(final long primaryNo) throws SQLException {
		try {

			Connection connection=ConnectionPool.getConnection();
			//Fetch Connection from Connection Pool

			logger.info("Connection allocated, Pool Size: "+ConnectionPool.getSize());

			final String searchMsgSQL = "SELECT * FROM MESSAGE_DETAIL INNER JOIN NUMBER_DETAIL WHERE NUMBER_DETAIL.REGION_ID=? AND MESSAGE_DETAIL.FROM_NUMBER=NUMBER_DETAIL.NUMBER AND MESSAGE_DETAIL.TO_NUMBER=? AND NUMBER_DETAIL.OPERATOR_ID=?;";

			final PreparedStatement searchpreparedstmt = connection.prepareStatement(searchMsgSQL);

			searchpreparedstmt.setInt(1, 2); // 2 is Region id for Punjab
			searchpreparedstmt.setLong(2, primaryNo);
			searchpreparedstmt.setInt(3, 4); // 4 is Operator id for Jio
			final ResultSet resultset = searchpreparedstmt.executeQuery();

			System.out.println();
			messageContent = new ArrayList<String>();

			while (resultset.next()) {
				messageContent.add(resultset.getString("MESSAGE_CONTENT"));
			}

			boolean releaseOutcome=ConnectionPool.releaseConnection(connection);

			logger.info("Status of Connection Release: "+releaseOutcome+", Pool size:"+ConnectionPool.getSize());

			return messageContent;
			
		} catch (final Exception e) {
			logger.error(e.getMessage());
			throw new SQLException();
		}
	}




	// ------------------ This method searches for failed messages sent from a region ------------------ 
	static List<String> searchFailedMsg() throws SQLException {
		try {

			Connection connection=ConnectionPool.getConnection();
			//Fetch Connection from Connection Pool

			logger.info("Connection allocated, Pool Size: "+ConnectionPool.getSize());

			final String searchMsgSQL = "SELECT * FROM MESSAGE_DETAIL INNER JOIN NUMBER_DETAIL WHERE NUMBER_DETAIL.REGION_ID=? AND MESSAGE_DETAIL.FROM_NUMBER=NUMBER_DETAIL.NUMBER AND MESSAGE_DETAIL.STATUS='F';";

			final PreparedStatement searchpreparedstmt = connection.prepareStatement(searchMsgSQL);

			searchpreparedstmt.setInt(1, 2); // 2 is Region id for Punjab
			final ResultSet resultset = searchpreparedstmt.executeQuery();

			System.out.println();
			messageContent = new ArrayList<String>();

			while (resultset.next()) {
				messageContent.add(resultset.getString("MESSAGE_CONTENT"));
			}

			boolean releaseOutcome=ConnectionPool.releaseConnection(connection);

			logger.info("Status of Connection Release: "+releaseOutcome+", Pool size:"+ConnectionPool.getSize());

			return messageContent;
			
		} catch (final Exception e) {
			logger.error(e.getMessage());
			throw new SQLException();
		}
	}
}
