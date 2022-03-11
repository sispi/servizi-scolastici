/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.mapper.dto;

import it.filippetti.ks.api.portal.dto.FavoriteProceedingDTO;
import it.filippetti.ks.api.portal.mapper.ContextMapper;
import it.filippetti.ks.api.portal.mapper.MappingContext;
import it.filippetti.ks.api.portal.model.FavoriteProceeding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author dino
 */
@Component
public class FavoriteProceedingMapper extends ContextMapper<FavoriteProceeding, FavoriteProceedingDTO>{
    
    @Autowired
    private ProceedingMapper proceedingMapper;

    @Override
    protected FavoriteProceedingDTO doMapping(FavoriteProceeding input, MappingContext context) throws Exception {
        FavoriteProceedingDTO favoriteProceedingDTO = new FavoriteProceedingDTO();
        favoriteProceedingDTO.setId(input.getId());
        favoriteProceedingDTO.setProceeding(proceedingMapper.map(input.getProceeding(), context));
        return favoriteProceedingDTO;
    }
    
}
