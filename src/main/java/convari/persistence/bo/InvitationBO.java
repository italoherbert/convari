package convari.persistence.bo;

import italo.persistence.db.ConnectionDBManager;
import italo.persistence.db.DBManagerException;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import convari.persistence.InvitationManager;
import convari.persistence.PersistenceException;
import convari.persistence.bean.InvitationBean;
import convari.persistence.dao.InvitationDAO;
import convari.persistence.dao.DAOException;


public class InvitationBO implements InvitationManager {

	private ConnectionDBManager manager;
	private InvitationDAO invitationDAO;
	
	public InvitationBO(ConnectionDBManager manager, InvitationDAO contactDAO) {
		super();
		this.manager = manager;
		this.invitationDAO = contactDAO;
	}

	public void invite( int uid1, int uid2 ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				boolean added = invitationDAO.added( c, uid1, uid2 );				
				if( added )
					invitationDAO.delete( c, uid1, uid2 );				
				invitationDAO.insert(c, uid1, uid2, InvitationBean.PENDING_INVITATION );				
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

	public void remove( int uid1, int uid2 ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				invitationDAO.delete( c, uid1, uid2 );
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
	
	public void updateStatus(int uid1, int uid2, String status) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				invitationDAO.updateStatus( c, uid1, uid2, status );
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
	
	public InvitationBean find( int uid1, int uid2 ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return invitationDAO.find( c, uid1, uid2 );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	
	public String status(int uid1, int uid2) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return invitationDAO.status( c, uid1, uid2 );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public int countForUserByStatus( int uid, String status ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return invitationDAO.countForUserByStatus( c, uid, status );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public List<InvitationBean> listForUser( int uid, Timestamp cControlDate, Timestamp iControlDate, boolean contactsOnly ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return invitationDAO.listForUser( c, uid, cControlDate, iControlDate, contactsOnly );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}

	public List<InvitationBean> listAcceptedOrRemoved( int fromUserId, Timestamp date ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return invitationDAO.listAcceptedOrRemoved( c, fromUserId, date );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}

	public List<InvitationBean> listForUserByPendingStatus( int uid, Timestamp date ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return invitationDAO.listForUserByPendingStatus( c, uid, date );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}
	
	public List<InvitationBean> listForUserByStatus( int uid, String status ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return invitationDAO.listForUserByStatus( c, uid, status );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}
	
	public List<InvitationBean> listForFromUserByStatus( int fromUserId, String status ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return invitationDAO.listForFromUserByStatus( c, fromUserId, status );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	public List<InvitationBean> listForToUserByStatus( int toUserId, String status ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return invitationDAO.listForToUserByStatus( c, toUserId, status );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

}
