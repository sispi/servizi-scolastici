package keysuite.docer.client;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializerBase;
import com.fasterxml.jackson.databind.util.StdConverter;
import com.github.underscore.lodash.U;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;
import keysuite.docer.client.corrispondenti.Corrispondente;
import keysuite.docer.client.corrispondenti.ICorrispondente;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

//import it.kdm.doctoolkit.utils.Utils;

public class Documento extends DocerBean {

    public enum ArchiveType{
        PAPER,
        URL,
        ARCHIVE
    }

    public enum StatoArchivistico{
        GENERICO("0"),
        DEFINITIVO("1"),
        REGISTRATO("2"),
        PROTOCOLLATO("3"),
        CLASSIFICATO("4"),
        FASCICOLATO("5"),
        CONSERVATO("6");

        public String getValue() {
            return value;
        }

        String value;

        StatoArchivistico(String value){
            this.value = value;
        }

        public static StatoArchivistico getValueOf(String value){
            for( StatoArchivistico sa : StatoArchivistico.values() )
                if (sa.value.equals(value))
                    return sa;
            return StatoArchivistico.valueOf(value);
        }

        @Override
        public String toString(){
            return this.value;
        }

    }

    public static final String TYPE = "documento";

    public static final String CLASSIFICHE = "CLASS_SECONDARIE_SS";

    public static class StringURL extends StdConverter<String, URL> {

