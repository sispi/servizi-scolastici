<#assign report= webClient.getMap("/docer/v1/reports/list?id="+utils.getRequest().getParameter("qt") ) />
<#assign dummy=utils.getResponse().setContentType("text/plain") />
${utils.FromBase64(report.data[0].base64)}