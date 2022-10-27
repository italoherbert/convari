package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import convari.persistence.bean.UserGeneralDataBean;


public class UserGeneralDataDAO {
			
	public String findImagePath( Connection c, int id ) throws DAOException {
		try {			
			PreparedStatement ps = c.prepareStatement( 
				"select imagepath " +
				"from user " +
				"where id=? " 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
				return rs.getString( 1 );			
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
		
	public UserGeneralDataBean find( Connection c, int id ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select imagepath, name, lastname, sex, website " +
				"from user " +
				"where id=?" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				UserGeneralDataBean data = new UserGeneralDataBean();
				data.setImagepath( rs.getString( 1 ) );
				data.setName( rs.getString( 2 ) );
				data.setLastname( rs.getString( 3 ) );
				data.setSex( rs.getString( 4 ) );
				data.setWebsite( rs.getString( 5 ) );
				return data;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}
	
	public void load( Connection c, int id, UserGeneralDataBean data ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select imagepath, name, lastname, sex, website " +
				"from user " +
				"where id=?" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				data.setImagepath( rs.getString( 1 ) );
				data.setName( rs.getString( 2 ) );
				data.setLastname( rs.getString( 3 ) );
				data.setSex( rs.getString( 4 ) );
				data.setWebsite( rs.getString( 5 ) );
			}
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}
	
	public void update(Connection c, int userId, UserGeneralDataBean data) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update user set " +
				"name=?, lastname=?, sex=?, website=? " +
				"where id=?" 
			);
			ps.setString( 1, data.getName());
			ps.setString( 2, data.getLastname() );
			ps.setString( 3, data.getSex() ); 
			ps.setString( 4, data.getWebsite() );
			ps.setInt( 5, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public void updateImagepath(Connection c, int userId, String imagepath) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update user set " +
				"imagepath=? " +
				"where id=?" 
			);
			ps.setString( 1, imagepath );
			ps.setInt( 2, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
}
