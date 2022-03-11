/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;

/**
 *
 * @author dino
 */
public class JsonUtil {
    
    private static XmlMapper XML_MAPPER = new XmlMapper();
    private static ObjectMapper JSON_MAPPER = new ObjectMapper();
    
    public static ObjectMapper getJsonMapper(){
        return JSON_MAPPER;
    }
    
    public static XmlMapper getXmlMapper(){
        return XML_MAPPER;
    }
    
    public static String xmlToJson(String xml) throws IOException{
        return getJsonMapper().writeValueAsString(getXmlMapper().readTree(xml));
    }
    
}
