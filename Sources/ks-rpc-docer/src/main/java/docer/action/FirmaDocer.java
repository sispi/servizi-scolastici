package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.services.FirmaService;
import java.io.*;
import java.util.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by adc on 21/04/16.
 */
public class FirmaDocer extends DocerAction {
    //TODO
    private final static Logger log = LoggerFactory.getLogger(docer.action.FirmaDocer.class);

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
        //String tempDir = System.getProperty("java.io.tmpdir");

        log.info("init method execute CadesSignedDoc");
        Map<String, Object> result = new HashMap<String, Object>();

        Map mappaDocumenti= new HashMap();

        String esitoSigned="-1"; //errore
        try{

            String tempDir = ServerProperties.getParamsBpm("mail.attachDirEmailPec");
            UUID guid = UUID.randomUUID();

            String token = getToken(inputs);
            List<HashMap<String, String>> docDaFirmare = inputs.containsKey("documenti")?(List<HashMap<String, String>>)inputs.get("documenti"):null;
            if (docDaFirmare == null){
                throw new ActionRuntimeException("PIN 'documenti' non trovato nei parametri di input!");
            }
            String alias = inputs.containsKey("alias")?(String)inputs.get("alias"):null;
           // String dominio = inputs.containsKey("dominio")?(String)inputs.get("dominio"):null;
            String pin = inputs.containsKey("pin")?(String)inputs.get("pin"):null;
            String otp = inputs.containsKey("otp")?(String)inputs.get("otp"):null;
            String tipo = inputs.containsKey("tipo")?(String)inputs.get("tipo"):null;
            /*if(dominio==null){
                throw new ActionRuntimeException("PIN 'dominio' non trovato nei parametri di input!");
            }*/
            if(alias==null){
                throw new ActionRuntimeException("PIN 'alias' non trovato nei parametri di input!");
            }
            if(pin==null){
                throw new ActionRuntimeException("PIN 'pin' non trovato nei parametri di input!");
            }
            if(otp==null){
                throw new ActionRuntimeException("PIN 'otp' non trovato nei parametri di input!");
            }
            if(tipo==null){
                throw new ActionRuntimeException("PIN 'tipo' non trovato nei parametri di input!");
            }

            ArrayList<String> docNumDocumenti=new ArrayList<String>();
            Boolean alreadySign=false;
            for (HashMap<String, String> m : docDaFirmare) {
                if(m.containsKey("alreadySigned") && "true".equals(m.get("alreadySigned"))){
                    alreadySign=true;
                }
                docNumDocumenti.add(m.get("DOCNUM"));

            }
            Map mappaDocumentiFirmati = new HashMap();
            if(!alreadySign) {
                String[] listaDocumentiArr = new String[docNumDocumenti.size()];
                listaDocumentiArr = docNumDocumenti.toArray(listaDocumentiArr);


                mappaDocumenti = FirmaService.firmaRemota(token, alias, pin, tipo, otp, listaDocumentiArr);



                Iterator it = mappaDocumenti.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    Map documento = (HashMap) pair.getValue();

                    String nomeDocumento = (String) documento.get("name");
                    String docNum = (String) documento.get("docnum");
                    String nomeOrginale = (String) documento.get("originalName");
                    InputStream streamFile = (InputStream) documento.get("streamFile");

                    //--------------------------------------------------------

                /*Operazioni per la creazione del file*/
                    //DocerFile fileDownload = DocerService.downloadDocument(token, docNum);
                    File originalFile = new File(tempDir + File.separator + guid, nomeDocumento);
                    originalFile.getParentFile().mkdirs();
                    FileWriter writer = new FileWriter(originalFile);
                    OutputStream out = new FileOutputStream(originalFile);
                    IOUtils.copy(streamFile, out);
                    out.close();

                    //String linkFileUrl = ServerProperties.getParamsBpm("url.download.file");
                    String filepath = getUrlFromFile(originalFile);


                    //costruisco la mappa relativa al documento
                    Map tmpMap = new HashMap();
                    tmpMap.put("name", nomeDocumento);
                    tmpMap.put("filePath", filepath);
                    tmpMap.put("originalName", nomeOrginale);
                    tmpMap.put("docnum", docNum);

                    // restituisco la mappa dei documenti firmati
                    mappaDocumentiFirmati.put(docNum, tmpMap);

                    //result.put("filePath", linkFileUrl+originalFile.getAbsolutePath());
                    //--------------------------------------------------------
                    //System.out.println(pair.getKey() + " = " + pair.getValue());
                    //it.remove(); // avoids a ConcurrentModificationException
                }

                if (mappaDocumentiFirmati.size() > 0) {
                    esitoSigned = "1";
                }

                result.put("esitoSigned", esitoSigned);
                result.put("mappaDocumenti", mappaDocumentiFirmati);
                result.put("userToken", token);
            }else{
                result.put("esitoSigned", "1");
                for (HashMap<String, String> m : docDaFirmare) {
                    mappaDocumentiFirmati.put(m.get("docnum"),m);
                }
                result.put("mappaDocumenti", mappaDocumentiFirmati);
                result.put("userToken", token);
            }
        } catch (Exception e) {

            /*
            * 1) CASO OTP SBAGLIATO
                 method execute error:java.lang.RuntimeException: StartTransaction error: Invalid OTP

                2) CASO ALIAS ERRATO
                 method execute error:java.lang.RuntimeException: No certificate chain found for 'aaaa'

                3) CASO PIN ERRATO
                 method execute error:java.lang.RuntimeException: StartTransaction error: GetKeyData error: AccessDenied


                if(e.getMessage().containsIgnorecase("")){

                }
            *
            * */

            if(e.getMessage().contains("Invalid OTP")){
                log.info("method execute error:{}",e.getMessage());
                result.put("esitoSigned", "ERRORE - Codice OTP non valido");
            }
            else if(e.getMessage().contains("No certificate chain found for")){
                log.info("method execute error:{}",e.getMessage());
                result.put("esitoSigned", "ERRORE - Alias non valido");
            }
            else if(e.getMessage().contains("GetKeyData error")){
                log.info("method execute error:{}",e.getMessage());
                result.put("esitoSigned", "ERRORE - Codice PIN non valido");
            }
            else if(e.getMessage().contains("already locked")){
                log.info("method execute error:{}",e.getMessage());
                result.put("esitoSigned", "ERRORE -  Utenza bloccata riprovare tra 10 minuti");
            }

            // gestione errori per provider firma aruba
            else if(e.getMessage().toLowerCase().contains("Errore durante il processo di firma".toLowerCase())){
                log.info("method execute error:{}",e.getMessage());
                result.put("esitoSigned", "ERRORE - Pin, Alias oppure OTP potrebbero non essere validi");
            }

            else{
                log.error("method execute error:{}",e.getMessage());
                throw new ActionRuntimeException(e);
            }
        }
        log.info("end method execute");
        return result;
    }

}
