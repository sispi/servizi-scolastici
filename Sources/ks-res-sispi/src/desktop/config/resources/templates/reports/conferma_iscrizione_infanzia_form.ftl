<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/jquery.tokeninput.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/token-input-bootstrappy.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<div class='report-search-form'>
 <h2>Ricerca Iscrizione</h2>
 <form action='/reports/report' method='GET'>
  <input type='hidden' name='qt' value='conferma_iscrizione_infanzia'>
  <input type='hidden' name='CF_MINORE_ANAGRAFE' value=''>
  <input type='hidden' name='TIPO_PROCEDIMENTO' id='TIPO_PROCEDIMENTO' value=''>
  <div class='panel panel-default'>
   <div class='panel-body'>
    <div class='row'>
	 <div class='form-group col-md-6'>
      <label for='CF_MINORE_ANAGRAFE'>C.F. iscritto</label>
	  <input id='CF_MINORE_ANAGRAFE' class='form-control'>
     </div>
     <div class='form-group col-md-6'>
      <label for='ANNO_SCOLASTICO'>Anno scolastico</label>
      <select name='ANNO_SCOLASTICO' id='ANNO_SCOLASTICO' class='form-control'>
       <option value=''></option>
       <option value='2020/2021'>2020/2021</option>
       <option value='2021/2022'>2021/2022</option>
      </select>
     </div>
    </div>
	<div class='row'>
	 <div class='form-group col-md-6'>
      <label for='NOME_MINORE'>Nome minore</label>
	  <input name='NOME_MINORE' id='NOME_MINORE' class='form-control'>
     </div>
	 <div class='form-group col-md-6'>
      <label for='COGNOME_MINORE'>Cognome minore</label>
	  <input name='COGNOME_MINORE' id='COGNOME_MINORE' class='form-control'>
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
	settaQueryParams();
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
	var tipoProcedimento = "ScuolaInfanzia";
	$('#CF_MINORE_ANAGRAFE').tokenInput('/docer/v1/report?qt=autocomplete_codicefiscaleminore&TIPO_PROCEDIMENTO='+tipoProcedimento, settingsTokenInput);
}

function settaQueryParams(){
	const urlParams = new URLSearchParams(window.location.search);
	for(var key of urlParams.keys()) {
		if(urlParams.has(key)){
			$('#'+key).val(urlParams.get(key));
		}
	}
}

function elementoSelezionatoTokenInput(item) {
	$('input[name="CF_MINORE_ANAGRAFE"]').val(item.ID);
}

function elementoRimossoTokenInput(item){
	$('input[name="CF_MINORE_ANAGRAFE"]').val("");
}

</script>
