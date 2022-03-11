/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "Form", 
    description = ""
    + "Fetchable:"
    + "<ul><li>schema"
    + "<li>definition"
    + "<li>backups</ul>"
    + "Sortable:"
    + "<ul><li>name (default)"
    + "<li>lastModifiedTs</ul>") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormDTO {
   
    @ApiModelProperty(position = 1, 
        example = "123e4567-e89b-12d3-a456-556642440000") 
    private String id;

    @ApiModelProperty(position = 2, 
        example = "MyForm") 
    private String name;

    @ApiModelProperty(position = 3, 
        example = "mario.rossi") 
    private String lastModifiedBy;

    @ApiModelProperty(position = 4, 
        example = "2020-11-10T09:59:42.000Z") 
    private Date lastModifiedTs;    

    @ApiModelProperty(position = 5) 
    private ObjectNode schema;

    @ApiModelProperty(position = 6) 
    private ObjectNode definition;

    @ApiModelProperty(position = 7) 
    private List<BackupDTO> backups;

    public FormDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedTs() {
        return lastModifiedTs;
    }

    public void setLastModifiedTs(Date lastModifiedTs) {
        this.lastModifiedTs = lastModifiedTs;
    }

    public ObjectNode getSchema() {
        return schema;
    }

    public void setSchema(ObjectNode schema) {
        this.schema = schema;
    }

    public ObjectNode getDefinition() {
        return definition;
    }

    public void setDefinition(ObjectNode definition) {
        this.definition = definition;
    }

    public List<BackupDTO> getBackups() {
        return backups;
    }

    public void setBackups(List<BackupDTO> backups) {
        this.backups = backups;
    }
    
    @ApiModel("Page<Form>")
    public static class Page extends PageDTO<FormDTO> {}
}
