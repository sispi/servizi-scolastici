package it.kdm.docer.conservazione.batch.scheduler;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.conservazione.batch.Azione;
import it.kdm.docer.conservazione.batch.Conservazione;
import it.kdm.docer.conservazione.batch.ConservazioneResult;
import it.kdm.docer.conservazione.batch.Constants;
import it.kdm.docer.conservazione.batch.DocerLib;
import it.kdm.docer.conservazione.batch.DocerLib.EnumStatiConservazione;
import it.kdm.docer.parer.daemon.database.Job;
import it.kdm.docer.parer.daemon.database.JobManager;
import it.kdm.docer.parer.daemon.database.Log;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Scheduler {
	
	/** 
	 * Il massimo numero di risultati ottenibili da una query
	 * sul database.
	 */
	private static final int MAX_JOBS_TO_RETRIEVE = 100;
	
	/** Le credenziali per l'accesso a Doc/ER */
	private String username;
	private String password;
	private String codiceEnte;
	
	/** I client soap necessari alla conservazione */
	private Conservazione conservazione;
	private DocerLib docerLib;

	/** I token generati dall'autenticazione */
	private String tokenDocer;
	private String tokenConservazione;
	
	Logger logger = LoggerFactory.getLogger(Scheduler.class);

	/** 
	 * Il JobManager che esegue le operazioni su db.
	 * E' inizializzato e gestito da Spring.
	 */
	private JobManager manager;

	// TODO: Pick config from arguments
	public Scheduler() throws Exception {
		try {
			logger.info("Sheduler di Conservazione");
			
			Properties props = ConfigurationUtils.loadProperties("demone.properties");

			username = props.getProperty("username");
			password = props.getProperty("password");
			codiceEnte = props.getProperty("codiceEnte");

			logger.info("Inizializzato lo scheduler con username {} e codiceEnte {}",
					username, codiceEnte);
			
			logger.info("Epr di conservazione: {}", props.getProperty("conservazione.epr"));
			logger.info("Epr di docer: {}", props.getProperty("docer.epr"));
			
			conservazione = new Conservazione();
			conservazione.setEprConservazione(
							props.getProperty("conservazione.epr"));
			conservazione.setEprDocer(props.getProperty("docer.epr"));
			
			logger.info("Autenticazione su conservazione in corso...");
			tokenConservazione = conservazione.login(username,
													 password, codiceEnte);
			logger.info("Autenticazione su conservazione avvenuta con successo");
						
			docerLib = new DocerLib();
			docerLib.setEprDocer(props.getProperty("docer.epr"));
			docerLib.setEprConservazione(props.getProperty("conservazione.epr"));

			logger.info("Autenticazione su docer in corso...");
			tokenDocer = docerLib.login(username, password, codiceEnte);
			logger.info("Autenticazione su docer avvenuta con successo");
			
		} catch (Exception e) {
			logger.error("E' avvenuto un errore in fase di inizializzazione",
					e);
			throw e;
		}
	}
	
	public void process() {
		ClassPathXmlApplicationContext ctx = 
				new ClassPathXmlApplicationContext(
						"META-INF/spring/applicationContext.xml");
		manager = ctx.getBean(JobManager.class);
		
		// Jobs interrotti. Normalmente dovrebbe essercene soltanto uno,
		// ma e' probabile che se ne possano accumulare dopo interruzzioni 
		// successive del batch
		logger.info("Ricerca di jobs interrotti");
		List<Job> jobs = manager.getInterruptedJobs(); 
		
		if (jobs.size() == 0) {
			logger.info("Non ci sono jobs interrotti");
		}
		
		for (Job job : jobs) {
			processJob(job);
		}
		
		// Nuovi jobs da processare
		logger.info("Ricerca di jobs da processare");
		jobs = manager.getJobsToProcess(MAX_JOBS_TO_RETRIEVE);

		if (jobs.size() == 0) {
			logger.info("Non ci sono jobs da processare");
		}
		
		for (Job job : jobs) {
			processJob(job);
		}
		
		manager = null;
		ctx.close();
	}
	
	private void processJob(Job job) {
		logger.info("Trovato {}", job);
		
		if (job.getStato() == 'A') {
			logger.debug("stato: {} -> {}", job.getStato(), 'W');
			job.setStato('W');
            job = manager.updateJob(job);
		}
		
		Log log = new Log(job);
		manager.saveLog(log);

		try {
			// Se il job e' in stato W puo' essere appena cominciato
			// oppure interrotto durante l'esecuzione della azione specifica
			if (job.getStato() == 'W') {
				log = performAction(job, log);
			}
		} catch (Exception ex) {
			// Lo stato E e' per i jobs la cui conservazione e' fallita
			logger.error("Errore durante la conservazione di {}",
					job, ex);
			logger.debug("stato: {} -> {}", log.getEsito(), 'E');
			log.setEsito('E');
			log.setMessage(ex.getMessage());
		}
		
		log = manager.updateLog(log);

		try {
			// A questo punto sono rimasti solo i jobs in stato X (conservati)
			// o i jobs il cui update di Doc/ER e' fallito, o jobs in errore.
			assert job.getStato() == 'X' || job.getStato() == 'F'
					|| job.getStato() == 'E';
			updateDocer(job);
			if (job.getStato() != 'E') {
				logger.debug("stato: {} -> {}", log.getEsito(), 'C');
				log.setEsito('C');
			}
			
			log = manager.updateLog(log);
			
		} catch (Exception ex) {
			// Lo stato F e' per i jobs il cui update su Doc/ER e' fallito
			logger.error("L'aggiornamento di Doc/ER e' fallito per {}",
					job, ex);
			logger.debug("stato: {} -> {}", log.getEsito(), 'F');
			log.setEsito('F');
			
			manager.updateLog(log);
		}
	}

	private void updateDocer(Job job) throws Exception {
		EnumStatiConservazione stato;
		logger.info("Aggiornamento di Doc/ER in corso per {}", job);
		switch (job.getStato()) {
		case 'X':
			logger.info("Job in stato conservato");
			stato = EnumStatiConservazione.conservato;
			break;
		case 'F':
			logger.info("Job in stato di errore in update di Doc/ER. " +
					"Da impostare come conservato");
			stato = EnumStatiConservazione.conservato;
			break;
		default:
			logger.info("Job in errore.");
			logger.debug("Stato trovato: {}", job.getStato());
			stato = EnumStatiConservazione.in_errore;
			break;
		}
		
		String docId = job.getDocId();
		if (job.getAzione() == Azione.AGGIUNGI_DOCUMENTO.toChar()) {
			docId = docId.substring(docId.indexOf(Constants.DOCID_SEPARATOR)+1);
		}
		docerLib.updateDocumentState(tokenDocer, docId, stato);

		//setta il t_conserv
		Map<String, String> metaMap = new HashMap<String, String>();
		metaMap.put("T_CONSERV", job.getTipoConservazione());

        // DOCER-37 Aggiunta gestione del campo errcode_conserv (12-10-2015)
        if (stato == EnumStatiConservazione.in_errore) {
//            Map<String, String> metaMap = new HashMap<String, String>();
            metaMap.put("ERRCODE_CONSERV", job.getErrorCode());
        }

		docerLib.updateDocumentProfile(tokenDocer, docId, metaMap);

		logger.info("Aggiornamento di Doc/ER terminato con successo");
	}

	private Log performAction(Job job, Log log) throws Exception {
		logger.info("Conservazione di {} in corso", job);
		log.setDataChiamata(Calendar.getInstance());
		
		try {
			ConservazioneResult result = execute(job);
			
			log.setDataRisposta(Calendar.getInstance());
			log.setXmlRichiesta(result.getXmlChiamata());
			log.setXmlEsito(result.getXmlEsitoVersamento());
			
			switch (result.getEsito()) {
			case POSITIVO:
				log.setErrorCode("0");
				log.setMessage("success");
				logger.debug("stato: {} -> {}", log.getEsito(), 'X');
				log.setEsito('X');
				logger.info("Conservazione effettuata con successo");
				break;
			case WARNING:
				log.setErrorCode(result.getErrorCode());
				log.setMessage(result.getErrorMessage());
				logger.debug("stato: {} -> {}", log.getEsito(), 'X');
				log.setEsito('X');
				logger.info("Conservazione effettuata con warning");
				logger.info("Codice di warning: {}", result.getErrorCode());
				logger.info("Messaggio di warning: {}", result.getErrorMessage());
				break;
			case ERRORE:
				log.setErrorCode(result.getErrorCode());
				log.setMessage(result.getErrorMessage());
				logger.debug("stato: {} -> {}", log.getEsito(), 'E');
				log.setEsito('E');
				logger.info("Conservazione in errore");
				logger.info("Codice di errore: {}", result.getErrorCode());
				logger.info("Messaggio di errore: {}", result.getErrorMessage());
				break;
			default:
				assert false : result.getEsito();
			}
		} catch (Exception e) {
			log.setDataRisposta(Calendar.getInstance());
			log.setErrorCode("500");
			log.setMessage(e.getLocalizedMessage());
			logger.debug("stato: {} -> {}", log.getEsito(), 'E');
			log.setEsito('E');
			logger.error("Errore non recuperabile di conservazione", e);
        } catch (Error e) {
            log.setDataRisposta(Calendar.getInstance());
            log.setErrorCode("500");
            log.setMessage(e.getLocalizedMessage());
            logger.debug("stato: {} -> {}", log.getEsito(), 'E');
            log.setEsito('E');
            logger.error("Errore non recuperabile di conservazione", e);
        }
		
		return log;
	}

	private ConservazioneResult execute(Job job) throws Exception {
		ConservazioneResult result;
		switch (job.getAzione()) {
		case 'C':
			logger.info("Esecuzione del versamento");
			result = conservazione.versamento(
						tokenConservazione, job.getDocId(), 
						job.getXmlDocumento(), job.getXmlAllegati(), 
						job.getFiles(), job.getTipoConservazione(), 
						job.getTipoDocumento(), job.getAppChiamante(), 
						job.getForzaCollegamento(), job.getForzaAccettazione(), 
						job.getForzaConservazione(), job.getCodEnte(), job.getCodAoo());
			break;
		case 'M':
			logger.info("Esecuzione della modifica dei metadati");
			result = conservazione.modificaMetadati(tokenConservazione, 
						job.getDocId(), job.getXmlDocumento(), 
						job.getTipoConservazione(), job.getTipoDocumento(), 
						job.getAppChiamante(), job.getCodEnte(), job.getCodAoo());
			break;
		case 'A':
			logger.info("Esecuzione dell'aggiunta di un documento");
			result = conservazione.aggiungiDocumento(tokenConservazione, 
					job.getDocId(), job.getXmlDocumento(), job.getXmlAllegati(), 
					job.getFiles(), job.getTipoConservazione(), 
					job.getTipoDocumento(), job.getAppChiamante(), 
					job.getForzaCollegamento(), job.getForzaAccettazione(), 
					job.getForzaConservazione(), job.getCodEnte(), job.getCodAoo());
			break;
		default:
			assert false : job.getAzione();
			result = null;
		}
				 
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {
		Scheduler sched = new Scheduler();
		sched.process();
	}
}
