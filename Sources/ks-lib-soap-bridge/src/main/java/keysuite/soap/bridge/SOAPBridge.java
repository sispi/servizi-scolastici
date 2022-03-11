package keysuite.soap.bridge;

import com.google.common.base.Strings;
import com.ibm.wsdl.extensions.schema.SchemaImpl;
import com.ibm.wsdl.extensions.schema.SchemaImportImpl;
import io.swagger.models.*;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.properties.*;
import io.swagger.parser.Swagger20Parser;
import org.apache.ws.commons.schema.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.wsdl.*;
import javax.wsdl.Operation;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.wsdl.extensions.soap12.SOAP12Operation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

class SOAPBridge {

    private static final Logger logger = LoggerFactory.getLogger(SOAPBridge.class);

    static final String APPLICATION_SOAP11 = "text/xml; charset=utf-8";
    static final String APPLICATION_SOAP12 = "application/soap+xml; charset=utf-8";

    static final String soap12 = "http://www.w3.org/2003/05/soap-envelope";
    static final String soap11 = "http://schemas.xmlsoap.org/soap/envelope/";

    Map<QName, XmlSchemaType> types;
    Map<QName, XmlSchemaElement> elements;
    Set<String> imported;
    Map<String,String> namespaces;
    Set<String> usednamespaces;

    String wsdl;
    Definition wsdlInstance;
    Swagger swagger;
    String serviceName;
    String port;

    SOAPBridge(String wsdl,String service,String port)
    {
        this.wsdl = wsdl;
        this.serviceName = service;
        this.port = port;
    }

    int prefixCounter=0;
    private boolean importElements(SchemaImpl extension){
        boolean fullyQualified = true;
        if (extension instanceof SchemaImpl) {
            Element schElement = ((SchemaImpl) extension).getElement();
            XmlSchema schema = new XmlSchemaCollection().read(schElement);
            this.types.putAll(schema.getSchemaTypes());
            this.elements.putAll(schema.getElements());

            if (schema.getElementFormDefault() == null || !schema.getElementFormDefault().name().equals("QUALIFIED"))
                fullyQualified = false;


            prefixCounter++;
            for ( String prefix : schema.getNamespaceContext().getDeclaredPrefixes() )
                this.namespaces.put( (String) schema.getNamespaceContext().getNamespaceURI(prefix),prefix+prefixCounter);

            Map map = ((SchemaImpl) extension).getImports();

            for ( Object o : map.values() ){

                if (o instanceof Vector){
                    Vector v = (Vector) o;
                    for( int i=0; i<v.size(); i++){
                        o = v.get(i);
                        if (o instanceof SchemaImportImpl){
                            SchemaImportImpl impl = (SchemaImportImpl) o;
                            if (!this.imported.contains(impl.getSchemaLocationURI())) {
                                this.imported.add(impl.getSchemaLocationURI());
                                Schema s = impl.getReferencedSchema();
                                if (s instanceof SchemaImpl) {
                                    boolean fq2 = importElements((SchemaImpl) s);
                                    fullyQualified = fullyQualified && fq2;
                                }
                            }
                        }
                    }
                }


            }
        }
        return fullyQualified;
    }

