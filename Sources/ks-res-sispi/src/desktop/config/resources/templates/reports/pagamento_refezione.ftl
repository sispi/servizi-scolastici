<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<table class='table table-striped table-condensed' id='risultatoQueryTable'>
 <thead>
  <tr>
   <th>${(params["facet.ID_ISTANZA.label"])!}</th>
   <th>${(params["facet.NUMERO_PROTOCOLLO.label"])!}</th>
   <th>${(params["facet.DATA_PROTOCOLLO.label"])!}</th>
   <th>${(params["facet.DOCNUM_PRINCIPALE.label"])!}</th>
   <th>${(params["facet.IMPORTO.label"])!}</th>
   <th>${(params["facet.CF_MINORE.label"])!}</th>
   <th>${(params["facet.ANNO_REFEZIONE.label"])!}</th>
  </tr>
 </thead>
 <tbody>
 <#list data as item >
  <tr>
   <td><strong><a target='_blank' href='/bpm/instances/details?id=${(item.ID_ISTANZA)!}'>${(item.ID_ISTANZA)!}</a></strong></td>
   <td>${(item.NUMERO_PROTOCOLLO)!}</td>
   <td>${(item.DATA_PROTOCOLLO_FORMATTATA)!}</td>
   <td><strong><a target='_blank' href='/documenti/viewProfile?DOCNUM=${(item.DOCNUM_PRINCIPALE)!}'>${(item.DOCNUM_PRINCIPALE)!}</a></strong></td>
   <td>${(item.IMPORTO)!}${(item.IMPORTO_OLD)!}</td>
   <td>${(item.CF_MINORE)!}${(item.CF_MINORE_OLD)!}</td>
   <td>${(item.ANNO_REFEZIONE)!}${(item.ANNO_REFEZIONE_OLD)!}</td>
  </tr>
 </#list>
 </tbody>
</table>
