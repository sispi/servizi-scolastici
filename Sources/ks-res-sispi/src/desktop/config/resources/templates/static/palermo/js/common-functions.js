//////////////////////////////////////////
// "waiting dialog". "Blocca" la pagina facendo comparire il messaggio di "Attendere".
// waitingDialog.show(message);
// message: string/html. Il messaggio da visualizzare
// esempio javascript:
// waitingDialog.show('Attendere');
//////////////////////////////////////////

//////////////////////////////////////////
// "confirm dialog". Apre una form "modal" con per una richiesta di conferma
// confirm(heading, question, cancelButtonTxt, okButtonTxt, callback);
// heading: string/html. Il titolo della confirm dialog
// question: string/html. Il body della confirm dialog
// cancelButtonTxt: string. Il testo del bottone. Es: "Annulla"
// okButtonTxt: string . Il testo del bottone. Es: "Conferma"
// callback: function. La funzione richiamata in caso di conferma.
// esempio javascript:
// var heading = 'Richiesta conferma';
// var question = '<p>Sei sicuro di proseguire?</p>';
// var cancelButtonTxt = 'Annulla';
// var okButtonTxt = 'Conferma';
// var callback = function() {
// 	$('form').submit();
// };
// confirmKS(heading, question, cancelButtonTxt, okButtonTxt, callback);
//////////////////////////////////////////

//////////////////////////////////////////
// "datime picker"
// componente per la gestione del calendario. Per utilizzarlo va aggiunta la classe "data-input" al campo "<input>" che contiene la data.
// Se la data da visualizzare deve essere formattata in maniera diversa rispetto a quella passata nel "submit" della form, va aggiunto un
// ulteriore campo nascosto.
// Esempio html (con campo nascosto):
// <div class="form-group col-md-6">
//  <label  for="dataInizio">Data inizio</label>
//  <input type="hidden" name="dataInizio" id="dataInizioSubmit" value="${dataInizio}" />
//  <input type="text" placeholder="Data inizio" id="dataInizio" class="code form-control data-input" autocomplete="off" />
// </div>
//
// Esempio html (semplice):
// <div class="form-group col-md-6">
//  <label for="dataInizio">Data inizio</label>
//  <input type="text" placeholder="Data inizio" value="${dataInizio}" name="dataInizio" id="dataInizio" class="code form-control data-input" autocomplete="off" />
// </div>
//
// Per inizializzare il calendario va aggiunto il seguente javascript al caricamento della pagina:
//
// Questa parte serve per inizializzare il calendario con parametri di default. Per vedere tutti i possibili parametri consultare https://api.jqueryui.com/datepicker/
// La proprieta "dateFormat" imposta come deve essere visualizzata la data.
// La proprieta "altFormat" imposta come deve essere memorizzata la data nel campo "alternative" (questa opzione va usata solo quando e' presente anche il campo nascosto).
// Esempio javascript:
//
//  Va fatto solo una volta al caricamento della form
//  $.datepicker.setDefaults({
//		altFormat: "yy-mm-dd",
//		dateFormat:'dd/mm/yy'
//	});
//
//  Va fatto per ogni campo di tipo data nella form
//	$( "#dataInizio" ).datepicker({
//		altField: "#dataInizioSubmit"
//	});
//  ...
//	$( "#dataFine" ).datepicker({
//		altField: "#dataFineSubmit"
//	});
//  ...

//////////////////////////////////////////


//////////////////////////////////////////
// "lista ricercabile"
// componente ottenere un menu a tendina con la possibilita' di ricercare un elemento digitando una parte del testo.
// Va aggiunta la classe "searchable" al "<div>" che contiene il campo di "<input>".
// Esempio html:
//
// <div class='form-group col-md-4 searchable'>
//  <label for='luogo' >Luogo</label>
//	<input type="text" class="form-control" name="luogo" id="luogo" value="${luogo}" autocomplete="off">
//	<ul></ul>
// </div>
//
// I valori da aggiungere alla "<ul>" possono essere caricati tramite variabile di template "ftl", oppure tramite una chiamata ajax.
//
// Esempio di dati precaricati:
// <ul>
//  <!-- <#list listaElementi as elemento> -->
//	<li>${elemento}</li>
//	<!-- </#list> -->
// </ul>
//
//
// Esempio di dati caricati tramite ajax:
// var data = [array json di risultati];
// var ulComponent = $('#luogo').closest('.searchable').find('ul');
// for (i = 0; i <= data.length - 1; i++) {
//    var dataItem = data[i];
//    var elemento = dataItem.description + " (" + dataItem.code + ")";
//    ulComponent.append('<li>'+elemento+'</li>');
// }
//////////////////////////////////////////

