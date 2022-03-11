/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.utilities;

import it.filippetti.ks.api.payment.exception.ApplicationException;
import java.io.IOException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dino
 */
public class PrintUtil {

    private static final Logger log = LoggerFactory.getLogger(MicroserviceUtil.class);
    
    public static String generateHtmlReceipt(JSONObject json)throws ApplicationException, IOException{
        log.info("Create html");
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang=\"en\">");
        
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
        html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        html.append("<title>Ricevuta di pagamento</title>");
        html.append("</head>");
        
        html.append("<body>");
        html.append("<p>").append(json.getJSONObject("enteBeneficiario").getString("denominazioneBeneficiario")).append("</P>");
        html.append("<p>C.F. ").append(json.getJSONObject("enteBeneficiario").getJSONObject("identificativoUnivocoBeneficiario").getString("codiceIdentificativoUnivoco")).append("</P>");
        html.append("<div style=\"text-align: right;\">");
        html.append("<p>Gent.le</p>");
        html.append("<p>").append(json.getJSONObject("soggettoPagatore").getString("anagraficaPagatore")).append("</p>");
        html.append("<p>C.F. ").append(json.getJSONObject("soggettoPagatore").getJSONObject("identificativoUnivocoPagatore").getString("codiceIdentificativoUnivoco")).append("</p>");
        html.append("</div>");
        
        html.append("<p><strong>Dettaglio del pagamento effettuato tramite ")
                .append(json.getJSONObject("istitutoAttestante").getString("denominazioneAttestante"))
                .append(", identificato con il codice ")
                .append(json.getJSONObject("datiPagamento").getJSONObject("datiSingoloPagamento").getString("identificativoUnivocoRiscossione"))
                .append(":</strong></p>");
        
        html.append("<table>");
        html.append("<tr>")
                .append("<td style=\"background-color: #dddddd;\">Documento</td>")
                .append("<td>").append(json.getJSONObject("datiPagamento").getJSONObject("datiSingoloPagamento").getString("causaleVersamento")).append("</td></tr>");
        html.append("<tr>")
                .append("<td style=\"background-color: #dddddd;\">Identificativo Ordine</td>")
                .append("<td>").append(json.getJSONObject("datiPagamento").getString("CodiceContestoPagamento")).append("</td></tr>");
        html.append("<tr>")
                .append("<td style=\"background-color: #dddddd;\">Codice Operazione assegnato dal Comune (IUV)</td>")
                .append("<td>").append(json.getJSONObject("datiPagamento").getString("identificativoUnivocoVersamento")).append("</td></tr>");
        html.append("<tr>")
                .append("<td style=\"background-color: #dddddd;\">Codice Operazione assegnato dal PSP (IUR)</td>")
                .append("<td>").append(json.getJSONObject("datiPagamento").getJSONObject("datiSingoloPagamento").getString("identificativoUnivocoRiscossione")).append("</td></tr>");
        html.append("<tr>")
                .append("<td style=\"background-color: #dddddd;\">Data Pagamento</td>")
                .append("<td>").append(json.getJSONObject("datiPagamento").getJSONObject("datiSingoloPagamento").getString("dataEsitoSingoloPagamento")).append("</td></tr>");
        html.append("<tr>")
                .append("<td style=\"background-color: #dddddd;\">Importo</td>")
                .append("<td>").append(json.getJSONObject("datiPagamento").getJSONObject("datiSingoloPagamento").getString("singoloImportoPagato")).append("</td></tr>");
        html.append("<tr>")
                .append("<td style=\"background-color: #dddddd;\">Causale Pagamento</td>")
                .append("<td>").append(json.getJSONObject("datiPagamento").getJSONObject("datiSingoloPagamento").getString("causaleVersamento")).append("</td></tr>");
        html.append("</table>");
        
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }
    
}
