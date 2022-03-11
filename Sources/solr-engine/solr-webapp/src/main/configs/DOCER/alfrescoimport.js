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

function acl(doc)
{
	var acl_explcit = doc.getFieldValues( "__ACL_EXPLICIT__" );
	
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
			else if (right=="NormalAccess")
				right = "normalAccess";
			else if (right=="ReadOnlyAccess" || right=="Read")
				right = "readOnly";
			
			var actor = acl.split(":")[0];
			var actorType = "user";
			
			if (actor.indexOf("GROUP_") == 0)
			{
				actor = actor.substring(6);
				
				if ( actor.toUpperCase() == "EVERYONE")
					actor = "everyone";
				
				/*if ( actor==aoo )
					actorType = "aoo";
				else if ( actor==ente )
					actorType = "ente";
				else*/
				actorType = "group";
				
			}
			
			acl_explcit[i] = orgprefix+ encid(actor) + "@" + actorType + ":" + right;
			
		}
		doc.setField( "acl_explicit" , acl_explcit );
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
	doc.setField("GROUP_ID", orgprefix + sid);
	doc.setField("ADMIN_GROUP_ID" , orgprefix + "ADMINS_ENTE_"+sid);
	doc.setField("ADMIN_GROUP_NAME" , "Admins Ente " + name);
	
	this.views.add("group");
	
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
	doc.setField("GROUP_ID", orgprefix + sid);
	doc.setField("ADMIN_GROUP_ID" , orgprefix + "ADMINS_AOO_"+sid);
	doc.setField("ADMIN_GROUP_NAME" , "Admins Aoo " + name);
	doc.setField("PARENT_GROUP_ID" , ente );
	
	this.views.add("group");
	
	return true;
}

