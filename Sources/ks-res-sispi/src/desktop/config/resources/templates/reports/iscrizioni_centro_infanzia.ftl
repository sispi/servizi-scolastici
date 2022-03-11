<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/jquery.tokeninput.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<script type="text/javascript" src="/static/palermo/js/gestione-centri-infanzia.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/token-input-bootstrappy.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<div class=''>
 <div class='row'>
  <div class='form-group col-md-2 offset-md-10'>
   <button type='button' class='btn btn-primary' data-toggle='modal' data-target='#recordModal' data-idrecord=''>Aggiungi iscritto</button>
  </div>
 </div>
 <div class='row'>
  <table class='table table-striped table-condensed'>
   <thead>
    <tr>
		 <th><a class="order" href="/reports/report?orderBy=ANNO_SCOLASTICO${querystringParams}">${(params["facet.ANNO_SCOLASTICO.label"])!}</a></th>
		 <th><a class="order" href="/reports/report?orderBy=ESITO_VALUTAZIONE${querystringParams}">${(params["facet.ESITO_VALUTAZIONE.label"])!}</a></th>
		 <th><a class="order" href="/reports/report?orderBy=CF_MINORE${querystringParams}">${(params["facet.CF_MINORE.label"])!}</th>
		 <th><a class="order" href="/reports/report?orderBy=NOME_MINORE${querystringParams}">${(params["facet.NOME_MINORE.label"])!}</a></th>
		 <th><a class="order" href="/reports/report?orderBy=COGNOME_MINORE${querystringParams}">${(params["facet.COGNOME_MINORE.label"])!}</th>
		 <th><a class="order" href="/reports/report?orderBy=CONFERMATA_ISCRIZIONE${querystringParams}">${(params["facet.CONFERMATA_ISCRIZIONE.label"])!}</th>
		 <th><a class="order" href="/reports/report?orderBy=CONFERMA_NOTIFICATA${querystringParams}">${(params["facet.CONFERMA_NOTIFICATA.label"])!}</th>
		 <th></th>
		 <th></th>
	</tr>
    </thead>

	 <tbody>
		<#list data as item >
			<tr>
				<td>${(item.ANNO_SCOLASTICO)!}</td>
				<td>${(item.ESITO_VALUTAZIONE)!}</td>
				<td>${(item.CF_MINORE)!}</td>
				<td>${(item.NOME_MINORE)!}</td>
				<td>${(item.COGNOME_MINORE)!}</td>
				<td>${(item.CONFERMATA_ISCRIZIONE)!}</td>
				<td>${(item.CONFERMA_NOTIFICATA)!}</td>
				<td>
					<a class='editRecord' title="Modifica ${(item.NOME_MINORE)!} ${(item.COGNOME_MINORE)!}" data-backdrop='static' data-keyboard='false' data-toggle='modal' data-target='#recordModal' data-idrecord='${item.ID}'><span class='glyphicon glyphicon-pencil' aria-hidden='true'></span></a>
				</td>					
				<td>					
					<a title='Dettaglio iscrizione ${(item.NOME_MINORE)!} ${(item.COGNOME_MINORE)!}' target='_blank' href='/reports/report?qt=iscrizioni_centro_infanzia_dettaglio&ID_ISCRIZIONE=${(item.ID)!}'><span class='glyphicon glyphicon-list-alt' aria-hidden='true'></span></a>
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
     <h4 class='ml-2'><strong>${(params["facet.ANNO_SCOLASTICO.label"])!}</strong></h4>
    </div>
    <div class='row'>
     <div class='form-group col-md-12'>
      <p class='form-control-static' id='annoScolasticoHtml'></p>
     </div>
    </div>
    <hr class='clearfix' />
    <input type='hidden' id='SALTA_NOTIFICA'>
	<input type='hidden' class='inputModal' id='idRecord'>
    <input type='hidden' class='inputModal' id='annoScolastico'>
    <div class='row'>
     <h4 class='ml-2'>Dati Richiedente</h4>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='nomeRichiedente'>${(params["facet.NOME_RICHIEDENTE.label"])!}</label>
	  <input type='text' class='form-control inputModal' id='nomeRichiedente'>
	 </div>
	 <div class='form-group col-md-6'>
      <label for='cognomeRichiedente'>${(params["facet.COGNOME_RICHIEDENTE.label"])!}</label>
	  <input type='text' class='form-control inputModal' id='cognomeRichiedente'>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='emailUtente'>${(params["facet.EMAIL_UTENTE.label"])!}</label>
	  <input type='text' class='form-control inputModal' id='emailUtente'>
     </div>
     <div class='form-group col-md-6'>
      <label for='iseeRiferimentoStatic'>${(params["facet.ISEE_RIFERIMENTO.label"])!}</label>
	  <p class='form-control-static' id='iseeRiferimentoStatic'></p>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='cfRichiedente'>${(params["facet.CF_RICHIEDENTE.label"])!}</label>
      <input type='text' class='form-control inputModal upperCaseText' id='cfRichiedente'>
     </div>
     <div class='form-group col-md-6 inputModalUpdateRecord'>
      <label for='idFamigliaStatic'>${(params["facet.ID_FAMIGLIA.label"])!}</label>
	  <p class='form-control-static' id='idFamigliaStatic'></p>
     </div>
     <div class='form-group col-md-6 inputModalInsertRecord'>
      <label for='idFamiglia'>${(params["facet.ID_FAMIGLIA.label"])!}</label>
	  <input type='text' class='form-control inputModal upperCaseText' id='idFamiglia'>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='tipoRichiedente'>${(params["facet.TIPO_RICHIEDENTE.label"])!}</label>
      <select class='form-control inputModal' id='tipoRichiedente'>
       <option value='' selected>--Seleziona--</option>
       <option value='Genitore'>Genitore</option>
       <option value='Tutore'>Tutore</option>
       <option value='Casa famiglia'>Casa famiglia</option>
       <option value='Legale rappresentante'>Legale rappresentante</option>
       <option value='Altro genitore'>Altro genitore</option>
      </select>
     </div>
    </div>
    <hr class='clearfix' />
    <div class='row'>
     <h4 class='ml-2'>Dati Minore</h4>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='nomeMinore'>${(params["facet.NOME_MINORE.label"])!}</label>
      <input type='text' class='form-control inputModal' id='nomeMinore'>
     </div>
     <div class='form-group col-md-6'>
      <label for='cognomeMinore'>${(params["facet.COGNOME_MINORE.label"])!}</label>
      <input type='text' class='form-control inputModal' id='cognomeMinore'>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='cfMinore'>${(params["facet.CF_MINORE.label"])!}</label>
      <input type='text' class='form-control inputModal upperCaseText' id='cfMinore'>
     </div>
     <div class='form-group col-md-6'>
      <label for='dataNascitaMinore'>${(params["facet.DATA_NASCITA_MINORE.label"])!}</label>
      <input type='hidden' id='dataNascitaMinoreSubmit' />
      <input type='text' id='dataNascitaMinore' class='form-control inputModal data-input' autocomplete='off' />
     </div>
    </div>
    <hr class='clearfix' />
    <div class='row'>
     <div class='form-group col-md-6 inputModalUpdateRecord'>
      <label for='dataInizioIscrizioneStatic'>${(params["facet.DATA_INIZIO_ISCRIZIONE.label"])!}</label>
	  <p class='form-control-static' id='dataInizioIscrizioneStatic'></p>
     </div>
     <div class='form-group col-md-6 inputModalInsertRecord'>
      <label for='dataInizioIscrizione'>${(params["facet.DATA_INIZIO_ISCRIZIONE.label"])!}</label>
	  <input type='hidden' id='dataInizioIscrizioneSubmit' />
	  <input type='text' id='dataInizioIscrizione' class='form-control inputModal data-input' autocomplete='off' />
     </div>
     <div class='form-group col-md-6'>
      <label for='dataFineIscrizione'>${(params["facet.DATA_FINE_ISCRIZIONE.label"])!}</label>
	  <input type='hidden' id='dataFineIscrizioneSubmit' />
      <input type='text' id='dataFineIscrizione' class='form-control inputModal data-input' autocomplete='off' />
     </div>
    </div>
	<hr class='clearfix' />
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
    <div class='row'>
     <div class='form-group col-md-10 text-center offset-md-1'>
      <p class='form-control-static' id='GESTIONE_FASCIA_ISTITUTO'></p>
     </div>
    </div>
	<hr class='clearfix' />
    <div class='row'>
     <div class='form-group col-md-6'>
      <label class='control-label'>Dati Tariffa</label>
      <table class='table table-condensed' id='tableTariffa'>
      </table>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='valoreRata'>${(params["facet.VALORE_RATA.label"])!}</label>
      <p class='form-control-static' id='valoreRata'></p>
     </div>
    </div>
    <div class='row'>
     <div class='col-md-3 offset-md-5'>
      <button type='button' class='btn btn-primary' id='btnRicalcolaTariffa'>Ricalcola retta</button>
     </div>
    </div>
    <hr class='clearfix' />
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='confermataIscrizione'>${(params["facet.CONFERMATA_ISCRIZIONE.label"])!}</label>
      <select class='form-control inputModal' id='confermataIscrizione'>
       <option value='' selected>--Seleziona--</option>
       <option value='C'>Confermata</option>
       <option value='NC'>Non confermata</option>
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
   </div>
  </div>
 </div>
