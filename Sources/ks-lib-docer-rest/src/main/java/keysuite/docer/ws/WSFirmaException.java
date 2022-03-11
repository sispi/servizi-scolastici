package keysuite.docer.ws;

public class WSFirmaException extends Exception{

    private static final long serialVersionUID = 1461663513444L;

    //private it.kdm.doctoolkit.clients.firma.WSFirmaStub.WSFirmaFirmaException faultMessage;


    public WSFirmaException() {
        super("WSFirmaException");
    }

    public WSFirmaException(String s) {
        super(s);
    }

    public WSFirmaException(String s, Throwable ex) {
        super(s, ex);
    }

    public WSFirmaException(Throwable cause) {
        super(cause);
    }


    /*public void setFaultMessage(it.kdm.doctoolkit.clients.firma.WSFirmaStub.WSFirmaFirmaException msg){
        faultMessage = msg;
    }

    public it.kdm.doctoolkit.clients.firma.WSFirmaStub.WSFirmaFirmaException getFaultMessage(){
        return faultMessage;
    }*/
}
