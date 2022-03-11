<!DOCTYPE HTML>
<html lang="${lang!''}" >

<head>

	<style>
		:root {
			--logo: ${theme['logo']!'url(/static/images/logo.png)'};
			--header-height: ${theme['header-height']!'70px'};
			--subheader-height: ${theme['subheader-height']!'35px'};
			--footer-height: ${theme['footer-height']!'35px'};
			--menu-width: ${theme['menu-width']!'200px'};
			--header-bg-color: ${theme['header-bg-color']!'royalblue'};
			--header-text-color: ${theme['header-text-color']!'white'};
			--footer-bg-color: ${theme['header-bg-color']!'royalblue'};
			--footer-text-color: ${theme['header-text-color']!'white'};
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
		var dateTimeFormat = '${$.env.getProperty("dateTimeFormat", "dd/MM/YYYY HH:mm")}';
		var dateFormat = '${$.env.getProperty("dateFormat", "dd/MM/YYYY")}';
	</script>

	<!------------ vendor ------------>

	<link rel="stylesheet" href="/static/vendor/bootstrap.min.css"  >
	<link rel="stylesheet" href="/static/css/glyphicon.css"  >
	<link rel="stylesheet" href="/static/css/roboto.css" >
	<link rel="stylesheet" href="/static/vendor/fontawesome/all.min.css" >
	<link rel="stylesheet" href="/static/vendor/select2.min.css">

	<script src="/static/vendor/jquery-3.5.1.min.js"></script>
	<script src="/static/vendor/jquery-migrate-3.3.0.min.js"></script>
	<script src="/static/vendor/moment.js"></script>
	<script src="/static/vendor/popper-1.14.7.min.js"></script>
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

	<script src="/static/js/common.js?no-cache"></script>
	<script src="/static/js/components/editor.js?no-cache"></script>
	<script src="/static/js/components/select2.js?no-cache" ></script>
	<script src="/static/js/components/reports.js?no-cache" ></script>
	<script src="/static/js/components/simplemodal.js?no-cache" ></script>
	<script src="/static/js/components/tree-list.js?no-cache" ></script>
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

		<#if ( ($.request.getParameter('wt')!'') == 'print') >
		.print-hide  {
			display: none;
		}
		</#if>
	</style>

</head>

<body <#list (bodyAttributes!{})?keys as att>@${att}="${bodyAttributes[att]}"</#list> >

	<div id="body" class="margin-menu" >
		<div id="main">

		${body!""}

		<#attempt>
			<#include theme.common + "/inspector.ftl" />
			<#recover>
				${ utils.ftl('error/error.ftl', {"errortitle":"inspector.ftl", "exception": {"message":.error}} ) }
		</#attempt>

		</div>
	</div>

	<div class="d-print-none">
		<#attempt>
			<#include theme.default + "/header.ftl" />
			<#recover>
				${ utils.ftl('error/error.ftl', {"errortitle":"header.ftl", "exception": {"message":.error}} ) }
		</#attempt>
	</div>

	<script>
	<#if $.env.getProperty("badges.url")?? >
	$(function() {
	 	updateBadges("${utils.kdmUtils.getProperty("badges.url")}",${utils.kdmUtils.getProperty("badges.delay","5")},${utils.kdmUtils.getProperty("badges.maxDelay","0")});
	});
	</#if>
	$(function() {
		window.addEventListener("hashchange", function () {
			window.scrollTo(window.scrollX, window.scrollY - 86);
		});
	});
	</script>

	<nav style="z-index: 10000;height:var(--footer-height);background-color: var(--footer-bg-color)" class="nav-footer fixed-bottom navbar navbar-light navbar-expand-md">
		<span class="w-100 navbar-brand text-center" >
			<div style="height: var(--footer-height);" class="k-logo">
				<small style="color: var(--footer-text-color)" class="float-right navbar-text" >${theme['footer-text']} - &copy; 2020</small>
			</div>
		</span>
	</nav>

</body>
</html>
<#-- model keys:
<#list .data_model?keys as key>
${key}
</#list> -->