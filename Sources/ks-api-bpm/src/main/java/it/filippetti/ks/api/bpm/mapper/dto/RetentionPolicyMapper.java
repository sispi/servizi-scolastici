/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.RetentionPolicyDTO;
import it.filippetti.ks.api.bpm.mapper.SimpleMapper;
import it.filippetti.ks.api.bpm.model.RetentionPolicy;
import org.springframework.stereotype.Component;


/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class RetentionPolicyMapper extends SimpleMapper<RetentionPolicy, RetentionPolicyDTO> {

    @Override
    protected RetentionPolicyDTO doMapping(RetentionPolicy retentionPolicy) throws Exception {
        
        RetentionPolicyDTO dto = new RetentionPolicyDTO();
        
        dto.setDays(retentionPolicy.days());
        dto.setClean(retentionPolicy.isCleanEnabled());
        
        return dto;
    }
    
}
