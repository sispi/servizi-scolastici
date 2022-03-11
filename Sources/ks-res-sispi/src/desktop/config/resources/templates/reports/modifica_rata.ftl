<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<div class=''>
 <div class='row'>
  <table class='table table-hover table-sm'>
   <thead>
    <tr>
     <th>${(params["facet.ANNO_SCOLASTICO.label"])!}</th>
     <th>${(params["facet.CF_ISCRITTO.label"])!}</th>
     <th>${(params["facet.ISCRITTO.label"])!}</th>
     <th>${(params["facet.DESCRIZIONE.label"])!}</th>
     <th>${(params["facet.VALORE_RATA.label"])!}</th>
     <th>${(params["facet.NOME_ISTITUTO.label"])!}</th>
     <th>${(params["facet.RATA_PAGATA.label"])!}</th>
     <th>${(params["facet.VALORE_CONGUAGLIO.label"])!}</th>
     <th>${(params["facet.IMPORTO_TOTALE.label"])!}</th>
     <th></th>
     <th></th>
    </tr>
   </thead>
   <tbody>
   <#list data as item >
    <#assign DIFFERENZA_PAGAMENTO = 0>
    <tr
	<#if item.IMP_PAGAMENTO?? >
	<#assign DIFFERENZA_PAGAMENTO = (item.IMP_PAGAMENTO?number-item.TOTALE_RATA?number)>
	<#if item.TOTALE_RATA?number == item.IMP_PAGAMENTO?number >style='background-color: #5aff4b'</#if>
	<#if item.TOTALE_RATA?number lt item.IMP_PAGAMENTO?number >style='background-color: #f7ea02'</#if>
	<#if item.TOTALE_RATA?number gt item.IMP_PAGAMENTO?number >style='background-color: #ff6c6c'</#if>
	</#if>
	>
     <td>${(item.ANNO_SCOLASTICO)!}</td>
     <td>${(item.CF_ISCRITTO)!}</td>
     <td>${(item.NOME_MINORE)!} ${(item.COGNOME_MINORE)!}</td>
     <td>${(item.DESCRIZIONE)!}</td>
     <td>${(item.IMP_RATA_STRING)!}</td>
     <td>${(item.NOME_ISTITUTO)!}</td>
     <td>${(item.RATA_PAGATA)!} <#if item.PAGATO_COMPLETO != 'Si' >(${(DIFFERENZA_PAGAMENTO?c)!})</#if></td>
     <td>${(item.VALORE_CONGUAGLIO)!}</td>
     <td>${(item.IMPORTO_TOTALE)!}</td>
     <td>
     <#if "S"==(item.CONFERMA_NOTIFICATA)!>
     <#if item.RATA_PAGATA == "No" >
      <a class='editRecord' title='Modifica ${item.ID}' data-backdrop='static' data-keyboard='false' data-toggle='modal' data-target='#recordModal' data-idrecord='${item.ID}'><span class='glyphicon glyphicon-pencil' aria-hidden='true'></span></a>
	 <#else>
	  <a title='Pagamento ${(item.CF_ISCRITTO)!}' target='_blank' href='/reports/report?qt=pagamenti_retta_scolastica&CF_MINORE=${(item.CF_ISCRITTO)!}'><span class='glyphicon glyphicon-euro' aria-hidden='true'></span></a>
     </#if>
     </#if>
     </td>
     <td>
	  <a title='Log rata ${(item.ID)!}' target='_blank' href='/reports/report?qt=rata_log&ID_RATA=${(item.ID)!}'><span class='glyphicon glyphicon-tasks' aria-hidden='true'></span></a>
     </td>
    </tr>
   </#list>
   </tbody>
  </table>
 </div>
 <div class='row'>
  <div class='form-group col-md-12'>
   <div id='validazione-div' role='alert'></div>
  </div>
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
    <input type='hidden' class='inputModal' id='id'>
    <input type='hidden' class='inputModal' id='cfIscritto'>
    <input type='hidden' class='inputModal' id='annoScolastico'>
    <div class='row'>
     <h4 class='ml-2'>Dati Rata</h4>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='nomeIstituto'>${(params["facet.NOME_ISTITUTO.label"])!}</label>
	  <p class='form-control-static' id='nomeIstituto'></p>
     </div>
     <div class='form-group col-md-6'>
      <label for='annoScolastico'>${(params["facet.ANNO_SCOLASTICO.label"])!}</label>
      <p class='form-control-static' id='annoScolastico'></p>
     </div>
    </div>
    <hr class='clearfix' />
    <div class='row'>
     <div class='form-group col-md-4'>
      <label for='importoRetta'>${(params["facet.IMPORTO_RETTA.label"])!}</label>
      <p class='form-control-static' id='importoRetta'></p>
     </div>
     <div class='form-group col-md-4'>
      <label for='giorniFrequenza'>${(params["facet.GIORNI_FREQUENZA.label"])!}</label>
      <select class='form-control inputModal' id='giorniFrequenza'>
      </select>
     </div>
     <div class='form-group col-md-4'>
      <label for='valoreRata'>${(params["facet.IMPORTO_CALCOLATO.label"])!}</label>
	  <input type='hidden' class='inputModal' id='valoreRata'>
      <p class='form-control-static' id='valoreRataHtml'></p>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='valoreConguaglio'>${(params["facet.VALORE_CONGUAGLIO.label"])!}</label>
      <input type='text' class='form-control importo inputModal' id='valoreConguaglio'>
     </div>
    </div>
    <hr class='clearfix' />
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='importoTotale'>${(params["facet.IMPORTO_TOTALE.label"])!}</label>
	  <p class='form-control-static' id='importoTotale'></p>
     </div>
    </div>
    <hr class='clearfix' />
    <div class='row'>
     <div class='form-group col-md-12'>
      <label for='motivazioneVariazione'>${(params["facet.MOTIVAZIONE_VARIAZIONE.label"])!}</label>
	  <input type='text' class='form-control inputModal' id='motivazioneVariazione'>
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

