var isCustom = false;
var type;
var sequence;
var division;
var sid;
var location;
var COD_ENTE;
var COD_AOO;
var PARENT_TYPE;
var PARENT_SID;

function encid(str) {
    if (str==null)
        return str;

    var idChars = "\\/:*?\"<>!.@"; //+-&|(){}[]^~

    return encode(str,idChars);
}
function encname(str) {
    if (str==null)
        return str;

    var fsChars = "\\/:*?\"<>|";

    return encode(str,fsChars);
}
function encode(token, specialChars) {
    specialChars += "%";

    if (token==null)
        return null;

    token = "" + token;

    var s = "";

    for( var i=0; i< token.length; i++ )
        if (specialChars.indexOf(token[i])!=-1)
        {
            var ch = token.charCodeAt(i).toString(16);

            if (ch.length==2)
                s+="%"+ch;
            else
                s+="%0"+ch;
        }
        else
            s+=token[i]

    return s;

}
function encXml(str){

    if (!str)
        return "";

    return str.replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&apos;');
}
function tohex(num) {
    hex = parseInt(num).toString(16);
    while( hex.length<8 )
        hex = "0"+hex;

    return hex;
}

function hash(file){
    var hash = null;
    try{
        var digest = java.security.MessageDigest.getInstance("SHA-256");

        var stream = new java.io.FileInputStream(file);

        var dis = new java.security.DigestInputStream(stream, digest);
        hash = ""+org.apache.commons.codec.binary.Hex.encodeHexString(digest.digest());

        stream.close();

    } catch(e) {
        logger.error("non possibile calcolare hash:{}",e.message);
    }
    return hash;
}

function encdocname(str,doc) {
    if (str==null)
        return str;

    //var sequence = doc.getFieldValue("sequence");

    var idx = str.lastIndexOf(".");

    if (idx==-1)
        return encname(str); //+"~"+tohex(sequence);
    else
        return encname(str.substring(0,idx))+str.substring(idx); //+"~"+tohex(sequence)
}
function encuniquename(str,doc) {
    if (str==null)
        return str;

    if (sequence == str)
        return str;

    return encname(str)+"~"+tohex(sequence);
}

function to_map(value){
    var map = {};

    var fields_arr = value.split(";");

    if (fields_arr.length<=1)
        return null;

    for(var i=0; i<fields_arr.length; i++) {
        var val = fields_arr[i];
        if (!val)
            continue;
        var field = val.split(":")[0];
        var after = val.split(":")[1];

        map[field] = after;
    }
    return map;
}

