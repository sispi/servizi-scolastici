package it.kdm.docer.core.tracer.bean;

/**
 * Created by antsic on 12/12/14.
 */
public class Configuration {


    private boolean isEnabledJMS;
    private String methodToSkipJMS;

    public boolean isEnabledJMS() {
        return isEnabledJMS;
    }

    public void setEnabledJMS(boolean isEnabledJMS) {
        this.isEnabledJMS = isEnabledJMS;
    }

    public String getMethodToSkipJMS() {
        return methodToSkipJMS;
    }

    public void setMethodToSkipJMS(String methodToSkipJMS) {
        this.methodToSkipJMS = methodToSkipJMS;
    }
}
