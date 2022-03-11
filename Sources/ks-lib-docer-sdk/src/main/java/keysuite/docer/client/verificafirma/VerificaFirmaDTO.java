package keysuite.docer.client.verificafirma;

import java.util.ArrayList;
import java.util.List;

public class VerificaFirmaDTO {
    Token.Indication indication;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    int size;
    List<Signature> signatures = new ArrayList<>();

    public Integer getElapsed() {
        return elapsed;
    }

    public void setElapsed(Integer elapsed) {
        this.elapsed = elapsed;
    }

    Integer elapsed;

    public Token.Indication getIndication() {
        return indication;
    }

    public void setIndication(Token.Indication indication) {
        this.indication = indication;
    }

    public List<Signature> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<Signature> signatures) {
        this.signatures = signatures;
    }

    public List<Timestamp> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Timestamp> timestamps) {
        this.timestamps = timestamps;
    }

    List<Timestamp> timestamps = new ArrayList<>();
}
