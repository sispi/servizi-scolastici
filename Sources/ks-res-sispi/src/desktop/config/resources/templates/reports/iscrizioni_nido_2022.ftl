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
   <td>${(item.STATO_DESCR)!}</td>
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
       <#if "Si" == (item.CRITERIO_4)! > ${(params["facet.CRITERIO_4.label"])!} &nbsp; </#if>
       <#if "Si" == (item.CRITERIO_5)! > ${(params["facet.CRITERIO_5.label"])!} &nbsp; </#if>
       <#if "Si" == (item.CRITERIO_6)! > ${(params["facet.CRITERIO_6.label"])!} &nbsp; </#if>
       <#if "Si" == (item.CRITERIO_7)! > ${(params["facet.CRITERIO_7.label"])!} &nbsp; </#if>
       <#if "Si" == (item.CRITERIO_8)! > ${(params["facet.CRITERIO_8.label"])!} &nbsp; </#if>
       <#if "Si" == (item.CRITERIO_9)! > ${(params["facet.CRITERIO_9.label"])!} &nbsp; </#if>
       <#if "Si" == (item.CRITERIO_10)! > ${(params["facet.CRITERIO_10.label"])!} &nbsp; </#if>
       <#if "Si" == (item.CRITERIO_11)! > ${(params["facet.CRITERIO_11.label"])!} &nbsp; </#if>
      </p>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-4'>
      <label for='VACCINAZIONE_MINORE'>${(params["facet.VACCINAZIONE_MINORE.label"])!}:</label>
      <p class='form-control-static' id='VACCINAZIONE_MINORE'>${(item.VACCINAZIONE_MINORE)!}</p>
     </div>
     <div class='form-group col-md-4'>
      <label for='DATA_NASCITA_MINORE'>${(params["facet.DATA_NASCITA_MINORE.label"])!}:</label>
      <p class='form-control-static' id='DATA_NASCITA_MINORE'>${(item.DATA_NASCITA_MINORE)!}</p>
     </div>
     <div class='form-group col-md-4'>
      <label for='COMUNE_RESIDENZA_MINORE'>${(params["facet.COMUNE_RESIDENZA_MINORE.label"])!}:</label>
      <p class='form-control-static' id='COMUNE_RESIDENZA_MINORE'>${(item.COMUNE_RESIDENZA_MINORE)!}</p>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-6'>
      <label for='NOME_ASILO'>${(params["facet.NOME_ASILO.label"])!}:</label>
      <p class='form-control-static' id='NOME_ASILO'>${(item.NOME_ASILO)!} </p>
     </div>
    </div>
    <div class='row'>
     <div class='form-group col-md-4'>
      <label for='ISEE_PRESENTATO'>${(params["facet.ISEE_PRESENTATO.label"])!}:</label>
      <p class='form-control-static' id='ISEE_PRESENTATO'>${(item.ISEE_PRESENTATO)!}</p>
     </div>
     <div class='form-group col-md-4'>
      <label for='STATO_NASCITA'>${(params["facet.STATO_NASCITA.label"])!}:</label>
      <p class='form-control-static' id='STATO_NASCITA'>${(item.STATO_NASCITA)!}</p>
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
});

function showHideElement(idElement, isHidden){
	if(isHidden){  
		$('#'+idElement).show();      
	}
}
</script>