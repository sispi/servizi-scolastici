/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.InstanceVariableDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.InstanceVariable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class InstanceVariablesMapper extends ContextMapper<Collection<InstanceVariable>, Map<String, InstanceVariableDTO>> {

    @Override
    protected Map<String, InstanceVariableDTO> doMapping(Collection<InstanceVariable> variables, MappingContext ctx) throws Exception {
        
        Map<String, InstanceVariableDTO> dtoMap = new HashMap<>();
        InstanceVariableDTO dto;
        Set<String> names;
        
        names = ctx
            .fetcher()
            .keys()
            .stream()
            .filter(k -> k.startsWith("@"))
            .map(k -> k.substring(1))
            .collect(Collectors.toSet());
        
        for (InstanceVariable variable : variables) {
            if (names.isEmpty() || names.contains(variable.getName())) {
                dto = new InstanceVariableDTO();
                dto.setBusiness(variable.isBusiness());
                dto.setIn(variable.isIn());
                dto.setOut(variable.isOut());
                dto.setConfig(variable.isConfig());
                dto.setLastModifiedTs(variable.getLastModifiedTs());
                if (ctx.fetch("value")) {
                    dto.setValue(variable.getValue().asObject());
                }
                dtoMap.put(variable.getName(), dto);
            }
        }
        return dtoMap;
    }
}
