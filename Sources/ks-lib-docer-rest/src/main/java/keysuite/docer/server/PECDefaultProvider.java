package keysuite.docer.server;

import com.github.underscore.lodash.U;
import it.kdm.docer.PEC.IProvider;
import it.kdm.docer.PEC.PECException;
import it.kdm.docer.commons.XMLUtil;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.*;
import keysuite.docer.client.corrispondenti.ICorrispondente;
import keysuite.docer.client.verificafirma.Token;
import keysuite.docer.client.verificafirma.VerificaFirmaDTO;
import keysuite.docer.sdk.APIClient;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class PECDefaultProvider implements IProvider {
    //private String token = null;
    private ILoggedUserInfo currentUser = null;
    //APIClient APIClient = null;

    IFirma firmaService;
    FileServiceCommon fsc = new FileServiceCommon(null,null);

    public PECDefaultProvider() {
        firmaService = (IFirma) it.kdm.orchestratore.session.Session.getBean("firmaService");
    }

    public String login(String username, String password, String codiceEnte) {
       return password;
    }

    public void logout(String token) throws PECException {
    }

    /*public void setToken(String token) throws PECException {
        this.token = token;
    }*/

    public void setCurrentUser(ILoggedUserInfo info) throws PECException {
        this.currentUser = info;
    }

    private List getList(Object obj){
        if (obj==null || obj instanceof List)
            return (List) obj;
        else
            return new ArrayList(Collections.singletonList(obj));
    }

    private boolean verificaFirme(URL url){

        //NamedInputStream stream = null;
        try {
            //stream =  fsc.openURL(url,null);
            Object[] dtos = firmaService.verificaFirme(null,null,url);

            VerificaFirmaDTO dto = (VerificaFirmaDTO) dtos[0];

            Token.Indication indication = dto.getIndication();

            if (Token.Indication.TOTAL_PASSED.equals(indication))
                return true;
            else
                return false;

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            /*if (stream!=null) {
                try {
                    stream.getStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        }

    }

    private MimeBodyPart attachUri(URL url) throws MalformedURLException, MessagingException {
        MimeBodyPart attachmentBodyPart = new MimeBodyPart();

        URLDataSource bads = new URLDataSource(url);

        attachmentBodyPart.setDataHandler(new DataHandler(bads));
        attachmentBodyPart.setFileName(bads.getName());
        attachmentBodyPart.setDisposition("attachment");

        return attachmentBodyPart;
    }

    public String invioPEC(long documentoId, String datiPec) {
        try {


            String ente_user = this.currentUser.getCodiceEnte();
            String username = this.currentUser.getUserId();

            XMLUtil dom = new XMLUtil(datiPec);
            File schema = ConfigurationUtils.loadResourceFile("pec-provider-validation.xsd");
            Properties options = ConfigurationUtils.loadProperties("pec-provider.properties");
            dom.validate(schema.getAbsolutePath());

            Map<String, Object> map = (Map) U.fromXml(datiPec);

            String oggetto = U.get(map, "Segnatura.Intestazione.Oggetto");
            String body = U.get(map, "Segnatura.Intestazione.Body");
            String modalitaInvio = U.get(map, "Segnatura.Intestazione.Flusso.ModalitaInvio");
            String tipoRichiesta = U.get(map, "Segnatura.Intestazione.Flusso.TipoRichiesta");
            String forzaInvio = U.get(map, "Segnatura.Intestazione.Flusso.ForzaInvio");
            Object dest = U.get(map, "Segnatura.Intestazione.Destinatari");

            //List list = dest instanceof List ? (List) dest : Collections.singletonList(dest);

            Documento d = new Documento();
            d.otherFields().put("DESTINATARI", U.toXml(Collections.singletonMap("Destinatari",dest)) );

            ICorrispondente[] corr = d.getDestinatari();

            Map documento = U.get(map,"Segnatura.Documenti.Documento");
            List<Map> allegati = getList(U.get(map, "Segnatura.Documenti.Allegati.Documento") );
            List<Map> annessi = getList(U.get(map, "Segnatura.Documenti.Annotazioni.Documento") );
            //List<Map> annotazioni = getList(U.get(map, "Segnatura.Documenti.Annessi.Documento") );

            Map<String, Object> profile = new HashMap<>();

            List<Map> parametri = getList(U.get(map, "Segnatura.Documenti.Documento.Metadati.Parametro"));

            if (parametri!=null) {
                for (Map m : parametri) {
                    profile.put((String) m.get("-nome"), m.get("-valore"));
                }
            }

            profile.put("oggetto",oggetto);

            if (body==null) {
                String tmpl = options.getProperty("body-template");
                String tmpl_all = options.getProperty("body-template.allegato","${DOCNAME}");

                //assert String.valueOf(documentoId).equals(profile.get("DOCNUM"));

                if (tmpl != null) {

                    StrLookup lookup = new StrLookup() {
                        @Override
                        public String lookup(String s) {

                            if ("annessi".equals(s)) {
                                String str = "";
                                if (annessi==null)
                                    return "";
                                for (Map allegato : annessi) {
                                    Map<String, Object> all = new HashMap<>();
                                    List<Map> par = getList(U.get(allegato, "Metadati.Parametro"));

                                    for (Map m : par) {
                                        all.put((String) m.get("-nome"), m.get("-valore"));
                                    }

                                    str += new StrSubstitutor(all).replace(tmpl_all);

                                }
                                return str;
                            } else if ("allegati".equals(s)) {
                                String str = "";
                                if (allegati==null)
                                    return "";
                                for (Map allegato : allegati) {
                                    Map<String, Object> all = new HashMap<>();
                                    List<Map> par = getList(U.get(allegato, "Metadati.Parametro"));

                                    for (Map m : par) {
                                        all.put((String) m.get("-nome"), m.get("-valore"));
                                    }

                                    str += new StrSubstitutor(all,"{{","}}").replace(tmpl_all);

                                }
                                return str;
                            } else {
                                return profile.getOrDefault(s, "").toString();
                            }
                        }
                    };

                    StrSubstitutor sub = new StrSubstitutor(lookup,"{{","}}",'$');

                    body = sub.replace(tmpl);
                }
            }

            String ente = (String) profile.get("COD_ENTE");
            String aoo = (String) profile.get("COD_AOO");
            String uo = (String) profile.get("COD_UO");

            String smtp = null;

            if (uo!=null && options.containsKey("smtp."+uo))
                smtp = options.getProperty("smtp."+uo);
            else if (aoo!=null && options.containsKey("smtp."+aoo))
                smtp = options.getProperty("smtp."+aoo);
            else if (ente_user!=null && options.containsKey("smtp."+ente_user))
                smtp = options.getProperty("smtp."+ente_user);
            else
                smtp = options.getProperty("smtp");

            List<NameValuePair> pairs = URLEncodedUtils.parse(smtp, Charset.defaultCharset());



            Properties prop = new Properties();

            for ( NameValuePair pair : pairs){
                prop.put(pair.getName(), pair.getValue());
            }
            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(prop.getProperty("username"), prop.getProperty("password"));
                }
            });

            String from = prop.getProperty("from");

            MimeMessage message = new MimeMessage(session);

            if (from!=null)
                message.setFrom(new InternetAddress(from));

            InternetAddress[] to = new InternetAddress[corr.length];

            for( int i=0; i<corr.length; i++ ){
                to[i] = InternetAddress.parse(corr[i].getIndirizzoTelematico())[0];
            }

            message.setRecipients(
                    Message.RecipientType.TO, to);

            message.setSubject(oggetto);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            URL url = new URL( (String) documento.get("-uri") );

            if ("0".equals(forzaInvio) && !verificaFirme(url)){
                String msg = "il file "+url+" non è firmato";
                return "<Esito><Codice>-2</Codice><Descrizione>"+msg+"</Descrizione></Esito>";
            }

            MimeBodyPart attachmentBodyPart = attachUri(url);
            attachmentBodyPart.setFileName( (String) profile.get("DOCNAME"));

            multipart.addBodyPart(attachmentBodyPart);

            if ("C".equals(tipoRichiesta) && allegati!=null){

                for( Map all : allegati ){

                    url = new URL(  (String) all.get("-uri") );

                    if ("0".equals(forzaInvio) && !verificaFirme(url)){
                        String msg = "il file "+url+" non è firmato";
                        return "<Esito><Codice>-2</Codice><Descrizione>"+msg+"</Descrizione></Esito>";
                    }

                    Map<String, Object> allmap = new HashMap<>();
                    List<Map> par = getList(U.get(all, "Metadati.Parametro"));

                    for (Map m : par) {
                        allmap.put((String) m.get("-nome"), m.get("-valore"));
                    }

                    attachmentBodyPart = attachUri(url);
                    attachmentBodyPart.setFileName( (String) allmap.get("DOCNAME"));

                    multipart.addBodyPart(attachmentBodyPart);
                }
            }

            String identificativo = null;
            boolean smtpSend = "S".equals(modalitaInvio);

            if ("D".equals(modalitaInvio)){

                try {

                    String bearer = new APIClient().loginJWT(aoo,username,currentUser.getTicket());

                    APIClient APIClient = new APIClient(bearer);

                    ByteArrayOutputStream os = new ByteArrayOutputStream(1);

                    message.writeTo(os);

                    ByteArrayInputStream stream = new ByteArrayInputStream(os.toByteArray());

                    Documento doc = APIClient.documenti().get(String.valueOf(documentoId));
                    Documento mail = new Documento();
                    mail.setDocname(oggetto + ".eml");
                    mail.setType("MAIL");
                    mail.setStream(stream);
                    mail.setTipoComponente("ANNESSO");

                    mail.otherFields().put("MAIL_TO" , InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)));
                    mail.otherFields().put("MAIL_SUBJECT" , message.getSubject());
                    mail.otherFields().put("MAIL_FROM" , from );
                    mail.otherFields().put("MAIL_BODY" , body );

                    //String zulu = ISODateTimeFormat.dateTime().print(new DateTime(message.getSentDate()));

                    //mail.otherFields().put("MAIL_DATE" , zulu );
                    mail.otherFields().put("MAIL_STATE" , "1" );

                    mail = APIClient.documenti().createMulti(true, mail, doc)[0];

                    identificativo = "DOCNUM:"+mail.getDocnum();

                    if ("true".equals(options.get("invia-annesso")))
                        smtpSend = true;

                } catch (KSExceptionNotFound | KSExceptionBadRequest | KSExceptionForbidden exc) {
                    return "<Esito><Codice>-1</Codice><Descrizione>"+exc.getMessage()+"</Descrizione></Esito>";
                }
            }

            if (smtpSend){
                Transport.send(message);
                identificativo = "MSGID:"+StringEscapeUtils.escapeXml( message.getMessageID() );
            }

            if (identificativo==null)
                return "<Esito><Codice>500</Codice><Descrizione>500</Descrizione></Esito>";

            return "<Esito><Codice>0</Codice><Descrizione/><Identificativo>" + identificativo + "</Identificativo></Esito>";



        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }


    }
}

