package msg;

import syncron.controller.ServerController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageServer {

	ServerController controller = ServerController.getInstance();
	public    SimpleDateFormat		sdf = new SimpleDateFormat("HH:mm:ss");
	public static String IP = "192.168.1.109";
	public static int port = 6005;
	public static ServerSocket serverSock;
	public static Socket socket;
	public static MessageWrapper msg;
	public static int count =  0;

	// Start server
	// ///////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) {
		try {SimpleDateFormat		sdf = new SimpleDateFormat("HH:mm:ss");
			serverSock = new ServerSocket(port);
			for (count = 0; count < 100; count++) {
				System.out.println("waiting for clients to connect");
				Socket socket = serverSock.accept();
				System.out.println("Client connected"+"[" + sdf.format(new Date()) + "]");

				ObjectMessengerThread messenger = new ObjectMessengerThread(
						socket);

				messenger.socket = socket;
				messenger.setName("MsgServerTread");
				// messenger.start();
				// messenger.sendMessage(msg);

				synchronized (messenger) {

					try {
						messenger.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			System.out.println("terminating");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public synchronized static int getCount() {

		return count;
	}
}
