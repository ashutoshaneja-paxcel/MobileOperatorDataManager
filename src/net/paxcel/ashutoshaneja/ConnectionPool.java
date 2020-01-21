package net.paxcel.ashutoshaneja;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;

public class ConnectionPool implements ConnectionInterface {

	private String URL;
	private String user;
	private String password;
	private List<Connection> connectionPool;
	private List<Connection> usedConnections = new ArrayList<>();
	private static int INITIAL_POOL_SIZE = 10;


	public static ConnectionPool create(
			String url, String user, 
			String password) throws SQLException {

		List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
		for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
			pool.add(createConnection(url, user, password));
		}
		return new ConnectionPool(url, user, password, pool);
	}

	// standard constructors

	@Override
	public Connection getConnection() {
		Connection connection = connectionPool
				.remove(connectionPool.size() - 1);
		usedConnections.add(connection);
		return connection;
	}

	@Override
	public boolean releaseConnection(Connection connection) {
		connectionPool.add(connection);
		return usedConnections.remove(connection);
	}

	private static Connection createConnection(
			String url, String user, String password) 
					throws SQLException {
		return DatabaseConnection.getConnection(url, user, password);
	}

	public int getSize() {
		return connectionPool.size() + usedConnections.size();
	}

	// standard getters
}