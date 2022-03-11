<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/jquery.tokeninput.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<script type="text/javascript" src="/static/palermo/js/gestione-centri-infanzia.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/token-input-bootstrappy.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<div class=''>
 <div class='row'>
  <table class='table table-striped table-condensed'>
   <thead>
    <tr>
     <th>${(params["facet.ANNO_SCOLASTICO.label"])!}</th>
     <th>${(params["facet.CF_MINORE.label"])!}</th>
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
    <#assign VALORE_CONGUAGLIO_NUMBER = item.VALORE_CONGUAGLIO?number>
    <#assign VALORE_RATA_NUMBER = item.VALORE_RATA?number>
	<#assign IMPORTO_TOTALE_NUMBER = (VALORE_RATA_NUMBER+VALORE_CONGUAGLIO_NUMBER)>
    <tr
	<#if item.IMPORTO_PAGAMENTO?? >
	<#assign IMPORTO_PAGAMENTO_NUMBER = item.IMPORTO_PAGAMENTO?number>
	<#assign DIFFERENZA_PAGAMENTO = (IMPORTO_PAGAMENTO_NUMBER-IMPORTO_TOTALE_NUMBER)>
	<#if DIFFERENZA_PAGAMENTO == 0 >style='background-color: #5aff4b'</#if>
	<#if DIFFERENZA_PAGAMENTO gt 0 >style='background-color: #f7ea02'</#if>
	<#if DIFFERENZA_PAGAMENTO lt 0 >style='background-color: #ff6c6c'</#if>
	</#if>
	>
     <td>${(item.ANNO_SCOLASTICO)!}</td>
     <td>${(item.CF_MINORE)!}</td>
     <td>${(item.NOME_MINORE)!} ${(item.COGNOME_MINORE)!}</td>
     <td>${(item.DESCRIZIONE)!}</td>
     <td>${(item.VALORE_RATA)!}</td>
     <td>${(item.NOME_ISTITUTO)!}</td>
     <td>${(item.RATA_PAGATA)!} <#if DIFFERENZA_PAGAMENTO != 0 >(${(DIFFERENZA_PAGAMENTO?c)!})</#if></td>
     <td>${(item.VALORE_CONGUAGLIO)!}</td>
     <td>${(IMPORTO_TOTALE_NUMBER?string["0.##"])!}</td>
     <td>
     <#if item.RATA_PAGATA == "No" >
      <a class='editRecord' title='Modifica ${item.ID}' data-backdrop='static' data-keyboard='false' data-toggle='modal' data-target='#recordModal' data-idrecord='${item.ID}'><span class='glyphicon glyphicon-pencil' aria-hidden='true'></span></a>
	 <#else>
	  <a title='Pagamento ${(item.DESCRIZIONE)!}' target='_blank' href='/reports/report?qt=pagamenti_centro_infanzia&ID_ISCRIZIONE=${(item.ID_ISCRIZIONE)!}'><span class='glyphicon glyphicon-euro' aria-hidden='true'></span></a>
     </#if>
     </td>
     <td>
	  <a title='Log dettaglio ${(item.DESCRIZIONE)!}' target='_blank' href='/reports/report?qt=centro_infanzia_log&ID_DETTAGLIO_ISCRIZIONE_CENTRO_INFANZIA=${(item.ID)!}'><span class='glyphicon glyphicon-tasks' aria-hidden='true'></span></a>
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
    <div class='row'>
     <h4 class='ml-2'>Dati Rata</h4>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='annoScolasticoStatic'>${(params["facet.ANNO_SCOLASTICO.label"])!}</label>
      <p class='form-control-static' id='annoScolasticoStatic'></p>
     </div>
	 <div class='form-group col-md-6'>
      <label for='iseeRiferimentoStatic'>${(params["facet.ISEE_RIFERIMENTO.label"])!}</label>
      <p class='form-control-static' id='iseeRiferimentoStatic'></p>
     </div>
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
     <div class="form-group col-md-6">
      <label>Applica a mesi succesivi</label>
	  <div class='radio'>
       <div class='form-check form-check-inline'>
        <input type='radio' class='form-check-input' value='si' name='applicaMesiSuccessivi' id='applicaMesiSuccessiviSi'>
        <label class='form-check-label' for='applicaMesiSuccessiviSi'>Si</label>
       </div>
       <div class='form-check form-check-inline'>
        <input type='radio' class='form-check-input' value='no' name='applicaMesiSuccessivi' id='applicaMesiSuccessiviNo'>
        <label class='form-check-label' for='applicaMesiSuccessiviNo'>No</label>
       </div>
      </div>
     </div>
    </div>
    <div class='row applicaMesiSuccessiviSi'>
     <div class='col-md-12 alert alert-warning'>
	  Attenzione! Le modifiche ai dati istituto/fascia saranno applicate anche a tutti i mesi successivi.
     </div>
    </div>
    <hr class='clearfix' />
    <div class='row'>
     <div class='form-group col-md-4'>
      <label for='valoreRataStatic'>${(params["facet.VALORE_RATA.label"])!}</label>
      <p class='form-control-static' id='valoreRataStatic'></p>
     </div>
     <div class='form-group col-md-4'>
      <label for='giorniFrequenza'>${(params["facet.GIORNI_FREQUENZA.label"])!}</label>
      <p class='form-control-static' id='giorniFrequenza'></p>
     </div>
     <div class='form-group col-md-4'>
      <label for='valoreRata'>${(params["facet.IMPORTO_CALCOLATO.label"])!}</label>
	  <input type='hidden' id='valoreRata'>
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
	<hr class='clearfix confermaNotificataNo' />
    <div class='row confermaNotificataNo'>
     <div class='col-md-12 alert alert-info'>
     Iscrizione non confermata. Le modifiche ai dati non saranno notificate all'utente.
     </div>
    </div>
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

