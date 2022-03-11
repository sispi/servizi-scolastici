/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.ConfigurationDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.Configuration;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class ConfigurationMapper extends ContextMapper<Configuration, ConfigurationDTO> {

    @Autowired
    private DeploymentMapper deploymentMapper;
    
    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private RetentionPolicyMapper retentionPolicyMapper;
    
    @Override
    protected ConfigurationDTO doMapping(Configuration configuration, MappingContext ctx) throws Exception {
        
        ConfigurationDTO dto = new ConfigurationDTO();

        dto.setId(configuration.getId());
        dto.setProfile(configuration.getProfile());
        dto.setActive(configuration.isActive());
        dto.setRunnable(configuration.isRunnable());
        dto.setSearchable(configuration.isSearchable());
        dto.setCategory(configuration.getCategory());
        if (ctx.fetch("deployment")) {
            dto.setDeployment(deploymentMapper.map(
                configuration.getDeployment(),
                ctx.of("deployment"))); 
        }
        if (ctx.fetch("permissions")) {
            dto.setPermissions(permissionMapper.map(
                configuration.getPermissions()));
        }
        if (ctx.fetch("defaultInput")) {
            dto.setDefaultInput(configuration.getSettings().getDefaultInput());
        }
        if (ctx.fetch("assets")) {
            dto.setAssets(assetMapper.map(
                configuration.getAssets(), 
                ctx.of("assets"))); 
        }
        if (ctx.fetch("retentionPolicy")) {
            dto.setRetentionPolicy(retentionPolicyMapper.map(
                configuration.getRetentionPolicy()));
        }
        if (ctx.fetch("authorizations")) {
            dto.setAuthorizations(
                configuration.getAuthorizations(ctx.authentication())
                    .stream()
                    .map(e -> e.name())
                    .collect(Collectors.toList()));
        }        
        return dto;
    }    
}
