package net.paxcel.ashutoshaneja;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import static net.paxcel.ashutoshaneja.LoadResources.logger;
import static net.paxcel.ashutoshaneja.DatabaseConnection.*;

public class TestCase {

	List<String> expectedOutcome = new ArrayList<String>();

	@BeforeClass
	public static void setupBeforeTesting() throws Exception{
		createConnectionPool();
		System.out.println("Starting JUnit Testing..");
	}

	@Before
	public void setupList() {
		expectedOutcome.clear();
	}

	@Test
	public void testMsgSentBy() throws Exception{
		logger.info("Starting Test Case for Method - searchMsgSentBy() ");

		expectedOutcome.add("a1GD8eLJTT");
		expectedOutcome.add("oaBR8iNEiu");

		final long primaryNo = 9814129697l;
		assertEquals(expectedOutcome, searchMsgSentBy(String.valueOf(primaryNo)));
		logger.info("Test Case Cleared! ✔");
	}

	@Test
	public void testMsgReceivedBy() throws Exception {
		logger.info("Starting Test Case for Method - searchMsgReceivedBy() ");

		expectedOutcome.add("iMaGecNP2I");
		expectedOutcome.add("iOSiFT7jJX");

		final long primaryNo = 9814430424l;
		assertEquals(expectedOutcome, searchMsgReceivedBy(primaryNo, "anyRegion"));
		logger.info("Test Case Cleared! ✔");
	}

	@Test
	public void testMsgFromOneNumberToOther() throws Exception {
		logger.info("Starting Test Case for Method - searchMsgFromOneNumberToOther() ");

		expectedOutcome.add("GPvye3YRJh");
		expectedOutcome.add("earTaQDpUm");

		final long primaryNo = 9917213839l;
		final long secondaryNo = 9917226381l;
		assertEquals(expectedOutcome, searchMsgFromOneNumberToOther(primaryNo, secondaryNo));
		logger.info("Test Case Cleared! ✔");
	}

	@Test
	public void testMsgReceivedByRegion() throws Exception {
		logger.info("Starting Test Case for Method - searchMsgReceivedBy() ");

		expectedOutcome.add("FTkEuPg38F");
		expectedOutcome.add("UUsPZEJyy9");

		final long primaryNo = 9872317017l;
		assertEquals(expectedOutcome, searchMsgReceivedBy(primaryNo, "punjabRegion"));
		logger.info("Test Case Cleared! ✔");
	}

	@Test
	public void testMsgByOperatorAndRegion() throws Exception {
		logger.info("Starting Test Case for Method - searchMsgByOperatorAndRegion() ");

		expectedOutcome.add("UUsPZEJyy9");

		final long primaryNo = 9872317017l;
		assertEquals(expectedOutcome, searchMsgByOperatorAndRegion(primaryNo));
		logger.info("Test Case Cleared! ✔");
	}

	@Test
	public void testMsgSentByRegex() throws Exception {
		logger.info("Starting Test Case for Method - searchMsgSentBy() ");

		expectedOutcome.add("omnDTH5r4p");
		expectedOutcome.add("U7Rk6N5xjs");
		expectedOutcome.add("UUsPZEJyy9");

		final String primaryNo = "99175*****";
		assertEquals(expectedOutcome, searchMsgSentBy(primaryNo));
		logger.info("Test Case Cleared! ✔");
	}

	@Test
	public void testFailedMsg() throws Exception {
		logger.info("Starting Test Case for Method - searchFailedMsg() ");

		expectedOutcome.add("uRma8gI1Er");
		expectedOutcome.add("UUsPZEJyy9");
		expectedOutcome.add("omnDTH5r4p");
		assertEquals(expectedOutcome, searchFailedMsg());
		logger.info("Test Case Cleared! ✔");
	}

	@AfterClass
	public static void teardownAfterTesting() throws Exception{
		System.out.println("Ending JUnit Testing..");
	}
}
