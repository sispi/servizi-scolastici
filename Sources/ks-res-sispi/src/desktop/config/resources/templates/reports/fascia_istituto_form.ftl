<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/jquery.tokeninput.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/token-input-bootstrappy.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<div class='report-search-form'>
 <h2>Ricerca Fascia/Istituto</h2>
 <form action='/reports/report' method='GET'>
  <input type='hidden' name='qt' value='fascia_istituto'>
  <input type='hidden' name='CODICE_FISCALE' value=''>
  <div class='panel panel-default'>
   <div class='panel-body'>
    <div class='row'>
	 <div class='form-group col-md-6'>
      <label for='CODICE_FISCALE'>C.F. Iscritto</label>
	  <input id='CODICE_FISCALE' class='form-control'>
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
	$('#CODICE_FISCALE').tokenInput('/docer/v1/report?qt=autocomplete_codicefiscaleminore', settingsTokenInput);
}

function elementoSelezionatoTokenInput(item) {
	$('input[name="CODICE_FISCALE"]').val(item.ID);
}

function elementoRimossoTokenInput(item){
	$('input[name="CODICE_FISCALE"]').val("");
}

</script>
