package it.filippetti.ks.api.payment.payment;

import it.filippetti.ks.api.payment.model.PaymentInstance;
import static it.filippetti.ks.api.payment.payment.PayParam.Service.*;

/**
 *
 * @author Raffaele Dell'Aversana
 * @since 14 Jul 2018
 */
public class ReceiptRequest extends LoginRequest {
    private String internalReference;
    private String uniquePaymentId;
    private String receiptFormat;

    private PaymentInstance levy;
    private String paymentResult;

    @PayParam(service = PMPAY, param="rifInterno")
    public String getInternalReference() {
        return internalReference;
    }

    @PayParam(service = PMPAY, param="rifInterno")
    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
    }

    @PayParam(service = PMPAY, param="IUV")
    @PayParam(service = CPAY, param="I", note="idPortale?")
    public String getUniquePaymentId() {
        return uniquePaymentId;
    }

    @PayParam(service = PMPAY, param="IUV")
    @PayParam(service = CPAY, param="I", note="idPortale?")
    public void setUniquePaymentId(String uniquePaymentId) {
        this.uniquePaymentId = uniquePaymentId;
    }

    @PayParam(service = CPAY, param="F")
    public String getReceiptFormat() {
        return receiptFormat;
    }

    @PayParam(service = CPAY, param="F")
    public void setReceiptFormat(String receiptFormat) {
        this.receiptFormat = receiptFormat;
    }

    @PayParam(service = BNL, param="paymentResult")
    public String getPaymentResult() {
        return paymentResult;
    }

    @PayParam(service = BNL, param="paymentResult")
    public void setPaymentResult(String paymentResult) {
        this.paymentResult = paymentResult;
    }

    @PayParam(service = BNL, param="levy")
    public PaymentInstance getLevy() {
        return levy;
    }

    @PayParam(service = BNL, param="levy")
    public void setLevy(PaymentInstance levy) {
        this.levy = levy;
    }

    @Override
    public String toString() {
        return "ReceiptRequest{"
                + "internalReference='" + getInternalReference() + '\''
                + ", uniquePaymentId='" + getUniquePaymentId() + '\''
                + ", companyId='" + getCompanyId()+ '\''
                + ", username='" + getUsername()+ '\''
                + ", requestId='" + getRequestId()+ '\''
                + ", password='" + getPassword()+ '\''
                + ", requestDate='" + getRequestDate()+ '\''
                + ", receiptFormat='" + getReceiptFormat()+ '\''
                + '}';
    }
}
