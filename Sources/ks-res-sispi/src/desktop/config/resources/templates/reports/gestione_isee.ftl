<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<div class=''>
 <div class='row'>
  <div class='form-group col-md-2 offset-md-10'>
   <button type='button' class='btn btn-primary' data-toggle='modal' data-target='#recordModal' data-idrecord=''>Aggiungi ISEE</button>
  </div>
 </div>
 <div class='row'>
  <table class='table table-striped table-sm' id='risultatoQueryTable'>
   <thead>
    <tr>
     <th>${(params["facet.DATA_PRESENTAZIONE.label"])!}</th>
     <th>${(params["facet.ANNO.label"])!}</th>
     <th><a class='order' href='/reports/report?orderBy=CODICE_FISCALE${querystringParams}'>${(params["facet.CODICE_FISCALE.label"])!}</th>
     <th>${(params["facet.IMPORTO.label"])!}</th>
	 <th><a class='order' href='/reports/report?orderBy=DATA_INIZIO_VALIDITA${querystringParams}'>${(params["facet.DATA_INIZIO_VALIDITA.label"])!}</th>
	 <th><a class='order' href='/reports/report?orderBy=DATA_FINE_VALIDITA${querystringParams}'>${(params["facet.DATA_FINE_VALIDITA.label"])!}</th>
	 <th>${(params["facet.ID_ISTANZA.label"])!}</th>
	 <th>${(params["facet.STATO.label"])!}</th>
     <th>${(params["facet.UTENTE_INSERITORE.label"])!}</th>
     <th>${(params["facet.UTENTE_RICHIEDENTE.label"])!}</th>
     <th>${(params["facet.DOCNUM_PRINCIPALE.label"])!}</th>
     <th>${(params["facet.CAUSALE.label"])!}</th>
	 <th></th>
    </tr>
   </thead>
   <tbody>
   <#list data as item >
    <tr>
     <td>${(item.DATA_PRESENTAZIONE)!}</td>
     <td>${(item.ANNO)!}</td>
     <td>${(item.CODICE_FISCALE)!}</td>
     <td>${(item.IMPORTO)!}</td>
     <td>${(item.DATA_INIZIO_VALIDITA_STRING)!}</td>
     <td>${(item.DATA_FINE_VALIDITA_STRING)!}</td>
	 <td><strong><a href='/bpm/instances/details?id=${(item.ID_ISTANZA)!}'>${(item.ID_ISTANZA)!}</a></strong></td>
	 <td>${(item.STATO)!}</td>
	 <td>${(item.UTENTE_INSERITORE)!}</td>
     <td>${(item.UTENTE_RICHIEDENTE)!}</td>
	 <td><strong><a target='_blank' href='/documenti/viewProfile?DOCNUM=${(item.DOCNUM_PRINCIPALE)!}'>${(item.DOCNUM_PRINCIPALE)!}</a></strong></td>
     <td>${(item.CAUSALE)!}</td>
     <td>
	  <#if "Approvato" == (item.STATO)!>
	  <a class='editRecord' title="Modifica ${(item.CODICE_FISCALE)!}" data-backdrop='static' data-keyboard='false' data-toggle='modal' data-target='#recordModal' data-idrecord='${(item.ID)!}'><span class='glyphicon glyphicon-pencil' aria-hidden='true'></span></a>
	  <#elseif "Da Validare" == (item.STATO)!>
	  <a title='Approvazione ${(item.ID_ISTANZA)!}' target='_blank' href='/bpm/instances/details?id=${(item.ID_ISTANZA)!}'><span class='glyphicon glyphicon-hourglass' aria-hidden='true'></span></a>
	  <#else>
	  <a title='ISEE non approvato ${(item.ID_ISTANZA)!}' href='#'><span class='glyphicon glyphicon-ban-circle' aria-hidden='true'></span></a>
	  </#if>
     </td>
    </tr>
   </#list>
   </tbody>
  </table>
 </div>
</div>

