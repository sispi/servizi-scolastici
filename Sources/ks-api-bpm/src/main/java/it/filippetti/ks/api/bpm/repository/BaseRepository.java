/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.Identifiable;
import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author marco.mazzocchetti
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface BaseRepository<T extends Identifiable, ID extends Serializable> 
    extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    
    public EntityManager getEntityManager();
    public void clear();
    public T refresh(T t);
    public T reload(T t);
    public T reload(Class<T> type, Object id);
    public void detach(T t);
    
}