function titolario(doc)
{
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

function fascicolo(doc)
{
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

var alf = true;

function documento(doc)
{
	var content = doc.getFieldValue("__CONTENT__");
	
	var ente = doc.getFieldValue("COD_ENTE");
	var aoo = doc.getFieldValue("COD_AOO");
	
	var classifica = doc.getFieldValue("CLASSIFICA") || "";
	var anno = doc.getFieldValue("ANNO_FASCICOLO") || "";
	
	var sid = doc.getFieldValue("DOCNUM");
	var division = this.workspace + "." + encid(ente) + "!" + encid(aoo);
	var name = doc.getFieldValue("DOCNAME") || sid;
	
	var progr = doc.getFieldValue("PROGR_FASCICOLO") || "";
	var folder = doc.getFieldValue("PARENT_FOLDER_ID") || "";
	
	var id = division + "!" + encid(sid) + "@documento";
	doc.setField("id" , id );
	doc.setField("name" , encdocname(name,doc) );
	doc.setField("division" , division );
	doc.setField("sid" , sid );
	
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
	
	if (content_versions.size()>0)
	{
		content_versions.add(workingCopy);
	}
		
	
	//else
	//	content_versions.add( content_id+"?1");
		
		//content_id = null;
	/*}
	else
	{
		var versions = doc.getFieldValues("__VERSIONS__");
		
		id = id.replace( /[!\.\/]+/g , "/" ); 
		var impDate = content.substring(8);
	
		var idx = id.lastIndexOf("/") ;
		var idx2 = impDate.lastIndexOf("/") ;
	
		content_id = "filesystem://"+id.substring(0,idx+1) + impDate.substring(0,idx2+1) + id.substring(idx+1);
		
		if (versions!=null)
		{
			versions = versions.toArray();
			
			for ( var j=0; j<versions.length-1;j++)
				content_versions.add(versions[j]);
		}
		
		content_versions.add(content);
		
		for( var i=0; i<content_versions.size(); i++)
		{
			var content_url = ""+content_versions.get(i);
			
			if (content_url.indexOf("//") != -1)
				content_url = content_url.substring(8);
			
			content_versions.set(i, content_id + "?" + content_url);
		}
	}*/
	
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

function folder(doc)
{
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
	
	/*if (aoo=="")
	{
		logger.warn( "folder non inserito perchè perchè aoo non è valorizzato \n{}",doc);
		return false;
	}*/
	
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


	var owner = doc.getFieldValue("FOLDER_OWNER");

	if(owner==null)
		owner = "admin";
	owner = orgprefix + owner;

	doc.setField("FOLDER_OWNER" , owner);
	
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

var orgprefix = req.getParams().get("orgprefix") || "";

if (orgprefix != "" && !orgprefix.endsWith("__") )
    orgprefix += "__";

function group(doc)
{
	var gs = doc.getFieldValue("GRUPPO_STRUTTURA");
	
	if (gs=="true")
	{
		logger.debug( "group non inserito perchè perchè struttura \n{}",doc);
		return false;
	}
	
	var sid = doc.getFieldValue("GROUP_ID") || this.uuid;
	var division = orgprefix + encid(sid);
	var name = doc.getFieldValue("GROUP_NAME") || sid;
	
	var group = doc.getFieldValue("PARENT_GROUP_ID") || "";
	var aooente = doc.getFieldValue("AOOENTE") || "";
	
	doc.setField("id" , division + "@group" );
	doc.setField("name" , encuniquename(name,doc) );
	doc.setField("division" , division );
	doc.setField("sid" , orgprefix + sid );
	doc.setField("GROUP_ID" , orgprefix + sid );
	
	if (aooente!="")
	{
        //doc.removeField("AOOENTE");

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

function user(doc)
{
	var sid = doc.getFieldValue("USER_ID") || this.uuid;;
	var division = orgprefix + encid(sid);
	var name = doc.getFieldValue("FULL_NAME") || sid || this.uuid;
	
	doc.setField("id" , division + "@user" );
	doc.setField("name" , encuniquename(name,doc) );
	doc.setField("division" , division );
	doc.setField("sid" , orgprefix + sid );
	doc.setField("USER_ID" , orgprefix + sid );

    var secret = req.getParams().get("secret","SECRET");
	
	var password = org.apache.commons.codec.digest.DigestUtils.md5Hex( sid+secret );
	
	doc.setField("USER_PASSWORD" , password );
	
	var groups = doc.getFieldValues( "GROUPS" );
	
	var newGroups = new java.util.ArrayList();
	
	newGroups.add("everyone@group");
	
	var isAdmin = false;
	
	if (groups != null)
	{
		doc.removeField("GROUPS");
		groups = groups.toArray();
		
		for( var i=0; i<groups.length ; i++ )
		{
			if (groups[i].toUpperCase() == "ALFRESCO_ADMINISTRATORS")
				isAdmin = true;
			
			newGroups.add( orgprefix + encid(groups[i]) + "@group" );
		}

        if (isAdmin)
		    newGroups.add( "admins@group" );
		
	}
	
	doc.setField( "groups" , newGroups );

    addeveryone(doc);

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

function versions( doc, type )
{
	return related(doc,type);
}

function related( doc , type )
{
	var ente = doc.getFieldValue("COD_ENTE");
	var aoo = doc.getFieldValue("COD_AOO");
	
	var related = ""+doc.getFieldValue("__RELATED__");
	var sid = ""+doc.getFieldValue("__SID__");
	var princ = ""+doc.getFieldValue("__PRINC__");
	
	var division = this.workspace + "." + encid(ente) + "!" + encid(aoo);
	
	related = related.split(",");	
	related.push(princ);
	
	doc.setField("id" , division + "!" + sid + "@" + type );
	doc.setField("name" , sid );
	doc.setField("division" , division );
	doc.setField("sid" , sid );
	
	var list = new java.util.ArrayList();
	
	for( var i=0; i< related.length ; i++ )
	{
		list.add( division + "!" + related[i] + "@documento" );
	}
	
	doc.setField("related" ,list );
	doc.setField("parent" , division+"!@aoo");
	doc.setField("acl_explicit" , encid(aoo) + "@group:normalAccess");
	
	return true;
}

function processAdd(cmd) { 

    var doc = cmd.solrDoc;

    var schema = req.getSchema();
	
	logger.debug("********** import document:"+doc.toString() );
	
	//node.id id, node.uuid uuid, qt.local_name , acl.inherits, acl.inherits_from

	this.uuid = doc.getFieldValue("uuid");
	
	var sequence = doc.getFieldValue("id");
	var type = doc.getFieldValue("local_name");
	
	var acl_inherits = doc.getFieldValue("inherits");
	var inherits_from = doc.getFieldValue("inherits_from");
	var alf_name = doc.getFieldValue("name");
	var enabled = doc.getFieldValue("enabled");
	
	doc.removeField("id");
	doc.removeField("local_name");
	doc.removeField("inherits");
	doc.removeField("inherits_from");
	doc.removeField("uuid");
	doc.removeField("name");
	doc.removeField("icon");
	doc.removeField("enabled");
	
	this.workspace = 'DOCAREA' ;
	
	this.workspace = encname(this.workspace);
	
	acl_inherits = ( acl_inherits == 1);
	
	
	var mappings = {

                		"progressivoFascicolo" : "PROGR_FASCICOLO",
                		"author" : "AUTHOR_ID",
                		"dataCreazione" : "CREATION_DATE",
                		"parentProgressivoFascicolo" : "PARENT_PROGR_FASCICOLO",
                		"tipoDataContrCer" : "T_D_CONTR_CER",
                		"numRegistraz" : "N_REGISTRAZ",
                		"annoRegistraz" : "A_REGISTRAZ",
                		"oggettoRegistraz" : "O_REGISTRAZ",
                		"dataAperturaFascicolo" : "DATA_APERTURA",
                		"dataChiusuraFascicolo" : "DATA_CHIUSURA",
						
						"fascicoliSecondari" : "FASC_SECONDARI"

  					}
					
	this.anagrafiche = {
		
		"areatematica" : "AREA",
		"fascicoloanno" : ""
	}
	
	//this.gruppi_aoo = [ "AOOTEST" ];
	//this.gruppi_ente = [ "ENTETEST" ];
					
	if (type=="person")
	{
		type="user";
		mappings["organization"] = "COD_ENTE";
		mappings["location"] = "COD_AOO";
		mappings["middleName"] = "FULL_NAME";
		mappings["userName"] = "USER_ID";
		mappings["email"] = "EMAIL_ADDRESS";
		mappings["homeFolder"] = "";
		mappings["homeFolderProvider"] = "";
	}
	if (type=="advancedVersion")
		type = "versions";
	
	if (type=="authorityContainer")
	{
		type = "group";
		mappings["authorityDisplayName"] = "GROUP_ID";
		mappings["nomeGruppo"] = "GROUP_NAME";
		doc.removeField("authorityName");
	}
					
	var keys = doc.keySet().toArray();

	for (var i=0; i<keys.length; i++)
	{
		
		key = "" + keys[i];
		
		var val = ""+doc.getFieldValue(key);
		
		var mapping = mappings[key];
		
		if (mapping == null)
		{
			mapping = key.replace( /([a-z])([A-Z])/g , "$1_$2" ).toUpperCase() ;
		}
		
		var field = doc.get(key);
		field.setName(mapping);

		doc.removeField(key);
		
		if (val=="" || mapping == "")
			continue;
		
		doc.put(mapping, field );
		
	}

	var created_by = doc.getFieldValue("CREATOR");
	var modified_by = doc.getFieldValue("MODIFIER");
	
	if (created_by == null){
		created_by = "admin";
		doc.setField("CREATOR" , orgprefix + created_by );
	} else {
		doc.setField("CREATOR" , orgprefix + created_by );
		created_by += "@user";
	}
	
	if (modified_by == null){
		modified_by = "admin";
		doc.setField("MODIFIER" , orgprefix + modified_by );
	} else {
		doc.setField("MODIFIER" , orgprefix + modified_by );
		modified_by += "@user";
	}
	
					
	doc.setField("created_by" , orgprefix + created_by );
	doc.setField("modified_by" , orgprefix +  modified_by );
	
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

        /*if (val.match(/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{3}[-+].*$/ ))
        {
            val = val.substring(0,23)+"Z";
            doc.setField( key, val);
        }*/
    }
	
	if (uuid!=null)
		doc.setField("uuid" , uuid);
	
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
	
	logger.debug("********** after mapping:"+doc.toString() );
	
	/*var func=null;
	try
	{
		func = eval(type);
	}
	catch(e)
	{
	}




	
	var result;
	
	if (func)
		result = func(doc,type);
	else
		result = custom(  doc , type );*/
	
	if (type=='location')
		result = location(doc,type)
	else if (type=='ente')
		result = ente(doc,type)
	else if (type=='aoo')
		result = aoo(doc,type)
	else if (type=='titolario')
		result = titolario(doc,type)
	else if (type=='folder')
		result = folder(doc,type)
	else if (type=='documento')
		result = documento(doc,type)
	else if (type=='fascicolo')
		result = fascicolo(doc,type)
	else if (type=='related')
		result = related(doc,type)
	else if (type=='versions')
		result = versions(doc,type)
	else if (type=='user')
		result = user(doc,type)
	else if (type=='group')
		result = group(doc,type)
	else 
		result = custom(doc,type)
	
	
	if (result)
		logger.debug("********** after process:"+doc.toString() );
	else
		logger.debug("********** skipped:"+doc.toString() );
	
	return result;
	
	/*if (type=="location")
		location(doc);
	else if(type=="ente")
		ente(doc);
	else if(type=="aoo")
		aoo(doc);
	else if(type=="fascicolo")
		fascicolo(doc);
	else if(type=="documento")
		documento(doc);
	else if(type=="folder")
		folder(doc);
	else if(type=="user")
		user(doc);
	else if(type=="group")
		group(doc);*/
	

}

function parseDate(val)
{
    logger.trace("date:{}",val );

    if (val.match(/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{3}Z$/ ))
    {
        return val;
    }

    if (val.match(/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}\.\d{3}[-+].*$/ ))
    {
        return val.substring(0,23)+"Z";
    }

    if (val.match(/^\d{4}-\d{2}-\d{2}$/ ))
    {
        return val + "T00:00:00.000Z";
    }

    val = (""+val).replace( /[^\d]/g, "" );

    var iso = "0000-00-00T00:00:00.000Z";

    var j=0;
    for( i=0; i< iso.length ; i++)
    {
        if (iso[i].match(/\d/) && j<val.length)
            iso = iso.substring(0, i) + val[j++] + iso.substring(i+1) ;
    }

    logger.trace("iso:{}",iso );

    return iso;
}

function processDelete(cmd) {}
function processMergeIndexes(cmd) {}
function processCommit(cmd) {

    if (cmd.optimize)
        it.kdm.solr.components.common.ZkUtils.initCounter();
}
function processRollback(cmd) {}
function finish() {}
