/**
 *
 */
package sql.workers;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Dawson
 */
public interface DbDataInterface {

	//public static void dbData() {
	public static int      columns   = 0;
	public static String[] colLabels = null;

	public static ArrayList<String[]>            RowList   = new ArrayList<String[]>();
	public static ArrayList<Map<String, String>> alRowList = new ArrayList<Map<String, String>>();

	public static Map<String, String> rsRowMap   = null;
	public static Map<String, Map>    rsMap      = null;
	public static String[]            rsRowArray = null;
//	}
    /*
	 	public static void dbData() {
	int columns = 0;
	String[] colLabels = null;

	ArrayList<String[]> RowList = new ArrayList<String[]>();
	ArrayList<Map<String, String>> alRowList = new ArrayList<Map<String, String>>();

	Map<String, String> rsRowMap = null;
	Map<String, Map> rsMap = null;
	String[] rsRowArray = null;
	 */
}
