package it.filippetti.ks.rpc.docer.service;

import docer.action.DocerAction;
import docer.exception.ActionRuntimeException;
import it.kdm.orchestratore.session.ActorsCache;
import java.util.Map;
import keysuite.desktop.exceptions.Code;
import keysuite.desktop.exceptions.KSException;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.remoting.DocerActionService;
import org.springframework.stereotype.Component;

@Component
public class DocerActionServiceImpl implements DocerActionService {

    @Override
    public Map<String, Object> execute(Map<String, Object> context, Map<String, Object> inputs ) throws KSException {

        DocerAction actionInstance;

        String operationId = (String) context.get(DocerActionService.OPERATION_ID);
        String deploymentId = (String) context.get(DocerActionService.DEPLOYMENT_ID);
        String instanceId = String.valueOf(context.get(DocerActionService.INSTANCE_ID));
        String workItemId = String.valueOf(context.get(DocerActionService.WORKITEM_ID));
        Boolean iRetry = Boolean.parseBoolean(String.valueOf(context.get(DocerActionService.OPERATION_RETRY)));

        try {
            actionInstance = (DocerAction) Class.forName("docer.action."+operationId).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        actionInstance
                .withDeploymentId(deploymentId)
                .withProcessInstanceId(Long.parseLong(instanceId))
                .withWorkItemId(Long.parseLong(workItemId))
                .isRetry(iRetry);

        try {
            String ente = actionInstance.getEnte();

            ActorsCache.setThreadCodEnte(ente);

            String bearer = actionInstance.getJWTToken(inputs);

            DocerAction.bearer.set(bearer);

            return actionInstance.execute(inputs);
        } catch (ActionRuntimeException e) {
            KSException exc = new KSException(Code.H408,e.getMessage());
            throw exc;
        } catch (Exception e) {
            KSRuntimeException exc = new KSRuntimeException(Code.H503,e.getMessage());
            throw exc;
        }
    }
}
