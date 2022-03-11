<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<script type="text/javascript" src="/static/palermo/js/gestione-fascia-istituto.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<div class=''>
<div class='row'>
<table class='table table-striped table-sm' id='risultatoQueryTable'>
 <thead>
  <tr>
   <th>${(params["facet.ID_ISTANZA.label"])!}</th>
   <th>${(params["facet.DOCNUM_DOMANDA.label"])!}</th>
   <th>${(params["facet.DATA_PRESENTAZIONE_FORMATTATA.label"])!}</th>
   <th><a class="order" href="/reports/report?orderBy=CF_RICHIEDENTE${querystringParams}">${(params["facet.CF_RICHIEDENTE.label"])!}</a></th>
   <th>${(params["facet.CF_MINORE.label"])!}</th>
   <th>${(params["facet.NUMERO_PROTOCOLLO.label"])!}</th>
   <th>${(params["facet.ANNO_PROTOCOLLO.label"])!}</th>
   <th>${(params["facet.TIPO_RICHIESTA.label"])!}</th>
   <th>${(params["facet.STATO.label"])!}</th>
   <th></th>
  </tr>
 </thead>
 <tbody>
 <#list data as item >
  <tr class='${item?item_parity}-row'>
   <td><strong><a href='/bpm/instances/details?id=${item.ID_ISTANZA}'>${(item.ID_ISTANZA)!}</a></strong></td>
   <td><strong><a href='/documenti/viewProfile?DOCNUM=${(item.DOCNUM_DOMANDA)!}'>${(item.DOCNUM_DOMANDA)!}</a></strong></td>
   <td>${(item.DATA_PRESENTAZIONE_FORMATTATA)!}</td>
   <td>${(item.CF_RICHIEDENTE)!}</td>
   <td>${(item.CF_MINORE)!}</td>
   <td>${(item.NUMERO_PROTOCOLLO)!}</td>
   <td>${(item.ANNO_PROTOCOLLO)!}</td>
   <td>${(item.TIPO_RICHIESTA)!}</td>
   <td>${(item.STATO)!}</td>
   <td>
    <a class='showDetail' title='Dettaglio ${(item.ID_ISTANZA)!}' data-idrecord='dettaglio${(item.ID_ISTANZA)!}'><span class='glyphicon glyphicon-info-sign'></span></a>
    <#if (item.ID_ISTANZA_ISCRIZIONE)!?has_content>
	<a title='Dettaglio ${(item.ID_ISTANZA)!}' target='_blank' href='/reports/report?qt=iscrizioni_log&ID_ISTANZA_ISCRIZIONE=${(item.ID_ISTANZA)!}'><span class='glyphicon glyphicon-list' aria-hidden='true'></span></a>
	</#if>
   </td>
  </tr>
  <tr class='row-detail ${item?item_parity}-row' id='dettaglio${(item.ID_ISTANZA)!}'>
   <td colspan='9'>
    <div class='row'>
     <div class='form-group col-md-12'>
      <label for='LISTA_CRITERI'>${(params["facet.LISTA_CRITERI.label"])!}:</label>
      <p class='form-control-static' id='LISTA_CRITERI'>
	   <#if "Si" == (item.CRITERIO_1)! > ${(params["facet.CRITERIO_1.label"])!} &nbsp; </#if>
       <#if "Si" == (item.CRITERIO_2)! > ${(params["facet.CRITERIO_2.label"])!} &nbsp; </#if>
       <#if "Si" == (item.CRITERIO_3)! > ${(params["facet.CRITERIO_3.label"])!} &nbsp; </#if>
      </p>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-4'>
      <label for='NOME_SCUOLA'>${(params["facet.NOME_SCUOLA.label"])!}:</label>
      <p class='form-control-static' id='NOME_SCUOLA'>${(item.NOME_SCUOLA)!} </p>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-4'>
      <label for='FREQUENZA_ANTICIPATA'>${(params["facet.FREQUENZA_ANTICIPATA.label"])!}:</label>
      <p class='form-control-static' id='FREQUENZA_ANTICIPATA'>${(item.FREQUENZA_ANTICIPATA)!} </p>
     </div>
     <div class='form-group col-md-4'>
      <label for='VACCINAZIONE_MINORE'>${(params["facet.VACCINAZIONE_MINORE.label"])!}:</label>
      <p class='form-control-static' id='VACCINAZIONE_MINORE'>${(item.VACCINAZIONE_MINORE)!} </p>
     </div>
     <div class='form-group col-md-4'>
      <label for='DATA_NASCITA_MINORE'>${(params["facet.DATA_NASCITA_MINORE.label"])!}:</label>
      <p class='form-control-static' id='DATA_NASCITA_MINORE'>${(item.DATA_NASCITA_MINORE)!} </p>
     </div>
    </div>
    <div class='row'>
	 <#if "1" == (item.SCELTA_ASILO_NIDO)! >
     <div class='form-group col-md-4'>
      <label for='CIRCOSCRIZIONE_RESIDENZA'>${(params["facet.CIRCOSCRIZIONE_RESIDENZA.label"])!}:</label>
      <p class='form-control-static' id='CIRCOSCRIZIONE_RESIDENZA'>${(item.CIRCOSCRIZIONE_RESIDENZA)!}</p>
     </div>
	 </#if>
	 <#if "2" == (item.SCELTA_ASILO_NIDO)! >
     <div class='form-group col-md-4'>
      <label for='CIRCOSCRIZIONE_LAVORO'>${(params["facet.CIRCOSCRIZIONE_LAVORO.label"])!}:</label>
      <p class='form-control-static' id='CIRCOSCRIZIONE_LAVORO'>${(item.CIRCOSCRIZIONE_LAVORO)!}</p>
     </div>
	 </#if>
	 <#if "3" == (item.SCELTA_ASILO_NIDO)! >
     <div class='form-group col-md-4'>
      <label for='CIRCOSCRIZIONE_NONNI'>${(params["facet.CIRCOSCRIZIONE_NONNI.label"])!}:</label>
      <p class='form-control-static' id='CIRCOSCRIZIONE_NONNI'>${(item.CIRCOSCRIZIONE_NONNI)!}</p>
     </div>
	 </#if>
    </div>
   </td>
  </tr>
 </#list>
 </tbody>
</table>
</div>
</div>

<script>
$( document ).ready(function() {
	$('.row-detail').hide();
	$('.showDetail').on('click', function() {
		var idRecord = $(this).data('idrecord');
		var isHidden = $('#'+idRecord).is(':hidden');
		$('.row-detail').hide();
		showHideElement(idRecord, isHidden);
	});
	fixControlStatic();
});

function fixControlStatic(){
	$(".form-control-static").each(function(){
		var idInput = $(this).attr("id");
		var contenuto = $(this).html();
		if(contenuto.trim()==""){
			$(this).html("&nbsp;");
		}
	});
}

function showHideElement(idElement, isHidden){
	if(isHidden){  
		$('#'+idElement).show();      
	}
}
</script>