function getXmlCorr(value,forza_tipo){

    if (!value)
        return null;

    /* o posizionale separati da $ oppure mappa separata da ; */

    var corr = null;
    var map = to_map(value);

    var type = forza_tipo || map.type;

    if (type == "U" || type == "OU" || type == "UO") {

        corr = 	   '<Amministrazione> \
						<Denominazione /> \
						<CodiceAmministrazione>{ente}</CodiceAmministrazione> \
						<UnitaOrganizzativa> \
							<Denominazione>{descrizione}</Denominazione> \
							<Identificativo>{codice}</Identificativo> \
							<IndirizzoTelematico >{email}</IndirizzoTelematico>\
						</UnitaOrganizzativa>\
						<Metadati>\
							<Parametro nome="mezzo" valore="{mezzo}"></Parametro>\
						</Metadati>\
					</Amministrazione>\
					<AOO>\
						<Denominazione />\
						<CodiceAOO>{aoo}</CodiceAOO>\
					</AOO>';

    } else if(type == "O" || type == "E" || type == "PA" || type == "A" || type == "AOO" || type == "ENTE"){

        corr = 	   '<Amministrazione> \
						<Denominazione>{descrizione}</Denominazione> \
						<CodiceAmministrazione>{ente}</CodiceAmministrazione> \
						<Metadati>\
							<Parametro nome="mezzo" valore="{mezzo}"></Parametro>\
						</Metadati>\
					</Amministrazione>\
					<AOO>\
						<Denominazione>{descrizione}</Denominazione>\
						<CodiceAOO>{aoo}</CodiceAOO>\
					</AOO>';

    } else if(type == "G" || type == "PG"){

        corr = 		'<PersonaGiuridica id="{codice}" tipo="partita_iva">\
						<Denominazione>{descrizione}</Denominazione>\
						<IndirizzoPostale>\
							<Denominazione>{indirizzo}</Denominazione>\
						</IndirizzoPostale>\
						<IndirizzoTelematico>{email}</IndirizzoTelematico>\
						<Metadati>\
							<Parametro nome="mezzo" valore="{mezzo}"></Parametro>\
						</Metadati>\
					</PersonaGiuridica>';

    } else if(type == "F" || type == "P" || type == "PF"){
        corr = 		'<Persona id="{codice}" >\
						<Identificativo>{codice}</Identificativo>\
						<Denominazione>{descrizione}</Denominazione>\
						<IndirizzoPostale>\
							<Denominazione>{indirizzo}</Denominazione>\
						</IndirizzoPostale>\
						<IndirizzoTelematico>{email}</IndirizzoTelematico>\
						<Metadati>\
							<Parametro nome="mezzo" valore="{mezzo}"></Parametro>\
						</Metadati>\
					</Persona>';

    }

    //PEC,PEO,FAX,Raccomandata,Posta Ordinaria,Brevi manu

    var mezzo = (map.mezzo||"").toLowerCase();

    if (mezzo.indexOf("atti giudiziari")!=-1 || mezzo.indexOf("raccomandata")!=-1 || mezzo.indexOf("racc.")!=-1  || mezzo.indexOf("corriere")!=-1)
        map.mezzo = "Raccomandata";
    else if (mezzo.indexOf("fax")!=-1)
        map.mezzo = "Fax";
    else if (mezzo.indexOf("manu")!=-1 || mezzo.indexOf("a mano")!=-1)
        map.mezzo = "Brevi manu";
    else if (mezzo.indexOf("pec")!=-1)
        map.mezzo = "PEC";
    else if (mezzo.indexOf("peo")!=-1 || mezzo.indexOf("mail")!=-1)
        map.mezzo = "PEO";
    else if (mezzo.indexOf("posta")!=-1 || mezzo.indexOf("telegramma")!=-1)
        map.mezzo = "Posta Ordinaria";
    else
        map.mezzo = "altro";


    corr = corr.replace(/{codice}/g,encXml(map.codice)).
    replace(/{descrizione}/g,encXml(map.descrizione)).
    replace(/{ente}/g,map.ente || "").
    replace(/{aoo}/g,map.aoo || "").
    replace(/{indirizzo}/g,encXml(map.indirizzo)).
    replace(/{email}/g,encXml(map.email)).
    replace(/{mezzo}/g,encXml(map.mezzo));

    return corr;

}

function _division(doc){

    division = location + ".";

    if (COD_ENTE)
        division += encid(COD_ENTE) + "!";

    if (COD_AOO)
        division += encid(COD_AOO) + "!";

    if (type=="user" || type=="group")
        division = "";

    doc.setField("division" , division );

    return division;
}

function _sid(doc){

    if (type=="group")
        doc.setField("GROUP_ID",sid);
    else if (type=="user")
        doc.setField("USER_ID",sid);
    else if (type=="titolario")
        doc.setField("CLASSIFICA",sid);
    else if (type=="fascicolo"){
        var parts = sid.split("|");
        doc.setField("CLASSIFICA",parts[0]);
        doc.setField("ANNO_FASCICOLO",parts[1]);
        doc.setField("PROGR_FASCICOLO",parts[2]);
    } else if (isCustom) {
        doc.setField("COD_"+type,sid);
    }

    return sid;
}

function _id(doc){
    doc.setField("id" , division + encid(sid) + "@" + type );
    return _id;
}

