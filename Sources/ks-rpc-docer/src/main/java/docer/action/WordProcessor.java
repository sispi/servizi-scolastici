package docer.action;

import docer.exception.ActionRuntimeException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordProcessor extends DocerAction {
	private final static Logger log = LoggerFactory.getLogger(docer.action.WordProcessor.class);
	@Override
	public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {

		log.info("init method execute");
        String operazioniAmmesse="tabellaMS,secondaOperazioni,...,";
		Map<String, Object> result = new HashMap<String, Object>();
		String token=null;
        String tabellaXML=null;
		ArrayList<HashMap<String, Object>> valoriMap = null;
		valoriMap = inputs.containsKey("valori")?(ArrayList<HashMap<String, Object>>)inputs.get("valori"):null;

		if(valoriMap == null)throw new ActionRuntimeException("mode 'valori' non trovato nei parametri di input!");

		 String tipo = inputs.containsKey("mode")?(String)inputs.get("mode"):null;
	        if (tipo == null)throw new ActionRuntimeException("PIN 'mode' non valorizzato nelle proprietà!");

        boolean ammesso=false;
        for(int z=0;z<operazioniAmmesse.split(",").length; z++){
                if(operazioniAmmesse.split(",")[z].equals(tipo)){
                    ammesso=true;
                }
        }
        if(!ammesso){
            throw new ActionRuntimeException("Ad oggi sono supportate le seguenti operazioni: "+ operazioniAmmesse );
        }

        if (tipo.equals("tabellaMS")){


            String header = (String) valoriMap.get(0).get("header");
            String[] headerSplit=header.split(",");
            ArrayList<HashMap<String, Object>> ListMap=new ArrayList<>();

            for(int i=0; i<valoriMap.size(); i++){
                if (i>0){
                    ListMap.add(valoriMap.get(i));
                }
            }

            XWPFDocument doc = new XWPFDocument();

            try {
                token = getToken(inputs);
                int numRighe=ListMap.size();
                int numColonne=headerSplit.length;
                XWPFTable table = doc.createTable(numRighe+1,numColonne);
                CTTblWidth width = table.getCTTbl().addNewTblPr().addNewTblW();
                width.setType(STTblWidth.DXA);
                width.setW(BigInteger.valueOf(8072));


                for(int i=0;i<headerSplit.length; i++){
                    //table.getRow(0).getCell(i).setText(headerSplit[i]);
                    table.getRow(0).getCell(i).setText("");
                }
                for(int j=0; j<ListMap.size(); j++){
                    HashMap currentMap=ListMap.get(j);

                    for(int k=0; k<headerSplit.length;k++){
                        table.getRow(j+1).getCell(k).setText((String) currentMap.get(headerSplit[k]));
                    }

                }
                /*************************************************************STILE DELLA TABELLA***************************************************************/

// Set the table style. If the style is not defined, the table style
                // will become "Normal".
                CTTblPr tblPr = table.getCTTbl().getTblPr();
                CTString styleStr = tblPr.addNewTblStyle();
                styleStr.setVal("StyledTable");
// Get a list of the rows in the table
                List<XWPFTableRow> rows = table.getRows();

                int rowCt = 0;
                int colCt = 0;
                for (XWPFTableRow row : rows) {

                    // get the cells in this row
                    List<XWPFTableCell> cells = row.getTableCells();
                    // add content to each cell
                    for (XWPFTableCell cell : cells) {

                        // get a table cell properties element (tcPr)
                        CTTcPr tcpr = cell.getCTTc().addNewTcPr();
                        // set vertical alignment to "center"
                        CTVerticalJc va = tcpr.addNewVAlign();
                        va.setVal(STVerticalJc.CENTER);

                        // create cell color element
                        CTShd ctshd = tcpr.addNewShd();
                        ctshd.setColor("auto");
                        ctshd.setVal(STShd.CLEAR);

                        if (rowCt == 0) {
                            // header row
                            ctshd.setFill("A7BFDE");
                        }

                        // get 1st paragraph in cell's paragraph list
                        XWPFParagraph para = cell.getParagraphs().get(0);
                        // create a run to contain the content
                        XWPFRun rh = para.createRun();

                        if (rowCt == 0) {
                            // header row

                            rh.setText(headerSplit[colCt]);
                            rh.setBold(true);
                            para.setAlignment(ParagraphAlignment.CENTER);
                        }
                        colCt++;

                    }
                    colCt = 0;
                    rowCt++;
                }


/*************************************************************FINE STILE***************************************************************/

                tabellaXML=doc.getXWPFDocument().getDocument().getBody().toString();

            } catch (Exception e) {
                log.error("errore word process:"+e.getMessage());
                e.printStackTrace();
                throw new ActionRuntimeException(e);
            } finally {
                try {
                    doc.close();
                } catch (IOException e) {
                    log.error("errore word process:"+e.getMessage());
                    e.printStackTrace();
                    throw new ActionRuntimeException(e);
                }
            }

        }
        result.put("resultProcess",tabellaXML);
        result.put("userToken", token);
		log.info("end method execute");
        log.info("Ritorno blocco wordProcess : "+result.toString());
		return result;
	}

	

}
