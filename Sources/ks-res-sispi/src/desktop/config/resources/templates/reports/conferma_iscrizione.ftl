<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<script type="text/javascript" src="/static/palermo/js/gestione-fascia-istituto.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<div class=''>
 <div class='row'>
  <div class='form-group col-md-2 offset-md-10'>
   <button type='button' class='btn btn-primary' data-toggle='modal' data-target='#recordModal' data-idrecord=''>Aggiungi iscritto</button>
  </div>
 </div>
 <div class='row'>
  <table class='table table-striped table-sm'>
   <thead>
    <tr>
	     <th><a class="order" href="/reports/report?orderBy=ANNO_SCOLASTICO${querystringParams}">${(params["facet.ANNO_SCOLASTICO.label"])!}</a></th>
		 <th><a class="order" href="/reports/report?orderBy=ESITO_VALUTAZIONE${querystringParams}">${(params["facet.ESITO_VALUTAZIONE.label"])!}</a></th>
		 <th><a class="order" href="/reports/report?orderBy=NOME_ISTITUTO${querystringParams}">${(params["facet.NOME_ISTITUTO.label"])!}</th>
		 <th><a class="order" href="/reports/report?orderBy=FASCIA${querystringParams}">${(params["facet.FASCIA.label"])!}</th>
		 <th><a class="order" href="/reports/report?orderBy=CF_MINORE_ANAGRAFE${querystringParams}">${(params["facet.CF_MINORE_ANAGRAFE.label"])!}</th>
		 <th><a class="order" href="/reports/report?orderBy=NOME_MINORE${querystringParams}">${(params["facet.NOME_MINORE.label"])!}</a></th>
		 <th><a class="order" href="/reports/report?orderBy=COGNOME_MINORE${querystringParams}">${(params["facet.COGNOME_MINORE.label"])!}</th>
		 <th><a class="order" href="/reports/report?orderBy=CONFERMATA_ISCRIZIONE${querystringParams}">${(params["facet.CONFERMATA_ISCRIZIONE.label"])!}</th>
		 <th><a class="order" href="/reports/report?orderBy=CONFERMA_NOTIFICATA${querystringParams}">${(params["facet.CONFERMA_NOTIFICATA.label"])!}</th>
		 <th></th>
		 <th></th>
		 <th></th>
	</tr>
    </thead>

	 <tbody>
		<#list data as item >
			<tr>
				<td>${(item.ANNO_SCOLASTICO)!}</td>
				<td>${(item.ESITO_VALUTAZIONE)!}</td>
				<td>${(item.NOME_ISTITUTO)!}</td>
				<td>${(item.FASCIA)!}</td>
				<td>${(item.CF_MINORE_ANAGRAFE)!}</td>
				<td>${(item.NOME_MINORE)!}</td>
				<td>${(item.COGNOME_MINORE)!}</td>
				<td>${(item.CONFERMATA_ISCRIZIONE)!}</td>
				<td>${(item.CONFERMA_NOTIFICATA)!}</td>
				<td>
					<a class='editRecord' title="Modifica ${(item.NOME_MINORE)!} ${(item.COGNOME_MINORE)!}" data-backdrop='static' data-keyboard='false' data-toggle='modal' data-target='#recordModal' data-idrecord='${item.ID_COD}'><span class='glyphicon glyphicon-pencil' aria-hidden='true'></span></a>
				</td>
				<td>
				 <#if "Asilo Nido" == (item.TIPO_PROCEDIMENTO)! && ""!=(item.CF_MINORE_ANAGRAFE)!>
					<a class='editRecord' title="Rate ${(item.NOME_MINORE)!} ${(item.COGNOME_MINORE)!}" target='_blank' href="/reports/report?qt=modifica_rata&title=Piano%20Tariffario%20-%20${(item.NOME_MINORE)!}%20${(item.COGNOME_MINORE)!}&CF_ISCRITTO=${(item.CF_MINORE_ANAGRAFE)!}&ANNO_SCOLASTICO=${(item.ANNO_SCOLASTICO)!}"><span class='glyphicon glyphicon-folder-open' aria-hidden='true'></span></a>
				 </#if>
				</td>
				<td>
				 <#if "Asilo Nido" == (item.TIPO_PROCEDIMENTO)! && ""!=(item.CF_MINORE_ANAGRAFE)!>
					<a class='editRecord' title="Pagamenti ${(item.NOME_MINORE)!} ${(item.COGNOME_MINORE)!}" target='_blank' href="/reports/report?qt=pagamenti_retta_scolastica&title=Pagamenti%20-%20${(item.NOME_MINORE)!}%20${(item.COGNOME_MINORE)!}&CF_MINORE=${(item.CF_MINORE_ANAGRAFE)!}"><span class='glyphicon glyphicon-euro' aria-hidden='true'></span></a>
				 </#if>
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
     <h4 class='ml-2'><strong>Nome procedimento</strong></h4>
    </div>
    <div class='row'>
     <div class='form-group col-md-12'>
      <p class='form-control-static' id='nomeProcedimento'></p>
     </div>
    </div>
    <div class='row'>
     <h4 class='ml-2'><strong>${(params["facet.ANNO_SCOLASTICO.label"])!}</strong></h4>
    </div>
    <div class='row'>
     <div class='form-group col-md-12'>
      <p class='form-control-static' id='annoScolasticoStatic'></p>
     </div>
    </div>
    <hr class='clearfix' />
    <input type='hidden' id='SALTA_NOTIFICA'>
	<input type='hidden' class='inputModal' id='tipoProcedimento'>
	<input type='hidden' class='inputModal' id='dpsFlgvalutazione'>
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
     <div class='form-group col-md-6 datiAsiloNido'>
      <label for='iseeRiferimentoStatic'>${(params["facet.ISEE_RIFERIMENTO.label"])!}</label>
	  <p class='form-control-static' id='iseeRiferimentoStatic'></p>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='cfContribuente'>${(params["facet.DPS_CFRICHIEDENTE.label"])!}</label>
      <input type='text' class='form-control inputModal upperCaseText' id='cfContribuente'>
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
      <label for='cfMinore'>${(params["facet.CF_MINORE_ANAGRAFE.label"])!}</label>
      <input type='text' class='form-control inputModal upperCaseText' id='cfMinore'>
     </div>
    </div>
    <hr class='clearfix datiAsiloNido' />
    <div class='row datiAsiloNido'>
     <div class='form-group col-md-6 inputModalUpdateRecord'>
      <label for='dataInizioIscrizioneStatic'>${(params["facet.DATA_INIZIO_ISCRIZIONE.label"])!}</label>
	  <p class='form-control-static' id='dataInizioIscrizioneStatic'></p>
     </div>
     <div class='form-group col-md-6 inputModalInsertRecord'>
      <label for='dataInizioIscrizione'>${(params["facet.DATA_INIZIO_ISCRIZIONE.label"])!}</label>
	  <input type='text' id='dataInizioIscrizione' class='form-control inputModal data-input' autocomplete='off' />
     </div>
     <div class='form-group col-md-6'>
      <label for='dataFineIscrizione'>${(params["facet.DATA_FINE_ISCRIZIONE.label"])!}</label>
      <input type='text' id='dataFineIscrizione' class='form-control inputModal data-input' autocomplete='off' />
     </div>
    </div>
	<hr class='clearfix' />
    <div class='row'>
     <h4 class='ml-2'>Dati Istituto</h4>
    </div>
    <div class='row datiAsiloNido'>
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
    <div class='row datiAsiloNido'>
     <div class='form-group col-md-10 text-center offset-md-1'>
      <p class='form-control-static' id='gestioneFasciaIstituto'></p>
     </div>
    </div>
	<hr class='clearfix datiAsiloNido' />
    <div class='row datiAsiloNido'>
     <div class='form-group col-md-6'>
      <label class='control-label'>Dati Tariffa</label>
      <table class='table table-sm' id='tableTariffa'>
      </table>
     </div>
     <div class='form-group col-md-6'>
      <label class='control-label'>${(params["facet.SCONTO_FAMIGLIA.label"])!}</label>
      <p class='form-control-static' id='scontoFamiglia'></p>
     </div>
    </div>
    <div class='row datiAsiloNido'>
     <div class='form-group col-md-6'>
      <label class='control-label'>${(params["facet.IMPORTO_RETTA.label"])!}</label>
      <p class='form-control-static' id='importoRetta'></p>
     </div>
    </div>
    <div class='row datiAsiloNido'>
     <div class='col-md-4 offset-md-5'>
      <button type='button' class='btn btn-primary' id='btnRicalcolaTariffa'>Ricalcola retta</button>
     </div>
    </div>
    <div class='row datiInfanzia'>
     <div class='form-group col-md-6'>
      <label for='nomeIstitutoInfanzia'>${(params["facet.NOME_ISTITUTO.label"])!}</label>
      <input type='text' id='nomeIstitutoInfanzia' class='form-control inputModal upperCaseText' autocomplete='off' />
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