</div>

<script>

var recordSelezionato = {};
//var NOTIFICA_ISCRIZIONE_CENTRO_INFANZIA_URL = '/bpm-server/process/startProcess/Notifica Iscrizione Centro Infanzia1.0/Notifica Iscrizione Centro Infanzia';
var AVVIA_ISTANZA_URL = '/bpm/v1/instances';

$( document ).ready(function() {

	$('#btnRicalcolaTariffa').on('click', function() {
		var vecchioValoreConfermataIscrizione = $('#confermataIscrizione').val();
		$('#confermataIscrizione').val('C');
		if(validazione()=="si"){
			var callback = function() {
				$('#SALTA_NOTIFICA').val('S');
				eseguiInsertInviaNotifica();
			};
			confirmKS('Conferma', '<p>Salvare i dati e calcolare la tariffa?</p>', 'Cancella', 'Conferma', callback);
		}else{
			$('#confermataIscrizione').val(vecchioValoreConfermataIscrizione);
		}
	});

	$('#btnSalvaRecord').on('click', function() {
		if(validazione()=="si"){
			var callback = function() {
				$('#SALTA_NOTIFICA').val('');
				eseguiInsertInviaNotifica();
			};
			confirmKS('Conferma', '<p>'+getMessaggioSalvataggioDati()+'</p>', 'Cancella', 'Conferma', callback);
		}
	});

	$('#recordModal').on('show.bs.modal', function (event) {
		var button = $(event.relatedTarget);
		var idRecord = button.data('idrecord');
		settaDatiModal(idRecord);
	});

	initDateInput();

	$('#fascia').on('change', function() {
		ricaricaListaFasciaOraria();
    });

	$('#fasciaOraria').on('change', function() {
		ricaricaDatiTariffe();
    });
	
	$('#cfMinore').on('change', function() {
		caricaDatiIsee($(this).val());
    });

	caricaElencoTariffe();
	settaQueryParams();
});

