package convari.persistence.bean;

import java.sql.Timestamp;

public class InvitationBean {

	public final static String ACCEPTED_INVITATION = "accepted";
	public final static String REFUSED_INVITATION = "refused";
	public final static String PENDING_INVITATION = "pending";
	public final static String CANCELED_INVITATION = "canceled";
	public final static String REMOVED_INVITATION = "removed";

	private int id;
	private int fromUserId;
	private int toUserId;
	private Timestamp sendDate;
	private Timestamp responseDate;
	private String status = PENDING_INVITATION;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	public int getToUserId() {
		return toUserId;
	}

	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getSendDate() {
		return sendDate;
	}

	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}

	public Timestamp getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Timestamp responseDate) {
		this.responseDate = responseDate;
	}

}