//var TABLE_NAME = "PORTSCU_ISTANZE";
var IMPORTO_ISEE_PER_SCONTO = 15000;
var recordSelezionato = {};
var codiciTipoProcedimentoAsilo = ["4","7","8"];
var AVVIA_ISTANZA_URL = '/bpm/v1/instances';

$( document ).ready(function() {

	$('#btnRicalcolaTariffa').on('click', function() {
		var vecchioValoreConfermataIscrizione = $('#confermataIscrizione').val();
		$('#confermataIscrizione').val('C');
		if(validazione()=="si"){
			var callback = function() {
				$('#SALTA_NOTIFICA').val('S');
				//waitingDialog.show('');
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
				//waitingDialog.show('');
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

	$('#ricaricaNomeIstituto').on('click', function() {
		initAutoCompleteNomeIstituto();
	});

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

function resetDatiScuola(){
	$('#nomeIstituto').val('');
	$('#fascia').val('');
	$('#fasciaOraria').val('');
	$('#nomeIstitutoInfanzia').val('');
	ricaricaDatiTariffe();
}

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
	var annoScolasticoFilter = getAnnoRiferimentoByAnnoScolastico($('#annoScolastico').val());
	var result = ASILOTARIFFE_TABLE.filter(record => $('#nomeIstituto').val()==record.denominazioneAsilo);
	result = result.filter(record => $('#fascia').val()==record.descrizioneCategoria);
	result = result.filter(record => annoScolasticoFilter==record.annoRiferimento);
	result = getTariffaByFasciaOraria(result);
	settaDatiTariffa(result);
}

function getAnnoRiferimentoByAnnoScolastico(annoScolastico){
	var result = null;
	if(!!annoScolastico){
		result = annoScolastico.substring(0, 4);
	}
	return result;
}

function settaDatiTariffa(datiTariffe){
	for (i=0; i<datiTariffe.length; i++){
		var singoloElemento = datiTariffe[i];
		$('#tableTariffa').append(getTrTable(singoloElemento, 6));
	}
}

function getTrTable(singoloElemento, bootstrapCol) {
	var headCol = 1;
	if(bootstrapCol){
		headCol = bootstrapCol;
	}
	var bodycol = 12-headCol;
	var trColor="";
	var valoreIseeFloat = stringToNumber($('#iseeRiferimentoStatic').html());
	var iseeDa = getValoreObject(singoloElemento, "iseeDa");
	if(iseeDa==""){
		iseeDa = 0;
	}
	var iseeA = getValoreObject(singoloElemento, "iseeA");
	if(iseeA==""){
		iseeA = Number.MAX_VALUE;
	}
	if(iseeDa<=valoreIseeFloat && iseeA>=valoreIseeFloat){
		trColor="background-color: #5aff4b";
	}
	return "<tr><th class='col-md-"+headCol+"' style='text-align: right;"+trColor+"'>"+singoloElemento.iseeDa+" - "+singoloElemento.iseeA+"</th><td class='col-md-"+bodycol+"' style='text-align: right;"+trColor+"'>"+singoloElemento.importo+"</td></tr>";
}

function getTariffaByFasciaOraria(datiAsiloTariffeFiltrata){
	var fasciaOraria = $('#fasciaOraria').val();
	var risultato = [];
	if(!!fasciaOraria){
		for (i=0; i<datiAsiloTariffeFiltrata.length; i++){
			var singoloRisultato = {};
			var singoloElemento = datiAsiloTariffeFiltrata[i];
			singoloRisultato["iseeDa"] = getValoreObject(singoloElemento, "importoIseeDa");
			singoloRisultato["iseeA"] = getValoreObject(singoloElemento, "importoIseeA");

			if(fasciaOraria==singoloElemento.fasciaOraria1){
				singoloRisultato["importo"] = getValoreObject(singoloElemento, "importoFasciaOraria1");
			}else if(fasciaOraria==singoloElemento.fasciaOraria2){
				singoloRisultato["importo"] = getValoreObject(singoloElemento, "importoFasciaOraria2");
			}else if(fasciaOraria==singoloElemento.fasciaOraria3){
				singoloRisultato["importo"] = getValoreObject(singoloElemento, "importoFasciaOraria3");
			}else if(fasciaOraria==singoloElemento.fasciaOraria4){
				singoloRisultato["importo"] = getValoreObject(singoloElemento, "importoFasciaOraria4");
			}else if(fasciaOraria==singoloElemento.fasciaOraria5){
				singoloRisultato["importo"] = getValoreObject(singoloElemento, "importoFasciaOraria5");
			}
			risultato.push(singoloRisultato);
		}
		risultato.sort(ordinaTariffe);
	}
	return risultato;
}

function ordinaTariffe(a, b){
	if(a.iseeDa=="" || b.iseeA==""){
		return -1;
	}
	if(a.iseeA=="" || b.iseeDa==""){
		return 1;
	}
	return (a.iseeA-b.iseeDa);
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
	var datiForm = getDatiFormObject();
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
		//var params = {};
		//params['where'] = "CODICE_FISCALE = '"+cfMinore+"'";
		//params['orderBy'] = "DATA_INIZIO_VALIDITA DESC";
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
				//waitingDialog.hide();
			}
		});
	}
}


function settaDatiModal(idRecord){
	resetDatiModal();
	if(idRecord){
		caricaDati(idRecord);
	}else{
		initNuovoInserimento();
	}
}

function initNuovoInserimento(){
	var tipoProcedimento = getTipoProcedimento();
	$('#tipoProcedimento').val(tipoProcedimento);
	$('#dpsFlgvalutazione').val('P');
	mostraDatiAsiloInfanzia(tipoProcedimento);
	var titoloModal="Nuova iscrizione";
	var annoScolasticoAttuale = "2021/2022";
	$('#recordModal').find('.modal-title').html(titoloModal);
	$('#recordModal').find('.modal-body #confermataIscrizione').val('C');
	$('#recordModal').find('.modal-body #annoScolasticoStatic').html(annoScolasticoAttuale);
	$('#recordModal').find('.modal-body #annoScolastico').val(annoScolasticoAttuale);
	$('#recordModal').find('.modal-body #confermataIscrizione').attr('disabled', 'disabled');
	$('.inputModalInsertRecord').show();
	recordSelezionato["annoScolastico"]=annoScolasticoAttuale;
}

function getTipoProcedimento(){
	var titoloReport = "${title?json_string}";
	if(titoloReport.includes("nido")){
		return "4";
	}else{
		return "5";
	}
}

function resetDatiModal(){
	rimuoviErroriModal();
	recordSelezionato = {};
	nomeIstitutoCancel();
	rimuoviMessaggio('validazione-modal-div');
	$("#recordModal").find('.inputModal').each(function(){
		$(this).removeAttr('disabled');
	});

	$('#nomeProcedimento').html('');
	$('#annoScolasticoStatic').html('');
	$('#scontoFamiglia').html('');
	$('#importoRetta').html('');
	$('#btnSalvaRecord').removeAttr('disabled');
	$('#btnRicalcolaTariffa').removeAttr('disabled');
	$('#cfMinore').removeAttr('disabled');
	$('.datiAsiloNido').hide();
	$('.datiInfanzia').hide();
	$('.inputModalUpdateRecord').hide();
	$('.inputModalInsertRecord').hide();
	$('#iseeRiferimentoStatic').html('');
	$('#gestioneFasciaIstituto').html('');
}

function fixDatiRecord(datiRecord){
	var result = datiRecord;
	if("cfContribuente" in datiRecord && datiRecord["cfContribuente"]){
		result["cfContribuente"] = datiRecord["cfContribuente"].toUpperCase();
	}
	return result;
}

function caricaDati(idRecord){
	//var params = {};
	//var whereCondition = "ID_COD="+idRecord;
	//params['where'] = whereCondition;
	//params['orderBy'] = "ID_COD desc";

	$.ajax ({
		method: 'GET',
		//url: '/bpm-server/db/select/'+TABLE_NAME,
		url: '/sispi/v1/istanza/getById/'+idRecord,
		contentType: 'application/json',
		timeout: 100000,
		success: function(data, textStatus, jqXHR){
			if(!data){
				visualizzaMessaggio('validazione-div', '<p>Nessun record trovato</p>', 'danger');
			}else{
				var dataItem = data;
				dataItem = fixDatiRecord(dataItem);
				recordSelezionato = dataItem;
				nomeIstitutoSelect(dataItem.nomeIstituto);
				for(chiave in dataItem){
					var valoreInput = getValoreInputFixed(chiave, dataItem);
					$('#recordModal').find('.modal-body #'+chiave).val(valoreInput);
					$('#recordModal').find('.modal-body p[id="'+chiave+'"]').html(valoreInput);
				}
				//Fix per campi diversi tra insert e update
				$('#recordModal').find('.modal-body #nomeIstitutoInfanzia').val(dataItem.nomeIstituto);
				
				if(dataItem.dataInizioIscrizione){
					$('#recordModal').find('.modal-body #dataInizioIscrizioneStatic').html(dataItem.dataInizioIscrizione);
				}else{
					$('#recordModal').find('.modal-body #dataInizioIscrizioneStatic').html("01/09/2021");
				}
				$('#recordModal').find('.modal-body #idFamigliaStatic').html(dataItem.idFamiglia);
				$('#recordModal').find('.modal-body #annoScolasticoStatic').html(dataItem.annoScolastico);

				ricaricaListaFasciaOraria(dataItem.fasciaOraria);
				var titoloModal="Conferma iscrizione istituto <strong>"+dataItem.nomeIstituto+"</strong>";
				$('#recordModal').find('.modal-title').html(titoloModal);

				if("S" == dataItem.confermaNotificata){
					$('#recordModal').find('.modal-body #confermataIscrizione').attr('disabled', 'disabled');
					$('#btnRicalcolaTariffa').attr('disabled', 'disabled');
					$('#recordModal').find('.modal-body #cfMinore').attr('disabled', 'disabled');
				}
				
				if("C" == dataItem.confermataIscrizione){
					$('#recordModal').find('.modal-body #nomeIstituto').attr('disabled', 'disabled');
					$('#recordModal').find('.modal-body #fascia').attr('disabled', 'disabled');
					$('#recordModal').find('.modal-body #fasciaOraria').attr('disabled', 'disabled');
					$('#recordModal').find('.modal-body #gestioneFasciaIstituto').html("<div class='row'><div class='col-md-12 alert alert-info '>Per modificare i dati di istituto e fascia oraria vai alla <a target='_blank' href='/reports/report?qt=fascia_istituto&CODICE_FISCALE="+dataItem.cfMinore+"&ANNO_SCOLASTICO="+dataItem.annoScolastico+"'>Gestione Istituto/Fascia Oraria</a></div></div><div class='row'></div>");
				}

				if("S" == dataItem.ricalcoloRate){
					$("#recordModal").find('.inputModal').each(function(){
						$(this).attr('disabled', 'disabled');
					});
					$('#btnSalvaRecord').attr('disabled', 'disabled');
					visualizzaMessaggio('validazione-modal-div', '<p>Attenzione: &egrave; in corso il ricalcolo delle rate per l\'iscritto selezionato. Attendere il termine del ricalcolo prima di poter modificare nuovamente i dati</p>', 'warning');
				}
				mostraDatiAsiloInfanzia(dataItem.tipoProcedimento);
				$('.inputModalUpdateRecord').show();
				caricaDatiIsee(dataItem.cfMinore);
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('Errore nel caricamento dati');
			visualizzaMessaggio('validazione-div', '<p>Errore nel caricamento dati</p>', 'danger');
		},
		complete: function( jqXHR, textStatus ){
			console.log('complete');
			//waitingDialog.hide();
		}
	});
}

function mostraDatiAsiloInfanzia(tipoProcedimento){
	$('.datiAsiloNido').hide();
	$('.datiInfanzia').hide();
	if(isAsiloNido(tipoProcedimento)){
		$('.datiAsiloNido').show();
	}else{
		$('.datiInfanzia').show();
	}
}

function getValoreInputFixed(chiave, dataItem){
	var valoreInput = dataItem[chiave];
	//fix per la FASCIA
	if("fascia" == chiave && valoreInput){
		valoreInput = valoreInput.toUpperCase();
	}
	//fix per la FASCIA
	if("scontoFamiglia" == chiave && valoreInput){
		valoreInput = ("S"==valoreInput) ? "Si" : "No";
	}
	return valoreInput;
}

function getDatiFormObject(){
	var params = cloneObject(recordSelezionato);
	//params['@entity'] = TABLE_NAME;

	$("#recordModal").find('.inputModal').each(function(){
		var idInput = $(this).attr("id");
		params[idInput] = $(this).val().trim();
	});

	if(!recordSelezionato["idFamiglia"] && !params["idFamiglia"]){
		params["idFamiglia"]="0";
	}

	//Fix per scuola infanzia
	if(!isAsiloNido(params["tipoProcedimento"])){
		params['nomeIstituto'] = params['nomeIstitutoInfanzia'];
	}
	delete params.nomeIstitutoInfanzia;

	if("S"!=recordSelezionato["confermaNotificata"] && checkEsitoValutazione(params)){
		params['ricalcoloRate'] = "S";
	}

	if(checkEsitoValutazione(params)){
		params['esitoValutazione'] = "AMMESSO";
	}
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
	//solo se confermaNotificata == "S"
	if("S" == recordOriginale.confermaNotificata){
		if(!isProprietaUguali(recordModificato, recordOriginale, "nomeIstituto")){
			result = true;
		}
		if(!isProprietaUguali(recordModificato, recordOriginale, "fasciaOraria")){
			result = true;
		}
		if(!isProprietaUguali(recordModificato, recordOriginale, "fascia")){
			result = true;
		}
		if(!isProprietaUguali(recordModificato, recordOriginale, "dataFineIscrizione")){
			result = true;
		}
	}
	if(!isIseeValido()){
		result = false;
	}
	return result;
}

function isAsiloNido(tipoProcedimento) {
	return codiciTipoProcedimentoAsilo.indexOf(tipoProcedimento)>-1;
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
	//if($('#ID_COD').val()==""){
	//	eseguiChiamataAjax('${context}/query/PORTSCU_ISTANZE_nextid', null, successChiamataAjaxIstanzeNextId, 'application/json', 'GET');
	//}else{
	//	eseguiChiamataAjax('/sispi/v1/istanza/save', getDatiFormObject(), successChiamataAjaxInviaNotifica);
	//}

	var datiFormJson = JSON.stringify(getDatiFormObject());
	eseguiChiamataAjax('/sispi/v1/istanza/save', datiFormJson, successChiamataAjaxInviaNotifica);
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
			//waitingDialog.hide();
		}
	});
}

