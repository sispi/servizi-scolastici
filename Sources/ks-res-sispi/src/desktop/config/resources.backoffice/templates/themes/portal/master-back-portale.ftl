<!DOCTYPE HTML>
<!-- config home: ${utils.kdmUtils.getProperty("KEYSUITE_CONFIG","")} -->
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
	<link rel="stylesheet" href="/static/css/glyphicon.css"  >
	<link rel="stylesheet" href="/static/css/roboto.css" >
	<link rel="stylesheet" href="/static/vendor/fontawesome/all.min.css" >
	<link rel="stylesheet" href="/static/vendor/bootstrap-icons/bootstrap-icons.css">
	<link rel="stylesheet" href="/static/vendor/select2.min.css">
	<!--<link rel="stylesheet" href="/static/portal/css/bootstrap-italia.min.css">-->
	<link rel="stylesheet" href="/static/vendor/bootstrap.min.css">

	<script src="/static/vendor/jquery-3.5.1.min.js"></script>
	<script src="/static/vendor/jquery-migrate-3.3.0.min.js"></script>
	<script src="/static/vendor/moment.js"></script>
	<script src="/static/vendor/popper-1.14.7.min.js"></script>

	<!--<script src="/static/portal/js/bootstrap-italia.min.js"></script>-->
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
	<script src="/static/js/bootstrap-toc.js"></script>
	<script src="/static/js/docer.js?no-cache"></script>
	<script src="/static/js/bpm.js?no-cache" ></script>
	<script src="/static/js/desktop.js?no-cache"></script>
	<script src="/static/js/components/file.js?no-cache" ></script>

	<script src="https://cdn.ckeditor.com/ckeditor5/28.0.0/classic/ckeditor.js"></script>
	<script src="https://www.gstatic.com/charts/loader.js"></script>
	<!-- CSS Backend -->
	<link rel="stylesheet" href="/static/css/back-style.css">

	<style>
		body, html {
			margin: 0;
			padding: 0;
		}
		.my-body-settings {
			margin: 8px;
		}
		.icon-settings-custom {
			font-size: 20px;
			color: #555;
		}
	</style>

	${head!""}

</head>

<body>


<style>
	.secureblock.<#list utils.getUserInfo().getUserTokens() as token>${token}<#sep>,.secureblock.</#list>
	{
		display: unset !important;
		pointer-events: unset !important;
		opacity: unset !important;
	}
	#body{
		margin-bottom:60px;
	}
</style>


<div id="body">
	<#include "header-back-portale.ftl" />
	<div id="main">

		${body!""}
	</div>

	<#include theme.common + "/inspector.ftl" />

	<#include "footer-back-portale.ftl" />
</div>

</body>
</html>
<#-- model keys:
<#list .data_model?keys as key>
${key}
</#list> -->