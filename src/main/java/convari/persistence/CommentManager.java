package convari.persistence;

import convari.persistence.bean.CommentBean;

public interface CommentManager {

	public CommentBean create();
	
	public void insert( CommentBean comment ) throws PersistenceException;
	
}
