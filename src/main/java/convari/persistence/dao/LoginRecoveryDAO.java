package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import convari.persistence.bean.LoginRecoveryBean;


public class LoginRecoveryDAO {

	public void insert( Connection c, int userId, String code, Timestamp time ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into login_recovery (user_id, code, date) values (?, ?, ?)" 
			);
			ps.setInt( 1, userId );
			ps.setString( 2, code );
			ps.setTimestamp( 3, time );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public void update( Connection c, int id, int userId, String code, Timestamp time ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update login_recovery set " +
				"user_id=?, code=?, date=? " +
				"where id=?" 
			);
			ps.setInt( 1, userId );
			ps.setString( 2, code );
			ps.setTimestamp( 3, time );
			ps.setInt( 4, id );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public void delete( Connection c, int id ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"delete from login_recovery where id=?" 
			);
			ps.setInt( 1, id );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public int count( Connection c, int userId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select count(*) from login_recovery" 
			);
			ps.setInt( 1, userId );
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
				return rs.getInt( 1 );			
			return -1;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public LoginRecoveryBean findForTimeoutAndCode( Connection c, String code, Timestamp date ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select id, user_id, date " +
				"from login_recovery " +
				"where code=? and date>=?" 
			);
			ps.setString( 1, code );
			ps.setTimestamp( 2, date ); 
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				LoginRecoveryBean precovery = new LoginRecoveryBean();
				precovery.setId( rs.getInt( 1 ) );
				precovery.setUserId( rs.getInt( 2 ) );
				precovery.setCode( code );
				precovery.setDate( rs.getTimestamp( 3 ) );
				return precovery;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public LoginRecoveryBean findForTimeoutAndLogin( Connection c, int userId, Timestamp date ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select id, code, date " +
				"from login_recovery " +
				"where user_id=? and date>=?" 
			);
			ps.setInt( 1, userId );
			ps.setTimestamp( 2, date ); 
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				LoginRecoveryBean precovery = new LoginRecoveryBean();
				precovery.setId( rs.getInt( 1 ) );
				precovery.setCode( rs.getString( 2 ) );
				precovery.setDate( rs.getTimestamp( 3 ) );
				precovery.setUserId( userId );
				return precovery;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
		
	public LoginRecoveryBean findByLogin( Connection c, int userId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select id, code, date from login_recovery where user_id=?" 
			);
			ps.setInt( 1, userId );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				LoginRecoveryBean precovery = new LoginRecoveryBean();
				precovery.setId( rs.getInt( 1 ) );
				precovery.setCode( rs.getString( 2 ) );
				precovery.setDate( rs.getTimestamp( 3 ) );
				precovery.setUserId( userId );
				return precovery;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public void updateForUser( Connection c, int userId, String code, Timestamp time ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update login_recovery set " +
				"code=?, date=? " +
				"where user_id=?" 
			);
			ps.setString( 1, code );
			ps.setTimestamp( 2, time );
			ps.setInt( 3, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public void deleteForLogin( Connection c, int userId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"delete from login_recovery where user_id=?" 
			);
			ps.setInt( 1, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	} 
	
	public void deleteForTimeoutAndLogin( Connection c, int userId, Timestamp date ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"delete from login_recovery where user_id=? and date<?" 
			);
			ps.setInt( 1, userId );
			ps.setTimestamp( 2, date );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
}
