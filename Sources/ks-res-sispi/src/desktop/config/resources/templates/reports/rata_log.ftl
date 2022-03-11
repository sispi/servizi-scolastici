<script type="text/javascript" src="/static/palermo/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/palermo/js/datepicker-it.js"></script>
<script type="text/javascript" src="/static/palermo/js/common-functions.js?no-cache"></script>
<link type="text/css" rel="stylesheet" href="/static/palermo/css/jquery-ui.min.css" />
<link type="text/css" rel="stylesheet" href="/static/palermo/css/style.css?no-cache" />

<hr class='clearfix' />
<div class=''>
 <div class='row'>
  <table class='table table-striped table-sm'>
   <thead>
    <tr>
     <th>${(params["facet.UTENTE_MODIFICA.label"])!}</th>
     <th>${(params["facet.DATA_MODIFICA.label"])!}</th>
     <th>${(params["facet.CAUSALE.label"])!}</th>
     <th>${(params["facet.ID_RATA.label"])!}</th>
     <th>${(params["facet.TIPO_MODIFICA.label"])!}</th>
     <th>${(params["facet.VALORE_RATA.label"])!}</th>
    </tr>
   </thead>
   <tbody>
   <#list data as item >
    <tr>
    <td>${(item.UTENTE_MODIFICA)!}</td>
    <td>${(item.DATA_MODIFICA_STRING)!}</td>
    <td>${(item.CAUSALE)!}</td>
    <td>${(item.ID_RATA)!}</td>
    <td>${(item.TIPO_MODIFICA)!}</td>
    <td>${(item.VALORE_RATA)!}</td>
    </tr>
   </#list>
   </tbody>
  </table>
 </div>
 <div class='row'>
  <div class='form-group col-md-12'>
   <div id='validazione-div' role='alert'></div>
  </div>
 </div>
</div>

<script>

$( document ).ready(function() {

});

</script>