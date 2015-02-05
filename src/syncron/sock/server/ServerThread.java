package syncron.sock.server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import msg.ObjectMessengerThread;
import syncron.controller.ServerController;

// import reworkedChatServer.ServerGUI;
// import reworkedChatServer.Server.ClientThread;

public class ServerThread extends Thread {

	// Analog input values
	public static int[]				analogVals		= null;
	public static int				msgCount		= 0;

	// Digital inputs/outputs
	public static boolean[]			digitalInput	= new boolean[10];

	public static boolean[]			digitalOutput	= new boolean[10];
	// Message intent member that will store the protocol constant

	// a unique ID for each connection
	private static int				uniqueId;
	// an ArrayList to keep the list of the Client
	private ArrayList<ClientThread>	al;

	// to display time
	public static SimpleDateFormat		sdf;
	// the port number to listen for connection
	private int						port;
	// the boolean that will be turned of to stop the server
	private boolean					keepGoing;

	// [UDP] Socket
	public static String			mAnalogString	= null;
	public static UDPServerThread	udpThread		= null;
	public static Thread			udpThreadHelper	= null;
	public static DatagramSocket	udpSocket		= null;
	// ip of server to send to
	public static InetAddress		receiverAddress	= null;
	// 49 bytes in typical formated 12 value analog data string //
	// "0123456789".getBytes();
	public static int				UdpBufferLength	= 49;
	public static byte[]			UdpBuffer		= new byte[UdpBufferLength];
	public static byte[]			UdpOutputBuffer	= new byte[10];
	public static int				udpPort			= 10000;						// arbitrary
	public static String			serverIP		= "192.168.1.109";
	public static String			syncronIP		= "192.163.250.179";
	ServerController controller = ServerController.getInstance();
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	// [UDP]
	//

	/**
	 * server constructor that receive the port to listen to for connection as
	 * parameter in console
	 */

	public ServerThread() {
		super("ServerThread");
		  
		// udpThread = new UDPServerThread(UdpBuffer);
		// udpThread.start();
		// startDebugInput();
		// to display hh:mm:ss
		sdf = new SimpleDateFormat("HH:mm:ss");
		startUDP();
		// ArrayList for the Client list
		al = new ArrayList<ClientThread>();
		this.start();
	}

	public ServerThread(int port) {
		this();
		this.port = port;
	}

	public synchronized void startUDP() {
		udpThread = new UDPServerThread(UdpBuffer);
		udpThread.start();
	}

	public void printAnalogData() {
		Thread thread = new Thread() {
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO
					e.printStackTrace();
				}
				while (msgCount < 1000 && analogVals[1] > 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO
						e.printStackTrace();
					}
					StringBuffer sb = new StringBuffer();

					sb.append(msgCount + " <");
					for (int n : getAnalogVals()) {
						// for (int n : ServerThread.analogVals)
						sb.append((msgCount == 0 ? "" : " ,") + n);
					}
					System.out.println(sb.append(">" + "[" + sdf.format(new Date()) + "]").toString());
					msgCount++;
				}
			}
		};
		thread.start();
		// System.out.println(sb.toString());
	}

	/*
	 // to display hh:mm:ss sdf = new
	 * SimpleDateFormat("HH:mm:ss"); // ArrayList for the Client list al = new
	 * ArrayList<ClientThread>(); }
	 */

	public void run() {
		keepGoing = true;

		/* create socket server and wait for connection requests */
		try {
			// the socket used by the server
			ServerSocket serverSocket = new ServerSocket(port);

			// infinite loop to wait for connections
			while (keepGoing) {
				System.out.println("waiting for clients to connect");
				Socket socket = serverSocket.accept(); // accept connection
				System.out.println("Client connected");
				ObjectMessengerThread messenger = new ObjectMessengerThread(socket);
				messenger.setName("MsgServerTread");

				// ObjectMessenger messager = new ObjectMessenger(socket);
				// messager.start();
				// ClientThread t = new ClientThread(socket); // make a thread
				// of
				// it
				// al.add(t); // save it in the ArrayList
				// t.start();
				if (!keepGoing) break;

			}

			// I was asked to stop
			try {
				serverSocket.close();
				 
				for (int i = 0; i < al.size(); ++i) {
					ClientThread tc = al.get(i);
					try {
						tc.sInput.close();
						tc.sOutput.close();
						tc.socket.close();
					} catch (IOException ioE) {
						// not much I can do
					}
				}
			} catch (Exception e) {
				// display("Exception closing the server and clients: " + e);
			}
		}
		// something went bad
		catch (IOException e) {
			String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			// display(msg);
		} finally {
			
			synchronized (this) {

				try {
					this.notify();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("failed to notify");
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * @return object analogString of type String
	 */
	public static synchronized String getAnalogString() {
		return mAnalogString;
	}

	/**
	 * @param analogString
	 *            the analogString to set
	 */
	public static synchronized void setAnalogString(String analogString) {
		mAnalogString = analogString;
	}

	public static synchronized int[] getAnalogVals() {
		return analogVals;
	}

	public static synchronized void setAnalogVals(int[] analogVals) {
		ServerThread.analogVals = analogVals;
	}

	public static synchronized boolean[] getDigitalOutput() {
		return digitalOutput;
	}

	public static synchronized void setDigitalOutput(boolean[] digitalOutput) {
		ServerThread.digitalOutput = digitalOutput;
	}
}
