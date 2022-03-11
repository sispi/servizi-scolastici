<#assign report= webClient.getMap("/docer/v1/report?"+utils.getRequest().getQueryString() ) />
<#assign dummy=utils.getResponse().setContentType(report['content-type']) />
<#compress>
    <#list report.columns as item>${item}<#sep>,</#list>
    <#list report.data as record>
        <#if record?is_sequence >
            <#list record as value><#if (value!"")?is_sequence >${value?size}<#elseif (value!"")?is_number >${value?c!""}<#elseif (value!"")?is_boolean>${value?c!""}<#else>${value!""}</#if><#sep>,</#list>
        <#else>
            <#list record?values as value><#if (value!"")?is_sequence >${value?size}<#elseif (value!"")?is_number >${value?c!""}<#elseif (value!"")?is_boolean>${value?c!""}<#else>${value!""}</#if><#sep>,</#list>
        </#if>
    </#list>
</#compress>
