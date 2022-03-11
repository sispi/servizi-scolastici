package it.kdm.docer.core.database;

import it.kdm.docer.core.tracer.Trace;
import it.kdm.docer.core.tracer.Tracer.TYPE;

import org.joda.time.DateTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class IDatabaseProvider {
	
	public abstract void openConnection() throws SQLException;
	public abstract void closeConnection() throws SQLException;
	public abstract boolean isConnected() throws SQLException;

	public abstract List<Map<String, Object>> readTracerLog(Collection<Long> docnum, String extradata, String user, Calendar from, Calendar to, String optype) throws SQLException, ParseException;
	public abstract void saveTracerLog(Trace traceItem, String level, TYPE type) throws SQLException;

	protected abstract void shutdown() throws SQLException;
	
	@Override
	protected final void finalize() throws Throwable {
		this.shutdown();
		super.finalize();
	}
}
