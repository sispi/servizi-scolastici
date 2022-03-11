/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.fonte;

import org.joda.time.DateTime;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.fonte.batch.BusinessLogic;
import it.kdm.docer.fonte.batch.popolamentoFonte.BatchPopolamentoFonte;
import it.kdm.docer.fonte.batch.popolamentoRaccoglitore.BatchPopolamentoRaccoglitore;

public class WSFonteBatch {  

    private static BusinessLogic businessLogic;

    public WSFonteBatch() throws FonteBatchException {
        try {
            
            if(businessLogic==null){
                businessLogic = new BusinessLogic();
            }
        }
        catch (Exception e) {
            throw new FonteBatchException(e.getMessage());
        }
    }

    public String login(String userId, String password, String codiceEnte) throws FonteBatchException {
        try {                       
            
            return businessLogic.login(userId, password, codiceEnte);
        }
        catch (SecurityException e) {
            throw new FonteBatchException(e.getMessage());
        }
        catch (Exception e) {
            throw new FonteBatchException(e.getMessage());
        }
    }

    public boolean logout(String token) throws FonteBatchException {
        try {
            businessLogic.logout(token);
        }
        catch (SecurityException e) {
            throw new FonteBatchException(e.getMessage());
        }
        catch (Exception e) {
            throw new FonteBatchException(e.getMessage());
        }
        return true;
    }

    public boolean writeConfig(String token, String xmlConfig) throws FonteBatchException {
        try {
            return businessLogic.writeConfig(xmlConfig);
        }
        catch (Exception e) {
            throw new FonteBatchException(e.getMessage());
        }
    }

    public String readConfig(String token) throws FonteBatchException {
        try {
            return businessLogic.readConfig(token);
        }
        catch (Exception e) {
            throw new FonteBatchException(e.getMessage());
        }
    }

    public boolean scheduleBatchPopolamentoFonte(String token) throws FonteBatchException {
        try {
            businessLogic.scheduleBatchPopolamentoFonte(token);
        }
        catch (SecurityException e) {
            throw new FonteBatchException(e.getMessage());
        }
        catch (Exception e) {
            throw new FonteBatchException(e.getMessage());
        }
        return true;
    }

    public boolean unscheduleBatchPopolamentoFonte(String token) throws FonteBatchException {
        try {
            businessLogic.unscheduleBatchPopolamentoFonte(token);
        }
        catch (SecurityException e) {
            throw new FonteBatchException(e.getMessage());
        }
        catch (Exception e) {
            throw new FonteBatchException(e.getMessage());
        }
        
        return true;
    }

    public boolean scheduleBatchPopolamentoRaccoglitore(String token) throws FonteBatchException {
        try {
            businessLogic.scheduleBatchPopolamentoRaccoglitore(token);
        }
        catch (SecurityException e) {
            throw new FonteBatchException(e.getMessage());
        }
        catch (Exception e) {
            throw new FonteBatchException(e.getMessage());
        }
        return true;
    }

    public boolean unscheduleBatchPopolamentoRaccoglitore(String token) throws FonteBatchException {
        try {
            businessLogic.unscheduleBatchPopolamentoRaccoglitore(token);
        }
        catch (SecurityException e) {
            throw new FonteBatchException(e.getMessage());
        }
        catch (Exception e) {
            throw new FonteBatchException(e.getMessage());
        }
        
        return true;
    }
    
    public String getStaus(String token) throws FonteBatchException {
        try {
            return businessLogic.getStatus(token);
        }
        catch (SecurityException e) {
            throw new FonteBatchException(e.getMessage());
        }
        catch (Exception e) {
            throw new FonteBatchException(e.getMessage());
        }
    }

    public String getLastMessages(String token) {
        return BatchPopolamentoFonte.getLastMessage() +"\\r\\n" +BatchPopolamentoRaccoglitore.getLastMessage();        	        
    }

    public String getServerLocalTime(String token){
        DateTime d = new DateTime();
    	return d.toString("yyyy-MM-dd'T'HH:mm:ss.SSSZ");    	
    }
//    @Override
//    protected void finalize() throws Throwable {
//
//        try {
//            businessLogic.shutdown();
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        super.finalize();
//    }
}
