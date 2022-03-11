/*
 * (c) 2018 Raffaele Dell'Aversana
 */
package it.filippetti.ks.api.payment.pmpay;

import it.filippetti.ks.api.payment.ApplicationContextHolder;
import it.filippetti.ks.api.payment.configuration.ApplicationProperties;
import it.filippetti.ks.api.payment.pmpay.authpa.jaxb.HeaderRichiestaIUV;
import it.filippetti.ks.api.payment.pmpay.authpa.jaxb.RICHIEDIIUVREQUEST;
import it.filippetti.ks.api.payment.pmpay.authpa.jaxb.RICHIEDIIUVRESPONSE;
import it.filippetti.ks.api.payment.pmpay.authpa.jaxb.RichiestaIUV;
import it.filippetti.ks.api.payment.pmpay.authpa.jaxb.WSLOGINREQUEST;
import it.filippetti.ks.api.payment.pmpay.authpa.jaxb.WSLOGINRESPONSE;
import it.filippetti.ks.api.payment.pmpay.paypa.jaxb.AnagraficaPagamento;
import it.filippetti.ks.api.payment.pmpay.paypa.jaxb.HeaderPagamento;
import it.filippetti.ks.api.payment.pmpay.paypa.jaxb.HeaderRichiestaRT;
import it.filippetti.ks.api.payment.pmpay.paypa.jaxb.PAGAMENTOREQUEST;
import it.filippetti.ks.api.payment.pmpay.paypa.jaxb.PAGAMENTORESPONSE;
import it.filippetti.ks.api.payment.pmpay.paypa.jaxb.Pagamento;
import it.filippetti.ks.api.payment.pmpay.paypa.jaxb.RICHIEDIRTREQUEST;
import it.filippetti.ks.api.payment.pmpay.paypa.jaxb.RICHIEDIRTRESPONSE;
import it.filippetti.ks.api.payment.pmpay.paypa.jaxb.RichiestaRT;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

/**
 * Helper class per i servisi pmpay, basata sui seguenti servizi soap:
 * <ul>
 * <li>https://service.pmpay.it/authPA/services/AuthPASoap?wsdl</li>
 * <li>https://service.pmpay.it/payPA/services/PayPA?wsdl</li>
 * </ul>
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jul 2018
 */
public class PMPayService {

    /* factories degli object da usare per i servizi */
    private static final it.filippetti.ks.api.payment.pmpay.authpa.jaxb.ObjectFactory authpaFactory;
    private static final it.filippetti.ks.api.payment.pmpay.paypa.jaxb.ObjectFactory pmpayFactory;

    /* logger, per disabilitarlo vedere il blocco static di seguito */
    private static final Logger LOG;

    /* url reali dei servizi, non quelli del wsdl */
    private static final String authpaServiceUrl;
    private static final String paypaServiceUrl;

    /* endpoint dei ws */
    private static final it.filippetti.ks.api.payment.pmpay.authpa.AuthPASoap authPAService;
    private static final it.filippetti.ks.api.payment.pmpay.paypa.PayPA payPAService;
    //private static RelaxedPropertyResolver propertyResolver;
    public static final String PAGOPA = "pagopa.";
    public static final String PAGOPA_URL = "application.pagoPAUrl";

    
    // public static final ApplicationContext APPLICATION_CONTEXT;
    /* inizializzazioni varie */
    static {
        LOG = Logger.getLogger(PMPayService.class.getName());
        // per disabilitare il logger
        // LOG.setLevel(Level.OFF);
        
        authpaFactory = new it.filippetti.ks.api.payment.pmpay.authpa.jaxb.ObjectFactory();
        pmpayFactory = new it.filippetti.ks.api.payment.pmpay.paypa.jaxb.ObjectFactory();
        
        String BASE_URL = ApplicationProperties.get().pagoPAUrl();
        // String BASE_URL = "https://service.pmpay.it/"; // "https://secure.pmpay.it/";
        authpaServiceUrl = BASE_URL + "authPA/services/AuthPASoap";
        paypaServiceUrl = BASE_URL + "payPA/services/PayPA";
        LOG.log(Level.INFO, "authpaServiceUrl: " + authpaServiceUrl);
        LOG.log(Level.INFO, "paypaServiceUrl: " + paypaServiceUrl);
        
        it.filippetti.ks.api.payment.pmpay.authpa.AuthPA authService = new it.filippetti.ks.api.payment.pmpay.authpa.AuthPA();
        authPAService = authService.getAuthPASoap();
        ((BindingProvider) authPAService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, authpaServiceUrl);

        it.filippetti.ks.api.payment.pmpay.paypa.PayPA_Service payService = new it.filippetti.ks.api.payment.pmpay.paypa.PayPA_Service();
        payPAService = payService.getPayPA();
        ((BindingProvider) payPAService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, paypaServiceUrl);
    }

