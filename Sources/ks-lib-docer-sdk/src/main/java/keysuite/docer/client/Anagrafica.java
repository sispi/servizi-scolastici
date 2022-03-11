package keysuite.docer.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Anagrafica extends DocerBean {

    public Anagrafica(){
    }

    @Override
    public String getDocerId() {
        return getCodice();
    }

    @Override
    public String getName() {
        return getDescrizione();
    }

    /*@Override
    public String getType() {
        return getTypeId();
    }*/

    @JsonCreator
    public Anagrafica(@JsonProperty("type") String type){
        this();
        //this.typeId=type;
        this.setType(type);
    }

    public Anagrafica(String typeId, String codice){
        this();
        //this.typeId = typeId;
        //this.codice = codice;
        this.setTypeId(typeId);
        this.setCodice(codice);
    }

    @JsonProperty("TYPE_ID")
    public String getTypeId() {
        return getType();
    }

    public void setTypeId(String typeId) {
        this.setType(typeId);
    }

    @JsonIgnore
    public String getCodice() {
        if (getTypeId()!=null)
            return (String) otherFields().get("COD_"+getTypeId().toUpperCase());
        else
            return null;
    }

    public void setCodice(String codice) {
        if (getTypeId()!=null)
            otherFields().put("COD_"+getTypeId().toUpperCase(),codice);
        else
            throw new UnsupportedOperationException("unkown typeId");
    }

    @JsonIgnore
    public String getDescrizione() {
        if (getTypeId()!=null)
            return (String) otherFields().get("DES_"+getTypeId().toUpperCase());
        else
            return null;
    }

    public void setDescrizione(String descrizione) {
        if (getTypeId()!=null)
            otherFields().put("DES_"+getTypeId().toUpperCase(),descrizione);
        else
            throw new UnsupportedOperationException("unkown typeId");
    }

    //String typeId;
    //String codice;
    //String descrizione;
    //String codiceParent;

    @JsonIgnore
    public String getCodiceParent() {
        if (getTypeId()!=null)
            return (String) otherFields().get("PARENT_COD_"+getTypeId().toUpperCase());
        else
            return null;
    }

    public void setCodiceParent(String codiceParent) {
        if (getTypeId()!=null)
            otherFields().put("PARENT_COD_"+getTypeId().toUpperCase(),codiceParent);
        else
            throw new UnsupportedOperationException("unkown typeId");
    }

    @Override
    protected Integer getRightsMask() {
        return anagrafica_mask;
    }

    /*@Override
    @JsonAnySetter
    public void setOtherField(String name, Object value) {

        if (("type").equals(name)){
            typeId = (String) value;
            return;
        }

        String typeDef = null;
        if (typeId!=null)
            typeDef = typeId;
        else if (type!=null)
            typeDef = type;

        if (typeDef!=null){
            if (("COD_"+typeDef.toUpperCase()).equals(name)){
                codice = (String) value;
                return;
            }

            if (("DES_"+typeDef.toUpperCase()).equals(name)){
                descrizione = (String) value;
                return;
            }

            if (("PARENT_COD_"+typeDef.toUpperCase()).equals(name)){
                codiceParent = (String) value;
                return;
            }
        }

        super.setOtherField(name,value);
    }*/

    /*@Override
    @JsonAnyGetter
    public Map<String, Object> otherFields() {
        Map<String, Object> map = super.otherFields();
        if (typeId!=null){
            map.put("COD_"+typeId.toUpperCase(),codice);
            map.put("DES_"+typeId.toUpperCase(),descrizione);
            map.put("PARENT_COD_"+typeId.toUpperCase(),codiceParent);
            map.put("type",typeId);
        }
        return map;
    }*/

}
