package keysuite.desktop.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(value = { "stackTrace","cause","localizedMessage","suppressed" } )
public interface CodeException {

    static CodeException getCodeException(Throwable exception){
        CodeException ce;
        if (exception instanceof CodeException){
            ce = (CodeException) exception;
        } else {
            ce = new KSRuntimeException(exception);
        }
        return ce;
    }

    Code getCode();
    String getUrl();
    CodeException setUrl(String url);
    Map<String,Object> getDetails();
    String getStack();
    String getCauseStack();
    String getMessage();
    String getType();
    String getCauseMessage();
    boolean isRetryable();

}
