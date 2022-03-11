/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.service;

import it.filippetti.ks.api.payment.dto.CreateProviderDTO;
import it.filippetti.ks.api.payment.dto.PageDTO;
import it.filippetti.ks.api.payment.dto.ProviderDTO;
import it.filippetti.ks.api.payment.dto.UpdateProviderDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.exception.AuthorizationException;
import it.filippetti.ks.api.payment.exception.NotFoundException;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.mapper.dto.ProviderMapper;
import it.filippetti.ks.api.payment.model.AuthenticationContext;
import it.filippetti.ks.api.payment.model.Pager;
import it.filippetti.ks.api.payment.model.Provider;
import it.filippetti.ks.api.payment.repository.ProviderRepository;
import java.util.List;
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
public class ProviderService {
    
    private static final Logger log = LoggerFactory.getLogger(ProviderService.class);
    
    @Autowired
    private ProviderRepository providerRepository;
    
    @Autowired
    private ProviderMapper providerMapper;
    
    public ProviderDTO create(AuthenticationContext context, CreateProviderDTO createProviderDTO) throws ApplicationException{
        log.info("Request to create Provider " + createProviderDTO.toString());
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        Provider provider = new Provider(
                createProviderDTO.getName(),
                createProviderDTO.getLogo(),
                createProviderDTO.getPackageClass(),
                createProviderDTO.getServiceUser(),
                createProviderDTO.getServiceUrl(),
                createProviderDTO.getWsdlUrl()
        );
        return providerMapper.map(providerRepository.save(provider), MappingContext.of(context));
    }
    
    public ProviderDTO findOne(AuthenticationContext context, Long id) throws ApplicationException{
        log.info("Request to get the Provider with id " + id);
        Optional<Provider> providerOp = providerRepository.findById(id);
        if (!providerOp.isPresent()) {
            throw new NotFoundException();
        }
        return providerMapper.map(providerOp.get(), MappingContext.of(context));
    }
    
    public void delete(AuthenticationContext context, Long id) throws ApplicationException{
        log.info("Request to delete Provider with id " + id);
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        providerRepository.deleteById(id);
    }
    
    public ProviderDTO update(AuthenticationContext context, UpdateProviderDTO updateProviderDTO, Long id) throws ApplicationException{
        log.info("Request to update Provider " + updateProviderDTO.toString());
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        Optional<Provider> pOp = providerRepository.findById(id);
        if (!pOp.isPresent()) {
            throw new NotFoundException();
        }
        Provider provider = new Provider(
                id,
                updateProviderDTO.getName(),
                updateProviderDTO.getLogo(),
                updateProviderDTO.getPackageClass(),
                updateProviderDTO.getServiceUser(),
                updateProviderDTO.getServiceUrl(),
                updateProviderDTO.getWsdlUrl()
        );
        return providerMapper.map(providerRepository.save(provider), MappingContext.of(context));
    }
    
    public PageDTO<ProviderDTO> findAllPaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy) throws ApplicationException{
        log.info("Request to get paged Providers");
        return providerMapper.map(
                providerRepository.findAll(Pager.of(Provider.class, pageNumber, pageSize, orderBy)),
                MappingContext.of(context)
        );
    }
    
    public List<ProviderDTO> findAll(AuthenticationContext context) throws ApplicationException{
        log.info("Request to get all Providers");
        return providerMapper.map(
                providerRepository.findAll(),
                MappingContext.of(context)
        );
    }
}
