/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.filippetti.ks.api.bpm.configuration.ApplicationProperties;
import it.filippetti.ks.api.bpm.dto.ActivateDeploymentDTO;
import it.filippetti.ks.api.bpm.dto.CreateDeploymentDTO;
import it.filippetti.ks.api.bpm.dto.DeleteDeploymentDTO;
import it.filippetti.ks.api.bpm.dto.DeploymentDTO;
import it.filippetti.ks.api.bpm.dto.PageDTO;
import it.filippetti.ks.api.bpm.dto.ShareDeploymentDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.exception.AuthorizationException;
import it.filippetti.ks.api.bpm.exception.BusinessError;
import it.filippetti.ks.api.bpm.exception.BusinessException;
import it.filippetti.ks.api.bpm.exception.NotFoundException;
import it.filippetti.ks.api.bpm.exception.OperationException;
import it.filippetti.ks.api.bpm.exception.ValidationException;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.mapper.dto.DeploymentMapper;
import it.filippetti.ks.api.bpm.model.ActiveInstancesPolicy;
import it.filippetti.ks.api.bpm.model.Asset;
import it.filippetti.ks.api.bpm.model.AssetType;
import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Configuration;
import it.filippetti.ks.api.bpm.model.Deployment;
import it.filippetti.ks.api.bpm.model.DeploymentDependency;
import it.filippetti.ks.api.bpm.model.DeploymentStatus;
import it.filippetti.ks.api.bpm.model.DeploymentUnit;
import it.filippetti.ks.api.bpm.model.Fetcher;
import it.filippetti.ks.api.bpm.model.Pager;
import it.filippetti.ks.api.bpm.model.ProcessMetadata;
import it.filippetti.ks.api.bpm.repository.DeploymentRepository;
import it.filippetti.ks.api.bpm.repository.DeploymentStatusRepository;
import it.filippetti.ks.api.bpm.repository.EventRepository;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import org.drools.core.util.MVELSafeHelper;
import org.drools.core.xml.SemanticModules;
import org.jbpm.bpmn2.xml.BPMNDISemanticModule;
import org.jbpm.bpmn2.xml.BPMNExtensionsSemanticModule;
import org.jbpm.bpmn2.xml.BPMNSemanticModule;
import org.jbpm.compiler.xml.XmlProcessReader;
import org.jbpm.kie.services.impl.KModuleDeploymentUnit;
import org.jbpm.kie.services.impl.utils.PreUndeployOperations;
import org.jbpm.process.audit.event.AuditEventBuilder;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.runtime.manager.impl.DefaultRegisterableItemsFactory;
import org.jbpm.springboot.services.SpringKModuleDeploymentService;
import org.jbpm.util.PatternConstants;
import org.jbpm.workflow.core.node.SubProcessNode;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.Message;
import org.kie.api.definition.process.Node;
import org.kie.api.definition.process.Process;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.manager.RegisterableItemsFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.conf.DeploymentDescriptor;
import org.kie.internal.runtime.conf.RuntimeStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.xml.sax.SAXException;
import org.zeroturnaround.zip.ByteSource;
import org.zeroturnaround.zip.ZipEntryCallback;
import org.zeroturnaround.zip.ZipEntrySource;
import org.zeroturnaround.zip.ZipUtil;

/**
 *
 * @author marco.mazzocchetti
 */
public class DeploymentService extends SpringKModuleDeploymentService {
    
    private static final Logger log = LoggerFactory.getLogger(DeploymentService.class);
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private ValidationService validationService;
    
    @Autowired
    private ConfigurationService configurationService;
    
    @Autowired
    private EventService eventService;

    @Autowired
    private FormAssetService formAssetService;
    
    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private DeploymentStatusRepository deploymentStatusRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    @Qualifier("defaultDeploymentDescriptor")
    private DeploymentDescriptor deploymentDescriptor;
    
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private ThreadPoolTaskScheduler taskExecutor;
    
    @Autowired
    private RegisterableItemsFactory registerableItemsFactory;

    @Autowired
    private DeploymentMapper deploymentMapper;

    @Autowired
    private ObjectMapper objectMapper;
    
    private ScheduledFuture synchronizer;
    
