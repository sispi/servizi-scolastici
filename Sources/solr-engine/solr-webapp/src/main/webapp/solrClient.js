var selectHandler="/select";
var getHandler="/get";
var indexHandler="/index";
var uploadHandler="/upload";
var createHandler="/create";
var syncHandler="/sync";
var updateHandler="/update";
var deleteHandler="/update";
var retrieveHandler="/retrieve";
var repositoryHandler="/repository";
var getaclHandler="/getacl";
var updatebyqueryHandler="/updatebyquery";

function parseParams(str) {
    return str.split('&').reduce(function (params, param) {
        var paramSplit = param.split('=').map(function (value) {
            return decodeURIComponent(value.replace('+', ' '));
        });
        params[paramSplit[0]] = paramSplit[1];
        return params;
    }, {});
}

function SolrServer(path,ticket)
{
	this.local = path;
	this.path = path.split("_")[0];;
	this.ticket = ticket;
	
	var parts = this.path.split("/");
	this.core = parts.pop();
	
	if (this.core=="solr")
	{
		this.core = null;
		this.server = path;
	}
	else
	{
		this.server = parts.join("/");
	}
	
	this.commit = function(waitFlush, waitSearcher, softCommit){ 
		var request = new UpdateRequest().setAction( COMMIT, waitFlush, waitSearcher,softCommit );
		return this.request(request);
	}
	
	this.optimize = function(waitFlush, waitSearcher, maxSegments){ 
		var request = new UpdateRequest().setAction( OPTIMIZE, waitFlush, waitSearcher, maxSegments );
		return this.request(request);
	}
	
	this.rollback = function(){
		var request = new UpdateRequest().rollback();
		return this.request(request);
	}

	this.get = function( params )
	{
		params.ticket = this.ticket;
		return HTTP( this.path + (params.qt || getHandler) , params );
	}

	this.adminrequest = function( params )
	{
		params.ticket = this.ticket;
		return HTTP( this.server + "/admin" + params.qt , params );
	}

	this.ping = function( params )
	{
		params = params || {};
		params.ticket = this.ticket;
		return HTTP( this.path + "/admin/ping" , params );
	}

	this.query = function( params , params2 )
	{
		if (params2)
		{
			var queryString = parseParams(params2);

			for( var k in queryString )
				params[k] = queryString[k];
		}

		var base = this.path;

		if (params.distrib == "false")
			base = this.local;

		params.ticket = this.ticket;
		return HTTP( base + (params.qt || selectHandler) , params );
	}
	
	this.post = function(params, file)
	{
		params.ticket = this.ticket;
		return HTTP( this.path + (params.qt || selectHandler) , params , file );
	}
	
	this.request = function(request)
	{
		if (request.command.length==0)
			return this.query( request.params );
		else
			return this.post( request.params , request.command );
	}

	this.sync = function( updatenode , params )
	{	
		var request = new UpdateRequest( syncHandler );
		request.add(updatenode);
		request.setParams(params);
		return this.request(request);
	}

	this.update = function( updatenode , params )
	{	
		var request = new UpdateRequest( updateHandler );
		request.add(updatenode);
		request.setParams(params);
		return this.request(request);
	}

	this.create = function( updatenode , params )
	{	
		var request = new UpdateRequest( createHandler );
		request.add(updatenode);
		request.setParams(params);
		return this.request(request);
	}

	this.delete = function( id , commitWithinMs )
	{	
		var request = new UpdateRequest( deleteHandler );
		request.deleteById(id,commitWithinMs);
		return this.request(request);
	}
	
	this.deleteByQuery = function( query, commitWithinMs )
	{
		var request = new UpdateRequest(updateHandler);
		//request.setParam("ticket",ticket);
		request.deleteByQuery(query,commitWithinMs);
		return this.request(request);
	}
	
	/* METODI NUOVI */
	
	this.check = function()
	{
		return this.query( { qt: "/check" , core : this.core , q : "*:*" , wt : "skipper" } );
	}
	
	this.reload = function()
	{
		this.path = this.server;
		return this.query( { qt: "/admin/cores" , action : "RELOAD" , core : this.core } );
	}
	
	this.clusterstatus = function()
	{
		this.path = this.server;
		return this.query( { qt: "/admin/collections" , action : "CLUSTERSTATUS" } );
	}
	
	this.initSchema = function()
	{
		http://localhost:8983/solr/get?type=node&initSchema
		return this.query({ qt: "/initschema" });
	}
	
	this.crawl = function(q,rows)
	{	
		var Query = {qt : "/crawl"};
		
		if (q) Query.q = q;
		if (rows) Query.rows = rows;
		
		return this.query(Query);
	}
	
	/*this.getTypeConfig = function(type)
	{
		type = (type || "node").split("@").pop().split(".")[0];
		return this.query({ qt: "/get", cache:"schema" , key:type }).value;
	}*/

	/*this.getAuthorityConfig = function(authority)
	{
		return this.query({ qt: "/realget", id: authority+"!@authority" }).doc;
	}*/
	
	this.retrieve = function( request ){
		if (!request.qt) request.qt = getHandler;
		return this.query( request );
	}
}