function _description(doc){

    var description = doc.getFieldValue("DESCRIPTION") || sid;
    doc.removeField("DESCRIPTION");

    var name = encname(description);

    if (type=="documento"){
        doc.setField("DOCNAME",description);
        name = encdocname(description,doc);
    } else if (type=="group"){
        doc.setField("GROUP_NAME",description);
        name = encuniquename(description,doc);
    } else if (type=="user"){
        doc.setField("FULL_NAME",description);
        name = encuniquename(description,doc);
    } else if (type=="ente"){
        doc.setField("DES_ENTE",description);
    } else if (type=="aoo"){
        doc.setField("DES_AOO",description);
    } else if (type=="titolario"){
        doc.setField("DES_TITOLARIO",description);
        name = encuniquename(description,doc);
    } else if (type=="fascicolo"){
        doc.setField("DES_FASCICOLO",description);
        name = encuniquename(description,doc);
    } else if (type=="folder"){
        doc.setField("FOLDER_NAME",description);
        name = encuniquename(description,doc);
    } else if (isCustom){
        doc.setField("DES_"+type,description);
        name = encuniquename(description,doc);
    }

    doc.setField("name" , name );

    return name;
}
function _parent(doc){

    var parent = null;

    if (PARENT_SID)
        parent = division + endid(PARENT_SID) + "@" + PARENT_TYPE;
    else if (COD_AOO)
        parent = division + "@aoo";
    else if (COD_ENTE) /* ente */
        parent = division + "@ente";

    doc.setField("parent",parent);

    return parent;
}
function _acl(doc) {

    var acl_explicit = doc.getFieldValues( "ACL_EXPLICIT" ) || new java.util.ArrayList();
    doc.removeField("ACL_EXPLICIT");

    if (type=="user" || type=="group"){
        acl_explicit.add("everyone@group:readOnly");
    }

    if ( COD_AOO && (type=="aoo" || type=="user" || type=="group")){
        acl_explicit.add("ADMINS_AOO_"+endid(COD_AOO)+"@group:readOnly");
    }

    if ( COD_ENTE && (type=="ente" || type=="aoo" || type=="user" || type=="group") ){
        acl_explicit.add("ADMINS_ENTE_"+endid(COD_ENTE)+"@group:readOnly");
    }

    var acl_read = new java.util.ArrayList();

    acl_explicit = acl_explicit.toArray();

    for( var i=0; i< acl_explicit.length ; i++ )
    {
        var acl = ""+acl_explicit[i];

        var right = acl.split(":")[1];

        if (right=="FullAccess" || right=="All" || right=="FULL")
            right = "fullAccess";
        else if (right=="NormalAccess" || right=="EDIT" || right=="NORMAL")
            right = "normalAccess";
        else if (right=="ReadOnlyAccess" || right=="Read" || right=="READ")
            right = "readOnly";

        var actor = acl.split(":")[0];
        var actorType = "user";

        if (actor.indexOf("U") == 0) {
            if (actor.indexOf("UTENTE_") == 0) {
                actor = actor.substring(7);
            } else {
                actor = actor.substring(1);
            }
        }

        if (actor.indexOf("G") == 0)
        {
            actorType = "group";
            if (actor.indexOf("GRUPPO_") == 0) {
                actor = actor.substring(7);
            } else {
                actor = actor.substring(1);
            }
        }

        if (actor.indexOf("ACL_") == 0 ){
            actor = actor.substring(4);
            actorType = "group";
        }

        actor = encid(actor) + "@" + actorType;

        acl_explicit[i] = actor + ":" + right;

        acl_read.add(actor);

    }
    doc.setField( "acl_explicit" , acl_explicit );
    doc.setField( "acl_read" , acl_read.toArray() );

    return acl_explicit;
}

function _password(doc){

    var secret = req.getParams().get("secret","SECRET");

    var password = org.apache.commons.codec.digest.DigestUtils.md5Hex( sid+secret );

    doc.setField("USER_PASSWORD" , password );

    return password;
}

function _groups(doc) {

    var groups = doc.getFieldValues( "GROUPS" );
    doc.removeField("GROUPS");

    var newGroups = new java.util.ArrayList();

    newGroups.add("everyone@group");

    if (groups != null)
    {
        doc.removeField("GROUPS");
        groups = groups.toArray();

        for( var i=0; i<groups.length ; i++ )
        {
            if (groups[i].toUpperCase() == "SYS_ADMINS")
                newGroups.add( "admins@group" );

            newGroups.add( encid(groups[i]) + "@group" );
        }
    }

    doc.setField( "groups" , newGroups );

    return newGroups;
}

