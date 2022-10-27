package italo.persistence.db;

import java.sql.Connection;
import java.sql.Savepoint;

public interface ConnectionDBManager {
	
	public void execute( DBCommand command ) throws DBManagerException;
	
	public Connection openConnection() throws DBManagerException;
		
	public void closeConnection( Connection conn ) throws DBManagerException;
	
	public void commit( Connection connection ) throws DBManagerException;
	
	public void rollback( Connection connection ) throws DBManagerException;
	
	public void rollback( Connection connection, Savepoint savepoint ) throws DBManagerException;
	
	public boolean getAutoCommit(Connection connection) throws DBManagerException;
	
	public void setAutoCommit(Connection connection, boolean autoCommit ) throws DBManagerException;
		
}
