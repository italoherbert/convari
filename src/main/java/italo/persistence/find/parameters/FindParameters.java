package italo.persistence.find.parameters;

import java.sql.Timestamp;


public class FindParameters {
	
	public final static int INT_NULL = -1;
	
	private int pagesCount = INT_NULL;
	private Timestamp controlDate = null;
	private int limit = INT_NULL;

	public Timestamp getControlDate() {
		return controlDate;
	}

	public void setControlDate(Timestamp controlDate) {
		this.controlDate = controlDate;
	}

	public int getPagesCount() {
		return pagesCount;
	}

	public void setPagesCount(int pagesCount) {
		this.pagesCount = pagesCount;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
