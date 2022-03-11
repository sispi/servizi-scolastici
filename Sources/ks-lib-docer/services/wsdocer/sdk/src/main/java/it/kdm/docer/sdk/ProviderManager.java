package it.kdm.docer.sdk;

import it.kdm.docer.sdk.exceptions.DocerException;

 
public class ProviderManager {

	public static IProvider create(final String providerType) throws DocerException { 
			
		IProvider p = null;
        	try {
				// instanzio il provider 				
				p = (IProvider)Class.forName(providerType).newInstance();
			} catch (InstantiationException e) {
				throw new DocerException("Errore nell'instanza del provider: InstantiationException: " + providerType + ": " + e.getMessage());			
			} catch (IllegalAccessException e1) {
				throw new DocerException("Errore nell'instanza del provider: IllegalAccessException: " + providerType + ": " + e1.getMessage());
			} catch (ClassNotFoundException e1) {
				throw new DocerException("Errore nell'instanza del provider: ClassNotFoundException: " + providerType + ": " + e1.getMessage());
			} catch (NoClassDefFoundError x) {
				throw new DocerException("Errore nell'instanza del provider: NoClassDefFoundError: " + providerType + ": " + x.getMessage());
			}
        
        return p;				
	}
	
}
