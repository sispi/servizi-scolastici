package keysuite.desktop.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonPropertyOrder({ "code", "message", "url", "details" })
public class KSException extends Exception implements CodeException, Serializable {

    private static final long serialVersionUID = -2338626292552177485L;

    /*public DesktopException(ErrorDetail error){
        this(error.getCode(),error.getMessage(),error.getCause());
        this.setUrl(error.getUrl());
    }*/

    @JsonProperty("@class")
    public String getExceptionClass(){
        return this.getClass().getName();
    }

    @JsonCreator
    public KSException(@JsonProperty("status") Integer status, @JsonProperty("code") String code, @JsonProperty("message")  String message, @JsonProperty("url") String url, @JsonProperty("details") Map<String,Object> details){
        this( (code!=null || status==null) ? Code.get(code) : Code.get("H"+status),message,url,details);
    }

    public KSException(String code, String message, String url, Map<String,Object> details){
        this(Code.get(code),message,url,details);
    }

    public KSException(Code code, String message, String url, Map<String,Object> details){
        this(code,message,null);
        setUrl(url);
        if (details==null)
            details = new LinkedHashMap<>();
        this.details = details;
    }

    public KSException(Code code,String message,Throwable throwable){
        super(Code.getBestMessage(code,message,throwable),throwable);
        this.code = code;

        StackTraceElement el = this.getStackTrace()[0];

        if (throwable==null && (!el.getMethodName().startsWith("newInstance") || !el.getClassName().equals("jdk.internal.reflect.NativeConstructorAccessorImpl")) )
            this.stack = ExceptionUtils.getStackTrace(this);
        if (this.getCause()!=null)
            this.causeStack = ExceptionUtils.getStackTrace(this.getCause());
    }

    /*public DesktopException(){
        this(Code.E000);
    }*/

    public KSException(int httpstatus, String message, Throwable throwable){
        this( Code.get("H"+httpstatus),message,throwable);
    }

    public KSException(int httpstatus, String message){
        this(httpstatus,message,null);
    }

    public KSException(int httpstatus, Throwable throwable){
        this(httpstatus,null,throwable);
    }

    public KSException(int httpstatus){
        this(httpstatus,null,null);
    }

    public KSException(Code code, String message){
        this(code,message,null);
    }

    public KSException(Code code, Throwable throwable){
        this(code,null,throwable);
    }

    public KSException(Code code){
        this(code,null,null);
    }

    public KSException(Throwable throwable, String message){
        this(Code.getByThrowable(throwable),message,throwable);
    }

    public KSException(Throwable throwable){
        this(Code.getByThrowable(throwable),null,throwable);
    }

    public void setMessage(String message){
        try {
            Field f1 = Throwable.class.getDeclaredField("detailMessage");
            f1.setAccessible(true);
            f1.set(this, message);
        } catch (Exception e) {
        }
    }

    Code code;
    String url = null;
    Map<String,Object> details = new LinkedHashMap<>();
    String stack;
    String causeStack;

    @Override
    public Code getCode() {
        return code;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public Map<String, Object> getDetails() {
        return details;
    }

    @Override
    public String getStack() {
        return stack;
    }

    @Override
    public String getCauseStack() {
        return causeStack;
    }

    @Override
    public String getType() {
        if (code!=null)
            return code.getType();
        else
            return null;
    }

    @Override
    public String getCauseMessage() {
        Throwable cause = getCause();
        if (cause!=null){
            String msg = cause.getMessage();
            if (msg!=null)
                return msg;
            else
                return cause.getClass().getSimpleName();
        }
        return null;
    }

    @Override
    public boolean isRetryable() {
        return (code!=null && code.isRetryable());
    }

    /*@Override
    public String getStackTraceString() {
        return ExceptionUtils.getStackTrace(this);
    }*/

    public KSException addDetail(String key, Object value){
        this.details.put(key,value);
        return this;
    }

    public KSException setUrl(String url){
        this.url = url;
        return this;
    }
}
