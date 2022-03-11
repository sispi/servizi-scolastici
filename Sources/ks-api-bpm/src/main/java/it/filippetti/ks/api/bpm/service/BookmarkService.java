/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import it.filippetti.ks.api.bpm.dto.BookmarkDTO;
import it.filippetti.ks.api.bpm.dto.PageDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.exception.NotFoundException;
import it.filippetti.ks.api.bpm.mapper.dto.BookmarkMapper;
import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Bookmark;
import it.filippetti.ks.api.bpm.model.Pager;
import it.filippetti.ks.api.bpm.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class BookmarkService {

    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BookmarkMapper bookmarkMapper;
    
    public BookmarkService() {
    }
    
    /**
     * 
     * @param context
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @return
     * @throws ApplicationException 
     */
    public PageDTO<BookmarkDTO> getBookmarks(
        AuthenticationContext context,
        Integer pageNumber, Integer pageSize, String orderBy) 
        throws ApplicationException {
        
        return bookmarkMapper.map(
            bookmarkRepository.findAll(
                context, 
                Pager.of(Bookmark.class, pageNumber, pageSize, orderBy)));
    }
    
    /**
     * 
     * @param context
     * @param bookmarkId
     * @throws ApplicationException 
     */
    public void deleteBookmark(
        AuthenticationContext context, Long bookmarkId) 
        throws ApplicationException {
        
        Bookmark bookmark;
        
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get bookmark
            bookmark = bookmarkRepository.findById(context, bookmarkId);
            if (bookmark == null) {
                throw new NotFoundException();
            }
            // delete bookmark
            bookmarkRepository.delete(bookmark);
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
    }    
}
