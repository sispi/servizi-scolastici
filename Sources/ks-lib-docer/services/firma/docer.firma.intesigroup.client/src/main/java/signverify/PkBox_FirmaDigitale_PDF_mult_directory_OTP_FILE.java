package signverify;

import java.io.BufferedReader;
import java.io.File;

import java.io.InputStreamReader;
import java.util.Date;

import it.pkbox.client.*;

public class PkBox_FirmaDigitale_PDF_mult_directory_OTP_FILE {
	/**
	 * @param args
	 */

	public static void main(String[] args) {

		try {
			String versionePkBox = Version.getVersion();
			System.out.println("Stai usando la versione di PkBox: "+ versionePkBox);
			System.out.print("Inserire il percorso della directory: ");
			BufferedReader directory = new BufferedReader(new InputStreamReader(System.in));
			String dir = directory.readLine();
			File file = new File(dir);
			String ultimo_carattere = dir.substring(((dir.length()) - 1), dir.length());

			int count = 0;

			if (file != null && file.isDirectory() && file.canRead()) {

				String as[] = file.list();
				// count the number file into Directory
				for (int i = 0; i < as.length; i++) {
					String estensione_file = as[i].substring(
							((as[i].length()) - 3), as[i].length());
					if (estensione_file.toUpperCase().equals("PDF")) {
						count++;
					}
				}
				System.out.println("Numero di file PDF presenti: " + count);

				String percorso_completo = null;
				File fileIn = null;
				File fileOut = null;
				String docfile_Out = null;
				BufferedReader alias = null;
				String alias_name = null;
				BufferedReader pin = null;
				String pin_num = null;
				BufferedReader otp = null;
				String otp_num = null;

				Date data = null;
				String docfile_delete = null;

				String docfile_Out1 = null;

				PKBox pbox = new PKBox();

				String url = "http://192.168.0.39:8080/pkserver/servlet/StampingAntohandler";
				pbox.addServer(url, null, null, null);

				Envelope enve = new Envelope(pbox);

				for (int i = 0; i < as.length; i++) {

					String estensione_file = as[i].substring(((as[i].length()) - 3), as[i].length());
					if (estensione_file.toUpperCase().equals("PDF")) {
						File fileN = new File(dir, as[i]);
						if (ultimo_carattere.equals("\\"))
							percorso_completo = (dir + as[i]);
						else {
							percorso_completo = (dir + "\\" + as[i]);
						}

						System.out.println("FILE: " + percorso_completo + "Length:  " + fileN.length() + " byte");
						System.out.print("INSERIRE PIU' FIRMATARI? (S/N) ");
						BufferedReader firmatari = new BufferedReader( new InputStreamReader(System.in));
						String firm = firmatari.readLine();

						if (firm.toUpperCase().equals("S")) {

							System.out.print("INSERIRE IL NUMERO DI FIRMATARI PER IL FILE "+ as[i] + ": ");
							BufferedReader num_firmatari = new BufferedReader(new InputStreamReader(System.in));
							String num_firm = num_firmatari.readLine();
							int nFirm = Integer.parseInt(num_firm);

							fileIn = new File(percorso_completo);
							docfile_Out = percorso_completo.substring(0,((percorso_completo.length()) - 4)) + "_sign.pdf";
							fileOut = new File(docfile_Out);

							System.out.println("FIRMA 1: ");
							System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
							alias = new BufferedReader(new InputStreamReader(System.in));
							alias_name = alias.readLine();

							System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
							pin = new BufferedReader(new InputStreamReader(System.in));
							pin_num = pin.readLine();

							System.out.print("INSERIRE IL OTP DEL FIRMATARIO: ");
							otp = new BufferedReader(new InputStreamReader(System.in));
							otp_num = otp.readLine();

							enve.pdfsign(fileIn, null, "Rea_son.01",
									"Loca_tion.01", "Con_tent.01", "",
									alias_name, pin_num, otp_num, data, null,
									0, 0, 0, 0, fileOut);
							System.out.println("FILE FIRMATO: " + docfile_Out);
							System.out.println("");

							for (int j = 0; j < (nFirm - 1); j++) {

								fileIn = new File(docfile_Out);
								docfile_Out1 = percorso_completo.substring(0,((percorso_completo.length()) - 4))+ "_" + j + "_addsign.pdf";
								fileOut = new File(docfile_Out1);

								System.out.println("FIRMA " + (j + 2) + ": ");
								System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
								alias = new BufferedReader(new InputStreamReader(System.in));
								alias_name = alias.readLine();

								System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
								pin = new BufferedReader(new InputStreamReader(System.in));
								pin_num = pin.readLine();

								System.out.print("INSERIRE IL OTP DEL FIRMATARIO: ");
								otp = new BufferedReader(new InputStreamReader(System.in));
								otp_num = otp.readLine();

								enve.pdfaddsign(fileIn, null, "Rea_son.0"
										+ (j + 2), "Loca_tion.0" + (j + 2),
										"Con_tent.0" + (j + 2), "", alias_name,
										pin_num, otp_num, data, null, 0, 0, 0,
										0, fileOut);

								System.out.println("FileName della firma aggiunta: "+ docfile_Out1);
								docfile_delete = docfile_Out;
								docfile_Out = docfile_Out1;

								File f1 = new File(docfile_delete);
								boolean success = f1.delete();
								if (!success) {
									System.out.println("Deletion failed."
											+ docfile_delete);
									// System.exit(0);
								} else {
									System.out.println("File" + docfile_delete
											+ " deleted.");
									System.out.println("");
								}
							}

						} else {
							fileIn = new File(percorso_completo);
							docfile_Out = percorso_completo.substring(0,((percorso_completo.length()) - 4))+ "_sign.pdf";
							fileOut = new File(docfile_Out);

							System.out.println("FIRMA 1: ");
							System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
							alias = new BufferedReader(new InputStreamReader(System.in));
							alias_name = alias.readLine();

							System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
							pin = new BufferedReader(new InputStreamReader(System.in));
							pin_num = pin.readLine();

							System.out.print("INSERIRE IL OTP DEL FIRMATARIO: ");
							otp = new BufferedReader(new InputStreamReader(System.in));
							otp_num = otp.readLine();

							enve.pdfsign(fileIn, null, "Rea_son.01",
									"Loca_tion.01", "Con_tent.01", "",
									alias_name, pin_num, otp_num, data, null,
									0, 0, 0, 0, fileOut);

							System.out.println("FILE FIRMATO: " + docfile_Out);
							System.out.println("");

						}
					}
				}

			} else {

				if (!file.isDirectory())
					System.out.println("La directory non esiste. ");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Execution done.");
	}

}
