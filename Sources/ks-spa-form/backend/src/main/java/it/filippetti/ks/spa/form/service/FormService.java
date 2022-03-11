/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Charsets;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import it.filippetti.ks.spa.form.configuration.ApplicationProperties;
import it.filippetti.ks.spa.form.configuration.PreviewConfiguration;
import it.filippetti.ks.spa.form.dto.BackupDTO;
import it.filippetti.ks.spa.form.dto.CreateFormDTO;
import it.filippetti.ks.spa.form.dto.CreateFormViewDTO;
import it.filippetti.ks.spa.form.dto.FormDTO;
import it.filippetti.ks.spa.form.dto.OperationDTO;
import it.filippetti.ks.spa.form.dto.PageDTO;
import it.filippetti.ks.spa.form.dto.UpdateFormDTO;
import it.filippetti.ks.spa.form.exception.ApplicationException;
import it.filippetti.ks.spa.form.exception.AuthorizationException;
import it.filippetti.ks.spa.form.exception.BusinessError;
import it.filippetti.ks.spa.form.exception.BusinessException;
import it.filippetti.ks.spa.form.exception.NotFoundException;
import it.filippetti.ks.spa.form.mapper.MappingContext;
import it.filippetti.ks.spa.form.mapper.dto.BackupsMapper;
import it.filippetti.ks.spa.form.mapper.dto.FormMapper;
import it.filippetti.ks.spa.form.model.AuthenticationContext;
import it.filippetti.ks.spa.form.model.Backup;
import it.filippetti.ks.spa.form.model.Fetcher;
import it.filippetti.ks.spa.form.model.Form;
import it.filippetti.ks.spa.form.model.Pager;
import it.filippetti.ks.spa.form.repository.FormRepository;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class FormService {
    
    private static final Logger log = LoggerFactory.getLogger(FormService.class);
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("templateObjectMapper")
    private ObjectMapper templateObjectMapper;

    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private PreviewConfiguration previewConfiguration;
    
    @Autowired
    private ValidationService validationService;

    @Autowired
    private OperationService operationService;
    
    @Autowired
    private FormRepository formRepository;

    @Autowired
    private FormMapper formMapper;

    @Autowired
    private BackupsMapper backupsMapper;

    private Configuration configuration;
    
    private Set<String> templateTypes;
    
    private Cache<String, Template> templateCache;

    private ObjectNode emptyDefinition;
    
    @PostConstruct
    public void init() {
        try {
            // init freemarker configurations
            configuration = new Configuration(Configuration.VERSION_2_3_30);
            configuration.setTemplateLoader(
                StringUtils.isEmpty(applicationProperties.ftlPath()) ?
                    new ClassTemplateLoader(FormService.class.getClassLoader(), "ftl") :
                    new FileTemplateLoader(new File(applicationProperties.ftlPath())));
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setLogTemplateExceptions(false);
            configuration.setWrapUncheckedExceptions(true);
            configuration.setFallbackOnNullLoopVariable(false);             
            configuration.setWhitespaceStripping(true);

            // init template types
            templateTypes = Set.of(
                "vue"
            );

            // init template cache
            templateCache = CacheBuilder.newBuilder()
                .initialCapacity(applicationProperties.templateCacheMinSize())
                .maximumSize(applicationProperties.templateCacheMaxSize())
                .concurrencyLevel(8)
                .build();

            // init default empty definition
            emptyDefinition = (ObjectNode) objectMapper
                .readTree(new ClassPathResource("json/definition.empty.json")    
                .getInputStream());
        } catch (IOException e) {
            throw new ExceptionInInitializerError();
        }
    }
    
    /**
     * 
     * @param context
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public FormDTO createForm(
        AuthenticationContext context, CreateFormDTO dto) 
        throws ApplicationException {
        
        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        // validate dto
        validationService.validate(dto);
        
        return operationService.execute(dto.getOperationId(), () -> {
    
            Form form;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                 new DefaultTransactionDefinition());
            try {
                // get form if exists
                if (dto.getId() != null) {
                    form = formRepository.findById(context, dto.getId());
                } else {
                    form = null;
                }
                // check name
                if (formRepository.existsByName(context, dto.getName(), dto.getId())) {
                    throw new BusinessException(
                        BusinessError.FORM_ALREADY_EXISTS, 
                        "Form with same name already exists");
                }
                // update
                if (form != null) {
                    form.updateName(context, dto.getName());
                    form.updateSchema(context, dto.getSchema());
                    form.updateDefinition(context, dto.getDefinition());
                } 
                // create
                else {
                    form = new Form(
                        context,
                        dto.getId() != null ? 
                            dto.getId() : 
                            UUID.randomUUID().toString(),
                        dto.getName(), 
                        dto.getSchema(), 
                        dto.getDefinition() != null ? 
                            dto.getDefinition() : 
                            emptyDefinition);
                }
                // store
                form = formRepository.save(form);
            } catch (Throwable t) {
                // rollback
                transactionManager.rollback(tx);
                throw t;
            }

            // commit
            transactionManager.commit(tx);

            // map and return
            return formMapper.map(
                form, 
                MappingContext.of(context));
        }, 
        FormDTO.class);        
    }

    /**
     * 
     * @param context
     * @param name
     * @param match
     * @param pageNumber
     * @param pageSize
     * @param orderBy
     * @param fetch
     * @return
     * @throws ApplicationException 
     */
    public PageDTO<FormDTO> getForms(
        AuthenticationContext context, 
        String name, 
        boolean match,
        Integer pageNumber, Integer pageSize, String orderBy, 
        String fetch) 
        throws ApplicationException {

        Pageable pageable;
        Page<Form> page;
        
        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        pageable = Pager.of(Form.class, pageNumber, pageSize, orderBy);
        if (match) {
            page = formRepository.findByName(context, name, pageable);
            if (page.isEmpty()) {
                throw new NotFoundException();
            }
        } else {
            page = formRepository.findAll(context, name, pageable);
        }
        return formMapper.map(
            page, 
            MappingContext.of(context, Fetcher.of(Form.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param formId
     * @param fetch
     * @return
     * @throws ApplicationException 
     */
    public FormDTO getForm(
        AuthenticationContext context, 
        String formId, 
        String fetch) 
        throws ApplicationException {
        
        Form form;

        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }

        form = formRepository.findById(context, formId);
        if (form == null) {
            throw new NotFoundException();
        }
        
        return formMapper.map(
            form, 
            MappingContext.of(context, Fetcher.of(Form.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param formId
     * @param fetch
     * @return
     * @throws ApplicationException 
     */
    public List<BackupDTO> getFormBackups(
            
        AuthenticationContext context, 
        String formId, 
        String fetch) 
        throws ApplicationException {
        
        Form form;
        
        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }

        form = formRepository.findById(context, formId);
        if (form == null) {
            throw new NotFoundException();
        }
        
        return backupsMapper.map(
            form.getBackups(), 
            MappingContext.of(context, Fetcher.of(Backup.class, fetch)));
    }
    
    /**
     * 
     * @param context
     * @param formId
     * @param fetch
     * @return
     * @throws ApplicationException 
     */
    public ObjectNode getFormBackupDefinition(
            
        AuthenticationContext context, 
        String formId,
        Integer backupIndex) 
        throws ApplicationException {
        
        Form form;
        Backup backup;

        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        // get form
        form = formRepository.findById(context, formId);
        if (form == null) {
            throw new NotFoundException();
        }
        
        // get backup
        try {
            backup = form.getBackups().get(backupIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new NotFoundException();
        }
        
        // return definition
        return backup.getDefinition();
    }

    /**
     * 
     * @param context
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public FormDTO updateForm(
        AuthenticationContext context, String formId, UpdateFormDTO dto) 
        throws ApplicationException {
        
        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        // validate dto
        validationService.validate(dto);
        
        return operationService.execute(dto.getOperationId(), () -> {
    
            Form form;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                 new DefaultTransactionDefinition());
            try {
                // get form
                form = formRepository.findById(context, formId);
                if (form == null) {
                    throw new NotFoundException();
                }
                
                // update
                if (dto.getName() != null) {
                    // check name
                    if (formRepository.existsByName(context, dto.getName(), formId)) {
                        throw new BusinessException(
                            BusinessError.FORM_ALREADY_EXISTS, 
                            "Form with same name already exists");
                    }
                    form.updateName(context, dto.getName());
                }
                if (dto.getSchema() != null) {
                    form.updateSchema(context, dto.getSchema());
                }
                if (dto.getDefinition() != null) {
                    form.updateDefinition(context, dto.getDefinition());
                    refreshFormTemplates(form);
                }
                
                // store
                form = formRepository.save(form);
            } catch (Throwable t) {
                // rollback
                transactionManager.rollback(tx);
                throw t;
            }

            // commit
            transactionManager.commit(tx);

            // map and return
            return formMapper.map(
                form, 
                MappingContext.of(context));
        }, 
        FormDTO.class);        
    }
    
    /**
     * 
     * @param context
     * @param dto
     * @return
     * @throws ApplicationException 
     */
    public void deleteForm(
        AuthenticationContext context, 
        String formId, 
        OperationDTO dto) 
        throws ApplicationException {
        
        // endpoint autorization
        if (!context.isAdmin()) {
            throw new AuthorizationException();
        }
        
        operationService.execute(dto.getOperationId(), () -> {
    
            Form form;

            // start transaction
            TransactionStatus tx = transactionManager.getTransaction(
                 new DefaultTransactionDefinition());
            try {
                // get form
                form = formRepository.findById(context, formId);
                if (form == null) {
                    throw new NotFoundException();
                }
                
                // delete
                formRepository.delete(form);
            } catch (Throwable t) {
                // rollback
                transactionManager.rollback(tx);
                throw t;
            }

            // commit
            transactionManager.commit(tx);
            
            // no return
            return null;
        }, 
        Void.class);        
    }    
    
    /**
     * 
     * @param context
     * @param formId
     * @param type
     * @param preview
     * @param response
     * @throws ApplicationException
     * @throws TemplateException
     * @throws IOException 
     */
    public void getFormTemplate(
        AuthenticationContext context, 
        String formId, 
        String type,
        boolean preview,
        HttpServletResponse response) 
        throws ApplicationException, TemplateException, IOException {
        
        Form form;
        Template template;
        
        if (type == null) {
            type = "vue";
        }
        
        // get form
        form = formRepository.findById(context, formId);
        if (form == null) {
            throw new NotFoundException();
        }
        // get form template
        template = getFormTemplate(form, type, preview);
        if (template == null) {
            throw new BusinessException(
                BusinessError.FORM_TEMPLATE_UNDEFINED, 
                String.format("Form template type undefined: %s", type));
        }
        
        // set response
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("text/plain");
        response.setCharacterEncoding(Charsets.UTF_8.name());

        // write template
        try {
            // alternative to dump that preserve line breaks and whitespaces 
            // for better readability
            IOUtils.copy(
                new StringReader(template.getRootTreeNode().getSource()), 
                response.getWriter());
        } catch (Throwable t) {
            response.reset();
            throw t;
        }
    }    
    
    /**
     * 
     * @param context
     * @param formId
     * @param dto
     * @param preview
     * @param response
     * @throws ApplicationException
     * @throws IOException 
     */
    public void createFormView(
        AuthenticationContext context, 
        String formId, 
        CreateFormViewDTO dto,
        boolean preview,
        HttpServletResponse response) 
        throws ApplicationException, IOException {
        
        Form form;
        Template template;
        Map<String, Object> data;
        
        // validate
        validationService.validate(dto);
        
        // get form
        form = formRepository.findById(context, formId);
        if (form == null) {
            throw new NotFoundException();
        }
        // get template
        template = getFormTemplate(form, dto.getType(), preview);
        if (template == null) {
            throw new BusinessException(
                BusinessError.FORM_TEMPLATE_UNDEFINED, 
                String.format("Form template type undefined: %s", dto.getType()));
        }

        // set response
        response.setStatus(HttpStatus.CREATED.value());
        response.setContentType("text/html");
        response.setCharacterEncoding(Charsets.UTF_8.name());

        // build data
        data = new HashMap<>();
        data.putAll(dto.getOptions());
        data.put("model", dto.getModel());
        
        // generate and write view
        try {
            // process and write
            template.process(data, response.getWriter());
        } catch (TemplateException t) {
            response.reset();
            throw new BusinessException(
                BusinessError.FORM_VIEW_GENERATION_ERROR,
                t.getMessage());
        }
    }    
    
    /**
     * 
     * @param context
     * @param formId
     * @param response
     * @throws ApplicationException
     * @throws TemplateException
     * @throws IOException 
     */
    public void getFormScript(
        AuthenticationContext context, 
        String formId, 
        HttpServletResponse response) 
        throws ApplicationException, TemplateException, IOException {
        
        Form form;
        
        // get form
        form = formRepository.findById(context, formId);
        if (form == null) {
            throw new NotFoundException();
        }
        
        // set response
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("text/javascript");
        response.setCharacterEncoding(Charsets.UTF_8.name());

        // write script
        try {
            IOUtils.copy(
                new StringReader(form.getScript()), 
                response.getWriter());
        } catch (Throwable t) {
            response.reset();
            throw t;
        }
    }    
    
    /**
     * 
     * @param form
     * @param type
     * @param preview
     * @return 
     */
    private Template getFormTemplate(Form form, String type, boolean preview) {
        
        String key;
        
        if (!templateTypes.contains(type)) {
            return null;
        }
        
        key = String.format("%s:%s", preview ? "preview" : type, form.getId());
        try {
            return templateCache.get(key, new Callable<Template>() {
                @Override
                public Template call() throws Exception {
                    
                    Map<String, Object> data;
                    StringWriter writer;
                    StringBuffer buffer;
                    Matcher matcher;
                    String path, source;
        
                    // build data
                    data = new HashMap<>();
                    data.put("definition", templateObjectMapper.treeToValue(form.getDefinition(), Map.class));
                    if (preview) {
                        data.put("template", String.format("/template/%s.ftl", type));
                        data.put("assetsBaseUrl", previewConfiguration.getAssetsBaseUrl());
                        data.put("assets", previewConfiguration.getAssets());
                    }
                    
                    // process and write
                    writer = new StringWriter();
                    configuration
                        .getTemplate(preview ? "preview.ftl" : String.format("template/%s.ftl", type))
                        .process(data, writer);
                    
                    // replace include directives with inline source
                    buffer = new StringBuffer(writer.toString());
                    matcher = Pattern.compile("<#include \"(.+)\">").matcher(buffer);
                    while (matcher.find()) {
                        path = matcher.group(1);
                        if (path.startsWith("/")) {
                            path = String.format("ftl%s", path);
                        } else {
                            path = String.format("ftl/template/%s", path);
                        }
                        buffer.replace(
                            matcher.start(),
                            matcher.end(),
                            IOUtils.toString(
                                new ClassPathResource(path).getInputStream(),
                                Charsets.UTF_8));
                    }
                    
                    // strip empty lines
                    source = buffer.toString().replaceAll("(?m)^[ \t]*\r?\n", "").trim();
                    
                    return new Template(
                        key,
                        source,
                        configuration);
                }
            });
        } catch (Throwable t) {
            throw new RuntimeException(t.getMessage());
        }
    }
    
    /**
     * 
     * @param form 
     */
    private void refreshFormTemplates(Form form) {
        
        templateCache.invalidate(String.format("preview:%s", form.getId()));
        templateTypes.forEach(type -> {
            templateCache.invalidate(String.format("%s:%s", type, form.getId()));
        });
    }
}
