package italo.persistence.find.parameters;


public class TopicFindParameters extends FindParameters {

	private String tQuery = null;
	private String uQuery = null;
	private int userId = INT_NULL;
	private int visibilityWeight = INT_NULL;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTQuery() {
		return tQuery;
	}

	public void setTQuery(String tQuery) {
		this.tQuery = tQuery;
	}

	public String getUQuery() {
		return uQuery;
	}

	public void setUQuery(String uQuery) {
		this.uQuery = uQuery;
	}

	public int getVisibilityWeight() {
		return visibilityWeight;
	}

	public void setVisibilityWeight(int visibilityWeight) {
		this.visibilityWeight = visibilityWeight;
	}
	
}
