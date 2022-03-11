package signverify;


import it.pkbox.client.*;

import java.io.*;
import java.util.Date;

public class PkBox_FirmaDigitale_normal_mult_con_MENU {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try
		{
			String versionePkBox = Version.getVersion();
			System.out.println("Stai usando la versione di PkBox: " + versionePkBox);
//			System.out.print("Enter file name (inserire il percorso completo del file): ");
		    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//		    String docfile1 = in.readLine();
		    String docfile1 = "/Users/francescomongali/Desktop/TestFile.pdf";
		    File f = new File(docfile1);


		    
		if(f.exists()){
			
			long file_size = f.length();
			System.out.println("La dimensione del file � di: " + file_size + " byte");
			
			
			System.out.print("Inserire piu' firmatari al file? (S/N): ");
		    BufferedReader num = new BufferedReader(new InputStreamReader(System.in));
		    String sn = num.readLine();
		    		    
		    PKBox pbox = new PKBox();
		    String url = "https://intranet.kdm.it:8443/pkserver/servlet/defaulthandler";
		    //String url = "http://localhost:8080/pkserver/servlet/defaulthandler";
		    pbox.addServer(url, null, null, null);
			
			
			FileInputStream fileIn;
			FileOutputStream fileOut;
			Envelope enve = new Envelope(pbox);
			String nome_fileOutEnve = null;
			String alias_name = null;
			String PIN_num = null;
			String estensione_docfile1 = docfile1.substring(docfile1.length()-3,docfile1.length());
			
			fileIn = new FileInputStream(docfile1);
			String docfileN = docfile1.substring(0,((docfile1.length())-4)) + ".p7m";

		
			fileOut = new FileOutputStream(docfileN);
	
//		if(sn.toUpperCase().equals("S")){
//			
//			System.out.print("Inserire il numero di firmatari che desiderano firmare il file: ");
//			BufferedReader numFirm = new BufferedReader(new InputStreamReader(System.in));
//		    String numFirme = numFirm.readLine();
//		    int nFirme = Integer.parseInt(numFirme);
//		    System.out.println("numero firme " + nFirme);
//		   
//		    String arrayNome_file[] = new String[100];
//		    
//		    arrayNome_file[0]=docfileN;
//		    
//		    FileInputStream fileIn1;
//		    FileOutputStream fileOut1;
//		    System.out.println("FIRMATARIO 1: ");
//		    System.out.print("Inserire l'ALIAS corretto del firmatario 1: ");
//			BufferedReader alias = new BufferedReader(new InputStreamReader(System.in));
//		    alias_name = alias.readLine();
//		    
//		    System.out.print("Inserire il PIN corretto del firmatario 1: ");
//			BufferedReader PIN = new BufferedReader(new InputStreamReader(System.in));
//		    PIN_num = PIN.readLine();
//		    
//			enve.sign(fileIn, fileIn.available(), alias_name, PIN_num, Envelope.implicitMode, Envelope.base64Encoding, null,fileOut); 
//			System.out.println("FileName output: " + docfileN);
//			String docfileVerify = null;
//			
//		    for (int i =1; i<nFirme; i++){
//		    	System.out.println("FIRMATARIO :" +(i+1));
//		    	
//			    System.out.print("Inserire l'ALIAS corretto del firmatario: ");
//				alias = new BufferedReader(new InputStreamReader(System.in));
//			    alias_name = alias.readLine();
//			    
//			    System.out.print("Inserire il PIN corretto del firmatario: ");
//				PIN = new BufferedReader(new InputStreamReader(System.in));
//			    PIN_num = PIN.readLine();
//			      	
//	
//			    fileIn1 = new FileInputStream(docfileN);
//				
//			    String docfileN1 = docfile1.substring(0,((docfile1.length())-4)) +"_"+ i+ "_addsign.p7m";
//			    
//				fileOut1 = new FileOutputStream(docfileN1);
//				enve.addsign(null, 0, fileIn1, fileIn1.available(), alias_name, PIN_num, Envelope.base64Encoding, null, fileOut1);
//				fileIn1.close();
//				fileOut1.close();
//				
//				nome_fileOutEnve = docfileN1;
//				docfileN = docfileN1;
//				arrayNome_file[i] = docfileN1;
//	
//				docfileVerify = docfile1.substring(0,((docfile1.length())-4)) + "_verify." + estensione_docfile1;
//	
//			}
//			
//			
//			FileInputStream fileOutEnve;
//			fileOutEnve = new FileInputStream(nome_fileOutEnve);
//			
//			FileOutputStream filedocfileNVerify;
//			filedocfileNVerify = new FileOutputStream(docfileVerify);
//			
//			VerifyInfo vi = enve.verify (null, fileIn.available(), fileOutEnve, fileOutEnve.available(), null, filedocfileNVerify);
//			System.out.println("Numero di firme apportate al documento : " + vi.getSignerCount() );
//			
//			int index = vi.getSignerCount();
//			// LOOP THROUGH THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR SUBJECT DN
//			  for(int i = 0; i < index; i++){
//			    System.out.println("Singer " + (i+1) + " is " + vi.getSigner(i).getSubjectDN());
//			    Date signTime = vi.getSigner(i).getSigningTime();
//			    System.out.println("SigningTime " + (i+1) + " is " + signTime);
//			}
//			  
//			fileOutEnve.close();
//			filedocfileNVerify.close();
//			
//			//Delete file verify original  
//			File file_verify = new File(docfileVerify);
//			boolean success_verify = file_verify.delete();
//			 if (!success_verify){
//			      System.out.println("Deletion failed." + docfileVerify);
//			      //System.exit(0);
//			    }else{
//			      System.out.println("File" + docfileVerify + " deleted.");
//			    	}
//	
//
//	
//			
//			//Delete temporary file 
//			for (int i=0; i<(nFirme-1); i++){
//				fileOut.close();
//				File f1 = new File(arrayNome_file[i]);
//				boolean success = f1.delete();
//				 if (!success){
//				      System.out.println("Deletion failed." + arrayNome_file[i]);
//				      //System.exit(0);
//				    }else{
//				      System.out.println("File" +arrayNome_file[i] + " deleted.");
//				    	}
//			}
//		
//		
//		}
//		else
//			{
			
			System.out.println("UNICA FIRMA");
			
		    System.out.print("Inserire l'ALIAS corretto del firmatario: ");
		    BufferedReader alias = new BufferedReader(new InputStreamReader(System.in));
		    alias_name = alias.readLine();
		    
		    System.out.print("Inserire il PIN corretto del firmatario: ");
		    BufferedReader PIN = new BufferedReader(new InputStreamReader(System.in));
		    PIN_num = PIN.readLine();
		    
			enve.sign(fileIn, fileIn.available(), alias_name, PIN_num, Envelope.implicitMode, Envelope.base64Encoding, null, fileOut);
			fileOut.close();
			
			nome_fileOutEnve = docfileN;
			FileInputStream fileOutEnve;
			fileOutEnve = new FileInputStream(nome_fileOutEnve);
			String docfileVerify = docfile1.substring(0,((docfile1.length())-4)) + "_verify." + estensione_docfile1;
			FileOutputStream filedocfileNVerify;
			filedocfileNVerify = new FileOutputStream(docfileVerify);
			VerifyInfo vi = enve.verify (null,  fileIn.available(), fileOutEnve, fileOutEnve.available(), filedocfileNVerify);
			System.out.println("File FIRMATO");
			System.out.println("Numero di firme apportate al documento: " + vi.getSignerCount() );
		
		
		  //THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR SUBJECT DN
		 
		    System.out.println("Singer is " + vi.getSigner(0).getSubjectDN());
		    Date signTime = vi.getSigner(0).getSigningTime();
		    System.out.println("SigningTime is " + signTime);
				
			fileOutEnve.close();
			fileIn.close();
			filedocfileNVerify.close();
			
			//Delete file verify original
			File f1 = new File(docfileVerify);
			boolean success = f1.delete();
			 if (!success){
			      System.out.println("Deletion failed." + docfileVerify);
			      //System.exit(0);
			    }else{
			      System.out.println("File" + docfileVerify + " deleted.");
			    	}
//			}

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
