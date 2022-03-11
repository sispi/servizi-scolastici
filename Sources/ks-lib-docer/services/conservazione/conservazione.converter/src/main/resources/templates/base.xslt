<?xml version="1.0" encoding="UTF-8" standalone="no" ?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:utils="java:it.kdm.docer.conservazione.Utils">

	<xsl:template match="/">
		<unitaDocumentaria>
			<versione>1.1</versione>
			<codiceEnte>regione_emilia-romagna</codiceEnte>
			<codiceAoo>giunta</codiceAoo>
			
			<ambiente>PARER_TEST</ambiente>
			
			<userId>egrammata</userId>
			<numeroPG><xsl:value-of select="/root/documento/NUM_PG" /></numeroPG>
			<annoPG><xsl:value-of select="/root/documento/ANNO_PG" /></annoPG>
			<registroPG><xsl:value-of select="/root/documento/REGISTRO_PG" /></registroPG>

			<xsl:variable name="type">
				<xsl:call-template name="get-type" />
			</xsl:variable>			
			<tipologia><xsl:value-of select="$type" /></tipologia>
			
			<xsl:if test="/root/documento/COD_TITOLARIO">
				<fascicoloPrincipale>
					<!-- TODO: Potrebbe stare dentro DES_TITOLARIO.
						Formato: x.x.x - Descrizione
					 -->
					<!-- RER -->
					<!-- <xsl:variable name="classifica"
						 select="/root/documento/COD_TITOLARIO"/> -->
					<!-- PROBO -->
					<xsl:variable name="classifica"
						select="substring-before(/root/documento/DES_TITOLARIO,'-')" />
					
					<codiceTitolario><xsl:value-of select="$classifica" /></codiceTitolario>
					<xsl:if test="/root/documento/NUM_FASCICOLO">
						<codiceFascicolo>
							<xsl:value-of select="$classifica" />
							<xsl:text>/</xsl:text>
							<xsl:value-of select="/root/documento/ANNO_FASCICOLO" />
							<xsl:text>/</xsl:text>
							<xsl:value-of select="/root/documento/NUM_FASCICOLO" />
						</codiceFascicolo>
						<descrizioneFascicolo><xsl:value-of select="/root/documento/DES_FASCICOLO" /></descrizioneFascicolo>
						
						<xsl:if test="/root/documento/NUM_SUBFASCICOLO">
							<sottoFascicolo>
								<codiceFascicolo>
									<xsl:value-of select="$classifica" />
									<xsl:text>/</xsl:text>
									<xsl:value-of select="/root/documento/ANNO_FASCICOLO" />
									<xsl:text>/</xsl:text>
									<xsl:value-of select="/root/documento/NUM_SUBFASCICOLO" />
								</codiceFascicolo>
								<descrizioneFascicolo><xsl:value-of select="/root/documento/DES_SUBFASCICOLO" /></descrizioneFascicolo>
							</sottoFascicolo>
						</xsl:if>
					</xsl:if>
				</fascicoloPrincipale>
				
				<xsl:if test="/root/documento/FASC_SECONDARI">
				
					<xsl:for-each select="tokenize(/root/documento/FASC_SECONDARI, ';')">
						
						<fascicoloSecondario>
							<xsl:variable name="tokens" select="tokenize(., '/')" />
							<codiceTitolario><xsl:value-of select="$tokens[1]" /></codiceTitolario>
							<codiceFascicolo>
								<xsl:value-of select="$tokens[1]" />
								<xsl:text>/</xsl:text>
								<xsl:value-of select="$tokens[2]" />
								<xsl:text>/</xsl:text>
								<xsl:value-of select="$tokens[3]" />
							</codiceFascicolo>
							<descrizioneFascicolo/>
							<xsl:if test="count($tokens) &gt; 3">
								<sottoFascicolo>
									<codiceFascicolo><xsl:value-of select="."/></codiceFascicolo>
									<descrizioneFascicolo />
								</sottoFascicolo>
							</xsl:if>
						</fascicoloSecondario>
					</xsl:for-each>
				
				</xsl:if>
			</xsl:if>
			
			<xsl:variable name="oggetto" select="/root/documento/OGGETTO_PG" />
			<xsl:if test="$oggetto and $oggetto != ''">
				<oggetto><xsl:value-of select="$oggetto" /></oggetto>
			</xsl:if>
			
			<xsl:variable name="data" select="/root/documento/D_REGISTRAZ" />
			<xsl:if test="$data and $data != ''">
				<data><xsl:value-of select="$data" /></data>
			</xsl:if>
			
			<xsl:apply-templates select="/root/documento">
				<xsl:with-param name="type" select="$type" />
			</xsl:apply-templates>
			<xsl:apply-templates select="/root/allegati/allegato" />
			
		</unitaDocumentaria>
	</xsl:template>
	
	<xsl:template name="get-type">
		<xsl:text>DEFAULT TYPE</xsl:text>
	</xsl:template>
	
	<xsl:template match="documento">
		<xsl:param name="type" />
		<documento>
			<xsl:call-template name="documento-template">
				<xsl:with-param name="type" select="$type" />
			</xsl:call-template>	
		</documento>
	</xsl:template>
	
	<xsl:template match="allegato">
		<allegato>
			<xsl:call-template name="documento-template">
				<!-- TODO: mappare tipi allegato -->
				<xsl:with-param name="type" select="'GENERICO'" /> <!-- Default -->
			</xsl:call-template>
		</allegato>
	</xsl:template>
	
	<xsl:template name="documento-template">
		<xsl:param name="type" />
		<docNum><xsl:value-of select="DOCNUM" /></docNum>
		<typeId><xsl:value-of select="$type" /></typeId>
		<description><xsl:value-of select="TYPE_DOC" /></description>
		<!-- <authorId><xsl:value-of select="AUTHOR_ID" /></authorId> -->
		<datiSpecifici>
			<VersioneDatiSpecifici>1.0</VersioneDatiSpecifici>
			<xsl:call-template name="get-dati-specifici" />
		</datiSpecifici>
		<tipoStruttura>DocumentoGenerico</tipoStruttura> <!-- TODO: Fissa? -->
		<componente>
			<xsl:variable name="filename">
				<xsl:choose>
					<xsl:when test="DOCNUM and DOCNUM != ''">
						<xsl:value-of select="concat(DOCNUM, '-', DOCNAME, '.', DEFAULT_EXTENSION)" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="concat(DOCNAME, '.', DEFAULT_EXTENSION)" />				
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<id><xsl:value-of select="$filename" /></id>
			<ordinePresentazione>1</ordinePresentazione>
			<tipoComponente>Contenuto</tipoComponente> <!-- TODO: Fissa? -->
			<tipoSupportoComponente>FILE</tipoSupportoComponente>
			<nomeComponente><xsl:value-of select="$filename" /></nomeComponente>
			<formatoFileVersato><xsl:value-of select="utils:findExtension($filename)" /></formatoFileVersato>
			<xsl:choose>
				<xsl:when test="USA_D_CO_CER = 'SI'">
					<utilizzoDataFirmaPerRifTemp>true</utilizzoDataFirmaPerRifTemp>
					<riferimentoTemporale><xsl:value-of select="D_CO_CER"/></riferimentoTemporale>
					<descrizioneRiferimentoTemporale><xsl:value-of select="T_D_CONTR_CER" /></descrizioneRiferimentoTemporale>
				</xsl:when>
				<xsl:otherwise>
					<utilizzoDataFirmaPerRifTemp>false</utilizzoDataFirmaPerRifTemp>
				</xsl:otherwise>
			</xsl:choose>
		</componente>
	</xsl:template>
	
	<xsl:template name="get-dati-specifici">
		
	</xsl:template>

</xsl:stylesheet>