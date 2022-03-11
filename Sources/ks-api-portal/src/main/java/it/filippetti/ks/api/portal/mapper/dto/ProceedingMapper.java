/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.mapper.dto;

import it.filippetti.ks.api.portal.dto.ProceedingDTO;
import it.filippetti.ks.api.portal.mapper.ContextMapper;
import it.filippetti.ks.api.portal.mapper.MappingContext;
import it.filippetti.ks.api.portal.model.Proceeding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dino
 */
@Component
public class ProceedingMapper extends ContextMapper<Proceeding, ProceedingDTO>{
    
    @Autowired
    private ServiceMapper serviceMapper;

    @Override
    protected ProceedingDTO doMapping(Proceeding input, MappingContext context) throws Exception {
        ProceedingDTO proceedingDTO = new ProceedingDTO();
        proceedingDTO.setTitle(input.getTitle());
        proceedingDTO.setAccountableOffice(input.getAccountableOffice());
        proceedingDTO.setAccountableStaff(input.getAccountableStaff());
        proceedingDTO.setActiveCommunication(input.getActiveCommunication());
        proceedingDTO.setApplicantRequirement(input.getApplicantRequirement());
        proceedingDTO.setAttachments(input.getAttachments());
        proceedingDTO.setBody(input.getBody());
        proceedingDTO.setCosts(input.getCosts());
        proceedingDTO.setEndDate(input.getEndDate());
        proceedingDTO.setFilingPlan(input.getFilingPlan());
        proceedingDTO.setHowToSubmit(input.getHowToSubmit());
        proceedingDTO.setId(input.getId());
        proceedingDTO.setIsActive(input.getIsActive());
        proceedingDTO.setIsOnline(input.getIsOnline());
        proceedingDTO.setMultipleInstance(input.getMultipleInstance());
        proceedingDTO.setOperatorStaff(input.getOperatorStaff());
        proceedingDTO.setConfigurationId(input.getConfigurationId());
        proceedingDTO.setService(serviceMapper.map(input.getService(), context));
        proceedingDTO.setStartDate(input.getStartDate());
        proceedingDTO.setTimeNeeded(input.getTimeNeeded());
        proceedingDTO.setUpdating(input.getUpdating());
        proceedingDTO.setVersion(input.getVersion());
        proceedingDTO.setUo(input.getUo());
        proceedingDTO.setCustomTemplate(input.getCustomTemplate());
        proceedingDTO.setShowCustomTemplate(input.getShowCustomTemplate());
        proceedingDTO.setUniqueInstance(input.getUniqueInstance());
        proceedingDTO.setSendIfExpired(input.getSendIfExpired());
        proceedingDTO.setProcessId(input.getProcessId());
        proceedingDTO.setSingleInstance(input.getSingleInstance());
        return proceedingDTO;
    }
    
}
