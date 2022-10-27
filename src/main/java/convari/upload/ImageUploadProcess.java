package convari.upload;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import convari.response.ResponseMessage;
import convari.response.ResponseBuilder;


public class ImageUploadProcess {

	public enum Status {
		READY, CANCELING, FINISHING, PROCESSING, FINISHED, CANCELED, FAILED
	}

	private int progress = 0;
	private Timestamp date;
	private Status status = Status.READY;
	
	private List<ResponseMessage> messageKeys = new ArrayList<ResponseMessage>();

	public synchronized void processMessageKeys( ResponseBuilder responseBuilder ) {
		for( ResponseMessage message:messageKeys ) {
			if( message.getType().equals( ResponseMessage.ERROR_TYPE ) )
				responseBuilder.processKeyErrorMSG( message.getKey(), message.getParams() );
			else if( message.getType().equals( ResponseMessage.INFO_TYPE ) )
				responseBuilder.processKeyInfoMSG( message.getKey(), message.getParams() );
		}
		messageKeys.clear();
	}
			
	public synchronized void addInfoMessageKey( String key, String... params ) {
		messageKeys.add( new ResponseMessage( ResponseMessage.INFO_TYPE, null, key, params ) );
	}
	
	public synchronized void addErrorMessageKey( String key, String... params ) {
		messageKeys.add(  new ResponseMessage( ResponseMessage.ERROR_TYPE, null, key, params )  );
	}
		
	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}
