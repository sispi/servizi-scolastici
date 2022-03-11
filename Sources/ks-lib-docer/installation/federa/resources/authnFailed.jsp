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
<%@ page language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<html>
	<head>
		<link rel="stylesheet" href="styles/federa-main.css" />
	</head>
	<body>
		<div id="container">
			<div id="header">
				<p class="logo-sx">
					<img src="images/logo-federa.jpg" alt="logo Lepida spa">
				</p>
			  	<div class="titolo">Sistema FedERa<br />Gestione Identit&agrave; digitali</div>				
			</div>
			<div id="mainContent" align="center">
				<div id="disclaimerbox" class="centeredBox">
					<div class="boxhead" align="center">
						<h2>
							Autenticazione fallita
						</h2>
					</div>
					<div class="sidebox">
						<div class="boxbody" align="center">
							<p>
								Le credenziali fornite non consentono di accedere al servizio								
							</p>
							<p>
								<a href="index.html">Torna alla pagina precedente</a>								
							</p>
						</div>
					</div>
				</div>
				<p>La response SAML ricevuta è la seguente: </p>
					<form action="#">
						<textarea rows="40" cols="80"><%=(String)session.getAttribute("responseXml") %></textarea>
					</form>
			</div>
		</div>
	</body>
</html>
