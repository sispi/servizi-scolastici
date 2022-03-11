/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.service;

import it.filippetti.ks.api.payment.dto.CreateTransactionDTO;
import it.filippetti.ks.api.payment.dto.PageDTO;
import it.filippetti.ks.api.payment.dto.TransactionDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.exception.NotFoundException;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.mapper.dto.TransactionMapper;
import it.filippetti.ks.api.payment.model.*;
import it.filippetti.ks.api.payment.repository.PaymentInstanceRepository;
import it.filippetti.ks.api.payment.repository.TransactionRepository;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author dino
 */
@Service
public class TransactionService {
    
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private PaymentInstanceRepository paymentRepository;
    
    @Autowired
    private TransactionMapper transactionMapper;
    
    public TransactionDTO create(AuthenticationContext context, CreateTransactionDTO createTransactionDTO) throws ApplicationException{
        log.info("Resquest to create Transaction " + createTransactionDTO.toString());
        PaymentInstance paymentInstance = paymentRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), createTransactionDTO.getPaymentId());
        
        Transaction transaction = new Transaction(
            createTransactionDTO.getOutcome(),
            context.getTenant(),
            context.getOrganization(),
            createTransactionDTO.getShopCode(),
            createTransactionDTO.getRequestDate(),
            createTransactionDTO.getResponseDate(),
            createTransactionDTO.getPaymentService(),
            createTransactionDTO.getRating(),
            paymentInstance.getReferenceUserId(),
            paymentInstance
        );
        
        return transactionMapper.map(transactionRepository.save(transaction), MappingContext.of(context));
    }
    
    public TransactionDTO findOne(AuthenticationContext context, Long id) throws ApplicationException{
        log.info("Request to get the Transaction with id " + id);
        Transaction transaction = transactionRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), id);
        if (transaction == null) {
            throw new NotFoundException();
        }
        return transactionMapper.map(transaction, MappingContext.of(context));
    }
    
    public PageDTO<TransactionDTO> findAllPaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy) throws ApplicationException{
        log.info("Request to get paged Transactions");
        return transactionMapper.map(transactionRepository.findAllPaginated(
                context, 
                Pager.of(Transaction.class, pageNumber, pageSize, orderBy)),
                MappingContext.of(context));
    }
    
    
    
    public TransactionDTO update(AuthenticationContext context, Transaction transaction) throws ApplicationException{
        log.info("Request to update Transaction " + transaction.toString());
        
        Transaction t = transactionRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), transaction.getId());
        if (t == null) {
            throw new NotFoundException();
        }
                
        return transactionMapper.map(transactionRepository.save(transaction), MappingContext.of(context));
    }
    
    
    public List<Transaction> findAllByPaymentId(AuthenticationContext context, Long paymentId) throws ApplicationException{
        log.info("Request to get Transactions by Payment");
        
        return transactionRepository.findAllByTenantAndOrganizationAndPaymentInstanceId(
                context.getTenant(), context.getOrganization(), paymentId);
    }
    
    
    public void updateLatestTransactionByPaymentInstance(PaymentInstance paymentInstance) throws ApplicationException {

        Transaction transaction = new Transaction(
                paymentInstance.getOutcome(), // outcome
                paymentInstance.getTenant(), // tenant
                paymentInstance.getOrganization(), // organization
                paymentInstance.getIuv(), // shopCode
                paymentInstance.getProcessingDate(), // requestDate
                new Date(), // responseDate
                paymentInstance.getPaymentService(), // paymentService
                0, // rating
                paymentInstance.getReferenceUserId(), // userId
                paymentInstance // paymentInstance 
        );

        transactionRepository.save(transaction);
    }
}
