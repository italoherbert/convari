package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OperationConstraintDAO {

	public void addConstraintToOperation( Connection c, int userId, int opId, int visibilityId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 	 							
				"insert into operation_constraint ( user_id, operation_id, visibility_id ) values (?, ?, ?)"											
			);
			ps.setInt( 1, userId );
			ps.setInt( 2, opId );
			ps.setInt( 3, visibilityId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}		
	
	public String getVisibilityToOperation( Connection c, int userId, String opName ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 	 							
				"select ov.visibility as visibility " +
				"from visibility ov inner join operation_constraint oc on ov.id=oc.visibility_id " +
							"inner join operation op on op.id=oc.operation_id " +
				"where oc.user_id=? and op.name=? limit 1"											
			);
			ps.setInt( 1, userId );
			ps.setString( 2, opName ); 
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
				return rs.getString( 1 );			
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public boolean addedConstraintToOperation( Connection c, int userId, int opId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 	 							
				"select id from operation_constraint where user_id=? and operation_id=? limit 1"											
			);
			ps.setInt( 1, userId );
			ps.setInt( 2, opId ); 
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public void updateVisibilityConstraintToOperation( Connection c, int userId, int opId, int visibilityId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 	 							
				"update operation_constraint set visibility_id=? where user_id=? and operation_id=?"											
			);
			ps.setInt( 1, visibilityId );
			ps.setInt( 2, userId );
			ps.setInt( 3, opId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public void deleteConstraintsForUser( Connection c, int userId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 	 							
				"delete from operation_constraint where user_id=?"											
			);
			ps.setInt( 1, userId );
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public void deleteConstraint( Connection c, int userId, int opId ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 	 							
				"delete from operation_constraint where user_id=? and operation_id=?"											
			);
			ps.setInt( 1, userId );
			ps.setInt( 2, opId ); 
			ps.executeUpdate();
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	
}
