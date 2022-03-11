/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.model;

/**
 *
 * @author dino
 */
public class Html {
    String htmlText;
    String headerHtmlText;
    String footerHtmlText;

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public String getHeaderHtmlText() {
        return headerHtmlText;
    }

    public void setHeaderHtmlText(String headerHtmlText) {
        this.headerHtmlText = headerHtmlText;
    }

    public String getFooterHtmlText() {
        return footerHtmlText;
    }

    public void setFooterHtmlText(String footerHtmlText) {
        this.footerHtmlText = footerHtmlText;
    }
    
}
