package it.kdm.sign.factory;

import it.kdm.sign.enums.TipoFirma;

import java.util.HashMap;
import java.util.Map;

public class DocumentSignerFactory {
	
	private static Map <TipoFirma,SignerStrategy> signers = new HashMap<>();
	
	static {
		signers.put(TipoFirma.AUTOMATICA, new AutomaticSignerStrategy());
		signers.put(TipoFirma.REMOTA, new RemoteSignerStrategy());
	}
	
	public static SignerStrategy getInstance( TipoFirma tipo ){
		return signers.get(tipo);
	}
	
	public static void main(String[] args) {
		SignerStrategy d = DocumentSignerFactory.getInstance(TipoFirma.REMOTA);
		System.out.println(d);
	}

}



