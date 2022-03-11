package it.kdm.docer.conservazione.provider.parer;

import it.kdm.docer.conservazione.ConservazioneException;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;

public class EntePasswordManager {
	private final byte[] key = new byte[] { 54, 45, 44, -82, -123, 126, -88,
			87, 10, -96, -10, 106, 70, 12, 14, -37 };

	private SecretKeySpec skeySpec;
	private Cipher cipher;

	private Map<String, String[]> usersMap;

	public EntePasswordManager(File usersFile) throws IOException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		this.skeySpec = new SecretKeySpec(this.key, "AES");
		this.cipher = Cipher.getInstance("AES");
		
		if(usersFile.exists()) {
			this.usersMap = parseUsersFile(usersFile);
		} else {
			this.usersMap = new HashMap<String, String[]>();
		}
	}

	private Map<String, String[]> parseUsersFile(File usersFile)
			throws IOException {
		List<String> lines = FileUtils.readLines(usersFile);

		Map<String, String[]> usersMap = new HashMap<String, String[]>();

		boolean dirtyFile = false;

		for (String line : lines) {
			int separatorIndex = line.indexOf("=");
			String ente = line.substring(0, separatorIndex);
			String encodedCredentials = line.substring(separatorIndex+1);
			String decodedCredentials;
			try {
				decodedCredentials = this.decrypt(encodedCredentials);
			} catch (Exception ex) {
				decodedCredentials = encodedCredentials;
				dirtyFile = true;
			}

			String[] credentials = decodedCredentials.split("\\|");
			if (credentials.length != 2) {
				throw new IOException("Error: usersfile is malformed: "
						+ usersFile.getAbsolutePath());
			}
			usersMap.put(ente, credentials);
		}

		if (dirtyFile) {
			updateUsersFile(usersMap, usersFile);
		}

		return usersMap;
	}

	private void updateUsersFile(Map<String, String[]> usersMap, File usersFile) {
		List<String> lines = new ArrayList<String>();

		try {
			StringBuilder builder = new StringBuilder();

			for (String ente : usersMap.keySet()) {
				String[] credentials = usersMap.get(ente);

				builder.append(credentials[0]);
				builder.append("|");
				builder.append(credentials[1]);

				String encodedCredentials = this.encrypt(builder.toString());

				builder.delete(0, builder.length());

				builder.append(ente);
				builder.append("=");
				builder.append(encodedCredentials);

				lines.add(builder.toString());

				builder.delete(0, builder.length());

			}

			FileUtils.writeLines(usersFile, lines);

		} catch (Exception e) {
		}
	}

	String toHex(byte[] data) {
		StringBuffer strBuf = new StringBuffer();
		for (byte b : data) {
			if (((int) b & 0xff) < 0x10)
				strBuf.append("0");
			strBuf.append(Integer.toString((b & 0xff), 16));
		}

		return strBuf.toString();
	}

	byte[] fromHex(String s) {

		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	private String encrypt(String text) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {

		byte[] input = text.getBytes();
		byte[] output = runCipher(Cipher.ENCRYPT_MODE, input);

		return toHex(output);
	}

	private String decrypt(String text) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {

		byte[] input = fromHex(text);
		byte[] output = runCipher(Cipher.DECRYPT_MODE, input);
		return new String(output);
	}

	byte[] runCipher(int mode, byte[] input) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		this.cipher.init(mode, this.skeySpec);

		return this.cipher.doFinal(input);
	}

	public String getUsername(String ente, String aoo) throws ConservazioneException {
		return this.getValue(ente, aoo)[0];
	}

	public String getPassword(String ente, String aoo) throws ConservazioneException {
		return this.getValue(ente, aoo)[1];
	}
	
	private String[] getValue(String ente, String aoo) throws ConservazioneException {
		String key = ente+"."+aoo;
		if(this.usersMap.containsKey(key)) {
			return this.usersMap.get(key);
		} else if(this.usersMap.containsKey(ente)) {
			return this.usersMap.get(ente);
		} else {
			ConservazioneException ex = new ConservazioneException(
					"L'ente " + ente + " non e' abilitato.");
			ex.fillInStackTrace();
			throw ex;
		}
	}
	
}