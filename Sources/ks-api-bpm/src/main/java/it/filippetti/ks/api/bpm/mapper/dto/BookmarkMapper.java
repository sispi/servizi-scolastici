/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.BookmarkDTO;
import it.filippetti.ks.api.bpm.mapper.SimpleMapper;
import it.filippetti.ks.api.bpm.model.Bookmark;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class BookmarkMapper extends SimpleMapper<Bookmark, BookmarkDTO> {

    @Override
    protected BookmarkDTO doMapping(Bookmark bookmark) throws Exception {
        
        BookmarkDTO dto = new BookmarkDTO();
        
        dto.setId(bookmark.getId());
        dto.setInstanceId(bookmark.getInstance().getId());
        dto.setDescription(bookmark.getDescription());
        return dto;
    }
}
