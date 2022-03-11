package it.kdm.docer.commons;

//import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.axiom.om.impl.jaxp.OMResult;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import it.kdm.docer.commons.configuration.ConfigurationUtils;


public class PdfComposer {

	private Logger log = org.slf4j.LoggerFactory.getLogger(PdfComposer.class);
	
	private static final long serialVersionUID = 1L;
	private TransformerFactory transformerFactory;
	private DocumentBuilder builder;
	private FopFactory fopFactory;

	private Options opts;
//	private static final String XML_FILE = "C:/Curriculum.xml";
	private Document XML_FILE;
//	private static final String XSL_FILE = "C:/Curriculum.xsl"; 
//	private static final String PDF_FILE = "C:\\Curriculum.pdf"; 


	ByteArrayOutputStream out = new ByteArrayOutputStream(); 

	FileOutputStream pdfFOS = null; 
	FileInputStream xslFileIS = null; 
	FileInputStream xmlFileIS = null;
	
	private Map<String, Long> timestampMap;
	private Map<String, String> resourceMap;

	public PdfComposer () throws ParserConfigurationException {
		log.debug("Initialization PDF Composer");
		this.timestampMap = Collections.synchronizedMap(new HashMap<String, Long>());
		this.resourceMap = Collections.synchronizedMap(new HashMap<String, String>());

		this.transformerFactory = TransformerFactory.newInstance();
		this.fopFactory = FopFactory.newInstance();
		this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

		opts = new Options();
		opts.mode = MODE.XML; 
	}