    /**
     * effettua un pagamento
     *
     * @param request parametri per il pagamento
     * @return la risposta di pmpay
     * @throws PMPayException
     */
    public static PagamentoResponse payment(PagamentoRequest request)
            throws PMPayException {

        WSLOGINREQUEST loginRequest = createLoginRequest(request);
        LOG.log(Level.INFO, toString(loginRequest));

        WSLOGINRESPONSE loginResponse = getTicket(loginRequest);
        LOG.log(Level.INFO, toString(loginResponse));

        if (loginResponse.getCODICEERRORE() != null) {
            final String error = "Error " + loginResponse.getCODICEERRORE() + ": " + loginResponse.getDESCRIZIONEERRORE();
            LOG.log(Level.SEVERE, error);
            throw new PMPayException(error);
        }

        RICHIEDIIUVREQUEST iuvRequest = createIUVRequest(loginRequest, loginResponse, request.getRifInterno());
        LOG.log(Level.INFO, toString(iuvRequest));

        RICHIEDIIUVRESPONSE iuvResponse = getIUV(iuvRequest);
        LOG.log(Level.INFO, toString(iuvResponse));

        if (iuvResponse.getCODICEERRORE() != null) {
            final String error = "Error " + iuvResponse.getCODICEERRORE() + ": " + iuvResponse.getDESCRIZIONEERRORE();
            LOG.log(Level.SEVERE, error);
            throw new PMPayException(error);
        }

        PAGAMENTOREQUEST pagamentoRequest = createPagamentoRequest(loginRequest, loginResponse, iuvResponse, request);
        LOG.log(Level.INFO, toString(pagamentoRequest));
        
        PAGAMENTORESPONSE pagamentoResponse = pagamento(pagamentoRequest);
        LOG.log(Level.INFO, toString(pagamentoResponse));

        return new PagamentoResponse(pagamentoResponse, iuvResponse);
    }

    public static RicevutaResponse getRicevuta(RicevutaRequest request) throws PMPayException {
        WSLOGINREQUEST loginRequest = createLoginRequest(request);
        LOG.log(Level.INFO, toString(loginRequest));

        WSLOGINRESPONSE loginResponse = getTicket(loginRequest);
        LOG.log(Level.INFO, toString(loginResponse));

        if (loginResponse.getCODICEERRORE() != null) {
            final String error = "Error " + loginResponse.getCODICEERRORE() + ": " + loginResponse.getDESCRIZIONEERRORE();
            LOG.log(Level.SEVERE, error);
            throw new PMPayException(error);
        }

        RICHIEDIRTREQUEST rtRequest = new RICHIEDIRTREQUEST();

        HeaderRichiestaRT header = new HeaderRichiestaRT();
        header.setCODICEAZIENDA(loginRequest.getCODICEAZIENDA());
        header.setDATARICHIESTA(loginRequest.getDATARICHIESTA());
        header.setIDCLIENT(loginRequest.getIDCLIENT());
        header.setIDRICHIESTA(loginRequest.getIDRICHIESTA());
        header.setTOKEN(loginResponse.getTOKEN());
        rtRequest.setHeaderRichiestaRT(header);

        RichiestaRT richiesta = new RichiestaRT();
        richiesta.setIUV(request.getIuv());
        richiesta.setRIFINTERNO(request.getRifInterno());
        rtRequest.setRichiestaRT(richiesta);

        RICHIEDIRTRESPONSE response = getRT(rtRequest);
        return new RicevutaResponse(response);
    }

