package network;

import msg.MessageWrapper;
import syncron.utils.obj.sock.MsgObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;


public class ObjectMessenger extends Thread {
    Socket mSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    ObjectOutputStream outMsg;
    ObjectInputStream inMsg;
    String message, query;
    // DbDataObject dbData;
    public int mObjectPort = 6005;
    public static String syncronIP = "192.163.250.179";
    public static String IP = syncronIP;
    MsgObject msgObj = new MsgObject();
    public boolean quit = false;
    private MessageWrapper msg;

    //public   Syncron app;
    public ObjectMessenger() {
    }

    //public ObjectMessenger(Syncron app) {this.app = app;}
    public ObjectMessenger(int port) {
        mObjectPort = port;
    }

    public ObjectMessenger(Socket socket) {
        mSocket = socket;
    }

//@TODO send via content service
//synchronized public void setExternalObj(syncron.utils.obj.sock.MsgObject msgObj) {
//	setMsgObj(msgObj);
//}

//synchronized public void setExternalObj(MessageWrapper msg) {
//	Syncron.setMsgObj(msg);
//}

    public void run() {
//	Debug.out("android socket thread is running");

//      app = Syncron.getInstance();
        try (
                Socket socket = mSocket;//new Socket(IP, mObjectPort);
                // get Output stream FIRST (for server)
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Streams s = new Streams(socket, in, out)
        ) {
            readMessage(s, msg);
            sendMessage(s, msg);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //app.sendToHander(msg);
    }


    synchronized void sendMessage(Streams s, MessageWrapper msg) throws IOException {
        s.out.writeObject(msg);
        s.out.flush();
    }


    synchronized void readMessage(Streams s, MessageWrapper msg) throws IOException, ClassNotFoundException {
        msg = (MessageWrapper) s.in.readObject();
        //setExternalObj(msg);
    }

    public class Streams implements AutoCloseable {
        Socket socket;
        ObjectInputStream in;
        ObjectOutputStream out;

        public Streams(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
            setStreams(socket, in, out);
        }

        public void setStreams(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
            this.socket = socket;
            this.in = in;
            this.out = out;
        }

        public void getStreams(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
            socket = this.socket;
            in = this.in;
            out = this.out;
        }

        @Override
        public void close() throws Exception {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }

}
