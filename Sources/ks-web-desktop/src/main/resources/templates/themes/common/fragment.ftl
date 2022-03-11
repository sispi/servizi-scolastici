<#if xFragment?contains("subheader")>
<#attempt>
<#include theme.folder + "/subheader.ftl" />
<#recover>
</#attempt>
</#if>
<title>${title!''}</title>
${body!""}