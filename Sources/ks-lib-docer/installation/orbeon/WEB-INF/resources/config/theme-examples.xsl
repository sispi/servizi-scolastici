<!--
  Copyright (C) 2010 Orbeon, Inc.

  This program is free software; you can redistribute it and/or modify it under the terms of the
  GNU Lesser General Public License as published by the Free Software Foundation; either version
  2.1 of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU Lesser General Public License for more details.

  The full text of the license is available at http://www.gnu.org/copyleft/lesser.html
  -->
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:f="http://orbeon.org/oxf/xml/formatting"
    xmlns:xhtml="http://www.w3.org/1999/xhtml"
    xmlns:xforms="http://www.w3.org/2002/xforms"
    xmlns:xxforms="http://orbeon.org/oxf/xml/xforms"
	xmlns:kforms="http://kforms.tempuri.org"
    xmlns:version="java:org.orbeon.oxf.common.Version">
	



	
	<!--
	<kforms:head kforms:element="kforms|userinfo" xsl:version="2.0" >
		<xforms:model>			
			<xforms:instance id="userinfo" src="input:userInfo"/>					
		</xforms:model>
	</kforms:head>
	-->
    
	<xsl:output cdata-section-elements="script" />
    <!-- XML formatting -->
    <!--<xsl:import href="oxf:/ops/utils/formatting/formatting.xsl"/>-->

    <!-- This contains some useful request information -->
    <xsl:variable name="request" select="doc('input:request')" as="document-node()"/>

    <!-- List of applications -->
    <!--<xsl:variable name="config" select="doc('../configuration.xml')"  as="document-node()"/>-->
	
	<!--
	<xforms:model>			
		<xforms:instance id="myUserInfo" src="input:userInfo"/>					
	</xforms:model>
	-->
	
    <xsl:variable name="applications">
      <xsl:choose>
	<xsl:when test="//node()[@id='user-info']">
	  <xsl:copy-of select="doc(concat($request/request/request-url, '?action=get-apps-list&amp;output-type=xml'))" />
	</xsl:when>
	<xsl:otherwise>
	  <applications />
	</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <!-- Current application id -->
    <!--<xsl:variable name="current-application-id" select="tokenize(doc('input:request')/*/request-path, '/')[2]" as="xs:string"/>-->
    <xsl:variable name="current-application-id" select="concat(doc('input:request')/*/request-path,'?',doc('input:request')/*/query-string)" as="xs:string"/>
   
    <xsl:variable name="title" select="if (/xhtml:html/xhtml:head/xhtml:title != '')
                                       then /xhtml:html/xhtml:head/xhtml:title
                                       else if (/xhtml:html/xhtml:body/xhtml:h1)
                                            then (/xhtml:html/xhtml:body/xhtml:h1)[1]
                                            else '[Untitled]'" as="xs:string"/>
											
											
    <xsl:variable name="whois" select="substring-before(substring-after(/xhtml:html/xhtml:body,'&lt;ente>&lt;item>&lt;label/>&lt;value>'),'&lt;/value>')" />											
    
	

	
	
	<!-- MARCO 30-11-2012 : ESTRAPOLO GLI ENTI ASSOCIATI ALL'UTENTE LOGGATO -->	
    <!--
	<xsl:variable name="myuserinfo">
      <xsl:choose>
		<xsl:when test="//node()[@id='user-info']">
			<xsl:copy-of select="doc(concat($request/request/request-url, '?action=lookup-ente&amp;output-type=xml'))" />
		</xsl:when>
      </xsl:choose>
    </xsl:variable>	
	-->
	
	<!-- MARCO 30-11-2012 : ESTRAPOLO I GRUPPI ASSOCIATI ALL'UTENTE LOGGATO -->    
	<xsl:variable name="myuserinfo">
      <xsl:choose>
		<xsl:when test="//node()[@id='userinfo_groups']">
			<xsl:copy-of select="//node()[@id='userinfo_groups']" />
		</xsl:when>
      </xsl:choose>
    </xsl:variable>		
	
	

	
    <!-- - - - - - - Themed page template - - - - - - -->
    <xsl:template match="/">
        <xhtml:html xmlns="http://www.w3.org/1999/xhtml">
            <xsl:apply-templates select="/xhtml:html/@*"/>
            <xhtml:head>
				<xhtml:script type="text/javascript" src="/config/theme/jquery-1.7.min.js" />
				<xhtml:script type="text/javascript" src="/config/theme/jquery.bgiframe.min.js" /> 
				<xhtml:script type="text/javascript" src="/config/theme/highlight.pack.js" />
				<xhtml:script type="text/javascript" src="/config/theme/spin.min.js" />
				<xhtml:script type="text/javascript" src="/config/theme/customscript.js" />

                <!-- Add meta as early as possible -->
                <xsl:apply-templates select="/xhtml:html/xhtml:head/xhtml:meta"/>
                <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
                <xhtml:title><xsl:value-of select="$title"/> - Doc/er</xhtml:title>
                <!-- NOTE: The XForms engine may place additional scripts and stylesheets here as needed -->
                <xhtml:link rel="stylesheet" href="/config/theme/orbeon.css" type="text/css" media="all"/>
                
                <!-- Handle head elements except scripts -->
                <!--<xsl:apply-templates select="/xhtml:html/xhtml:head/(xhtml:link | xhtml:style)"/>-->
                <xsl:apply-templates select="/xhtml:html/xhtml:head/xhtml:link" />
                <!--<xsl:apply-templates select="/xhtml:html/xhtml:head/xhtml:style" />-->
                
                <!-- Favicon -->
                <xhtml:link rel="shortcut icon" href="/config/theme/docER-images/DocER_icon.ico"/>
                <xhtml:link rel="icon" href="/config/theme/docER-images/DocER_icon.png" type="image/png"/>
                
                <xhtml:link rel="stylesheet" href="/config/theme/styles/vs.css" type="text/css" media="all"/>
                <xhtml:link rel="stylesheet" href="/config/theme/DocER_custom.css" type="text/css" media="all"/>
                <xhtml:link rel="stylesheet" href="/config/theme/DocER_custom_IE6.css" type="text/css" media="all"/>
                <xhtml:link rel="stylesheet" href="/config/theme/DocER_custom_IE7.css" type="text/css" media="all"/>
			
				<!-- Copy first users scripts then orbeon ones -->
				<xsl:apply-templates select="/xhtml:html/xhtml:head/xhtml:script"/>
				<xsl:apply-templates select="/xhtml:html/xhtml:script"/>

				<xhtml:script type="text/javascript" src="/config/theme/jquery.blockUI.js" />

            </xhtml:head>
            <xhtml:body>
			
			
			<!--<xsl:value-of select="umbraco.library:RequestCookies('cookie')" />-->
			
                <!-- Copy body attributes -->
                <xsl:apply-templates select="/xhtml:html/xhtml:body/@*"/>
             
                <xhtml:table id="main" width="100%" border="0" cellpadding="0" cellspacing="0">
                    <!-- Banner (with search) -->
                    <xhtml:tr>
                        <xhtml:th scope="col" id="banner">
                        
                            <xhtml:div style="float: left">
                                <xhtml:h1>
								<xhtml:a href="XForms?action=search" f:url-norewrite="true">
								
                                    <xhtml:img f:url-norewrite="false" 
                                               style="border: 0 white; margin-left: 1em; margin-top: 0.2em; margin-bottom: 0.4em"
                                               src="/config/theme/docER-images/logo.gif" alt='doc/ER'/>
                                
								</xhtml:a>
								</xhtml:h1>
                            </xhtml:div>
                            <xhtml:span style="float: right; margin-right: 1em; margin-top: .2em; white-space: nowrap">
                                
                            </xhtml:span>
                            <div id="cner-link"><a href="https://cm.regione.emilia-romagna.it/cner/" title="Community Network Emilia Romagna"><xhtml:img src="/config/theme/docER-images/logo_cner.gif" alt="Community Network Emilia Romagna"/></a></div>
                            
                        </xhtml:th>
                    </xhtml:tr>
                    <!-- Tabs -->
                    <xhtml:tr>
                        <xhtml:td>
                            <xhtml:div class="tabs">
                                <xsl:copy-of select="//node()[@id='user-info']" />
                            </xhtml:div>
                        </xhtml:td>
                    </xhtml:tr>

					<!--
                    <xhtml:tr>
                        <xhtml:td>
                            <xhtml:div class="tabs">
                                <xsl:copy-of select="//node()[@id='userinfo_groups']" />
                            </xhtml:div>
                        </xhtml:td>
                    </xhtml:tr>	
					-->					
																	
                    <xhtml:tr>
                    <xhtml:td valign="top" >
					<!-- List of examples -->
						<xhtml:table id="allcontent" width="100%" border="0" cellpadding="0" cellspacing="0">
							<xhtml:tr>
							<xhtml:td id="leftcontent" valign="top" >
                       
                            <xhtml:a href="#maincontent" class="skip-nav" accesskey="p">Vai al contenuto principale</xhtml:a>
                            <xhtml:div class="tree-sections">
								
								<!-- MARCO 30-11-2012 : METTO IN UNA VARIABILE IL NODESET DEGLI ENTI ASSOCIATI ALL'UTENTE LOGGATO -->
								<xsl:variable name="enteassociato" select="tokenize($myuserinfo,';')" />	

								<!--<xsl:value-of select="$enteassociato" />-->
											
								<!-- MARCO 30-11-2012 : CICLO SOLO LE APPLICATION CON GROUP COMPRESO IN QUELLI ASSOCIATI ALL'UTENTE LOGGATO -->											
                                <!--<xsl:for-each select="$applications/*/section[application/@visible='true' and application/group=$enteassociato]">--> <!-- PRECEDENTE -->
								<xsl:for-each select="$applications/*/section[application/@visible='true' or application/group=$enteassociato]">
											
								<!--<xsl:choose>-->
								<!--<xsl:when test="./application[@visible='true']/group=$enteassociato">	-->
								<!--<xsl:when test="''=''">-->
								
								<!--<xsl:value-of select="concat(count($enteassociato),' - ', count(./application/group))" />-->
								
                                <xhtml:div class="menu">   
													
                                    <xhtml:h2 class="tree-section">
                                        <xsl:value-of select="@label"/>										
                                    </xhtml:h2>
																																													
                                    <xhtml:ul class="tree-items" >
                                        <!--<xsl:for-each select="application[@visible='true']">--> <!-- PRECEDENTE -->
										<xsl:for-each select="application">							                                      
                                            <xsl:variable name="selected" as="xs:boolean" select=" concat('/',@id) = $current-application-id"/>

											<!-- MARCO 30-11-2012 : FILTRO PER VISUALIZZARE SOLO LE APPLICATION ABILITATE PER L'ENTE ASSOCIATO ALL'UTENTE LOGGATO -->																																
											<xsl:choose>
												<!--<xsl:when test="./group=$enteassociato">-->  <!-- PRECEDENTE -->
												<xsl:when test="./group=$enteassociato or ./@visible='true'">								
											
																										
													<!-- MARCO 30-11-2012 : FILTRO PER NON VISUALIZZARE IL TITOLO DELLA SEZIONE SE NON CI SONO APPLICATION ASSOCIATE-->													
													<!--
													<xsl:choose>
														<xsl:when test="position()=1">
															<xhtml:h2 class="tree-section">
																<xsl:value-of select="../@label"/>										
															</xhtml:h2>
														</xsl:when>
													</xsl:choose>
													-->													
													
												
													<xhtml:li class="{if ($selected) then 'tree-items-selected' else 'tree-items'}" style="white-space: nowrap" >
													
														<xhtml:span class="fixed-width floatleft" >
														<xsl:choose>															
															<xsl:when test="$selected or not(@id) ">
																<xsl:value-of select="@label"/>
															</xsl:when>
															<xsl:otherwise>
																<xhtml:a  href="/{@id}">
																	<xsl:value-of select="@label"/>
																</xhtml:a>
															</xsl:otherwise>															
														</xsl:choose>
														</xhtml:span>
														
														<xhtml:span class="menubutton-group">
														
														<xsl:if test="@list">					
														
															<xsl:variable name="list_icon">
																<xsl:choose>															
																	<xsl:when test="string-length(@list_icon) > 0">
																		<xsl:value-of select="@list_icon"/>
																	</xsl:when>
																	<xsl:otherwise>
																		<xsl:value-of select="'file.gif'"/>
																	</xsl:otherwise>															
																</xsl:choose>
															</xsl:variable>
															
															<xhtml:a class="menubutton" title="Ricerca" href="/{@list}">
																<xhtml:img alt="Ricerca" src="/config/theme/docER-images/icone/{$list_icon}" />
															</xhtml:a>
														</xsl:if>
														
														<!--*** MODIFICA TRIPLA RICERCA -->
														<xsl:if test="@searchd">																								
															<xhtml:a class="menubutton" title="Ricerca Documenti" href="/{@searchd}">
																<xhtml:img alt="Ricerca Documenti" src="/config/theme/docER-images/icone/file.gif" />
															</xhtml:a>
														</xsl:if>																
														
														<xsl:if test="@searchf">																								
															<xhtml:a class="menubutton" title="Ricerca Fascicoli" href="/{@searchf}">
																<xhtml:img alt="Ricerca Fascicoli" src="/config/theme/docER-images/icone/search-fascicoli.gif" />
															</xhtml:a>
														</xsl:if>			

														<xsl:if test="@searcht">																								
															<xhtml:a class="menubutton" title="Ricerca Titolari" href="/{@searcht}">
																<xhtml:img alt="Ricerca Titolari" src="/config/theme/docER-images/icone/search-titolario.gif" />
															</xhtml:a>
														</xsl:if>
																										
														

														<xsl:if test="@create">
														
															<xsl:variable name="create_icon">
																<xsl:choose>															
																	<xsl:when test="string-length(@create_icon) > 0">
																		<xsl:value-of select="@create_icon"/>
																	</xsl:when>
																	<xsl:otherwise>
																		<xsl:value-of select="'add-vers.gif'"/>
																	</xsl:otherwise>															
																</xsl:choose>
															</xsl:variable>
														
															<xhtml:a class="menubutton" title="Nuovo" style="display:inline" href="/{@create}">
																<xhtml:img alt="Nuovo" src="/config/theme/docER-images/icone/{$create_icon}" />
															</xhtml:a>													   
														</xsl:if>
														</xhtml:span>


													</xhtml:li>
													
											<!-- MARCO 30-11-2012 : -->												
												</xsl:when>
											</xsl:choose>
											
											

                                        </xsl:for-each>
                                    </xhtml:ul>
                                    
                                    </xhtml:div>
									
								<!--	
								</xsl:when>
								</xsl:choose>
								-->
								
                                </xsl:for-each>
                            </xhtml:div>
							</xhtml:td>
							<xhtml:td id="maincontent" valign="top" >
                        
                            <xhtml:div class="maincontent">
								<xhtml:a name="maincontent" class="skiptocontent"></xhtml:a>    
								<!--<xhtml: div class="maintitle">

                                <xhtml:h1>
                                    <xsl:value-of select="$title"/>
                                </xhtml:h1>

								</xhtml: div>-->
                                <!-- Body -->
                                <xhtml:div id="mainbody">
									
                                    <xsl:apply-templates select="/xhtml:html/xhtml:body/node()"/>
                                   
                                </xhtml:div>
                            </xhtml:div>
							</xhtml:td>
						</xhtml:tr>
						</xhtml:table>

                    </xhtml:td>
					</xhtml:tr>
                </xhtml:table>
                <xhtml:p class="ops-version">
                <span id="piter-logo"><xhtml:img src="/config/theme/docER-images/logo_piter.gif" alt="PiTER - Piano telematico dell'Emilia Romagna" /></span>
				<a id="ermes-link" href="http://www.regione.emilia-romagna.it/" title="E-R: il portale della Regione Emilia Romagna"><xhtml:img src="/config/theme/docER-images/logo_rer_b.gif" alt="Regione Emilia Romagna"/></a>
