package convari.persistence;

import java.sql.Timestamp;
import java.util.List;

import convari.persistence.bean.InvitationBean;


public interface InvitationManager {
	
	public void invite( int uid1, int uid2 ) throws PersistenceException;
	
	public void remove( int uid1, int uid2 ) throws PersistenceException;
	
	public String status( int uid1, int uid2 ) throws PersistenceException;
	
	public InvitationBean find( int uid1, int uid2 ) throws PersistenceException;
		
	public int countForUserByStatus( int uid, String status ) throws PersistenceException;
	
	public List<InvitationBean> listForUser( int uid, Timestamp cControlDate, Timestamp iControlDate, boolean contactsOnly ) throws PersistenceException;
	
	public List<InvitationBean> listAcceptedOrRemoved( int fromUserId, Timestamp date ) throws PersistenceException;
		
	public List<InvitationBean> listForUserByStatus( int uid, String status ) throws PersistenceException;
	
	public List<InvitationBean> listForUserByPendingStatus( int uid, Timestamp date ) throws PersistenceException;
	
	public List<InvitationBean> listForFromUserByStatus( int fromUserId, String status ) throws PersistenceException;

	public List<InvitationBean> listForToUserByStatus( int toUserId, String status ) throws PersistenceException;
	
	public void updateStatus( int uid1, int uid2, String status ) throws PersistenceException;
	
}