function settaQueryParams(){
	const urlParams = new URLSearchParams(window.location.search);
	for(var key of urlParams.keys()) {
		if(urlParams.has(key)){
			//$('#'+key).val(urlParams.get(key));
			console.log("Param: "+key+" - Value: "+urlParams.get(key));
			if("ANNO_SCOLASTICO"==key) {
				$('#titoloReport').html("Lista Conferma Iscrizione "+urlParams.get(key));
			}
		}
	}
}

function ricaricaDatiTariffe(){
	cancellaDatiTariffa();
	var iseeRiferimento = $('#iseeRiferimentoStatic').html();
	if(!isNaN(iseeRiferimento)){
		var annoScolasticoFilter = getAnnoRiferimentoByAnnoScolastico($('#annoScolastico').val());
		console.log("annoScolasticoFilter: "+annoScolasticoFilter);
		var result = ASILOTARIFFE_TABLE.filter(record => $('#nomeIstituto').val()==record.denominazioneAsilo);
		result = result.filter(record => $('#fascia').val()==record.categoria);
		result = result.filter(record => $('#fasciaOraria').val()==record.fasciaOraria1);
		result = result.filter(record => annoScolasticoFilter==record.annoRiferimento);
		result = getTariffaByFasciaOraria(result);
		settaDatiTariffa(result, iseeRiferimento);
	}
}

