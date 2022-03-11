<link rel="stylesheet" href="/static/vendor/fontawesome/all.min.css" >
<script src="/static/vendor/fontawesome/all.min.js"></script>

<style>
	<#assign logo = theme['logo']!'/static/images/logo.png' />
	:root {
		--topnav-height: ${theme['topnav-height']!'35px'};
		--topnav-bg-color: ${theme['topnav-bg-color']!'#343a40'};
		--header-height: ${theme['header-height']!'70px'};
		--subheader-height: ${theme['subheader-height']!'35px'};
		--footer-height: ${theme['footer-height']!'35px'};
		--menu-width: ${theme['menu-width']!'200px'};
		--header-bg-color: ${theme['header-bg-color']!'royalblue'};
		--header-text-color: ${theme['header-text-color']!'white'};
		--footer-bg-color: ${theme['header-bg-color']!'royalblue'};
		--footer-text-color: ${theme['header-text-color']!'white'};
	}

	#k-topnav{

		position: fixed;
		height: var(--topnav-height);
		line-height: var(--topnav-height);

		max-width: 100%;
		margin-left: 0px;
		margin-right: auto;

		text-align: left;

		background-color: var(--topnav-bg-color) !important;
		left: 0;
		right: 0;
		top: 0;
		z-index:100000;

		background: url(${logo}) var(--header-bg-color) no-repeat;
		background-size: 80px;
		background-position: right;
	}

	#k-topnav .item {
		color: rgba(255,255,255,.5) !important;
		font-size: 1rem!important;
		font-family: -apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"!important;
		font-weight: 400!important;
		line-height: 1.5!important;
		padding-left: 0px 8px 0px 8px!important;
	}

	#k-topnav *:hover {
		background-color: unset!important;
		text-decoration: unset!important;
	}

	#k-topnav .item.active {
		color: rgba(255,255,255,1) !important;;
	}

	#k-topnav img {
		height: calc(var(--topnav-height) - 0.5rem);
	}

	body{
		padding-top:  calc( var(--topnav-height) ) ;
	}

	/*#k-topnav{
		top:calc( var(--topnav-height)*(-1) ) !important;
	}*/

</style>
<div id="k-topnav">
	<#list header.apps as tab>
		<#if (utils.hasGroup(tab.roles![]) && !(tab.visible!true)==false ) >
		<a style="${tab.style!''}" class="item ${ (tab.appName==($.currentApp.appName!''))?string('active','') } ${tab.class!''}" name="${tab.appName}" href="${tab.link}">
			<i class="${(tab.icon!'')?starts_with('glyphicon')?then('glyphicon','fas')} ${tab.icon!'fa-window-maximize'}"></i>
			&nbsp;${tab.title!tab.appName}
		</a>
		</#if>
	</#list>
</div>