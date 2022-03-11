<#setting url_escaping_charset='utf-8'>

<span class="row facets container-fluid d-print-none px-0">

<#list facets as facet>

	<#assign vals = lreq[facet]![] />
	<#assign prefix = "facet."+facet+"."+(facet==businessState)?string(processName!''+'.','') />

	<div class="row col-12 inline-name" >$[facet.${facet}.label:${facet?lower_case?capitalize?replace('_',' ')}]</div>
	<div class="inline facet px-0" role="group" >

	<#list counts[facet] as key,value >


		<#assign message = (params[prefix+key])!(utils.getDisplayName(key)) />

		<#if ( vals?seq_contains(key) ) >
			<a title='${message}' class="value current cleanurl btn btn-primary" href="${baseUrl}?${utils.remove(querystringParams,facet,key)}" >
			<span class="ellipsis option">${message}</span>
			<span class="count">${ (value!0)?c}</span>
		</a>
		<#else>
			<a title='${message}' class="value ${ (key == '...')?string('disabled','') } cleanurl btn btn-primary" href="${baseUrl}?${facet}=${key}${querystringParams}" >
			<span class="ellipsis option">${message}</span>
			<span class="count">${ (value!0)?c}</span>
		</a>
		</#if>
	</#list>
	</div>
</#list>

<#if (combos?size>0 || reset) >

	<div class="combo-container container-fluid px-0" >

	<#list combos as facet,multivalue >

		<#assign vals = lreq[facet]![] />

		<#assign prefix = "facet."+facet+"."+(facet==businessState)?string(processName!''+'.','') />

		<div class="combo facet dropdown">
			<#if (vals?size>0) >

				<#assign message = (params[prefix+vals?first])!(utils.getDisplayName(vals?first)) />



				<#if (vals?size>1 && moreItems) >
				<button class="selected btn btn-link dropdown-toggle" type="button" data-toggle="dropdown" >

				<a title="${message}" class="first current cleanurl btn btn-link" href="${baseUrl}?${ moreItems?string( utils.remove(querystringParams,facet), utils.remove(querystringParams,facet,vals?first?replace(' ','+')) ) }" >
					<span class="ellipsis option" >${message}</span>
						<#if (!moreItems && vals?size>1) >
							<span class="info" title="${vals[1..]?join(', ')}" >(+${vals?size-1})</span>
						</#if>
					<i title="Rimuovi filtro" class="remove glyphicon glyphicon-remove"></i>
				</a>

					<#--if (multivalue) >
						<span class="glyphicon triangle glyphicon-triangle-bottom"></span>
					</#if-->
				</button>
				<div class="dropdown-menu">
				<#list 1..(vals?size-1) as idx >

					<#assign message = (params[prefix+vals[idx]])!(utils.getDisplayName(vals[idx])) />

					<a title="${message}" class="dropdown-item next current cleanurl btn btn-link" href="${baseUrl}?${utils.remove(querystringParams,facet,vals[idx])}" >
						<span class="ellipsis option" >${message}</span>
						<i title="Rimuovi filtro" class="remove glyphicon glyphicon-remove"></i>
					</a>
				</#list>
				</div>
				<#else>
					<a title="${message}" class="first current cleanurl btn btn-link" href="${baseUrl}?${ moreItems?string( utils.remove(querystringParams,facet), utils.remove(querystringParams,facet,vals?first?replace(' ','+')) ) }" >
					<span class="ellipsis option" >${message}</span>
						<#if (!moreItems && vals?size>1) >
							<span class="info" title="${vals[1..]?join(', ')}" >(+${vals?size-1})</span>
						</#if>
					<i title="Rimuovi filtro" class="remove glyphicon glyphicon-remove"></i>
				</a>
				</#if>
			<#else>

				<#assign message = (params[prefix+"label"])!(facet?lower_case?capitalize?replace('_',' ')) />
				<button title="${message}" class="unselected btn btn-link dropdown-toggle" type="button" data-toggle="dropdown"  >
			<span class="ellipsis name" >${message}</span>
			<#--span class="glyphicon triangle glyphicon-triangle-bottom"></span-->
		</button>
			</#if>

			<#if ( multivalue || vals?size==0 ) >

				<ul class="dropdown-menu" >

			<#list counts[facet] as key,value >

				<#assign message = (params[prefix+key])!(utils.getDisplayName(key)) />

				<li class="dropdown-item" title="${message}" >

				<#if ( vals?seq_contains(key) ) >
					<a class="value selected cleanurl btn btn-link" href="${baseUrl}?${utils.remove(querystringParams,facet,key)}" >
					<span class="ellipsis option">${message}</span>
					<span class="count">
						${ (value!0)?c }
						<i title="Rimuovi filtro" class="remove glyphicon glyphicon-remove"></i>
					</span>
				</a>
				<#else>
					<a class="value unselected ${ (key=='...')?string('disabled','') } cleanurl btn btn-link" href="${baseUrl}?${facet}=${key?url}${querystringParams}" >
					<span class="ellipsis option">${message}</span>
					<span class="count">${ (value!0)?c }</span>
				</a>
				</#if>
			</li>
			</#list>
		</ul>

			</#if>
	</div>

	</#list>

			<#if reset >
				<div class="remove-all">
		<#if (combos?size>0) ><i class="glyphicon glyphicon-option-vertical"></i></#if>
		<a class="cleanurl btn-link" href="${baseUrl}?qt=${qt}">Rimuovi filtri</a>
	</div>
			</#if>

</div>
</#if>

</span>