function getAnnoRiferimentoByAnnoScolastico(annoScolastico){
	var result = null;
	if(!!annoScolastico){
		result = annoScolastico.substring(0, 4);
	}
	return result;
}

function settaDatiTariffa(datiTariffe, iseeRiferimento){
	for (i=0; i<datiTariffe.length; i++){
		var singoloElemento = datiTariffe[i];
		$('#tableTariffa').append(getTrTable(singoloElemento, 6, iseeRiferimento));
	}
}

function getTrTable(singoloElemento, bootstrapCol, iseeRiferimento) {
	var headCol = 1;
	if(bootstrapCol){
		headCol = bootstrapCol;
	}
	var bodycol = 12-headCol;
	var trColor="";
	var valoreIseeFloat = stringToNumber(iseeRiferimento);
	var iseeDa = getValoreObject(singoloElemento, "ISEEDA");
	if(iseeDa==""){
		iseeDa = 0;
	}
	var iseeA = getValoreObject(singoloElemento, "ISEEA");
	if(iseeA==""){
		iseeA = Number.MAX_VALUE;
	}
	if(iseeDa<=valoreIseeFloat && iseeA>=valoreIseeFloat){
		trColor="background-color: #5aff4b";
	}
	return "<tr><th class='col-md-"+headCol+"' style='text-align: right;"+trColor+"'>"+singoloElemento.ISEEDA+" - "+singoloElemento.ISEEA+"</th><td class='col-md-"+bodycol+"' style='text-align: right;"+trColor+"'>"+singoloElemento.IMPORTO+"</td></tr>";
}

function getTariffaByFasciaOraria(datiAsiloTariffeFiltrata){
	var fasciaOraria = $('#fasciaOraria').val();
	var risultato = [];
	if(!!fasciaOraria){
		for (i=0; i<datiAsiloTariffeFiltrata.length; i++){
			var singoloRisultato = {};
			var singoloElemento = datiAsiloTariffeFiltrata[i];
			singoloRisultato["ISEEDA"] = getValoreObject(singoloElemento, "iseeDa");
			singoloRisultato["ISEEA"] = getValoreObject(singoloElemento, "iseeA");

			if(fasciaOraria==singoloElemento.fasciaOraria1){
				singoloRisultato["IMPORTO"] = getValoreObject(singoloElemento, "importoFasciaOraria1");
			}else if(fasciaOraria==singoloElemento.fasciaOraria2){
				singoloRisultato["IMPORTO"] = getValoreObject(singoloElemento, "importoFasciaOraria2");
			}else if(fasciaOraria==singoloElemento.fasciaOraria3){
				singoloRisultato["IMPORTO"] = getValoreObject(singoloElemento, "importoFasciaOraria3");
			}else if(fasciaOraria==singoloElemento.fasciaOraria4){
				singoloRisultato["IMPORTO"] = getValoreObject(singoloElemento, "importoFasciaOraria4");
			}else if(fasciaOraria==singoloElemento.fasciaOraria5){
				singoloRisultato["IMPORTO"] = getValoreObject(singoloElemento, "importoFasciaOraria5");
			}
			risultato.push(singoloRisultato);
		}
		risultato.sort(ordinaTariffe);
	}
	return risultato;
}

function ordinaTariffe(a, b){
	if(a.ISEEDA=="" || b.ISEEA==""){
		return -1;
	}
	if(a.ISEEA=="" || b.ISEEDA==""){
		return 1;
	}
	return a.ISEEA-b.ISEEDA;
}

function getValoreObject(obj, chiave){
	var result = "";
	if(chiave in obj && obj[chiave]){
		result = obj[chiave];
	}
	return result;
}

