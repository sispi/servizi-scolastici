package it.kdm.docer.fonte.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.joda.time.DateTime;

public class BatchLog {

    private boolean started = false;
    private File log_file = null;
    private FileWriter log_fw = null;
    private BufferedWriter log_bw = null;
    private PrintWriter log_printwriter = null;

    public BatchLog(File parentDir, String logName) {

        DateTime now = new DateTime();
        log_file = new File(parentDir, logName + "_" + now.getMillis() + ".log");

        try {
            log_file.createNewFile();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        try {
            log_fw = new FileWriter(log_file.getAbsoluteFile());
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        log_bw = new BufferedWriter(log_fw);
        log_printwriter = new PrintWriter(log_bw);

        started = true;
    }

    public File getLog() {
        return log_file;
    }

    public void info(String message) {
        if (started) {
            DateTime d = new DateTime();
            String now = d.toString("HH:mm:ss");
            log_printwriter.println(now + " INFO - " + message);
            log_printwriter.flush();
        }
    }

    public void error(String message) {
        if (started) {
            DateTime d = new DateTime();
            String now = d.toString("HH:mm:ss");
            log_printwriter.println(now + " ERROR - " + message);
            log_printwriter.flush();
        }
    }   
    
    public void Close() {
        try {
            log_printwriter.close();
        }
        catch (Exception e1) {

        }

        log_fw = null;
        log_bw = null;
        log_printwriter = null;
        started = false;
    }

    public void deleteLogFile() {

        try {
            log_file.delete();
        }
        catch (Exception e) {

        }
    }

}