</xhtml:p>

				
            </xhtml:body>
			
			
            
        </xhtml:html>
    </xsl:template>

    <!-- Simply copy everything that's not matched -->
    <xsl:template match="@*|node()" priority="-2">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
     <xsl:template match="node()[starts-with(@id,'help$')]" priority="100" />
     <xsl:template match="node()[@id='user-info']" priority="100" />
     <xsl:template match="node()[@id='help']" priority="1000" />
     <xsl:template match="node()[starts-with(@style,'kforms:kforms|dialog')]" priority="200" />
      
     <xsl:template match="xhtml:form" priority="100">
     
			
		
			<xsl:copy>
				<xsl:apply-templates select="@*"/>
				
				<xhtml:div class="maintitle">
				<xhtml:h1><xsl:value-of select="$title"/></xhtml:h1>
					<xsl:apply-templates select="//node()[@id='help']" mode="copy-div" />
				</xhtml:div>
				
                                <xsl:apply-templates select="node()[starts-with(@class,'xforms-loading')]"/>
				
				<xhtml:div>
				
					<xsl:apply-templates select="//node()[starts-with(@style,'kforms:kforms|dialog')]" mode="dialog" />
                                        
                                        <xsl:apply-templates select="node()[not(starts-with(@class,'xforms-loading'))]"/>
				
				</xhtml:div>
				
			</xsl:copy>
			
			
		
    </xsl:template>
    
    <xsl:template match="//node()" mode="dialog">

		<xhtml:div>
			
			<xsl:apply-templates select="@*"/>
			
			<xsl:apply-templates select="node()"/>
		
		</xhtml:div>    
    </xsl:template>
      
      <xsl:template match="@*|node()" mode="copy-div"  >
		  <xhtml:div>
            <xsl:apply-templates select="@*|node()"/>
		</xhtml:div>
      </xsl:template>
      
      <xsl:template match="@*|node()" mode="copy"  >
		  <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
      </xsl:template>
	  
	  
<!--<xsl:include href="actions.xsl"/>-->	  
      
<xsl:include href="xhtml.xsl"/>

</xsl:stylesheet>
