package convari.persistence.bo;

import italo.persistence.db.ConnectionDBManager;
import italo.persistence.db.DBManagerException;

import java.sql.Connection;

import convari.persistence.CommentManager;
import convari.persistence.PersistenceException;
import convari.persistence.bean.CommentBean;
import convari.persistence.dao.CommentDAO;
import convari.persistence.dao.DAOException;


public class CommentBO implements CommentManager {

	private ConnectionDBManager manager;
	private CommentDAO commentDAO;
	
	public CommentBO(ConnectionDBManager manager, CommentDAO commentDAO) {
		super();
		this.manager = manager;
		this.commentDAO = commentDAO;
	}
	
	public CommentBean create() {		
		return new CommentBean();
	}

	public void insert( CommentBean comment ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				commentDAO.insert( c, comment );
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
	
}
