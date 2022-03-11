/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.repository;

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author marco.mazzocchetti
 */
public class BaseRepositoryImpl<T, ID extends Serializable> 
    extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    
    private final EntityManager entityManager;
    
    public BaseRepositoryImpl(
        JpaEntityInformation entityInformation, 
        EntityManager entityManager) {
        
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }
    
    @Override
    @Transactional
    public T refresh(T t) {
        entityManager.refresh(t);
        return t;
    }

    @Override
    public void detach(T t) {
        entityManager.detach(t);
    }

    @Override
    public void clear() {
        entityManager.clear();
    }
}
