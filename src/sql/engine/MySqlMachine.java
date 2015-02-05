package sql.engine;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sql.workers.DbDataObject;
import sql.mysql.SocketMySql;

public class MySqlMachine {

	DbDataObject	dbData;
	String			query;

	// public static void main(String[] args) throws SQLException {

	public MySqlMachine() throws SQLException {
		try {
			this.query = "SELECT * FROM DataLive LIMIT 10";

			SocketMySql sql = new SocketMySql(query);
			dbData = sql.getDbData();
			// dbData.printData();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[ERROR - Feb 2, 2015 4:11:02 PM] \n\t [MySqlMachine::MySqlMachine] \n\t TYPE = SQLException | VAR = e");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[ERROR - " + (new SimpleDateFormat("HH:mm:ss")).format(new Date()) + "] -> [MySqlMachine::MySqlMachine] Exception");
		}

	}
	

	public MySqlMachine(String query) throws SQLException {


		try {
			if (query.length() < 5) {
				System.out.println("ERROR: EMPTY EMPTY QUERY");
				throw new SQLException();
			}
			this.query = query; // "SELECT * FROM DataLive";

			SocketMySql sql = new SocketMySql(this.query);
			dbData = sql.getDbData();
			// dbData.printData();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("[ERROR - " + (new SimpleDateFormat("HH:mm:ss")).format(new Date()) + "] -> [MySqlMachine::MySqlMachine] SQLException");
		} catch (Exception e) {
			System.out.println("[ERROR - " + (new SimpleDateFormat("HH:mm:ss")).format(new Date()) + "] -> [MySqlMachine::MySqlMachine] Exception");

		}
	}

	/**
	 * @return the dbData
	 */
	public DbDataObject getDbData() {
		return dbData;
	}
	
	public synchronized void insertQuery( String query) {
		try {
			SocketMySql sql = new SocketMySql();
			sql.insertQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[ERROR - " + (new SimpleDateFormat("HH:mm:ss")).format(new Date()) + "] -> [MySqlMachine::insertQuery]");
		}
	}
}
