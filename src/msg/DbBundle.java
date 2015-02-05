package msg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class DbBundle  implements Serializable  {
	public int									columns			= 0;
	public String[]								colLabels		= null;
	public ArrayList<String[]>					RowList			= new ArrayList<String[]>();
	public ArrayList<Map<String, String>>		alRowList		= new ArrayList<Map<String, String>>();
	public Map<String, String>					rsRowMap		= null;
	public Map<String, Map>						rsMap			= null;
	public String[]								rsRowArray		= null;
	public String								rowData			= "";
	public String								sqlQuery		= "";

	
	
	public DbBundle() {}



}
