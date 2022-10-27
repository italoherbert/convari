package convari.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Test {

	public static void main(String[] args) throws SecurityException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		String pass = "italo";
		
		Security security = new SecurityFacade();
		System.out.println( security.genMAC( pass ) );

		Timestamp date = new Timestamp( System.currentTimeMillis() );
		
		SimpleDateFormat dayFormat = new SimpleDateFormat( "dd" );
		SimpleDateFormat weekFormat = new SimpleDateFormat( "MMMM" );
		SimpleDateFormat yearFormat = new SimpleDateFormat( "yyyy" );
		SimpleDateFormat hourFormat = new SimpleDateFormat( "hh:mm:ss" );

		String formattedDate = dayFormat.format( date );
		formattedDate += " de " + weekFormat.format( date );
		formattedDate += " de " + yearFormat.format( date );
		formattedDate += ", " + hourFormat.format( date );

		System.out.println( formattedDate );
		
		/*Keys keys = security.genKeys();
		
		String enc = security.encrypt( pass, keys.getPublicKey() );
		String dec = security.decrypt( enc, keys.getPrivateKey() );
		
		System.out.println( "PUBLIC KEY: \n"+keys.getPublicKey() );
		System.out.println( "\nPRIVATE KEY: \n"+keys.getPrivateKey() );
		System.out.println( "\nAUTH CODE: "+keys.getAuthCode() );
		System.out.println( "\nENC: "+enc );
		System.out.println( "PASS: "+ dec );*/
	}
	
}
