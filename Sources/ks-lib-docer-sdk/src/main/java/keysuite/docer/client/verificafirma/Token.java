package keysuite.docer.client.verificafirma;

import java.util.ArrayList;
import java.util.List;

public class Token {

    public enum Indication{
        TOTAL_PASSED,
        TOTAL_FAILED,
        INDETERMINATE,
        NO_SIGNATURE_FOUND;
    }

    protected String issuer;
    protected String signingTime;
    protected String level;
    protected Indication indication;
    protected List<String> infos = new ArrayList<>();
    protected List<String> warnings = new ArrayList<>();
    protected List<String> errors = new ArrayList<>();
    protected List<String> chain = new ArrayList<>();

    public List<String> getInfos() {
        return infos;
    }

    public void setInfos(List<String> infos) {
        this.infos = infos;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getChain() {
        return chain;
    }

    public void setChain(List<String> chain) {
        this.chain = chain;
    }

    public Indication getIndication() {
        return indication;
    }

    public void setIndication(Indication indication) {
        this.indication = indication;
    }

    public String getSigningTime() {
        return signingTime;
    }

    public void setSigningTime(String signingTime) {
        this.signingTime = signingTime;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