    public Options getOptions() {
        return opts;
    }
    public void setOptions(Options opts) {
        this.opts = opts;
    }    
    public void setOptions(HttpServletRequest request) throws Exception {
        this.opts = this.parseRequestParameters(request);
    }    
    public void setOption(String name, String value) {
    	this.opts.params.put(name, value);
    }    
    public void setFileName(String value) {
    	opts.filename = String.format("%s.pdf", value);
    }
    public void setContent(String value) throws SAXException, IOException, ParserConfigurationException {

    	opts.source = new StreamSource(new StringReader(value)); //builder.parse(new InputSource(new StringReader(value)));;
    }   
    public void setMode(String value) {
    	opts.mode = MODE.valueOf(value.toUpperCase());
    }   
    public void setTemplate(String value) throws IOException {
    	opts.template = getTemplate(value);	 //this.getTemplate(value);
    }   
    public void setConf(String value) throws IOException {
    	opts.conf = this.getConfiguration(value);
    }   
    

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public DataHandler createPdf(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			//Document params = getPostData(request, opts);
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			FopFactory fopFactory = this.fopFactory;
			if(opts.conf != null) {
				fopFactory = FopFactory.newInstance();
				//fopFactory.setUserConfig(opts.conf);
			}
			
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
			
			//Setup Transformer
			Transformer transformer = transformerFactory.newTransformer(new StreamSource(opts.template));

			//Make sure the XSL transformation's result is piped through to FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			//Start the transformation and rendering process
			transformer.transform(opts.source, res);
			
			
//			response.setContentType(MimeConstants.MIME_PDF);
//			response.setContentLength(out.size());
//			response.addHeader("Content-Disposition", 
//					String.format("attachment; filename=\"%s\"", opts.filename));
//			
//			response.getOutputStream().write(out.toByteArray());
//			response.getOutputStream().flush();
			
//converto in FileDescriptionHandler
//            byte[] byteArray = out.getBytes("UTF-8");

            ByteArrayDataSource source = new ByteArrayDataSource(out.toByteArray());
            
            DataHandler handler  = new DataHandler(source);
            InputStream is = handler.getInputStream();

            return handler;
            
//fine converto in FileDescriptionHandler	
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	
	protected Document getPostData(HttpServletRequest request, Options opts) 
						throws SAXException, IOException, Exception {
		
		Document params;
		
		switch(opts.mode) {
		case XML:
			params = this.builder.parse(request.getInputStream());
			break;
		case FORM:
			params = this.builder.newDocument();
			Element parameters = params.createElement("parameters");
			params.appendChild(parameters);
			
			for(String key : opts.params.keySet()) {
				Element param = params.createElement("parameter");
				parameters.appendChild(param);
				
				Element name = params.createElement("name");
				name.setTextContent(key);
				param.appendChild(name);
				
				Element value = params.createElement("value");
				value.setTextContent(opts.params.get(key));
				param.appendChild(value);
			}
			
			break;
		default:
			throw new Exception("Passed mode is not valid");
		}
		
		return params;
	}

	private Options parseRequestParameters(HttpServletRequest request) throws Exception {
		
		Options opts = new Options();
		
		Enumeration<?> params = request.getParameterNames();
		
		while(params.hasMoreElements()) {
			String name = (String)params.nextElement();
			
			if(!name.equalsIgnoreCase("filename") &&
			   !name.equalsIgnoreCase("mode") &&
			   !name.equalsIgnoreCase("template") &&
			   !name.equalsIgnoreCase("conf") &&
			   !name.equalsIgnoreCase("debug")) {
				
				opts.params.put(name, request.getParameter(name));
			}
		}
		
		opts.filename = String.format("%s.pdf", this.getParameter("filename", request));
		opts.mode = MODE.valueOf(this.getParameter("mode", request).toUpperCase());
//		opts.template = this.getTemplate(this.getParameter("template", request));
		opts.conf = this.getConfiguration(this.getParameter("conf", request, true));
		
		opts.debug = request.getParameterMap().containsKey("debug");
		
		return opts;
	}

	private File getConfiguration(String confFilename) throws IOException {
		if(confFilename == null || confFilename.isEmpty()) {
			return null;
		}
				
		return ConfigurationUtils.loadResourceFile(String.format("configurations/%s.fo", confFilename));
	}

	private File getTemplate(String templateName) throws IOException {
		
		
		return ConfigurationUtils.loadResourceFile(String.format("templates/%s.fo", templateName));
		
	}

	private String getParameter(String parameter, HttpServletRequest request) throws Exception {
		return this.getParameter(parameter, request, false);
	}
	
	private String getParameter(String parameter, HttpServletRequest request, boolean isEmptyPermitted) throws Exception {
		
		String paramValue = request.getParameter(parameter);
		
		if(!isEmptyPermitted && (paramValue == null || paramValue.isEmpty())) {
			throw new Exception(String.format("Parameter '%s' cannot be empty", parameter));
		}
		
		return paramValue;
	}

	public synchronized String getTemplateString(String action) throws Exception  {
	
		try {
			File file = ConfigurationUtils.loadResourceFile(String.format("templates/%s.fo", action));
			long timestamp = file.lastModified();
			
			if(!this.timestampMap.containsKey(action)) {
				this.timestampMap.put(action, timestamp);
			}
			
			if(this.timestampMap.get(action) < timestamp || 
					!this.resourceMap.containsKey(action)) {
	
				
//				TransformerFactoryImpl saxonFactory = TransformerFactory.newInstance();
//				saxonFactory.setAttribute(FeatureKeys.XINCLUDE, "true");
//				Transformer trans = saxonFactory.newTransformer();	
				Transformer trans = TransformerFactory.newInstance().newTransformer();
				OMResult result = new OMResult();
				
				log.debug("getTemplate() -> parsing and transforming fop template");
				trans.transform(new StreamSource(file), result);
				log.debug("getTemplate() -> parsed and transformed fop template");
				
				String content = result.getRootElement().toString();
	
				
				this.timestampMap.put(action, timestamp);
				this.resourceMap.put(action, content);
			}
			
			return this.resourceMap.get(action);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}
}

class Options {
	public boolean debug=false;
	
	public String filename;
	public MODE mode;
	public File conf;
	public File template;
	public Source source;
	
	public Map<String, String> params = new HashMap<String, String>();
}

enum MODE {
	XML,
	FORM
}

