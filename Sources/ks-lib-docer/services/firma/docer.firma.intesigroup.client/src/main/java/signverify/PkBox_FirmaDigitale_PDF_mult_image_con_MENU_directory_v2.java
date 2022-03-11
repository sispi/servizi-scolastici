package signverify;

import it.pkbox.client.*;

import java.io.*;
import java.util.Date;

public class PkBox_FirmaDigitale_PDF_mult_image_con_MENU_directory_v2 {
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

			if (file != null && file.isDirectory() && file.canRead()) {
				
				PKBox pbox = new PKBox();
				String url = "http://localhost:8080/pkserver/servlet/prova10handler";
				pbox.addServer(url, null, null, null);
				Envelope enve = new Envelope(pbox);

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

				System.out
						.print("Inserire il nome della directory dove andranno creati i file firmati: ");
				BufferedReader directory_new = new BufferedReader(
						new InputStreamReader(System.in));
				String dir_new = directory_new.readLine();
				String ultimo_carattere_new_dir = dir_new.substring(((dir_new
						.length()) - 1), dir_new.length());

				String percorso_completo_new_dir = null;

				if (ultimo_carattere.equals("\\")
						&& ultimo_carattere_new_dir.equals("\\"))
					percorso_completo_new_dir = (dir + dir_new);

				if (ultimo_carattere.equals("\\")
						&& !ultimo_carattere_new_dir.equals("\\"))
					percorso_completo_new_dir = (dir + dir_new + "\\");

				if (!ultimo_carattere.equals("\\")
						&& !ultimo_carattere_new_dir.equals("\\"))
					percorso_completo_new_dir = (dir + "\\" + dir_new + "\\");

				if (!ultimo_carattere.equals("\\")
						&& ultimo_carattere_new_dir.equals("\\"))
					percorso_completo_new_dir = (dir + "\\" + dir_new);

				boolean success_new_dir = (new File(percorso_completo_new_dir))
						.mkdir();

				if (success_new_dir) {
					System.out.println("CARTELLA " + dir_new + " CREATA");

					System.out
							.println("PERCORSO COMPLETO DELLA CARTELLA CREATA: "
									+ percorso_completo_new_dir);

					String percorso_completo = null;
					FileInputStream fileIn;
					FileOutputStream fileOut;
					FileInputStream fileImage;

					String docfile_Out = null;
					BufferedReader alias = null;
					String alias_name = null;
					BufferedReader pin = null;
					String pin_num = null;
					Date data = null;
					String docfile_delete = null;
					String percorso_new_file = null;
					String signerPin = null;
					String fname_image = null;

					System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
					alias = new BufferedReader(new InputStreamReader(System.in));
					alias_name = alias.readLine();

					System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
					pin = new BufferedReader(new InputStreamReader(System.in));
					pin_num = pin.readLine();

					System.out
							.print("Inserire l'immagine da utilizzare per la prima firma digitale: ");
					BufferedReader in1 = new BufferedReader(
							new InputStreamReader(System.in));
					fname_image = in1.readLine();

					System.out
							.println("Scegliere in quale pagina inserire l'immagine");
					System.out.println("Valori permessi: ");
					System.out.println("                0 or 1 = first page ");
					System.out.println("                < 0 = last page ");
					System.out.println("                N = page number");
					System.out.print("                SCELTA: ");
					BufferedReader numPage = new BufferedReader(
							new InputStreamReader(System.in));
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
					BufferedReader numPosition = new BufferedReader(
							new InputStreamReader(System.in));
					String numPosizione = numPosition.readLine();
					int nPs = Integer.parseInt(numPosizione);

					for (int i = 0; i < as.length; i++) {
						String estensione_file = as[i].substring(((as[i]
								.length()) - 3), as[i].length());
						if (estensione_file.toUpperCase().equals("PDF")) {

							File fileN = new File(dir, as[i]);

							if (ultimo_carattere.equals("\\"))
								percorso_completo = (dir + as[i]);
							else {
								percorso_completo = (dir + "\\" + as[i]);
							}

							System.out.println("FILE: " + percorso_completo
									+ "  " + fileN.length() + " byte");

							fileIn = new FileInputStream(percorso_completo);
							docfile_Out = percorso_completo_new_dir
									+ as[i]
											.substring(0,
													((as[i].length()) - 4))
									+ "_sign.pdf";
							fileOut = new FileOutputStream(docfile_Out);
                            /*
							PKBox pbox = new PKBox();
							String url = "http://localhost:8080/pkserver/servlet/prova10handler";
							pbox.addServer(url, null, null, null);
							Envelope enve = new Envelope(pbox);
                            */
							fileImage = new FileInputStream(fname_image);

							enve.pdfsign(fileIn, fileIn.available(),
									"Rea_son.01", "Loca_tion.01",
									"Customer_info_01", "Con_tent.01",
									alias_name, pin_num, signerPin, data,
									fileImage, fileImage.available(), nPg, nPs,
									0, 0, fileOut);

							System.out.println("FILE FIRMATO: " + docfile_Out);
							System.out.println("");

							fileImage.close();
							fileIn.close();
							fileOut.close();
						}

					}

					System.out.print("INSERIRE UN'ALTRA FIRMA? (S/N) ");
					BufferedReader firmatari = new BufferedReader(
							new InputStreamReader(System.in));
					String firm = firmatari.readLine();

					File new_dir = new File(percorso_completo_new_dir);
					String array_elenco_file_new_dir[] = new_dir.list();

					if (firm.toUpperCase().equals("S")) {
						System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
						alias = new BufferedReader(new InputStreamReader(
								System.in));
						alias_name = alias.readLine();

						System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
						pin = new BufferedReader(new InputStreamReader(
								System.in));
						pin_num = pin.readLine();

						System.out
								.print("Inserire un'altra immagine da utilizzare per la firma digitale aggiuntiva: ");
						BufferedReader in2 = new BufferedReader(
								new InputStreamReader(System.in));
						fname_image = in2.readLine();

						System.out
								.println("Scegliere in quale pagina inserire l'immagine");
						System.out.println("Valori permessi: ");
						System.out
								.println("                0 or 1 = first page ");
						System.out.println("                < 0 = last page ");
						System.out.println("                N = page number");
						System.out.print("                SCELTA: ");
						numPage = new BufferedReader(new InputStreamReader(
								System.in));
						numPg = numPage.readLine();
						nPg = Integer.parseInt(numPg);

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
						numPosition = new BufferedReader(new InputStreamReader(
								System.in));
						numPosizione = numPosition.readLine();
						nPs = Integer.parseInt(numPosizione);

						for (int i = 0; i < array_elenco_file_new_dir.length; i++) {
							percorso_new_file = percorso_completo_new_dir
									+ array_elenco_file_new_dir[i];
							System.out.println("FILE DA FIRMARE: "
									+ percorso_new_file);
							fileIn = new FileInputStream(percorso_new_file);
							String docfile_Out1 = percorso_new_file.substring(
									0, ((percorso_new_file.length()) - 9))
									+ "_addsign.pdf";
							fileOut = new FileOutputStream(docfile_Out1);
							fileImage = new FileInputStream(fname_image);

							/*
							PKBox pbox = new PKBox();
							String url = "http://localhost:8080/pkserver/servlet/prova10handler";
							pbox.addServer(url, null, null, null);

							Envelope enve = new Envelope(pbox);
                            */
							enve.pdfaddsign(fileIn, fileIn.available(),
									"Rea_son.0" + (i + 2), "Loca_tion.0"
											+ (i + 2), "Customer_info_0"
											+ (i + 2), "Con_tent.0" + (i + 2),
									alias_name, pin_num, signerPin, data,
									fileImage, fileImage.available(), nPg, nPs,
									0, 0, fileOut);
							System.out
									.println("FileName OUTPUT della firma aggiunta: "
											+ docfile_Out1);

							fileIn.close();
							fileOut.close();
							fileImage.close();

							docfile_delete = percorso_new_file;

							File f1 = new File(docfile_delete);
							boolean success = f1.delete();
							if (!success) {
								System.out.println("Deletion failed."
										+ docfile_delete);
								// System.exit(0);
							} else {
								System.out.println("File: " + docfile_delete
										+ " deleted.");
								System.out.println("");
							}
						}
					}
				} else
					System.out.println("Impossibile creare: " + dir_new);

			} else if (!file.isDirectory())
				System.out.println("La directory non esiste. ");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Execution done.");
	}
}
