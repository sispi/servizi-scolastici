package keysuite.docer.utils;

import com.google.common.base.Strings;
import it.kdm.doctoolkit.services.ToolkitConnector;
import it.kdm.doctoolkit.zookeeper.ApplicationProperties;
import it.kdm.orchestratore.beans.EmailBean;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import keysuite.docer.server.RestUtilsService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.mail.smime.SMIMESignedParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ReadMail {

	Logger log = LoggerFactory.getLogger(ReadMail.class);

	private static final String datiCertXml = "daticert.xml";
	private static final String POSTACERTIFICATA ="posta-certificata";

	public EmailBean parseMail(Message message, Session session, String messageId, String typeUrl, String alternativeDownloadPath) throws MessagingException, IOException, ParserConfigurationException
    {
        log.info("init method executeWorkItem ");

        EmailBean mimeBean = new EmailBean();
        //preparazione input
        String attachmentsDirectory = null;
        String linkFileUrl = null;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        /**
         * TODO va definito se utilizzarlo
         */
        ObjectMapper m = new ObjectMapper();
        Map<String, Object> output = new HashMap<String, Object>();

        // sono state inserite per evitare un errore a runtime di cast
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

        File attachmentsDirectoryFile;
        String destinationDir;
        String messageIdDir;
        Boolean daControllare = false;

        try {


            //prelevo la directory da utilizzare per salvare le mail e allegati
            attachmentsDirectory = ApplicationProperties.get("mail.attachDirEmailPec");
            //prelevo il link da settare come fileUrl per gli attach
            if(typeUrl.equalsIgnoreCase(RestUtilsService.FILE_TYPE_FILE)){
                linkFileUrl = "file://";
            }else{
                try {
                    linkFileUrl = ApplicationProperties.get("url.download.file");
                    if(StringUtils.isEmpty(linkFileUrl)){
                        linkFileUrl = alternativeDownloadPath;
                    }
                }catch (Exception e){
                    linkFileUrl = alternativeDownloadPath;
                }
            }

            //recupero i parametri passati in input (sono tutti obbligatori)
            //setto il messageId eliminando i caratteri "<" e ">" che causano problemi
            messageIdDir = messageId.replace("<", "");
            messageIdDir = messageIdDir.replace(">", "");
            messageIdDir = messageIdDir.replace("+", "");


            destinationDir = attachmentsDirectory + File.separator + "mails" + File.separator + format.format(cal.getTime()) + File.separator + messageIdDir + File.separator + "ATT_" + messageIdDir;

            attachmentsDirectoryFile = new File(destinationDir);

            if (!attachmentsDirectoryFile.exists()) {
                attachmentsDirectoryFile.setExecutable(true, true);
                attachmentsDirectoryFile.setReadable(true, true);
                attachmentsDirectoryFile.setWritable(true, true);
                attachmentsDirectoryFile.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());

        }
        try {

            //recupero indirizzi mittenti
            //recupero sendDate
            Date sendDate = message.getSentDate();
            if (sendDate != null) {
                DateTime dateTime = new DateTime(sendDate);
                mimeBean.setSendDate(fmt.print(dateTime.toDateTime(DateTimeZone.UTC)));
            }
            //recupero receivedDate
            Date receivedDate = message.getReceivedDate();
            if (receivedDate != null) {
                DateTime dateTime = new DateTime(receivedDate);
                mimeBean.setReceivedDate(fmt.print(dateTime.toDateTime(DateTimeZone.UTC)));
            }


            mimeBean.setMessageId(messageId);


            mimeBean.setIsRicevutaPec("false");
            Message messageToRead = null;

            try{
                message.isMimeType("multipart/signed");
            }catch (Exception ee){
                if (message instanceof MimeMessage && "Unable to load BODYSTRUCTURE".equalsIgnoreCase(ee.getMessage())) {
                    message = (Message) new MimeMessage((MimeMessage) message);
                } else {
                    log.error("Malformed Mail", ee);
                }
            }


            if (message.isMimeType("multipart/signed")) {
                MimeMultipart mm = (MimeMultipart) message.getContent();
                final SMIMESignedParser s = new SMIMESignedParser(mm);

                final MimeBodyPart mimePart = s.getContent();
                final DataHandler data = mimePart.getDataHandler();
                final MimeMultipart multiPart = (MimeMultipart) data.getContent();
                if (multiPart.getCount() < 1) {
                    throw new MessagingException("Missing attachments");
                }
                boolean isPec = false;
                for (int i = 0; i < multiPart.getCount(); i++) {
                    BodyPart bodyPart = multiPart.getBodyPart(i);

                    if (datiCertXml.equals(bodyPart.getFileName())) {
                        isPec = true;
                        break;
                    }
                }

                for (int i = 0; i < multiPart.getCount(); i++) {

                    BodyPart bodyPart = multiPart.getBodyPart(i);

                    if (datiCertXml.equals(bodyPart.getFileName())) {

                        String pathDatiCert = this.saveAttachementsCert(bodyPart, attachmentsDirectoryFile);
                        String encodedPath = URLEncoder.encode(destinationDir + File.separator + datiCertXml, "UTF-8");
                        mimeBean.getAllegati().add(linkFileUrl + encodedPath);


                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(pathDatiCert);

                        doc.getDocumentElement().normalize();

                        String tipoMail = doc.getDocumentElement().getAttribute("tipo");
                        System.out.println("Tipo daticert.xml :" + tipoMail);

                        if (!POSTACERTIFICATA.equals(tipoMail)) {
                            mimeBean.setIsRicevutaPec("true");
                        }


                        log.info("datiCertXml:" + pathDatiCert);
                    } else if ("eml".equals(FilenameUtils.getExtension(bodyPart.getFileName())) && isPec) {
                        bodyPart.getDataHandler();
                        MimeMessage eml = new MimeMessage(session, bodyPart.getInputStream());
                        messageToRead = eml;
                    } else if (bodyPart.getFileName() == null) {
                        mimeBean = this.getContentMultipart(mimeBean, message, attachmentsDirectoryFile, destinationDir, linkFileUrl);
                    }

                }

            }

            if (messageToRead == null) {
                messageToRead = message;
            }

            mimeBean = this.getContentMultipart(mimeBean, messageToRead, attachmentsDirectoryFile, destinationDir, linkFileUrl);

            InternetAddress from[] = (InternetAddress[]) messageToRead.getFrom();
            List<Map<String, String>> mittenti = new ArrayList<>();
            for (InternetAddress address : from) {
                mimeBean.getMittenti().add(getAddress(address));
            }


            //recupero indirizzi TO
            InternetAddress to[] = (InternetAddress[]) messageToRead.getRecipients(Message.RecipientType.TO);

            if (to != null) {

                for (InternetAddress address : to) {
                    mimeBean.getDestinatari().add(getAddress(address));
                }
            }


            //@type
            mimeBean.setType("message");

            // recupero e inserimento cc
            InternetAddress cc[] = (InternetAddress[]) messageToRead.getRecipients(Message.RecipientType.CC);
            List<Map<String, String>> ccList = new ArrayList<>();
            if (cc != null) {
                for (InternetAddress address : cc) {
                    mimeBean.getConoscenza().add(getAddress(address));
                }
            }

            // recupero e setto i bcc
            InternetAddress bcc[] = (InternetAddress[]) messageToRead.getRecipients(Message.RecipientType.BCC);
            List<Map<String, String>> bccList = new ArrayList<>();
            if (bcc != null) {
                for (InternetAddress address : bcc) {
                    mimeBean.getNascosta().add(getAddress(address));
                }
            }

            //creo la directory per il body della mail
            File mailBackupDirFile = new File(attachmentsDirectory + File.separator + "mails" + File.separator + format.format(cal.getTime()) + File.separator + messageIdDir + File.separator + "BODY_" + messageIdDir);
            if (!mailBackupDirFile.exists()) {
                mailBackupDirFile.setExecutable(true, true);
                mailBackupDirFile.setReadable(true, true);
                mailBackupDirFile.setWritable(true, true);
                mailBackupDirFile.mkdirs();
            }

            //salvo la mail nel file body.eml
            String path = mailBackupDirFile + File.separator + "body" + ".eml";
            OutputStream out = new FileOutputStream(path);
            messageToRead.writeTo(out);
            out.flush();
            out.close();

            path = URLEncoder.encode(path, "UTF-8");
            mimeBean.getAllegati().add(linkFileUrl + path);


            String smimePath = mailBackupDirFile + File.separator + "smime" + ".eml";
            OutputStream smimeOut = new FileOutputStream(smimePath);
            message.writeTo(smimeOut);
            smimeOut.flush();
            smimeOut.close();

            smimePath = URLEncoder.encode(smimePath, "UTF-8");
            mimeBean.getAllegati().add(linkFileUrl + smimePath);

            String subject = messageToRead.getSubject();
            if (!Strings.isNullOrEmpty(subject)) {
                mimeBean.setSubject(MimeUtility.decodeText(subject));
            } else {
                mimeBean.setSubject("");
            }

        } catch (MessagingException mex) {
            mex.printStackTrace();
            log.error("Error method executeWorkItem:{}", mex.getMessage());
            throw mex;

		}
        catch (SAXException e) {
            //Errore nel parse di daticert.xml per verifica isRicevuta
            daControllare = true;
            log.error("Errore nel parse di daticert.xml per verifica isRicevuta", e);
        }
        catch (CMSException e) {
            daControllare = true;
            log.error("Errore Malformed PEC", e);
        }



			if(daControllare){

					mimeBean = new EmailBean();
					UUID guid = UUID.randomUUID();
					File mailBackupDirFile = new File(attachmentsDirectory + File.separator + "mails" + File.separator + format.format(cal.getTime()) + File.separator + guid + File.separator );

					if (!mailBackupDirFile.exists()) {
						mailBackupDirFile.setExecutable(true, true);
						mailBackupDirFile.setReadable(true, true);
						mailBackupDirFile.setWritable(true, true);
						mailBackupDirFile.mkdirs();
					}

					String smimePath = mailBackupDirFile + File.separator + "Pec da controllare" + ".eml";
					OutputStream smimeOut = new FileOutputStream(smimePath);
					message.writeTo(smimeOut);
					smimeOut.flush();
					smimeOut.close();

					smimePath = URLEncoder.encode(smimePath, "UTF-8");
					mimeBean.getAllegati().clear();
					mimeBean.getAllegati().add(linkFileUrl + smimePath);


					mimeBean.setMessageId(messageId);
					mimeBean.setDaControllare(true);


			}

			return mimeBean;

    }


    private boolean isUndeliveredMail(String subject){
	    java.lang.String exceptionMail = ToolkitConnector.getGlobalProperty("undelivered.mail.subject", "undelivered mail");
        java.lang.String[] splittedExceptionMail = exceptionMail.split(",");

	    boolean isUndeliverable = false;
	    for(java.lang.String em: splittedExceptionMail){
            if(subject.toLowerCase().contains(em)){
                isUndeliverable = true;
                break;
            }
        }


	    return isUndeliverable;
    }




	private Map<String, String> getAddress(InternetAddress address){
		HashMap<String, String> map = new HashMap<>();
		map.put("IndirizzoTelematico", address.getAddress());
		if(address.getPersonal()!=null)
			map.put("Denominazione", address.getPersonal());
		else
			map.put("Denominazione", "vuoto");
		map.put("tipo", address.getType());
		return map;
	}


	/*
	 * Return the primary text content of the message.
	 */
	private static String getText(Part p) throws
			MessagingException, IOException {
		if (p.isMimeType("text/*")) {
		    String s = null;
		    if(p.getContent() instanceof InputStream) {
                s = StreamUtils.copyToString((InputStream) p.getContent(), Charset.defaultCharset());
            }else {
                s = MimeUtility.decodeText((String) p.getContent());
            }

			return s.replace("\u0003","");
		}

		if (p.isMimeType("multipart/alternative")) {
			// prefer html text over plain text
			Multipart mp = (Multipart)p.getContent();
			String text = null;
			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/html")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/plain")) {
					String s = getText(bp);
					if (s != null)
						return s.replace("\u0003","");
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart part = mp.getBodyPart(i);

				String s = getText(part);
				if (s != null)
					return s.replace("\u0003","");
			}
		}

		return null;
	}

	private static String saveXMLCert(final SMIMESignedParser s, File attachmentsDirectoryFile)
			throws Exception {
		final MimeBodyPart mimePart = s.getContent();
		final DataHandler data = mimePart.getDataHandler();
		final MimeMultipart multiPart = (MimeMultipart) data.getContent();
		if (multiPart.getCount() < 1) {
			throw new MessagingException("Missing attachments");
		}
		final BodyPart bodyCert = multiPart.getBodyPart(1);
		final DataHandler dataCert = bodyCert.getDataHandler();
		final DataSource dataSourceCert = dataCert.getDataSource();
		final InputStream idataCert = dataSourceCert.getInputStream();

		String path = attachmentsDirectoryFile + File.separator + datiCertXml;

		OutputStream outputStream = new FileOutputStream(new File(path));

		IOUtils.copy(idataCert, outputStream);
		idataCert.close();
		outputStream.close();

		return path;

	}



	private static String saveAttachementsCert(final BodyPart bodyCert, File attachmentsDirectoryFile) throws MessagingException, IOException {


		final DataHandler dataCert = bodyCert.getDataHandler();
		final DataSource dataSourceCert = dataCert.getDataSource();
		final InputStream idataCert = dataSourceCert.getInputStream();
		String fileName = null;

		if(bodyCert.getFileName()==null){

		fileName =MimeUtility.decodeText(bodyCert.getFileName());

		}else{
			fileName = bodyCert.getFileName();
		}



		String path = attachmentsDirectoryFile + File.separator + fileName;

		OutputStream outputStream = new FileOutputStream(new File(path));

		IOUtils.copy(idataCert, outputStream);
		idataCert.close();
		outputStream.close();

		return path;

	}





	private EmailBean getContentMultipart(EmailBean mimeBean, Message msg, File attachmentsDirectoryFile, String destinationDir, String linkFileUrl) throws MessagingException, IOException {
		if (msg.getContentType().contains("multipart")) {
			Multipart mp = (Multipart) msg.getContent();  //ERROR com.sun.mail.imap.IMAPInputStream cannot be cast to javax.mail.Multipart

			for (int j = 0; j < mp.getCount(); j++) {
				MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(j);

				String disposition=null;
				//Gestisco il caso in cui l'header del content-disposition arriva corrotto
				try{
				    disposition  = part.getDisposition();
                }catch (Exception ex1){
				    if(ex1.getMessage().equalsIgnoreCase("Unbalanced quoted string")){
				        if(part.getHeader("Content-Disposition") != null && part.getHeader("Content-Disposition").length>0) {
                            disposition = "" + part.getHeader("Content-Disposition")[0] + "\"";
                            part.setHeader("Content-Disposition", disposition);
                        }
                    }
                }





				if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) || Part.INLINE.equalsIgnoreCase(part.getDisposition())) {

					//verifico i mime type
					//TODO usare la giusta routine per il nome del file
					String fileName = null;
					if (part.getFileName() != null) {
                        fileName = MimeUtility.decodeText(part.getFileName());
                        fileName = fileName.replaceAll("\\/", "_");
                        fileName = fileName.replaceAll("\\ ", "_");
                    }else {
                        fileName = UUID.randomUUID().toString() + ".dat";
                    }
					File fileRename = renameFile(attachmentsDirectoryFile,fileName);


					part.saveFile(fileRename);
					long fileSize= FileUtils.sizeOf(fileRename);
					if(fileSize<1){
						FileUtils.write(fileRename,"\n");
					}

                    String encodePath = URLEncoder.encode(destinationDir, "UTF-8");
					mimeBean.getAllegati().add(linkFileUrl+encodePath+ File.separator + URLEncoder.encode(fileName,"UTF-8"));
					//}
					//}
				} else if (Pattern.compile(Pattern.quote("text/plain"), Pattern.CASE_INSENSITIVE)
						.matcher(part.getContentType()).find()) {
					mimeBean.setContentText(MimeUtility.decodeText((String) part.getContent()));
				} else if (Pattern.compile(Pattern.quote("text/html"), Pattern.CASE_INSENSITIVE)
						.matcher(part.getContentType()).find()) {
				    if(part.getContent() instanceof Multipart){
                        Multipart mpp = ((Multipart) part.getContent());
				        for(int y=0; y< mpp.getCount(); y++){
                            MimeBodyPart partx = (MimeBodyPart) mpp.getBodyPart(y);
                            if (Pattern.compile(Pattern.quote("text/html"), Pattern.CASE_INSENSITIVE)
                                    .matcher(partx.getContentType()).find()) {

                                String htmlContent = "";
                                try{
                                    htmlContent = MimeUtility.decodeText(((String) partx.getContent()) != null ?((String) partx.getContent()).replace("\u0003",""):"");
                                }catch (Exception e){
                                    log.error(e.getMessage());
                                }
                                mimeBean.setContentHtml(htmlContent);
                                mimeBean.setContentText(cleanHtml(htmlContent));
                                break;
                            }
                        }
                    }else {
                        String htmlContent = "";
                        try{
                            htmlContent = MimeUtility.decodeText(((String) part.getContent())!= null ?((String) part.getContent()).replace("\u0003",""): "");
                        }catch (Exception e){
                            log.error(e.getMessage());
                        }
                        mimeBean.setContentHtml(htmlContent);
                        mimeBean.setContentText(cleanHtml(htmlContent));
                    }
				} else if (part.isMimeType("image/*")) {
					// scarto l'immagine
				} else {
					String ttx = getText(part);
					if(!Strings.isNullOrEmpty(ttx)) {
                        mimeBean.setContentText(ttx.replace("\u0003", ""));
                    }
				}
			}
		} else {

			mimeBean.setContentText(getText(msg));
		}
		return mimeBean;
	}


	private File renameFile(File destinationDir, String fileName ) throws FileAlreadyExistsException {

		File fileDoc = new File(destinationDir, fileName);
		if(fileDoc.exists()){
			UUID guid = UUID.randomUUID();
			String extFile = FilenameUtils.getExtension(fileName);
			String baseFileName = FilenameUtils.getBaseName(fileName);
			fileName=baseFileName+"_"+guid.toString()+"."+extFile;
			fileDoc =new File(destinationDir, fileName);
			if(fileDoc == null){
				log.error("renameFile failed.");
				throw new FileAlreadyExistsException("Renamefile error. Impossible rename file to"+destinationDir+ File.separator+fileName);
			}
		}
		return fileDoc;
	}

	private String cleanHtml(String html){
        String s = html;
        try {
            s = Jsoup.clean(html, "", Whitelist.relaxed(), new org.jsoup.nodes.Document.OutputSettings().prettyPrint(false));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return s;
    }

}