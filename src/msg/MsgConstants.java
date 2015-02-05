/**
 * 
 */
package msg;

/**
 * @author Dawson Constants for message identification
 */
public interface MsgConstants {
	public static final int		TEST			= 0, STATUS = 2, SUCCESS = 10, FAIL = 11, QUIT = 20;
	public static final int		SQL				= 20, TESTSQL = 21;
	public static final int		STREAM			= 30;
	// Device identifiers
	public static final int		ANDROID			= 100, PC = 105, SERVER = 110, NODE = 115;
	public static final String	QUERY4			= "SELECT * FROM DataLive", QUERY5 = "SELECT * FROM log LIMIT 50";
	public static final String	Q_INSERT_12ANALOG = "INSERT INTO `DataLive`(`device_name`, `live_value`) VALUES ('Analog_0',400)";

public static       String IP_HOME      = "192.168.1.109";
public static       String IP           = IP_HOME;
public static       String IP_SERVER    = "192.163.250.179";
public static       String HTTP_SYNCRON = "http://syncron.ca";
public static       String HTTP_DAWSON  = "http://dawsonmyers.ca";
public static       String HTTP_SERVER  = "http://dawsonmyers.ca";
public static final int    PORT_SERVER  = 6005;

//@TODO add query ids to array list for easy access
//  query id
public int qSTREAM    = -1;
public int qDATA_LIVE = 0;
public int qTEST_DB   = 1;
public int qNODE_DATA = 2;
public int qOTHER     = 3;


}
