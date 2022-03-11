/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.orchestratore.activation;

import java.util.Arrays;

/**
 *
 * @author marco.mazzocchetti
 */
public class JarClassLoader {
    
    private String className;

    public JarClassLoader(String className) {
        this.className = className;
    }
    
    public Class get() {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Object newInstance(Object... args)  {
        try {
            return Class
                .forName(className)
                .getConstructor(Arrays
                    .stream(args)
                    .map(o -> o.getClass())
                    .toArray(Class[]::new))
                .newInstance(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }    
}
