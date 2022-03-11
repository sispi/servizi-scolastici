package it.kdm.docer.fascicolazione.batch;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import it.kdm.docer.fascicolazione.batch.persistence.ConnectionManager;
import it.kdm.docer.fascicolazione.batch.persistence.Query;
import org.slf4j.Logger;

public class ProcessorManager {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(ProcessorManager.class);

	/*
	static {
		try {
			ConnectionManager.INSTANCE.getQuery("create table if not exists load_execution ("
					+ "id bigint not null auto_increment, status char(1), entity_type varchar(255), "
					+ "start_date timestamp, end_date timestamp, updated_rows bigint, primary key(id))").execute().close();

			ConnectionManager.INSTANCE.getQuery("create table if not exists load_execution_data ("
					+ "id bigint not null auto_increment, status char(1), load_execution_id bigint, "
					+ "entity_id bigint, primary key(id), "
					+ "foreign key (load_execution_id) references load_execution(id))").execute().close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}*/

	public ProcessorManager() {
	}

    public List<Processor> getProcessor(String name) {
		List<Processor> processors = new LinkedList<Processor>();
		String processorClassName = ProcessorManager.class.getPackage().getName() + "." + name + "Processor";
		Query query = null;
		try {
			query = ConnectionManager.INSTANCE.getQuery("select * from load_execution where entity_type = ?", true);
			ResultSet executionRs = query.setParameter(1, name).execute().getResult();
			while (executionRs.next()) {
				if (!executionRs.getString("status").equals(Processor.Status.END.name().substring(0, 1))) {
					addProcessor(processors, processorClassName, executionRs.getLong("id"), executionRs.getLong("updated_rows"));
				}
			}

			executionRs.moveToInsertRow();
			executionRs.updateTimestamp("start_date", new Timestamp(System.currentTimeMillis()));
			executionRs.updateString("status", Processor.Status.BEGIN.name().substring(0, 1));
			executionRs.updateString("entity_type", name);
			executionRs.insertRow();
			executionRs.last();

            ConnectionManager.INSTANCE.commitTransaction();
			log.info("Inserimento della nuova load_execution effettuato.");

			addProcessor(processors, processorClassName, executionRs.getLong("id"), executionRs.getLong("updated_rows"));

			return processors;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
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

	private void addProcessor(List<Processor> processors, String processorClassName, long processorId, long updatedRows)
			throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException,
			ClassNotFoundException {
		Processor processor = (Processor) Class.forName(processorClassName).getConstructor(long.class, long.class)
				.newInstance(processorId, updatedRows);
		processors.add(processor);
	}

}
