package convari.persistence.bo;

import italo.persistence.db.ConnectionDBManager;
import italo.persistence.db.DBManagerException;

import java.sql.Connection;

import convari.persistence.MenuManager;
import convari.persistence.PersistenceException;
import convari.persistence.bean.MenuBean;
import convari.persistence.dao.DAOException;
import convari.persistence.dao.MenuDAO;


public class MenuBO implements MenuManager {

	private ConnectionDBManager manager;
	private MenuDAO menuDAO;
	
	public MenuBO(ConnectionDBManager manager, MenuDAO menuDAO) {
		super();
		this.manager = manager;
		this.menuDAO = menuDAO;
	}
	
	public MenuBean findCounts( int userId, boolean includeTopicsCount ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return menuDAO.findCounts( c, userId, includeTopicsCount );
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
