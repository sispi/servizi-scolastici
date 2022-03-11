/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Configuration;
import it.filippetti.ks.api.bpm.model.PermissionType;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author marco.mazzocchetti
 */
@Repository
public interface ConfigurationRepository extends BaseRepository<Configuration, Long> {

    public default Configuration findById(AuthenticationContext context, Long id) {
        return findById(
            context.getTenant(), 
            context.getOrganization(), 
            context.isAdmin(),
            context.getIdentities(), 
            PermissionType.View,
            id);
    }

    public default Page<Configuration> findAll(
        AuthenticationContext context, 
        String processId, 
        boolean runnableOnly, 
        Pageable pageable) {
        return findAll(
            context.getTenant(), 
            context.getOrganization(), 
            processId != null ? processId.replaceAll("%", "").toLowerCase().trim() + "%" : "%",
            context.isAdmin(),            
            context.getIdentities(), 
            runnableOnly ? PermissionType.Start : PermissionType.View,
            runnableOnly,
            pageable);
    }
    
    public default Configuration findPreviousVersion(Configuration configuration) {
        
        return findPreviousVersions(
             configuration.getDeployment().getTenant(), 
             configuration.getOrganization(),   
             configuration.getDeployment().getName(), 
             configuration.getDeployment().getVersionNumber(), 
             PageRequest.of(0, 1))
            .stream()
            .findFirst()
            .orElse(null);
    }

    public default int deactivatePreviousVersions(Configuration configuration) {
        return deactivatePreviousVersions(
            configuration.getDeployment().getTenant(), 
            configuration.getOrganization(),
            configuration.getDeployment().getName(), 
            configuration.getDeployment().getVersionNumber());
    }

    @Query(""
        + "SELECT c "
        + "FROM Configuration c "
        + "WHERE c.id = :id "
        + "AND c.organization = :organization "            
        + "AND c.deployment.tenant = :tenant "            
        + "AND ("
            + ":isAdmin = true OR "
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM Permission p "
                + "WHERE p.configuration = c "
                + "AND p.type = :permissionType "
                + "AND p.identity IN :identities))")            
    public Configuration findById(
        @Param("tenant") String tenant,
        @Param("organization") String organization,    
        @Param("isAdmin") boolean isAdmin,    
        @Param("identities") Set<String> identities,    
        @Param("permissionType") PermissionType permissionType,   
        @Param("id") Long id);   

    @Query(""
        + "SELECT c "
        + "FROM Configuration c "
        + "WHERE c.organization = :organization "
        + "AND c.runnable = CASE "
            + "WHEN :runnableOnly = true "
            + "THEN true "
            + "ELSE c.runnable END "            
        + "AND c.deployment.tenant = :tenant "
        + "AND LOWER(c.deployment.processId) LIKE :processId "            
        + "AND ("
            + ":isAdmin = true OR "
            + "EXISTS ("
                + "SELECT 1 "
                + "FROM Permission p "
                + "WHERE p.configuration = c "
                + "AND p.type = :permissionType "
                + "AND p.identity IN :identities))")            
    public Page<Configuration> findAll(
        @Param("tenant") String tenant,
        @Param("organization") String organization,    
        @Param("processId") String processId,    
        @Param("isAdmin") boolean isAdmin,    
        @Param("identities") Set<String> identities,    
        @Param("permissionType") PermissionType permissionType,   
        @Param("runnableOnly") boolean runnableOnly,   
        Pageable pageable); 
        
    @Query(""
        + "SELECT c "
        + "FROM Configuration c "
        + "WHERE c.organization = :organization "
        + "AND c.deployment.tenant = :tenant "
        + "AND c.deployment.name = :name "
        + "AND c.deployment.versionNumber < :versionNumber "
        + "ORDER BY c.deployment.versionNumber DESC")
    public List<Configuration> findPreviousVersions(
        @Param("tenant") String tenant, 
        @Param("organization") String organization,             
        @Param("name") String name, 
        @Param("versionNumber") Integer versionNumber,
        Pageable pageable);
    
    @Transactional
    @Modifying
    @Query(""
        + "UPDATE Configuration c "
        + "SET c.active = false "
        + "WHERE c.deployment IN ("
            + "SELECT d "
            + "FROM Deployment d "
            + "WHERE d.tenant = :tenant "
            + "AND d.name = :name "
            + "AND d.versionNumber < :versionNumber) "
        + "AND c.organization = :organization")
    public int deactivatePreviousVersions(
        @Param("tenant") String tenant, 
        @Param("organization") String organization,             
        @Param("name") String name, 
        @Param("versionNumber") Integer versionNumber);
}
