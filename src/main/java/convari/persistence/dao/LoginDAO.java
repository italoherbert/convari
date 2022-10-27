package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import convari.persistence.bean.LoginBean;



public class LoginDAO {
				
	public LoginBean find(Connection c, int id) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
					"select u.username, u.password, us.status, u.last_access, u.connected, u.absent " +
					"from user u inner join user_status us on u.status_id=us.id " +
					"where u.id=?" 
			);
			ps.setInt( 1, id );		
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				LoginBean login = new LoginBean();
				login.setUsername( rs.getString( 1 ) );
				login.setPassword( rs.getString( 2 ) );
				login.setStatus( rs.getString( 3 ) );
				login.setLastAccess( rs.getTimestamp( 4 ) );
				login.setConnected( rs.getBoolean( 5 ) );
				login.setAbsent( rs.getBoolean( 6 ) );
				
				return login;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}
	
	public void load(Connection c, int id, LoginBean login) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
					"select u.username, u.password, us.status, u.last_access, u.connected, u.absent " +
					"from user u inner join user_status us on u.status_id=us.id " +
					"where u.id=?" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				login.setUsername( rs.getString( 1 ) );
				login.setPassword( rs.getString( 2 ) );
				login.setStatus( rs.getString( 3 ) );
				login.setLastAccess( rs.getTimestamp( 4 ) );
				login.setConnected( rs.getBoolean( 5 ) );
				login.setAbsent( rs.getBoolean( 6 ) );
			}
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}
	
	public int count(Connection c, String username) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select count(*) " +
				"from user " +
				"where username=?" 
			);
			ps.setString( 1, username );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				return rs.getInt( 1 );
			}
			return -1;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}	
		
	public void updatePassword(Connection c, int userId, String password) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update user set password=sha1(?) where id=?" 
			);
			ps.setString( 1, password );
			ps.setInt( 2, userId );			
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}
	
	public void updateStatus(Connection c, int userId, String status) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update user set status_id=(select id from user_status where status=?) where id=?" 
			);
			ps.setString( 1, status );
			ps.setInt( 2, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}
	
	public void updateLastAccess(Connection c, int userId, Timestamp lastAccess) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update user set last_access=? where id=?" 
			);
			ps.setTimestamp( 1, lastAccess );
			ps.setInt( 2, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}
	
	public void updateConnected(Connection c, int userId, boolean connected) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update user set connected=? where id=?" 
			);
			ps.setBoolean( 1, connected );
			ps.setInt( 2, userId );			
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}
	
	public void updateAbsent(Connection c, int userId, boolean absent) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update user set absent=? where id=?" 
			);
			ps.setBoolean( 1, absent );
			ps.setInt( 2, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}

	public void update(Connection c, int userId, LoginBean login) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update user set " +
				"username=?, password=sha1(?) " +
				"where id=?" 
			);
			ps.setString( 1, login.getUsername() );
			ps.setString( 2, login.getPassword() );
			ps.setInt( 3, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}

}