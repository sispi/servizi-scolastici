package keysuite.docer.client;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;

public interface IMail {
    String sendMail(String oggetto, String body, String mittente, String[] destinatari, String[] destinatari_cc, String[] resources) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden;
    //String sendMail(String docnum, String[] subDestinatari, String[] subComponents) throws ServiceException;
}
