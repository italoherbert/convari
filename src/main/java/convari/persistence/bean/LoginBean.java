package convari.persistence.bean;

import java.sql.Timestamp;



public class LoginBean implements Bean {
		
	public final static String UNOCCUPIED_STATUS = "unoccupied";
	public final static String OCCUPIED_STATUS = "occupied";
	public final static String INVISIBLE_STATUS = "invisible";
	
	private String username;		
	private String password;
	private boolean connected;
	private boolean absent;
	private String status = UNOCCUPIED_STATUS;
	private Timestamp lastAccess;
			
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean isAbsent() {
		return absent;
	}

	public void setAbsent(boolean absent) {
		this.absent = absent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(Timestamp lastAccess) {
		this.lastAccess = lastAccess;
	}
	
}
