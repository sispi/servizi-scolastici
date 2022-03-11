package it.kdm.docer.digitalstamp.provider.intesi;

import it.kdm.docer.digitalstamp.provider.intesi.stub.HandlerStub;

import javax.activation.DataHandler;

/**
 * Created by antsic on 10/08/17.
 */
public class DigitalStampDTO {
    private DataHandler documento;
    private HandlerStub.KeyValueData[] metadata;
    private int stampPage;
    private int positionX;
    private int positionY;
    private String  positionTag;
    private String correctionLevel;
    private String signer;
    private String pin;
    private String otp;
    private int action;
    private int qrsize;
    private boolean isShortnedURL;
    private String url;
    private String typeDoc;
    private String templateName;
    private String locale;
    private String title;
    private String exDate;



    public DataHandler getDocumento() {
        return documento;
    }

    public void setDocumento(DataHandler documento) {
        this.documento = documento;
    }

    public HandlerStub.KeyValueData[] getMetadata() {
        return metadata;
    }

    public void setMetadata(HandlerStub.KeyValueData[] metadata) {
        this.metadata = metadata;
    }

    public int getStampPage() {
        return stampPage;
    }

    public void setStampPage(int stampPage) {
        this.stampPage = stampPage;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public String getPositionTag() {
        return positionTag;
    }

    public void setPositionTag(String positionTag) {
        this.positionTag = positionTag;
    }

    public String getCorrectionLevel() {
        return correctionLevel;
    }

    public void setCorrectionLevel(String correctionLevel) {
        this.correctionLevel = correctionLevel;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getQrsize() {
        return qrsize;
    }

    public void setQrsize(int qrsize) {
        this.qrsize = qrsize;
    }

    public boolean isShortnedURL() {
        return isShortnedURL;
    }

    public void setShortnedURL(boolean isShortnedURL) {
        this.isShortnedURL = isShortnedURL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExDate() {
        return exDate;
    }

    public void setExDate(String exDate) {
        this.exDate = exDate;
    }
}
