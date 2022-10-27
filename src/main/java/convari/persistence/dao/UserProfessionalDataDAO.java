package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import convari.persistence.bean.UserProfessionalDataBean;


public class UserProfessionalDataDAO {
	
	public UserProfessionalDataBean find(Connection c, int id) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select occupation, academic " +
				"from user " +
				"where id=?" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				UserProfessionalDataBean info = new UserProfessionalDataBean();
				info.setOccupation( rs.getString( 1 ) );
				info.setAcademic( rs.getString( 2 ) );
				return info;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public void load(Connection c, int id, UserProfessionalDataBean info) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select occupation, academic " +
				"from user " +
				"where id=?" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				info.setOccupation( rs.getString( 1 ) );
				info.setAcademic( rs.getString( 2 ) );
			}
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}

	public void update(Connection c, int userId, UserProfessionalDataBean info) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update user set " +
				"occupation=?, academic=? " +
				"where id=?" 
			);
			ps.setString( 1, info.getOccupation() );
			ps.setString( 2, info.getAcademic() );
			ps.setInt( 3, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
}
