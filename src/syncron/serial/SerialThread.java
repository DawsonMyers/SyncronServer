package syncron.serial;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import syncron.sock.client.SocketClientThread;

public class SerialThread extends Thread {
	public static String[]   data          = null;
	public static int[]      dataInt       = null;
	public static boolean    isReady       = false;
	public static boolean    gotData       = false;
	public static boolean    isListening   = false;
	static        SerialPort serialPort    = new SerialPort("COM4");
	public static int        i             = 0;
	public static int        avail         = 0;
	public static int[]      mAnalogVals   = new int[12];
	// Analog input values
	public        int[]      analogVals    = null;
	public static String     mAnalogString = null;
	// Digital inputs/outputs
	public static boolean[]  digiInput     = new boolean[10];
	public static boolean[]  digiOutput    = new boolean[10];

	public static void setIO() {

	}

	public SerialThread() {
		try {
			serialPort.openPort();

			serialPort.setParams(SerialPort.BAUDRATE_115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

			serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);

		} catch (SerialPortException ex) {
			System.out.println("There are an error on writing string to port : " + ex);
		}

		// try {
		// startSerialServer();
		// } catch (SerialPortException e) {
		// // TODO
		// e.printStackTrace();
		// }

	}

	private static class PortReader implements SerialPortEventListener {

		// private String[] data;
		@Override
		synchronized public void serialEvent(SerialPortEvent event) {
			if (event.isRXCHAR() && event.getEventValue() > 0) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				// System.out.println("SERIAL EVENT");
				currentThread().setName("eventThread");
				long t = System.currentTimeMillis();
				byte[] data;
				String temp = null;
				// try{
				int len = 0;
				StringBuffer sb = new StringBuffer();
				try {
					Thread.sleep(30);
					Outer:
					while (serialPort.getInputBufferBytesCount() > 0) {

						temp = serialPort.readString(1);
						if (temp.equals("<")) {
							sb.append(temp);
							while (serialPort.getInputBufferBytesCount() > 0) {
								temp = serialPort.readString(1);
								sb.append(temp);
								if (temp.equals(">")) {
									temp = sb.toString();
									break Outer;
								}
							}
						}
						// break;
						// temp = serialPort.readString();
					}
				} catch (SerialPortException | InterruptedException e) {
					// TODO
					e.printStackTrace();
				}

				// buff = buff == null ? temp : buff + temp;
				// if (buff.contains(">")) isReady = true;
				if (temp.length() > 5) {
					// System.out.println("processing input serial data");
					isReady = true;
					// System.out.println(temp);
					String[] str = temp.toString().replace("<", "").replace(">", "").split("_");
					int[] dataInt = new int[12]; // str.length];
					int i = 0;
					for (String s : str) {
						dataInt[i] = Integer.parseInt(s);
						i++;
						if (i > 11) {

							break;
						}
					}

					// Send data to socket thread
					setAnalogString(temp);
					mAnalogVals = dataInt;
//					SocketClientThread.setAnalogVals(dataInt);

					// System.out.println("sending data int to socket thread");
					// System.out.println("Serial input processing - time: " +
					// Integer.toString(((int)(System.currentTimeMillis() -
					// t))));
					incAvail();
					setIsListening(false);
					temp = null;

					isListening = false;

					setIsListening(false);
				}
				setIsListening(false);
			}
			setIsListening(false);
		}


	}

	public synchronized static void setIsListening(boolean b) {
		isListening = b;
	}

	public synchronized static void decAvail() {
		avail--;
	}

	public synchronized static void incAvail() {
		avail++;
	}

	public synchronized static boolean isAvail() {
		return avail > 0;
	}

	public void run() {
		boolean firstRun = true;
		// //////////////////////////////////////////// NEED TO DELAY AT START
		// IN ORDER FOR IT TO RUN!!!!!!!!!!!
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			// TODO
			e2.printStackTrace();
		}
		// ////////////////////////////////////////////
		incAvail();
		while (serialPort.isOpened()) {


			// if (!isListening) {
			if (isAvail()) {
				// System.out.println("sending 'a' over serial");
				try {
					isListening = true;
					decAvail();
					//String cmdString = SocketClientThread.getUdpInputBufferString(); // toString(SocketClientThread.getDigitalOutput());
					// o: write to outputs, a: get analog values
					// &: command terminator, >: end of output states
					//if (cmdString == null) cmdString = "1000000000";


					//serialPort.writeString("o&" + cmdString + ">");
					serialPort.writeString("a&");
				} catch (SerialPortException e1) {
					// TODO
					e1.printStackTrace();
				}
			    /*
				 * while (!isReady) { try { Thread.sleep(10); } catch
				 * (InterruptedException e) { // TODO e.printStackTrace(); } };
				 */
				/*
				 * if (SocketClientThread.isDataReady()) { try {
				 * serialPort.writeString("o");
				 * serialPort.writeString(toString(digiOutput)); } catch
				 * (SerialPortException e) { // TODO e.printStackTrace(); }
				 * setDataReady(false); }
				 */
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO
				e.printStackTrace();
			}
			synchronized (PortReader.class) {

			}
			/*
			 * synchronized (Thread("eventThread")) { try { this.wait();
			 * isListening = false; } catch (InterruptedException e) { // TODO
			 * e.printStackTrace(); } }
			 */
		}

	}

	/**
	 * @param out
	 */
	private static String toString(boolean[] out) {
		StringBuffer sb = new StringBuffer();
		for (boolean b : out)
			sb.append(b == true ? "1" : "0");
		return sb.toString();
	}

	/**
	 * @return object analogVals of type int[]
	 */
	public static synchronized int[] getAnalogVals() {
		return mAnalogVals;
	}

	/**
	 * @param analogVals the analogVals to set
	 */
	public static synchronized void setAnalogVals(int[] analogVals) {
		mAnalogVals = analogVals;
	}

	public static synchronized String getAnalogString() {
		return mAnalogString;
	}

	/**
	 * @param analogString the analogString to set
	 */
	public static synchronized void setAnalogString(String analogString) {
		mAnalogString = analogString;
	}

	// // return commanded output values (which were received from the server)
	// synchronized public static void setCommandedOutput(boolean[] digiInput) {
	// SerialEventTest.digiInput = digiInput;
	//
	// }

	/**
	 * @return the digiInput
	 */
	public static synchronized boolean[] getDigiInput() {
		return digiInput;
	}

	/**
	 * @param digiInput the digiInput to set
	 */
	public static synchronized void setDigiInput(boolean[] digiInput) {
		SerialThread.digiInput = digiInput;
	}

	/**
	 * @return the digiOutput
	 */
	public static synchronized boolean[] getDigiOutput() {
		return digiOutput;
	}

	/**
	 * @param digiOutput the digiOutput to set
	 */
	public static synchronized void setDigiOutput(boolean[] digiOutput) {
		SerialThread.digiOutput = digiOutput;
	}

	synchronized public static boolean isCmdReady() {

		return SocketClientThread.isCmdReady();

	}

	synchronized public static boolean isDataReady() {

		return SocketClientThread.mDataReady;

	}

	synchronized public static void setDataReady(boolean b) {

		SocketClientThread.mDataReady = b;

	}

}