var GIORNI_FREQUENZA_DEFAULT = "30";
//var TABLE_NAME = "RATA";
var recordSelezionato = {};
var recordIstanzaSelezionato = {};

$( document ).ready(function() {

	$('#btnSalvaRecord').on('click', function() {
		if(validazione()=="si"){
			confirmKS('Conferma', '<p>Salvare i dati?</p>', 'Cancella', 'Conferma', eseguiQueryInsert);
		}
	});

	$('#giorniFrequenza').on('change', function() {
		calcolaValoreRata();
	});

	$('#valoreConguaglio').on('change', function() {
		calcolaValoreRata();
	});

	$('#recordModal').on('show.bs.modal', function (event) {
		var button = $(event.relatedTarget);
		var idRecord = button.data('idrecord');
		settaDatiModal(idRecord);
	});

	caricaSelectGiorniFrequenza();
});

function caricaSelectGiorniFrequenza(){
	var giorniFrequenzaDefault = parseInt(GIORNI_FREQUENZA_DEFAULT);
	for (let i = 0; i <= giorniFrequenzaDefault; i++) {
		$('#giorniFrequenza').append("<option value='"+i+"'>"+i+"</option>");
	}
}
function settaDatiModal(idRecord){
	resetDatiModal();
	caricaDati(idRecord);
}

function resetDatiModal(){
	$("#recordModal").find('.inputModal').each(function(){
		$(this).val("");
		var idInput = $(this).attr("id");
		rimuoviErroreInput(idInput);
	});
	$('#recordModal').find('.modal-body #giorniFrequenza').val(GIORNI_FREQUENZA_DEFAULT);
	recordSelezionato = {};
	recordIstanzaSelezionato = {};
}

