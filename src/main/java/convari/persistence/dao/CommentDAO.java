package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import convari.persistence.bean.CommentBean;


public class CommentDAO {

	public void insert( Connection c, CommentBean comment ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 
				"insert into comment (message, user_id) values (?, ?)" 
			);
			ps.setString( 1, comment.getMessage() );
			ps.setInt( 2, comment.getUserId() );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}	
	}
	
}