function isIseeValido(){
	var iseeValido = true;
	var iseePresente = isValoreFloat($('#iseeRiferimentoStatic').html());
	var isAsilo = isAsiloNido($('#tipoProcedimento').val());
	if(isAsilo && !iseePresente){
		iseeValido = false;
	}
	return iseeValido;
}

function successChiamataAjaxInviaNotifica(data, textStatus, jqXHR){
	if(data && "confermaNotificata" in data){
		var urlToCall='';
		var datiAvvioProcesso = getDatiAvvioProcesso();
		if("S"==$('#SALTA_NOTIFICA').val() && isIseeValido()){
			//waitingDialog.show('Avvio processo notifica iscrizione senza invio notifica');
			datiAvvioProcesso['input']['saltaNotifica']="S";
			datiAvvioProcesso['processId'] = 'Calcolo Notifica Iscrizione2.0';
			eseguiChiamataAjax(AVVIA_ISTANZA_URL, JSON.stringify(datiAvvioProcesso), successChiamataAjax);
		}else if("S"==data["confermaNotificata"] && eseguiRicalcoloRate(getDatiFormObject(), recordSelezionato)){
			//waitingDialog.show('Aggiornamento dati rate');
			datiAvvioProcesso['processId'] = 'Ricalcolo Rate Iscrizione2.0';
			eseguiChiamataAjax(AVVIA_ISTANZA_URL, JSON.stringify(datiAvvioProcesso), successChiamataAjax);
		}else if("S"!=data["confermaNotificata"] && checkEsitoValutazione(data) ){
			//waitingDialog.show('Avvio processo notifica iscrizione');
			datiAvvioProcesso['processId'] = 'Calcolo Notifica Iscrizione2.0';
			eseguiChiamataAjax(AVVIA_ISTANZA_URL, JSON.stringify(datiAvvioProcesso), successChiamataAjax);
		}else{
			successChiamataAjax();
		}
	}else{
		visualizzaMessaggio('validazione-modal-div', '<p>Errore nel salvataggio dati</p>', 'danger');
	}
}

