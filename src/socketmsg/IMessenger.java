package socketmsg;

import msg.MessageWrapper;
import socketmsg.ObjectMessengerBase.ObjectStreamer;

public interface IMessenger {

 

	public void 			sendMessage(MessageWrapper msg);
	public MessageWrapper 	readMessage( );
	//public void sendMessage(MessageWrapper msg, ObjectStreamer s);
	//public MessageWrapper readMessage(ObjectStreamer s );

}