//////////////////////////////////////////
// gestione campi "importo"
// Permette di aggiungere controlli sul campo dove inserire un valore di tipo importo. Per attivare qusto componente va aggiunta la classe "importo"
// alla input di tipo "text".
// Viene controllato che i valori immessi siano solo numeri, che ci sia un solo separatore decimale (".") e massimo 2 cifre dopo il "."
// Esempio di codice html
//
// <div class="form-group col-md-5">
//  <label for='valoreimporto'>Importo (&euro;)</label>
//	<input type="text" id="valoreimporto" class="form-control importo" name="valoreimporto" value="${valoreimporto}">
// </div>


$(document).ready(function() {
	$('.data-input').each(function(){
		valorizzaCampiData($(this));
	});
	$('.data-input').on('change', function(){
		verificaDataInput($(this));
    });
	$(".importo").bind('keydown paste', functionKeyDownImporto);
	$(".importo").bindFirst('change', functionChangeImporto);
	initSearchInput();
	$('[data-toggle="tooltip"]').tooltip();

	initSelect2();

	$('.upperCaseText').bind('change', function(){
		this.value = this.value.toLocaleUpperCase();
	});

});

//se si svuota il campo data si svuota anche il relativo campo nascosto
function verificaDataInput(inputElem){
	if(inputElem.val()==""){
		var valoreId = inputElem.attr("id");
		$("#"+valoreId+"Submit").val("");
		console.log("resettata data: " + valoreId);	
	}else{
		var formatoDataReg = /^[0-9]{2}\/[0-9]{2}\/[0-9]{4}$/;
		var resRegExp = formatoDataReg.test(inputElem.val());
		if(!resRegExp){
			mostraAlertHt.show("Formato data non valido (<b>gg/mm/aaaa</b>). Valore inserito: <b>"+inputElem.val()+"</b>", {headerText: "Attenzione!"});
			resetDataInput(inputElem);
		}
	}
}

function resetDataInput(inputElem){
	var valoreId = inputElem.attr("id");
	$("#"+valoreId+"Submit").val("");
	inputElem.val("");
}

function roundNumber(num, dec) {
	var result = num;
	if(!isNaN(num)){
		result = +(Math.round(num + "e+" + dec)  + "e-" + dec);
	}
	return result;
}

//Autocomplete //////////////////////////////

function initSearchInput(){
  $(".searchable input").focus(function () {
    $(this).closest(".searchable").find("ul").show();
    $(this).closest(".searchable").find("ul li").show();
  });
  $(".searchable input").blur(function () {
    let that = this;
	console.log("valore input:" + $(that).val());
	var lista = getListaElementi(this);
	var valoreImpostato = $(that).val();
	if(!valoreImpostato || !lista.includes(valoreImpostato)){
		console.log("valore input nullo");
		$(that).val("");
		var onCancelFunction = $(this).data("oncancel");
		console.log("onCancelFunction"+onCancelFunction);
		if (onCancelFunction != '' && onCancelFunction != undefined) {
			window[onCancelFunction]();
		}
	}
    setTimeout(function () {
        $(that).closest(".searchable").find("ul").hide();
    }, 300);
  });

  $(document).on('click', '.searchable ul li', function () {
	$(this).closest(".searchable").find("input").val($(this).text()).blur();
	var onSelectFunction = $(this).closest(".searchable").find("input").data("onselect");
	if (onSelectFunction != '' && onSelectFunction != undefined) {
		window[onSelectFunction]($(this).text());
		 //onSelectFunction($(this).text());
	}else{
		onSelect($(this).text());
	}
  });

  $(".searchable ul li").hover(function () {
    $(this).closest(".searchable").find("ul li.selected").removeClass("selected");
    $(this).addClass("selected");
  });
  
  $(".searchable input").keyup(filterFunction);
}

function getListaElementi(inputSelector){
	var listaElementi = [];
	$(inputSelector).closest(".searchable").find("ul li").each(function(){
		listaElementi.push($(this).text());
	});
	return listaElementi;
}

function filterFunction(event) {
    let container, input, filter, li, input_val;
    container = $(this).closest(".searchable");
    input_val = container.find("input").val().toUpperCase();

    if (["ArrowDown", "ArrowUp", "Enter"].indexOf(event.key) != -1) {
        keyControl(event, container)
    } else {
        li = container.find("ul li");
        li.each(function (i, obj) {
            if ($(this).text().toUpperCase().indexOf(input_val) > -1) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });

        container.find("ul li").removeClass("selected");
        setTimeout(function () {
            container.find("ul li:visible").first().addClass("selected");
        }, 100)
    }
}

