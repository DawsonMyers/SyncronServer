/**
 *
 */
package sql.workers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Dawson
 *	variables for sql query processing
 */
public class DbVariables implements Serializable{
	public  int columns; // = 0;
	public  String[] colLabels = null;

	public  ArrayList<String[]> RowList = new ArrayList<String[]>();
	public  ArrayList<Map<String, String>> alRowList = new ArrayList<Map<String, String>>();  //Map<String, String>>();

	public  Map<String, String> rsRowMap = null;
	public  Map<String, Map> rsMap = null;
	public  String[] rsRowArray = null;
	public String rowData = "";

	public DbVariables() {
		columns = 0;
	}

	/**
	 * @return the rowData
	 */
	public String getRowData() {
		return rowData;
	}


}
