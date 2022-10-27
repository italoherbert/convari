package convari.security;

public class SecurityFacade implements Security {
			
	private Encrypter encrypter = new Encrypter();
	private Decrypter decrypter = new Decrypter();
	private KeyGen keyGen = new KeyGen();		
	
	public String genMAC( String message ) throws SecurityException {
		try {
			return keyGen.genMAC( message );
		} catch (KeyGenException e) {
			throw new SecurityException( e );
		}
	}
	
	public Keys genKeys() throws SecurityException {
		try {
			return keyGen.genKeys();
		} catch (KeyGenException e) {
			throw new SecurityException( e );
		}
	}

	public String encrypt( String data, String publicKey ) throws SecurityException {
		try {
			return encrypter.encrypt( data, publicKey );
		} catch (EncryptException e) {
			throw new SecurityException( e );
		}					
	}
	
	public String decrypt( String cdata, String privateKey ) throws SecurityException {
		try {
			return decrypter.decrypt( cdata, privateKey );
		} catch (DecryptException e) {
			throw new SecurityException( e );
		}
	}	
		
}

