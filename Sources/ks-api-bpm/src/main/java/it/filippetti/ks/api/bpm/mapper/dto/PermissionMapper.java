/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.mapper.SimpleMapper;
import it.filippetti.ks.api.bpm.model.Permission;
import it.filippetti.ks.api.bpm.model.PermissionType;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class PermissionMapper extends SimpleMapper<Set<Permission>, Map<String, Set<String>>> {

    @Override
    protected Map<String, Set<String>> doMapping(Set<Permission> permissions) throws Exception {
        return Arrays
            .stream(PermissionType.values())
            .collect(Collectors.toMap(
                e -> e.name(), 
                e -> permissions
                    .stream()
                    .filter(p -> p.getType().equals(e))
                    .map(p -> p.getIdentity())
                    .collect(Collectors.toSet())));
    } 
}
