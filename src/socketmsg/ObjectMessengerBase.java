package socketmsg;

import msg.MessageWrapper;
import msg.MsgTimer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectMessengerBase extends Thread implements IMessenger {
	public MessageWrapper msg;
	public ObjectStreamer os;
	public long     msgTime    = 0;
	public MsgTimer timer      = new MsgTimer();
	static String   threadName = "ObjectMsgThread";

	// public static void main(String[] args) {
	//
	// }
	public ObjectMessengerBase() {
		super(threadName);
		os = new ObjectStreamer();
	}


	public ObjectMessengerBase(String ip, int port) {
		os = new ObjectStreamer(ip, port);
	}

	public ObjectMessengerBase(Socket socket) {
		os = new ObjectStreamer(socket);
	}

	@Override
	public synchronized void sendMessage(MessageWrapper msg) {
		os.sendMessage(msg);
	}

	@Override
	public synchronized MessageWrapper readMessage() {
		msg = os.readMessage();
		return msg;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public class ObjectStreamer implements AutoCloseable, IMessenger {
		Socket socket;
		ObjectInputStream in;
		ObjectOutputStream out;
		public int port;
		public String ipAdress;
		public String IP         = "192.168.1.109";
		public int    serverPort = 6005;

		// send/receive message objects
		// ///////////////////////////////////////////////////////////////////////////////////
		@Override
		public void sendMessage(MessageWrapper msg) {
			try {
				out.writeObject(msg);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public MessageWrapper readMessage() {
			try {
				msg = (MessageWrapper) os.in.readObject();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			return msg;
		}


		// connect streams
		// ///////////////////////////////////////////////////////////////////////////////////
		public void connectClient(String IpAdress, int serverPort) {
			if (serverPort > 0)
				this.port = serverPort;
			this.ipAdress = IpAdress;
			try { // Client -> create input first
				socket = new Socket(IpAdress, port);
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void connectServer(Socket socket) {
			try { // Server -> create output first
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		// use if member data was manually set
		public void connectServer() {
			try { // Server -> create output first
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Contructors
		// ///////////////////////////////////////////////////////////////////////////////////
		public ObjectStreamer() {
		}

		public ObjectStreamer(String ip, int port) {
			// connectClient(ip, port);

		}

		public ObjectStreamer(Socket socket) {
			connectServer(socket);

		}

		// only used when being used as a bundle to hold all the stream objects
		public ObjectStreamer(Socket socket, ObjectInputStream in,
		                      ObjectOutputStream out) {
			setStreams(socket, in, out);
		}

		public ObjectStreamer(int port, ObjectInputStream in,
		                      ObjectOutputStream out) {
			setStreams(port, in, out);
		}

		// set members streams
		// ///////////////////////////////////////////////////////////////////////////////////
		public void setStreams(Socket socket, ObjectInputStream in,
		                       ObjectOutputStream out) {
			this.socket = socket;
			this.in = in;
			this.out = out;
		}

		public void setStreams(int port, ObjectInputStream in,
		                       ObjectOutputStream out) {
			this.port = port;
			this.in = in;
			this.out = out;
		}

		public void getStreams(Socket socket, ObjectInputStream in,
		                       ObjectOutputStream out) {
			socket = this.socket;
			in = this.in;
			out = this.out;
		}

		// close streams
		// ///////////////////////////////////////////////////////////////////////////////////
		@Override
		public void close() {
			try {
				if (socket != null)
					socket.close();
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	//	thread locking object
	public class ObjLock {
		int lockId = 0;

		public ObjLock() {
		}
	}
}
