function safeexec()
{
    try {
        var r = eval(v('cmd'));
    }
    catch(err) {
        if (err.stack)
            log(err.message);
    }

    u(r);
}

//var logEnabled = true;


function clearLog()
{
    $('#raw2').html( "" );
}

var alt = false;

function replId(match,m1,m2,m3){

    var descM2 = m2;
    m2 = encodeURIComponent(m2);

    return m1+"<a  href='javascript:u(server.get({id:\""+m2+"\",fl:v(\"fl\")}));s(\"sid\",\""+m2+"\");'>"+descM2+"</a>"+m3;
}

function log(str,alternate)
{
    //if (!logEnabled)
    //	return;

    /*if (typeof str == "object")
    {
        if (str.error)
            $('#raw2').append( "<br/>***********ERROR: ************" );
        str = JSON.stringify(str,null,4);
    }*/



    if (str==null)
        str = "";
    else
    {
        //if (str.length>4092)
        //	str = str.substring(0,4092)+"...";

        var regex = /(http:\/\/[^\s\"]*)/g;
        str = str.replace(regex, "<a target=_new href='$1'>$1</a>");


        //"$1<a  href='javascript:u(server.get({id:\"$2\",fl:v(\"fl\")}));s(\"sid\",\"$2\");'>$2</a>$3"
        //"$1<a  href='javascript:u(server.get({id:\"$2\",fl:v(\"fl\")}))'>$2</a>$3"
        str = str.replace(idregex, replId);


    }

    var color = "green";
    if (alt)
        color = "red";

    str = "<br/><div style='line-height:12px;width:100%;display:inline;color:"+color+"' >"+str+"</div>"


    $('#raw2').append(  str );

    var x = $('#fs')[0].scrollHeight;

    $('#fs').scrollTop(x);

    alt = !alt;
}





function applyTemplate(template)
{
    if (!template)
    {
        $('#cmd').val("");
        $('#cmd').attr('rows', '1');
    }
    else
    {
        $('#cmd').val( v(template) );
        $('#cmd').attr('rows', '5');
    }
}

//var LOCATIONS;
/*var LOCATIONS = {
"Milano" : "http://localhost:8984/solr/collection-mi" ,
					"Roma" : "http://localhost:8983/solr/collection1" ,

					"Torino" : "http://localhost:8983/solr/collection-tp" ,
					"Parma" : "http://localhost:8983/solr/collection-pa" ,
}*/

var msg1 = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Ut odio. Nam sed est. Nam a risus et est iaculis adipiscing. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Integer ut justo. In tincidunt viverra nisl. Donec dictum malesuada magna. Curabitur id nibh auctor tellus adipiscing pharetra. Fusce vel justo non orci semper feugiat. Cras eu leo at purus ultrices tristique.\n\n";

var msg2 = "Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.\n\n";

var msg3 = "Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi.\n\n";

var msg4 = "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat.\n\n";

var msg5 = "Cras consequat magna ac tellus. Duis sed metus sit amet nunc faucibus blandit. Fusce tempus cursus urna. Sed bibendum, dolor et volutpat nonummy, wisi justo convallis neque, eu feugiat leo ligula nec quam. Nulla in mi. Integer ac mauris vel ligula laoreet tristique. Nunc eget tortor in diam rhoncus vehicula. Nulla quis mi. Fusce porta fringilla mauris. Vestibulum sed dolor. Aliquam tincidunt interdum arcu. Vestibulum eget lacus. Curabitur pellentesque egestas lectus. Duis dolor. Aliquam erat volutpat. Aliquam erat volutpat. Duis egestas rhoncus dui. Sed iaculis, metus et mollis tincidunt, mauris dolor ornare odio, in cursus justo felis sit amet arcu. Aenean sollicitudin. Duis lectus leo, eleifend mollis, consequat ut, venenatis at, ante.\n\n";

var msg6 = "Consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.\n\n";

var words = "casetta neve sabbia netturbino camaleonte termosifone cartello spazzolino seggiovia graffetta mare scrivania giacca cellulare pavimento mirtilli sifone vetro treno benzina acetone penna cielo auricolare stadio telecomando ringhiera giostra martello panettiere schermo elefante calendario agendina banchina cestino tacchino mosaico tesoro autostrada ciclista bandiera boccale ciabatta braccialepanettiere schermo elefante calendario agendina".split(" ");

var names = "paolo aaron abe abel abner abraham absalom ace adam adolph adrian al alan alban";

var alfresco = ["0ed2513a-d73c-412b-b315-ddd69e721fd3"];

//var searchC = null;
//var updateC = null;
//var authC = new AuthorityComponent("principal");
var types = ["folder","document","document","document","document"];
var locs = ["41.9100711,12.5359979",
    "45.4627338,9.1777323",
    "45.070139,7.6700892",
    "44.7974535,10.3238655"];

//var ticket = null;

function guid()
{
    //'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'
    return 'xxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}



function rndIds(q,s,location)
{
    var Query = { q: (q || "*:*") , fl:"id", rows:s||10 , sort: "random_"+rndInt(1000)+" desc" };

    if (location)
        Query.location = location;

    var response = server.query( Query ).response;

    var ids = [];
    for (var i=0; i<response.docs.length; i++)
        ids.push(response.docs[i].id);
    return ids;
}

function rndFile()
{
    return "docs/"+rndInt(2) + "." + rnd(["docx","xlsx","txt","pptx","pdf"]);
}

/*function rndAlfresco()
{
	var content = rnd(alfresco);
	return content;
	//return "/d/d/workspace/SpacesStore/"+content;
}*/

function rndInt(i)
{
    return Math.floor((Math.random() * (i||100)) + 1);
}

function rndText()
{
    return eval("msg"+rndInt(6));
}

function rndHierarchy(d)
{
    var depth= rndInt(d||3);
    var hierarchy = [];
    var vals = [];
    for (var i=0; i<depth; i++)
    {
        hierarchy.push(rndWord());
        vals.push( ""+i+"/"+hierarchy.join("/")+"/" );
    }
    return vals;
}

function rndWord()
{
    return rnd(words);
}

function rndDate()
{
    var d = new Date(Date.now()-rndInt(200000*1000000));
    return d.toISOString();
}

function rnd(list)
{
    return list[rndInt(list.length)-1];
}

function rndName()
{
    return rnd(names.split(" "));
}

function rndGroup()
{
    return "group"+rndInt(10);
}

var actors = null;
var profiles = null;

function rndAcl()
{
    if (!actors)
    {
        var params = {realtime:false,q:"acl_explicit:*",rows:0,facet:true,"facet.field":"acl_explicit","facet.limit":10 };
        var json = server.query( params );

        var acls = json.facet_counts.facet_fields.acl_explicit;

        actors = [];
        profiles = [];

        for ( var i=0; i<acls.length ; i++ )
        {
            if (acls[i] && (""+acls[i]).indexOf(":") != -1)
            {

                actors.push( acls[i].split(":")[0] );
                profiles.push( acls[i].split(":")[1] );
            }
        }

    }


    return rnd(actors) + ":" + rnd(profiles);
}

function rndAcls()
{
    var acl = [];
    var c = rndInt(3);
    for (var i=0;i<c;i++)
    {
        acl.push( rndAcl() );
    }

    return acl;
}

function contentUrl(id,version)
{
    var uri = getLocation()+"/get?id="+id+"&ticket="+server.ticket+"&wt=raw";

    if (version)
        uri+="&getVersion="+version;

    return uri;
}

function getLocations()
{
    var urlparts = location.href.replace(/[^/]+\#\//,"").split("/");

    urlparts = urlparts.slice(0,5);

    var replica = urlparts.join("/");

    var coll = replica.split("_")[0];

    return { "local" : replica , "cloud" : coll };
}

function getLocation()
{
    var urlparts = location.href.replace(/[^/]*\#\//,"").split("/");

    urlparts = urlparts.slice(0,5);

    var replica = urlparts.join("/");

    return replica;
}

function felogin(ticket)
{
    //t=t || "admin";

    //ticket = t;

    //searchC = new SearchComponent(getLocation(),t);
    //updateC = new UpdateComponent(getLocation(),t);

    //var location = getLocations()["cloud"];

    server = new SolrServer( getLocation() ,ticket);

    var r = u(server.query({ rows:0, "session.info": true}).session);

    //if(r)
    //	log(ticket + " is logged on " +  getLocation() ) ;
}

function sort()
{
    var sort = v('sort');

    if (sort=="random")
        return "random_"+rndInt(1000)+" desc";
    else
        return sort;
}





/*function updatebyquery(reindex)
{
	if (confirm())
	{
		var cmd = v('cmd');

		cmd = eval(cmd);

		var params = { rows:v('rows') , maxrows:v('rows'), sort: sort() , reindex : reindex };

		var response = updateC.updateByQuery( v('query'), cmd , params );

		u(response);
		return response;
	}
}

function deletebyquery()
{
	if (confirm())
	{
		var params = { rows:v('rows') , sort: sort() };

		var response = updateC.deleteByQuery(v('query'),params);

		u(response);
		return response;
	}
}

function undeletebyquery()
{
	if (confirm())
	{
		var params = { rows:v('rows') , sort: sort() };

		var response = updateC.undeleteByQuery(v('query'),params);

		u(response);
		return response;
	}
}

function erasebyquery()
{
	if (confirm())
	{
		var params = { rows:v('rows') , sort: sort() };

		var response = updateC.eraseByQuery(v('query'),params);

		u(response);
		return response;
	}
}

var cachedfile=null;
function getcachedfile()
{
	return upload().response.content_cache;
}

function create(parent)
{
	var file = document.getElementById("afile").files[0];

	upload();

	var node ={
		parent: parent,
		name: file.name,
		type: "document",
		content_cache: cachedfile
	}

	var response = updateC.createNode( node );
	return response;
}

function upload(id)
{
	cachedfile = null;
	var file = document.getElementById("afile").files[0];
	var response = null;

	response = updateC.uploadContent( file );
	cachedfile = response.response.content_cache;

	if (id)
	{
		response = updateC.updateNode(id, { content_cache : response.response.content_cache });
	}

	return response;
}

function index(id)
{
	var response = updateC.indexContent( id );
	return response;
}*/

/*var LOCATIONS = {
"Milano" : "http://localhost:8984/solr/collection-mi" ,
					"Roma" : "http://localhost:8983/solr/collection1" ,

					"Torino" : "http://localhost:8983/solr/collection-tp" ,
					"Parma" : "http://localhost:8983/solr/collection-pa" ,
}*/

function getShard( url )
{
    return url.split("_").slice(1,-1).join("_");
}

function sreport()
{
    var resp = server.query( { q: '-type:(user group)' } , 'rows=0&debugQuery=true&json.nl=map&facet.field=division&facet=true&facet.mincount=2' );

    var track = resp.debug.track;

    var report = {};

    divs = {};

    if (!track)
    {


        divs = resp.facet_counts.facet_fields.division;

        report[ "shard1" ] = divs;
    }
    else
    {
        var execution = track.EXECUTE_QUERY;

        for ( var shard in execution )
        {
            var item = execution[shard];

            var txt = item.Response;

            var idx0 = txt.indexOf("division={");
            var idx1 = txt.indexOf("}",idx0+1);

            var sub = txt.substring( idx0+10 , idx1 );
            var sublist = sub.split(",");

            item = {};

            for ( i in sublist )
            {
                var c = sublist[i].split("=");

                if (c[1] < 2)
                    continue;

                item[c[0]] = parseInt(c[1]);

                divs[c[0]] = true;
            }
            if (Object.keys(item).length==0)
                continue;

            report[ getShard(shard) ] = item;
        }
    }





    var c = 100;

    var w = 200 + c*Object.keys(divs).length;

    var table = '<div class="content clearfix" id="details" style="display: block;">';
    table+= '<table style="width:'+w+'px;table-layout:fixed;" border=0 cellspacing=1 cellpadding=0 >';

    var collName = getLocation().split('_')[0].split('/').reverse()[0];


    table+= '<tr style="" ><td style="width:200px;" >&nbsp;</td>';

    divs = Object.keys(divs);

    divs.sort();

    for (var x in divs)
    {
        var key = divs[x];
        if (!key )
            continue;

        //var desc = x; //x.split("!")[1] || x.split("!")[0];

        var onclick = "var cfr=confirm('split key "+key+"! on collection "+collName+"?'); if (!cfr) return; ";

        onclick += "var resp=server.adminrequest({ qt : '/collections', action : 'SPLITSHARD', 'collection' : '"+collName+"' , 'split.key' : '"+key+"!' });alert(JSON.stringify(resp.responseHeader,null,4));sreport();";

        key = key.replace(/\!$/,"");

        var tag = "<a style='font-size:8px;' title='SPLITKEY "+key+"!' onclick=\""+onclick+"\" >..."+key.split(/[\.!]/).slice(-1)+"</a>";

        table += "<td title='"+key+"' style='background-color: #f2f2f2;text-align:center;font-size:9px;overflow:hidden;text-overflow:ellipsis;white-space: nowrap;' >" +tag + "</td>";
    }

    table += "</tr>";

    var shards = Object.keys(report);

    shards.sort();

    for ( shard in shards )
    {
        shard = shards[shard];

        var onclick = "var cfr=confirm('split shard "+shard+" on collection "+collName+"?'); if (!cfr) return; ";

        onclick += "var resp=server.adminrequest({ qt : '/collections', action : 'SPLITSHARD', 'collection' : '"+collName+"' , 'shard' : '"+shard+"' });alert(JSON.stringify(resp.responseHeader,null,4));sreport();";

        var tag = "<a style='font-size:8px;' title='SPLITSHARD "+shard+"' onclick=\""+onclick+"\" >"+shard+"</a>";

        var tr = "<tr><td title='"+shard+"' style='text-align:right;width:200px;overflow:hidden;background-color: #f2f2f2;' >" + tag + "</td>";

        item = report[ shard ];

        var cnt = 0;

        for (var x in divs)
        {
            x = divs[x];

            if (!x  ) continue;

            cnt += (item[x] || 0);

            tr += "<td style='border:1px groove;width:"+c+"px;overflow:hidden;text-align:center' >" + (item[x] || "") + "</td>";
        }

        tr += "</tr>";

        if (cnt>0)
            table += tr;
    }



    table += "</table></div>";





    $('#raw').html( table );


}

function renderdocs(resp)
{
    var docs = resp.docs;

    $('#raw').html("numFound:"+resp.numFound);

    var fl = v('fl').split(",");

    var c = 150;
    var f = 200;

    //var w = f + c * fl.length;

    var table = '<div class="content clearfix" id="details" style="display: block;">';
    table+= '<br/><table style="width:100%;table-layout:fixed;" border=0 cellspacing=1 cellpadding=0 >';


    table+= '<tr style="" >';

    var idField = "id";

    for (var x in fl)
    {
        x = fl[x];
        if (!x) continue;

        var desc = x;

        if (x.indexOf(":")>0 && x.split(":")[1] == "id")
            idField = x.split(":")[0];

        table += "<td title='"+desc+"' style='background-color: #f2f2f2;text-align:center;font-size:9px;overflow:hidden;text-overflow:ellipsis;white-space: nowrap;' >" +desc + "</td>";
    }

    table += "</tr>";

    for (var x in docs )
    {
        var doc = docs[x];

        var id = doc[idField];

        var descId = id;

        id = encodeURIComponent(id);

        id = "<a title='"+id+"' style='font-size:9px;' href='javascript:u(server.get({id:\""+id+"\",fl:v(\"fl\")}))'>"+descId+"</a>";

        var tr = "<tr><td class='ellipsis' style='max-width:"+f+"px;background-color: #f2f2f2;' >" + id + "</td>";

        for (var x in fl)
        {
            x = fl[x];
            if (!x) continue;

            x = x.split(":")[0];

            if (x==idField)
                continue;



            var desc = doc[x] || "";


            if (x=="*")
            {
                desc = "{...}";
                doc[x] = JSON.stringify( doc , null, 4);
            }
            else if (desc && desc.indexOf && desc.indexOf("/")==0)
                desc = "<a style='font-size:9px;' href=\"javascript:u(server.query({q:'{!PATH}"+desc+"/\*',fl:v('fl')},v('params')), '"+desc+"/\*')\" >"+desc+"</a>";
            else if (desc && desc.indexOf && desc.indexOf("@") != -1)
                desc = "<a title='"+desc+"' style='font-size:9px;' href='javascript:u(server.get({id:\""+desc+"\",fl:v(\"fl\")}))'>"+desc+"</a>";
            else if (x=="name" && fl.indexOf("[FULLPATH]") != -1)
            {
                var fp = doc["[FULLPATH]"];

                var path = fp;

                desc = "<a title='"+path+"' style='font-size:9px;' href=\"javascript:u(server.query({q:'{!PATH}"+path+"/\*',fl:v('fl')},v('params')), '"+path+"/\*')\" >"+desc+"</a>";
            }
            else if (x=="name" && fl.indexOf("parent") != -1)
            {
                var parent = doc["parent"] || "";

                var path = parent + "/" + desc;

                desc = "<a title='"+path+"' style='font-size:9px;' href=\"javascript:u(server.query({q:'{!PATH}"+path+"/\*',fl:v('fl')},v('params')), '"+path+"/\*')\" >"+desc+"</a>";
            }

            tr += "<td title='"+doc[x]+"' class='ellipsis' style='border:1px groove;max-width:"+c+"px;' >" + desc + "</td>";
        }

        tr += "</tr>";

        table += tr;
    }

    table += "</table></div>";

    $('#editor').html( table );
}

var updSchema = {

    id: { type: "string" , "propertyOrder" : 0 ,readOnly : "true" },
    name: { type: "string" , "propertyOrder" : 10},
    "[FULLPATH]": { type: "string", "propertyOrder" : 20, readOnly : "true"},
    type: { type: "string", "propertyOrder" : 21, readOnly : "true"},
    parent: { type: "string", "propertyOrder" : 22},
    acl_parent: { type: "string" , readOnly : "true", "propertyOrder" : 30 },
    enabled : { type: "boolean", format: "checkbox", "propertyOrder" : 40 },
    acl_inherits : { type: "boolean", format: "checkbox" , "propertyOrder" : 50},
    acl_explicit :

        {
            "type": "array",

            "propertyOrder" : 60,
            "format" : "table",
            "items": {
                "title" : "\0",
                "type": "string"
            }
        },

    state: {
        "type": "array",
        "propertyOrder" : 70,
        "uniqueItems": true,
        "items": {
            "type": "string",
            "enum": [
                "profile",
                "content",
                "clean",
                "xhtml",
                "synced"
            ]
        }
    }
}

var rof = [ "acl_parent", "[FULLPATH]", "built_on" , "indexed_on", "type" ,"id", "views" , "modified_on" , "content_id" , "created_by" , "created_on" , "modified_by" , "location" , "acl_read" , "_version_" ]

function createDoc()
{
    var editor = renderdoc();
    editor.enable();
    $(editor.element).find(".json-editor-btn-edit").show();
    document.getElementById('edit').innerHTML = "Cancel";
}

function renderdoc(doc,properties)
{
    //var doc = server.get({ id: v('sid'), fl: v('fl') }).doc;

    if (!properties)
        properties = updSchema;

    var create = false;

    var props = {};

    for (var field in properties)
        props[field] = properties[field];

    for( var field in doc )
    {
        if (!props[field])
        {
            var val = doc[field];

            if (val==null)
                continue;

            if (val.pop)
                props[field] = properties.acl_explicit;
            else if (typeof(val)=="number")
                props[field] = { type:"integer"};
            else if (typeof(val)=="boolean")
                props[field] = { type: "boolean", format: "checkbox" };
            else
                props[field] = { type:"string"};

            if (rof.indexOf(field) != -1)
                props[field] = { type:"string" , readOnly : "true" };
        }
    }

    if (!doc)
    {
        doc ={
            parent: v("sid"),
            name: "newName",
            type: null
        }
        create = true;

        props.type = {
            "type": "string",
            "propertyOrder" : 1,


            "enum": [
                "documento",
                "folder",
                "aoo",
                "ente",
                "fascicolo",
                "location",
                "titolario",
                "custom"
            ]

        }

        props["[stream.file]"] = {
            "title": "stream.file",
            "type": "string"
        };

        props["id"] = { "type": "string","propertyOrder" : 0 };

        delete props["[FULLPATH]"];
        delete props.acl_parent;
        delete props.enabled;
    }

    var schema = {
        type: "object",
        title: doc.type,

        properties: props

    };

    $('#raw').html("<div style='position:absolute;left:500px'><nobr><button id='edit'>Edit</button><button id='save'>Update</button></nobr></div>");
    $('#editor').html("");

    JSONEditor.defaults.editors.string.options.input_width='400px';
    JSONEditor.defaults.editors.array.options.input_width='400px';
    JSONEditor.defaults.editors.object.options.input_width='400px';

    //JSONEditor.defaults.theme = 'jqueryui';
    //JSONEditor.defaults.iconlib = 'fontawesome4';


    var editor = new JSONEditor(document.getElementById('editor') , { startval: doc, disable_collapse : true, disable_array_reorder : true , schema: schema } );

    editor.disable();
    $(editor.element).find(".json-editor-btn-edit").hide();

    document.getElementById('edit').addEventListener('click',function() {
        if(editor.isEnabled()){
            editor.disable();
            $(editor.element).find(".json-editor-btn-edit").hide();
            this.innerHTML = "Edit";
        } else {
            editor.enable();
            $(editor.element).find(".json-editor-btn-edit").show();
            this.innerHTML = "Cancel";
        }

    });

    document.getElementById('save').addEventListener('click',function() {
        // Get the value from the editor

        var node = editor.getValue();

        //editor.destroy();

        var params = {};

        var script = "node = ";
        script += JSON.stringify(node,null,4);

        if (create)
            script += "\nserver.create( node , {} );"
        else
            script += "\nserver.update( node , {} );"

        $('#cmd').val(script);
        $('#cmd').attr('rows', '5');

    });

    return editor;

}

function v(id)
{
    return $('#'+id).val();
}

function s(id, value)
{
    $('#'+id).val(value);
}



var idregex = /(\")([^@\"\:]+@[^\"\:]+)([\"\:"])/gm
var pathregex = /\"\[FULLPATH\]\"\: \"([^\""]+)\"/gm
//var parentpathregex = /\"\[PARENTPATH\]\"\: \"([^\""]+)\/[^\/]+\"/gm

var lastresponse = null;
var lastq = null;
var render = true;

function u(resp,q)
{
    if (!resp)
    {
        resp = lastresponse;
        q = lastq;
    }
    else
    {
        lastresponse = resp;
        lastq = q;
    }

    if (!resp)
    {
        $('#editor').html("");
        $("#raw").html("");
        return;
    }

    var content = resp;
    var docs = null;
    var doc = null;

    var thisrender = render;

    if (resp.doc)
    {
        content = resp.doc;
        doc = resp.doc;
    }
    else if (resp.response)
    {
        content = resp.response;
    }

    if (!doc && !content.docs)
        thisrender = false;

    if (!thisrender)
    {
        var text = JSON.stringify( content, null, 4 );

        text = text.replace(idregex, replId);
        text = text.replace(pathregex, '"[FULLPATH]": "<a  href=\'javascript:u(server.query({q:"{!PATH}$1/*",fl:v("fl")},v("params")),"{!PATH}$1/*")\' >$1</a>"');

        $('#editor').html("");
        $("#raw").html(text);

        return;
    }

    if (doc)
    {
        s("sid",doc.id);
        renderdoc(doc,updSchema);
        return;
    }

    if (content.docs)
    {
        breadcrumb(q);

        renderdocs(content);
        return;
    }

}

function breadcrumb(q)
{

    var breadcrumb = "";

    if (q)
    {
        s("query",q);

        if (q.indexOf("/")!=-1 && q.indexOf(":")==-1)
        {
            var parts = q.split("/");

            breadcrumb = "";

            var path="";

            for (var i=1; i<parts.length-1;i++)
            {
                path += "/" + parts[i];

                var sin = "{!PATH}"+path;

                var com = "{!PATH}"+path + "/*";

                breadcrumb += '/<a title="'+com+'" ondblclick=\'u(server.query({q:"'+sin+'\",fl:v("fl")},v("params")).response,\"'+sin+'\");\'  href=\'javascript:u(server.query({q:"'+com+'\",fl:v("fl")},v("params")).response,\"'+com+'\");\' >'+parts[i]+'</a>';
            }
            breadcrumb += "/" + parts.slice(-1);

        }
        else
        {
            breadcrumb = '<a title="'+q+'" href=\'javascript:u(server.query({q:"'+q+'\",fl:v("fl")},v("params")).response,\"'+q+'\");\' >'+q+'</a>';
        }
        $("#breadcrumb").html(breadcrumb);
    }
    else
    {
        $("#breadcrumb").html("");
    }


}

/*function _search(q,qt,a,val)
{
	qt = qt || "/select" ;

	q = q || v('query');

	s('query',q);

	var Query = { qt:qt, fl:v('fl'), rows:v('rows') , sort: sort() };

	if (q.indexOf("/")==0)
		Query.PATH = q;
	else
		Query.q = q;

	if (a)
		Query[a] = val;

	var response = server.query( Query );

	u(response);
}*/


/*$("#jqGrid").jqGrid({
                datatype: "local",
				data: response.docs,
                height: "100%",
				width: "3009px",
                colModel: [
                    { label: 'id', name: 'id', key:true },
                    { label: 'name', name: 'name' },
                    { label: 'FULLPATH', name: '[FULLPATH]' },
                    { label: 'indexed_on', name: 'indexed_on' }
                ],
                shrinkToFit: false,
                autowidth : true,
                viewrecords: true, // show the current page, data rang and total records on the toolbar
                caption: "Load jqGrid through Javascript Array",
            });*/

/*if (response.PATHQUERY)
{
    var parts = response.PATHQUERY.PARENTPATH.split("/");

    var html = "<a href=javascript:_search('PATH:/*') >PATH:</a>";
    var pPATH = "";
    for (var i=0; i<parts.length;i++)
    {
        html += "/<a href=javascript:_search('PATH:/"+pPATH+parts[i]+"/*') >"+parts[i]+"</a>";
        pPATH = pPATH + parts[i] + "/";
    }

    $("#breadcrumb").html(html);
}
else
{
    $("#breadcrumb").html( "q:"+q );
}*/
		
	
	