package sql.mysql;


import sql.workers.ResultData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class MySql {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://dawsonmyers.ca/";
		String dbName = "testDB";
		String driver = "com.mysql.jdbc.Driver";
		String userName = "root";
		String password = "dawson";


		// create a new instance of SimpleMySQL
		// SimpleMySQL mysql;
		// mysql = new SimpleMySQL();

		// Singleton property
		SimpleMySQL mysql;
		mysql = SimpleMySQL.getInstance();

		mysql.connect("dawsonmyers.ca", userName, password, dbName);

		SimpleMySQLResult result;
		result = mysql.Query("SELECT * FROM DataLive");

		/*
		 * while (result.next()){ System.out.println(result.getString(2));
		 * //System.out.println(result.getString("ts")); }
		 */

		ResultSet rs = result.getResultSet();

		ResultData rsData = new ResultData(rs);
		rsData.printData();

		//Map<String, String> rsRowMap =  rsData.;
		ArrayList<Map<String, String>> alRowList = rsData.getAlRowList();
		//Map<String, Map> rsMap;
		String[] rsArray = rsData.getRowList().get(2);
		String[] rsLabels = null;


		/*int columns = rs.getMetaData().getColumnCount();

		String colLabel = null;
		//HashMap<Integer, String> colLabels = new HashMap<Integer, String>();
		HashMap<Integer, String> colLabels = new HashMap<Integer, String>();

		Map<String, String> rsRowMap =  null;
		ArrayList<Map<String, String>> alRowList = new ArrayList<Map<String, String>>();
		Map<String, Map> rsMap;
		String[] rsArray;
		String[] rsLabels = null;
		StringBuilder message = new StringBuilder();
		// rs.first();
		rs.beforeFirst();
		while (rs.next()) {
			for (int i = 1; i <= columns; i++) {
				message.append(rs.getString(i) + " ");

				colLabel = rs.getMetaData().getColumnName(i);
				colLabels. put(i, colLabel);

				//rsLabels[i] = colLabel;

				rsRowMap= result.FetchAssoc();
				alRowList.add(rsRowMap);
				 rsArray = result.FetchArray();
			}
			message.append("\n");
		}


		colLabel = rs.getMetaData().getColumnName(i);
		colLabels. put(i, colLabel);

		//rsLabels[i] = colLabel;

		rsRowMap= result.FetchAssoc();
		alRowList.add(rsRowMap);
		 rsArray = result.FetchArray();*/


		///////////////////////////////////////////////////////////
//		Map<String, String> rsMap= result.FetchAssoc();
//		String[] rsArray = result.FetchArray();


		////////////////////////////////////////////////////////////
//		String qMsg = message.toString();
//		System.out.println(qMsg); // print table contents
		rs.close();
		result.close();
	}

}
/*
 * while (result.next()){ System.out.println((result.getString("name")); }
 * result.close();
 */