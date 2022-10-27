package convari.security;

import italo.codec.Hex;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class Encrypter {

	public String encrypt( String data, String publicKey ) throws EncryptException {
		try {
			byte[] pkey = Hex.decode( publicKey );									
			X509EncodedKeySpec pkSpec = new X509EncodedKeySpec( pkey );
			KeyFactory factory = KeyFactory.getInstance( "RSA" );
			PublicKey key = factory.generatePublic( pkSpec );
			Cipher cipher = Cipher.getInstance( "RSA" );	
			cipher.init( Cipher.ENCRYPT_MODE, key );
			return Hex.encode( cipher.doFinal( data.getBytes() ) );
		} catch (InvalidKeyException e) {
			throw new EncryptException( e );
		} catch (IllegalBlockSizeException e) {
			throw new EncryptException( e );
		} catch (BadPaddingException e) {
			throw new EncryptException( e );
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptException( e );
		} catch (NoSuchPaddingException e) {
			throw new EncryptException( e );
		} catch (InvalidKeySpecException e) {
			throw new EncryptException( e );
		}						
	}
	
}
