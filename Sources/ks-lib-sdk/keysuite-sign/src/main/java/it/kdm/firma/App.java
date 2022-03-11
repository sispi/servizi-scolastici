package it.kdm.firma;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

/**
 * Hello world!
 *
 */
public class App {

    public static final String REPORT_XML_NAME = "report.xml";
    public static final String REPORT_JSON_NAME = "report.json";

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("ERROR, argomenti errati");
            System.exit(0);
        }
        String binaryDocument = args[0];
        String reportPathPrefix = args[1];
        boolean verifyCertificates = Boolean.parseBoolean(args[2]);
        SignatureValidator validator = new SignatureValidator(new FileInputStream(binaryDocument));
        validator.validateDocument();

        // System.out.println(validator.getFirmatari().get(0).getCognomeNome());

        PrintWriter outFile = new PrintWriter(new File(reportPathPrefix + "/simpleReport.xml"));
        outFile.print(validator.getSimpleReport());
        outFile.close();
        outFile = new PrintWriter(new File(reportPathPrefix + "/detailedReport.xml"));
        outFile.print(validator.getDetailedReport());
        outFile.close();
        outFile = new PrintWriter(new File(reportPathPrefix + "/diagnosticReport.xml"));
        outFile.print(validator.getDiagnosticReport());
        outFile.close();
        outFile = new PrintWriter(new File(reportPathPrefix + "/italianXmlReport.xml"));
        outFile.print(validator.getItalianXmlReport());
        outFile.close();
        outFile = new PrintWriter(new File(reportPathPrefix + "/italianJsonReport.json"));
        outFile.print(validator.getItalianJsonReport());
        outFile.close();
        System.out.println("Report generati");

        System.exit(0);
    }
}
