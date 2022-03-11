<?xml version="1.0" encoding="UTF-8" standalone="no" ?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:utils="java:it.kdm.docer.conservazione.Utils">

	<xsl:template match="/">
		<unitaDocumentaria>
			<versione>1.1</versione>
			<codiceEnte>regione_emilia-romagna</codiceEnte>
			<codiceAoo>
				<xsl:choose>
					<xsl:when test="/root/documento/COD_AOO='AOO_EMR'">
						<xsl:text>giunta</xsl:text>
					</xsl:when>
					<xsl:when test="/root/documento/COD_AOO='AOO_IB'">
						<xsl:text>IBACN</xsl:text>
					</xsl:when>
				</xsl:choose>
			</codiceAoo>	
					
			<ambiente>PARER</ambiente>
			
			<userId>egrammata</userId>
			<numeroPG><xsl:value-of select="/root/documento/NUM_PG" /></numeroPG>
			<annoPG><xsl:value-of select="/root/documento/ANNO_PG" /></annoPG>
			<registroPG><xsl:value-of select="/root/documento/REGISTRO_PG" /></registroPG>

			<xsl:variable name="type">
				<xsl:text>SUPPLEMENTO SPECIALE DEL BOLLETTINO UFFICIALE</xsl:text>
			</xsl:variable>			
			<tipologia><xsl:value-of select="$type" /></tipologia>
			
			<xsl:if test="/root/documento/COD_TITOLARIO != ''">
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
					<xsl:if test="/root/documento/NUM_FASCICOLO != ''">
						<codiceFascicolo>
							<xsl:value-of select="$classifica" />
							<xsl:text>/</xsl:text>
							<xsl:value-of select="/root/documento/ANNO_FASCICOLO" />
							<xsl:text>/</xsl:text>
							<xsl:value-of select="/root/documento/NUM_FASCICOLO" />
						</codiceFascicolo>
						<descrizioneFascicolo><xsl:value-of select="/root/documento/DES_FASCICOLO" /></descrizioneFascicolo>
						
						<xsl:if test="/root/documento/NUM_SUBFASCICOLO != ''">
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
				
				<xsl:if test="/root/documento/FASC_SECONDARI != ''">
				
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
			<xsl:if test="utils:isDateParsable($data)">
				<data><xsl:value-of select="utils:parseDateTime($data)" /></data>
			</xsl:if>
			
			<xsl:apply-templates select="/root/documento">
				<xsl:with-param name="type" select="$type" />
			</xsl:apply-templates>
			<xsl:apply-templates select="/root/allegati/allegato" />
			
		</unitaDocumentaria>
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
				<xsl:with-param name="mode" select="'ALLEGATO'" />
			</xsl:call-template>
		</allegato>
	</xsl:template>
	
	<xsl:template name="documento-template">
		<xsl:param name="type" />
		<xsl:param name="mode" select="'DOC'" />
		<docNum><xsl:value-of select="DOCNUM" /></docNum>
		<typeId><xsl:value-of select="$type" /></typeId>
		<description><xsl:value-of select="TYPE_DOC" /></description>
		<authorId>Regione Emilia-Romagna</authorId>
		<xsl:if test="$mode = 'DOC'">
			<datiSpecifici>
				<VersioneDatiSpecifici>1.0</VersioneDatiSpecifici>
				
				<xsl:if test="ANNATA_SSBU and ANNATA_SSBU != ''" >
					<annata><xsl:value-of select="ANNATA_SSBU" /></annata>
				</xsl:if>
				<xsl:if test="DATA_SSBU and DATA_SSBU != ''" >
					<data><xsl:value-of select="DATA_SSBU" /></data>
				</xsl:if>
				<xsl:if test="NUMERAZIONE_SSBU and NUMERAZIONE_SSBU != ''" >
					<numerazione><xsl:value-of select="NUMERAZIONE_SSBU" /></numerazione>
				</xsl:if>
				<xsl:if test="NUMERO_SSBU and NUMERO_SSBU != ''" >
					<numero><xsl:value-of select="NUMERO_SSBU" /></numero>
				</xsl:if>
			</datiSpecifici>
		</xsl:if>
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
			<utilizzoDataFirmaPerRifTemp>false</utilizzoDataFirmaPerRifTemp>
			<xsl:if test="utils:isDateParsable(D_CO_CER)">
				<riferimentoTemporale><xsl:value-of select="utils:parseDateTime(D_CO_CER)"/></riferimentoTemporale>
				<descrizioneRiferimentoTemporale><xsl:value-of select="T_D_CONTR_CER" /></descrizioneRiferimentoTemporale>
			</xsl:if>
		</componente>
	</xsl:template>

</xsl:stylesheet>