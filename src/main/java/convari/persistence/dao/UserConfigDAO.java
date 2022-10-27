package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import convari.persistence.bean.UserConfigBean;


public class UserConfigDAO {

	public UserConfigBean find(Connection c, int id) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select " +
						"( select v.visibility from visibility v where v.id=u.ender_visibility_id ), " +
						"( select v.visibility from visibility v where v.id=u.tel_visibility_id ), " +
						"( select v.visibility from visibility v where v.id=u.mail_visibility_id ) " +
				"from user u " +
				"where u.id=?" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				UserConfigBean config = new UserConfigBean();
				config.setEnderVisibility( rs.getString( 1 ) );
				config.setTelVisibility( rs.getString( 2 ) );
				config.setMailVisibility( rs.getString( 3 ) );
				return config;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public void load(Connection c, int id, UserConfigBean config) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select " +
						"( select v.visibility from visibility v where v.id=u.ender_visibility_id ), " +
						"( select v.visibility from visibility v where v.id=u.tel_visibility_id ), " +
						"( select v.visibility from visibility v where v.id=u.mail_visibility_id ) " +
				"from user u " +
				"where u.id=?" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				config.setEnderVisibility( rs.getString( 1 ) );
				config.setTelVisibility( rs.getString( 2 ) );
				config.setMailVisibility( rs.getString( 3 ) );
			}
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public void update(Connection c, int userId, UserConfigBean config ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update user set " +
						"ender_visibility_id=( select id from visibility where visibility=? ), " +
						"tel_visibility_id=( select id from visibility where visibility=? ), " +
						"mail_visibility_id=( select id from visibility where visibility=? ) " +
				"where id=?" 
			);
			ps.setString( 1, config.getEnderVisibility() );
			ps.setString( 2, config.getTelVisibility() );
			ps.setString( 3, config.getMailVisibility() );
			ps.setInt( 4, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}

}
