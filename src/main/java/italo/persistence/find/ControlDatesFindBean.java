package italo.persistence.find;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ControlDatesFindBean {
	
	private List<Timestamp> controlDates = new ArrayList<Timestamp>();
	private boolean hasMore = true;

	public List<Timestamp> getControlDates() {
		return controlDates;
	}

	public boolean getHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}
	
}
