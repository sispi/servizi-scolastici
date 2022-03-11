package keysuite.desktop.exceptions;

//import it.kdm.orchestratore.session.TemplateUtils;

public enum Code {

    F400("Bad Request",400),
    F405("TemplateNotFoundException",404),
    F404("ResourceNotFound",404),
    F403("AccessDeniedException",403),
    E000("GenericError",500),
    E001("GenericError",500,true),
    S101("ScriptException",500),
    H4xx("ClientHttpError",400),
    H400("Bad Request",400),
    H401("Unauthorized",401),
    H403("Forbidden",403),
    H404("Not Found",404),
    H405("Method not Allowed",405),
    H406("Not Acceptable",406),
    H407("Proxy Authentication Required",407),
    H408("ClientHttpError",408,true),
    H409("Conflict",409),

    H429("ClientHttpError",429,true),
    H5xx("GenericServerError",500),
    H503("ServerHttpError",503,true),
    H504("ConnectTimeoutException",504,true),
    H505("SocketTimeoutException",505,true);

    private final String type;
    private final boolean retryable;
    private final int httpStatus;

    //static final TemplateUtils utils = new TemplateUtils();

    Code(final String type, int httpStatus) {
        this.type = type;
        this.retryable = false;
        this.httpStatus = httpStatus;
    }

    Code(final String type, int httpStatus, boolean retryable ) {
        this.type = type;
        this.retryable = retryable;
        this.httpStatus = httpStatus;
    }

    public String getType(){
        return type;
    }

    /*public String getMessage(){
        return name(); //return utils.getMessage("error."+name(),type.replaceAll("([^A-Z])([A-Z])", "$1 $2"));
    }*/

    public boolean isRetryable(){
        return retryable;
    }

    public int getHttpStatus(){
        return httpStatus;
    }

    @Override
    public String toString() {
        return name();
    }

    public static Code getByType(String type){
        for(Code v : values()) {
            if (v.getType().equals(type))
                return v;
        }
        return Code.E000;
    }

    public static Code getByThrowable(Throwable exception){
        if (exception instanceof CodeException)
            return ((CodeException)exception).getCode();
        else if (exception!=null)
            return Code.getByType(exception.getClass().getSimpleName());
        else
            return Code.E000;
    }

    public static String getBestMessage(Code code,String message,Throwable throwable){
        if (message!=null)
            return message;
        if (throwable!=null){
            message = throwable.getMessage();
            if (message!=null)
                return message;
            else
                return throwable.getClass().getSimpleName();
        }
        if (code!=null)
            return code.getType();
        else
            return Code.E000.getType();
    }

    public static Code get(String code){
        try{
            return Code.valueOf(code);
        } catch(Exception e) {
            if (code==null)
                return Code.E000;
            if (code.startsWith("H4"))
                return Code.H4xx;
            else if (code.startsWith("H5"))
                return Code.H5xx;
            else
                return Code.E000;
        }
    }
}
