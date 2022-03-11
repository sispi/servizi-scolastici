package it.kdm.docer.conservazione.quartz;

import it.kdm.docer.conservazione.model.Aoo;
import it.kdm.docer.conservazione.model.Ente;
import it.kdm.docer.parer.daemon.database.JobManager;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SendMailJob implements Job {

	Logger log = org.slf4j.LoggerFactory.getLogger(SendMailJob.class);

	private List<Ente> enti;

	private Map<String,String> smtpConfig;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat sdfMail = new SimpleDateFormat("dd/MM/yyyy");

	private static final Character ESITO_ERR = 'E';
	private final static Character ESITO_CONSERV = 'C';
	
	JobManager manager;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			ClassPathXmlApplicationContext ctx =
					new ClassPathXmlApplicationContext(
							"META-INF/spring/applicationContext.xml");
			manager = ctx.getBean(JobManager.class);

			JobDataMap dataMap = context.getJobDetail().getJobDataMap();

			enti = (ArrayList<Ente>) dataMap.get("data");
			smtpConfig = (HashMap<String, String>) dataMap.get("smtpConfig");

			String from = smtpConfig.get("mail.from");

			Properties properties = new Properties();
			properties.putAll(smtpConfig);

			for (Ente ente : enti) {
				for (Aoo aoo : ente.getAoo()) {

					String hours = "-24";
					if (aoo.getPeriod_hours() != null && aoo.getPeriod_hours().trim() != "") {
						hours = aoo.getPeriod_hours().startsWith("-") ? aoo.getPeriod_hours() : "-".concat(aoo.getPeriod_hours());
					}
					Integer amount = Integer.parseInt(hours);

					Calendar endDate = Calendar.getInstance();
					Calendar startDate = Calendar.getInstance();

					startDate.add(Calendar.HOUR, amount.intValue());

					String enteValue = ente.getName();
					String aooValue = aoo.getName();

					//docId:XXXX;descrizione:YYY,docType:WWW;errCode:QQQ;maxRows:10000
					String whereCondition = aoo.getWhere_condition();
					Map<String, String> conditionValue = new HashMap<String, String>();

					if (whereCondition != null && whereCondition.length() > 0) {
						List<String> conditions = Arrays.asList(whereCondition.split(";"));
						for (String singleCondition : conditions) {
							String[] values = singleCondition.split(":");
							conditionValue.put(values[0], values[1]);
						}
					}

					String docId = conditionValue.containsKey("docId") ? conditionValue.get("docId") : null;
					String descrizione = conditionValue.containsKey("descrizione") ? conditionValue.get("descrizione") : null;
					String docType = conditionValue.containsKey("docType") ? conditionValue.get("docType") : null;
					String errCode = conditionValue.containsKey("errCode") ? conditionValue.get("errCode") : null;
					int maxRows = conditionValue.containsKey("maxRows") ? Integer.parseInt(conditionValue.get("maxRows")) : 10000;

					// Li prendo tutti poi li divido per C (conservati) e E (in errore)
					List<it.kdm.docer.parer.daemon.database.Job> jobs =
							manager.searchJobs(startDate,
									endDate,
									null,
									docId,
									descrizione,
									docType,
									errCode,
									enteValue,
									aooValue,
									maxRows
							);

					// Splitto in due liste
					List<it.kdm.docer.parer.daemon.database.Job> jobsOk = new ArrayList<it.kdm.docer.parer.daemon.database.Job>();
					List<it.kdm.docer.parer.daemon.database.Job> jobsInError = new ArrayList<it.kdm.docer.parer.daemon.database.Job>();
					if (!jobs.isEmpty()) {
						for (it.kdm.docer.parer.daemon.database.Job job : jobs) {
							if (job.getStato() == ESITO_CONSERV) {
								jobsOk.add(job);
							} else if (job.getStato() == ESITO_ERR) {
								jobsInError.add(job);
							}
						}
					}

					String to = aoo.getTo();

					final String user = properties.getProperty("mail.smtp.user");
					final String password = properties.getProperty("mail.smtp.password");
					Session session = null;
					if ("true".equals(properties.getProperty("mail.smtp.auth"))) {
						session = Session.getDefaultInstance(properties,
								new javax.mail.Authenticator() {
									protected PasswordAuthentication getPasswordAuthentication() {
										return new PasswordAuthentication(user, password);
									}
								});
					}else {
						session = Session.getDefaultInstance(properties);
					}

					try {
						// Oggetto della mail
						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress(from));
						message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
						String subject = aoo.getSubject();
						if (!StringUtils.isEmpty(subject))
							subject = subject.replace("{0}", sdfMail.format(new Date()))
									.replace("{1}", String.valueOf(jobsOk.size()))
									.replace("{2}", String.valueOf(jobsInError.size()));
						message.setSubject(subject);


						// Corpo della mail
						String body = aoo.getBody();
						if (jobsInError.size() > 0)
							body += "\n\nConsultare il report in allegato per il dettaglio degli errori.";
						body = body.replace("{0}", sdfMail.format(new Date()))
								.replace("{1}", String.valueOf(jobsOk.size()))
								.replace("{2}", String.valueOf(jobsInError.size()));

						MimeBodyPart messageBodyPart = new MimeBodyPart();
						messageBodyPart.setContent(message, "text/html");
						messageBodyPart.setText(body);

						Multipart multipart = new MimeMultipart();
						multipart.addBodyPart(messageBodyPart);

						// Se ci sono errori aggiungo l'allegato
						if (jobsInError.size() > 0) {
							MimeBodyPart attachPart = new MimeBodyPart();

							String fileName = "/tmp/docer/errorjob_" + enteValue + "_" + aooValue + "_" + sdf.format(new Date()) + ".csv";
							writeCsvFile(fileName, jobsInError);

							try {
								attachPart.attachFile(fileName);
							} catch (IOException e) {
								e.printStackTrace();
							}
							multipart.addBodyPart(attachPart);
						}

						message.setContent(multipart);

						Transport.send(message);
						log.info("Messaggio inviato correttamente");
					} catch (MessagingException mex) {
						log.error("Errore durante l'invio della mail: " + mex.getMessage(), mex);
					}
				}
			}
		}catch(Exception e) {
			log.error("Errore durante l'esecuzione del job: " + e.getMessage(), e);
		}
	}



	private void writeCsvFile(String fileName, List<it.kdm.docer.parer.daemon.database.Job> jobs ) {
		log.info( "generazione cvs in corso" );
		String COMMA_DELIMITER = ";";
		String NEW_LINE_SEPARATOR = "\n";
		String FILE_HEADER = "doc id;tipo documento;codice errore;messaggio errore;ente;aoo";

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());

			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			//Write a new student object list to the CSV file
			for ( it.kdm.docer.parer.daemon.database.Job job : jobs ) {
				fileWriter.append(job.getDocId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(job.getTipoDocumento() );
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(job.getErrorCode());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(job.getErrorMessage());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(job.getCodEnte());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(job.getCodAoo());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
