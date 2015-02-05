/**
 * Holds data from node
 */
package msg;

import java.io.Serializable;

/**
 * @author Dawson 
 */
public class NodeMsgData implements Serializable  {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	// Analog input values
	public int[] analogVals = null;
	public String analogString = "";

	// Digital inputs/outputs
	public boolean[] digitalInput = null;

	public boolean[] digitalOutput = null;
}
