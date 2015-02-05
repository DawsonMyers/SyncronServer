package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MsgObject implements Serializable {
	/**
	 *
	 */
	// constants
	// ///////////////////////////////////////////////////////////////////////////////////
	private static final long	serialVersionUID	= 1L;
	public String				SenderID, ResponderID, DeviceType;
	// Protocol constants
	public static final int		TEST				= 0, STATUS = 2, SUCCESS = 10, FAIL = 11, QUIT = 20;
	public static final int		SQL					= 20, TESTSQL = 21;
	public static final int		STREAM				= 30;
	// Device identifiers
	public static final int		ANDROID				= 100, PC = 105, SERVER = 110, NODE = 115;
	public static final String	QUERY4				= "SELECT * FROM DataLive", QUERY5 = "SELECT * FROM log LIMIT 50";

	/**
	 * @return the sql
	 */
	public static int getSql() {
		return SQL;
	}
	// message data
	// ///////////////////////////////////////////////////////////////////////////////////

	// Analog input values
	public int[]								analogVals		= null;
	// Digital inputs/outputs
	public boolean								keepStreaming	= false;
	public boolean[]							digitalInput	= null;

	public boolean[]							digitalOutput	= null;
	// Message intent member that will store the protocol constant
	public int									mIntent			= 0;
	private int									mStatus			= 0;
	// Sql string to be used as a query to the Db
	public String								sqlQuery		= QUERY5, testString = "";
	// members to store data returned from a query
	public int									columns			= 0;
	public String[]								colLabels		= null;
	public ArrayList<String[]>					RowList			= new ArrayList<String[]>();
	public ArrayList<Map<String, String>>	alRowList		= new ArrayList<Map<String, String>>();
	public Map<String, String>					rsRowMap		= null;
	public Map<String, Map>						rsMap			= null;
	public String[]								rsRowArray		= null;
	public String								rowData			= "";

	// methods
	// ///////////////////////////////////////////////////////////////////////////////////

	public synchronized int[] getAnalogVals() {
		return this.analogVals.clone();
	}

	public synchronized void setAnalogVals(int[] analogVals) {
		this.analogVals = analogVals.clone();
	}

	/**
	 * @return the digitalInput
	 */
	public synchronized boolean[] getDigitalInput() {
		return this.digitalInput;
	}

	/**
	 * @param digitalInput
	 *            the digitalInput to set
	 */
	public synchronized void setDigitalInput(boolean[] digitalInput) {
		this.digitalInput = digitalInput;
	}

	/**
	 * @return the digitalOutput
	 */
	public synchronized boolean[] getDigitalOutput() {
		return this.digitalOutput;
	}

	/**
	 * @param digitalOutput
	 *            the digitalOutput to set
	 */
	public synchronized void setDigitalOutput(boolean[] digitalOutput) {
		this.digitalOutput = digitalOutput;
	}

	// Access to constants
	public int SQL() {
		return SQL;
	}

	public int TESTSQL() {
		return TESTSQL;
	}

	// ////////////////////////////////////////////////////////////////////////
	public MsgObject() {
		// TODO Auto-generated constructor stub
	}

	public boolean reqTest() {
		return mIntent == TEST;
	}

	public boolean reqSql() {
		return mIntent == SQL;
	}

	public boolean getSuccess() {
		return mStatus == SUCCESS;
	}

	public void setSuccess(boolean bool) {
		mStatus = bool ? SUCCESS : FAIL;
	}

	public boolean msgQuit() {
		return mStatus == QUIT;
	}

	/**
	 * @return the msgIntent
	 */
	public int getIntent() {
		return mIntent;
	}

	/**
	 * @param pMsgIntent
	 *            the msgIntent to set
	 */
	public void setIntent(int intent) {
		this.mIntent = intent;
	}

	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int pStatus) {
		mStatus = pStatus;
	}

	public void print(String input) {
		System.out.println(input);
	}
}
