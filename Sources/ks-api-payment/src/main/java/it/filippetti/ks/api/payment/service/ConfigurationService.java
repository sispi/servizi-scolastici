/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.service;

import it.filippetti.ks.api.payment.dto.ConfigurationDTO;
import it.filippetti.ks.api.payment.dto.CreateConfigurationDTO;
import it.filippetti.ks.api.payment.dto.PageDTO;
import it.filippetti.ks.api.payment.dto.UpdateConfigurationDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.exception.AuthorizationException;
import it.filippetti.ks.api.payment.exception.NotFoundException;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.mapper.dto.ConfigurationMapper;
import it.filippetti.ks.api.payment.model.AuthenticationContext;
import it.filippetti.ks.api.payment.model.Channel;
import it.filippetti.ks.api.payment.model.Configuration;
import it.filippetti.ks.api.payment.model.Pager;
import it.filippetti.ks.api.payment.repository.ChannelRepository;
import it.filippetti.ks.api.payment.repository.ConfigurationRepository;
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
public class ConfigurationService {
    
    private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class);
    
    @Autowired
    private ConfigurationRepository configurationRepository;
    
    @Autowired
    private ConfigurationMapper configurationMapper;
    
    @Autowired
    private ChannelRepository channelRepository;
    
    public ConfigurationDTO create(AuthenticationContext context, CreateConfigurationDTO createConfigurationDTO, Long channelId)  throws ApplicationException{
        log.info("Request to create Configuration " + createConfigurationDTO.toString());
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        Channel channel = channelRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), channelId);
        Configuration configuration = new Configuration(
                createConfigurationDTO.getPaymentEnabled(),
                createConfigurationDTO.getSecretKey(),
                createConfigurationDTO.getSecretUser(),
                createConfigurationDTO.getServicePassword(),
                createConfigurationDTO.getTerminalId(),
                createConfigurationDTO.getCurrencyCode(),
                createConfigurationDTO.getLangId(),
                createConfigurationDTO.getCashMode(),
                channel,
                createConfigurationDTO.getDefaultConf(),
                context.getTenant(),
                context.getOrganization()
        );
        return configurationMapper.map(configurationRepository.save(configuration), MappingContext.of(context));
    }
    
    public ConfigurationDTO findOne(AuthenticationContext context, Long id) throws ApplicationException{
        log.info("Request to get the Configuration with id " + id);
        Configuration configuration = configurationRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), id);
        if(configuration == null){
            throw new NotFoundException();
        }
        return configurationMapper.map(configuration, MappingContext.of(context));
    }
    
    public void delete(AuthenticationContext context, Long id) throws ApplicationException{
        log.info("Request to delete Configuration with id " + id);
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        configurationRepository.deleteById(id);
    }
    
    public ConfigurationDTO update(AuthenticationContext context, UpdateConfigurationDTO updateConfigurationDTO, Long id) throws ApplicationException{
        log.info("Request to update Configuration " + updateConfigurationDTO.toString());
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        Configuration c = configurationRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), id);
        if (c == null) {
            throw new NotFoundException();
        }
        Channel channel = channelRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), updateConfigurationDTO.getChannelId());
        Configuration configuration = new Configuration(
                id,
                updateConfigurationDTO.getPaymentEnabled(),
                updateConfigurationDTO.getSecretKey(),
                updateConfigurationDTO.getSecretUser(),
                updateConfigurationDTO.getServicePassword(),
                updateConfigurationDTO.getTerminalId(),
                updateConfigurationDTO.getCurrencyCode(),
                updateConfigurationDTO.getLangId(),
                updateConfigurationDTO.getCashMode(),
                channel,
                updateConfigurationDTO.getDefaultConf(),
                context.getTenant(),
                context.getOrganization()
        );
        return configurationMapper.map(configurationRepository.save(configuration), MappingContext.of(context));
    }
    
    public PageDTO<ConfigurationDTO> findAllPaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy) throws ApplicationException{
        log.info("Request to get paged Configuration");
        return configurationMapper.map(configurationRepository.findAllPaginated(
                context, 
                Pager.of(Configuration.class, pageNumber, pageSize, orderBy)),
                MappingContext.of(context));
    }
    
    public PageDTO<ConfigurationDTO> findAllByChannelPaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy, Long channelId) throws ApplicationException{
        log.info("Request to get paged Configurations of a Channel with id " + channelId);
        Channel channel = channelRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), channelId);
        return configurationMapper.map(
                configurationRepository.findAllByChannelPaginated(
                        context,
                        Pager.of(Configuration.class, pageNumber, pageSize, orderBy),
                        channel
                ),
                MappingContext.of(context)
        );
    }
    
    public List<ConfigurationDTO> findAllByChannel(AuthenticationContext context, Long channelId) throws ApplicationException{
        log.info("Request to get the list of Configurations with the id of Channel " + channelId);
        //è sicuro che solo un admin può prendere la lista delle configurazioni?
        return configurationMapper.map(
                configurationRepository.findAllByTenantAndOrganizationAndChannelId(context.getTenant(), context.getOrganization(), channelId),
                MappingContext.of(context)
        );
    }
    
    public void setDefault(AuthenticationContext context, Long configurationId, Long channelId) throws ApplicationException{
        log.info("Request to set defaul the configuration with id " + configurationId);
        if(!context.isAdmin()){
            throw new AuthorizationException();
        }
        List<Configuration> configurations = configurationRepository.findAllByTenantAndOrganizationAndChannelId(context.getTenant(), context.getOrganization(), channelId);
        for(Configuration c : configurations){
            if(c.getId().equals(configurationId)){
                c.setDefaultConf(true);
            } else {
                c.setDefaultConf(false);
            }
            configurationRepository.save(c);
        }
    }
    
    public ConfigurationDTO findDefaultChannelConfiguration(AuthenticationContext context, Long channelId)  throws ApplicationException{
        log.info("Request to find a channel configuration with default='true'");
        //TODO fare query!
        List<ConfigurationDTO> configurationDTOs = findAllByChannel(context, channelId);
        if(configurationDTOs.size() > 0){
            for(ConfigurationDTO conf : configurationDTOs){
                if(conf.getDefaultConf()) return conf;
            }
        }
        throw new NotFoundException();
    }
    
}
