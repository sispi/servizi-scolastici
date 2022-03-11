package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.Corrispondente;
import it.kdm.doctoolkit.model.DocerFile;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.model.PersonaFisica;
import it.kdm.doctoolkit.services.DocerService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by adc on 13/07/17.
 */

public class VerificaFirmatari extends DocerAction {

    private final static Logger log = LoggerFactory.getLogger(docer.action.VerificaFirmatari.class);

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {
        log.info("init method execute - VerificaFirmatari");


        Map<String, Object> result = new HashMap<String, Object>();

        String token=null;
        String docNum = null;


        HashMap<String, Object> documentoMap = null;

        Documento documento = new Documento();
        String dataProto;
        documentoMap = inputs.containsKey("document")?(HashMap<String, Object>)inputs.get("document"):null;
        dataProto = inputs.containsKey("dataProto")?(String) inputs.get("dataProto"):null;

        if(documentoMap == null)throw new ActionRuntimeException("PIN 'document' non trovato nei parametri di input!");
        if(dataProto == null)throw new ActionRuntimeException("PIN 'dataProto' non trovato nei parametri di input!");

        try {

            token = getToken(inputs);

            if(!documentoMap.containsKey("DOCNUM") || documentoMap.get("DOCNUM")==null){
                throw new ActionRuntimeException("DOCNUM not present or null");
            }else{
                docNum=  documentoMap.get("DOCNUM").toString();
            }

            //prima di effettuare la chiamata alla rest scarico il documento ed effettuo il controllo sui firmatari
            String tempDir = ServerProperties.getParamsBpm("mail.attachDirEmailPec");
            UUID guid = UUID.randomUUID();

            Documento doc = DocerService.recuperaProfiloDocumento(token, docNum);
            DocerFile fileDownload = DocerService.downloadDocument(token, docNum);
            String ext= FilenameUtils.getExtension(doc.getDocName());

            /*if(doc.getDocName().length()>240)
            {
                name=doc.getDocName();
                name = name.substring(0,150);
            }*/

            String name = "file."+ext;

            File originalFile = new File(tempDir+ File.separator+guid,name);
            originalFile.getParentFile().mkdirs();
            //FileWriter writer = new FileWriter(originalFile);
            OutputStream out = new FileOutputStream(originalFile);
            IOUtils.copy(fileDownload.getContent().getInputStream(), out);
            out.close();


            //vado a mappare il doucmenot in input con l'oggetto document

            if(DocerService.isFileSigned(originalFile.getAbsolutePath())) {

                //List<PersonaFisica> lista = new FirmatariHelper().getVerificaFirmatari(token,docNum,dataProto);

                DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date date = df.parse(dataProto);


                List<PersonaFisica> lista = DocerService.getFirmatari(token,docNum,date);


                //apporre la lista di firmatari in
                documento.setFirmatari((List<Corrispondente>) (List<? extends Corrispondente>) lista);

                // fine gestione
            }
            documento.setTipoComponente((String)documentoMap.get("TIPO_COMPONENTE"));
            documento.setDocType((String)documentoMap.get("TYPE_ID"));
            documento.setDescrizione((String)documentoMap.get("ABSTRACT"));
            documento.setDocName((String) documentoMap.get("DOCNAME"));

            documento.setDocNum(docNum);
            //riprendo il token in  input poiche' in precedenza e' stato utilizzato per la chiamata rest
            token=getToken(inputs);
            documento = DocerService.aggiornaDocumento(token, documento);
            documento.setProperty("@structure", "document");
            result.put("document", documento.toFlowObject());
            result.put("userToken", token);


        } catch (Exception e) {
            log.error("method execute error:{}",e.getMessage());
            throw new ActionRuntimeException(e);
        }
        log.info("end method execute");
        return result;
    }
}
