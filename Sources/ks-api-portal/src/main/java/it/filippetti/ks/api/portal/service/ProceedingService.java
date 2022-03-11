/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.service;

import it.filippetti.ks.api.portal.dto.CreateProceedingDTO;
import it.filippetti.ks.api.portal.dto.PageDTO;
import it.filippetti.ks.api.portal.dto.ProceedingDTO;
import it.filippetti.ks.api.portal.dto.UpdateProceedingDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.exception.AuthorizationException;
import it.filippetti.ks.api.portal.exception.NotFoundException;
import it.filippetti.ks.api.portal.mapper.MappingContext;
import it.filippetti.ks.api.portal.mapper.dto.ProceedingMapper;
import it.filippetti.ks.api.portal.model.AuthenticationContext;
import it.filippetti.ks.api.portal.model.Pager;
import it.filippetti.ks.api.portal.model.Proceeding;
import it.filippetti.ks.api.portal.repository.ProceedingRepository;
import it.filippetti.ks.api.portal.repository.ServiceRepository;
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
public class ProceedingService {
    private static final Logger log = LoggerFactory.getLogger(ProceedingService.class);
    
    @Autowired
    private ProceedingRepository proceedingRepository;
    
    @Autowired
    private ProceedingMapper proceedingMapper;
    
    @Autowired
    private ServiceRepository serviceRepository;
    
