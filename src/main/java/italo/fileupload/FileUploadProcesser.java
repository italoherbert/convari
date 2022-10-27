package italo.fileupload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class FileUploadProcesser {
	
	public void process( HttpServletRequest request, String outputDirPath, String outputFileName ) throws IOException {
		int fileSize = request.getContentLength();
		String contentDisposition = "";
		String paramName = "";
		String fileName = "";		
		String fileType = "";
		
		InputStream in = request.getInputStream();
		BufferedReader input = new BufferedReader( new InputStreamReader( in ) );
		
		String line1 = input.readLine();
		String line2 = input.readLine();
		String line3 = input.readLine();
		line1 = line1.trim();
		
		line2 += ";" + line3;
		
		input.readLine();
		
		String[] pars = line2.split( ";" );
		for( String param:pars ) {
			Pattern p = Pattern.compile( "[=:]" );
			String[] map = p.split( param );
			String pname = map[0].trim();
			String pvalue = map[1].trim().replace( "\"", "" ); 
			if( pname.equals( "Content-Disposition" ) ) {
				contentDisposition = pvalue;
			} else if( pname.equals( "name" ) ) {
				paramName = pvalue;
			} else if( pname.equals("filename") ) {
				fileName = pvalue;
			} else if( pname.equals( "Content-Type" ) ) {
				fileType = pvalue;
			} else {
				System.err.println( pname+"="+pvalue );
			}
		}
					
		String outputExt = fileType.replaceFirst( "[a-zA-Z0-9]+[/]", "" );
		if( outputExt.equals( "jpeg" ) )
			outputExt = "jpg";
		outputExt = "."+outputExt;
				
		File outputFile = new File( outputDirPath + outputFileName + outputExt );
		
		OutputStream out = new FileOutputStream( outputFile );
		int length = 0;
		byte[] buffer = new byte[ 2048 ];
		int len = in.read( buffer, 0, buffer.length );
		while( len > -1 && length <= fileSize ) {
			out.write( buffer, 0, len );
			length += len;
			len = in.read( buffer, 0, buffer.length );
		}
		out.close();
		
		System.out.println( "File name: "+ fileName );
		System.out.println( "File size: "+ fileSize );
		System.out.println( "File type: "+ fileType + "; Ext: "+outputExt );
		System.out.println( "Param: "+ paramName );
		System.out.println( "Content Disposition: "+contentDisposition );
		
	}
	
}
