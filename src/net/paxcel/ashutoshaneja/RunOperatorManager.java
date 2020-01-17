package net.paxcel.ashutoshaneja;
import java.sql.SQLException;
import java.util.*;

public class RunOperatorManager {

	private static Scanner scanner =new Scanner(System.in);
	public static void main(String[] args) {

		try {
			DatabaseConnection databaseconnection = new DatabaseConnection();
			LoadResources.areValid();	

			System.out.println("-- Welcome to Mobile Operator Data Manager --");
			/*System.out.print("Enter your 10 digit mobile number: ");

			long primaryNo=scanner.nextLong();*/

			System.out.println("\nOperations: -");
			System.out.println("1. Print messages from your number to any specific number");
			System.out.println("2. Print messages received from any specific number");
			System.out.println("3. Print messages sent to any specific number");
			System.out.println("4. Print messages received from any Punjab number");
			System.out.println("5. Print messages received from any specific Region-Operator");
			System.out.println("6. Print messages received from any number (Regex Pattern)");
			System.out.println("7. Print messages received from any specific region but FAILED");
			System.out.println("~ Enter any other key to exit.");
			System.out.println("Enter your option to perform the specific operation: ");

			int option=scanner.nextInt();
			switch(option) {
			case 1:{
				System.out.print("Enter sender's number (10-Digit): ");
				//				long secondaryNo = scanner.nextLong();
				databaseconnection.insertData(9898989895l);
				break;
			}
			case 2:{
				break;
			}
			case 3:{
				break;
			}
			case 4:{
				break;
			}
			case 5:{
				break;
			}
			case 6:{
				break;
			}
			case 7:{
				break;
			}
			default:{
				System.out.println("Bad Choice :(");
				break;
			}
			}

		}
		catch(SQLException sqle) {
			System.out.println("SQL Exception in DB Connection..");
		}
		catch(Exception exception) {
			System.out.println("Error encountered. Try again :(");
			LoadResources.logger.error("Exception found! \nStackTrace: "+exception.getStackTrace(), exception);
		}

	}

}
