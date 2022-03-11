/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.fonte.batch;

import it.kdm.docer.commons.Config;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.fonte.batch.popolamentoFonte.BatchPopolamentoFonte;
import it.kdm.docer.fonte.batch.popolamentoRaccoglitore.BatchPopolamentoRaccoglitore;
import it.kdm.docer.fonte.batch.scheduler.CronScheduler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.quartz.SchedulerException;

public class BusinessLogic {

    
    private static it.kdm.docer.fonte.batch.scheduler.CronScheduler cronScheduler = null;

    private Config config;
    private Properties configurationProperties;   

    private Logger logger = org.slf4j.LoggerFactory.getLogger(BusinessLogic.class);
    
    private static List<String> allowedUsers = new ArrayList<String>();
    
    private static boolean batchPopolamentoFonteEnabled = false;
    private static boolean batchPopolamentoRaccoglitoreEnabled = false;
    
    TicketCipher cipher = new TicketCipher();
    
    public BusinessLogic() throws Exception {

        try {                                
        	  
            this.config = new Config();
            readAllowedUsers();
            readEnabled();
            
            InputStream in = this.getClass().getResourceAsStream("/configuration.properties");
            if (in == null) {                
                throw new Exception("file /configuration.properties non trovato");
            }

            configurationProperties = new Properties();
            configurationProperties.load(in);

            in.close();
            
        }
        catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw e;
        }
        catch (XMLStreamException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw e;
        }    

