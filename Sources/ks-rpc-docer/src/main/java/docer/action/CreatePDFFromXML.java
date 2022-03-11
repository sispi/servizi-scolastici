package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.DocerFile;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.services.DocerService;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FilenameUtils;
import org.apache.fop.apps.*;
import static org.apache.fop.apps.FopFactory.newInstance;
import org.apache.xalan.processor.TransformerFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by matteog on 06/02/17.
 */
public class CreatePDFFromXML extends DocerAction {
    private final static Logger log = LoggerFactory.getLogger(CreateFileXmlFromObject.class);


    @Override
    public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {

        log.info("init method execute");
        Map<String, Object> result = new HashMap<String, Object>();
        String token = null;
        String docNumXsl = null;
        String docNumXml = null;
        HashMap<String, Object> documentoXml = null;
        HashMap<String, Object> documentoXsl = null;

        documentoXml = inputs.containsKey("documentoXml") ? (HashMap<String, Object>) inputs.get("documentoXml") : null;
        documentoXsl = inputs.containsKey("documentoXsl") ? (HashMap<String, Object>) inputs.get("documentoXsl") : null;


        try {

            //String linkFileUrl = ServerProperties.getParamsBpm("url.download.file");
            token = getToken(inputs);

            if (!documentoXsl.containsKey("DOCNUM") || documentoXsl.get("DOCNUM") == null) {
                throw new ActionRuntimeException("DOCNUM not present on documentoXsl or null");
            } else {
                docNumXsl = documentoXsl.get("DOCNUM").toString();
            }

            if (!documentoXml.containsKey("DOCNUM") || documentoXml.get("DOCNUM") == null) {
                throw new ActionRuntimeException("DOCNUM not present on documentoXsl or null");
            } else {
                docNumXml = documentoXml.get("DOCNUM").toString();
            }

            DocerFile dfXsl = DocerService.downloadDocument(token, docNumXsl);
            DocerFile dfXml = DocerService.downloadDocument(token, docNumXml);



            String tempDir = ServerProperties.getParamsBpm("mail.attachDirEmailPec");


            // rinomino il file da creare con il nome del file xml
            String fileName = null;
            if (documentoXml.get("DOCNAME") != null && !"".equals(documentoXml.get("DOCNAME"))) {
                fileName = FilenameUtils.getBaseName((String) documentoXml.get("DOCNAME"));
                fileName = fileName + ".xml";
            } else {
                fileName = docNumXml + ".xml";
            }

            UUID guid = UUID.randomUUID();
            File originalXMLFile = new File(tempDir + File.separator + guid, fileName);
            if(originalXMLFile.getParentFile().mkdirs()) {
                log.debug("folder "+originalXMLFile.getPath()+" created");
            }
            OutputStream out = new FileOutputStream(originalXMLFile);
            dfXml.getContent().writeTo(out);
            out.close();

            File originalXslFile = new File(tempDir + File.separator + guid, docNumXsl + ".xsl");
            if (originalXslFile.getParentFile().mkdirs()) {
                log.debug("folder "+originalXslFile.getPath()+" created");
            }
            OutputStream outxsl = new FileOutputStream(originalXslFile);
            dfXsl.getContent().writeTo(outxsl);
            outxsl.close();

            // the XML file from which we take the name
            StreamSource source = new StreamSource(originalXMLFile);
            // creation of transform source
            StreamSource transformSource = new StreamSource(originalXslFile);
            // create an instance of fop factory
            FopFactory fopFactory = newInstance();
            // a user agent is needed for transformation
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // to store output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            Transformer xslfoTransformer;
            try
            {
                xslfoTransformer = getTransformer(transformSource);
                // Construct fop with desired output format
                Fop fop;
                try
                {
                    fop = fopFactory.newFop
                            (MimeConstants.MIME_PDF, foUserAgent, outStream);
                    // Resulting SAX events (the generated FO)
                    // must be piped through to FOP
                    Result res = new SAXResult(fop.getDefaultHandler());

                    // Start XSLT transformation and FOP processing
                    try
                    {
                        // everything will happen here..
                        if (xslfoTransformer != null) {
                            xslfoTransformer.transform(source, res);
                        }
                        // if you want to get the PDF bytes, use the following code
                        //return outStream.toByteArray();

                        // if you want to save PDF file use the following code
                        String xmlFilename = null;
                        if (documentoXml.get("DOCNAME") != null && !"".equals(documentoXml.get("DOCNAME"))) {
                            xmlFilename = FilenameUtils.getBaseName((String) documentoXml.get("DOCNAME"));
                            xmlFilename = xmlFilename + ".pdf" ;
                        } else {
                            xmlFilename = docNumXml + ".pdf" ;
                        }
                        File pdffile = new File(tempDir + File.separator + guid, xmlFilename);
                        //File pdffile = new File("Result.pdf");
                        //OutputStream output = new FileOutputStream(pdffile);
                        //out = new BufferedOutputStream(out);
                        FileOutputStream str = new FileOutputStream(pdffile);
                        str.write(outStream.toByteArray());
                        str.close();
                        //out.close();

                        Documento documentoPdf = new Documento();
                        documentoPdf.properties.put("filePath", getUrlFromFile(pdffile) );

                        result.put("userToken", token);
                        result.put("documentoPdf", documentoPdf.toFlowObject());

                        // to write the content to out put stream
                        /*byte[] pdfBytes = outStream.toByteArray();
                        response.setContentLength(pdfBytes.length);
                        response.setContentType("application/pdf");
                        response.addHeader("Content-Disposition",
                                "attachment;filename=pdffile.pdf");
                        response.getOutputStream().write(pdfBytes);
                        response.getOutputStream().flush();*/
                    }
                    catch (TransformerException e) {
                        log.error("TransformerException error:{} {}", e.getMessage(),e.getLocationAsString());
                        throw e;
                    }
                }
                catch (FOPException e) {
                    log.error("FOP error:{}", e.getMessage());
                    throw e;
                }
            }
            catch (TransformerConfigurationException e)
            {
                log.error("Transformer Configuration error:{}", e.getMessage());
                throw e;
            }

        } catch (Exception e) {
            log.error("method execute error:{}", e.getMessage());
            throw new ActionRuntimeException(e);
        }
        log.info("end method execute");
        return result;
    }

    private Transformer getTransformer(StreamSource transformSource) throws TransformerConfigurationException {
        // setup the xslt transformer
        TransformerFactoryImpl impl = new TransformerFactoryImpl();
        impl.setErrorListener(new org.apache.xml.utils.DefaultErrorHandler(true));

        try {
            return impl.newTransformer(transformSource);

        } catch (TransformerConfigurationException e) {
            log.error("Transformer Configuration error:{}", e.getMessage());
            throw  e;
            //e.printStackTrace();
        }
        //return null;
    }
}
