package convari.security;

import italo.codec.Hex;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class Decrypter {

	public String decrypt( String cdata, String privateKey ) throws DecryptException {
		try {
			byte[] pkey = Hex.decode( privateKey );			
			PKCS8EncodedKeySpec pkSpec = new PKCS8EncodedKeySpec( pkey );
			KeyFactory factory = KeyFactory.getInstance( "RSA" );
			PrivateKey key = factory.generatePrivate( pkSpec );
			
			Cipher cipher = Cipher.getInstance( "RSA" );	
			cipher.init( Cipher.DECRYPT_MODE, key );			
			return new String( cipher.doFinal( Hex.decode( cdata ) ) );
		} catch (InvalidKeyException e) {
			throw new DecryptException( e );
		} catch (IllegalBlockSizeException e) {
			throw new DecryptException( e );
		} catch (BadPaddingException e) {
			throw new DecryptException( e );
		} catch (NoSuchAlgorithmException e) {
			throw new DecryptException( e );
		} catch (NoSuchPaddingException e) {
			throw new DecryptException( e );
		} catch (InvalidKeySpecException e) {
			throw new DecryptException( e );
		}		
	}	
	
}
