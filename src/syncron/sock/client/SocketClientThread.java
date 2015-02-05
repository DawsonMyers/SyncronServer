package syncron.sock.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import syncron.serial.SerialThread;
import syncron.utils.obj.sock.MsgObject;
// .MainActivity;

// import javax.swing.JOptionPane;
// import workers.DbDataObject;
// import engine.MySqlSocketTester;
// SocketClientThread.isDataReady()
public class SocketClientThread extends Thread {
	public static final int			TEST					= 0, STATUS = 2, SUCCESS = 10, FAIL = 11, QUIT = 20;
	public static final int			SQL						= 20, TESTSQL = 21;
	public static final int			STREAM					= 30;
	// Device identifiers
	public static final int			ANDROID					= 100, PC = 105, SERVER = 110, NODE = 115;
	public static final String		QUERY4					= "SELECT * FROM DataLive", QUERY5 = "SELECT * FROM log LIMIT 50";

	// to display time
	private SimpleDateFormat		sdf;

	public static Socket			requestSocket;
	ObjectOutputStream				out;
	ObjectInputStream				in;
	ObjectOutputStream				outMsg;
	ObjectInputStream				inMsg;
	String							message, query;

	public static MsgObject			msgObj					= new MsgObject();

	public boolean					quit					= false;
	private boolean[]				digitalInput;
	private static boolean[]		digitalOutput			= new boolean[10];
	public static boolean			mDataReady				= false;
	public static boolean			mCmdReady				= false;
	public static int[]				mAnalogVals				= null;
	public static String			mAnalogString			= null;
	public static MsgObject			mMsg					= new MsgObject();
	// UDP Socket
	public static DatagramSocket	udpSocket				= null;
	public static InetAddress		receiverAddress			= null;
	public static int				UdpBufferLength			= 49;
	public static byte[]			UdpBuffer				= new byte[UdpBufferLength];
	public static int				UdpInputBufferLength	= 10;
	public static byte[]			UdpInputBuffer			= new byte[UdpInputBufferLength];
	public static String			UdpInputBufferString	= new String(UdpInputBuffer);
	public static int				udpPort					= 10000;
	public static int				udpInputPort			= 10005;
	public static String			serverIP				= "192.168.1.109";
	public static Thread			udpListenerThread		= null;
	public static String syncronIP = "192.163.250.179";
	public static String IP = serverIP ;//syncronIP;
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// [UDP]
	public synchronized static void initUDP() throws SocketException, UnknownHostException {
		udpSocket = new DatagramSocket();
		receiverAddress = InetAddress.getByName(IP); // getLocalHost();
	}

	public synchronized static void sendUDP(byte[] buf) throws IOException {
		UdpBuffer = buf.clone();
		if (udpSocket == null) initUDP();
		DatagramPacket packet = new DatagramPacket(UdpBuffer, UdpBuffer.length, receiverAddress, udpPort);
		udpSocket.send(packet);
	}

	public synchronized static void receiveUDP(byte[] buf) throws IOException {
		if (udpSocket == null) udpSocket = new DatagramSocket(udpInputPort);
		// byte[] buffer = new byte[10];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		udpSocket.receive(packet);
		buf = packet.getData();
		UdpInputBufferString = new String(buf);
		// if (buf.length == 10)
	}

	/*
	 * private static void processOutputStates(byte[] buf) { for(int i = 0; i <
	 * 10; i++) { if(buf[i] == 48) { } } for(byte b : buf) { if(b == 48) } }
	 */

	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public SocketClientThread() throws IOException {
		super("SocketClientThread");
		sdf = new SimpleDateFormat("HH:mm:ss");
		// requestSocket = new Socket("192.168.1.109", 6004);
		this.start();
	}

	public SocketClientThread(Socket socket) {
		super("SocketClientThread");
		requestSocket = socket;
	}

	synchronized public void setExternalObj(MsgObject msgObj) {
		// MainActivity.setMsgObj(msgObj);
	}

	public void run() {
		// Debug.out("android socket thread is running");
		// msgObj.columns = 99;
		String s = "S";
		String q = "QUIT";
		// try {

		// Socket requestSocket = new Socket("192.168.1.109", 6004);
		// 2. get Input and Output streams
		//

		// /////////////////////////////////////////////////////////////////////////////
		// [TEST Prompt]

		// /////////////////////////////////////////////////////////////////////////////
		// never stop sending UDP
		startUdpStream();
		// /////////////////////////////////////////////////////////////////////////////
		//
		BufferedReader inLine = new BufferedReader(new InputStreamReader(System.in));

		String input = "";

		try {
			while (!input.equals("p")) {
				input = inLine.readLine();
				if (input.equals("send")) {
					UdpBuffer = mAnalogString.getBytes();
					sendUDP(UdpBuffer);
				}
			}
		} catch (IOException e3) {
			e3.printStackTrace();
		}
		// /////////////////////////////////////////////////////////////////////////////
		// [TEST Prompt]
		try {
			in = new ObjectInputStream(requestSocket.getInputStream());
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
		} catch (IOException e2) {
			// TODO
			e2.printStackTrace();
		}

		// 3: Communicating with the server
		// BufferedReader inLine = new BufferedReader(new
		// InputStreamReader(System.in));
		System.out.println("SEVER:  Object streams created");
		// /////////////////////////////////////////


		msgObj.mIntent = MsgObject.STREAM;
		msgObj.keepStreaming = true;

		// /////////////////////////////////////////
		while (!quit) {
			try {
				Thread.sleep(15);
			} catch (InterruptedException e1) {
				// TODO
				e1.printStackTrace();
			}


			if (mDataReady) {
				// System.out.println("Enter query:");
				message = "Sending to server";
				System.out.println("client> " + message);
				// sendObject(msgObj);

				msgObj.setStatus(STREAM);
				sendObject(msgObj);
				/*
				 * try { out.writeObject(msgObj); out.flush(); } catch
				 * (Exception e) { e.printStackTrace(); }
				 */
				// try {
				// msgObj = (MsgObject) in.readObject();
				// // setExternalObj(msgObj);
				// } catch (ClassNotFoundException e) {
				// e.printStackTrace();
				// }
				if (msgObj.getSuccess()) {
					// SerialThread.setDigiOutput(getDigitalOutput());
					mCmdReady = true;
					setDataReady(false);
				} else // System.out.println("msg not successfull");

				mDataReady = false;
				// synchronized (this) {
				// this.notify();
				// }
				// quit = true;
				SerialThread.isListening = false;
			}
		}

		try {
			in.close();
			out.close();
			requestSocket.close();
			synchronized (this) {
				this.notify();
			}
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}


	}