function keyControl(e, container) {
    if (e.key == "ArrowDown") {
        if (container.find("ul li").hasClass("selected")) {
            if (container.find("ul li:visible").index(container.find("ul li.selected")) + 1 < container.find("ul li:visible").length) {
                container.find("ul li.selected").removeClass("selected").nextAll().not('[style*="display: none"]').first().addClass("selected");
            }
        } else {
            container.find("ul li:first-child").addClass("selected");
        }
    } else if (e.key == "ArrowUp") {
        if (container.find("ul li:visible").index(container.find("ul li.selected")) > 0) {
            container.find("ul li.selected").removeClass("selected").prevAll().not('[style*="display: none"]').first().addClass("selected");
        }
    } else if (e.key == "Enter") {
        container.find("input").val(container.find("ul li.selected").text()).blur();
        onSelect(container.find("ul li.selected").text())
    }
    container.find("ul li.selected")[0].scrollIntoView({
        behavior: "smooth",
    });
}

function onSelect(val) {
	console.log("select: " + val);
	if (typeof elementoSelezionatoAutocomplete == 'function') {
		elementoSelezionatoAutocomplete(val);
	}
}

//////////////////////////////////////////////////////////////////



///////////////////Date functions ////////////////////////////////

function comparaDate(dataInizio, oraInizio, dataFine, oraFine) {
	var result = false;
	if(!dataInizio || !dataFine){
		result = true;
	}else if(dataInizio < dataFine){
		result = true;
	}else if(dataInizio == dataFine){
		result = comparaOra(oraInizio, oraFine);
	}
	return result;
}


function checkDataAttuale(inputData, inputOra){
	date = new Date();
	var gg = (date.getDate() < 10 ? '0' : '') + date.getDate();
	var mm = ((date.getMonth() + 1) < 10 ? '0' : '') + (date.getMonth() + 1);
	var aa = date.getFullYear();
	var dataAttualeString = aa+"-"+mm+"-"+gg;
	var hh = (date.getHours() < 10 ? '0' : '') + date.getHours();
	var mi = (date.getMinutes() < 10 ? '0' : '') + date.getMinutes();
	var oraAttualeString = hh+":"+mi;
	var result = comparaDate(dataAttualeString, oraAttualeString, inputData, inputOra);
	return result;
}

function comparaOra(oraInizio, oraFine) {
	var result = false;
	if(!oraInizio || !oraFine){
		result = true;
	}else {
		result = oraInizio<=oraFine;
	}
	return result;
}

function dateDiff(date1, date2) {
    date1.setHours(0);
    date1.setMinutes(0, 0, 0);
    date2.setHours(0);
    date2.setMinutes(0, 0, 0);
    var datediff = date1.getTime() - date2.getTime(); // difference 
	if(datediff<0){
		datediff=0;
	}
    return parseInt(datediff / (24 * 60 * 60 * 1000), 10); //Convert values days and return value      
}

function convertDateTimeToString(date) {
	var anno = date.getFullYear();
	var mese = aggiungiZero(date.getMonth()+1);
	var giorno = aggiungiZero(date.getDate());
	var ore = aggiungiZero(date.getHours());
    var minuti = aggiungiZero(date.getMinutes());
	return giorno+"/"+mese+"/"+anno+" "+ore+":"+minuti;
}

function convertDateToString(date) {
	var anno = date.getFullYear();
	var mese = aggiungiZero(date.getMonth()+1);
	var giorno = aggiungiZero(date.getDate());
	return giorno+"/"+mese+"/"+anno;
}

//Converte una data "yyyy-mm-dd" in un oggetto Date
function convertStringToDate(dateString) {
	var dateStringArray = dateString.split("-");
	var anno = dateStringArray[0];
	var mese = dateStringArray[1]-1;
	var giorno = dateStringArray[2];
	var dateObject = new Date(anno, mese, giorno);
	return dateObject;
}

function aggiungiZero(valore){
	var result = valore;
	if(valore < 10){
		result = "0"+valore;
	}
	return result;
}

function valorizzaCampiData(inputData) {
	var idCampoData = inputData.attr("id");
	var idCampoDataAltFormat = idCampoData + "Submit";
	idCampoDataAltFormat = escapeSelector(idCampoDataAltFormat);
	var valoreDataAltFormat = $("#"+idCampoDataAltFormat).val();
	if(!!valoreDataAltFormat){
		var dataFormattata = convertDate(valoreDataAltFormat);
		inputData.val(dataFormattata);
	}else{
		console.log("valoreDataAltFormat null");
	}
}

function escapeSelector( elemSelector ) {
	result = elemSelector;
	if(elemSelector){
		result = elemSelector.replace( /(:|\.|\[|\]|,|=)/g, "\\$1" );
	}
    return result;
}