//var GIORNI_FREQUENZA_DEFAULT = "30";
var recordSelezionato = {};
var recordIstanzaSelezionato = {};
var listaGiorniFrequenzaIstituto = {};

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
	$('#fascia').on('change', function() {
		ricaricaListaFasciaOraria();
    });

	$('#fasciaOraria').on('change', function() {
		ricaricaDatiTariffe();
    });

	$('input[type=radio][name="applicaMesiSuccessivi"]').change(function() {
		controlloMesiSuccessivi();
	});

	$('#recordModal').on('show.bs.modal', function (event) {
		var button = $(event.relatedTarget);
		var idRecord = button.data('idrecord');
		settaDatiModal(idRecord);
	});
	caricaElencoTariffe();
	//caricaSelectGiorniFrequenza();
	caricaElencoGiorniFrequenzaIstituto();
});

function controlloMesiSuccessivi(){
	$('.applicaMesiSuccessiviSi').hide();
	var applicaMesiSuccessivi = $('input[name="applicaMesiSuccessivi"]:checked').val();
	if("si"==applicaMesiSuccessivi){
		$('.applicaMesiSuccessiviSi').show();
	}
}

function caricaElencoGiorniFrequenzaIstituto(){
	//var params = {};
	//params['orderBy'] = "SORT";
	$.ajax ({
		method: 'GET',
		//url: '/bpm-server/db/select/PORTSCU_GIORNI_FREQUENZA_ISTITUTO',
		url: '/sispi/v1/centroinfanzia/listaGiorniFrequenza',
		contentType: 'application/json',
		//data: params,
		timeout: 100000,
		success: function(data, textStatus, jqXHR){
			if(data.length==0){
				console.log("Nessun elemento trovato giorni frequenza");
				//visualizzaMessaggio('validazione-div', '<p>Nessun record trovato</p>', 'warning');
			}else{
				for (i = 0; i < data.length; i++) {
					var dataItem = data[i];
					var annoScolastico = dataItem.annoScolastico;
					if(!(annoScolastico in listaGiorniFrequenzaIstituto)){
						listaGiorniFrequenzaIstituto[annoScolastico] = {};
					}
					listaGiorniFrequenzaIstituto[annoScolastico][dataItem.sort] = {};
					listaGiorniFrequenzaIstituto[annoScolastico][dataItem.sort]["descrizione"] = dataItem.descrizione;
					listaGiorniFrequenzaIstituto[annoScolastico][dataItem.sort]["giorniFrequenza"] = dataItem.giorniFrequenza;
				}
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('Errore nel caricamento PORTSCU_GIORNI_FREQUENZA_ISTITUTO');
			visualizzaMessaggio('validazione-div', '<p>Errore nel caricamento dati GIORNI_FREQUENZA_ISTITUTO</p>', 'danger');
		},
		complete: function( jqXHR, textStatus ){
			console.log('complete');
		}
	});
}

//function caricaSelectGiorniFrequenza(){
//	var giorniFrequenzaDefault = parseInt(GIORNI_FREQUENZA_DEFAULT);
//	for (let i = 0; i <= giorniFrequenzaDefault; i++) {
//		$('#GIORNI_FREQUENZA').append("<option value='"+i+"'>"+i+"</option>");
//	}
//}

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
	rimuoviMessaggio('validazione-modal-div');
	$('#giorniFrequenza').html('');
	$('#iseeRiferimentoStatic').html('');
	$('#annoScolasticoStatic').html('');
	$("input[name='applicaMesiSuccessivi'][value='no']").prop('checked', true);
	recordSelezionato = {};
	recordIstanzaSelezionato = {};
	controlloMesiSuccessivi();
	$('.confermaNotificataNo').hide();
}