function _related(doc) {

    var rel = ""+doc.getFieldValue("RELATED");
    doc.removeField("RELATED");

    if (rel && type=="related"){
        var aoo = doc.getFieldValue("COD_AOO");

        rel = rel.split(",");

        var list = new java.util.ArrayList();

        for( var i=0; i< rel.length ; i++ )
            list.add( division + rel[i] + "@documento" );

        doc.setField("related" ,list );
        doc.setField("acl_explicit" , encid(aoo) + "@group:normalAccess");
        doc.setField("acl_read" , encid(aoo) );
        return list;
    }
    return null;
}

function _corrispondenti(doc){

    var prot = doc.getFieldValue("TIPO_PROTOCOLLAZIONE") || "";

    var mittenti = doc.getFieldValues( "MITTENTI" );

    var tipo = null;

    if (mittenti != null){
        mittenti = mittenti.toArray();
        doc.removeField("MITTENTI");

        if (prot == 'I' || prot == 'U')
            tipo = "OU";

        var xml = "<Mittenti>"
        for( var i=0; i< mittenti.length ; i++ ){
            xml += "<Mittente>" + getXmlCorr(mittenti[i],tipo) + "</Mittente>";
        }
        xml += "</Mittenti>";

        doc.setField("MITTENTI" , xml);
    }

    var destinatari = doc.getFieldValues( "DESTINATARI" );

    if (destinatari != null){
        destinatari = destinatari.toArray();
        doc.removeField("DESTINATARI");

        if (prot == 'E')
            tipo = "OU";

        var xml = "<Destinatari>"
        for( var i=0; i< destinatari.length ; i++ ){
            xml += "<Destinatario>" + getXmlCorr(destinatari[i],tipo) + "</Destinatario>";
        }
        xml += "</Destinatari>";

        doc.setField("DESTINATARI" , xml);
    }

    var firmatari = doc.getFieldValues( "FIRMATARIO" );

    if (firmatari != null){
        firmatari = firmatari.toArray();
        doc.removeField("FIRMATARIO");

        var xml = "<Firmatario>"
        for( var i=0; i< firmatari.length ; i++ ){
            xml += getXmlCorr(firmatari[i],"PF");
        }
        xml += "</Firmatario>";

        doc.setField("FIRMATARIO" , xml);
    }

}
function _riferimenti(doc){
    var rif = doc.getFieldValues( "RIFERIMENTI" );

    if (rif && type=="documento")
    {
        doc.removeField("RIFERIMENTI");
        var rif = riferimenti.toArray();

        for( var i=0; i<rif.length ; i++ )
        {
            rif[i] = division + rif[i] + "@documento";
        }
        doc.setField( "riferimenti" , rif );
    }
}
function _file(doc){

    var content = doc.getFieldValue("PATH");
    var versions = doc.getFieldValues("VERSIONS");

    doc.removeField("PATH");
    doc.removeField("VERSIONS");

    if (!content){
        logger.error("documento non inserito perch content non  valorizzato il PATH \n{}", doc);
        return;
    }

    if (content.indexOf("http") == 0){
        doc.setField("ARCHIVE_TYPE" , "URL" );
        doc.setField("DOC_URL" , content );
        return;
    }

    var idx = content.lastIndexOf("/");

    var content_id = content.substring(0,idx);

    /* verifica dimensione */
    var store = req.getParams().get("store") || "../store";

    var fp = store + "/" + content;
    fp = fp.replace(/[\\/]+/g,"/");

    var size;
    var file = new java.io.File(fp);
    var file_hash = null;

    if (file.exists()){
        file_hash = hash(file);
        size = file.length();
    }
    else
        size = -1;
    /**************/

    var user = doc.getFieldValue("MODIFIER");
    var date = doc.getFieldValue("modified_on");

    var contents = new java.util.ArrayList();
    contents.add(content);

    if (versions)
        contents.addAll(versions);

    contents = contents.toArray();

    var content_versions = new java.util.ArrayList();

    var VERSIONS = "<versions>";

    for (var version=0; version< contents.length; version++ ){

        content = contents[version];

        var path = content.substring(idx+1);

        var label = "workingCopy";

        if (version>0)
            label = "version"+version;

        var content_version = "import://id="+encodeURIComponent(content_id)+"&version="+version+"&path="+encodeURIComponent(path)+"&label="+encodeURIComponent(label);

        content_versions.add(content_version);

        var VERSION = "<version>\
							<number>{number}</number>\
							<date>{date}</date>\
							<userId>{user}</userId>\
							<filename>{filename}</filename>\
						</version>";

        VERSION = VERSION.replace("{number}",version+1);
        VERSION = VERSION.replace("{date}",date);
        VERSION = VERSION.replace("{user}",encXml(user) );
        VERSION = VERSION.replace("{filename}",encXml(path) );

        VERSIONS += VERSION;
    }

    VERSIONS += "</versions>";

    doc.setField("VERSIONS" , VERSIONS);

    doc.setField("content_modified_by" , user );
    doc.setField("content_modified_on" , date );
    doc.setField("content_size" , size );
    doc.setField("content_hash" , file_hash );
    doc.setField("DOC_HASH" , file_hash );
    doc.setField("content_id" , content_id);
    doc.addField("state" , "content");
    doc.setField("content_versions" , content_versions );
    doc.setField("ARCHIVE_TYPE" , "ARCHIVE" );
}

