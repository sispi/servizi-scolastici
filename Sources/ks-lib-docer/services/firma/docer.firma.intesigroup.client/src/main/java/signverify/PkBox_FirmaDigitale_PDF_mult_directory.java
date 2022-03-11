package signverify;

import it.pkbox.client.*;

import java.io.*;
import java.util.Date;

public class PkBox_FirmaDigitale_PDF_mult_directory {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String versionePkBox = Version.getVersion();
			System.out.println("Stai usando la versione di PkBox: "
					+ versionePkBox);
			System.out.print("Inserire il percorso della directory: ");
			BufferedReader directory = new BufferedReader(
					new InputStreamReader(System.in));
			String dir = directory.readLine();
			File file = new File(dir);
			String ultimo_carattere = dir.substring(((dir.length()) - 1), dir
					.length());
			int count = 0;

			// System.out.println("Ultimo carattere:      " + ultimo_carattere);

			if (file != null && file.isDirectory() && file.canRead()) {

				String as[] = file.list();

				for (int i = 0; i < as.length; i++) { // questo ciclo serve per
														// contare SOLO i file
														// che hanno
														// l'estensione .PDF
					String estensione_file = as[i].substring(
							((as[i].length()) - 3), as[i].length());
					if (estensione_file.toUpperCase().equals("PDF")) {
						count++;
					}
				}

				System.out.println("Numero di file PDF presenti: " + count);

				String percorso_completo = null;
				FileInputStream fileIn;
				FileOutputStream fileOut;
				String docfile_Out = null;
				BufferedReader alias = null;
				String alias_name = null;
				BufferedReader pin = null;
				String pin_num = null;
				Date data = null;
				String docfile_delete = null;
				String docfile_Out1 = null;

				PKBox pbox = new PKBox();
				String url = "http://localhost:8080/pkserver/servlet/prova10handler";
				pbox.addServer(url, null, null, null);

				Envelope enve = new Envelope(pbox);

				for (int i = 0; i < as.length; i++) {

					String estensione_file = as[i].substring(
							((as[i].length()) - 3), as[i].length());
					if (estensione_file.toUpperCase().equals("PDF")) {

						File fileN = new File(dir, as[i]);

						if (ultimo_carattere.equals("\\"))
							percorso_completo = (dir + as[i]);
						else {
							percorso_completo = (dir + "\\" + as[i]);
						}

						System.out.println("FILE: " + percorso_completo + "  "
								+ fileN.length() + " byte");

						System.out.print("INSERIRE PIU' FIRMATARI? (S/N) ");
						BufferedReader firmatari = new BufferedReader(
								new InputStreamReader(System.in));
						String firm = firmatari.readLine();

						if (firm.toUpperCase().equals("S")) {

							System.out
									.print("INSERIRE IL NUMERO DI FIRMATARI PER IL FILE "
											+ as[i] + ": ");
							BufferedReader num_firmatari = new BufferedReader(
									new InputStreamReader(System.in));
							String num_firm = num_firmatari.readLine();
							int nFirm = Integer.parseInt(num_firm);

							fileIn = new FileInputStream(percorso_completo);
							docfile_Out = percorso_completo.substring(0,
									((percorso_completo.length()) - 4))
									+ "_sign.pdf";
							fileOut = new FileOutputStream(docfile_Out);

							System.out.println("FIRMA 1: ");
							System.out
									.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
							alias = new BufferedReader(new InputStreamReader(
									System.in));
							alias_name = alias.readLine();

							System.out
									.print("INSERIRE IL PIN DEL FIRMATARIO: ");
							pin = new BufferedReader(new InputStreamReader(
									System.in));
							pin_num = pin.readLine();

							enve.pdfsign(fileIn, fileIn.available(),
									"Rea_son.01", "Loca_tion.01",
									"Con_tent.01", alias_name, pin_num, data,
									fileOut);
							System.out.println("FILE FIRMATO: " + docfile_Out);
							System.out.println("");

							fileIn.close();
							fileOut.close();

							for (int j = 0; j < (nFirm - 1); j++) {
								fileIn = new FileInputStream(docfile_Out);
								docfile_Out1 = percorso_completo.substring(0,
										((percorso_completo.length()) - 4))
										+ "_" + j + "_addsign.pdf";
								fileOut = new FileOutputStream(docfile_Out1);

								System.out.println("FIRMA " + (j + 2) + ": ");
								System.out
										.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
								alias = new BufferedReader(
										new InputStreamReader(System.in));
								alias_name = alias.readLine();

								System.out
										.print("INSERIRE IL PIN DEL FIRMATARIO: ");
								pin = new BufferedReader(new InputStreamReader(
										System.in));
								pin_num = pin.readLine();

								enve.pdfaddsign(fileIn, fileIn.available(),
										"Rea_son.0" + (j + 2), "Loca_tion.0"
												+ (j + 2), "Con_tent.0"
												+ (j + 2), alias_name, pin_num,
										data, fileOut);
								System.out
										.println("FileName della firma aggiunta: "
												+ docfile_Out1);

								docfile_delete = docfile_Out;
								docfile_Out = docfile_Out1;

								fileIn.close();
								fileOut.close();

								// nome_fileOutEnve = docfile_Out1;

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
							fileIn = new FileInputStream(percorso_completo);
							docfile_Out = percorso_completo.substring(0,
									((percorso_completo.length()) - 4))
									+ "_sign.pdf";
							fileOut = new FileOutputStream(docfile_Out);

							System.out.println("FIRMA 1: ");
							System.out
									.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
							alias = new BufferedReader(new InputStreamReader(
									System.in));
							alias_name = alias.readLine();

							System.out
									.print("INSERIRE IL PIN DEL FIRMATARIO: ");
							pin = new BufferedReader(new InputStreamReader(
									System.in));
							pin_num = pin.readLine();

							/*
							 * PKBox pbox = new PKBox(); String url =
							 * "http://localhost:8080/pkserver/servlet/prova10handler"
							 * ; pbox.addServer(url, null, null, null);
							 * 
							 * Envelope enve = new Envelope(pbox);
							 */

							enve.pdfsign(fileIn, fileIn.available(),
									"Rea_son.01", "Loca_tion.01",
									"Con_tent.01", alias_name, pin_num, data,
									fileOut);

							System.out.println("FILE FIRMATO: " + docfile_Out);
							System.out.println("");

							fileIn.close();
							fileOut.close();

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
