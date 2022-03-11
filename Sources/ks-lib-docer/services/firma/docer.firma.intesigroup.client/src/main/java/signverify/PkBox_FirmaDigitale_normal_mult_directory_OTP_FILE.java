package signverify;

import it.pkbox.client.Envelope;
import it.pkbox.client.PKBox;
import it.pkbox.client.VerifyInfo;
import it.pkbox.client.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;

public class PkBox_FirmaDigitale_normal_mult_directory_OTP_FILE {
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

			if (file != null && file.isDirectory() && file.canRead()) {

				
				PKBox pbox = new PKBox();
				String url = "http://192.168.0.39:8080/pkserver/servlet/StampingAntohandler";
				pbox.addServer(url, null, null, null);

				Envelope enve = new Envelope(pbox);
				String as[] = file.list();

				System.out.println("Numero di file presenti: " + as.length);

				String percorso_completo = null;
				File fileIn;
				File fileOut;
				String docfile_Out = null;
				BufferedReader alias = null;
				String alias_name = null;
				BufferedReader pin = null;
				BufferedReader otp = null;
				String pin_num = null;
				String otp_num = null;
				String docfileVerify = null;
				String docfile_delete = null;
				String docfile_Out1 = null;

				for (int i = 0; i < as.length; i++) {

					File fileN = new File(dir, as[i]);

					if (ultimo_carattere.equals("\\"))
						percorso_completo = (dir + as[i]);
					else {
						percorso_completo = (dir + "\\" + as[i]);
					}
					String estensione_fileOriginale = percorso_completo.substring(percorso_completo.length() - 3,percorso_completo.length());
					System.out.println("");
					System.out.println("FILE: " + percorso_completo + "  "+ fileN.length() + " byte");

					

					System.out.print("INSERIRE PIU' FIRMATARI? (S/N) "+ as[i] + ": ");
					BufferedReader firmatari = new BufferedReader(new InputStreamReader(System.in));
					String firm = firmatari.readLine();

					if (firm.toUpperCase().equals("S")) {

						System.out.print("INSERIRE IL NUMERO DI FIRMATARI PER IL FILE "+ as[i] + ": ");
						BufferedReader num_firmatari = new BufferedReader(new InputStreamReader(System.in));
						String num_firm = num_firmatari.readLine();
						int nFirm = Integer.parseInt(num_firm);

						fileIn = new File(percorso_completo);
						docfile_Out = percorso_completo.substring(0,((percorso_completo.length()) - 4))+ ".p7m";
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
						otp_num = pin.readLine();

						enve.sign(fileIn, null, alias_name, pin_num, otp_num,
								Envelope.implicitMode, Envelope.base64Encoding,
								null, fileOut);

						System.out.println("FILE FIRMATO: " + docfile_Out);
						System.out.println("");

						for (int j = 0; j < (nFirm - 1); j++) {
							
							fileIn = new File(docfile_Out);
							docfile_Out1 = percorso_completo.substring(0,((percorso_completo.length()) - 4))+ "_" + j + "_addsign.p7m";
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
							
							enve.addsign(null, fileIn, null, alias_name,
									pin_num, otp_num, Envelope.base64Encoding,
									null, fileOut);
							
							System.out.println("FileName della firma aggiunta: "+ docfile_Out1);

							docfile_delete = docfile_Out;
							docfile_Out = docfile_Out1;

							docfileVerify = percorso_completo.substring(0,((percorso_completo.length()) - 4))+ "_verify." + estensione_fileOriginale;

							// Delete temporary file
							File f1 = new File(docfile_delete);
							boolean success = f1.delete();
							if (!success) {
								System.out.println("Deletion failed. "+ docfile_delete);
								// System.exit(0);
							} else {
								System.out.println("File" + docfile_delete+ " deleted.");
								System.out.println("");
							}

						}

						File fileOutEnve = new File(docfile_Out1);

						File filedocfileNVerify = new File(docfileVerify);

						
						VerifyInfo vi = enve.verify(null, fileOutEnve, null,
								null, filedocfileNVerify);
						System.out.println("Numero di firme apportate al documento : "+ vi.getSignerCount());

						int index = vi.getSignerCount();
						// LOOP THROUGH THE LIST OF SIGNERS OF THE DATA AND
						// PRINT THEIR SUBJECT DN
						for (int j = 0; j < index; j++) {
							System.out.println("Singer " + (j + 1) + " is "+ vi.getSigner(j).getSubjectDN());
							Date signTime = vi.getSigner(j).getSigningTime();
							System.out.println("SigningTime " + (j + 1)+ " is " + signTime);
						}

						// Delete file verify original
						File file_verify = new File(docfileVerify);
						boolean success_verify = file_verify.delete();
						if (!success_verify) {
							System.out.println("Deletion failed."
									+ docfileVerify);
							// System.exit(0);
						} else {
							System.out.println("File" + docfileVerify
									+ " deleted.");
						}

					} else {
						fileIn = new File(percorso_completo);
						docfile_Out = percorso_completo.substring(0,((percorso_completo.length()) - 4))+ ".p7m";
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

						enve.sign(fileIn, null, alias_name, pin_num, otp_num,
								Envelope.implicitMode, Envelope.base64Encoding,
								null, fileOut);
						
						File fileOutEnve = new File(docfile_Out);
						docfileVerify = percorso_completo.substring(0,((percorso_completo.length()) - 4))+ "_verify." + estensione_fileOriginale;
						File filedocfileNVerify = new File(docfileVerify);
						
						VerifyInfo vi = enve.verify(null, fileOutEnve, null,
								null, filedocfileNVerify);
						
						System.out.println("FILE FIRMATO: " + docfile_Out);
						System.out.println("");

						System.out.println("Numero di firme apportate al documento: "+ vi.getSignerCount());

						// THE LIST OF SIGNERS OF THE DATA AND PRINT THEIR
						// SUBJECT DN

						System.out.println("Singer is "+ vi.getSigner(0).getSubjectDN());
						Date signTime = vi.getSigner(0).getSigningTime();
						System.out.println("SigningTime is " + signTime);

						// Delete file verify original
						File file_verify = new File(docfileVerify);
						boolean success_verify = file_verify.delete();
						if (!success_verify) {
							System.out.println("Deletion failed."+ docfileVerify);
							// System.exit(0);
						} else {
							System.out.println("File" + docfileVerify+ " deleted.");
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
