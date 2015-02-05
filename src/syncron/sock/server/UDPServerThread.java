package syncron.sock.server;

import interfaces.SyncronNetwork;
import msg.NodeMsgData;
import sql.mysql.SocketMySql;
import syncron.controller.ServerController;

import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPServerThread extends Thread implements SyncronNetwork {
	ServerController controller = ServerController.getInstance();
	// [General]
	// to display time
	private SimpleDateFormat sdf;
	private long             lastReceivedTime;

	// [UDP] Socket
	public static          Thread         udpThread       = null;
	public static          DatagramSocket udpSocket       = null;
	// ip of server to send to
	public static          InetAddress    receiverAddress = null;
	public static          InetAddress    returnIP        = null;
	// 49 bytes in typical formated 12 value analog data string //
	// "0123456789".getBytes();
	public static          boolean[]      digiInput       = new boolean[10];
	public static          boolean[]      digiOutput      = new boolean[10];
	public static volatile boolean        newDataAvail    = false;
	public static volatile boolean        isRunning       = false;

	public static int    UdpOutBufferLength = 50;
	public static byte[] UdpOutBuffer       = new byte[UdpOutBufferLength];

	public static int    UdpBufferLength = 49;
	public static byte[] UdpBuffer       = new byte[UdpBufferLength];
	public static byte[] UdpBuffer1      = new byte[UdpBufferLength];
	public static int    udpPort         = 10000;                            // arbitrary
	public static int    returnPort      = 10005;                            // arbitrary

	public static NodeMsgData nodeMsgData = new NodeMsgData();

	public static String serverIP         = "192.168.1.109";
	public static String syncronIP        = "192.163.250.179";
	public static String IP               = syncronIP;
	// UDP Processing
	public static int[]  analogVals       = null;
	public static int    databaseDelay    = 5 * 60 * 1000; // minutes
	public static Thread dbInjectorThread = null;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// [UDP]
	//(new SimpleDateFormat("[MMM-dd HH.mm.ss.SSS]")).format(new Date())

	UDPServerThread() {
		super("UDPServerThread");
		startDatabaseInjection();
		sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			initServerUDP();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
	}


	/**
	 *
	 */
	private void startDatabaseInjection() {

		dbInjectorThread = new Thread("DbInjector") {

			public void run() {
				try {
					while (true) {
						Thread.sleep(databaseDelay);
						SocketMySql sql = new SocketMySql();
						int[] vals = ServerController.dataHandler.getAnalogArray();
						// "INSERT INTO `DataLive`(`device_name`, `live_value`) VALUES ('Analog_0',400)";
						// String quFrag = "I"
						String query = "INSERT INTO `DataLive`(`device_name`, `live_value`) VALUES ";
						if (newDataAvail) {
							String end = ";";
							for (int i = 0; i < vals.length; i++) {
								end = i == (vals.length - 1) ? ";" : ", ";
								query += String.format("('Analog_%s',%s)%s", i, vals[i], end);
//								query += String.format("INSERT INTO `DataLive`(`device_name`, `live_value`) VALUES ('Analog_%s',%s)%s", i, vals[i],end);
							}
							System.out.println((new SimpleDateFormat("[MMM-dd HH.mm.ss.SSS]")).format(new Date()) + "data insert into Db");
							//System.out.println(query);
							sql.insertQuery(query);
						}
						newDataAvail = false;


					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println((new SimpleDateFormat("[MMM-dd HH.mm.ss.SSS]")).format(new Date())
					                   + "  -> [UDPServerThread.startDatabaseInjection().new Thread() {...}::run]");

				}
			}
		};
		dbInjectorThread.start();
	}

	public UDPServerThread(byte[] buffer) {
		this();
		UdpBuffer = buffer;
	}

	public synchronized void run() {
		System.out.println((new SimpleDateFormat("[MMM-dd HH.mm.ss.SSS]")).format(new Date()) + " UDP Thread started");
		while (true) {
			try {
				receiveUDP(UdpBuffer);
				// digiOutput = ServerThread.getDigitalOutput();
				// UdpOutBuffer = ( toString(digiOutput)).getBytes();
				// sendUDP(UdpOutBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// System.out.println("UDP Packet Received");

			processAnalog();

		}
	}

	static String toString(boolean[] out) {
		StringBuffer sb = new StringBuffer();
		for (boolean b : out)
			sb.append(b == true ? "1" : "0");
		return sb.toString();
	}

	public synchronized void processAnalog() throws NumberFormatException {
		long t = System.currentTimeMillis();
		byte[] data;
		String temp = null;
		// try{
		int len = 0;
		StringBuffer sb = new StringBuffer();

		// System.out.println("processing UDP Packet data");
		temp = new String(UdpBuffer, 0, UdpBuffer.length);
		String[] str = temp.toString().replace("<", "").replace(">", "").replace(" ", "").trim().split("_");
		int[] dataInt = new int[12]; // str.length];
		int i = 0;
		for (String s : str) {
			dataInt[i] = Integer.parseInt(s);
			i++;
			if (i > 11) {
				break;
			}
		}

		// ServerThread.setAnalogVals(dataInt);
		// ServerThread.setAnalogString(temp);
		String date = sdf.format(new Date());
		// System.out.println("[" + date + "] " + "Received data string: " +
		// temp);
		// System.out.println("processed data: " +
		// SyncronNetwork.toString(ServerThread.getDigitalOutput()));
		// System.out.println("Processing Complete");

		// Send data to controller
		nodeMsgData.analogVals = dataInt.clone();
		nodeMsgData.analogString = temp;
		temp = null;

		setLastReceivedTime();
		newDataAvail = true;
		ServerController.dataHandler.setNodeData(nodeMsgData);
	}

	/**
	 *
	 */
	private void setLastReceivedTime() {
		lastReceivedTime = System.currentTimeMillis();
	}

	private boolean newDataCheck() {
		return (System.currentTimeMillis() - lastReceivedTime) > 1000;


	}


	/*
	 * public synchronized static void StartUdpThread() throws SocketException,
	 * UnknownHostException { udpThread = new Thread{ public void run() {
	 * while(true) { receiveUDP(UdpBuffer); } } }; }
	 */

	public synchronized static void initServerUDP() throws SocketException, UnknownHostException {
		udpSocket = new DatagramSocket(udpPort);
		receiverAddress = InetAddress.getByName(serverIP);
	}

	public synchronized static void initClientUDP() throws SocketException, UnknownHostException {
		udpSocket = new DatagramSocket();
		receiverAddress = InetAddress.getLocalHost();
	}

	public synchronized static void sendUDP(byte[] buf) throws IOException {
		// UdpBuffer = buf.clone();
		if (udpSocket == null) initClientUDP();
		if (returnIP != null) receiverAddress = returnIP;

		/*
		 * //
		 * ///////////////////////////////////////////////////////////////////
		 * ////////// // BufferedReader inLine = new BufferedReader(new
		 * InputStreamReader(System.in)); String input = ""; try { while
		 * (!input.equals("p")) { input = inLine.readLine(); if
		 * (input.equals("set")) { buf[0]=49; buf[1]=49; buf[2]=49; break; } if
		 * (input.equals("reset")) { buf[0]=48; buf[1]=48; buf[2]=48; break; } }
		 * } catch (IOException e3) { e3.printStackTrace(); } //
		 * ////////////////
		 * /////////////////////////////////////////////////////////////
		 */
		DatagramPacket packet = new DatagramPacket(buf, buf.length, returnIP, returnPort);
		udpSocket.send(packet);
	}

	public synchronized static void receiveUDP(byte[] buf) throws IOException {
		if (udpSocket == null) udpSocket = new DatagramSocket(udpPort);
		// byte[] buffer = new byte[10];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		udpSocket.receive(packet);
		buf = packet.getData();
		returnIP = packet.getAddress();
		returnPort = packet.getPort();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
}
