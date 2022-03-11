package it.kdm.docer.fascicolazione.batch;

import java.sql.SQLException;
import java.util.List;

import it.kdm.docer.fascicolazione.batch.persistence.ConnectionManager;

import org.slf4j.Logger;

public class Main {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {

		// Aggiungo un hook per gestire il blocco forzato dell'applicazione
    	//MyShutdown sh = new MyShutdown();
    	//Runtime.getRuntime().addShutdownHook(sh);

		if (args.length < 2) {
			log.error("Numero di argomenti errato. Deve essere java -cp <JAR> <MAIN-CLASS> <ENTITY> <PROPS-PATH>");
			System.exit(1);
		}

        String configPath = args[1];
		Configuration.init(configPath);

		try {
			// Se c'è il terzo parametro è il file xml da importare nel db
			if (args.length > 2) {
				XmlImportManager xmlManager = new XmlImportManager(args[2]);
				xmlManager.executeImport();

				// Se c'è il quarto parametro ed è T allora siamo in TEST mode
				if (args.length > 3) {
					if ("T".equals(args[3]))
						System.exit(0);
				}
			}

			ProcessorManager processorManager = new ProcessorManager();
			String processorName = args[0].substring(0, 1).toUpperCase() + args[0].substring(1);

			List<Processor> processors = processorManager.getProcessor(processorName);

			if (processors == null) {
                log.error("The processor '" + processorName + "' does not exists");
				System.exit(1);
			} else {
                log.info("Inizio elaborazione...");
				for (Processor processor : processors) {
					String processorId = processorName + "(id: " + processor.getExecutionId() + ")";
                    log.info("The processor '" + processorId + "' has been launched");
					try {
						processor.execute();
                        log.info("The processor '" + processorId + "' has been terminated successfully");
					} catch (Exception e) {
                        log.error("The processor '" + processorId + "' has been terminated with errors", e);
					}
				}
                log.info("Fine elaborazione.");
				System.exit(0);
			}
        } catch(Exception e) {
            log.error(e.getMessage(), e);
		} finally {
			try {
				ConnectionManager.INSTANCE.closeConnection();
			} catch (SQLException e) {
                log.error("Error closing db connection: " + e.getMessage(), e);
			}
		}

	}

}

// Classe che serve a gestire l'arresto forzato dell'applicazione
class MyShutdown extends Thread {
	
	 public void run() {
		 System.out.println("Processo terminato");
		 try {
			ConnectionManager.INSTANCE.closeConnection();
		} catch (SQLException e) {
			System.out.println("Error closing db connection: " + e.getMessage());
		}
	 }
}
