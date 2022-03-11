/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.api.bpm.configuration.ApplicationProperties;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author marco.mazzocchetti
 */
@Api(tags = {"/tests"}) 
@RestController
@RequestMapping("/tests")
@Profile("development")
public class TestEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(TestEndpoint.class);
    
    @Value("${test.value:ciao!}")
    private String testValue;

    @Autowired
    private ApplicationProperties applicationProperties;
    
    public TestEndpoint() {
    }

    @ApiOperation(
        value = "Get cluster node")
    @ApiResponses({
        @ApiResponse(code = 200, response = String.class, 
            message = "<b>Cluster node message</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials")
    })    
    @ResponseStatus(HttpStatus.OK)               
    @RequestMapping(
        path = "/cluster-node",
        method = RequestMethod.GET, 
        produces = "text/plain"
    )
    public ResponseEntity<?> getClusterNode() throws IOException {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(String.format(
                "Cluster node '%s' says: %s", 
                 applicationProperties.node(), 
                 testValue));
    }
}
