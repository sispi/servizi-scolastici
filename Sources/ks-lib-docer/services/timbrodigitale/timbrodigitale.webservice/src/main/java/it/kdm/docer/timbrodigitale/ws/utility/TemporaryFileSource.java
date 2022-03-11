package it.kdm.docer.timbrodigitale.ws.utility;
import java.io.File;

import javax.activation.FileDataSource;

//import org.slf4j.Logger;

public class TemporaryFileSource extends FileDataSource {

    	private File file;
//    	Logger log = org.slf4j.LoggerFactory.getLogger(TemporaryFileSource.class);
    	
		public TemporaryFileSource(File file) {
			super(file);
			this.file = file;
//			log.debug("new TemporaryFileSource() called with file: " + file.getAbsolutePath());
		}

		@Override
		protected void finalize() throws Throwable {
//			log.debug("TemporaryFileSource.finalize() Called");
//			log.info("Deleting temporary file " + file.getName());
			if(!this.file.delete()) {
//				log.debug("Failed deletition of " + file.getName());
//				log.debug("Flagging " + file.getName() + " for deletion on exit");
				this.file.deleteOnExit();
			}
			super.finalize();
		}
		
    }
