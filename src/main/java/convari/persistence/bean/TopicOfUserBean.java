package convari.persistence.bean;

public class TopicOfUserBean extends TopicBean {
	
	private int postsCount = 0;

	public int getPostsCount() {
		return postsCount;
	}

	public void setPostsCount(int postsCount) {
		this.postsCount = postsCount;
	}
	
}
