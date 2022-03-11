/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.service;

import it.filippetti.ks.api.portal.dto.CreateMediaDTO;
import it.filippetti.ks.api.portal.dto.MediaDTO;
import it.filippetti.ks.api.portal.dto.PageDTO;
import it.filippetti.ks.api.portal.dto.UpdateMediaDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.exception.AuthorizationException;
import it.filippetti.ks.api.portal.exception.NotFoundException;
import it.filippetti.ks.api.portal.mapper.MappingContext;
import it.filippetti.ks.api.portal.mapper.dto.MediaLightMapper;
import it.filippetti.ks.api.portal.mapper.dto.MediaMapper;
import it.filippetti.ks.api.portal.model.AuthenticationContext;
import it.filippetti.ks.api.portal.model.Media;
import it.filippetti.ks.api.portal.model.Pager;
import it.filippetti.ks.api.portal.repository.MediaRepository;
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
public class MediaService {
    
    private static final Logger log = LoggerFactory.getLogger(MediaService.class);
    
    @Autowired
    private MediaRepository mediaRepository;
    
    @Autowired
    private MediaMapper mediaMapper;
    
    @Autowired
    private MediaLightMapper mediaLightMapper;
    
    public MediaDTO create(AuthenticationContext context, CreateMediaDTO createMediaDTO)throws ApplicationException{

        if(!context.isAdmin()){
            throw new AuthorizationException();
        }

        log.info("Request to create Media " + createMediaDTO.toString());

        Media media = new Media();
        media.setDescription(createMediaDTO.getDescription());
        media.setFile(createMediaDTO.getFile());
        media.setFileType(createMediaDTO.getFileType());
        media.setMyKey(createMediaDTO.getMyKey());
        media.setMimeType(createMediaDTO.getMimeType());
        media.setName(createMediaDTO.getName());
        media.setOrganization(context.getOrganization());
        media.setSize(createMediaDTO.getSize());
        media.setTenant(context.getTenant());
        media = mediaRepository.save(media);
        return mediaMapper.map(media, MappingContext.of(context));
    }
    
    public MediaDTO findOne(AuthenticationContext context, Long mediaId) throws ApplicationException{
        log.info("Request to get Media with id='" + mediaId + "'");
        Optional<Media> mediaOpt = mediaRepository.findByTenantAndOrganizationAndId(context.getTenant(), context.getOrganization(), mediaId);
        if(mediaOpt.isPresent()){
            return mediaMapper.map(mediaOpt.get(), MappingContext.of(context));
        } else {
            throw new NotFoundException();
        }
    }
    
    public PageDTO<MediaDTO> findAllPaged(AuthenticationContext context, Integer pageNumber, Integer pageSize, String orderBy)
        throws ApplicationException{
        log.info("Request to get paged Medias");
        //return mediaLightMapper.map(mediaRepository.findAll(context, Pager.of(Media.class, pageNumber, pageSize, orderBy)), MappingContext.of(context));
        return mediaMapper.map(mediaRepository.findAll(context, Pager.of(Media.class, pageNumber, pageSize, orderBy)), MappingContext.of(context));
    }
    
    public void delete(AuthenticationContext context, Long mediaId) throws ApplicationException{

        if(!context.isAdmin()){
            throw new AuthorizationException();
        }

        log.info("Request to delete Proceeding with id='" + mediaId + "'");
        mediaRepository.deleteById(mediaId);
    }
    
    public MediaDTO update(AuthenticationContext context, UpdateMediaDTO updateMediaDTO) throws ApplicationException{

        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }

        log.info("Request to update Media " + updateMediaDTO.toString());
        Media media = new Media(updateMediaDTO.getId(),
                updateMediaDTO.getName(),
                updateMediaDTO.getDescription(),
                updateMediaDTO.getFile(),
                updateMediaDTO.getFileType(),
                updateMediaDTO.getMimeType(),
                updateMediaDTO.getSize(),
                updateMediaDTO.getMyKey(),
                context.getTenant(),
                context.getOrganization()
        );
        media = mediaRepository.save(media);
        return mediaMapper.map(media, MappingContext.of(context));
    }
    
    public MediaDTO findOneByKey(AuthenticationContext context, String myKey) throws ApplicationException{
        log.info("Request to get Media with key " + myKey);
        Optional<Media> mediaOptional = mediaRepository.findByTenantAndMyKey(context.getTenant(), myKey);
        if(mediaOptional.isPresent()){
            return mediaMapper.map(mediaOptional.get(), MappingContext.of(context));
        } else {
            throw new NotFoundException();
        }
    }
}
