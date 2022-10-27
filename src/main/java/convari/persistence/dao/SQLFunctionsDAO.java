package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class SQLFunctionsDAO {
	
	public int lastInsertId( Connection c ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"select last_insert_id()" 
			);
			ResultSet rs = ps.executeQuery();
			if( rs.next() )				
				return rs.getInt( 1 );
			else throw new DAOException( "Falha na busca por ultimo id gerado para inser��o." );
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}	
	
}
