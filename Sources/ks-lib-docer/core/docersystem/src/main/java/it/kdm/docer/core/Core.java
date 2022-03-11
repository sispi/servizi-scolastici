package it.kdm.docer.core;

import it.kdm.docer.core.database.IDatabaseProvider;
import it.kdm.docer.core.tracer.bean.Configuration;

public final class Core {
	
	private static Core instance = null;
	private IDatabaseProvider databaseProvider;
	
	private Core() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// Leggi configurazione
		Class<?> klass = Class.forName("it.kdm.docer.core.database.systemDBProvider");
		//Class<?> klass = Class.forName("it.kdm.docer.core.database.JavaDBProvider");
		this.databaseProvider = (IDatabaseProvider)klass.newInstance();
	}
	
	public static Core getInstance() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(instance == null) {
			instance = new Core();
		}
		
		return instance;
	}
	
	public IDatabaseProvider getDatabaseProvider() {
		return this.databaseProvider;
	}



}
