package msg;

import sql.engine.MySqlMachine;
import sql.workers.DbDataObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlMachine {
	public DbBundle dbBundle;

	public SqlMachine(DbBundle dbBundle) {
		this.dbBundle = dbBundle;
	}
	// get data from database
	// ///////////////////////////////////////////////////////////////////////////////////

	synchronized public DbBundle getSql() {
		try {
			if (dbBundle.sqlQuery.length() < 5) return new DbBundle();
			DbDataObject dbData = null;
			try {
				dbData = new MySqlMachine(dbBundle.sqlQuery).getDbData();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println(
						"[ERROR - " + (new SimpleDateFormat("MMM-dd HH.mm.ss.SSS")).format(new Date()) + "] -> [SqlMachine::getSql] SQLException");
			}
			String field = dbData.colLabels[1];
			String row = dbData.getRowData();
			//System.out.println(row);

			loadSqlBundle(dbData);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(
					"[ERROR - " + (new SimpleDateFormat("MMM-dd HH.mm.ss.SSS")).format(new Date()) + "] -> [SqlMachine::getSql] Exception");
			return new DbBundle();
		}

		return dbBundle;
	}


	// load database bundle
	// ///////////////////////////////////////////////////////////////////////////////////

	synchronized void loadSqlBundle(DbDataObject dbData) {

		try {
			// MsgObject msgObj = new MsgObject();
			dbBundle.alRowList = dbData.alRowList;
			dbBundle.colLabels = dbData.colLabels;
			dbBundle.columns = dbData.columns;
			dbBundle.rowData = dbData.getRowData();
			dbBundle.RowList = dbData.RowList;
			dbBundle.rsMap = dbData.rsMap;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(
					"[ERROR - " + (new SimpleDateFormat("MMM-dd HH.mm.ss.SSS")).format(new Date()) + "] -> [SqlMachine::loadSqlBundle] Exception");
		}

	}

}
