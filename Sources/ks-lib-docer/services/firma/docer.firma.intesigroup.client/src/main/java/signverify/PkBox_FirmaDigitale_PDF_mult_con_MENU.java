package signverify;

import it.pkbox.client.*;

import java.io.*;
import java.util.Date;

public class PkBox_FirmaDigitale_PDF_mult_con_MENU {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String versionePkBox = Version.getVersion();
			System.out.println("Stai usando la versione di PkBox: "
					+ versionePkBox);
			System.out
					.print("Enter file name (inserire il percorso completo del file PDF): ");
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String fname = in.readLine();
			File f = new File(fname);

			String estensione_file = fname.substring(((fname.length()) - 3),
					fname.length());

			if (f.exists() && estensione_file.toUpperCase().equals("PDF")) {

				long file_size = f.length();
				System.out.println("La dimensione del file � di: " + file_size
						+ " byte");
				FileInputStream fileIn;
				FileOutputStream fileOut;

				System.out
						.print("Inserire piu' firmatari al file PDF? (S/N): ");
				BufferedReader num = new BufferedReader(new InputStreamReader(
						System.in));
				String sn = num.readLine();

				PKBox pbox = new PKBox();
//				String url = "http://localhost:8080/pkserver/servlet/defaulthandler";
				String url = "https://intranet.kdm.it:8443/pkserver/servlet/defaulthandler";
				pbox.addServer(url, null, null, null);

				Envelope enve = new Envelope(pbox);

				String nome_fileOutEnve = null;

				fileIn = new FileInputStream(fname);
				String docfileN = fname.substring(0, ((fname.length()) - 4))+ "_sign.pdf";

				fileOut = new FileOutputStream(docfileN);

				Date data = null;
				String alias_name = null;
				String PIN_num = null;
				BufferedReader alias = null;
				BufferedReader PIN = null;

//				if (sn.toUpperCase().equals("S")) {
//					System.out
//							.print("Inserire il numero di firmatari che desiderano firmare il file: ");
//					BufferedReader numFirm = new BufferedReader(
//							new InputStreamReader(System.in));
//					String numFirme = numFirm.readLine();
//					int nFirme = Integer.parseInt(numFirme);
//					System.out.println("numero firme " + nFirme);
//
//					String arrayNome_file[] = new String[100];
//
//					arrayNome_file[0] = docfileN;
//
//					System.out.println("FIRMATARIO 1: ");
//					System.out
//							.print("Inserire l'ALIAS corretto del firmatario 1: ");
//					alias = new BufferedReader(new InputStreamReader(System.in));
//					alias_name = alias.readLine();
//
//					System.out
//							.print("Inserire il PIN corretto del firmatario 1: ");
//					PIN = new BufferedReader(new InputStreamReader(System.in));
//					PIN_num = PIN.readLine();
//
//					enve.pdfsign(fileIn, fileIn.available(), "Rea_son.01",
//							"Loca_tion.01", "Con_tent.01", alias_name, PIN_num,
//							data, fileOut);
//					fileIn.close();
//					fileOut.close();
//					System.out.println("FileName output PRIMA FIRMA: "
//							+ docfileN);
//
//					for (int i = 1; i < nFirme; i++) {
//						System.out.println("FIRMATARIO :" + (i + 1));
//
//						System.out
//								.print("Inserire l'ALIAS corretto del firmatario: ");
//						alias = new BufferedReader(new InputStreamReader(
//								System.in));
//						alias_name = alias.readLine();
//
//						System.out
//								.print("Inserire il PIN corretto del firmatario: ");
//						PIN = new BufferedReader(new InputStreamReader(
//								System.in));
//						PIN_num = PIN.readLine();
//
//						fileIn = new FileInputStream(docfileN);
//
//						String docfileN1 = fname.substring(0,
//								((fname.length()) - 4))
//								+ i + "_addsign.pdf";
//						fileOut = new FileOutputStream(docfileN1);
//
//						enve.pdfaddsign(fileIn, fileIn.available(), "Rea_son.0"
//								+ (i + 1), "Loca_tion.0" + (i + 1),
//								"Con_tent.0" + (i + 1), alias_name, PIN_num,
//								data, fileOut);
//						System.out.println("FileName output della " + (i + 1)
//								+ " firma digitale: " + docfileN1);
//
//						fileIn.close();
//						fileOut.close();
//
//						nome_fileOutEnve = docfileN1;
//						docfileN = docfileN1;
//						arrayNome_file[i] = docfileN1;
//					}
//
//					FileInputStream fileOutEnve;
//					fileOutEnve = new FileInputStream(nome_fileOutEnve);
//					VerifyInfo vi = enve.pdfverify(fileOutEnve, fileOutEnve
//							.available());
//					System.out
//							.println("Numero di firme apportate al documento: "
//									+ vi.getSignerCount());
//
//					int index = vi.getSignerCount();
//					// LOOP THROUGH THE LIST OF SIGNERS OF THE DATA AND PRINT
//					// THEIR SUBJECT DN
//					for (int i = 1; i <= index; i++) {
//						System.out.println("Singer " + (i) + " is "
//								+ vi.getSigner(index - i).getSubjectDN());
//						Date signTime = vi.getSigner(index - i)
//								.getSigningTime();
//						System.out.println("SigningTime " + (i) + " is "
//								+ signTime);
//					}
//
//					fileOutEnve.close();
//
//					// Delete temporary file
//					for (int i = 0; i < (nFirme - 1); i++) {
//						fileOut.close();
//						File f1 = new File(arrayNome_file[i]);
//						boolean success = f1.delete();
//						if (!success) {
//							System.out.println("Deletion failed."
//									+ arrayNome_file[i]);
//							// System.exit(0);
//						} else {
//							System.out.println("File " + arrayNome_file[i]
//									+ " deleted.");
//						}
//					}
//				} else {
					System.out.println("UNICA FIRMA");

					System.out
							.print("Inserire l'ALIAS corretto del firmatario: ");
					alias = new BufferedReader(new InputStreamReader(System.in));
					alias_name = alias.readLine();

					System.out
							.print("Inserire il PIN corretto del firmatario: ");
					PIN = new BufferedReader(new InputStreamReader(System.in));
					PIN_num = PIN.readLine();

					enve.pdfsign(fileIn, fileIn.available(), "Rea_son.01", "Loca_tion.01", "Con_tent.01", alias_name, PIN_num, data, fileOut);

					nome_fileOutEnve = docfileN;
					FileInputStream fileOutEnve;
					fileOutEnve = new FileInputStream(nome_fileOutEnve);
					VerifyInfo vi = enve.pdfverify(fileOutEnve, fileOutEnve
							.available());
					System.out
							.println("Numero di firme apportate al documento: "
									+ vi.getSignerCount());

					// THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR SUBJECT
					// DN

					System.out.println("Singer is "
							+ vi.getSigner(0).getSubjectDN());
					Date signTime = vi.getSigner(0).getSigningTime();
					System.out.println("SigningTime is " + signTime);

					fileOutEnve.close();

//				}
			} else {
				if (f.exists() && !estensione_file.toUpperCase().equals("PDF"))
					System.out
							.println("Sono accettati solo file con estensione .PDF!!!");
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
