/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import it.filippetti.ks.api.bpm.configuration.ApplicationProfile;
import it.filippetti.ks.api.bpm.configuration.ApplicationProperties;
import it.filippetti.ks.api.bpm.dto.ErrorDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.exception.BusinessError;
import it.filippetti.ks.api.bpm.exception.BusinessException;
import it.filippetti.ks.api.bpm.model.Asset;
import it.filippetti.ks.api.bpm.model.AssetContent;
import it.filippetti.ks.api.bpm.model.AssetType;
import it.filippetti.ks.api.bpm.model.AuthenticationContext;
import it.filippetti.ks.api.bpm.repository.AssetContentRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class FormAssetService {

    @Value("${form.service.url}")
    private String serviceUrl;
    
    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AssetContentRepository assetContentRepository;
    
    private Configuration configuration;
    
    private LoadingCache<Long, FormAsset> cache;
    
    private RestTemplate restTemplate;
    
    public FormAssetService() {
    }    
    
    @PostConstruct
    public void init() {
        this.configuration = new Configuration(Configuration.VERSION_2_3_29);
        this.configuration.setTemplateLoader(null);
        this.configuration.setDefaultEncoding("UTF-8");
        this.configuration.setTemplateExceptionHandler(
            applicationProperties.profile().isAnyOf(
                ApplicationProfile.staging, ApplicationProfile.release) ? 
                TemplateExceptionHandler.RETHROW_HANDLER : 
                TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        this.configuration.setLogTemplateExceptions(false);
        this.configuration.setWrapUncheckedExceptions(true);
        this.configuration.setFallbackOnNullLoopVariable(false);   
        
        cache = CacheBuilder.newBuilder()
            .initialCapacity(100)
            .maximumSize(1000)
            .concurrencyLevel(8)
            .build(new CacheLoader<Long, FormAsset>() {
                @Override
                public FormAsset load(Long templateAssetId) throws Exception {
                   return loadFormAsset(templateAssetId);
                }
            });   
        
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(uriBuilderFactory);        
    }
    /**
     * 
     * @param asset
     * @return
     * @throws IOException 
     */
    public FormAsset get(Asset asset) throws IOException {
        if (!asset.getType().isForm()) {
            throw new IllegalArgumentException();
        }
        try {
            return cache.get(asset.getId());
        } catch (ExecutionException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            } else {
                throw (RuntimeException) e.getCause();
            }
        }
    }       

    /**
     * 
     * @param asset 
     */
    public void invalidate(Asset asset) {
        if (!asset.isNew()) {
            cache.invalidate(asset.getId());
        }
    }
    
    /**
     * 
     * @param context
     * @param content
     * @throws ApplicationException
     * @throws IOException 
     */
    protected void deploy(AuthenticationContext context, byte[] content)
        throws ApplicationException, IOException {
        
        Iterator<Map.Entry<String, JsonNode>> forms;
                
        if (content == null) {
            return;
        }
        
        forms = objectMapper.readTree(content).fields();
        while (forms.hasNext()) {
            ObjectNode form = (ObjectNode) forms.next().getValue();
            try {
                restTemplate.execute(
                    String.format("%s/api/forms", serviceUrl),  
                    HttpMethod.POST, 
                    (ClientHttpRequest r) -> {
                        r.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                        r.getHeaders().setAccept(List.of(MediaType.ALL));
                        r.getHeaders().setBearerAuth(context.getJwtToken());
                        objectMapper.writeValue(
                            r.getBody(), 
                            form.retain(
                                "id",
                                "name",
                                "schema",
                                "definition"));
                    }, 
                    (ClientHttpResponse r) -> {
                        return true;
                    });
            } catch (RestClientResponseException e) {
                ErrorDTO error = objectMapper.readValue(
                    e.getResponseBodyAsByteArray(),
                    ErrorDTO.class);
                if (error.getStatus() == 400 || error.getStatus() == 422) {
                    throw new BusinessException(
                        BusinessError.ASSET_NOT_VALID,
                        String.format(
                            "%s: %s", 
                            AssetType.forms.entryName(), 
                            e.getMessage()));                
                } else {
                    throw new RuntimeException(String.format(
                        "Form service failure (%d): %s", 
                        error.getStatus(), 
                        error.getMessage()));
                }
            } catch (Throwable t) {
                throw t;
            }                
        }
    }
    
    /**
     * 
     * @param assetId
     * @return
     * @throws IOException 
     */
    private FormAsset loadFormAsset(Long assetId) throws IOException {
        
        AssetContent assetContent;
        BufferedReader reader;
        String header;
        Matcher matcher;
        
        // get asset content
        assetContent = assetContentRepository
            .findById(assetId)
            .orElseThrow(() -> {
                throw new IllegalArgumentException(String.format(
                    "Asset content %s not found", 
                    assetId));
            });

        // check header
        reader = assetContent.asBufferedReader();
        header = reader.readLine();
        matcher = Pattern.compile("^#(.+)$").matcher(header);

        // header match: managed template, store formId reference
        if (matcher.matches() && reader.read() == -1 ) {
            return new FormAsset(matcher.group(1));
        } 
        // header not match: local template, build and store template itself
        else {
            return new FormAsset(new Template(
                String.valueOf(assetId), 
                assetContent.asReader(), 
                configuration));
        }
    }    
    
    /**
     * 
     */
    public class FormAsset {
        
        private String formId;
        private Template template;

        private FormAsset(String formId) {
            this.formId = formId;
        }
        
        private FormAsset(Template template) {
            this.template = template;
        }
        
        /**
         * 
         * @param type
         * @param response
         * @throws IOException 
         */
        public void getTemplate(
            AuthenticationContext context,
            String type,
            HttpServletResponse response) throws ApplicationException, IOException {
            
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("text/plain");
            response.setCharacterEncoding(com.google.common.base.Charsets.UTF_8.name());
            response.setHeader("X-FormType", template != null ? "legacy": type);
            
            if (template != null) {
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
            } else {
                try {
                    IOUtils.copy(
                        restTemplate.execute(
                            String.format("%s/api/forms/%s/template?type=%s", 
                                serviceUrl, UriUtils.encodeQuery(formId, Charsets.UTF_8), type),  
                            HttpMethod.GET, 
                            (ClientHttpRequest r) -> {
                                r.getHeaders().setBearerAuth(context.getJwtToken());
                            }, 
                            (ClientHttpResponse r) -> {
                                return IOUtils.toBufferedInputStream(r.getBody());
                            }), 
                        response.getWriter(), 
                        Charsets.UTF_8);
                } catch (RestClientResponseException e) {
                    ErrorDTO error = objectMapper.readValue(
                        e.getResponseBodyAsByteArray(),
                        ErrorDTO.class);
                    if (error.getStatus() == 422) {
                        throw new BusinessException(
                            BusinessError.valueOf(error.getCode()),
                            error.getMessage());
                    } else {
                        throw new RuntimeException(String.format(
                            "Form service failure (%d): %s", 
                            error.getStatus(), 
                            error.getMessage()));
                    }
                } catch (Throwable t) {
                    response.reset();
                    throw t;
                }                
            }
        }
        
        /**
         * 
         * @param type
         * @param options
         * @param model
         * @param response
         * @throws ApplicationException
         * @throws IOException 
         */
        public void createView(
            AuthenticationContext context,
            String type, 
            Map<String, Object> options, 
            Map<String, Object> model, 
            HttpServletResponse response) 
            throws ApplicationException, IOException {
            
            response.setStatus(HttpStatus.CREATED.value());
            response.setContentType("text/html");
            response.setCharacterEncoding(com.google.common.base.Charsets.UTF_8.name());
            response.setHeader("X-FormType", template != null ? "legacy": type);
            
            if (template != null) {
                try {
                    template.process(model, response.getWriter());
                } catch (Throwable t) {
                    response.reset();
                    throw new BusinessException(
                        BusinessError.FORM_VIEW_GENERATION_ERROR, 
                        t.toString());
                }                
            } else {
                try {
                    IOUtils.copy(
                        restTemplate.execute(
                            String.format("%s/api/forms/%s/views", 
                                serviceUrl, UriUtils.encodeQuery(formId, Charsets.UTF_8)), 
                            HttpMethod.POST, 
                            (ClientHttpRequest r) -> {
                                r.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                r.getHeaders().setAccept(List.of(MediaType.ALL));
                                r.getHeaders().setBearerAuth(context.getJwtToken());
                                objectMapper.writeValue(
                                    r.getBody(),
                                    Map.of(
                                        "type", type,
                                        "options", options,
                                        "model", model
                                    ));
                            }, 
                            (ClientHttpResponse r) -> {
                                return IOUtils.toBufferedInputStream(r.getBody());
                            }), 
                        response.getWriter(), 
                        Charsets.UTF_8);
                } catch (RestClientResponseException e) {
                    ErrorDTO error = objectMapper.readValue(
                        e.getResponseBodyAsByteArray(),
                        ErrorDTO.class);
                    if (error.getStatus() == 422) {
                        throw new BusinessException(
                            BusinessError.valueOf(error.getCode()),
                            error.getMessage());
                    } else {
                        throw new RuntimeException(String.format(
                            "Form service failure (%d): %s", 
                            error.getStatus(), 
                            error.getMessage()));
                    }
                } catch (Throwable t) {
                    response.reset();
                    throw t;
                }                
            }
        }        
        
        /**
         * 
         * @param context
         * @param response
         * @throws ApplicationException
         * @throws IOException 
         */
        public void getScript(
            AuthenticationContext context,
            HttpServletResponse response) throws ApplicationException, IOException {
            
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("text/javascript");
            response.setCharacterEncoding(com.google.common.base.Charsets.UTF_8.name());
            
            if (template != null) {
                // do nothing
            } else {
                try {
                    IOUtils.copy(
                        restTemplate.execute(
                            String.format("%s/api/forms/%s/script",
                                serviceUrl, UriUtils.encodeQuery(formId, Charsets.UTF_8)),  
                            HttpMethod.GET, 
                            (ClientHttpRequest r) -> {
                                r.getHeaders().setBearerAuth(context.getJwtToken());
                            }, 
                            (ClientHttpResponse r) -> {
                                return IOUtils.toBufferedInputStream(r.getBody());
                            }), 
                        response.getWriter(), 
                        Charsets.UTF_8);
                } catch (RestClientResponseException e) {
                    ErrorDTO error = objectMapper.readValue(
                        e.getResponseBodyAsByteArray(),
                        ErrorDTO.class);
                    throw new RuntimeException(String.format(
                        "Form service failure (%d): %s", 
                        error.getStatus(), 
                        error.getMessage()));
                } catch (Throwable t) {
                    response.reset();
                    throw t;
                }                
            }
        }        
    }
}
