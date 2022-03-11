package signverify;

import it.pkbox.client.Envelope;
import it.pkbox.client.PKBox;
import it.pkbox.client.VerifyInfo;
import it.pkbox.client.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;

public class PkBox_FirmaDigitale_PDF_mult_image_con_MENU_OTP_FILE {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String versionePkBox = Version.getVersion();
			System.out.println("Stai usando la versione di PkBox: " + versionePkBox);
			System.out.print("Enter file name (inserire il percorso completo del file PDF): ");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String fname = in.readLine();
			File f = new File(fname);

			String estensione_file = fname.substring(((fname.length()) - 3),fname.length());

			PKBox pbox = new PKBox();
			String url = "http://192.168.0.39:8080/pkserver/servlet/StampingAntohandler";
			pbox.addServer(url, null, null, null);
			Envelope enve = new Envelope(pbox);

			if (f.exists() && estensione_file.toUpperCase().equals("PDF")) {

				long file_size = f.length();
				System.out.println("La dimensione del file è di: " + file_size + " byte");
				File fileIn = null;
				File fileOut = null;

				String docfile1 = fname;

				System.out.print("Inserire l'immagine da utilizzare per la prima firma digitale: ");
				BufferedReader in1 = new BufferedReader(new InputStreamReader(System.in));
				String fname_image = in1.readLine();

				String docfileN = docfile1.substring(0, ((fname.length()) - 4))+ "_sign.pdf";
				String arrayNome_file[] = new String[100];
				arrayNome_file[0] = docfileN;

				fileIn = new File(docfile1);

				System.out.println("FileName output: " + docfileN);
				fileOut = new File(docfileN);
				Date data = null;

				File fileImage = null;
				fileImage = new File(fname_image);

				String nome_fileOutEnve = null;

				System.out.println("Scegliere in quale pagina inserire l'immagine");
				System.out.println("Valori permessi: ");
				System.out.println("                0 or 1 = first page ");
				System.out.println("                < 0 = last page ");
				System.out.println("                N = page number");
				System.out.print("                SCELTA: ");
				BufferedReader numPage = new BufferedReader(new InputStreamReader(System.in));
				String numPg = numPage.readLine();
				int nPg = Integer.parseInt(numPg);

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

				String alias_name = null;
				String PIN_num = null;
				String OTP_num = null;
				
				System.out.print("Inserire piu' firmatari al file PDF? (S/N): ");
				BufferedReader num = new BufferedReader(new InputStreamReader(System.in));
				String sn = num.readLine();

				if (sn.toUpperCase().equals("S")) {

					System.out.print("Inserire il numero di firmatari che desiderano firmare il file: ");
					BufferedReader numFirm = new BufferedReader(new InputStreamReader(System.in));
					String numFirme = numFirm.readLine();
					int nFirme = Integer.parseInt(numFirme);
					System.out.println("numero firme " + nFirme);

					System.out.println("FIRMATARIO 1: ");
					System.out.print("Inserire l'ALIAS corretto del firmatario 1: ");
					BufferedReader alias = new BufferedReader(new InputStreamReader(System.in));
					alias_name = alias.readLine();

					System.out.print("Inserire il PIN corretto del firmatario 1: ");
					BufferedReader PIN = new BufferedReader(new InputStreamReader(System.in));
					PIN_num = PIN.readLine();

					System.out.print("Inserire il OTP corretto del firmatario 1: ");
					BufferedReader OTP = new BufferedReader(new InputStreamReader(System.in));
					OTP_num = OTP.readLine();

					enve.pdfsign(fileIn, null, "Rea_son.01", "Loca_tion.01",
							"Con_tent.01", "", alias_name, PIN_num, OTP_num,
							data, fileImage, nPg, nPs, 0, 0, fileOut);
					System.out.println("FileName output: " + docfileN);

					File fileIn1 = null;
					File fileOut1 = null;

					String docfileNArr[] = new String[100];

					for (int i = 1; i < nFirme; i++) {
						System.out.println("FIRMATARIO :" + (i + 1));

						System.out.print("Inserire l'ALIAS corretto del firmatario "+ (i + 1) + ": ");
						alias = new BufferedReader(new InputStreamReader(System.in));
						alias_name = alias.readLine();

						System.out.print("Inserire il PIN corretto del firmatario "+ (i + 1) + ": ");
						PIN = new BufferedReader(new InputStreamReader(System.in));
						PIN_num = PIN.readLine();

						System.out.print("Inserire il OTP corretto del firmatario "+ (i + 1) + ": ");
						OTP = new BufferedReader(new InputStreamReader(System.in));
						OTP_num = PIN.readLine();

						docfileNArr[0] = docfileN;

						if (i == 0)
							fileIn1 = new File(docfileNArr[0]);
						else
							fileIn1 = new File(docfileNArr[i - 1]);

						String docfileN1 = docfile1.substring(0, ((fname.length()) - 4))+ i + "_addsign.pdf";
						fileOut1 = new File(docfileN1);
						System.out.print("Inserire un'altra immagine da utilizzare per la firma digitale aggiuntiva: ");
						BufferedReader in2 = new BufferedReader(new InputStreamReader(System.in));
						String fname_image1 = in2.readLine();

						BufferedReader numPageN = null;
						String numPgN = null;
						int nPgN = 0;
						BufferedReader numPositionN = null;
						String numPosizioneN = null;
						int nPsN = 0;

						System.out.println("Scegliere in quale pagina inserire l'immagine");
						System.out.println("Valori permessi: ");
						System.out.println("                0 or 1 = first page ");
						System.out.println("                < 0 = last page ");
						System.out.println("                N = page number");
						System.out.print("                SCELTA: ");
						numPageN = new BufferedReader(new InputStreamReader(System.in));
						numPgN = numPageN.readLine();
						nPgN = Integer.parseInt(numPgN);

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
						numPositionN = new BufferedReader(new InputStreamReader(System.in));
						numPosizioneN = numPositionN.readLine();
						nPsN = Integer.parseInt(numPosizioneN);

						File fileImage1 = null;
						fileImage1 = new File(fname_image1);

						enve.pdfaddsign(fileIn1, null, "Rea_son.0" + (i + 1),
								"Loca_tion.0" + (i + 1),
								"Con_tent.0" + (i + 1), "", alias_name,
								PIN_num, OTP_num, data, fileImage1, nPgN, nPsN,
								0, 0, fileOut1);
						System.out.println("FileName output: " + docfileN1);

						nome_fileOutEnve = docfileN1;
						docfileNArr[i] = docfileN1;
						arrayNome_file[i] = docfileN1;

					}

					File fileOutEnve = null;
					fileOutEnve = new File(nome_fileOutEnve);

					VerifyInfo vi = enve.pdfverify(fileOutEnve, null, null);
					System.out.println("Numero di firme apportate al documento: "+ vi.getSignerCount());

					int index = vi.getSignerCount();
					// LOOP THROUGH THE LIST OF SIGNERS OF THE DATA AND PRINT
					// THEIR SUBJECT DN
					for (int i = 0; i < index; i++) {
						System.out.println("Singer " + (i + 1) + " is "+ vi.getSigner(index - i - 1).getSubjectDN());
						Date signTime = vi.getSigner(index - i - 1).getSigningTime();
						System.out.println("SigningTime " + (i + 1) + " is "+ signTime);
					}

					for (int i = 0; i < (nFirme - 1); i++) {
						// fileOut.close();
						File f1 = new File(arrayNome_file[i]);
						boolean success = f1.delete();
						if (!success) {
							System.out.println("Deletion failed."+ arrayNome_file[i]);
							// System.exit(0);
						} else {
							System.out.println("File" + arrayNome_file[i]+ " deleted.");
						}
					}

					// S/N
				} else {
					System.out.println("FIRMATARIO 1: ");
					System.out.print("Inserire l'ALIAS corretto del firmatario 1: ");
					BufferedReader alias = new BufferedReader(new InputStreamReader(System.in));
					alias_name = alias.readLine();

					System.out.print("Inserire il PIN corretto del firmatario 1: ");
					BufferedReader PIN = new BufferedReader(new InputStreamReader(System.in));
					PIN_num = PIN.readLine();

					System.out.print("Inserire il OTP corretto del firmatario 1: ");
					BufferedReader OTP = new BufferedReader(new InputStreamReader(System.in));
					OTP_num = OTP.readLine();

					enve.pdfsign(fileIn, null, "Rea_son.01", "Loca_tion.01",
							"Con_tent.01", "", alias_name, PIN_num, OTP_num,
							data, fileImage, nPg, nPs, 0, 0, fileOut);
					
					nome_fileOutEnve = docfileN;
					File fileOutEnve = null;
					fileOutEnve = new File(nome_fileOutEnve);
					VerifyInfo vi = enve.pdfverify(fileOutEnve, null, null);
					System.out.println("Numero di firme apportate al documento: "+ vi.getSignerCount());

					// THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR SUBJECT
					// DN

					System.out.println("Singer is "+ vi.getSigner(0).getSubjectDN());
					Date signTime = vi.getSigner(0).getSigningTime();
					System.out.println("SigningTime is " + signTime);
				}
				// PDF
			} else {
				if (f.exists() && !estensione_file.toUpperCase().equals("PDF"))
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
