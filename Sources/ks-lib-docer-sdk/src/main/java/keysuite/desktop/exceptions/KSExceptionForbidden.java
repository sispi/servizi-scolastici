package keysuite.desktop.exceptions;

import java.io.Serializable;

public class KSExceptionForbidden extends KSException implements Serializable {

    private static final long serialVersionUID = -2338626292512177485L;

    public KSExceptionForbidden(Throwable throwable, String message) {
        super(Code.F403,(message==null && throwable!=null)?throwable.getMessage():message,throwable);
    }

    public KSExceptionForbidden(Throwable throwable) {
        this(throwable,null);
    }

    public KSExceptionForbidden(String message) {
        this(null,message);
    }

    public KSExceptionForbidden() {
        this(null,"Forbidden");
    }
}
