package it.kdm.docer.fascicolazione.batch;

import java.sql.ResultSet;

import it.kdm.docer.fascicolazione.batch.persistence.ConnectionManager;
import it.kdm.docer.fascicolazione.batch.persistence.Query;
import it.kdm.docer.fascicolazione.batch.persistence.ResultSetMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

public class FascicoloProcessor extends Processor {

	private static Logger log = org.slf4j.LoggerFactory.getLogger(FascicoloProcessor.class);

	public FascicoloProcessor(long loadExecutionId, long updatedRows) {
		super(loadExecutionId, updatedRows);
	}

	/*
	static {
		try {
			ConnectionManager.INSTANCE.getQuery("create table if not exists load_fascicolo ("
					+ "id bigint not null auto_increment, cod_ente varchar(255), "
					+ "cod_aoo varchar(255), classifica varchar(255), "
					+ "parent_progr_fascicolo varchar(255), progr_fascicolo varchar(255), "
					+ "anno_fascicolo varchar(255), des_fascicolo varchar(255), "
					+ "enabled varchar(255), des_titolario varchar(255), primary key(id))").execute().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	@Override
	protected Query getEntityDataQuery(ResultSet executionDataRs) throws Exception {
		// Prelevo i record in ordine di id, per cui il db deve essere caricato con i record nel giusto ordine
		//	senza titolari/fascicoli figli che precedono titolari/fascicoli padri
		return ConnectionManager.INSTANCE
				.getQuery("select * from load_fascicolo where id not in (select entity_id from load_execution_data)" +
						//" order by cod_ente, cod_aoo, anno_fascicolo, CAST(progr_fascicolo AS UNSIGNED)");
						" order by id");
	}

	/**
	 * Inserisce un fascicolo e, nel caso non esista, anche il titolario
	 * @param loadExecutionRs
	 * @return
	 * @throws Exception
	 */
	@Override
	protected int[] doSingleExecution(ResultSet loadExecutionRs, String pianoClass) throws Exception {
		Query query = null;
		int[] counter = new int[]{0,0};
		try {
			query = ConnectionManager.INSTANCE.getQuery("select * from load_fascicolo where id = ?").setParameter(1,
					loadExecutionRs.getLong("entity_id"));
			ResultSet rs = query.execute().getResult();
			rs.next();

			// Se des_titolario non è vuoto devo inserire il titolario
			String desTitolario = rs.getString("des_titolario");
			if (desTitolario != null && !desTitolario.trim().equals("")) {
				ResultSetMap rsmTit = new ResultSetMap(rs, "cod_ente", "cod_aoo", "parent_classifica", "classifica", "des_titolario", "enabled");

				String titId = buildId(rsmTit, "titolario");
				if (StringUtils.isEmpty(pianoClass))
					pianoClass = "";

				rsmTit.put("piano_class", pianoClass);

				// Controllo se esiste già il titolario
				if (!BLFacade.existsTitolario(rsmTit)) {
					BLFacade.createTitolario(rsmTit);
					log.info("Titolario " + titId + " inserito con successo.");
					BLFacade.setACLTitolario(rsmTit, rs.getString("acls"));
					log.info("ACL Titolario " + titId + " settate con successo.");
					// Contatore titolari
					counter[0]++;
				} else {
					String logString = "Titolario " + titId + " gia' esistente";
					logString += (StringUtils.isEmpty(pianoClass) ? "." : " per il piano di classificazione " + pianoClass);
					log.info(logString);
				}
			}

			// Se anno_fascicolo non è vuoto inserisco il fascicolo
			String annoFascicolo = rs.getString("anno_fascicolo");
			if (annoFascicolo != null && !annoFascicolo.trim().equals("")) {
				String[] fieldsToAvoid = {"id", "des_titolario", "parent_classifica"};
				ResultSetMap rsmFasc = new ResultSetMap(rs, fieldsToAvoid, true);

				// Controllo se esiste già il fascicolo
				String fascId = buildId(rsmFasc, "fascicolo");
				if (!BLFacade.existsFascicolo(rsmFasc)) {
					BLFacade.createFascicolo(rsmFasc);
					log.info("Fascicolo " + fascId + " inserito con successo.");
					BLFacade.setACLFascicolo(rsmFasc, rs.getString("acls"));
					log.info("ACL Fascicolo " + fascId + " settate con successo.");
					// Contatore fascicoli
					counter[1]++;
				} else {
					log.info("Fascicolo " + fascId + " gia' esistente.");
				}
			}

			return counter;

		} catch (Exception e) {
			throw e;
		} finally {
			if (query != null) {
				try {
					query.close();
				} catch (Exception e) {
                    log.error(e.getMessage(), e);
				}
			}
		}
	}

	private String buildId(ResultSetMap mapEntity, String type){
		StringBuilder entityId = new StringBuilder("");
		entityId.append(mapEntity.get("cod_ente")).append(" ");
		entityId.append(mapEntity.get("cod_aoo")).append(" ");
		entityId.append(mapEntity.get("classifica")).append(" ");

		if ("fascicolo".equals(type)) {
			entityId.append(mapEntity.get("anno_fascicolo")).append(" ");
			entityId.append(mapEntity.get("progr_fascicolo")).append(" ");
		}

		return entityId.toString();
	}

}
