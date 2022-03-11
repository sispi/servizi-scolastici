/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.mapper.dto;

import it.filippetti.ks.api.payment.dto.ProviderDTO;
import it.filippetti.ks.api.payment.mapper.ContextMapper;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.model.Provider;
import org.springframework.stereotype.Component;

/**
 *
 * @author dino
 */
@Component
public class ProviderMapper extends ContextMapper<Provider, ProviderDTO>{
    
    @Override
    protected ProviderDTO doMapping(Provider input, MappingContext context) throws Exception {
        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setId(input.getId());
        
        providerDTO.setName(input.getName());
        providerDTO.setPackageClass(input.getPackageClass());
        providerDTO.setServiceUser(input.getServiceUser());
        providerDTO.setServiceUrl(input.getServiceUrl());
        providerDTO.setWsdlUrl(input.getWsdlUrl());
        providerDTO.setLogo(input.getLogo());
        return providerDTO;
    }
    
    
    public Provider toEntity(ProviderDTO providerDTO, MappingContext context) throws Exception {
        
        return new Provider (
            providerDTO.getId(),
            providerDTO.getName(),
            providerDTO.getLogo(),
            providerDTO.getPackageClass(),
            providerDTO.getServiceUser(),
            providerDTO.getServiceUrl(),
            providerDTO.getWsdlUrl()
        );
        
    }
    
}