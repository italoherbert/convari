package convari.persistence;

import java.sql.Timestamp;

import convari.persistence.bean.LoginBean;


public interface LoginManager {
	
	public LoginBean find( int id ) throws PersistenceException;
		
	public void setPassword( int id, String password ) throws PersistenceException;
	
	public void setLastAccess( int id, Timestamp date ) throws PersistenceException;

	public void setConnected( int id, boolean connected ) throws PersistenceException;
	
	public void setAbsent( int id, boolean absent ) throws PersistenceException;
	
	public void setStatus( int id, String status ) throws PersistenceException;
	
}
