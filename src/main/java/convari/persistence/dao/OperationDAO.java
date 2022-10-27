package convari.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import convari.persistence.bean.OperationBean;


public class OperationDAO {
	
	public String getOperationVisibility( Connection c, String opName ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 	 							
				"select v.visibility " +
				"from visibility v inner join operation op on v.id=op.visibility_id " +
				"where op.name=? " +
				"limit 1"											
			);
			ps.setString( 1, opName ); 
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
				return rs.getString( 1 );			
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public int idToOperationForName( Connection c, String opName ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 	 							
				"select id from operation where name=? limit 1"											
			);
			ps.setString( 1, opName ); 
			ResultSet rs = ps.executeQuery();
			if( rs.next() )
				return rs.getInt( 1 );			
			return -1;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}		
	
	public List<OperationBean> getOperationsByUser( Connection c, int ownerUID ) throws DAOException {
		try {
			PreparedStatement ps = c.prepareStatement( 																			
				"select op.id, op.name, ov.visibility " +
				"from visibility ov inner join operation op on ov.id=op.visibility_id " +
						"inner join role_operation_map romap on op.id=romap.operation_id " +
						"inner join user_role_map urmap on romap.role_id=urmap.role_id " +
				"where urmap.user_id=? "											
			);
			ps.setInt( 1, ownerUID );
			ResultSet rs = ps.executeQuery();
			List<OperationBean> operations = new ArrayList<OperationBean>();
			while( rs.next() ) {
				OperationBean operation = new OperationBean();				
				operation.setId( rs.getInt( 1 ) );
				operation.setName( rs.getString( 2 ) );
				operation.setVisibility( rs.getString( 3 ) );
				operations.add( operation );
			}
			return operations;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}	
	
	public List<OperationBean> listByVisibility( Connection c, String visibility ) throws DAOException {
		List<OperationBean> operations = new ArrayList<OperationBean>();
		try {			
			PreparedStatement ps = c.prepareStatement( 				
				"select op.id, op.name " +
				"from visibility ov inner join operation op on ov.id=op.visibility_id " +
				"where ov.visibility=?" 
			);
			ps.setString( 1, visibility );
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				OperationBean operation = new OperationBean();
				
				operation.setId( rs.getInt( 1 ) );
				operation.setName( rs.getString( 2 ) );
				operation.setVisibility( visibility );
				operations.add( operation );
			}
			return operations;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
	public List<OperationBean> list( Connection c ) throws DAOException {
		List<OperationBean> operations = new ArrayList<OperationBean>();
		try {			
			PreparedStatement ps = c.prepareStatement( 				
				"select op.id, op.name, ov.visibility " +
				"from visibility ov inner join operation op on ov.id=op.visibility_id"
			);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ) {
				OperationBean operation = new OperationBean();				
				operation.setId( rs.getInt( 1 ) );
				operation.setName( rs.getString( 2 ) );
				operation.setVisibility( rs.getString( 3 ) ); 
				operations.add( operation );
			}
			return operations;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
		
	public OperationBean getOperation( Connection c, String opName ) throws DAOException {
		try {			
			PreparedStatement ps = c.prepareStatement( 				
				"select op.id, ov.visibility " +
				"from visibility ov inner join operation op on ov.id=op.visibility_id " +
				"where op.name=? " +
				"limit 1"
			);
			ps.setString( 1, opName ); 
			ResultSet rs = ps.executeQuery();
			if( rs.next() ) {
				OperationBean operation = new OperationBean();				
				operation.setId( rs.getInt( 1 ) );
				operation.setName( opName );
				operation.setVisibility( rs.getString( 2 ) );
				return operation;
			}
			return null;
		} catch( SQLException e ) {
			throw new DAOException( e );
		}
	}
	
}
