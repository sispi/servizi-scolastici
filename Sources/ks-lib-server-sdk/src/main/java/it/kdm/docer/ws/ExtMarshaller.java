package it.kdm.docer.ws;

import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.mime.MimeContainer;
import org.springframework.util.xml.StaxUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.UUID;

public class ExtMarshaller extends Jaxb2Marshaller {

    private static class ByteArrayDataSource implements DataSource {

        private final byte[] data;

        private final String contentType;

        private final int offset;

        private final int length;

        public ByteArrayDataSource(String contentType, byte[] data, int offset, int length) {
            this.contentType = contentType;
            this.data = data;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(this.data, this.offset, this.length);
        }

        @Override
        public OutputStream getOutputStream() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }

        @Override
        public String getName() {
            return "ByteArrayDataSource";
        }
    }

    private static class Jaxb2AttachmentMarshaller2 extends AttachmentMarshaller {

        private final MimeContainer mimeContainer;

        public Jaxb2AttachmentMarshaller2(MimeContainer mimeContainer) {
            this.mimeContainer = mimeContainer;
        }

        @Override
        public String addMtomAttachment(byte[] data, int offset, int length, String mimeType,
                                        String elementNamespace, String elementLocalName) {
            ByteArrayDataSource dataSource = new ByteArrayDataSource(mimeType, data, offset, length);
            return addMtomAttachment(new DataHandler(dataSource), elementNamespace, elementLocalName);
        }

        @Override
        public String addMtomAttachment(DataHandler dataHandler, String elementNamespace, String elementLocalName) {
            String host = getHost(elementNamespace, dataHandler);
            String contentId = UUID.randomUUID() + "@" + host;
            this.mimeContainer.addAttachment("<" + contentId + ">", dataHandler);
            /*try {
                contentId = URLEncoder.encode(contentId, "UTF-8");
            }
            catch (UnsupportedEncodingException ex) {
                // ignore
            }*/
            return "cid:" + contentId;
        }

        private String getHost(String elementNamespace, DataHandler dataHandler) {
            try {
                URI uri = new URI(elementNamespace);
                return uri.getHost();
            }
            catch (URISyntaxException ex) {
                // ignore
            }
            return dataHandler.getName();
        }

        @Override
        public String addSwaRefAttachment(DataHandler dataHandler) {
            String contentId = UUID.randomUUID() + "@" + dataHandler.getName();
            this.mimeContainer.addAttachment(contentId, dataHandler);
            return contentId;
        }

        @Override
        public boolean isXOPPackage() {
            return this.mimeContainer.convertToXopPackage();
        }
    }

    @Override
    public void marshal(Object graph, Result result, MimeContainer mimeContainer) throws XmlMappingException {
        try {
            Marshaller marshaller = createMarshaller();
            if (mimeContainer != null) {
                marshaller.setAttachmentMarshaller(new Jaxb2AttachmentMarshaller2(mimeContainer));
            }
            if (StaxUtils.isStaxResult(result)) {

                XMLStreamWriter streamWriter = StaxUtils.getXMLStreamWriter(result);
                if (streamWriter != null) {
                    marshaller.marshal(graph, streamWriter);
                } else {
                    XMLEventWriter eventWriter = StaxUtils.getXMLEventWriter(result);
                    if (eventWriter == null) {
                        throw new IllegalArgumentException("StAX Result contains neither XMLStreamWriter nor XMLEventConsumer");
                    }

                    marshaller.marshal(graph, eventWriter);
                }
            }
            else {
                marshaller.marshal(graph, result);
            }
        }
        catch (JAXBException ex) {
            throw convertJaxbException(ex);
        }
    }

}
