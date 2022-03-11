package keysuite.docer.controllers;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.docer.server.BridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bridge")
public class BridgeController extends BaseController {

    @Autowired
    BridgeService bridgeService;

    @ApiResponses({
            @ApiResponse( code = 200 , message = "OK"),
            @ApiResponse( code = 404 , message = "NotFound")
    })
    @RequestMapping(value="{prefix}/**",produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,RequestMethod.PATCH, RequestMethod.DELETE})
    public void bridge(@PathVariable String prefix, HttpServletRequest request, HttpServletResponse response){
         bridgeService.bridge(prefix, request, response);
    }

}
