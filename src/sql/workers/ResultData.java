package sql.workers;

import sql.mysql.SimpleMySQLResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultData extends DbVariables { // implements DbDataInterface {
	DbDataObject dbData = new DbDataObject();
	ResultSet rs;
	public SimpleMySQLResult rsObj;
	public DbBundle dbBundle = new DbBundle();

	/*
	 * public int columns; public String[] colLabels;
	 * 
	 * ArrayList<String[]> RowList = new ArrayList<String[]>();
	 * ArrayList<Map<String, String>> alRowList = new ArrayList<Map<String,
	 * String>>();
	 * 
	 * Map<String, String> rsRowMap = null; Map<String, Map> rsMap; String[]
	 * rsRowArray;
	 */

	/**
	 * Constructor
	 *
	 * @param rs
	 * @throws SQLException
	 */
	public ResultData(ResultSet rs) throws SQLException {

		this.rs = rs;
		rsObj = new SimpleMySQLResult(rs);
		initColLabels();
		initRowData();
		initDbDataObject();
		dbData.setRowData();
	}

	/**
	 * @return the dbData
	 */
	public DbDataObject getDbData() {
		return dbData;
	}

	void initColLabels() throws SQLException {
		columns = rs.getMetaData().getColumnCount();
		colLabels = new String[columns];
		rs.beforeFirst();
		for (int i = 1; i <= columns; i++) {
			colLabels[i - 1] = rs.getMetaData().getColumnName(i);
		}
	}

	/**
	 * Puts the resultset data into an arraylist<HashMap<String, String>>
	 *
	 * @throws SQLException
	 */
	void initRowData() throws SQLException {
		// String[] rowArray;
		rsObj.beforeFirst();
		rs.beforeFirst();
		while (rsObj.next()) {
			RowList.add(rsObj.FetchArray());
			alRowList.add((HashMap<String, String>) rsObj.FetchAssoc());
		}
	}

	public void initDbDataObject() {
		dbData.columns = columns;
		dbData.colLabels = colLabels;
		dbData.RowList = RowList;
		dbData.alRowList = alRowList;
		dbData.rsRowMap = rsRowMap;
		dbData.rsMap = rsMap;
		dbData.rsRowArray = rsRowArray;

		dbBundle.columns = columns;
		dbBundle.colLabels = colLabels;
		dbBundle.RowList = RowList;
		dbBundle.alRowList = alRowList;
		dbBundle.rsRowMap = rsRowMap;
		dbBundle.rsMap = rsMap;
		dbBundle.rsRowArray = rsRowArray;
	}

	/**
	 * Sets the index for the arraylist which holds the hashmap for a complete
	 * row of fields from the Db table. Then, use the column string name array
	 * in order to retrieve the correct value from the hashmap
	 */
	public void printData() {
		// String rowData = "";
		int j = 0;
		for (String[] row : RowList) {
			int i = 0;

			for (String field : row) {
				rowData += field + " ";

				System.out.print(alRowList.get(j).get(colLabels[i]) + " ");
				i++;
			}
			j++;
			System.out.println();
			rowData += "\n";
		}
		// System.out.println(rowData);
	}

	public void getStrinData() {
		// String rowData = "";
		int j = 0;
		for (String[] row : RowList) {
			int i = 0;

			for (String field : row) {
				rowData += field + " ";
				// Set the index for the arraylist which holds the hashmap for a
				// complete row of fields
				// from the Db table. Then, use the column string name array in
				// order to retrieve the correct
				// value from the hashmap
				// System.out.print(alRowList.get(j).get(colLabels[i]) + " ");
				i++;
			}
			j++;
			System.out.println();
			rowData += "\n";
		}
		// System.out.println(rowData);
	}

	/**
	 * @return the rsObj
	 */
	public SimpleMySQLResult getRsObj() {
		return rsObj;
	}

	/**
	 * @param pRsObj
	 *            the rsObj to set
	 */
	public void setRsObj(SimpleMySQLResult pRsObj) {
		rsObj = pRsObj;
	}

	/**
	 * @return the columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * @param pColumns
	 *            the columns to set
	 */
	public void setColumns(int pColumns) {
		columns = pColumns;
	}

	/**
	 * @return the rowList
	 */
	public ArrayList<String[]> getRowList() {
		return RowList;
	}

	/**
	 * @param pRowList
	 *            the rowList to set
	 */
	public void setRowList(ArrayList<String[]> pRowList) {
		RowList = pRowList;
	}

	/**
	 * @return the alRowList
	 */
	public ArrayList<Map<String, String>> getAlRowList() {
		return alRowList;
	}

	/**
	 * @param pAlRowList
	 *            the alRowList to set
	 */
	public void setAlRowList(ArrayList<Map<String, String>> pAlRowList) {
		alRowList = pAlRowList;
	}

	/**
	 * @param pColLabels
	 *            the colLabels to set
	 */
	public void setColLabels(String[] pColLabels) {
		colLabels = pColLabels;
	}

	/**
	 * @return object dbBundle of type DbBundle
	 */
	public synchronized DbBundle getDbBundle() {
		return this.dbBundle;
	}
}
