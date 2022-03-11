package it.kdm.docer.ws;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.configuration.ResourceCache;
import it.kdm.docer.fascicolazione.FascicolazioneException;
import it.kdm.docer.protocollazione.ProtocollazioneException;
import it.kdm.docer.providers.solr.SolrBaseUtil;
import it.kdm.docer.registrazione.RegistrazioneException;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.webservices.DownloadDocumentResponse;
//import it.kdm.orchestratore.session.Session;
import keysuite.cache.ClientCacheAuthUtils;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.client.User;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ContentDisposition;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.MethodEndpoint;
import org.springframework.ws.server.endpoint.adapter.DefaultMethodEndpointAdapter;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;
import org.springframework.ws.server.endpoint.adapter.method.MethodArgumentResolver;
import org.springframework.ws.server.endpoint.adapter.method.MethodReturnValueHandler;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static it.kdm.docer.ws.AuthenticationServiceEndpoint.TICKET_KEY;

//import static it.kdm.docer.commons.handlers.ServiceFilterUtils.TICKET_KEY;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    /*@Bean
    WebServiceMessageFactory messageFactory()
    {
        SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
        messageFactory.setSoapVersion(SoapVersion.SOAP_11);
        return messageFactory;
    }*/

    final String[] paths = new String[] {
            "/docersystem/services/*",
            "/WSDocer/services/*",
            "/WSFascicolazione/services/*",
            "/WSProtocollazione/services/*",
            "/WSRegistrazione/services/*",
            "/WSPEC/services/*",
            "/WSFirma/services/*"
    };

    MarshallingPayloadMethodProcessor methodProcessor = null;
    Jaxb2Marshaller marshaller = null;

    @Bean
    @RequestScope
    public SoapMessageFactory messageFactory() {
        DualProtocolSaajSoapMessageFactory s = new DualProtocolSaajSoapMessageFactory();
        return s;
    }

    /*@Bean
    public SoapMessageFactory messageFactory() {
        AxiomSoapMessageFactory axiomSoapMessageFactory = new AxiomSoapMessageFactory();
        axiomSoapMessageFactory.setPayloadCaching(false);
        return axiomSoapMessageFactory;
    }*/

    @Bean
    public ServletRegistrationBean<Servlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        //servlet.setMessageFactoryBeanName("axiom");
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<Servlet>(servlet,paths);
    }

    @Bean(name = "DocerServices")
    public Wsdl11Definition DocerServicesWsdl() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("/wsdl/DocerServices.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "AuthenticationService")
    public Wsdl11Definition AuthenticationServiceWsdl() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("/wsdl/AuthenticationService.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "WSFascicolazione")
    public Wsdl11Definition WSFascicolazioneWsdl() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("/wsdl/WSFascicolazione.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "WSRegistrazione")
    public Wsdl11Definition WSRegistrazioneWsdl() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("/wsdl/WSRegistrazione.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "WSProtocollazione")
    public Wsdl11Definition WSProtocollazioneWsdl() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("/wsdl/WSProtocollazione.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "WSPEC")
    public Wsdl11Definition WSPECWsdl() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("/wsdl/WSPEC.wsdl"));
        return wsdl11Definition;
    }

    @Bean(name = "WSFirma")
    public Wsdl11Definition WSFirmaWsdl() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("/wsdl/WSFirma.wsdl"));
        return wsdl11Definition;
    }

    @Bean
    public Jaxb2Marshaller marshaller() {

        if (marshaller==null){
            marshaller = new ExtMarshaller(); //new Jaxb2Marshaller();
            //marshaller.setContextPath("it.kdm.docer.webservices");
            marshaller.setClassesToBeBound(DownloadDocumentResponse.class);
            marshaller.setMtomEnabled(true);
        }
        return marshaller;
    }

    @Bean
    public MarshallingPayloadMethodProcessor methodProcessor() {

        if (methodProcessor==null){
            methodProcessor = new MarshallingPayloadMethodProcessor(marshaller);
        }

        return methodProcessor;
    }

    /*@Override
    public void addArgumentResolvers(List<MethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(methodProcessor);
    }*/

    @Bean
    DefaultMethodEndpointAdapter defaultMethodEndpointAdapter() {

        DefaultMethodEndpointAdapter adapter = new DefaultMethodEndpointAdapter(){
            @Override
            protected void handleMethodReturnValue(MessageContext messageContext, Object returnValue, MethodEndpoint methodEndpoint) throws Exception {

                if (Boolean.TRUE.equals(DualProtocolSaajSoapMessageFactory.forceJaxb.get())){
                    MethodParameter returnType = methodEndpoint.getReturnType();
                    methodProcessor.handleReturnValue(messageContext, returnType, returnValue);
                    DualProtocolSaajSoapMessageFactory.forceJaxb.set(false);
                } else {
                    super.handleMethodReturnValue(messageContext,returnValue,methodEndpoint);
                }
            }
        };
        //adapter.setMethodArgumentResolvers(Collections.singletonList( (MethodArgumentResolver) methodProcessor(marshaller())));
        //adapter.setMethodReturnValueHandlers(Collections.singletonList( (MethodReturnValueHandler) methodProcessor(marshaller())));

        List<MethodArgumentResolver> argumentResolvers = new ArrayList();
        List<MethodReturnValueHandler> returnValueHandlers = new ArrayList();

        //argumentResolvers.add(methodProcessor);
        //returnValueHandlers.add(methodProcessor);

        this.addArgumentResolvers(argumentResolvers);
        this.addReturnValueHandlers(returnValueHandlers);
        adapter.setCustomMethodArgumentResolvers(argumentResolvers);
        adapter.setCustomMethodReturnValueHandlers(returnValueHandlers);
        return adapter;
    }

    /*@Bean
    DefaultMethodEndpointAdapter endpointAdapter(MarshallingPayloadMethodProcessor methodProcessor) {

    }*/

    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {
        SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);

        Properties errorMappings = new Properties();
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(AuthenticationException.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(DocerException.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(RegistrazioneException.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(ProtocollazioneException.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(FascicolazioneException.class.getName(), SoapFaultDefinition.SERVER.toString());
        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }

    final static Pattern p = Pattern.compile("\\$\\{([^}]+)}");

    @Bean
    public FilterRegistrationBean<Filter> compatibilityFilter(){
        FilterRegistrationBean<Filter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                if ("GET".equals(request.getMethod())){
                    if ("wsdl".equalsIgnoreCase(request.getQueryString()))
                        request.getSession().getServletContext().getRequestDispatcher(request.getRequestURI() + ".wsdl").forward(request, response);
                    else {
                        String url = request.getRequestURI();
                        int idx = url.lastIndexOf("/");

                        String methodUrl = url.substring(idx);

                        if ("/downloadDocument".equals(methodUrl)) {
                            try {
                                downloadDocument(request, response);
                                return;

                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }

                        File file = ResourceCache.get("static"+methodUrl+".xml");

                        String xml = FileUtils.readFileToString(file, Charset.defaultCharset());

                        Matcher m = p.matcher(xml);

                        StringBuffer sb = new StringBuffer();
                        while (m.find()) {
                            String par = request.getParameter(m.group(1));
                            if (par==null)
                                par="";
                            m.appendReplacement(sb, par);
                        }
                        m.appendTail(sb);

                        xml = sb.toString();

                        final InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

                        HttpServletRequest req = new HttpServletRequestWrapper(request){

                            @Override
                            public Enumeration<String> getHeaderNames(){

                                Set<String> list = new HashSet(Collections.list(super.getHeaderNames()));
                                list.add("content-type");

                                return Collections.enumeration(list);
                            }

                            @Override
                            public String getHeader(String name){
                                if ("content-type".equalsIgnoreCase(name)){
                                    return MimeTypeUtils.TEXT_XML_VALUE;
                                } else {
                                    return super.getHeader(name);
                                }
                            }

                            @Override
                            public Enumeration<String> getHeaders(String name){
                                if ("content-type".equalsIgnoreCase(name)){
                                    return Collections.enumeration(Collections.singleton(MimeTypeUtils.TEXT_XML_VALUE));
                                } else {
                                    return super.getHeaders(name);
                                }
                            }

                            @Override
                            public String getMethod(){
                                return "POST";
                            }

                            @Override
                            public ServletInputStream getInputStream() throws IOException {

                                ServletInputStream servletInputStream = new ServletInputStream() {
                                    @Override
                                    public boolean isFinished() {
                                        try {
                                            return inputStream.available() == 0;
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }

                                    @Override
                                    public boolean isReady() {
                                        return true;
                                    }

                                    @Override
                                    public void setReadListener(ReadListener listener) {
                                        throw new UnsupportedOperationException();
                                    }

                                    public int read() throws IOException {
                                        return inputStream.read();
                                    }
                                };
                                return servletInputStream;
                            }

                        };

                        request.getSession().getServletContext().getRequestDispatcher(request.getRequestURI()).forward(req, response);
                    }

                } else {
                    filterChain.doFilter(request, response);
                }
            }

            private void downloadDocument(HttpServletRequest request, HttpServletResponse response) throws DocerException, IOException {
                int blockSize = Integer.parseInt(System.getProperty("docer.blockSize" , "1000000"));
                String docnum = request.getParameter("docnum");
                if (Strings.isNullOrEmpty(docnum)){
                    docnum = request.getParameter("docId");
                }
                String token = request.getParameter("token");
                String filename = request.getParameter("filename");
                String ticket;

                if (Strings.isNullOrEmpty(docnum)){
                    throw new RuntimeException("docnum o docId è obbligatorio");
                }

                if (Strings.isNullOrEmpty(token)){
                    throw new RuntimeException("token obbligatorio");
                }

                String callType = Utils.extractTokenKeyNoExc(token, "calltype");

                if ("internal".equals(callType)) {
                    ticket = Utils.removeTokenKey(token, "calltype").replace("|", "");
                } else if (token.contains("|")) {
                    AuthenticationServiceEndpoint.verifyToken(token);
                    String docTicket = Utils.extractTokenKeyNoExc(token, TICKET_KEY);
                    ticket = new TicketCipher().decryptTicket(docTicket);

                } else {
                    User user = ClientCacheAuthUtils.getInstance().getUser(token);
                    Claims claims = ClientUtils.parseJWTTokenWithoutSecret(token);

                    if (user==null){
                        throw new AuthenticationException("user "+claims.getSubject()+" not found");
                    }
                    ticket = ClientCacheAuthUtils.getInstance().getDMSTicket(user,(String) claims.get("secret"));
                }

                BusinessLogic bl = new BusinessLogic();
                InputStream is = bl.streamDocument(ticket,docnum);

                response.setContentType("application/octet-stream");

                Map<String,Object> extra = SolrBaseUtil.extraFields.get();
                if (extra!=null && extra.containsKey("streamSize")){
                    Number length = (Number) extra.get("streamSize") ;
                    response.setContentLength( length.intValue());
                }

                if (Strings.isNullOrEmpty(filename)){
                    Map<String,String> profile = bl.getProfileDocument(ticket,docnum);
                    filename = profile.get("DOCNAME");
                } else if (filename.endsWith(".")) {
                    filename = filename.substring(0, filename.length()-1);
                }

                ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                        .filename(filename)
                        .build();

                String cd = contentDisposition.toString();

                response.setHeader("Content-Disposition",cd);

                IOUtils.copy(is, response.getOutputStream(), blockSize);

                return;
            }
        });
        registrationBean.addUrlPatterns(paths);

        return registrationBean;
    }
}