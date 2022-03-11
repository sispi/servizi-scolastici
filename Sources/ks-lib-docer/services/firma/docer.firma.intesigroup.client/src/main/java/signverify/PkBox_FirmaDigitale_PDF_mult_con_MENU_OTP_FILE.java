package signverify;

import it.pkbox.client.Envelope;
import it.pkbox.client.PKBox;
import it.pkbox.client.VerifyInfo;
import it.pkbox.client.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;

public class PkBox_FirmaDigitale_PDF_mult_con_MENU_OTP_FILE {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			String versionePkBox = Version.getVersion();
			System.out.println("Stai usando la versione di PkBox: "+ versionePkBox);
			System.out.print("Enter file name (inserire il percorso completo del file PDF): ");

			//BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			//String fname = in.readLine();
			String fname = "/Users/francescomongali/Desktop/TestFile.pdf";
			File f = new File(fname);
			String extension_file = fname.substring(((fname.length()) - 3),fname.length());

			if (f.exists() && extension_file.toUpperCase().equals("PDF")) {

				long file_size = f.length();
				System.out.println("La dimensione del file � di: " + file_size+ " byte");

				System.out.print("Inserire piu' firmatari al file PDF? (S/N): ");
				BufferedReader num = new BufferedReader(new InputStreamReader(System.in));
				String sn = num.readLine();

				PKBox pbox = new PKBox();
				//String url = "http://192.168.0.39:8080/pkserver/servlet/StampingAntohandler";
				//String url = "http://192.168.0.39:8080/pkserver/servlet/FirmaAntohandler";
				String url = "https://intranet.kdm.it:8443/pkserver/servlet/defaulthandler";
//				
				pbox.addServer(url, null, null, null);

				Envelope enve = new Envelope(pbox);

				// OPERAZIONE CON I FILE

				//File image = new File("C:\\provaPksuite\\immagine.jpg");
				// create link to file
				File fin = null;
				File fout = null;
				fin = new File(fname);
				String docfileN = fname.substring(0, ((fname.length()) - 4))+ "_sign.pdf";
				fout = new File(docfileN);

				String name_fileOutEnve = null;

				Date data = null;
				BufferedReader alias = null;
				BufferedReader PIN = null;
				BufferedReader signerPin = null;

				String alias_name = null;
				String PIN_num = null;
				String signerPin_num = null;

				if (sn.toUpperCase().equals("S")) {

					System.out.print("Inserire il numero di firmatari che desiderano firmare il file: ");
					BufferedReader numFirm = new BufferedReader(new InputStreamReader(System.in));
					String numFirme = numFirm.readLine();
					int nFirme = Integer.parseInt(numFirme);
					System.out.println("numero firme " + nFirme);

					String arrayNome_file[] = new String[100];
					arrayNome_file[0] = docfileN;

					System.out.println("FIRMATARIO 1: ");
					System.out.print("Inserire l'ALIAS corretto del firmatario 1: ");
					alias = new BufferedReader(new InputStreamReader(System.in));
					alias_name = alias.readLine();

					System.out.print("Inserire il PIN corretto del firmatario 1: ");
					PIN = new BufferedReader(new InputStreamReader(System.in));
					PIN_num = PIN.readLine();

					System.out.print("Inserire il OTP corretto del firmatario 1: ");
					signerPin = new BufferedReader(new InputStreamReader(System.in));
					signerPin_num = signerPin.readLine();

					enve.pdfsign(fin, null, "Rea_son.01", "Loca_tion.01",
							"Con_tent.01", "", alias_name, PIN_num,
							signerPin_num, data, null, 0, 0, 0, 0, fout);

					System.out.println("FileName output PRIMA FIRMA: "+ docfileN);

					for (int i = 1; i < nFirme; i++) {
						System.out.println("FIRMATARIO :" + (i + 1));
						System.out.print("Inserire l'ALIAS corretto del firmatario: ");
						alias = new BufferedReader(new InputStreamReader(System.in));
						alias_name = alias.readLine();

						System.out.print("Inserire il PIN corretto del firmatario: ");
						PIN = new BufferedReader(new InputStreamReader(System.in));
						PIN_num = PIN.readLine();

						System.out.print("Inserire il OTP corretto del firmatario: ");
						signerPin = new BufferedReader(new InputStreamReader(System.in));
						signerPin_num = signerPin.readLine();

						fin = new File(docfileN);

						String docfileN1 = fname.substring(0,((fname.length()) - 4))+ i + "_addsign.pdf";

						fout = new File(docfileN1);
						enve.pdfaddsign(fin, null, "Rea_son.0" + (i + 1),
								"Loca_tion.0" + (i + 1),
								"Con_tent.0" + (i + 1), "", alias_name,
								PIN_num, signerPin_num, data, null, 0, 0, 0, 0,
								fout);
						System.out.println("FileName output della " + (i + 1)+ " firma digitale: " + docfileN1);

						// filename for verify
						name_fileOutEnve = docfileN1;
						docfileN = docfileN1;
						arrayNome_file[i] = docfileN1;
					}

					File fileOutEnve = new File(name_fileOutEnve);
					VerifyInfo vi = enve.pdfverify(fileOutEnve, null, null);

					System.out.println("Numero di firme apportate al documento: "+ vi.getSignerCount());

					int index = vi.getSignerCount();
					// LOOP THROUGH THE LIST OF SIGNERS OF THE DATA AND PRINT
					// THEIR SUBJECT DN
					for (int i = 1; i <= index; i++) {
						System.out.println("Singer " + (i) + " is "+ vi.getSigner(index - i).getSubjectDN());
						Date signTime = vi.getSigner(index - i).getSigningTime();
						System.out.println("SigningTime " + (i) + " is "+ signTime);
					}

					// Delete temporary file
					for (int i = 0; i < (nFirme - 1); i++) {

						File f1 = new File(arrayNome_file[i]);
						boolean success = f1.delete();
						if (!success) {
							System.out.println("Deletion failed."+ arrayNome_file[i]);

						} else {
							System.out.println("File " + arrayNome_file[i]+ " deleted.");
						}
					}

				} else {

					System.out.println("UNICA FIRMA");
					System.out.print("Inserire l'ALIAS corretto del firmatario: ");
					alias = new BufferedReader(new InputStreamReader(System.in));
					alias_name = alias.readLine();

					System.out.print("Inserire il PIN corretto del firmatario: ");
					PIN = new BufferedReader(new InputStreamReader(System.in));
					PIN_num = PIN.readLine();

					System.out.print("Inserire il OTP corretto del firmatario : ");
					signerPin = new BufferedReader(new InputStreamReader(System.in));
					signerPin_num = signerPin.readLine();

					
					
					enve.pdfsign(fin, null, "Rea_son.01", "Loca_tion.01", "Con_tent.01", "", alias_name, PIN_num,signerPin_num, data, null, 0, 0, 0, 0, fout);
					
					name_fileOutEnve = docfileN;

					File fileOutEnve = new File(name_fileOutEnve);
					System.out.println("name_fileOutEnve: " + name_fileOutEnve );
					VerifyInfo vi = enve.pdfverify(fileOutEnve, null, null);

					System.out.println("Numero di firme apportate al documento: "+ vi.getSignerCount());

					// THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR SUBJECT
					// DN

					System.out.println("Singer is "+ vi.getSigner(0).getSubjectDN());
					Date signTime = vi.getSigner(0).getSigningTime();
					System.out.println("SigningTime is " + signTime);

				}

			} else {
				if (f.exists() && !extension_file.toUpperCase().equals("PDF"))

					System.out.println("Sono accettati solo file con estensione .PDF!!!");
				else
					System.out.println("File does not exists.");

				System.exit(0);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Execution done.");

	}

}
