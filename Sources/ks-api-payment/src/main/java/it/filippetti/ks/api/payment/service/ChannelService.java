/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.service;

import it.filippetti.ks.api.payment.dto.ChannelDTO;
import it.filippetti.ks.api.payment.dto.CreateChannelDTO;
import it.filippetti.ks.api.payment.dto.UpdateChannelDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.exception.AuthorizationException;
import it.filippetti.ks.api.payment.exception.NotFoundException;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.mapper.dto.ChannelMapper;
import it.filippetti.ks.api.payment.model.AuthenticationContext;
import it.filippetti.ks.api.payment.model.Channel;
import it.filippetti.ks.api.payment.model.Provider;
import it.filippetti.ks.api.payment.repository.ChannelRepository;
import it.filippetti.ks.api.payment.repository.ProviderRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author dino
 */
@Service
public class ChannelService {
    
    private static final Logger log = LoggerFactory.getLogger(ChannelService.class);
    
    @Autowired
    private ChannelRepository channelRepository;
    
    @Autowired
    private ProviderRepository providerRepository;
    
    @Autowired
    private ChannelMapper channelMapper;
    
    public ChannelDTO create(AuthenticationContext context, CreateChannelDTO createChannelDTO) throws ApplicationException{
        log.info("Request to create Channel " + createChannelDTO.toString());
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        Optional<Provider> providerOp = providerRepository.findById(createChannelDTO.getProviderId());
        Channel channel = new Channel(
                createChannelDTO.getName(),
                context.getTenant(),
                context.getOrganization(),
                providerOp.get()
        );
        return channelMapper.map(channelRepository.save(channel), MappingContext.of(context));
    }
    
    public ChannelDTO findOne(AuthenticationContext context, Long id) throws ApplicationException{
        log.info("Request to get the Channel with id " + id);
        Channel channel = channelRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), id);
        if(channel == null){
            throw new NotFoundException();
        }
        return channelMapper.map(channel, MappingContext.of(context));
    }
    
    public void delete(AuthenticationContext context, Long id) throws ApplicationException{
        log.info("Request to delete Channel with id " + id);
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        channelRepository.deleteById(id);
    }
    
    public ChannelDTO update(AuthenticationContext context, UpdateChannelDTO updateChannelDTO, Long id) throws ApplicationException{
        log.info("Request to update Channel " + updateChannelDTO.toString());
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        Channel c = channelRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), id);
        if (c == null) {
            throw new NotFoundException();
        }
        Optional<Provider> providerOp = providerRepository.findById(updateChannelDTO.getProviderId());
        Channel channel = new Channel(
                id,
                updateChannelDTO.getName(),
                context.getTenant(),
                context.getOrganization(),
                providerOp.get()
        );
        return channelMapper.map(channelRepository.save(channel), MappingContext.of(context));
    }
    
    public List<ChannelDTO> findAll(AuthenticationContext context) throws ApplicationException{
        log.info("Request to get paged Channels");
        return channelMapper.map(channelRepository.findAll(context), MappingContext.of(context));
    }
    
}
