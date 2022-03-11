/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.fascicolazione.utils;

import it.kdm.docer.fascicolazione.bean.Fascicolo;
import it.kdm.docer.sdk.classes.KeyValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.List;

public class XmlUtils{


    public static Element createXml(){
        Document xml = DocumentHelper.createDocument();
        xml.setXMLEncoding("UTF-8");
        Element root = xml.addElement("Segnatura");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        Element intestazione = root.addElement("Intestazione");
        return root;
    }


    public static Element createPrimarioXml(Element element, Fascicolo fascicolo){
        Element intestazione = element.element("Intestazione");
        Element fascicoloPrimario = intestazione.addElement("FascicoloPrimario");
        return createFascicoloXml(fascicoloPrimario,fascicolo,false);
    }

    public static Element createPrimarioXmlSfascicola(Element element, Fascicolo fascicolo){
        Element intestazione = element.element("Intestazione");
        Element fascicoloPrimario = intestazione.addElement("FascicoloPrimario");
        return createFascicoloXml(fascicoloPrimario,fascicolo,true);
    }


    public static Element createSecondarioXmlToRemoveAll(Element element) {

            Element intestazione = element.element("Intestazione");
            return intestazione.addElement("FascicoliSecondari");
    }

    public static Element createSecondarioXml(Element element,List<Fascicolo> fascicoli) {
        Element fascicoliSecondari = null;
        if(fascicoli.size()>0) {
            Element intestazione = element.element("Intestazione");
            fascicoliSecondari = intestazione.addElement("FascicoliSecondari");
            for (Fascicolo fasc : fascicoli) {
                Element fascicoliSecondariIn = fascicoliSecondari.addElement("FascicoloSecondario");
                createFascicoloXml(fascicoliSecondariIn, fasc,false);
            }
        }
        return fascicoliSecondari;
    }


    private static Element createFascicoloXml(Element fasc,Fascicolo fascicolo, boolean isNotSfascicola) {

        Element CodiceAmministrazione = fasc.addElement("CodiceAmministrazione");
        CodiceAmministrazione.addText(fascicolo.getEnte());

        Element CodiceAOO = fasc.addElement("CodiceAOO");
        CodiceAOO.addText(fascicolo.getAoo());

        Element Classifica = fasc.addElement("Classifica");
        Classifica.addText(fascicolo.getClassifica());


            Element Anno = fasc.addElement("Anno");
            Anno.addText(fascicolo.getAnno());
        if (!isNotSfascicola){
            Element Progressivo = fasc.addElement("Progressivo");
            Progressivo.addText(fascicolo.getProgressivo());
        }else{
            Element Progressivo = fasc.addElement("Progressivo");
            Progressivo.addText("");
        }
        return (Element) fasc.clone();

    }

}