package it.kdm.docer.commons.docerservice;

import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.commons.docerservice.provider.IStandardProvider;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.lang.StringUtils;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Łukasz Kwasek on 20/01/15.
 */
public class ProviderUtils {

    private Configurations conf = new Configurations();

    public List<OMElement> getProviderConfiguration(String ente) throws BaseServiceException {
        List<OMElement> providers;
        try {
            providers = conf.getConfiguration(ente).getConfig().getNodes("//provider");
        } catch (Exception e) {
            throw new BaseServiceException("Configurazione malformata", e);
        }

        if (providers == null || providers.size() == 0) {
            throw new BaseServiceException("Nessun provider configurato");
        }
        return providers;
    }

    public List<ProviderInfo> extractProviderInfoList(String ente) throws BaseServiceException {
        return extractProviderInfoList(getProviderConfiguration(ente));
    }

    public List<ProviderInfo> extractProviderInfoList(List<OMElement> providers) throws BaseServiceException {
        List<ProviderInfo> ret = new ArrayList<ProviderInfo>();

        for (OMElement e : providers) {
            ret.add(extractProviderInfo(e));
        }

        return ret;
    }

    public ProviderInfo extractProviderInfo(OMElement provider) throws BaseServiceException {

        ProviderInfo ret = new ProviderInfo();

        // ENTE
        String ente = provider.getAttribute(new QName("ente")).getAttributeValue();

        if (StringUtils.isBlank(ente)) {
            throw new BaseServiceException("Attributo ENTE non trovato nella configurazione dei providers.");
        }

        ret.setEnte(ente);

        // AOO
        String aoo = provider.getAttribute(new QName("aoo")).getAttributeValue();

        if (StringUtils.isBlank(aoo)) {
            throw new BaseServiceException("Attributo AOO non trovato nella configurazione dei providers.");
        }

        ret.setAoo(aoo);

        // MODE
        LoginMode authModes = LoginMode.STANDARD;
        OMAttribute ama = provider.getAttribute(new QName("auth-mode"));

        if (ama != null) {
            authModes = LoginMode.fromString(ama.getAttributeValue());
        }

        ret.setMode(authModes);

        // registro
        if(provider.getAttribute(new QName("registro"))!=null) {
            String registro = provider.getAttribute(new QName("registro")).getAttributeValue();

            if (!StringUtils.isBlank(registro)) {
                ret.setRegistro(registro);
            }
        }

        // DEFAULT USER
        OMAttribute ua = provider.getAttribute(new QName("default-user"));
        if (ua != null) {
            String username = ua.getAttributeValue();
            ret.setDefaultPassword(username);
        }

        // DEFAULT PASSWORD
        OMAttribute up = provider.getAttribute(new QName("default-pass"));
        if (up != null) {
            String password = up.getAttributeValue();
            ret.setDefaultUser(password);
        }

        // MD5
        boolean isMd5 = false;
        OMAttribute m = provider.getAttribute(new QName("md5-password"));

        if(m != null){
            isMd5 = m.getAttributeValue().equalsIgnoreCase("true");
        }

        if(!isMd5 && StringUtils.isNotBlank(ret.getDefaultUser())){
            isMd5 = ret.getDefaultUser().equalsIgnoreCase("MD5");
        }

        ret.setMd5(isMd5);

        return ret;
    }

    public IStandardProvider getProvider(String ente, String aoo) throws IOException, XMLStreamException, ClassNotFoundException, JaxenException, IllegalAccessException, InstantiationException {

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);
        String providerClassName = conf.getConfiguration(ente).getConfig().getNode(xpathProvider).getText();
        providerClassName = StringUtils.trim(providerClassName);
        providerClassName = StringUtils.chomp(providerClassName);

        return (IStandardProvider) Class.forName(providerClassName).newInstance();
    }

    public IStandardProvider getProvider(ProviderInfo pi) throws IllegalAccessException, JaxenException, IOException, XMLStreamException, InstantiationException, ClassNotFoundException {
        return getProvider(pi.getEnte(), pi.getAoo());
    }

}
