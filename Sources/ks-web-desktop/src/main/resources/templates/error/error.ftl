<#assign guid = .now?long?c />
<div class="p-3" >
<#if ((level!'error') == 'error')>
<h3>${errortitle!$.request.getParameter("message")!'Errore nella richiesta'}</h3>
    <#assign level = 'danger' />
</#if>
<p class="row alert alert-${level!'danger'}" >
    <span class="col-10">${(exception.message)!''}</span>
    <#if (utils.userInfo.admin) >
    <span class="col-2">
    <a href="#" style="float: right" class="p-0 btn btn-link collapsed" data-toggle="collapse" data-target="#detail${guid}">Mostra dettagli</a>
    </span>
    <#if ((exception.code.retryable)!false && exception.url??) >
    <p>
        Puoi riprovare ad eseguire la richiesta a questo link:
        <a href="${exception.url}" class="" >Riprova</a>
    </p>
    </#if>
    </#if>
</p>

<#if utils.userInfo.admin >
<div id="detail${guid}" class="collapse" >

<#if exception.code?? >
<span><b>Codice:</b>&nbsp;${exception.code}</span>
<span><b>Tipo:</b>&nbsp;${(exception.code.type)!''}</span>
<span><b>Codice HTTP:</b>&nbsp;${(exception.code.httpStatus)!''}</span>
<span><b>Url richiesto:</b>&nbsp;${exception.url!''}</span>
</#if>
<#if ((exception.details!{})?size>0) >
<br/><b>Dettagli eccezione:</b>
<#list exception.details?keys as key>
<div>
<i>${key}:</i>
<pre style="background-color:lightyellow" class="p-2"  >
<#if (exception.details[key]?is_string)>
${exception.details[key]}
<#else>
${utils.ToJson(exception.details[key],true)}
</#if>
</pre>
</div>
</#list>
</#if>


<#if exception.causeStack?? >
    <div>
        <a href="#" class="p-0 btn btn-link collapsed" data-toggle="collapse" data-target="#causeStack${guid}">Mostra stack trace della causa</a>
        <div id="causeStack${guid}" class="collapse" >
<pre style="background-color:lightgray" class="p-2" >
${exception.causeStack}</pre>
        </div>
    </div>
</#if>

<#if exception.stack?? >
<div>
<a href="#" class="p-0 btn btn-link collapsed" data-toggle="collapse" data-target="#stack${guid}">Mostra stack trace completo</a>
<div id="stack${guid}" class="collapse" >
<pre style="background-color:lightgray" class="p-2" >
${exception.stack}</pre>
</div>
</div>
</#if>

</div>
</#if>
</div>




