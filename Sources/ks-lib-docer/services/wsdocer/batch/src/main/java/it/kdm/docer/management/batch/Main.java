package it.kdm.docer.management.batch;

import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;
import it.kdm.docer.management.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.slf4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 28/10/15
 * Time: 14.24
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    private static final int DEFAULT_MAXROWS = 1000;
    private static Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);

	private final static String sampleFile = 	"username=<username>\n" +
            "password=<password>\n" +
            "codiceEnte=<library>\n\n" +
            "xml=<path xml operations>";

	
    public static void main(String[] args) {
        try {
            log.info("Inizio elaborazione...");

            CommandLineParser parser = new PosixParser();
    		
    		Options options = new Options();
    		options.addOption("u", true, "username");
    		options.addOption("p", true, "password");
    		options.addOption("ce", true, "Codice Ente");
    		options.addOption("rows", true, "max rows");
    		options.addOption("xml", true, "file path xml to process");
    		options.addOption("R", "run-mode", true, "Run mode"); // T = test, R = run, A = test & run
    		
    		options.addOption(OptionBuilder.withLongOpt("properties")
    							.withDescription("Preconfigured properties file").hasArg()
    							.withArgName("FILE").create());
    		
    		options.addOption(OptionBuilder.withLongOpt("gen-properties")
    							.withDescription("Prints a sample properties file").create());
    		
    		CommandLine cmd = parser.parse(options, args);
    		HelpFormatter formatter = new HelpFormatter();

    		if(cmd.hasOption("gen-properties")) {
    			System.out.println(sampleFile);
    		} else if (
    				!cmd.hasOption("u") && 
    				!cmd.hasOption("p") && 
    				!cmd.hasOption("ce") && 
    				!cmd.hasOption("xml") &&
    				!cmd.hasOption("rows") &&
    				!cmd.hasOption("run-mode") &&
    				!cmd.hasOption("properties") ) {
    			formatter.printHelp("batch Change Managment ", options);
    		} else {
    			XStream xStream = new XStream() {
    				@Override
    				protected MapperWrapper wrapMapper(MapperWrapper next) {
    					return new MapperWrapper(next) {
    						@Override
    						public boolean shouldSerializeMember(Class definedIn, String fieldName) {
    							if (definedIn == Object.class) {
    								return false;
    							}
    							return super.shouldSerializeMember(definedIn, fieldName);
    						}
    					};
    				}
    			};
    			
    			Class[] types = new Class [] {Batch.class, Rule.class, Groups.class, Group.class, Source.class, Target.class};
    			xStream.processAnnotations(types);

				String username;
				String password;
				String codiceEnte;
				RunMode runMode;
                String pianoClass = "";
				Batch batch;
                int searchMaxRows = DEFAULT_MAXROWS;
                String configPath;
				String schemaPath;

                String xml = cmd.getOptionValue("xml");

    			if (cmd.hasOption("properties")) {
    				File propsFile = new File(cmd.getOptionValue("properties"));
    				Properties props = new Properties();
    				props.load(new FileInputStream(propsFile));
    				
    				username = props.getProperty("username");
    				password = props.getProperty("password");
    				codiceEnte = props.getProperty("codiceEnte");
                    pianoClass = props.getProperty("pianoClass");
                    configPath = props.getProperty("batch.config.path");
					schemaPath = props.getProperty("batch.xsd.path");

                    try {
                        searchMaxRows = Integer.parseInt(props.getProperty("bl.provider.searchMaxRows"));
                    } catch(NumberFormatException ne) {
                        log.error("Il formato del parametro searchMaxRows e' errato.", ne);
                    }

                    runMode = RunMode.fromString( cmd.getOptionValue("run-mode") );

    			} else {
    				username = cmd.getOptionValue("u");
    				password = cmd.getOptionValue("p");
    				codiceEnte = cmd.getOptionValue("ce");
                    configPath = cmd.getOptionValue("cfg");
					schemaPath = cmd.getOptionValue("xsd");
    				 try {
                         searchMaxRows = Integer.parseInt( cmd.getOptionValue("rows"));
                     } catch(NumberFormatException ne) {
                         log.error("Il formato del parametro searchMaxRows e' errato.", ne);
                     }
    				runMode = RunMode.fromString( cmd.getOptionValue("run-mode") );
    			}
    			
    			log.info( "username: " + username );
                log.info( "password: " + "******" );
                log.info( "codiceEnte: " + codiceEnte );
                log.info( "runMode: " + runMode );
                log.info( "maxRows: " + searchMaxRows );
                log.info( "pianoClass: " + pianoClass );
                log.info( "config path: " + configPath );
				log.info( "xsd path: " + schemaPath);

				// Validazione del formato del file xml
				SchemaFactory scFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				File fileSchema = new File(schemaPath);
				Schema schema = scFactory.newSchema(fileSchema);
				Validator validator = schema.newValidator();
				validator.validate(new StreamSource(new File(xml)));

				xStream.registerConverter(new ToAttributedValueConverter(Group.class, xStream.getMapper(), xStream.getReflectionProvider(),
						xStream.getConverterLookup(), "value"));

    			batch = (Batch) xStream.fromXML( new File ( xml ));
                log.info( "batch: " + batch );

                ChangeManagment cm = new ChangeManagementImpl(username, password, codiceEnte, pianoClass, batch, searchMaxRows, runMode, configPath);
    			cm.execute();
                log.info("Fine elaborazione.");
    		}
		} catch(SAXException sex) {
			log.error("Errore nel formato del file XML: " + sex.getMessage(), sex);
		} catch(Exception ex) {
			log.error("Errore nell'elaborazione: " + ex.getMessage(), ex);
		}
	}

}
