/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.filippetti.ks.api.payment.dto.ConfigurationDTO;
import it.filippetti.ks.api.payment.dto.CreatePaymentInstanceDTO;
import it.filippetti.ks.api.payment.dto.CreateTransactionDTO;
import it.filippetti.ks.api.payment.dto.CustomerDTO;
import it.filippetti.ks.api.payment.dto.PageDTO;
import it.filippetti.ks.api.payment.dto.PaymentInstanceDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.exception.AuthorizationException;
import it.filippetti.ks.api.payment.exception.BusinessError;
import it.filippetti.ks.api.payment.exception.BusinessException;
import it.filippetti.ks.api.payment.exception.NotFoundException;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.mapper.dto.CustomerMapper;
import it.filippetti.ks.api.payment.mapper.dto.PaymentInstanceMapper;
import it.filippetti.ks.api.payment.model.*;
import it.filippetti.ks.api.payment.payment.*;
import it.filippetti.ks.api.payment.repository.ChannelRepository;
import it.filippetti.ks.api.payment.repository.ConfigurationRepository;
import it.filippetti.ks.api.payment.repository.PaymentInstanceRepository;
import it.filippetti.ks.api.payment.utilities.JsonUtil;
import it.filippetti.ks.api.payment.utilities.PrintUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author dino
 */
@Service
public class PaymentInstanceService {

    @Value("${application.baseUrl}")
    private String appBaseUrl;

    @Value("${application.portalAPIBaseUrl}")
    private String portalAPIBaseUrl;
    
    @Value( "${application.ksUtilsAPIBaseUrl}")
    private String ksUtilsAPIBaseUrl;
    
    @Value( "${application.docerAPIBaseUrl}")
    private String docerAPIBaseUrl;

    @Value("${application.expiryTimeInHours}")
    private Integer expiryTimeInHours;

    private static final Logger log = LoggerFactory.getLogger(PaymentInstanceService.class);
    
    @Autowired
    private PaymentInstanceRepository paymentRepository;
    
    @Autowired
    private ChannelRepository channelRepository;
    
    @Autowired
    private PaymentInstanceMapper paymentMapper;
    
    @Autowired
    private ConfigurationService configurationService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ConfigurationRepository configurationRepository;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {

        restTemplate = new RestTemplate();

        // remove xml message converter from rest template
        // https://stackoverflow.com/questions/53486032/avoid-jackson-xmlmapper-being-used-as-default-objectmapper-with-spring
        restTemplate.getMessageConverters().removeIf(c -> 
                (c instanceof MappingJackson2XmlHttpMessageConverter));
        
    }
    
