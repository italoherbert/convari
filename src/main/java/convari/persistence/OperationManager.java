package convari.persistence;

import java.util.List;

import convari.persistence.bean.OperationBean;


public interface OperationManager {
		
	public List<OperationBean> list() throws PersistenceException;
	
	public List<OperationBean> listByVisibility( String visibility ) throws PersistenceException;

	public List<OperationBean> getOperationsByUser( int uid ) throws PersistenceException;
	
	public void setConstraintToOperation( int uid, String opName, String visibility ) throws PersistenceException;

	public void removeConstraintsForUser( int uid ) throws PersistenceException;

	public String getConstraintOperationVisibility( int uid, String opName ) throws PersistenceException;
	
	public String getOperationVisibility( String opName ) throws PersistenceException;
	
	public String getOperationVisibility( int uid, String opName ) throws PersistenceException;
	
}
