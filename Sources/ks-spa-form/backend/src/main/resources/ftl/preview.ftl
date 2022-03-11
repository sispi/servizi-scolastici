<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Form preview</title>
        <#list assets as asset>
            <#if asset?ends_with(".css")>
        <link rel="stylesheet" href="${assetsBaseUrl}${asset}<#if !asset?starts_with("vendor/")>?no-cache</#if>">
            <#else>
        <script src="${assetsBaseUrl}${asset}<#if !asset?starts_with("vendor/")>?no-cache</#if>"></script>
            </#if>
        </#list>
    </head>
    <body>
        <#include template>
        <script>
            window.onKsFormAction = function (action, model, identifier) {
                console.log("Action", action, model, identifier)
            }
        </script
    </body>
</html>

