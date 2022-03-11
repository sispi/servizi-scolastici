/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.mapper.dto;

import it.filippetti.ks.api.payment.dto.TransactionDTO;
import it.filippetti.ks.api.payment.mapper.ContextMapper;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dino
 */
@Component
public class TransactionMapper extends ContextMapper<Transaction, TransactionDTO>{
    
    @Autowired
    private PaymentInstanceMapper paymentMapper;
    
    @Override
    protected TransactionDTO doMapping(Transaction input, MappingContext context) throws Exception {
        TransactionDTO TransactionDTO = new TransactionDTO();
        TransactionDTO.setId(input.getId());

        TransactionDTO.setTenant(input.getTenant());
        TransactionDTO.setOrganization(input.getOrganization());
        TransactionDTO.setShopCode(input.getShopCode());
        TransactionDTO.setRequestDate(input.getRequestDate());
        TransactionDTO.setResponseDate(input.getResponseDate());
        TransactionDTO.setPaymentService(input.getPaymentService());
        
        TransactionDTO.setRating(input.getRating());
        TransactionDTO.setUserId(input.getUserId());
        TransactionDTO.setPaymentInstance(paymentMapper.map(input.getPaymentInstance(), context));
        return TransactionDTO;
    }
    
}