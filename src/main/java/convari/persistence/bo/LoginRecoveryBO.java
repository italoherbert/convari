package convari.persistence.bo;

import italo.persistence.db.ConnectionDBManager;
import italo.persistence.db.DBManagerException;

import java.sql.Connection;
import java.sql.Timestamp;

import convari.persistence.LoginRecoveryManager;
import convari.persistence.PersistenceException;
import convari.persistence.bean.LoginRecoveryBean;
import convari.persistence.dao.DAOException;
import convari.persistence.dao.LoginRecoveryDAO;


public class LoginRecoveryBO implements LoginRecoveryManager {

	private ConnectionDBManager manager;
	private LoginRecoveryDAO loginRecoveryDAO;	
	private long timeout = 86400000;

	public LoginRecoveryBO(ConnectionDBManager manager, LoginRecoveryDAO pwRecoveryDAO) {
		super();
		this.manager = manager;
		this.loginRecoveryDAO = pwRecoveryDAO;
	}

	public Timestamp setLoginRecovery(int loginId, String code) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				LoginRecoveryBean recovery = loginRecoveryDAO.findByLogin( c, loginId );
				long time = System.currentTimeMillis();
				Timestamp date = new Timestamp( time );
				if( recovery == null ) {
					loginRecoveryDAO.insert( c, loginId, code, date );
				} else {
					loginRecoveryDAO.updateForUser( c, loginId, code, date );
				}
				manager.commit( c );
				return new Timestamp( time+timeout );
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
	
	public LoginRecoveryBean findForTimeoutAndCode( String code ) throws PersistenceException {
		try {			
			Connection c = manager.openConnection();
			try {
				long yesterday = System.currentTimeMillis() - timeout;
				return loginRecoveryDAO.findForTimeoutAndCode( c, code, new Timestamp( yesterday ) ); 
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	public LoginRecoveryBean findForTimeoutAndLogin(int loginId) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				long yesterday = System.currentTimeMillis() - timeout;
				return loginRecoveryDAO.findForTimeoutAndLogin( c, loginId, new Timestamp( yesterday ) ); 
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}

	public void removeForLogin(int loginId) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				loginRecoveryDAO.deleteForLogin(c, loginId ); 
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
	
	public void removeForTimeoutAndLogin(int loginId) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				long yesterday = System.currentTimeMillis() - timeout;
				loginRecoveryDAO.deleteForTimeoutAndLogin(c, loginId, new Timestamp( yesterday ) ); 
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

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
}
