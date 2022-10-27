package convari.security;

import italo.codec.Hex;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;


public class KeyGen {
	
	public String genMAC( String message ) throws KeyGenException {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance( "RSA" );
			generator.initialize( 512 );
									
			KeyGenerator keygen = KeyGenerator.getInstance( "HmacMD5" );
			SecretKey secretKey = keygen.generateKey();
			
			Mac mac = Mac.getInstance( keygen.getAlgorithm() );
			mac.init( secretKey );
			byte[] authCode = mac.doFinal( message.getBytes() );
			
			return Hex.encode( authCode );			
		} catch (NoSuchAlgorithmException e) {
			throw new KeyGenException( e );
		} catch (InvalidKeyException e) {
			throw new KeyGenException( e );
		}
	}
	
	public Keys genKeys() throws KeyGenException {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance( "RSA" );
			generator.initialize( 1024 );
			
			KeyPair pair = generator.generateKeyPair();
			PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();
						
			KeyGenerator keygen = KeyGenerator.getInstance( "HmacSHA1" );
			SecretKey secretKey = keygen.generateKey();
			
			Mac mac = Mac.getInstance( keygen.getAlgorithm() );
			mac.init( secretKey );
			String macMessage = new Date().toString();
			byte[] authCode = mac.doFinal( macMessage.getBytes() );
			
			Keys keys = new Keys();
			keys.setPrivateKey( Hex.encode( privateKey.getEncoded() ) );
			keys.setPublicKey( Hex.encode( publicKey.getEncoded() ) );
			keys.setAuthCode( Hex.encode( authCode ) );			
			return keys;
		} catch (NoSuchAlgorithmException e) {
			throw new KeyGenException( e );
		} catch (InvalidKeyException e) {
			throw new KeyGenException( e );
		}
	}
	
}
