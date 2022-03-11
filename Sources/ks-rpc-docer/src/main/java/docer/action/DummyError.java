/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docer.action;

import docer.exception.ActionRuntimeException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author marco.mazzocchetti
 */
public class DummyError extends Dummy {
    
    private static final Logger log = LoggerFactory.getLogger(Dummy.class);

    @Override
    public Map<String, Object> execute(Map<String, Object> inputs) throws ActionRuntimeException {
        if (System.currentTimeMillis() % 2 == 0) {
            return super.execute(inputs);
        } else {
            throw new ActionRuntimeException("Dummy Error");
        }
    }
}