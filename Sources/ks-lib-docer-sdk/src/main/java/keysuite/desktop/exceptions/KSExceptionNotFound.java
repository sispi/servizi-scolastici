package keysuite.desktop.exceptions;

import java.io.Serializable;

public class KSExceptionNotFound extends KSException implements Serializable {

    private static final long serialVersionUID = -2338626292552177585L;

    public KSExceptionNotFound(Throwable throwable, String message) {
        super(Code.F404,(message==null && throwable!=null)?throwable.getMessage():message,throwable);
    }

    public KSExceptionNotFound(Throwable throwable) {
        this(throwable,null);
    }

    public KSExceptionNotFound(String message) {
        this(null,message);
    }

    public KSExceptionNotFound() {
        this(null,"NotFound");
    }
}
