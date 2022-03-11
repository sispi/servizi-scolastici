/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.repository;

import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Deployment;
import it.filippetti.ks.api.bpm.model.DeploymentReference;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author marco.mazzocchetti
 */
@Repository
public interface DeploymentRepository extends BaseRepository<Deployment, Long> {

    public default Deployment findById(AuthenticationContext context, Long id) {
        if (context.isAdmin()) {
            return findById(context.getTenant(), context.getOrganization(), id);
        } else {
            return null;
        }
    }

    public default Page<Deployment> findAll(
        AuthenticationContext context,
        String processId,
        Pageable pageable) {
        if (context.isAdmin()) {
            return findAll(
                context.getTenant(),
                context.getOrganization(),
                processId != null ? processId.replaceAll("%", "").toLowerCase().trim() + "%" : "%",
                pageable);
        } else {
            return Page.<Deployment>empty();
        }

    }

    public default List<Deployment> findPreviousVersions(Deployment deployment) {
        return findPreviousVersions(
            deployment.getTenant(), 
            deployment.getOrganization(),
            deployment.getName(), 
            deployment.getVersionNumber());
    }
    
    public default Deployment findLatestVersion(Deployment deployment) {
        return findLatestVersions(
             deployment.getTenant(), 
             deployment.getOrganization(),
             deployment.getName(), 
             PageRequest.of(0, 1))
            .stream()
            .findFirst()
            .orElse(null);
    }

    public default boolean existsShared(Deployment deployment) {
        return existsShared(
            deployment.getTenant(),
            deployment.getProcessId());
    }

    @Query(""
        + "SELECT d "
        + "FROM Deployment d "
        + "WHERE  d.id = :id "
        + "AND d.tenant = :tenant "         
        + "AND (d.shared = true OR d.organization = :organization)")
    public Deployment findById(
        @Param("tenant") String tenant,
        @Param("organization") String organization,             
        @Param("id") Long id);   

    @Query(""
        + "SELECT d "
        + "FROM Deployment d "
        + "WHERE d.unitId = :unitId")
    public Deployment findByUnitId(
        @Param("unitId") String unitId);        
    
    @Query(""
        + "SELECT d "
        + "FROM Deployment d "
        + "WHERE d.tenant = :tenant "
        + "AND d.processId = :processId "            
        + "AND (d.shared = true OR d.organization = :organization)")
    public Deployment findByProcessId(
        @Param("tenant") String tenant, 
        @Param("organization") String organization, 
        @Param("processId") String processId);

    @Query(""
        + "SELECT d "
        + "FROM Deployment d "
        + "WHERE d.tenant = :tenant "
        + "AND LOWER(d.processId) LIKE :processId "            
        + "AND (d.shared = true OR d.organization = :organization)")
    public Page<Deployment> findAll(
        @Param("tenant") String tenant,
        @Param("organization") String organization,
        @Param("processId") String processId,
        Pageable pageable);   

    @Query(value = ""
        + "SELECT new DeploymentReference("
            + "d.id, "
            + "d.unitId, "
            + "d.tenant, "
            + "d.organization) "
        + "FROM Deployment d "
        + "WHERE d.tenant = :tenant "
        + "AND (d.shared = true OR d.organization = :organization)")
    public List<DeploymentReference> findAllReferences(
        @Param("tenant") String tenant,
        @Param("organization") String organization);   

    @Query(""
        + "SELECT d "
        + "FROM Deployment d "
        + "WHERE d.tenant = :tenant "
        + "AND d.organization = :organization "            
        + "AND d.name = :name "
        + "AND d.versionNumber < :versionNumber")
    public List<Deployment> findPreviousVersions(
        @Param("tenant") String tenant, 
        @Param("organization") String organization,             
        @Param("name") String name,
        @Param("versionNumber") Integer versionNumber);
    
    @Query(""
        + "SELECT d "
        + "FROM Deployment d "
        + "WHERE d.tenant = :tenant "
        + "AND d.organization = :organization "                        
        + "AND d.name = :name "
        + "ORDER BY d.versionNumber DESC")
    public List<Deployment> findLatestVersions(
        @Param("tenant") String tenant, 
        @Param("organization") String organization,             
        @Param("name") String name,
        Pageable pageable);

    @Query(""
        + "SELECT CASE WHEN COUNT(d)> 0 THEN true ELSE false END "
        + "FROM Deployment d "
        + "WHERE d.tenant = :tenant "
        + "AND d.processId = :processId "                        
        + "AND d.shared = true")
    public boolean existsShared(
        @Param("tenant") String tenant, 
        @Param("processId") String processId);   
}
