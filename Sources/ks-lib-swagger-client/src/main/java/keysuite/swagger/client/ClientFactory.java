package keysuite.swagger.client;

import io.swagger.models.Swagger;
import io.swagger.parser.Swagger20Parser;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClientFactory {

    private static Map<String,Swagger> swaggers = new HashMap<>();

    public static Swagger getRESTService( String apiUri ) throws IOException {

        int hash = apiUri.hashCode();
        String key = "rest."+hash;

        Swagger swagger = swaggers.get(key);

        if (swagger==null)
        {
            swagger = new Swagger20Parser().read(apiUri, null);
            swaggers.put(key,swagger);
        }
        return swagger;
    }

    public static Swagger getSOAPService( String wsdl ) throws Exception {
        return getSOAPService(wsdl,null,null);
    }

    public static Swagger getSOAPService( String wsdl , String service ) throws Exception {
        return getSOAPService(wsdl,service,null);
    }

    public static Swagger getSOAPService( String wsdl , String service, String port ) throws Exception {
        int hash = wsdl.hashCode();

        String key = "wsdl" + hash + ";" + ((service == null) ? "" : service) + ";" + ((port == null) ? "" : port);

        Swagger swagger = swaggers.get(key);

        if (swagger==null)
        {
            Class bridgeClass = Class.forName("keysuite.soap.bridge.SOAPBridge");
            Constructor c = bridgeClass.getConstructor(String.class,String.class,String.class);
            Object bridge = c.newInstance(wsdl,service,port);
            Method m = bridgeClass.getDeclaredMethod("build");
            swagger = (Swagger) m.invoke(bridge);

            swaggers.put(key,swagger);
        }

        return swagger;
    }
}
