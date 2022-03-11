<#if menu??>

<#assign sections = (menu?is_hash?then(menu.sections,menu))![] />
<#list sections as item >

	<#assign idx = item?index />
	<#assign map = item?is_hash?then(item, {'url':item}) />

	<#if !(utils.hasGroup(map.roles![]))>
		<#continue />
	</#if>

	<#assign url = (map.url!'#')?remove_ending(".html")?remove_ending(".ftl") />

	<#if ( (map.url!'')?ends_with(".html") && sections?seq_contains(url))>
		<#continue />
	</#if>

	<#assign name = map.name!(url?split("?")[0]) />

	<div class="menu-group" >

		<#assign collapse = map.closed!false />

		<#if name??>
			<a	title="${map.title!name}"
				style="width:12vw;text-align:left;${map.style!''}"
				class="menu-title btn btn-link text-truncate ${map.class!''}"
				${collapse?then('data-toggle="collapse"','')}
				data-target="#section-${idx}"
				href="${url}" >
				<i class="${(map.icon!'')?starts_with('glyphicon')?then('glyphicon','fas')} ${map.icon!'fa-caret-right'}"></i>
				${name}
			</a>
		</#if>

		<div id="section-${idx}" class="${collapse?string('collapse','')}">

		<#list (map.menuItems![]) as mi >
		<#if utils.hasGroup(mi.roles![])>
			<div class="menu-item ellipsis ${mi.class!''}" style="padding-left:0.5vw;padding-right:0.5vw;display: block;">
				<a style="max-width: 9vw;${mi.style!''}" class="ellipsis btn-link text-dark ${mi.class!''}" href="${mi.url!'#'}" target="${mi.target!'_self'}" title="${(mi.title!mi.name)!mi.url}" >
					<i class="${(mi.icon!'')?starts_with('glyphicon')?then('glyphicon','fas')} ${mi.icon!'fa-link'}"></i>
					<#if mi.badge?? >
						<span style="display: none;" id="${mi.badge}" class="badge badge-pill badge-danger badge"></span>
					</#if>
					&nbsp;${mi.name!mi.url}
				</a>
				<#if mi.buttons?? >
				<div style="" class="menu-ops text-secondary">
					<#list mi.buttons as button >
					<a style="${button.style!''}" href="${button.url}" class="btn-link ${ utils.hasGroup(button.roles![])?string('','disabled') } ${button.class!''}" title="${(button.title!button.title.name)!''}" >
						<i style="font-size:11px" class="${(button.icon!'')?starts_with('glyphicon')?then('glyphicon','fas')} ${button.icon!'fa-circle'}"></i>
					</a>
					</#list>
				</div>
				</#if>
			</div>
		</#if>
		</#list>
		</div>
	</div>
</#list>

</#if>

