/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.service;

import it.filippetti.ks.api.payment.dto.CreateExampleDTO;
import it.filippetti.ks.api.payment.dto.ExampleDTO;
import it.filippetti.ks.api.payment.dto.PageDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.exception.AuthorizationException;
import it.filippetti.ks.api.payment.exception.NotFoundException;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.mapper.dto.ExampleMapper;
import it.filippetti.ks.api.payment.model.AuthenticationContext;
import it.filippetti.ks.api.payment.model.Example;
import it.filippetti.ks.api.payment.model.Fetcher;
import it.filippetti.ks.api.payment.model.Pager;
import it.filippetti.ks.api.payment.repository.ExampleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class ExampleService {
    
    private static final Logger log = LoggerFactory.getLogger(ExampleService.class);
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private ValidationService validationService;

    @Autowired
    private OperationService operationService;
    
    @Autowired
    private ExampleRepository exampleRepository;

    @Autowired
    private ExampleMapper exampleMapper;
    
    /**
     * 
     * @param context
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public ExampleDTO createExample(
        AuthenticationContext context, CreateExampleDTO dto) 
        throws ApplicationException {
        
        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        // validate dto
        validationService.validate(dto);
        
        return operationService.execute(dto.getOperationId(), () -> {
    
            Example example;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                 new DefaultTransactionDefinition());
            try {
                // create and store
                example = exampleRepository.save(new Example(
                    context.getTenant(),
                    context.getOrganization(), 
                    dto.getName(), 
                    null,
                    dto.getChildren()));
            } catch (Throwable t) {
                // rollback
                transactionManager.rollback(tx);
                throw t;
            }

            // commit
            transactionManager.commit(tx);

            // map and return
            return exampleMapper.map(
                example, 
                MappingContext.of(context));
        }, 
        ExampleDTO.class);        
    }
    
    /**
     * 
     * @param context
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */
    public PageDTO<ExampleDTO> getExamples(
        AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy, String fetch) 
        throws ApplicationException {

        return exampleMapper.map(
            exampleRepository.findAll(
                context, 
                Pager.of(Example.class, pageNumber, pageSize, orderBy)), 
            MappingContext.of(context, Fetcher.of(Example.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param exampleId
     * @param fetch
     * @return
     * @throws ApplicationException 
     */
    public ExampleDTO getExample(AuthenticationContext context, Long exampleId, String fetch) 
        throws ApplicationException {
        
        Example example;

        example = exampleRepository.findById(context, exampleId);
        if (example == null) {
            throw new NotFoundException();
        }
        
        return exampleMapper.map(
            example, 
            MappingContext.of(context, Fetcher.of(Example.class, fetch)));
    }
}
