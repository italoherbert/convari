package convari.persistence.bo;

import italo.persistence.db.ConnectionDBManager;
import italo.persistence.db.DBManagerException;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import convari.persistence.NotificationManager;
import convari.persistence.PersistenceException;
import convari.persistence.bean.NotificationBean;
import convari.persistence.dao.DAOException;
import convari.persistence.dao.NotificationDAO;


public class NotificationBO implements NotificationManager{

	private ConnectionDBManager manager;
	private NotificationDAO notificationDAO;
	
	public NotificationBO(ConnectionDBManager manager, NotificationDAO messageDAO) {
		super();
		this.manager = manager;
		this.notificationDAO = messageDAO;
	}
	
	public NotificationBean create() {
		NotificationBean notification = new NotificationBean();
		return notification;
	}
	
	public void insert( NotificationBean notification ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				notificationDAO.insert( c, notification );
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
	
	public List<NotificationBean> listForUser( int userId, Timestamp controlDate ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return notificationDAO.listByUser( c, userId, controlDate );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public void deleteForUser( int userId ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				notificationDAO.deleteForUser( c, userId );
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
	
	public void delete( int userId, int nid ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				notificationDAO.delete( c, userId, nid );
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
