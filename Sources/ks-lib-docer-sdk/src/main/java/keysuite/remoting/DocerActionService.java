package keysuite.remoting;

import keysuite.desktop.exceptions.KSException;

import java.util.Map;

public interface DocerActionService {

    static final String DEPLOYMENT_ID = "DeployementId";
    static final String WORKITEM_ID = "WorkitemId";
    static final String INSTANCE_ID = "InstanceId";
    static final String OPERATION_ID = "OperationId";
    static final String OPERATION_RETRY = "OperationRetry";

    Map<String,Object> execute(Map<String,Object> context, Map<String,Object> inputs) throws KSException;
}
