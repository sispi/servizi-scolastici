<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<table class='table table-striped table-condensed'>
 <thead>
  <tr>
   <th>${(params["facet.ID.label"])!}</th>
   <th><a class='order' href='/reports/report?orderBy=CF_MINORE${querystringParams}'>${(params["facet.CF_MINORE.label"])!}</a></th>
   <th>${(params["facet.IMPORTO_PAGAMENTO.label"])!}</th>
   <th><a class='order' href='/reports/report?orderBy=CF_RICHIEDENTE${querystringParams}'>${(params["facet.CF_RICHIEDENTE.label"])!}</a></th>
   <th>${(params["facet.DOCNUM_PAGAMENTO.label"])!}</th>
   <th>${(params["facet.MODALITA.label"])!}</th>
   <th>${(params["facet.NOME_ISTITUTO.label"])!}</th>
   <th>${(params["facet.DESCRIZIONE.label"])!}</th>
   <th>${(params["facet.DATA_ORA_RICEVUTA.label"])!}</th>
  </tr>
 </thead>
 <tbody>
 <#list data as item >
  <tr>
   <td><strong><a target='_blank' href='/bpm/instances/details?id=${(item.ISTANZA_PAGAMENTO)!}'>${(item.ISTANZA_PAGAMENTO)!}</a></strong></td>
   <td>${(item.CF_MINORE)!}</td>
   <td>${(item.IMPORTO_STRING)!}</td>
   <td>${(item.CF_RICHIEDENTE)!}</td>
   <td><strong><a target='_blank' href='/documenti/viewProfile?DOCNUM=${(item.DOCNUM_PAGAMENTO)!}'>${(item.DOCNUM_PAGAMENTO)!}</a></strong></td>
   <td>${(params["facet.MODALITA_ID."+item.MODALITA_ID])!}</td>
   <td>${(item.NOME_ISTITUTO)!}</td>
   <td>${(item.DESCRIZIONE)!}</td>
   <td>${(item.DATA_ORA_RICEVUTA_STRING)!}${(item.DATA_ORA_RICEVUTA_PAY_STRING)!}</td>
  </tr>
 </#list>
 </tbody>
</table>


<script>

$( document ).ready(function() {

});

</script>
