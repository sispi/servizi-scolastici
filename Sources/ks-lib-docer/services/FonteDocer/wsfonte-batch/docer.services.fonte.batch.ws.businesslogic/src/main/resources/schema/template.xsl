<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output
method="xml"
omit-xml-declaration="yes"
indent="yes"/>

<xsl:template match="/">
<Segnatura xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<Documenti>
		<Documento uri="" id="7555">
			<Metadati>
                            <xsl:apply-templates select="item"/>
			</Metadati>
		</Documento>
	</Documenti>
</Segnatura>
</xsl:template>

<xsl:template match="item">
<xsl:apply-templates select="*"/>
</xsl:template>

<xsl:template match="ente">
	<Parametro nome="{name()}" valore="{item/value}"/>
</xsl:template>

<xsl:template match="aoo">
	<Parametro nome="{name()}" valore="{item/value}"/>
</xsl:template>

<xsl:template match="titolario">
	<Parametro nome="CLASSIFICA" valore="{item/value}"/>
</xsl:template>

<xsl:template match="custom_fields" />
<xsl:template match="forza_accettaz" />
<xsl:template match="lock" />
<xsl:template match="stato_pantarei" />
<xsl:template match="rights" />
<xsl:template match="destinatari" />
<xsl:template match="mittenti" />
<xsl:template match="visto" />

<xsl:template match="fascicolo">
	<Parametro nome="FASCICOLO_PRIMARIO" valore="{item/value}"/>
</xsl:template>

<xsl:template match="*">
	<Parametro nome="{name()}" valore="{.}"/>
</xsl:template>
</xsl:stylesheet>