function _stato_archivistico(doc){
    var TYPE_ID = doc.getFieldValue("TYPE_ID") || "DOCUMENTO";

    doc.setField( "TYPE_ID" , TYPE_ID );

    var STATO_ARCHIVISTICO = "0";

    var REGISTRO_ID = doc.getFieldValue("REGISTRO_ID") || "";
    var NUM_PG = "" + (doc.getFieldValue("NUM_PG") || "");
    var CLASSIFICA = doc.getFieldValue("CLASSIFICA") || "";
    var PROGR_FASCICOLO = doc.getFieldValue("PROGR_FASCICOLO") || "";
    var ANNO_FASCICOLO = doc.getFieldValue("ANNO_FASCICOLO") || "";
    var STATO_BUSINESS = doc.getFieldValue("STATO_BUSINESS") || "0";
    var STATO_CONSERV = doc.getFieldValue("STATO_CONSERV") || "0";
    var FASC_SECONDARI = doc.getFieldValues("FASC_SECONDARI");

    if (PARENT_TYPE == "titolario" && !CLASSIFICA){
        CLASSIFICA = PARENT_SID;
    }

    if (PARENT_TYPE == "fascicolo" && !PROGR_FASCICOLO){
        var parts = PARENT_SID.split("|");
        CLASSIFICA = parts[0];
        ANNO_FASCICOLO = parts[1];
        PROGR_FASCICOLO = parts[2];
    }

    if (FASC_SECONDARI){
        FASC_SECONDARI = FASC_SECONDARI.toArray();
        var ff = [];
        for( var i=0; i<FASC_SECONDARI; i++){
            ff.push(FASC_SECONDARI);
        }
        FASC_SECONDARI = ff.join(";");
        FASC_SECONDARI = FASC_SECONDARI.split(";");

        var fascicoli = new java_util.ArrayList();
        var acl_sequence = doc.getFieldValue("acl_sequence");

        if (PROGR_FASCICOLO){
            fascicoli.add(division + encid(CLASSIFICA + "|" + ANNO_FASCICOLO + "|" + PROGR_FASCICOLO) + "@fascicolo" );
        }

        for( var i=0; i<FASC_SECONDARI; i++){
            fascicoli.add(division + encid(FASC_SECONDARI[i]) + "@fascicolo" );
        }

        doc.setField( "fascicoli_mv" , fascicoli );
    }

    if (REGISTRO_ID)
        STATO_ARCHIVISTICO = "2";

    if (NUM_PG){

        NUM_PG = "0000000".substring(0,7-NUM_PG.length) + NUM_PG;
        var DATA_PG =  doc.getFieldValue("DATA_PG") || "";

        doc.setField( "NUM_PG" , NUM_PG );

        if (DATA_PG)
            doc.setField( "ANNO_PG" , DATA_PG.substring(0,4) );

        if (PROGR_FASCICOLO)
            STATO_ARCHIVISTICO = "5";
        else if (CLASSIFICA)
            STATO_ARCHIVISTICO = "4";
        else
            STATO_ARCHIVISTICO = "3";
    }

    doc.setField( "STATO_ARCHIVISTICO" , STATO_ARCHIVISTICO );
    doc.setField( "STATO_CONSERV" , STATO_CONSERV );
    doc.setField( "STATO_BUSINESS" , STATO_BUSINESS );
    doc.setField( "CLASSIFICA" , CLASSIFICA );
    doc.setField( "PROGR_FASCICOLO" , PROGR_FASCICOLO );
    doc.setField( "ANNO_FASCICOLO" , ANNO_FASCICOLO );
    doc.setField( "FASC_SECONDARI" , FASC_SECONDARI );

    return STATO_ARCHIVISTICO;
}