function getDatiAvvioProcesso(){
	var params = {};
	params['input'] = {};
	params['input']['cfIscritto'] = $('#cfMinore').val();
	params['input']['istanzaOriginale'] = recordSelezionato;
	return params;
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
	
	//var iseeRiferimento = $('#ISEE_RIFERIMENTO').val().trim();
	//if(iseeRiferimento != "" && !verificaValoreImporto(iseeRiferimento)){
	//	aggiungiErroreInput("ISEE_RIFERIMENTO","Valore ISEE riferimento non valido (es: 10000.00)");
	//	prosegui="no";
	//}
	var cfRichiedente = $('#cfContribuente').val().trim();
	if( cfRichiedente!="" && !verificaCodiceFiscale(cfRichiedente)){
		aggiungiErroreInput("cfContribuente", "Codice fiscale richiedente non valido");
		prosegui="no";
	}
	
	if($('#confermataIscrizione').val()=="C"){
		if($('#cfMinore').val()==""){
			aggiungiErroreInput("cfMinore", "Codice fiscale iscritto obbligatorio");
			prosegui="no";
		}
		if($('#nomeMinore').val()==""){
			aggiungiErroreInput("nomeMinore", "Nome iscritto obbligatorio");
			prosegui="no";
		}
		if($('#cognomeMinore').val()==""){
			aggiungiErroreInput("cognomeMinore", "Cognome iscritto obbligatorio");
			prosegui="no";
		}
		if($('#emailUtente').val()==""){
			aggiungiErroreInput("emailUtente", "Email richiedente obbligatoria");
			prosegui="no";
		}
		if($('#nomeRichiedente').val()==""){
			aggiungiErroreInput("nomeRichiedente", "Nome richiedente obbligatorio");
			prosegui="no";
		}
		if($('#cognomeRichiedente').val()==""){
			aggiungiErroreInput("cognomeRichiedente", "Cognome richiedente obbligatorio");
			prosegui="no";
		}
		if($('#cfContribuente').val()==""){
			aggiungiErroreInput("cfContribuente", "C.F. richiedente obbligatorio");
			prosegui="no";
		}
		if(isAsiloNido($('#tipoProcedimento').val())){
			if($('#ID_COD').val()=="" && $('#dataInizioIscrizione').val()==""){
				aggiungiErroreInput("dataInizioIscrizione", "Data inizio iscrizione obbligatoria");
				prosegui="no";
			}
			if($('#dataFineIscrizione').val()==""){
				aggiungiErroreInput("dataFineIscrizione", "Data fine iscrizione obbligatoria");
				prosegui="no";
			}else if(!isIntervalloDataFineValida($("#dataFineIscrizione").datepicker("getDate"), $('#annoScolastico').val())){
				aggiungiErroreInput("dataFineIscrizione", "Data fine iscrizione non valida");
				prosegui="no";
			}
			if($('#nomeIstituto').val()==""){
				aggiungiErroreInput("nomeIstituto", "Istituto obbligatorio");
				prosegui="no";
			}
			if($('#fascia').val()==""){
				aggiungiErroreInput("fascia", "Fascia obbligatoria");
				prosegui="no";
			}
			if($('#fasciaOraria').val()==""){
				aggiungiErroreInput("fasciaOraria", "Fascia oraria obbligatoria");
				prosegui="no";
			}
		}else{
			if($('#nomeIstitutoInfanzia').val()==""){
				aggiungiErroreInput("nomeIstitutoInfanzia", "Istituto obbligatorio");
				prosegui="no";
			}
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

function checkIseePerSconto(valoreIsee){
	var varificaPresenzaIdFamiglia=false;
	var importoIseeScontoFloat = parseFloat(IMPORTO_ISEE_PER_SCONTO);
	var valoreIseeFloat = getValoreFloat(valoreIsee);
	if(valoreIseeFloat<=importoIseeScontoFloat){
		varificaPresenzaIdFamiglia=true;
	}
	return varificaPresenzaIdFamiglia;
}

function verificaValoreImporto(importo){
	const importoRegex = /^\d+(\.\d{1,2}){0,1}$/;
	return importoRegex.test(importo);
}

function initDateInput(){
	$.datepicker.setDefaults({
		dateFormat:'dd/mm/yy'
	});
	$('#dataInizioIscrizione').datepicker();
	$('#dataFineIscrizione').datepicker();
}

</script>