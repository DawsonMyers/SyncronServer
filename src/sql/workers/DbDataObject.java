/**
 *
 */
package sql.workers;

import java.io.Serializable;

/**
 * @author Dawson
 *	processes sql query result into an organized structure
 */
public class  DbDataObject extends DbVariables implements Serializable {

	/**
	 *	Process sql
	 */
	public DbBundle dbBundle;
	private static final long serialVersionUID = 12L;
	public DbDataObject() {
		// TODO Auto-generated constructor stub
		// super();
		// colLabels[0] ="";
	}

	public void loadDbBundel(){
//		dbBundle.alRowList = dbData.alRowList;
//		dbBundle.colLabels = dbData.colLabels;
//		dbBundle.columns = dbData.columns;
//		dbBundle.rowData = dbData.getRowData();
//		dbBundle.RowList = dbData.RowList;
//		dbBundle.rsMap = dbData.rsMap;
	}
	public void printData() {
		String rowData = "";
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
				System.out.print(alRowList.get(j).get(colLabels[i]) + " ");
				i++;
			}
			j++;
			System.out.println();
			rowData += "\n";
		}
		// System.out.println(rowData);
	}

	// alRowList.get(j).get(colLabels[i]);

	public void setRowData() {
		this.rowData = getRowData();
	}

	public String getRowData() {
		rowData = getRowDataString();
		return rowData;
	}
	/**
	 * Converts all the data from the resultset to a single string
	 * @return
	 */
	public String getRowDataString() {
		String rowData = "";
		int j = 0;
		for (String[] row : RowList) {
			int i = 0;
			for (String field : row) {
				rowData += field + " ";
				i++;
			}
			j++;
			rowData += "\n";
		}
		return rowData;
	}

}
