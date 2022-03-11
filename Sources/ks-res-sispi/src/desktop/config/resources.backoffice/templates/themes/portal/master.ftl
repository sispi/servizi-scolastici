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

	<script src="https://cdn.ckeditor.com/ckeditor5/28.0.0/classic/ckeditor.js"></script>

	<style>
		body{
			font-family: 'Roboto', sans-serif;
		}
		body, html {
			margin: 0;
			padding: 0;
		}
		.my-body-settings {
			margin: 8px;
		}
		.bg-light{
			border-bottom: 2px solid #B3002D;
			background-color: #CC0033!important;
		}
		.navbar-brand {
			width: 45px;
			height: 43px;
			margin-top: -4px;
			background-repeat: no-repeat;
			background-image: url(data:image/jpeg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAAA8AAD/4QOLaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA2LjAtYzAwMiA3OS4xNjQ0NjAsIDIwMjAvMDUvMTItMTY6MDQ6MTcgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InhtcC5kaWQ6ZGIyNjFiZDMtNmZlOS0zNzRhLWI3YjAtOWU0NDFkNjEzMGRiIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjQyNjVGRTEwMTIxNjExRUM4NEIxRTI2RTczQUIzOUQyIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjQyNjVGRTBGMTIxNjExRUM4NEIxRTI2RTczQUIzOUQyIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKE1hY2ludG9zaCkiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo1NGMwOGQzYy0zODhhLWEzNDMtYjlkNy00MDNkMjY3NWFhMzYiIHN0UmVmOmRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDoxOWQ2NWQwYS1kY2I4LTExNDktYjgzNy1jOTUzZjQ1MmZkYTciLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7/7gAOQWRvYmUAZMAAAAAB/9sAhAAGBAQEBQQGBQUGCQYFBgkLCAYGCAsMCgoLCgoMEAwMDAwMDBAMDg8QDw4MExMUFBMTHBsbGxwfHx8fHx8fHx8fAQcHBw0MDRgQEBgaFREVGh8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx8fHx//wAARCAA2ADYDAREAAhEBAxEB/8QAlAAAAQUBAQAAAAAAAAAAAAAABQACBAYHAQMBAAIDAQEBAAAAAAAAAAAAAAAEAwUGAgEHEAACAQMDAwIDBwUBAAAAAAABAgMRBAUAEgYhMRNRIkEyI2FxgUJSFCTBYkMVFgcRAAEDAgUBBgUCBwAAAAAAAAEAAgMRBCExQVEScWHBIjITBfCBkaEUQqLR4VJiIzMV/9oADAMBAAIRAxEAPwDMNLr7iloQkKkgeuhBVgzPFbiyxUORi+rAwX9w6OriNm7Kduqez91bLKYzg7TAiqrre/D5Cw4HTDNV/VxRWKWhCWhC5oQnIjO6ooqzEBR9p14SAKleE0FVrFjgMBj+KzpNardSpCz3jBaymSnZPiAvw18+n9wuJrxpa7gOXh2p2rJS3U0lwCDxFcNqINx/C3UXEckjxN58lGTDCerbU6x9PVm1ae43zHXsZB8MRxPXNO3dy03DCDgw496I5LjWKu+I2yrZiyyKQhohSkm8D3B/UNTSVt7nLHeOq/nGXY7U0p0S8N5Iy4Pi5Mr8vksuIIJB6EdCNbtakLmhCWhC97GVYryCRvlSRWP3A6jmaXMcBqCo5W1aR2LUchlZLBI5LdDLdXLCO0iU9XZ+o/DrrB21oJiQ40Y0VcdqLLxQCSodg1uaLSrkNr+KjZFU3KlRTzBa7a9qV6arWGOor/rr+2uaTaWa+Sv2Qezyq3tq95Lujli3rdRufcjpUsD/AE1Zz2hieGDEGnEjUHJOSQcHBoyOSyyZw80jjszEj8TretFAAtU0UACZrpdJaEKZisc1/diBW2ChLNStAPs0vdXAhZyUM83ptqtC4jxnlmTkF1aJTH4LyiLJTqQrdPl2n5tv9vXVFcRMka4NbjLQvxo1vbyyx2Wd9xvbeJpDvNJSrRmrI2Gv0t4QmetmyBuZN15tBgKom8KVDV2vXYo71Gq//nwF5FWceNOPI168qfyVP+c3kawv4Uy16qqct41yvE3HjyERjx+ckSR76Me0kr8lB0Xr66soGMja0ubR8TTxBxBG4dr3K79vvIJmgs80Y8pzVByFm1neSW7Nu2Ho3qNXtvMJGB260MUnNoKjamUiWhCIYO+SzyMcshKxNVJGHwVvj+GlL2AyREDPMJe6iL2EDNXHIvyWTCy47FXzPiJWM72Kk7QwHzK5JqX/AE6pILuMFrZgfCcNq9rezdU8PoiQPkb/AJBhX42QqW/kbADjox84vofexBBowO8tQD9J1Oy3AuPyubfTdh3UTLYgJvW5DgUWtbzkEGItbfP3zJi7ImW0s2bs9Oj1HcipAXS807JKx24J5HHb6adUrIyJ0hMLfG7M9ypGVvRe381yBtVz7QevQdBq/tYfSjDdldwR8GBqiaYUyLY7j8l1Ytf3F1BY2e8xRS3DEeSRRuKoB16Aip1yXUSk10Gu4gFzuzQJ0nEuQrG0q2UkluFaTzoKoUU0ZgfQa6QL6KtOWOymYvC8sjy/+psjJHOAsjkH6aqwqrsT0pqCW2jk8zQVDPPAY/UdQj7orYY3lt1Z3dzbSxCS1Z4DDWkjyIu93Un8xU/E9dKn2yI5jX4HRKyyW7XBpBocfloEEyOE5TPHHcXMU1wzKzSJRmaLbIY6Ov5SSNORQMjFGNonYbiBpIaQO/Cqjf8AK8joW/109B3Ow6lUn5sP9QUeXDZaJYWktJVFwrPDVT7lUbmI+5RXXlVILiM1oRgiWLustb4Ul7S2u8S05EIvCAqz7RuMf1Im+Wm7uuhLTMjdJ5nNfT9O3bgf4ozd3vN1WT97YQMnicyb2G3wlhUe2UDaG7a9KUZHbYcXHP7/AEXoL3nYyzyDH25uDFbKYQy+MDd/GcES993b3U9RrzVc+na8KcjSruv92igzXHNRKzT2wMYyJa4QkeE3exfZLR9u3bSlTT7dBzUzW21MD+jDfj2YI9Nff+pnyFsfGAzxgqjL7ZQxKn2y1r8DXprrFJNjscPEdfp9EEsp+eCMeC3Zl8hoWP5/OD1q4/y9NchOSNta4nTu6bKTNdc/bIW/8ONL2O6YVUr9S6NuerVkK1EPalF0KNrLXgfF4eP7eXTf5r//2Q==);
		}
		.logoGF {
			text-align: center;
			border-top: 1px solid #e5e5e5;
			position: fixed;
			height: 40px;
			background-color: #efefef;
			bottom: 0px;
			left: 0px;
			right: 0px;
			padding-top: 3px;
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
		margin:0px 0vw 4vw 0vw;
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