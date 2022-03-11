/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.mapper.dto;

import it.filippetti.ks.api.portal.dto.ServiceDTO;
import it.filippetti.ks.api.portal.mapper.ContextMapper;
import it.filippetti.ks.api.portal.mapper.MappingContext;
import it.filippetti.ks.api.portal.model.Service;
import org.springframework.stereotype.Component;

/**
 *
 * @author dino
 */
@Component
public class ServiceMapper extends ContextMapper<Service, ServiceDTO>{

    @Override
    protected ServiceDTO doMapping(Service input, MappingContext context) throws Exception {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(input.getId());
        serviceDTO.setName(input.getName());
        serviceDTO.setCode(input.getCode());
        serviceDTO.setExternalService(input.isExternalService());
        serviceDTO.setValid(input.isValid());
        if(input.getParent() != null){
            serviceDTO.setParent(doMapping(input.getParent(), context));
        } else {
            serviceDTO.setParent(null);
        }
        serviceDTO.setPosition(input.getPosition());
        serviceDTO.setLogo(input.getLogo());
        serviceDTO.setLink(input.getLink());
        return serviceDTO;
    }
    
    
}
