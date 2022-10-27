package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import convari.persistence.bean.UserContactDataBean;

public class UserContactDataDAO {

	public UserContactDataBean find( Connection c, int id ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select ender, city, state, country, tel, tel2, mail, mail2 " +
				"from user " +
				"where id=?" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				UserContactDataBean data = new UserContactDataBean();
				data.setEnder( rs.getString( 1 ) );
				data.setCity( rs.getString( 2 ) );
				data.setState( rs.getString( 3 ) );
				data.setCountry( rs.getString( 4 ) );
				data.setTel( rs.getString( 5 ) );
				data.setTel2( rs.getString( 6 ) );
				data.setMail( rs.getString( 7 ) );
				data.setMail2( rs.getString( 8 ) );				
				return data;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}
	
	public void load( Connection c, int id, UserContactDataBean data ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select ender, city, state, country, tel, tel2, mail, mail2 " +
				"from user " +
				"where id=?" 
			);
			ps.setInt( 1, id );
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				data.setEnder( rs.getString( 1 ) );
				data.setCity( rs.getString( 2 ) );
				data.setState( rs.getString( 3 ) );
				data.setCountry( rs.getString( 4 ) );
				data.setTel( rs.getString( 5 ) );
				data.setTel2( rs.getString( 6 ) );
				data.setMail( rs.getString( 7 ) );
				data.setMail2( rs.getString( 8 ) );				
			}
		} catch( SQLException e ) {
			throw new DAOException( e );
		}		
	}
	
	public void update( Connection c, int userId, UserContactDataBean data ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"update user set " +
				"ender=?, city=?, state=?, country=?, tel=?, tel2=?, mail=?, mail2=? " +
				"where id=?" 
			);
			ps.setString( 1, data.getEnder() );
			ps.setString( 2, data.getCity());
			ps.setString( 3, data.getState() );
			ps.setString( 4, data.getCountry() ); 
			ps.setString( 5, data.getTel() );
			ps.setString( 6, data.getTel2() );
			ps.setString( 7, data.getMail() );
			ps.setString( 8, data.getMail2() );
			ps.setInt( 9, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
}
