<link rel="stylesheet" href="${theme.common}/static/header.css?no-cache" >
<link rel="stylesheet" href="/static/css/flag-icon.min.css" >

<nav id="top-nav" class="p-0 d-print-none navbar navbar-expand-md navbar-dark bg-dark">

	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#topnavCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>

	<div class="collapse navbar-collapse" id="topnavCollapse" >
		<ul class="navbar-nav mr-auto">
			<#list header.apps as tab>
				<#if (utils.hasGroup(tab.roles![]) && !(tab.visible!true)==false ) >
					<li class="nav-item ${ (tab.appName==($.currentApp.appName!''))?string('active','') }">
						<a style="${tab.style!''}" class="text-nowrap nav-link ${tab.class!''}" name="${tab.appName}" href="${tab.link}">
							<i class="${(tab.icon!'')?starts_with('glyphicon')?then('glyphicon','fas')} ${tab.icon!'fa-window-maximize'}"></i>
							&nbsp;${tab.title!tab.appName}
						</a>
					</li>
				</#if>
			</#list>
		</ul>
	</div>

	<form class="form-inline">

		<#if !$.userInfo.guest >

			<a href="/auth/userinfo" target="@modal" class="nav-link" title="${utils.userInfo.username}">
				<i class="fas fa-user"></i>${utils.userInfo.fullname}
			</a>

			<#assign aoos = utils.userInfo.allAoos />
			<#if (aoos?size > 1) >
				<div class="dropdown d-inline"  >
					<a href="#" class="nav-link dropdown-toggle" role="button" data-toggle="dropdown" title="${utils.userInfo.aoo.cod}">
						<span ><i class="fas fa-building"></i>
							<span style="max-width: 60px;vertical-align: bottom;" class="text-truncate d-inline-block">${utils.userInfo.aoo.desc}</span>
						</span>
					</a>
					<div class="dropdown-menu" style="left:unset;right:0px;max-width: 200px">
						<#list aoos as cod >
							<#if cod != utils.userInfo.aoo.cod >
								<a style="color: #0a0833!important;" class="dropdown-item btn btn-link" href="/auth/executeLogin?aoo=${cod}&switchAoo=true">${$.clientCache.getAOO(cod).name}</a>
							</#if>
							<#if cod == utils.userInfo.aoo.cod >
								<a style="color: #0a0833!important;" class="disabled dropdown-item btn btn-link" href="/auth/executeLogin?aoo=${cod}&switchAoo=true">${$.clientCache.getAOO(cod).name}</a>
							</#if>
						</#list>
					</div>
				</div>
			</#if>

			<#assign exit = (header['public-home']!'/auth/login?username='+utils.userInfo.username+'&aoo='+utils.userInfo.aoo.cod)?url('utf-8') />

			<#if exit?has_content>
				<a title="Esegui logout" class="nav-link" href="/auth/logout?requestURL=${exit}">
					<i class="fas fa-sign-out-alt"></i>
				</a>
			</#if>

		<#else>
			<a title="Esegui login" class="nav-link" href="/auth/login">
				<i class="fas fa-sign-in-alt"></i>Login
			</a>
			<a title="Esegui registrazione" class="nav-link" href="/auth/register">
				<i class="fas fa-pencil-alt"></i>Registrazione
			</a>
		</#if>

		<#assign path = requestURL?split(";")[0] />

		<div id="flag-menu" class="dropdown d-inline pr-3" >

			<a href="#" role="button" data-toggle="dropdown" class="nav-link dropdown-toggle" >
				<i title="${lang}" class="m-0 p-0 flag-icon flag-icon-${lang} flag-icon-${header.locales[lang]?split('-')[0]} flag-icon-${header.locales[lang]?split('-')[1]?lower_case}">
					<!--<span class="flag-text" ></span>-->
				</i>
			</a>

			<div class="dropdown-menu" >
				<#list (header.locales!{})?keys as key>
					<a href="${path};lang=${key}" class="dropdown-item flag-link p-0 m-0 ${((lang!'')==key)?then('d-none','')}" >
						<i class="mx-1 flag-icon flag-icon-${key} flag-icon-${header.locales[key]?split('-')[0]} flag-icon-${header.locales[key]?split('-')[1]?lower_case}"></i>${key}
					</a>
				</#list>
			</div>
		</div>

	</form>
