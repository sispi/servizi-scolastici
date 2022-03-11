package keysuite.swagger.client;

import com.google.common.base.Strings;
import io.swagger.models.ModelImpl;
import io.swagger.models.Xml;
import io.swagger.models.properties.*;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

//import static it.kdm.orchestratore.producers.workitem.SOAPBridge.*;

public class SOAPEnvelopeBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SOAPEnvelopeBuilder.class);

    Map<String,String> headers;
    Map<String,Object> params;
    io.swagger.models.Operation operation;
    Document envelope;
    Map<String,String> attachments;
    Map<String,String> envNamespaces;
    HttpEntity httpEntity;
    String soapContentType;
    ModelImpl model;

    public String getXmlEnvelope() {
        return xmlEnvelope;
    }

    String xmlEnvelope;

    public SOAPEnvelopeBuilder(Map<String,String> headers, Map<String,Object> params, ModelImpl model)
    {
        this.headers = headers;
        this.params = params;
        this.model = model;
        this.xmlEnvelope = null;
    }

    public HttpEntity build(){
        return build("utf-8");
    }

    public HttpEntity build(String charset) {

        Object body = this.params.get("Body");

        if (body!=null && body instanceof Map){
            Map<String,Object> bodyMap = (Map<String,Object>) this.params.remove("Body");

            for(String key : bodyMap.keySet()){
                params.put(key,bodyMap.get(key));
            }
        }

        createParts(charset);

        this.xmlEnvelope = RestUtils.toXML(envelope);

        this.xmlEnvelope = this.xmlEnvelope.replace("xmlns=\"\"","");

        logger.info("Soap Envelope:\n{}",xmlEnvelope);
        if (attachments.size()==0)
        {
            httpEntity = new StringEntity(xmlEnvelope,charset);

            ((StringEntity)httpEntity).setContentType(soapContentType);

            return httpEntity;
        }
        else
        {
            MultipartEntityBuilder mpBuilder = MultipartEntityBuilder.create();

            mpBuilder.addTextBody("envelope",xmlEnvelope);

            for ( String cid : attachments.keySet() )
            {
                mpBuilder.addBinaryBody(cid,RestUtils.getFile(attachments.get(cid)));
            }

            BasicHeader mpContentType;

            List<FormBodyPart> bParts;
            try {
                Field f = mpBuilder.getClass().getDeclaredField("subType");
                f.setAccessible(true);
                f.set(mpBuilder,"Related");

                f = mpBuilder.getClass().getDeclaredField("bodyParts");
                f.setAccessible(true);
                bParts = (List<FormBodyPart>) f.get(mpBuilder);

                for( FormBodyPart part : bParts )
                {
                    part.getHeader().removeFields("Content-Disposition");
                    part.getHeader().removeFields("Content-Type");
                    part.getHeader().removeFields("Content-Transfer-Encoding");

                    if (attachments.containsKey(part.getName()))
                    {
                        part.addField("Content-ID",part.getName());
                        part.addField("Content-Transfer-Encoding","binary");
                        part.addField("Content-Type","application/octet-stream");
                    }
                    else
                    {
                        part.addField("Content-Type","application/xop+xml;"+soapContentType);
                    }
                }

                httpEntity = mpBuilder.build();

                f = httpEntity.getClass().getDeclaredField("contentType");
                f.setAccessible(true);
                mpContentType = (BasicHeader) f.get(httpEntity);

                String ct = mpContentType.getValue();
                ct = ct.replace("multipart/form-data","multipart/related");
                ct += "; start-info=\""+soapContentType+"\"; type=\"application/xop+xml\"";

                mpContentType = new BasicHeader(mpContentType.getName(),ct);
                f.set(httpEntity,mpContentType);
                return httpEntity;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createParts(String charset) {

        /*ModelImpl model = null;

        for ( Parameter p : parameters )
            if (p.getIn().equals("body"))
            {
                model = (ModelImpl) ((BodyParameter) p).getSchema();
                break;
            }

        if (model==null)
            throw new RuntimeException("invalid model for SOAP");*/

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        envNamespaces = new HashMap<String, String>();

        String tns=null, soapAction=null, tnsPrefix=null;
        String xmlPath = null;

        if (model.getXml()!=null)
        {
            tns = model.getXml().getNamespace();
            xmlPath = model.getXml().getName();
            int idx = xmlPath.indexOf("/");
            if (idx>0)
            {
                soapAction = xmlPath.substring(0,idx);
                xmlPath = xmlPath.substring(idx+1);
            } else {
                soapAction = xmlPath;
                xmlPath = null;
            }
            tnsPrefix = model.getXml().getPrefix();
        }

        if (soapAction==null)
            throw new RuntimeException("path opertion not specifica in xml tag");

        if (tns==null || tnsPrefix==null)
            throw new RuntimeException("Target Namespace not specified");

        String namespaces = (String) model.getVendorExtensions().get("x-namespaces");

        if (namespaces!=null)
        {
            String[] nss = namespaces.split("\\s+");

            for ( int i=0; i<nss.length; i++)
            {
                String[] parts = nss[i].split("\\=");

                String ns;

                if (parts[0].startsWith("xmlns:"))
                    ns = parts[0].substring(6);
                else
                    ns = "";

                envNamespaces.put(ns,parts[1]);
            }
        }

        envNamespaces.put(tnsPrefix,tns);

        String soapNs = envNamespaces.get("soapEnv");

        if (soapNs.equals("http://schemas.xmlsoap.org/soap/envelope/"))
            soapContentType = "text/xml; charset="+charset;
        else
            soapContentType = "application/soap+xml; charset="+charset;

        /*if (operation.getTags().contains("SOAP12"))
            envNamespaces.put("soap",soap12);
        else
            envNamespaces.put("soap",soap11);*/

//        envNamespaces.put("soap","http://schemas.xmlsoap.org/wsdl/soap/");

        envelope = docBuilder.newDocument();

        Element envElement = createElement("soapEnv:Envelope");
        Element bodyElement = createElement("soapEnv:Body");
        Element actionElement = createElement(tnsPrefix+":"+soapAction);

        bodyElement.appendChild(actionElement);
        for( String xmlns : envNamespaces.keySet() )
        {
            String fq;
            if (xmlns.equals(""))
                fq = "xmlns";
            else
                fq = "xmlns:"+xmlns;

            actionElement.setAttributeNS("http://www.w3.org/2000/xmlns/",fq,envNamespaces.get(xmlns));
        }

        if (xmlPath!=null){
            String[] parts = xmlPath.split("/");

            for ( int i=0; i<parts.length; i++){
                Element el = createElement(parts[i]);
                actionElement.appendChild(el);
                actionElement = el;
            }
        }
        attachments = new HashMap();

        if (model!=null && model.getProperties() != null) {

            List<String> required = model.getRequired();

            for (String name : model.getProperties().keySet()) {

                Property prop = model.getProperties().get(name);

                Object value;

                value = params.remove(name);

                if (value==null && name.contains(":")){
                    value = params.remove(name.split(":")[1]);
                }

                if (value==null)
                {
                    if (prop instanceof StringProperty)
                        value = ((StringProperty) prop).getDefault();
                    else if (prop instanceof IntegerProperty)
                        value = ((IntegerProperty) prop).getDefault();
                    else if (prop instanceof DoubleProperty)
                        value = ((DoubleProperty) prop).getDefault();

                    if (value==null){
                        if (prop.getRequired() || (required!=null && required.contains(name)))
                            value = "";
                        else
                            continue;
                    }
                }

                if ( !(value instanceof List) && RestUtils.isFile(value.toString()))
                {
                    String cid = newCid();
                    attachments.put(cid,value.toString());
                    value = "cid:"+cid;
                }
                else if (value instanceof List && ((List) value).size()>0 && RestUtils.isFile(((List) value).get(0).toString()))
                {
                    for( int i=0; i<((List)value).size(); i++)
                    {
                        String cid = newCid();
                        attachments.put(cid,((List)value).get(i).toString());
                        ((List)value).set(i,"cid:"+cid);
                    }
                }

                List<Element> elems = createElements(name,value, prop);

                for ( Node elem : elems)
                    actionElement.appendChild(elem);
            }
        }


        envelope.appendChild(envElement);

        if (headers!=null && headers.size()>0)
        {
            Element headerElement = createElement("soapEnv:Header");
            envElement.appendChild(headerElement);

            for ( String header : headers.keySet())
            {
                Element elem = createElement(header);
                elem.setTextContent(headers.remove(header).toString());
                headerElement.appendChild(elem);
            }
        }

        envElement.appendChild(bodyElement);
    }

    private static String newCid() {
        return ((Integer)new Random().nextInt(10000000)).toString();
    }

    private List<Element> createElements(String nodeName, Object value, Property prop) {
        if (prop instanceof ArrayProperty)
        {
            Property items = ((ArrayProperty) prop).getItems();
            if (value instanceof Map)
            {
                ObjectProperty obj = (ObjectProperty) ((ArrayProperty) prop).getItems();

                if (obj!=null && obj.getProperties()!=null && obj.getProperties().size()==2 )
                {
                    Object[] props = obj.getProperties().keySet().toArray();

                    Map<String,Object> map = (Map) value;
                    value = new ArrayList();
                    for( String key : map.keySet())
                    {
                        Map item = new HashMap();
                        item.put(props[0].toString(),key);
                        item.put(props[1].toString(),map.get(key));
                        ((List)value).add(item);
                    }
                }
            }

            if (!(value instanceof List))
            {
                value = Collections.singletonList(value);
            }

            List<Element> elems = new ArrayList();

            Xml xml = prop.getXml();
            String wrapper = null;

            if (xml!=null && xml.getName()!=null)
                wrapper = xml.getName();
            else if (prop.getVendorExtensions().get("x-wrapped")!=null) {
                String xWrapped = (String) prop.getVendorExtensions().get("x-wrapped");
                //TODO: se xWrapped è diverso dal nome della prop corrente (compreso il namespace) allore wrapped=xWrapped
                if (!nodeName.equalsIgnoreCase(xWrapped)) {
                    wrapper=xWrapped;
                }
            }

            for (Object item : ((List)value))
            {
                if (wrapper!=null)
                    elems.addAll(createElements(wrapper,item,items));
                else
                    elems.addAll(createElements(nodeName,item,items));
            }

            //if (prop.getVendorExtensions().get("x-wrapped")!=null)
            if (wrapper!=null)
            {
                //nodeName = (String) prop.getVendorExtensions().get("x-wrapped");

                Element elem = envelope.createElementNS(envNamespaces.get(nodeName.split("\\:")[0]),nodeName);

                for( Node e : elems )
                    elem.appendChild(e);

                return Collections.singletonList( elem);
            }
            else
                return elems;
        }
        else if (prop instanceof ObjectProperty)
        {
            Element elem = createElement(nodeName);

            ObjectProperty model = (ObjectProperty) prop;

            if (model.getProperties() != null && model.getProperties().size()>0) {

                Map<String,Object> map = new HashMap<String,Object>();

                if (value instanceof Map)
                {
                    map = (Map) value;
                }
                else if (value instanceof List)
                {
                    int idx = 0;
                    for (String name : model.getProperties().keySet()) {
                        if (((List)value).size()<=idx)
                            break;
                        map.put(name,((List)value).get(idx++));
                    }
                }
                else
                {
                    map.put(model.getProperties().keySet().iterator().next(),value);
                }

                for (String name : model.getProperties().keySet()) {

                    String nsKey = !name.contains(":") ? "" : name.split("\\:")[0];

                    Object val = map.get(name);

                    if (val==null)
                    {
                        String ns2 = envNamespaces.get(nsKey);
                        if (!Strings.isNullOrEmpty(ns2))
                            if (!ns2.endsWith("/"))
                                ns2+="/";

                        val = map.get(ns2+name);

                        if (val==null && name.contains(":")){
                            val = map.get(name.split(":")[1]);
                        }
                    }

                    if (val==null)
                        continue;

                    List<Element> elems = createElements(name,val, model.getProperties().get(name));

                    for ( Node e : elems)
                        elem.appendChild(e);
                }
            }
            return Collections.singletonList( elem);
        }
        else
        {
            Element elem = createElement(nodeName);
            if(value.getClass().isArray()){
                int size = Array.getLength(value);
                if(size==0){
                    value="";
                }else{
                    value = Array.get(value,0);
                }
            }
            if (value.toString().startsWith("cid:"))
            {
                elem.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:inc","http://www.w3.org/2004/08/xop/include");

                Element inc = envelope.createElementNS("http://www.w3.org/2004/08/xop/include","inc:Include");
                inc.setAttribute("href",value.toString());
                elem.appendChild(inc);
            }
            else
                elem.setTextContent(value.toString());
            return Collections.singletonList( elem);
        }
        /*else
        {
            Node elem = document.createTextNode(value.toString());
            return Collections.singletonList(elem);
        }*/
    }

    private Element createElement(String nodeName) {
        if (nodeName.startsWith(":"))
            nodeName = nodeName.substring(1);

        if (nodeName.contains(":"))
            return envelope.createElementNS(envNamespaces.get(nodeName.split("\\:")[0]),nodeName);
        else
            return envelope.createElement(nodeName);
    }
}
