<link rel="stylesheet" href="/static/css/footer.css?no-cache" >
<nav id="k-nav-footer" style="z-index: 100000000000000000" class="k-fixed-bottom k-navbar k-navbar-expand-md k-navbar-dark ">
	<div class="k-logo"></div>
	<div id="k-brand" >
		<span class="k-text-white">Gruppo Filippetti - &copy; 2020</span>
		<#attempt>
			<#assign version = utils.kdmUtils.getBean('buildProperties').version />
		<#recover>
			<#assign version = "" />
		</#attempt>
		&nbsp;
		<span class="k-text-info"><a href="${(utils.hasGroup(['admins']))?string('info','#')}">${version}</a></span>
	</div>
</nav>