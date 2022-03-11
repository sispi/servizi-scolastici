package docer.exception;

import keysuite.desktop.exceptions.Code;
import keysuite.desktop.exceptions.KSException;

public class ActionRuntimeException extends KSException {

    public ActionRuntimeException(Exception cause){
        super(cause);
    }

    public ActionRuntimeException(String message){
        super(Code.H408,message);
    }

    public ActionRuntimeException(Code code,String message){
        super(code,message);
    }
}
