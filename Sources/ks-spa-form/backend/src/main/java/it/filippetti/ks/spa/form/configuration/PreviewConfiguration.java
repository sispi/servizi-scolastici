/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.configuration;

import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
@ConfigurationProperties(prefix = "application.preview")
public class PreviewConfiguration {

    private String externalUrl;
    
    private String template;
    private Map<String, Object> options;
    private String assetsBaseUrl;
    private List<String> assets;

    public PreviewConfiguration() {
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

    public String getAssetsBaseUrl() {
        return assetsBaseUrl;
    }

    public void setAssetsBaseUrl(String assetsBaseUrl) {
        this.assetsBaseUrl = assetsBaseUrl;
    }

    public List<String> getAssets() {
        return assets;
    }

    public void setAssets(List<String> assets) {
        this.assets = assets;
    }
}