function caricaDati(idRecord){
	//var whereCondition = "ID="+idRecord;
	//var params = {};
	//params['where'] = whereCondition;
	//params['orderBy'] = "SORT asc";

	$.ajax ({
		method: 'GET',
		//url: '/bpm-server/db/select/'+TABLE_NAME,
		url: '/sispi/v1/pagamentoretta/rata/'+idRecord,
		contentType: 'application/json',
		timeout: 100000,
		//data: params,
		success: function(data, textStatus, jqXHR){
			if(!data){
				visualizzaMessaggio('validazione-modal-div', '<p>Nessun record trovato</p>', 'danger');
			}else{
				var dataItem = data;
				recordSelezionato = dataItem;
				valorizzaInputModal(dataItem);
				$('#valoreConguaglio').val(formatFloat(stringToNumber(dataItem.valoreConguaglio)));
				$('#valoreConguaglio').trigger('change');
				caricaDatiIstanza(dataItem.cfIscritto, dataItem.annoScolastico);
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('Errore nel caricamento dati');
			visualizzaMessaggio('validazione-modal-div', '<p>Errore nel caricamento dati rata</p>', 'danger');
		},
		complete: function( jqXHR, textStatus ){
			console.log('complete');
		}
	});
}

function caricaDatiIstanza(cfIscritto, annoScolastico){
	var params = {};
	params['cfMinoreAnagrafe'] = cfIscritto;
	params['annoScolastico'] = annoScolastico;

	$.ajax ({
		method: 'GET',
		//url: '/bpm-server/db/select/PORTSCU_ISTANZE',
		url: '/sispi/v1/istanza/getByCfMinoreAndAnnoScolastico',
		contentType: 'application/json',
		timeout: 100000,
		data: params,
		success: function(data, textStatus, jqXHR){
			if(!data){
				visualizzaMessaggio('validazione-modal-div', '<p>Nessun record trovato</p>', 'danger');
			}else{
				var dataItem = data;
				recordIstanzaSelezionato = dataItem;
				valorizzaInputModal(dataItem);
				calcolaGiorniFrequenza();
				calcolaValoreRata();
				var titoloModal="Modifica importo rata: <strong>"+recordSelezionato.descrizione+" - "+dataItem.nomeMinore+" "+dataItem.cognomeMinore+" ("+recordSelezionato.cfIscritto+")</strong>";
				$('#recordModal').find('.modal-title').html(titoloModal);
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('Errore nel caricamento dati');
			visualizzaMessaggio('validazione-modal-div', '<p>Errore nel caricamento dati istanza</p>', 'danger');
		},
		complete: function( jqXHR, textStatus ){
			console.log('complete');
		}
	});
}

function calcolaGiorniFrequenza(){
	if(!$('#giorniFrequenza').val()){
		var valoreRata = recordSelezionato.valoreRata;
		var valoreRataFloat = stringToNumber(valoreRata);
		var valoreRettaFloat = recordIstanzaSelezionato.importoRetta;
		var giorniFrequenzaDefaultFloat = getValoreFloat(GIORNI_FREQUENZA_DEFAULT);
		var risultato = Math.round( (valoreRataFloat*giorniFrequenzaDefaultFloat)/valoreRettaFloat );
		$('#giorniFrequenza').val(risultato);
	}
}

function calcolaValoreRata(){
	valorizzaImportoCalcolato("");
	var giorniFrequenza = $('#giorniFrequenza').val();
	var giorniFrequenzaFloat = getValoreFloat(giorniFrequenza);
	var giorniFrequenzaDefaultFloat = getValoreFloat(GIORNI_FREQUENZA_DEFAULT);
	var importoRetta = recordIstanzaSelezionato.importoRetta;
	var nuovoValore = (importoRetta/giorniFrequenzaDefaultFloat)*giorniFrequenzaFloat;
	valorizzaImportoCalcolato(formatFloat(nuovoValore));
	calcolaValoreTotale();
}

function valorizzaImportoCalcolato(valoreInput){
	$('p[id="valoreRataHtml"]').html(valoreInput);
	var valoreInputFloat = getValoreFloat(valoreInput);
	$('#valoreRata').val(valoreInputFloat);
}

function calcolaValoreTotale(){
	var valoreInputFloat = stringToNumber($('#valoreRata').val());
	var valoreConguaglioFloat = getValoreFloat($('#valoreConguaglio').val());
	var valoreTotale = (valoreInputFloat+valoreConguaglioFloat);
	$('#importoTotale').html(formatFloat(valoreTotale));
}

function valorizzaInputModal(mappaParametri){
	for(chiave in mappaParametri){
		//var valoreInput = getValoreInputFixed(chiave, mappaParametri);
		var valoreInput = mappaParametri[chiave];
		$('#recordModal').find('.modal-body #'+chiave).val(valoreInput);
		$('#recordModal').find('.modal-body p[id="'+chiave+'"]').html(valoreInput);
	}
}

function rimuoviErroriModal(){
	$("#recordModal").find('input').each(function(){
		var idInput = $(this).attr("id");
		rimuoviErroreInput(idInput);
	});
}

//function getValoreInputFixed(chiave, dataItem){
//	var valoreInput = dataItem[chiave];
//	//fix per la FASCIA
//	if("FASCIA" == chiave && valoreInput){
//		valoreInput = valoreInput.toUpperCase();
//	}
//	return valoreInput;
//}

function getDatiForm(){
	var params = {};
	for (key in recordSelezionato) {
		params[key] = recordSelezionato[key];
	}
	$("#recordModal").find('.inputModal').each(function(){
		var idInput = $(this).attr("id");
		params[idInput] = $(this).val();
	});
	params['valoreConguaglio'] = getValoreFloat(params['valoreConguaglio']);
	params['id'] = recordSelezionato.id;
	return params;
}

//function getDataAttualeString(){
//	var d = new Date()
//	var date = d.toISOString().split('T')[0];
//	var time = d.toTimeString().split(' ')[0];
//	return date+" "+time;
//}

function eseguiQueryInsert() {
	eseguiChiamataAjax('/bpm/v1/instances', getDatiAvvioProcesso(), successChiamataAjax);
}

function getDatiAvvioProcesso(){
	var params = {};
	params['processId'] = 'Aggiorna Rata2.0';
	var datiFormTmp = {};
	datiFormTmp['datiForm'] = getDatiForm();
	params['input'] = datiFormTmp;
	return JSON.stringify(params);
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
			visualizzaMessaggio('validazione-modal-div', '<p>Errore nel salvataggio dati</p>', 'danger');
		},
		complete: function( jqXHR, textStatus ){
			console.log('complete salvataggio dati');
		}
	});
}

function successChiamataAjax(data, textStatus, jqXHR){
	location.reload();
}

function validazione(){
	rimuoviErroriModal();
	var messaggio = "";
	var prosegui="si";
	
	if($('#giorniFrequenza').val()==""){
		aggiungiErroreInput("giorniFrequenza","Inserire un valore per i giorni di frequenza");
		prosegui="no";
	}else{
		var giorniFrequenza = $('#giorniFrequenza').val();
		var giorniFrequenzaFloat = getValoreFloat(giorniFrequenza);
		var giorniFrequenzaDefaultFloat = getValoreFloat(GIORNI_FREQUENZA_DEFAULT);
		if(giorniFrequenzaFloat>giorniFrequenzaDefaultFloat || giorniFrequenzaFloat<0){
			aggiungiErroreInput("giorniFrequenza","Specificare un valore tra 0 e 30");
			prosegui="no";
		}
	}
	if($('#motivazioneVariazione').val()==""){
		aggiungiErroreInput("motivazioneVariazione","Inserire una motivazione");
		prosegui="no";
	}
	//prosegui="no";
	return prosegui;
}

</script>