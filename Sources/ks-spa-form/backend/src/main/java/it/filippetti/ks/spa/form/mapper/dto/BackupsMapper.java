/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.mapper.dto;

import it.filippetti.ks.spa.form.dto.BackupDTO;
import it.filippetti.ks.spa.form.mapper.ContextMapper;
import it.filippetti.ks.spa.form.mapper.MappingContext;
import it.filippetti.ks.spa.form.model.Backup;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class BackupsMapper extends ContextMapper<List<Backup>, List<BackupDTO>> {

    @Override
    protected List<BackupDTO> doMapping(List<Backup> backups, MappingContext ctx) throws Exception {
        
        List<BackupDTO> dtoList = new ArrayList<>();
        BackupDTO dto;
        
        for (int i = 0; i < backups.size(); i++) {
            dto = new BackupDTO();
            dto.setIndex(i);
            dto.setLastModifiedBy(backups.get(i).getLastModifiedBy());
            dto.setLastModifiedTs(backups.get(i).getLastModifiedTs());
            if (ctx.fetch("definition")) {
                dto.setDefinition(backups.get(i).getDefinition());
            }
            dtoList.add(dto);
        }
        return dtoList;
    }    
}
