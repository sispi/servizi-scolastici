/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.service;

import it.filippetti.ks.api.portal.dto.CreateServiceDTO;
import it.filippetti.ks.api.portal.dto.PageDTO;
import it.filippetti.ks.api.portal.dto.ServiceDTO;
import it.filippetti.ks.api.portal.dto.ServiceTreeDTO;
import it.filippetti.ks.api.portal.dto.UpdateServiceDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.exception.AuthorizationException;
import it.filippetti.ks.api.portal.exception.NotFoundException;
import it.filippetti.ks.api.portal.mapper.MappingContext;
import it.filippetti.ks.api.portal.mapper.dto.ServiceMapper;
import it.filippetti.ks.api.portal.model.AuthenticationContext;
import it.filippetti.ks.api.portal.model.Pager;
import it.filippetti.ks.api.portal.repository.ServiceRepository;
import java.util.ArrayList;
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
public class ServiceService {
    private static final Logger log = LoggerFactory.getLogger(ServiceService.class);
    
    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private ServiceMapper serviceMapper;
    
    public PageDTO<ServiceDTO> findAllPaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy)
        throws ApplicationException{
        log.info("Request to get paged Services");
        return serviceMapper.map(serviceRepository.findAll(
                context,
                Pager.of(it.filippetti.ks.api.portal.model.Service.class, pageNumber, pageSize, orderBy)),
                MappingContext.of(context)
        );
    }
    
    public ServiceDTO create(AuthenticationContext context, CreateServiceDTO dto) throws ApplicationException{

        if(!context.isAdmin()){
            throw new AuthorizationException();
        }

        log.info("Request to create Service " + dto.toString());
        it.filippetti.ks.api.portal.model.Service parent = null;
        if(dto.getParentId() != null){
            parent = serviceRepository.findOne(context, dto.getParentId());
        }
        it.filippetti.ks.api.portal.model.Service service = new it.filippetti.ks.api.portal.model.Service(
                parent, dto.getName(), dto.getPosition(), dto.getLogo(),
                dto.getCode(), dto.isValid(), dto.isExternalService(), dto.getLink(), context.getTenant(), context.getOrganization()
        );
        service = serviceRepository.save(service);
        return serviceMapper.map(service, MappingContext.of(context));
    }
    
    public ServiceDTO findOne(AuthenticationContext context, Long serviceId) throws ApplicationException{
        log.info("Request to get Service with id " + serviceId);
        it.filippetti.ks.api.portal.model.Service service = serviceRepository.findOne(context, serviceId);
        if(service == null){
            throw new NotFoundException();
        }
        return serviceMapper.map(service, MappingContext.of(context));
    }
    
    public ServiceDTO update(AuthenticationContext context, UpdateServiceDTO updateServiceDTO) throws ApplicationException{

        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }

        log.info("Request to update Service " + updateServiceDTO.toString());
        it.filippetti.ks.api.portal.model.Service parent = null;
        if(updateServiceDTO.getParentId() != null){
            parent = serviceRepository.findOne(context, updateServiceDTO.getParentId());
        }
        it.filippetti.ks.api.portal.model.Service service = serviceRepository.save(
                new it.filippetti.ks.api.portal.model.Service(
                        updateServiceDTO.getId(), parent, updateServiceDTO.getName(),
                        updateServiceDTO.getPosition(), updateServiceDTO.getLogo(), updateServiceDTO.getCode(),
                        updateServiceDTO.isValid(), updateServiceDTO.isExternalService(), updateServiceDTO.getLink(),
                        context.getTenant(), context.getOrganization()
                )
        );
        return serviceMapper.map(service, MappingContext.of(context));
    }
    
    public void delete(AuthenticationContext context, Long serviceId)throws ApplicationException{
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }

        log.info("Request to delete Service with id " + serviceId);
        serviceRepository.deleteById(serviceId);
    }
    
    public List<ServiceTreeDTO> serviceTree(AuthenticationContext context) throws ApplicationException{
        log.info("Request to get service tree");
        List<ServiceTreeDTO> response = new ArrayList<>();
        List<ServiceDTO> parents = findFirstLevel(context);
        for(ServiceDTO parent : parents){
            ServiceTreeDTO p = convertServiceToTree(parent);
            List<ServiceDTO> childrens = serviceMapper.map(serviceRepository.findAllByParentId(parent.getId()), MappingContext.of(context));
            p.setChildren(convertListToTree(childrens));
            response.add(p);
        }
        return response;
    }
    
    private List<ServiceDTO> findFirstLevel(AuthenticationContext context) throws ApplicationException{
        List<it.filippetti.ks.api.portal.model.Service> services = serviceRepository.findAllByTenantAndOrganizationAndParentIsNull(context.getTenant(), context.getOrganization());
        return serviceMapper.map(services, MappingContext.of(context));
    }
    
    private ServiceTreeDTO convertServiceToTree(ServiceDTO serviceDTO){
        List<ServiceTreeDTO> children = new ArrayList<>();
        ServiceTreeDTO tree = new ServiceTreeDTO();
        tree.setChildren(children);
        tree.setCode(serviceDTO.getCode());
        tree.setExternalService(serviceDTO.isExternalService());
        tree.setId(serviceDTO.getId());
        tree.setLink(serviceDTO.getLink());
        tree.setLogo(serviceDTO.getLogo());
        tree.setName(serviceDTO.getName());
        tree.setPosition(serviceDTO.getPosition());
        tree.setValid(serviceDTO.isValid());
        return tree;
    }
    
    private List<ServiceTreeDTO> convertListToTree(List<ServiceDTO> servicesDTO){
        List<ServiceTreeDTO> servicesTree = new ArrayList<>();
        for(ServiceDTO serviceDTO : servicesDTO){
            ServiceTreeDTO serviceTree = convertServiceToTree(serviceDTO);
            servicesTree.add(serviceTree);
        }
        return servicesTree;
    }
    
}
