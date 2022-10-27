package convari.security;

public interface Security {

	public String genMAC( String message ) throws SecurityException;
	
	public Keys genKeys() throws SecurityException;
	
	public String encrypt( String data, String publicKey ) throws SecurityException;
	
	public String decrypt( String cdata, String privateKey ) throws SecurityException;
		
}
