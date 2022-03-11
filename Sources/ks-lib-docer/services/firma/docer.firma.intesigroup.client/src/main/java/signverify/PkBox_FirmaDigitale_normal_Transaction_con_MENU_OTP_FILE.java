package signverify;

import it.pkbox.client.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SignatureException;

public class PkBox_FirmaDigitale_normal_Transaction_con_MENU_OTP_FILE
{
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		try	
		{

			System.out.println("Stai usando la versione delle API Java del PkBox: " + Version.getVersion());
			System.out.print("Inserire il percorso completo del file da firmare: ");
		    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		    String file_da_firmare = in.readLine();
		    File f = new File(file_da_firmare);
		    
			System.out.print("Inserire il percorso completo del certificato SecurePIN: ");
		    BufferedReader secpin = new BufferedReader(new InputStreamReader(System.in));
		    String secure_pin = secpin.readLine();
		    File f2 = new File(secure_pin);
		    if (!f2.exists())
		    {
		    	System.out.println("File non trovato.");
		    	System.exit(0);
		    }

		    if(f.exists()){
			
				long file_size = f.length();
				
				System.out.println("La dimensione del file è di: " + file_size + " byte");
				
				PKBox pbox = new PKBox();
				String url = "http://192.168.0.39:8080/pkserver/servlet/defaulthandler";
				pbox.addServer(url, null, null, null);
				pbox.setTimeout(3600);
				pbox.setSecurePINCert(secure_pin);

				// Initialize Envelope Object
				Envelope enve = new Envelope(pbox);
				// Initialize Utils object
				Utils utils_PkBox = new Utils(pbox);

			    System.out.print("Inserire l'ALIAS corretto del firmatario: ");
			    BufferedReader alias = new BufferedReader(new InputStreamReader(System.in));
			    String alias_name = alias.readLine();
			    
			    System.out.print("Inserire il PIN corretto del firmatario: ");
			    BufferedReader pin = new BufferedReader(new InputStreamReader(System.in));
			    String pin_num = pin.readLine();
			    
			    System.out.print("Inserire il valore di OTP del firmatario: ");
			    BufferedReader otp = new BufferedReader(new InputStreamReader(System.in));
			    String signerPin = otp.readLine();
			    
				System.out.println("NOME FILE DA FIRMARE >> " + file_da_firmare);
				System.out.println("");

               	byte[] document; 
            	byte[] outdocument;
            	byte[] encrypted;
            	InputStream ins;
				File fileIn = new File(file_da_firmare);
				
            	document = new byte[(int)fileIn.length()];
            	ins = new FileInputStream(fileIn);
            	ins.read(document);
            	ins.close();

            	// Authentication data
            	String authData = null;
            	String[] encCerts = {alias_name};

            	encrypted = enve.cipher(document, encCerts, Envelope.derEncoding, "Intesi");

            	authData = enve.startTransaction("Intesi", alias_name, pin_num, signerPin);

            	outdocument = enve.sign(document, "Intesi", alias_name, pin_num, authData, Envelope.implicitMode, Envelope.derEncoding, null);

            	outdocument = enve.addsign(null, outdocument, "Intesi", alias_name, pin_num, authData, Envelope.derEncoding, null);

            	outdocument = enve.countersign(null, outdocument, null, "Intesi", alias_name, pin_num, authData, Envelope.derEncoding, null);
            	
            	enve.decipher(encrypted, alias_name, pin_num, authData, "Intesi");

            	byte[] digest = utils_PkBox.digest("data".getBytes("UTF-8"), Utils.sha256, Utils.rawBinaryEncoding);
            	
            	utils_PkBox.rawsign(digest, "Intesi", null, alias_name, pin_num, authData, Utils.rsaPkcs1_15, Utils.rawBinaryEncoding);
            	
            	byte[][] digests = {digest, digest};
            	utils_PkBox.multirawsign(digests, "Intesi", null, alias_name, pin_num, authData, Utils.rsaPkcs1_15, Utils.rawBinaryEncoding);

            	digest = utils_PkBox.digest("data".getBytes("UTF-8"), Utils.sha256, Utils.derEncoding);
            	
            	byte[] signedDigest = enve.signdigest(digest, "Intesi", alias_name, pin_num, authData, Utils.derEncoding, null);
            	
            	digests[0] = digest;
            	digests[1] = digest;
            	
            	byte[][] signeddigests = enve.multisigndigest(digests, "Intesi", alias_name, pin_num, authData, Utils.derEncoding, null);
            	
            	byte[] merged = enve.merge("data".getBytes("UTF-8"), signeddigests[0], Envelope.derEncoding);
            	
            	System.out.println();

            	VerifyInfo vi = null;
            	vi = enve.verify("data".getBytes("UTF-8"), signedDigest, null);
            	System.out.println("Detached signature");
            	verifyIntegrity (vi);
            	System.out.println(vi);
            	System.out.println();

            	vi = enve.verify(null, merged, null);
            	System.out.println("Merged signature");
            	verifyIntegrity (vi);
            	System.out.println(vi);
            	System.out.println();
		    }
		    else{
		    	System.out.println("File non trovato.");
		    	System.exit(0);
		    }
		}
		catch(Exception e)
		{e.printStackTrace();}
		System.out.println("Execution done.");
	}
	
    public static void verifyIntegrity (VerifyInfo vi) throws SignatureException
    {
    	if (vi.getInvalidSignCount() > 0)
    		throw new SignatureException("Invalid signer count: " + vi.getInvalidSignCount());
    	
    	for (int index = 0; index < vi.getSignerCount(); index++)
    		verifyIntegrity (vi.getSigner(index));
    }

    public static void verifyIntegrity (Signer signer) throws SignatureException
    {
    	if (signer.getInvalidSignCount() > 0)
    		throw new SignatureException("Invalid counter signer count: " + signer.getInvalidSignCount());
    	
    	for (int index = 0; index < signer.getSignerCount(); index++)
    		verifyIntegrity (signer.getSigner(index));
    }

}
