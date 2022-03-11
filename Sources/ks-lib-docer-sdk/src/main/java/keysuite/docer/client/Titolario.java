package keysuite.docer.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Titolario extends DocerBean {

    public static final String TYPE = "titolario";

    public final static String CLASS_SEP = ".";

    public Titolario(){
        super();
    }

    public Titolario(String classifica){
        this();
        setClassifica(ClientUtils.getOnlyClass(classifica));
        setPianoClass(ClientUtils.getOnlyPiano(classifica));
    }

    public Titolario(String classifica, String piano){
        this();
        setClassifica(classifica);
        setPianoClass(piano);
    }

    @Override
    public String getDocerId() {
        return getClassifica();
    }

    @Override
    public String getName() {
        return getDesTitolario();
    }

    @JsonProperty("CLASSIFICA")
    private String classifica;

    @JsonProperty("PARENT_CLASSIFICA")
    private String parentClassifica;

    @JsonProperty("DES_TITOLARIO")
    String desTitolario;

    public String getPianoClass() {
        return pianoClass;
    }

    public void setPianoClass(String pianoClass) {
        this.pianoClass = pianoClass;
    }

    @JsonProperty("PIANO_CLASS")
    String pianoClass;

    public String getClassifica() {
        return classifica;
    }

    public void setClassifica(String classifica) {
        this.classifica = classifica;
        if (classifica!=null){
            int idx = classifica.lastIndexOf(CLASS_SEP);
            if (idx>0)
                this.setParentClassifica(classifica.substring(0,idx));
        }
    }

    public String getParentClassifica() {
        return parentClassifica;
    }

    public void setParentClassifica(String parentClassifica) {
        this.parentClassifica = parentClassifica;
    }

    public String getDesTitolario() {
        return desTitolario;
    }

    public void setDesTitolario(String desTitolario) {
        this.desTitolario = desTitolario;
    }

    @Override
    protected Integer getRightsMask() {
        return titolario_mask;
    }
}
