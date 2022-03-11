package it.kdm.utils;

import net.sf.saxon.TransformerFactoryImpl;

public class TransformerFactory {
    
    public static TransformerFactoryImpl newInstance() {
        return new TransformerFactoryImpl();
    }
}
