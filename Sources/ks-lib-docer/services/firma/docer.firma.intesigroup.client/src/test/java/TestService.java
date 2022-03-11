import it.kdm.sign.model.ResultInfo;
import it.kdm.sign.service.SignServiceImplementation;
import it.kdm.sign.service.SignServiceInterface;
import it.pkbox.client.test.XADESTest;


import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class TestService {
	

	static final String KDMDir;
	static final String inputDir;
	static final String outputDir;
	static final String pin;
	static final String otp;
	static final String alias;
	static final String pkboxServerUrl;
	static final String securPin;
	static final String otpAddressAuth;
	static final String otpAddressAdmin;
	
	static{
		InputStream isProps;
		try {
			isProps = Class.forName(XADESTest.class.getName()).getResourceAsStream("config.properties");
			Properties props = new Properties();
			props.load(isProps);
			KDMDir = props.getProperty("KDMDir");
			inputDir = props.getProperty("inputDir");
			outputDir = props.getProperty("outputDir");
			alias = props.getProperty("alias");
			pin = props.getProperty("pin");
			pkboxServerUrl = props.getProperty("pkboxServerUrl");
			securPin = props.getProperty("securPin");
			otpAddressAuth = props.getProperty("otpAddressAuth");
			otpAddressAdmin = props.getProperty("otpAddressAdmin");
			otp = props.getProperty("otp");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Properties non loaded");
		}
	}
	
	//	Firma remota
	//	otpId: remota02
	//	otpProvider: kdm
	//	alias: remota02
	//	pin: 12345678
	//
	//	Firma automatica
	//	alias: auto01
	//	pin: 12345678
	
	public static void main(String[] args) {
		
		SignServiceInterface signService = new SignServiceImplementation();
		Map<String,String> config= new HashMap<String, String>();
		config.put("otpCerthPath", KDMDir + File.separator +"kdm.ssl.cli.time4eid.01.p12");
		config.put("otpCerthKey", pin);
		config.put("otpProvider", "kdm");
		config.put("otpAddressAuth", otpAddressAuth);
		config.put("otpAddressAdmin", otpAddressAdmin);
		config.put("pkBoxServerUrl", pkboxServerUrl);
		config.put("pkBoxSecurPin", securPin);
		
		signService.setConfig( config );
		
//		testOTP ( signService );
		
//		testFirmaXADESAutomatica(signService);
		
		testFirmaXADESAutoSenzaAsterisco( signService );
		
//		testFirmaXADESRemota(signService);
		
		
//		testFirmaCADESConAsterisco ( signService );
		
//		testFirmaCADESSenzaAsteriscoPDF( signService );
		
		/*TEST FIRMA REMOTA CON FILE PDF
		 * 
		 * OK
		 * 
		 * */
//		testFirmaPADESRemota( signService );
		
		/*TEST FIRMA AUTOMATICA CON FILE PDF
		 * 
		 * OK
		 * 
		 * */
//		testFirmaPADESAutomatica(signService);
		
		/*TEST FIRMA AUTOMATICA CON FILE DOC*/
//		testFirmaCADESSenzaAsteriscoDOC( signService );
		
		/*	TEST FIRMA AUTOMATICA CON FILE SINGOLO NON ESISTENTE
		 * 
		 * 	output:  [status=400, info=Il file [C:\Users\annibale\Desktop\KDM\test\inputDir\sample_sample.doc] non esiste]
		 * 
		 * */
//		testFirmaCADESFileNonPresente( signService );
		
		
		/*	TEST FIRMA AUTOMATICA CON ASTERISCO SU INPUT DIR VUOTA
		 * 
		 * 	OUTPUT: Nessun file da firmare
		 * 
		 * */
//		testFirmaCADESFileNonPresenti( signService );
		
		
		
		/* TEST FIRMA AUTOMATICA SU LISTA CON DUE FILE NON ESISTENTI
		 * 
		 * output: [C:\Users\annibale\Desktop\KDM\test\inputDirVuota\sample_sample.doc] non esiste] , [C:\Users\annibale\Desktop\KDM\test\inputDirVuota\secondofile.pdf] non esiste]
		 * 
		 * */
//		testFirmaCADESFilesNonEsistentiInLista( signService );
		
		/* TEST FIRMA AUTOMATICA SENZA FORMATO FIRMA
		 * 
		 * output: formato firma non valido
		 * */
//		testFirmaCADESFormatoFirmaMancante( signService );
		
		
		/* TEST FIRMA AUTOMATICA CON FORMATO FIRMA ERRATO
		 * 
		 * output: formato firma non valido
		 * 
		 * */
//		testFirmaCADESFormatoFirmaErrato( signService );
		
		
		
		/* TEST FIRMA AUTOMATICA CON TIPO FIRMA MANCANTE
		 * 
		 * output: tipo firma non valido
		 * 
		 * */
//		testFirmaCADESTipoFirmaNonSpecificato( signService );
		
		
		
		/* TEST FIRMA AUTOMATICA CON PARAMETRO INPUTDIR MANCANTE
		 * 
		 * output: InputDir non esiste o non e' una directory
		 * 
		 * */
//		testFirmaCADESParametroInputDirMancante( signService );
		

		/* TEST FIRMA AUTOMATICA CON PARAMETRO OUTPUTDIR MANCANTE
		 * 
		 * output:  OutputDir non esiste o non e' una directory
		 * 
		 * */
//		testFirmaCADESParametroOutputDirMancante( signService );
		

		/* TEST FIRMA AUTOMATICA CON PARAMETRO ALIAS MANCANTE
		 * 
		 * output:  You must specify alias parameter
		 * 
		 * */
//		testFirmaCADESConAliasMancante( signService );
		
		
		/* TEST FIRMA AUTOMATICA CON PARAMETRO PIN MANCANTE
		 * 
		 * output:  You must specify pin parameter
		 * 
		 * */
//		testFirmaCADESConPINMancante( signService );
		
		

	}


	private static void testFirmaXADESAutoSenzaAsterisco( SignServiceInterface signService) {
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "XADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		List<String> documenti = Arrays.asList( "sample2.xml" );
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, null , opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}

	@SuppressWarnings("unused")
	private static void testOTP ( SignServiceInterface signService ) {
		ResultInfo resultInfo = signService.richiestaOTP( alias, "1111");
		System.out.println( resultInfo );
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaPADESRemota( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "PADES");
		opzioni.put( "tipoFirma" , "REMOTA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	@SuppressWarnings("unused")
	private static void testFirmaPADESAutomatica( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "PADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESConAsterisco( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "CADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String otp = "";
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESSenzaAsteriscoPDF( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "CADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String otp = "";
		documenti = Arrays.asList( "example.pdf" );
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESSenzaAsteriscoDOC( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "CADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String otp = "";
		documenti = Arrays.asList( "sample.doc" );
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESFileNonPresente( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "CADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String otp = "";
		documenti = Arrays.asList( "sample_sample.doc" );
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESFileNonPresenti( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "CADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String otp = "";
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESFilesNonEsistentiInLista( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "CADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String otp = "";
		documenti = Arrays.asList( "sample_sample.doc" ,"secondofile.pdf" );
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESFormatoFirmaMancante( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String otp = "";
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESFormatoFirmaErrato( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "DCSDC");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String otp = "";
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESTipoFirmaNonSpecificato( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "CADES");
		opzioni.put( "tipoFirma" , "");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String otp = "";
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESParametroInputDirMancante( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "CADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , "");
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String otp = "";
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESParametroOutputDirMancante( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "CADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , "");
		String alias = "auto01";
		String otp = "";
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESConAliasMancante( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "CADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "";
		String otp = "";
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaCADESConPINMancante( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "CADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String pin ="";
		String otp = "";
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaXADESRemota( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "XADES");
		opzioni.put( "tipoFirma" , "REMOTA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "remota02";
		String pin = TestService.pin;
		String otp = TestService.otp;
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	@SuppressWarnings("unused")
	private static void testFirmaXADESAutomatica( SignServiceInterface signService){
		List<String> documenti = Arrays.asList( "*" );
		Map<String, String> opzioni = new HashMap<String, String>();
		opzioni.put( "formatoFirma" , "XADES");
		opzioni.put( "tipoFirma" , "AUTOMATICA");
		opzioni.put( "inputDir" , inputDir);
		opzioni.put( "outputDir" , outputDir);
		String alias = "auto01";
		String pin = TestService.pin;
		String otp = null;
		Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, otp, opzioni, documenti);
		for (Map.Entry<String, ResultInfo> entry : result.entrySet() ) {
		    System.out.println( entry );
		}
	}
	
	
}
