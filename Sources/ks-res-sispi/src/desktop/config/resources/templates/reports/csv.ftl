<#assign report= webClient.getMap("/docer/v1/report?"+utils.getRequest().getQueryString() ) />
<#assign dummy=utils.getResponse().setContentType(report['content-type']) />
<#compress>
<#if report.columns?? && report.columns?seq_contains("REPORTPAGAMENTIRETTASCOLASTICA")>
 ID_PAGAMENTO;CF_MINORE;IMPORTO;CF_CONTRIBUENTE;MODALITA;NOME_ISTITUTO;DESCRIZIONE;
 <#assign custom_list=[1,2,3,4,6,8,10]>
 <#list report.data as record>
  <#list record as value><#if custom_list?seq_contains(value?index)><#if value?index==3> ${value?replace(".", ",")} <#elseif value?index==6> <#if value=="2"> Bollettino cartaceo <#elseif value=="1"> Bollettino online <#elseif value=="0"> PagoPA </#if> <#elseif (value!"")?is_sequence >${value?size}<#elseif (value!"")?is_number >${value?c!""}<#elseif (value!"")?is_boolean>${value?c!""}<#else>${value!""}</#if><#sep>;</#if></#list>
 </#list>
<#elseif report.columns?? && report.columns?seq_contains("REPORTISCRIZIONI")>
 <#assign indiceColonna=0 /><#assign indiceColonnaDatiRichiesta=0 /><#list report.columns as item><#assign indiceColonna=indiceColonna+1 /><#if item!="DATI_RICHIESTA">${item}<#sep>;<#else><#assign indiceColonnaDatiRichiesta=indiceColonna /></#if></#list>
 <#list report.data as record>
  <#assign indiceColonna=0 />
  <#if record?is_sequence >
   <#list record as value><#assign indiceColonna=indiceColonna+1 /><#if indiceColonna!=indiceColonnaDatiRichiesta><#if (value!"")?is_sequence >${value?size}<#elseif (value!"")?is_number >${value?c!""}<#elseif (value!"")?is_boolean>${value?c!""}<#else>${value!""}</#if><#sep>;</#if></#list>
  <#else>
   <#list record?values as value><#if (value!"")?is_sequence >${value?size}<#elseif (value!"")?is_number >${value?c!""}<#elseif (value!"")?is_boolean>${value?c!""}<#else>${value!""}</#if><#sep>;</#list>
  </#if>
 </#list>
<#else>
 <#list report.columns as item>${item}<#sep>;</#list>
 <#list report.data as record>
  <#if record?is_sequence >
   <#list record as value><#if (value!"")?is_sequence >${value?size}<#elseif (value!"")?is_number >${value?c!""}<#elseif (value!"")?is_boolean>${value?c!""}<#else>${value!""}</#if><#sep>;</#list>
  <#else>
   <#list record?values as value><#if (value!"")?is_sequence >${value?size}<#elseif (value!"")?is_number >${value?c!""}<#elseif (value!"")?is_boolean>${value?c!""}<#else>${value!""}</#if><#sep>;</#list>
  </#if>
 </#list>
</#if>

</#compress>