$(document).ready(function() {

	$('#fascia').on('change', function() {
		ricaricaListaFasciaOraria();
    });

});

var ASILOTARIFFE_TABLE = [];
var elencoTariffe = {};
var listaCentriInfanziaComunali = [];

function caricaElencoTariffe(){
	//var params = {};
	//params['orderBy'] = "CTA_DESDENOMASILO,CTA_DESFASCIAORARIA1";
	$.ajax ({
		method: 'GET',
		//url: '/bpm-server/db/select/PORTSCU_SCU_CENTRITARIFFE',
		url: '/sispi/v1/centroinfanzia/listatariffe',
		contentType: 'application/json',
		//data: params,
		timeout: 100000,
		success: function(data, textStatus, jqXHR){
			if(data.length==0){
				console.log("Nessun elemento trovato autocomplete_nomeistituto");
				//visualizzaMessaggio('validazione-div', '<p>Nessun record trovato</p>', 'warning');
			}else{
				ASILOTARIFFE_TABLE = data;
				for (i = 0; i < data.length; i++) {
					var dataItem = data[i];
					var descrizioneScuola = dataItem.denominazioneAsilo;
					var descrizioneCategoria = dataItem.categoria;
					var fasciaOraria1 = dataItem.fasciaOraria1;
					var fasciaOraria2 = dataItem.fasciaOraria2;
					var fasciaOraria3 = dataItem.fasciaOraria3;
					var fasciaOraria4 = dataItem.fasciaOraria4;
					var fasciaOraria5 = dataItem.fasciaOraria5;

					if(!listaCentriInfanziaComunali.includes(descrizioneScuola) && "S"==dataItem.flagComunale){
						listaCentriInfanziaComunali.push(descrizioneScuola);
					}
					
					if(!(descrizioneScuola in elencoTariffe)){
						elencoTariffe[descrizioneScuola]={};
					}
					var mappaCategorieScuola = elencoTariffe[descrizioneScuola];
					
					if(!(descrizioneCategoria in mappaCategorieScuola)){
						elencoTariffe[descrizioneScuola][descrizioneCategoria]=[];
					}
					var listaFasciaOraria = elencoTariffe[descrizioneScuola][descrizioneCategoria];
					
					if(fasciaOraria1!=null && !listaFasciaOraria.includes(fasciaOraria1)){
						elencoTariffe[descrizioneScuola][descrizioneCategoria].push(fasciaOraria1);
					}

					if(fasciaOraria2!=null && !listaFasciaOraria.includes(fasciaOraria2)){
						elencoTariffe[descrizioneScuola][descrizioneCategoria].push(fasciaOraria2);
					}

					if(fasciaOraria3!=null && !listaFasciaOraria.includes(fasciaOraria3)){
						elencoTariffe[descrizioneScuola][descrizioneCategoria].push(fasciaOraria3);
					}

					if(fasciaOraria4!=null && !listaFasciaOraria.includes(fasciaOraria4)){
						elencoTariffe[descrizioneScuola][descrizioneCategoria].push(fasciaOraria4);
					}

					if(fasciaOraria5!=null && !listaFasciaOraria.includes(fasciaOraria5)){
						elencoTariffe[descrizioneScuola][descrizioneCategoria].push(fasciaOraria5);
					}
				}
				caricaElencoNomeIstituto(elencoTariffe);
			}
		},
		error: function(jqXHR, textStatus, errorThrown) {
			console.log('Errore nel caricamento PORTSCU_SCU_ASILOTARIFFE');
			visualizzaMessaggio('validazione-div', '<p>Errore nel caricamento dati PORTSCU_SCU_ASILOTARIFFE</p>', 'danger');
		},
		complete: function( jqXHR, textStatus ){
			console.log('complete');
		}
	});
}

function isCentroInfanziaComunale(descrizioneScuola){
	return listaCentriInfanziaComunali.includes(descrizioneScuola);
}

function caricaElencoNomeIstituto(mappaTariffe){
	var data = Object.keys(mappaTariffe);
	var ulComponent = $('#nomeIstituto').closest(".searchable").find("ul");
	ulComponent.find("li").remove();
	if(data.length==0){
		console.log("Nessun elemento trovato autocomplete_nomeistituto");
		//visualizzaMessaggio('validazione-div', '<p>Nessun record trovato</p>', 'warning');
	}else{
		for (i = 0; i < data.length; i++) {
			var dataItem = data[i];
			ulComponent.append('<li>'+dataItem+'</li>');
		}
	}
}

function ricaricaListaFasciaOraria(valoreFascia){
	fasciaOrariaCancel();
	var listaFasciaOraria = getListaFasciaOraria();
	for (i = 0; i < listaFasciaOraria.length; i++) {
		var dataItem = listaFasciaOraria[i];
		$('#fasciaOraria').append(new Option(dataItem, dataItem));
	}
	if(valoreFascia){
		$('#fasciaOraria').val(valoreFascia);
		$('#fasciaOraria').trigger('change');
	}
}

function getListaFasciaOraria(){
	var listaFasciaOraria = [];
	var nomeIstituto = $('#nomeIstituto').val();
	var fascia = $('#fascia').val();
	if(nomeIstituto && fascia && nomeIstituto in elencoTariffe && fascia in elencoTariffe[nomeIstituto]){
		listaFasciaOraria = elencoTariffe[nomeIstituto][fascia];
	}
	return listaFasciaOraria; 
}

function nomeIstitutoSelect(elementoSelezionato) {
	nomeIstitutoCancel();
	scuolaSelezionata = elencoTariffe[elementoSelezionato];
	if(scuolaSelezionata){
		settaDatiScuolaSelezionata(scuolaSelezionata);
	}
}

function settaDatiScuolaSelezionata(scuolaSelezionata) {
	var data = Object.keys(scuolaSelezionata);
	for (i = 0; i < data.length; i++) {
		var dataItem = data[i];
		$('#fascia').append(new Option(dataItem, dataItem));
	}
}

function nomeIstitutoCancel() {
	$('#fascia option').not(":first").remove();
	$('#fascia').val("");
	fasciaOrariaCancel();
}

function fasciaOrariaCancel() {
	$('#fasciaOraria option').not(":first").remove();
	$('#fasciaOraria').val("");
	$('#fasciaOraria').trigger('change');
	cancellaDatiTariffa();
}

function cancellaDatiTariffa(){
	$('#tableTariffa tr').remove();
}
