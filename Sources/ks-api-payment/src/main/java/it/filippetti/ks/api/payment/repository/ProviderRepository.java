/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.repository;

import it.filippetti.ks.api.payment.model.Provider;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dino
 */
@Repository
public interface ProviderRepository  extends BaseRepository<Provider, Long>{
    
}
