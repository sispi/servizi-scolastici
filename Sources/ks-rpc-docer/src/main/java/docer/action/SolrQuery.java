package docer.action;

import docer.exception.ActionRuntimeException;
import it.kdm.doctoolkit.model.GenericCriteria;
import it.kdm.doctoolkit.services.SOLRClient;
import it.kdm.doctoolkit.utils.SOLRResponse;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by matteog on 21/03/17.
 */
public class SolrQuery extends DocerAction {

    private final static Logger log = LoggerFactory.getLogger(docer.action.SolrQuery.class);

    public static SolrInputDocument toSolrInputDocument(SolrDocument d )
    {
        SolrInputDocument doc = new SolrInputDocument();
        for( String name : d.getFieldNames() ) {
            doc.addField( name, d.getFieldValue(name) );
        }
        return doc;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> execute(Map<String, Object> inputs)throws ActionRuntimeException {
        log.info("init method SolrQuery");

        //preparazione input
        Map<String, String> criteria = new HashMap<String, String>();
        HashMap<String, String> params = new HashMap<String, String>();
        Map<String, Object> output = new HashMap<String, Object>();


        String userToken = null;


        try {
            userToken = inputs.containsKey("userToken") ? (String) inputs.get("userToken") : null;
            if (userToken == null) throw new ActionRuntimeException("PIN 'userToken' non trovato nei parametri di input!");

            criteria = inputs.containsKey("criteria") ? (HashMap<String, String>) inputs.get("criteria") : null;
            if (criteria == null) throw new ActionRuntimeException("PIN 'criteria' non trovato nei parametri di input!");

            params = inputs.containsKey("params") ? (HashMap<String, String>) inputs.get("params") : null;
            if (params == null) throw new ActionRuntimeException("PIN 'params' non trovato nei parametri di input!");

            GenericCriteria genericCriteria = new GenericCriteria();

            //remove designer keys from criteria object
            if (criteria.containsKey("id")) {criteria.remove("id");}
            if (criteria.containsKey("name")) {criteria.remove("name");}
            if (criteria.containsKey("@structure")) {criteria.remove("@structure");}

            //remove designer keys from params object
            if (params.containsKey("id")) {params.remove("id");}
            if (params.containsKey("name")) {params.remove("name");}
            if (params.containsKey("@structure")) {params.remove("@structure");}


            for (Map.Entry<String, String> entry : criteria.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();


                if(value.matches("^\\s*\\[.*\\]\\s*$")) {
                    value = value.trim(); //elimino eventuali gli spazi iniziali e finali
                }else if(value.matches("^\\s*\\(.*\\)\\s*$")) {
                    value = value.trim(); //elimino eventuali gli spazi iniziali e finali
                    value=value.substring(1,value.length()-1);
                    value=value.trim();
                    String[]values = value.split("\\s+(or|OR)\\s+");
                    for(int i=0; i<values.length; i++){
                        values[i]=values[i].replaceAll("([\\(\\)\\s\\+\\-\\&\\!\\{\\}\\[\\]\\^\\~\\?\\:\\\\\\/])", "\\\\$1");
                    }
                    value="("+ StringUtils.join(values, " OR ")+")";
                }else {
                value = value.replaceAll("([\\(\\)\\s\\+\\-\\&\\!\\{\\}\\[\\]\\^\\~\\?\\:\\\\\\/])","\\\\$1");

                }
                genericCriteria.setRawProperty(key, value);
            }


            SOLRClient client = new SOLRClient();
            SOLRResponse resp = client.rawSolrSearch(userToken, genericCriteria, params, false);

            if(resp!=null && resp.getStatus()!=0){
                output.put("status", resp.getStatus());
                output.put("userToken",userToken);
                log.error("Error method SolrQueryche status: {}", resp.getStatus());
                throw new ActionRuntimeException("Error method SolrQueryche status: "+ resp.getStatus());
            }

            SolrDocumentList result = resp.getResults();
            String xmlFragment = "";

            for (SolrDocument doc : result) {
                xmlFragment += ClientUtils.toXML(toSolrInputDocument(doc));
            }
            output.put("userToken",userToken);
            output.put("xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root>"+xmlFragment+"</root>");
            output.put("json", result);
            output.put("numFound", result.size());
            output.put("status", 0);


        }
        catch (SolrServerException e){
            e.printStackTrace();
            output.put("status", -1);
            output.put("userToken",userToken);
            log.error("Error method SolrQuery:{}", e.getMessage());
            throw new ActionRuntimeException(e);
        }
        catch (Exception e){
            e.printStackTrace();
            output.put("status", -2);
            output.put("userToken",userToken);
            log.error("Error method SolrQuery:{}", e.getMessage());
            throw new ActionRuntimeException(e);
        }

        log.info("end method SolrQuery");
        return output;

    }




}
