<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<div class=''>
 <div class='row'>
  <table class='table table-striped table-sm'>
   <thead>
    <tr>
     <th>${(params["facet.UTENTE_MODIFICA.label"])!}</th>
     <th>${(params["facet.DATA_MODIFICA.label"])!}</th>
     <th>${(params["facet.CAUSALE.label"])!}</th>
     <th>${(params["facet.TIPO_MODIFICA.label"])!}</th>
     <th>${(params["facet.VALORE_RATA.label"])!}</th>
	 <th></th>
    </tr>
   </thead>
   <tbody>
   <#list data as item >
    <tr>
     <td>${(item.UTENTE_MODIFICA)!}</td>
     <td>${(item.DATA_MODIFICA_STRING)!}</td>
     <td>${(item.CAUSALE)!}</td>
     <td>${(item.TIPO_MODIFICA)!}</td>
     <td>${(item.VALORE_RATA)!}</td>
	 <td>
      <#if (item.DATI_PRE)! != (item.DATI_POST)! >
	  <a class='editRecord' title="Visualizza dati ${(item.ID_DETTAGLIO_ISCRIZIONE_CENTRO_INFANZIA)!}" data-backdrop='static' data-keyboard='false' data-toggle='modal' data-target='#recordModal' data-idrecord='${item.ID}'><span class='glyphicon glyphicon-info-sign' aria-hidden='true'></span></a>
      </#if>
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
    <div class='row'>
     <div class='form-group col-md-12'>
      <label class='control-label'>Dati Modificati</label>
      <table class='table table-condensed' id='tableDatiModificati'>
       <tr><th class='col-md-4'>Nome propriet&agrave;</th><th class='col-md-4'>Valore precedente</th><th class='col-md-4'>Valore nuovo</th></tr>
      </table>
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
   </div>
  </div>
 </div>
</div>
<script>

var recordSelezionato = {};

$( document ).ready(function() {
	$('#recordModal').on('show.bs.modal', function (event) {
		var button = $(event.relatedTarget);
		var idRecord = button.data('idrecord');
		settaDatiModal(idRecord);
	});
});

function settaDatiModal(idRecord){
	resetDatiModal();
	if(idRecord){
		caricaDati(idRecord);
	}else{

	}
}

function caricaDati(idRecord){
	var params = {};
	params['idRecord'] = idRecord;
	
	$.ajax ({
		method: 'GET',
		//url: '/bpm-server/db/select/CENTRO_INFANZIA_LOG',
		url: '/sispi/v1/centroinfanzia/getDettaglioLog',
		contentType: 'application/json',
		timeout: 100000,
		data: params,
		success: function(data, textStatus, jqXHR){
			if(!data){
				visualizzaMessaggio('validazione-modal-div', '<p>Nessun record trovato</p>', 'danger');
			}else{
				var dataItem = data;
				recordSelezionato = dataItem;
				controllaDatiModificati(dataItem.datiPre, dataItem.datiPost);
				var titoloModal="Visualizza dati dettaglio <strong>"+getTitoloModal(dataItem)+"</strong>";
				$('#recordModal').find('.modal-title').html(titoloModal);
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('Errore nel caricamento dati');
			visualizzaMessaggio('validazione-modal-div', '<p>Errore nel caricamento dati</p>', 'danger');
		},
		complete: function( jqXHR, textStatus ){
			console.log('complete');
			waitingDialog.hide();
		}
	});
}

function getTitoloModal(datiRecord){
	var result = "";
	if(datiRecord["dettaglioIscrizione"]){
		result = datiRecord.dettaglioIscrizione.id;
	}else{
		result = datiRecord.id;
	}
	return result;
}

function controllaDatiModificati(datiPre, datiPost){
	var datiDiversi = false;

	for(chiave in datiPre){
		if(chiave != "iscrizione"){
			if(!isProprietaUguali(datiPost, datiPre, chiave)){
				datiDiversi = true;
				$('#tableDatiModificati').append(getTrTable(chiave, datiPre[chiave], datiPost[chiave]));
			}
		}
	}

	//var datiIscrizionePreObject = datiPre["iscrizione"];
	//var datiIscrizionePostObject = datiPost["iscrizione"];
	//for(chiave in datiIscrizionePreObject){
	//	if(!isProprietaUguali(datiIscrizionePostObject, datiIscrizionePreObject, chiave)){
	//		datiDiversi = true;
	//		$('#tableDatiModificati').append(getTrTable("iscrizione."+chiave, datiIscrizionePreObject[chiave], datiIscrizionePostObject[chiave]));
	//	}
	//}

	if(!datiDiversi){
		visualizzaMessaggio('validazione-modal-div', '<p>Dati non modificati</p>', 'warning');
	}
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

function getTrTable(nomeProprieta, valoreOld, valoreNew) {
	return "<tr><td class='col-md-4'>"+nomeProprieta+"</td><td class='col-md-4'>"+parseHtmlValue(valoreOld)+"</td><td class='col-md-4'>"+parseHtmlValue(valoreNew)+"</td></tr>";
}

function parseHtmlValue(valore){
	var result = "";
	if(valore){
		result = valore;
	}
	return result;
}

function resetDatiModal(){
	rimuoviErroriModal();
	recordSelezionato = {};
	$('#tableDatiModificati tr').not(':first').remove();
	rimuoviMessaggio('validazione-modal-div');
	$("#recordModal").find('.inputModal').each(function(){
		$(this).removeAttr('disabled');
	});
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

</script>