<div class='modal' id='recordModal' role='dialog' aria-labelledby='recordModalLabel'>
 <div class='modal-dialog modal-lg modal-dialog-scrollable' role='document'>
  <div class='modal-content'>
   <div class='modal-header'>
    <h4 class='modal-title' id='recordModalLabel'></h4>
    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
     <span aria-hidden="true">&times;</span>
    </button>
   </div>
   <div class='modal-body'>
    <input type='hidden' class='inputModal' id='id'>
    <input type='hidden' class='inputModal' id='anno'>
    <input type='hidden' class='inputModal' id='utenteInseritore'>
    <input type='hidden' class='inputModal' id='docnumPrincipale'>
    <input type='hidden' class='inputModal' id='idIstanza'>
    <input type='hidden' class='inputModal' id='stato'>
    <div class='row'>
     <div class='form-group col-md-6 inputModalInsertRecord'>
      <label for='dataPresentazione'>${(params["facet.DATA_PRESENTAZIONE.label"])!}</label>
      <input type='hidden' id='dataPresentazioneSubmit'/>
      <input type='text' id='dataPresentazione' class='form-control inputModal data-input' autocomplete='off' />
     </div>
     <div class='form-group col-md-6 inputModalUpdateRecord'>
      <label for='dataPresentazioneStatic'>${(params["facet.DATA_PRESENTAZIONE.label"])!}</label>
      <p class='form-control-static' id='dataPresentazioneStatic'></p>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-6 inputModalInsertRecord'>
      <label for='codiceFiscale'>${(params["facet.CODICE_FISCALE.label"])!}</label>
      <input type='text' class='form-control inputModal upperCaseText' id='codiceFiscale'>
     </div>
     <div class='form-group col-md-6 inputModalUpdateRecord'>
      <label for='codiceFiscaleStatic'>${(params["facet.CODICE_FISCALE.label"])!}</label>
      <p class='form-control-static' id='codiceFiscaleStatic'></p>
     </div>
     <div class='form-group col-md-6'>
      <label for='importo'>${(params["facet.IMPORTO.label"])!}</label>
      <input type='text' class='form-control importo inputModal' id='importo'>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-6 inputModalInsertRecord'>
      <label for='dataInizioValidita'>${(params["facet.DATA_INIZIO_VALIDITA.label"])!}</label>
      <input type='hidden' id='dataInizioValiditaSubmit'/>
      <input type='text' id='dataInizioValidita' class='form-control inputModal data-input' autocomplete='off' />
     </div>
     <div class='form-group col-md-6 inputModalUpdateRecord'>
      <label for='dataInizioValiditaStatic'>${(params["facet.DATA_INIZIO_VALIDITA.label"])!}</label>
      <p class='form-control-static' id='dataInizioValiditaStatic'></p>
     </div>
     <div class='form-group col-md-6'>
      <label for='dataFineValidita'>${(params["facet.DATA_FINE_VALIDITA.label"])!}</label>
      <input type='hidden' class='inputModal' id='dataFineValidita'/>
      <p class='form-control-static' id='dataFineValiditaHtml'></p>
     </div>
    </div>
    <hr class='clearfix' />
    <div class='row'>
     <div class='form-group col-md-6 inputModalInsertRecord'>
      <label for='utenteRichiedente'>${(params["facet.UTENTE_RICHIEDENTE.label"])!}</label>
      <input type='text' class='form-control inputModal upperCaseText' id='utenteRichiedente'>
     </div>
     <div class='form-group col-md-6 inputModalUpdateRecord'>
      <label for='utenteRichiedenteStatic'>${(params["facet.UTENTE_RICHIEDENTE.label"])!}</label>
      <p class='form-control-static' id='utenteRichiedenteStatic'></p>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-12'>
      <label for='causale'>${(params["facet.CAUSALE.label"])!}</label>
      <input type='text' class='form-control inputModal' id='causale'>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='dataProtocollo'>${(params["facet.DATA_PROTOCOLLO.label"])!}</label>
      <input type='hidden' id='dataProtocolloSubmit'/>
      <input type='text' id='dataProtocollo' class='form-control inputModal data-input' autocomplete='off' />
     </div>
     <div class='form-group col-md-6'>
      <label for='numeroProtocollo'>${(params["facet.NUMERO_PROTOCOLLO.label"])!}</label>
      <input type='text' class='form-control inputModal' id='numeroProtocollo'>
	  <small id='numeroProtocolloHelp' class='form-text text-muted'>Esempio: INPS-ISEE-202X-XXXXXXXXX-00</small>
     </div>
    </div>
    <hr class='clearfix' />
	<div class='row'>
     <div class='form-group col-md-12'>
      <div class='' id='validazione-modal-div' role='alert'></div>
     </div>
    </div>
   </div>
   <div class='modal-footer'>
    <button type='button' class='btn btn-default' data-dismiss='modal'>Chiudi</button>
    <button type='button' class='btn btn-primary' id='btnSalvaRecord'>Salva</button>
   </div>
  </div>
 </div>
