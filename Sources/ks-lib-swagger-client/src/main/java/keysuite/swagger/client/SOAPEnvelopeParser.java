package keysuite.swagger.client;

import io.swagger.models.Response;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class SOAPEnvelopeParser {

    private static final Logger logger = LoggerFactory.getLogger(SOAPEnvelopeBuilder.class);

    Map<String, String> outputHeaders;
    HttpEntity httpEntity;
    io.swagger.models.Operation operation;
    Map<String,Response> responses;
    MimeMultipart mp;
    private Integer responseCode=null;
    String xmlEnvelope;

    public SOAPEnvelopeParser(HttpEntity httpEntity, Map<String,Response> responses ) {
        this.outputHeaders = new HashMap<>();
        this.httpEntity = httpEntity;
        this.responses = responses;
        this.xmlEnvelope = null;
    }

    public Map<String,String> getOutputHeaders() {
        return this.outputHeaders;
    }

    private Object parseNode(Node node, Property prop) {
        if (prop instanceof ObjectProperty)
        {
            if (node==null)
                return null;

            ObjectProperty obj = (ObjectProperty) prop;

            Map<String,Object> map = new HashMap();

            if (prop.getFormat()!=null)
                map.put("@type",prop.getFormat());

            if (node.getFirstChild()==null || obj.getProperties()==null || obj.getProperties().size()==0)
                return map;

            Property firstProperty = obj.getProperties().values().iterator().next();

            /* trova un falso array da trasormare in mappa */
            if (obj.getProperties().size()==1
                    && firstProperty instanceof ArrayProperty
                    && RestUtils.isObject((ArrayProperty) firstProperty))
            {
                Object value = parseNodeList( node.getChildNodes() , (ArrayProperty) firstProperty );
                return value;
            }

            /* c'è un solo figlio e non è quello atteso ... entro dentro */
            while (node.getChildNodes().getLength()==1 && !obj.getProperties().containsKey( RestUtils.getLocalName(node.getFirstChild()) ) )
            {
                node = node.getFirstChild();
            }

            XPath xPath = XPathFactory.newInstance().newXPath();

            for( String xkey : obj.getProperties().keySet() )
            {
                try {

                    Property p = obj.getProperties().get(xkey);

                    Object value;
                    if (p instanceof ArrayProperty)
                    {
                        NodeList result = (NodeList) xPath.evaluate(xkey, node, XPathConstants.NODESET);

                        value = parseNodeList(result,(ArrayProperty) p);
                    }
                    else
                    {
                        Node result = (Node) xPath.evaluate(xkey, node, XPathConstants.NODE);

                        value = parseNode(result, p);
                    }

                    map.put(xkey,value);

                } catch (XPathExpressionException e) {
                    throw new RuntimeException(e);
                }
            }

            return map;
        }
        else
        {
            if (node==null)
                return null;

            if ( "Include".equals(RestUtils.getLocalName(node.getFirstChild())) && node.getFirstChild().getAttributes().getNamedItem("href")!=null)
            {
                String cid = node.getFirstChild().getAttributes().getNamedItem("href").getTextContent();
                cid = "<"+cid.replaceAll("^cid:","")+">";

                try {
                    BodyPart bp = mp.getBodyPart(cid);

                    if (bp!=null)
                        return RestUtils.toFile( bp.getInputStream(),bp.getFileName()  );
                    else
                        throw new RuntimeException("CID not found in SOAP response");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                String value = node.getTextContent();

                if (value!=null)
                    value = value.trim();

                String type = null,format = null;
                if (prop!=null)
                {
                    type = prop.getType();
                    format = prop.getFormat();
                }

                if ( prop instanceof FileProperty || "binary".equals(format))
                {
                    return RestUtils.toFile(IOUtils.toInputStream(value),null);
                }
                else if ("byte".equals(format) || "base64".equals(format) )
                {
                    byte[] bytes = Base64.decodeBase64(value);
                    return RestUtils.toFile(new ByteArrayInputStream(bytes),null);
                }
                else
                {
                    return RestUtils.cast(type,value);
                }
            }
        }
    }

    private Object parseNodeList(NodeList nodes, ArrayProperty arr) {
        List<Object> values = new ArrayList();

        if (nodes==null)
            return null;

        for (int i=0; i<nodes.getLength();i++)
        {
            Object value=parseNode( nodes.item(i) , arr.getItems()); ;
            /*Property p = arr.getItems();
            if (p instanceof ArrayProperty)
                value = parseNodeList( (NodeList) nodes.item(i) ,(ArrayProperty) p);
            else
                value = parseNode( nodes.item(i) , p);*/

            //Object value = parseXmlElement(nodes.item(i),XPathConstants.NODE,arr.getItems(), mp);
            values.add(value);
        }

        if (RestUtils.isObject(arr))
        {
            Object[] props = ((ObjectProperty) arr.getItems()).getProperties().keySet().toArray();

            Map<String, Object> map = new HashMap();

            for (Object obj : values) {
                map.put(((Map) obj).get(props[0].toString()).toString(), ((Map) obj).get(props[1].toString()));
            }
            return map;
        }
        else
            return values;
    }

    public Object parse() {

        mp = null;
        //String envelope;
        if (httpEntity.getContentType()!=null && RestUtils.checkContentType(httpEntity.getContentType().getValue(), RestUtils.MULTIPART))
        {
            try {
                mp = RestUtils.getMultipartContent(httpEntity.getContent(),httpEntity.getContentType().getValue(),null);
                BodyPart bp = mp.getBodyPart(0);
                xmlEnvelope = IOUtils.toString((InputStream)bp.getContent());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            try {
                xmlEnvelope = EntityUtils.toString(httpEntity, RestUtils.DEFAULT_CHARSET);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        logger.info("Soap Envelope Response:\n{}",xmlEnvelope);

        Document xml = RestUtils.parseXML(xmlEnvelope);

        if (xml==null)
            return null;
        Node envelopeNode = xml.getFirstChild();
        Node headerNode=null;
        Node bodyNode=null;
        Node faultNode=null;

        NodeList children = envelopeNode.getChildNodes();

        for( int i=0; i< children.getLength(); i++)
        {
            Node node = children.item(i);

            if ("Body".equals(RestUtils.getLocalName(node)))
            {
                bodyNode = node;

                node = bodyNode.getFirstChild();

                if ("Fault".equals(RestUtils.getLocalName(node)))
                    faultNode = node;
            }
            else if ("Header".equals(RestUtils.getLocalName(node)))
                headerNode = node;
        }

        if (faultNode!=null)
        {
            ObjectProperty faultBody = (ObjectProperty) responses.get("500").getSchema();;

            /*for ( String key : responses.keySet())
            {
                if (!"200".equals(key))
                {
                    oBody = responses.get(key).getSchema();
                    faultType = key;
                    break;
                }
            }

            if (oBody==null)
                return null;*/

            String faultType=null;
            responseCode = 500;

            try {

                String detailKey = "Detail";
                if (xmlEnvelope.contains("faultcode"))
                    detailKey = "detail";

                XPath xPath = XPathFactory.newInstance().newXPath();
                Node node = (Node) xPath.evaluate(detailKey, faultNode, XPathConstants.NODE);

                if (node!=null && node.getFirstChild()!=null)
                {
                    String ln = RestUtils.getLocalName(node.getFirstChild()); // node.getFirstChild().getNodeName().substring(node.getFirstChild().getNodeName().indexOf(":")+1);

                    for ( String key : responses.keySet())
                    {
                        Response faultResponse = responses.get(key);
                        String desc = faultResponse.getDescription();
                        if (desc!=null && ln.equals(desc.split(":")[0]))
                        {
                            faultBody = (ObjectProperty) faultResponse.getSchema();
                            faultType = ln;
                            responseCode = Integer.parseInt(key);
                            break;
                        }
                    }
                }
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }

            //Map<String,Object> faultResponse = (Map<String, Object>) parseNode( faultNode,faultBody);

            String code = null;
            String msg = null;
            Map<String,String> details = new LinkedHashMap<>();

            try{
                NodeList list;
                if (xmlEnvelope.contains("faultcode")){
                    code = (String) XPathFactory.newInstance().newXPath().evaluate("faultcode", faultNode, XPathConstants.STRING);
                    msg = (String) XPathFactory.newInstance().newXPath().evaluate("faultstring", faultNode, XPathConstants.STRING);
                    list = ((NodeList)XPathFactory.newInstance().newXPath().evaluate("detail/*", faultNode, XPathConstants.NODESET));
                } else {
                    code = (String) XPathFactory.newInstance().newXPath().evaluate("Code", faultNode, XPathConstants.STRING);
                    msg = (String) XPathFactory.newInstance().newXPath().evaluate("Reason", faultNode, XPathConstants.STRING);
                    list = ((NodeList)XPathFactory.newInstance().newXPath().evaluate("Detail/*", faultNode, XPathConstants.NODESET));
                }

                for( int i=0; i<list.getLength(); i++){
                    Node node = list.item(i);
                    details.put(node.getNodeName(),node.getTextContent());
                }

            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }

            Map<String,Object> faultResponse = new LinkedHashMap<>();

            faultResponse.put("faultcode",code);
            faultResponse.put("Code",code);

            faultResponse.put("faultstring",msg);
            faultResponse.put("Reason",msg);

            faultResponse.put("detail",details);
            faultResponse.put("Detail",details);

            return faultResponse;
        }
        else
        {
            if (headerNode!=null)
            {
                NodeList headers = headerNode.getChildNodes();

                for( int i=0; i<headers.getLength(); i++)
                {
                    Node header = headers.item(i);

                    String value = header.getTextContent();
                    if (value!=null)
                        outputHeaders.put( RestUtils.getLocalName(header) , value.trim() );
                }
            }

            Property oBody = responses.get("200").getSchema();

            Object result = parseNode( bodyNode.getFirstChild() ,oBody);
            responseCode = 200;
            /*if (oBody instanceof ArrayProperty)
                result = parseNodeList( bodyNode.getFirstChild().getChildNodes() , (ArrayProperty) oBody);
            else
                result = parseNode( bodyNode.getFirstChild() ,oBody);
            return result;*/
            return result;
        }
    }

    public Integer getResponseCode() {
        return responseCode;
    }
}
