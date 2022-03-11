function processError(error,callback){
	if (callback)
		callback(error);
	else
		showError(error);
}

/*

url (*):	url assoluto o relativo da invocare (deve supportare i CORS)

target (facoltativo): elemento html dove verr� posizionato l'html nuovo e deve iniziare per # o @:
	se inizia per # viene aggiornato l'url del browser
	se inizia per @ non viene aggiornato l'url del browser
	se parametro assente oppure � @modal, @modal-sm o @modal-lg viene aperto come modal ed in questo caso la richiesta pu� contenere una intera modal o solo il body
	per altri valori la pagina viene aperta normalmente

source : element html prelevato dall'url in modo specifico, altrimenti viene presa l'intera risposta html

data : pu� essere:
	1) object 	=> la richiesta avviene in POST con application/json
	2) FormData => la richiesta avviene in POST con multipart/form-data
	3) stringa 	=> la richiesta avviene in POST con application/x-www-form-urlencoded

L'url � invocato aggiungendo l'header X -fragment:true che causa la produzione del solo body senza master pageX

Se l'url contiene un anchor e l'anchor esiste , viene scrollato

*/
function openUrl( url, target,data,  source){
	/* url assoluto o relativo da invocare
	* se inizia per # aggiorna l'history altrimenti @ per non aggiornare. null, @modal, @modal-lg o @modal-sm per modale
	* object, FormData o querystring per invocare in POST
	* eventuale cssQuery della porzione della sorgente
	* Es. openUrl("view/userinfo")
	 */

	$('#modal button,#modal .modal-header,#modal .modal-footer').hide();
	$("#modal .modal-dialog").removeClass("modal-lg").removeClass("modal-sm");

	if (typeof target == "object"){
		var tmp = data;
		data = target;
		target = tmp;
	}

	if (target && !target.match(/^[#@\$]/) ){
		window.location.href = url;
		return;
	}

	var updateUrl = target && !target.startsWith("@");
	var parts = url.split("#");
	var anchor = parts[1];

	if (!window.history.pushState || url==window.location.href)
		updateUrl = false;

	if ( parts[0] == window.location.href.split("#")[0]){

		if (anchor){
			alink = $('a[href="#' + anchor + '"]');
			if (alink.length>0)
				alink.get(0).scrollIntoView();
		}

		if (updateUrl)
			window.history.pushState({path:url},'',url);

		return;
	}

	url = url.replace("&FORCEREFRESH","");

	var isModal = (!target || target=="@modal" || target=="@modal-xl" || target=="@modal-lg" || target=="@modal-sm" );

	var headers = { "X-fragment" : (target=="#main"||target=="@main") ? "subheader+body" : "body" };

	var process = function(response){

		var html;

		if (source){
			html = $('<output>').append($.parseHTML(response.data)).find(source).parent().html();
		} else {
			html = response.data;
		}

		var elem;

		if (isModal){

			showModal(html,target,url);

		} else {

			if (updateUrl){
				window.history.pushState({path:url},'',url);
			}


			elem = $("#"+target.substring(1));
			elem.html(html);
		}

		initDOM(elem);

		if (anchor){
			alink = $('a[href="#' + anchor + '"]');
			if (alink.length>0)
				alink.get(0).scrollIntoView();
		}

	}

	var error = function(err){
		//console.log("error:"+err);

		/*if (err.response && err.response.headers["error-page"] == "true"){
			process(err.response);
			return;
		}

		alert(err);*/
		showError(err);

		/*if (err.request && err.request.responseURL && err.request.responseURL.endsWith("/error")){
			//alert("non gestito:"+err.message);
			process(err.response);
			return;
		}
		openError(err);*/
	}

	if (data instanceof FormData){
		headers['content-type']='multipart/form-data';
	} else {
		headers['content-type']='application/json';
	}

	//url = resolveBaseUrl(url);

	if (data==null){
		axios.get(url, { headers : headers } ).then(process).catch(error);
	} else {
		axios.post(url, data, { headers : headers } ).then(process).catch(error);
	}

}

function showModal(html,target,url){

	$("#modal .modal-body").html("");

	if ($("#modal").is(":visible")){
		$('#modal').modal('hide');
		$('#modal').on('hidden.bs.modal', function (e) {
			$('#modal').unbind("hidden.bs.modal");
			$('#modal').hide();
			showModal(html,target,url);
		})
		return;
	}

	//modal
	var mdl;
	if (typeof html == "string"){
		mdl = $('<output>').append($.parseHTML(html));
	} else {
		mdl = $('<output>').append($(html));
	}

	if (mdl.find(".modal").length>0){

		elem = $("#temp");
		if (elem.length==0) {
			elem = $("<div id='temp'></div>");
			elem.appendTo(document.body);
		}
		elem.html(html);

		elem.find(".modal").modal();

	} else {

		elem = $("#modal");


		var header = mdl.find("header");
		var footer = mdl.find("footer");
		var title = mdl.find("title,.page-title");

		if (footer.length==0){
			$('#modal .modal-footer').show();
			$('#modal .modal-close').show();
			$('#modal .modal-ok').hide();
			$('#modal .modal-clear').hide();

		} else {
			$('#modal .modal-footer').hide();
		}

		if (header.length==0){
			$("#modal .modal-header").show();
			$("#modal .modal-header .close").show();
			$("#modal .modal-title").text(title.text());
			title.remove();
		} else {
			$("#modal .modal-header").hide();
		}

		elem.find(".modal-body").html(html);

		if (target && target.startsWith("@modal-"))
			$("#modal .modal-dialog").addClass(target.substring(1));

		elem.modal();
	}
	if (url){
		elem.data("url",url);
		elem.data("target",target);
	} else {
		elem.data("url","");
		elem.data("target","");
	}
	return elem;
}

function openPage( url ){
	openUrl(url,"#main");
}

function openModal(url){
	return openUrl(url,'@target-xl');
}

function showError(error){

	var payload;

	if (error.isAxiosError){

		payload = {};

		if (error.response){
			if (error.response.data){
				if (typeof error.response.data == "object")
					payload = error.response.data;
				else {
					try{
						payload.message = $("<div/>").html(error.response.data).find("#main").text().replace(/\s+/g," ").trim();
					} catch (e) {

					}
				}
			}
		}

		if (!payload.message)
			payload.message = error.message || "Errore non specificato";

		if (!payload.code){
			if (error.response)
				payload.code = "H"+(error.response.status||"4xx");
			else
				payload.code = "H500";
		}

		if (!payload.status){
			if (error.response)
				payload.status = error.response.status||"400";
			else
				payload.status = "500";
		}

		if (!payload.url)
			payload.url = error.config ? error.config.url : null;

	} else if (typeof error == "object") {

		payload = error;
		payload.code = payload.code || "H500";
		payload.message = payload.message || "Errore non specificato";
		payload.status = payload.status || "500";

	} else {
		payload = {};
		payload.code = "H500";
		payload.message = "" + error;
		payload.status = "500";
	}

	payload.status = ""+payload.status;

	var cls = "danger";
	if (payload.status[0]=='4')
		cls = "warning";

	var template = `

	<title>Errore nella richiesta</title>
		
	<div class="pl-3 pr-3" >
		<p class="row alert alert-`+cls+`" >
			<span id="message" class="col-10"></span>
			<span class="col-2">
				<a href="#" style="float: right" class="p-0 btn btn-link collapsed" data-toggle="collapse" data-target="#detailErr">Mostra dettagli</a>
			</span>
			<p id="retry" style="display: none" >
				Puoi riprovare ad eseguire la richiesta a questo link:
				<a href="" class="" >Riprova</a>
			</p>
		</p>
		<div id="detailErr" class="collapse pb-3" >
			<div id="code" ><b>Codice:</b>&nbsp;<text></text></div>
			<div id="type" ><b>Tipo:</b>&nbsp;<text></text></div>
			<div id="status" ><b>Codice HTTP:</b>&nbsp;<text></text></div>
			<div id="url" ><b>Url richiesto:</b>&nbsp;<text></text></div>
			<div id="details" style="display: none" >
				<br/>
				<b>Dettagli eccezione:</b>
				<pre style="background-color:lightyellow" class="p-2"  ></pre>
			</div>
		</div>
		
	</div>
	<footer/>

	`;

	var el = $(template);

	el.find("#message").text(payload.message);

	/*if (payload.retryable && payload.url){
		el.find("#retry").show();
		el.find("#retry a").attr("href", payload.url);
	}*/

	if (payload.code)
		el.find("#code text").text(payload.code);
	if (payload.type)
		el.find("#type text").text(payload.type);
	if (payload.status)
		el.find("#status text").text(payload.status);
	if (payload.url)
		el.find("#url text").text(payload.url);

	if (payload.details){
		var details = "";
		for( x in payload.details ){
			details += x +":"+ payload.details[x] + "<br/>";
		}
		if (details){
			el.find("#details").show();
			el.find("#details pre").html(details);
		}
	}

	console.log(payload);

	showModal(el, "@modal-lg");

	//openUrl("/errorModal","@modal-lg",payload);
}

function confirm(msg,callback) {
	/* apre modale con messaggio e al confirm chiama la callback */
	/* Es. confirm("sei sicuro?", () => alert('confermato')) */

	$("#alert .modal-dialog").removeClass("modal-lg").removeClass("modal-sm");
	$('#alert button,#alert .modal-header,#alert .modal-footer').hide();

	var elem;
	var title;
	var e = msg;

	if (e.currentTarget){
		elem = $(e.currentTarget);

		if (elem.hasClass('confirmed')){
			elem.removeClass('confirmed');
			return true;
		}

		e.stopImmediatePropagation();
		e.stopPropagation();
		e.preventDefault();
	} else if (typeof e == "string"){
		elem = $("<a/>");
		elem.data("title",e);
	} else {
		elem = $(e);
	}

	var title = elem.data("title") || elem.attr("title") || "Confermare";
	$("#alert .modal-body").text(title);

	$('#alert .modal-ok').show();
	$('#alert .modal-close').show();
	$('#alert .modal-footer').show();

	$("#alert .modal-dialog").removeClass("modal-lg").removeClass("modal-sm");
	$('#alert').modal({show:true});

	$('#alert .modal-close').unbind("click");
	$('#alert .modal-close').on('click', function() {
		elem.removeClass('confirmed');
	});

	$('#alert .modal-ok').unbind("click");
	$('#alert .modal-ok').on('click', function() {

		$('#alert').modal('hide');
		elem.addClass('confirmed');

		if (callback){
			callback();
		} else if (elem[0].click){
			elem[0].click();
		} else {
			var event = new Event('click');
			event.confirmed = true;

			elem[0].dispatchEvent(event);
		}
	});

	return false;
}

function prompt(data,callback) {
	/* apre modale con form di input e all'ok chiama la callback */
	/* Se 'data' contiene chiave url, apre l'url */
	/* Se 'data' contiene chiave title, lo usa come titolo */
	/* Ogni altra chiave di 'data' è usata come input */
	/* Es. prompt( { 'info' : '' } , (form) => alert(form.info)) */

	$("#alert .modal-dialog").removeClass("modal-lg").removeClass("modal-sm");
	$('#alert button,#alert .modal-header,#alert .modal-footer').hide();

	var elem;
	var title;
	var target;
	var e = data;
	var onlyValue = false;

	if (e.currentTarget){
		elem = $(e.currentTarget);

		if (elem.hasClass('confirmed')){
			elem.removeClass('confirmed');
			return true;
		}

		e.stopImmediatePropagation();
		e.stopPropagation();
		e.preventDefault();
	} else if (typeof e == "string"){
		elem = $("<a/>");
		elem[0].dataset["title"] = e;
		elem[0].dataset["value"] = "";
		onlyValue = true;
	} else {
		elem = $("<a/>");
		for( k in e )
			elem[0].dataset[k] = e[k];
	}

	target = elem[0];

	var url = target.dataset.url;

	if (url){

		openUrl(url,'@modal');

	} else {

		$("#alert .modal-body").html("");

		var fields = target.dataset;
		for( field in target.dataset ){

			if (field=='title')
				continue;

			var labelField = field;
			if (onlyValue)
				labelField = '';
			labelField = labelField.replace("_", " ");

			$("#alert .modal-body").append("<label style='width:100%'></label>"+labelField+"<input style='width:100%' id='data-"+field+"' name='"+field+"' type='text' value='"+(target.dataset[field]||"")+"' >")
		}

		$('#alert').modal({show:true});
	}


	var title = elem.data("title") || elem.attr("title") || "Inserire i parametri richiesti";

	$("#alert .modal-title").text(title);
	$("#alert .modal-header").show();
	$('#alert .modal-footer').show();
	$('#alert .modal-close').show();


	$('#alert .modal-close').unbind("click");
	$('#alert .modal-close').on('click', function() {
		elem.removeClass('confirmed');
	});

	$('#alert .modal-clear').show();
	$('#alert .modal-clear').unbind("click");
	$('#alert .modal-clear').on('click', function() {
		$("#alert .modal-body input[name]").each(function(){
			$(this).val("");
		});
	});

	$('#alert .modal-ok').show();
	$('#alert .modal-ok').unbind("click");
	$('#alert .modal-ok').on('click', function() {

		var form = $("#alert form")[0];

		if (form && !form.checkValidity()){
			$("<button/>").appendTo(form).click().remove();
			return;
		}

		$('#alert').modal('hide');
		elem.addClass('confirmed');

		var fields = $("#alert .modal-body input[name]");

		for( var i=0; i<fields.length; i++ ){

			var field = $(fields[i]);
			var name = field.attr('name');
			var val = field.val();

			elem.data(name,val);
			elem.attr("data-"+name,val);

			if (name=='value')
				elem.val(val);
		}

		if (callback){
			callback(onlyValue ? target.value : target.dataset);
		} else if (target.click){
			target.click();
		} else {
			var event = new Event('click');
			event.confirmed = true;

			target.dispatchEvent(event);
		}
	});

}

function getModal(id){
	var modal = $("#"+id);
	if (modal.length==0){
		var html = `
		<div class="modal fade" name="auto-modal" style="z-index:1000000" id="`+id+`" tabindex="-1" role="dialog" aria-labelledby="`+id+`-title" aria-hidden="true">
		  <div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title"></h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					  <span aria-hidden="true">&times;</span>
					</button>
				 </div>
				 <div class="modal-body"></div>
				 <div class="modal-footer">
					<button type="button" class="modal-close btn btn-secondary" data-dismiss="modal">Chiudi</button>
					<button type="button" class="modal-ok btn btn-primary">OK</button>
					<button type="button" class="modal-clear btn btn-info">Pulisci</button>
				 </div>
			</div>
		  </div>
		</div>`;
		$('body').append(html);
		modal = $("#"+id);
	}

	return modal;
}

/*
inizializza i controlli con classe 'confirm' per aprire una dialog di conferma
E' possibile specificare il contenuto con l'attributo 'title' o 'data-title'
Solo dopo la conferma l'evento onclick del controllo contiene 'confirmed' a true

inizializza i controlli con class 'prompt' per aprire una dialog di inserimento dati
E' possibile specificare il title con l'attributo 'title' o 'data-title'
Ogni attributo di tipo 'data-...' � prodotto con input box e poi aggiornato con l'inserimento
Solo dopo la compilazione  l'evento onclick del controllo contiene 'confirmed' a true

viene sostituita la funzione nativa 'alert' con la modal di bootstrap
 */
function initAlerts(root){

	/*
		text: testo da visualizzare
		delay: chiusura automatica dopo il numero di ms specificato
	*/

	if (root[0] == document.body){

		getModal("modal");
		getModal("alert");
		var backdrop = $("#backdrop");

		if (backdrop.length==0){
			var html = `
				<div class="modal" data-backdrop="static" data-keyboard="false" data-focus="false" style="z-index:1000000" id="backdrop" tabindex="-1" role="dialog">
					<div class="modal-dialog" style="top: 40%;text-align: center;" role="document">
						<i class="fa fa-spinner fa-pulse fa-3x fa-fw"></i>
						<span class="sr-only">Loading...</span>
					</div>
				</div>`;
			$('body').append(html);
		}

		window.alert = function(text,delay){

			if (typeof text == "object"){
				text = JSON.stringify(text,null,4);
			}

			$("#modal .modal-dialog").removeClass("modal-lg").removeClass("modal-sm");
			$('#modal button,#modal .modal-header,#modal .modal-footer').hide();

			if ($("#modal").is(":visible")){
					$('#modal').modal('hide');
					$('#modal').on('hidden.bs.modal', function (e) {
					$('#modal').unbind("hidden.bs.modal");
					$('#modal').hide();
					alert(text||"");
				})
				return;
			}

			$('#modal .modal-close').show();
			$('#modal .modal-footer').show();

			$("#modal .modal-body").html("<pre>"+text||""+"</pre>");

			$("#modal .modal-dialog").removeClass("modal-lg").removeClass("modal-sm");

			$('#modal').modal({show:true});

			if (delay){
				setTimeout(function() {
				   $("#modal").modal('hide');
			   }, delay);
			}
		}
	}


    //root.find(".confirm").unbind("click");
    //root.find(".prompt").unbind("click");

    root.find(".confirm").bindUp("click",confirm);

    root.find(".prompt").bindUp("click",prompt );
}

/*
	funzione che assicura che l'evento sia attachato in testa
*/
$.fn.bindUp = function(type, fn) {
	this.each(function() {
		$(this).bind(type, fn);
		var events = $(this).data("events") || $._data(this, "events");

		if (events){

			var evt = events[type];

			var def = this["on"+type];
			if (def){
				this.removeAttribute("on"+type)
				$(this).bind(type, def);


				if (evt && evt.length>1){
					evt.splice(0, 0, evt.pop());
				}
			}

			if (evt && evt.length>1){
				evt.splice(0, 0, evt.pop());
			}
		} else {
			console.error("events not found!!!");
		}
	});
};

function initSelect2(index,elem) {

	function templateResult (state,arg) {
		if (!state.id) {
			return state.text;
		}
		if (state.id!=state.text){
			return state.text+" ("+state.id+")";
		} else {
			return state.text;
		}
	}

	function templateSelection (state) {
		if (!state.id) {
			return state.text;
		}
		var display;
		if (state.id!=state.text){
			display = state.text+" ("+state.id+")";
		} else {
			display = state.text;
		}
		if (self.allowEdit!='true')
			return display;
		return $("<span onclick='changeTag(\""+state.id+"\")'>"+display+"</span>");
	};

	elem.setValue = function (draft) {
		var obj = $(this);
		var v = draft[obj.attr("name")];
		if (v){
			if (obj.find("option[value='" + v + "']").length) {
				obj.val(v).trigger('change');
			} else {
				var newOption = new Option(v, v, true, true);
				obj.append(newOption).trigger('change');
			}
		}
	}

	var el = $(elem);

	var _url = elem.url || el.data("url");
	var _processResults = elem.processResults || el.data("process-results");

	var _urlF = el.data("process-url");

	if (typeof window[_url] == "function")
		_url = window[_url];
	if (typeof window[_data] == "function")
		_data = window[_data];
	if (typeof window[_processResults] == "function")
		_processResults = window[_processResults];

	var _data = null;

	if (_urlF!=null){
		_url = function(params){
			return eval(_urlF);
		}
	} else if (typeof _url == "string"){

		var url = _url;
		_url = _url.replace("...","");

		var idx = url.lastIndexOf("&");
		if (idx==-1)
			idx = url.lastIndexOf("?");
		if (idx>=0){
			var _url0 = _url.substring(idx+1);

			_url = _url.substring(0,idx);

			_data = function(params) {
				var term = params.term || "__NULL__";
				if (term.push){
					return _url0 + "("+term.join(" ")+")";
				} else
					return _url0 + (term+"*").replace("**","*");
			}
		}
	}

	if (_data==null){
		_data = function(params) {
			return "";
		}
	}

	var _processResults2 = _processResults;

	if (typeof _processResults == "string"){
		_processResults = function(data){
			var results = eval(_processResults2);

			return {results : results };
		}
	}

	if (_processResults==null){
		_processResults = function (data) {
			var data= $.map(data.data, function (item) {
				item.id = item.id || item.sid;
				return item;
			});

			data.results = data;
			return data;
		}
	}

	var config = {
		templateSelection: templateSelection,
		templateResult: templateResult
	};

	if (_url){
		config.ajax = {
			dataType: 'json',
			url : _url,
			data : _data,
			processResults : _processResults
		}
	}

	el.select2(config);

	return el;
}

function initDOM(root){

	if (root==null)
		return;

	root = $(root);

	root.find("select.select2").each(initSelect2);

	root.find("div[search-object]").each( function(index,elem) {
		var el = $(elem);
		var maxoccurs = el.data("maxoccurs") || "1";
		var prefix = el.data("prefix");
		var label = el.data("label");

		var select2 = $("<select multiple='multiple' ></select>")[0];

		$(select2).attr("style", el.attr("style"));
		$(select2).attr("class", el.attr("class") + " select2 form-value-target");
		$(select2).attr("data-placeholder",label);
		$(select2).attr("name",prefix);

		var getResults = function(data) {
			return {results:  $.map(data.data, (item) => {
					item.text = item.name;
					item.id = item.docerId || item.id;
					return item;
				})};
		}

		select2.processResults = getResults;

		var url =function(params) {
			return  "/docer/v1/fascicoli?q=text:"+params.term+'*';
		};

		//maximum-selection-length="1" multiple="true"

		$(select2).attr("data-maximum-selection-length",maxoccurs);

		select2.url = url;

		el.replaceWith(select2);

		initSelect2(index,select2);
	})

	root.find("input.auto-completion").each( function(index,elem) {
		var el = $(elem);
		var multiple = el.attr("multiple");
		var name = el.attr("name");
		var source = el.data("source");
		var part;
		var placeholder = el.attr("placeholder") || "seleziona";

		var select2 = $("<select multiple='multiple' class='select2'></select>")[0];

		$(select2).attr("name",name);
		$(select2).attr("style", el.attr("style"));
		$(select2).attr("class", el.attr("class") + " select2 form-value-target");
		$(select2).attr("data-placeholder",placeholder);

		var getResults = function(data) {
			return {results:  $.map(data.data, (item) => {
					item.text = item.name;
					item.id = item.docerId || item.id;
					return item;
				})};
		}

		select2.processResults = getResults;

		var userGroups = userInfo ? userInfo.groups.join(' ') : '';

		//'/docer/v1/solr/select?fq=type:user&q=text:'

		switch(source){
			case 'users':
				part = "/utenti?";
				break;

			case 'groups':
				part = "/gruppi?";
				break;

			case 'gruppistruttura':
				part = "/gruppi?fq=GRUPPO_STRUTTURA=true";
				break;

			case 'gruppifunzionali':
				part = "/gruppi?fq=GRUPPO_STRUTTURA:false";
				break;

			case 'usergroups':
				part = "/gruppi?fq=GROUP_ID:("+userGroups+")";
				break;

			case 'usersroles':
				part = "/gruppi?fq=GRUPPO_STRUTTURA:false&fq=GROUP_ID:("+userGroups+")";
				break;

			case 'membershipGroups':
				part = "/gruppi?fq=GRUPPO_STRUTTURA:true&fq=GROUP_ID:("+userGroups+")";
				break;

			case 'tipologie':
				part = "/documenti/tipologie?";

				select2.processResults = function(data) {
					var results = [];
					for(x in data){
						results.push({
							id: x,
							text: data[x]
						})
					}
					return {
						results: results
					}
				}
				break;

			default:
				part = "xxx";
		}


		var url =function(params) {
			return  "/docer/v1" + part + '&q=text:'+params.term+'*';
		};

		//maximum-selection-length="1" multiple="true"

		if (!multiple)
			$(select2).attr("data-maximum-selection-length","1");

		select2.url = url;

		el.replaceWith(select2);

		initSelect2(index,select2);
	})

	/* tutti gli ace editors */
	root.find(".ace-editor").each(function() {
		var textarea = $(this);
		var mode = textarea.data('lang') || 'text';
		var theme = textarea.data('theme') || 'dawn';
		var editDiv = $('<div>', {
			position: 'absolute',
			width: "100%",
			height: textarea.height(),
			'class': textarea.attr('class')
		}).insertBefore(textarea);
		textarea.css('display', 'none');
		var editor = ace.edit(editDiv[0]);
		editor.renderer.setShowGutter(textarea.data('gutter') || true);
		editor.getSession().setValue(textarea.val());
		editor.getSession().setMode("ace/mode/" + mode);
		editor.setTheme("ace/theme/"+ theme);

		if(textarea.prop('readonly')){
			editor.setReadOnly(true);
		} else {
			editor.on('change', () => {
				textarea.val(editor.getSession().getValue());
			});
		}

		// copy back to textarea on form submit...
		/*textarea.closest('form').submit(function() {
			textarea.val(editor.getSession().getValue());
		})*/
	});

	/* tutti gli anchor senza baseUrl vengono riscritti per non cadere nel base url di pagina */
	root.find("a[href^='#']").each(function() {
		if (!$(this).attr('data-toggle')){
			var path = window.location.href.split('#')[0];
			$(this).attr('href',path+$(this).attr('href'));
		}
	});

	/* viene agganciata l'apertura ajax dei contenuti a tutti gli i tag 'a' con target che inizia per # o @ */
	root.find("a[target^='#'],a[target^='@'],a[target^='$']").on('click', function () {
        var target = $(this).attr('target');

        if ( target.startsWith("#") && $(target)[0] && $(target)[0].tagName.toLowerCase() == 'a')
        	return true;

        if (target.length==1)
        	target = target+"main";

        var url = $(this).attr('href');
		var source = $(this).data('source');

        openUrl(url,target,source);

        return false;
    });

	/* viene agganciata l'apertura ajax dei contenuti a tutti gli i tag 'a' con target che inizia per # o @ */
	root.find("button[formtarget^='#'],button[formtarget^='@']").each(function () {

		var target = $(this).attr('formtarget');

		if (!target)
			return;

		var formId = $(this).attr("form");
		var form = formId ? $("#"+formId) : $(this).closest("form");

        var source = $(this).data('source');
		var url = form.attr('action');
		var method = (form.attr('method') || 'get').toLowerCase();

		if (method=='post') {
			form.on("submit",function(e) {
				e.preventDefault();

				var enctype = (form.attr('enctype') || "application/x-www-form-urlencoded").toLowerCase();

				var data;
				if (enctype=="application/x-www-form-urlencoded"){
					data = form.serialize();
				} else if (enctype=="multipart/form-data"){
					data = new FormData(form[0]);
				} else { /* application/json */
					var pairs = form.serializeArray();
					var data = {};

					for(idx in pairs){
						data[pairs[idx].name] = pairs[idx].value;
					}
				}

				openUrl(url,target,source,data);

				return false;
			});
		} else {
			form.on("submit",function(e) {
				e.preventDefault();
				url = url + "?" + form.serialize();
				openUrl(url,target,source);
				return false;
			});
		}

        return false;
    });

	initAlerts(root);

	initFiles(root);

	if (root[0] == document.body){
		/* forza reload nel back */
		window.onpopstate = function(event) {
			if (event.state!=null){
				window.location.reload();
			}
		};

		/* ricarica solo il body */
		location.refresh = function(){
			if ($("#modal").is(':visible')){
				var url = $("#modal").data("url");
				var target = $("#modal").data("target");
				if (url && target){
					openUrl(url,target);
					return;
				}
			}
			openPage(location.href+"&FORCEREFRESH");
			console.log("refreshed");
		}
	}
}

function initFiles(root){

	$(root).find(".file2").each( function(index,elem){

		var template =  `

    <div>
            <input style="opacity: 0.0;filter: alpha(opacity=0); width:1px; height:1px; overflow: hidden; position: absolute;" class="input-file" type="file"  >
            <label role="button" class="w-100 label m-0">
                <i class="bi bi-upload"></i>&nbsp;Clicca qui o trascina per aggiungere un file
            </label>
    </div>`;

		var el = $(template);



		el.attr("style", $(this).attr("style"));
		el.attr("class", "file-component p-2 border border-secondary rounded " + $(this).attr("class"));
		//var outcolor = el.css("background-color");

		//var incolor = "rgba("+outcolor+", 0.8)";

		var id = $(this).attr("id");
		var name = $(this).attr("name");
		var mlt = $(this).attr("multiple");
		var identity;
		var required = $(this).attr("required") ? true : false;
		var multiple = mlt ? true : false;
		var maxLength = $(this).data("max-length");
		var basepath = 'upload/path_'+new Date().getTime();
		var opacity = 0.8;

		if (!id){
			identity = 'file_'+(new Date().getTime()+Math.floor(Math.random() * 100000));
		} else {
			identity = id;
		}
		el.attr("id",identity+"_div");

		el.on("dragleave", function() {
			el.css("opacity", opacity);
		});
		el.on("dragover", function() {
			el.css("opacity", 1);
		});

		var label = $(el.find(".label"));
		label.on("mouseover", function() {
			el.css("opacity", opacity);
		});
		label.on("mouseleave", function() {
			el.css("opacity", 1);
		});
		label.attr("for", identity+"_inputfile");

		var inputFile = $(el.find(".input-file"));

		inputFile.attr("id" , identity+"_inputfile");

		//inputHidden.attr("required" , required);
		el.attr("id" , identity);

		if (multiple)
			inputFile.attr("multiple" , "multiple");

		var slot = `
			<div class="list">
				<a class="link"></a>
				<input class="hidden" type="hidden" />
				<button class="p-0 pb-1 btn btn-link" data-toggle="tooltip" data-placement="top" title="Elimina"><i class="fas fa-times text-danger"></i></button>
			</div>`;

		var refresh_value = function() {
			var values = [];
			el.find(".list a").each(function (index, elem) {
				values.push($(this).attr("href"));
			});
			var v;
			if (values.length==0)
				v = multiple? [] : null;
			else if (multiple)
				v = values;
			else
				v = values[0];

			el.data("value",v);
			el.val(v);

			console.log(el.val());
		}

		var onchange = function(e) {
			el.css("opacity", 1);

			let files = (e.dataTransfer||e.target).files;
			if(!files)
				return false;

			for( x in files){
				if (files[x].size == 0){
					alert(files[x].name + " è vuoto");
					return;
				}
				if (maxLength){
					if (files[x].size > Number(maxLength)){
						alert(files[x].name + " supera il limite di "+maxLength+" bytes");
						return;
					}
				}
			}

			var fileClient = new FileClient();
			fileClient.createMulti(files,basepath, (data)=> {
				var index = 0;
				for (key in data) {
					var f = {
						name: data[key],
						url: "/docer/v1/files/"+key,
						size: files[index++].size,
					}
					var fel = $(slot);
					var link = fel.find(".link");
					var btn = fel.find("button");
					var inputHidden = fel.find(".hidden");

					inputHidden.attr("name",name);
					inputHidden.val(f.url);

					link.attr("href",f.url);
					link.text(f.name);

					btn.on("click", function() {
						$(this).parent().remove();
						refresh_value();
					})

					if(!multiple)
						el.find(".list").remove();

					el.append(fel);
					refresh_value();
				}
			});
			return false;
		}

		el.on("drop", onchange);
		inputFile.on("change", onchange );

		$(this).replaceWith(el);

	});

}

function on_error(error){
	$('#backdrop').modal('hide');
}

function on_finish(response){
	$('#backdrop').modal('hide');
}

function on_start(request){
	if (!(request.url||"").endsWith("#nopanel"))
		$('#backdrop').modal();
}

$( document ).ajaxSend(function(event, xhr, settings, error) {
  on_start(settings);
});

$( document ).ajaxStop(function(event, xhr, settings, error) {
  on_finish(settings);
});

$( document ).ajaxError(function(event, xhr, settings, error) {
  on_error(error);
});

axios.interceptors.request.use(function (config) {
    on_start(config);
    return config;
  }, function (error) {
	on_error(error);
    return Promise.reject(error);
  });

axios.interceptors.response.use(function (response) {
    on_finish(response);
    return response;
  }, function (error) {
    on_error(error);
    return Promise.reject(error);
  });

function updateBadges(url,delay,maxdelay){

	if ($('.k-badge').length==0){
		console.log("no badges to update");
		return;
	}

	window.badgesDelays = window.badgesDelays || delay;
	window.badgesUrl = window.badgesUrl || url;
	window.badgesMaxdelay = window.badgesMaxdelay || maxdelay;
	axios.get(window.badgesUrl+"#nopanel")
	.then( function(response) {
		var columns = response.data.columns;
		var cnt = 0;
		for( var idx in response.data.data){
			var row = response.data.data[idx]
			var key,count ;
			if (row.push){
				key = row[columns.indexOf('value')];
				count = row[columns.indexOf('count')];
			} else {
				key = row['value'];
				count = row['count'];
			}

			var badge = $("#"+key);
			if (badge.length>0){
				badge.text(count);
				count>0 ? badge.show() : badge.hide();
				cnt++;
			}

		}
		console.log(cnt+" badges updated");
	})
	.catch( function(error) {
		console.log(error);
	});

	window.badgesDelays = window.badgesDelays * 2;

	if (window.badgesDelays<=window.badgesMaxdelay){
		setTimeout( updateBadges, window.badgesDelays*1000 );
		console.log("badges scheduled:" + window.badgesDelays);
	} else {
		console.log("badges not scheduled:" +  window.badgesDelays);
	}
}

// Cookies
function createCookie(name, value, days) {
	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		var expires = "; expires=" + date.toGMTString();
	}
	else var expires = "";

	document.cookie = name + "=" + value + expires + "; path=/";
}
function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') c = c.substring(1, c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
	}
	return null;
}
function eraseCookie(name) {
	createCookie(name, "", -1);
}

