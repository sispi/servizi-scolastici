<#if (inspector?? && inspector.timersInfo??)>
    <hr/>
    <a id="inspector" />
    <p class="alert alert-info" >
		<span class="col-12">
    		<a href="#" class="p-0 btn btn-link collapsed" data-toggle="collapse" data-target="#inspector">Mostra inspector</a>
    	</span>

    <div id="inspector" class="container-fluid collapse show" >
        <#list inspector.timersInfo?keys as mark >
            <#if (mark=='t0')>
                <#assign tot2 = .now?long-inspector.timersInfo[mark] />
                <span class="row">
						<span class="col-4"><b>ftl-master</b></span><span style="text-align: right;" class="col-2">${tot2-inspector.timersInfo['t1']}ms</span>
					</span>
                <span class="row">
						<span class="col-4"><b>total time</b></span><span style="text-align: right;" class="col-2">${tot2}ms</span>
					</span>
            <#elseif (mark!='t1')>
                <span class="row">
						<span title="${mark}" class="col-4 text-truncate"><b>${mark}</b></span><span style="text-align: right;" class="col-2">${inspector.timersInfo[mark]}ms</span>
					</span>
            </#if>
            </span>
        </#list>

        <#list (inspector.sources!{})?keys as mark >
            <#attempt>
                <#assign txt= utils.ToJson(utils.FromJson(inspector.sources[mark]),true) >
                <#recover>
                    <#assign txt= (inspector.sources[mark])!'' >
            </#attempt>
            <span class="row">
					<a href="#" class="btn btn-link" data-toggle="collapse" data-target="#inspector_source_${mark?index}">Mostra risposta</a><a target="_blank" class="btn btn-link label-info" href="${mark}"><i class="glyphicon glyphicon-link"></i>&nbsp;${mark}</a>
				</span>
            <pre id="inspector_source_${mark?index}" style="background-color:lightyellow" class="p-2 collapse"  >${txt}</pre>
        </#list>

        <#if inspector.rawBody?? >
        <span class="row">
            <a href="#" class="btn btn-link" data-toggle="collapse" data-target="#inspector_rawbody">Mostra raw body</a>
        </span>

        <pre id="inspector_rawbody" style="background-color:lightblue" class="p-2 collapse"  ><code>${inspector.rawBody?html }</code></pre>
        </#if>

        <#if inspector.outBody?? >
        <span class="row">
            <a href="#" class="btn btn-link" data-toggle="collapse" data-target="#inspector_outbody">Mostra out body</a>
        </span>

        <pre id="inspector_outbody" style="background-color:lightblue" class="p-2 collapse"  ><code>${inspector.outBody?html}</code></pre>
        </#if>

        <#if inspector.model?? >
        <span class="row">
            <a href="#" class="btn btn-link" data-toggle="collapse" data-target="#inspector_model">Mostra modello FTL</a>
        </span>
        <#attempt>
        <pre id="inspector_model" style="background-color:lightblue" class="p-2 collapse"  >${utils.ToJson(inspector.model,true)?html}</pre>
        <#recover>
        <pre id="inspector_model" style="background-color:lightblue" class="p-2 collapse"  >(serialization error)</pre>
        </#attempt>
        </#if>

    </div>
    </p>

</#if>