
function encid(str)
{
    if (str==null)
        return str;

    var idChars = "\\/:*?\"<>!.@"; //+-&|(){}[]^~

    return encode(str,idChars);
}

function encname(str)
{
    if (str==null)
        return str;

    var fsChars = "\\/:*?\"<>|";

    return encode(str,fsChars);
}

function encode(token, specialChars)
{
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

function tohex(num)
{
    hex = parseInt(num).toString(16);
    while( hex.length<8 )
        hex = "0"+hex;

    return hex;
}

function encdocname(str,doc)
{
    if (str==null)
        return str;

    var sequence = doc.getFieldValue("sequence");

    var idx = str.lastIndexOf(".");

    if (idx==-1)
        return encname(str)+"~"+tohex(sequence);
    else
        return encname(str.substring(0,idx))+"~"+tohex(sequence)+str.substring(idx);
}

function encuniquename(str,doc)
{
    if (str==null)
        return str;

    var fsChars = "\\/:*?\"<>|";

    var sequence = doc.getFieldValue("sequence");

    return str+"~"+tohex(sequence);
}

var orgprefix = req.getParams().get("orgprefix") || "";

String.prototype.capitalize = function() {
    return this.charAt(0).toUpperCase() + this.slice(1);
};

function acl(doc)
{
    var acl_explcit = doc.getFieldValues( "__ACL_EXPLICIT__" );

    //logger.debug( "Acls per il documento \n{} : {}", doc.getFieldValue("DOCNUM"), acl_explcit);

    var ente = doc.getFieldValue("COD_ENTE");
    var aoo = doc.getFieldValue("COD_AOO");

    if (acl_explcit != null)
    {
        acl_explcit = acl_explcit.toArray();

        doc.removeField("__ACL_EXPLICIT__");

        for( var i=0; i< acl_explcit.length ; i++ )
        {
            var acl = ""+acl_explcit[i];

            var right = acl.split(":")[1];

            if (right=="FullAccess" || right=="All")
                right = "fullAccess";
            else if (right=="NormalAccess" || right=="EDIT")
                right = "normalAccess";
            else if (right=="ReadOnlyAccess" || right=="Read" || right=="READ")
                right = "readOnly";

            var actor = acl.split(":")[0];
            var actorType = "user";

            if (actor.indexOf("UTENTE_") == 0) {
                actor = actor.substring(7);
            }

            if (actor.indexOf("GRUPPO_") == 0)
            {
                actor = actor.substring(7);

                actorType = "group";

            }

            if ( actor.indexOf("ACL_") == 0 ){
                actor = "everyone";

                actorType = "group";
            }

            acl_explcit[i] = orgprefix+ encid(actor) + "@" + actorType + ":" + right;

        }
        doc.setField( "acl_explicit" , acl_explcit );
    }
}


function corrispondenti(doc , type)
{
    var single_types = {
        "destinatari":"destinatario",
        "mittenti":"mittente",
        "firmatari":"firmatario"
    };

    var metadati = "";
    if(type == "destinatari" || type == "mittenti")
        metadati = '<Parametro nome="mezzo" valore="mezzo"></Parametro>';

    if(type == "firmatari")
        metadati = '<Parametro nome="tipofirma" valore="mezzo"></Parametro>';

    var list = null;

    logger.debug( "TYPE corrispondenti {} \n", type);

    if(type == "destinatari")
        list = doc.getFieldValues( "__DESTINATARI__" );
    if(type == "mittenti")
        list = doc.getFieldValues( "__MITTENTI__" );
    if(type == "firmatari")
        list = doc.getFieldValues( "__FIRMATARI__" );

    if (list != null)
    {
        list = list.toArray();

        var obj = "<" + type.capitalize() + ">";

        for( var i=0; i< list.length ; i++ ) {

            var fields_arr = list[i].split("|$|");

            logger.debug( "Fields type {} ", fields_arr[1].toUpperCase());

            var fields = {
                ente: fields_arr[4].toUpperCase() !== "NULL" ? fields_arr[4] : '',
                descrizione: fields_arr[3].toUpperCase() !== "NULL" ? fields_arr[3] : '',
                codice: fields_arr[2].toUpperCase() !== "NULL" ? fields_arr[2] : '',
                email: fields_arr[7].toUpperCase() !== "NULL" ? fields_arr[7] : '',
                aoo: fields_arr[5].toUpperCase() !== "NULL" ? fields_arr[5] : '',
                indirizzo: fields_arr[6].toUpperCase() !== "NULL" ? fields_arr[6] : '',
                mezzo: fields_arr[8].toUpperCase() !== "NULL" ? fields_arr[8] : ''
            };

            if (fields_arr[1].toUpperCase() != "NULL") {

                var v = "<" + single_types[type].capitalize() + ">";

                if (fields_arr[1].toUpperCase() == "U") {

                    v += "<Amministrazione>";

                    v += "<Denominazione />";

                    v += "<CodiceAmministrazione>" + fields.ente + "</CodiceAmministrazione>";

                    v += "<UnitaOrganizzativa>" +
                        "<Denominazione>" + fields.descrizione + "</Denominazione>" +
                        "<Identificativo>" + fields.codice + "</Identificativo>" +
                        "<IndirizzoTelematico >" + fields.email + "</IndirizzoTelematico>" +
                        "</UnitaOrganizzativa>";

                    v += "<Metadati>"+ metadati +"</Metadati>";
                    v += "</Amministrazione>";

                    v += "<AOO>" +
                        "<Denominazione />" +
                        "<CodiceAOO>" + fields.aoo + "</CodiceAOO>" +
                        "</AOO>";


                    obj += v;

                } else if(fields_arr[1].toUpperCase() == "G"){
                    v += '<PersonaGiuridica id="'+ fields.codice +'" tipo="partita_iva">';

                    v += "<Denominazione>"+ fields.descrizione +"</Denominazione>";

                    v += "<IndirizzoPostale>" +
                        "<Denominazione>" + fields.indirizzo + "</Denominazione>" +
                        "</IndirizzoPostale>";

                    v += "<IndirizzoTelematico>" + fields.email +"</IndirizzoTelematico>";

                    v += "<Metadati>"+ metadati +"</Metadati>";

                    v += "</PersonaGiuridica>";


                } else if(fields_arr[1].toUpperCase() == "F"){
                    v += '<Persona id="'+ fields.codice +'" >';

                    v += "<Identificativo>"+ fields.codice +"</Identificativo>";

                    v += "<Denominazione>"+ fields.descrizione +"</Denominazione>";

                    v += "<IndirizzoPostale>" +
                        "<Denominazione>" + fields.indirizzo + "</Denominazione>" +
                        "</IndirizzoPostale>";

                    v += "<IndirizzoTelematico>"+ fields.email +"</IndirizzoTelematico>";

                    v += "<Metadati>"+ metadati +"</Metadati>";

                    v += "</Persona>";


                } else if(fields_arr[1].toUpperCase() == "O"){
                    v += "<Amministrazione>";

                    v += "<Denominazione></Denominazione>";

                    v += "<CodiceAmministrazione>" + fields.ente + "</CodiceAmministrazione>";

                    v += "<Metadati>"+ metadati +"</Metadati>";

                    v += "</Amministrazione>";

                    v += "<AOO>" +
                        "<Denominazione>"+ fields.descrizione +"</Denominazione>" +
                        "<CodiceAOO>" + fields.aoo + "</CodiceAOO>" +
                        "<IndirizzoTelematico>" + fields.email + "</IndirizzoTelematico>" +
                        "</AOO>";



                } else if(fields_arr[1].toUpperCase() == "A"){
                    v += "<Amministrazione>";

                    v += "<Denominazione>"+ fields.descrizione +"</Denominazione>";

                    v += "<CodiceAmministrazione>" + fields.codice + "</CodiceAmministrazione>";

                    v += "<IndirizzoTelematico>"+ fields.email +"</IndirizzoTelematico>";

                    v += "<Metadati>"+ metadati +"</Metadati>";

                    v += "</Amministrazione>";

                    v += "<AOO>" +
                        "<Denominazione></Denominazione>" +
                        "<CodiceAOO></CodiceAOO>" +
                        "</AOO>";



                }
                else {
                    //noop
                }

                obj += v;
                obj += "</" + single_types[type].capitalize() + ">";
            }
        }

        obj += "</" + type.capitalize() + ">";

        doc.setField(type.toUpperCase() , obj);


        if(type == "destinatari")
            doc.removeField("__DESTINATARI__");
        if(type == "mittenti")
            doc.removeField("__MITTENTI__");
        if(type == "firmatari")
            doc.removeField("__FIRMATARI__");

    }

}

function location(doc)
{
    doc.setField("id" , this.workspace + "!@location" );
    doc.setField("name" , this.workspace );
    doc.setField("division" , this.workspace );
    doc.setField("sid" , this.workspace );

    return true;
}

function ente(doc)
{
    var sid = doc.getFieldValue("COD_ENTE");
    var division = this.workspace + "." + encid(sid);
    var name = doc.getFieldValue("DES_ENTE") || sid;

    doc.setField("id" , division + "!@ente" );
    doc.setField("name" , encname(name) );
    doc.setField("division" , division );
    doc.setField("sid" , sid );
    doc.setField("parent" , this.workspace+"!@location");

    doc.setField("GROUP_NAME", name);
    doc.setField("GROUP_ID", sid);
    doc.setField("ADMIN_GROUP_ID" , "ADMINS_ENTE_"+sid);
    doc.setField("ADMIN_GROUP_NAME" , "Admins Ente " + name);

    this.views.add("group");

    return true;
}

function addeveryone(doc)
{
    var acl_explcit = doc.getFieldValues( "acl_explicit" );

    var acl2 = new java.util.ArrayList();

    if (acl_explcit != null)
    {
        var appo = acl_explcit.toArray();

        for( var i=0; i<appo.length ; i++ )
        {
            acl2.add(appo[i]);
        }
    }

    acl2.add("everyone@group:readOnly");

    acl_explcit = acl2.toArray();

    doc.setField("acl_explicit",acl_explcit);
}


function group(doc)
{
    var gs = doc.getFieldValue("GRUPPO_STRUTTURA");

    if (gs=="true")
    {
        logger.debug( "group non inserito perchè perchè struttura \n{}",doc);
        return false;
    }

    var sid = doc.getFieldValue("GROUP_ID") || this.uuid;

    logger.debug( "******** group \n{}", doc);

    var division = orgprefix + encid(sid);
    var name = doc.getFieldValue("GROUP_NAME") || sid;

    var group = doc.getFieldValue("PARENT_GROUP_ID") || "";
    var aooente = doc.getFieldValue("AOOENTE") || "";

    doc.setField("id" , division + "@group" );
    doc.setField("name" , encuniquename(name,doc) );
    doc.setField("division" , division );
    doc.setField("sid" , sid );

    if (aooente!="")
    {
        doc.removeField("AOOENTE");

        var ente = (""+aooente).split("!")[0];
        var aoo = (""+aooente).split("!")[1];

        //doc.setField("COD_ENTE" , ente );

        if (aoo!="")
        {
            //doc.setField("COD_AOO" , aoo);
            doc.setField("parent" , this.workspace+"."+encid(ente) + "!" + encid(aoo) +"!@aoo" );
        }
        else
        {
            doc.setField("parent" , this.workspace+"."+encid(ente) +"!@ente" );
        }
    }
    else if (group!="")
        doc.setField("parent" , orgprefix+encid(group)+"@group");

    addeveryone(doc);

    return true;

}

function aoo(doc)
{
    var ente = doc.getFieldValue("COD_ENTE");

    var sid = doc.getFieldValue("COD_AOO");
    var division = this.workspace + "." + encid(ente) + "!" + encid(sid);
    var name = doc.getFieldValue("DES_AOO") || sid;

    doc.setField("id" , division + "!@aoo" );
    doc.setField("name" , encname(name) );
    doc.setField("division" , division );
    doc.setField("sid" , sid );
    doc.setField("parent" , this.workspace+"."+encid(ente)+"!@ente");

    doc.setField("GROUP_NAME", name);
    doc.setField("GROUP_ID", sid);
    doc.setField("ADMIN_GROUP_ID" , "ADMINS_AOO_"+sid);
    doc.setField("ADMIN_GROUP_NAME" , "Admins Aoo " + name);
    doc.setField("PARENT_GROUP_ID" , ente );

    this.views.add("group");

    return true;
}

function documento(doc)
{
    var content = doc.getFieldValue("__CONTENT__") || "";

    //TODO:FIX temporanea ente
    doc.setField("COD_ENTE", "demo1");

    var corr_type = doc.getFieldValue("__CORR_TYPE__") || "";
    var ente = doc.getFieldValue("COD_ENTE");
    var aoo = doc.getFieldValue("COD_AOO");

    var classifica = doc.getFieldValue("CLASSIFICA") || "";
    var anno = doc.getFieldValue("ANNO_FASCICOLO") || "";

    var sid = doc.getFieldValue("DOCNUM");
    var division = this.workspace + "." + encid(ente) + "!" + encid(aoo);
    var name = doc.getFieldValue("DOCNAME") || sid;

    var progr = doc.getFieldValue("FASCICOLAZIONE") || "";
    var folder = doc.getFieldValue("PARENT_FOLDER_ID") || "";

    var id = division + "!" + encid(sid) + "@documento";
    doc.setField("id" , id );
    doc.setField("name" , encdocname(name,doc) );
    doc.setField("division" , division );
    doc.setField("sid" , sid );

    if (corr_type!="")
        corrispondenti( doc, corr_type);


    if (folder!="")
        doc.setField("parent" , division+"!"+folder+"@folder");
    else if (progr!="")
        doc.setField("parent" , division+"!"+encid(classifica)+"|"+anno+"|"+encid(progr)+"@fascicolo");
    else if (classifica!="")
        doc.setField("parent" , division+"!"+encid(classifica)+"@titolario");
    else
        doc.setField("parent" , division+"!@aoo");

    var riferimenti = doc.getFieldValues( "RIFERIMENTI" );

    if (riferimenti != null)
    {
        doc.removeField("RIFERIMENTI");
        var riferimenti = riferimenti.toArray();

        for( var i=0; i<riferimenti.length ; i++ )
        {
            riferimenti[i] = division + "!" + riferimenti[i] + "@documento";
        }
        doc.setField( "riferimenti" , riferimenti );
    }

    if (content==null)
    {
        var archiveType = ""+doc.getFieldValue("ARCHIVE_TYPE");

        if (archiveType=="URL")
            return true;

        logger.error( "documento non inserito perchè content non è valorizzato \n{}",doc);
        return false;
    }

    var content_id = "import://" + this.uuid;

    var content_versions = new java.util.ArrayList();

    var verList = {};

    var versions_idx = doc.getFieldValues("__VERS_REF__");

    var workingCopy = "import://id="+this.uuid+"&version="+this.uuid+"&path=/"+content.substring(8)+"&label=workingCopy";

    if (versions_idx!=null)
    {
        //cont_refs = cont_refs.toArray();
        var idxes = versions_idx.toArray();
        var refs = doc.getFieldValues("__CONT_REF__").toArray();
        var paths = doc.getFieldValues("__VERSIONS__").toArray();

        for ( var j=0; j<idxes.length;j++)
        {
            var idx = parseInt(idxes[j]);

            var shortIdx = (""+idx).split(".")[0];

            if (shortIdx != "0")
                verList[shortIdx] = "import://id="+this.uuid+"&version="+refs[j]+"&path=/"+paths[j].substring(8)+"&label="+idxes[j];
        }

        for ( var ref in verList )
            content_versions.add(verList[ref]);
    }

    if (content_versions.size()==0)
    {
        content_versions.add(workingCopy);
    }

    var advVersions = doc.getFieldValue("VERSIONS");

    if (advVersions==null)
    {
        advVersions = "<versions>";

        for( var i=0; i<content_versions.size(); i++)
        {
            var verNum = ""+(i+1);
            var v = "<version><number>"+ verNum.split(".")[0] + "</number>";

            var userId = "admin";
            var filename = name;

            v += "<date></date>";

            v += "<userId></userId>";

            v += "<filename>"+name+"</filename>";

            v += "</version>";

            advVersions += v;
        }

        advVersions += "</versions>";

        doc.setField("VERSIONS" , advVersions);
    }

    doc.setField("content_modified_by" , doc.getFieldValue("modified_by") );
    doc.setField("content_modified_on" , doc.getFieldValue("modified_on") );
    doc.setField("content_size" , doc.getFieldValue("__CONTENT_SIZE__") );
    doc.setField("content_id" , content_id);
    doc.addField("state" , "content");
    doc.setField("content_versions" , content_versions );

    return true;
}

function custom(doc,type)
{
    var ente = doc.getFieldValue("COD_ENTE");
    var aoo = doc.getFieldValue("COD_AOO");

    var suffix = this.anagrafiche[type] || type.toUpperCase();

    if (this.anagrafiche[type]=="")
    {
        logger.debug( "elemento non inserito perchè il type è ignorato \n{}",doc);
        return false;
    }

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

function processAdd(cmd) {

    doc = cmd.solrDoc;
    var schema = req.getSchema();

    //logger.info("********** import document:" + doc.toString() );

    this.uuid = doc.getFieldValue("id");

    var sequence = doc.getFieldValue("id");
    var type = doc.getFieldValue("type");
    var acl_inherits = doc.getFieldValue("inherits");
    var inherits_from = doc.getFieldValue("inherits_from");
    var enabled = doc.getFieldValue("enabled");

    doc.removeField("id");
    doc.removeField("type");

    this.workspace = 'DOCAREA' ;

    this.workspace = encname(this.workspace);

    acl_inherits = ( acl_inherits == 1);

    var created_by = doc.getFieldValue("CREATOR");
    if (created_by == null)
        created_by = "admin";
    else
        created_by += "@user";

    var modified_by = doc.getFieldValue("MODIFIER");
    if (modified_by == null)
        modified_by = "admin";
    else
        modified_by += "@user";


    doc.setField("created_by" , created_by );
    doc.setField("modified_by" ,  modified_by );

    var now = new java.util.Date();

    var created_on = doc.getFieldValue("CREATED") || now;
    var modified_on = doc.getFieldValue("MODIFIED") || now;

    if (!created_on.getDate)
        created_on = created_on.substring(0,23)+"Z";

    if (!modified_on.getDate)
        modified_on = modified_on.substring(0,23)+"Z";

    doc.setField("modified_on" , modified_on );
    doc.setField("created_on" , created_on );
    doc.setField("CREATION_DATE" , created_on );
    doc.setField("location" , this.workspace );
    doc.setField("acl_inherits" , acl_inherits );
    doc.setField("inheritsAcl" , ""+acl_inherits );
    doc.setField("type" , type );
    doc.setField("sequence" , sequence );

    keys = doc.keySet().toArray();


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
            }
        }
    }

    if (acl_inherits)
    {
        doc.setField("acl_sequence" , inherits_from );
        doc.setField("acl_sequences" , inherits_from );
    }

    if (enabled=="false")
    {
        doc.setField("enabled",false);
        doc.setField("ENABLED" , "false");
    }
    else
    {
        doc.setField("enabled",true);
        doc.setField("ENABLED" , "true");
    }


    this.views = new java.util.ArrayList();
    this.views.add(type);
    this.views.add("abstract");

    doc.setField("views" , views);

    acl(doc);

    //logger.debug("********** after mapping:"+doc.toString() );

    if (type=='location')
        result = location(doc,type);
    else if (type=='ente')
        result = ente(doc,type);
    else if (type=='aoo')
        result = aoo(doc,type);
    else if(type=='documento')
        result = documento(doc, type);
    else if(type=='group')
        result = group(doc, type);
    else
        result = custom(doc,type);


    /* if (result)
     logger.debug("********** after process:"+doc.toString() );
     else
     logger.debug("********** skipped:"+doc.toString() );
     */
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

