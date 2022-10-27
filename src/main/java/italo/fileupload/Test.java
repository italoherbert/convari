package italo.fileupload;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		
		File imageFile = new File( "E:/PostLink/images/image.jpg" );
		File outputImageFile = new File( "E:/PostLink/images/image2.jpg" );
		
		BufferedImage image = new BufferedImage( 100, 100, BufferedImage.TYPE_INT_RGB );
		Graphics g = image.getGraphics();
		g.setColor( Color.RED );
		g.fillRect( 10, 10, 80, 80 );
		ImageIO.write( image, "jpg", imageFile );
		
		OutputStream output = new FileOutputStream( outputImageFile );		
		
		InputStream in = new FileInputStream( imageFile );
		byte[] buffer = new byte[ 2048 ];
		int len = in.read( buffer, 0, buffer.length );
		while( len > -1 ) { 
			output.write( buffer, 0, len );
			len = in.read( buffer, 0, buffer.length );
		}
		output.close();
		in.close();
	}

}
