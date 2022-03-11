/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.endpoint;

import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.api.bpm.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.bpm.dto.ActivateConfigurationDTO;
import it.filippetti.ks.api.bpm.dto.ConfigurationDTO;
import it.filippetti.ks.api.bpm.dto.CreateFormViewDTO;
import it.filippetti.ks.api.bpm.dto.ErrorDTO;
import it.filippetti.ks.api.bpm.dto.UpdateConfigurationDTO;
import it.filippetti.ks.api.bpm.dto.VoidDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.service.ConfigurationService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author marco.mazzocchetti
 */
@Api(tags = {"/configurations"}) 
@RestController
@RequestMapping("/configurations")
public class ConfigurationsEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(ConfigurationsEndpoint.class);
    
    @Autowired
    private ConfigurationService configurationService;

    @ApiOperation(
        value = "Get configurations",
        notes = ""
            + "Get a paged list of process configurations.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details) and these filters:"
            + "<ul><li><b>processId</b>: get configurations by processId starting with, case insensitive"
            + "<li><b>runnableOnly</b>: get only runnable configurations</ul>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ConfigurationDTO.Page.class, 
            message = "<b>Configurations list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
    })
    @ResponseStatus(HttpStatus.OK)        
    @RequestMapping(
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getConfigurations(
        @RequestParam(name = "processId", required = false) String processId, 
        @RequestParam(name = "runnableOnly", required = false, defaultValue = "false") Boolean runnableOnly,            
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(configurationService.getConfigurations(AuthenticationContextHolder.get(), 
                processId,
                runnableOnly, 
                pageNumber, pageSize, orderBy,
                fetch));
    }    
    @ApiOperation(
        value = "Get configuration",
        notes = ""
            + "Get detail of process configuration.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details).")
    @ApiResponses({
        @ApiResponse(code = 200, response = ConfigurationDTO.class, 
            message = "<b>Configuration detail</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)             
    @RequestMapping(
        path = "/{id}",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getConfiguration(
        @PathVariable("id") Long id,
        @RequestParam(name = "fetch", required = false) String fetch) 
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(configurationService.getConfiguration(AuthenticationContextHolder.get(), id, fetch));
    }    

    @ApiOperation(
        value = "Download configuration asset",
        notes = ""
            + "Download a single configuration asset content.<br/><br/>"
            + "Operation requires this filter:"
            + "<ul><li><b>name</b>: get content by asset name (e.g. settings.json)</ul>")
    @ApiResponses({
        @ApiResponse(code = 200,
            message = "<b>Configuration asset content</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)                 
    @RequestMapping(
        path = "/{id}/asset-content",
        method = RequestMethod.GET, 
        produces = MediaType.ALL_VALUE
    )
    public void downloadConfigurationAsset(
        @PathVariable("id") Long id, 
        @RequestParam(name = "name", required = true) String name,
        HttpServletResponse response) 
        throws ApplicationException, IOException {
 
        configurationService.downloadConfigurationAsset(AuthenticationContextHolder.get(), id, name, response);
    }    
    
    @ApiOperation(
        value = "Activate configuration", 
        notes = ""
        + "Activate or deactivate a process configuration.<br/>"
        + "A deactivated configuration prevents process instantiation. "
        + "Anyway, active instances are not affected by this setting.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ConfigurationDTO.class, 
            message = "<b>Configuration successfully activated</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")                    
    })
    @ResponseStatus(HttpStatus.OK)        
    @RequestMapping(
        path = "/{id}/actions/activate",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> activateConfiguration(
        @PathVariable("id") Long id,     
        @RequestBody ActivateConfigurationDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(configurationService.activateConfiguration(AuthenticationContextHolder.get(), id, body));
    }    

    @ApiOperation(
        value = "Update configuration", 
        notes = ""
        + "Update a process configuration.<br/><br/>"
        + "Operation support partial updates, so only valued keys are take into account for update.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ConfigurationDTO.class, 
            message = "<b>Configuration successfully updated</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>ASSET_NOT_VALID</b>: Missing or invalid asset content"
            + "<li><b>CONFIGURATION_PERMISSION_NOT_VALID</b>: A reference to an invalid user or group was detected")
    })
    @ResponseStatus(HttpStatus.OK)       
    @RequestMapping(
        path = "/{id}",    
        method = RequestMethod.PUT, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateConfiguration(
        @PathVariable("id") Long id,     
        @RequestBody UpdateConfigurationDTO body) 
        throws ApplicationException, IOException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(configurationService.updateConfiguration(AuthenticationContextHolder.get(), id, body));
    }    
    
    @ApiOperation(
        value = "Delete configuration", 
        notes = ""
        + "Delete a process configuration.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = VoidDTO.class,
            message = "<b>Configuration successfully deleted</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>CONFIGURATION_IN_USE_BY_INSTANCE</b>: Configuration in use by active instances")
    })      
    
    @RequestMapping(
        path = "/{id}",    
        method = RequestMethod.DELETE, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteConfiguration(
        @PathVariable("id") Long id) 
        throws ApplicationException {
        
        configurationService.deleteConfiguration(AuthenticationContextHolder.get(), id);
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }    
    
    @ApiOperation(
        value = "Get configuration form template",
        notes = ""
        + "Get configure/start form FTL template of the specified type.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200,
            message = "<b>Form template</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>FORM_TEMPLATE_UNDEFINED</b>: Form template undefined")         
    })    
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
        path = "/{id}/forms/{name}/template",
        method = RequestMethod.GET, 
        produces = "text/plain"
    )
    public void getConfigurationFormTemplate(
        @PathVariable("id") Long id, 
        @ApiParam(allowableValues = "configure,start", required = true)    
        @PathVariable("name") String name, 
        @RequestParam(value = "type", required = false, defaultValue = "vue") String type,
        HttpServletResponse response)
        throws ApplicationException, IOException{
        
        configurationService.getConfigurationFormTemplate(
            AuthenticationContextHolder.get(), id, name, type, response);
    }       

    @ApiOperation(
        value = "Get configuration form script",
        notes = ""
        + "Get configure/start form script.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200,
            message = "<b>Form script</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")
    })    
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
        path = "/{id}/forms/{name}/script",
        method = RequestMethod.GET, 
        produces = "text/javascript"
    )
    public void getConfigurationFormScript(
        @PathVariable("id") Long id, 
        @ApiParam(allowableValues = "configure,start", required = true)    
        @PathVariable("name") String name, 
        HttpServletResponse response)
        throws ApplicationException, IOException{
        
        configurationService.getConfigurationFormScript(
            AuthenticationContextHolder.get(), id, name, response);
    }       
    
    @ApiOperation(
        value = "Create configuration form view",
        notes = ""
        + "Generate a configure/start form HTML view for the specified template type.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, 
            message = "<b>Form view successfully created</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found"),        
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>FORM_TEMPLATE_UNDEFINED</b>: Form template undefined"         
            + "<li><b>FORM_VIEW_GENERATION_ERROR</b>: An error has occurred during view generation")             
    })    
    @ResponseStatus(HttpStatus.CREATED)    
    @RequestMapping(
        path = "/{id}/forms/{name}/views",
        method = RequestMethod.POST, 
        produces = "text/html"
    )
    public void createConfigurationFormView(
        @PathVariable("id") Long id, 
        @ApiParam(allowableValues = "configure,start", required = true)    
        @PathVariable("name") String name, 
        @RequestBody CreateFormViewDTO body,
        HttpServletResponse response)
        throws ApplicationException, IOException, TemplateException{
        
        configurationService.createConfigurationFormView(
            AuthenticationContextHolder.get(), id, name, body, response);
    }          
}