function flow(doc){

    _division(doc);
    _sid(doc);
    _id(doc);
    _description(doc);
    _parent(doc);
    _acl(doc);

    if (type=="related"){
        _related(doc);
    }

    if (type=="documento"){
        _corrispondenti(doc);
        _riferimenti(doc);
        _file(doc);
        _stato_archivistico(doc);
    }

    if (type=="user"){
        _groups(doc);
        _password(doc);
    }

    return true;
}

function fixDates(doc){
    var keys = doc.keySet().toArray();
    var schema = req.getSchema();

    for (var i=0; i<keys.length; i++)
    {
        key = "" + keys[i];

        var val = doc.getFieldValue(key);

        if (val==null)
            continue;

        if (val.getDate)
        {
            //val = Packages.it.kdm.solr.components.common.Utils.formatDate(val);
            val = Packages.it.kdm.solr.common.FieldUtils.formatDate(val);
            doc.setField( key, val);
        }
        else
        {
            var ft;

            var schemaField = schema.getFieldOrNull(key);
            if (schemaField!=null)
                ft = schemaField.getType();
            else
                ft = schema.getDynamicFieldType(key);

            if (ft!=null && ft.parseMath!=null)
            {
                val = parseDate(""+val);
                doc.setField( key, val);
            } else if (val.trim){
                var trimmed = val.trim();

                doc.setField( key, trimmed);
            }
        }
    }
}

function fixFields(doc){
    var keys = doc.keySet().toArray();

    for (var i=0; i<keys.length; i++) {
        key = "" + keys[i];
        doc.put(key.toUpperCase(), doc.remove(key));
    }
}

