/**
 *
 */
package sql.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sql.dbLogin.Login;
import sql.workers.DbDataObject;
import sql.workers.ResultData;

/**
 * @author Dawson
 */
public class SocketMySql {

	DbDataObject dbData = new DbDataObject();
	Login L = new Login();
	SimpleMySQL mysql;
	String query;

	// constructors
	// ///////////////////////////////////////////////////////////////////////////////////
	// mysql.connect("dawsonmyers.ca", userName, password, dbName);
	public SocketMySql() {
	}

	public SocketMySql(String query) throws SQLException {
		if (query.length() < 5) return;
		this.query = query;
		getQueryData();
	}

	// methods
	// ///////////////////////////////////////////////////////////////////////////////////
	public void getQueryData(String query) throws SQLException {
		if (query.length() < 5) return;
		this.query = query;
		getQueryData();
	}

	public void insertQuery(String query) {
		try {
			if (query == null | query == "") return;
			if (query.length() < 5) return;

			mysql = SimpleMySQL.getInstance();
			mysql.connect(L.url, L.userName, L.password, L.dbName);
			mysql.Query(query);
			mysql.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[ERROR - " + (new SimpleDateFormat("[MMM-dd HH.mm.ss.SSS]")).format(
					new Date()) + "] -> [SocketMySql::insertQuery]TYPE = Exception | VAR = e");
		}
	}

	public void getQueryData() throws SQLException {
		if (query == null | query == "") return;
		if (query.length() < 5) return;
		SimpleMySQLResult result;
		mysql = SimpleMySQL.getInstance();

		mysql.connect(L.url, L.userName, L.password, L.dbName);

//		result = mysql.Query("SELECT * FROM DataLive");

		if (query != null | query != "") {
			result = mysql.Query(query);
			ResultSet rs = result.getResultSet();
			ResultData rsData = new ResultData(rs);
//		rsData.printData();
			dbData = rsData.getDbData();
			rs.close();
			result.close();
		} else System.out.println("EMPTY QUERY");

	}

	/**
	 * @return the dbData
	 */
	public DbDataObject getDbData() {
		return dbData;
	}

}

