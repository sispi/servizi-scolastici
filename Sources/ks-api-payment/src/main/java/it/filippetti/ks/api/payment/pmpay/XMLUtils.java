/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.pmpay;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author raffaele
 */
public class XMLUtils {

    /**
     * Converte bean in XML
     *
     * @param <T>
     * @param bean il bean
     * @param qName il QName del bean
     * @return
     * @throws JAXBException
     */
    protected static <T> String marshal(T bean, QName qName) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        Class<T> cl = (Class<T>) bean.getClass();
        JAXBContext jaxbContext = JAXBContext.newInstance(cl);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        // format the XML output
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        JAXBElement<T> root = new JAXBElement<>(qName, cl, (T) bean);
        jaxbMarshaller.marshal(root, stringWriter);
        String result = stringWriter.toString();
        return result;
    }

    /**
     * Converte xml in JavaBean
     *
     * @param <T>
     * @param xml
     * @param cl
     * @return
     * @throws JAXBException
     */
    protected static <T> T unmarshal(String xml, Class<T> cl) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(cl);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<T> root = jaxbUnmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes())), cl);
        return root.getValue();
    }

}
