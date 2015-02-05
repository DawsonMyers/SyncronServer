package interfaces;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;

import syncron.sock.server.UDPServerThread;
import syncron.utils.obj.sock.MsgObject;

public interface SyncronNetwork {

	public static final int TEST = 0, STATUS = 2, SUCCESS = 10;
	public static final int FAIL = 11, QUIT = 20;
	public static final int SQL = 20, TESTSQL = 21;
	public static final int STREAM  = 30;
	// Device identifiers
	public static final int ANDROID = 100, PC = 105, SERVER = 110;
	public static final int    NODE   = 115;
	public static final String QUERY4 = "SELECT * FROM DataLive";
	public static final String QUERY5 = "SELECT * FROM log LIMIT 50";

	// to display time
	SimpleDateFormat sdf = null;

	public static UDPServerThread udpThread       = null;
	public static Thread          udpThreadHelper = null;

	public static boolean[]      digiInput            = new boolean[10];
	public static boolean[]      digiOutput           = new boolean[10];
	public        boolean        quit                 = false;
	public static boolean[]      digitalOutput        = null;
	public static boolean        mDataReady           = false;
	public static boolean        mCmdReady            = false;
	public static int[]          mAnalogVals          = null;
	public static String         mAnalogString        = null;
	public static MsgObject      mMsg                 = new MsgObject();
	// UDP Socket
	public static DatagramSocket udpSocket            = null;
	public static InetAddress    receiverAddress      = null;
	public static byte[]         UdpBuffer            = null;
	public static int            UdpBufferLength      = 49;
	public static byte[]         UdpInputBuffer       = null;
	public static int            UdpInputBufferLength = 50;
	public static byte[]         UdpOutBuffer         = null;
	public static int            UdpOutBufferLength   = 50;
	public static int            udpPort              = 10000;
	public static int            udpInputPort         = 10005;
	public static String         serverIP             = "192.168.1.109";
	public static Thread         udpListenerThread    = null;
	public static int[]          analogVals           = null;

	static String toString(boolean[] out) {
		StringBuffer sb = new StringBuffer();
		for (boolean b : out)
			sb.append(b == true ? "1" : "0");
		return sb.toString();
	}
}
