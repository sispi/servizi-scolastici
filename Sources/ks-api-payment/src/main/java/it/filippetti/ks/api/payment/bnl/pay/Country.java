package it.filippetti.ks.api.payment.bnl.pay;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Country.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Country">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ABW"/>
 *     &lt;enumeration value="AFG"/>
 *     &lt;enumeration value="AGO"/>
 *     &lt;enumeration value="AIA"/>
 *     &lt;enumeration value="ALB"/>
 *     &lt;enumeration value="AND"/>
 *     &lt;enumeration value="ANT"/>
 *     &lt;enumeration value="ARE"/>
 *     &lt;enumeration value="ARG"/>
 *     &lt;enumeration value="ARM"/>
 *     &lt;enumeration value="ASM"/>
 *     &lt;enumeration value="ATA"/>
 *     &lt;enumeration value="ATF"/>
 *     &lt;enumeration value="ATG"/>
 *     &lt;enumeration value="AUS"/>
 *     &lt;enumeration value="AUT"/>
 *     &lt;enumeration value="AZE"/>
 *     &lt;enumeration value="BDI"/>
 *     &lt;enumeration value="BEL"/>
 *     &lt;enumeration value="BEN"/>
 *     &lt;enumeration value="BFA"/>
 *     &lt;enumeration value="BGD"/>
 *     &lt;enumeration value="BGR"/>
 *     &lt;enumeration value="BHR"/>
 *     &lt;enumeration value="BHS"/>
 *     &lt;enumeration value="BIH"/>
 *     &lt;enumeration value="BLR"/>
 *     &lt;enumeration value="BLZ"/>
 *     &lt;enumeration value="BMU"/>
 *     &lt;enumeration value="BOL"/>
 *     &lt;enumeration value="BRA"/>
 *     &lt;enumeration value="BRB"/>
 *     &lt;enumeration value="BRN"/>
 *     &lt;enumeration value="BTN"/>
 *     &lt;enumeration value="BVT"/>
 *     &lt;enumeration value="BWA"/>
 *     &lt;enumeration value="CAF"/>
 *     &lt;enumeration value="CAN"/>
 *     &lt;enumeration value="CCK"/>
 *     &lt;enumeration value="CHE"/>
 *     &lt;enumeration value="CHL"/>
 *     &lt;enumeration value="CHN"/>
 *     &lt;enumeration value="CIV"/>
 *     &lt;enumeration value="CMR"/>
 *     &lt;enumeration value="COD"/>
 *     &lt;enumeration value="COG"/>
 *     &lt;enumeration value="COK"/>
 *     &lt;enumeration value="COL"/>
 *     &lt;enumeration value="COM"/>
 *     &lt;enumeration value="CPV"/>
 *     &lt;enumeration value="CRI"/>
 *     &lt;enumeration value="CUB"/>
 *     &lt;enumeration value="CXR"/>
 *     &lt;enumeration value="CYM"/>
 *     &lt;enumeration value="CYP"/>
 *     &lt;enumeration value="CZE"/>
 *     &lt;enumeration value="DEU"/>
 *     &lt;enumeration value="DJI"/>
 *     &lt;enumeration value="DMA"/>
 *     &lt;enumeration value="DNK"/>
 *     &lt;enumeration value="DOM"/>
 *     &lt;enumeration value="DZA"/>
 *     &lt;enumeration value="ECU"/>
 *     &lt;enumeration value="EGY"/>
 *     &lt;enumeration value="ERI"/>
 *     &lt;enumeration value="ESH"/>
 *     &lt;enumeration value="ESP"/>
 *     &lt;enumeration value="EST"/>
 *     &lt;enumeration value="ETH"/>
 *     &lt;enumeration value="FIN"/>
 *     &lt;enumeration value="FJI"/>
 *     &lt;enumeration value="FLK"/>
 *     &lt;enumeration value="FRA"/>
 *     &lt;enumeration value="FRO"/>
 *     &lt;enumeration value="FSM"/>
 *     &lt;enumeration value="FXX"/>
 *     &lt;enumeration value="GAB"/>
 *     &lt;enumeration value="GBR"/>
 *     &lt;enumeration value="GEO"/>
 *     &lt;enumeration value="GHA"/>
 *     &lt;enumeration value="GIB"/>
 *     &lt;enumeration value="GIN"/>
 *     &lt;enumeration value="GLP"/>
 *     &lt;enumeration value="GMB"/>
 *     &lt;enumeration value="GNB"/>
 *     &lt;enumeration value="GNQ"/>
 *     &lt;enumeration value="GRC"/>
 *     &lt;enumeration value="GRD"/>
 *     &lt;enumeration value="GRL"/>
 *     &lt;enumeration value="GTM"/>
 *     &lt;enumeration value="GUF"/>
 *     &lt;enumeration value="GUM"/>
 *     &lt;enumeration value="GUY"/>
 *     &lt;enumeration value="HKG"/>
 *     &lt;enumeration value="HMD"/>
 *     &lt;enumeration value="HND"/>
 *     &lt;enumeration value="HRV"/>
 *     &lt;enumeration value="HTI"/>
 *     &lt;enumeration value="HUN"/>
 *     &lt;enumeration value="IDN"/>
 *     &lt;enumeration value="IND"/>
 *     &lt;enumeration value="IOT"/>
 *     &lt;enumeration value="IRL"/>
 *     &lt;enumeration value="IRN"/>
 *     &lt;enumeration value="IRQ"/>
 *     &lt;enumeration value="ISL"/>
 *     &lt;enumeration value="ISR"/>
 *     &lt;enumeration value="ITA"/>
 *     &lt;enumeration value="JAM"/>
 *     &lt;enumeration value="JOR"/>
 *     &lt;enumeration value="JPN"/>
 *     &lt;enumeration value="KAZ"/>
 *     &lt;enumeration value="KEN"/>
 *     &lt;enumeration value="KGZ"/>
 *     &lt;enumeration value="KHM"/>
 *     &lt;enumeration value="KIR"/>
 *     &lt;enumeration value="KNA"/>
 *     &lt;enumeration value="KOR"/>
 *     &lt;enumeration value="KWT"/>
 *     &lt;enumeration value="LAO"/>
 *     &lt;enumeration value="LBN"/>
 *     &lt;enumeration value="LBR"/>
 *     &lt;enumeration value="LBY"/>
 *     &lt;enumeration value="LCA"/>
 *     &lt;enumeration value="LIE"/>
 *     &lt;enumeration value="LKA"/>
 *     &lt;enumeration value="LSO"/>
 *     &lt;enumeration value="LTU"/>
 *     &lt;enumeration value="LUX"/>
 *     &lt;enumeration value="LVA"/>
 *     &lt;enumeration value="MAC"/>
 *     &lt;enumeration value="MAR"/>
 *     &lt;enumeration value="MCO"/>
 *     &lt;enumeration value="MDA"/>
 *     &lt;enumeration value="MDG"/>
 *     &lt;enumeration value="MDV"/>
 *     &lt;enumeration value="MEX"/>
 *     &lt;enumeration value="MHL"/>
 *     &lt;enumeration value="MKD"/>
 *     &lt;enumeration value="MLI"/>
 *     &lt;enumeration value="MLT"/>
 *     &lt;enumeration value="MMR"/>
 *     &lt;enumeration value="MNG"/>
 *     &lt;enumeration value="MNP"/>
 *     &lt;enumeration value="MOZ"/>
 *     &lt;enumeration value="MRT"/>
 *     &lt;enumeration value="MSR"/>
 *     &lt;enumeration value="MTQ"/>
 *     &lt;enumeration value="MUS"/>
 *     &lt;enumeration value="MWI"/>
 *     &lt;enumeration value="MYS"/>
 *     &lt;enumeration value="MYT"/>
 *     &lt;enumeration value="NAM"/>
 *     &lt;enumeration value="NCL"/>
 *     &lt;enumeration value="NER"/>
 *     &lt;enumeration value="NFK"/>
 *     &lt;enumeration value="NGA"/>
 *     &lt;enumeration value="NIC"/>
 *     &lt;enumeration value="NIU"/>
 *     &lt;enumeration value="NLD"/>
 *     &lt;enumeration value="NOR"/>
 *     &lt;enumeration value="NPL"/>
 *     &lt;enumeration value="NRU"/>
 *     &lt;enumeration value="NZL"/>
 *     &lt;enumeration value="OMN"/>
 *     &lt;enumeration value="PAK"/>
 *     &lt;enumeration value="PAN"/>
 *     &lt;enumeration value="PCN"/>
 *     &lt;enumeration value="PER"/>
 *     &lt;enumeration value="PHL"/>
 *     &lt;enumeration value="PLW"/>
 *     &lt;enumeration value="PNG"/>
 *     &lt;enumeration value="POL"/>
 *     &lt;enumeration value="PRI"/>
 *     &lt;enumeration value="PRK"/>
 *     &lt;enumeration value="PRT"/>
 *     &lt;enumeration value="PRY"/>
 *     &lt;enumeration value="PSE"/>
 *     &lt;enumeration value="PYF"/>
 *     &lt;enumeration value="QAT"/>
 *     &lt;enumeration value="REU"/>
 *     &lt;enumeration value="ROU"/>
 *     &lt;enumeration value="RUS"/>
 *     &lt;enumeration value="RWA"/>
 *     &lt;enumeration value="SAU"/>
 *     &lt;enumeration value="SDN"/>
 *     &lt;enumeration value="SEN"/>
 *     &lt;enumeration value="SGP"/>
 *     &lt;enumeration value="SGS"/>
 *     &lt;enumeration value="SHN"/>
 *     &lt;enumeration value="SJM"/>
 *     &lt;enumeration value="SLB"/>
 *     &lt;enumeration value="SLE"/>
 *     &lt;enumeration value="SLV"/>
 *     &lt;enumeration value="SMR"/>
 *     &lt;enumeration value="SOM"/>
 *     &lt;enumeration value="SPM"/>
 *     &lt;enumeration value="STP"/>
 *     &lt;enumeration value="SUR"/>
 *     &lt;enumeration value="SVK"/>
 *     &lt;enumeration value="SVN"/>
 *     &lt;enumeration value="SWE"/>
 *     &lt;enumeration value="SWZ"/>
 *     &lt;enumeration value="SYC"/>
 *     &lt;enumeration value="SYR"/>
 *     &lt;enumeration value="TCA"/>
 *     &lt;enumeration value="TCD"/>
 *     &lt;enumeration value="TGO"/>
 *     &lt;enumeration value="THA"/>
 *     &lt;enumeration value="TJK"/>
 *     &lt;enumeration value="TKL"/>
 *     &lt;enumeration value="TKM"/>
 *     &lt;enumeration value="TLS"/>
 *     &lt;enumeration value="TON"/>
 *     &lt;enumeration value="TTO"/>
 *     &lt;enumeration value="TUN"/>
 *     &lt;enumeration value="TUR"/>
 *     &lt;enumeration value="TUV"/>
 *     &lt;enumeration value="TWN"/>
 *     &lt;enumeration value="TZA"/>
 *     &lt;enumeration value="UGA"/>
 *     &lt;enumeration value="UKR"/>
 *     &lt;enumeration value="UMI"/>
 *     &lt;enumeration value="URY"/>
 *     &lt;enumeration value="USA"/>
 *     &lt;enumeration value="UZB"/>
 *     &lt;enumeration value="VAT"/>
 *     &lt;enumeration value="VCT"/>
 *     &lt;enumeration value="VEN"/>
 *     &lt;enumeration value="VGB"/>
 *     &lt;enumeration value="VIR"/>
 *     &lt;enumeration value="VNM"/>
 *     &lt;enumeration value="VUT"/>
 *     &lt;enumeration value="WLF"/>
 *     &lt;enumeration value="WSM"/>
 *     &lt;enumeration value="YEM"/>
 *     &lt;enumeration value="YUG"/>
 *     &lt;enumeration value="ZAF"/>
 *     &lt;enumeration value="ZMB"/>
 *     &lt;enumeration value="ZWE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Country")
