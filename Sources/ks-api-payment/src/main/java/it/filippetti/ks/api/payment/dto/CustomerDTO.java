/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author dino
 */
@ApiModel(value = "Customer") 
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {
    
    @ApiModelProperty(position = 1, example = "0") 
    private Long id;
    
    @ApiModelProperty(position = 2, example = "privato")
    private String customerType;
    
    @ApiModelProperty(position = 3, example = "Mario") 
    private String firstName;

    @ApiModelProperty(position = 4, example = "Rossi") 
    private String lastName;
    
    @ApiModelProperty(position = 5, example = "Via Milano, 5") 
    private String address;

    @ApiModelProperty(position = 6, example = "Roma") 
    private String city;

    @ApiModelProperty(position = 7, example = "00120") 
    private String zip;

    @ApiModelProperty(position = 8, example = "RM") 
    private String stateCode;

    @ApiModelProperty(position = 9, example = "IT") 
    private String countryCode;
 
    @ApiModelProperty(position = 10, example = "pippo@pluto.com") 
    private String email;

    @ApiModelProperty(position = 11, example = "+39333366464") 
    private String phone;
    
    @ApiModelProperty(position = 12, example = "andrea") 
    private String operatorStaff;
    
    @ApiModelProperty(position = 13, example = "ABCEFG91M20A525X") 
    private String fiscalCode;
    
    @ApiModelProperty(position = 14, example = "IT") 
    private String langKey;
    
    public CustomerDTO() {
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerType() {
        return this.customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStateCode() {
        return this.stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOperatorStaff() {
        return this.operatorStaff;
    }

    public void setOperatorStaff(String operatorStaff) {
        this.operatorStaff = operatorStaff;
    }

    @ApiModel("Page<Customer>")
    public static class Page extends PageDTO<CustomerDTO> {}
    
    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", customerType='" + getCustomerType() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", zip='" + getZip() + "'" +
            ", stateCode='" + getStateCode() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", operatorStaff='" + getOperatorStaff() + "'" +
            "}";
    }

}