</div>

<script>

var listaIseeIscritto = [];
var recordSelezionato = {};

$( document ).ready(function() {
	$('#btnSalvaRecord').on('click', function() {
		if(validazione()=="si"){
			var callback = function() {
				eseguiInsertDati();
			};
			confirmKS('Conferma', '<p>Aggiornare i dati?</p>', 'Cancella', 'Conferma', callback);
		}
	});

	$('#codiceFiscale').on('change', function() {
		cercaIseeIscritto($(this).val());
    });
	
	$('#dataInizioValidita').on('change', function() {
		validaDataInizio();
    });

	$('#recordModal').on('show.bs.modal', function (event) {
		var button = $(event.relatedTarget);
		var idRecord = button.data('idrecord');
		settaDatiModal(idRecord);
	});
	initDateInput();
});

function settaDatiModal(idRecord){
	resetDatiModal();
	if(idRecord){
		caricaDati(idRecord);
	}else{
		initNuovoInserimento();
	}
}

function caricaDati(idRecord){
	//var whereCondition = "ID="+idRecord;
	//var params = {};
	//params['where'] = whereCondition;
	
	$.ajax ({
		method: 'GET',
		//url: '/bpm-server/db/select/'+TABLE_NAME,
		url: '/sispi/v1/isee/getIsee/'+idRecord,
		contentType: 'application/json',
		timeout: 100000,
		//data: params,
		success: function(data, textStatus, jqXHR){
			if(!data){
				visualizzaMessaggio('validazione-modal-div', '<p>Nessun record trovato</p>', 'danger');
			}else{
				var dataItem = data;
				recordSelezionato = dataItem;
				for(chiave in dataItem){
					var valoreInput = dataItem[chiave];
					$('#recordModal').find('.modal-body #'+chiave).val(valoreInput);
					$('#recordModal').find('.modal-body p[id="'+chiave+'"]').html(valoreInput);
				}
				if(dataItem['importo']){
					$('#importo').val(formatFloat(dataItem.importo));
					$('#importo').trigger('change');
				}

				var dataObj = null;
				if(dataItem['dataPresentazione']){
					dataObj = convertStringToDate(dataItem.dataPresentazione);
					$('#dataPresentazione').datepicker("setDate", dataObj);
					$('#dataPresentazioneStatic').html(convertDateToString(convertStringToDate(dataItem.dataPresentazione)));
				}

				if(dataItem['dataInizioValidita']){
					dataObj = convertStringToDate(dataItem['dataInizioValidita']);
					$('#dataInizioValidita').datepicker("setDate", dataObj);
					$('#dataInizioValiditaStatic').html(convertDateToString(convertStringToDate(dataItem.dataInizioValidita)));
				}

				if(dataItem['dataFineValidita']){
					$('#dataFineValiditaHtml').html(convertDateToString(convertStringToDate(dataItem.dataFineValidita)));
				}
				
				if(dataItem['dataProtocollo']){
					dataObj = convertStringToDate(dataItem.dataProtocollo);
					$('#dataProtocollo').datepicker("setDate", dataObj);
				}

				$('#codiceFiscaleStatic').html(dataItem.codiceFiscale);
				$('#utenteRichiedenteStatic').html(dataItem.utenteRichiedente);

				var titoloModal="Modifica ISEE <strong>"+dataItem.codiceFiscale+"</strong>";
				$('#recordModal').find('.modal-title').html(titoloModal);

				$('.inputModalUpdateRecord').show();
				if(!verificaAnnoDataInizioValidita()){
					$('#btnSalvaRecord').attr('disabled', 'disabled');
					visualizzaMessaggio('validazione-modal-div', '<p>ISEE non valido per l\'anno in corso</p>', 'danger');
				}
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('Errore nel caricamento dati');
			visualizzaMessaggio('validazione-div', '<p>Errore nel caricamento dati</p>', 'danger');
		},
		complete: function( jqXHR, textStatus ){
			console.log('complete');
		}
	});
}


function initNuovoInserimento(){
	var titoloModal="Nuovo ISEE";
	$('#recordModal').find('.modal-title').html(titoloModal);
	var annoAttuale=getAnnoAttuale();
	$('#anno').val(annoAttuale);
	$('#stato').val("APPROVATO");
	$('#dataInizioValidita').datepicker("setDate", new Date(annoAttuale, 0, 1));
	var dataFineValidita = getDataFineValidita();
	$('#dataFineValidita').val(dataFineValidita);
	$('#dataFineValiditaHtml').html(convertDateToString(convertStringToDate(dataFineValidita)));
	$('#utenteInseritore').val(userInfo.username);
	$('.inputModalInsertRecord').show();
}

function getDataFineValidita(){
	return getAnnoAttuale()+"-12-31";
}

function getAnnoAttuale(){
	var date = new Date();
	var annoAttuale = date.getFullYear();
	return annoAttuale;
}

function resetDatiModal(){
	rimuoviErroriModal();
	listaIseeIscritto = [];
	recordSelezionato = {};
	rimuoviMessaggio('validazione-modal-div');
	$("#recordModal").find('.inputModal').each(function(){
		$(this).removeAttr('disabled');
	});

	$('#btnSalvaRecord').removeAttr('disabled');
	$('.inputModalUpdateRecord').hide();
	$('.inputModalInsertRecord').hide();
}

function cercaIseeIscritto(cfIscritto){
	//var whereCondition = "CODICE_FISCALE='"+cfIscritto+"'";
	//var params = {};
	//params['where'] = whereCondition;
	//params['orderBy'] = "DATA_INIZIO_VALIDITA DESC";

	$.ajax ({
		method: 'GET',
		//url: '/bpm-server/db/select/'+TABLE_NAME,
		url: '/sispi/v1/isee/lista/'+cfIscritto,
		contentType: 'application/json',
		timeout: 100000,
		//data: params,
		success: function(data, textStatus, jqXHR){
			listaIseeIscritto=data;
			validaDataInizio();
		},
		error: function(jqXHR, textStatus, errorThrown) {
			visualizzaMessaggio('validazione-modal-div', '<p>Errore nel recuper dati isee di: '+cfIscritto+'</p>', 'danger');
		},
		complete: function( jqXHR, textStatus ){
			console.log('complete');
			//waitingDialog.hide();
		}
	});
}

function validaDataInizio(){
	rimuoviErroreInput("dataInizioValidita");
	var dataInizioMaggiore = checkDataInizioSuperata();
	if(dataInizioMaggiore!=null){
		aggiungiErroreInput("dataInizioValidita","La data inizio deve essere maggiore di "+convertDateToString(dataInizioMaggiore));
	}
	return dataInizioMaggiore;
}

function checkDataInizioSuperata(){
	var result = null;
	var dataInizioValidita = $("#dataInizioValidita").datepicker("getDate");
	if(dataInizioValidita){
		for (i=0; i<listaIseeIscritto.length; i++){
			var dataItem = listaIseeIscritto[i];
			var dataInizioValiditaVecchiIsee = convertStringToDate(dataItem.dataInizioValidita);
			if(dataInizioValiditaVecchiIsee>=dataInizioValidita){
				result = dataInizioValiditaVecchiIsee;
				break;
			}
		}
	}
	return result;
}

function getDatiFormObject(){
	var params = {};

	$("#recordModal").find('.inputModal').each(function(){
		var idInput = $(this).attr("id");
		params[idInput] = $(this).val();
	});
	//Fix per importo isee
	params['importo'] = getValoreFloat(params['importo']);

	//Fix per date
	params['dataInizioValidita'] = $('#dataInizioValiditaSubmit').val();
	params['dataPresentazione'] = $('#dataPresentazioneSubmit').val();
	params['dataProtocollo'] = $('#dataProtocolloSubmit').val();
	return params;
}

function eseguiInsertDati(){
	//waitingDialog.show('Avvio processo aggiorna ISEE');
	var datiAvvioProcesso = getDatiAvvioProcesso();
	eseguiChiamataAjax('/bpm/v1/instances', datiAvvioProcesso, successChiamataAjax);
}

function isProprietaUguali(recordModificato, recordOriginale, chiave) {
	var result = false;
	if(chiave in recordModificato && chiave in recordOriginale){
		if(!recordModificato[chiave] && !recordOriginale[chiave]){
			result = true;
		}else if( recordModificato[chiave] && recordOriginale[chiave] && (recordModificato[chiave]==recordOriginale[chiave] )){
			result = true;
		}
	}
	return result;
}

function eseguiChiamataAjax(urlToCall, parametri, successFunction, contentType='application/json', requestMethod='POST') {
	return $.ajax ({
		method: requestMethod,
		url: urlToCall,
		contentType: contentType,
		timeout: 100000,
		data: parametri,
		success: successFunction,
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('Errore nel salvataggio dati');
			visualizzaMessaggio('validazione-div', '<p>Errore nel salvataggio dati</p>', 'danger');
		},
		complete: function( jqXHR, textStatus ){
			console.log('complete salvataggio dati');
			//waitingDialog.hide();
		}
	});
}

