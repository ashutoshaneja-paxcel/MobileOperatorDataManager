package net.paxcel.ashutoshaneja;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ConnectionPool {

	private static List<Connection> usedConnections = new ArrayList<>();
	private static int INITIAL_POOL_SIZE = 10;
	private static List<Connection> pool;

	public static boolean createInitialPool(String driverClass, String url, String user, String password) throws SQLException, ClassNotFoundException {
		boolean flag=false;

		Class.forName(driverClass);
		pool = new ArrayList<>(INITIAL_POOL_SIZE);
		for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
			pool.add(createConnection(url, user, password));
			flag=true;
		}
		return flag;
	}

	public static synchronized Connection getConnection() {
		Connection connection = pool.remove(pool.size() - 1);
		usedConnections.add(connection);
		return connection;
	}

	public static boolean releaseConnection(Connection connection) {
		usedConnections.remove(connection);
		return pool.add(connection);
	}

	private static Connection createConnection(String url, String user, String password)
			throws SQLException, ClassNotFoundException {
		Connection connection = DriverManager.getConnection(url, user, password);
		return connection;
	}

	public static int getSize() {
		return pool.size();
	}
}