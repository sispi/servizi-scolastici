<#-- macro to convert freemarker object to javascript inline/json structure -->
<#macro js object="" inline=true>
    <@compress single_line=true>
        <#if object?is_hash || object?is_hash_ex>
            <#assign first = "true">
            {<#t>
            <#list object?keys as key>
                <#if first == "false">, </#if><#t>
                <#assign value><@js object=object[key] inline=inline/></#assign>
                <#if inline>${key}<#else>"${key}"</#if>: ${value?trim}<#t>
                <#assign first="false">
            </#list>
            }<#t>
        <#elseif object?is_enumerable>
            <#assign first="true">
            [<#t>
            <#list object as item>
                <#if first="false">, </#if><#t>
                <#assign value><@js object=item  inline=inline/></#assign>
                ${value?trim}<#t>
                <#assign first="false">
            </#list>
            ]<#t>
        <#elseif object?is_number>
            ${object?c}<#t>
        <#elseif object?is_boolean>
            ${object?string('true', 'false')}<#t>
        <#elseif object?is_date_like>
            "${object?datetime_if_unknown?iso_utc}"<#t>
        <#elseif object?has_content>
            <#if inline>"${object?js_string}"<#else>"${object?json_string}"</#if><#t>
        <#elseif object?is_string>
            ""<#t>
        <#else>
            null<#t>
        </#if>
    </@compress>
</#macro>
