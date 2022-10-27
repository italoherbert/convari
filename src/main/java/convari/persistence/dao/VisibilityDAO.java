package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VisibilityDAO {

	public int findIdToVisibility( Connection c, String visibility ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 	 							
				"select id from visibility where visibility=? limit 1"											
			);
			ps.setString( 1, visibility ); 
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
				return rs.getInt( 1 );			
			return -1;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
}