function getMessaggioSalvataggioDati(){
	var messaggio = "Salvare i dati";
	var datiForm = getDatiForm();
	if("S"==recordSelezionato["confermaNotificata"] && eseguiRicalcoloRate(datiForm, recordSelezionato)){
		messaggio += " e ricalcolare le rate";
	}else if("S"!=recordSelezionato["confermaNotificata"] && checkEsitoValutazione(datiForm)){
		messaggio += " e avvia il processo di notifica iscrizione";
	}
	messaggio += "?";
	return messaggio;
}

function caricaDatiIsee(cfMinore){
	if(!!cfMinore){
		$.ajax ({
			method: 'GET',
			//url: '/bpm-server/db/select/ISEE',
			url: '/sispi/v1/isee/lista/'+cfMinore,
			contentType: 'application/json',
			//data: params,
			timeout: 100000,
			success: function(data, textStatus, jqXHR){
				if(data.length==0){
					console.log("Nessun isee trovato");
					$('#recordModal').find('.modal-body #iseeRiferimentoStatic').html("<div class='row'><div class='col-md-12'>Isee non presente <a target='_blank' href='/reports/report?qt=gestione_isee'>Gestione Isee</a></div></div><div class='row'><div class='col-md-12'><div class='alert alert-info'>Dopo aver inserito l'isee &egrave; necessario ricaricare questa pagina</div></div></div>");
					//visualizzaMessaggio('validazione-div', '<p>Nessun record trovato</p>', 'warning');
				}else{
					var dataItem = data[0];
					$('#recordModal').find('.modal-body #iseeRiferimentoStatic').html(dataItem.importo);
				}
				ricaricaDatiTariffe();
			},
			error: function(jqXHR, textStatus, errorThrown) {
				console.log('Errore nel caricamento ISEE');
				visualizzaMessaggio('validazione-div', '<p>Errore nel caricamento dati ISEE</p>', 'danger');
			},
			complete: function( jqXHR, textStatus ){
				console.log('complete');
			}
		});
	}
}

//function caricaDatiDettaglio(idRecordPrincipale){
//	var params = {};
//	params['where'] = "ID_ISCRIZIONE_CENTRO_INFANZIA = "+idRecordPrincipale;
//	params['orderBy'] = "SORT";
//	$.ajax ({
//		method: 'GET',
//		url: '/bpm-server/db/select/PORTSCU_DETTAGLIO_ISCRIZIONI_CENTRO_INFANZIA',
//		contentType: 'application/json',
//		data: params,
//		timeout: 100000,
//		success: function(data, textStatus, jqXHR){
//			if(data.length==0){
//				console.log("Nessun dettaglio trovato");
//				visualizzaMessaggio('validazione-modal-div', '<p>Nessun dettaglio trovato</p>', 'warning');
//			}else{
//				var dataItem = data[0];
//				var titoloModal="Conferma iscrizione centro infanzia <strong>"+dataItem.NOME_ISTITUTO+"</strong>";
//				$('#recordModal').find('.modal-title').html(titoloModal);
//				nomeIstitutoSelect(dataItem.NOME_ISTITUTO);
//				for(chiave in dataItem){
//					var valoreInput = dataItem[chiave];
//					$('#recordModal').find('.modal-body #'+chiave).val(valoreInput);
//					$('#recordModal').find('.modal-body p[id="'+chiave+'"]').html(valoreInput);
//				}
//				ricaricaListaFasciaOraria(dataItem.FASCIA_ORARIA);
//				ricaricaDatiTariffe();
//			}
//		},
//		error: function(jqXHR, textStatus, errorThrown) {
//			console.log('Errore nel caricamento ISEE');
//			visualizzaMessaggio('validazione-modal-div', '<p>Errore nel caricamento dati dettaglio</p>', 'danger');
//		},
//		complete: function( jqXHR, textStatus ){
//			console.log('complete');
//			waitingDialog.hide();
//		}
//	});
//}

function settaDatiModal(idRecord){
	resetDatiModal();
	if(idRecord){
		caricaDati(idRecord);
	}else{
		initNuovoInserimento();
	}
}

