package msg;

import java.io.Serializable;

/**
 * Created by Dawson on 1/29/2015.
 */
public class MessageObject implements Serializable, MsgConstants {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	public int mMsgId, mObjectId, mUserId, mActionId, mStatus;
public String[] mStringsArgs;
public int[]    mIntArgs;
public String   mIntent;

public NodeMsgData nodeData      = new NodeMsgData();
public MsgObject   msgObj        = new MsgObject();
public DbBundle    dbBundle      = new DbBundle();
// data members
// ///////////////////////////////////////////////////////////////////////////////////
// Analog input values
public int[]       analogVals    = null;
// Digital inputs/outputs
public boolean     keepStreaming = false;
public boolean[]   digitalInput  = null;
public boolean[]   digitalOutput = null;
Long   mTime;
Object mMsgObject;
private String mAnalogString;

public MessageObject() {}

// methods
// ///////////////////////////////////////////////////////////////////////////////////
public synchronized int[] getAnalogVals() {
	return this.analogVals.clone();
}

public synchronized void setAnalogVals(int[] analogVals) {
	this.analogVals = analogVals.clone();
	}

public synchronized String getAnalogString() {
	try {
		if (analogVals != null) {
			for (int i = 0; i < analogVals.length; i++) {
				mAnalogString += analogVals[i] + "\t";
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return mAnalogString;
}

public int[] getAnalogValues() {
	return nodeData.analogVals = analogVals;
}

	public void setAnalogValues(int[] analogVals) {
		nodeData.analogVals = analogVals;
	}

	public void setRequestSql(String query) {
		dbBundle.sqlQuery = query;
	}
}