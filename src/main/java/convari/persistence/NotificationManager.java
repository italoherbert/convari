package convari.persistence;

import java.sql.Timestamp;
import java.util.List;

import convari.persistence.bean.NotificationBean;


public interface NotificationManager {

	public NotificationBean create();
	
	public List<NotificationBean> listForUser( int userId, Timestamp controlDate ) throws PersistenceException;

	public void deleteForUser( int userId ) throws PersistenceException;

	public void insert( NotificationBean notification ) throws PersistenceException;
				
	public void delete( int userId, int nid ) throws PersistenceException;
	
}
