package net.paxcel.ashutoshaneja;
import java.sql.SQLException;
import java.util.*;

/**This class executes the Operator Manager, provides Menu for interaction
 * @author Ashutosh
 *Methods in this class :- Main Method
 */
public class RunOperatorManager {
	
	private static final String DB_DRIVER_CLASS=LoadResources.dbProperties.getProperty("driver.class.name");
	private static final String DB_URL=LoadResources.dbProperties.getProperty("db.url");
	private static final String DB_USER=LoadResources.dbProperties.getProperty("db.user");
	private static final String DB_PASSWORD=LoadResources.dbProperties.getProperty("db.password");
	
	private static Scanner scanner =new Scanner(System.in);
	public static void main(final String[] args) {

		try {
			DatabaseConnection databaseconnection = new DatabaseConnection();
			LoadResources.areValid();	
			//Validate Resources and their configuration
			
			ConnectionPool.createInitialPool(DB_DRIVER_CLASS, DB_URL, DB_USER, DB_PASSWORD);
			//Create Initial Connection Pool
			
			
			//databaseconnection.insertData();
			// Populates the DB Tables with Random Data for performing operations

			/*                  ---Menu for User Interaction---                  */
			System.out.println("-- Welcome to Mobile Operator Data Manager --");

			System.out.println("\nOperations: -");
			System.out.println("1. Print messages sent from a number");
			System.out.println("2. Print messages received by a number");
			System.out.println("3. Print messages sent from one number to other number");
			System.out.println("4. Print messages received from any Punjab number");
			System.out.println("5. Print messages received from any specific Region-Operator");
			System.out.println("6. Print messages received from any number (Regex Pattern)");
			System.out.println("7. Print messages received from any specific region but FAILED");
			System.out.println("~ Enter any other key to exit.");
			System.out.println("Enter your option to perform the specific operation: ");
			/*                  ---Menu for User Interaction---                  */
			
			final int option=scanner.nextInt();
			
			switch(option) {
			case 1:{
				System.out.print("Searching for all Messages sent by \"9814129697\" :-");
				final long primaryNo = 9814129697l;
				databaseconnection.searchMsgSentBy(String.valueOf(primaryNo));

				break;
			}
			case 2:{
				System.out.print("Searching for all Messages received by \"9814430424\" :-");
				final long primaryNo = 9814430424l;
				databaseconnection.searchMsgReceivedBy(primaryNo, "anyRegion");

				break;
			}
			case 3:{
				System.out.print("Searching for Messages sent from \"9917213839\" to \"9917226381\" :-");
				final long primaryNo = 9917213839l;
				final long secondaryNo = 9917226381l;
				databaseconnection.searchMsgFromOneNumberToOther(primaryNo, secondaryNo);

				break;
			}
			case 4:{
				System.out.print("Searching for Messages received by \"9872317017\" from Punjab Number:-");
				final long primaryNo = 9872317017l;
				databaseconnection.searchMsgReceivedBy(primaryNo, "punjabRegion");
				break;
			}
			case 5:{
				System.out.print("Searching for Messages received by \"9872317017\" from Jio Punjab Number:-");
				final long primaryNo = 9872317017l;
				databaseconnection.searchMsgByOperatorAndRegion(primaryNo);
				break;
			}
			case 6:{
				System.out.println("Searching for Messages received by \"from 99175*****\" :-");
				final String primaryNo = "99175*****";
				databaseconnection.searchMsgSentBy(primaryNo);
				break;
			}
			case 7:{
				System.out.println("Searching for Messages received by Punjab Number BUT Failed :-");
				databaseconnection.searchFailedMsg();
				break;
			}
			default:{
				System.out.println("Bad Choice :(");
				System.exit(0);
				break;
			}
			}

		}
		catch(final InputMismatchException imexception) {
			System.out.println("Input Mismatch. Enter only integers!");
			System.exit(0);
		}
		catch(final SQLException sqlexception) {
			System.out.println("Connection could not be established. Try again :(");
			LoadResources.logger.error("Exception found! \nStackTrace: "+sqlexception.getStackTrace(), sqlexception);
		}
		catch(final Exception exception) {
			System.out.println("Error encountered. Try again :(");
			LoadResources.logger.info(exception.getMessage());
			LoadResources.logger.error("Exception found! \nStackTrace: "+exception.getStackTrace(), exception);
		}
		finally{
			LoadResources.logger.info("Resources released, System Shutdown!!");
		}

	}

}