    private static PAGAMENTOREQUEST createPagamentoRequest(WSLOGINREQUEST loginRequest, WSLOGINRESPONSE loginResponse, RICHIEDIIUVRESPONSE iuvResponse, PagamentoRequest request) {
        PAGAMENTOREQUEST pagamentoRequest = pmpayFactory.createPAGAMENTOREQUEST();
        HeaderPagamento headerPagamento = pmpayFactory.createHeaderPagamento();
        headerPagamento.setIDCLIENT(loginRequest.getIDCLIENT());
        headerPagamento.setTOKEN(loginResponse.getTOKEN());
        headerPagamento.setDATARICHIESTA(loginRequest.getDATARICHIESTA());
        headerPagamento.setIDRICHIESTA(loginRequest.getIDRICHIESTA());
        headerPagamento.setCODICEAZIENDA(loginRequest.getCODICEAZIENDA());
        headerPagamento.setCODICEFISCALE(request.getCodiceFiscale());
        headerPagamento.setTIPOCLIENT(request.getTipoClient());
        headerPagamento.setURLOK(request.getUrlOk());
        headerPagamento.setURLKO(request.getUrlKo());
        headerPagamento.setURLCANCEL(request.getUrlCancel());
        headerPagamento.setURLS2S(request.getUrlS2S());
        pagamentoRequest.setHeaderPagamento(headerPagamento);

        AnagraficaPagamento anagraficaPagamento = pmpayFactory.createAnagraficaPagamento();
        anagraficaPagamento.setNOME(request.getNome());
        anagraficaPagamento.setCOGNOME(request.getCognome());
        pagamentoRequest.setAnagraficaPagamento(anagraficaPagamento);

        Pagamento pagamento = pmpayFactory.createPagamento();
        pagamento.setCAUSALEPAGAMENTO(request.getCausalePagamento());
        pagamento.setSERVIZIOPAGAMENTO(request.getServizioPagamento());
        pagamento.setDIVISA(request.getDivisa());
        pagamento.setIMPORTOTOTALE(request.getImportoTotale());
        pagamento.setRIFPRATICA(iuvResponse.getIUV());
        pagamento.setDATAPAGAMENTO(loginRequest.getDATARICHIESTA());
        pagamentoRequest.getPagamento().add(pagamento);

        return pagamentoRequest;
    }

    private static RICHIEDIIUVREQUEST createIUVRequest(WSLOGINREQUEST loginRequest, WSLOGINRESPONSE loginResponse, String rifInterno) {
        RICHIEDIIUVREQUEST iuvRequest = authpaFactory.createRICHIEDIIUVREQUEST();
        HeaderRichiestaIUV headerRichiestaIUV = authpaFactory.createHeaderRichiestaIUV();
        headerRichiestaIUV.setIDCLIENT(loginRequest.getIDCLIENT());
        headerRichiestaIUV.setTOKEN(loginResponse.getTOKEN());
        headerRichiestaIUV.setDATARICHIESTA(loginRequest.getDATARICHIESTA());
        headerRichiestaIUV.setIDRICHIESTA(loginRequest.getIDRICHIESTA());
        headerRichiestaIUV.setCODICEAZIENDA(loginRequest.getCODICEAZIENDA());
        iuvRequest.setHeaderRichiestaIUV(headerRichiestaIUV);
        RichiestaIUV richiestaIUV = authpaFactory.createRichiestaIUV();
        richiestaIUV.setRIFINTERNO(rifInterno);
        richiestaIUV.setTIPOREFERENCE("A");
        iuvRequest.setRichiestaIUV(richiestaIUV);
        return iuvRequest;
    }

    /**
     * Crea una WSLOGINREQUEST
     *
     * @param request parametri di input per il pagamento
     * @return
     * @throws DatatypeConfigurationException
     */
    private static WSLOGINREQUEST createLoginRequest(LoginRequest request) throws PMPayException {
        WSLOGINREQUEST loginRequest = authpaFactory.createWSLOGINREQUEST();

        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(request.getDataRichiesta());
        XMLGregorianCalendar gdate;
        try {
            gdate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            throw new PMPayException("internal error");
        }
        loginRequest.setDATARICHIESTA(gdate);

        loginRequest.setIDCLIENT(request.getIdClient());
        loginRequest.setPWDCLIENT(request.getPwdClient());
        loginRequest.setIDRICHIESTA(request.getIdRichiesta());
        loginRequest.setCODICEAZIENDA(request.getCodiceAzienda());
        return loginRequest;
    }

    private static WSLOGINRESPONSE getTicket(WSLOGINREQUEST loginRequest) throws PMPayException {
        try {
            String content = toXML(loginRequest);
            String response = authPAService.getTicket(content);
            WSLOGINRESPONSE res = XMLUtils.unmarshal(response, WSLOGINRESPONSE.class);
            return res;
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            throw new PMPayException("internal error");
        }
    }

