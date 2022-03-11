/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.mapper.dto;

import it.filippetti.ks.api.payment.dto.CreateCustomerDTO;
import it.filippetti.ks.api.payment.dto.CustomerDTO;
import it.filippetti.ks.api.payment.mapper.ContextMapper;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.model.Customer;
import org.springframework.stereotype.Component;

/**
 *
 * @author dino
 */
@Component
public class CustomerMapper extends ContextMapper<Customer, CustomerDTO>{
    
    @Override
    protected CustomerDTO doMapping(Customer input, MappingContext context) throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(input.getId());
        customerDTO.setCustomerType(input.getCustomerType());
        customerDTO.setFirstName(input.getFirstName());
        customerDTO.setLastName(input.getLastName());
        customerDTO.setAddress(input.getAddress());
        customerDTO.setCity(input.getCity());
        customerDTO.setZip(input.getZip());
        customerDTO.setStateCode(input.getStateCode());
        customerDTO.setCountryCode(input.getCountryCode());
        customerDTO.setEmail(input.getEmail());
        customerDTO.setPhone(input.getPhone());
        customerDTO.setZip(input.getZip());
        customerDTO.setOperatorStaff(input.getOperatorStaff());
        customerDTO.setFiscalCode(input.getFiscalCode());
        customerDTO.setLangKey(input.getLangKey());
        
        return customerDTO;

    }
    
    public Customer toEntity(CustomerDTO customerDTO, MappingContext context) throws Exception {
        return new Customer (
            customerDTO.getId(),
            customerDTO.getCustomerType(),
            customerDTO.getFirstName(),
            customerDTO.getLastName(),
            customerDTO.getAddress(),
            customerDTO.getCity(),
            customerDTO.getZip(),
            customerDTO.getStateCode(),
            customerDTO.getCountryCode(),
            customerDTO.getEmail(),
            customerDTO.getPhone(),
            customerDTO.getOperatorStaff(),
            context.authentication().getTenant(),
            context.authentication().getOrganization(),
            customerDTO.getFiscalCode(),
            customerDTO.getLangKey(),
            ""
        );
    }
    
    public Customer toEntity(CreateCustomerDTO customerDTO, MappingContext context) throws Exception {
        return new Customer (
            customerDTO.getCustomerType(),
            customerDTO.getFirstName(),
            customerDTO.getLastName(),
            customerDTO.getAddress(),
            customerDTO.getCity(),
            customerDTO.getZip(),
            customerDTO.getStateCode(),
            customerDTO.getCountryCode(),
            customerDTO.getEmail(),
            customerDTO.getPhone(),
            customerDTO.getOperatorStaff(),
            context.authentication().getTenant(),
            context.authentication().getOrganization(),
            customerDTO.getFiscalCode(),
            customerDTO.getLangKey(),
            ""
        );
    }
    
    
    public CreateCustomerDTO toCustomerDTO(CustomerDTO customerDTO, MappingContext context) throws Exception {
        CreateCustomerDTO cdto = new CreateCustomerDTO();
        
        cdto.setAddress(customerDTO.getAddress());
        cdto.setCity(customerDTO.getCity());
        cdto.setCountryCode(customerDTO.getCountryCode());
        cdto.setCustomerType(customerDTO.getCustomerType());
        cdto.setEmail(customerDTO.getEmail());
        cdto.setFirstName(customerDTO.getFirstName());
        cdto.setFiscalCode(customerDTO.getFiscalCode());
        cdto.setLangKey(customerDTO.getLangKey());
        cdto.setLastName(customerDTO.getLastName());
        cdto.setOperatorStaff(customerDTO.getOperatorStaff());
        cdto.setPhone(customerDTO.getPhone());
        cdto.setStateCode(customerDTO.getStateCode());
        cdto.setZip(customerDTO.getZip());
        
        return cdto;
    }
    
   
}
