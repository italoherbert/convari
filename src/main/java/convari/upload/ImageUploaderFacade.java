package convari.upload;

import italo.graphic.GraphicUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.servlet.ServletContext;



public class ImageUploaderFacade implements ImageUploader {

	private String imageDirPath;
	private GraphicUtil graphicUtil = new GraphicUtil();
	
	public ImageUploaderFacade(String imageDirPath) {
		super();
		this.imageDirPath = imageDirPath;
	}
	
	public void process( ImageUploaderVO vo ) throws IOException, ImageUploaderCBException {
		ServletContext context = vo.getServletContext();
		String contentType = vo.getContentType();
		InputStream inputStream = vo.getInputStream();
		int userId = vo.getUserId();
		final ImageUploaderCallback callback = vo.getCallback();
		
		String suffix = this.getInputFormatType( contentType );
		final String outputFormat = this.outputFormatType( contentType ); 

		Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix( suffix );
		ImageReader reader = iterator.next();
		reader.setInput( ImageIO.createImageInputStream( inputStream ), false );
		reader.addIIOReadProgressListener( new IIOReadProgressListener() {
			
			public void thumbnailStarted(ImageReader reader, int arg1, int arg2) {}			
			public void thumbnailProgress(ImageReader reader, float p) {}			
			public void thumbnailComplete(ImageReader reader) {}			
			public void sequenceStarted(ImageReader reader, int index) {}			
			public void sequenceComplete(ImageReader reader) {}
			
			public void readAborted(ImageReader reader) {
				reader.dispose();
				callback.canceled();				
			}
			
			public void imageStarted(ImageReader reader, int index) {
				callback.started();
			}
			
			public void imageProgress(ImageReader reader, float p) {
				if( callback.cancelCondition() )
					reader.abort();
				else callback.progress( p );				
			}
			
			public void imageComplete(ImageReader reader) {
				callback.finishing();								
			}
		} );							
		

		String outputFilePath = String.valueOf( userId )+"."+outputFormat;
		File destFile = new File( context.getRealPath( imageDirPath ), outputFilePath );		
		if( destFile.exists() )
			destFile.delete();
		try {
			BufferedImage originalImage = reader.read( 0 );
			BufferedImage resizedImage = graphicUtil.resizeWidth2( originalImage, 150, 1 );									
			ImageIO.write( resizedImage, outputFormat, new FileOutputStream( destFile ) );
			
			callback.finished( imageDirPath + outputFilePath );
		} catch( IOException e ) {
			throw new ImageUploaderCBException( e );
		}
		
	}
	
	public String getDefaultImagePath() {
		return imageDirPath + "default.jpg";
	}

	public boolean isSupportedType( String type ) {
		for( String t:SUPPORTED_TYPES )
			if( type.equals( t ) )
				return true;		
		return false;										
	}		
	
	public String outputFormatType( String contentType ) {
		String newType = contentType;
		if( newType != null ) {
			newType = this.getInputFormatType( contentType ); 
			if( newType.equals( "gif" ) )
				newType = "jpg";
		}
		return newType;
	}
	
	public String getInputFormatType( String contentType ) {
		String newContentType = contentType.replaceFirst( "[a-zA-Z0-9]+[/]", "" );
		if( newContentType.equals( "pjpeg" ) || newContentType.equals( "jpeg" ) )
			newContentType = "jpg";
		return newContentType;
	}
		
	public String getFileRelativePath( String contentType, int userId ) {
		String ext = contentType.replaceFirst( "[a-zA-Z0-9]+[/]", "" );
		return String.valueOf( userId )+"."+ext;
	}			
	
}

/*

File tempFile = new File( context.getRealPath( tempDirPath ), filePath );
		File destFile = new File( context.getRealPath( imageDirPath ), filePath );
		if( destFile.exists() )
			destFile.delete();
				
		OutputStream out = new FileOutputStream( tempFile );	
		
if( !callback.cancelCondition() )
			callback.started();
				
		int pack = (int)(size*.01f);
		byte[] buffer = new byte[ pack ];
		int len = in.read( buffer );
		long bytesReaded = len;
		boolean cancel = callback.cancelCondition();
		while( !cancel && len > -1 ) {
			out.write( buffer, 0, len );
			callback.progress( size, bytesReaded );
			len = in.read( buffer );
			bytesReaded += len;
			cancel = callback.cancelCondition();
		}
		in.close();
		out.close();
		
		if( cancel ) {
			if( tempFile.exists() )
				tempFile.delete();
			callback.canceled();
		} else {
			callback.finishing();
			InputStream tempIn = new FileInputStream( tempFile );
			out = new FileOutputStream( destFile );
			len = tempIn.read( buffer );
			while( len > -1 ) {
				out.write( buffer, 0, len );
				len = tempIn.read( buffer );
			}
			tempIn.close();
			out.close();
			
			if( tempFile.exists() )
				tempFile.delete();
			callback.finished( imageDirPath + filePath );
		}

*/