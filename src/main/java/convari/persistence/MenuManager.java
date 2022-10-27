package convari.persistence;

import convari.persistence.bean.MenuBean;

public interface MenuManager {

	public MenuBean findCounts( int userId, boolean includeTopicsCount ) throws PersistenceException;
	
}
