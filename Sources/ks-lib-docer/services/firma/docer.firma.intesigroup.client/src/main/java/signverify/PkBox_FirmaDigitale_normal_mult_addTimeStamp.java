package signverify;


import it.pkbox.client.*;

import java.io.*;
import java.util.Date;

public class PkBox_FirmaDigitale_normal_mult_addTimeStamp {
	/**
	 * @param args
	 */
 public static void main(String[] args) {
  try
  {
	String versionePkBox = Version.getVersion();
	System.out.println("Stai usando la versione di PkBox: " + versionePkBox);
	System.out.print("Enter file name (inserire il percorso completo del file): ");
	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	String fname = in.readLine();
	File f = new File(fname);

	if(f.exists()){

		long file_size = f.length();
		System.out.println("La dimensione del file è di: " + file_size + " byte");
			
		System.out.print("Inserire piu' firmatari al file? (S/N): ");
		BufferedReader num = new BufferedReader(new InputStreamReader(System.in));
		String sn = num.readLine();
		    		    
		PKBox pbox = new PKBox();
		String url = "http://localhost:8080/pkserver/servlet/defaulthandler";
		pbox.addServer(url, null, null, null);
				
		FileInputStream fileIn;
		FileOutputStream fileOut;
		Envelope enve = new Envelope(pbox);
			
		String arrayNome_file[] = new String [100];
		String alias_name = null;
		String PIN_num = null;
		String docfileVerify = null;
		String estensione_fileOriginale = fname.substring(fname.length()-3,fname.length());
		BufferedReader alias = null;
		BufferedReader PIN = null;
		
		fileIn = new FileInputStream(fname);
		String docfileN = fname.substring(0,((fname.length())-4)) + ".p7m";
		fileOut = new FileOutputStream(docfileN);
		
		
		if(sn.toUpperCase().equals("S")){
			
			System.out.print("Inserire l'ALIAS corretto del firmatario 1: ");
			alias = new BufferedReader(new InputStreamReader(System.in));
		    alias_name = alias.readLine();
		    
		    System.out.print("Inserire il PIN corretto del firmatario 1: ");
			PIN = new BufferedReader(new InputStreamReader(System.in));
		    PIN_num = PIN.readLine();
		    
		    enve.sign(fileIn, fileIn.available(), alias_name, PIN_num, Envelope.implicitMode, Envelope.base64Encoding, null,fileOut); 
		    System.out.println("FileName output: " + docfileN);
		    System.out.println("");

		
			FileInputStream fileIn1;
			fileIn1 = new FileInputStream(docfileN);
			FileOutputStream fileOut1;
			String docfileN1 = fname.substring(0,((fname.length())-4)) + "_addsign.p7m";
			fileOut1 = new FileOutputStream(docfileN1);
			
			System.out.print("Inserire l'ALIAS corretto del firmatario 2: ");
			alias = new BufferedReader(new InputStreamReader(System.in));
		    alias_name = alias.readLine();
		    
		    System.out.print("Inserire il PIN corretto del firmatario 2: ");
			PIN = new BufferedReader(new InputStreamReader(System.in));
		    PIN_num = PIN.readLine();
		    
			enve.addsign(null, fileIn.available(), fileIn1, fileIn1.available(), alias_name, PIN_num, Envelope.base64Encoding, null, fileOut1);
			System.out.println("FileName output seconda firma: " + docfileN1);
			System.out.println("");
			
			arrayNome_file[0] = docfileN;
			arrayNome_file[1] = docfileN1;
		
		
			String nome_fileOutEnve = docfileN1;
			FileInputStream fileOutEnve = new FileInputStream(nome_fileOutEnve);
			docfileVerify = fname.substring(0,((fname.length())-4)) + "_verify." + estensione_fileOriginale;
			FileOutputStream filedocfileNVerify = new FileOutputStream(docfileVerify);
			VerifyInfo vi = enve.verify (null, fileIn.available(), fileOutEnve, fileOutEnve.available(), null, filedocfileNVerify);
			System.out.println("Numero di firme apportate al documento : " + vi.getSignerCount() );
		
			int index = vi.getSignerCount();
			  // LOOP THROUGH THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR SUBJECT DN
			for(int i = 0; i < index; i++){
				Signer Sign = vi.getSigner(i);
			    System.out.println("Singer " + (i+1) + " is " + Sign.getSubjectDN());
			    Date signTime = Sign.getSigningTime();
			    System.out.println("SigningTime " + (i+1) + " is " + signTime);
			}
			

			  
			FileInputStream fileInAddTime = new FileInputStream(docfileN1);
			String docfileN_AddTime = fname.substring(0,((fname.length())-4)) + "_sign_addTimeStamp.p7m";
			FileOutputStream fileOutAddTime = new FileOutputStream(docfileN_AddTime);
							
			enve.addTimeStamp(null, fileIn.available(), fileInAddTime, fileInAddTime.available(), null, "12345678", 2, fileOutAddTime);

			fileIn.close();
			fileOut.close();  
			fileIn1.close();
			fileOut1.close();
			fileOutEnve.close();
			fileInAddTime.close();
			fileOutAddTime.close();
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
			for (int i=0; i<2; i++){
				File f1 = new File(arrayNome_file[i]);
				boolean success = f1.delete();
				if (!success){
				      System.out.println("Deletion failed. " + arrayNome_file[i]);
				      //System.exit(0);
				}else{
				      System.out.println("File " +arrayNome_file[i] + " deleted.");
				}
			}
			System.out.println("*************************************************************************");
			System.out.println("");
		
		
		}
		else{
			
			System.out.print("Inserire l'ALIAS corretto del firmatario 1: ");
			alias = new BufferedReader(new InputStreamReader(System.in));
		    alias_name = alias.readLine();
		    
		    System.out.print("Inserire il PIN corretto del firmatario 1: ");
			PIN = new BufferedReader(new InputStreamReader(System.in));
		    PIN_num = PIN.readLine();
		    
			enve.sign(fileIn, fileIn.available(), alias_name, PIN_num, Envelope.implicitMode, Envelope.base64Encoding, null, fileOut);
			
			
			String nome_fileOutEnve = docfileN;
			FileInputStream fileOutEnve = new FileInputStream(nome_fileOutEnve);
			docfileVerify = fname.substring(0,((fname.length())-4)) + "_verify." + estensione_fileOriginale;
			FileOutputStream filedocfileNVerify = new FileOutputStream(docfileVerify);
			VerifyInfo vi = enve.verify (null, 0, fileOutEnve, fileOutEnve.available(), filedocfileNVerify);
			System.out.println("Numero di firme apportate al documento: " + vi.getSignerCount() );
		
		
			//THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR SUBJECT DN
				 
			System.out.println("Singer is " + vi.getSigner(0).getSubjectDN());
			Date signTime = vi.getSigner(0).getSigningTime();
			System.out.println("SigningTime is " + signTime);

			CertificateID Cert_Signer_array[] = new CertificateID[1];
			Cert_Signer_array[0] = vi.getSigner(0).getCertificateID();
				    
			FileInputStream fileInAddTime;
			fileInAddTime = new FileInputStream(docfileN);
			FileOutputStream fileOutAddTime;
			String docfileN_AddTime = fname.substring(0,((fname.length())-4)) + "_sign_addTimeStamp.p7m";
			fileOutAddTime = new FileOutputStream(docfileN_AddTime);
					
			enve.addTimeStamp(null, 0, fileInAddTime, fileInAddTime.available(), Cert_Signer_array, "12345678", 2, fileOutAddTime);   
				    
			fileIn.close();
			fileOut.close();   
			fileInAddTime.close();
			fileOutAddTime.close();
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
			File f1 = new File(docfileN);
			boolean success = f1.delete();
			if (!success){
			      System.out.println("Deletion failed. " + docfileN);
			}else{
			  System.out.println("File " +docfileN + " deleted.");
			}
			
			System.out.println("*************************************************************************");
			System.out.println("");
		
		}

	}
	else{	   
	   System.out.println("File does not exists.");
	   System.exit(0);   
	}	
  }
  catch(Exception e)
  {e.printStackTrace();}
  System.out.println("Execution done.");
 }
}
