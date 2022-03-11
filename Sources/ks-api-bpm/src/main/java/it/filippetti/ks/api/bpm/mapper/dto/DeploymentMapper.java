/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.DeploymentDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class DeploymentMapper extends ContextMapper<Deployment, DeploymentDTO> {

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private ConfigurationMapper configurationMapper;
    
    @Autowired
    private EventMapper eventMapper;
    
    @Override
    protected DeploymentDTO doMapping(Deployment deployment, MappingContext ctx) throws Exception {
    
        DeploymentDTO dto = new DeploymentDTO();

        dto.setId(deployment.getId());
        dto.setName(deployment.getName());
        dto.setVersion(deployment.getVersion());
        dto.setProcessId(deployment.getProcessId());
        dto.setOwned(deployment.isOwned(ctx.authentication().getOrganization()));
        dto.setShared(deployment.isShared());
        dto.setActive(deployment.getStatus().isActive());
        dto.setDeployTs(deployment.getStatus().getDeployTs());
        if (ctx.fetch("configurations")) {
            dto.setConfigurations(configurationMapper.map(
                deployment.getConfigurations(ctx.authentication().getOrganization()), 
                ctx.of("configurations")));
        }
        if (ctx.fetch("assets")) {
            dto.setAssets(assetMapper.map(
                deployment.getAssets(), 
                ctx.of("assets")));
        }
        if (ctx.fetch("dependencies")) {
            dto.setDependencies(map(
                deployment.getDependencyDeployments(),
                ctx.of("dependencies")));
        }
        if (ctx.fetch("dependants")) {
            dto.setDependants(map(
                deployment.getDependantDeployments(ctx.authentication().getOrganization()), 
                ctx.of("dependants")));
        }
        if (ctx.fetch("events")) {
            dto.setEvents(eventMapper.map(
                deployment.getEvents(),
                ctx.of("events")));
        }
        return dto;
    }    
}
