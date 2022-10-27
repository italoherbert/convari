package convari.persistence.bo;

import italo.persistence.db.ConnectionDBManager;
import italo.persistence.db.DBManagerException;

import java.sql.Connection;
import java.sql.Timestamp;

import convari.persistence.LoginManager;
import convari.persistence.PersistenceException;
import convari.persistence.bean.LoginBean;
import convari.persistence.dao.DAOException;
import convari.persistence.dao.LoginDAO;


public class LoginBO implements LoginManager {

	private ConnectionDBManager manager;
	private LoginDAO loginDAO;	

	public LoginBO(ConnectionDBManager manager, LoginDAO loginDAO) {
		super();
		this.manager = manager;
		this.loginDAO = loginDAO;
	}
			
	public LoginBean find( int id ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return loginDAO.find( c, id );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public void setPassword( int loginId, String password ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				loginDAO.updatePassword( c, loginId, password );
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
	
	public void setConnected( int id, boolean connected ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				loginDAO.updateConnected( c, id, connected );
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

	public void setAbsent( int id, boolean absent ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				loginDAO.updateAbsent( c, id, absent );
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
		
	public void setLastAccess( int id, Timestamp date ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				loginDAO.updateLastAccess(c, id, date );
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

	public void setStatus( int id, String status ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				loginDAO.updateStatus( c, id, status );
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
