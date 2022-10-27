package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogDAO {

	public void insertEventLog( Connection c, int userId, String type, String key ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into event_log (user_id, type, message_key) values (?, ?, ?)" 
			);
			ps.setInt( 1, userId );
			ps.setString( 2, type );
			ps.setString( 3, key );			
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public void insertTextEventLog( Connection c, int userId, String type, String text ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into text_event_log (user_id, type, text) values (?, ?, ?)" 
			);
			ps.setInt( 1, userId );
			ps.setString( 2, type );
			ps.setString( 3, text );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
			
	public void insertLoginFailLog( Connection c, String username, String ip ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into login_fail_log ( username, ip ) values (?, ?)" 
			);
			ps.setString( 1, username );
			ps.setString( 2, ip );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public void insertLoginSuccessLog( Connection c, int userId, String ip ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into login_success_log ( user_id, ip ) values ( ?, ? )" 
			);
			ps.setInt( 1, userId );
			ps.setString( 2, ip );  
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public void updateLogoutForLoginSuccessLog( Connection c, int id ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update login_success_log set finish_date=current_timestamp where id=?" 
			);
			ps.setInt( 1, id );			
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
}
