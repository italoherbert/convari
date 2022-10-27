package convari.persistence.bo;

import italo.persistence.db.ConnectionDBManager;
import italo.persistence.db.DBManagerException;

import java.sql.Connection;
import java.util.List;

import convari.persistence.OperationManager;
import convari.persistence.PersistenceException;
import convari.persistence.bean.OperationBean;
import convari.persistence.dao.DAOException;
import convari.persistence.dao.OperationConstraintDAO;
import convari.persistence.dao.OperationDAO;
import convari.persistence.dao.VisibilityDAO;

 
public class OperationBO implements OperationManager {

	private OperationDAO operationDAO;
	private OperationConstraintDAO operationConstraintDAO;
	private VisibilityDAO visibilityDAO;
	private ConnectionDBManager manager;

	public OperationBO(ConnectionDBManager manager, OperationDAO resourceDAO, 
			OperationConstraintDAO operationConstraintDAO, VisibilityDAO visibilityDAO ) {
		super();
		this.manager = manager;
		this.operationDAO = resourceDAO;
		this.operationConstraintDAO = operationConstraintDAO;
		this.visibilityDAO = visibilityDAO;
	}
				
	public String getOperationVisibility(int uid, String opName) throws PersistenceException {
		String visibility = this.getConstraintOperationVisibility( uid, opName );
		if( visibility == null )
			visibility = this.getOperationVisibility( opName );
		return visibility;
	}

	public void setConstraintToOperation( int userId, String opName, String visibility ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				OperationBean operation = operationDAO.getOperation( c, opName );
				if( operation != null ) {
					int opId = operation.getId();
					if( operation.getVisibility().equals( visibility ) ) {
						operationConstraintDAO.deleteConstraint( c, userId, opId );
					} else {
						int visibilityId = visibilityDAO.findIdToVisibility( c, visibility );
						if( visibilityId > -1 ) {
							boolean added = operationConstraintDAO.addedConstraintToOperation( c, userId, opId );
							if( added ) {
								operationConstraintDAO.updateVisibilityConstraintToOperation(c, userId, opId, visibilityId );
							} else {
								operationConstraintDAO.addConstraintToOperation( c, userId, opId, visibilityId );
							}						
						} else {
							throw new PersistenceException( "Inconsist�ncia - addConstraint; Visibilidade="+visibility );
						}
					}
				} else {
					throw new PersistenceException( "Inconsist�ncia - addConstraint; Opera��o="+opName );
				}
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}
	
	public void removeConstraintsForUser( int userId ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				operationConstraintDAO.deleteConstraintsForUser( c, userId );
				manager.commit( c );
			} catch (DAOException e) {
				manager.rollback( c );
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}

	public String getOperationVisibility( String opName ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return operationDAO.getOperationVisibility( c, opName );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}		
	}
	
	public String getConstraintOperationVisibility( int uid, String opName ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return operationConstraintDAO.getVisibilityToOperation( c, uid, opName );				
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}		
	}
	
	public List<OperationBean> getOperationsByUser( int userId ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return operationDAO.getOperationsByUser( c, userId );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}		
	}
		
	public List<OperationBean> list() throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return operationDAO.list( c );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}		
	}
	
	public List<OperationBean> listByVisibility( String visibility ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return operationDAO.listByVisibility( c, visibility );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}		
	}
		
}
