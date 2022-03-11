package signverify;

import it.pkbox.client.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SignatureException;
import java.util.Date;

public class PkBox_FirmaDigitale_PDF_Transaction_con_MENU_OTP_FILE
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
		    
			String estensione_file = file_da_firmare.substring(((file_da_firmare.length()) - 3),file_da_firmare.length());

			System.out.print("Inserire il percorso completo del certificato SecurePIN: ");
		    BufferedReader secpin = new BufferedReader(new InputStreamReader(System.in));
		    String secure_pin = secpin.readLine();
		    File f2 = new File(secure_pin);
		    if (!f2.exists())
		    {
		    	System.out.println("File non trovato.");
		    	System.exit(0);
		    }

		    System.out.print("Inserire il percorso dell'immagine da utilizzare per la firma digitale: ");
		    BufferedReader inImage = new BufferedReader(new InputStreamReader(System.in));
		    String fname_image = inImage.readLine();
		    File f3 = new File(fname_image);
		    if (!f3.exists())
		    {
		    	System.out.println("File non trovato.");
		    	System.exit(0);
		    }

		    if(f.exists() && estensione_file.toUpperCase().equals("PDF")){
			
				long file_size = f.length();
				
				System.out.println("La dimensione del file � di: " + file_size + " byte");
				
			    System.out.println("Scegliere in quale pagina inserire l'immagine");	
			    System.out.println("Valori permessi: ");
			    System.out.println("                0 or 1 = first page ");
			    System.out.println("                < 0 = last page ");
			    System.out.println("                N = page number");
			    System.out.print("                SCELTA: ");
			    BufferedReader numPage = new BufferedReader(new InputStreamReader(System.in));
			    String numPg = numPage.readLine();
			    int nPg = Integer.parseInt(numPg);
			    System.out.println("");
			    
			    System.out.println("Inserire la posizione desiderata");
			    System.out.println("Valori permessi: ");
			    System.out.println("                1 = top left");
			    System.out.println("                2 = top");
			    System.out.println("                3 = top right");
			    System.out.println("                4 = central left");
			    System.out.println("                5 = central");
			    System.out.println("                6 = central right");
			    System.out.println("                7 = bottom left");
			    System.out.println("                8 = bottom");
			    System.out.println("                9 = bottom right");
			    System.out.print("                SCELTA: ");
			    BufferedReader numPosition = new BufferedReader(new InputStreamReader(System.in));
			    String numPosizione = numPosition.readLine();
			    int nPs = Integer.parseInt(numPosizione);
				System.out.println("");

				PKBox pbox = new PKBox();
				String url = "http://192.168.0.39:8080/pkserver/servlet/StampingAntohandler";
				pbox.addServer(url, null, null, null);
				pbox.setTimeout(3600);
				pbox.setSecurePINCert(secure_pin);

				// Initialize Envelope Object
				Envelope enve = new Envelope(pbox);

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
             	InputStream ins;
				File fileIn = new File(file_da_firmare);
				
            	document = new byte[(int)fileIn.length()];
            	ins = new FileInputStream(fileIn);
            	ins.read(document);
            	ins.close();

            	// Authentication data
            	String authData = null;

            	authData = enve.startTransaction("KDM User", alias_name, pin_num, signerPin);

            	FileInputStream imageS = new FileInputStream(fname_image);
        		byte[] image = new byte [imageS.available()];
            	imageS.read(image);
            	imageS.close();
            	
            	int[] accessPermissions = {Envelope.NOT_CERTIFIED, Envelope.NOT_CERTIFIED, Envelope.NOT_CERTIFIED, Envelope.NOT_CERTIFIED, Envelope.NOT_CERTIFIED};
            	String[] fieldNames = {"Firma", "<new>", "<new>", "<new>", "<new>"};
            	String[] sigLayout = {"", "", "", "<Text>%cn%</Text><FontFamily>Courier</FontFamily><FontSize>8</FontSize><TextAlign>1</TextAlign>", "<FontFamily>Courier</FontFamily><FontSize>8</FontSize><TextAlign>1</TextAlign><FontColor>0xFF0000</FontColor>"};
            	String[] reason = {"Reason 1", "Reason 2", "Reason 3", "Reason 4", "Reason 5"};
            	String[] location = {"Location 1", "Location 2", "Location 3", "Location 4", "Location 5"};
            	String[] contact = {"Contact 1", "Contact 2", "Contact 3", "Contact 4", "Contact 5"};
            	byte[][] images = {null, null, null, image, null};
            	int[] page = {0, nPg, 0, 0, 0};
            	int[] position = {0, nPs, 0, 9, 7};
            	int[] cx = {0, 0, 0, 200, 100};
            	int[] cy = {0, 0, 0, 100, 50};
            	
            	outdocument = enve.pdfsign(document, accessPermissions[0], fieldNames[0], sigLayout[0], reason[0], location[0], contact[0], "Intesi", alias_name, pin_num, authData, null, images[0], nPg, nPs, 0, 0, cx[0], cy[0]);

				String nome_file_firmato = file_da_firmare.substring(0,((file_da_firmare.length())-4)) + "_sign.pdf";
				FileOutputStream fos = new FileOutputStream(nome_file_firmato);
				fos.write(outdocument);
				fos.close(); 

				VerifyInfo vi = enve.pdfverify(outdocument, null, null);
				System.out.println("File FIRMATO: " + nome_file_firmato);
				
				System.out.println("");
				System.out.println("Numero di firme apportate al documento: " + vi.getSignerCount() );
			 
			    System.out.println("Singer is " + vi.getSigner(0).getSubjectDN());
			    Date signTime = vi.getSigner(0).getSigningTime();
			    System.out.println("SigningTime is " + signTime);
			    System.out.println("");

			    outdocument = enve.multipdfsign(document, accessPermissions, fieldNames, sigLayout, reason, location, contact, "Intesi", alias_name, pin_num, authData, null, images, page, position, null, null, cx, cy);

				String nome_file_firmato_multi = file_da_firmare.substring(0,((file_da_firmare.length())-4)) + "_msign.pdf";
				fos = new FileOutputStream(nome_file_firmato_multi);
				fos.write(outdocument);
				fos.close();
				
				vi = enve.pdfverify(outdocument, null, null);
				System.out.println("File FIRMATO: " + nome_file_firmato_multi);
				
				System.out.println("");
				System.out.println("Numero di firme apportate al documento: " + vi.getSignerCount() );
			 
            	verifyIntegrity (vi);
            	System.out.println(vi);
            	System.out.println();

		    }
		    else{
				if (f.exists() && !estensione_file.toUpperCase().equals("PDF"))
					System.out.println("Sono accettati solo file con estensione .PDF!!!");
				else
					System.out.println("File does not exists.");
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
