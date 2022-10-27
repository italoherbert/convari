package italo.codec;

public class Hex {

	private final static String HEX_DIGITS = "0123456789abcdef";
	
	public static String encode( byte[] data ) {		
		String code = "";
		for( byte b:data ) {
			int intcode = (b + 256) % 256;
			int high = intcode >> 4;
			int low = intcode & 0x0F;
			code += HEX_DIGITS.charAt( high );
			code += HEX_DIGITS.charAt( low );	 		
		}
		return code;
	}
	
	public static byte[] decode( String code ) {		
		byte[] data = new byte[ code.length()/2];
		for( int i = 0; i < data.length; i++ ) {
			int high = Character.digit( code.charAt( i * 2 ), 16 );
			int low = Character.digit( code.charAt( ( i * 2 ) + 1 ), 16 );
			int intcode = ( high << 4 ) | low;
			if( intcode > 127 )
				intcode -= 256;
			data[i] = (byte)intcode;
		}
		return data;
	}
	
}
