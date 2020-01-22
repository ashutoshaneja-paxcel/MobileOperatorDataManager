package net.paxcel.ashutoshaneja;
import java.sql.SQLException;
import static net.paxcel.ashutoshaneja.LoadResources.*;
import static net.paxcel.ashutoshaneja.DatabaseConnection.*;
import java.util.*;

/**This class executes the Operator Manager, provides Menu for interaction.
 * @author Ashutosh
 *Methods in this class :- Main Method
 */
public class RunOperatorManager {

	private static Scanner scanner =new Scanner(System.in);
	static List<String> messageList = new ArrayList<String>();
	public static void main(final String[] args) {

		try {

			LoadResources.areValid();	
			//Validate Resources and their configuration

			if(createConnectionPool()) {
				logger.info("Connection Pool Created..");
			}
			else {
				throw new RuntimeException();
			}


			//insertData();
			// Populates the DB Tables with Random Data for performing operations


			/*                  ---Menu for User Interaction---                  */
			System.out.println("-- Welcome to Mobile Operator Data Manager --");

			System.out.println("\nOperations: -");
			System.out.println("1. Print all Messages sent by a number");
			System.out.println("2. Print all Messages received by a number");
			System.out.println("3. Print messages sent from one number to other number");
			System.out.println("4. Print messages received from any Punjab number");
			System.out.println("5. Print messages received from any Punjab Jio number");
			System.out.println("6. Print messages received from 99175*****");
			System.out.println("7. Print messages received from Punjab number but FAILED");
			System.out.println("~ Enter any other key to exit.");
			System.out.println("\nEnter your option to perform the specific operation: ");
			/*                  ---Menu for User Interaction---                  */

			final int option=scanner.nextInt();

			switch(option) {
			case 1:{
				System.out.print("Searching for all Messages sent by \"9814129697\" :-");
				final long primaryNo = 9814129697l;

				messageList = searchMsgSentBy(String.valueOf(primaryNo));
				
				System.out.println(messageList);
				break;
			}
			case 2:{
				System.out.print("Searching for all Messages received by \"9814430424\" :-");
				final long primaryNo = 9814430424l;

				messageList = searchMsgReceivedBy(primaryNo, "anyRegion");

				System.out.println(messageList);
				break;
			}
			case 3:{
				System.out.print("Searching for Messages sent from \"9917213839\" to \"9917226381\" :-");
				final long primaryNo = 9917213839l;
				final long secondaryNo = 9917226381l;

				messageList = searchMsgFromOneNumberToOther(primaryNo, secondaryNo);
				
				System.out.println(messageList);
				break;
			}
			case 4:{
				System.out.print("Searching for Messages received by \"9872317017\" from Punjab Number:-");
				final long primaryNo = 9872317017l;

				messageList = searchMsgReceivedBy(primaryNo, "punjabRegion");
				
				System.out.println(messageList);
				break;
			}
			case 5:{
				System.out.print("Searching for Messages received by \"9872317017\" from Jio Punjab Number:-");
				final long primaryNo = 9872317017l;

				messageList = searchMsgByOperatorAndRegion(primaryNo);
				
				System.out.println(messageList);
				break;
			}
			case 6:{
				System.out.println("Searching for Messages received by \"from 99175*****\" :-");
				final String primaryNo = "99175*****";

				messageList = searchMsgSentBy(primaryNo);
				
				System.out.println(messageList);
				break;
			}
			case 7:{
				System.out.println("Searching for Messages received by Punjab Number BUT Failed :-");

				messageList = searchFailedMsg();
				
				System.out.println(messageList);
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
			LoadResources.logger.error("DB Connection Issue! \nStackTrace: "+sqlexception.getStackTrace(), sqlexception);
		}

		catch(final RuntimeException rtexception) {
			System.out.println("Connection could not be established. Aborting System!! Try again :(");
			logger.error("Connection Pool not established! \nStackTrace: "+rtexception.getStackTrace(), rtexception);
			System.exit(0);
		}

		catch(final Exception exception) {
			System.out.println("Error encountered. Try again :(");
			logger.error("Exception found! \nStackTrace: "+exception.getStackTrace(), exception);
		}

		finally{
			logger.info("Resources released, System Shutdown!!");
		}

	}

}
