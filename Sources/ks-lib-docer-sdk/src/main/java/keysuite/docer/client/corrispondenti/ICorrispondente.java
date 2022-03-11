package keysuite.docer.client.corrispondenti;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.underscore.lodash.U;
import io.swagger.annotations.ApiModelProperty;
import keysuite.docer.client.ClientUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXISTING_PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

//@JsonDeserialize(using = CorrDeserializer.class )
@JsonTypeInfo(use = NAME, include = EXISTING_PROPERTY, property = "tipoCorrispondente")
@JsonSubTypes({
        //@JsonSubTypes.Type(value= CorrispondenteInterno.class, name = "UO"),
        @JsonSubTypes.Type(value= Persona.class, name = "Persona"),
        @JsonSubTypes.Type(value= PersonaGiuridica.class, name = "PersonaGiuridica"),
        @JsonSubTypes.Type(value= Amministrazione.class, name = "Amministrazione")
})
public interface ICorrispondente {
    @ApiModelProperty(example = "Ufficio 01")
    String getDenominazione();
    @ApiModelProperty(example = "UFFICIO01", allowEmptyValue = true)
    String getIdentificativo();

    @ApiModelProperty(example = "ufficio01@domain.com", allowEmptyValue = true)
    String getIndirizzoTelematico();

    @ApiModelProperty(example = "ufficio01@domain.com", allowEmptyValue = true)
    String getIndirizzoPostale();

    Mezzo getMezzo();

    String getRiferimentoMezzo();

    String getCodiceRubrica();

    //@ApiModelProperty(example = "UO", allowableValues = "UO,PF,PG,PA")
    String getTipoCorrispondente();

    Map<String,Object> toXmlMap();

    default String toXml(){
        return toXml(null);
    }

    static <T> T valueOf(Object obj, T def){
        if (obj instanceof String || obj instanceof Number)
            return (T) obj ;
        else if (obj instanceof Map && ((Map)obj).containsKey("#text")) {
            String text = (String) ((Map) obj).get("#text");
            if (text!=null)
                return (T) text.trim();
        }
        return def;
    }

    static <T> T valueOf(Object obj){
        return valueOf(obj,null);
    }

    default String toXml(String root){

        String xml;
        if (root==null)
            xml = U.toXml(toXmlMap());
        else{
            Map map = toXmlMap();

            Map map2 = new LinkedHashMap(){{
                put(root, map);
            }};

            xml = U.toXml(map2);
        }

        return ClientUtils.cleanUXml(xml);
    }


    public enum Mezzo {
        PEC,
        PEO,
        Posta,
        RAR,
        ROL,
        FAX,
        Brevimano,
        Altro
    }
}
