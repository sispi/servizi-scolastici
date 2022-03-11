package it.kdm.docer.fonte.batch.popolamentoFonte.objects;

public class AnagraficaCustomDescriptor {

    private String typeId;
    private String codiceFieldName;
    private String descrizioneFieldName;
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getCodiceFieldName() {
        return codiceFieldName;
    }
    public void setCodiceFieldName(String codiceFieldName) {
        this.codiceFieldName = codiceFieldName;
    }
    public String getDescrizioneFieldName() {
        return descrizioneFieldName;
    }
    public void setDescrizioneFieldName(String descrizioneFieldName) {
        this.descrizioneFieldName = descrizioneFieldName;
    }
}
