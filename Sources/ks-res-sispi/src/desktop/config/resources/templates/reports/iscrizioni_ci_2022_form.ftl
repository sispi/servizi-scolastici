<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/jquery.tokeninput.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<script type="text/javascript" src="/static/palermo/js/gestione-centri-infanzia.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/token-input-bootstrappy.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<div class='report-search-form'>
 <h2>Ricerca Iscrizioni Centro Infanzia</h2>
 <form action='/reports/report' method='GET'>
  <div class='panel panel-default'>
   <input type='hidden' name='qt' value='iscrizioni_ci_2022'>
   <input type='hidden' name='CF_RICHIEDENTE'>
   <div class='panel-body'>
    <div class='row'>
	 <div class='form-group col-md-6'>
      <label for='cfRichiedente'>Codice Fiscale Richiedente</label>
	  <input id='cfRichiedente' class='form-control'>
     </div>
	</div>
    <div class='row'>
	 <div class='form-group col-md-6'>
      <label for='idIstanza'>ID Istanza</label>
	  <input id='idIstanza' class='form-control' name='ID_ISTANZA'>
     </div>
	</div>
	<hr class='clearfix' />
    <div class='row text-center'>
     <div class='form-group col-md-3 col-md-offset-3'>
	  <button type='submit' class='btn btn-primary' >Esegui&nbsp;</button>
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
	$('#cfRichiedente').tokenInput('/docer/v1/report?qt=autocomplete_codicefiscale_ci_rest', settingsTokenInput);
}

function elementoSelezionatoTokenInput(item) {
	$('input[name="CF_RICHIEDENTE"]').val(item.ID);
}

function elementoRimossoTokenInput(item){
	$('input[name="CF_RICHIEDENTE"]').val("");
}

</script>