function uritemplate(template){
	/* risolse {....} con la querystring corrente */
	if (!template)
		return template;
	var q = new URLSearchParams(window.location.search);
	var url = template.replace(/{([^}]+)}/g, function(match, content)
		{
			return q.get(content)||"";
		}
	);
	return url;
}

var isServer = false;
var isBrowser = true;

(function( $ ){
	$.fn.toMap = function() {
		function parse(val){
			if (val == ""){ return null; }
			if (val == "true"){ return true; }
			if (val == "false"){ return false; }
			return val;
		}
		function toNestedObject(obj, arr){
			var key = arr.shift();
			if (arr.length > 0) {
				if (key.endsWith("[]")){
					key = key.substring(0,key.length-2);
					var v = toNestedObject(obj[key] || {}, arr);
					if (!v.push){
						if (v.startsWith("["))
							v = v.substring(1,v.length-1);
						if (v)
							v = v.split(",");
						else
							v = [];
					}
					obj[key] = v;
				} else {
					obj[key] = toNestedObject(obj[key] || {}, arr);
				}
				return obj;
			}
			return key;
		}
		if (this.length == 1){
			return $.makeArray(this[0].elements)
				.filter(function(e){
					return e.name != "" && (e.type == 'radio' ? e.checked : true);
				})
				.map(function(e){
					var names = e.name.split('.');
					if (e.type == 'checkbox') {
						e.value = e.checked;
					}
					names.push(parse(e.value));
					return names;
				})
				.reduce(toNestedObject, {});
		} else {
			throw({error:"Can work on a single form only"})
		}
	};

	$.flatten = function (obj){
		var ret = {}
		for (var key in obj){
			if (typeof obj[key] == 'object'){
				var fobj = $.flatten(obj[key]);
				for (var extkey in fobj){
					ret[key+"."+extkey] = fobj[extkey];
				}
			} else {
				ret[key] = String(obj[key]);
			}
		}
		return ret;
	}

})( jQuery );

$(function() {
	initDOM($(document.body));
});