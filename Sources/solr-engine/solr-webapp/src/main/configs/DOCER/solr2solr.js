/*

 da modificare in solrconfig.xml :


 ...

 <updateRequestProcessorChain name="updateChain">

 <processor class="solr.LogUpdateProcessorFactory" >
 <int name="maxNumToLog">100</int>
 </processor>

 <processor class="solr.StatelessScriptUpdateProcessorFactory">
 <bool name="enabled">false</bool>
 <str name="script">docer_due_quattro.js</str>
 </processor>

 <processor class="solr.RemoveBlankFieldUpdateProcessorFactory" >
 <!--<lst name="exclude">
 <str name="fieldName">parent</str>
 </lst>-->
 </processor>

 ....

 */

function getVersionsDates(doc) {
    var content_versions = doc.getFieldValues("content_versions");
    if (content_versions != null)
    {
        var versions = content_versions.toArray();
        var array = [];

        for( var i=0; i< versions.length ; i++ )
        {
            var str = ""+versions[i];
            var tStampRegexp = /&version=(\d*)&/;

            var match = tStampRegexp.exec(str);
            //logger.debug("docer_due_4#processAdd: sequence=" + doc.getFieldValue("DOCNUM") );
            //logger.debug("docer_due_4#processAdd: match=" + match[1] );


            var date_tmp = new java.util.Date(java.lang.Long.parseLong(match[1]));
            var date =  Packages.it.kdm.solr.common.FieldUtils.formatDate(date_tmp);
            
            array.push(date);
        }

        return array;
    }
}

//Import solr2solr per docer 2.4

var orgprefix = req.getParams().get("orgprefix") || "" ;

if (orgprefix != "" && !orgprefix.endsWith("__") )
    orgprefix += "__";

function acl(doc)
{
    var acl_explcit = doc.getFieldValues( "acl_explicit" );

    logger.debug("docer_due_4#acl(): acl_explicit=" + acl_explcit );

    if (acl_explcit != null)
    {
        acl_explcit = acl_explcit.toArray();

        for( var i=0; i< acl_explcit.length ; i++ )
        {
            acl_explcit[i] = orgprefix + acl_explcit[i];

        }
        doc.setField( "acl_explicit" , acl_explcit );
    }
}


function group(doc)
{
    var id = doc.getFieldValue("id");
    var sid = doc.getFieldValue("sid");

    logger.debug("docer_due_4#group(): id=" + id );


    if (id==null)
    {
        logger.debug( "group non inserito perchè perchè id \n{}", id);
        return false;
    }

    var division = orgprefix + id;
    doc.setField("id" , division );

    doc.setField("sid" , orgprefix + sid );

    var group_id = doc.getFieldValue("GROUP_ID") || "";
    if (group_id!="")
        doc.setField("GROUP_ID" , orgprefix + group_id );

    var group = doc.getFieldValue("PARENT_GROUP_ID") || "";
    if (group!="")
    {
        doc.setField("PARENT_GROUP_ID" , orgprefix + group );
        doc.setField("parent" , orgprefix + group+"@group" );
    }


    return true;

}

function aoo(doc)
{
    var COD_AOO = doc.getFieldValue("COD_AOO") || "";
    if (COD_AOO!="")
    {
        doc.setField("GROUP_ID" , orgprefix + COD_AOO );
        doc.setField("ADMIN_GROUP_ID" , orgprefix + "ADMINS_AOO_" + COD_AOO );
    }

    var group = doc.getFieldValue("PARENT_GROUP_ID") || "";
    if (group!="")
    {
        doc.setField("PARENT_GROUP_ID" , orgprefix + group );
        doc.setField("parent" , orgprefix + group+"@group" );
    }

    return true;
}

function ente(doc)
{
    var COD_ENTE = doc.getFieldValue("COD_ENTE") || "";
    if (COD_ENTE!="")
    {
        doc.setField("GROUP_ID" , orgprefix + COD_ENTE );
        doc.setField("ADMIN_GROUP_ID" , orgprefix + "ADMINS_ENTE_" + COD_ENTE );
    }
    return true;

}

function user(doc)
{
    var id = doc.getFieldValue("id");
    var sid = doc.getFieldValue("sid");

    logger.debug("docer_due_4#user(): id=" + id );

    if (id==null)
    {
        logger.debug( "user non inserito perchè perchè id \n{}", id);
        return false;
    }

    var division = orgprefix + id;
    doc.setField("id" , division);
    doc.setField("sid" , orgprefix + sid );

    var user_id = doc.getFieldValue("USER_ID") || "";
    if (user_id!="")
        doc.setField("USER_ID" , orgprefix + user_id );

    var groups = doc.getFieldValues( "groups" );

    var newGroups = new java.util.ArrayList();

    if (groups != null)
    {
        groups = groups.toArray();

        for( var i=0; i<groups.length ; i++ )
        {

            newGroups.add( orgprefix + groups[i] );
        }
    }

    doc.setField( "groups" , newGroups );

    return true;
}

function documento(doc) {

    var sid = doc.getFieldValue("DOCNUM");
    var userId = doc.getFieldValue("created_by") || "admin";
    var name = doc.getFieldValue("DOCNAME") || "";

    var versions_dates = getVersionsDates(doc);

    //versionamento avanzato
    doc.setField("DOC_VERSION" , sid + ".0" );


    sVersions = "<versions>";

    if(versions_dates && versions_dates != null){
        for (var i = versions_dates.length; i > 0; --i) {
            {
                var userRegexp = /(.*?)@user/;
                var match = userRegexp.exec(userId);

                var v = "<version><number>"+ i + "</number>";

                v += "<date>"+versions_dates[i-1]+"</date>";

                v += "<userId>"+match[1]+"</userId>";

                v += "<filename>"+name+"</filename>";

                v += "</version>";

                sVersions += v;
            }

            sVersions += "</versions>";

            doc.setField("VERSIONS" , sVersions);

        }
    }

    return true;

}

function folder(doc) {

    var ente = doc.getFieldValue("COD_ENTE");

    if (ente==null)
    {
        logger.debug( "folder non inserito perchè perchè ente non è valorizzato \n{}",doc);
        return false;
    }

    logger.debug( "folder inserito xxx  \n{}", doc);

    var owner = doc.getFieldValue("FOLDER_OWNER");

    if(owner==null)
        owner = "admin";
    owner = orgprefix + owner;

    doc.setField("FOLDER_OWNER" , owner);
    
}

function custom(doc,type)
{
    //noop
    return true;
}


function processAdd(cmd) {

    doc = cmd.solrDoc;
    var type = doc.getFieldValue("type");

    acl(doc);

    if (type == "documento")
        result = documento(doc, type);
    else if (type=='aoo')
        result = aoo(doc, type);
    else if (type=='ente')
        result = ente(doc, type);
    else if (type=='group')
        result = group(doc, type);
    else if (type=='user')
        result = user(doc, type);
    else if (type=='folder')
        result = folder(doc, type);
    else
        result = custom(doc,type);


    //gestione orgprefix su tutto
    var created_by = doc.getFieldValue("created_by");
    if (created_by == null)
        created_by = "admin";

    created_by = orgprefix + created_by;

    var modified_by = doc.getFieldValue("modified_by");
    if (modified_by == null)
        modified_by = "admin";

    modified_by = orgprefix + modified_by;


    doc.setField("created_by" , created_by );
    doc.setField("CREATOR" , created_by );
    doc.setField("modified_by" ,  modified_by );
    doc.setField("MODIFIER" ,  modified_by );
    doc.removeField("acl_read" );

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
