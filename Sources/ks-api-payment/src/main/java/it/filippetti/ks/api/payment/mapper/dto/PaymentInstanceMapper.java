/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.mapper.dto;

import it.filippetti.ks.api.payment.dto.PaymentInstanceDTO;
import it.filippetti.ks.api.payment.mapper.ContextMapper;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.model.PaymentInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dino
 */
@Component
public class PaymentInstanceMapper  extends ContextMapper<PaymentInstance, PaymentInstanceDTO>{
    
    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    protected PaymentInstanceDTO doMapping(PaymentInstance input, MappingContext context) throws Exception {
        PaymentInstanceDTO paymentDTO = new PaymentInstanceDTO();
        paymentDTO.setId(input.getId());
        paymentDTO.setReferenceClientId(input.getReferenceClientId());
        paymentDTO.setReferenceProcessId(input.getReferenceProcessId());
        paymentDTO.setReferenceInstanceId(input.getReferenceInstanceId());
        paymentDTO.setReferenceUserId(input.getReferenceUserId());
        paymentDTO.setIuv(input.getIuv());
        paymentDTO.setPaymentServiceSpecificId(input.getPaymentServiceSpecificId());
        paymentDTO.setRequestCode(input.getRequestCode());
        paymentDTO.setCurrencyCode(input.getCurrencyCode());
        paymentDTO.setPaymentType(input.getPaymentType());
        paymentDTO.setPaymentService(input.getPaymentService());
        paymentDTO.setCausal(input.getCausal());
        paymentDTO.setCreationDate(input.getCreationDate());
        paymentDTO.setExpiryDate(input.getExpiryDate());
        paymentDTO.setProcessingDate(input.getProcessingDate());
        paymentDTO.setPaymentType(input.getPaymentType());
        paymentDTO.setTotalAmount(input.getTotalAmount());
        paymentDTO.setNetAmount(input.getNetAmount());
        paymentDTO.setTransactionResult(input.getTransactionResult());
        paymentDTO.setReceipt(input.getReceipt());
        paymentDTO.setReceiptContentType(input.getReceiptContentType());
        paymentDTO.setProtocolNumber(input.getProtocolNumber());
        paymentDTO.setProtocolYear(input.getProtocolYear());
        paymentDTO.setCustomReceipt(input.getCustomReceipt());
        paymentDTO.setCustomReceiptContentType(input.getCustomReceiptContentType());
        paymentDTO.setInvoiceNumber(input.getInvoiceNumber());
        paymentDTO.setInvoiceYear(input.getInvoiceYear());
        paymentDTO.setCustomReceipt(input.getCustomReceipt());
        paymentDTO.setCustomer(customerMapper.map(input.getCustomer(), context));
        paymentDTO.setCallbackRedirect(input.getCallbackRedirect());
        paymentDTO.setCallbackStatus(input.getCallbackStatus());
        paymentDTO.setUuid(input.getUuid());
        paymentDTO.setPaymentSystemReference(input.getPaymentSystemReference());
        return paymentDTO;
    }
}