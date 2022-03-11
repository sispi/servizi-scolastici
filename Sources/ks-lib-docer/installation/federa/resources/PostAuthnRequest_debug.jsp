<%------------------------------------------------------------------------
Copyright (C) 2008-2009 CEFRIEL
Authors: M. Pianciamore, P. Selvini, M. Zuccala', A. Pregnolato, P. Cencioni, W. Corno
 
Title to Software and all associated intellectual property rights is retained by 
"CEFRIEL Societa' Consortile a Responsabilita' Limitata" located in via Renato
Fucini 2, 20133 Milano (Italy)
 
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 	
COVERED CODE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT
WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, WITHOUT 
LIMITATION, WARRANTIES THAT THE COVERED CODE IS FREE OF DEFECTS, MERCHANTABLE,
FIT FOR A PARTICULAR PURPOSE OR NON-INFRINGING. THE ENTIRE RISK AS TO THE
QUALITY AND PERFORMANCE OF THE COVERED CODE IS WITH YOU. SHOULD ANY COVERED
CODE PROVE DEFECTIVE IN ANY RESPECT, YOU (NOT THE INITIAL DEVELOPER OR ANY
OTHER CONTRIBUTOR) ASSUME THE COST OF ANY NECESSARY SERVICING, REPAIR OR
CORRECTION.
------------------------------------------------------------------------%>
<%@ page contentType="text/html" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page language="java" %>
<%@ page import="org.apache.commons.codec.binary.Base64"%>
<%@ page import="it.cefriel.icar.inf3.saml2.ICARSAML2Constants" %>
<%@page import="it.cefriel.icar.inf3.ICARConstants"%>
<jsp:useBean id="authnRequest" scope="request" type="it.cefriel.icar.inf3.web.beans.AuthenticationRequestBean" />

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>Post SAML 2.0 AuthnRequest</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
  </head>
  
  <body>
        <form method="post" action="<%=authnRequest.getDestination() %>">
            <input type="submit" value="Procedi"/><br/>
            Destination:<br/>
            <textarea rows=10 cols="80" name="Destination"><%=authnRequest.getDestination() %></textarea><br/>
            RelayState :<br/>
            <textarea rows=10 cols="80" name="<%= ICARSAML2Constants.RELAY_STATE %>"><%=authnRequest.getRelayState() %></textarea><br/>
            SAMLRequest :<br/>
            <textarea rows=10 cols="80" name="<%= ICARSAML2Constants.SAML_REQUEST %>"><%=authnRequest.getSamlRequest() %></textarea><br/>
            <%
				String saml2RequestB64Decoded = "";
				if (Base64.isArrayByteBase64(authnRequest.getSamlRequest().getBytes())) {
					saml2RequestB64Decoded = new String(Base64.decodeBase64(authnRequest.getSamlRequest().getBytes()));
			%>
					SAML Request (Base 64 decoded):
					<br/>
					<textarea rows=10 cols="80" name="saml2ResponseB64Decoded"><%=saml2RequestB64Decoded%></textarea>
			<%  } %>
			<br/>
			Language: <input type="text" name="<%=ICARConstants.INIT_PARAMETER_LANGUAGE %>" value="<%=authnRequest.getLanguage() %>"><br/>
            <input type="submit" value="Procedi"/>
        </form>
  </body>
</html>

