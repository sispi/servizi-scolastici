<!-- ${utils.request.getHeader("Referer")!''} -->
<!-- ${utils.request.servletPath!''} -->

<#assign gtitles = ($.request.getParameter("titles")!'')=="true" />

<#if ($.request.servletPath == context) >
    <#assign parts = [context] />
<#elseif ($.request.servletPath?length>(context?length)) >
    <#assign parts = $.request.servletPath[context?length+1..]?split('/') />
<#else>
    <#assign parts = [] />
</#if>

<nav id="subheader" aria-label="breadcrumb" class="d-print-none" >

    <ol class="breadcrumb">
        <#list parts as part>
            <#if part?is_last >
                <#if (titles??) >
                    <#list titles?keys as href>
                        <#if (titles[href]?has_content && href?has_content) >
                            <li class="breadcrumb-item"><a target="#main" href="${href}">${titles[href]}</a></li>
                        </#if>
                    </#list>
                </#if>
                <#if (title!part)?has_content >
                    <li class="breadcrumb-item active page-title" aria-current="page">${title!part}
                    </li>
                </#if>
            <#else>
                <li class="breadcrumb-item"><a target="#main" href="${parts[0..part?index]?join('/')}${gtitles?then('?titles','')}">${part?remove_beginning('~')}</a></li>
            </#if>
        </#list>
    </ol>

    <span class="subheader-ops">
        <#if absolutePath??>
            <a title="Modifica la pagina" href="/~admin/resources?${absolutePath}">
                <i style="cursor: pointer;" class="glyphicon glyphicon-edit"></i>
            </a>
        </#if>

        <#if (!(utils.getRequest().queryString!'')?contains("inspector") && utils.hasGroup(['admins'])) >
            <a title="Visualizza inspector" href="${requestURL + requestURL?contains("?")?then('','?')+"&inspector#inspector" }">
                <i style="cursor: pointer;" class="glyphicon glyphicon-eye-open"></i>
            </a>
        </#if>

        <#if (@help??) >
            <a title="Help contestuale" target="@modal-xl" href="${@help}">
                <i style="cursor: pointer;" class="glyphicon glyphicon-education"></i>
            </a>
        </#if>
    </span>
</nav>
<#if (utils.userInfo.admin && warnings??) >
    <#list warnings as warning>
        ${ utils.ftl('error/error.ftl', { "exception": warning, "level":"warning"} ) }
    </#list>
</#if>

<#if (utils.userInfo.admin && infos??) >
    <#list infos as info>
        ${ utils.ftl('error/error.ftl', { "exception": warning, "level":"info"} ) }
    </#list>
</#if>

<script>

    if (typeof $ == "undefined")
        window.location.reload();

</script>