function caricaDati(idRecord){
	//var whereCondition = "ID="+idRecord;
	//var params = {};
	//params['where'] = whereCondition;
	//params['orderBy'] = "SORT asc";
	var params = {};
	params['idRecord'] = idRecord;

	$.ajax ({
		method: 'GET',
		//url: '/bpm-server/db/select/PORTSCU_DETTAGLIO_ISCRIZIONI_CENTRO_INFANZIA',
		url: '/sispi/v1/centroinfanzia/getDettaglio',
		contentType: 'application/json',
		timeout: 100000,
		data: params,
		success: function(data, textStatus, jqXHR){
			if(data.length==0){
				visualizzaMessaggio('validazione-modal-div', '<p>Dati non trovati</p>', 'error');
			}else{
				var dataItem = data[0];
				recordSelezionato = dataItem;
				nomeIstitutoSelect(dataItem.nomeIstituto);
				valorizzaInputModal(dataItem);
				ricaricaListaFasciaOraria(dataItem.fasciaOraria);
				$('#valoreConguaglio').val(formatFloat(stringToNumber(dataItem.valoreConguaglio)));
				$('#valoreConguaglio').trigger('change');
				$('#valoreRataStatic').html(formatFloat(stringToNumber(dataItem.valoreRata)));

				recordIstanzaSelezionato = dataItem.iscrizione;
				valorizzaInputModal(recordIstanzaSelezionato);
				$('#recordModal').find('.modal-body p[id="annoScolasticoStatic"]').html(recordIstanzaSelezionato.annoScolastico);
				if("S"!=recordIstanzaSelezionato.confermaNotificata){
					$('.confermaNotificataNo').show();
				}
				var titoloModal="Modifica importo rata: <strong>"+recordSelezionato.descrizione+" - "+recordIstanzaSelezionato.nomeMinore+" "+recordIstanzaSelezionato.cognomeMinore+" ("+recordIstanzaSelezionato.cfMinore+")</strong>";
				$('#recordModal').find('.modal-title').html(titoloModal);
				caricaDatiIsee(recordIstanzaSelezionato.cfMinore);
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

function getAnnoRiferimentoByAnnoScolastico(annoScolastico){
	var result = null;
	if(!!annoScolastico){
		result = annoScolastico.substring(0, 4);
	}
	return result;
}

function getGiorniFrequenza(annoScolastico, sort){
	return listaGiorniFrequenzaIstituto[annoScolastico][sort]["giorniFrequenza"];
}

function ricaricaDatiTariffe(){
	cancellaDatiTariffa();
	var annoScolasticoFilter = getAnnoRiferimentoByAnnoScolastico($('#annoScolasticoStatic').html());
	console.log("annoScolasticoFilter: "+annoScolasticoFilter);
	var result = ASILOTARIFFE_TABLE.filter(record => $('#nomeIstituto').val()==record.denominazioneAsilo);
	result = result.filter(record => $('#fascia').val()==record.descrizioneCategoria);
	result = result.filter(record => $('#fasciaOraria').val()==record.fasciaOraria1);
	result = result.filter(record => annoScolasticoFilter==record.annoRiferimento);
	result = getTariffaByFasciaOraria(result);
	for (i=0; i<result.length; i++){
		var singoloElemento = result[i];
		if(isTariffaIsee(singoloElemento)){
			$('#valoreRataStatic').html(formatFloat(stringToNumber(singoloElemento.importoFasciaOraria1)));
			calcolaValoreRata();
			break;
		}
	}
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
					visualizzaMessaggio('validazione-modal-div', '<p>Errore nel caricamento dati ISEE</p>', 'danger');
				}else{
					var dataItem = data[0];
					$('#iseeRiferimentoStatic').html(dataItem.importo);
					var giorniFrequenza = getGiorniFrequenza(recordIstanzaSelezionato.annoScolastico, recordSelezionato.sort);
					$('#giorniFrequenza').html(giorniFrequenza);
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

function isTariffaIsee(singoloElemento){
	var result = false;
	var valoreIseeFloat = stringToNumber($('#iseeRiferimentoStatic').html());
	var iseeDa = getValoreObject(singoloElemento, "ISEEDA");
	if(iseeDa==""){
		iseeDa = 0;
	}
	var iseeA = getValoreObject(singoloElemento, "ISEEA");
	if(iseeA==""){
		iseeA = Number.MAX_VALUE;
	}
	if(iseeDa<=valoreIseeFloat && iseeA>=valoreIseeFloat){
		result = true;
	}
	return result;
}

function calcolaValoreRata(){
	valorizzaImportoCalcolato("");
	var importoRetta = getValoreFloat($('#valoreRataStatic').html());
	var nuovoValore = null;
	if(isCentroInfanziaComunale($('#nomeIstituto').val())){
		nuovoValore = importoRetta;
	}else{
		var giorniFrequenzaFloat = getValoreFloat($('#giorniFrequenza').html());
		nuovoValore = importoRetta*giorniFrequenzaFloat;
	}
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

function getDatiForm(){
	var params = {};
	$("#recordModal").find('.inputModal').each(function(){
		var idInput = $(this).attr("id");
		params[idInput] = $(this).val();
	});
	params['valoreConguaglio'] = getValoreFloat(params['valoreConguaglio']);
	return params;
}

function getDataAttualeString(){
	var d = new Date()
	var date = d.toISOString().split('T')[0];
	var time = d.toTimeString().split(' ')[0];
	return date+" "+time;
}

function eseguiQueryInsert() {
	eseguiChiamataAjax('/bpm/v1/instances', getDatiAvvioProcesso(), successChiamataAjax);
}

function getDatiAvvioProcesso(){
	var params = {};
	params['processId'] = 'Aggiorna Dettaglio Centro Infanzia2.0';

	var datiForm = getDatiForm();
	//FIX ID RECORD
	datiForm["id"] = recordSelezionato.id;
	var datiFormTmp = {};
	datiFormTmp['datiForm'] = datiForm;
	datiFormTmp['utenteProcesso'] = userInfo.username;
	datiFormTmp['salvaSuccessivi'] = $('input[name="applicaMesiSuccessivi"]:checked').val();

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
			visualizzaMessaggio('validazione-div', '<p>Errore nel salvataggio dati</p>', 'danger');
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
	rimuoviMessaggio('validazione-modal-div');
	var messaggio = "";
	var prosegui="si";
	if($('#nomeIstituto').val()==""){
		aggiungiErroreInput("nomeIstituto","Inserire un istituto");
		prosegui="no";
	}
	if($('#fascia').val()==""){
		aggiungiErroreInput("fascia","Inserire una fascia");
		prosegui="no";
	}
	if($('#fasciaOraria').val()==""){
		aggiungiErroreInput("fasciaOraria","Inserire una fascia oraria");
		prosegui="no";
	}
	if($('#motivazioneVariazione').val()==""){
		aggiungiErroreInput("motivazioneVariazione","Inserire una motivazione");
		prosegui="no";
	}
	if("no"==prosegui){
		visualizzaMessaggio('validazione-modal-div', '<p>Dati mancanti o non validi</p>', 'danger');
	}
	//prosegui="no";
	return prosegui;
}

</script>
