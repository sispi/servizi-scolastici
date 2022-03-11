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
public class FirmaDocerAutomatica extends DocerAction {
    //TODO
    private final static Logger log = LoggerFactory.getLogger(FirmaDocer.class);

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

            if(tipo==null){
                throw new ActionRuntimeException("PIN 'tipo' non trovato nei parametri di input!");
            }

            ArrayList<String> docNumDocumenti=new ArrayList<String>();
            for (HashMap<String, String> m : docDaFirmare) {
                docNumDocumenti.add(m.get("DOCNUM"));

            }

            String[] listaDocumentiArr = new String[docNumDocumenti.size()];
            listaDocumentiArr = docNumDocumenti.toArray(listaDocumentiArr);


            mappaDocumenti= FirmaService.firmaAutomatica(token, alias, pin, tipo, listaDocumentiArr);

            Map mappaDocumentiFirmati=new HashMap();

            Iterator it = mappaDocumenti.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                Map documento= (HashMap) pair.getValue();

                String nomeDocumento=(String)documento.get("name");
                String docNum=(String)documento.get("docnum");
                String nomeOrginale=(String)documento.get("originalName");
                InputStream streamFile= (InputStream) documento.get("streamFile");

                //--------------------------------------------------------

                /*Operazioni per la creazione del file*/
                //DocerFile fileDownload = DocerService.downloadDocument(token, docNum);
                File originalFile = new File(tempDir+ File.separator+guid,nomeDocumento);
                originalFile.getParentFile().mkdirs();
                FileWriter writer = new FileWriter(originalFile);
                OutputStream out = new FileOutputStream(originalFile);
                IOUtils.copy(streamFile, out);
                out.close();

                //String linkFileUrl = ServerProperties.getParamsBpm("url.download.file");
                String filepath=getUrlFromFile(originalFile);


                //costruisco la mappa relativa al documento
                Map tmpMap=new HashMap();
                tmpMap.put("name",nomeDocumento);
                tmpMap.put("filePath",filepath);
                tmpMap.put("originalName",nomeOrginale);
                tmpMap.put("docnum",docNum);

                // restituisco la mappa dei documenti firmati
                mappaDocumentiFirmati.put(docNum,tmpMap);


                //result.put("filePath", linkFileUrl+originalFile.getAbsolutePath());
                //--------------------------------------------------------
                //System.out.println(pair.getKey() + " = " + pair.getValue());
                //it.remove(); // avoids a ConcurrentModificationException
            }

            if (mappaDocumentiFirmati.size()>0){
                esitoSigned="1";
            }

            result.put("esitoSigned", esitoSigned);
            result.put("mappaDocumenti", mappaDocumentiFirmati);
            result.put("userToken", token);

        } catch (Exception e) {
            log.error("method execute error:{}",e.getMessage());
            throw new ActionRuntimeException(e);
        }
        log.info("end method execute");
        return result;
    }

}
