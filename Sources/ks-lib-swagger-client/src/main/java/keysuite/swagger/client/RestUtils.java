package keysuite.swagger.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.ObjectProperty;
import keysuite.docer.client.FileServiceCommon;
import keysuite.docer.client.NamedInputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.activation.DataSource;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class RestUtils {

    public static final String FILE_PREFIX = "file:";
    public static final String BODY_TMP = "body.tmp";/*
    public static final Integer CONNECT_TIMEOUT = 60000;
    public static final Integer READ_TIMEOUT = 5000;*/
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String MULTIPART = "multipart/*";

    public static boolean isObject(ArrayProperty ap)
    {
        if (ap.getItems() instanceof ObjectProperty
                && ((ObjectProperty) ap.getItems()).getProperties().size()==2)
        {
            for( String pKey : ((ObjectProperty) ap.getItems()).getProperties().keySet() )
                if ("key".endsWith(getLocalName(pKey)))
                    return true;
        }
        return false;
    }

    public static boolean checkContentType(String contentType, String... patterns)
    {
        if (contentType==null)
            return false;

        for( int i=0; i<patterns.length; i++)
        {
            if (patterns[i].endsWith("*"))
                patterns[i] = patterns[i].substring(0,patterns[i].length()-1);
            if (contentType.toLowerCase().startsWith(patterns[i].toLowerCase()))
                return true;
        }

        return false;
    }

    final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String percentEncode(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return percentEncode(inputStream);
        } catch ( IOException io){
            throw new RuntimeException(io);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String percentEncode(InputStream inputStream) {

        StringBuilder sb = new StringBuilder();

        try {

            byte[] bytes = new byte[1024];

            while (true)
            {
                int i=inputStream.read(bytes);

                if (i==-1)
                    break;

                char[] hexChars = new char[i * 3];

                for ( int j = 0; j < i; j++ ) {
                    int v = bytes[j] & 0xFF;
                    hexChars[j * 2] = '%';
                    hexChars[j * 2 + 1] = hexArray[v >>> 4];
                    hexChars[j * 2 + 2] = hexArray[v & 0x0F];
                }

                sb.append(hexChars);
            }

        } catch ( IOException io) {
            throw new RuntimeException(io);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public static boolean checkStatus(Integer responseCode, String acceptedStatus)
    {
        for( String range : acceptedStatus.split("\\,") )
        {
            String[] parts = range.split("-");

            long lower = 0;
            long upper = 599;

            if (parts.length>0 && parts[0].matches("\\d+"))
                lower = Integer.parseInt(parts[0]);

            if (parts.length>1 && parts[1].matches("\\d+"))
                upper = Integer.parseInt(parts[1]);

            if (parts.length==1)
                upper = lower;

            if (responseCode>=lower && responseCode<=upper)
                return true;
        }
        return false;
    }

    public static Object cast(String type, Object value)
    {
        if (value==null)
            return null;

        if ("number".equals(type))
            return Double.parseDouble(value.toString());
        else if ("integer".equals(type))
            return Integer.parseInt(value.toString());
        else if ("float".equals(type))
            return Float.parseFloat(value.toString());
        else if ("boolean".equals(type))
            return Boolean.parseBoolean(value.toString());
        else
            return value;
    }

    public static MimeMultipart getMultipartContent(final InputStream is, final String contentType, final String name)
    {
        DataSource ds = new DataSource() {
            @Override
            public InputStream getInputStream() throws IOException { return is; }
            @Override
            public OutputStream getOutputStream() throws IOException { throw new UnsupportedOperationException("entity can't be written to"); }
            @Override
            public String getContentType() { return contentType; }
            @Override
            public String getName() { return name; }
        };

        try {
            return new MimeMultipart(ds);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /*RequestContext ctx = new RequestContext()

            @Override
            public String getContentType() {
                return entity.getContentType().getValue();
            }

            @Override
            public int getContentLength() {
                return ((Long)entity.getContentLength()).intValue();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return entity.getContent();
            }
        };

        try {
            ServletFileUpload sfu = new ServletFileUpload();
            sfu.setFileItemFactory(new DiskFileItemFactory());
            return sfu.parseRequest(ctx);
        } catch (FileUploadException e) {
            throw new RuntimeException(e);
        }*/

    }

    public static InputStream toStream(InputStream stream, String format) throws IOException
    {
        if ("byte".equals(format) || "base64".equals(format) )
        {
            String base64 = IOUtils.toString(stream, RestUtils.DEFAULT_CHARSET);
            byte[] bytes = Base64.decodeBase64(base64);
            stream = new ByteArrayInputStream(bytes);
            //result = RestUtils.toFile(new ByteArrayInputStream(bytes),filename);
        }

        if (stream instanceof EofSensorInputStream){
            //if (length==-1) {
            byte[] bytes = IOUtils.toByteArray(stream);
            stream = new ByteArrayInputStream(bytes);
        }

        return stream;
    }

    public static InputStream toStream(HttpEntity entity, String format) throws IOException
    {
        InputStream stream = entity.getContent();

        if ("byte".equals(format) || "base64".equals(format) )
        {
            String base64 = EntityUtils.toString(entity, RestUtils.DEFAULT_CHARSET);
            byte[] bytes = Base64.decodeBase64(base64);
            stream = new ByteArrayInputStream(bytes);
            //result = RestUtils.toFile(new ByteArrayInputStream(bytes),filename);
        }

        if (stream instanceof EofSensorInputStream){
            //if (length==-1) {
            byte[] bytes = IOUtils.toByteArray(stream);
            stream = new ByteArrayInputStream(bytes);
        }

        return stream;
    }

    public static String toFile(InputStream initialStream, String filename)
    {
        try {
            FileServiceCommon fsc = new FileServiceCommon(null, null);

            String fileId = fsc.create(
                    NamedInputStream.getNamedInputStream(initialStream, filename),
                    "swagger"
            );

            File targetFile = fsc.getFile(fileId);

        /*String tmpPath = FileServiceCommon.getStorePath("swagger");


        if (!Strings.isNullOrEmpty(folderRef)){
            tmpPath += "/" + folderRef;
        }

        if (!Strings.isNullOrEmpty(filename))
            tmpPath += "/" + UUID.randomUUID().toString();
        else
            filename = UUID.randomUUID().toString() + ".tmp";

        File targetFile = new File(tmpPath,filename);
        //File targetFile = new File(Files.createTempDir(),filename);

        try {
            FileUtils.copyInputStreamToFile(initialStream, targetFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

            return FILE_PREFIX + "//" + targetFile.getPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isFile(Object value)
    {
        if (value instanceof File)
            return true;
        if (value == null)
            return false;
        String path = value.toString();
        return (!Strings.isNullOrEmpty(path) && path.startsWith(FILE_PREFIX));
        //return (!Strings.isNullOrEmpty(path) && new File(path).exists());
    }

    public static boolean isStreamSource(Object value) {
        return isFile(value) || value instanceof InputStreamBody || value instanceof InputStream;
    }

    public static InputStreamBody getStreamSource(Object value) {

        if (value==null)
            throw new RuntimeException("invalid value for stream source:"+value);

        if (isFile(value))
            value = getFile(value);

        if (value instanceof InputStreamBody){
            return (InputStreamBody) value;
        } else if (value instanceof InputStream){
            InputStream is = (InputStream) value;
            return new InputStreamBody(is, org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM, "file.bin");
        } else if (value instanceof File){
            File file = (File) value;
            ContentType ct = org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM;
            try {
                String pct = java.nio.file.Files.probeContentType(file.toPath());
                ct = ContentType.parse(pct);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return new InputStreamBody(new FileInputStream(file), ct, file.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } /*else if (isJSON(value.toString())){
            InputStream is = IOUtils.toInputStream(value.toString());
            return new InputStreamBody(is, ContentType.APPLICATION_JSON, "file.json");
        } else if (isXML(value.toString())){
            InputStream is = IOUtils.toInputStream(value.toString());
            return new InputStreamBody(is, ContentType.APPLICATION_JSON, "file.xml");
        }*/
        throw new RuntimeException("invalid value for stream source:"+value);
    }

    public static File getFile(Object value){

        if (value instanceof File)
            return (File) value;

        if (value == null)
            return null;

        String path = value.toString();

        if (path.startsWith(FILE_PREFIX))
            path = path.substring(FILE_PREFIX.length());

        if (path.startsWith("///"))
            path = path.substring(2);

        return new File(path);
    }

    public static boolean isXML(String xml)
    {
        if (Strings.isNullOrEmpty(xml))
            return false;

        try {
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            db.parse(is);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Document parseXML(String xml)
    {
        if (Strings.isNullOrEmpty(xml))
            return null;

        try {
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));

            Document doc = db.parse(is);
            return doc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toXML(Node doc)
    {
        try
        {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.getBuffer().toString();
        }
        catch(TransformerException e)
        {
            throw new RuntimeException("Invalid content to XML",e);
        }
    }

    public static boolean isJSON(String json)
    {
        if (json==null)
            return false;
        char first = json.trim().charAt(0);

        try
        {
            if (first=='{')
                parseJSON(json);
            else
                return false;

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public static boolean isJSONList(String json)
    {
        if (json==null)
            return false;
        char first = json.trim().charAt(0);

        try
        {
            if (first=='[')
                parseJSONList(json);
            else
                return false;

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public static Object parseScalar(String json){

        if (Strings.isNullOrEmpty(json))
            return null;

        json = json.trim();

        if (json.startsWith("\"") && json.endsWith("\""))
            return json.substring(1,json.length()-1);
        else if ("null".equals(json))
            return null;
        else if ("true".equals(json))
            return true;
        else if ("false".equals(json))
            return false;
        else
            try {
                return NumberFormat.getInstance().parse(json);
            } catch (ParseException e) {
                return json;
            }
    }

    public static boolean isJSONScalar(String json){
        if (Strings.isNullOrEmpty(json))
            return false;

        return json.matches("\\s*(\".*\"|\\d+|\\d+\\.\\d+|true|false|null)\\s*");
    }

    public final static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).enable(SerializationFeature.INDENT_OUTPUT);

    public static Map<String,Object> parseJSON(String json)
    {

        TypeReference<HashMap<String,Object>> typeRef
                = new TypeReference<HashMap<String,Object>>() {};

        try {
            return mapper.readValue(json, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON",e);
        }
    }

    public static List<Object> parseJSONList(String json)
    {
        TypeReference<List<Object>> typeRef
                = new TypeReference<List<Object>>() {};

        try {
            return mapper.readValue(json, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON",e);
        }
    }

    public static String toJSON(Object obj)
    {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid content to JSON",e);
        }
    }

    public static HttpClient getHttpClient(Integer readTimeout, Integer connectTimeout) {

        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(readTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectTimeout)
                .build();

        HttpClientBuilder clientBuilder = HttpClientBuilder.create()
                .setDefaultRequestConfig(config);

        //per fare redirect anche dei POST
        clientBuilder.setRedirectStrategy(new RedirectAllStrategy());

        return clientBuilder.build();
    }

    public static String getCollectionFormat(String format)
    {
        if (format==null || format.equals("csv"))
            return ",";
        else if (format.equals("pipes"))
            return "|";
        else if (format.equals("tsv"))
            return "\t";
        else if (format.equals("ssv"))
            return " ";
        else
            return format;
    }

    public static List getParamAsList(Object value, String collectionFormat) {
        List values = new ArrayList();

        if (value.getClass().isArray()){
            int l = Array.getLength(value);
            for ( int i=0; i<l; i++)
                values.add( getParamAsList( Array.get(value,i), collectionFormat ) );
        } else if (value instanceof List) {
            values = (List) value;
        } else if (value!=null) {
            values.add(value);
        }

        if (values.size()>0 && collectionFormat!=null && !collectionFormat.equals("multi"))
        {
            collectionFormat = getCollectionFormat(collectionFormat);
            collectionFormat = org.apache.commons.lang3.StringUtils.join(values,collectionFormat);
            values.clear();
            values.add(collectionFormat);
        }

        return values;
    }

    public static List<String> getParamAsStrings(Object value, String collectionFormat) {

        ArrayList<String> strings = new ArrayList<String>();

        if (value==null)
            return strings;

        if (value.getClass().isArray()){
            int l = Array.getLength(value);
            for ( int i=0; i<l; i++)
                strings.addAll( getParamAsStrings(Array.get(value,i),"," ) );
        } else if (value instanceof List) {
            for( Object item : (List) value )
                strings.addAll( getParamAsStrings(item,"," ) );
        } else if (value!=null) {
            strings.add(value.toString());
        }

        if (strings.size()>0 && collectionFormat!=null && !collectionFormat.equals("multi"))
        {
            collectionFormat = getCollectionFormat(collectionFormat);
            collectionFormat = org.apache.commons.lang3.StringUtils.join(strings,collectionFormat);
            strings.clear();
            strings.add(collectionFormat);
        }

        return strings;
    }

    public static Object fromJSON(String json) {
        if (json.trim().startsWith("["))
            return parseJSONList(json);
        else if (json.trim().startsWith("{"))
            return parseJSON(json);
        else
            return parseJSONList("["+json+"]").get(0);
    }

    static String getLocalName(Node node)
    {
        if (node==null)
            return null;
        else if (node.getLocalName()!=null)
            return node.getLocalName();
        else
            return getLocalName(node.getNodeName());
    }

    static String getLocalName(String name)
    {
        if (name==null)
            return null;
        else
            return name.substring(name.indexOf(":")+1);
    }

    static class RedirectAllStrategy extends DefaultRedirectStrategy {
        @Override
        protected boolean isRedirectable(final String method) {
            return true;
        }
    }
}