function processAdd(cmd) {

    doc = cmd.solrDoc;

    fixFields(doc);

    var now = new java.util.Date();

    logger.debug("********** import document:" + doc.toString() );

    sequence = doc.getFieldValue("ID");
    sid = doc.getFieldValue("SID") || (""+sequence);
    type = doc.getFieldValue("TYPE");
    location = doc.getFieldValue("LOCATION") || "DOCAREA" ;
    COD_ENTE = req.getParams().get("COD_ENTE") || doc.getFieldValue("COD_ENTE");
    COD_AOO = req.getParams().get("COD_AOO") || doc.getFieldValue("COD_AOO");
    PARENT_SID = doc.getFieldValue("PARENT_SID") || "";
    PARENT_TYPE = doc.getFieldValue("PARENT_TYPE") || "";

    var acl_sequence = doc.getFieldValue("ACL_PARENT_ID");
    var enabled = doc.getFieldValue("ENABLED");
    var CREATOR = doc.getFieldValue("CREATOR") || "admin";
    var created_by = CREATOR + "@user";
    var MODIFIER = doc.getFieldValue("MODIFIER") || "admin"; //MODIFIER
    var modified_by = MODIFIER + "@user";
    var created_on = doc.getFieldValue("CREATION_DATE") || now;
    var modified_on = doc.getFieldValue("UPDATE_DATE") || now;
    var AUTHOR_ID = doc.getFieldValue("AUTHOR_ID") || "";
    var TYPIST_ID = doc.getFieldValue("TYPIST_ID") || "";

    if (type=="location")
        COD_ENTE = null;

    if (type=="ente" || type=="location")
        COD_AOO = null;

    if (enabled=="false" || enabled == false || enabled == 0)
        enabled = false;
    else
        enabled = true;

    var ENABLED = "true";

    if (!enabled && type=="documento")
        ENABLED = "false";

    if (!created_on.getDate)
        created_on = created_on.substring(0,23)+"Z";

    if (!modified_on.getDate)
        modified_on = modified_on.substring(0,23)+"Z";

    var views = new java.util.ArrayList();
    views.add(type);
    views.add("abstract");

    if (type!="documento" &&
        type!="fascicolo" &&
        type!="titolario" &&
        type!="location" &&
        type!="folder" &&
        type!="ente" &&
        type!="aoo" &&
        type!="user" &&
        type!="group" &&
        type!="related" &&
        type!="version")

        isCustom = true;

    if (isCustom && type!="custom" )
        views.add("custom");

    doc.removeField("ID");
    doc.removeField("SID");
    doc.removeField("TYPE");
    doc.removeField("LOCATION");
    doc.removeField("ACL_PARENT_ID");
    doc.removeField("UPDATE_DATE");
    doc.removeField("PARENT_SID");
    doc.removeField("PARENT_TYPE");
    doc.removeField("TIMESTAMP");

    doc.setField("sequence" , sequence );
    doc.setField("sid" , sid );
    doc.setField("type" , type );
    doc.setField("location" , encname(location) );

    doc.setField("COD_ENTE" , COD_ENTE );
    doc.setField("COD_AOO" , COD_AOO );

    doc.setField("created_by" , created_by );
    doc.setField("CREATOR" , CREATOR );

    doc.setField("modified_by" ,  modified_by );
    doc.setField("MODIFIER" , MODIFIER );

    if (!AUTHOR_ID && type=="documento")
        doc.setField("AUTHOR_ID" , CREATOR );

    if (!TYPIST_ID && type=="documento")
        doc.setField("TYPIST_ID" , MODIFIER );

    doc.setField("modified_on" , modified_on );
    doc.setField("MODIFIED" , modified_on );

    doc.setField("created_on" , created_on );
    doc.setField("CREATION_DATE" , created_on );
    doc.setField("CREATED" , created_on );

    doc.setField("enabled",enabled);
    doc.setField("ENABLED" , ENABLED);
    doc.setField("APP_VERSANTE" , "dbimport");

    doc.setField("acl_inherits" , acl_sequence!=null );
    doc.setField("INHERITS_ACL" , ""+(acl_sequence!=null) );
    doc.setField("acl_sequence" , acl_sequence );
    doc.setField("acl_sequences" , acl_sequence );

    doc.setField("views" , views);

    fixDates(doc);

    logger.debug("********** after mapping:"+doc.toString() );

    var result = flow(doc);

    doc.removeField("RELATED");

    if (result)
        logger.debug("********** after process:"+doc.toString() );
    else
        logger.debug("********** skipped:"+doc.toString() );

    return result;
}
function processDelete(cmd) {
    // no-op
}
function processMergeIndexes(cmd) {
    // no-op
}
function processCommit(cmd) {
    // no-op
}
function processRollback(cmd) {
    // no-op
}
function finish() {
    // no-op
}

