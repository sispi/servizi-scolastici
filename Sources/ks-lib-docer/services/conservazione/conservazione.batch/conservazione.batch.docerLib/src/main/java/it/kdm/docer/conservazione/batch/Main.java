package it.kdm.docer.conservazione.batch;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;


public class Main {
	
	private final static String sampleFile = "username=<username>\n" +
                                                "password=<password>\n" +
                                                "codiceEnte=<library>\n\n" +
                                                "timeout=<timeout in seconds>\n\n" +
                                                "# An epr is the url to the webservice\n" +
                                                "docer.epr=<epr>\n" +
                                                "conservazione.epr=<epr>\n" +
                                                "demone.epr=<epr>";
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {

		CommandLineParser parser = new PosixParser();
		
		Options options = new Options();
		
		options.addOption("u", true, "username");
		options.addOption("p", true, "password");
		options.addOption("ce", true, "codiceEnte");
		
		options.addOption("ed", "erpDocer", true, "Epr di DocER");
		options.addOption("ec", "erpConservazione", true, "Epr di WSConservazione");
		options.addOption("ede", "erpDemone", true, "Epr di WSDemone");
        options.addOption("t", "timeout", true, "Timeout in seconds");
        
        options.addOption("T", "test-mode", false, "Reports found documents without sending them");
		
		options.addOption(OptionBuilder.withLongOpt("properties")
							.withDescription("Preconfigured properties file").hasArg()
							.withArgName("FILE").create());
		
		options.addOption(OptionBuilder.withLongOpt("gen-properties")
							.withDescription("Prints a sample properties file").create());
		
		CommandLine cmd = parser.parse(options, args);
		HelpFormatter formatter = new HelpFormatter();
		
		if(cmd.hasOption("gen-properties")) {
			System.out.println(sampleFile);
		} else if(cmd.getArgs().length < 1 || (!cmd.hasOption("ed") && !cmd.hasOption("ec") && 
				!cmd.hasOption("u") && !cmd.hasOption("p") && !cmd.hasOption("ce") &&
				!cmd.hasOption("properties"))) {
			formatter.printHelp("docerLib", options);
		} else {
			File f = new File(cmd.getArgs()[0]);
			DocerLib lib = new DocerLib();
			String token;
			if(cmd.hasOption("properties")) {
				File propsFile = new File(cmd.getOptionValue("properties"));
				Properties props = new Properties();
				
				props.load(new FileInputStream(propsFile));
				
				String docerEpr = props.getProperty("docer.epr");
				lib.setEprDocer(docerEpr);
				
				String conservazioneEpr = props.getProperty("conservazione.epr");
				lib.setEprConservazione(conservazioneEpr);
				
				String demoneEpr = props.getProperty("demone.epr");
				lib.setEprDemone(demoneEpr);
                                
                                if(props.getProperty("timeout") != null) {
                                    long timeout = Long.parseLong(props.getProperty("timeout"));
                                    lib.setTimeout(timeout, TimeUnit.SECONDS);
                                }
				
				String username = props.getProperty("username");
				String password = props.getProperty("password");
				String library = props.getProperty("codiceEnte");

				token = lib.login(username, password, library);
				
			} else {
				lib.setEprDocer(cmd.getOptionValue("ed"));
				lib.setEprConservazione(cmd.getOptionValue("ec"));
                                if(cmd.hasOption("t")) {
                                    long timeout = Long.parseLong(cmd.getOptionValue("t"));
                                    lib.setTimeout(timeout, TimeUnit.SECONDS);
                                }
				token = lib.login(cmd.getOptionValue("u"), cmd.getOptionValue("p"), 
								cmd.getOptionValue("ce"));
			}
			
			lib.preparaConservazione(token, f.toURI(), cmd.hasOption("test-mode"));
		}
		
	}
}
