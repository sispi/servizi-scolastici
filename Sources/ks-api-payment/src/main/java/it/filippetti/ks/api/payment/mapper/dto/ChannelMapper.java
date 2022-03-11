/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.mapper.dto;

import it.filippetti.ks.api.payment.dto.ChannelDTO;
import it.filippetti.ks.api.payment.mapper.ContextMapper;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.model.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dino
 */
@Component
public class ChannelMapper extends ContextMapper<Channel, ChannelDTO>{
    
    @Autowired
    private ProviderMapper providerMapper;
    
    @Override
    protected ChannelDTO doMapping(Channel input, MappingContext context) throws Exception {
        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setId(input.getId());
        channelDTO.setName(input.getName());
        channelDTO.setProvider(providerMapper.map(input.getProvider(), context));
        return channelDTO;
    }
    
    public Channel toEntity(ChannelDTO channelDTO, MappingContext context) throws Exception {
        
        return new Channel (
            channelDTO.getId(),
            channelDTO.getName(),
            context.authentication().getTenant(),
            context.authentication().getOrganization(),
            providerMapper.toEntity(channelDTO.getProvider(), context)
        );
        
    }
    
}