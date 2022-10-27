package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import convari.persistence.bean.NotificationBean;


public class NotificationDAO {

	public void insert( Connection c, NotificationBean notification ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into notification (message, user_id) values (?, ?)" 
			);
			ps.setString( 1, notification.getMessage() );
			ps.setInt( 2, notification.getUserId() );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public List<NotificationBean> listByUser( Connection c, int userId, Timestamp controlDate ) throws DAOException {
		try {
			List<NotificationBean> notifications = new ArrayList<NotificationBean>();
			PreparedStatement ps = c.prepareStatement( 
				"select id, message, date " +
				"from notification " +
				"where user_id=? " +
				( controlDate != null ? "and date>? " : "" ) +
				"order by date desc" 
			);
			ps.setInt( 1, userId );
			if( controlDate != null )
				ps.setTimestamp( 2, controlDate );
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				NotificationBean notification = new NotificationBean();
				notification.setId( rs.getInt( 1 ) );
				notification.setMessage( rs.getString( 2 ) );
				notification.setUserId( userId );
				notification.setDate( rs.getTimestamp( 3 ) );
				notifications.add( notification );
			}
			return notifications;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public void deleteForUser( Connection c, int userId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"delete from notification where user_id=?" 
			);
			ps.setInt( 1, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public void delete( Connection c, int userId, int nid ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"delete from notification where id=? and user_id=?" 
			);
			ps.setInt( 1, nid );
			ps.setInt( 2, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
}
