package it.kdm.docer.conservazione.quartz;

import java.io.IOException;

import org.slf4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CommandLineExecuterJob implements Job {
	
	Logger log = org.slf4j.LoggerFactory.getLogger(CommandLineExecuterJob.class);
	private String commandLine; 
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		commandLine = (String)dataMap.get("data");
		log.info( "start CommandLineExecuterJob with commandaline: " + commandLine );

		Runtime rt = Runtime.getRuntime();
		try {
			Process pr = rt.exec(commandLine);
		} catch (IOException e) {
            log.error(e.getMessage(), e);
		}
	}

}