//Cambia una data da "yyyy-mm-dd" a "dd/mm/yyyy"
function convertDate(input) {
	var parts = input.match(/(\d+)/g);
	return parts[2]+"/"+parts[1]+"/"+parts[0];
}

//Aggiunge un input con la data attuale.
//@Param inputName. Il "name" del campo di input
function aggiungiDataSubmit(inputName){
	var dataSubmitFormString = convertDateTimeToString(new Date());
	console.log(dataSubmitFormString);
	$("<input />").attr("type", "hidden").attr("name", inputName).attr("value", dataSubmitFormString).appendTo("#formPrincipale");
}

function cancellaAttivita(inputName){
	var callback = function() {
		$("<input />").attr("type", "hidden").attr("name", inputName).attr("value", "1").appendTo("#formPrincipale");
		$('form').submit();
	};
	confirmKS('Richiesta conferma annullamento', '', 'Cancella', 'Conferma', callback);
}

//Converte una stringa in float se possibile oppure torna 0
function getValoreFloat(valore){
	var valoreFloat = parseFloat(0);
	if(isValoreFloat(valore)){
		valore = valore.replace(/\./g, '');
		valore = valore.replace(/\,/g, '.');
		valoreFloat = parseFloat(valore);
	}
	return valoreFloat;
}

function isValoreFloat(valore){
	var isFloat = false;

	if(valore && valore.length>0){
		valore = valore.replace(/\./g, '');
		valore = valore.replace(/\,/g, '.');
		if (!isNaN(valore) ) {
			isFloat = true;
		}
	}
	return isFloat;
}

function formatFloat(valore){
	var options = {
			maximumFractionDigits: 2,
			minimumFractionDigits: 2
	};
	var result = valore;
	if(!isNaN(valore)){
		result = valore.toLocaleString(undefined, options);
	}
	return result;
}

function stringToNumber(inputNumber){
	var result = 0;
	if (inputNumber && !isNaN(inputNumber) ) {
		result = parseFloat(inputNumber);
	}
	return result;
}

//////////////////////////////////////////////////////////////////


///  devbridgeAutocomplete  //////////////////////////////////////

function initDevbridgeAutocomplete(){
	$.extend( $.Autocomplete.defaults, {
		minChars: 3,
		deferRequestBy: 600,
		ajaxSettings: {timeout: 3000},
	    onSelect: function (suggestion) {
	    	console.log('selected value: ' + suggestion.data);
	    },
	    onSearchStart: function (query) {
			console.log('onSearchStart: ' + query);
	    },
	    onSearchComplete: function (query, suggestions) {
	    	console.log('onSearchComplete: ' + query + " -- " + suggestions);
	    },
	    onSearchError: function (query, jqXHR, textStatus, errorThrown) {
	    	console.log(textStatus);
	    	if (textStatus == "error"){
	    		//TODO: gestire
	    	}else if (textStatus == "timeout"){
	    		//TODO: gestire
	    	}else if (textStatus == "parsererror"){
	    		//TODO: gestire
	    	}
	    },
	    dataType: 'json',
	    preventBadQueries: true,
	    paramName: 'where',
	    params: {},
	    transformResult: function(response) {
	    	return response;
	    },
	    showNoSuggestionNotice: true,
	    noSuggestionNotice: "nessun risultato"
	} );
}

//////////////////////////////////////////////////////////////////////






///////// wait modal /////////////////////////////////////////////////




