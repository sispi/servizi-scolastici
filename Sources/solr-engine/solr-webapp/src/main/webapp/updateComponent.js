function UpdateComponent(server,ticket)
{
	this.ticket = ticket;
	this.core = new SolrServer(server);
	
	this.create = create;
	this.update = update;
	/*//this.renameNode = renameNode;
	this.updateACL = updateACL;
	this.moveNode = moveNode;
	this.lock = lock;
	this.unlock = unlock;
	this.templock = templock;
	this.deleteById = deleteById;
	this.copyVersion = copyVersion;
	this.uploadContent = uploadContent;
	this.deleteVersion = deleteVersion;
	//this.deleteByQuery = deleteByQuery;
	this.updateByQuery = updateByQuery;
	//this.eraseByQuery = eraseByQuery;
	this.undeleteById = undeleteById;
	//this.undeleteByQuery = undeleteByQuery;
	this.indexContent = indexContent;*/
	
}

/*function copyVersion ( id )
{
	var response = this.core.retrieve( { qt: repositoryHandler , id: id , action:"copy" , ticket: this.ticket } );
	
	if (response.error)
		throw response.error;
	
	return response.versions;
}

function deleteVersion( id , version )
{
	var response = this.core.retrieve( { qt: repositoryHandler , id: id, action:"delete" , ticket: this.ticket, version: version||"*" });
	
	if (response.error)
		throw response.error;
	
	return response;
}

function indexContent( id )
{
	var response = this.core.retrieve( { qt: retrieveHandler,  id: id , ticket: this.ticket } );
	
	if (response.error)
		throw response.error;
	
	var path = response.fspath;
	
	var request = {id:id , "stream.file": path , ticket:this.ticket, qt: indexHandler};
	
	var response = this.core.query( request );
	
	if (response.error)
		throw response.error;
	
	return response;
}

function uploadContent( streamref )
{
	var request = { ticket:this.ticket , qt: uploadHandler }; 
	
	var response;
	
	if (streamref instanceof File)
	{
		request["stream.name"] = streamref.name;
		response = this.core.post( request, streamref );
	}
	else
	{
		if (streamref.indexOf("http://")==0)
			request["stream.url"] = streamref;
		else
			request["stream.file"] = streamref;
			
		response = this.core.query( request );
	}
		
	if (response.error)
		throw response.error;
	
	return response;
}*/

function create( inputdoc , params )
{
	var request = new UpdateRequest( createHandler, this.ticket );
	request.add(inputdoc);
	request.setParams(params);
	return this.core.request(request);
	
	/*params = params || {};
	//params.id=id;
	params.ticket=this.ticket;
	params.qt = createHandler;
	//if (params.qt)
	//	request.handler = params.qt;
	
	request.setParams(params);
	

	//if (params.commit != false)
	//	request.setAction(COMMIT,true,true);
	
	//request.setAction(COMMIT,true,true);
	
	//if (index)
	//	request.setParam("index",true);
		
	request.add(inputdoc);
	
	var response = this.core.request(request);
	
	if (response.error)
		throw response.error;
		
	return response;*/
	
	//return server.query( { id:response.id , qt:config.getHandler, ticket:this.ticket }).doc;
}

function update( updatenode , params )
{	
	var request = new UpdateRequest( updateHandler , this.ticket );
	request.add(updatenode);
	request.setParams(params);

	alert(request.params.newVersion);
	
	return this.core.request(request);
	
	/*params = params || {};
	//params.id=id;
	params.ticket=this.ticket;
	params.qt = updateHandler;
	//if (params.qt)
	//	request.handler = params.qt;
	
	request.setParams(params);*/
	
	//if (params.commit != false)
	//	request.setAction(COMMIT,true,true);
		
	/*if (updatenode)
	{
		updatenode.id = id;
		request.add(updatenode);
	}
	else
	{
		params.id = id;
	}*/

	/*request.add(updatenode);
	
	var response = this.core.request(request);
	
	if (response.error)
		throw response.error;
		
	return response;*/
	
	//return server.query( { id:id , qt:config.getHandler, ticket:this.ticket }).doc;
}

/*function deleteById(id)
{
	return this.updateNode( id, { enabled : false } );
}

function undeleteById(id)
{
	return this.updateNode( id, { enabled : true } );
}*/

/*function undeleteById(id)
{
	return this.updateByQuery("id:"+id+" parentIds:"+id,null,{undelete:true});
}

function undeleteByQuery(q,params)
{
	params = params || {};
	params.undelete = true;
	return this.updateByQuery(q,null,params);
}

function deleteByQuery(q,params)
{
	return this.updateByQuery(q,{parent:"@"},params);
}*/

/*function eraseByQuery(q,params)
{
	params = params || {};
	params["delete"] = true;
	return this.updateByQuery(q,null,params);
}*/

/*function updateByQuery(q,updatenode,params)
{
	Query = params || {};
	
	for (k in updatenode)
		Query["set."+k] = updatenode[k];
	
	Query.q = q;
	Query.qt = updatebyqueryHandler ;
	Query.ticket = this.ticket;
	Query.commit = true;
	Query.softCommit = true;
	
	var response = this.core.query(Query);
	
	if (response.error)
		throw response.error;
	
	return response;
}*/
