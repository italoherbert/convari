package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAO {
	
	public void addRoleToUser( Connection c, int userId, int roleId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into user_role_map (user_id, role_id) values (?, ?)" 
			);
			ps.setInt( 1, userId );
			ps.setInt( 2, roleId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
	public int idForName( Connection c, String name ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select id from role where name=? limit 1" 
			);
			ps.setString( 1, name );
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
				return rs.getInt( 1 );
			return -1;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
}