    private static RICHIEDIIUVRESPONSE getIUV(RICHIEDIIUVREQUEST iuvRequest) throws PMPayException {
        try {
            String content = toXML(iuvRequest);
            String t = authPAService.getIUV(content);
            RICHIEDIIUVRESPONSE res = XMLUtils.unmarshal(t, RICHIEDIIUVRESPONSE.class);
            return res;
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            throw new PMPayException("internal error");
        }
    }

    private static PAGAMENTORESPONSE pagamento(PAGAMENTOREQUEST request) throws PMPayException {
        try {
            String content = toXML(request);
            LOG.log(Level.INFO, "PAGAMENTORESPONSE pagamento");
            LOG.log(Level.INFO, content);
            String t = payPAService.pagamento(content);
            PAGAMENTORESPONSE res = XMLUtils.unmarshal(t, PAGAMENTORESPONSE.class);
            return res;
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            throw new PMPayException("internal error");
        }
    }

    private static RICHIEDIRTRESPONSE getRT(RICHIEDIRTREQUEST rtRequest) throws PMPayException {
        try {
            String content = toXML(rtRequest);
            String t = payPAService.getRT(content);
            RICHIEDIRTRESPONSE res = XMLUtils.unmarshal(t, RICHIEDIRTRESPONSE.class);
            return res;
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            throw new PMPayException("internal error");
        }
    }

    // WSLOGINREQUEST
    private static String toString(WSLOGINREQUEST loginRequest) {
        try {
            return toXML(loginRequest);
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private static String toXML(WSLOGINREQUEST loginRequest) throws JAXBException {
        return XMLUtils.marshal(loginRequest, new QName("urn:authPA", "WSLOGIN_REQUEST"));
    }

    // WSLOGINRESPONSE
    private static String toString(WSLOGINRESPONSE loginResponse) {
        try {
            return toXML(loginResponse);
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private static String toXML(WSLOGINRESPONSE loginResponse) throws JAXBException {
        return XMLUtils.marshal(loginResponse, new QName("urn:authPA", "WSLOGIN_RESPONSE"));
    }

    // RICHIEDIIUVREQUEST
    private static String toString(RICHIEDIIUVREQUEST iuvRequest) {
        try {
            return toXML(iuvRequest);
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private static String toXML(RICHIEDIIUVREQUEST iuvRequest) throws JAXBException {
        return XMLUtils.marshal(iuvRequest, new QName("urn:authPA", "RICHIEDI_IUV_REQUEST"));
    }

    // RICHIEDIIUVRESPONSE
    private static String toString(RICHIEDIIUVRESPONSE richiediIUVResponse) {
        try {
            return toXML(richiediIUVResponse);
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private static String toXML(RICHIEDIIUVRESPONSE richiediIUVResponse) throws JAXBException {
        return XMLUtils.marshal(richiediIUVResponse, new QName("urn:authPA", "RICHIEDI_IUV_RESPONSE"));
    }

    // PAGAMENTOREQUEST
    private static String toString(PAGAMENTOREQUEST pagamentoRequest) {
        try {
            return toXML(pagamentoRequest);
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private static String toXML(PAGAMENTOREQUEST pagamentoRequest) throws JAXBException {
        return XMLUtils.marshal(pagamentoRequest, new QName("urn:pmpay", "PAGAMENTO_REQUEST"));
    }

    // PAGAMENTORESPONSE
    private static String toString(PAGAMENTORESPONSE pagamentoResponse) {
        try {
            return toXML(pagamentoResponse);
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private static String toXML(PAGAMENTORESPONSE rtRequest) throws JAXBException {
        return XMLUtils.marshal(rtRequest, new QName("urn:pmpay", "PAGAMENTO_RESPONSE"));
    }

    // RICHIEDIRTREQUEST
    private static String toString(RICHIEDIRTREQUEST rtRequest) {
        try {
            return toXML(rtRequest);
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private static String toXML(RICHIEDIRTREQUEST rtRequest) throws JAXBException {
        return XMLUtils.marshal(rtRequest, new QName("urn:pmpay", "RICHIEDI_RT_REQUEST"));
    }

    // RICHIEDIRTREQUEST
    private static String toString(RICHIEDIRTRESPONSE rtRequest) {
        try {
            return toXML(rtRequest);
        } catch (JAXBException ex) {
            Logger.getLogger(PMPayService.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private static String toXML(RICHIEDIRTRESPONSE rtRequest) throws JAXBException {
        return XMLUtils.marshal(rtRequest, new QName("urn:pmpay", "RICHIEDI_RT_RESPONSE"));
    }

}
