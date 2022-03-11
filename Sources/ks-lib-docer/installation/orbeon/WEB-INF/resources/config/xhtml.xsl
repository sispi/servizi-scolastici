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
    xmlns:version="java:org.orbeon.oxf.common.Version">

  <xsl:template match="@id|@name|@for" priority="100">
    <xsl:attribute name="{local-name()}" >
      <xsl:choose>
	<xsl:when test="../../../../@id='xforms-select1-full-template' and .='$xforms-item-effective-id$'">
	  <xsl:value-of select="concat(replace(.,'\$','__'), '1')" />
	</xsl:when>
	<xsl:when test="starts-with(.,'orbeon-xforms-inspector')">
	  <xsl:value-of select="." />
	</xsl:when>
	<xsl:when test="local-name() = 'name' and starts-with(.,'$')">
	  <xsl:value-of select="." />
	</xsl:when>
	
	<xsl:when test="contains(., '$')">
	  <xsl:value-of select="replace(.,'\$','__')" />
	</xsl:when>
	<xsl:otherwise>
	  <xsl:value-of select="."/>
	</xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>
  </xsl:template>

    <xsl:template match="xhtml:div[@class='xforms-repeat-delimiter']" priority="100">
		<xhtml:span class="xforms-repeat-delimiter"  />
    </xsl:template>
    
    <xsl:template match="xhtml:tr[@class='xforms-repeat-template']" priority="100">
    		
    </xsl:template>
    
    <xsl:template match="xhtml:tr[count(*)=0]" priority="100">
    </xsl:template>
    
    <xsl:template match="@xml:base" priority="100" />
    <xsl:template match="@xhtml:base" priority="100" />
    <xsl:template match="@base" priority="100" />
    
    <xsl:template match="@type[../@name = '$client-state']" priority="100">
    	<xsl:attribute name="type" select="'hidden'" /> 
    </xsl:template>
    
    <xsl:template match="@autocomplete" priority="100" />
    
    <xsl:template match="@class[. = 'xforms-baseline']" priority="100" />
    
    <xsl:template match="@style" priority="100" />
    
    <xsl:template match="@unselectable" priority="100" />
    
    <xsl:template match="@*:xmlns" priority="200" />
    
    <xsl:template match="@xmlns" priority="200" />
    
	<xsl:template match="xhtml:span[starts-with(@class,'xforms-loading')]">
		<xhtml:div>
			<xsl:apply-templates select="@*|node()"/>
		</xhtml:div>
	</xsl:template>
	
	<xsl:template match="xhtml:span[contains(@style,'display:block') or count(.//xhtml:p)>0 or count(.//xhtml:div)>0 or count(.//xhtml:*[contains(@style,'display:block')])>0 ]" priority="100"  >
		<xhtml:div>
			
			<xsl:apply-templates select="@*"/>
			
			<xsl:apply-templates select="node()"/>
		
		</xhtml:div>
	</xsl:template>
	
    <xsl:template match="xhtml:select[count(*)=0]">
		<xsl:copy>
			<xsl:copy-of select="@*" />
			<xhtml:option value=""></xhtml:option>
		</xsl:copy>
    </xsl:template>
    
    <xsl:template match="xhtml:input[@type='hidden']" priority="99">
		<xhtml:div>
			<xsl:copy>
				<xsl:apply-templates select="@*|node()"/>
			</xsl:copy>
		</xhtml:div>
    </xsl:template>

	<xsl:template match="@method" priority="100">
		<xsl:attribute name="method" select="lower-case(.)" />
		
    
			
    </xsl:template>

    <!--<xsl:template match="xhtml:label" priority="100">
		<xsl:variable name="for" select="./@for" />
		<xsl:variable name="for2" select="replace($for,'≡','__')" />
		<xsl:variable name="for1" select="replace($for,'__','≡')" />
		
		<xsl:choose>
		  <xsl:when test="../../@id='xforms-select1-full-template'">
		    <xsl:copy>
		      <xsl:copy-of select="@*[name() != 'for']" />
		      <xsl:attribute name="for" select="concat(replace(@for,'≡','__'), '1')" />
		      <xsl:apply-templates select="*" />
		    </xsl:copy>
		  </xsl:when>

		<xsl:when test="//node()[replace(@id,'≡','__')=$for2 or not($for) ]">
			<xsl:copy>
				<xsl:copy-of select="@class" />
				
				
				<xsl:if test="$for" >
					<xsl:attribute name="for" select="$for2" />
				</xsl:if>
				
				<xsl:apply-templates select="node()|text()"/>
			</xsl:copy>
		</xsl:when>
		</xsl:choose>
		
	</xsl:template>-->
	

	<!--<xsl:template match="@for" priority="100" >
		<xsl:variable name="for" select="." />
	
		<xsl:if test="//node()[@id = $for]">
			<xsl:attribute name="for" select="$for" />
		</xsl:if>
	
	</xsl:template>-->

</xsl:stylesheet>
