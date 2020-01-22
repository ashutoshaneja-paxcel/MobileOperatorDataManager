package net.paxcel.ashutoshaneja;
import java.io.*;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**This class loads Resources required for execution and ensures that they are authentic, else close the program from getting executed.
 * @author Ashutosh
 *
 */
class LoadResources {


	protected static Logger logger;
	static Properties dbProperties;
	static Properties log4jProperties;

	static {


		try (InputStream dbPropertyPath = new FileInputStream("Properties/db.properties");
				InputStream log4jPropertyPath = new FileInputStream("Properties/log4j.properties");) {
			logger=Logger.getLogger("GLOBAL");

			dbProperties=new Properties();
			dbProperties.load(dbPropertyPath);

			log4jProperties=new Properties();
			log4jProperties.load(log4jPropertyPath);

			logger.setLevel(Level.INFO);

			PropertyConfigurator.configure(log4jProperties);    


			logger.info("\n\nResources successfully Loaded....");
		} 
		catch (final Exception exception){
			System.out.println("Resources couldn't be loaded. Try again :(");
			logger.info("Unsuccessful Resources Load. Find Stack Trace on below line");
			logger.error(exception.getStackTrace(),exception);
			System.exit(0);
		}

	}

	static void areValid() {}
	//static method to execute static block

}