	private void startUdpStream() {
		//startUdpListener();
		while (true) {

			try {
				Thread.sleep(1000);
				mAnalogString = SerialThread.getAnalogString();
				if (mAnalogString != null) {
					UdpBuffer = mAnalogString.getBytes();
					sendUDP(UdpBuffer);
				}
				// receiveUDP(UdpInputBuffer);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void startUdpListener() {
		udpListenerThread = new Thread("UdpListenerThread") {
			public void run() {
				while (true) {
					try {

						receiveUDP(UdpInputBuffer);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		udpListenerThread.start();
	}

	synchronized void sendObject(MsgObject msgObj) {
		try {
			out.writeObject(msgObj);
			out.flush();
			System.out.println("client>" + "Db object sent to server");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	/**
	 * @return the msgObj
	 */
	public static synchronized void setSerialData() {
		SerialThread.setDigiOutput(msgObj.getDigitalOutput());
	}

	/**
	 * @param msgObj
	 *            the msgObj to set
	 */
	public static synchronized void setmsgObj(MsgObject msgObj) {
		SocketClientThread.msgObj = msgObj;
	}

	synchronized void sendMsg(String msg) {
		try {
			outMsg.writeObject(msg);
			outMsg.flush();
			System.out.println("server>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Control
	/**
	 * @return the mDataReady
	 */
	public static synchronized boolean isDataReady() {
		return mDataReady;
	}

	/**
	 * @param mDataReady
	 *            the mDataReady to set
	 */
	public static synchronized void setDataReady(boolean mDataReady) {
		SocketClientThread.mDataReady = mDataReady;
	}

	/**
	 * @return the mCmdReady
	 */
	public static synchronized boolean isCmdReady() {
		return mCmdReady;
	}

	/**
	 * @param mCmdReady
	 *            the mCmdReady to set
	 */
	public static synchronized void setCmdReady(boolean mCmdReady) {
		SocketClientThread.mCmdReady = mCmdReady;
	}

	// /////////////////////////////////////////////////////////////////////////////
	// Msg
	public synchronized int[] getAnalogVals() {
		return mAnalogVals.clone();
	}

	public synchronized static void setAnalogVals(int[] analogVals) {
		mAnalogVals = analogVals.clone();
		setDataReady(true);
	}

	public synchronized String getAnalogString() {
		return mAnalogString;
	}

	public synchronized static void setAnalogString(String analogString) {
		mAnalogString = analogString;
		// msgObj.setAnalogVals(analogVals.clone());
		// setDataReady(true);
	}


	/**
	 * @param digitalInput
	 *            the digitalInput to set
	 */
	public synchronized void setDigitalInput(boolean[] digitalInput) {
		this.digitalInput = digitalInput;
		setCmdReady(true);
	}

	/**
	 * @return the digitalOutput
	 */
	public synchronized static boolean[] getDigitalOutput() {
		return digitalOutput;
	}

	/**
	 * @param digitalOutput
	 *            the digitalOutput to set
	 */
	public synchronized void setDigitalOutput(boolean[] digitalOutput) {
		SocketClientThread.digitalOutput = digitalOutput;
	}

	public static synchronized String getUdpInputBufferString() {
		return UdpInputBufferString	= new String(UdpInputBuffer);
	}

	public static synchronized void setUdpInputBufferString(String udpInputBufferString) {
		SocketClientThread.UdpInputBufferString = udpInputBufferString;
	}

	/*
	*//**
	 * @return the digitalOutput
	 */
	/*
	 * public synchronized boolean[] getDigitalOutput() { return
	 * msgObj.digitalOutput; }
	 *//**
	 * @param digitalOutput
	 *            the digitalOutput to set
	 */
	/*
	 * public synchronized void setDigitalOutput(boolean[] digitalOutput) {
	 * msgObj.digitalOutput = digitalOutput; }
	 */
	// ///////////////////////////////////////////////////////////////////////////////
	/**
	 * @return the msgObj
	 */
	public static synchronized MsgObject getMsg() {
		return msgObj;
	}

	/**
	 * @param msgObj
	 *            the msgObj to set
	 */
	public static synchronized void setMsg(MsgObject msgObj) {
		SocketClientThread.msgObj = msgObj;
	}

	// public static void main(String args[]) throws SQLException,
	// ClassNotFoundException {
	// Requester client = new Requester();
	// client.run();
	// }

	private void close() {
		// try to close the connection
		try {
			if (out != null) out.close();
		} catch (Exception e) {}
		try {
			if (in != null) in.close();
		} catch (Exception e) {}
        try {
			if (requestSocket != null) requestSocket.close();
		} catch (Exception e) {}

		synchronized (this) {
			this.notify();
		}
	}
}
