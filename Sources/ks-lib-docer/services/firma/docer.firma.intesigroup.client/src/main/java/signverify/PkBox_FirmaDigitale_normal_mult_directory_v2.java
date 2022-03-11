package signverify;

import it.pkbox.client.*;

import java.io.*;
import java.util.Date;

public class PkBox_FirmaDigitale_normal_mult_directory_v2 {
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
			
	String as[] = file.list();
			
	String ultimo_carattere = dir.substring(((dir.length())-1),dir.length() );
			
	if(file != null && file.isDirectory()&& file.canRead()){
			
			System.out.print("Inserire il nome della directory dove andranno creati i file firmati: ");
			BufferedReader directory_new = new BufferedReader(new InputStreamReader(System.in));
			String dir_new = directory_new.readLine();
			String ultimo_carattere_new_dir = dir_new.substring(((dir_new.length())-1),dir_new.length() );
			
			String percorso_completo_new_dir = null;
			
			if(ultimo_carattere.equals("\\") && ultimo_carattere_new_dir.equals("\\"))	
				percorso_completo_new_dir = (dir + dir_new);
			
			if(ultimo_carattere.equals("\\")&& !ultimo_carattere_new_dir.equals("\\"))
				percorso_completo_new_dir = (dir + dir_new + "\\");
			
			if(!ultimo_carattere.equals("\\")&& !ultimo_carattere_new_dir.equals("\\"))
				percorso_completo_new_dir = (dir + "\\" + dir_new + "\\");
			
			if(!ultimo_carattere.equals("\\")&& ultimo_carattere_new_dir.equals("\\"))
				percorso_completo_new_dir = (dir + "\\" + dir_new);
			System.out.println();
		    boolean success_new_dir = (new File(percorso_completo_new_dir)).mkdir();

		    if (success_new_dir) {
			    PKBox pbox = new PKBox();
			    String url = "http://localhost:8080/pkserver/servlet/defaulthandler";
			    pbox.addServer(url, null, null, null);
				
				Envelope enve = new Envelope(pbox);
		    	
			    System.out.println("CARTELLA " + dir_new + " CREATA");
	
				System.out.println("PERCORSO COMPLETO DELLA CARTELLA CREATA: " + percorso_completo_new_dir);
	
				System.out.println("Numero di file presenti: " + as.length);
				
				String percorso_completo = null;
				FileInputStream fileIn;
				FileOutputStream fileOut;
				String docfile_Out = null;
				BufferedReader alias = null;
				String alias_name = null;
				BufferedReader pin = null;
				String pin_num = null;
				String docfile_delete = null;
				String percorso_new_file = null;
				String docfileVerify = null;
				String estensione_fileOriginale = null;
				
						
				System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
			    alias = new BufferedReader(new InputStreamReader(System.in));
			    alias_name = alias.readLine();
			    
				System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
			    pin = new BufferedReader(new InputStreamReader(System.in));
			    pin_num = pin.readLine();
			    
				System.out.println();
				for(int i = 0; i < as.length; i++) {
		 
					File fileN = new File(dir, as[i]);
	
					if(ultimo_carattere.equals("\\"))
						percorso_completo = (dir + as[i]);
					else {
						percorso_completo = (dir + "\\" + as[i]);
					}
				
					System.out.println("FILE: " + percorso_completo + "  " + fileN.length() + " byte");
					estensione_fileOriginale = percorso_completo.substring(percorso_completo.length()-3,percorso_completo.length());
		
					fileIn = new FileInputStream(percorso_completo);
					docfile_Out = percorso_completo_new_dir + as[i].substring(0,((as[i].length()))) + ".p7m";
					fileOut = new FileOutputStream(docfile_Out);
								    
					enve.sign(fileIn, fileIn.available(), alias_name, pin_num, Envelope.implicitMode, Envelope.base64Encoding, null,fileOut);
					
				    System.out.println("FILE FIRMATO: " + docfile_Out);
				    System.out.println("");
	
					fileOut.close();
					
					FileInputStream fileOutEnve = new FileInputStream(docfile_Out);
					docfileVerify = percorso_completo.substring(0,((percorso_completo.length())-4)) + "_verify." + estensione_fileOriginale;
					FileOutputStream filedocfileNVerify = new FileOutputStream(docfileVerify);
					VerifyInfo vi = enve.verify (null,  fileIn.available(), fileOutEnve, fileOutEnve.available(), filedocfileNVerify);
					
					System.out.println("Numero di firme apportate al documento: " + vi.getSignerCount() );
					
					//THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR SUBJECT DN
				 
				    System.out.println("Singer is " + vi.getSigner(0).getSubjectDN());
				    Date signTime = vi.getSigner(0).getSigningTime();
				    System.out.println("SigningTime is " + signTime);
						
				    fileIn.close();
					fileOutEnve.close();
					filedocfileNVerify.close();
					
					//Delete file verify original  
					File file_verify = new File(docfileVerify);
					boolean success_verify = file_verify.delete();
					if (!success_verify){
					      System.out.println("Deletion failed." + docfileVerify);
					      System.out.println("");
					}else{
					      System.out.println("File: " + docfileVerify + " deleted.");
					      System.out.println("");
					}
					System.out.println("*************************************************************************");
					System.out.println("");
				}
				
	
				System.out.print("INSERIRE UN'ALTRA FIRMA? (S/N) ");
				BufferedReader firmatari = new BufferedReader(new InputStreamReader(System.in));
				String firm = firmatari.readLine();
					
				File new_dir = new File(percorso_completo_new_dir);
				String array_elenco_file_new_dir[] = new_dir.list();
				System.out.println();
				if(firm.toUpperCase().equals("S")){
					System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
				    alias = new BufferedReader(new InputStreamReader(System.in));
				    alias_name = alias.readLine();
				    
					System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
				    pin = new BufferedReader(new InputStreamReader(System.in));
				    pin_num = pin.readLine();
					System.out.println();
					for(int i = 0; i < array_elenco_file_new_dir.length; i++) {
						String file_originale = as[i];
						percorso_new_file = percorso_completo_new_dir+array_elenco_file_new_dir[i];
						System.out.println("FILE: " +percorso_new_file);
						fileIn = new FileInputStream(percorso_new_file);
						String docfile_Out1 = percorso_new_file.substring(0,((percorso_new_file.length())-4))+ "_addsign.p7m";
						fileOut = new FileOutputStream(docfile_Out1);
						 
						enve.addsign(null, 0, fileIn, fileIn.available(), alias_name, pin_num, Envelope.base64Encoding, null, fileOut);
						
						System.out.println("FileName della firma aggiunta: " + docfile_Out1);

						fileOut.close();
						
						docfile_delete = percorso_new_file;
						
						FileInputStream fileOutEnve = new FileInputStream(docfile_Out1);
						estensione_fileOriginale = file_originale.substring(file_originale.length()-3,file_originale.length());
						
						docfileVerify = file_originale.substring(0,((file_originale.length())-4)) + "_verify." + estensione_fileOriginale;
						FileOutputStream filedocfileNVerify = new FileOutputStream(docfileVerify);
						
						VerifyInfo vi = enve.verify (null, fileIn.available(), fileOutEnve, fileOutEnve.available(), null, filedocfileNVerify);
						System.out.println("Numero di firme apportate al documento : " + vi.getSignerCount() );
						
						int index = vi.getSignerCount();
						// LOOP THROUGH THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR SUBJECT DN
						for(int j = 0; j < index; j++){
						    System.out.println("Singer " + (j+1) + " is " + vi.getSigner(j).getSubjectDN());
						    Date signTime = vi.getSigner(j).getSigningTime();
						    System.out.println("SigningTime " + (j+1) + " is " + signTime);
						}
					    
						fileIn.close();
						fileOutEnve.close();
						filedocfileNVerify.close();
						
						//Delete file verify original  
						File file_verify = new File(docfileVerify);
						boolean success_verify = file_verify.delete();
						if (!success_verify){
						      System.out.println("Deletion failed." + docfileVerify);
						      System.out.println("");
						}else{
						      System.out.println("File: " + docfileVerify + " deleted.");
						      System.out.println("");
						}
						
						//Delete temporary file 
						File f1 = new File(docfile_delete);
						boolean success = f1.delete();
						if (!success){
						      System.out.println("Deletion failed." + docfile_delete);
						      //System.exit(0);
						}else{
					      System.out.println("File: " +docfile_delete + " deleted.");
					      System.out.println("*************************************************************************");
					      System.out.println("");
						}

					}
				}
			}else
			      System.out.println("Impossibile creare: " + dir_new);
			    
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
