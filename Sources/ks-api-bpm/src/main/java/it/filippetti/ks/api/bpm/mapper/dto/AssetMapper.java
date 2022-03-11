/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.mapper.dto;

import it.filippetti.ks.api.bpm.dto.AssetDTO;
import it.filippetti.ks.api.bpm.mapper.ContextMapper;
import it.filippetti.ks.api.bpm.mapper.MappingContext;
import it.filippetti.ks.api.bpm.model.Asset;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class AssetMapper extends ContextMapper<Asset, AssetDTO> {

    @Override
    protected AssetDTO doMapping(Asset asset, MappingContext ctx) throws Exception {
        
        AssetDTO dto = new AssetDTO();

        dto.setType(asset.getType().name());
        dto.setContentType(asset.getType().mediaType().toString());
        if (ctx.fetch("content")) {
            if (asset.getType().isJson()) {
                dto.setContent(asset.getContent().asMap());
            } else {
                 dto.setContent(asset.getContent().asString());
            }
        }
        return dto;
    }
}
