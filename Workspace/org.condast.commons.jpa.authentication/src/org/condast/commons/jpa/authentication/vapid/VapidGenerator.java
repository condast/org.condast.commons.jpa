package org.condast.commons.jpa.authentication.vapid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Base64.Encoder;

/**
 * Create a VAPID key
 * @see: https://stackoverflow.com/questions/44149608/generate-a-vapid-keys-in-java-and-pass-them-to-javascript-pushmanager
 * @See: https://golb.hplar.ch/2019/08/webpush-java.html
 * @author Kees
 *
 */
public class VapidGenerator {

	private static final String S_EC = "DH";
	//private static final String S_04 = "04";
	//private static final String S_PARAM_SPEC ="secp256r1";
	private static final String S_ERR_INVALID_PATH ="The folder specified for this path does not exist: ";

	public enum Keys{
		PUBLIC,
		PRIVATE;

		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}
	private KeyPair keyPair;
	private byte[] publicKeyUncompressed;

	private String privateKeyBase64;
	private String publicKeyBase64;

	public VapidGenerator() {
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public byte[] getPublicKeyUncompressed() {
		return publicKeyUncompressed;
	}

	public String getPublicKeyBase64() {
		return publicKeyBase64;
	}


	public void initKeys( String path ) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, IOException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance( S_EC);
		//ECGenParameterSpec spec = new ECGenParameterSpec( S_PARAM_SPEC);
		//keyPairGenerator.initialize( spec, new SecureRandom());
		keyPairGenerator.initialize( 1024 );
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		PublicKey publicKey = keyPair.getPublic();
		/*
		ECPoint ecp = publicKey.getW();

		byte[] x = ecp.getAffineX().toByteArray();
		byte[] y = ecp.getAffineY().toByteArray();
		// Convert 04 to bytes
		String s= S_04;
		int len = s.length();
		byte[] firstBit = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
		{
		    firstBit[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) +
		Character.digit(s.charAt(i+1), 16));
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		outputStream.write(firstBit);
		outputStream.write(x);
		outputStream.write(y);

		publicKeyUncompressed = outputStream.toByteArray( );
*/
		Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode(publicKey.getEncoded());
		publicKeyBase64 = encoder.encodeToString(encodedBytes);

		encodedBytes = encoder.encode(keyPair.getPrivate().getEncoded());
		privateKeyBase64 = encoder.encodeToString(encodedBytes);

		StringBuilder builder = new StringBuilder();
		builder.append(Keys.PUBLIC);
		builder.append("=");
		builder.append(publicKeyBase64);
		builder.append(Keys.PUBLIC);
		builder.append("\n");
		builder.append(privateKeyBase64);
		File file = new File( path );
		if( !file.getParentFile().exists())
			throw new IllegalArgumentException( S_ERR_INVALID_PATH + file.getAbsolutePath());
		try {
			Files.write(Paths.get(path), builder.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}