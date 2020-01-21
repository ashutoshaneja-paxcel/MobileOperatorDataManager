package net.paxcel.ashutoshaneja;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class DBUtil {
	private static final String DB_DRIVER_CLASS=LoadResources.dbProperties.getProperty("driver.class.name");
	private static final String DB_URL=LoadResources.dbProperties.getProperty("db.url");
	private static final String DB_USER=LoadResources.dbProperties.getProperty("db.user");
	private static final String DB_PASSWORD=LoadResources.dbProperties.getProperty("db.password");
	private static BasicDataSource datasource;

	static {
		datasource = new BasicDataSource();
		datasource.setDriverClassName(DB_DRIVER_CLASS);
		datasource.setUrl(DB_URL);
		datasource.setUsername(DB_USER);
		datasource.setPassword(DB_PASSWORD);

		datasource.setMinIdle(100);
		datasource.setMaxIdle(1000);   
	}

	public static DataSource getDataSource() {
		return datasource;
	}
}
