package convari.persistence.bo;

import italo.persistence.db.ConnectionDBManager;
import italo.persistence.db.DBManagerException;

import java.sql.Connection;

import convari.persistence.LogManager;
import convari.persistence.PersistenceException;
import convari.persistence.dao.DAOException;
import convari.persistence.dao.LogDAO;
import convari.persistence.dao.SQLFunctionsDAO;


public class LogBO implements LogManager {

	private ConnectionDBManager manager;
	private LogDAO logDAO;
	private SQLFunctionsDAO sqlFuncsDAO;
	
	public LogBO(ConnectionDBManager manager, LogDAO logDAO, SQLFunctionsDAO sqlFuncsDAO) {
		super();
		this.manager = manager;
		this.logDAO = logDAO;
		this.sqlFuncsDAO = sqlFuncsDAO;
	}
	
	public void addEventLog( int userId, String type, String key ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				logDAO.insertEventLog( c, userId, type, key );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public void addTextEventLog( int userId, String type, String text ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				logDAO.insertTextEventLog( c, userId, type, text );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public void addLoginFailLog( String username, String ip ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				logDAO.insertLoginFailLog( c, username, ip );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public int addLoginSuccessLog( int userId, String ip ) throws PersistenceException {
		int id = -1;
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				logDAO.insertLoginSuccessLog( c, userId, ip );
				id = sqlFuncsDAO.lastInsertId( c );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
		return id;
	}
	
	public void setLogoutForLoginSuccessLog( int id ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				logDAO.updateLogoutForLoginSuccessLog( c, id );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
}