/*
function custom(doc,type) {
    var ente = doc.getFieldValue("COD_ENTE");
    var aoo = doc.getFieldValue("COD_AOO");

    logger.debug( "DOC CUSTOM \n{}",doc);
    logger.debug( "CUSTOM TYPE \n{}", type);

    var suffix = type.toUpperCase();

    var sid = doc.getFieldValue("COD_"+suffix);
    var division = this.workspace + "." + encid(ente) + "!" + encid(aoo);
    var name = doc.getFieldValue("DES_"+suffix) || sid;

    var parent = doc.getFieldValue("PARENT_COD_"+suffix) || "";

    doc.setField("id" , division + "!" + sid + "@folder" );
    doc.setField("name" , encuniquename(name,doc) );
    doc.setField("division" , division );
    doc.setField("sid" , sid );

    if (parent!="")
        doc.setField("parent" , this.workspace+"."+encid(ente)+"!"+encid(aoo)+"!"+encid(parent)+"@"+type);
    else
        doc.setField("parent" , this.workspace+"."+encid(ente)+"!"+encid(aoo)+"!@aoo");

    this.views.add("custom");

    return true;
}

function titolario(doc) {
    var ente = doc.getFieldValue("COD_ENTE");
    var aoo = doc.getFieldValue("COD_AOO");

    var sid = doc.getFieldValue("CLASSIFICA");
    var division = this.workspace + "." + encid(ente) + "!" + encid(aoo);
    var name = doc.getFieldValue("DES_TITOLARIO") || sid;

    var parent_classifica = doc.getFieldValue("PARENT_CLASSIFICA") || "";

    doc.setField("id" , division + "!" + encid(sid) + "@titolario" );
    doc.setField("name" , encuniquename(name,doc) );
    doc.setField("division" , division );
    doc.setField("sid" , sid );

    if (parent_classifica=="")
        doc.setField("parent" , division+"!@aoo");
    else
        doc.setField("parent" , division+"!"+encid(parent_classifica)+"@titolario");

    return true;
}

function fascicolo(doc) {
    var ente = doc.getFieldValue("COD_ENTE");
    var aoo = doc.getFieldValue("COD_AOO");

    var classifica = doc.getFieldValue("CLASSIFICA");
    var anno = doc.getFieldValue("ANNO_FASCICOLO");

    var progr = doc.getFieldValue("PROGR_FASCICOLO");

    var sid = classifica + "|" + anno + "|" + progr;
    var division = this.workspace + "." + encid(ente) + "!" + encid(aoo);
    var name = doc.getFieldValue("DES_FASCICOLO") || sid;

    var parent_progr = doc.getFieldValue("PARENT_PROGR_FASCICOLO") || "";

    doc.setField("id" , division + "!" + encid(sid) + "@fascicolo" );
    doc.setField("name" , encuniquename(name,doc) );
    doc.setField("division" , division );
    doc.setField("sid" , sid );

    if (parent_progr=="")
        doc.setField("parent" , division+"!"+encid(classifica)+"@titolario");
    else
        doc.setField("parent" , division+"!"+encid(classifica+"|"+anno+"|"+parent_progr)+"@fascicolo");

    return true;
}

function folder(doc) {
    var ente = doc.getFieldValue("COD_ENTE");
    var aoo = doc.getFieldValue("COD_AOO") || "";

    if (ente==null)
    {
        logger.debug( "folder non inserito perchè perchè ente non è valorizzato \n{}",doc);
        return false;
    }

    var sequence = doc.getFieldValue("sequence");

    var folder = doc.getFieldValue("PARENT_FOLDER_ID") || "";

    if (aoo=="")
        folder = "";



    var sid = doc.getFieldValue("FOLDER_ID");

    if (sid==null)
    {
        if (sequence==null)
        {
            logger.warn( "folder non inserito perchè perchè FOLDER_ID non è valorizzato \n{}",doc);
            return false;
        }
        sid = ""+sequence;
        doc.setField("FOLDER_ID" , sid);
    }

    var division = this.workspace + "." + encid(ente) + "!" + encid(aoo);;

    var name = doc.getFieldValue("FOLDER_NAME") || sid;

    doc.setField("id" , division + "!" + encid(sid) + "@folder" );
    doc.setField("name" , encname(name) );
    doc.setField("division" , division );
    doc.setField("sid" , sid );

    if (folder!="")
        doc.setField("parent" , this.workspace+"."+encid(ente)+"!"+encid(aoo)+"!"+folder+"@folder");
    else if (aoo!="")
        doc.setField("parent" , this.workspace+"."+encid(ente)+"!"+encid(aoo)+"!@aoo");
    else
        doc.setField("parent" , this.workspace+"."+encid(ente)+"!@ente");

    return true;
}*/