function getDatiAvvioProcesso(){
	var params = {};
	params['processId'] = 'Aggiorna ISEE2.2';

	datiForm = getDatiFormObject();
	var datiFormTmp = {};
	datiFormTmp['datiForm'] = datiForm;
	datiFormTmp['utenteProcesso'] = userInfo.username;

	if(!datiForm["id"] || !isProprietaUguali(datiForm, recordSelezionato, "importo")){
		datiFormTmp['importoIseeModificato'] = '1';
	}

	params['input'] = datiFormTmp;
	return JSON.stringify(params);
}

function successChiamataAjax(data, textStatus, jqXHR){
	location.reload();
}

function rimuoviErroriModal(resetValore=true){
	$("#recordModal").find('.inputModal').each(function(){
		if(resetValore){
			$(this).val("");
		}
		var idInput = $(this).attr("id");
		rimuoviErroreInput(idInput);
	});
}

function verificaSuperamentoDataAttuale(dataString){
	var dataSuperata = true;
	var dataAttuale = new Date();
	var dataObject = convertStringToDate(dataString);
	if(dataAttuale>=dataObject){
		dataSuperata = false;
	}
	return dataSuperata;
}

function validazione(){
	var prosegui="si";
	rimuoviMessaggio('validazione-modal-div');
	rimuoviErroriModal(false);

	var dataPresentazione = $('#dataPresentazioneSubmit').val();
	if(dataPresentazione==""){
		aggiungiErroreInput("dataPresentazione","Data presentazione obbligatoria");
		prosegui="no";
	}else if(verificaSuperamentoDataAttuale(dataPresentazione)){
		aggiungiErroreInput("dataPresentazione","Data presentazione maggiore della data attuale");
		prosegui="no";
	}
	var codiceFiscale = $('#codiceFiscale').val();
	if( codiceFiscale==""){
		aggiungiErroreInput("codiceFiscale","Codice fiscale obbligatorio");
		prosegui="no";
	}else if(!verificaCodiceFiscale(codiceFiscale)){
		aggiungiErroreInput("codiceFiscale","Codice fiscale non valido");
		prosegui="no";
	}
	if($('#importo').val()==""){
		aggiungiErroreInput("importo","Importo obbligatorio");
		prosegui="no";
	}

	if(validaDataInizio()!=null){
		prosegui="no";
	}
	var dataInizioValidita = $('#dataInizioValiditaSubmit').val();
	if(dataInizioValidita==""){
		aggiungiErroreInput("dataInizioValidita","Data inizio validit&agrave; obbligatoria");
		prosegui="no";
	} else {
		if(verificaSuperamentoDataAttuale(dataInizioValidita)){
			aggiungiErroreInput("dataInizioValidita","Data inizio validit&agrave; maggiore della data attuale");
			prosegui="no";
		}
		if(!verificaAnnoDataInizioValidita()){
			aggiungiErroreInput("dataInizioValidita","Data inizio validit&agrave; deve avere anno: "+annoAttuale);
			prosegui="no";
		}
	}

	if($('#utenteRichiedente').val()==""){
		aggiungiErroreInput("utenteRichiedente", "Utente richiedente obbligatorio");
		prosegui="no";
	}
	if($('#causale').val()==""){
		aggiungiErroreInput("causale","Causale obbligatoria");
		prosegui="no";
	}

	var dataProtocollo = $('#dataProtocolloSubmit').val();
	if(dataProtocollo==""){
		aggiungiErroreInput("dataProtocollo","Data protocollo obbligatoria");
		prosegui="no";
	}else if(verificaSuperamentoDataAttuale(dataProtocollo)){
		aggiungiErroreInput("dataProtocollo", "Data protocollo maggiore della data attuale");
		prosegui="no";
	}

	var numeroProtocollo = $('#numeroProtocollo').val();
	if( numeroProtocollo==""){
		aggiungiErroreInput("numeroProtocollo","Numero protocollo obbligatorio");
		prosegui="no";
	}else if(!verificaNumeroProtocollo(numeroProtocollo)){
		aggiungiErroreInput("numeroProtocollo","Numero protocollo non valido");
		prosegui="no";
	}

	if("no"==prosegui){
		visualizzaMessaggio('validazione-modal-div', '<p>Non &egrave; possibile salvare l\'ISEE. Dati obbligatori mancanti</p>', 'danger');
	}
	//prosegui="no";
	return prosegui;
}

function verificaAnnoDataInizioValidita(){
	var dataInizioValiditaDate = $("#dataInizioValidita").datepicker("getDate");
	var annoInizioValidita = dataInizioValiditaDate.getFullYear();
	var annoAttuale = getAnnoAttuale();
	if(annoInizioValidita!=annoAttuale){
		return false;
	}
	return true;
}

function verificaNumeroProtocollo(numeroProtocollo){
	const protocolloRegex = /^INPS-ISEE-[0-9]{4}-[0-9]{8}[A-Z]-[0-9]{2}$/;
	return protocolloRegex.test(numeroProtocollo);
}

function initDateInput(){
	$.datepicker.setDefaults({
      altFormat: "yy-mm-dd",
      dateFormat:'dd/mm/yy'
    });
	$('#dataPresentazione').datepicker({
		altField: "#dataPresentazioneSubmit"
	});
	$('#dataInizioValidita').datepicker({
		altField: "#dataInizioValiditaSubmit"
	});
	$('#dataProtocollo').datepicker({
		altField: "#dataProtocolloSubmit"
	});
}

</script>