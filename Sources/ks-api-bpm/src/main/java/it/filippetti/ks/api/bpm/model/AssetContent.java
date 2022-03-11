/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import it.filippetti.ks.api.bpm.ApplicationContextHolder;
import it.filippetti.ks.api.bpm.service.FormAssetService;
import it.filippetti.ks.api.bpm.service.JsonAssetContentService;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.commons.codec.Charsets;

/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(name = "KS_ASSET_CONTENT")
public class AssetContent extends Auditable {

    @Id 
    private Long id;    

    @MapsId 
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
    private Asset asset;
    
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private byte[] data;

    public AssetContent() {
    }

    protected AssetContent(Asset asset, byte[] data) {
        this.asset = asset;
        this.data = data;
    }
    
    @Override
    public Long getId() {
        return id;
    }

    public Asset getAsset() {
        return asset;
    }
    
    public byte[] asBytes() {
        return Arrays.copyOf(data, data.length);
    }  
    
    public InputStream asInputStream() {
        return new ByteArrayInputStream(data);
    }

    public String asString() {
        return new String(data, Charsets.UTF_8);
    }  

    public Reader asReader() {
        return new InputStreamReader(asInputStream(), Charsets.UTF_8);
    }

    public BufferedReader asBufferedReader() {
        return new BufferedReader(asReader());
    }

    public Map asMap() throws IOException {
        return asJsonAssetContent().getMap();
    }  
   
    public JsonAssetContent asJsonAssetContent() throws IOException {
        return asJsonAssetContent(JsonAssetContent.class);
    }

    public <T extends JsonAssetContent> T asJsonAssetContent(Class<T> type) throws IOException {

        if (!asset.getType().isJson()) {
            throw new UnsupportedOperationException();
        }
        try {
            return type.getDeclaredConstructor(AssetContent.class).newInstance(this);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void write(OutputStream outputStream) throws IOException {
        outputStream.write(data);
    }
    
    public void update(byte[] data) {
        this.data = data;
        this.asset.setLastModifiedTs();
        if (this.asset.getType().isJson()) {
            getJsonCache().invalidate(this);
        }
        if (this.asset.getType().isForm()) {
            getFormCache().invalidate(asset);
        }
        
    }

    public void update(InputStream inputStream) throws IOException {
        update(inputStream.readAllBytes());
    }

    public void update(String data) {
        update(data.getBytes(Charsets.UTF_8));
    }

    public void update(Map map) throws IOException {
        asJsonAssetContent().setMap(map).flush();
    }
    
    private static final JsonAssetContentService getJsonCache() {
        return ApplicationContextHolder.getBean(JsonAssetContentService.class);
    }     
    
    private static final FormAssetService getFormCache() {
        return ApplicationContextHolder.getBean(FormAssetService.class);
    }    
}
