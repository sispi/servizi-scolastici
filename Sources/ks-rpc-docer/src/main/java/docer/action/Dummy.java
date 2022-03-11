/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docer.action;

import docer.exception.ActionRuntimeException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author marco.mazzocchetti
 */
public class Dummy extends DocerAction {
    
    private static final Logger log = LoggerFactory.getLogger(docer.action.Dummy.class);

    @Override
    public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {
        
        /*log.info(String.format(
            "%s from Dummy Action!", 
            getConfiguration().getString("hello.property")));*/
        
        return new HashMap<>();
    }
}
