package it.kdm.docer.sdk.frontend;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.impl.jaxp.OMResult;
import org.apache.axiom.om.impl.jaxp.OMSource;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axis2.databinding.ADBBean;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.ConfigurationMap;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.jaxen.JaxenException;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.frontend.utils.ResourceManager;
import it.kdm.docer.sdk.frontend.utils.UserInfoCache;
import it.kdm.utils.TransformerFactory;
import net.sf.saxon.FeatureKeys;
import net.sf.saxon.TransformerFactoryImpl;

public abstract class BaseFrontendExtension {
	
	private static ConfigurationMap config;
	private static String defaultAction;
	
	private static Logger log = org.slf4j.LoggerFactory.getLogger(BaseFrontendExtension.class);
	
	static {
		try {
			File confFile = ConfigurationUtils.loadResourceFile("xformsconf.xml");
			XMLConfiguration conf = new XMLConfiguration(confFile);
			
			defaultAction = conf.getString("extensions[@default]");
			config = new ConfigurationMap(conf.configurationAt("extensions"));
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static final BaseFrontendExtension getExtension(String action) throws DocerException {
		try {
			String className = config.containsKey(action) ? 
					(String)config.get(action) : defaultAction;
			
			log.debug(String.format("Getting instance of %s", className));
			Class<?> klass = Class.forName(className);
			return (BaseFrontendExtension)klass.newInstance();
			
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage(), e);
			throw new DocerException(e);
		} catch (InstantiationException e) {
			log.error(e.getMessage(), e);
			throw new DocerException(e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
			throw new DocerException(e);
		}
	}
	
	public abstract OMElement getModel(String token, String action, OMElement submission) throws DocerException;
	public abstract FileDataDescriptor downloadFile(String token, OMElement submission) throws IOException, DocerException;
	public FileDataDescriptor downloadTimbro(String token, OMElement submission) throws IOException {
		throw new NotImplementedException();
	}
	
	public FileDataDescriptor downloadData(String token, OMElement submission) throws DocerException, IOException{
		throw new NotImplementedException();
	}
	
	/*public String getTemplate(String action) throws TransformerFactoryConfigurationError, TransformerException, URISyntaxException, DocerException {
		log.debug(String.format("Requested template for %s", action));
		URL fileUrl = this.getClass().getResource(String.format("/templates/%s.xhtml", action));
		OMFactory factory = OMAbstractFactory.getOMFactory();
		
//		URL transformUrl = this.getClass().getResource("/xbl_apply.xslt");
		File xslt = getXsltFile("kforms.xslt");
		
		TransformerFactoryImpl saxonFactory = (TransformerFactoryImpl)TransformerFactory.newInstance();
		saxonFactory.setAttribute(FeatureKeys.XINCLUDE, "true");
		Transformer trans = saxonFactory.newTransformer(
				new StreamSource(xslt.toURI().toString()));	
		OMResult result = new OMResult(factory);
		
		log.debug("getTemplate() -> parsing and transforming template");
		trans.transform(new StreamSource(fileUrl.toURI().toString()), result);
		log.debug("getTemplate() -> parsed and transformed template");
		
		return result.getDocument().getOMDocumentElement().toString();
	}*/
        public OMElement buildRestResponseOK() {
            return buildRestResponse("0", "");
        }
	public OMElement buildRestResponse(String errno, String errMessage) {
            return buildRestResponse(errno,errMessage,null);
        }
        
        public OMElement buildRestResponse(String errno, String errMessage, List<OMElement> customNodes ) {
            OMFactory factory = OMAbstractFactory.getOMFactory();
            OMElement result = factory.createOMElement(new QName("result"));
            OMElement errnoNode = factory.createOMElement(new QName("errno"));
            OMElement messageNode = factory.createOMElement(new QName("message"));
            
            errnoNode.setText(errno);
            messageNode.setText(errMessage);
            
            result.addChild(errnoNode);
            result.addChild(messageNode);
            
            if (customNodes != null)
                for (OMElement item : customNodes)
                    result.addChild(item);
            
            return result;
        }
        
	public String getTemplate(String action) throws DocerException {
		return ResourceManager.INSTANCE.getTemplate(action);
	}
	
	protected final OMElement changeView(String newAction, OMElement submission) throws DocerException {
		
		try {
			String xpath = "/parameters/action";
			AXIOMXPath expr = new AXIOMXPath(xpath);
			OMElement action = (OMElement)expr.selectSingleNode(submission);
			action.setText(newAction);
			
			return submission;
		} catch (JaxenException e) {
			log.error(e.getMessage(), e);
			throw new DocerException(e);
		}
	}
	
	/*protected final OMElement redirect(String token, String targetAction, OMElement submission) throws DocerException {
		this.changeView(targetAction, submission);
		
		BaseFrontendExtension targetExtension = BaseFrontendExtension.getExtension(targetAction);
		return targetExtension.getModel(token, targetAction, submission);
	}*/
	
	protected final OMElement redirect(String token, String targetAction, OMElement submission) throws DocerException {
		try {
			this.changeView(targetAction, submission);
			
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader reader = factory.createXMLStreamReader(
					this.getClass().getResourceAsStream("/redirect.xml"));
			StAXOMBuilder builder = new StAXOMBuilder(reader);
			OMElement doc = builder.getDocumentElement();
			
			String redirectUrl;
			if(targetAction.toLowerCase().startsWith("http://")) {
				redirectUrl = targetAction;
			} else {
				redirectUrl = String.format("XForms?action=%s", targetAction);
			}
			
			OMElement url = getChild("url", doc);
			url.setText(redirectUrl);
			
			return doc;
		
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DocerException(e);
		}
		
	}
	
	public final OMElement getUserInfo(String token, String action, OMElement submission) throws DocerException {
		return UserInfoCache.INSTANCE.getUserInfo(token);
	}
	
	private File getXsltFile(String fileName) throws DocerException
	{
		try {
			File file = ConfigurationUtils.loadResourceFile(String.format("/templates/library/%s", fileName));
			return file;
		} catch (Exception e) {
			throw new DocerException("xsltFile not found: " + fileName, e);
		}
		
	}
	
	protected final OMElement xsltTransform(OMElement node, String xsltFileName) throws DocerException {
		File file = this.getXsltFile(xsltFileName);
		
		OMResult result;
		try {
			TransformerFactoryImpl saxonFactory = TransformerFactory.newInstance();
			saxonFactory.setAttribute(FeatureKeys.XINCLUDE, "true");
			Transformer t = saxonFactory.newTransformer(new StreamSource(file));
			result = new OMResult();
			t.transform(new OMSource(node), result);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DocerException(e);
		}
		
		return result.getRootElement();
		
	}
	
	protected final OMElement xPathSelectSingleNode(String xpath, OMElement node) throws JaxenException {
		AXIOMXPath path = new AXIOMXPath(node, xpath);
		return (OMElement)path.selectSingleNode(node);
	}
	
	protected final List<?> xPathSelectNodes(String xpath, OMElement node) throws JaxenException {
		AXIOMXPath path = new AXIOMXPath(node, xpath);
		return path.selectNodes(node);
	}
	
	protected final OMElement getSoapContent(ADBBean response) throws DocerException {
		try {
			Field field = response.getClass().getDeclaredField("MY_QNAME");
			QName qname = (QName) field.get(response.getClass());
			
			Method method = response.getClass().getDeclaredMethod("getOMElement", 
					QName.class, OMFactory.class);
			
			return (OMElement)method.invoke(response, qname, OMAbstractFactory.getOMFactory());
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new DocerException(e);
		}
	}
	
	
	protected final OMElement getChild(String nodeName, OMElement node) throws DocerException {
		Iterator<?> nodes = node.getChildrenWithName(new QName(nodeName));
		if(!nodes.hasNext()) {
			throw new DocerException(
					String.format("Node %s not found in node %s.",nodeName,
									node.getLocalName()));
		}
		
		return (OMElement)nodes.next();
	}
	
	protected final OMElement getChildOrNull(String nodeName, OMElement node) {
		Iterator<?> nodes = node.getChildrenWithName(new QName(nodeName));
		if(!nodes.hasNext()) {
			return null;
		}
		
		return (OMElement)nodes.next();
	}

	public final OMElement getError(String token, String action, OMElement submission) throws DocerException {
		return getInstance(submission);
	}

	public final OMElement getFeedback(String token, String action,
			OMElement submission) throws DocerException {
		return getInstance(submission);
	}

	private OMElement getInstance(OMElement submission) throws DocerException {
		OMElement instance = this.getChild("instance", submission);
		return instance.getFirstElement();
	}
	
	protected DataHandler getDataHandler(OMElement fileElem)
			throws URISyntaxException {
		String type = fileElem.getAttributeValue(new QName(
				"http://www.w3.org/2001/XMLSchema-instance", "type"));
		
		DataHandler handler;
		
		if(type.equals("xs:anyURI")) {
			URI fileURI = new URI(fileElem.getText());
			File file = new File(fileURI.getRawPath());
			handler = new DataHandler(new FileDataSource(file));
		} else {
			String content = fileElem.getText();
			handler = new DataHandler(
					new ByteArrayDataSource(Base64.decodeBase64(content)));
		}
		return handler;
	}

	protected DataHandler getDataHandlerFromXml(OMElement xml) throws Exception {
		
		// TODO: Does not work!
		//return XMLStreamReaderUtils.getDataHandlerFromElement(xml.getXMLStreamReader());
		
		String encoding = xml.getXMLStreamReader().getCharacterEncodingScheme();
		byte[] bytes;
		
		if(encoding != null) {
			bytes = xml.toString().getBytes(encoding);
		} else {
			bytes = xml.toString().getBytes();
		}
		
		return new DataHandler(new ByteArrayDataSource(bytes));
	}
}
