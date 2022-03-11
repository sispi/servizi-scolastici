/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.service;

import it.filippetti.ks.api.portal.dto.CreateFavoriteProceedingDTO;
import it.filippetti.ks.api.portal.dto.FavoriteProceedingDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.exception.AuthorizationException;
import it.filippetti.ks.api.portal.exception.NotFoundException;
import it.filippetti.ks.api.portal.mapper.MappingContext;
import it.filippetti.ks.api.portal.mapper.dto.FavoriteProceedingMapper;
import it.filippetti.ks.api.portal.model.AuthenticationContext;
import it.filippetti.ks.api.portal.model.FavoriteProceeding;
import it.filippetti.ks.api.portal.model.Proceeding;
import it.filippetti.ks.api.portal.repository.FavoriteProceedingRepository;
import it.filippetti.ks.api.portal.repository.ProceedingRepository;
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
public class FavoriteProceedingService {
    
    private static final Logger log = LoggerFactory.getLogger(FavoriteProceedingService.class);
    
    @Autowired
    private ProceedingRepository proceedingRepository;
    
    @Autowired
    private FavoriteProceedingRepository favoriteProceedingRepository;
    
    @Autowired
    private FavoriteProceedingMapper favoriteProceedingMapper;
    
    public FavoriteProceedingDTO create(AuthenticationContext context, CreateFavoriteProceedingDTO createFavoriteProceedingDTO) throws ApplicationException{
        
        if (!(context.isAdmin() || context.isUser())) {
            throw new AuthorizationException();
        }
        
        log.info("Request to create FavoriteProceeding " + createFavoriteProceedingDTO.toString());
        Proceeding proceeding = proceedingRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), createFavoriteProceedingDTO.getProceedingId());
        FavoriteProceeding favoriteProceeding = new FavoriteProceeding(proceeding, context.getTenant(), context.getOrganization(), context.getUserId());
        favoriteProceeding = favoriteProceedingRepository.save(favoriteProceeding);
        return favoriteProceedingMapper.map(favoriteProceeding, MappingContext.of(context));
    }
    
    public FavoriteProceedingDTO findOne(AuthenticationContext context, Long id) throws ApplicationException{
        
        log.info("Request to get the FavouriteProceeding with id " + id);
        FavoriteProceeding favoriteProceeding = favoriteProceedingRepository.findByTenantAndOrganizationAndUserIdAndProceedingId(context.getTenant(), context.getOrganization(), context.getUserId(), id);
        if(favoriteProceeding == null){
            throw new NotFoundException();
        }
        return favoriteProceedingMapper.map(favoriteProceeding, MappingContext.of(context)); 
    }
    
    public List<FavoriteProceedingDTO> findAllByUser(AuthenticationContext context) throws ApplicationException{
        
        log.info("Request to get all FavouriteProceeding");
        List<FavoriteProceeding> favoriteProceedings = favoriteProceedingRepository.findAllByTenantAndOrganizationAndUserId(context.getTenant(), context.getOrganization(), context.getUserId());
        return favoriteProceedingMapper.map(favoriteProceedings, MappingContext.of(context));
    }
    
    public void delete(AuthenticationContext context, Long id)throws ApplicationException{
        
        if (!(context.isAdmin() || context.isUser())) {
            throw new AuthorizationException();
        }
        
        log.info("Request to delete FavoriteProceeding with id " + id);
        favoriteProceedingRepository.deleteById(id);
    }
}
