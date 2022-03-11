<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<script type="text/javascript" src="/static/palermo/js/gestione-fascia-istituto.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<div class=''>
 <div class='row'>
  <table class='table table-striped table-condensed'>
   <thead>
    <tr>
     <th><a class="order" href="?orderBy=ANNO_SCOLASTICO${querystringParams}">${(params["facet.ANNO_SCOLASTICO.label"])!}</a></th>
     <th><a class="order" href="?orderBy=CODICE_FISCALE${querystringParams}">${(params["facet.CODICE_FISCALE.label"])!}</a></th>
     <th><a class="order" href="?orderBy=DESCRIZIONE${querystringParams}">${(params["facet.DESCRIZIONE.label"])!}</a></th>
     <th><a class="order" href="?orderBy=NOME_ISTITUTO${querystringParams}">${(params["facet.NOME_ISTITUTO.label"])!}</th>
     <th><a class="order" href="?orderBy=FASCIA${querystringParams}">${(params["facet.FASCIA.label"])!}</th>
     <th><a class="order" href="?orderBy=FASCIA_ORARIA${querystringParams}">${(params["facet.FASCIA_ORARIA.label"])!}</th>
     <th></th>
    </tr>
   </thead>
   <tbody>
    <#list data as item >
    <tr>
     <td>${(item.ANNO_SCOLASTICO)!}</td>
     <td>${(item.CODICE_FISCALE)!}</td>
     <td>${(item.DESCRIZIONE)!}</td>
     <td>${(item.NOME_ISTITUTO)!}</td>
     <td>${(item.FASCIA)!}</td>
     <td>${(item.FASCIA_ORARIA)!}</td>
     <td>
      <a class='editRecord' title="Modifica ${(item.CODICE_FISCALE)!}" data-backdrop='static' data-keyboard='false' data-toggle='modal' data-target='#recordModal' data-idrecord='${item.ID}'><span class='glyphicon glyphicon-pencil' aria-hidden='true'></span></a>
     </td>
    </tr>
    </#list>
   </tbody>
  </table>
 </div>
</div>

<div class='modal fade' id='recordModal' role='dialog' aria-labelledby='recordModalLabel'>
 <div class='modal-dialog modal-lg modal-dialog-scrollable' role='document'>
  <div class='modal-content'>
   <div class='modal-header'>
    <h4 class='modal-title' id='recordModalLabel'></h4>
	<button type="button" class="close" data-dismiss="modal" aria-label="Close">
     <span aria-hidden="true">&times;</span>
    </button>
   </div>
   <div class='modal-body'>
    <div class='row'>
     <div class='form-group col-md-4'>
      <label for='codiceFiscale'>${(params["facet.CODICE_FISCALE.label"])!}</label>
      <p class='form-control-static' id='codiceFiscale'></p>
     </div>
     <div class='form-group col-md-4'>
      <label for='annoScolastico'>${(params["facet.ANNO_SCOLASTICO.label"])!}</label>
      <p class='form-control-static' id='annoScolastico'></p>
     </div>
     <div class='form-group col-md-4'>
      <label for='descrizione'>${(params["facet.DESCRIZIONE.label"])!}</label>
      <p class='form-control-static' id='descrizione'></p>
     </div>
    </div>
    <hr class='clearfix' />
    <input type='hidden' class='inputModal' id='id'>
    <input type='hidden' id='SALVA_SUCCESSIVI'>
    <div class='row'>
     <h4 class='ml-2'>Dati Istituto</h4>
    </div>
    <div class='row'>
     <div class='form-group col-md-4 searchable'>
      <label for='nomeIstituto'>${(params["facet.NOME_ISTITUTO.label"])!}</label>
      <input type='text' class='code form-control inputModal' id='nomeIstituto' data-onSelect='nomeIstitutoSelect' data-onCancel='nomeIstitutoCancel' autocomplete='off' />
      <ul></ul>
     </div>
     <div class='form-group col-md-4'>
      <label for='fascia'>${(params["facet.FASCIA.label"])!}</label>
      <select class='form-control inputModal' id='fascia'>
       <option value='' selected>--Seleziona--</option>
      </select>
     </div>
     <div class='form-group col-md-4'>
      <label for='fasciaOraria'>${(params["facet.FASCIA_ORARIA.label"])!}</label>
      <select class='form-control inputModal' id='fasciaOraria'>
       <option value='' selected>--Seleziona--</option>
      </select>
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
    <button type='button' class='btn btn-success' id='btnSalvaSuccessivi'>Salva successivi</button>
   </div>
  </div>
 </div>
</div>

<script>

var recordSelezionato = {};
//var AGGIORNA_FASCIA_ISTITUTO_URL = '/bpm-server/process/startProcess/Aggiorna Fascia Istituto1.0/Aggiorna Fascia Istituto';
var AVVIA_ISTANZA_URL = '/bpm/v1/instances';

