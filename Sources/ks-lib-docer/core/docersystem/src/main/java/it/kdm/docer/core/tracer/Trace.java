/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.core.tracer;

import java.util.Calendar;

/**
 * @author stefano.vigna
 */
public class Trace {
    private String message;
    private String serviceName;
    private String serviceNameLabel;
    private String methodName;
    private String methodNameLabel;
    private String livello;
    private String tipo;
    private String user;
    private String docNum;
    private String extraData;
    private String serviceUrl;
    private String urData;
    private Calendar date;
    private String application;
    private String codEnte;
    private String ip;


    public String getMethodNameLabel() {
        return methodNameLabel;
    }

    public void setMethodNameLabel(String methodNameLabel) {
        this.methodNameLabel = methodNameLabel;
    }

    public String getUrData() {
        return urData;
    }

    public void setUrData(String urData) {
        this.urData = urData;
    }

    public String getServiceNameLabel() {
        return serviceNameLabel;
    }

    public void setServiceNameLabel(String serviceNameLabel) {
        this.serviceNameLabel = serviceNameLabel;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String DocNum) {
        this.docNum = DocNum;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getLivello() {
        return livello;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getCodEnte() {
        return codEnte;
    }

    public void setCodEnte(String codEnte) {
        this.codEnte = codEnte;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
