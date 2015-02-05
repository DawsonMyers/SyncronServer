package msg;

import syncron.controller.ServerController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageServerThread extends Thread {


	ServerController				controller	= ServerController.getInstance();
	public SimpleDateFormat			sdf			= new SimpleDateFormat("HH:mm:ss");
	public static String			IP			= "192.168.1.109";
	public static int				port		= 6004;
	public static ServerSocket		serverSock;
	public static Socket			socket;
	public static MessageWrapper	msg;
	public static int				count		= 0;


	public MessageServerThread() {
		super("MsgServerThread");
		start();
	}

	// Start server
	// ///////////////////////////////////////////////////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();

		// SimpleDateFormat sdf = (new SimpleDateFormat("HH:mm:ss")).format(new
		// Date);
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			serverSock = new ServerSocket(port);
			// for (count = 0; count < 100; count++) {
			while (serverSock.isBound()) {
				System.out.println((new SimpleDateFormat("[MMM-dd HH.mm.ss.SSS]")).format(new Date()) + " waiting for clients to connect");
				Socket socket = serverSock.accept();
				count++;

				System.out.println((new SimpleDateFormat("[MMM-dd HH.mm.ss.SSS]")).format(new Date()) + " Client connected");

				try {
					ObjectMessengerThread messenger;

					messenger = new ObjectMessengerThread(socket);


				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println("[ERROR - " + (new SimpleDateFormat("MMM-dd HH.mm.ss.SSS")).format(new Date()) + "] -> [MessageServerThread::run]TYPE = Exception | VAR = e1");
				}
			}

			System.out.println("terminating");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("[ERROR - " + (new SimpleDateFormat("MMM-dd HH.mm.ss.SSS]")).format(new Date()) + " -> [MessageServerThread::run]TYPE = IOException | VAR = e1");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[ERROR - " + (new SimpleDateFormat("MMM-dd HH.mm.ss.SSS]")).format(new Date()) + " -> [MessageServerThread::run]TYPE = Exception | VAR = e1");
		} finally {
			synchronized (this) {
				this.notify();
			}
		}

	}

	public synchronized static int getCount() {

		return count;
	}
}
