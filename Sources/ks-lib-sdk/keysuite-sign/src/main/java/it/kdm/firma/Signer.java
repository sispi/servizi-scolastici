package it.kdm.firma;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureForm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.service.http.commons.TimestampDataLoader;
import eu.europa.esig.dss.service.tsp.OnlineTSPSource;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import it.kdm.orchestratore.utils.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

public abstract class Signer {

    protected String tspServer = "http://dss.nowina.lu/pki-factory/tsa/good-tsa";
    protected SignatureLevel level = null;
    protected SignatureForm profile;
    protected SignaturePackaging packaging = null;
    protected DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;
    Pkcs12SignatureToken token = null;

    String alias = null;

    public Signer withLevel(SignatureLevel level){
        this.level = level;
        return this;
    }

    public Signer withTimestampServer(String tspServer){
        this.tspServer = tspServer;
        return this;
    }

    public OnlineTSPSource getTSPSource(){
        OnlineTSPSource tspSource = new OnlineTSPSource(tspServer);
        tspSource.setDataLoader(new TimestampDataLoader()); // uses the specific content-type
        return tspSource;
    }

    public Signer withPackaging(SignaturePackaging packaging){
        this.packaging = packaging;
        return this;
    }

    public Signer withDigestAlgorithm(DigestAlgorithm digestAlgorithm){
        this.digestAlgorithm = digestAlgorithm;
        return this;
    }

    public Signer withKeyStore(String keyStore, String password) throws IOException {
        InputStream is = ResourceUtils.getResourceAsStream(keyStore+".p12");
        this.token = new Pkcs12SignatureToken(is,new KeyStore.PasswordProtection(password.toCharArray()));
        return this;
    }

    protected DSSPrivateKeyEntry getPrivateKey(){
        DSSPrivateKeyEntry privateKey;
        if (alias!=null)
            privateKey = token.getKey(alias);
        else
            privateKey = token.getKeys().get(0);
        return privateKey;
    }

    public Signer withAlias(String alias){
        this.alias = alias;
        return this;
    }

    public DSSDocument signDocument(InputStream stream) {
        return signDocument(new InMemoryDocument(stream));
    }

    public DSSDocument signDocument(byte[] bytes) throws IOException {
        return signDocument(new InMemoryDocument(bytes));
    }

    public DSSDocument signDocument(File file) throws IOException {
        return signDocument(new FileDocument(file));
    }

    public abstract DSSDocument signDocument(DSSDocument document);

    public DSSDocument timestampDocument(InputStream stream) {
        return timestampDocument(new InMemoryDocument(stream));
    }

    public DSSDocument timestampDocument(byte[] bytes) throws IOException {
        return timestampDocument(new InMemoryDocument(bytes));
    }

    public DSSDocument timestampDocument(File file) throws IOException {
        return timestampDocument(new FileDocument(file));
    }

    public abstract DSSDocument timestampDocument(DSSDocument document);
}
