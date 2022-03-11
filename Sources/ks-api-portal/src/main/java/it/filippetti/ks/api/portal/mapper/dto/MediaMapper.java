/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.mapper.dto;

import it.filippetti.ks.api.portal.dto.MediaDTO;
import it.filippetti.ks.api.portal.mapper.ContextMapper;
import it.filippetti.ks.api.portal.mapper.MappingContext;
import it.filippetti.ks.api.portal.model.Media;
import org.springframework.stereotype.Component;

/**
 *
 * @author dino
 */
@Component
public class MediaMapper extends ContextMapper<Media, MediaDTO>{

    @Override
    protected MediaDTO doMapping(Media input, MappingContext context) throws Exception {
        MediaDTO mediaDTO = new MediaDTO();
        mediaDTO.setDescription(input.getDescription());
        mediaDTO.setFile(input.getFile());
        mediaDTO.setFileType(input.getFileType());
        mediaDTO.setId(input.getId());
        mediaDTO.setMyKey(input.getMyKey());
        mediaDTO.setMimeType(input.getMimeType());
        mediaDTO.setName(input.getName());
        mediaDTO.setSize(input.getSize());
        return mediaDTO;
    }
    
}