(function (root, factory) {
	'use strict';

	if (typeof define === 'function' && define.amd) {
		define(['jquery'], function ($) {
			return (root.waitingDialog = factory($));
		});
	}
	else {
		root.waitingDialog = root.waitingDialog || factory(root.jQuery);
	}

}(this, function ($) {
	'use strict';

	/**
	 * Dialog DOM constructor
	 */
	function constructDialog($dialog) {
		// Deleting previous incarnation of the dialog
		if ($dialog) {
			$dialog.remove();
		}
		return $(
		       '<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true" style="padding-top:15%; overflow-y:visible;">' +
				'<div class="modal-dialog modal-lg modal-dialog-scrollable" role="document">' +
					'<div class="modal-content">' +
						'<div class="modal-body">' +
							'<div class="progress">' +
								'<div class="progress-bar progress-bar-striped progress-bar-animated" style="width: 100%"></div>' +
							'</div>' +
						'</div>' +
					'</div>' +
				'</div>' +
			'</div>'
		);
	}

	var $dialog, // Dialog object
		settings; // Dialog settings

	return {
		/**
		 * Opens our dialog
		 * @param message Custom message
		 * @param options Custom options:
		 *   options.headerText - if the option is set to boolean false,
		 *     it will hide the header and "message" will be set in a paragraph above the progress bar.
		 *     When headerText is a not-empty string, "message" becomes a content
		 *     above the progress bar and headerText string will be set as a text inside the H3;
		 *   options.headerSize - this will generate a heading corresponding to the size number. Like <h1>, <h2>, <h3> etc;
		 *   options.headerClass - extra class(es) for the header tag;
		 *   options.dialogSize - bootstrap postfix for dialog size, e.g. "sm", "m";
		 *   options.progressType - bootstrap postfix for progress bar type, e.g. "success", "warning";
		 *   options.contentElement - determines the tag of the content element.
		 *     Defaults to "p", which will generate a <p> tag;
		 *   options.contentClass - extra class(es) for the content tag.
		 */
		show: function (message, options) {
			// Assigning defaults
			if (typeof options === 'undefined') {
				options = {};
			}
			if (typeof message === 'undefined') {
				message = 'Loading';
			}
			settings = $.extend({
				headerText: '',
				headerSize: 3,
				headerClass: '',
				dialogSize: 'm',
				progressType: '',
				contentElement: 'p',
				contentClass: 'content',
				onHide: null, // This callback runs after the dialog was hidden
				onShow: null // This callback runs after the dialog was shown
			}, options);

			var $headerTag, $contentTag;

			$dialog = constructDialog($dialog);

			// Configuring dialog
			//$dialog.find('.modal-dialog').attr('class', 'modal-dialog').addClass('modal-' + settings.dialogSize);
			//$dialog.find('.progress-bar').attr('class', 'progress-bar progress-bar-striped progress-bar-animated');
			if (settings.progressType) {
				$dialog.find('.progress-bar').addClass('progress-bar-' + settings.progressType);
				$dialog.find('.progress-bar').addClass('bg-' + settings.progressType);
			}

			// Generate header tag
			$headerTag = $('<h' + settings.headerSize + ' />');
			$headerTag.css({ 'margin': 0 });
			if (settings.headerClass) {
				$headerTag.addClass(settings.headerClass);
			}

			// Generate content tag
			$contentTag = $('<' + settings.contentElement + ' />');
			if (settings.contentClass) {
				$contentTag.addClass(settings.contentClass);
			}

			if (settings.headerText === false) {
				$contentTag.html(message);
				$dialog.find('.modal-body').prepend($contentTag);
			}
			else if (settings.headerText) {
				$headerTag.html(settings.headerText);
				$dialog.find('.modal-header').html($headerTag).show();

				$contentTag.html(message);
				$dialog.find('.modal-body').prepend($contentTag);
			}
			else {
				$headerTag.html(message);
				$dialog.find('.modal-header').html($headerTag).show();
			}

			// Adding callbacks
			if (typeof settings.onHide === 'function') {
				$dialog.off('hidden.bs.modal').on('hidden.bs.modal', function () {
					settings.onHide.call($dialog);
				});
			}
			if (typeof settings.onShow === 'function') {
				$dialog.off('shown.bs.modal').on('shown.bs.modal', function () {
					settings.onShow.call($dialog);
				});
			}
			// Opening dialog
			$dialog.modal();
		},
		/**
		 * Closes dialog
		 */
		hide: function () {
			if (typeof $dialog !== 'undefined') {
				$dialog.modal('hide');
			}
		},
		/**
		 * Changes or displays current dialog message
		 */
		message: function (newMessage) {
			if (typeof $dialog !== 'undefined') {
				if (typeof newMessage !== 'undefined') {
					return $dialog.find('.modal-header>h' + settings.headerSize).html(newMessage);
				}
				else {
					return $dialog.find('.modal-header>h' + settings.headerSize).html();
				}
			}
		}
	};

}));

/////////////////////////////////////////////////////////////////////


////////////// ALERT ////////////////////////////////////////////////

