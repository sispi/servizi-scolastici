package keysuite.docer.sdk;

import keysuite.swagger.client.SwaggerClient;

public class APIFactory {

    public static <T extends IDocerAPI> T getClient(String apiUri, SwaggerClient.addBeaererHeader bearer){
        return (T) new APIClient(apiUri,bearer);
    }

    public static <T extends IDocerAPI> T getClient(SwaggerClient.addBeaererHeader bearer){
        return (T) new APIClient(bearer);
    }

    public static <T extends IDocerAPI> T getClient(String apiUri, String Authorization){
        return (T) new APIClient(apiUri,Authorization);
    }

    public static <T extends IDocerAPI> T getClient(String Authorization){
        return (T) new APIClient(Authorization);
    }



}
