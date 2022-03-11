<!DOCTYPE HTML>
<html lang="${lang!''}" >

<head>

	<#assign logo = theme['logo']!'/static/images/logo.png' />
	<style>
		:root {
			--topnav-bg-color: ${theme['topnav-bg-color']!'#343a40'};
			--topnav-height: ${theme['topnav-height']!'35px'};
			--header-height: ${theme['header-height']!'70px'};
			--subheader-height: ${theme['subheader-height']!'35px'};
			--footer-height: ${theme['footer-height']!'35px'};
			--menu-width: ${theme['menu-width']!'200px'};
			--menu-bg-color: ${theme['menu-bg-color']!'#343a40'};
			--header-bg-color: ${theme['header-bg-color']!'royalblue'};
			--header-text-color: ${theme['header-text-color']!'white'};
			--footer-bg-color: ${theme['footer-bg-color']!'royalblue'};
			--footer-text-color: ${theme['footer-text-color']!'white'};
		}
	</style>

	<base href="${context}/" >

	<#if title?? >
	<title>${title!''}</title>
	</#if>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<link rel="shortcut icon" href="/static/images/favicon.ico">

	<script type="text/javascript">
		window.FontAwesomeConfig = { autoReplaceSvg: false }
		var dateTimeFormat = '${$.env.getProperty("dateTimeFormat", "")}' || "DD/MM/YYYY HH:mm:ss";
		var dateFormat = '${$.env.getProperty("dateFormat", "")}' || "DD/MM/YYYY";
		var userInfo = ${utils.toJson(utils.getUserInfo())};
		var desktopBaseUrl = location.protocol+"//"+location.host;

		function desktopUrl(url){
			return url
				.replace('${$.env.getProperty("zuul.routes.docer.url")}','${$.env.getProperty("zuul.routes.docer.path")?replace("/**","")}');
		}
		function serviceUrl(url){
			return url
				.replace('${$.env.getProperty("zuul.routes.docer.path")?replace("/**","")}','${$.env.getProperty("zuul.routes.docer.url")}');
		}
	</script>

	<!------------ vendor ------------>

	<link rel="stylesheet" href="/static/vendor/bootstrap.min.css"  >
	<link rel="stylesheet" href="/static/vendor/bootstrap-icons/bootstrap-icons.css"  >
	<link rel="stylesheet" href="/static/css/glyphicon.css"  >
	<link rel="stylesheet" href="/static/css/roboto.css" >
	<link rel="stylesheet" href="/static/vendor/fontawesome/all.min.css" >
	<link rel="stylesheet" href="/static/vendor/select2.min.css">

	<script src="/static/vendor/jquery-3.5.1.min.js"></script>
	<script src="/static/vendor/jquery-migrate-3.3.0.min.js"></script>
	<script src="/static/vendor/moment.js"></script>
	<script src="/static/vendor/popper.min.js"></script>
	<script src="/static/vendor/bootstrap.min.js"></script>
	<script src="/static/vendor/vue.min.js"></script>
	<script src="/static/vendor/axios.min.js"></script>
	<script src="/static/vendor/fontawesome/all.min.js"></script>
	<script src="/static/vendor/ace/ace.js" ></script>
	<script src="/static/vendor/vue-scrollto.js"></script>
	<script src="/static/vendor/select2.min.js"></script>

	<!-- keysuite -->

	<link rel="stylesheet" href="/static/css/base-style.css">
	<link rel="stylesheet" href="/static/css/modal-style.css">
	<link rel="stylesheet" href="/static/css/tree-list.css?no-cache">
	<link rel="stylesheet" href="/static/css/bootstrap-toc.css" />
	<link rel="stylesheet" href="/static/css/custom-bootstrap-style.css?no-cache">

	<script src="/static/js/common.js?no-cache"></script>
	<script src="/static/js/components/editor.js?no-cache"></script>
	<script src="/static/js/components/select2.js?no-cache" ></script>
	<script src="/static/js/components/reports.js?no-cache" ></script>
	<script src="/static/js/components/simplemodal.js?no-cache" ></script>
	<script src="/static/js/components/tree-list.js?no-cache" ></script>
	<script src="/static/js/components/file.js?no-cache" ></script>
	<script src="/static/js/bootstrap-toc.js"></script>
	<script src="/static/js/docer.js?no-cache"></script>
	<script src="/static/js/bpm.js?no-cache" ></script>
	<script src="/static/js/desktop.js?no-cache"></script>

	<link rel="stylesheet" href="${theme.folder}/static/master.css?no-cache">

	${head!""}

	<style>
		.secureblock.<#list utils.getUserInfo().getUserTokens() as token>${token}<#sep>,.secureblock.</#list>
		{
			display: unset !important;
			pointer-events: unset !important;
			opacity: unset !important;
		}

		<#if ( ($.request.getParameter('wt')!'') != 'print') >
		@media print {
		</#if>
			.d-print-none {
				display: none;
			}
		<#if ( ($.request.getParameter('wt')!'') != 'print') >
		}
		</#if>

	</style>

</head>

<#assign @menu = @menu!theme['menu']!'vertical' />

<#if (($.userInfo.options.MENU)!'')?has_content >
	<#assign @menu = $.userInfo.options.MENU />
</#if>

<body <#list (bodyAttributes!{})?keys as att>@${att}="${bodyAttributes[att]}"</#list> >

	<#attempt>
		<#include "header.ftl" />
	<#recover>
		${ utils.ftl('error/error.ftl', {"errortitle":"header.ftl", "exception": {"message":.error}} ) }
	</#attempt>

	<div style="display: flex; flex-direction: row">

		<#if @menu=='vertical' >
		<div id="menu">
			<#include "menu.ftl" />
		</div>
		</#if>

		<div id="main" >
			<#attempt>
				<#include "subheader.ftl" />
				<#recover>
					${ utils.ftl('error/error.ftl', {"errortitle":"subheader.ftl", "exception": {"message":.error}} ) }
			</#attempt>

			<div id="body">
				${body!""}
			</div>
		</div>

	</div>

	<#attempt>
		<#include theme.common + "/inspector.ftl" />
		<#recover>
			${ utils.ftl('error/error.ftl', {"errortitle":"inspector.ftl", "exception": {"message":.error}} ) }
	</#attempt>

	<nav id="footer" class="d-print-none nav-footer navbar navbar-light navbar-expand-md">
		<span class="navbar-text text-white position-absolute" style="right: 10px" >
			Gruppo Filippetti - &copy; 2020
		</span>

		<a href="/" class="navbar-brand" >
			<img src="${logo}"></img>
		</a>


	</nav>

	<script>
	<#if (($.env.getProperty("badges.url")!'')?length>0) >
	$(function() {
	 	updateBadges("${utils.kdmUtils.getProperty("badges.url")}",${utils.kdmUtils.getProperty("badges.delay","5")},${utils.kdmUtils.getProperty("badges.maxDelay","0")});
	});
	</#if>
	/*$(function() {
		window.addEventListener("hashchange", function () {
			window.scrollTo(window.scrollX, window.scrollY - 86);
		});
	});*/


	</script>



</body>
</html>