(function (root, factory) {
	'use strict';

	if (typeof define === 'function' && define.amd) {
		define(['jquery'], function ($) {
			return (root.mostraAlertHt = factory($));
		});
	}
	else {
		root.mostraAlertHt = root.mostraAlertHt || factory(root.jQuery);
	}

}(this, function ($) {
	'use strict';

	/**
	 * Dialog DOM constructor
	 */
	function constructDialog($dialog) {
		// Deleting previous incarnation of the dialog
		if ($dialog) {
			$dialog.remove();
		}
		return $(
			'<div class="modal fade" id="alertModalHT" tabindex="-1" role="dialog" aria-labelledby="myModalLabelHT" aria-hidden="true" style="display: none;">'+
				'<div class="modal-dialog">'+
					'<div class="modal-content">'+
						'<div class="modal-header">'+
							'<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>'+
							'<h4 class="modal-title" id="myModalLabelHT"></h4>'+
						'</div>'+
						'<div class="modal-body"></div>'+
						'<div class="modal-footer">'+
							'<button type="button" class="btn btn-default" data-dismiss="modal">Chiudi</button>'+
						'</div>'+
					'</div>'+
				'</div>'+
			'</div>'
		);
	}

	var $dialog, // Dialog object
		settings; // Dialog settings

	return {
		/**
		 * Opens our dialog
		 * @param message Custom message
		 * @param options Custom options:
		 *   options.headerText - if the option is set to boolean false,
		 *     it will hide the header and "message" will be set in a paragraph above the progress bar.
		 *     When headerText is a not-empty string, "message" becomes a content
		 *     above the progress bar and headerText string will be set as a text inside the H3;
		 *   options.headerSize - this will generate a heading corresponding to the size number. Like <h1>, <h2>, <h3> etc;
		 *   options.headerClass - extra class(es) for the header tag;
		 *   options.dialogSize - bootstrap postfix for dialog size, e.g. "sm", "m";
		 *   options.progressType - bootstrap postfix for progress bar type, e.g. "success", "warning";
		 *   options.contentElement - determines the tag of the content element.
		 *     Defaults to "p", which will generate a <p> tag;
		 *   options.contentClass - extra class(es) for the content tag.
		 */
		show: function (message, options) {
			//Se gia mostrato non apro un nuovo dialog
			if (typeof $dialog !== 'undefined') {
				return;
			}
			// Assigning defaults
			if (typeof options === 'undefined') {
				options = {};
			}
			if (typeof message === 'undefined') {
				message = 'Loading';
			}
			settings = $.extend({
				headerText: '',
				headerSize: 3,
				headerClass: '',
				dialogSize: 'm',
				progressType: '',
				contentElement: 'p',
				contentClass: 'content',
				onHide: null, // This callback runs after the dialog was hidden
				onShow: null // This callback runs after the dialog was shown
			}, options);

			var $headerTag, $contentTag;

			$dialog = constructDialog($dialog);
			$dialog.on('hidden.bs.modal', fixMultipleModal);

			// Configuring dialog
			$dialog.find('.modal-dialog').attr('class', 'modal-dialog').addClass('modal-' + settings.dialogSize);

			// Generate header tag
			$headerTag = $('<h' + settings.headerSize + ' />');
			$headerTag.css({ 'margin': 0 });
			if (settings.headerClass) {
				$headerTag.addClass(settings.headerClass);
			}

			// Generate content tag
			$contentTag = $('<' + settings.contentElement + ' />');
			if (settings.contentClass) {
				$contentTag.addClass(settings.contentClass);
			}

			if (settings.headerText === false) {
				$contentTag.html(message);
				$dialog.find('.modal-body').prepend($contentTag);
			}
			else if (settings.headerText) {
				$headerTag.html(settings.headerText);
				$dialog.find('.modal-header').html($headerTag).show();

				$contentTag.html(message);
				$dialog.find('.modal-body').prepend($contentTag);
			}
			else {
				$headerTag.html(message);
				$dialog.find('.modal-header').html($headerTag).show();
			}

			// Adding callbacks
			if (typeof settings.onHide === 'function') {
				$dialog.off('hidden.bs.modal').on('hidden.bs.modal', function () {
					settings.onHide.call($dialog);
				});
			}
			if (typeof settings.onShow === 'function') {
				$dialog.off('shown.bs.modal').on('shown.bs.modal', function () {
					settings.onShow.call($dialog);
				});
			}
			// Opening dialog
			$dialog.modal();
		},
		/**
		 * Closes dialog
		 */
		hide: function () {
			if (typeof $dialog !== 'undefined') {
				$dialog.modal('hide');
			}
		},
		/**
		 * Changes or displays current dialog message
		 */
		message: function (newMessage) {
			if (typeof $dialog !== 'undefined') {
				if (typeof newMessage !== 'undefined') {
					return $dialog.find('.modal-header>h' + settings.headerSize).html(newMessage);
				}
				else {
					return $dialog.find('.modal-header>h' + settings.headerSize).html();
				}
			}
		}
	};

}));

/////////////////////////////////////////////////////////////////////


function confirmKS(heading, question, cancelButtonTxt, okButtonTxt, callbackFunction) {
	if($("#confirmModal").length){
		$("#confirmModal").remove();
	}
	var confirmModal = 
    $('<div class="modal fade" id="confirmModal">' +
       '<div class="modal-dialog modal-lg">' +
        '<div class="modal-content">' +
         '<div class="modal-header">' +
		  '<h3>' + heading +'</h3>' +
		  '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
         '</div>' +

         '<div class="modal-body">' +
           question +
         '</div>' +

         '<div class="modal-footer">' +
		  '<button type="button" class="btn btn-default" data-dismiss="modal">' +
           cancelButtonTxt + 
          '</button>' +
          '<button type="button" class="btn btn-primary" id="okButton">' +
           okButtonTxt +
          '</button>' +
         '</div>' +
        '</div>' +
       '</div>' +
      '</div>');

    confirmModal.find('#okButton').click(function(event) {
      callbackFunction();
      confirmModal.modal('hide');
    });

    confirmModal.modal('show');

	confirmModal.on('hidden.bs.modal', fixMultipleModal);
};

