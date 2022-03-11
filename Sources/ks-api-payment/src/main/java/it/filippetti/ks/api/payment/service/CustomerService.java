/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.service;

import it.filippetti.ks.api.payment.dto.CreateCustomerDTO;
import it.filippetti.ks.api.payment.dto.CustomerDTO;
import it.filippetti.ks.api.payment.dto.PageDTO;
import it.filippetti.ks.api.payment.dto.UpdateCustomerDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.exception.AuthorizationException;
import it.filippetti.ks.api.payment.exception.NotFoundException;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.mapper.dto.CustomerMapper;
import it.filippetti.ks.api.payment.model.AuthenticationContext;
import it.filippetti.ks.api.payment.model.Customer;
import it.filippetti.ks.api.payment.model.Pager;
import it.filippetti.ks.api.payment.repository.CustomerRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author dino
 */
@Service
public class CustomerService {
    
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CustomerMapper customerMapper;
    
    public Customer create(AuthenticationContext context, CreateCustomerDTO createCustomerDTO)  throws ApplicationException{
        log.info("Request to create Customer " + createCustomerDTO.toString());
        Customer customer = new Customer(
                createCustomerDTO.getCustomerType(),
                createCustomerDTO.getFirstName(),
                createCustomerDTO.getLastName(),
                createCustomerDTO.getAddress(),
                createCustomerDTO.getCity(),
                createCustomerDTO.getZip(),
                createCustomerDTO.getStateCode(),
                createCustomerDTO.getCountryCode(),
                createCustomerDTO.getEmail(),
                createCustomerDTO.getPhone(),
                createCustomerDTO.getOperatorStaff(),
                context.getTenant(),
                context.getOrganization(),
                createCustomerDTO.getFiscalCode(),
                createCustomerDTO.getLangKey(),
                context.getUserId()
        );
        return customerRepository.save(customer);
    }
    
    public CustomerDTO findOne(AuthenticationContext context, Long id) throws ApplicationException{
        log.info("Request to get the Customer with id " + id);
        Customer customer = customerRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), id);
        if(customer == null){
            throw new NotFoundException();
        }
        return customerMapper.map(customer, MappingContext.of(context));
    }
    
    public void delete(AuthenticationContext context, Long id) throws ApplicationException{
        log.info("Request to delete Customer with id " + id);
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        customerRepository.deleteById(id);
    }
    
    public CustomerDTO update(AuthenticationContext context, UpdateCustomerDTO updateCustomerDTO) throws ApplicationException{
        log.info("Request to update Customer " + updateCustomerDTO.toString());
        Customer customer = new Customer(
                updateCustomerDTO.getId(),
                updateCustomerDTO.getCustomerType(),
                updateCustomerDTO.getFirstName(),
                updateCustomerDTO.getLastName(),
                updateCustomerDTO.getAddress(),
                updateCustomerDTO.getCity(),
                updateCustomerDTO.getZip(),
                updateCustomerDTO.getStateCode(),
                updateCustomerDTO.getCountryCode(),
                updateCustomerDTO.getEmail(),
                updateCustomerDTO.getPhone(),
                updateCustomerDTO.getOperatorStaff(),
                context.getTenant(),
                context.getOrganization(),
                updateCustomerDTO.getFiscalCode(),
                updateCustomerDTO.getLangKey(),
                context.getUserId()
        );
        return customerMapper.map(customerRepository.save(customer), MappingContext.of(context));
    }
    
    public PageDTO<CustomerDTO> findAllPaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy) throws ApplicationException{
        log.info("Request to get paged Customer");
        return customerMapper.map(customerRepository.findAllPaginated(
                context, 
                Pager.of(Customer.class, pageNumber, pageSize, orderBy)),
                MappingContext.of(context));
    }
    
    public CustomerDTO findByBpmUserId(AuthenticationContext context)  throws ApplicationException{
        log.info("Request to get customer with bpmUserId " + context.getUserId());
        Optional<Customer> custOptional = customerRepository.findByBpmUserId(context.getUserId());
        if(custOptional.isPresent()){
            return customerMapper.map(custOptional.get(), MappingContext.of(context));
        } else {
            throw new NotFoundException();
        }
    }
    
}