    /**
     * 
     * @param context
     * @param createPaymentDTO
     * @return
     * @throws ApplicationException
     * @throws Exception 
     */
    public PaymentInstanceDTO create(AuthenticationContext context, CreatePaymentInstanceDTO createPaymentDTO)  throws ApplicationException, Exception {
        log.info("Request to create Payment " + createPaymentDTO.toString());

        Customer customer = customerService.create(
                context, customerMapper.toCustomerDTO(createPaymentDTO.getCustomer(), MappingContext.of(context)));

        String iuv = "", requestCode = "", paymentType = "";
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(expiryTimeInHours);
        PaymentInstance payment = new PaymentInstance(
                createPaymentDTO.getReferenceClientId(),
                createPaymentDTO.getReferenceProcessId(),
                createPaymentDTO.getReferenceInstanceId(),
                context.getTenant(),
                context.getOrganization(),
                iuv,
                createPaymentDTO.getPaymentServiceSpecificId(),
                requestCode,
                createPaymentDTO.getCurrencyCode(),
                createPaymentDTO.getPaymentService(),
                createPaymentDTO.getCausal(),
                java.sql.Timestamp.valueOf(LocalDateTime.now()),
                java.sql.Timestamp.valueOf(expiryDate),
                null,
                paymentType,
                createPaymentDTO.getTotalAmount(),
                createPaymentDTO.getNetAmount(),
                Outcome.READY.toString(),
                null, "", "", null, null, "", "", null,
                customer,
                null,
                createPaymentDTO.getReferenceUserId(),
                createPaymentDTO.getCallbackRedirect(),
                createPaymentDTO.getCallbackStatus(),
                UUID.randomUUID().toString(),
                Outcome.READY,
                ""
        );

        PaymentInstance paymentInstance = paymentRepository.save(payment);
        return paymentMapper.map(paymentInstance, MappingContext.of(context));
    }
    
    
    /**
     * 
     * @param context
     * @param id
     * @return
     * @throws ApplicationException 
     */
    public PaymentInstanceDTO findOne(AuthenticationContext context, Long id) throws ApplicationException{
        log.info("Request to get the Payment with id " + id);
        PaymentInstance payment = paymentRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), id);
        if (payment == null) {
            throw new NotFoundException();
        }
        return paymentMapper.map(payment, MappingContext.of(context));
    }
    
    
    /**
    * 
    * @param context
    * @param uuid
    * @throws ApplicationException 
    */
    public void delete(AuthenticationContext context, String uuid) throws ApplicationException{
        log.info("Request to delete PaymentInstance with uuid {}", uuid);
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        
        PaymentInstance paymentInstance = findOneByUuid(context, uuid);
        updateToCancel(paymentInstance);        
    }
    
    /**
     * 
     * @param context
     * @param uuid
     * @return
     * @throws ApplicationException 
     */
    public PaymentInstance findOneByUuid(AuthenticationContext context, String uuid) throws ApplicationException{
        log.info("Request to get the Payment with uuid {} ", uuid);
        PaymentInstance payment = paymentRepository.findByTenantAndOrganizationAndUuid(context.getTenant(), context.getOrganization(), uuid);
        if (payment == null) {
            throw new NotFoundException();
        }
        return payment;
    }
    
    
    /**
     * 
     * @param context
     * @param uuid
     * @return
     * @throws ApplicationException
     * @throws IOException 
     */
    public String xmlToJson(AuthenticationContext context, String uuid) throws ApplicationException, IOException{
        log.info("Request to get the Payment with uuid {} ", uuid);
        PaymentInstance payment = findOneByUuid(context, uuid);
        if (payment == null) {
            throw new NotFoundException();
        }
        return JsonUtil.xmlToJson(new String(payment.getReceipt()));
    }
    
    
    /**
     * 
     * @param context
     * @param uuid
     * @return
     * @throws ApplicationException
     * @throws IOException 
     */
    public Map<String, Object> mapFromJson(AuthenticationContext context, String uuid) throws ApplicationException, IOException{
        String json = xmlToJson(context, uuid);
        Map<String, Object> map = new ObjectMapper().readValue(json, HashMap.class);
        return map;
    }
    
    
    /**
     * @param context
     * @return the total number of payments made by tenant and organization
     * @throws ApplicationException 
     */
    public Integer count(AuthenticationContext context) throws ApplicationException{
        log.info("Request to get the number of Payments");
        return paymentRepository
                .countByTenantAndOrganization(context.getTenant(), context.getOrganization());
    }
    
    
    /**
     * @param context
     * @return the total total amount of payments made by tenant and organization
     * @throws ApplicationException 
     */
    public Double totalAmount(AuthenticationContext context) throws ApplicationException{
        log.info("Request to get the total amount of Payments");
        return paymentRepository
                .sumTotalAmountByTenantAndOrganization(context.getTenant(), context.getOrganization());
    }
    
    
    /**
     * @param context
     * @param startDate
     * @param endDate
     * @return the total total amount of payments made by tenant and organization
     * @throws ApplicationException 
     */
    public List<Object[]> totalAmountByMonth(AuthenticationContext context, Date startDate, Date endDate) throws ApplicationException{
        log.info("Request to get the total amount of Payments by month from {} to {}", startDate, endDate);
        return paymentRepository
                .getTotalsByMonth(context.getTenant(), context.getOrganization(), startDate, endDate);
    }
    
    
    /**
     * 
     * @param context
     * @param uuid
     * @return
     * @throws ApplicationException
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException 
     */
    public Object getDocumentUrl(AuthenticationContext context, String uuid) throws ApplicationException, IOException, URISyntaxException, InterruptedException{
        log.info("Get PDF document");
        JSONObject json = new JSONObject(xmlToJson(context, uuid));
        String html = PrintUtil.generateHtmlReceipt(json);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("AUTHORIZATION", context.getJwtToken());
        headers.add("Content-Type", "application/json");
        RestTemplate restTemplate = new RestTemplate();
        
        Html convertRequest = new Html();
        convertRequest.setFooterHtmlText("");
        convertRequest.setHeaderHtmlText("");
        convertRequest.setHtmlText(html);
        HttpEntity<Object> request = new HttpEntity<>(convertRequest, headers);
        String url = ksUtilsAPIBaseUrl + "/bl/convert";
        log.info("Get PDF document headers {}, url {} ", headers, url);
        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(url, request, Object.class);
            if(response.hasBody()){
                Map<String, Object> map = new HashMap<>();
                if(response.getBody() instanceof Map){
                    map.putAll((Map)response.getBody());
                    JSONObject jsonResponse = new JSONObject(map);
                    String fileUrl = jsonResponse.getJSONObject("file").getString("uri");
                    int lastIndex = fileUrl.lastIndexOf("/") + 1;
                    log.info("VEDI: "+ fileUrl.substring(lastIndex));
                    String downloadUrl = docerAPIBaseUrl + "/files/upload/convert/" + fileUrl.substring(lastIndex);
                    Map<String, Object> body = new HashMap<>();
                    body.put("url", downloadUrl);
                    return body;
                } else {
                    throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR, "Object cannot be cast into Map.");
                }
            } else {
                return response;
            }
        } catch (BusinessException | JSONException | RestClientException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR, "General error calling HTML to PDF service.");
        }
    }
    
    
    /**
     * 
     * @param context
     * @param uuid
     * @return
     * @throws ApplicationException 
     */
    public PaymentInstanceDTO findOneCompleteByUuid(AuthenticationContext context, String uuid) throws ApplicationException{
        log.info("Request to get the Payment with uuid " + uuid);
        PaymentInstance payment = paymentRepository.findByTenantAndOrganizationAndUuid(context.getTenant(), context.getOrganization(), uuid);
        if (payment == null) {
            throw new NotFoundException();
        }
        return paymentMapper.map(payment, MappingContext.of(context));
    }
    
    
    /**
     * 
     * @param context
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @return
     * @throws ApplicationException 
     */
    public PageDTO<PaymentInstanceDTO> findAllPaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy) throws ApplicationException{
        log.info("Request to get paged Payments");
        return paymentMapper.map(paymentRepository.findAllPaginated(context, 
                Pager.of(PaymentInstance.class, pageNumber, pageSize, orderBy)),
                MappingContext.of(context));
    }
    
    
    /**
     * 
     * @param context
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @return
     * @throws ApplicationException 
     */
    public PageDTO<PaymentInstanceDTO> findAllByUserIdPaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy) throws ApplicationException{
        log.info("Request to get a paged list of a user's Payments");
        return paymentMapper.map(paymentRepository.findAllByUserIdPaginated(context, 
                Pager.of(PaymentInstance.class, pageNumber, pageSize, orderBy)),
                MappingContext.of(context));
    }
    
    
    /**
     * 
     * @param context
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param referenceInstanceId
     * @param referenceProcessId
     * @param referenceClientId
     * @return
     * @throws ApplicationException 
     */
    public PageDTO<PaymentInstanceDTO> findAllByReferencePaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy, String referenceInstanceId, String referenceProcessId, String referenceClientId) throws ApplicationException{
        log.info("Request to obtain a list of a user's payment pages related to a case");
        return paymentMapper.map(paymentRepository.findAllByReferencePaginated(context,
                Pager.of(PaymentInstance.class, pageNumber, pageSize, orderBy), referenceInstanceId, referenceProcessId, referenceClientId),
                MappingContext.of(context));
    }
    
    /**
     * Executes the payment
     * @param request
     * @param context
     * @param paymentUuid
     * @param channelId
     * @return
     * @throws ApplicationException 
     */
    public String executePayment(HttpServletRequest request, AuthenticationContext context, String paymentUuid, Long channelId) throws ApplicationException {
        log.info("Request to execute payment with uuid {}", paymentUuid);
        StringBuffer requestURL = request.getRequestURL();
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String baseUrl = requestURL.substring(0, requestURL.length() - uri.length() + ctx.length());
        log.info("old baseUrl='" + baseUrl + "'");
        log.info("new appBaseUrl='" + appBaseUrl + "'");

        try {
            PaymentInstance pi = findOneByUuid(context, paymentUuid);
            
            LocalDateTime expiryDate = LocalDateTime.now().plusHours(expiryTimeInHours); 

            Channel channel = channelRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), channelId);
            pi.setChannel(channel);
            pi.setExpiryDate(java.sql.Timestamp.valueOf(expiryDate));
            pi.setTransactionResult(Outcome.WAITING.toString());
            pi.setOutcome(Outcome.WAITING);

            paymentRepository.save(pi);

            PaymentRequest paymentRequest = generatePaymentRequest(appBaseUrl, context, pi);

            // Update shopId for transactionPayment
            /* Long shopIdIncrease = 1000000L;
            transactions.forEach(transaction -> {
                String shopId = String.valueOf(shopIdIncrease + transaction.getId());
                transaction.setShopCode(shopId);
                transaction.setResponseDate(new DateTime().toDate());
                //transactionRepository.save(transaction);
            });
            */
            return executePayment(
                    paymentRequest, 
                    context, 
                    transactionService.findAllByPaymentId(context, pi.getId()), 
                    channel.getProvider());
            
        } catch (Exception e) {
            throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR, "General error during the execution of the payment.");
        }   
    }
    
    
    /**
     * 
     * @param context
     * @param id
     * @param timestamp
     * @param reference
     * @return
     * @throws ApplicationException 
     */
    public PaymentInstance updateToOk(
            AuthenticationContext context, String id, String timestamp, String reference) throws ApplicationException {
        log.info("Request to update payment with uuid {} to OK status", id);
        // Pagamento completato ma ricevuta non ancora arrivata
        try {
            PaymentInstance pi = paymentRepository
                    .findByTenantAndOrganizationAndId(
                            context.getTenant(),
                            context.getOrganization(),
                            Long.parseLong(id));

            pi.setProcessingDate(new Date(Long.parseLong(timestamp)));
            pi.setPaymentSystemReference(reference);

            return updateToOk(pi);

        } catch (ApplicationException | NumberFormatException e) {
            throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR, "Error updating payment to OK status.");
        }
    }


    /**
     * 
     * @param paymentInstance
     * @return
     * @throws ApplicationException 
     */
    public PaymentInstance updateToOk(PaymentInstance paymentInstance) throws ApplicationException {
        log.info("Request to update payment with uuid {} to OK status", paymentInstance);
        // Pagamento completato ma ricevuta non ancora arrivata
        try {

            // Verifica se già in uno stato successivo
            if(paymentInstance.getTransactionResult().equals(Outcome.RECEIPT.toString())) {
                return paymentInstance;
            }
            
            paymentInstance.setOutcome(Outcome.COMPLETED);
            paymentInstance.setTransactionResult(Outcome.COMPLETED.toString());
            paymentInstance = paymentRepository.save(paymentInstance);

            transactionService.updateLatestTransactionByPaymentInstance(paymentInstance);

            return paymentInstance;
        } catch (ApplicationException e) {
            throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR, "Error updating payment to OK status.");
        }
    }


    /**
     * 
     * @param context
     * @param id
     * @param timestamp
     * @param reference
     * @return
     * @throws ApplicationException 
     */
    public PaymentInstance updateToKo(
            AuthenticationContext context, String id, String timestamp, String reference) throws ApplicationException {
        log.info("Request to update payment with uuid {} to KO status", id);
        try {
            PaymentInstance pi = paymentRepository
                    .findByTenantAndOrganizationAndId(
                            context.getTenant(),
                            context.getOrganization(),
                            Long.parseLong(id));

            pi.setProcessingDate(new Date(Long.parseLong(timestamp)));
            pi.setPaymentSystemReference(reference);

            return updateToKo(pi);

        } catch (NumberFormatException e) {
            throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR, "Error updating payment to KO status.");
        }
    }

    
    /**
     * 
     * @param paymentInstance
     * @return
     * @throws ApplicationException 
     */
    public PaymentInstance updateToKo(PaymentInstance paymentInstance) throws ApplicationException {
        log.info("Request to update payment with uuid {} to KO status", paymentInstance);
        try {

            // Verifica se già in uno stato successivo
            if(paymentInstance.getTransactionResult().equals(Outcome.RECEIPT.toString())) {
                return paymentInstance;
            }
            
            paymentInstance.setOutcome(Outcome.FAILED);
            paymentInstance.setTransactionResult(Outcome.FAILED.toString());
            paymentInstance = paymentRepository.save(paymentInstance);

            transactionService.updateLatestTransactionByPaymentInstance(paymentInstance);

            return paymentInstance;
        } catch (ApplicationException e) {
            throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR, "Error updating payment to KO status.");
        }
    }

    
    /**
     * 
     * @param context
     * @param paymentUuid
     * @return
     * @throws ApplicationException 
     */
    public PaymentInstance updateToCancel(
            AuthenticationContext context, String paymentUuid) throws ApplicationException {
        log.info("Request to update payment with uuid {} to Cancel status", paymentUuid);
        try {
            PaymentInstance pi = paymentRepository
                    .findByTenantAndOrganizationAndUuid(
                            context.getTenant(),
                            context.getOrganization(),
                            paymentUuid);

            return updateToCancel(pi);
        } catch (NumberFormatException e) {
            throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR, "Error updating payment to Cancel status.");
        }
    }


    /**
     * 
     * @param paymentInstance
     * @return
     * @throws ApplicationException 
     */
    public PaymentInstance updateToCancel(PaymentInstance paymentInstance) throws ApplicationException {
        log.info("Request to update payment with uuid {} to Cancel status", paymentInstance);
        try {
            // Non posso cancellare se ormai è andata!
            if( paymentInstance.getTransactionResult().equals(Outcome.RECEIPT.toString()) || 
                paymentInstance.getTransactionResult().equals(Outcome.COMPLETED.toString())) {
                    return paymentInstance;
            }
                        
            paymentInstance.setOutcome(Outcome.CANCELED);
            paymentInstance.setTransactionResult(Outcome.CANCELED.toString());
            paymentInstance = paymentRepository.save(paymentInstance);

            transactionService.updateLatestTransactionByPaymentInstance(paymentInstance);

            return paymentInstance;
        } catch (ApplicationException e) {
            throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR, "Error updating payment to Cancel status.");
        }
    }


    /**
     * 
     * @param paymentInstance
     * @return
     * @throws ApplicationException 
     */
    public PaymentInstance updateToWaiting(PaymentInstance paymentInstance) throws ApplicationException {
        log.info("Request to update payment with uuid {} to Waiting status", paymentInstance);
        try {
            // Non posso metter in waiting se ormai è andata!
            if( paymentInstance.getTransactionResult().equals(Outcome.RECEIPT.toString()) || 
                paymentInstance.getTransactionResult().equals(Outcome.COMPLETED.toString())) {
                    return paymentInstance;
            }
            paymentInstance.setOutcome(Outcome.WAITING);
            paymentInstance.setTransactionResult(Outcome.WAITING.toString());

            LocalDateTime expiryDate = LocalDateTime.now().plusHours(expiryTimeInHours);
            paymentInstance.setExpiryDate(java.sql.Timestamp.valueOf(expiryDate));

            paymentInstance = paymentRepository.save(paymentInstance);

            transactionService.updateLatestTransactionByPaymentInstance(paymentInstance);

            return paymentInstance;
        } catch (ApplicationException e) {
            throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR, "Error updating payment to Waiting status.");
        }
    }

    
    /**
     * 
     * @param paymentInstance
     * @return
     * @throws ApplicationException 
     */
    public PaymentInstance updateToReceiptOk(PaymentInstance paymentInstance) throws ApplicationException {
        log.info("Request to update payment with uuid {} to Receipt OK status", paymentInstance);
        // Pagamento completato ma ricevuta non ancora arrivata
        try {
            paymentInstance.setOutcome(Outcome.RECEIPT);
            paymentInstance.setTransactionResult(Outcome.RECEIPT.toString());
            paymentInstance = paymentRepository.save(paymentInstance);

            transactionService.updateLatestTransactionByPaymentInstance(paymentInstance);

            // Make S2S call
            callbackStatus(paymentInstance);

            return paymentInstance;
        } catch (ApplicationException e) {
            throw new BusinessException(BusinessError.SERVICE_BUSINESS_ERROR, "Error updating payment to Receipt OK status.");
        }
    }


    /**
     *
     * @param paymentInstance
     * @return
     */
    public PaymentInstance getReceipt(PaymentInstance paymentInstance) {
        log.debug("getReceipt for payment: " + paymentInstance.getUuid());

        Date requestDate = paymentInstance.getProcessingDate();
        Configuration configuration =
            configurationRepository.findByChannelIdAndDefaultConfTrue(paymentInstance.getChannel().getId());

        ReceiptRequest ricevutaRequest = new ReceiptRequest();
        ricevutaRequest.setCompanyId(configuration.getSecretKey());
        ricevutaRequest.setRequestDate(requestDate);
        ricevutaRequest.setUsername(configuration.getSecretUser());
        ricevutaRequest.setRequestId(paymentInstance.getRequestCode());
        ricevutaRequest.setUniquePaymentId(paymentInstance.getIuv());
        ricevutaRequest.setPassword(configuration.getServicePassword());
        ricevutaRequest.setInternalReference(paymentInstance.getId().toString());

        log.debug("ricevutaRequest bean: {}", ricevutaRequest.toString());

        try {
            PaymentService paymentService = PaymentService.getInstance("PMPay");
            ReceiptResponse ricevutaResponse = paymentService.getReceipt(ricevutaRequest);
            paymentInstance.setReceipt(Base64.decodeBase64(ricevutaResponse.getStream()));
            paymentInstance.setReceiptContentType("application/xml");

            if (paymentInstance.getReceipt() != null && paymentInstance.getReceipt().length != 0){
                paymentInstance.setTransactionResult(Outcome.RECEIPT.toString());
                paymentInstance.setOutcome(Outcome.RECEIPT);

                updateToReceiptOk(paymentInstance);
            } else {
                if (ricevutaResponse.getErrorCode() != null) {
                    // paymentInstance.setTransactionResult (Outcome.FAILED.toString());
                    // paymentInstance.setOutcome(Outcome.FAILED);
                    
                    log.debug("ricevutaResponse error {} - {}", ricevutaResponse.getErrorCode(), ricevutaResponse.getErrorDescription());
                    // updateToKo(paymentInstance);
                }
            }

            // paymentRepository.save(paymentInstance);

        } catch (PaymentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return paymentInstance;
    }

    
    /**
     *
     * @param requestId
     * @param reference
     * @param outcome
     * @throws Exception
     */
    public void manageS2S(String requestId, String reference, String outcome) throws Exception {
        log.info("Start S2S service with params requestId: {} reference: {} outcome: {}", requestId, reference, outcome);

        paymentRepository.findById(Long.parseLong(requestId)).ifPresent(
            paymentInstance -> {
                if( outcome != null && 
                    paymentInstance.getIuv().equals(reference) && 
                    !paymentInstance.getTransactionResult().equals(Outcome.RECEIPT.toString())) {
                    
                    try {
                        if(outcome.equals("CANCELLED")) {
                            // 0: transazione cancellata dall'utente - CANCELLED
                            updateToCancel(paymentInstance);
                        }
                        if(outcome.equals("OK")) {
                            // 1: transazione andata a buon fine - OK
                            updateToOk(paymentInstance);
                        }
                        if(outcome.equals("ERROR")) {
                            // 2: transazione NON andata a buon fine - ERROR
                            updateToKo(paymentInstance);
                        }
                        if(outcome.equals("PENDING")) {
                            // 3: transazione in corso - PENDING
                            updateToWaiting(paymentInstance);
                        }
                        if(outcome.equals("WAIT")) {
                            // 3: transazione in corso - WAIT
                            updateToWaiting(paymentInstance);
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    // La callback viene eseguita in tutti i casi, quindi in caso di errore viene eseguita con paymentInstance allo stato precedente.
                    callbackStatus(paymentInstance);
                }
            }
        );
    }


    // @Scheduled(fixedRateString = "${cron.paymentInstanceService.scan.executionfixedrate}")
    @Scheduled(fixedDelay = 30000)
    public void scan() {
        log.info("Start PaymentInstance cron");
        List<PaymentInstance> paymentInstanceList =
                paymentRepository.findAllByTransactionResultAndReceiptNull(Outcome.COMPLETED.toString());
        for (PaymentInstance paymentInstance : paymentInstanceList) {
            try {
                getReceipt(paymentInstance);
            } catch (Exception e) {
                // e.printStackTrace();
                log.error("Cron - Impossibile scaricare la ricevuta del pagamento: " + paymentInstance.getUuid());
            }
        }
        log.info("Stop PaymentInstance cron");
    }
    
    
    @Scheduled(fixedDelay = 30000)
    public void scanPaymentsWaiting() {
        log.info("Start of PaymentInstance scanning with waiting status");
        List<PaymentInstance> paymentInstanceList =
                paymentRepository.findAllByTransactionResult(Outcome.WAITING.toString());
        for (PaymentInstance paymentInstance : paymentInstanceList) {
            Date date = new Date();
            long difference = date.getTime() - paymentInstance.getExpiryDate().getTime();
            // long diffMinutes = difference / (60 * 1000);
            // long diffHours = difference / (60 * 60 * 1000);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);
            if(minutes > 5){
                paymentInstance.setTransactionResult(Outcome.READY.toString());
                paymentInstance.setOutcome(Outcome.READY);
                paymentRepository.save(paymentInstance);
            }
        }
        log.info("end of the PaymentInstance scan with waiting status");
    }    
    
    
    private PaymentRequest generatePaymentRequest(String baseUrl, AuthenticationContext context, PaymentInstance paymentInstance) throws Exception {
        log.info("Generating PaymentRequest...");
        DateTime dateTime = new DateTime();
        CustomerDTO customerDTO = customerService.findOne(context, paymentInstance.getCustomer().getId());
        ConfigurationDTO configurationDTO = configurationService.findDefaultChannelConfiguration(context, paymentInstance.getChannel().getId());
        
        CreateTransactionDTO transaction = new CreateTransactionDTO();
        transaction.setOrganization("");
        transaction.setOutcome(Outcome.WAITING);
        transaction.setRating(0);
        transaction.setRequestDate(dateTime.toDate());
        transaction.setShopCode("");
        transaction.setUserId(paymentInstance.getReferenceUserId());
        transaction.setPaymentId(paymentInstance.getId());
        transactionService.create(context, transaction);
        
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setBaseUrl(baseUrl);
        paymentRequest.setUsername(configurationDTO.getSecretUser());
        paymentRequest.setPassword(configurationDTO.getServicePassword());
        paymentRequest.setRequestDate(dateTime.toDate());
        paymentRequest.setCompanyId(configurationDTO.getSecretKey());
        paymentRequest.setUrlOk(baseUrl + "/payment/ok");
        paymentRequest.setUrlKo(baseUrl + "/payment/ko");
        paymentRequest.setUrlCancel(baseUrl + "/payment/cancel/" + paymentInstance.getUuid());
        paymentRequest.setUrlS2S(baseUrl + "/paymentInstances/s2s");
        
        paymentRequest.setPaymentDate(null);
        paymentRequest.setCurrency(paymentInstance.getCurrencyCode());

        //TODO: check this
        paymentRequest.setReferenceId(Long.parseLong(paymentInstance.getReferenceInstanceId()));
        paymentRequest.setRatePayer(customerDTO);
        paymentRequest.setBackUrl(baseUrl + "/payment/backUrl");
        
        paymentRequest.setTotalAmount(BigDecimal.valueOf(paymentInstance.getTotalAmount()));
        paymentRequest.setInternalReference("");
        /*
        paymentRequest.setFiscalCode(customerDTO.getFiscalCode());
        paymentRequest.setClientType(customerDTO.getCustomerType());
        paymentRequest.setFirstName(customerDTO.getFirstName());
        paymentRequest.setLastName(customerDTO.getLastName()); */
        paymentRequest.setPaymentReason(paymentInstance.getCausal());
        paymentRequest.setPaymentService(paymentInstance.getPaymentService());
        paymentRequest.setPaymentInstance(paymentInstance);
        return paymentRequest;
    }
    
    
    private void callbackStatus (PaymentInstance paymentInstance) {
        String resourceUrl = String.format("%s%s?tenant=%s&organization=%s&paymentId=%s",
                portalAPIBaseUrl,
                paymentInstance.getCallbackStatus(),
                paymentInstance.getTenant(),
                paymentInstance.getOrganization(),
                paymentInstance.getUuid());
        log.info("Request to update payment to Receipt OK resourceUrl = {} ", resourceUrl);

        ResponseEntity<String> response
                = restTemplate.getForEntity(resourceUrl, String.class);
        log.info("Request to update payment to Receipt OK ResponseEntity = {} ", response);

    }
    
    
    private String executePayment(PaymentRequest paymentRequest, AuthenticationContext context, List<Transaction> transactions, Provider provider) {
        log.info("Executing Payment...");
        try {
            PaymentService paymentService = PaymentService.getInstance(provider.getPackageClass());
            PaymentResponse paymentResponse = paymentService.payment(paymentRequest);
            
            //Update the PaymentInstance
            if(paymentResponse.getErrorCode() == null){
                //update iuv,codicePosizione,codiceRichiesta,dataElaborazione for all payments
                transactions.forEach(transaction -> {
                    try {
                        transaction.setPaymentService(paymentResponse.getUniquePaymentId());
                        
                        // Long shopIdIncrease = 1000000L; //TODO: check
                        Date now = new DateTime().toDate();
                        if(transaction.getShopCode() == null)
                            transaction.setShopCode(paymentResponse.getUniquePaymentId());
                            // transaction.setShopCode(String.valueOf(shopIdIncrease + transaction.getId()));

                        if(transaction.getResponseDate() == null)
                            transaction.setResponseDate(now);
                        
                        transactionService.update(context, transaction);
                        
                        PaymentInstance paymentInstance = transaction.getPaymentInstance();
                        paymentInstance.setIuv(paymentResponse.getUniquePaymentId());
                        // paymentInstance.setOutcome(Outcome.COMPLETED); // Si potrebbe mettere anche qui il giusto outcome?
                        paymentInstance.setProcessingDate(now);
                        paymentInstance.setRequestCode(paymentResponse.getRequestId());
                        
                        paymentRepository.save(paymentInstance);
                    } catch (ApplicationException ex) {
                        java.util.logging.Logger.getLogger(PaymentInstanceService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            } else {
                new Exception();
            }
            return paymentResponse.getUrlToCall();
        } catch(PaymentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
