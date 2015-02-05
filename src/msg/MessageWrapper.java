package msg;

import java.io.Serializable;

/**
 * Created by Dawson on 1/29/2015.
 */
public class MessageWrapper implements Serializable, MsgConstants {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public int					mMsgId, mObjectId, mUserId, mActionId, mStatus;
	public int					mRequestId;
	public String[]				mStringsArgs;
	public int[]				mIntArgs;
	public String				mIntent;
	public MessageObject		messageObj			= new MessageObject();

	public Long					mTime;

	public Object				mMsgObject;
	private String				mQuery;

	// main
	// ///////////////////////////////////////////////////////////////////////////////////
	public MessageWrapper() {
		mRequestId = STREAM;
	}

	public MessageWrapper(Object msgObject) {
		mMsgObject = msgObject;
	}

	public MessageWrapper(int requestId, int actionId) {
		mRequestId = requestId;
		mActionId = actionId;
	}

	public MessageWrapper(Object msgObject, int actionId) {
		mMsgObject = msgObject;
		mActionId = actionId;
	}

	public synchronized MessageObject getMessage() {
		return messageObj;
	}

	public void setRequestId(int requestId) {
		this.mRequestId = requestId;
	}

	public int getRequestId() {
		return mRequestId;
	}

	public void setRequestSql(String query) {
		messageObj.setRequestSql(query);
	}

	public String getQuery() {
		mQuery = messageObj.dbBundle.sqlQuery;
		return mQuery;
	}

	public synchronized String getAnalogString() {
		return messageObj.getAnalogString();
	}

}
