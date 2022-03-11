/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package docer.configuration;

import it.kdm.doctoolkit.zookeeper.ApplicationProperties;

/**
 *
 * @author marco.mazzocchetti
 */
public class DocerConfiguration {
    public String getParamsBpm(String key){
        return ApplicationProperties.getEnv().getProperty(key);
    }
}
