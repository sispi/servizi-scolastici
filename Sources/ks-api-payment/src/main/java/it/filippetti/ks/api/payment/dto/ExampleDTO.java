/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

/**
 *
 * @author marco.mazzocchetti
 */
@ApiModel(
    value = "Example", 
    description = ""
    + "Fetchable:"
    + "<ul><li>parent"
    + "<li>children</ul>"
    + "Sortable:"
    + "<ul><li>name (default)</ul>") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExampleDTO {
    
    @ApiModelProperty(position = 1, 
        example = "77") 
    private Long id;

    @ApiModelProperty(position = 1, 
        example = "2020-11-11T14:57:48.000Z", 
        notes = "The timestamp") 
    private Date timestamp;
    
    @ApiModelProperty(position = 2, 
        example = "foo", 
        notes = "The name")
    private String name;

    @ApiModelProperty(position = 3, 
        notes = "The parent")     
    private ExampleDTO parent; 
    
    @ApiModelProperty(position = 4, 
        example = "[]",
        notes = "The children")     
    private List<ExampleDTO> children; 

    public ExampleDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ExampleDTO getParent() {
        return parent;
    }

    public void setParent(ExampleDTO parent) {
        this.parent = parent;
    }

    public List<ExampleDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ExampleDTO> children) {
        this.children = children;
    }
    
    @ApiModel("Page<Example>")
    public static class Page extends PageDTO<ExampleDTO> {}
}