function initNuovoInserimento(){
	var titoloModal="Nuova iscrizione";
	var annoScolasticoAttuale = "2021/2022";
	$('#recordModal').find('.modal-title').html(titoloModal);
	$('#recordModal').find('.modal-body #confermataIscrizione').val('C');
	$('#recordModal').find('.modal-body #annoScolasticoHtml').html(annoScolasticoAttuale);
	$('#recordModal').find('.modal-body #annoScolastico').val(annoScolasticoAttuale);
	$('#recordModal').find('.modal-body #confermataIscrizione').attr('disabled', 'disabled');
	$('.inputModalInsertRecord').show();
	recordSelezionato["annoScolastico"]=annoScolasticoAttuale;
}

function resetDatiModal(){
	rimuoviErroriModal();
	recordSelezionato = {};
	nomeIstitutoCancel();
	rimuoviMessaggio('validazione-modal-div');
	$("#recordModal").find('.inputModal').each(function(){
		$(this).removeAttr('disabled');
	});

	$('#annoScolasticoHtml').html('');
	$('#valoreRata').html('');
	$('#btnSalvaRecord').removeAttr('disabled');
	$('#btnRicalcolaTariffa').removeAttr('disabled');
	//$('.datiAsiloNido').hide();
	//$('.datiInfanzia').hide();
	$('.inputModalUpdateRecord').hide();
	$('.inputModalInsertRecord').hide();
	$('#iseeRiferimentoStatic').html('');
	$('#GESTIONE_FASCIA_ISTITUTO').html('');
}

