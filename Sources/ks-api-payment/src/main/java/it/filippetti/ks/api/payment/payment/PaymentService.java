package it.filippetti.ks.api.payment.payment;

import it.filippetti.ks.api.payment.service.PaymentInstanceService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 * interfaccia astratta per servizi di pagamento<BR>
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jun 2018
 */
public interface PaymentService {
    
    static final org.slf4j.Logger log = LoggerFactory.getLogger(PaymentInstanceService.class);
    
    /**
     * Do a payment
     *
     * @param request
     * @return the response, with non null error code if failed or null on success
     * @throws PaymentException
     */
    public PaymentResponse payment(PaymentRequest request) throws PaymentException;

    /**
     * Verify a payment
     *
     * @param request
     * @return the response, with non null error code if failed or null on success
     * @throws PaymentException
     */
    public String verify(PaymentRequest request) throws PaymentException;

    /**
     * Get the receipt of a payment
     *
     * @param request
     * @return
     * @throws it.pianetacloud.idea.web.payment.PaymentException
     */
    public ReceiptResponse getReceipt(ReceiptRequest request)  throws PaymentException;

    /**
     * Factory method.<BR>
     * Ask for an instance of a specific payment service.<BR>
     * The method look for a class named <pre>"it.pianetacloud.idea.web.payment.PaymentService" + paymentService</pre><BR>
     *
     * @param paymentService Payment service name; examples: "CPay", "PMPay".
     * @return
     * @throws it.pianetacloud.idea.web.payment.PaymentServiceException
     */
    static public PaymentService getInstance(String paymentService) throws PaymentServiceException {
        try {
            String service = "it.filippetti.ks.api.payment.payment.PaymentService" + paymentService;
            log.info("PaymentService getInstance with service class {}", service);
            Class<PaymentService> ps = (Class<PaymentService>) Class.forName(service);
            return ps.newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            Logger.getLogger(PaymentService.class.getName()).log(Level.SEVERE, null, ex);
            throw new PaymentServiceException("getInstance: " + ex.getLocalizedMessage());
        }
    }

}
