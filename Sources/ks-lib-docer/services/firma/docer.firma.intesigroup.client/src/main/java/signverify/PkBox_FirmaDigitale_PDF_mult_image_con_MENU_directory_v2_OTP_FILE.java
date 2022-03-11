package signverify;

import it.pkbox.client.Envelope;
import it.pkbox.client.PKBox;
import it.pkbox.client.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;

public class PkBox_FirmaDigitale_PDF_mult_image_con_MENU_directory_v2_OTP_FILE {

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

				String as[] = file.list();

				for (int i = 0; i < as.length; i++) {
					String estensione_file = as[i].substring(
							((as[i].length()) - 3), as[i].length());
					if (estensione_file.toUpperCase().equals("PDF")) {
						count++;
					}
				}

				System.out.println("Numero di file PDF presenti: " + count);

				PKBox pbox = new PKBox();
				String url = "http://192.168.0.39:8080/pkserver/servlet/StampingAntohandler";
				pbox.addServer(url, null, null, null);

				Envelope enve = new Envelope(pbox);

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
					File fileIn = null;
					File fileOut = null;
					File fileImage = null;

					String docfile_Out = null;
					BufferedReader alias = null;
					String alias_name = null;
					BufferedReader pin = null;
					BufferedReader otp = null;
					String pin_num = null;
					String otp_num = null;
					Date data = null;
					String docfile_delete = null;
					String percorso_new_file = null;

					String fname_image = null;

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

					System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
					alias = new BufferedReader(new InputStreamReader(System.in));
					alias_name = alias.readLine();

					System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
					pin = new BufferedReader(new InputStreamReader(System.in));
					pin_num = pin.readLine();

					for (int i = 0; i < as.length; i++) {
						String estensione_file = as[i].substring(((as[i]
								.length()) - 3), as[i].length());
						if (estensione_file.toUpperCase().equals("PDF")) {

							System.out
									.print("INSERIRE IL OTP DEL FIRMATARIO: ");
							otp = new BufferedReader(new InputStreamReader(
									System.in));
							otp_num = otp.readLine();

							File fileN = new File(dir, as[i]);

							if (ultimo_carattere.equals("\\"))
								percorso_completo = (dir + as[i]);
							else {
								percorso_completo = (dir + "\\" + as[i]);
							}

							System.out.println("FILE: " + percorso_completo
									+ "  " + fileN.length() + " byte");

							fileIn = new File(percorso_completo);
							docfile_Out = percorso_completo_new_dir
									+ as[i]
											.substring(0,
													((as[i].length()) - 4))
									+ "_sign.pdf";
							fileOut = new File(docfile_Out);

							fileImage = new File(fname_image);

							enve.pdfsign(fileIn, null, "Rea_son.01",
									"Loca_tion.01", "Con_tent.01", "",
									alias_name, pin_num, otp_num, data,
									fileImage, nPg, nPs, 0, 0, fileOut);
							System.out.println("FILE FIRMATO: " + docfile_Out);
							System.out.println("");
						}

					}

					System.out.print("INSERIRE UN'ALTRA FIRMA? (S/N) ");
					BufferedReader firmatari = new BufferedReader(
							new InputStreamReader(System.in));
					String firm = firmatari.readLine();

					File new_dir = new File(percorso_completo_new_dir);
					String array_elenco_file_new_dir[] = new_dir.list();

					if (firm.toUpperCase().equals("S")) {

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

						System.out.print("INSERIRE L'ALIAS DEL FIRMATARIO: ");
						alias = new BufferedReader(new InputStreamReader(
								System.in));
						alias_name = alias.readLine();

						System.out.print("INSERIRE IL PIN DEL FIRMATARIO: ");
						pin = new BufferedReader(new InputStreamReader(
								System.in));
						pin_num = pin.readLine();

						for (int i = 0; i < array_elenco_file_new_dir.length; i++) {

							System.out
									.print("INSERIRE IL OTP DEL FIRMATARIO: ");
							otp = new BufferedReader(new InputStreamReader(
									System.in));
							otp_num = otp.readLine();

							percorso_new_file = percorso_completo_new_dir
									+ array_elenco_file_new_dir[i];
							System.out.println("FILE DA FIRMARE: "
									+ percorso_new_file);
							fileIn = new File(percorso_new_file);
							String docfile_Out1 = percorso_new_file.substring(
									0, ((percorso_new_file.length()) - 9))
									+ "_addsign.pdf";
							fileOut = new File(docfile_Out1);
							fileImage = new File(fname_image);

							enve.pdfaddsign(fileIn, null,
									"Rea_son.0" + (i + 2), "Loca_tion.0"
											+ (i + 2), "Con_tent.0" + (i + 2),
									"", alias_name, pin_num, otp_num, data,
									null, 0, 0, 0, 0, fileOut);
							System.out
									.println("FileName OUTPUT della firma aggiunta: "
											+ docfile_Out1);

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
