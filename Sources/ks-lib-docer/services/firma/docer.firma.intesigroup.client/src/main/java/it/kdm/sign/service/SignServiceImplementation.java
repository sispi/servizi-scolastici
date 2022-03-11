package it.kdm.sign.service;

import com.intesigroup.service.Time4eIDAdminService;
import com.intesigroup.service.Time4eIDAuthService;
import com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.LockOutputSoap;
import com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.UnlockToken;
import com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub.UnlockTokenResponse;
import com.intesigroup.ws.time4eid.Time4EIDAuthenticationServiceStub.PushOTP;
import com.intesigroup.ws.time4eid.Time4EIDAuthenticationServiceStub.PushOTPResponse;
import com.intesigroup.ws.time4eid.Time4EIDAuthenticationServiceStub.Result;
import it.kdm.sign.enums.FormatoFirma;
import it.kdm.sign.enums.TipoFile;
import it.kdm.sign.enums.TipoFirma;
import it.kdm.sign.exceptions.DirectoryException;
import it.kdm.sign.exceptions.FormatoFirmaException;
import it.kdm.sign.exceptions.ListaDocumentiException;
import it.kdm.sign.exceptions.TipoFirmaException;
import it.kdm.sign.factory.DocumentSignerFactory;
import it.kdm.sign.factory.SignerStrategy;
import it.kdm.sign.model.ResultInfo;
import it.pkbox.client.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class SignServiceImplementation implements SignServiceInterface{

	private Map<String, String> config;
	private static final Logger logger = LogManager.getLogger(SignServiceInterface.class);

	@Override
	public void setConfig(Map<String, String> config) {
		this.config = config;
	}

	@Override
	public ResultInfo sbloccaOTP( String otpId ) {

		String certhPath = null;
		String certhKey = null;
		String otpProvider = null;
		String otpAddress = null;

		ResultInfo resultInfo = new ResultInfo ();

		if ( config == null ) {
			resultInfo.setStatus( 100 );
			resultInfo.setInfo( "mappa parametri di configurazioni non inizializzata." );

		} else {

			if ( otpId == null || "".equals(otpId.trim()) ) {
				resultInfo.setStatus( 200 );
				resultInfo.setInfo( "otpId non valorizzato" );

			} else {

				try {
					certhPath = readConfigProperty ( "otpCerthPath" );
					certhKey = readConfigProperty ( "otpCerthKey" );
					otpProvider = readConfigProperty ( "otpProvider" );
					otpAddress = readConfigProperty ( "otpAddressAdmin");


					Time4eIDAdminService t4eidAdmin = new Time4eIDAdminService(otpAddress, certhPath, certhKey);

					UnlockToken pushOTP = new UnlockToken();
					pushOTP.setOtpProvider(otpProvider);
					pushOTP.setOtpId(otpId);

					UnlockTokenResponse pushOTPResponse = t4eidAdmin.getStub().unlockToken( pushOTP );


					LockOutputSoap result = pushOTPResponse.get_return();
					resultInfo.setStatus( Integer.parseInt( result.getErrorCode() ) );
					resultInfo.setInfo( result.getErrorMessage() );

				} catch ( Exception ex ) {
					resultInfo.setStatus( 300 );
					resultInfo.setInfo( ex.getMessage() );
				}

			}
		}
		return resultInfo;
	}

	@Override
	public ResultInfo richiestaOTP(String otpId, String pin) {

		String certhPath = null;
		String certhKey = null;
		String otpProvider = null;
		String otpAddress = null;

		ResultInfo resultInfo = new ResultInfo ();

		if ( config == null ) {
			resultInfo.setStatus( 100 );
			resultInfo.setInfo( "mappa parametri di configurazioni non inizializzata." );

		} else {

			if ( otpId == null || "".equals(otpId.trim()) ) {
				resultInfo.setStatus( 200 );
				resultInfo.setInfo( "otpId non valorizzato" );

			} else {

				try {
					certhPath = readConfigProperty ( "otpCerthPath" );
					certhKey = readConfigProperty ( "otpCerthKey" );
					otpProvider = readConfigProperty ( "otpProvider" );
					otpAddress = readConfigProperty ( "otpAddressAuth");

					Time4eIDAuthService t4eidAuth = new Time4eIDAuthService( otpAddress, certhPath, certhKey);

					PushOTP pushOTP = new PushOTP();
					pushOTP.setOtpProvider(otpProvider);
					pushOTP.setOtpId(otpId);
					pushOTP.setPin(pin);

					PushOTPResponse pushOTPResponse = t4eidAuth.getStub().pushOTP( pushOTP );

					Result result = pushOTPResponse.get_return();
					resultInfo.setStatus( Integer.parseInt( result.getErrorCode() ) );
					resultInfo.setInfo( result.getErrorMessage() );

				} catch ( Exception ex ) {
					resultInfo.setStatus( 300 );
					resultInfo.setInfo( ex.getMessage() );
				}

			}
		}
		return resultInfo;
	}

	private String readConfigProperty (String key) {
		if ( config.containsKey( key ) ) {
			return config.get( key );
		}
		throw new RuntimeException ( "property [" + key + "] mancate" );
	}

	@Override
	public Map<String, ResultInfo> firmaDocumenti(String alias, String pin, String OTP, Map<String, String> opzioni, List<String> documenti) {

		//CADES --- .p7m --- metodo: sign
		//PADES --- .pdf --- metodo: pdfSign
		//XADES ---  --- metodo: 

		Map<String, ResultInfo> returnValue = new HashMap<String, ResultInfo>();

		String pkboxServerUrl = readConfigProperty( "pkBoxServerUrl" ); //Non puo essere vuota!!!  
		String securPin = readConfigProperty( "pkBoxSecurPin" ); //Non puo essere vuota!!!  
		String serviceUserId = readConfigProperty( "serviceUserId" ); //Non puo essere vuota!!!
		String customerInfo = readConfigProperty( "customerInfo" ); //Non puo essere vuota!!!

		String formatoFirma = opzioni.get( "formatoFirma" ); // [XADES,PADES,CADES]
		String tipoFirma = opzioni.get( "tipoFirma" ); // [LOCALE, REMOTA, AUTOMATICA]
		String inputDir = opzioni.get( "inputDir" ); //Non puo essere vuota!!!
		String outputDir = opzioni.get( "outputDir" ); //Non puo essere vuota!!!

		File fileInputDir = null;
		File fileOutputDir = null;

		if ( documenti == null || documenti.isEmpty()) {
			throw new ListaDocumentiException ( "lista documenti vuota" );
		} else if ( FormatoFirma.getEnum( formatoFirma.toUpperCase()) == null ) {
			throw new FormatoFirmaException( "formato firma non valido" );
		} else if (  TipoFirma.getEnum( tipoFirma.toUpperCase()) == null  ) {
			throw new TipoFirmaException ( "tipo firma non valido" );
		} else {
			fileInputDir = new File ( inputDir );
			fileOutputDir = new File ( outputDir );
			if ( !fileInputDir.exists() || !fileInputDir.isDirectory()  ) {
				throw new DirectoryException( "InputDir non esiste o non e' una directory" );
			} else if ( !fileInputDir.canRead() ) {
				throw new DirectoryException ( "Non e' possibile leggere dalla cartella di input" );
			}
			if ( !fileInputDir.exists() || !fileOutputDir.isDirectory() ) {
				throw new DirectoryException ( "OutputDir non esiste o non e' una directory" );
			} else if ( !fileInputDir.canWrite() ) {
				throw new DirectoryException ( "Non e' possibile scrivere nella cartella di output" );
			}
		}

		SignerStrategy signer = null;
		boolean firmaTuttiIFile = documenti.size() == 1 && "*".equalsIgnoreCase(documenti.get(0) );


		logger.debug( "tipoFirma: " + tipoFirma );
		logger.debug( "firmaTuttiIFile: " + firmaTuttiIFile );

		List<String> documentiValidi = new ArrayList<String>();
		if ( firmaTuttiIFile ) {
			String as[] = fileInputDir.list();
			for(int i = 0; i < as.length; i++) {
				String fullPathFile = inputDir.concat( File.separator ).concat( as[i] );
				logger.debug( "fullPathFile: " + fullPathFile );
				String estensioneFile = FilenameUtils.getExtension(fullPathFile);
//				if( TipoFile.getEnum( estensioneFile.toUpperCase() ) != null ){
				if ( FormatoFirma.PADES.getFormato().equalsIgnoreCase( formatoFirma ) && !TipoFile.PDF.getTipoFile().equalsIgnoreCase( estensioneFile )  ) {
					returnValue.put( as[i], new ResultInfo (110 ,"la firma PADES è applicabile solo a file PDF") );
				} else if (  FormatoFirma.XADES.getFormato().equalsIgnoreCase( formatoFirma ) && !TipoFile.XML.getTipoFile().equalsIgnoreCase( estensioneFile )  ) {
					returnValue.put( as[i], new ResultInfo (120 ,"la firma XADES è applicabile solo a file XML") );
				} else {
					documentiValidi.add( fullPathFile );
				}
//				} else {
//					returnValue.put( as[i], new ResultInfo (100 ,"formato file non valido") );
//				}
			}
		} else {

			for ( String documento : documenti ) {
				String fullPathFile = inputDir.concat( File.separator ).concat( documento );
				logger.debug( "fullPathFile: " + fullPathFile );
				String estensioneFile = FilenameUtils.getExtension(fullPathFile);
//				if( TipoFile.getEnum(estensioneFile.toUpperCase()) != null ){
				if ( FormatoFirma.PADES.getFormato().equalsIgnoreCase( formatoFirma ) && !TipoFile.PDF.getTipoFile().equalsIgnoreCase( estensioneFile )  ) {
					returnValue.put( documento, new ResultInfo (110 ,"la firma PADES è applicabile solo a file PDF") );
				} else if (  FormatoFirma.XADES.getFormato().equalsIgnoreCase( formatoFirma ) && !TipoFile.XML.getTipoFile().equalsIgnoreCase( estensioneFile )  ) {
					returnValue.put( documento, new ResultInfo (120 ,"la firma XADES è applicabile solo a file XML") );
				} else {
					documentiValidi.add( fullPathFile );
				}
//				} else {
//					returnValue.put( documento, new ResultInfo (100 ,"formato file non valido") );
//				}
			}
		}
		if(documentiValidi.size() ==0 ){
			throw new RuntimeException("Nessun file da firmare");
		}
		PKBox pbox = null;
		Envelope enve = null;
		try {
			pbox = new PKBox();
			pbox.addServer(pkboxServerUrl, null, null, null);
			pbox.setTimeout( 3600 );
			pbox.setSecurePINCert( securPin );
			enve = new Envelope(pbox);
		} catch ( Exception ex ) {
			throw new RuntimeException ( ex.getMessage() );
		}
		String authData;
		try {
			logger.debug( "file da firmare: {}", documentiValidi.size() );
			authData = enve.startTransaction(serviceUserId, alias, pin, OTP, documentiValidi.size() );
		} catch ( Exception ex ) {
			throw new RuntimeException ( ex.getMessage() );
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		for (String currentFile : documentiValidi ) {

			File fin = new File(currentFile);
			String indexUnique = String.valueOf(documentiValidi.indexOf(currentFile));
			if ( fin.exists() ) {
				File fout = null;
				String fileName = FilenameUtils.getBaseName(currentFile);
				String extFile =  FilenameUtils.getExtension(currentFile);
				String outFileName = null;

				if ( "PADES".equalsIgnoreCase( formatoFirma ) || "XADES".equalsIgnoreCase(formatoFirma) ) {
					outFileName = outputDir.concat( File.separator ).concat( fileName ).concat("_").concat( sdf.format(new Date())).concat(indexUnique).concat(".").concat( FilenameUtils.getExtension(currentFile) );
				} else if ( "CADES".equalsIgnoreCase( formatoFirma ) ) {
					outFileName = outputDir.concat( File.separator ).concat(fileName).concat("_").concat( sdf.format(new Date())).concat(indexUnique).concat(".").concat(extFile).concat(".p7m");
				}
				logger.debug( outFileName );
				fout = new File( outFileName );
				if(  "XADES".equalsIgnoreCase(formatoFirma) ){
					pbox = null;
					XMLEnvelope xmlEnve = null;
					try {
						pbox = new PKBox();
						pbox.addServer(pkboxServerUrl, null, null, null);
						pbox.setTimeout( 3600 );
						pbox.setSecurePINCert( securPin );
						xmlEnve = new XMLEnvelope(pbox);
						XMLReference[] references = new XMLReference[1];
						references[0] = new XMLReference(null, false, new XMLDataObjectFormat(), null, "", null, false, true);
						if(TipoFirma.REMOTA.getTipoFirma().equalsIgnoreCase(tipoFirma)){
							logger.debug( "firma remota XADES" );
							signer = DocumentSignerFactory.getInstance( TipoFirma.REMOTA );
							signer.xmlsign(references, fin, alias, pin, authData, xmlEnve, returnValue, fout);
						}else{
							//tipofirma = "AUTOMATICA" 
							logger.debug( "firma automatica XADES" );
							signer = DocumentSignerFactory.getInstance( TipoFirma.AUTOMATICA );
							signer.xmlsign(references, fin, alias, pin, null, xmlEnve, returnValue, fout);
						}
					} catch ( Exception ex ) {
						throw new RuntimeException ( ex.getMessage() );
					}
				}else{
					int accessPermissions=0;
					if(opzioni.containsKey("accessPermissions")){
						accessPermissions = Integer.parseInt(opzioni.get("accessPermissions"));
					}

					if(TipoFirma.REMOTA.getTipoFirma().equalsIgnoreCase(tipoFirma)){
						logger.debug( "firma remota" );
						signer = DocumentSignerFactory.getInstance( TipoFirma.REMOTA );
						signer.sign(customerInfo, alias, pin, formatoFirma, fin, authData, fout, enve, returnValue,accessPermissions);
					}else{
						//tipofirma = "AUTOMATICA" 
						logger.debug( "firma automatica" );
						signer = DocumentSignerFactory.getInstance( TipoFirma.AUTOMATICA );
						signer.sign(customerInfo, alias, pin, formatoFirma, fin, OTP, fout, enve, returnValue,accessPermissions);
					}
				}


			} else {
				String message = "Il file [" + fin.getAbsolutePath() + "] non esiste";
				returnValue.put( fin.getName() , new ResultInfo (400, message ) );
			}
		}
		return returnValue;


	}

}
