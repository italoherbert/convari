package convari;

import javax.swing.JApplet;

import convari.security.EncryptException;
import convari.security.Encrypter;

public class EncrypterBean extends JApplet {

	private static final long serialVersionUID = 1L;

	private String publicKey = "30819f300d06092a864886f70d010101050003818d0030818902818100a879c8cc765bc251b7405fc268645816f05c21767ae1ddbc2a58f4fd59daa9fd29c7a926b980d651423d1c86bd6ec499672bf6985ecd32fedcb9c79d3f69e547890a1c1ba88909328aa62e0ee72b70da3071dfe34ede3ca240ccd5fdc230fef1047f109cea909084fccb38688e9181faa0960e53a917113cc655b91b514fb9bd0203010001";
	private Encrypter encrypter = new Encrypter();

	public String encrypt(String data) {
		try {
			return encrypter.encrypt(data, publicKey);
		} catch (EncryptException e) {
			return null;
		}
	}

}
