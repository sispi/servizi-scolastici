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
@ApiModel(value = "CreateCustomer")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateCustomerDTO {
    
    @ApiModelProperty(position = 2, required = true, example = "privato")
    private String customerType;
    
    @ApiModelProperty(position = 3, required = true, example = "Mario") 
    private String firstName;

    @ApiModelProperty(position = 4, required = true, example = "Rossi") 
    private String lastName;
    
    @ApiModelProperty(position = 5, required = false, example = "Via Milano, 5") 
    private String address;

    @ApiModelProperty(position = 6, required = false, example = "Roma") 
    private String city;

    @ApiModelProperty(position = 7, required = false, example = "00120") 
    private String zip;

    @ApiModelProperty(position = 8, required = false, example = "RM") 
    private String stateCode;

    @ApiModelProperty(position = 9, required = false, example = "IT") 
    private String countryCode;
 
    @ApiModelProperty(position = 10, required = false, example = "pippo@pluto.com") 
    private String email;

    @ApiModelProperty(position = 11, required = false, example = "+39333366464") 
    private String phone;
    
    @ApiModelProperty(position = 12, required = false, example = "andrea") 
    private String operatorStaff;
    
    @ApiModelProperty(position = 13, required = true, example = "ABCEFG91M20A525X") 
    private String fiscalCode;
    
    @ApiModelProperty(position = 14, required = true, example = "IT") 
    private String langKey;

    public CreateCustomerDTO() {
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

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOperatorStaff() {
        return operatorStaff;
    }

    public void setOperatorStaff(String operatorStaff) {
        this.operatorStaff = operatorStaff;
    }
    
    @Override
    public String toString() {
        return "{" +
            " customerType='" + getCustomerType() + "'" +
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