@XmlEnum
public enum Country {

    ABW,
    AFG,
    AGO,
    AIA,
    ALB,
    AND,
    ANT,
    ARE,
    ARG,
    ARM,
    ASM,
    ATA,
    ATF,
    ATG,
    AUS,
    AUT,
    AZE,
    BDI,
    BEL,
    BEN,
    BFA,
    BGD,
    BGR,
    BHR,
    BHS,
    BIH,
    BLR,
    BLZ,
    BMU,
    BOL,
    BRA,
    BRB,
    BRN,
    BTN,
    BVT,
    BWA,
    CAF,
    CAN,
    CCK,
    CHE,
    CHL,
    CHN,
    CIV,
    CMR,
    COD,
    COG,
    COK,
    COL,
    COM,
    CPV,
    CRI,
    CUB,
    CXR,
    CYM,
    CYP,
    CZE,
    DEU,
    DJI,
    DMA,
    DNK,
    DOM,
    DZA,
    ECU,
    EGY,
    ERI,
    ESH,
    ESP,
    EST,
    ETH,
    FIN,
    FJI,
    FLK,
    FRA,
    FRO,
    FSM,
    FXX,
    GAB,
    GBR,
    GEO,
    GHA,
    GIB,
    GIN,
    GLP,
    GMB,
    GNB,
    GNQ,
    GRC,
    GRD,
    GRL,
    GTM,
    GUF,
    GUM,
    GUY,
    HKG,
    HMD,
    HND,
    HRV,
    HTI,
    HUN,
    IDN,
    IND,
    IOT,
    IRL,
    IRN,
    IRQ,
    ISL,
    ISR,
    ITA,
    JAM,
    JOR,
    JPN,
    KAZ,
    KEN,
    KGZ,
    KHM,
    KIR,
    KNA,
    KOR,
    KWT,
    LAO,
    LBN,
    LBR,
    LBY,
    LCA,
    LIE,
    LKA,
    LSO,
    LTU,
    LUX,
    LVA,
    MAC,
    MAR,
    MCO,
    MDA,
    MDG,
    MDV,
    MEX,
    MHL,
    MKD,
    MLI,
    MLT,
    MMR,
    MNG,
    MNP,
    MOZ,
    MRT,
    MSR,
    MTQ,
    MUS,
    MWI,
    MYS,
    MYT,
    NAM,
    NCL,
    NER,
    NFK,
    NGA,
    NIC,
    NIU,
    NLD,
    NOR,
    NPL,
    NRU,
    NZL,
    OMN,
    PAK,
    PAN,
    PCN,
    PER,
    PHL,
    PLW,
    PNG,
    POL,
    PRI,
    PRK,
    PRT,
    PRY,
    PSE,
    PYF,
    QAT,
    REU,
    ROU,
    RUS,
    RWA,
    SAU,
    SDN,
    SEN,
    SGP,
    SGS,
    SHN,
    SJM,
    SLB,
    SLE,
    SLV,
    SMR,
    SOM,
    SPM,
    STP,
    SUR,
    SVK,
    SVN,
    SWE,
    SWZ,
    SYC,
    SYR,
    TCA,
    TCD,
    TGO,
    THA,
    TJK,
    TKL,
    TKM,
    TLS,
    TON,
    TTO,
    TUN,
    TUR,
    TUV,
    TWN,
    TZA,
    UGA,
    UKR,
    UMI,
    URY,
    USA,
    UZB,
    VAT,
    VCT,
    VEN,
    VGB,
    VIR,
    VNM,
    VUT,
    WLF,
    WSM,
    YEM,
    YUG,
    ZAF,
    ZMB,
    ZWE;

    public String value() {
        return name();
    }

    public static Country fromValue(String v) {
        return valueOf(v);
    }

}
