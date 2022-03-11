package it.kdm.docer.fascicolazione.batch;

import java.sql.ResultSet;
import java.sql.Timestamp;

import it.kdm.docer.fascicolazione.batch.persistence.ConnectionManager;
import it.kdm.docer.fascicolazione.batch.persistence.Query;
import org.slf4j.Logger;

public abstract class Processor {

    static Logger log = org.slf4j.LoggerFactory.getLogger(Processor.class);

	private long loadExecutionId;
	private long updatedRows;

    public enum Status {
		BEGIN, END
	}

	protected abstract int[] doSingleExecution(ResultSet loadExecutionRs, String pianoClass) throws Exception;

	protected abstract Query getEntityDataQuery(ResultSet executionDataRs) throws Exception;

	public Processor(long loadExecutionId, long updatedRows) {
		this.loadExecutionId = loadExecutionId;
		this.updatedRows = updatedRows;

		Query executionQuery = null;
		Query entityDataQuery = null;
		try {
			executionQuery = ConnectionManager.INSTANCE
					.getQuery("select * from load_execution_data where load_execution_id = ?", true).setParameter(1, 0);
			ResultSet executionDataRs = executionQuery.execute().getResult();
			if (!executionDataRs.next()) {
				entityDataQuery = getEntityDataQuery(executionDataRs);
				ResultSet entityDataRs = entityDataQuery.execute().getResult();
				while (entityDataRs.next()) {
					executionDataRs.moveToInsertRow();
					executionDataRs.updateString("status", Processor.Status.BEGIN.name().substring(0, 1));
					executionDataRs.updateLong("entity_id", entityDataRs.getLong("id"));
					executionDataRs.updateLong("load_execution_id", loadExecutionId);
					executionDataRs.insertRow();
					executionDataRs.moveToCurrentRow();
				}

				ConnectionManager.INSTANCE.commitTransaction();
				log.info("Inserimento di load_execution_data effettuato.");
			}
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			if (entityDataQuery != null) {
				entityDataQuery.close();
			}
			if (executionQuery != null) {
				executionQuery.close();
			}
		}
	}

	public long getExecutionId() {
		return loadExecutionId;
	}

	public boolean hasRecordsToBeProcessed() {
		Query entityDataQuery = null;
		try {
			entityDataQuery = getEntityDataQuery(null);
			return entityDataQuery.execute().getResult().next();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} finally {
			if (entityDataQuery != null) {
				entityDataQuery.close();
			}
		}

	}

	public void execute() throws Exception {

		String pianoClass = Configuration.getInstance().getProperty("pianoClass");
		int titolariInseriti = 0;
		int fascicoliInseriti = 0;

		Query query = null;
		try {
			query = ConnectionManager.INSTANCE
					.getQuery("select * from load_execution_data where load_execution_id = ? and status <> 'E'", true)
					.setParameter(1, loadExecutionId);
			ResultSet loadExecutionDataRs = query.execute().getResult();
			while (loadExecutionDataRs.next()) {
				int[] res = doSingleExecution(loadExecutionDataRs, pianoClass);
				titolariInseriti += res[0];
				fascicoliInseriti += res[1];
				loadExecutionDataRs.updateString("status", Processor.Status.END.name().substring(0, 1));
				loadExecutionDataRs.updateRow();
				ConnectionManager.INSTANCE.commitTransaction();
			}

		} catch (Exception e) {
			throw e;
		} finally {
			log.info("Sono stati inseriti " + titolariInseriti + " titolari e " + fascicoliInseriti + " fascicoli.");
			updatedRows += titolariInseriti + fascicoliInseriti;
			if (query != null) {
				try {
					query.close();
				} catch (Exception e) {
				}
			}
		}

		try {
			query = ConnectionManager.INSTANCE.getQuery("select * from load_execution where id = ?", true)
					.setParameter(1, loadExecutionId);
			ResultSet loadExecutionRs = query.execute().getResult();
			if (loadExecutionRs.next()) {
				loadExecutionRs.updateLong("updated_rows", updatedRows);
				loadExecutionRs.updateTimestamp("end_date", new Timestamp(System.currentTimeMillis()));
				loadExecutionRs.updateString("status", Processor.Status.END.name().substring(0, 1));
				loadExecutionRs.updateRow();

                ConnectionManager.INSTANCE.commitTransaction();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (query != null) {
				try {
					query.close();
				} catch (Exception e) {
				}
			}
		}
	}

}
