/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.endpoint;

import com.fasterxml.jackson.databind.node.ObjectNode;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.spa.form.authentication.AuthenticationContextHolder;
import it.filippetti.ks.spa.form.dto.BackupDTO;
import it.filippetti.ks.spa.form.dto.CreateFormDTO;
import it.filippetti.ks.spa.form.dto.CreateFormViewDTO;
import it.filippetti.ks.spa.form.dto.ErrorDTO;
import it.filippetti.ks.spa.form.dto.FormDTO;
import it.filippetti.ks.spa.form.dto.OperationDTO;
import it.filippetti.ks.spa.form.dto.UpdateFormDTO;
import it.filippetti.ks.spa.form.dto.VoidDTO;
import it.filippetti.ks.spa.form.exception.ApplicationException;
import it.filippetti.ks.spa.form.service.FormService;
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
@Api(tags = {"/forms"}) 
@RestController
@RequestMapping("/api/forms")
public class FormsEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(FormsEndpoint.class);
    
    @Autowired
    private FormService formService;

    @ApiOperation(
        value = "Create form", 
        notes = ""
        + "Create a new form.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = FormDTO.class, 
            message = "<b>Form successfully created</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>FORM_ALREADY_EXISTS</b>: Form with same name already exists")   
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createForm(
        @RequestBody CreateFormDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(formService.createForm(AuthenticationContextHolder.get(), body));
    }    

    @ApiOperation(
        value = "Get forms",
        notes = ""
            + "Get a paged list of forms.<br/><br/>"
            + "Operation supports paging, dynamic properties fetch and sort (see model for details) and these filters:"
            + "<ul><li><b>name</b>: search forms by name exact match or starting with, case insensitive"   
            + "<li><b>match</b>: enable/disable exact match search</ul>")    
    @ApiResponses({
        @ApiResponse(code = 200, response = FormDTO.Page.class, 
            message = "<b>Form list</b>"),
        @ApiResponse(code = 400, 
            message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found (applies only for exact match search)")        
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getForms(
        @RequestParam(name = "name", required = false) String name, 
        @RequestParam(name = "match", required = false, defaultValue = "false") boolean match, 
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy, 
        @RequestParam(name = "fetch", required = false) String fetch)
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(formService.getForms(AuthenticationContextHolder.get(), 
                name,    
                match,
                pageNumber, pageSize, orderBy,
                fetch));
    }    

    @ApiOperation(
        value = "Get form",
        notes = ""
            + "Get detail of form.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details)")
    @ApiResponses({
        @ApiResponse(code = 200, response = FormDTO.class, 
            message = "<b>Form detail</b>"),
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
    public ResponseEntity<?> getForm(
        @PathVariable("id") String id,
        @RequestParam(name = "fetch", required = false) String fetch) 
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(formService.getForm(AuthenticationContextHolder.get(), id, fetch));
    }   

    @ApiOperation(
        value = "Get form backups",
        notes = ""
            + "Get a list of form backups.<br/><br/>"
            + "Operation supports dynamic properties fetch (see model for details)")
    @ApiResponses({
        @ApiResponse(code = 200, response = BackupDTO.class, responseContainer = "List",
            message = "<b>Backup list</b>"),
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
        path = "/{id}/backups",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getFormBackups(
        @PathVariable("id") String id,
        @RequestParam(name = "fetch", required = false) String fetch) 
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(formService.getFormBackups(AuthenticationContextHolder.get(), id, fetch));
    }   
    
    
    @ApiOperation(
        value = "Get form backup definition",
        notes = ""
            + "Get a form backup definition.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = ObjectNode.class, 
            message = "<b>Backup definition</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, 
            message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/{id}/backups/{index}/definition",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getFormBackupDefinition(
        @PathVariable("id") String id,
        @PathVariable("index") Integer index,
        @RequestParam(name = "fetch", required = false) String fetch) 
        throws ApplicationException{
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(formService.getFormBackupDefinition(AuthenticationContextHolder.get(), id, index));
    }  
    
    @ApiOperation(
        value = "Update form", 
        notes = ""
        + "Update a form.<br/><br/>"
        + "Operation support partial updates, so only valued keys are take into account for update.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = FormDTO.class, 
            message = "<b>Form successfully updated</b>"),
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
            + "<li><b>FORM_ALREADY_EXISTS</b>: From with same name already exists")      
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
        path = "/{id}",    
        method = RequestMethod.PUT, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateForm(
        @PathVariable("id") String id,    
        @RequestBody UpdateFormDTO body) 
        throws ApplicationException {
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(formService.updateForm(AuthenticationContextHolder.get(), id, body));
    }        

    @ApiOperation(
        value = "Delete form", 
        notes = ""
        + "Delete a form.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = VoidDTO.class, 
            message = "<b>Form successfully deleted</b>"),
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
        method = RequestMethod.DELETE, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> deleteForm(
        @PathVariable("id") String id,    
        @RequestBody OperationDTO body) 
        throws ApplicationException {
        
        formService.deleteForm(AuthenticationContextHolder.get(), id, body);
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .build();
    }        
    
    @ApiOperation(
        value = "Get form template", 
        notes = ""
        + "Get form FTL template of the specified type.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, 
            message = "<b>Form template</b>"),
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
        path = "/{id}/template", 
        method = RequestMethod.GET, 
        produces = "text/plain"
    )
    public void getFormTemplate(
        @PathVariable("id") String id,
        @RequestParam(name = "type", required = false, defaultValue = "vue") String type, 
        HttpServletResponse response) 
        throws ApplicationException, TemplateException, IOException {
        
        formService.getFormTemplate(
            AuthenticationContextHolder.get(), id, type, false, response);
    }  
    
    @ApiOperation(
        value = "Create form view", 
        notes = ""
        + "Generate a form HTML view for the specified template type.<br/>")
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
        path = "/{id}/views", 
        method = RequestMethod.POST, 
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = "text/html"
    )
    public void createFormView(
        @PathVariable("id") String id,
        @RequestBody CreateFormViewDTO body, 
        HttpServletResponse response) 
        throws ApplicationException, TemplateException, IOException {
        
        formService.createFormView(
            AuthenticationContextHolder.get(), id, body, false, response);

    }            

    @ApiOperation(
        value = "Get form script", 
        notes = ""
        + "Get form script.<br/>")
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
        path = "/{id}/script", 
        method = RequestMethod.GET, 
        produces = "text/javascript"
    )
    public void getFormScript(
        @PathVariable("id") String id,
        HttpServletResponse response) 
        throws ApplicationException, TemplateException, IOException {
        
        formService.getFormScript(
            AuthenticationContextHolder.get(), id, response);
    }      
}
