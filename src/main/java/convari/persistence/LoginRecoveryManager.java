package convari.persistence;

import java.sql.Timestamp;

import convari.persistence.bean.LoginRecoveryBean;


public interface LoginRecoveryManager {

	public Timestamp setLoginRecovery( int loginId, String code ) throws PersistenceException;
	
	public LoginRecoveryBean findForTimeoutAndCode( String code ) throws PersistenceException;
	
	public LoginRecoveryBean findForTimeoutAndLogin( int loginId ) throws PersistenceException;
	
	public void removeForLogin(int loginId) throws PersistenceException;

	public void removeForTimeoutAndLogin( int loginId ) throws PersistenceException;
	
	public long getTimeout(); 

	public void setTimeout(long timeout);
	
}
