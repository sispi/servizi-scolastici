package it.kdm.docer.conservazione.servlet;

import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.commons.Config;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.conservazione.ConservazioneException;
import it.kdm.docer.conservazione.model.Aoo;
import it.kdm.docer.conservazione.model.Ente;
import it.kdm.docer.conservazione.model.Group;
import it.kdm.docer.conservazione.model.Section;
import it.kdm.docer.sdk.exceptions.DocerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.slf4j.Logger;
import org.jaxen.JaxenException;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 22/10/15
 * Time: 15.37
 * To change this template use File | Settings | File Templates.
 */
public class QuartzServlet extends HttpServlet {

	Logger log = org.slf4j.LoggerFactory.getLogger(QuartzServlet.class);

	private BusinessLogic bl;
	private Scheduler scheduler;
	private Config config;
	private File configFile;
	private XStream xStream;
	private Map<String, String> smtpConfig;
	private Properties propsConservazione = new Properties();

	private void readPropertiesFile () throws IOException, XMLStreamException, JaxenException, DocerException {
		configFile = new java.io.File(propsConservazione.getProperty("config.path.job"));
		if(!configFile.exists()) {
			throw new FileNotFoundException(configFile.getAbsolutePath());
		}
		config = new Config();
		config.setConfigFile(configFile);
		
		String docerProvider = config.getNode(makePropertyXpath("docer", "provider")).getText();

		int primarySearchMaxRows = Integer.parseInt(config.getNode(makePropertyXpath("docer", "primarySearchMaxRows")).getText());

		BusinessLogic.setConfigPath(configFile.getAbsolutePath());
		bl = new BusinessLogic(docerProvider, primarySearchMaxRows);
		
	}

	@Override
	public void init() throws ServletException {

		try {
			
			propsConservazione.putAll(ConfigurationUtils.loadProperties("conservazione.properties"));
			smtpConfig= new HashMap<String,String>();
			smtpConfig.put("mail.smtp.host", propsConservazione.getProperty("mail.smtp.host"));
			smtpConfig.put("mail.smtp.port", propsConservazione.getProperty("mail.smtp.port"));
			smtpConfig.put("mail.smtp.auth", propsConservazione.getProperty("mail.smtp.auth"));
			smtpConfig.put("mail.smtp.user", propsConservazione.getProperty("mail.smtp.user"));
			smtpConfig.put("mail.smtp.password", propsConservazione.getProperty("mail.smtp.password"));
			smtpConfig.put("mail.from", propsConservazione.getProperty("mail.from"));

			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();

			xStream = new XStream() {
				@Override
				protected MapperWrapper wrapMapper(MapperWrapper next) {
					return new MapperWrapper(next) {
						@Override
						public boolean shouldSerializeMember(Class definedIn, String fieldName) {
							if (definedIn == Object.class) {
								return false;
							}
							return super.shouldSerializeMember(definedIn, fieldName);
						}
					};
				}
			};
			Class[] types = new Class [] {Group.class, Aoo.class, Ente.class, Section.class};
			xStream.processAnnotations(types);
			
			readPropertiesFile();
			schedule();
			
		} catch(Exception e) {
			log.error("errore!!!!!", e);
			e.printStackTrace();

		}
	}

	private String makePropertyXpath(String section, String property) {
		return String.format("//group[@name='conservazione']/section[@name='%s']/%s", section, property);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			readPropertiesFile();
			schedule();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (JaxenException e) {
			e.printStackTrace();
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printJobs();
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	private void schedule () throws ClassNotFoundException, JaxenException, SchedulerException {
		log.info("schedule jobs");
		
		//String valueFromGetConfig = "<?xml version='1.0' encoding='UTF-8'?><group name='jobs'><section name='mailConservazioneJob' cronExpr='0/30 * * * * ?' className='it.kdm.docer.conservazione.quartz.SendMailJob'><log_path /><rows_displayed>10</rows_displayed><tes /><ente name='ENTEPROVA'><aoo name='AOOPROVA'><where_condition /><to>francesco.mongali@silicondev.com</to><subject>Oggetto della mail</subject><body>Corpo della mail</body></aoo><aoo name='AOOPROVA1'><where_condition /><to>francesco.mongali@silicondev.com</to><subject>Oggetto della mail AOOPROVA2</subject><body>Corpo della mail AOOPROVA2</body></aoo></ente><ente name='ENTEPROVA1'><aoo name='AOOPROVA'><where_condition /><to>francesco.mongali@silicondev.com</to><subject>Oggetto della mail</subject><body>Corpo della mail</body></aoo></ente></section><section name='testJob' cronExpr='0 1 * * * ?' className='it.kdm.docer.conservazione.quartz.HelloWordJob' /></group>";
		
        OMElement jobsNode = config.getNode("//group[@name='jobs']");
        log.debug( jobsNode.toString() );
        Group groupJobs = (Group) xStream.fromXML( jobsNode.toString()  );

		scheduler.clear();
		for ( Section section : groupJobs.getSection() ) {
			String jobName = section.getName();
			String className = section.getClassName();
			String cronTrigger = section.getCronExpr();
			String cliParams = section.getCli_params();
			Class<Job> clazz=(Class<Job>)Class.forName(className);
			JobDetail job = JobBuilder.newJob(clazz)
					.withIdentity(jobName, "admins")
					.build(); 

			CronTrigger cronTrigger1 = TriggerBuilder.newTrigger()
					.withIdentity(jobName.concat("Trigger"), "admins")
					.withSchedule(CronScheduleBuilder.cronSchedule(cronTrigger))
					.withPriority(10)
					.forJob(jobName, "admins")
					.build();                
			
            if ( section.getIsCliJob() ) {
            	job.getJobDataMap().put("data", cliParams );
            } else {
				job.getJobDataMap().put("data", section.getEnte() );
				job.getJobDataMap().put("smtpConfig", smtpConfig );
            }
            log.info( "scheduling job: " + job );
            scheduler.scheduleJob(job, cronTrigger1);
		}
		
		log.info("jobs scheduled");
	}
	
	
	private void printJobs ( ) {
		try {
			for (String groupName : scheduler.getJobGroupNames()) {
			     for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					  String jobName = jobKey.getName();
					  String jobGroup = jobKey.getGroup();
					  List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
					  CronTrigger cronTrigger = (CronTrigger) triggers.get(0);
					  Date nextFireTime = cronTrigger.getNextFireTime();
					  String cronExpression = cronTrigger.getCronExpression();
					  log.debug("[jobName] : " + jobName + " [groupName] : " + jobGroup + " - " + nextFireTime + " - [cronExpression]: " + cronExpression );

				  }
			    }
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		printJobs();
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	public void destroy() {
		super.destroy();
		try {
			log.debug( "shutdown quartz scheduler" );
			scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public String readConfig(String token) throws ConservazioneException {
		try {
			return config.readConfig();
		} catch (Exception e) {
			throw new ConservazioneException(e);
		}
	}

}
