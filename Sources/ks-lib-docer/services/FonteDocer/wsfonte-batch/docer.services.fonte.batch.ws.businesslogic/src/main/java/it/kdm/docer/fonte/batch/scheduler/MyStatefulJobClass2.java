package it.kdm.docer.fonte.batch.scheduler;

import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MyStatefulJobClass2 implements org.quartz.StatefulJob {   
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        System.out.println("-- MyStatefulJobClass2 --");
        System.out.println("-- ScheduledFireTime: " +context.getScheduledFireTime());
        System.out.println("-- FireTime: " +context.getFireTime());
        System.out.println("-- NextFireTime: " +context.getNextFireTime());       
        
        JobDataMap dataMap = context.getMergedJobDataMap();
     
        
        System.out.println("-- EndTime: " +new Date());
        
        
        
        System.out.println("");
        System.out.println("--");
        System.out.println("");
    }

}
