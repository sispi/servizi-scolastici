package signverify;

import it.pkbox.client.*;

import java.io.*;
import java.util.Date;

public class PkBox_FirmaDigitale_normal_mult_directory {
	/**
	 * @param args
	 */
 public static void main(String[] args) {
  try
   {
	String versionePkBox = Version.getVersion();
	System.out.println("Stai usando la versione di PkBox: " + versionePkBox);
	System.out.print("Inserire il percorso della directory: ");
	BufferedReader directory = new BufferedReader(new InputStreamReader(System.in));
	String dir = directory.readLine();
	File file = new File(dir);
	String ultimo_carattere = dir.substring(((dir.length())-1),dir.length() );
	
			
	if(file != null && file.isDirectory()&& file.canRead()){
	
	        PKBox pbox = new PKBox();
		    String url = "http://localhost:8080/pkserver/servlet/defaulthandler";
		    pbox.addServer(url, null, null, null);

			String as[] = file.list();

			System.out.println("Numero di file presenti: " + as.length);
			
			String percorso_completo = null;
			FileInputStream fileIn;
			FileOutputStream fileOut;
			String docfile_Out = null;
			BufferedReader alias = null;
			String alias_name = null;
			BufferedReader pin = null;
			String pin_num = null;
			String docfileVerify = null;
			String docfile_delete = null;
			String docfile_Out1 = null;
			

			
			for(int i = 0; i < as.length; i++) {

				File fileN = new File(dir, as[i]);

				if(ultimo_carattere.equals("\\"))	
					percorso_completo = (dir + as[i]);
				else {
					percorso_completo = (dir + "\\" + as[i]);
				}
				String estensione_fileOriginale = percorso_completo.substring(percorso_completo.length()-3,percorso_completo.length());
				System.out.println("");
				System.out.println("FILE: " + percorso_completo + "  " + fileN.length() + " byte");
				
			    /*
				PKBox pbox = new PKBox();
			    String url = "http://localhost:8080/pkserver/servlet/defaulthandler";
			    pbox.addServer(url, null, null, null);
				*/
				
				Envelope enve = new Envelope(pbox);
				
				System.out.print("INSERIRE PIU' FIRMATARI? (S/N) ");
				BufferedReader firmatari = new BufferedReader(new InputStreamReader(System.in));
				String firm = firmatari.readLine();
			
				if(firm.toUpperCase().equals("S")){

					System.out.print("INSERIRE IL NUMERO DI FIRMATARI PER IL FILE " +as[i]+ ": ");
				    BufferedReader num_firmatari = new BufferedReader(new InputStreamReader(System.in));
				    String num_firm = num_firmatari.readLine();
				    int nFirm = Integer.parseInt(num_firm);
					
					fileIn = new FileInputStream(percorso_completo);
					docfile_Out = percorso_completo.substring(0,((percorso_completo.length())-4)) + ".p7m";
					fileOut = new FileOutputStream(docfile_Out);
	
					
					System.out.println("FIRMA 1: ");
					System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
				    alias = new BufferedReader(new InputStreamReader(System.in));
				    alias_name = alias.readLine();
				    
					System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
				    pin = new BufferedReader(new InputStreamReader(System.in));
				    pin_num = pin.readLine();

				    enve.sign(fileIn, fileIn.available(), alias_name, pin_num, Envelope.implicitMode, Envelope.base64Encoding, null,fileOut);
				    
				    System.out.println("FILE FIRMATO: " + docfile_Out);
				    System.out.println("");
				    
					fileIn.close();
					fileOut.close();
			 
					for(int j = 0; j<(nFirm-1); j++){
						fileIn = new FileInputStream(docfile_Out);
						docfile_Out1 = percorso_completo.substring(0,((percorso_completo.length())-4)) + "_"+ j + "_addsign.p7m";
						fileOut = new FileOutputStream(docfile_Out1);
						
						System.out.println("FIRMA " +(j+2)+ ": ");
						System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
					    alias = new BufferedReader(new InputStreamReader(System.in));
					    alias_name = alias.readLine();
					    
						System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
					    pin = new BufferedReader(new InputStreamReader(System.in));
					    pin_num = pin.readLine();
					    
					    enve.addsign(null, 0, fileIn, fileIn.available(), alias_name, pin_num, Envelope.base64Encoding, null, fileOut);
						System.out.println("FileName della firma aggiunta: " + docfile_Out1);
					
						docfile_delete = docfile_Out;
						docfile_Out = docfile_Out1;
						
						fileIn.close();
						fileOut.close();
						
						docfileVerify = percorso_completo.substring(0,((percorso_completo.length())-4)) + "_verify." + estensione_fileOriginale;
						
						//Delete temporary file 
						File f1 = new File(docfile_delete);
						boolean success = f1.delete();
						if (!success){
						      System.out.println("Deletion failed. " + docfile_delete);
						      //System.exit(0);
						}else{
						      System.out.println("File" +docfile_delete + " deleted.");
						      System.out.println("");
						}
						
					}
					FileInputStream fileInVerify = new FileInputStream(docfile_Out);
					FileInputStream fileOutEnve = new FileInputStream(docfile_Out1);
					
					FileOutputStream filedocfileNVerify = new FileOutputStream(docfileVerify);
					
					VerifyInfo vi = enve.verify (null, fileInVerify.available(), fileOutEnve, fileOutEnve.available(), null, filedocfileNVerify);
					System.out.println("Numero di firme apportate al documento : " + vi.getSignerCount() );
					
					int index = vi.getSignerCount();
					// LOOP THROUGH THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR SUBJECT DN
					  for(int j = 0; j < index; j++){
					    System.out.println("Singer " + (j+1) + " is " + vi.getSigner(j).getSubjectDN());
					    Date signTime = vi.getSigner(j).getSigningTime();
					    System.out.println("SigningTime " + (j+1) + " is " + signTime);
					}
				    
					fileOutEnve.close();
					filedocfileNVerify.close();
					fileInVerify.close();
					
					
					
					//Delete file verify original  
					File file_verify = new File(docfileVerify);
					boolean success_verify = file_verify.delete();
					if (!success_verify){
					      System.out.println("Deletion failed." + docfileVerify);
					      //System.exit(0);
					}else{
					      System.out.println("File" + docfileVerify + " deleted.");
					}
					
				}else {
					fileIn = new FileInputStream(percorso_completo);
					docfile_Out = percorso_completo.substring(0,((percorso_completo.length())-4)) + ".p7m";
					fileOut = new FileOutputStream(docfile_Out);
					
					System.out.println("FIRMA 1: ");
					System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
				    alias = new BufferedReader(new InputStreamReader(System.in));
				    alias_name = alias.readLine();
				    
					System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
				    pin = new BufferedReader(new InputStreamReader(System.in));
				    pin_num = pin.readLine();
				    
				    
				    enve.sign(fileIn, fileIn.available(), alias_name, pin_num, Envelope.implicitMode, Envelope.base64Encoding, null,fileOut);
					fileOut.close();
					
					FileInputStream fileOutEnve = new FileInputStream(docfile_Out);
					docfileVerify = percorso_completo.substring(0,((percorso_completo.length())-4)) + "_verify." + estensione_fileOriginale;
					FileOutputStream filedocfileNVerify = new FileOutputStream(docfileVerify);
					VerifyInfo vi = enve.verify (null,  fileIn.available(), fileOutEnve, fileOutEnve.available(), filedocfileNVerify);
				    System.out.println("FILE FIRMATO: " + docfile_Out);
				    System.out.println("");
				    
					System.out.println("Numero di firme apportate al documento: " + vi.getSignerCount() );
				
				
				  //THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR SUBJECT DN
				 
				    System.out.println("Singer is " + vi.getSigner(0).getSubjectDN());
				    Date signTime = vi.getSigner(0).getSigningTime();
				    System.out.println("SigningTime is " + signTime);
						
					fileOutEnve.close();
					//fileIn.close();
					filedocfileNVerify.close();
				    
					
					//Delete file verify original  
					File file_verify = new File(docfileVerify);
					boolean success_verify = file_verify.delete();
					if (!success_verify){
					      System.out.println("Deletion failed." + docfileVerify);
					      //System.exit(0);
					}else{
					      System.out.println("File" + docfileVerify + " deleted.");
					}
	
				}
				
				
			   }
			
			}
			else {
				if(!file.isDirectory())
					System.out.println("La directory non esiste. ");
			}
			
		}
		catch(Exception e)
		{e.printStackTrace();}

		System.out.println("Execution done.");
	}
}
