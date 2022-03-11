package keysuite.swagger.client;

import bpmn2.ServiceException;

import java.util.Map;

public class SOAPFaultException {

    public static ServiceException create(Map<String,Object> fault)
    {
        return new ServiceException(
                (String) (fault.containsKey("faultcode") ? fault.get("faultcode") : fault.get("Code")),
                (String) (fault.containsKey("faultstring") ? fault.get("faultstring") : fault.get("Reason")),
                fault.containsKey("detail") ? fault.get("detail") : fault.get("Detail")
        );
    }

    public static ServiceException create(Map<String,Object> fault, Integer httpStatus)
    {
        return new ServiceException(
                (String) (fault.containsKey("faultcode") ? fault.get("faultcode") : fault.get("Code")),
                (String) (fault.containsKey("faultstring") ? fault.get("faultstring") : fault.get("Reason")),
                fault.containsKey("detail") ? fault.get("detail") : fault.get("Detail"),
                httpStatus
        );
    }
}
