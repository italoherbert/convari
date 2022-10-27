package italo.persistence.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Savepoint;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBManagerImpl implements DBManager {

	private DBConfig config;
	private boolean initialized = false;	
	
	public DBManagerImpl() {
		this( new DBConfigImpl() );
	}
	
	public DBManagerImpl(DBConfig config) {
		super();
		this.config = config;
	}

	public void loadDriver() throws DBManagerException {
		loadDriver( config.getDriver() );
	}
		
	public Connection openConnection() throws DBManagerException {
		this.initializationVerifier();
		try {
			if( config.isDataSourceConnection() ) {
				InitialContext context = new InitialContext();
				DataSource source = (DataSource)context.lookup( config.getDataSourceURL() );
				return source.getConnection(); 
			} else {
				return DriverManager.getConnection( config.getUrl(), config.getUsername(), config.getPassword() );
			}			
		} catch (NamingException e) {
			throw new DBManagerException( e );
		} catch (SQLException e) {
			throw new DBManagerException( e );
		}
	}
	
	public void loadDriver( String driver ) throws DBManagerException {
		try {
			Class.forName( driver );
			initialized = true;
		} catch (ClassNotFoundException e) {
			throw new DBManagerException( e );
		}			
	}
		
	public void closeConnection(Connection connection) throws DBManagerException {
		this.initializationVerifier();
		try {
			connection.close();
		} catch (SQLException e) {
			throw new DBManagerException( e );
		}
		
	}
	
	public boolean getAutoCommit(Connection connection) throws DBManagerException {
		this.initializationVerifier();
		try {
			return connection.getAutoCommit();
		} catch (SQLException e) {
			throw new DBManagerException( e );
		}	
	}
	
	public void setAutoCommit(Connection connection, boolean autoCommit ) throws DBManagerException {
		this.initializationVerifier();
		try {
			connection.setAutoCommit( autoCommit );
		} catch (SQLException e) {
			throw new DBManagerException( e );
		}	
	}
	
	public void commit(Connection connection) throws DBManagerException {
		this.initializationVerifier();
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new DBManagerException( e );
		}	
	}

	public void rollback(Connection connection) throws DBManagerException {
		this.initializationVerifier();
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new DBManagerException( e );
		}		
	}
	
	public void rollback(Connection connection, Savepoint savepoint) throws DBManagerException {
		this.initializationVerifier();
		try {
			connection.rollback(savepoint);
		} catch (SQLException e) {
			throw new DBManagerException( e );
		}		
	}
	
	public void execute( DBCommand command ) throws DBManagerException {
		this.initializationVerifier();
		Connection connection = openConnection();
		try {
			command.execute( connection );
		} catch( SQLException e ) {
			throw new DBManagerException( e );
		} finally {
			closeConnection( connection );
		}		
	}
	
	private void initializationVerifier() throws DBManagerException {
		if( !initialized && !config.isDataSourceConnection() )
			throw new DBManagerException( "Módulo não inicializado! - É necessário utilizar o método \"DBManager.initialize\"." );		
	}

	public DBConfig getConfig() {
		return config;
	}

	public void setConfig(DBConfig config) {
		this.config = config;
	}

	public boolean isInitialized() {
		return initialized;
	}
}
