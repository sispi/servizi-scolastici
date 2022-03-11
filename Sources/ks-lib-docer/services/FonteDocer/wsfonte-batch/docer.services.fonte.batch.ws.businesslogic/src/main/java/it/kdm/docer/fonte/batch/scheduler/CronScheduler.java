package it.kdm.docer.fonte.batch.scheduler;

import it.kdm.docer.commons.Config;
import it.kdm.docer.fonte.batch.popolamentoFonte.BatchPopolamentoFonte;
import it.kdm.docer.fonte.batch.popolamentoRaccoglitore.BatchPopolamentoRaccoglitore;

import java.text.ParseException;
import java.util.Properties;

import org.apache.axiom.om.OMElement;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;


public class CronScheduler{

    private static boolean started = false;
    private static boolean batchPopolamentoFonteScheduled = false;    
    private static boolean batchPopolamentoRaccoglitoreScheduled = false;
    
    private static Scheduler scheduler = null;
    
    public CronScheduler() throws SchedulerException{        
        if(!started){
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            started = true;
        }        
    }
    
    public void shutdown() throws SchedulerException{        
        if(started){
            scheduler.shutdown();           
        }     
    }
    

    public void scheduleBatchPopolamentoFonte(Config config, Properties configurationProperties) throws Exception {
        
        if(!started){
            throw new SchedulerException("scheduler not started");
        }
        
        if(!batchPopolamentoFonteScheduled){        	        	
        	
            try {

            	boolean enabled = false;
            	OMElement ome = config.getNode("//group[@name='batch-popolamento-fonte']/section[@name='config']/enabled");
                if(ome!=null){
                    enabled = Boolean.valueOf(ome.getText());
                }
                
                if(!enabled){
                	return;
                }
                
            	
                String runCronExp = readConfigKey(config, "//group[@name='batch-popolamento-fonte']/section[@name='config']/runCronExp");

                if (runCronExp == null){
                	throw new Exception("Parametri non completi per la configurazione del batch: chiave //group[@name='batch-popolamento-fonte']/section[@name='config']/runCronExp non trovata");
                }
                    
                 
                BatchPopolamentoFonte.setConfig(config, configurationProperties);
                
                JobDetail job1 = JobBuilder.newJob(BatchPopolamentoFonte.class)
                        .withIdentity("jobBatchPopolamentoFonte", "admins").build();                             
                
                //job1.getJobDataMap().put("count", 0);
                
                CronTrigger cronTrigger1 = TriggerBuilder.newTrigger()
                        .withIdentity("cronTriggerBatchPopolamentoFonte", "admins")
                        .withSchedule(CronScheduleBuilder.cronSchedule(runCronExp))
                        .withPriority(10)
                        .forJob("jobBatchPopolamentoFonte", "admins")
                        .build();                
                        
                // schedule the job with the trigger
                scheduler.scheduleJob(job1, cronTrigger1);
                
                batchPopolamentoFonteScheduled = true;
                
                System.out.println("BatchPopolamentoFonte has been Scheduled");
                
            }
            catch (SchedulerException e){
                throw e;
            }            
           
        }
    }
    
    public void scheduleBatchPopolamentoRaccoglitore(Config config, Properties configurationProperties) throws Exception {
        
        if(!started){
            throw new SchedulerException("scheduler not started");
        }
        
        if(!batchPopolamentoRaccoglitoreScheduled){
            
        	try {                
        		boolean enabled = false;
            	OMElement ome = config.getNode("//group[@name='batch-popolamento-raccoglitore']/section[@name='config']/enabled");
                if(ome!=null){
                	enabled = Boolean.valueOf(ome.getText());
                }
                
                if(!enabled){
                	return;
                }
                
                String runCronExp = readConfigKey(config, "//group[@name='batch-popolamento-raccoglitore']/section[@name='config']/runCronExp");

                if (runCronExp == null){
                	throw new Exception("Parametri non completi per la configurazione del batch: chiave //group[@name='batch-popolamento-raccoglitore']/section[@name='config']/runCronExp non trovata");
                }
                                    
                BatchPopolamentoRaccoglitore.setConfig(config, configurationProperties);
                
                JobDetail job2 = JobBuilder.newJob(BatchPopolamentoRaccoglitore.class)
                        .withIdentity("jobBatchPopolamentoRaccoglitore", "admins").build();                             
                
                //job1.getJobDataMap().put("count", 0);
                
                CronTrigger cronTrigger2 = TriggerBuilder.newTrigger()
                        .withIdentity("cronTriggerBatchPopolamentoRaccoglitore", "admins")
                        .withSchedule(CronScheduleBuilder.cronSchedule(runCronExp))
                        .withPriority(5) // 5 e' il default
                        .forJob("jobBatchPopolamentoRaccoglitore", "admins")
                        .build();                
                        
                // schedule the job with the trigger
                scheduler.scheduleJob(job2, cronTrigger2);
                
                batchPopolamentoRaccoglitoreScheduled = true;
                
                System.out.println("BatchPopolamentoRaccoglitore has been Scheduled");
            }
            catch (SchedulerException e){
                throw e;
            }
           
        }
    }
    
    
    public void unscheduleBatchPopolamentoFonte() throws SchedulerException{
        // and start it off
        if(!started){
            throw new SchedulerException("scheduler not started");
        }
        if(batchPopolamentoFonteScheduled){
            
            scheduler.deleteJob(JobKey.jobKey("jobBatchPopolamentoFonte", "admins"));
            //scheduler.unscheduleJob(TriggerKey.triggerKey("cronTriggerBatchPopolamentoFonte", "admins"));
            
            batchPopolamentoFonteScheduled = false;
            
            System.out.println("BatchPopolamentoFonte has been Unscheduled");
        }               
                
    }
    
    public void unscheduleBatchPopolamentoRaccoglitore() throws SchedulerException{
        // and start it off
        if(!started){
            throw new SchedulerException("scheduler not started");
        }
        
        if(batchPopolamentoRaccoglitoreScheduled){
            scheduler.deleteJob(JobKey.jobKey("jobBatchPopolamentoRaccoglitore", "admins"));
            //scheduler.unscheduleJob(TriggerKey.triggerKey("cronTriggerBatchPopolamentoRaccoglitore", "admins"));
            batchPopolamentoRaccoglitoreScheduled = false;
            
            System.out.println("BatchPopolamentoRaccoglitore has been Unscheduled");
        }        
                
    }
    
    public String getStatus() throws SchedulerException{        
       
        return "scheduler started:" +started +"; BatchPopolamentoFonte scheduled: "+batchPopolamentoFonteScheduled +"; BatchPopolamentoRaccoglitore scheduled: " +batchPopolamentoRaccoglitoreScheduled;
        
    }    
    
    private String readConfigKey(Config config, String xpath) throws Exception {
        OMElement omelement = config.getNode(xpath);
        if (omelement == null) {
            throw new Exception("chiave " + xpath + " mancante in configurazione");
        }

        return omelement.getText();
    }
   
    
}