        try {
            if(cronScheduler==null){
                cronScheduler = new CronScheduler();
                logger.log(Level.DEBUG, "CronScheduler Started");
            }

        }
        catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw e;
        }               
                              
    }   
    
    private void readAllowedUsers() throws Exception{
    
        allowedUsers.clear();
        
        List<OMElement> omelements = config.getNodes("//group[@name='common-configuration']/section[@name='allowed_users']/userid");
        if(omelements==null || omelements.size()<1){
            logger.log(Level.ERROR, "nessun utente autorizzato: definire gli utenti nelle chiavi di configurazione //group[@name='batch-users']/section[@name='allowed_users']/userid");
            throw new Exception("nessun utente autorizzato: definire gli utenti nelle chiavi di configurazione //group[@name='batch-users']/section[@name='allowed_users']/userid");
        }
        
         for(OMElement elem : omelements){
             if(elem.getText().equals("")){
                 continue;
             }
             
             allowedUsers.add(elem.getText().trim());
         }
         
    }
    
    private void readEnabled() throws Exception{
        
        OMElement ome = config.getNode("//group[@name='batch-popolamento-fonte']/section[@name='config']/enabled");
        if(ome!=null){
            batchPopolamentoFonteEnabled = Boolean.valueOf(ome.getText());
        }
        
        ome = config.getNode("//group[@name='batch-popolamento-raccoglitore']/section[@name='config']/enabled");
        if(ome!=null){
            batchPopolamentoRaccoglitoreEnabled = Boolean.valueOf(ome.getText());
        }        
        
    }
    
    public Config getConfig(){
        return this.config;
    }
    
    public Properties getConfigurationProperties() {
        return configurationProperties;
    }
    
    public String login(String userId, String password, String codiceEnte) throws Exception {
        
        if(allowedUsers.contains(userId)){
            return cipher.encryptTicket("allowed");
        }

        return cipher.encryptTicket("not-allowed");
    }

    public void logout(String ticket) throws Exception {

    }

    public boolean writeConfig(String xml) throws Exception {
        try {
            
//            if(!ticket.equals("allowed")){
//                throw new Exception("utente non autorizzato");
//            }
          
            this.config.writeConfig(xml);
            readAllowedUsers();
            readEnabled();
            
            cronScheduler.unscheduleBatchPopolamentoFonte();
            
            cronScheduler.unscheduleBatchPopolamentoRaccoglitore();
            
            if(batchPopolamentoFonteEnabled){               
                cronScheduler.scheduleBatchPopolamentoFonte(config, configurationProperties);
            }
            
            if(batchPopolamentoRaccoglitoreEnabled){               
                cronScheduler.scheduleBatchPopolamentoRaccoglitore(config, configurationProperties);
            }
            
            return true;
        }
        catch (Exception ex) {
            logger.log(Level.ERROR, ex.getMessage(), ex);
            throw ex;
        }
        
    }

    public String readConfig(String ticket) throws Exception {
        try {
                        
            return this.config.readConfig();
        }
        catch (Exception ex) {
            logger.log(Level.ERROR, ex.getMessage(), ex);
            throw ex;
        }
    }

    public void executeBatchPopolamentoFonte(String ticket, Config configuration, Properties configProperties) throws Exception {

        try {
        	ticket = cipher.decryptTicket(ticket);
        	
            if(!ticket.equals("allowed")){
                throw new Exception("utente non autorizzato");
            }
            
            BatchPopolamentoFonte.setConfig(configuration, configProperties);
            BatchPopolamentoFonte bpf = new BatchPopolamentoFonte();
            bpf.execute(null);                       
            
        }
        catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw e;
        }

    }
    
    public void executeBatchPopolamentoRaccoglitore(String ticket, Config configuration, Properties configProperties) throws Exception {

        try {
            
        	ticket = cipher.decryptTicket(ticket);
        	
            if(!ticket.equals("allowed")){
                throw new Exception("utente non autorizzato");
            }
            
            
            BatchPopolamentoRaccoglitore.setConfig(configuration, configProperties);
            BatchPopolamentoRaccoglitore bpr = new BatchPopolamentoRaccoglitore();
            bpr.execute(null);                       
            
        }
        catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw e;
        }

    }
    
    public void scheduleBatchPopolamentoFonte(String ticket) throws Exception {

        try {
            
        	ticket = cipher.decryptTicket(ticket);
        			
            if(!ticket.equals("allowed")){
                throw new Exception("utente non autorizzato");
            }
                      
            if(batchPopolamentoFonteEnabled){
                
                cronScheduler.scheduleBatchPopolamentoFonte(config, configurationProperties);
            }
                        
            
        }
        catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw e;
        }

    }

    public void scheduleBatchPopolamentoRaccoglitore(String ticket) throws Exception {

        try {
            
        	ticket = cipher.decryptTicket(ticket);
        	
            if(!ticket.equals("allowed")){
                throw new Exception("utente non autorizzato");
            }
            
            if(batchPopolamentoRaccoglitoreEnabled){
                               
                cronScheduler.scheduleBatchPopolamentoRaccoglitore(config, configurationProperties);
            }
            
        }
        catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw e;
        }

    }
    
    public void unscheduleBatchPopolamentoFonte(String ticket) throws Exception {

        try {
        	
        	ticket = cipher.decryptTicket(ticket);
        			
            if(!ticket.equals("allowed")){
                throw new Exception("utente non autorizzato");
            }
            
            cronScheduler.unscheduleBatchPopolamentoFonte();
        }
        catch (SchedulerException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw e;
        }
    }

    public void unscheduleBatchPopolamentoRaccoglitore(String ticket) throws Exception {

        try {
        	ticket = cipher.decryptTicket(ticket);
        	
            if(!ticket.equals("allowed")){
                throw new Exception("utente non autorizzato");
            }
            
            cronScheduler.unscheduleBatchPopolamentoRaccoglitore();
        }
        catch (SchedulerException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw e;
        }
    }
    
    public String getStatus(String ticket) throws Exception {

        try {
        	
            return cronScheduler.getStatus();
        }
        catch (SchedulerException e) {
            logger.log(Level.ERROR, e.getMessage(), e);
            throw e;
        }

    }

    public void shutdown() throws Throwable {

        try {
            
            cronScheduler.shutdown();
            logger.log(Level.DEBUG, "cronScheduler shutdown");
        }
        catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage(), e);
        }

    }
    
  
}
