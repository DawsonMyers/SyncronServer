package syncron.sock.server;

import sql.workers.DbDataObject;
import sql.engine.MySqlMachine;
import syncron.utils.obj.sock.MsgObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;
// import reworkedChatServer.ChatMessage;
// import reworkedChatServer.Server.ClientThread;
// import socketExample.MsgObject;

public class ClientThread extends Thread {
	public final int TEST = 0, STATUS = 2, SUCCESS = 10, FAIL = 11, QUIT = 20;
	public final int SQL = 20, TESTSQL = 21, STREAM = 30;

	Socket             socket;
	ObjectInputStream  sInput;
	ObjectOutputStream sOutput;
	int  msgCount = 0;
	long t        = 0;
	int id;

	String username;
	// the only type of message a will receive
	// MsgObject msgObj;
	// the date I connect
	//sdf.format(new Date())
	String date;

	DbDataObject dbData = null;

	private int uniqueId;
	MsgObject msgObj = new MsgObject();

	// Constructor
	public ClientThread(Socket socket) {
		// a unique id
		id = ++uniqueId;
		this.socket = socket;
		/* Creating both Data Stream */
		System.out.println("Thread trying to create Object Input/Output Streams");
		date = new Date().toString() + "\n";
		// ConnectionDeamon(this.socket);
	}

	private void ConnectionDeamon(Socket socket) {
		Thread thread = new Thread() {
			public void run() {
				while (!socket.isClosed()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO
						e.printStackTrace();
					}
				}
				close();
				// ClientThread.currentThread().
			}
		};
		thread.start();
	}

	private void display(String pString) {
		// TODO Auto-generated method stub

	}

	// what will run forever
	public void run() {
		// MsgObject msgObj = null;
		// to loop until LOGOUT
		ConnectionDeamon(this.socket);
		boolean keepGoing = true;
		try {// (
			// create output first
			/* ObjectOutputStream */
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			sOutput.flush();
			/* ObjectInputStream */
			sInput = new ObjectInputStream(socket.getInputStream());

			// System.out.println("Object streams succeslly created");
			// MsgObject msgObj = null;
			while (!socket.isClosed()) {
				// read a String (which is an object)
				if (!keepGoing) break;
				if (!socket.isClosed()) {
					//msgObj = (MsgObject) sInput.readObject();
					readObject(msgObj);

					msgResponder(msgObj);
				}
				keepGoing = false;
				// if(msgObj.msgQuit()) break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} /*catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// remove(id);
			// close();
		}*/
	}

	// remove myself from the arrayList containing the list of the
	// connected Clients
	// remove(id);
	// close();
	// }

	synchronized void sendObject(MsgObject msgObj) {

		try {
			msgObj.analogVals = ServerThread.getAnalogVals().clone();
			sOutput.writeObject(msgObj);
			sOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
			close();
		}

	}

	synchronized MsgObject readObject(MsgObject msgObj) {

		try {
			msgObj = (MsgObject) sInput.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			close();
		}
		this.msgObj = msgObj;
		return msgObj;

	}

	synchronized private void msgResponder(MsgObject msgObj) {

		if (msgObj.reqTest()) {
			//msgObj.print(msgObj.testString);
		}
		msgObj.setSuccess(true);
		//msgObj.testString = "From Server: the msg was received successfully";
		this.msgObj = msgObj;
		switch (this.msgObj.getIntent()) {
			case STREAM:
				StartControlStream(msgObj);
				break;
			case TEST:
				msgObj.print(msgObj.testString);
				sendObject(msgObj);
				break;
			case TESTSQL:
				getSql(msgObj);
				break;
			case SQL:
				getSql(msgObj);
				break;
			default:
				break;
		}
		// load the msg object with new info

		// reply to indicate that the msg was successful

		// try {
		// sOutput.writeObject(msgObj);
		// sOutput.flush();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// close();
		// }

	}

	private void StartControlStream(MsgObject msgObj) {

		sendObject(msgObj);
	}

	synchronized private void getSql(MsgObject msgObj) {

		DbDataObject dbData = null;
		try {
			if (msgObj.reqSql()) dbData = new MySqlMachine(msgObj.sqlQuery).getDbData();
			else dbData = new MySqlMachine().getDbData();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String field = dbData.colLabels[1];
		String row = dbData.getRowData();
		System.out.println(row);
		sendSql(dbData);

	}

	synchronized void sendSql(DbDataObject dbData) {
		// MsgObject msgObj = new MsgObject();
		msgObj.alRowList = dbData.alRowList;
		msgObj.colLabels = dbData.colLabels;
		msgObj.columns = dbData.columns;
		msgObj.rowData = dbData.getRowData();
		msgObj.RowList = dbData.RowList;
		msgObj.rsMap = dbData.rsMap;

		sendObject(msgObj);
		System.out.println("client>" + "Db object sent to server");
		// sOutput.writeObject(msgObj);
		// sOutput.flush();
		// out.writeObject(dbData);

	}

	// try to close everything
	private void close() {
		// try to close the connection
		try {
			if (sOutput != null) sOutput.close();
		} catch (Exception e) {
		}
		try {
			if (sInput != null) sInput.close();
		} catch (Exception e) {
		}
		try {
			if (socket != null) socket.close();
		} catch (Exception e) {
		}

		synchronized (this) {
			this.notify();
		}
	}


	// for a client who logoff using the LOGOUT message
	/*
	 * synchronized void remove(int id) { // scan the array list until we found
	 * the Id for (int i = 0; i < al.size(); ++i) { ClientThread ct = al.get(i);
	 * // found it if (ct.id == id) { al.remove(i); return; } } }
	 */
	/*
	 * Write a String to the Client output stream
	 */
	private boolean writeMsg(MsgObject msg) {
		// if Client is still connected send the message to it
		if (!socket.isConnected()) {
			close();
			return false;
		}
		// write the message to the stream
		try {
			sOutput.writeObject(msg);
		}
		// if an error occurs, do not abort just inform the user
		catch (IOException e) {
			display("Error sending message to " + username);
			display(e.toString());
		}
		return true;
	}
}
