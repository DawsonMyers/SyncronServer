/**
 * handles all data transaction
 */
package syncron.controller;

import msg.DbBundle;
import msg.NodeMsgData;
import msg.SqlMachine;

/**
 * @author Dawson
 *
 */
public class DataHandler {
	ServerController	controller	= ServerController.getInstance();


	// controller data interface
	// ///////////////////////////////////////////////////////////////////////////////////

	// Node data access
	// ////////////////////////////////////////////////////////////////

	public synchronized NodeMsgData getNodeData() {
		NodeMsgData msgData;
		try {
			msgData = NodeData.getNodeMsgData();
		} catch (Exception e) {
			e.printStackTrace();
			msgData = new NodeMsgData();
		}
		return msgData;
				
	}

	public synchronized void getNodeData(NodeMsgData nodeMsgData) {
		try {
			ServerController.nodeData.setNodeMsgData(nodeMsgData);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[DataHandler::setNodeMsgData] >> ERROR SETTING NodeMsgData");
		}
	}

	public synchronized void setNodeData(NodeMsgData nodeMsgData) {
		try {
			ServerController.nodeData.setNodeMsgData(nodeMsgData);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[DataHandler::setNodeMsgData] >> ERROR GETTING NodeMsgData");
		}
	}

	// Database data access
	// ////////////////////////////////////////////////////////////////

	public synchronized DbBundle getFromDatabase(DbBundle dbBundle) {
		try {
			dbBundle = new SqlMachine(dbBundle).getSql();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[DataHandler::getFromDatabase] >> ERROR GETTING DATA FROM DATABASE");
		}
		return dbBundle;
	}

	public synchronized int[] getAnalogArray() {
		return  NodeData.getAnalogVals();
	}

}
