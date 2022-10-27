package convari.persistence;


public interface LogManager {

	public void addEventLog( int userId, String type, String key ) throws PersistenceException;
	
	public void addTextEventLog( int userId, String type, String text ) throws PersistenceException;
	
	public void addLoginFailLog( String username, String ip ) throws PersistenceException;
	
	public int addLoginSuccessLog( int userId, String ip ) throws PersistenceException;
	
	public void setLogoutForLoginSuccessLog( int id ) throws PersistenceException;
	
}

