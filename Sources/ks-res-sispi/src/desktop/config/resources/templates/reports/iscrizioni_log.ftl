<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<script type="text/javascript" src="/static/palermo/js/gestione-fascia-istituto.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<div class=''>
 <div class='row'>
  <table class='table table-striped table-condensed' id='risultatoQueryTable'>
   <thead>
    <tr>
     <th>${(params["facet.UTENTE_MODIFICA.label"])!}</th>
     <th>${(params["facet.DATA_MODIFICA.label"])!}</th>
     <th>${(params["facet.CAUSALE.label"])!}</th>
     <th>${(params["facet.ESITO_PRE.label"])!}</th>
     <th>${(params["facet.ESITO_POST.label"])!}</th>
     <th>${(params["facet.MOTIVAZIONE.label"])!}</th>
     <th></th>
    </tr>
   </thead>
   <tbody>
   <#list data as item >
    <tr>
     <td>${(item.UTENTE_MODIFICA)!}</td>
     <td>${(item.DATA_MODIFICA_STRING)!}</td>
	 <td>${(params["facet.CAUSALE."+item.CAUSALE])!}</td>
	 <td>${(params["facet.ESITO."+item.ESITO_PRE])!}</td>
	 <td>${(params["facet.ESITO."+item.ESITO_POST])!}</td>
	 <td class='truncate'>
	  <span title="${(item.MOTIVAZIONE)!}">${(item.MOTIVAZIONE)!}</span>
	 </td>
     <td>
      <#if (item.DATI_RICHIESTA_PRE)! != (item.DATI_RICHIESTA_POST)! >
	  <a class='editRecord' title="Visualizza dati ${(item.ID_ISTANZA_ISCRIZIONE)!}" data-backdrop='static' data-keyboard='false' data-toggle='modal' data-target='#recordModal' data-idrecord='${item.ID_ISTANZA_ISCRIZIONE}'><span class='glyphicon glyphicon-info-sign' aria-hidden='true'></span></a>
      </#if>
     </td>
    </tr>
   </#list>
   </tbody>
  </table>
 </div>
</div>

<div class='modal fade' id='recordModal' role='dialog' aria-labelledby='recordModalLabel'>
 <div class='modal-dialog modal-lg' role='document'>
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

var TABLE_NAME = "ISCRIZIONI_LOG";
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
	//var whereCondition = "ID="+idRecord;
	//var params = {};
	//params['where'] = whereCondition;
	
	$.ajax ({
		method: 'GET',
		url: '/sispi/v1/iscrizionilog/lista?idIstanza='+idRecord,
		//url: '/bpm-server/db/select/'+TABLE_NAME,
		contentType: 'application/json',
		timeout: 100000,
		//data: params,
		success: function(data, textStatus, jqXHR){
			if(data.length==0){
				visualizzaMessaggio('validazione-modal-div', '<p>Nessun record trovato</p>', 'danger');
			}else{
				var dataItem = data[0];
				recordSelezionato = dataItem;
				
				controllaDatiModificati(dataItem.datiRichiestaPre, dataItem.datiRichiestaPost);
				
				//for(chiave in dataItem){
				//	var valoreInput = dataItem[chiave];
				//	$('#recordModal').find('.modal-body #'+chiave).val(valoreInput);
				//	$('#recordModal').find('.modal-body p[id="'+chiave+'"]').html(valoreInput);
				//}

				var titoloModal="Visualizza dati istanza <strong>"+dataItem.idIstanzaIscrizione+"</strong>";
				$('#recordModal').find('.modal-title').html(titoloModal);
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('Errore nel caricamento dati');
			visualizzaMessaggio('validazione-modal-div', 'Errore nel caricamento dati', 'danger');
		},
		complete: function( jqXHR, textStatus ){
			console.log('complete');
			waitingDialog.hide();
		}
	});
}

function controllaDatiModificati(datiPreJson, datiPostJson){
	var datiPre = getObjFromJson(datiPreJson);
	var datiPost = getObjFromJson(datiPostJson);
	var datiDiversi = false;
	for(chiave in datiPre){
		if(!isProprietaEsclusa(chiave) && !isProprietaUguali(datiPost, datiPre, chiave)){
			datiDiversi = true;
			$('#tableDatiModificati').append(getTrTable(chiave, datiPre[chiave], datiPost[chiave]));
		}
	}
	
	if(!datiDiversi){
		visualizzaMessaggio('validazione-modal-div', '<p>Dati non modificati</p>', 'warning');
	}
}

function isProprietaEsclusa(nomeProprieta) {
	let chiaviDaEscludere = ["userinfo", "datiIntegrazione"];
	return chiaviDaEscludere.indexOf(nomeProprieta)>-1;
}

function getObjFromJson(datiJson){
	var result = {};
	if(datiJson){
		result = JSON.parse(datiJson);
	}
	return result;
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
	return "<tr><td class='col-md-4'>"+nomeProprieta+"</td><td class='col-md-4'>"+valoreOld+"</td><td class='col-md-4'>"+valoreNew+"</td></tr>";
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
