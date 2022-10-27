package convari.upload;


public interface ImageUploaderCallback {
	
	public void started();
	
	public void progress(  float percent  );
	
	public void finishing();
	
	public void finished( String imagePath ) throws ImageUploaderCBException;
	
	public void canceled();
	
	public boolean cancelCondition();
	
}
