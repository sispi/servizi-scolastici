/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.repository;

import it.filippetti.ks.api.portal.model.FavoriteProceeding;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dino
 */
@Repository
public interface FavoriteProceedingRepository extends BaseRepository<FavoriteProceeding, Long>{
    public List<FavoriteProceeding> findAllByTenantAndOrganizationAndUserId(String tenant, String organization, String userId);
    public FavoriteProceeding findByTenantAndOrganizationAndUserIdAndProceedingId(String tenant, String organization, String userId, Long proceedingId);
}
