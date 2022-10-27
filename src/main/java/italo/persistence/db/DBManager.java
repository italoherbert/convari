package italo.persistence.db;

import java.sql.Connection;

public interface DBManager extends ConnectionDBManager {
	
	public void loadDriver() throws DBManagerException;
	
	public void loadDriver( String driver ) throws DBManagerException;
	
	public Connection openConnection() throws DBManagerException;
			
	public boolean isInitialized();
	
	public DBConfig getConfig();
	
	public void setConfig(DBConfig config);
	
}
