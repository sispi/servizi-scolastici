package conservazione.demone.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.kdm.docer.conservazione.batch.Azione;
import it.kdm.docer.parer.daemon.database.Job;
import it.kdm.docer.parer.daemon.database.JobManager;
import it.kdm.docer.parer.daemon.database.Log;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.utils.DataRow;
import it.kdm.utils.DataTable;
import it.kdm.utils.json.StringDataTableDeserializer;
import it.kdm.utils.json.StringDataTableSerializer;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WSDemone {

	private static JobManager manager;
	private Gson gson;

	static {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
		manager = ctx.getBean(JobManager.class);
	}

	public WSDemone() throws IOException, ClassNotFoundException {
		//TODO: Max results?
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DataTable.class, new StringDataTableDeserializer());
		gsonBuilder.registerTypeAdapter(DataTable.class, new StringDataTableSerializer());
		gsonBuilder.create();

		this.gson = gsonBuilder.create();
	}

	public boolean addJob(String web_service, String docId,
						  String documentoPrincipale, String allegati, String files,
						  String tipoConservazione, String tipoDocumento,
						  String applicazioneChiamante, boolean forzaCollegamento,
						  boolean forzaAccettazione, boolean forzaConservazione,
						  Calendar dataRegistrazione, String chiave_doc, String codEnte,
						  String codAoo, char azione)
			throws Exception {

		try {
			// se WEB_SERVICE=VSYNC controllo c'è gia un idDoc presente nella
			// coda dei job, poi
			// caso 0.se idDoc=null ==> inserisco il JOB
			// caso 1.se idDoc=not null && WEB_SERVICE=VSYNC && Stato in (E,X,A)
			// ==> vado in update sui parametri
			// caso 2.se idDoc=not null && WEB_SERVICE=VSYNC && Stato=WA ==>
			// errore perchè job in esecuzione
			// caso 3.se idDoc=not null && WEB_SERVICE=VSYNC && Stato=C ==>
			// errore perchè concluso con successo

			Job job = manager.findJobByDoc(docId, azione);

			if (job == null || azione != Azione.CONSERVAZIONE.toChar()) {
				job = new Job();
				job.setWebService(web_service);
				job.setDocId(docId);
				job.setXmlDocumento(documentoPrincipale);
				job.setXmlAllegati(allegati);
				job.setFiles(files);
				job.setTipoConservazione(tipoConservazione);
				job.setTipoDocumento(tipoDocumento);
				job.setAppChiamante(applicazioneChiamante);
				job.setForzaCollegamento(forzaCollegamento);
				job.setForzaAccettazione(forzaAccettazione);
				job.setForzaConservazione(forzaConservazione);
				job.setDataRegistrazione(dataRegistrazione.getTime());

				job.setAzione(azione);
				job.setStato('A');
				job.setDataInserimento(Calendar.getInstance());

				job.setCodEnte(codEnte);
				job.setCodAoo(codAoo);
				job.setChiaveDoc(chiave_doc);

				manager.saveJob(job);

				return true;

			} else {
				char stato = job.getStato();

				if (stato == 'C') {
					return false;
				}

				if (stato == 'E') {
					job.setStato('A');
				}

				job.setXmlDocumento(documentoPrincipale);
				job.setXmlAllegati(allegati);
				job.setFiles(files);
				job.setForzaCollegamento(forzaCollegamento);
				job.setForzaAccettazione(forzaAccettazione);
				job.setForzaConservazione(forzaConservazione);
				job.setDataRegistrazione(dataRegistrazione.getTime());

				job.setDataUltimaModifica(Calendar.getInstance());
				job.setChiaveDoc(chiave_doc);
				job.setCodEnte(codEnte);
				job.setCodAoo(codAoo);

				manager.updateJob(job);

				return false;
			}

		} catch (ConstraintViolationException ex) {
			for(ConstraintViolation<?> cv : ex.getConstraintViolations()) {
				System.out.println(cv.getMessage());
			}
			throw ex;
		}
	}

	public String searchJob(Calendar dataInizio, Calendar dataFine, Character esito,
							String docId, String docType, String errCode,
							String ente, String aoo, int maxRows)
			throws Exception {
		//TODO: Implement ente, aoo and maxRows
		List<Log> jobs = manager.search(dataInizio, dataFine, esito,
				docId, null, docType, errCode, ente, aoo, maxRows);

		OMElement xmlResults = resultSetToXML(jobs);
		StringWriter writer = new StringWriter();
		xmlResults.serialize(writer);
		return writer.toString();
		//return jobs.toArray(new Log[0]);
	}

	public boolean updateJob(long id_job, Character stato,
							 boolean forzaCollegamento, boolean forzaAccettazione,
							 boolean forzaConservazione) throws Exception {

		// Lancia un eccezione se il job non esiste
		Job job = manager.findJob(id_job);

		job.setStato(stato);
		job.setForzaCollegamento(forzaCollegamento);
		job.setForzaAccettazione(forzaAccettazione);
		job.setForzaConservazione(forzaConservazione);

		job.setDataUltimaModifica(Calendar.getInstance());

		manager.updateJob(job);

		return true;

	}

	public Job[] searchJobEstesa(KeyValuePair[] searchCriteria, Integer maxRows) {
		if(maxRows==null){
			maxRows = 1000;
		}

		if (searchCriteria == null) {
			return new Job[0];
		} else {
			String docId = null, descrizione = null;
			String docType = null, errCode = null, ente = null, aoo = null;

			Character esito = null;

			Calendar dataInizio = null, dataFine = null;

			DateTimeFormatter format = ISODateTimeFormat.dateTime();
			for (KeyValuePair criteria : searchCriteria) {
				if (criteria.getKey().equals("DATA_INIZIO") && StringUtils.isNotEmpty(criteria.getValue())) {
					dataInizio = format.parseDateTime(criteria.getValue()).toGregorianCalendar();
				} else if (criteria.getKey().equals("DATA_FINE") && StringUtils.isNotEmpty(criteria.getValue())) {
					dataFine = format.parseDateTime(criteria.getValue()).toGregorianCalendar();
				} else if (criteria.getKey().equals("ESITO") && StringUtils.isNotEmpty(criteria.getValue())) {
					esito = criteria.getValue().charAt(0);
				} else if (criteria.getKey().equals("DOC_NUM")) {
					docId = criteria.getValue();
				} else if (criteria.getKey().equals("DESCRIZIONE")) {
					descrizione = criteria.getValue();
				} else if (criteria.getKey().equals("DOC_TYPE")) {
					docType = criteria.getValue();
				} else if (criteria.getKey().equals("ERR_CODE")) {
					errCode = criteria.getValue();
				} else if (criteria.getKey().equals("COD_ENTE")) {
					ente = criteria.getValue();
				} else if (criteria.getKey().equals("COD_AOO")) {
					aoo = criteria.getValue();
				}
			}

			List<Job> results = manager.searchJobs(dataInizio, dataFine, esito, docId,
					descrizione, docType, errCode, ente, aoo, maxRows);

			return results.toArray(new Job[results.size()]);
		}
	}

	public Log[] searchLogEstesa(KeyValuePair[] searchCriteria, Integer maxRows) throws Exception {

		if (maxRows == null) {
			maxRows = 1000;
		}

		if (searchCriteria == null) {
			return new Log[0];
		} else {
			String docId = null, descrizione = null;
			String docType = null, errCode = null, ente = null, aoo = null;

			Character esito = null;

			Calendar dataInizio = null, dataFine = null;

			DateTimeFormatter format = ISODateTimeFormat.dateTime();
			for (KeyValuePair criteria : searchCriteria) {
				if (criteria.getKey().equals("DATA_INIZIO") && StringUtils.isNotEmpty(criteria.getValue())) {
					dataInizio = format.parseDateTime(criteria.getValue()).toGregorianCalendar();
				} else if (criteria.getKey().equals("DATA_FINE") && StringUtils.isNotEmpty(criteria.getValue())) {
					dataFine = format.parseDateTime(criteria.getValue()).toGregorianCalendar();
				} else if (criteria.getKey().equals("ESITO") && StringUtils.isNotEmpty(criteria.getValue())) {
					esito = criteria.getValue().charAt(0);
				} else if (criteria.getKey().equals("DOC_NUM")) {
					docId = criteria.getValue();
				} else if (criteria.getKey().equals("DESCRIZIONE")) {
					descrizione = criteria.getValue();
				} else if (criteria.getKey().equals("DOC_TYPE")) {
					docType = criteria.getValue();
				} else if (criteria.getKey().equals("ERR_CODE")) {
					errCode = criteria.getValue();
				} else if (criteria.getKey().equals("COD_ENTE")) {
					ente = criteria.getValue();
				} else if (criteria.getKey().equals("COD_AOO")) {
					aoo = criteria.getValue();
				}
			}

			List<Log> results = manager.search(dataInizio, dataFine, esito, docId,
					descrizione, docType, errCode, ente, aoo, maxRows);

			return results.toArray(new Log[results.size()]);
		}
	}

	public Log[] readLogsByIdDoc(String id_doc, char azione) throws Exception {
		List<Log> logs = manager.findLogByDoc(id_doc, azione);
		return logs.toArray(new Log[0]);
	}

	public String getJobGson(String id_job) throws Exception {
		Job job = manager.findJob(Long.parseLong(id_job));

		DataTable<String> dtResults = initDataTableJob();

		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

		if (job!= null) {
			DataRow<String> dr = dtResults.newRow();

//			for (String col : dtResults.getColumnNames()) {
//				String fieldName = "get"+StringUtils.capitalize(col.replace("_"," ")).replace(" ","");
//				String val = String.valueOf(job.getClass().getMethod(fieldName).invoke(job));
//				//String val = job.getClass().getField(col).toGenericString();
//				dr.put(col, val);
//			}

			dr.put("FORZA_ACCETTAZIONE", job.getForzaAccettazione().toString());
			dr.put("CHIAVE_DOC", job.getChiaveDoc().toString());
			dr.put("DT_CHIAMATA", job.getDataChiamata()==null?"":formatter.format(job.getDataChiamata().getTime()));
			dr.put("WEB_SERVICE", job.getWebService().toString());
			dr.put("ERRMESSAGE", job.getErrorMessage()==null?"":job.getErrorMessage().toString());
			dr.put("STATO", String.valueOf(job.getStato()));
			dr.put("DATA_REGISTRAZIONE", job.getDataRegistrazione()==null?"":formatter.format(job.getDataRegistrazione().getTime()));
			dr.put("FORZA_CONSERVAZIONE", job.getForzaConservazione().toString());
			dr.put("ERRCODE", job.getErrorCode()==null?"":job.getErrorCode().toString());
			dr.put("TIPO_DOC", job.getTipoDocumento().toString());
			dr.put("TIPO_CONSERVAZIONE", job.getTipoConservazione().toString());
			dr.put("APP_CHIAMANTE", job.getAppChiamante().toString());
			dr.put("FORZA_COLLEGAMENTO", job.getForzaCollegamento().toString());
			dr.put("COD_ENTE", job.getCodEnte().toString());
			dr.put("DT_ULT_MOD", job.getDataUltimaModifica()==null? "" : formatter.format(job.getDataUltimaModifica().getTime()));
			dr.put("docId", job.getDocId().toString());
			dr.put("COD_AOO", job.getCodAoo().toString());
			dr.put("XML_PROFILO_DOC_PRINCIPALE", job.getXmlDocumento()==null?"":job.getXmlDocumento().toString());
			dr.put("XML_PROFILO_DOC_ALLEGATI", job.getXmlAllegati()==null?"":job.getXmlAllegati().toString());
			dr.put("ID_JOB", job.getId().toString());
			dr.put("DT_INS", job.getDataInserimento()==null?"":formatter.format(job.getDataInserimento().getTime()));
			dtResults.addRow(dr);
		}

		return this.gson.toJson(dtResults);
	}

	public Job getJob(long id_job) throws Exception {
		return manager.findJob(id_job);
	}

	public Job getJobByDocId(String id_doc, char azione) throws Exception {
		return manager.findJobByDoc(id_doc, azione);
	}

	public String readLogGson_byIdJob(String id_job) throws Exception {
		List<Log> logs = manager.findLogByJob(Long.parseLong(id_job));
		DataTable<String> dtResults = initDataTableLog();

		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

		if (logs!= null && logs.size()!=0) {
			for (Log log:logs) {
				DataRow<String> dr = dtResults.newRow();

				dr.put("ID_LOG", log.getId().toString());

				dr.put("ID_JOB", log.getJob().getId().toString());
				dr.put("CHIAVE_DOC", log.getJob().getChiaveDoc().toString());
				dr.put("docId", log.getJob().getDocId().toString());
				dr.put("ESITO", String.valueOf(log.getEsito()));
				dr.put("DT_INS", log.getJob().getDataInserimento()==null?"":formatter.format(log.getJob().getDataInserimento().getTime()));
				dr.put("DT_CHIAMATA", log.getDataChiamata() == null ? "" : formatter.format(log.getDataChiamata().getTime()));
				dr.put("DT_RISPOSTA", log.getDataRisposta()==null?"":formatter.format(log.getDataRisposta().getTime()));
				dr.put("ERRCODE", log.getErrorCode()==null?"":log.getErrorCode().toString());
				dr.put("MESSAGE", log.getMessage()==null?"":log.getMessage().toString());
				dr.put("XML_PROFILO_DOC_PRINCIPALE", log.getJob().getXmlDocumento()==null?"":log.getJob().getXmlDocumento().toString());
				dr.put("XML_PROFILO_DOC_ALLEGATI", log.getJob().getXmlAllegati()==null?"":log.getJob().getXmlAllegati().toString());
				dr.put("LISTA_URI", "");
				dr.put("TIPO_DOC", log.getJob().getTipoDocumento()==null?"":log.getJob().getTipoDocumento().toString());
				dr.put("APP_CHIAMANTE", log.getJob().getAppChiamante()==null?"":log.getJob().getAppChiamante().toString());
				dr.put("TIPO_CONSERVAZIONE", log.getJob().getTipoConservazione()==null?"":log.getJob().getTipoConservazione().toString());
				dr.put("FORZA_COLLEGAMENTO", log.getForzaCollegamento()==null?"":log.getForzaCollegamento().toString());
				dr.put("FORZA_ACCETTAZIONE", log.getForzaAccettazione()==null?"":log.getForzaAccettazione().toString());
				dr.put("FORZA_CONSERVAZIONE", log.getForzaConservazione()==null?"":log.getForzaConservazione().toString());
				dr.put("DATA_REGISTRAZIONE", log.getDataRegistrazione()==null?"":formatter.format(log.getDataRegistrazione().getTime()));
				dr.put("WEB_SERVICE", log.getJob().getWebService()==null?"":log.getJob().getWebService().toString());
				dr.put("XML_RICHIESTA", log.getXmlRichiesta()==null?"":log.getXmlRichiesta().toString());
				dr.put("XML_ESITO", log.getXmlEsito()==null?"":log.getXmlEsito().toString());

				dtResults.addRow(dr);
			}
		}

		return this.gson.toJson(dtResults);
	}

	public Log[] readLogsByIdJob(long id_job) throws Exception {
		List<Log> logs = manager.findLogByJob(id_job);
		return logs.toArray(new Log[0]);
	}

	public boolean deleteJob_byIdJob(long id_job) throws Exception {

		manager.removeJob(id_job);

		return true;

	}

	public boolean deleteJob_ByIdDoc(String id_doc, char azione) throws Exception {

		Job job = manager.findJobByDoc(id_doc, azione);
		manager.removeJob(job.getId());

		return true;
	}

	private DataTable<String> initDataTableLog () {
		DataTable<String> dtResults = new DataTable<String>();

		dtResults.addColumn("ID_LOG");
		dtResults.addColumn("ID_JOB");
		dtResults.addColumn("CHIAVE_DOC");
		dtResults.addColumn("docId");
		dtResults.addColumn("ESITO");
		dtResults.addColumn("DT_INS");
		dtResults.addColumn("DT_CHIAMATA");
		dtResults.addColumn("DT_RISPOSTA");
		dtResults.addColumn("ERRCODE");
		dtResults.addColumn("MESSAGE");
		dtResults.addColumn("XML_PROFILO_DOC_PRINCIPALE");
		dtResults.addColumn("XML_PROFILO_DOC_ALLEGATI");
		dtResults.addColumn("LISTA_URI");
		dtResults.addColumn("TIPO_DOC");
		dtResults.addColumn("APP_CHIAMANTE");
		dtResults.addColumn("TIPO_CONSERVAZIONE");
		dtResults.addColumn("FORZA_COLLEGAMENTO");
		dtResults.addColumn("FORZA_ACCETTAZIONE");
		dtResults.addColumn("FORZA_CONSERVAZIONE");
		dtResults.addColumn("DATA_REGISTRAZIONE");
		dtResults.addColumn("WEB_SERVICE");
		dtResults.addColumn("XML_RICHIESTA");
		dtResults.addColumn("XML_ESITO");

		return dtResults;
	}

	private DataTable<String> initDataTableJob() {
		DataTable<String> dtResults = new DataTable<String>();

		dtResults.addColumn("ID_JOB");
		dtResults.addColumn("CHIAVE_DOC");
		dtResults.addColumn("STATO");
		dtResults.addColumn("COD_ENTE");
		dtResults.addColumn("COD_AOO");
		dtResults.addColumn("DT_INS");
		dtResults.addColumn("DT_ULT_MOD");
		dtResults.addColumn("DT_CHIAMATA");
		dtResults.addColumn("WEB_SERVICE");
		dtResults.addColumn("docId");
		dtResults.addColumn("XML_PROFILO_DOC_PRINCIPALE");
		dtResults.addColumn("XML_PROFILO_DOC_ALLEGATI");
		dtResults.addColumn("LISTA_URI");
		dtResults.addColumn("TIPO_DOC");
		dtResults.addColumn("APP_CHIAMANTE");
		dtResults.addColumn("TIPO_CONSERVAZIONE");
		dtResults.addColumn("FORZA_COLLEGAMENTO");
		dtResults.addColumn("FORZA_ACCETTAZIONE");
		dtResults.addColumn("FORZA_CONSERVAZIONE");
		dtResults.addColumn("DATA_REGISTRAZIONE");
		dtResults.addColumn("ERRCODE");
		dtResults.addColumn("ERRMESSAGE");

		return dtResults;
	}

	private OMElement resultSetToXML(List<Log> results)
			throws SQLException {

		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMElement root = fac.createOMElement("resultSet", null);

		OMElement col = null;

		String dateFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

		for(Log tmp:results) {
			OMElement row = fac.createOMElement("row", null);

			col = fac.createOMElement("FORZA_ACCETTAZIONE", null);
			col.setText(tmp.getJob().getForzaAccettazione().toString());
			row.addChild(col);

			col = fac.createOMElement("CHIAVE_DOC", null);
			col.setText(tmp.getJob().getChiaveDoc().toString());
			row.addChild(col);

			col = fac.createOMElement("DT_CHIAMATA", null);
			col.setText(formatter.format(tmp.getDataChiamata()==null?"":tmp.getDataChiamata().getTime()));
			row.addChild(col);

			col = fac.createOMElement("WEB_SERVICE", null);
			col.setText(tmp.getJob().getWebService()==null?"":tmp.getJob().getWebService().toString());
			row.addChild(col);

			col = fac.createOMElement("ERRMESSAGE", null);
			col.setText(tmp.getJob().getErrorMessage()==null?"":tmp.getJob().getErrorMessage().toString());
			row.addChild(col);

			col = fac.createOMElement("STATO", null);
			col.setText(String.valueOf(tmp.getJob().getStato()));
			row.addChild(col);

			col = fac.createOMElement("DATA_REGISTRAZIONE", null);
			col.setText(formatter.format(tmp.getJob().getDataRegistrazione()==null?"":tmp.getJob().getDataRegistrazione().getTime()));
			row.addChild(col);

			col = fac.createOMElement("FORZA_CONSERVAZIONE", null);
			col.setText(tmp.getJob().getForzaConservazione().toString());
			row.addChild(col);

			col = fac.createOMElement("ERRCODE", null);
			col.setText(tmp.getJob().getErrorCode()==null?"":tmp.getJob().getErrorCode().toString());
			row.addChild(col);

			col = fac.createOMElement("TIPO_DOC", null);
			col.setText(tmp.getJob().getTipoDocumento().toString());
			row.addChild(col);

			col = fac.createOMElement("TIPO_CONSERVAZIONE", null);
			col.setText(tmp.getJob().getTipoConservazione().toString());
			row.addChild(col);

			col = fac.createOMElement("APP_CHIAMANTE", null);
			col.setText(tmp.getJob().getAppChiamante()==null?"":tmp.getJob().getAppChiamante().toString());
			row.addChild(col);

			col = fac.createOMElement("FORZA_COLLEGAMENTO", null);
			col.setText(tmp.getJob().getForzaCollegamento().toString());
			row.addChild(col);

			col = fac.createOMElement("COD_ENTE", null);
			col.setText(tmp.getJob().getCodEnte().toString());
			row.addChild(col);

			col = fac.createOMElement("DT_ULT_MOD", null);
			col.setText(tmp.getJob().getDataUltimaModifica()==null?"":formatter.format(tmp.getJob().getDataUltimaModifica().getTime()));
			row.addChild(col);

			col = fac.createOMElement("DOCID", null);
			col.setText(tmp.getJob().getDocId().toString());
			row.addChild(col);

			col = fac.createOMElement("COD_AOO", null);
			col.setText(tmp.getJob().getCodAoo().toString());
			row.addChild(col);

			col = fac.createOMElement("XML_PROFILO_DOC_PRINCIPALE", null);
			col.setText(tmp.getJob().getXmlDocumento().toString());
			row.addChild(col);

			col = fac.createOMElement("XML_PROFILO_DOC_ALLEGATI", null);
			col.setText(tmp.getJob().getXmlAllegati().toString());
			row.addChild(col);

			col = fac.createOMElement("ID_JOB", null);
			col.setText(tmp.getJob().getId().toString());
			row.addChild(col);

			col = fac.createOMElement("DT_INS", null);
			col.setText(tmp.getJob().getDataInserimento()==null?"":formatter.format(tmp.getJob().getDataInserimento().getTime()));
			row.addChild(col);

//			col = fac.createOMElement("LISTA_URI", null);
//			col.setText(tmp.get.toString());
//			row.addChild(col);

			root.addChild(row);
		}



		root.addAttribute("results", Integer.toString(results.size()), null);
		root.addAttribute("count", Integer.toString(results.size()), null);

		return root;
	}

}
