/*
	aggiunge il metodo format alle stringe per formattare nella sintassi {0}...{1}....
*/
String.prototype.format = function() {
	a = this;
	for (k in arguments) {
		a = a.replace(new RegExp("\\{" + k + "\\}","g"), arguments[k])
	}
	a = a.replace(new RegExp("\\{\\d+\\}","g"),"");
	return a
}

String.prototype.capitalize = function(allWords) {
	if (allWords)
		return this.toLowerCase().replace(/(^\w{1})|([\s_\-]{1}\w{1})/g, match => match.toUpperCase());
	else
		return (this.charAt(0).toUpperCase() + this.toLowerCase().slice(1));
}

String.prototype.labelize = function(capitalize,allWords) {
	var s = this.replace(/([a-z])([A-Z])/g,'$1 $2').toLowerCase().replace(/[_\-]+/g, ' ');
	if (capitalize)
		return s.capitalize(allWords);
	else
		return s;
}

function parseTag(id,json){
	if (typeof id == "object" || id == null)
		return id;

	var tag = 	$(id[0]=='#'?id:"#"+id);
	if (tag.length!=1)
		return null;
	var tagName = tag.prop('tagName');
	if (tagName!='SCRIPT')
		return tag[0].outerHTML;

	var html = tag[0].innerHTML;

	if (!html)
		return html;

	if (tag.prop('type') == "application/json") {
		json = true;
	}

	if (json){
		html = $.parseHTML(html)[0].wholeText;
		html =  JSON.parse(html);
	} else if (html.indexOf("<")==-1){
		html = $('<div>').html(html).text();
	}

	return html;
}

function data(id){
	/*try{
		return JSON.parse($.parseHTML($(id[0]=='#'?id:"#"+id).text())[0].wholeText);
	}catch(e){
		return {};
	}*/
	return parseTag(id,true);
}

function template(id){
	//return $('<div>').html($(id[0]=='#'?id:"#"+id).html()).text();
	return parseTag(id, false);
}

function reportdata(arg,columns){
	var model;
	if (typeof arg == "string")
		arg = data(arg);
	if (arg.data)
		model = arg;
	else if (arg.push){
		model = {
			data : arg
		}
	} else
		model = arg; //JSON.parse($("#"+arg).html());

	model.params = model.params || {};
	if (columns){
		if (columns.push)
			model.columns = columns;
		else
			model.columns = columns.split(",");
	}

	if (model.orderBy)
		model.orderBy = model.orderBy.replace(":"," ");

	if (model.data.length>0 && !model.columns)
		model.columns = Object.keys(model.data[0]);

	if (model.columns){
		for( x in model.columns ){
			var column = model.columns[x];
			if (column.indexOf(":")>0){
				var alias = column.split(":")[0];
				column = column.split(":")[1];
				model.columns[x] = column;
				model.params["column."+column+".label"] = alias;
			}
		}
	}

	model.pageSize = model.pageSize || model.data.length;
	model.totResults = model.count || model.data.length;
	model.totPage = Math.ceil(model.totResults/model.pageSize);
	model.title = model.title || document.title;
	model.req = {};
	model.lreq = {};

	var qs = new URLSearchParams(location.search);

	qs.forEach(function(value, key) {
		model.req[key] = qs.get(key);
		model.lreq[key] = qs.getAll(key);
	});

	return model;
}

function bpmdata(arg,columns){
	return reportdata(arg,columns);
}

function bytesToSize(bytes) {
	if (bytes==null)
		return null;
	var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
	if (bytes == 0) return '0 Byte';
	var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
	return Math.round(bytes / Math.pow(1024, i), 2) + ' ' + sizes[i];
}

function changeQs(param,value,clean,onlyQs){

	if (value!=null)
		value = ""+value;

	if (value)
		value = encodeURIComponent(value);

	var newurl;
	if (!param) {
		newurl = location.href.split("?")[0]+"?";
	} else if (param[0]=='+'){
		newurl = (location.href + ( location.href.indexOf("?")>0 ? "&":"?") + param.substring(1) + "=" + (value||"")).replace(/&+/g,"&");
	} else if (param[0]=='-'){
		newurl = (location.href.replace( new RegExp("([&?])"+param.substring(1)+"="+(value||"[^&*]")+"(?:&|$)", 'gi') ,"$1")).replace(/&+/g,"&");
	} else {
		newurl = (location.href.replace( new RegExp("([&?])"+param+"=[^&]*", 'gi') ,"$1") + (value? ( (location.href.indexOf("?")>0?"&":"?") + param + "=" + value) : "")).replace(/&+/g,"&");
	}

	if (clean){
		newurl = newurl.replace( new RegExp("([&?])"+clean+"=[^&]*", 'gi') ,"$1").replace(/&+/g,"&");
	}

	if (document && document.baseURI)
		newurl = newurl.substring(document.baseURI.length);

	if (onlyQs)
		return newurl.split("?")[1];
	return newurl;
}

function cloneObject(obj){
	return JSON.parse(JSON.stringify(obj));
}
/*
function PUT(url,payload,done,error) {
	if (!url)
		throw "null url";

	axios.put(url , payload, {headers: {
		"content-type":"application/json",
		"accept":"application/json"
	} } )
		.then(function(response){
			if (done)
				done(response.data);
			else
				alert(response.data || "OK");
		})
		.catch(function(err){
			if (error)
				error(err);
			else
				openError(err);
		});
}

function PATCH(url,payload,done,error) {
	if (!url)
		throw "null url";

	axios.patch(url , payload, {headers: {"accept":"application/json"} } )
		.then(function(response){
			if (done)
				done(response.data);
			else
				alert(response.data || "OK");
		})
		.catch(function(err){
			if (error)
				error(err);
			else
				openError(err);
		});
}

function GET(url,done,error) {
	if (!url)
		throw "null url";

	axios.get(url , {headers: {"accept":"application/json"} } )
		.then(function(response){
			if (done)
				done(response.data);
			else
				alert(response.data || "OK");
		})
		.catch(function(err){
			if (error)
				error(err);
			else
				openError(err);
		});
}

function DELETE(url,done,error) {
	if (!url)
		throw "null url";

	axios.delete(url , {headers: {"accept":"application/json"} } )
		.then(function(response){
			if (done)
				done(response.data);
			else
				alert(response.data || "OK");
		})
		.catch(function(err){
			if (error)
				error(err);
			else
				openError(err);
		});
}

function POST(url,payload,done,error) {
	if (!url)
		throw "null url";

	axios.post(url , payload, {headers: {
		"content-type":"application/json",
		"accept":"application/json"
	} } )
		.then(function(response){
			if (done)
				done(response.data);
			else
				alert(response.data || "OK");
		})
		.catch(function(err){
			if (error)
				error(err);
			else{
				openError(err);
			}

		});
}

 */