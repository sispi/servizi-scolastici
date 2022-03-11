/*
 * (c) 2018 Raffaele Dell'Aversana
 */
package it.filippetti.ks.api.payment.bnl;

import it.filippetti.ks.api.payment.bnl.pay.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.BindingProvider;

public class BnlService {

    /* factories degli object da usare per i servizi */
    private static final ObjectFactory payFactory;

    /* logger, per disabilitarlo vedere il blocco static di seguito */
    private static final Logger LOG;

    /* url reali dei servizi, non quelli del wsdl */
    private static final String payServiceUrl;

    /* endpoint dei ws */
    private static final PaymentInitGateway payPAService;
    public static final String PAGOPA = "pagopa.";
    public static final String PAGOPA_URL = "serviceUrl";
    private static final String TERMINAL_ID="terminalId";
    private static final String LANG_ID="langId";

    private static final String TRANSACTION_TYPE="PURCHASE";


    /* inizializzazioni varie */
    static {
        LOG = Logger.getLogger(BnlService.class.getName());

        payFactory = new ObjectFactory();
        /*
        propertyResolver = new RelaxedPropertyResolver(ApplicationContextHolder
            .getEnvironment(), PAGOPA);
        */
        //INSERIRE QUI LA BASE URL
        String BASE_URL = "BASE_URL";  //propertyResolver.getProperty(PAGOPA_URL);

        payServiceUrl = BASE_URL+"BNL_CG_SERVICES/services/PaymentInitGatewayPort";

        PaymentInitGateway_Service payService = new PaymentInitGateway_Service();
        payPAService = payService.getPaymentInitGatewayPort();
        ((BindingProvider) payPAService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, payServiceUrl);
    }

