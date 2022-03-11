<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/jquery.tokeninput.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/token-input-bootstrappy.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<div class='report-search-form'>
 <h2>Ricerca Rata</h2>
 <form action='/reports/report' method='GET'>
  <input type='hidden' name='qt' value='modifica_rata'>
  <input type='hidden' name='CF_ISCRITTO'>
  <div class='panel panel-default'>
   <div class='panel-body'>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='CF_ISCRITTO'>C.F. iscritto</label>
      <input id='CF_ISCRITTO' class='form-control'>
     </div>
    </div>
    <hr class='clearfix' />
    <div class='row'>
     <div class='form-group col-md-4 offset-md-2'>
      <button class='btn btn-primary pull-right' type='submit'>Esegui&nbsp;</button>
     </div>
    </div>
   </div>
  </div>
 </form>
</div>

<script>

$( document ).ready(function() {
	initAutoCompleteCFMinore();
});

function initAutoCompleteCFMinore(){
	var settingsTokenInput = {};
	settingsTokenInput.theme = "facebook";
	settingsTokenInput.propertyToSearch = "NAME";
	settingsTokenInput.tokenValue = "ID";
	settingsTokenInput.tokenLimit = 1;
	settingsTokenInput.hintText = "Digita un carattere";
	settingsTokenInput.searchingText = "Ricerca..";
	settingsTokenInput.noResultsText = "Nessun risultato";
	settingsTokenInput.deleteText = "<i class='icon-remove glyphicon glyphicon-remove small'></i>";
	settingsTokenInput.searchDelay = 500;
	settingsTokenInput.preventDuplicates = true;
	settingsTokenInput.onResult = processaRisultati;
	settingsTokenInput.onAdd = elementoSelezionatoTokenInput;
	settingsTokenInput.onDelete = elementoRimossoTokenInput;
	$('#CF_ISCRITTO').tokenInput('/docer/v1/report?qt=autocomplete_codicefiscaleminore_rata', settingsTokenInput);
}

function  processaRisultati(results) {
	var listaElementi = [];
	var chiavi = {};
	$.each(results.data, function (index, value) {
		if( !(value.ID in chiavi) ){
			chiavi[value.ID] = value;
			listaElementi.push(value);
		}
    });
	return listaElementi;
}

function checkCfMinore(rata) {
  return rata > 18;
}

function elementoSelezionatoTokenInput(item) {
	$('input[name="CF_ISCRITTO"]').val(item.ID);
}

function elementoRimossoTokenInput(item){
	$('input[name="CF_ISCRITTO"]').val("");
}

</script>