</nav>

<nav id="header-nav" class="d-print-none navbar navbar-expand-md navbar-dark bg-dark">
	<a href="/" class="navbar-brand" >
		<img src="${logo}"></img>
	</a>

	<#if @menu=='horizontal' >
		<div class="collapse navbar-collapse" style="min-width: 70%;max-width: 70%;" id="navbarCollapse" >
			<ul class="navbar-nav mr-auto hmenu" >
				<#assign sections = (menu?is_hash?then(menu.sections,menu))![] />
				<#list (sections)![] as item>
				<#--<#if springMacroRequestContext.requestUri?contains("/index")>
                    <li class="nav-item active">
                        <a class="nav-link active" href="${item.url}">
                            <span>${item.title} </span><span class="sr-only">current</span>
                        </a>
                    </li>-->

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

					<#assign children = map.menuItems![] />

					<#if children?size==0>

						<#assign url = (item.url!'#')?remove_ending(".html")?remove_ending(".ftl") />

						<li class="nav-item hitem">
							<a style="${item.style!''}" class="nav-link text-truncate hitem ${item.class!''}" href="${url!'#'}">
								<span class="hitem">${name!'section'} </span>
							</a>
						</li>
					<#else>
						<li class="nav-item hitem dropdown">
							<a style="${item.style!''}" class="nav-link dropdown-toggle hitem ${item.class!''}" href="#" data-toggle="dropdown" aria-expanded="false">
								<span class="hitem">${name!'section'}</span>
							</a>
							<div class="dropdown-menu">

								<#list children as child>
									<#if utils.hasGroup(child.roles![])>
										<#assign name = child.title!child.name!child.url />

										<span class="dropdown-item nowrap">
									<span class="row">
									<a style="${child.style!''}" class="pl-1 col-10 ${child.class!''} text-truncate" href="${child.url}"><span>${name}</span>
									</a>

									<#if child.buttons?? >
										<div style="" class="px-0 col-2 menu-ops text-secondary pull-right">
											<#list child.buttons as button >
												<a style="${button.style!''}" href="${button.url}" class="btn-link ${ utils.hasGroup(button.roles![])?string('','disabled') } ${button.class!''}" title="${(button.title!button.title.name)!''}" >
													<i style="font-size:11px" class="${(button.icon!'')?starts_with('glyphicon')?then('glyphicon','fas')} ${button.icon!'fa-circle'}"></i>
												</a>
											</#list>
										</div>
									</#if>
									</span>
								</span>


									</#if>
								</#list>

							</div>
						</li>
					</#if>
				</#list>
			</ul>
		</div>
	<#else >
		<div class="collapse navbar-collapse" style="min-width: 30%" id="navbarCollapse" ></div>
	</#if>

	<form class="form-inline m-0 p-0" style="width: 100%" action="/documenti/listaRisultati" method="GET" >
		<div class="form-group row m-0 p-0 col-12" >
			<input type="hidden" name="database" value="false" />
			<input style="max-width: 400px" class="form-control-m-0 col-10" name="fq" placeholder="testo di ricerca" aria-label="Search">
			<span class="btn btn-link p-0 m-0 col-2" style="text-align: left">
			<button class="btn btn-link px-2 m-0"  type="submit"><i class="text-white fas fa-search"></i></button>
		</span>
		</div>
	</form>

	<#if @menu=='horizontal' >
		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
	</#if>

</nav>



<!--<script src="/static/js/menu.js?no-cache"></script>-->