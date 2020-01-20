package net.paxcel.ashutoshaneja;

import java.nio.charset.Charset;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**This class generates Random data for populating tables
 * @author Ashutosh
 *
 */
public class GenerateData {

	final static Random random = new Random();

	static List<String> getOperatorList(){
		List<String> operatorList = new ArrayList<String>();

		operatorList.add("Airtel");
		operatorList.add("BSNL");
		operatorList.add("Idea");
		operatorList.add("Jio");

		return operatorList;
	}

	static List<String> getRegionList(){
		List<String> regionList = new ArrayList<String>();

		regionList.add("J&K");
		regionList.add("Punjab");
		regionList.add("UP");
		regionList.add("Delhi");

		return regionList;
	}
	static String getMessage() 
	{ 

		// length is bounded by 256 Character 
		byte[] array = new byte[256]; 
		new Random().nextBytes(array); 

		String randomString 
		= new String(array, Charset.forName("UTF-8")); 

		// Create a StringBuffer to store the result 
		StringBuffer r = new StringBuffer(); 

		// remove all special char 
		String  AlphaNumericString 
		= randomString 
		.replaceAll("[^A-Za-z0-9]", ""); 

		// Append first 20 alphanumeric characters 
		// from the generated random String into the result 
		int n=10;
		for (int k = 0; k < AlphaNumericString.length(); k++) { 

			if (Character.isLetter(AlphaNumericString.charAt(k)) 
					&& (n > 0) 
					|| Character.isDigit(AlphaNumericString.charAt(k)) 
					&& (n > 0)) { 

				r.append(AlphaNumericString.charAt(k)); 
				n--; 
			} 
		} 

		return r.toString();
	}

	static List<Long> getNumberList(){

		Random random = new Random();

		List<Long> totalNumberList = new ArrayList<Long>();

		for(int i=0; i<10; i++) {

			String airtel = new String("9872");
			String randomNumber = "";

			airtel+= String.valueOf(random.nextInt(10));
			// Range from 98720***** to 98729*****

			for(int j=0; j<5; j++) {
				randomNumber+= String.valueOf(random.nextInt(10));
			}

			airtel+=randomNumber;
			totalNumberList.add(Long.parseLong(airtel));
		}

		for(int i=0; i<10; i++) {

			String idea = new String("9814");
			String randomNumber = "";

			idea+= String.valueOf(random.nextInt(10));
			// Range from 98720***** to 98729*****

			for(int j=0; j<5; j++) {
				randomNumber+= String.valueOf(random.nextInt(10));
			}

			idea+=randomNumber;
			totalNumberList.add(Long.parseLong(idea));
		}

		for(int i=0; i<10; i++) {

			String jio = new String("9917");
			String randomNumber = "";

			jio+= String.valueOf(random.nextInt(10));
			// Range from 98720***** to 98729*****

			for(int j=0; j<5; j++) {
				randomNumber+= String.valueOf(random.nextInt(10));
			}

			jio+=randomNumber;
			totalNumberList.add(Long.parseLong(jio));
		}

		for(int i=0; i<10; i++) {

			String bsnl = new String("9480");
			String randomNumber = "";

			bsnl+= String.valueOf(random.nextInt(10));
			// Range from 98720***** to 98729*****

			for(int j=0; j<5; j++) {
				randomNumber+= String.valueOf(random.nextInt(10));
			}

			bsnl+=randomNumber;
			totalNumberList.add(Long.parseLong(bsnl));
		}

		Collections.shuffle(totalNumberList);
		return totalNumberList;
	}

	static List<String>getRangeList(){
		List<String> rangeList = new ArrayList<String>();
		rangeList.add("9872010*** to 9872929***");
		rangeList.add("9872030*** to 9872969***");
		rangeList.add("9872070*** to 9872999***");
		rangeList.add("9480010*** to 9480929***");
		rangeList.add("9480030*** to 9480969***");
		rangeList.add("9480070*** to 9480999***");
		rangeList.add("9814010*** to 9814929***");
		rangeList.add("9814030*** to 9814969***");
		rangeList.add("9814070*** to 9814999***");
		rangeList.add("9917010*** to 9917929***");
		rangeList.add("9917030*** to 9917969***");
		rangeList.add("9917070*** to 9917999***");

		return rangeList;
	}

	static int findOperator(String number) {

		int operator=0;

		if(number.startsWith("9872"))
			operator = 1;
		else if(number.startsWith("9480"))
			operator = 2;
		else if(number.startsWith("9814"))
			operator = 3;
		else if(number.startsWith("9917"))
			operator = 4;

		return operator;
	}

	static int findRegion(String number) {

		int regionID;

		if((Integer.parseInt(number.substring(5, 7))>=10) && (Integer.parseInt(number.substring(5, 7))<30))
			regionID = 1;
		else if((Integer.parseInt(number.substring(4, 6))>=30) && (Integer.parseInt(number.substring(4, 6))<70))
			regionID = 2;
		else if((Integer.parseInt(number.substring(5, 7))>=70) && (Integer.parseInt(number.substring(5, 7))<100))
			regionID = 3;
		else regionID = 4;

		return regionID;
	}

	static String getTime() {

		final int millisInDay = 24*60*60*1000;
		Time time = new Time((long)random.nextInt(millisInDay));
		return time.toString();
	}

	static String getStatus() {
		String status = "";
		if(random.nextBoolean()==true)
			status = "S";
		else status = "F";
		return status;
	}

}
