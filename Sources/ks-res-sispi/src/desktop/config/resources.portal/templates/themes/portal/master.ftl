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
	<link rel="stylesheet" href="/static/vendor/select2.min.css">
	<link rel="stylesheet" href="/static/portal/css/bootstrap-italia.min.css">

	<script src="/static/vendor/jquery-3.5.1.min.js"></script>
	<script src="/static/vendor/jquery-migrate-3.3.0.min.js"></script>
	<script src="/static/vendor/moment.js"></script>
	<script src="/static/vendor/popper-1.14.7.min.js"></script>

	<script src="/static/portal/js/bootstrap-italia.min.js"></script>

	<script src="/static/vendor/vue.min.js"></script>
	<script src="/static/vendor/axios.min.js"></script>
	<script src="/static/vendor/fontawesome/all.min.js"></script>
	<script src="/static/vendor/ace/ace.js" ></script>
	<script src="/static/vendor/vue-scrollto.js"></script>
	<script src="/static/vendor/select2.min.js"></script>

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
	<!--<script src="/form/v1/vue/form.umd.min.js"></script>-->
	<script src="/static/js/components/renderer.js?no-cache"></script>
	<script src="https://cdn.ckeditor.com/ckeditor5/28.0.0/classic/ckeditor.js"></script>

	<!-- personalizzazione palermo -->
	<link rel="preconnect" href="https://fonts.googleapis.com">
	<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
	<link href="https://fonts.googleapis.com/css2?family=Raleway&display=swap" rel="stylesheet">
	<link rel="stylesheet" href="/static/css/palermo-style.css?no-cache">
	<!-- CSS form manager -->
	<link rel="stylesheet" href="/static/css/form-manager-style.css?no-cache">

	<!-- Google Font Roboto-->
	<link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,400;0,500;0,700;1,400;1,700&display=swap" rel="stylesheet">

	<style>
		body{
			font-family: 'Roboto', sans-serif;
		}
		body, html {
			margin: 0;
			padding: 0;
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

	.container-fluid, .container-lg, .container-md, .container-sm, .container-xl {
		width: 100%;
		padding-right: 0px;
		padding-left: 0px;
		margin-right: auto;
		margin-left: auto;
	}

	#body{
		margin:46px 0vw 4vw 0vw;
	}
</style>


<div id="body">
	<#include "header-portale.ftl" />
	<div id="main">

		${body!""}
	</div>

	<#include theme.common + "/inspector.ftl" />
	<div class="mt-5"></div>
	<#include "footer-portale.ftl" />

	<!-- test date Mac/Safari-->
	<!-- <input type="date" class="datechk"> -->

</div>

<script>
	$(function() {
		updateBadges("/model/reports/badges",5,0);
	});

	var response = {
		data: null,
		status: null,
		error: null
	};
	$.ajax({
		url: "/portale/v1/media/key/logo",
		async: false,
		type: "GET",
		success: function (data, status) {
			response = {
				data,
				status,
				error: null
			};

			var imgSrc = response.data.file;

			var $imgH = $("<img id='logoH'/>");
			$imgH.attr("src", "data:image/png;base64," + imgSrc);
			$("#logo-header").append($imgH);
			$("#logoH").css({"max-height": "70px", "margin-right": "15px"});

			var $imgF = $("<img id='logoF'/>");
			$imgF.attr("src", "data:image/png;base64," + imgSrc);
			$("#logo-footer").append($imgF);
			$("#logoF").css({"max-height": "38px", "margin-right": "10px"});

		},
		error: function (status, error) {
			response = {
				data: null,
				status,
				error
			}
		},
	});




	//fix datepicker Safari
	var dateClass='.datechk';
	$(document).ready(function ()
	{
		if(document.querySelector(dateClass)) {
			if (document.querySelector(dateClass).type !== 'date') {
				var oCSS = document.createElement('link');
				oCSS.type = 'text/css';
				oCSS.rel = 'stylesheet';
				oCSS.href = '//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/themes/base/jquery-ui.css';
				oCSS.onload = function () {
					var oJS = document.createElement('script');
					oJS.type = 'text/javascript';
					oJS.src = '//ajax.googleapis.com/ajax/libs/jqueryui/1.12.1/jquery-ui.min.js';
					oJS.onload = function () {
						$(dateClass).datepicker({dateFormat: 'dd-mm-yy'});
					}
					document.body.appendChild(oJS);
				}
				document.body.appendChild(oCSS);
			}
		}
	});


</script>


</body>
</html>
<#-- model keys:
<#list .data_model?keys as key>
${key}
</#list> -->