function fixMultipleModal(){
	//If there are any visible
	if($(".modal:visible").length > 0) {
	//Slap the class on it (wait a moment for things to settle)
		setTimeout(function() {
			$('body').addClass('modal-open');
		},100);
	}
}
/////////////////////////////////////////////////////////////////////




//////// File upload ///////////////////////////////////////////////


function verificaUploadfileCaricato(multiUploadElement){
	var result = false;
	var nomeFileUploaded = multiUploadElement.find('input[type="hidden"]').val();
	if(nomeFileUploaded){
		result = true;
	}
	return result;
}



////////////////////////////////////////////////////////////////////







//////// Gestione valuta  ///////////////////////////////////////////////


    //keys which shouldn't be blocked
var utlilityKeys = [
    37,   //left
    39,   //right
    8,    //backspace
    46,   //del
    9,    //tab
    32,   //space
    13,   //enter
    96    //return
];

//keys allowed in the phone input (inc shift + key)
var allowed = [[
    107,  //+
    109,  //-
    189,   // -
    110, // .
    190, // .
    187, // +
    188 // ,
],[
    57,   //shift + (
    48,   //shift + )
    187   //shift + +
]];

// returns true if number
function inRange(i){
    return ((i >= 48 && i <= 57) || (i >= 96 && i <= 105));
}

function functionChangeImporto(){
	var valoreFloat = getValoreFloat($(this).val());
	$(this).val(formatFloat(valoreFloat));
}

function functionKeyDownImporto(e){
	var invalidKey;
    if(e.shiftKey){
		//if the shift key is held down check the second array
        invalidKey = $.inArray(e.keyCode, allowed[1]) < 0;
	}else{
		//else check the first array and number keys
        invalidKey = $.inArray(e.keyCode, allowed[0]) < 0;
	}
	if(invalidKey && !inRange(e.keyCode) && ($.inArray(e.keyCode, utlilityKeys) < 0)){
		//prevent the keystroke
        e.preventDefault();
	}else{
		if($(this).val().toLowerCase().indexOf(",")!=-1 && (e.keyCode==188)){
			e.preventDefault();
		}
        if($(this).val().toLowerCase().indexOf("-")!=-1 && (e.keyCode==109 || e.keyCode==189 || e.keyCode==107 || e.keyCode==187 )  ){
			e.preventDefault();
		}
        if($(this).val().toLowerCase().indexOf("+")!=-1 && (e.keyCode==107 || e.keyCode==187 || e.keyCode==109 || e.keyCode==189 )  ){
			e.preventDefault();
		}
        if( $(this).val().length>0 && ( e.keyCode==107 || e.keyCode==187 || e.keyCode==109 || e.keyCode==189)){
			e.preventDefault();
		}

		var testoSelezionato = window.getSelection().toString();
		var testoNuovo = $(this).val();
		if(testoSelezionato!=""){
			testoNuovo = testoNuovo.replace(testoSelezionato, e.key);
		}else{
			var targetStart = e.target.selectionStart;
			var preText = "";
			if(targetStart>0){
				preText = testoNuovo.substring(0, targetStart);
			}
			var targetEnd = e.target.selectionEnd;
			var postText = testoNuovo.substring(targetEnd);
			testoNuovo = preText+e.key+postText;
		}

		var importoReg = /^[\-]?[1-9][0-9\.]*(\,[0-9]{0,2})?$/;
		var valoreDaTestare = testoNuovo;
		
		if("-"==valoreDaTestare || /^[\-]?[0]{1}(\,[0-9]{0,2})?$/.test(valoreDaTestare)){
			return;
		}
		var resRegExp = importoReg.test(valoreDaTestare);
		if(($.inArray(e.keyCode, utlilityKeys) < 0) && resRegExp==false){
			e.preventDefault();
		}
	}
}

////////////////////////////////////////////////////////////////////


function aggiungiErroreInput(idInput, testoMessaggio){
	var inputGroup = $('#'+idInput).parent('.form-group');
	if(inputGroup.length==0){
		console.log("form-group non trovato per input: "+idInput);
	}else{
		rimuoviErroreInput(idInput);
		$('#'+idInput).addClass('is-invalid');
		$('#'+idInput).after("<div class='invalid-feedback'><p>"+testoMessaggio+"</p></div>");
		inputGroup.find('.invalid-feedback').show();
	}
}

