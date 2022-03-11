package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Currency.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Currency">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ADP"/>
 *     &lt;enumeration value="AED"/>
 *     &lt;enumeration value="AFA"/>
 *     &lt;enumeration value="AFN"/>
 *     &lt;enumeration value="ALL"/>
 *     &lt;enumeration value="AMD"/>
 *     &lt;enumeration value="ANG"/>
 *     &lt;enumeration value="AOA"/>
 *     &lt;enumeration value="ARS"/>
 *     &lt;enumeration value="ATS"/>
 *     &lt;enumeration value="AUD"/>
 *     &lt;enumeration value="AWG"/>
 *     &lt;enumeration value="AZM"/>
 *     &lt;enumeration value="AZN"/>
 *     &lt;enumeration value="BAM"/>
 *     &lt;enumeration value="BBD"/>
 *     &lt;enumeration value="BDT"/>
 *     &lt;enumeration value="BGL"/>
 *     &lt;enumeration value="BGN"/>
 *     &lt;enumeration value="BHD"/>
 *     &lt;enumeration value="BIF"/>
 *     &lt;enumeration value="BMD"/>
 *     &lt;enumeration value="BND"/>
 *     &lt;enumeration value="BOB"/>
 *     &lt;enumeration value="BOV"/>
 *     &lt;enumeration value="BRL"/>
 *     &lt;enumeration value="BSD"/>
 *     &lt;enumeration value="BTN"/>
 *     &lt;enumeration value="BWP"/>
 *     &lt;enumeration value="BYR"/>
 *     &lt;enumeration value="BZD"/>
 *     &lt;enumeration value="CAD"/>
 *     &lt;enumeration value="CDF"/>
 *     &lt;enumeration value="CHE"/>
 *     &lt;enumeration value="CHF"/>
 *     &lt;enumeration value="CHW"/>
 *     &lt;enumeration value="CLF"/>
 *     &lt;enumeration value="CLP"/>
 *     &lt;enumeration value="CNY"/>
 *     &lt;enumeration value="COP"/>
 *     &lt;enumeration value="COU"/>
 *     &lt;enumeration value="CRC"/>
 *     &lt;enumeration value="CUP"/>
 *     &lt;enumeration value="CVE"/>
 *     &lt;enumeration value="CYP"/>
 *     &lt;enumeration value="CZK"/>
 *     &lt;enumeration value="DEM"/>
 *     &lt;enumeration value="DJF"/>
 *     &lt;enumeration value="DKK"/>
 *     &lt;enumeration value="DOP"/>
 *     &lt;enumeration value="DZD"/>
 *     &lt;enumeration value="EEK"/>
 *     &lt;enumeration value="EGP"/>
 *     &lt;enumeration value="ERN"/>
 *     &lt;enumeration value="ESP"/>
 *     &lt;enumeration value="ETB"/>
 *     &lt;enumeration value="EUR"/>
 *     &lt;enumeration value="FIM"/>
 *     &lt;enumeration value="FJD"/>
 *     &lt;enumeration value="FKP"/>
 *     &lt;enumeration value="FRF"/>
 *     &lt;enumeration value="GBP"/>
 *     &lt;enumeration value="GEL"/>
 *     &lt;enumeration value="GHC"/>
 *     &lt;enumeration value="GIP"/>
 *     &lt;enumeration value="GMD"/>
 *     &lt;enumeration value="GNF"/>
 *     &lt;enumeration value="GRD"/>
 *     &lt;enumeration value="GTQ"/>
 *     &lt;enumeration value="GWP"/>
 *     &lt;enumeration value="GYD"/>
 *     &lt;enumeration value="HKD"/>
 *     &lt;enumeration value="HNL"/>
 *     &lt;enumeration value="HRK"/>
 *     &lt;enumeration value="HTG"/>
 *     &lt;enumeration value="HUF"/>
 *     &lt;enumeration value="IDR"/>
 *     &lt;enumeration value="IEP"/>
 *     &lt;enumeration value="ILS"/>
 *     &lt;enumeration value="INR"/>
 *     &lt;enumeration value="IQD"/>
 *     &lt;enumeration value="IRR"/>
 *     &lt;enumeration value="ISK"/>
 *     &lt;enumeration value="ITL"/>
 *     &lt;enumeration value="JMD"/>
 *     &lt;enumeration value="JOD"/>
 *     &lt;enumeration value="JPY"/>
 *     &lt;enumeration value="KES"/>
 *     &lt;enumeration value="KGS"/>
 *     &lt;enumeration value="KHR"/>
 *     &lt;enumeration value="KMF"/>
 *     &lt;enumeration value="KPW"/>
 *     &lt;enumeration value="KRW"/>
 *     &lt;enumeration value="KWD"/>
 *     &lt;enumeration value="KYD"/>
 *     &lt;enumeration value="KZT"/>
 *     &lt;enumeration value="LAK"/>
 *     &lt;enumeration value="LBP"/>
 *     &lt;enumeration value="LKR"/>
 *     &lt;enumeration value="LRD"/>
 *     &lt;enumeration value="LSL"/>
 *     &lt;enumeration value="LTL"/>
 *     &lt;enumeration value="LUF"/>
 *     &lt;enumeration value="LVL"/>
 *     &lt;enumeration value="LYD"/>
 *     &lt;enumeration value="MAD"/>
 *     &lt;enumeration value="MDL"/>
 *     &lt;enumeration value="MGA"/>
 *     &lt;enumeration value="MGF"/>
 *     &lt;enumeration value="MKD"/>
 *     &lt;enumeration value="MMK"/>
 *     &lt;enumeration value="MNT"/>
 *     &lt;enumeration value="MOP"/>
 *     &lt;enumeration value="MRO"/>
 *     &lt;enumeration value="MTL"/>
 *     &lt;enumeration value="MUR"/>
 *     &lt;enumeration value="MVR"/>
 *     &lt;enumeration value="MWK"/>
 *     &lt;enumeration value="MXN"/>
 *     &lt;enumeration value="MXV"/>
 *     &lt;enumeration value="MYR"/>
 *     &lt;enumeration value="MZM"/>
 *     &lt;enumeration value="MZN"/>
 *     &lt;enumeration value="NAD"/>
 *     &lt;enumeration value="NGN"/>
 *     &lt;enumeration value="NIO"/>
 *     &lt;enumeration value="NLG"/>
 *     &lt;enumeration value="NOK"/>
 *     &lt;enumeration value="NPR"/>
 *     &lt;enumeration value="NZD"/>
 *     &lt;enumeration value="OMR"/>
 *     &lt;enumeration value="PAB"/>
 *     &lt;enumeration value="PEN"/>
 *     &lt;enumeration value="PGK"/>
 *     &lt;enumeration value="PHP"/>
 *     &lt;enumeration value="PKR"/>
 *     &lt;enumeration value="PLN"/>
 *     &lt;enumeration value="PTE"/>
 *     &lt;enumeration value="PYG"/>
 *     &lt;enumeration value="QAR"/>
 *     &lt;enumeration value="ROL"/>
 *     &lt;enumeration value="RON"/>
 *     &lt;enumeration value="RSD"/>
 *     &lt;enumeration value="RUB"/>
 *     &lt;enumeration value="RUR"/>
 *     &lt;enumeration value="RWF"/>
 *     &lt;enumeration value="SAR"/>
 *     &lt;enumeration value="SBD"/>
 *     &lt;enumeration value="SCR"/>
 *     &lt;enumeration value="SDD"/>
 *     &lt;enumeration value="SDG"/>
 *     &lt;enumeration value="SEK"/>
 *     &lt;enumeration value="SGD"/>
 *     &lt;enumeration value="SHP"/>
 *     &lt;enumeration value="SIT"/>
 *     &lt;enumeration value="SKK"/>
 *     &lt;enumeration value="SLL"/>
 *     &lt;enumeration value="SOS"/>
 *     &lt;enumeration value="SRD"/>
 *     &lt;enumeration value="SRG"/>
 *     &lt;enumeration value="STD"/>
 *     &lt;enumeration value="SVC"/>
 *     &lt;enumeration value="SYP"/>
 *     &lt;enumeration value="SZL"/>
 *     &lt;enumeration value="THB"/>
 *     &lt;enumeration value="TJS"/>
 *     &lt;enumeration value="TMM"/>
 *     &lt;enumeration value="TND"/>
 *     &lt;enumeration value="TOP"/>
 *     &lt;enumeration value="TPE"/>
 *     &lt;enumeration value="TRL"/>
 *     &lt;enumeration value="TRY"/>
 *     &lt;enumeration value="TTD"/>
 *     &lt;enumeration value="TWD"/>
 *     &lt;enumeration value="TZS"/>
 *     &lt;enumeration value="UAH"/>
 *     &lt;enumeration value="UGX"/>
 *     &lt;enumeration value="USD"/>
 *     &lt;enumeration value="USN"/>
 *     &lt;enumeration value="USS"/>
 *     &lt;enumeration value="UYU"/>
 *     &lt;enumeration value="UZS"/>
 *     &lt;enumeration value="VEB"/>
 *     &lt;enumeration value="VEF"/>
 *     &lt;enumeration value="VND"/>
 *     &lt;enumeration value="VUV"/>
 *     &lt;enumeration value="WST"/>
 *     &lt;enumeration value="XAF"/>
 *     &lt;enumeration value="XCD"/>
 *     &lt;enumeration value="XOF"/>
 *     &lt;enumeration value="XPF"/>
 *     &lt;enumeration value="YER"/>
 *     &lt;enumeration value="YUM"/>
 *     &lt;enumeration value="ZAR"/>
 *     &lt;enumeration value="ZMK"/>
 *     &lt;enumeration value="ZWD"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Currency")