        @Override
        public URL convert(String value) {
            try {
                if (!value.toLowerCase().startsWith("http")){

                    if (value.contains("/files/"))
                        value = value.substring(value.indexOf("/files/") + 7);
                    //if (!value.startsWith("/docer/"))
                    //    value = "/docer/v1/files/" + value;
                    value = DocerBean.localUrl+"/files/"+value;
                }
                return new URL(value);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class Base64InputStream extends InputStream {

        String base64;
        ByteArrayInputStream stream = null;
        public Base64InputStream(String base64){
            this.base64 = base64;
        }

        @Override
        public int available() throws IOException {
            if (stream==null)
                return (((base64.length() + 2) / 3) << 2);
            else
                return stream.available();
        }

        @Override
        public int read() throws IOException {
            if (stream==null){
                byte[] bytes = Base64.getDecoder().decode(base64);
                stream = new ByteArrayInputStream(bytes);
            }
            return stream.read();
        }

        @Override
        public String toString(){
            if (base64.length()>100)
                return base64.substring(0,100)+"...";
            else
                return base64;
        }
    }

    private static class StreamToStringSerializer extends ToStringSerializerBase {

        public StreamToStringSerializer() { super(Object.class); }

        public StreamToStringSerializer(Class<?> handledType) {
            super(handledType);
        }

        @Override
        public final String valueToString(Object value) {
            if (value instanceof Collection){
                return StringUtils.join(((Collection)value),DocerBean.MV_SEP);
            }else if (value instanceof Base64InputStream)
                return ((Base64InputStream)value).base64;
            else if (value instanceof InputStream){
                try {
                    byte[] bytes = IOUtils.toByteArray((InputStream)value);
                    return Base64.getEncoder().encodeToString(bytes);
                    //return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else
                return value.toString();
        }
    }

    public static String DEFAULT_TYPE = "DOCUMENTO";

    public Documento(){
        super();
        //this.tipologia = DEFAULT_TYPE;
        //this.archiveType = ArchiveType.ARCHIVE;
        //this.statoArchivistico = StatoArchivistico.GENERICO;
    }

    public Documento(String docnum){
        this();
        setDocnum(docnum);
    }

    @Override
    public String getDocerId() {
        return getDocnum();
    }

    @Override
    public String getName() {
        return this.getDocname();
    }

    public String getDocnum() {
        return docnum;
    }

    public void setDocnum(String docnum) {
        this.docnum = docnum;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public String getTipoComponente() {
        return tipoComponente;
    }

    public void setTipoComponente(String tipoComponente) {
        this.tipoComponente = tipoComponente;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String[] getRiferimenti() {
        return riferimenti;
    }

    public void addRiferimento(String... riferimenti) {
        if (this.riferimenti==null){
            this.riferimenti = riferimenti;
        } else if (riferimenti!=null && riferimenti.length>0){
            List<String> list = new ArrayList<>(Arrays.asList(this.riferimenti)) ;
            list.addAll(Arrays.asList(riferimenti));
            this.riferimenti = list.toArray(new String[0]);
        }
    }

    public void removeRiferimento(String... riferimenti ){
        if (this.riferimenti!=null && riferimenti!=null){
            List<String> list = new ArrayList<>(Arrays.asList(this.riferimenti));
            list.removeAll(Arrays.asList(riferimenti));
            this.riferimenti = list.toArray(new String[0]);
        }
    }

    public void setRiferimenti(String[] riferimenti) {
        this.riferimenti = riferimenti;
    }

    /*private boolean isMittInternal(){
        String verso = (String) otherFields().get("TIPO_PROTOCOLLAZIONE");

        if (!Strings.isNullOrEmpty(verso)){
            return !"E".equals(verso);
        }

        return false;
    }

    private boolean isDestInternal(){
        String verso = (String) otherFields().get("TIPO_PROTOCOLLAZIONE");

        if (!Strings.isNullOrEmpty(verso)){
            return "E".equals(verso);
        }

        return false;
    }*/

    @ApiModelProperty(hidden = true)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public ICorrispondente getMittente() {

        String MITTENTI = (String) otherFields().get("MITTENTI");
        if (Strings.isNullOrEmpty(MITTENTI))
            return null;

        Map xMap = (Map) U.fromXml(MITTENTI);

        xMap = (Map) U.get(xMap,"Mittenti.Mittente");

        if (xMap==null)
            return null;

        return Corrispondente.fromXmlMap(xMap);
    }

    //@JsonIgnore
    public void setMittente(ICorrispondente mittente) {
        //this.mittente = mittente;

        if (mittente!=null){
            removeNullField("MITTENTI");
            setOtherField("MITTENTI" , "<Mittenti>\n" + mittente.toXml("Mittente") + "\n</Mittenti>" );
        } else {
            addNullField("MITTENTI");
            setOtherField("MITTENTI" , null );
        }
    }

    @ApiModelProperty(hidden = true)
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public ICorrispondente[] getDestinatari() {

        String DESTINATARI = (String) otherFields().get("DESTINATARI");
        if (Strings.isNullOrEmpty(DESTINATARI))
            return null;

        Map xMap = (Map) U.fromXml(DESTINATARI);

        Object dests = U.get(xMap,"Destinatari.Destinatario");

        ICorrispondente[] arr = null;

        if (dests instanceof Map){
            arr = new ICorrispondente[] { Corrispondente.fromXmlMap((Map) dests)} ;
        }

        if (dests instanceof List){

            arr = new ICorrispondente[((List)dests).size()];

            for( int i=0; i<((List)dests).size() ; i++ ){
                Map dMap = (Map) ((List)dests).get(i);
                arr[i] = Corrispondente.fromXmlMap(dMap);
            }
        }

        return arr;

    }

    //@JsonIgnore
    public void setDestinatari(ICorrispondente... destinatari) {

        //this.destinatari = destinatari;

        if (destinatari!=null && destinatari.length>0){

            String xml = "<Destinatari>";

            for ( ICorrispondente dest : destinatari )
                xml += "\n" + dest.toXml("Destinatario");

            xml += "\n</Destinatari>";

            removeNullField("DESTINATARI");
            setOtherField("DESTINATARI" , xml );
        } else {
            addNullField("DESTINATARI");
            setOtherField("DESTINATARI" , null );
        }
    }

    public String getClassifica() {
        return classifica;
    }

    public void setClassifica(String classifica) {
        this.classifica = classifica;
        //fascicoloPrimario = ClientUtils.joinFascicoloId(classifica,annoFascicolo,progrFascicolo,pianoClass);
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("FASCICOLO_ID")
    public String getFascicoloPrimario() {
        return ClientUtils.joinFascicoloId(classifica,annoFascicolo,progrFascicolo,null);
        /*if (this.fascicoloPrimario==null) {
            return null;
        } else {
            String[] splitted = ClientUtils.splitFascicoloId(fascicoloPrimario);
            return ClientUtils.joinFascicoloId(splitted[0],splitted[1],splitted[2],null);
        }*/
    }

    public void setFascicoloPrimario(String docerId) {
        String[] splitted = ClientUtils.splitFascicoloId(docerId);
        if (splitted!=null){
            classifica = splitted[0];
            annoFascicolo = splitted[1];
            progrFascicolo = splitted[2];
            //pianoClass = splitted[3];
        }

        /*this.fascicoloPrimario = fascicoloPrimario;

        if (this.fascicoloPrimario==null) {
            //classifica = null;
            annoFascicolo = null;
            progrFascicolo = null;
            pianoClass = null;
        } else {
            String[] splitted = ClientUtils.splitFascicoloId(fascicoloPrimario);
            if (splitted!=null){
                classifica = splitted[0];
                annoFascicolo = splitted[1];
                progrFascicolo = splitted[2];
                pianoClass = splitted[3];
            }
        }*/
    }

    @ApiModelProperty(hidden = true)
    public String[] getClassificheSecondarie() {
        Object c = properties.get(Documento.CLASSIFICHE);
        if (c==null)
            return null;

        String[] secondarie;

        if (c instanceof String)
            secondarie = new String[] { (String) c};
        else if (c instanceof List)
            secondarie = (String[]) ((List)c).toArray(new String[0]);
        else if (c instanceof String[])
            secondarie = (String[]) c;
        else
            secondarie = null;

        return secondarie;
    }

    @ApiModelProperty(hidden = true)
    public String[] getFascicoliSecondari() {
        String fs = (String) properties.get("FASC_SECONDARI");
        if (!Strings.isNullOrEmpty(fs))
            return fs.substring(0,fs.length()-1).split(";");
        else
            return null;
    }

    public void setFascicoliSecondari(String... fascicoliSecondari) {
        if (fascicoliSecondari!=null && fascicoliSecondari.length>0)
            properties.put( "FASC_SECONDARI", StringUtils.join(fascicoliSecondari,";")+";" );
        else if (fascicoliSecondari==null)
            properties.remove("FASC_SECONDARI");
        else
            properties.put( "FASC_SECONDARI","");
    }

    /*public void setFascicoliSecondari(String... fascicoliSecondari) {
        if (fascicoliSecondari!=null && fascicoliSecondari.length>0) {
            for(int i=0; i<fascicoliSecondari.length; i++){
                String[] spitted = ClientUtils.splitFascicoloId(fascicoliSecondari[i]);
                fascicoliSecondari[i] = ClientUtils.joinFascicoloId(spitted[0],spitted[1],spitted[2],null);
            }
            properties.put("FASC_SECONDARI", StringUtils.join(fascicoliSecondari, ";") + ";");
        } else {
            setFascicoliSecondariWithoutPiano(fascicoliSecondari);
        };
    }*/

    public String getCartella() {
        return cartella;
    }

    public void setCartella(String cartella) {
        this.cartella = cartella;
    }

    public URL getURL() {
        if (url !=null)
            return url;

        if (docnum==null)
            return null;

        try {
            String baseUrl = DocerBean.baseUrl.get();

            if (baseUrl!=null){
                if (!baseUrl.endsWith("/"))
                    baseUrl+="/";
                return new URL(baseUrl+"documenti/"+getDocnum()+"/file");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setURL(URL file) {
        this.url = file;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public String getLockedTo() {
        return lockedTo;
    }

    @ApiModelProperty(hidden = true)
    public String getLockedBy() {
        return lockedTo!=null ? getModifier() : null ;
    }

    /*public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public void setLockedTo(String lockedTo) {
        this.lockedTo = lockedTo;
    }*/

    @ApiModelProperty(example = "http://localhost:8080/documenti/12345/file")
    @JsonDeserialize(converter = StringURL.class)
    private URL url;

    @ApiModelProperty(example = "dGVzdG8gcHJvdmE=", hidden = true)
    @JsonDeserialize(as = Base64InputStream.class)
    @JsonSerialize(using = StreamToStringSerializer.class)
    InputStream stream;

    @ApiModelProperty(example = "12345")
    @JsonProperty("DOCNUM")
    private String docnum;

    @ApiModelProperty(example = "documento.txt")
    @JsonProperty("DOCNAME")
    private String docname;

    @ApiModelProperty(example = "DOCUMENTO")
    @JsonProperty("TYPE_ID")
    private String tipologia;

    @ApiModelProperty(allowableValues = "PRINCIPALE,ALLEGATO,ANNESSO,ANNOTAZIONE" )
    @JsonProperty("TIPO_COMPONENTE")
    private String tipoComponente;

    @ApiModelProperty(hidden = true)
    @JsonProperty("ABSTRACT")
    private String descrizione;

    public Integer getContentSize() {
        return contentSize;
    }

    public void setContentSize(Integer contentSize) {
        this.contentSize = contentSize;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("content_size")
    private Integer contentSize;

    public String getContentModifiedOn() {
        return contentModifiedOn;
    }

    public void setContentModifiedOn(String contentModifiedOn) {
        this.contentModifiedOn = contentModifiedOn;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("content_modified_on")
    private String contentModifiedOn;

    //@ApiModelProperty(hidden = true)
    //@JsonProperty("FASCICOLO_ID")
    //private String fascicoloPrimario;

    @ApiModelProperty(hidden = true)
    @JsonProperty("CLASSIFICA")
    private String classifica;

    @ApiModelProperty(hidden = true)
    @JsonProperty("ANNO_FASCICOLO")
    private String annoFascicolo;

    public ArchiveType getArchiveType() {
        return archiveType;
    }

    public void setArchiveType(ArchiveType archiveType) {
        this.archiveType = archiveType;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("ARCHIVE_TYPE")
    private ArchiveType archiveType;

    public StatoArchivistico getStatoArchivistico() {
        return statoArchivistico;
    }

    public void setStatoArchivistico(StatoArchivistico statoArchivistico) {
        this.statoArchivistico = statoArchivistico;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("STATO_ARCHIVISTICO")
    @JsonSerialize(using = StreamToStringSerializer.class)
    private StatoArchivistico statoArchivistico;

    public String getPianoClass() {
        return pianoClass;
    }

    public void setPianoClass(String pianoClass) {
        this.pianoClass = pianoClass;
        //fascicoloPrimario = ClientUtils.joinFascicoloId(classifica,annoFascicolo,progrFascicolo,pianoClass);
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("PIANO_CLASS")
    String pianoClass;

    public String getAnnoFascicolo() {
        return annoFascicolo;
    }

    public void setAnnoFascicolo(String annoFascicolo) {
        this.annoFascicolo = annoFascicolo;
        //fascicoloPrimario = ClientUtils.joinFascicoloId(classifica,annoFascicolo,progrFascicolo,pianoClass);
    }

    public String getProgrFascicolo() {
        return progrFascicolo;
    }

    public void setProgrFascicolo(String progrFascicolo) {
        this.progrFascicolo = progrFascicolo;
        //fascicoloPrimario = ClientUtils.joinFascicoloId(classifica,annoFascicolo,progrFascicolo,pianoClass);
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("PROGR_FASCICOLO")
    private String progrFascicolo;

    /*@ApiModelProperty(hidden = true)
    @JsonProperty("FASC_SECONDARI")
    private String[] fascicoliSecondari;*/

    @ApiModelProperty(hidden = true)
    @JsonProperty("PARENT_FOLDER_ID")
    private String cartella;

    @ApiModelProperty(hidden = true)
    private String[] riferimenti;

    /*@ApiModelProperty(hidden = true)
    private ICorrispondente mittente;

    @ApiModelProperty(hidden = true)
    private ICorrispondente[] destinatari;*/

    @ApiModelProperty(hidden = true)
    private String lockedTo;



    /*private String xmlVersion(final Version[] versions){
        Map<String,Object> map = new LinkedHashMap(){{
            put("versions", new LinkedHashMap(){{
                put("version", new ArrayList(){{
                    for( int i=0; i<versions.length; i++ ){
                        Version v = versions[i];
                        final String idx = "" + (i+1);
                        if (v!=null){
                            add(
                                new LinkedHashMap(){{
                                    put("number" , idx );
                                    put("date" , v.date);
                                    put("userId" , v.author);
                                    put("filename", v.docName);
                                }});
                        } else {
                            add(
                                new LinkedHashMap(){{
                                    put("number" , idx );
                                    put("date" , ClientUtils.dateFormat.format(new Date()) );
                                    put("userId" , getCreator() );
                                    put("filename", getDocname() );
                                }});
                        }
                    }
                }});
            }});
        }};
        return ClientUtils.cleanUXml(U.toXml(map));
    }*/

    @ApiModelProperty(hidden = true)
    @JsonProperty("VERSION_COUNT")
    Integer versionCount;

    public Integer getVersionCount(){
        return versionCount;
    }

    public void setVersionCount(Integer versionCount){
        this.versionCount = versionCount;
    }

    /*public void setVersions(Version[] versions){
        setOtherField("VERSIONS", xmlVersion(versions));
    }*/

    /*public void updateLastVersion(boolean replace, String username){
        Version[] versions = getVersions();

        if (!replace)
            versions = Arrays.copyOf(versions,versions.length+1 );

        versions[versions.length-1] = new Documento.Version(getDocname(),ClientUtils.dateFormat.format(new Date()),username);

        setVersions(versions);
    }*/

    @JsonIgnore
    public Version[] getVersions() {

        String VERSIONS = (String) otherFields().get("VERSIONS");

        if (VERSIONS==null)
            return new Version[0];

        Map xMap = (Map) U.fromXml(VERSIONS);

        Object versionsXml = U.get(xMap,"versions.version");

        List<Map> nodes = new ArrayList<>();
        if (versionsXml instanceof Map) {
            nodes.add( (Map) versionsXml);
        } else if (versionsXml instanceof List)
            nodes = (List) versionsXml;

        Integer internalIdx = getVersionCount();

        Integer maxIdx = 0;
        Map<String, Map> indexedVersion = new LinkedHashMap<>();

        for( Map node : nodes ){
            String number = (String) node.get("number");
            int idx = Integer.parseInt(number);
            maxIdx = Math.max(idx,maxIdx);
            indexedVersion.put( number , node );
        }

        if (internalIdx==null)
            internalIdx = maxIdx;

        Version[] versions = new Version[internalIdx];

        for (int i=0; i<internalIdx; i++){

            final String number = ""+(i+1);

            Map node = indexedVersion.get(number);

            String filename;
            String userId;
            String date;

            if (node==null){
                filename = this.getDocname();
                userId = this.getCreator();
                date = ClientUtils.dateFormat.format(new Date());

                node = new LinkedHashMap();

                node.put("number" , number );
                node.put("date" , date);
                node.put("userId" , userId);
                node.put("filename", filename);
            } else {
                filename = (String) node.get("filename");
                userId = (String) node.get("userId");
                date = (String) node.get("date");
            }

            versions[i] = new Documento.Version(filename,date,userId);

            URL file = getURL();
            if (file!=null){
                try {
                    versions[i].file = new URL(file + "?version="+(i+1));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        return versions;
    }

    @Override
    @JsonAnySetter
    public void setOtherField(String name, Object value) {

        if ("content_versions".equals(name)) {
            if (! (value instanceof List) ){
                List l = new ArrayList();
                l.add(value);
                value = l;
            }

            versionCount = ((List) value).size();
        }

        if ("lock_to".equals(name)) {

            Long to = 0L;

            if (value instanceof Number)
                to = ((Number) value).longValue();
            else if (value instanceof String) {
                try {
                    to = ClientUtils.dateFormat.parse((String) value).getTime();
                } catch (ParseException e) {

                }
            }

            if (to<System.currentTimeMillis()){
                lockedTo = null;
            } else {
                lockedTo = ClientUtils.dateFormat.format(new Date(to));
            }
        }

        super.setOtherField(name,value);
    }

    public static class Version {

        public Version(){

        }

        public Version(String docName, String date, String author){
            this.docName = docName;
            this.date = date;
            this.author = author;
        }

        public URL getFile() {
            return file;
        }

        URL file;

        String docName;

        public String getDocName() {
            return docName;
        }

        public String getDate() {
            return date;
        }

        String date;

        public String getAuthor() {
            return author;
        }

        String author;
    }

    @Override
    protected Integer getRightsMask() {
        return documento_mask;
    }

}
