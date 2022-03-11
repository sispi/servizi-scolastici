/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author marco.mazzocchetti
 * @param <T> 
 */
@ApiModel(
    value = "Page", 
    description = "Standard object returned in all paged list responses")
public class PageDTO<T> {
    
    @ApiModelProperty(position = 1, 
        example = "43", 
        notes = "Total number of results")    
    private Long count;

    @ApiModelProperty(position = 2, 
        example = "1", 
        notes = "Applied pageNumber parameter")    
    private Integer pageNumber;

    @ApiModelProperty(position = 3,
        example = "10", 
        notes = "Applied pageSize parameter")    
    private Integer pageSize;

    @ApiModelProperty(position = 4,
        example = "startTs:asc",
        notes = "Applied orderBy parameter")    
    private String orderBy;

    @ApiModelProperty(position = 5, 
        notes = "Applied orderBy criteria (srtuctured orderBy)")    
    private List<Order> orderByCriteria;

    @ApiModelProperty(position = 6, 
        notes = "Page data")    
    private List<T> data;

    public PageDTO() {
    }

    public PageDTO(
        Long count, 
        Integer pageNumber, 
        Integer pageSize, 
        List<Order> orderBy, 
        List<T> data) {
        this.count = count;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.orderByCriteria = orderBy;
        this.data = data;      
        this.orderBy = orderBy
            .stream()
            .map(o -> String.format("%s:%s", o.getProperty(), o.getDirection()))
            .collect(Collectors.joining(","));
    }

    public Long getCount() {
        return count;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public List<Order> getOrderByCriteria() {
        return orderByCriteria;
    }

    public List<T> getData() {
        return data;
    }
    
    /**
     * 
     */
    @ApiModel("Order")    
    public static class Order {

        @ApiModelProperty(position = 1, example = "startTs", 
            notes = "Property name")    
        private String property;

        @ApiModelProperty(position = 2, example = "ASC",
            notes = "Sort direction. Allowed values are:"
            + "<li>ASC"
            + "<li>DESC")        
        private String direction;

        public Order() {
        }

        public Order(String property, String direction) {
            this.property = property;
            this.direction = direction;
        }

        public String getProperty() {
            return property;
        }

        public String getDirection() {
            return direction;
        }
    }
}