    /**
     * effettua un pagamento
     *
     * @param request parametri per il pagamento
     *
     *
     */
    public static PagamentoResponse initPayment(PagamentoRequest request)
        throws BnlException {

        PagamentoResponse pagamentoResponse=new PagamentoResponse();

        LOG.log(Level.INFO, "Creating payment init request ...");
        PaymentInitRequest paymentInitRequest=createPaymentInitRequest(request);

        LOG.log(Level.INFO, "Executing payment init request ...");
        PaymentInitResponse paymentInitResponse=executePaymentInit(paymentInitRequest);

        GregorianCalendar c = new GregorianCalendar();
        try {
            pagamentoResponse.setDataElaborazione(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            pagamentoResponse.setDataElaborazione(null);
        }

        if(paymentInitResponse.isError()){
            final String error = "Error in paymentInitResponse. Error desc: " + paymentInitResponse.getErrorDesc()+" - RC: " + paymentInitResponse.getRc();
            LOG.log(Level.SEVERE, error);
            pagamentoResponse.setCodiceErrore(paymentInitResponse.getRc());
            pagamentoResponse.setDescrizioneErrore(paymentInitResponse.getErrorDesc());
        }else{
            LOG.log(Level.INFO, "No error in paymentInitResponse.");
            pagamentoResponse.setPaymentID(paymentInitResponse.getPaymentID());
            pagamentoResponse.setRedirectURL(paymentInitResponse.getRedirectURL());
        }

        LOG.log(Level.INFO, pagamentoResponse.toString());
        return pagamentoResponse;

    }

    private static String substrOverSize(String string,int length){
        if(string.length()>length){
            return string.substring(0,length);
        }else return string;
    }

    private static String[] setAdditionalInfo(String reason){

        //var templateText = "Pagamento di {{pagamento.selectedproceeding.description}};
        // {{pagamento.selectedproceeding.code}};
        // {{pagamento.selectedtipologia.description}};
        // {{pagamento.selectedquantita.description}} quantità;
        // Costo unitario: {{pagamento.selectedproceeding.price}} euro";

        String[] addInfo=new String[5];
        String[] partsReason = reason.split(";");
        String description="Descrizione: "+partsReason[0];//always
        String code="Codice: "+partsReason[1];//always
        String type="Tipologia: "+partsReason[2].trim();//it could be empty
        String quantity=partsReason[3];//always
        String singlePrice=partsReason[4];//always

        addInfo[0]=substrOverSize(description,256);
        addInfo[1]=substrOverSize(code,256);
        addInfo[2]=substrOverSize(type,256);
        addInfo[3]=quantity;
        addInfo[4]=singlePrice;

        return addInfo;
    }



    public static PaymentInitRequest createPaymentInitRequest(PagamentoRequest request) throws BnlException{

        PaymentInitRequest paymentInitRequest=payFactory.createPaymentInitRequest();

        paymentInitRequest.setShopID(request.getShopID());//required - Chiave esterna identificante il pagamento.
        //paymentInitRequest.setTid(propertyResolver.getProperty(TERMINAL_ID));//required - Codice terminale dell'esercente.
        paymentInitRequest.setTid("08000001");
        paymentInitRequest.setTrType(TRANSACTION_TYPE);//required - Tipologia di una richiesta.
        paymentInitRequest.setAmount(request.getAmount());//required
        paymentInitRequest.setCurrencyCode(Currency.fromValue(request.getCurrency()));//required

        Language language=null;
        String customerLangKey=request.getLangKey();
        String langId= "IT";//propertyResolver.getProperty(LANG_ID);
        if(Language.fromValue(customerLangKey.toUpperCase())!=null){
            language=Language.fromValue(customerLangKey.toUpperCase());
        }else{
            language=Language.fromValue(langId);
        }
        paymentInitRequest.setLangID(language);//required - Lingua relativa alla pagina di inserimento dei dati sensibili associati ad una richiesta.

        paymentInitRequest.setNotifyURL(request.getNotifyUrl());//required - URL relativo alla pagina di notifica esito di una richiesta.
        paymentInitRequest.setErrorURL(request.getErrorUrl());//required - URL relativo alla pagina di errore associato ad una richiesta.

        paymentInitRequest.setShopUserRef(request.getShopUserRef()); // Identificativo cliente.
        paymentInitRequest.setShopUserName(request.getShopUserName()); //Cognome e Nome del cliente (separati dal carattere “,” es. rossi,mario )
        paymentInitRequest.setShopUserAccount(request.getShopUserAccount());


        String reason=request.getDescription();

        String[] additionalInfo=setAdditionalInfo(reason);

        //check and set description
        reason=substrOverSize(reason,100);
        paymentInitRequest.setDescription(reason);//Causale di pagamento

        paymentInitRequest.setPayInstr(null);
        paymentInitRequest.setApiVersion(null);
        paymentInitRequest.setMerID(null);
        paymentInitRequest.setReqTime(null);
        paymentInitRequest.setShopUserMobilePhone(null);
        paymentInitRequest.setShopUserIMEI(null);
        paymentInitRequest.setCallbackURL(null);
        paymentInitRequest.setAddInfo1(additionalInfo[0]);//description - Campo a disposizione dell'esercente.
        paymentInitRequest.setAddInfo2(additionalInfo[1]);//code - Campo a disposizione dell'esercente.
        paymentInitRequest.setAddInfo3(additionalInfo[2]);//type - Campo a disposizione dell'esercente.
        paymentInitRequest.setAddInfo4(additionalInfo[3]);//quantity - Campo a disposizione dell'esercente.
        paymentInitRequest.setAddInfo5(additionalInfo[4]);//singlePrice - Campo a disposizione dell'esercente.
       /*
        paymentInitRequest.setAddInfo1(null);//description - Campo a disposizione dell'esercente.
        paymentInitRequest.setAddInfo2(null);//code - Campo a disposizione dell'esercente.
        paymentInitRequest.setAddInfo3(null);//type - Campo a disposizione dell'esercente.
        paymentInitRequest.setAddInfo4(null);//quantity - Campo a disposizione dell'esercente.
        paymentInitRequest.setAddInfo5(null);//singlePrice - Campo a disposizione dell'esercente.*/
        paymentInitRequest.setPayInstrToken(null);//Token dello strumento di pagamento.
        paymentInitRequest.setRegenPayInstrToken(null);
        paymentInitRequest.setKeepOnRegenPayInstrToken(null);
        paymentInitRequest.setPayInstrTokenExpire(null);
        paymentInitRequest.setPayInstrTokenUsageLimit(null);
        paymentInitRequest.setPayInstrTokenAlg(null);
        paymentInitRequest.setPayInstrTokenAsTopUpID(null);
        paymentInitRequest.setBillingID(null);
        paymentInitRequest.setAccountName(null);//Cognome e Nome del titolare (separati dal carattere "," es. rossi,mario ) per il prefill dei rispettivi campi.
        paymentInitRequest.setLevel3Info(null);
        paymentInitRequest.setMandateInfo(null);
        paymentInitRequest.setPaymentReason(null);//Testo libero associato alla funzionalità Insegna Dinamica
        paymentInitRequest.setFreeText(null);
        paymentInitRequest.setTopUpID(null);
        paymentInitRequest.setFirstTopUp(null);
        paymentInitRequest.setValidityExpire(null);
        paymentInitRequest.setMinExpireMonth(null);
        paymentInitRequest.setMinExpireYear(null);

        String signature= null;
        try {
            signature = getSHA256Signature(request.getSignatureKey(),prepareInitSignature(paymentInitRequest));
        } catch (Exception e) {
            e.printStackTrace();
            signature="";
        }
        paymentInitRequest.setSignature(signature);//required

        LOG.log(Level.INFO, "Print PaymentInitRequest "+ paymentInitRequest);
        return  paymentInitRequest;
    }


    public static PaymentInitResponse executePaymentInit(PaymentInitRequest paymentInitRequest) throws BnlException{

        Init initRequest=payFactory.createInit();
        initRequest.setRequest(paymentInitRequest);

        InitResponse initResponse= payPAService.init(initRequest);
        PaymentInitResponse paymentInitResponse=initResponse.getResponse();

        LOG.log(Level.INFO, "Print PaymentInitResponse "+ paymentInitResponse.toString());
        return paymentInitResponse;
    }


    public static PagamentoResponse verifyPayment(PagamentoRequest request)
        throws BnlException {

        PagamentoResponse pagamentoResponse=new PagamentoResponse();

        LOG.log(Level.INFO, "Creating payment verify request ...");
        PaymentVerifyRequest paymentVerifyRequest=createPaymentVerifyRequest(request);

        LOG.log(Level.INFO, "Executing payment verify request ...");
        PaymentVerifyResponse paymentVerifyResponse=executePaymentVerify(paymentVerifyRequest);

        GregorianCalendar c = new GregorianCalendar();
        try {
            pagamentoResponse.setDataElaborazione(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
            pagamentoResponse.setDataElaborazione(null);
        }

        if(paymentVerifyResponse.isError()){
            final String error = "Error in paymentVerifyResponse. Error desc: " + paymentVerifyResponse.getErrorDesc()+" - RC: " + paymentVerifyResponse.getRc();
            LOG.log(Level.SEVERE, error);
            pagamentoResponse.setCodiceErrore(paymentVerifyResponse.getRc());
            pagamentoResponse.setDescrizioneErrore(paymentVerifyResponse.getErrorDesc());
        }else{
            LOG.log(Level.INFO, "No error in paymentVerifyResponse.");
            pagamentoResponse.setCodiceErrore(paymentVerifyResponse.getRc());
            pagamentoResponse.setDescrizioneErrore(paymentVerifyResponse.getErrorDesc());
        }

        LOG.log(Level.INFO, pagamentoResponse.toString());
        return pagamentoResponse;
    }


    public static PaymentVerifyRequest createPaymentVerifyRequest(PagamentoRequest request) throws BnlException{

        PaymentVerifyRequest paymentVerifyRequest=payFactory.createPaymentVerifyRequest();
        //paymentVerifyRequest.setTid(propertyResolver.getProperty(TERMINAL_ID));//required
        paymentVerifyRequest.setTid("TID");
        paymentVerifyRequest.setShopID(request.getShopID());//required
        paymentVerifyRequest.setPaymentID(request.getIuv());//required

        String signature= null;
        try {
            signature = getSHA256Signature(request.getSignatureKey(),prepareVerifySignature(paymentVerifyRequest));
        } catch (Exception e) {
            e.printStackTrace();
            signature="";
        }
        paymentVerifyRequest.setSignature(signature);//required

        LOG.log(Level.INFO, "Print PaymentVerifyRequest "+ paymentVerifyRequest);
        return  paymentVerifyRequest;
    }

    public static List<Object> prepareVerifySignature(PaymentVerifyRequest paymentVerifyRequest){

        List list=new ArrayList();
        list.add(paymentVerifyRequest.getTid());//list.add(new String("08000001"));//tid
        list.add(paymentVerifyRequest.getShopID());//list.add(new String("00005"));//shopID
        list.add(paymentVerifyRequest.getPaymentID());//list.add(null);//paymentID
        return list;
    }

    public static PaymentVerifyResponse executePaymentVerify(PaymentVerifyRequest paymentVerifyRequest) throws BnlException{

        Verify verifyRequest=payFactory.createVerify();
        verifyRequest.setRequest(paymentVerifyRequest);

        VerifyResponse verifyResponse= payPAService.verify(verifyRequest);
        PaymentVerifyResponse paymentVerifyResponse=verifyResponse.getResponse();

        LOG.log(Level.INFO, "Print PaymentVerifyResponse "+ paymentVerifyResponse.toString());
        return paymentVerifyResponse;
    }

    public static RicevutaResponse getRicevuta(RicevutaRequest request) throws BnlException {
        return new RicevutaResponse(null);
    }

    public static List<Object> prepareInitSignature(PaymentInitRequest paymentInitRequest){

        List list=new ArrayList();
        list.add(paymentInitRequest.getTid());//list.add(new String("08000001"));//tid
        list.add(paymentInitRequest.getShopID());//list.add(new String("00005"));//shopID
        list.add(paymentInitRequest.getShopUserRef());//list.add(null);//shopUserRef
        list.add(paymentInitRequest.getShopUserName());//list.add(null);//shopUserName
        list.add(paymentInitRequest.getShopUserAccount());//list.add(null);//shopUserAccount
        list.add(paymentInitRequest.getTrType());//list.add(new String("PURCHASE"));//trType
        list.add(paymentInitRequest.getAmount());//list.add(new String("100"));//amount
        list.add(paymentInitRequest.getCurrencyCode());//list.add(new String("EUR"));//currencyCode
        list.add(paymentInitRequest.getLangID());//list.add(new String("IT"));//langID
        list.add(paymentInitRequest.getNotifyURL());//list.add(new String("http://example.it/success"));//notifyURL
        list.add(paymentInitRequest.getErrorURL());//list.add(new String("http://example.it/error"));//errorURL
        list.add(paymentInitRequest.getCallbackURL());//list.add(new String("http://example.it/callbackURL"));//callbackURL
        list.add(paymentInitRequest.getAddInfo1());//list.add(null);//addInfo1
        list.add(paymentInitRequest.getAddInfo2());//list.add(null);//addInfo2
        list.add(paymentInitRequest.getAddInfo3());//list.add(null);//addInfo3
        list.add(paymentInitRequest.getAddInfo4());//list.add(null);//addInfo4
        list.add(paymentInitRequest.getAddInfo5());//list.add(null);//addInfo5
        list.add(paymentInitRequest.getAccountName());//list.add(null);//accountName
        list.add(null);//list.add(null);//description
        list.add(paymentInitRequest.getPayInstrToken());//list.add(null);//payInstrToken
        return list;
    }

    //calcolo della signature attraverso algoritmo SHA256
    //key = chiave segreta
    //fields= parametri del messaggio
    public static String getSHA256Signature(String key, List<Object> fields) throws Exception{

        StringBuilder sbuilder=new StringBuilder();
        for(Object field:fields){
            if(field!=null){
                sbuilder.append(field.toString());
            }
        }

        byte data[]=sbuilder.toString().getBytes();

        String alg="HmacSHA256";
        SecretKeySpec sk1=new SecretKeySpec(key.getBytes(),alg);
        Mac mac= Mac.getInstance(alg);
        mac.init(sk1);

        byte sig[]=mac.doFinal(data);

        String sigEncoded= Base64.getEncoder().encodeToString(sig);

        return sigEncoded;
    }


}
