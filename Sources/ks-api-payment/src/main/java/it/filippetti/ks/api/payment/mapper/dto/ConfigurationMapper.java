/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.mapper.dto;

import it.filippetti.ks.api.payment.dto.ConfigurationDTO;
import it.filippetti.ks.api.payment.mapper.ContextMapper;
import it.filippetti.ks.api.payment.mapper.MappingContext;
import it.filippetti.ks.api.payment.model.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dino
 */
@Component
public class ConfigurationMapper extends ContextMapper<Configuration, ConfigurationDTO>{

    @Autowired
    private ChannelMapper channelMapper;

    @Override
    protected ConfigurationDTO doMapping(Configuration input, MappingContext context) throws Exception {
        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        configurationDTO.setId(input.getId());
        configurationDTO.setPaymentEnabled(input.getPaymentEnabled());
        configurationDTO.setSecretKey(input.getSecretKey());
        configurationDTO.setSecretUser(input.getSecretUser());
        configurationDTO.setServicePassword(input.getServicePassword());
        configurationDTO.setTerminalId(input.getTerminalId());
        configurationDTO.setCurrencyCode(input.getCurrencyCode());
        configurationDTO.setLangId(input.getLangId());
        configurationDTO.setCashMode(input.getCashMode());
        configurationDTO.setChannel(channelMapper.map(input.getChannel(), context));
        configurationDTO.setDefaultConf(input.getDefaultConf());
        return configurationDTO;
    }
    
}