function rimuoviErroreInput(idInput){
	var inputGroup = $('#'+idInput).parent('.form-group');
	if(inputGroup.length==0){
		console.log("form-group non trovato per input: "+idInput);
	}else{
		$('#'+idInput).removeClass('is-invalid');
		inputGroup.find('.invalid-feedback').remove();
	}
}

//tipoMessaggio ['success', 'danger', 'warning', 'info']
function visualizzaMessaggio(idMessageDiv, testoMessaggio, tipoMessaggio){
	rimuoviMessaggio(idMessageDiv);
	$('#'+idMessageDiv).addClass('alert');
	if(!tipoMessaggio){
		tipoMessaggio='success';
	}
	$('#'+idMessageDiv).addClass('alert-'+tipoMessaggio);
	$('#'+idMessageDiv).append(testoMessaggio);
}

function rimuoviMessaggio(idMessageDiv){
	$('#'+idMessageDiv+' > p').remove();
	$('#'+idMessageDiv).removeClass();
}


////////////////////////////////////////////////////////////////////

////////tinymce

function init(elemento){
	tinymce.init({
		closed: /^(br|hr|input|meta|img|link|param|area|source)$/,
		selector: elemento,
		height: "600px",
		content_style: ".mce-content-body, p, body, span {font-family:Trebuchet MS; font-size:12pt;}",
		plugins: [
			"advlist autolink autosave link image lists charmap print preview hr anchor pagebreak spellchecker",
			"searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
			"table  directionality emoticons template textcolor paste fullpage textcolor colorpicker textpattern"
		],
		toolbar1: "| bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | forecolor backcolor | 	link | bullist outdent indent | table | fullscreen",
		// la seguente istruzione serve per non permettere il resizing degli oggetti ( es dell'oggetto tabella)
		//object_resizing : false,

		// e considerando che il template ha una certa formattazione,allora le tabelle inserite saranno al massimo larghe 450px
		//extended_valid_elements: "table[class=table|border:1|width:450px|border-color:#0d0101|cellpadding:1|cellspacing:1]",
		//extended_valid_elements : "omissis",
		//custom_elements: "omissis",
		table_toolbar: "tabledelete | tableinsertrowbefore tableinsertrowafter tabledeleterow | tableinsertcolbefore tableinsertcolafter tabledeletecol",

		setup: function(ed) {
			ed.on('init', function() {
				ed.buttons.table.menu.splice(1,1);
			});
		},
		//menubar: false,
		menubar: 'file edit insert view format table tools help',
		statusbar: false,
		toolbar_items_size: 'small',
		contextmenu_never_use_native: false,
		advlist_bullet_styles: 'default',
		//paste_as_text: true,
		paste_remove_styles_if_webkit: true
	});
}

function formatStringForDocx4j(str){
	var a= $(str).removeAttr("face").removeAttr("data-mce-style");
	var valore=a.html();
	var  valore= valore.replace(/ data-mce-bogus=\"1\"/g, "").replace(/<br>/g,"<br/>").replace(/&nbsp;/g," ").replace(/&quot;/g,"'");
	if((valore=="<p></p>") ||(valore=="<p><br/></p>")){
		valore="";
	}else{
		valore="<p style=\"font-family:\'Trebuchet MS\'; font-size:12pt;\">"+valore+"</p>";
	}
	return valore;
}


////////////////////////////////////////////////////////////////////




/////////  SELECT2 /////////////////////////////////////////////////

function initSelect2(){
	$.fn.modal.Constructor.prototype.enforceFocus = function() {};
	if($.fn.select2){
		$.fn.select2.defaults.set( "theme", "bootstrap" );
		$.fn.select2.defaults.set( "allowClear", true );
		$.fn.select2.defaults.set( "language", "it" );
		$('.select2').select2({
			placeholder: 'Seleziona'
		});
	}
}



////////////////////////////////////////////////////////////////////


$.fn.bindFirst = function(name, fn) {
    this.on(name, fn);
    this.each(function() {
        var handlers = $._data(this, 'events')[name.split('.')[0]];
        var handler = handlers.pop();
        handlers.splice(0, 0, handler);
    });
};

////////////////////////////////////////////////////////////////////

function verificaCodiceFiscale(codiceFiscale){
	//const codFiscRegex = /^[A-Z]{6}\d{2}[A-Z]\d{2}[A-Z]\d{3}[A-Z]$/;
	const codFiscRegex = /^[A-Z]{6}[0-9LMNPQRSTUV]{2}[A-Z]{1}[0-9LMNPQRSTUV]{2}[A-Z]{1}[0-9LMNPQRSTUV]{3}[A-Z]{1}$/;
	return codFiscRegex.test(codiceFiscale);
}
