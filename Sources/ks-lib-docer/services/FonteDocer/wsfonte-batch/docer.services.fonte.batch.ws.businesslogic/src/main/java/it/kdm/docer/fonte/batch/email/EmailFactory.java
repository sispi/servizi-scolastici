package it.kdm.docer.fonte.batch.email;

import it.kdm.docer.commons.Config;
import it.kdm.docer.fonte.batch.ConfigUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

public class EmailFactory {

    QName qnameIdFonte = new QName("id_fonte");
    String host;
    String fromAddress;
    String fromName;

    Map<String, String> recipients = null;

    String username;
    String password;
    String method;
    String smtpport;
    String subject;
    String sslport;

    Config config = null;

    public EmailFactory(Config config) {
        this.config = config;
    }

    private boolean Configure(Email email, String id_fonte) throws Exception {

        System.out.println("configurazione client email");

        if (config == null) {
            throw new Exception("EmailFactory non configurata: config=null");
        }

        if(recipients==null){
            recipients = new HashMap<String, String>();
            List<OMElement> omeToList = ConfigUtils.readConfigNodes(config, "//group[@name='common-configuration']/section[@name='email']/to/address");

            if (omeToList != null && omeToList.size() > 0) {

                for (OMElement addressNode : omeToList) {

                    OMAttribute omaIdFonte = addressNode.getAttribute(qnameIdFonte);
                    
                    String address = addressNode.getText();
                    String idf = omaIdFonte.getAttributeValue();
                    
                    recipients.put(idf, address);
                }
            }
        }
        

        String toAddress = recipients.get(id_fonte);

        if (toAddress == null || toAddress.equals("")) {
            System.out.println("destinatario per la Fonte " + id_fonte + " non definito");
            return false;
        }

        host = ConfigUtils.readConfigKey(config, "//group[@name='common-configuration']/section[@name='email']/host");
        fromAddress = ConfigUtils.readConfigKey(config, "//group[@name='common-configuration']/section[@name='email']/fromAddress");
        fromName = ConfigUtils.readConfigKey(config, "//group[@name='common-configuration']/section[@name='email']/fromName");
        username = ConfigUtils.readConfigKey(config, "//group[@name='common-configuration']/section[@name='email']/username");
        password = ConfigUtils.readConfigKey(config, "//group[@name='common-configuration']/section[@name='email']/password");

        method = ConfigUtils.readConfigKey(config, "//group[@name='common-configuration']/section[@name='email']/method");
        smtpport = ConfigUtils.readConfigKey(config, "//group[@name='common-configuration']/section[@name='email']/smtpport");
        subject = ConfigUtils.readConfigKey(config, "//group[@name='common-configuration']/section[@name='email']/subject");
        sslport = ConfigUtils.readConfigKey(config, "//group[@name='common-configuration']/section[@name='email']/sslport");

        email.setAuthentication(username, password);
        email.setHostName(host);
        System.out.println("impostato host: " + host);
        email.setSmtpPort(Integer.parseInt(smtpport));
        System.out.println("impostata porta SMTP: " + smtpport);

        if (method.equals("SSL")) {
            email.setSSLCheckServerIdentity(true);
            System.out.println("impostato SSL");
            if (sslport != null && !sslport.equals("")) {
                email.setSslSmtpPort(sslport);
                System.out.println("impostata porta ssl " + sslport);
            }
        }
        else if (method.equals("TLS")) {
            System.out.println("impostato TLS");
            email.setStartTLSEnabled(true);
        }

        email.setFrom(fromAddress, fromName);

        InternetAddress to = new InternetAddress(toAddress, toAddress);

        List<InternetAddress> toList = new ArrayList<InternetAddress>();
        toList.add(to);
        email.setTo(toList);

        return true;
    }

    // public void sendSimpleMail(String message, String applicationName) throws
    // Exception{
    // SimpleEmail email = new SimpleEmail();
    // Configure(email);
    // email.setMsg(message);
    // email.setSubject(subject +" - " +applicationName);
    // }

    private void addPriorityHeader(Email email, String priority) {

        if (priority.equals("High priority")) {
            email.addHeader("X-Priority", "2( high )");// or 1( highest ) 2(
                                                       // high ) 3( normal ) 4(
                                                       // low ) and 5( lowest )
        }
        else {
            email.addHeader("Priority", "4( low )");
        }

    }

    // public boolean sendMultiPartEmailMail(String id_fonte, String message,
    // Collection<File> attachments, String applicationName, String priority)
    // throws Exception{
    //
    // if(id_fonte==null || id_fonte.equals("")){
    // throw new Exception("id_fonte non specificata");
    // }
    //
    // MultiPartEmail email = new MultiPartEmail();
    //
    //
    // if(!Configure(email,id_fonte)){
    // return false;
    // }
    //
    // email.setMsg(message);
    // addPriorityHeader(email, priority);
    //
    // email.setSubject(priority +": " +subject +" - " +applicationName);
    // addAttachements(email, attachments);
    //
    // email.send();
    //
    // return true;
    // }

    public boolean sendMultiPartEmailMail(String id_fonte, String message, File attachment, String applicationName, String priority) throws Exception {

        if (id_fonte == null || id_fonte.equals("")) {
            throw new Exception("id_fonte non specificata");
        }

        MultiPartEmail email = new MultiPartEmail();

        if (!Configure(email, id_fonte)) {
            return false;
        }

        email.setMsg(message);
        addPriorityHeader(email, priority);

        email.setSubject(priority + ": " + subject + " - " + applicationName);
        addAttachement(email, attachment);

        email.send();

        return true;
    }

    //
    // public void sendImageHtmlEmail(String applicationName) throws Exception{
    // ImageHtmlEmail email = new ImageHtmlEmail();
    // Configure(email);
    // send(email, applicationName);
    // }
    //
    // public void sendHtmlEmail(String applicationName) throws Exception{
    // HtmlEmail email = new HtmlEmail();
    // Configure(email);
    // send(email, applicationName);
    // }

    // private void addAttachements(MultiPartEmail email, Collection<File>
    // attachments) throws EmailException {
    //
    // if(attachments==null){
    // return;
    // }
    // for (File f : attachments) {
    // String path = f.getAbsolutePath();
    // EmailAttachment attachment = new EmailAttachment();
    //
    // attachment.setPath(path);
    // attachment.setDisposition(EmailAttachment.ATTACHMENT);
    // attachment.setDescription(f.getName());
    // attachment.setName(f.getName());
    //
    // email.attach(attachment);
    // }
    //
    // }

    private void addAttachement(MultiPartEmail email, File attachment) throws EmailException {

        if (attachment == null || !attachment.exists()) {
            return;
        }
        
        String path = attachment.getAbsolutePath();
        EmailAttachment emailattachment = new EmailAttachment();

        emailattachment.setPath(path);
        emailattachment.setDisposition(EmailAttachment.ATTACHMENT);
        emailattachment.setDescription(attachment.getName());
        emailattachment.setName(attachment.getName());

        email.attach(emailattachment);

    }
}
