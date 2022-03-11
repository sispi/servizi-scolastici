/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import freemarker.template.TemplateException;
import it.filippetti.ks.api.bpm.dto.ActivateConfigurationDTO;
import it.filippetti.ks.api.bpm.dto.ConfigurationDTO;
import it.filippetti.ks.api.bpm.dto.CreateConfigurationDTO;
import it.filippetti.ks.api.bpm.dto.CreateFormViewDTO;
import it.filippetti.ks.api.bpm.dto.PageDTO;
import it.filippetti.ks.api.bpm.dto.UpdateConfigurationDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.exception.AuthorizationException;
import it.filippetti.ks.api.bpm.exception.BusinessError;
import it.filippetti.ks.api.bpm.exception.BusinessException;
import it.filippetti.ks.api.bpm.exception.NotFoundException;
import it.filippetti.ks.api.bpm.exception.ValidationException;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.mapper.dto.ConfigurationMapper;
import it.filippetti.ks.api.bpm.model.Asset;
import it.filippetti.ks.api.bpm.model.AssetType;
import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.model.Configuration;
import it.filippetti.ks.api.bpm.model.ConfigurationAuthorization;
import it.filippetti.ks.api.bpm.model.Deployment;
import it.filippetti.ks.api.bpm.model.Fetcher;
import it.filippetti.ks.api.bpm.model.Pager;
import it.filippetti.ks.api.bpm.model.PermissionType;
import it.filippetti.ks.api.bpm.repository.ConfigurationRepository;
import it.filippetti.ks.api.bpm.repository.DeploymentRepository;
import it.filippetti.ks.api.bpm.repository.InstanceRepository;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.drools.core.util.MVELSafeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class ConfigurationService {
    
    private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class);

    @Autowired    
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private ValidationService validationService;

    @Autowired
    private FormAssetService formAssetService;
    
    @Autowired
    private DeploymentService deploymentService;
    
    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private InstanceRepository instanceRepository;
    
    @Autowired
    private ConfigurationMapper configurationMapper;
    
    /**
     * 
     * @param context
     * @param deploymentId
     * @param dto
     * @return
     * @throws ApplicationException
     * @throws IOException 
     */
    @Transactional(rollbackFor = Exception.class)
    public ConfigurationDTO createConfiguration(
        AuthenticationContext context, Long deploymentId, CreateConfigurationDTO dto) 
        throws ApplicationException, IOException {
        
        Deployment deployment;
        Configuration configuration;
        
        // validate request dto
        validationService.validate(dto);

        deployment = deploymentRepository.findById(context, deploymentId);
        if (deployment == null) {
            throw new NotFoundException();
        }

        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        configuration = createConfiguration(deployment, context.getOrganization(), dto);

        // store
        configuration = configurationRepository.save(configuration);

        // map and return        
        return configurationMapper.map(
            configuration, 
            MappingContext.of(context, Fetcher.of(Configuration.class, null)));
    }

    /**
     * 
     * @param context
     * @param pageNumber
     * @param pageSize
     * @param fetch
     * @return
     * @throws ValidationException 
     */
    public PageDTO<ConfigurationDTO> getConfigurations(
        AuthenticationContext context, 
        String processId,
        boolean runnableOnly,
        Integer pageNumber, Integer pageSize, String orderBy, 
        String fetch) 
        throws ValidationException {
        
        return configurationMapper.map(
            configurationRepository.findAll(
                context, 
                processId,
                runnableOnly, 
                Pager.of(Configuration.class, pageNumber, pageSize, orderBy)), 
            MappingContext.of(
                context, 
                Fetcher.of(Configuration.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param configurationId
     * @return
     * @throws ApplicationException 
     */ 
    public ConfigurationDTO getConfiguration(AuthenticationContext context, Long configurationId, String fetch)
        throws ApplicationException {
        
        Configuration configuration;
        
        configuration = configurationRepository.findById(context, configurationId);
        if (configuration == null) {
            throw new NotFoundException();
        }
        return configurationMapper.map(
            configuration,
            MappingContext.of(context, Fetcher.of(Configuration.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param configurationId
     * @param assetName
     * @param response
     * @throws ApplicationException
     * @throws IOException 
     */  
    @Transactional(readOnly = true)
    public void downloadConfigurationAsset(
        AuthenticationContext context, Long configurationId, String assetName, HttpServletResponse response)
        throws ApplicationException, IOException {
        
        Configuration configuration;
        Asset asset;
        
        configuration = configurationRepository.findById(context, configurationId);
        if (configuration == null || !configuration.hasAsset(assetName)) {
            throw new NotFoundException();
        }
        
        asset = configuration.getAsset(assetName);
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
     * @param configurationid
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    @Transactional(rollbackFor = Exception.class)
    public ConfigurationDTO activateConfiguration(
        AuthenticationContext context, Long configurationid, ActivateConfigurationDTO dto) 
        throws ApplicationException {
        
        Configuration configuration;
        
        validationService.validate(dto);
        
        configuration = configurationRepository.findById(context, configurationid);
        if (configuration == null) {
            throw new NotFoundException();
        }
      
        if (!configuration.hasAuthorization(context, ConfigurationAuthorization.Configure)) {
            throw new AuthorizationException();
        } 
        
        configuration.setActive(dto.getActivate());
        
        if (dto.getDeactivatePreviousVersions() && configuration.isActive()) {
            configurationRepository.deactivatePreviousVersions(configuration);
        }
        
        configuration = configurationRepository.save(configuration);
        
        return configurationMapper.map(
            configuration, 
            MappingContext.of(context));
    }
    
    /**
     * 
     * @param context
     * @param configurationid
     * @param dto
     * @return
     * @throws ApplicationException
     */
    public ConfigurationDTO updateConfiguration(
        AuthenticationContext context, Long configurationid, UpdateConfigurationDTO dto) 
        throws ApplicationException, IOException {
        
        Configuration configuration;
        Asset asset;
        
        validationService.validate(dto);
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
             new DefaultTransactionDefinition());
        try {
            configuration = configurationRepository.findById(context, configurationid);
            if (configuration == null) {
                throw new NotFoundException();
            }

            if (!configuration.hasAuthorization(context, ConfigurationAuthorization.Configure)) {
                throw new AuthorizationException();
            } 

            configuration = updateConfiguration(configuration, dto);

            configuration = configurationRepository.save(configuration);

        } catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        
        // commit
        transactionManager.commit(tx);
        
        // map and return
        return configurationMapper.map(
            configuration,
            MappingContext.of(context, Fetcher.of(Configuration.class, null)));
    }
    
    /**
     * 
     * @param context
     * @param configurationId
     * @param dto
     * @throws ApplicationException 
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteConfiguration(AuthenticationContext context, Long configurationId) 
        throws ApplicationException {

        Configuration configuration;
        
        configuration = configurationRepository.findById(context, configurationId);
        if (configuration == null) {
            throw new NotFoundException();
        }
        
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        if (!instanceRepository.findActiveReferencesByConfiguration(configuration).isEmpty()) {
            throw new BusinessException(
                BusinessError.CONFIGURATION_IN_USE_BY_INSTANCE, 
                "Configuration in use by active instances");
        }
    
        configurationRepository.delete(configuration);
    }
    
    /**
     * 
     * @param context
     * @param configurationId
     * @param name
     * @param type
     * @param response
     * @throws ApplicationException
     * @throws IOException 
     */  
    public void getConfigurationFormTemplate(
        AuthenticationContext context, 
        Long configurationId,
        String name,
        String type,
        HttpServletResponse response)
        throws ApplicationException, IOException {
        
        Configuration configuration;
        AssetType assetType;

        // get configuration/form asset type
        configuration =  configurationRepository.findById(context, configurationId);
        assetType = AssetType.configurationForm(name);
        if (configuration == null || assetType == null || configuration.getAsset(assetType) == null) {
            throw new NotFoundException();
        }

        // get template
        formAssetService
            .get(configuration.getAsset(assetType))
            .getTemplate(context, type, response);       
    }
    
    /**
     * 
     * @param context
     * @param configurationId
     * @param name
     * @param response
     * @throws ApplicationException
     * @throws IOException 
     */
    public void getConfigurationFormScript(
        AuthenticationContext context, 
        Long configurationId,
        String name,
        HttpServletResponse response)
        throws ApplicationException, IOException {
        
        Configuration configuration;
        AssetType assetType;

        // get configuration/form asset type
        configuration =  configurationRepository.findById(context, configurationId);
        assetType = AssetType.configurationForm(name);
        if (configuration == null || assetType == null || configuration.getAsset(assetType) == null) {
            throw new NotFoundException();
        }

        // get template
        formAssetService
            .get(configuration.getAsset(assetType))
            .getScript(context, response);       
    }
    
    /**
     * 
     * @param context
     * @param configurationId
     * @param name
     * @param dto
     * @param response
     * @throws ApplicationException
     * @throws IOException
     * @throws TemplateException 
     */ 
    public void createConfigurationFormView(
        AuthenticationContext context, 
        Long configurationId, 
        String name,
        CreateFormViewDTO dto,
        HttpServletResponse response)
        throws ApplicationException, IOException, TemplateException {
        
        Configuration configuration;
        AssetType assetType;
        Map<String, Object> model, defaultInput;
        
        // get configuration/form asset type
        configuration =  configurationRepository.findById(context, configurationId);
        assetType = AssetType.configurationForm(name);
        if (configuration == null || assetType == null || configuration.getAsset(assetType) == null) {
            throw new NotFoundException();
        }

        // get default input model
        defaultInput = configuration.getSettings().getDefaultInput();
        
        // get context model
        switch (assetType) {
            case form_configure:
                model = configuration
                    .getAsset(AssetType.project)
                    .getContent()
                    .asJsonAssetContent()
                    .getMap("main", "settings", "flowBuffer")
                    .entrySet()
                    .stream()
                    .filter(e -> Boolean.TRUE.equals(((Map) e.getValue()).getOrDefault("isConfig", false)))
                    .filter(e -> defaultInput.get(e.getKey()) != null)
                    .collect(Collectors.toMap(e -> e.getKey(), e -> defaultInput.get(e.getKey())));                        
                break;
            case form_start:
                model = configuration
                    .getAsset(AssetType.project)
                    .getContent()
                    .asJsonAssetContent()
                    .getMap("main", "settings", "flowBuffer")
                    .entrySet()
                    .stream()
                    .filter(e -> Boolean.TRUE.equals(((Map) e.getValue()).getOrDefault("isInput", false)))
                    .filter(e -> defaultInput.get(e.getKey()) != null)                        
                    .collect(Collectors.toMap(e -> e.getKey(), e -> defaultInput.get(e.getKey())));                        
                break;
            default:
                throw new IncompatibleClassChangeError();
        }
        // apply model overrides
        model.putAll(dto.getModelOverrides());

        // create view        
        formAssetService
            .get(configuration.getAsset(assetType))
            .createView(
                context, 
                dto.getType(),
                dto.getOptions(),
                model, 
                response);      
    }
    
    /**
     * 
     * @param context
     * @param deployment
     * @param dto
     * @return
     * @throws IOException
     * @throws ApplicationException 
     */
    protected Configuration createConfiguration(
        Deployment deployment, String organization, CreateConfigurationDTO dto) 
        throws ApplicationException, IOException {
        
        Configuration configuration, previousConfiguration;
        
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        // get configuration
        configuration = deployment.getConfiguration(organization, dto.getProfile());  
        if (configuration == null) {
            // add configuration
            configuration = deployment.addConfiguration(organization, dto.getProfile());  
        } else {
            if (!dto.getOverride()) {
                throw new BusinessException(
                    BusinessError.CONFIGURATION_ALREADY_EXISTS, 
                    "Configuration for profile already exists");
            }
            // override assets
            configuration.setAssets(deployment);
        }
        // merge
        if (dto.getMerge()) {
            previousConfiguration = configurationRepository.findPreviousVersion(configuration);
            if (previousConfiguration != null) {
                try {
                    configuration.merge(previousConfiguration);
                } catch (InvalidObjectException e) {
                    throw new BusinessException(
                        BusinessError.ASSET_NOT_VALID,
                        String.format(
                            "%s: %s", 
                            AssetType.settings.entryName(), 
                            e.getMessage()));
                }
            }                
        }
        // setup
        try {
            configuration.setup();
        } catch (InvalidObjectException e) {
            throw new BusinessException(
                BusinessError.ASSET_NOT_VALID,
                String.format(
                    "%s: %s", 
                    AssetType.settings.entryName(), 
                    e.getMessage()));
        }
        // activate
        if (dto.getActivate() != null) {
            configuration.setActive(dto.getActivate());
        }
        if (dto.getDeactivatePreviousVersions() && configuration.isActive()) {
            configurationRepository.deactivatePreviousVersions(configuration);
        }
        // update
        configuration = updateConfiguration(configuration, dto);
        
        return configuration;
    }
    
    /**
     * 
     * @param configuration
     * @param dto
     * @return
     * @throws ApplicationException
     * @throws IOException 
     */
    protected Configuration updateConfiguration(Configuration configuration, UpdateConfigurationDTO dto) 
        throws ApplicationException, IOException {
        
        Asset asset;
        boolean checkDependencies, scheduleDeployment;
        Map<String, Object> oldVariables, newVariables;
        Object oldValue, newValue;
        Set<String> variableKeys;
        
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException();
        }
        
        checkDependencies = false;
        scheduleDeployment = false;
        oldVariables = null;
        
        if (dto.getRunnable() != null) {
            configuration.setRunnable(dto.getRunnable());
        }
        if (dto.getSearchable()!= null) {
            configuration.setSearchable(dto.getSearchable());
        }
        if (dto.getCategory() != null) {
            configuration.setCategory(dto.getCategory());
        }
        for (Map.Entry<String, Set<String>> entry : dto.getPermissions().entrySet()) {
            for (String identity : entry.getValue()) {
                if (identityService
                        .getActor(configuration.getDeployment().getTenant(), identity)
                        .isEmpty()) {
                    throw new BusinessException(
                        BusinessError.CONFIGURATION_PERMISSION_NOT_VALID, 
                        String.format("Invalid configuration permission identity '%s'", identity));                }
            }
            configuration.setPermissions(
                PermissionType.valueOf(entry.getKey()),
                entry.getValue());
        }
        if (dto.getRetentionPolicy() != null) {
            configuration.getRetentionPolicy().update(
                dto.getRetentionPolicy().getDays(),
                dto.getRetentionPolicy().getClean());
        }
        if (dto.getDefaultInput() != null) {
            checkDependencies = true;
            oldVariables = configuration.getSettings().getDefaultInput();
            configuration.getSettings().setDefaultInput(dto.getDefaultInput()).flush();
            
        }
        for (Map.Entry<String, Object> entry : dto.getAssets().entrySet()) {
            asset = configuration.getAsset(entry.getKey());
            try {
                if (asset == null) {
                    throw new IllegalArgumentException();
                }
                if (asset.getType().isJson()) {
                    if (asset.getType() == AssetType.settings && !checkDependencies) {
                        checkDependencies = true;
                        oldVariables = configuration.getSettings().getDefaultInput();
                    }
                    asset.getContent().update((Map) entry.getValue());
                } else {
                    asset.getContent().update((String) entry.getValue());
                }
            } catch (IllegalArgumentException | ClassCastException | IOException e) {
                throw new BusinessException(
                    BusinessError.ASSET_NOT_VALID, 
                    String.format(
                        "%s: invalid asset", 
                        entry.getKey()));
            }
        }
        if (checkDependencies) {
            newVariables = configuration.getSettings().getDefaultInput();
            variableKeys = configuration
                .getDeployment()
                .getDependencies()
                .stream()
                .map(d -> d.getVariableKeys())
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
            for (String key : variableKeys) {
                try {
                    oldValue = MVELSafeHelper.getEvaluator().eval(key, oldVariables); 
                } catch (Throwable t) {
                    log.warn(String.format(
                        "Incompatible buffer default input value detected for key '%s' (%s)", 
                        key,
                        t.getMessage()));
                    scheduleDeployment = true;
                    break;
                }
                try {
                    newValue = MVELSafeHelper.getEvaluator().eval(key, newVariables); 
                } catch (Throwable t) {
                    throw new BusinessException(
                        BusinessError.ASSET_NOT_VALID, 
                        String.format(
                            "%s: cannot evaluate buffer default input key '%s' (%s)", 
                            AssetType.settings.entryName(),
                            key, 
                            t.getMessage()));
                }
                if (!Objects.equals(oldValue, newValue)) {
                    scheduleDeployment = true;
                    break;
                }
            }
            if (scheduleDeployment) {
                deploymentService.scheduleDeployment(configuration.getDeployment(), null);
            }
        }
        return configuration;
    }    
}