@XmlEnum
public enum Currency {

    ADP,
    AED,
    AFA,
    AFN,
    ALL,
    AMD,
    ANG,
    AOA,
    ARS,
    ATS,
    AUD,
    AWG,
    AZM,
    AZN,
    BAM,
    BBD,
    BDT,
    BGL,
    BGN,
    BHD,
    BIF,
    BMD,
    BND,
    BOB,
    BOV,
    BRL,
    BSD,
    BTN,
    BWP,
    BYR,
    BZD,
    CAD,
    CDF,
    CHE,
    CHF,
    CHW,
    CLF,
    CLP,
    CNY,
    COP,
    COU,
    CRC,
    CUP,
    CVE,
    CYP,
    CZK,
    DEM,
    DJF,
    DKK,
    DOP,
    DZD,
    EEK,
    EGP,
    ERN,
    ESP,
    ETB,
    EUR,
    FIM,
    FJD,
    FKP,
    FRF,
    GBP,
    GEL,
    GHC,
    GIP,
    GMD,
    GNF,
    GRD,
    GTQ,
    GWP,
    GYD,
    HKD,
    HNL,
    HRK,
    HTG,
    HUF,
    IDR,
    IEP,
    ILS,
    INR,
    IQD,
    IRR,
    ISK,
    ITL,
    JMD,
    JOD,
    JPY,
    KES,
    KGS,
    KHR,
    KMF,
    KPW,
    KRW,
    KWD,
    KYD,
    KZT,
    LAK,
    LBP,
    LKR,
    LRD,
    LSL,
    LTL,
    LUF,
    LVL,
    LYD,
    MAD,
    MDL,
    MGA,
    MGF,
    MKD,
    MMK,
    MNT,
    MOP,
    MRO,
    MTL,
    MUR,
    MVR,
    MWK,
    MXN,
    MXV,
    MYR,
    MZM,
    MZN,
    NAD,
    NGN,
    NIO,
    NLG,
    NOK,
    NPR,
    NZD,
    OMR,
    PAB,
    PEN,
    PGK,
    PHP,
    PKR,
    PLN,
    PTE,
    PYG,
    QAR,
    ROL,
    RON,
    RSD,
    RUB,
    RUR,
    RWF,
    SAR,
    SBD,
    SCR,
    SDD,
    SDG,
    SEK,
    SGD,
    SHP,
    SIT,
    SKK,
    SLL,
    SOS,
    SRD,
    SRG,
    STD,
    SVC,
    SYP,
    SZL,
    THB,
    TJS,
    TMM,
    TND,
    TOP,
    TPE,
    TRL,
    TRY,
    TTD,
    TWD,
    TZS,
    UAH,
    UGX,
    USD,
    USN,
    USS,
    UYU,
    UZS,
    VEB,
    VEF,
    VND,
    VUV,
    WST,
    XAF,
    XCD,
    XOF,
    XPF,
    YER,
    YUM,
    ZAR,
    ZMK,
    ZWD;

    public String value() {
        return name();
    }

    public static Currency fromValue(String v) {
        return valueOf(v);
    }

}
