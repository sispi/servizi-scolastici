package it.kdm.firma;

import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;

public class SignerFactory {

    public static Signer get(String profile){
        profile = profile.replace("-","_");
        String[] parts = profile.split("_");
        String form = parts[0].substring(0,2).toUpperCase()
                +  parts[0].substring(2,3).toLowerCase()
                +  parts[0].substring(3,5).toUpperCase();

        if (parts.length==1)
            return get(SignatureForm.valueOf(form));
        else
            return get(SignatureLevel.valueOf(form+"_BASELINE_"+parts[1].toUpperCase()));
    }

    public static Signer get(SignatureLevel level){
        switch (level.getSignatureForm()){
            case CAdES:
                return new CAdESSigner(level);
            case XAdES:
                return new XAdESSigner(level);
            case PAdES:
                return new PAdESSigner(level);
            default:
                throw new UnsupportedOperationException("invalid profile "+level);
        }
    }

    public static Signer get(SignatureForm profile){
        switch (profile){
            case CAdES:
                return new CAdESSigner();
            case XAdES:
                return new XAdESSigner();
            case PAdES:
                return new PAdESSigner();
            default:
                throw new UnsupportedOperationException("invalid profile "+profile);
        }
    }
}
