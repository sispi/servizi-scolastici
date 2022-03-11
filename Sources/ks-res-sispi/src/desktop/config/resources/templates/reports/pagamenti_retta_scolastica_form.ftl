<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/jquery.tokeninput.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/token-input-bootstrappy.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<div class='report-search-form'>
 <h2>Ricerca pagamenti</h2>
 <form action='/reports/report' method='GET'>
  <input type='hidden' name='qt' value='pagamenti_retta_scolastica'>
  <input type='hidden' name='CF_MINORE' value=''>
  <div class='panel panel-default'>
   <div class='panel-body'>
    <div class='row'>
	 <div class='form-group col-md-6'>
      <label for='CF_MINORE'>C.F. Iscritto</label>
	  <input id='CF_MINORE' class='form-control'>
     </div>
    </div>
	<div class='row'>
	 <div class='form-group col-md-6'>
      <label for='DATA_RICEVUTA_DA'>Data Ricevuta Da</label>
      <input type='text' id='DATA_RICEVUTA_DA' name='DATA_RICEVUTA_DA' class='form-control data-input' autocomplete='off' />
     </div>
	 <div class='form-group col-md-6'>
      <label for='DATA_RICEVUTA_A'>Data Ricevuta A</label>
      <input type='text' id='DATA_RICEVUTA_A' name='DATA_RICEVUTA_A' class='form-control data-input' autocomplete='off' />
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
	initDateInput();
});

function initDateInput(){
	$.datepicker.setDefaults({
		dateFormat:'dd/mm/yy'
	});
	$('#DATA_RICEVUTA_DA').datepicker();
	$('#DATA_RICEVUTA_A').datepicker();
}

function initAutoCompleteCFMinore(){
	var settingsTokenInput = {};
	settingsTokenInput.theme = "facebook";
	settingsTokenInput.jsonContainer = "data";
	settingsTokenInput.propertyToSearch = "NAME";
	settingsTokenInput.tokenValue = "ID";
	settingsTokenInput.tokenLimit = 1;
	settingsTokenInput.hintText = "Digita un carattere";
	settingsTokenInput.searchingText = "Ricerca..";
	settingsTokenInput.noResultsText = "Nessun risultato";
	settingsTokenInput.deleteText = "<i class='icon-remove glyphicon glyphicon-remove small'></i>";
	settingsTokenInput.searchDelay = 500;
	settingsTokenInput.onAdd = elementoSelezionatoTokenInput;
	settingsTokenInput.onDelete = elementoRimossoTokenInput;
	$('#CF_MINORE').tokenInput('/docer/v1/report?qt=autocomplete_codicefiscaleminore', settingsTokenInput);
}

function elementoSelezionatoTokenInput(item) {
	$('input[name="CF_MINORE"]').val(item.ID);
}

function elementoRimossoTokenInput(item){
	$('input[name="CF_MINORE"]').val("");
}

</script>
