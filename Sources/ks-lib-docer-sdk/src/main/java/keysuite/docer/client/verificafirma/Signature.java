package keysuite.docer.client.verificafirma;

public class Signature extends Token {

    protected String signer;
    protected String signatureFormat;
    protected int position;
    protected String compliancyValidation; //NEGATIVE,POSITIVE,WARNING,INTERMEDIATE

    protected String BestSignatureTime;
    protected Boolean counterSignature;
    protected String extensionPeriodMin;
    protected String extensionPeriodMax;
    protected String parentId;

    protected String cryptographyValidation;
    protected String trustedListValidation;
    protected String certificateValidation;
    protected String revocationStatus;

    public String getSigner() {
        return signer;
    }

    public void setSigner(String cognomeNome) {
        this.signer = cognomeNome;
    }

    public Boolean getCounterSignature() {
        return counterSignature;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSignatureFormat() {
        return signatureFormat;
    }

    public void setSignatureFormat(String signatureFormat) {
        this.signatureFormat = signatureFormat;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCompliancyValidation() {
        return compliancyValidation;
    }

    public void setCompliancyValidation(String compliancyValidation) {
        this.compliancyValidation = compliancyValidation;
    }



    public String getBestSignatureTime() {
        return BestSignatureTime;
    }

    public void setBestSignatureTime(String bestSignatureTime) {
        BestSignatureTime = bestSignatureTime;
    }



    public Boolean isCounterSignature() {
        return counterSignature;
    }

    public void setCounterSignature(Boolean counterSignature) {
        this.counterSignature = counterSignature;
    }

    public String getExtensionPeriodMin() {
        return extensionPeriodMin;
    }

    public void setExtensionPeriodMin(String extensionPeriodMin) {
        this.extensionPeriodMin = extensionPeriodMin;
    }

    public String getExtensionPeriodMax() {
        return extensionPeriodMax;
    }

    public void setExtensionPeriodMax(String extensionPeriodMax) {
        this.extensionPeriodMax = extensionPeriodMax;
    }

    public String getCryptographyValidation() {
        return cryptographyValidation;
    }

    public void setCryptographyValidation(String cryptographyValidation) {
        this.cryptographyValidation = cryptographyValidation;
    }

    public String getTrustedListValidation() {
        return trustedListValidation;
    }

    public void setTrustedListValidation(String trustedListValidation) {
        this.trustedListValidation = trustedListValidation;
    }

    public String getCertificateValidation() {
        return certificateValidation;
    }

    public void setCertificateValidation(String certificateValidation) {
        this.certificateValidation = certificateValidation;
    }

    public String getRevocationStatus() {
        return revocationStatus;
    }

    public void setRevocationStatus(String revocationStatus) {
        this.revocationStatus = revocationStatus;
    }




}
