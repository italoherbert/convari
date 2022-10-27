package convari.upload;

import java.io.IOException;

public interface ImageUploader {

	public final static String PJPEG = "image/pjpeg";
	public final static String JPEG = "image/jpeg";
	public final static String JPG = "image/jpg";
	public final static String PNG = "image/png";
	public final static String GIF = "image/gif";

	public final static String[] SUPPORTED_TYPES = {
		PJPEG, JPEG, JPG, PNG, GIF
	};
	
	public boolean isSupportedType( String type );
	
	public String outputFormatType( String type );
			
	public String getDefaultImagePath();
	
	public void process( ImageUploaderVO vo ) throws IOException, ImageUploaderCBException;

}
