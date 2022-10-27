package convari.persistence;

import italo.persistence.find.ControlDatesFindBean;
import italo.persistence.find.parameters.TopicFindParameters;

import java.sql.Timestamp;
import java.util.List;

import convari.persistence.bean.PostBean;
import convari.persistence.bean.TopicBean;
import convari.persistence.bean.TopicOfUserBean;


public interface PostManager {

	public TopicBean createTopic();
	
	public PostBean createPost();

	public ControlDatesFindBean findControlDatesForTopics(  TopicFindParameters find ) throws PersistenceException;	
	
	public List<TopicBean> findTopics( TopicFindParameters find ) throws PersistenceException;

	public TopicBean findTopic( int id ) throws PersistenceException;
	
	public PostBean findPost( int id ) throws PersistenceException;
	
	public List<TopicBean> listTopics() throws PersistenceException; 
	
	public String getVisibilityForTopic( int topicId ) throws PersistenceException;
	
	public List<TopicOfUserBean> getPostsCountByTopics( int userId ) throws PersistenceException;
	
	public List<TopicBean> getTopicsByUser( int userId ) throws PersistenceException;
	
	public List<TopicBean> getTopicsByUser( int userId, Timestamp controlDate ) throws PersistenceException;
	
	public List<PostBean> getPostsByTopic( int topicId ) throws PersistenceException;
		
	public List<PostBean> getPostsByTopic( int topicId, Timestamp controlDate ) throws PersistenceException;
	
	public int addTopic( TopicBean topic, int userId ) throws PersistenceException;

	public void addPost( PostBean post, int userId, int topicId ) throws PersistenceException;
	
}
