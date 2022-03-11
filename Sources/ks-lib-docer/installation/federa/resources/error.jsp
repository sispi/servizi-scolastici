<%@ page session="false"%>
<%@ page isErrorPage="true"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html>
  <head>
    <title>FEDERA - Errore</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <meta name="description" content="Sistema di autenticazione digitale FEDERA">
	<link href="styles/federa-main.css" rel="stylesheet" type="text/css">
  </head>
  <body>
  	<div class="center-body">
      <div class="rbroundbox">
      	<div class="rbtop">
			<div></div>
		</div>
      	<div class="rbcontent">
		  <div id="top" class="top">
			<div id="header">
			  <p class="logo-sx">
				<img src="images/logo-federa.jpg" alt="logo Lepida spa">
			  </p>
			  <div class="titolo">Sistema FedERa<br />Gestione Identit&agrave; digitali</div>
			</div>
		  </div>
	      <div id="wrap">
	        <div class="all-content">
	          <div class="content">
		        <%
					String errorMessage = (String) request.getAttribute("errorMessage");
				%>
				<h1>ERRORE</h1>
				<br/>
				<br/>
				<div>
					Si sono verificati dei problemi tecnici durante l'esecuzione della risorsa "<%=request.getAttribute("javax.servlet.error.request_uri")%>"
				</div>
				<br/>
				<div>
					<c:if test="${errorMessage != null}">
						<b><c:out value='${errorMessage}' /> </b>
						<br/>
					</c:if>
				</div>
	
				<%
					boolean showDetails = new Boolean((String) application.getInitParameter("showDetailInErrorPage")).booleanValue();
					if (showDetails && request.getAttribute("javax.servlet.error.exception") != null) {
				%>
				
				<h3>Dettagli dell'errore</h3>
	
				<div class="as-label">
					<b><%=exception.getClass().getName() + ": " + exception.getMessage()%></b>
				</div>
				<div class="as-label">
				<%
					Throwable e = (Throwable) request.getAttribute("javax.servlet.error.exception");
						StackTraceElement[] stack = e.getStackTrace();

						for (int n = 0; n < Math.min(5, stack.length); n++) {
							out.write(stack[n].toString());
							out.write("<br/>");
						}

						out.write("<hr />");

						e = (e instanceof ServletException) ? ((ServletException) e).getRootCause() : e.getCause();

						if (e != null) {
							out.write("Cause: <b>" + e.getClass().getName() + "</b><div> [ " + e.getMessage() + " ] </div>");
							stack = e.getStackTrace();
							for (int n = 0; n < Math.min(5, stack.length); n++) {
								out.write(stack[n].toString());
								out.write("<br/>");
							}
						}
					}
				%>
				</div>
			  </div><!-- /content -->
		    </div><!--/all-content-->
          </div><!-- wrap -->
        </div><!-- /rbcontent -->
        <div class="rbbot">
		  <div></div>
	    </div>
      </div><!-- /rbroundbox -->
    </div><!-- /center-body -->
  </body>
</html>