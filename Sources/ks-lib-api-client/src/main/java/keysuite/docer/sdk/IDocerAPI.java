package keysuite.docer.sdk;

import keysuite.docer.client.*;

public interface IDocerAPI {

    IDocerAPI ping();
    //NamedInputStream openURL(URL url);
    IDocerAPI setTimeout(int timeout);
    //IDocerAPI bearer(SwaggerClient.addBeaererHeader bearer);
    IDocumenti documenti();
    IFascicoli fascicoli();
    ITitolari titolari();
    IFiles files();
    IAnagrafiche anagrafiche();
    ICartelle cartelle();
    IFirma firma();
    IGruppi gruppi();
    IUtenti utenti();

}