    public Swagger build() throws WSDLException {

        boolean isURI;
        boolean isWSDL;

        String test = wsdl.toLowerCase().trim();

        if (test.startsWith("<")){
            isURI = false;
            isWSDL = true;
        } else if (test.startsWith("{")){
            isURI = false;
            isWSDL = false;
        } else if (test.endsWith(".yaml")){
            isURI = true;
            isWSDL = false;
        } else if (test.contains("\\n")) {
            isURI = false;
            isWSDL = false;
        } else {
            isURI = true;
            isWSDL = !(test.endsWith(".json") || wsdl.toLowerCase().endsWith(".swagger") || wsdl.toLowerCase().endsWith(".yaml"));
        }

        if (test.startsWith("resource:"))
            wsdl = wsdl.substring(9);

        if (!isWSDL){
            //Swagger
            try{
                Swagger swagger;
                if (isURI) {
                    logger.info("Tentativo parsing swagger:",wsdl);
                    swagger = new Swagger20Parser().read(wsdl, null);
                    if (swagger==null)
                        throw new WSDLException("500","Errore swagger not found:"+wsdl);
                } else {
                    logger.info("Tentativo parsing swagger by string");
                    swagger = new Swagger20Parser().parse(wsdl);
                }
                return swagger;
            } catch (IOException ioe){
                throw new WSDLException("500","Errore parsing swagger",ioe);
            }
        }

        WSDLFactory factory = WSDLFactory.newInstance();

        WSDLReader reader = factory.newWSDLReader();

        if (isURI) {
            logger.info("Tentativo parsing wsdl:"+wsdl);
            wsdlInstance = reader.readWSDL(null, wsdl);
        } else {
            logger.info("Tentativo parsing wsdl by string");
            InputSource src = new InputSource( new StringReader( wsdl ) );
            wsdlInstance = reader.readWSDL( null, src );
        }

        Types types = wsdlInstance.getTypes();

        this.types = new HashMap<>();
        this.elements = new HashMap<>();
        this.imported = new HashSet<>();
        boolean fullyQualified = true;

        namespaces = new HashMap<>();

        if (types!=null) {

            List<?> extensions = types.getExtensibilityElements();
            for (Object extension : extensions) {
                if (extension instanceof SchemaImpl) {
                    boolean fq = importElements((SchemaImpl) extension);
                    fullyQualified = fullyQualified && fq;
                }
            }
        }

        for ( String prefix : ((Map<String,String>) wsdlInstance.getNamespaces()).keySet() )
            this.namespaces.put( (String) wsdlInstance.getNamespaces().get(prefix),prefix);

        usednamespaces = new LinkedHashSet<>();

        String tns = wsdlInstance.getTargetNamespace();

        try {
            String tnsPrefix;
            URI uri = new URI(tns);
            if (!fullyQualified)
                tnsPrefix = uri.getHost().replace(".","").substring(0,3);
            else
                tnsPrefix = "";

            namespaces.put(tns,tnsPrefix);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        swagger = parse();
        return swagger;
    }

    private boolean isSOAP12Address(Port port ) {
        if (port.getExtensibilityElements()==null)
            return false;

        for(Object ee : port.getExtensibilityElements())
        {
            if (ee instanceof SOAP12Address)
                return true;
        }
        return false;
    }

    private String getSoapActionURI(BindingOperation operation) {

        if (operation==null || operation.getExtensibilityElements()==null)
            return null;

        for(Object ee : operation.getExtensibilityElements())
        {
            if (ee instanceof SOAP12Operation) {
                return ((SOAP12Operation)ee).getSoapActionURI();

            } else if (ee instanceof SOAPOperation) {
                return ((SOAPOperation)ee).getSoapActionURI();
            }
        }
        return null;


    }

    private String getPortAddress( Port port ) {
        if (port.getExtensibilityElements()==null)
            return null;

        for(Object ee : port.getExtensibilityElements())
        {
            try {
                Method m = ee.getClass().getDeclaredMethod("getLocationURI");
                return (String) m.invoke(ee);
            } catch (NoSuchMethodException e) {
                continue;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private Swagger parse() throws WSDLException {

        Swagger swagger = new Swagger();

        String tns = wsdlInstance.getTargetNamespace();
        String tnsPrefix = namespaces.get(tns);

        Map<QName,Service> services = (Map<QName,Service>) wsdlInstance.getServices();

        Service srv=null;

        if (Strings.isNullOrEmpty(this.serviceName))
            srv = (Service) wsdlInstance.getServices().values().iterator().next();
        else
        {
            for ( QName qname : services.keySet() ) {
                if (qname.getLocalPart().equals(this.serviceName))
                    srv = (Service) wsdlInstance.getServices().get(qname);
            }
        }

        if (srv==null)
            throw new RuntimeException("service not found");

        Port port=null;

        if (Strings.isNullOrEmpty(this.port))
            port = (Port) srv.getPorts().values().iterator().next();
        else
            port = srv.getPort(this.port);

        if (port==null)
            throw new RuntimeException("port not found");

        Info info = new Info();
        info.setTitle(srv.getQName().getLocalPart());

        if (srv.getDocumentationElement()!=null)
            info.setDescription(srv.getDocumentationElement().getTextContent().trim());
        else if (wsdlInstance.getDocumentationElement()!=null)
            info.setDescription(wsdlInstance.getDocumentationElement().getTextContent().trim());

        swagger.setInfo(info);

        String address = getPortAddress(port);

        if (address==null)
            throw new RuntimeException("invalid binding");

        try {
            URI uri = new URI(address);

            swagger.setHost(uri.getAuthority());
            swagger.addScheme(Scheme.forValue(uri.getScheme().toLowerCase()));
            swagger.setBasePath(uri.getPath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        PortType portType = port.getBinding().getPortType();

        Map<String, Path> paths = new HashMap<String, Path>();
        swagger.setPaths(paths);

        for (Operation srvOperation : ((List<Operation>) portType.getOperations())) {

            Path path = new Path();
            paths.put("#" + srvOperation.getName(), path);

            BindingOperation bindingOperation = port.getBinding().getBindingOperation(srvOperation.getName(),srvOperation.getInput().getName(),srvOperation.getOutput().getName());

            io.swagger.models.Operation operation = new io.swagger.models.Operation();

            path.setPost(operation);

            operation.setSummary(srvOperation.getName());

            if (srvOperation.getDocumentationElement()!=null)
                operation.setDescription(srvOperation.getDocumentationElement().getTextContent().trim());

            //Object soapAddress = getSOAPAddress(port);



            /*if (operation.getTags().contains("SOAP12"))
            envNamespaces.put("soap",soap12);
        else
            envNamespaces.put("soap",soap11);*/

            usednamespaces.clear();

            operation.addTag("SOAP");

            boolean isSOAP12 = isSOAP12Address(port);

            if (isSOAP12)
            {
                operation.addTag("SOAP12");
                operation.consumes(APPLICATION_SOAP12);
                namespaces.put(soap12,"soapEnv");
                usednamespaces.add(soap12);
            }
            else
            {
                operation.addTag("SOAP11");
                operation.consumes(APPLICATION_SOAP11);
                namespaces.put(soap11,"soapEnv");
                usednamespaces.add(soap11);
            }



            /* INPUT */

            ModelImpl model = new ModelImpl();

            Xml xml = new Xml();
            xml.setPrefix(tnsPrefix);
            xml.setNamespace(tns);
            xml.setName(srvOperation.getName());

            model.setXml(xml);

            String SOAPAction = getSoapActionURI(bindingOperation);

            /* se non è esplicita come asttributo della operazione ... */
            if (SOAPAction == null) {

                SOAPAction = srvOperation.getName();

                int idx = SOAPAction.indexOf("/");
                if (idx>0) {
                    SOAPAction = srvOperation.getName().substring(0,idx);
                }

                if (!Strings.isNullOrEmpty(tnsPrefix))
                    SOAPAction = tnsPrefix+":"+SOAPAction;
                else if (!Strings.isNullOrEmpty(tns))
                    SOAPAction = tns+SOAPAction ;
            }

            //model.setVendorExtension("x-SOAPAction", SOAPAction);
            operation.setOperationId(SOAPAction);

            Input input = srvOperation.getInput();


            if (input != null) {
                Message inputMsg = input.getMessage();

                //TODO gestire headers
                Part part = (Part) inputMsg.getParts().values().iterator().next();

                if (part.getElementName() != null){
                    XmlSchemaElement elem = elements.get(part.getElementName());
                    XmlSchemaComplexType param = (XmlSchemaComplexType) getType(elem);

                    ObjectProperty prop = processComplexType(param, true, true);
                    if (prop.getXml()!=null){
                        xml.setName(xml.getName()+"/"+prop.getXml().getName());
                    }
                    model.setProperties(prop.getProperties());
                } else {

                    Property prop = processSimpleType(part.getTypeName());
                    Map<String, Property> props = new HashMap<>();
                    props.put(part.getName(),prop);
                    model.setProperties(props);
                }

                BodyParameter body = new BodyParameter();
                body.setSchema(model);
                //body.setName(part.getName());
                model.setTitle(part.getName());

                operation.addParameter(body);
            }

            /* OUTPUT */

            Output output = srvOperation.getOutput();

            if (output != null) {
                Message outputMsg = output.getMessage();

                //TODO gestire headers
                Part part = (Part) outputMsg.getParts().values().iterator().next();

                Response response = new Response();

                if (part.getElementName() != null){
                    XmlSchemaElement elem = elements.get(part.getElementName());
                    XmlSchemaComplexType param = (XmlSchemaComplexType) getType(elem);

                    ObjectProperty oResp = processComplexType(param, false, false);

                    oResp.setTitle(part.getName());

                    // se c'è una sola chiave nell'output diventa direttamente il risultato (ma solo se è una mappa)
                    if (oResp instanceof ObjectProperty && ((ObjectProperty) oResp).getProperties().size() == 1)
                    {
                        Property p = oResp.getProperties().values().iterator().next();

                        if (p instanceof ObjectProperty)
                            oResp = (ObjectProperty) p;
                    }

                    response.setSchema(oResp);
                } else {

                    Property prop = processSimpleType(part.getTypeName());
                    response.setSchema(prop);
                }

                operation.addResponse("200", response);
            }

            /* FAULTS */

            Response defaultFaultResponse = new Response();

            ObjectProperty defaultFaultResp = new ObjectProperty();

            Map<String,Property> dProps = new LinkedHashMap<>();

            defaultFaultResp.setProperties(dProps);

            if (isSOAP12)
            {
                dProps.put("Code", new StringProperty());
                dProps.put("Reason", new StringProperty());
                dProps.put("Node", new StringProperty());
                dProps.put("Role", new StringProperty());
            }
            else
            {
                dProps.put("faultcode", new StringProperty());
                dProps.put("faultstring", new StringProperty());
                dProps.put("faultfactor", new StringProperty());
            }

            defaultFaultResponse.setSchema(defaultFaultResp);
            defaultFaultResponse.setDescription("Soap Generic Fault");

            operation.addResponse("500", defaultFaultResponse);

            Integer code = 510;

            //if ( soapAddress != null)
            //{
            Map<String, Fault> faults = srvOperation.getFaults();

            for (String faultKey : faults.keySet()) {
                Fault fault = faults.get(faultKey);

                Message faultMsg = fault.getMessage();

                Part part = (Part) faultMsg.getParts().values().iterator().next();

                XmlSchemaElement elem = elements.get(part.getElementName());
                XmlSchemaComplexType param = (XmlSchemaComplexType) getType(elem);

                ObjectProperty faultDetail = processComplexType(param, false, false);

                Response faultResponse = new Response();

                String desc = "";

                for (String key : faultDetail.getProperties().keySet())
                    desc+= String.format("%s%s : %s","".equals(desc) ? "" : ", ", key, faultDetail.getProperties().get(key).getType() );

                String doc = "";
                if (fault.getDocumentationElement()!=null)
                    doc = "\n" + fault.getDocumentationElement().getTextContent().trim();
                if (faultMsg.getDocumentationElement()!=null)
                    doc = "\n" + faultMsg.getDocumentationElement().getTextContent().trim();

                faultResponse.setDescription( String.format("%s: { %s }%s", faultKey, desc, doc) );

                ObjectProperty faultResp = new ObjectProperty();

                Map<String,Property> props = new LinkedHashMap<>(dProps);

                faultResp.setProperties(props);

                if (isSOAP12)
                    props.put("Detail", faultDetail);
                else
                    props.put("detail" , faultDetail );

                faultResponse.setSchema(faultResp);
                operation.addResponse(code.toString(), faultResponse);

                code++;
            }
            //}

            /* NAMESPACES */

            String xnamespaces = "";
            for (String ns : usednamespaces) {
                if (!ns.equals(tns))
                    xnamespaces += " xmlns:" + namespaces.get(ns) + "=" + ns;
            }

            model.setVendorExtension("x-namespaces", xnamespaces.trim());
        }

        return swagger;
    }

    private Property processSimpleType(XmlSchemaSimpleType type) {

        Property p;
        if (isEnumeration(type)) {
            List<String> enums = enumeratorValues(type);
            p = new StringProperty();
            ((StringProperty)p).setEnum(enums);
            return p;

        } else {
            p = getPropertyByType(type.getQName());
        }
        return p;
    }

    private Property processSimpleType(QName type) {

        Property p = getPropertyByType(type);
        return p;
    }

    private static List<XmlSchemaElement> getElements(XmlSchemaParticle particle) {
        List<XmlSchemaElement> schemaElements = new ArrayList<XmlSchemaElement>();

        if (particle==null)
            return schemaElements;

        List<?> elementList = null;
        if (particle instanceof XmlSchemaSequence) {
            elementList =  ((XmlSchemaSequence) particle).getItems();
        } else if (particle instanceof XmlSchemaAll) {
            elementList = ((XmlSchemaAll) particle).getItems();
        } else if (particle instanceof XmlSchemaChoice) {
            elementList = ((XmlSchemaChoice) particle).getItems();
        }

        for( Object element : elementList )
        {
            if (element instanceof XmlSchemaElement)
                schemaElements.add( (XmlSchemaElement) element );
        }

        return schemaElements;
    }

    private ObjectProperty processComplexType(XmlSchemaComplexType elemType,boolean qualify,boolean xmlPath) {

        ObjectProperty oProp = new ObjectProperty();
        Map<String,Property> props = new LinkedHashMap<String, Property>();
        oProp.setProperties(props);

        List<XmlSchemaElement> elementList = new ArrayList<>();

        while(true)
        {
            if (elemType.getContentModel()!=null)
            {
                XmlSchemaContent content = elemType.getContentModel().getContent();
                XmlSchemaParticle particle;
                XmlSchemaType baseType;

                if (content instanceof XmlSchemaComplexContentExtension ) {

                    XmlSchemaComplexContentExtension ext = (XmlSchemaComplexContentExtension) content;
                    particle = ext.getParticle();
                    baseType = types.get(ext.getBaseTypeName());
                }
                else if (content instanceof XmlSchemaComplexContentRestriction)
                {
                    XmlSchemaComplexContentRestriction ext = (XmlSchemaComplexContentRestriction) content;
                    particle = ext.getParticle();
                    baseType = types.get(ext.getBaseTypeName());
                }
                else
                    throw new RuntimeException("not expected");

                List<XmlSchemaElement> elems = getElements(particle);
                Collections.reverse(elems);
                elementList.addAll(elems);

                if (baseType instanceof XmlSchemaComplexType)
                    elemType = (XmlSchemaComplexType) baseType;
                else
                    break;
            }
            else
            {
                XmlSchemaParticle particle = elemType.getParticle();

                if (particle!=null)
                {
                    List<XmlSchemaElement> elems = getElements(particle);
                    Collections.reverse(elems);
                    elementList.addAll(elems);
                }

                break;
            }
        }

        Collections.reverse(elementList);

        for (XmlSchemaElement element : elementList) {

            if (element.getName()==null && element.getRef()!=null){
                element = element.getRef().getTarget();
            }

            XmlSchemaType elementType = element.getSchemaType();
            if (elementType==null)
                elementType = types.get(element.getSchemaTypeName());

            String propName = element.getName();
            String title = element.getName();

            if (Strings.isNullOrEmpty(propName) || elementType==null)
                throw new RuntimeException("Non è stato possibile nominare il tipo xml:"+element);

            if (qualify && element.getParent().getElementFormDefault()!=null && element.getParent().getElementFormDefault().name().equals("QUALIFIED"))
            {
                String qns = element.getParent().getTargetNamespace();
                String ns = element.getQName().getNamespaceURI();

                assert(qns.equals(ns));

                if (!namespaces.get(ns).equals(""))
                    propName = namespaces.get(ns) + ":" + element.getName();

                usednamespaces.add(ns);
            }

            Property prop;

            if (elementType instanceof XmlSchemaSimpleType) {
                prop = processSimpleType((XmlSchemaSimpleType) elementType );

            } else if (elementType instanceof XmlSchemaComplexType) {

                ObjectProperty cProp = processComplexType((XmlSchemaComplexType) elementType,qualify,false );

                prop = cProp;

                if ( cProp.getProperties().size()==1){
                    String pName = cProp.getProperties().keySet().iterator().next();
                    Property p = cProp.getProperties().get(pName);

                    if (p instanceof ArrayProperty) {
                        Xml xml = cProp.getXml();

                        if (xmlPath) {
                            if (xml == null) {
                                xml = new Xml();
                                xml.setPrefix("");
                                xml.setNamespace("");
                                xml.setName(pName);
                            } else {
                                xml.setName(xml.getName() + "/" + pName);
                            }
                            p.setXml(xml);
                        }
                        ((ArrayProperty) p).setVendorExtension("x-wrapped",pName);

                        prop = p;
                    }
                }





            }
            else
            {
                prop = getPropertyByType(elementType.getQName());
            }

            if (element.getMaxOccurs()>1)
            {
                ArrayProperty ap = new ArrayProperty();
                ap.setItems(prop);

                if (prop instanceof ObjectProperty && ((ObjectProperty) prop).getProperties().size()==2) {
                    for( String pKey : ((ObjectProperty) ap.getItems()).getProperties().keySet() )
                        if (pKey.endsWith(":key") || pKey.equals("key")) {
                            ap.setFormat("object");
                            break;
                        }
                }

                ap.setMinItems( ((Number)element.getMinOccurs()).intValue() );
                ap.setMaxItems( ((Number)element.getMaxOccurs()).intValue() );
                ap.setRequired(element.getMinOccurs()>0);

                props.put(propName,ap);
                ap.setTitle(title);
            }
            else
            {
                prop.setRequired(element.getMinOccurs()>0);
                props.put(propName,prop);
                prop.setTitle(title);
            }
        }

        while (oProp.getProperties().size()==1 )
        {
            String pName = oProp.getProperties().keySet().iterator().next();
            Property p = oProp.getProperties().get(pName);

            if (p instanceof ObjectProperty) {
                Xml xml = oProp.getXml();
                oProp = (ObjectProperty) p;
                if (xmlPath) {


                    if (xml == null) {
                        xml = new Xml();
                        xml.setPrefix("");
                        xml.setNamespace("");
                        xml.setName(pName);
                    } else {
                        xml.setName(xml.getName() + "/" + pName);
                    }
                    oProp.setXml(xml);
                }
            }
            else
                break;
        }

        //oProp.setRequired(true);
        return oProp;
    }

    private static Property getPropertyByType(QName type) {

        String typeName = type.getLocalPart();

        Property p;

        switch (typeName)
        {
            case "date":
                p = new DateProperty();
                break;
            case "dateTime":
                p = new DateTimeProperty();
                break;
            case "hexBinary":
                p = new BinaryProperty();
                break;
            case "base64Binary":
                p = new ByteArrayProperty();
                break;
            case "long" :
                p = new LongProperty();
                break;
            case "decimal" :
                p = new DecimalProperty();
                break;
            case "byte" :
            case "short" :
            case "integer":
            case "nonPostiveInteger":
            case "nonNegativeInteger":
            case "negativeInteger":
            case "positiveInteger":
            case "unsignedInt":
            case "positiveShort":
            case "positiveByte":
            case "int":
                p = new IntegerProperty();
                break;
            case "float":
                p = new FloatProperty();
                break;
            case "double":
                p = new DoubleProperty();
                break;
            case "boolean":
                p = new BooleanProperty();
                break;
            default:
                p =  new StringProperty();
                break;
        }

        return p;
    }

    private XmlSchemaType getType(XmlSchemaElement element) {
        XmlSchemaType type = element.getSchemaType();
        if (type==null)
            return types.get(element.getSchemaTypeName());
        else
            return type;
    }

    private static boolean isEnumeration(XmlSchemaSimpleType type) {
        try {
            XmlSchemaSimpleTypeRestriction restriction = (XmlSchemaSimpleTypeRestriction) type.getContent();
            List<XmlSchemaFacet> facets = restriction.getFacets();
            for (XmlSchemaFacet facet : facets) {
                if (facet instanceof XmlSchemaEnumerationFacet) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private static List<String> enumeratorValues(XmlSchemaSimpleType type) {
        XmlSchemaSimpleTypeRestriction restriction = (XmlSchemaSimpleTypeRestriction) type.getContent();
        List<XmlSchemaFacet> facets = restriction.getFacets();
        List<String> values = new ArrayList<String>();
        for (XmlSchemaFacet facet : facets) {
            XmlSchemaEnumerationFacet enumFacet = (XmlSchemaEnumerationFacet) facet;
            values.add(enumFacet.getValue().toString());
        }
        return values;
    }
}
