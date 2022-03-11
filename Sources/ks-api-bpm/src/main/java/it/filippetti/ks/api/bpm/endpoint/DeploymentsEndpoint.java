/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.api.bpm.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.bpm.dto.ActivateDeploymentDTO;
import it.filippetti.ks.api.bpm.dto.ConfigurationDTO;
import it.filippetti.ks.api.bpm.dto.CreateConfigurationDTO;
import it.filippetti.ks.api.bpm.dto.CreateDeploymentDTO;
import it.filippetti.ks.api.bpm.dto.DeleteDeploymentDTO;
import it.filippetti.ks.api.bpm.dto.DeploymentDTO;
import it.filippetti.ks.api.bpm.dto.ErrorDTO;
import it.filippetti.ks.api.bpm.dto.ShareDeploymentDTO;
import it.filippetti.ks.api.bpm.dto.VoidDTO;
import it.filippetti.ks.api.bpm.exception.ApplicationException;
import it.filippetti.ks.api.bpm.service.ConfigurationService;
import it.filippetti.ks.api.bpm.service.DeploymentService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

/**
 *
 * @author marco.mazzocchetti
 */
@Api(tags = {"/deployments"}) 
@RestController
@RequestMapping("/deployments")
public class DeploymentsEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(DeploymentsEndpoint.class);
    
    @Autowired
    private DeploymentService deploymentService;

    @Autowired
    private ConfigurationService configurationService;

    @ApiOperation(
        value = "Create deployments in batch", 
        notes = ""
        + "Deploy or redeploy multiple processes in single batch.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = VoidDTO.class, 
            message = "<b>All deployments successfully created</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>ASSET_NOT_VALID</b>: Missing or invalid asset content"
            + "<li><b>DEPLOYMENT_ALREADY_EXISTS</b>: Deployment with same name and version already exists"
            + "<li><b>DEPLOYMENT_IN_USE_BY_INSTANCE</b>: Deployment in use by active instances"
            + "<li><b>DEPLOYMENT_IN_USE_BY_DEPENDANT</b>: Deployment is a dependency for other deployments"
            + "<li><b>DEPLOYMENT_ALREADY_SHARED</b>: Shared deployment with same name and version already exists"
            + "<li><b>DEPLOYMENT_DEPENDENCY_NOT_FOUND</b>: Unable to resolve deployment dependency for declared call activity"
            + "<li><b>DEPLOYMENT_BUILD_ERROR</b>: Deployment build failed"
            + "<li><b>CONFIGURATION_ALREADY_EXISTS</b>: Configuration for profile already exists"
            + "<li><b>CONFIGURATION_PERMISSION_NOT_VALID</b>: A reference to an invalid user or group was detected")       
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(
        path = "/batches",
        method = RequestMethod.POST, 
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createDeployments(
        @RequestParam(value = "content", required = true) MultipartFile content) 
        throws Exception {
        
        deploymentService.createDeployments(AuthenticationContextHolder.get(), content.getInputStream());
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }    
    
    @ApiOperation(
        value = "Create deployment", 
        notes = ""
        + "Deploy or redeploy a process.<br/>"
        + "Due to the nature of multipart content type, json body must be expressed as string "
        + "(see <b>CreateDeployment</b> model section for details)")
    @ApiResponses({
        @ApiResponse(code = 201, response = DeploymentDTO.class, 
            message = "<b>Deployment successfully created</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>ASSET_NOT_VALID</b>: Missing or invalid asset content"
            + "<li><b>DEPLOYMENT_ALREADY_EXISTS</b>: Deployment with same name and version already exists"
            + "<li><b>DEPLOYMENT_IN_USE_BY_INSTANCE</b>: Deployment in use by active instances"
            + "<li><b>DEPLOYMENT_IN_USE_BY_DEPENDANT</b>: Deployment is a dependency for other deployments"
            + "<li><b>DEPLOYMENT_ALREADY_SHARED</b>: Shared deployment with same name and version already exists"
            + "<li><b>DEPLOYMENT_DEPENDENCY_NOT_FOUND</b>: Unable to resolve deployment dependency for declared call activity"
            + "<li><b>DEPLOYMENT_BUILD_ERROR</b>: Deployment build failed"
            + "<li><b>CONFIGURATION_ALREADY_EXISTS</b>: Configuration for profile already exists"
            + "<li><b>CONFIGURATION_PERMISSION_NOT_VALID</b>: A reference to an invalid user or group was detected")       
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(
        method = RequestMethod.POST, 
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createDeployment(
        @RequestParam(value = "body", required = true) CreateDeploymentDTO dto,   
        @RequestParam(value = "content", required = true) MultipartFile content) 
        throws ApplicationException, IOException, SAXException {
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(deploymentService.createDeployment(AuthenticationContextHolder.get(), dto, content.getInputStream()));
    }    

    @ApiOperation(
        value = "Get deployments",
        notes = ""
            + "Get a paged list of deployed processes.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details) and these filters:"
            + "<ul><li><b>processId</b>: get deployments by processId starting with, case insensitive</ul>")
    @ApiResponses({
        @ApiResponse(code = 200, response = DeploymentDTO.Page.class, 
            message = "<b>Deployments list</b>"),
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
    public ResponseEntity<?> getDeployments(
        @RequestParam(name = "processId", required = false) String processId,             
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(deploymentService.getDeployments(AuthenticationContextHolder.get(), 
                processId, 
                pageNumber, pageSize, orderBy,
                fetch));
    }    

    @ApiOperation(
        value = "Get deployment",
        notes = ""
            + "Get detail of deployed process.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details)")
    @ApiResponses({
        @ApiResponse(code = 200, response = DeploymentDTO.class, 
            message = "<b>Deployment detail</b>"),
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
    public ResponseEntity<?> getDeployment(
        @PathVariable("id") Long id,
        @RequestParam(name = "fetch", required = false) String fetch) 
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(deploymentService.getDeployment(AuthenticationContextHolder.get(), id, fetch));
    }    

    @ApiOperation(
        value = "Download deployment",
        notes = ""
            + "Download the full deployment content archive.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200,
            message = "<b>Deployment content</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)             
    @RequestMapping(
        path = "/{id}/content",
        method = RequestMethod.GET, 
        produces = MediaType.ALL_VALUE
    )
    public void downloadDeployment(
        @PathVariable("id") Long id, 
        HttpServletResponse response) 
        throws ApplicationException, IOException {
        
        deploymentService.downloadDeployment(AuthenticationContextHolder.get(), id, response);
    }    

    @ApiOperation(
        value = "Download deployment asset",
        notes = ""
            + "Download a single deployment asset content.<br/><br/>"
            + "Operation requires this filter:"
            + "<ul><li><b>name</b>: get content by asset name (e.g. settings.json)</ul>")
    @ApiResponses({
        @ApiResponse(code = 200,
            message = "<b>Deployment asset content</b>"),
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
    public void downloadDeploymentAsset(
        @PathVariable("id") Long id,
        @RequestParam(name = "name", required = true) String name,
        HttpServletResponse response) 
        throws ApplicationException, IOException {
 
        deploymentService.downloadDeploymentAsset(AuthenticationContextHolder.get(), id, name, response);
    }    
    
    @ApiOperation(
        value = "Activate deployment", 
        notes = ""
        + "Activate or deactivate a deployed process.<br/>"
        + "A deactivated process will be no longer instantiable. "
        + "Anyway, active instances are not affected by this setting.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = DeploymentDTO.class, 
            message = "<b>Deployment successfully activated</b>"),
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
    public ResponseEntity<?> activateDeployment(
        @PathVariable("id") Long id,     
        @RequestBody ActivateDeploymentDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(deploymentService.activateDeployment(AuthenticationContextHolder.get(), id, body));
    }    

    @ApiOperation(
        value = "Share deployment", 
        notes = ""
        + "Share or unshare a deployed process.<br/>"
        + "A shared process will be visible, configurable and instantiable by all the organizations. "
        + "Anyway, active instances are not affected by this setting.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = DeploymentDTO.class, 
            message = "<b>Deployment successfully shared</b>"),
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
            + "<li><b>DEPLOYMENT_IN_USE_BY_DEPENDANT</b>: Deployment is a dependency for other deployments"
            + "<li><b>DEPLOYMENT_ALREADY_SHARED</b>: Shared deployment with same name and version already exists")
    })
    @ResponseStatus(HttpStatus.OK)      
    @RequestMapping(
        path = "/{id}/actions/share",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> shareDeployment(
        @PathVariable("id") Long id,     
        @RequestBody ShareDeploymentDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(deploymentService.shareDeployment(AuthenticationContextHolder.get(), id, body));
    }    
    
    @ApiOperation(
        value = "Create deployment configuration", 
        notes = ""
        + "Create a configuration for the process.<br/>"
        + "Only configured processes will be instantiable.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = ConfigurationDTO.class, 
            message = "<b>Configuration successfully created</b>"),
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
            + "<li><b>CONFIGURATION_ALREADY_EXISTS</b>: Configuration for profile already exists"
            + "<li><b>CONFIGURATION_PERMISSION_NOT_VALID</b>: A reference to an invalid user or group was detected")       
    })    
    @ResponseStatus(HttpStatus.CREATED)      
    @RequestMapping(
        path = "/{id}/configurations",    
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createDeploymentConfiguration(
        @PathVariable("id") Long id,     
        @RequestBody CreateConfigurationDTO body) 
        throws ApplicationException, IOException {
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(configurationService.createConfiguration(AuthenticationContextHolder.get(), id, body));
    }    
    
    @ApiOperation(
        value = "Delete deployment", 
        notes = ""
        + "Undeploy a process.<br/>"
        + "Operation also deletes all the assets of deployment.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = VoidDTO.class,
            message = "<b>Deployment successfully deleted</b>"),
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
            + "<li><b>DEPLOYMENT_IN_USE_BY_INSTANCE</b>: Deployment in use by active instances"
            + "<li><b>DEPLOYMENT_IN_USE_BY_DEPENDANT</b>: Deployment is a dependency for other deployments")
    })  
    @ResponseStatus(HttpStatus.OK)     
    @RequestMapping(
        path = "/{id}",    
        method = RequestMethod.DELETE, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteDeployment(
        @PathVariable("id") Long id,     
        @RequestBody DeleteDeploymentDTO body) 
        throws ApplicationException {
        
        deploymentService.deleteDeployment(AuthenticationContextHolder.get(), id, body);
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }    
  
    @Component
    public static class StringToNewDeploymentDTOConverter implements Converter<String, CreateDeploymentDTO> {

      @Autowired
      private ObjectMapper objectMapper;

      @Override
      public CreateDeploymentDTO convert(String source) {
          try {
              return objectMapper.readValue(source, CreateDeploymentDTO.class);
          } catch (JsonProcessingException e) {
              throw new IllegalArgumentException(e);
          }
      }
    }
}
