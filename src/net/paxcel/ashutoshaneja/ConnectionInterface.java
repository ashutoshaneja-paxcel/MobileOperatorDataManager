package net.paxcel.ashutoshaneja;

import java.sql.Connection;

public interface ConnectionInterface {
	Connection getConnection();
	boolean releaseConnection(Connection connection);
}
