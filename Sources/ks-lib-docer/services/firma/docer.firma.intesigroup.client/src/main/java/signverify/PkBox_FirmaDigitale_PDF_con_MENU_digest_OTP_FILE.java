package signverify;

import it.pkbox.client.Envelope;
import it.pkbox.client.PKBox;
import it.pkbox.client.Utils;
import it.pkbox.client.VerifyInfo;
import it.pkbox.client.Version;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

public class PkBox_FirmaDigitale_PDF_con_MENU_digest_OTP_FILE {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		try	
		{
			System.out.println("Stai usando la versione delle API Java del PkBox: " + Version.getVersion());
			System.out.print("Inserire il percorso completo del file PDF da firmare : ");
		    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		    String file_da_firmare = in.readLine();
		    File f = new File(file_da_firmare);
		    
		    String estensione_file = file_da_firmare.substring(((file_da_firmare.length())-3),file_da_firmare.length() );

		    if(f.exists() && estensione_file.toUpperCase().equals("PDF")){
			
				long file_size = f.length();
				
				System.out.println("La dimensione del file è di: " + file_size + " byte");
				
				PKBox pbox = new PKBox();
				String url = "http://192.168.0.219:8080/pkserver/servlet/defaulthandler";
				
				pbox.addServer(url, null, null, null);
				
				Envelope enve = new Envelope(pbox);
				Utils utils_PkBox = new Utils(pbox);
				
				Calendar c = Calendar.getInstance();
				Date data_corrente = c.getTime();
				
			    
			    System.out.print("Inserire l'ALIAS corretto del firmatario: ");
			    BufferedReader alias = new BufferedReader(new InputStreamReader(System.in));
			    String alias_name = alias.readLine();
			    
			    System.out.print("Inserire il PIN corretto del firmatario: ");
			    BufferedReader pin = new BufferedReader(new InputStreamReader(System.in));
			    String pin_num = pin.readLine();
			    
			    System.out.print("Inserire il OTP corretto del firmatario: ");
			    BufferedReader otp = new BufferedReader(new InputStreamReader(System.in));
			    String signerPin = otp.readLine();
			    
			    
				System.out.println("DATA CORRENTE: " + data_corrente);
				System.out.println("NOME FILE DA FIRMARE: " + file_da_firmare);
				
				
				System.out.println("");
				
				File fileIn = new File(file_da_firmare);
			//CREA IMPRONTA 
				byte[] digestInfo = utils_PkBox.pdfdigest(fileIn, null,null, "reason_01", "location_01", "contact_01",
						data_corrente, null, 0, 0, 0, 0, 3, 2);
				
			//FIRMA IMPRONTA 
				byte[] sign_digest = enve.signdigest(digestInfo, alias_name, pin_num, signerPin, 2,data_corrente);
				
				String nome_file_firmato = file_da_firmare.substring(0,((file_da_firmare.length())-4)) + "_sign.pdf";
				
				String file_temp = file_da_firmare.substring(0,((file_da_firmare.length())-4)) + "_temp_envelope.txt";
				
				FileOutputStream fos = new FileOutputStream(file_temp);
				fos.write(sign_digest);
				fos.close(); 
				
				File fileIN_stream_envelope = new File(file_temp);
				File fileOutput_Firmato = new File(nome_file_firmato);
				
				
                enve.pdfmerge(fileIn, fileIN_stream_envelope, null, null, "reason_01", "location_01", "contact_01",
                		      data_corrente, null, 0, 0, 0, 0, fileOutput_Firmato);
				
				File fileIN_envelope_Firmato = new File(nome_file_firmato);
				
				VerifyInfo vi = enve.pdfverify(fileIN_envelope_Firmato, null, null);
				
				System.out.println("File FIRMATO: " + nome_file_firmato);
				
				System.out.println("");
				System.out.println("Numero di firme apportate al documento: " + vi.getSignerCount() );
			 
			    System.out.println("Singer is " + vi.getSigner(0).getSubjectDN());
			    Date signTime = vi.getSigner(0).getSigningTime();
			    System.out.println("SigningTime is " + signTime);
				  
			    System.out.println("");
				
				//Delete file temporary
				File file_temp_delete = new File(file_temp);
				boolean success_temp = file_temp_delete.delete();
				if (!success_temp){
					System.out.println("Deletion failed: " + file_temp);
				}else{
					System.out.println("File: " + file_temp + " deleted.");
				}
		    }
		    else{	
		    	if(!estensione_file.toUpperCase().equals("PDF"))  
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
	
}
