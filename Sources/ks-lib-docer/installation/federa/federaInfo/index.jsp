<%@ page import="it.cefriel.icar.inf3.ICARConstants" %>
<%@ page import="it.cefriel.icar.inf3.web.beans.AuthenticationSessionBean" %>
<%@ page import="org.apache.commons.lang.SerializationUtils" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Test di autenticazione tramite FedERa</title>
</head>
<body>
<h1>Autenticazione tramite FedERa</h1>
<hr/>
<%
    Map serviceContextMap = (Map) session.getAttribute(ICARConstants.SERVICE_CONTEXT_MAP);
    String serviceURLPrefix = (String) session.getAttribute(ICARConstants.SERVICE_URL_PREFIX);
    AuthenticationSessionBean authBean = (AuthenticationSessionBean) serviceContextMap.get(serviceURLPrefix);

    byte[] map = SerializationUtils.serialize(authBean);
    //FileUtils.writeByteArrayToFile(new File("/home/test/map.dump"), map);

    String scm = String.format("ICARConstants.SERVICE_CONTEXT_MAP: %s \n", ICARConstants.SERVICE_CONTEXT_MAP);
    String sup = String.format("ICARConstants.SERVICE_URL_PREFIX: %s \n", ICARConstants.SERVICE_URL_PREFIX);
    String up = String.format("serviceURLPrefix: %s \n", serviceURLPrefix);

    String varsDump = String.format("%s\n%s\n%s", scm, sup, up);

    //FileUtils.writeStringToFile(new File("/home/test/vars.dump"), varsDump);

%>

<h3>Identificativo Utente: <% out.print(authBean.getUserID()); %></h3>

<h3>Attributi Utente:</h3>
<table>
    <%
        for (Object key : authBean.getAttributesMap().keySet()) {
            String nomeAttributo = (String) key;
            List valoreAttributoList = (List) authBean.getAttributesMap().get(key);
            String valoreAttributo = (String) valoreAttributoList.get(0);
            out.println("<tr><td><b>" + nomeAttributo + "</b></td><td>" + valoreAttributo + "</td></tr>");
        }

    %>
</table>

<h3>AuthenticationSessionBean:</h3>
<table>
    <%

        out.println("<tr><td><b>ICARConstants.SERVICE_CONTEXT_MAP</b></td><td>" + ICARConstants.SERVICE_CONTEXT_MAP + "</td></tr>");
        out.println("<tr><td><b>ICARConstants.SERVICE_URL_PREFIX</b></td><td>" + ICARConstants.SERVICE_URL_PREFIX + "</td></tr>");
        out.println("<tr><td><b>serviceURLPrefix</b></td><td>" + serviceURLPrefix + "</td></tr>");

        out.println("<tr><td><b>SAML Assertion XML</b></td><td>" + StringEscapeUtils.escapeHtml(authBean.getAuthenticationAssertion()) + "</td></tr>");

    %>
</table>
</body>
</html>
