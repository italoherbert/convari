package convari.persistence.bo;

import italo.persistence.db.ConnectionDBManager;
import italo.persistence.db.DBManagerException;
import italo.persistence.find.ControlDatesFindBean;
import italo.persistence.find.parameters.TopicFindParameters;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

import convari.persistence.PersistenceException;
import convari.persistence.PostManager;
import convari.persistence.bean.PostBean;
import convari.persistence.bean.TopicBean;
import convari.persistence.bean.TopicOfUserBean;
import convari.persistence.dao.DAOException;
import convari.persistence.dao.PostDAO;
import convari.persistence.dao.SQLFunctionsDAO;
import convari.persistence.dao.TopicDAO;


public class PostBO implements PostManager {

	private ConnectionDBManager manager;
	private TopicDAO topicDAO;
	private PostDAO postDAO;
	private SQLFunctionsDAO sqlFuncDAO;
		
	public PostBO( ConnectionDBManager manager, TopicDAO topicDAO, PostDAO postDAO, SQLFunctionsDAO sqlFuncDAO ) {
		this.manager = manager;
		this.topicDAO = topicDAO;
		this.postDAO = postDAO;
		this.sqlFuncDAO = sqlFuncDAO;
	} 
	
	public TopicBean createTopic() {
		TopicBean topic = new TopicBean();
		return topic;
	}

	public PostBean createPost() {
		PostBean post = new PostBean();
		return post;
	}
	
	public List<TopicBean> getTopicsByUser( int userId ) throws PersistenceException {
		return this.getTopicsByUser( userId, null );
	}

	public List<PostBean> getPostsByTopic( int topicId ) throws PersistenceException {
		return this.getPostsByTopic( topicId, null );
	}

	public int addTopic(TopicBean topic, int userId) throws PersistenceException {
		int tid = -1;
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				topicDAO.addTopic( c, topic, userId );
				tid = sqlFuncDAO.lastInsertId( c );
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
		return tid;
	}

	public void addPost(PostBean post, int userId, int topicId) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			manager.setAutoCommit( c, false );
			try {
				postDAO.addPost( c, post, userId, topicId );
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
	
	public List<TopicBean> listTopics() throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return topicDAO.list( c );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public List<TopicOfUserBean> getPostsCountByTopics( int userId ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return topicDAO.getPostsCountByTopics( c, userId );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public List<TopicBean> getTopicsByUser( int userId, Timestamp controlDate ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return topicDAO.getTopicsByUser( c, userId, controlDate );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public String getVisibilityForTopic( int topicId ) throws PersistenceException {
		try { 
			Connection c = manager.openConnection();
			try {
				return topicDAO.getVisibilityForTopic( c, topicId );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}

	public List<PostBean> getPostsByTopic( int topicId, Timestamp controlDate ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return postDAO.getPostsByTopic( c, topicId, controlDate );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public ControlDatesFindBean findControlDatesForTopics( TopicFindParameters findParameters ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return topicDAO.findControlDates( c, findParameters );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}	
	}

	public TopicBean findTopic( int id ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return topicDAO.find( c, id );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}		
	}
	
	public List<TopicBean> findTopics( TopicFindParameters find ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return topicDAO.find( c, find );
			} catch (DAOException e) {
				throw new PersistenceException( e );
			} finally {
				manager.closeConnection( c );
			}
		} catch( DBManagerException e ) {
			throw new PersistenceException( e );
		}
	}
	
	public PostBean findPost( int id ) throws PersistenceException {
		try {
			Connection c = manager.openConnection();
			try {
				return postDAO.find( c, id );
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
