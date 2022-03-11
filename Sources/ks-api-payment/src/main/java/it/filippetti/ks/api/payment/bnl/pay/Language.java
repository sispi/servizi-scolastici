package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Language.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Language">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="AR"/>
 *     &lt;enumeration value="BE"/>
 *     &lt;enumeration value="BG"/>
 *     &lt;enumeration value="CA"/>
 *     &lt;enumeration value="CS"/>
 *     &lt;enumeration value="DA"/>
 *     &lt;enumeration value="DE"/>
 *     &lt;enumeration value="EL"/>
 *     &lt;enumeration value="EN"/>
 *     &lt;enumeration value="ES"/>
 *     &lt;enumeration value="ET"/>
 *     &lt;enumeration value="FI"/>
 *     &lt;enumeration value="FR"/>
 *     &lt;enumeration value="GA"/>
 *     &lt;enumeration value="HI"/>
 *     &lt;enumeration value="HR"/>
 *     &lt;enumeration value="HU"/>
 *     &lt;enumeration value="IN"/>
 *     &lt;enumeration value="IS"/>
 *     &lt;enumeration value="IT"/>
 *     &lt;enumeration value="IW"/>
 *     &lt;enumeration value="JA"/>
 *     &lt;enumeration value="KO"/>
 *     &lt;enumeration value="LT"/>
 *     &lt;enumeration value="LV"/>
 *     &lt;enumeration value="MK"/>
 *     &lt;enumeration value="MS"/>
 *     &lt;enumeration value="MT"/>
 *     &lt;enumeration value="NL"/>
 *     &lt;enumeration value="NO"/>
 *     &lt;enumeration value="PL"/>
 *     &lt;enumeration value="PT"/>
 *     &lt;enumeration value="RO"/>
 *     &lt;enumeration value="RU"/>
 *     &lt;enumeration value="SK"/>
 *     &lt;enumeration value="SL"/>
 *     &lt;enumeration value="SQ"/>
 *     &lt;enumeration value="SR"/>
 *     &lt;enumeration value="SV"/>
 *     &lt;enumeration value="TH"/>
 *     &lt;enumeration value="TR"/>
 *     &lt;enumeration value="UK"/>
 *     &lt;enumeration value="VI"/>
 *     &lt;enumeration value="ZH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Language")
@XmlEnum
public enum Language {

    AR,
    BE,
    BG,
    CA,
    CS,
    DA,
    DE,
    EL,
    EN,
    ES,
    ET,
    FI,
    FR,
    GA,
    HI,
    HR,
    HU,
    IN,
    IS,
    IT,
    IW,
    JA,
    KO,
    LT,
    LV,
    MK,
    MS,
    MT,
    NL,
    NO,
    PL,
    PT,
    RO,
    RU,
    SK,
    SL,
    SQ,
    SR,
    SV,
    TH,
    TR,
    UK,
    VI,
    ZH;

    public String value() {
        return name();
    }

    public static Language fromValue(String v) {
        return valueOf(v);
    }

}
