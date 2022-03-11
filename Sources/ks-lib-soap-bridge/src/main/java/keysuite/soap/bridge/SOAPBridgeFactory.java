package keysuite.soap.bridge;

import io.swagger.models.Swagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.wsdl.WSDLException;
import java.util.HashMap;
import java.util.Map;

public class SOAPBridgeFactory {

    private static final Logger logger = LoggerFactory.getLogger(SOAPBridgeFactory.class);

    private static Map<String,Swagger> swaggers = new HashMap<>();

    public static Swagger get( String wsdl ) throws WSDLException {
        return get(wsdl,null,null);
    }

    public static Swagger get( String wsdl , String service ) throws WSDLException {
        return get(wsdl,service,null);
    }

    public static Swagger get( String wsdl , String service, String port ) throws WSDLException {

        int hash = wsdl.hashCode();

        String key = "wsdl" + hash + ";" + ((service == null) ? "" : service) + ";" + ((port == null) ? "" : port);

        Swagger swagger = swaggers.get(key);

        if (swagger==null)
        {
            SOAPBridge bridge = new SOAPBridge(wsdl,service,port);
            swagger = bridge.build();

            swaggers.put(key,swagger);

            logger.info("built executor for {}",wsdl);
        }

        return swagger;
    }
}
