package net.paxcel.ashutoshaneja;

import com.mysql.jdbc.Connection;

public interface ConnectionInterface {
	Connection getConnection();
	boolean releaseConnection(Connection connection);
	String getURL();
	String getUser();
	String getPassword();
}
