package keysuite.docer.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Fascicolo extends DocerBean{

    public final static String PROGR_SEP = "/";
    public static final String TYPE = "fascicolo" ;

    public Fascicolo(){
        super();
    }

    public Fascicolo(String classifica, String anno, String progressivo, String piano){
        this();
        setProgrFascicolo(progressivo);
        setAnno(anno);
        setClassifica(classifica);
        setPianoClass(piano);
    }

    @Override
    public String getDocerId() {
        return ClientUtils.joinFascicoloId(classifica,anno,progrFascicolo,null);
    }

    @Override
    public String getName() {
        return getDesFascicolo();
    }


    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getClassifica() {
        return classifica;
    }

    public void setClassifica(String classifica) {
        this.classifica = classifica;
    }

    public String getProgrFascicolo() {
        return progrFascicolo;
    }

    public void setProgrFascicolo(String progrFascicolo) {
        this.progrFascicolo = progrFascicolo;

        if (progrFascicolo!=null){
            int idx0 = progrFascicolo.lastIndexOf(PROGR_SEP);
            if (idx0>0){
                this.setParentProgrFascicolo(progrFascicolo.substring(0,idx0));
            } else {
                this.setParentProgrFascicolo("");
            }
        } else {
            this.setParentProgrFascicolo(null);
        }
    }

    public String getParentProgrFascicolo() {
        if (parentProgrFascicolo!=null)
            return parentProgrFascicolo;

        if (progrFascicolo==null)
            return null;
        int idx0 = progrFascicolo.lastIndexOf(PROGR_SEP);
        if (idx0>0){
            return progrFascicolo.substring(0,idx0);
        } else {
            return "";
        }
    }

    public void setParentProgrFascicolo(String parentProgrFascicolo) {
        this.parentProgrFascicolo = parentProgrFascicolo;
    }

    public String getDesFascicolo() {
        return desFascicolo;
    }

    public void setDesFascicolo(String desFascicolo) {
        this.desFascicolo = desFascicolo;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    //@JsonProperty(value = "FASCICOLO_ID", access = JsonProperty.Access.READ_ONLY)
    public String getIdAndPiano(){
        return ClientUtils.joinFascicoloId(classifica,anno,progrFascicolo,pianoClass);
    }

    public void setDocerid(String docerid){
        setIdAndPiano(docerid,null);
    }

    public void setIdAndPiano(String docerId, String piano){
        String[] splitted = ClientUtils.splitFascicoloId(docerId);
        if (splitted!=null) {
            setClassifica(splitted[0]);
            setAnno(splitted[1]);
            setProgrFascicolo(splitted[2]);
            setPianoClass(piano);
        } else {
            setClassifica(null);
            setAnno(null);
            setProgrFascicolo(null);
            setPianoClass(null);
        }
    }

    @JsonProperty("ANNO_FASCICOLO")
    private String anno;

    @JsonProperty("TYPE_ID")
    private String typeId;

    @JsonProperty("CLASSIFICA")
    private String classifica;

    @JsonProperty("PROGR_FASCICOLO")
    private String progrFascicolo;

    @JsonProperty("PARENT_PROGR_FASCICOLO")
    private String parentProgrFascicolo;

    @JsonProperty("DES_FASCICOLO")
    String desFascicolo;

    public String getPianoClass() {
        return pianoClass;
    }

    public void setPianoClass(String pianoClass) {
        this.pianoClass = pianoClass;
    }

    @JsonProperty("PIANO_CLASS")
    String pianoClass;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @JsonProperty("BUSINESS_TYPE")
    String businessType;

    @Override
    protected Integer getRightsMask() {
        return fascicolo_mask;
    }
}
