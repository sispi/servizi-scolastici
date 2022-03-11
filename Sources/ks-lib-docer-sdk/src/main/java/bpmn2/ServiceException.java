package bpmn2;

public class ServiceException extends WorkflowException {



    public ServiceException(String code, String reason, Object detail) {
        super(code,reason,detail);
    }

    public ServiceException(String code, String reason, Object detail, Integer httpStatus) {
        super(code,reason,detail);
        this.httpStatus = httpStatus;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    Integer httpStatus;
}