$( document ).ready(function() {

	$('#btnSalvaSuccessivi').on('click', function() {
		if(validazione()=="si"){
			$('#SALVA_SUCCESSIVI').val('SI');
			confirmKS('Conferma', '<p>Salvare i mesi successivi e ricalcolare le rate?</p>', 'Cancella', 'Conferma', eseguiInsertInviaNotifica);
		}
	});

	$('#btnSalvaRecord').on('click', function() {
		if(validazione()=="si"){
			$('#SALVA_SUCCESSIVI').val('');
			confirmKS('Conferma', '<p>Salvare il mese attuale e ricalcolare le rate?</p>', 'Cancella', 'Conferma', eseguiInsertInviaNotifica);
		}
	});

	$('#recordModal').on('show.bs.modal', function (event) {
		var button = $(event.relatedTarget);
		var idRecord = button.data('idrecord');
		settaDatiModal(idRecord);
	});

	caricaElencoTariffe();
	settaQueryParams();
});

function settaQueryParams(){
	const urlParams = new URLSearchParams(window.location.search);
	for(var key of urlParams.keys()) {
		if(urlParams.has(key)){
			console.log("Param: "+key+" - Value: "+urlParams.get(key));
			if("ANNO_SCOLASTICO"==key) {
				$('#titoloReport').html("Gestione fascia istituto "+urlParams.get(key));
			}
		}
	}
}

function settaDatiModal(idRecord){
	resetDatiModal();
	if(idRecord){
		caricaDati(idRecord);
	}
}

function resetDatiModal(){
	rimuoviErroriModal();
	recordSelezionato = {};
	nomeIstitutoCancel();
	rimuoviMessaggio('validazione-modal-div');

	$('#annoScolastico').html('');
	$('#codiceFiscale').html('');
	$('#btnSalvaRecord').removeAttr('disabled');
	$('#btnSalvaSuccessivi').removeAttr('disabled');
}

function caricaDati(idRecord){
	//var whereCondition = "ID="+idRecord;
	//var params = {};
	//params['where'] = whereCondition;

	$.ajax ({
		method: 'GET',
		//url: '/bpm-server/db/select/'+TABLE_NAME,
		url: '/sispi/v1/fasciaistituto/get/'+idRecord,
		contentType: 'application/json',
		timeout: 100000,
		//data: params,
		success: function(data, textStatus, jqXHR){
			if(!data){
				visualizzaMessaggio('validazione-modal-div', '<p>Errore nel recupero dati. Chiudere e riprovare</p>', 'danger');
				$('#btnSalvaRecord').attr('disabled', 'disabled');
				$('#btnSalvaSuccessivi').attr('disabled', 'disabled');
			}else{
				var dataItem = data;
				recordSelezionato = dataItem;
				nomeIstitutoSelect(dataItem.nomeIstituto);
				for(chiave in dataItem){
					var valoreInput = dataItem[chiave];
					$('#recordModal').find('.modal-body #'+chiave).val(valoreInput);
					$('#recordModal').find('.modal-body p[id="'+chiave+'"]').html(valoreInput);
				}
				ricaricaListaFasciaOraria(dataItem.fasciaOraria);
				var titoloModal="Gestione istituto/fascia <strong>"+dataItem.descrizione+" - "+dataItem.codiceFiscale+"</strong>";
				$('#recordModal').find('.modal-title').html(titoloModal);
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

function getDatiForm(){
	var params = {};
	//params['@entity'] = TABLE_NAME;
	for (key in recordSelezionato) {
		params[key] = recordSelezionato[key];
	}

	$("#recordModal").find('.inputModal').each(function(){
		var idInput = $(this).attr("id");
		params[idInput] = $(this).val().trim();
	});
	return params;
}

function eseguiInsertInviaNotifica(){
	eseguiChiamataAjax(AVVIA_ISTANZA_URL, getDatiAvvioProcesso(), successChiamataAjax);
	//eseguiChiamataAjax('/bpm-server/db/saveorupdate', getDatiForm(), successChiamataAjaxInviaNotifica);
}

function eseguiChiamataAjax(urlToCall, parametri, successFunction, contentType='application/json', requestMethod='POST') {
	return $.ajax ({
		method: requestMethod,
		url: urlToCall,
		crossOrigin: true,
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
		}
	});
}

//function successChiamataAjaxInviaNotifica(data, textStatus, jqXHR){
//	if(data && "ID" in data){
//		
//	}else{
//		visualizzaMessaggio('validazione-modal-div', '<p>Errore nel salvataggio dati</p>', 'danger');
//	}
//}

function getDatiAvvioProcesso(){
	var params = {};
	params['processId'] = 'Aggiorna Fascia Istituto2.0';
	var datiFormTmp = {};
	datiFormTmp['datiForm'] = getDatiForm();
	datiFormTmp['salvaSuccessivi'] = $('#SALVA_SUCCESSIVI').val();
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

function validazione(){
	var prosegui="si";
	rimuoviMessaggio('validazione-modal-div');
	rimuoviErroriModal(false);
	if($('#nomeIstituto').val()==""){
		aggiungiErroreInput("nomeIstituto","Istituto obbligatorio");
		prosegui="no";
	}
	if($('#fascia').val()==""){
		aggiungiErroreInput("fascia","Fascia obbligatoria");
		prosegui="no";
	}
	if($('#fasciaOraria').val()==""){
		aggiungiErroreInput("fasciaOraria","Fascia oraria obbligatoria");
		prosegui="no";
	}

	if("no"==prosegui){
		visualizzaMessaggio('validazione-modal-div', '<p>Non &egrave; possibile salvare l\'istituto/fascia. Dati obbligatori mancanti</p>', 'danger');
	}
	//prosegui="no";
	return prosegui;
}

</script>