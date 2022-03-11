<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <a class="navbar-brand" href="/backOffice/home" alt="back-office"></a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>
  
  
  <#assign path = $.request.servletPath />
  <#assign baseUrl = context + "/" />
  
  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
	
	<#list (menu)![] as item>


		<#assign idx = item?index />
		<#assign map = item?is_hash?then(item, {'url':item}) />

		<#if !(utils.hasGroup(map.roles![]))>
			<#continue />
		</#if>

		<#assign url = (map.url!'#')?remove_ending(".html")?remove_ending(".ftl") />
		<#assign page = (url?starts_with('/')?then('',baseUrl)) + (url?split("?")[0]) />
		
		<#assign name = map.name!(url?split("?")[0]) />
		<#assign children = map.menuItems![] />
		
		<#if children?size==0>
			<li class="nav-item ${(path==page)?then('active','')}">
				<a class="nav-link" href="${url}">${name!'section'}<span class="sr-only">${name!'section'}</span></a>
			</li>
		<#else>
		
			<li class="nav-item dropdown">
				<a class="nav-link dropdown-toggle" href="${url}" id="navbarDropdown-${idx}" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				  ${name!'section'}
				</a>
				<div class="dropdown-menu" aria-labelledby="navbarDropdown-${idx}">
				  <#list children as child>
					<#if child.sep!false >
						<div class="dropdown-divider"></div>
					<#elseif utils.hasGroup(child.roles![])>
						<#assign cpage = (child.url?starts_with('/')?then('',baseUrl)) + (child.url?split("?")[0]) />
						<a class="dropdown-item ${(path==cpage)?then('active','')}" href="${child.url}">${child.title}</a>
					</#if>
					
				  </#list>
				</div>
			  </li>
			 
		</#if>
	</#list>
    </ul>
    <#if utils.userInfo.authenticated >
      <div class="form-inline my-2 my-lg-0">
          <span style="color: #ffffffd9" class="glyphicon glyphicon-user"></span> <span style="color: #ffffffd9;margin: 0px 15px 0px 5px;">${utils.userInfo.fullname} (${utils.userInfo.claims.PREFERRED_USERNAME!utils.userInfo.username})</span>
          <!--
        <a href="userinfo" target="@modal" class="nav-link" title="${utils.userInfo.username}">
          <span class="glyphicon glyphicon-user"></span> ${utils.userInfo.fullname}
        </a>
          -->
        <#assign logoutUrl = utils.getProperty('auth.logoutUrl','/auth/logout') />
        <#if logoutUrl?has_content >
          <a style="border-color: #ffffff69;color: #ffffff8c;" class="btn btn-outline-danger my-2 my-sm-0" href="${logoutUrl}"><span class="glyphicon glyphicon-log-out"></span> Esci</a>
        </#if>
      </div>
    </#if>
  </div>
</nav>
<div class="mt-3"></div>