    @EventListener(ApplicationReadyEvent.class)
    public void startup() {        
        log.info("Starting deployment synchronizer");
        synchronizer = taskScheduler.scheduleWithFixedDelay(
            new Synchronizer(applicationProperties), 
            applicationProperties.deploySynchronizerDelay());
    }    

    @Override
    @PreDestroy
    public void shutdown() {
        log.info("Shutting down deployment synchronizer");
        synchronizer.cancel(false);
        super.shutdown();
    }
    
    /**
     * 
     * @param context
     * @param inputStream
     * @throws Exception 
     */
    public void createDeployments(
        AuthenticationContext context, InputStream inputStream) 
        throws Exception {

        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }        
        try {
            byte [] batchContent = inputStream.readAllBytes();
            ZipUtil.handle(
                new ByteArrayInputStream(batchContent), 
                "batch.json", 
                (batchIs, eb) -> {
                    try {
                        validationService
                            .validate(objectMapper
                                .readValue(
                                    new ByteArrayInputStream(batchIs.readAllBytes()), 
                                    CreateDeploymentDTO.Batch.class))
                            .forEach(batchItem -> {
                                ZipUtil.handle(
                                    new ByteArrayInputStream(batchContent), 
                                    batchItem.getEntry(), 
                                    (batchItemIs, ei) -> {
                                        try {
                                            createDeployment(
                                                context, 
                                                batchItem.getParams(), 
                                                new ByteArrayInputStream(batchItemIs.readAllBytes()));
                                        } catch (ApplicationException e) {
                                            e.getDetails().add(batchItem.getEntry());
                                            throw new OperationException(e);
                                        } catch (Exception e) {
                                            throw new OperationException(e);
                                        }
                                    });
                            });
                    } catch (OperationException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new OperationException(e);
                    }
                });
        } catch (OperationException e) {
            throw (Exception) e.getCause();
        }
    }
    
    /**
     * 
     * @param context
     * @param dto
     * @param inputStream
     * @return
     * @throws ApplicationException
     * @throws IOException 
     */
    public DeploymentDTO createDeployment(
        AuthenticationContext context, CreateDeploymentDTO dto, InputStream inputStream) 
        throws ApplicationException, IOException, SAXException {

        final ProcessMetadata metadata = new ProcessMetadata();
        final Set<AssetType> assetTypes = new HashSet<>();
        final Map<String, byte[]> assetContents = new HashMap<>();
        
        String unitId;
        Deployment deployment;
        DeploymentStatus status;
        ActiveInstancesPolicy activeInstancesPolicy;
     
        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        // validate dto
        validationService.validate(dto);
        
        // unzip content, validate structure and get deployment metadata
        try {
            ZipUtil.iterate(inputStream, new ZipEntryCallback() {
                @Override
                public void process(InputStream inputStream, ZipEntry entry) throws IOException {
 
                    AssetType assetType;
                    byte[] assetContent;
                    try {
                        assetType = AssetType.valueFor(entry.getName());
                        assetContent = inputStream.readAllBytes();
                        assetTypes.add(assetType);
                        assetContents.put(entry.getName(), assetContent);
                        if (assetType.isMetadata()) {
                            metadata.read(new ByteArrayInputStream(assetContent));
                        }
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException(String.format(
                            "%s: %s", 
                            entry.getName(), 
                            e.getMessage() != null ? 
                                e.getMessage() : 
                                "invalid asset" ));
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            throw new BusinessException(
                BusinessError.ASSET_NOT_VALID, 
                e.getMessage());
        }
        if (!assetTypes.containsAll(AssetType.requiredValues())) {
            throw new BusinessException(
                BusinessError.ASSET_NOT_VALID, 
                String.format(
                    "%s: required asset", 
                    Sets
                        .difference(AssetType.requiredValues(), assetTypes).stream()
                        .map(e -> e.entryName())
                        .collect(Collectors.joining(", "))));
        }
        
        // compute unitId
        unitId = String.format("%s.%s:%s:%s", 
            context.getTenant(), 
            context.getOrganization(),
            metadata.name().trim().replaceAll("\\s", "_"), 
            metadata.version());

        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
             new DefaultTransactionDefinition());
        try {
            // get deployment
            deployment = deploymentRepository.findByUnitId(unitId);
            if (deployment == null) {
                // get status
                status = deploymentStatusRepository.findByUnitId(unitId);
                if (status == null) {
                    // create status
                    status = new DeploymentStatus(unitId);
                }
                // create deloyment
                deployment = new Deployment(
                     unitId, 
                     context.getTenant(),
                     context.getOrganization(),
                     metadata.name(),
                     metadata.version(),
                     metadata.versionNumber(),
                     metadata.processId(),
                     status, 
                     assetContents);
            } else {
                // override
                if (dto.getOverride() == false) {
                    throw new BusinessException(
                        BusinessError.DEPLOYMENT_ALREADY_EXISTS,
                        "Deployment with same name and version already exists");
                }
                deployment.setAssets(assetContents);
            }

            // manage active instances policy
            activeInstancesPolicy = ActiveInstancesPolicy.valueOf(dto.getActiveInstancesPolicy());
            for (Deployment d : Lists.asList(deployment, deployment.getDependantDeployments().toArray(Deployment[]::new))) {
                if (!activeInstancesPolicy.getOperation().apply(d.getUnitId())) {
                    throw new BusinessException(
                        BusinessError.DEPLOYMENT_IN_USE_BY_INSTANCE, 
                        String.format("Deployment in use by active instances (%s failed)", 
                        activeInstancesPolicy));
                }
            }
            
            // share
            if (dto.getShare() != null) {
                shareDeployment(deployment, dto.getShare());
            }

            // configure
            if (dto.getConfiguration() != null) {
                configurationService.createConfiguration(
                    deployment, 
                    context.getOrganization(), 
                    dto.getConfiguration());
            }
            
            // update dependencies
            updateDependencies(deployment);

            // build
            buildDeployment(deployment);

            // update events
            eventService.updateEvents(deployment);
            
            // schedule
            scheduleDeployment(deployment, dto.getActivate());
            
            // store
            deployment = deploymentRepository.save(deployment);
            
            // deploy forms
            formAssetService.deploy(context, assetContents.get(AssetType.forms.entryName()));
            
        } catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        
        // commit
        transactionManager.commit(tx);
        
        // map and return
        return deploymentMapper.map(
            deployment, 
            MappingContext.of(context, Fetcher.of(Deployment.class, null)));
    }
    
    /**
     * 
     * @param context
     * @param pageNumber
     * @param pageSize
     * @param fetch
     * @return
     * @throws ApplicationException 
     */  
    public PageDTO<DeploymentDTO> getDeployments(
        AuthenticationContext context, String processId, Integer pageNumber, Integer pageSize, String orderBy, String fetch) 
        throws ApplicationException {

        return deploymentMapper.map(
            deploymentRepository.findAll(
                context, 
                processId, 
                Pager.of(Deployment.class, pageNumber, pageSize, orderBy)), 
            MappingContext.of(context, Fetcher.of(Deployment.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param deploymentId
     * @return
     * @throws ApplicationException 
     */ 
    public DeploymentDTO getDeployment(AuthenticationContext context, Long deploymentId, String fetch) throws ApplicationException {
        
        Deployment deployment;

        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        deployment = deploymentRepository.findById(context, deploymentId);
        if (deployment == null) {
            throw new NotFoundException();
        }
        
        return deploymentMapper.map(
            deployment, 
            MappingContext.of(context, Fetcher.of(Deployment.class, fetch)));
    }

    /**
     * 
     * @param context
     * @param deploymentId
     * @param response
     * @throws ApplicationException
     * @throws IOException 
     */  
    @Transactional(readOnly = true)    
    public void downloadDeployment(
        AuthenticationContext context, Long deploymentId, HttpServletResponse response) 
        throws ApplicationException, IOException {

        Deployment deployment;
        List<ZipEntrySource> entries;

        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        deployment = deploymentRepository.findById(context, deploymentId);
        if (deployment == null) {
            throw new NotFoundException();
        }
        
        entries = new ArrayList<>();
        for (Asset asset : deployment.getAssets().values()) {
            entries.add(new ByteSource(asset.getName(), asset.getContent().asBytes()));
        }
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", String.format(
            "attachment; filename=\"%s%s.zip\"", 
            deployment.getName(),
            deployment.getVersion()));  
        ZipUtil.pack(
            entries.toArray(new ByteSource[entries.size()]), 
            response.getOutputStream());
    }

    /**
     * 
     * @param context
     * @param deploymentId
     * @param assetName
     * @param response
     * @throws ApplicationException
     * @throws IOException 
     */
    @Transactional(readOnly = true)    
    public void downloadDeploymentAsset(
        AuthenticationContext context, Long deploymentId, String assetName, HttpServletResponse response) 
        throws ApplicationException, IOException {

        Deployment deployment;
        Asset asset;

        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        deployment = deploymentRepository.findById(context, deploymentId);
        if (deployment == null || !deployment.hasAsset(assetName)) {
            throw new NotFoundException();
        }
        
        asset = deployment.getAsset(assetName);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(asset.getType().mediaType().toString());
        response.setHeader("Content-Disposition", String.format(
            "attachment; filename=\"%s\"", 
            asset.getName())); 
        asset.getContent().write(response.getOutputStream());
    }

    /**
     * 
     * @param context
     * @param deploymentId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public DeploymentDTO activateDeployment(
        AuthenticationContext context, Long deploymentId, ActivateDeploymentDTO dto) 
        throws ApplicationException {

        Deployment deployment;

        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        // validate request dto
        validationService.validate(dto);
        
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            deployment = deploymentRepository.findById(context, deploymentId);                    
            if (deployment == null) {
                throw new NotFoundException();
            }

            if (!deployment.isOwned(context)) {
                throw new AuthorizationException();
            }
            
            // manage activate
            deploymentStatusRepository.save(deployment.getStatus().update(null, dto.getActivate()));
            if (dto.getActivate()) {
                // manage deactivatePreviousVersions
                if (dto.getDeactivatePreviousVersions()) {
                    for (Deployment previous : deploymentRepository.findPreviousVersions(deployment)) {
                        deploymentStatusRepository.save(previous.getStatus().update(null, false));
                    }
                }
            }
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
        
        // map and return
        return deploymentMapper.map(
            deployment,
            MappingContext.of(context));
    }

    /**
     * 
     * @param context
     * @param deploymentId
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public DeploymentDTO shareDeployment(
        AuthenticationContext context, Long deploymentId, ShareDeploymentDTO dto) 
        throws ApplicationException {

        Deployment deployment;
 
        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        // validate request dto
        validationService.validate(dto);
        
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            deployment = deploymentRepository.findById(context, deploymentId);                    
            if (deployment == null) {
                throw new NotFoundException();
            }
            
            if (!deployment.isOwned(context)) {
                throw new AuthorizationException();
            }   
            
            // share
            deployment = shareDeployment(deployment, dto.getShare());
            
            // store
            deployment = deploymentRepository.save(deployment);
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
        
        // map and return
        return deploymentMapper.map(
            deployment,
            MappingContext.of(context));
    }
    
    /**
     * 
     * @param context
     * @param deploymentId
     * @param dto
     * @throws ApplicationException 
     */
    public void deleteDeployment(
        AuthenticationContext context, Long deploymentId, DeleteDeploymentDTO dto) 
        throws ApplicationException {
        
        Deployment deployment;
        ActiveInstancesPolicy activeInstancesPolicy;

        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        // validate request dto
        validationService.validate(dto);
        
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // get deployment
            deployment = deploymentRepository.findById(context, deploymentId);
            if (deployment == null) {
                throw new NotFoundException();
            }
         
            if (!deployment.isOwned(context)) {
                throw new AuthorizationException();
            }
            
            if (!deployment.getDependantDeployments().isEmpty()) {
                throw new BusinessException(
                    BusinessError.DEPLOYMENT_IN_USE_BY_DEPENDANT, 
                    "Deployment is a dependency for other deployments");
            }

            // manage active instance policy
            activeInstancesPolicy = ActiveInstancesPolicy.valueOf(dto.getActiveInstancesPolicy());
            if (!activeInstancesPolicy
                .getOperation()
                .apply(deployment.getUnitId())) {
                throw new BusinessException(
                    BusinessError.DEPLOYMENT_IN_USE_BY_INSTANCE, 
                    String.format("Deployment in use by active instances (%s failed)", 
                    activeInstancesPolicy));
            }
            
            // update deployment status
            deploymentStatusRepository.save(deployment.getStatus().update(false, null));
            
            // delete
            deploymentRepository.delete(deployment);
            deploymentRepository.flush();
            eventRepository.deleteOrphans();
        }
        catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
    }
    
    /**
     * 
     * @param unitId
     * @return 
     */
    public DeploymentUnit getDeploymentUnit(String unitId) {
        return isDeployed(unitId) ? 
            (DeploymentUnit) getDeployedUnit(unitId).getDeploymentUnit() : 
            null;
    }

    /**
     * 
     * @param tenant
     * @param organization
     * @return 
     */
    public Collection<DeploymentUnit> getDeploymentUnitsByOrganization(
        String tenant, String organization) {
        return getDeployedUnits()
            .stream()
            .map(u -> (DeploymentUnit) u.getDeploymentUnit())  
            .filter(u -> 
                u.getTenant().equals(tenant) && 
                u.getOrganization().equals(organization))
            .collect(Collectors.toSet());
    }

    /**
     * 
     * @return 
     */
    public Collection<DeploymentUnit> getDeploymentUnits() {
        return getDeployedUnits()
            .stream()
            .map(u -> (DeploymentUnit) u.getDeploymentUnit())
            .collect(Collectors.toSet());
    }

    /**
     * 
     * @param unit 
     */
    @Override
    public void deploy(org.jbpm.services.api.model.DeploymentUnit unit) {
        
        Deployment deployment;
        
        if (!(unit instanceof DeploymentUnit)) {
            throw new IllegalArgumentException();
        }
        
        if (isDeployed(unit.getIdentifier())) {
            log.warn(String.format("Deployment unit %s is already deployed",
                 unit.getIdentifier()));
            return;
        }

        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // load
            deployment = deploymentRepository.findByUnitId(unit.getIdentifier());
            // build
            buildDeployment(deployment);
            // deploy
            super.deploy(unit);
        } catch (Throwable t) {
            log.error(String.format("Deploy of unit %s failed: %s",
                 unit.getIdentifier(), t.getMessage()));
            // rollback
            transactionManager.rollback(tx);
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t);
            }
        }
        // commit
        transactionManager.commit(tx);
        
        log.info(String.format("Deploy of unit %s completed",
             unit.getIdentifier()));
    }   

    /**
     * 
     * @param unit
     * @param preUndeployOperation 
     */
    @Override
    public void undeploy(
        org.jbpm.services.api.model.DeploymentUnit unit, 
        Function<org.jbpm.services.api.model.DeploymentUnit, Boolean> preUndeployOperation) {

        if (!(unit instanceof DeploymentUnit)) {
            throw new IllegalArgumentException();
        }

        if (!isDeployed(unit.getIdentifier())) {
            log.warn(String.format("Deployment unit %s is already undeployed",
                 unit.getIdentifier()));
            return;
        }
        
        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
            new DefaultTransactionDefinition());
        try {
            // undeploy
            super.undeploy(unit, preUndeployOperation);
        } catch (Throwable t) {
            log.error(String.format("Undeploy of unit %s failed: %s",
                 unit.getIdentifier(), t.getMessage()));
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
        
        log.info(String.format("Undeploy of unit %s completed",
             unit.getIdentifier()));
    }

    /**
     * 
     * @param unit
     */
    @Override
    public void undeploy(org.jbpm.services.api.model.DeploymentUnit unit) {
        undeploy(unit, PreUndeployOperations.doNothing());
    }
       
    /**
     * 
     * @param unitId 
     */
    @Override
    public void activate(String unitId) {
        
        if (!isDeployed(unitId)) {
            log.warn(String.format("Deployment unit %s is not deployed",
                 unitId));
            return;
        }
        if (getDeployedUnit(unitId).isActive()) {
            log.warn(String.format("Deployment unit %s is already activated",
                 unitId));
            return;
        }
        
        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
             new DefaultTransactionDefinition());
        try {
            // activate
            super.activate(unitId);
        } catch (Throwable t) {
            log.error(String.format("Activation of unit %s failed: %s",
                 unitId, t.getMessage()));
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);

        log.info(String.format("Activation of unit %s completed",
             unitId));
    }

    /**
     * 
     * @param unitId 
     */
    @Override
    public void deactivate(String unitId) {
        
        if (!isDeployed(unitId)) {
            log.warn(String.format("Deployment unit %s is not deployed",
                 unitId));
            return;
        }
        if (!getDeployedUnit(unitId).isActive()) {
            log.warn(String.format("Deployment unit %s is already deactivated",
                 unitId));
            return;
        }
        
        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
             new DefaultTransactionDefinition());
        try {
            // deactivate
            super.deactivate(unitId);
        } catch (Throwable t) {
            log.error(String.format("Deactivation of unit %s failed: %s",
                 unitId, t.getMessage()));
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
        
        log.info(String.format("Deactivation of unit %s completed",
             unitId));
    }
    
    /**
     * 
     * @param context
     * @param deployment
     * @param share
     * @throws ApplicationException 
     */
    private Deployment shareDeployment(Deployment deployment, boolean share) 
        throws ApplicationException {
        
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }

        // share
        if (share && !deployment.isShared()) {
            // check if sharable
            if (deploymentRepository.existsShared(deployment)) {
                throw new BusinessException(
                    BusinessError.DEPLOYMENT_ALREADY_SHARED,
                    "Shared deployment with same name and version already exists");
            } else {
                deployment.setShared(true);
            } 
        // unshare    
        } else if (!share && deployment.isShared()) {
            // check if unsharable
            final String organization = deployment.getOrganization();
            if (deployment.getDependantDeployments()
                .stream()
                .anyMatch(d -> !d.getOrganization().equals(organization))) {
                throw new BusinessException(
                    BusinessError.DEPLOYMENT_IN_USE_BY_DEPENDANT, 
                    "Deployment is a dependency for other deployments");
            } else {
                deployment.setShared(false);
            }
        }
        
        return deployment;
    }
    
    /**
     * 
     * @param deployment
     * @return
     * @throws ValidationException 
     */
    private KieModule buildDeployment(Deployment deployment)
        throws ApplicationException {
        
        KieServices kServices;
        KieFileSystem kFileSystem;
        KieBuilder kBuilder;

        kServices = KieServices.Factory.get();
        kFileSystem = kServices.newKieFileSystem();
        kBuilder = kServices.newKieBuilder(kFileSystem);

        // join transaction
        TransactionStatus tx = transactionManager.getTransaction(
             new DefaultTransactionDefinition());
        try {
            kFileSystem.generateAndWritePomXML(kServices.newReleaseId(
                deployment.getGroupId(),
                deployment.getArtifactId(),
                deployment.getVersion()));

            for (Deployment unitDeployment : deployment.getUnitDeployments()) {
                kFileSystem.write(String.format("%s/%s.%s", 
                        ResourceType.BPMN2.getDefaultPath(),
                        unitDeployment.getProcessId(),
                        ResourceType.BPMN2.getDefaultExtension()),
                    ResourceFactory
                        .newByteArrayResource(unitDeployment.getDefinition().getContent().asBytes())
                        .setResourceType(ResourceType.BPMN2));
            }

            kBuilder.buildAll();
            
            if (kBuilder.getResults().hasMessages(Message.Level.ERROR)) {
                throw new BusinessException(
                    BusinessError.DEPLOYMENT_BUILD_ERROR, 
                    kBuilder
                        .getResults()
                        .getMessages(Message.Level.ERROR)
                        .stream()
                        .map(message -> message.getText())
                        .collect(Collectors.toList()));
            }
        } catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
 
        return kBuilder.getKieModule();
    }    
    
    /**
     * 
     * @param deployment
     * @param active 
     */
    protected void scheduleDeployment(Deployment deployment, Boolean active) {
        
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        deploymentStatusRepository.save(deployment.getStatus().update(true, active));
        deployment.getDependantDeployments().forEach(dependant -> {
            deploymentStatusRepository.save(dependant.getStatus().update(true, null));
        });    
    }
    
    /**
     *
     * @param auditEventBuilder
     * @param kContainer
     * @param deploymentUnit
     * @return
     */
    @Override
    protected RegisterableItemsFactory getRegisterableItemsFactory(
        AuditEventBuilder auditEventBuilder, 
        KieContainer kContainer, 
        KModuleDeploymentUnit deploymentUnit) {
        ((DefaultRegisterableItemsFactory) registerableItemsFactory).setAuditBuilder(auditEventBuilder);
        return registerableItemsFactory;
    }

    /**
     * 
     * @param deployment
     * @throws ApplicationException 
     */
    private void updateDependencies(Deployment deployment) 
        throws IOException, SAXException, ApplicationException {
        
        RuleFlowProcess process;
        SubProcessNode subProcessNode;
        String processId;
        Deployment dependency;
        DependencyReference reference;
        Map<String, DependencyReference> references;
        
        process = parseDefinition(deployment);
        
        references = new HashMap<>();
        for (Node node : process.getNodesRecursively()) {
            if (node instanceof SubProcessNode) {
                subProcessNode = (SubProcessNode) node;
                if (subProcessNode.isCallActivity()) {
                    processId = subProcessNode.getProcessId() != null ?
                        subProcessNode.getProcessId() :
                        subProcessNode.getProcessName();
                    for (Configuration configuration : deployment.getConfigurations()) {
                        try {
                            reference = resolveDependencyReference(
                                processId, 
                                configuration.getSettings().getDefaultInput());
                            if (!process.getId().equals(reference.getProcessId())) {
                                if (!references.containsKey(reference.getProcessId())) {
                                    dependency = deploymentRepository.findByProcessId(
                                        deployment.getTenant(),
                                        deployment.getOrganization(),
                                        reference.getProcessId());
                                    if (dependency != null) {
                                        reference.setDependency(dependency);
                                        references.put(reference.getProcessId(), reference);                                        
                                    } else {
                                        throw new IllegalArgumentException("Deployment not found");
                                    }
                                } else {
                                    references
                                        .get(reference.getProcessId())
                                        .addVariableKeys(reference.getVariableKeys());
                                }
                            }
                        } catch (IllegalArgumentException e) {
                            throw new BusinessException(
                                BusinessError.DEPLOYMENT_DEPENDENCY_NOT_FOUND, 
                                String.format(
                                    "Unable to resolve dependency for call activity '%s' referencing process '%s': %s", 
                                    subProcessNode.getName(),
                                    processId, 
                                    e.getMessage()));
                        }
                    }
                }
            }
        }
        deployment.setDependencies(references
            .values()
            .stream()
            .map(r -> new DeploymentDependency(
                deployment, 
                r.getDependency(), 
                r.getVariableKeys()))
            .collect(Collectors.toSet()));
    }

    /**
     * 
     * @param processId
     * @param variables
     * @return 
     */
    private DependencyReference resolveDependencyReference(
        String processId, Map<String, Object> variables) {
        
        Matcher matcher;
        Map<String, String> replacements;
        String variableKey;
        Object variableValue;

        matcher = PatternConstants.PARAMETER_MATCHER.matcher(processId);
        replacements = new HashMap<String, String>();
        while (matcher.find()) {
            variableKey = matcher.group(1);
            if (!replacements.containsKey(variableKey)) {
                try {
                    variableValue = MVELSafeHelper.getEvaluator().eval(variableKey, variables);
                    replacements.put(
                        variableKey,  
                        variableValue != null ? 
                            variableValue.toString() : 
                            "");
                } catch (Throwable t) {
                    throw new IllegalArgumentException(t.getMessage());
                }
            }
        }
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            processId = processId.replace(
                String.format("#{%s}", entry.getKey()), 
                entry.getValue());
        }
        return new DependencyReference(
            processId, 
            new HashSet<>(replacements.keySet()));
    }
    
    /**
     * 
     * @param definition
     * @return
     * @throws ApplicationException 
     */
    private RuleFlowProcess parseDefinition(Deployment deployment) 
        throws IOException, SAXException, ApplicationException {
        
        List<Process> processes;

        processes = xmlProcessReader().read(
            deployment.getDefinition().getContent().asInputStream());
        for (Process process : processes) {
            if (process instanceof RuleFlowProcess &&
                process.getId().equals(deployment.getProcessId())) {
                return (RuleFlowProcess) process;
            }
        }
        throw new BusinessException(
            BusinessError.ASSET_NOT_VALID, 
            String.format(
                "%s: invalid declared processId, must be '%s'", 
                AssetType.definition.entryName(),
                deployment.getProcessId()));
    }
    
    /**
     * 
     * @return 
     */
    private XmlProcessReader xmlProcessReader() {
        
        SemanticModules modules = new SemanticModules();
        modules.addSemanticModule(new BPMNSemanticModule());
        modules.addSemanticModule(new BPMNExtensionsSemanticModule());
        modules.addSemanticModule(new BPMNDISemanticModule());

        return new XmlProcessReader(modules, getClass().getClassLoader());
    }      
    
    /**
     * 
     */
    private class DependencyReference {
        
        private String processId;
        private Set<String> variableKeys;
        private Deployment dependency;

        public DependencyReference(String processId, Set<String> variableKeys) {
            this.processId = processId;
            this.variableKeys = variableKeys;
        }

        public String getProcessId() {
            return processId;
        }

        public Deployment getDependency() {
            return dependency;
        }

        public void setDependency(Deployment dependency) {
            this.dependency = dependency;
        }

        public Set<String> getVariableKeys() {
            return variableKeys;
        }

        public void addVariableKeys(Set<String> variableKeys) {
            this.variableKeys.addAll(variableKeys);
        }
    }
    
    /**
     * Synchronize deployment persistent status with in memory unit
     */
    private class Synchronizer implements Runnable {

        private Date ts;
        private int batchSize;
        private boolean async;
        private CountDownLatch latch;
        
        private Synchronizer(ApplicationProperties applicationProperties) {
            ts = new Date(0L);
            batchSize = applicationProperties.deploySynchronizerBatchSize();
            async = applicationProperties.deploySynchronizerAsync();
        }
        
        @Override
        public void run() {
            
            List<DeploymentStatus> statuses;
            
            while ((statuses = deploymentStatusRepository.findUpdates(ts, batchSize)).size() > 0) {
                if (async) {
                    latch = new CountDownLatch(statuses.size());
                }
                for (DeploymentStatus status: statuses) {
                    if (async) {
                        taskExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                synchronize(status);
                            }
                        });
                    } else {
                        synchronize(status);
                    }
                    ts = status.getUpdateTs();
                }
                if (async) {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }                    
                }
            }
        }

        private void synchronize(DeploymentStatus status) {
            
            DeploymentUnit unit;
            boolean done;
            int retries, maxRetries;
            
            done = false;
            retries = 0;
            maxRetries = applicationProperties.deploySynchronizerMaxRetries();
            unit = getDeploymentUnit(status.getUnitId());
            while (!done && retries <= maxRetries) {
                try {
                    // deploy
                    if (status.isDeployed() && unit == null) {
                        deploy(newUnit(status)); 
                    }
                    // redeploy
                    else if (status.isDeployed() && unit != null && unit.getDeployTs().before(status.getDeployTs())) {
                        undeploy(unit);
                        deploy(newUnit(status)); 
                    }
                    // activate/deactivate
                    else if (status.isDeployed() && unit != null && unit.getDeployTs().equals(status.getDeployTs())) {
                        if (status.isActive() && !unit.isActive()) {
                            activate(unit.getIdentifier());
                            unit.setActive(true);
                        } else if (!status.isActive() && unit.isActive()) {
                            deactivate(unit.getIdentifier());
                            unit.setActive(false);
                        }
                    }
                    // undeploy
                    else if (!status.isDeployed() && unit != null) {
                        undeploy(unit);
                    }
                    done = true;
                } catch (Throwable t) {
                    retries++;
                    if (retries > maxRetries) {
                        log.error(String.format(
                            "Unexpected error synchronizing status of unit %s: %s", 
                            status.getUnitId(), 
                            t.getMessage()), t); 
                    }
                }
            }
            if (async) {
                latch.countDown();
            }
        }
        
        private DeploymentUnit newUnit(DeploymentStatus status) {

            DeploymentUnit unit;
            
            unit = new DeploymentUnit(
                status.getGroupId(),
                status.getArtifactId(),
                status.getVersion(),
                status.getDeployTs());
            unit.setStrategy(RuntimeStrategy.PER_PROCESS_INSTANCE);
            unit.setDeploymentDescriptor(deploymentDescriptor);
            unit.setDeployed(true);
            unit.setActive(status.isActive()); 
            return unit;
        }
    }
}
