<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<div class=''>
 <div class='row'>
  <table class='table table-striped table-sm' id='risultatoQueryTable'>
   <thead>
    <tr>
     <th>ID</th>
     <th><a class='order' href='/reports/report?orderBy=CF_MINORE${querystringParams}'>${(params["facet.CF_MINORE.label"])!}</th>
     <th>${(params["facet.IMPORTO.label"])!}</th>
     <th><a class='order' href='/reports/report?orderBy=CF_CONTRIBUENTE${querystringParams}'>${(params["facet.CF_CONTRIBUENTE.label"])!}</th>
     <th>${(params["facet.DOCNUM_PRINCIPALE.label"])!}</th>
     <th>${(params["facet.MODALITA.label"])!}</th>
     <th>${(params["facet.NOME_ISTITUTO.label"])!}</th>
     <th>${(params["facet.DESCRIZIONE.label"])!}</th>
     <th>${(params["facet.DATA_ORA_RICEVUTA.label"])!}</th>
    </tr>
   </thead>
   <tbody>
   <#list data as item >
    <tr>
     <td><strong><a target='_blank' href='/bpm/instances/details?id=${(item.ID_PAGAMENTO)!}'>${(item.ID_PAGAMENTO)!}</a></strong></td>
     <td>${(item.CF_MINORE)!}</td>
     <td>${(item.IMPORTO)!}</td>
     <td>${(item.CF_CONTRIBUENTE)!}</td>
     <td><strong><a target='_blank' href='/documenti/viewProfile?DOCNUM=${(item.DOCNUM_PRINCIPALE)!}'>${(item.DOCNUM_PRINCIPALE)!}</a></strong></td>
     <td>${(item.MODALITA_STRING)!}</td>
     <td>${(item.NOME_ISTITUTO)!}</td>
     <td>${(item.DESCRIZIONE)!}</td>
     <td>${(item.DATA_ORA_RICEVUTA_STRING)!}${(item.DATA_ORA_RICEVUTA_PAY_STRING)!}</td>
    </tr>
   </#list>
   </tbody>
  </table>
 </div>
</div>

<script>

$( document ).ready(function() {

});

</script>