function caricaDati(idRecord){
	var params = {};
	params['idIscrizione'] = idRecord;
	$.ajax ({
		method: 'GET',
		//url: '/bpm-server/db/select/ISCRIZIONI_CENTRO_INFANZIA',
		url: '/sispi/v1/centroinfanzia/getDettaglio',
		contentType: 'application/json',
		timeout: 100000,
		data: params,
		success: function(data, textStatus, jqXHR){
			if(data.length==0){
				visualizzaMessaggio('validazione-div', '<p>Nessun record trovato</p>', 'danger');
			}else{
				var dataItem = data[0];
				recordSelezionato = dataItem.iscrizione;
				$('#recordModal').find('.modal-body #idRecord').val(recordSelezionato.id);

				nomeIstitutoSelect(dataItem.nomeIstituto);
				valorizzaInputModal(dataItem);
				valorizzaInputModal(recordSelezionato);
				var dataInizioObj = convertStringToDate("2021-09-01");
				if(recordSelezionato.dataInizioIscrizione){
					dataInizioObj = convertStringToDate(recordSelezionato.dataInizioIscrizione);	
				}
				$('#dataInizioIscrizione').datepicker("setDate", dataInizioObj);
				$('#recordModal').find('.modal-body #dataInizioIscrizioneStatic').html(convertDateToString(dataInizioObj));

				if(recordSelezionato.dataFineIscrizione){
					var dataFineObj = convertStringToDate(recordSelezionato.dataFineIscrizione);
					$('#dataFineIscrizione').datepicker("setDate", dataFineObj);
				}

				if(recordSelezionato.dataNascitaMinore){
					var dataNascitaObj = convertStringToDate(recordSelezionato.dataNascitaMinore);
					$('#dataNascitaMinore').datepicker("setDate", dataNascitaObj);
				}

				$('#recordModal').find('.modal-body #idFamigliaStatic').html(recordSelezionato.idFamiglia);
				$('#recordModal').find('.modal-body #annoScolasticoHtml').html(recordSelezionato.annoScolastico);

				var titoloModal="Conferma iscrizione centro infanzia <strong>"+dataItem.nomeIstituto+"</strong>";
				$('#recordModal').find('.modal-title').html(titoloModal);
				ricaricaListaFasciaOraria(dataItem.fasciaOraria);
				ricaricaDatiTariffe();

				if("S" == recordSelezionato.confermaNotificata){
					$('#recordModal').find('.modal-body #confermaNotificata').attr('disabled', 'disabled');
					$('#btnRicalcolaTariffa').attr('disabled', 'disabled');
				}

				if("C" == recordSelezionato.confermataIscrizione){
					$('#recordModal').find('.modal-body #nomeIstituto').attr('disabled', 'disabled');
					$('#recordModal').find('.modal-body #fascia').attr('disabled', 'disabled');
					$('#recordModal').find('.modal-body #fasciaOraria').attr('disabled', 'disabled');
					$('#recordModal').find('.modal-body #GESTIONE_FASCIA_ISTITUTO').html("<div class='row'><div class='col-md-12 alert alert-info '>Per modificare i dati di istituto e fascia oraria vai alla <a target='_blank' href='/reports/report?qt=iscrizioni_centro_infanzia_dettaglio&ID_ISCRIZIONE="+recordSelezionato.id+"'>Gestione Dettaglio</a></div></div><div class='row'></div>");
				}

				if("S" == recordSelezionato.ricalcoloRate){
					$("#recordModal").find('.inputModal').each(function(){
						$(this).attr('disabled', 'disabled');
					});
					$('#btnSalvaRecord').attr('disabled', 'disabled');
					visualizzaMessaggio('validazione-modal-div', '<p>Attenzione: &egrave; in corso il ricalcolo delle rate per l\'iscritto selezionato. Attendere il termine del ricalcolo prima di poter modificare nuovamente i dati</p>', 'warning');
				}
				$('.inputModalUpdateRecord').show();
				caricaDatiIsee(recordSelezionato.cfMinore);
				//caricaDatiDettaglio(dataItem.ID);
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
	
	for (key in recordSelezionato) {
		params[key] = recordSelezionato[key];
	}

	$("#recordModal").find('.inputModal').each(function(){
		var idInput = $(this).attr("id");
		params[idInput] = $(this).val().trim();
	});

	if(!recordSelezionato["idFamiglia"] && !params["idFamiglia"]){
		params["idFamiglia"]="0";
	}

	if(checkEsitoValutazione(params)){
		params['esitoValutazione'] = "AMMESSO";
	}
	
	params['dataInizioIscrizione'] = $('#dataInizioIscrizioneSubmit').val();
	params['dataFineIscrizione'] = $('#dataFineIscrizioneSubmit').val();
	params['dataNascitaMinore'] = $('#dataNascitaMinoreSubmit').val();
	params['id'] = $('#idRecord').val();
	
	return params;
}

function checkEsitoValutazione(recordModificato) {
	var result = false;
	if("confermataIscrizione" in recordModificato && "C"==recordModificato['confermataIscrizione']){
		result = true;
	}
	return result;
}

function eseguiRicalcoloRate(recordModificato, recordOriginale) {
	var result = false;
	//solo se CONFERMA_NOTIFICATA == "S"
	if("S" == recordOriginale.confermaNotificata){
		if(!isProprietaUguali(recordModificato, recordOriginale, "dataFineIscrizione")){
			result = true;
		}
	}
	if(!isIseeValido()){
		result = false;
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

function eseguiInsertInviaNotifica(){
	var data = getDatiForm();
	var urlToCall='';
	var datiAvvioProcesso = getDatiAvvioProcesso();
	if("S"==$('#SALTA_NOTIFICA').val() && isIseeValido()){
		datiAvvioProcesso['input']['saltaNotifica']="S";
	}
	eseguiChiamataAjax(AVVIA_ISTANZA_URL, JSON.stringify(datiAvvioProcesso), successChiamataAjax);
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

function isIseeValido(){
	var iseeValido = true;
	var iseePresente = isValoreFloat($('#iseeRiferimentoStatic').html());
	if(!iseePresente){
		iseeValido = false;
	}
	return iseeValido;
}

function getDatiAvvioProcesso(){
	var params = {};
	params['processId'] = 'Notifica Iscrizione Centro Infanzia2.0';
	var datiFormTmp = {};
	var datiForm = getDatiForm();
	datiForm['ricalcoloRate'] = "S";
	datiFormTmp['datiForm'] = datiForm;
	datiFormTmp['istanzaOriginale'] = JSON.stringify(recordSelezionato);
	params['input'] = datiFormTmp;
	return params;
}

function fixDatiForm(datiObj){
	var result = datiObj;
	return result;
}

function successChiamataAjax(data, textStatus, jqXHR){
	location.reload();
}

function valorizzaInputModal(mappaParametri){
	for(chiave in mappaParametri){
		var valoreInput = mappaParametri[chiave];
		$('#recordModal').find('.modal-body #'+chiave).val(valoreInput);
		$('#recordModal').find('.modal-body p[id="'+chiave+'"]').html(valoreInput);
	}
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

	var cfRichiedente = $('#cfRichiedente').val().trim();
	if( cfRichiedente!="" && !verificaCodiceFiscale(cfRichiedente)){
		aggiungiErroreInput("cfRichiedente","Codice fiscale richiedente non valido");
		prosegui="no";
	}

	if($('#confermataIscrizione').val()=="C"){
		if($('#cfMinore').val()==""){
			aggiungiErroreInput("cfMinore","Codice fiscale iscritto obbligatorio");
			prosegui="no";
		}
		if($('#nomeMinore').val()==""){
			aggiungiErroreInput("nomeMinore","Nome iscritto obbligatorio");
			prosegui="no";
		}
		if($('#cognomeMinore').val()==""){
			aggiungiErroreInput("cognomeMinore","Cognome iscritto obbligatorio");
			prosegui="no";
		}
		if($('#emailUtente').val()==""){
			aggiungiErroreInput("emailUtente","Email richiedente obbligatoria");
			prosegui="no";
		}
		if($('#nomeRichiedente').val()==""){
			aggiungiErroreInput("nomeRichiedente","Nome richiedente obbligatorio");
			prosegui="no";
		}
		if($('#cognomeRichiedente').val()==""){
			aggiungiErroreInput("cognomeRichiedente","Cognome richiedente obbligatorio");
			prosegui="no";
		}
		if(cfRichiedente==""){
			aggiungiErroreInput("cfRichiedente","C.F. richiedente obbligatorio");
			prosegui="no";
		}
		if($('#idRecord').val()=="" && $('#dataInizioIscrizioneSubmit').val()==""){
			aggiungiErroreInput("dataInizioIscrizione","Data inizio iscrizione obbligatoria");
			prosegui="no";
		}
		if($('#dataFineIscrizione').val()==""){
			aggiungiErroreInput("dataFineIscrizione","Data fine iscrizione obbligatoria");
			prosegui="no";
		}else if(!isIntervalloDataFineValida($("#dataFineIscrizione").datepicker("getDate"), $('#annoScolastico').val())){
			aggiungiErroreInput("dataFineIscrizione","Data fine iscrizione non valida");
			prosegui="no";
		}
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
	}
	if("no"==prosegui){
		visualizzaMessaggio('validazione-modal-div', '<p>Non &egrave; possibile confermare l\'iscrizione. Dati obbligatori mancanti</p>', 'danger');
	}
	if(!isIseeValido()){
		visualizzaMessaggio('validazione-modal-div', '<p>Non &egrave; possibile confermare l\'iscrizione. Isee non valido</p>', 'danger');
		prosegui="no";
	}
	//prosegui="no";
	return prosegui;
}

function isIntervalloDataFineValida(dataFineValidita, annoScolastico){
	var result = false;
	var annoScolasticoBase = getAnnoRiferimentoByAnnoScolastico(annoScolastico);
	var annoScolasticoLimite = parseInt(annoScolasticoBase) + 1;
	var dataMinima = convertStringToDate(annoScolasticoBase+"-09-01");
	var dataMassima = convertStringToDate(annoScolasticoLimite+"-07-31");
	if(dataMinima<=dataFineValidita && dataMassima>=dataFineValidita){
		var result = true;
	}
	return result;
}

function initDateInput(){
	$.datepicker.setDefaults({
		altFormat: "yy-mm-dd",
		dateFormat:'dd/mm/yy'
    });
	$('#dataNascitaMinore').datepicker({
		altField: "#dataNascitaMinoreSubmit"
	});
	$('#dataInizioIscrizione').datepicker({
		altField: "#dataInizioIscrizioneSubmit"
	});
	$('#dataFineIscrizione').datepicker({
		altField: "#dataFineIscrizioneSubmit"
	});
}

</script>