package it.kdm.docer.fonte.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.slf4j.Logger;

public class Log4jUtils {

    @SuppressWarnings("unchecked")
    static public File getLogFile(Logger rootLogger, String name) {
        
        Collection<Logger> allLoggers = new ArrayList<Logger>();

        allLoggers.add(rootLogger);

        for (Enumeration<Logger> loggers =

        rootorg.slf4j.LoggerFactory.getLoggerRepository().getCurrentLoggers();

        loggers.hasMoreElements();) {

            allLoggers.add(loggers.nextElement());

        }

        Set<FileAppender> fileAppenders =

        new LinkedHashSet<FileAppender>();

        for (Logger logger : allLoggers) {

            for (Enumeration<Appender> appenders =

            logger.getAllAppenders();

            appenders.hasMoreElements();) {

                Appender appender = appenders.nextElement();

                if (appender instanceof FileAppender) {

                    fileAppenders.add((FileAppender)appender);

                }

            }

        }


        new LinkedHashMap<String, String>();

        for (FileAppender appender : fileAppenders) {

            if (appender.getName().equalsIgnoreCase(name)) {
                return new File(appender.getFile());
            }

        }
        
        return null;
    }
        
    
}
