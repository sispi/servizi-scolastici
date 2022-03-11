package keysuite.desktop.exceptions;

import java.io.Serializable;

public class KSExceptionBadRequest extends KSException implements Serializable {

    private static final long serialVersionUID = -2338621292552177485L;

    public KSExceptionBadRequest(Throwable throwable, String message) {
        super(Code.F400,(message==null && throwable!=null)?throwable.getMessage():message,throwable);
    }

    public KSExceptionBadRequest(Throwable throwable) {
        this(throwable,null);
    }

    public KSExceptionBadRequest(String message) {
        this(null,message);
    }

    public KSExceptionBadRequest() {
        this(null,"BadRequest");
    }
}