var OPTIMIZE = "optimize";
var COMMIT = "commit";
var ROLLBACK = "rollback";
function UpdateRequest(qt)
{
	if (!qt)
		qt = updateHandler;

	this.qt = qt;
		
	this.method = "POST";
	//this.handler = handler;
	this.params = { qt: qt };

	this.command = [];
	
	this.getParams = function()
	{
		return this.params;
	}
	
	this.setParams = function(params)
	{
		this.params = params || {};
		this.setParam("qt",this.qt);
	}
	
	this.setParam = function(param,value)
	{
		this.params[param] = value;
	}
	
	this.setCommitWithin = function(commitWithinMs)
	{
		this.command["commitWithin"] = commitWithinMs;
	}
	
	this.process = function(server) {
		return server.request(this);
	}
	
	this.rollback = function() {
		this.params[ROLLBACK] = true;
		return this;
	}

	this.optimize = function() {
		this.params[OPTIMIZE] = true;
		return this;
	}
	
	this.setAction = function(action,waitSearcher,softCommit,maxSegments,expungeDeletes ){
		if( action == OPTIMIZE ) {
			this.params[OPTIMIZE] = true;
			this.params["maxSegments"] = maxSegments || 1;
		}
		else if( action == COMMIT ) {
			this.params["commit"] = true;
			this.params["softCommit"] = softCommit || false;
		}
		this.params["waitSearcher"] = waitSearcher || true;
		if (expungeDeletes)
			this.params["expungeDeletes"] = expungeDeletes;
		return this;
	}
	
	this.add = function(input){ // SolrInputDocument | List , commitWithinMs
		this.command.push(input);
		return this;
	}  
	this.deleteById = function(id,commitWithinMs){ // id | List , commitWithinMs
		this.command = { "delete" : { id : id } } ;
		if (commitWithinMs)
			this.command["commitWithin"] = commitWithinMs;
		return this;
	} 
	this.deleteByQuery = function(query,commitWithinMs){
		this.command = { "delete" : { "query" : query } };
		if (commitWithinMs)
			this.command["delete"].commitWithin = commitWithinMs;
		return this;
	}
	
}

function HTTP(strURL,qs,post) {

	var logStr = "";
	
	var st = Date.now();
	
    var xmlHttpReq = false;
    var self = this;
    if (window.XMLHttpRequest) { // Mozilla/Safari
        self.xmlHttpReq = new XMLHttpRequest(); 
    }
    else if (window.ActiveXObject) { // IE
        self.xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
    }
	
	if (qs==null)
		qs = {};
		
	if (typeof qs === "string")
	{
		qs = JSON.parse(qs);
	}
	
	if (!qs.wt)
		qs.wt = "json";
	qs.indent = "on";
	
	strURL += "?_" + Date.now();
	
	for(var key in qs) {
		strURL = strURL + "&" + key + "="+ encodeURIComponent( qs[key] );
	}
	
	var body = undefined ;
	var contenttype;
	
	var METHOD = post ? "POST" : "GET";

	logStr+= METHOD+" "+strURL ;

	if (post instanceof File)
	{
		body = post;

		logStr += "\n[ file ]";

		contenttype = "application/octect-stream";
		//strURL = strURL + "&stream.name=" + encodeURIComponent(post.name);
	}
	else if (post)
	{
		body = JSON.stringify(post,null,4) ;

		logStr+= "\n"+body;

		contenttype = "application/json";
	}
	
	
	
	try
	{
		$("body").css("cursor", "progress");
		self.xmlHttpReq.open(METHOD, strURL, false);
		if  (contenttype)
			self.xmlHttpReq.setRequestHeader('Content-Type', contenttype);
		//self.xmlHttpReq.setRequestHeader('Origin', location.origin );
		self.xmlHttpReq.send(body);
	}
	catch(e)
	{
		$("body").css("cursor", "default");
		logStr += e;
		log(logStr);
		return;
	}
	
		
	var response = undefined;

	try
	{
		logStr += "\nRESPONSE: "+(Date.now()-st)+ "ms " + self.xmlHttpReq.responseText.length + "bytes" ;
		logStr += self.xmlHttpReq.responseText;

		response = JSON.parse( self.xmlHttpReq.responseText );
		
		/*if (response.error && response.error.msg)
		{
			if (/JavaScriptException/g.test(response.error.msg))
			{
				var regex = /(.*JavaScriptException\: )|( \(.*)/g;
				var js = response.error.msg.replace(regex, "").split(":");
				if (js.length==1)
					response.msg = js[0];
				else if (js.length>1)
				{
					response.error.code = js[0];
					response.error.msg = js[1];
				}
			}	
		}*/
		
		
		//log(logStr);
		//log("***************************************************",true);
		//return response;
	}
	catch(e)
	{
		//logStr += self.xmlHttpReq.responseText;
		//log(logStr);
		//log("***************************************************",true);
		//throw { code : self.xmlHttpReq.status , message : self.xmlHttpReq.statusText };
	}
	finally
	{
		$("body").css("cursor", "default");
	}
	log(logStr);
	return response;
}