    public PageDTO<ProceedingDTO> findAllPaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy)
        throws ApplicationException{
        log.info("Request to get paged Proceedinds");/*
        return proceedingMapper.map(proceedingRepository.findAll(
                context, 
                Pager.of(Proceeding.class, pageNumber, pageSize, orderBy)),
                MappingContext.of(context));*/
        return proceedingMapper.map(
                proceedingRepository.findAll(context.getTenant(), context.getOrganization(), Pager.of(Proceeding.class, pageNumber, pageSize, orderBy)),
                MappingContext.of(context)
        );
    }
    
    public ProceedingDTO create(AuthenticationContext context, CreateProceedingDTO createProceedingDTO)throws ApplicationException{

        if(!context.isAdmin()){
            throw new AuthorizationException();
        }

        log.info("Request to create Proceeding " + createProceedingDTO.toString());
        it.filippetti.ks.api.portal.model.Service service = serviceRepository.findOne(context, createProceedingDTO.getServiceId());
        
        Proceeding proceeding = new Proceeding();
        proceeding.setAccountableOffice(createProceedingDTO.getAccountableOffice());
        proceeding.setAccountableStaff(createProceedingDTO.getAccountableStaff());
        proceeding.setActiveCommunication(createProceedingDTO.getActiveCommunication());
        proceeding.setApplicantRequirement(createProceedingDTO.getApplicantRequirement());
        proceeding.setAttachments(createProceedingDTO.getAttachments());
        proceeding.setBody(createProceedingDTO.getBody());
        proceeding.setCosts(createProceedingDTO.getCosts());
        proceeding.setEndDate(createProceedingDTO.getEndDate());
        proceeding.setFilingPlan(createProceedingDTO.getFilingPlan());
        proceeding.setHowToSubmit(createProceedingDTO.getHowToSubmit());
        proceeding.setIsActive(createProceedingDTO.getIsActive());
        proceeding.setIsOnline(createProceedingDTO.getIsOnline());
        proceeding.setMultipleInstance(createProceedingDTO.getMultipleInstance());
        proceeding.setOperatorStaff(createProceedingDTO.getOperatorStaff());
        proceeding.setConfigurationId(createProceedingDTO.getConfigurationId());
        proceeding.setService(service);
        proceeding.setStartDate(createProceedingDTO.getStartDate());
        proceeding.setTimeNeeded(createProceedingDTO.getTimeNeeded());
        proceeding.setTitle(createProceedingDTO.getTitle());
        proceeding.setUpdating(createProceedingDTO.getUpdating());
        proceeding.setVersion(createProceedingDTO.getVersion());
        proceeding.setTenant(context.getTenant());
        proceeding.setOrganization(context.getOrganization());
        proceeding.setUo(createProceedingDTO.getUoId());
        proceeding.setCustomTemplate(createProceedingDTO.getCustomTemplate());
        proceeding.setShowCustomTemplate(createProceedingDTO.getShowCustomTemplate());
        proceeding.setUniqueInstance(createProceedingDTO.getUniqueInstance());
        proceeding.setSendIfExpired(createProceedingDTO.getSendIfExpired());
        proceeding.setProcessId(createProceedingDTO.getProcessId());
        proceeding.setSingleInstance(createProceedingDTO.getSingleInstance());
        proceeding = proceedingRepository.save(proceeding);
        return proceedingMapper.map(proceeding, MappingContext.of(context));
    }
    
    public void delete(AuthenticationContext context, Long proceedingId) throws ApplicationException{

        if(!context.isAdmin()){
            throw new AuthorizationException();
        }

        log.info("Request to delete Proceeding with id " + proceedingId);
        proceedingRepository.deleteById(proceedingId);
    }
    
    public ProceedingDTO update(AuthenticationContext context, UpdateProceedingDTO updateProceedingDTO) throws ApplicationException{

        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }

        log.info("Request to update Proceeding " + updateProceedingDTO.toString());
        it.filippetti.ks.api.portal.model.Service service = serviceRepository.findOne(context, updateProceedingDTO.getServiceId());
        
        Proceeding proceeding = proceedingRepository.save(
                new Proceeding(
                        updateProceedingDTO.getId(), service, updateProceedingDTO.getTitle(),
                        updateProceedingDTO.getBody(), updateProceedingDTO.getConfigurationId(), updateProceedingDTO.getProcessId(),
                        updateProceedingDTO.getApplicantRequirement(), updateProceedingDTO.getCosts(),
                        updateProceedingDTO.getAttachments(), updateProceedingDTO.getHowToSubmit(),
                        updateProceedingDTO.getTimeNeeded(), updateProceedingDTO.getAccountableStaff(),
                        updateProceedingDTO.getAccountableOffice(), updateProceedingDTO.getOperatorStaff(),
                        updateProceedingDTO.getIsActive(), updateProceedingDTO.getIsOnline(),
                        updateProceedingDTO.getMultipleInstance(), updateProceedingDTO.getFilingPlan(),
                        updateProceedingDTO.getStartDate(), updateProceedingDTO.getEndDate(),
                        updateProceedingDTO.getUpdating(), updateProceedingDTO.getActiveCommunication(),
                        updateProceedingDTO.getVersion(), context.getTenant(), context.getOrganization(), updateProceedingDTO.getUoId(),
                        updateProceedingDTO.getCustomTemplate(), updateProceedingDTO.getShowCustomTemplate(),
                        updateProceedingDTO.getUniqueInstance(), updateProceedingDTO.getSendIfExpired(),
                        updateProceedingDTO.getSingleInstance()
                )
        );
        return proceedingMapper.map(proceeding, MappingContext.of(context));
    }
    
    public ProceedingDTO findOne(AuthenticationContext context, Long proceedingId) throws ApplicationException{
        log.info("Request to get one Proceeding with id " + proceedingId);
        Proceeding proceeding = proceedingRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), proceedingId);
        if(proceeding == null){
            throw new NotFoundException();
        }
        return proceedingMapper.map(proceeding, MappingContext.of(context));
    }
    
    public List<ProceedingDTO> findAllByService(AuthenticationContext context, Long serviceId) throws ApplicationException{
        log.info("Request to get all Proceeding by serviceId " + serviceId);
        return proceedingMapper.map(proceedingRepository.findAllByTenantAndOrganizationAndServiceId(context.getTenant(), context.getOrganization(), serviceId), MappingContext.of(context));
    }
    
}
