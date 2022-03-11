<?xml version="1.0" encoding="UTF-8" standalone="no" ?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<unitaDocumentaria>
			<versione>1.1</versione>
			<codiceEnte><xsl:value-of select="//Intestazione/Versatore/Ente" /></codiceEnte>
			<codiceAoo><xsl:value-of select="//Intestazione/Versatore/Struttura" /></codiceAoo>
			<ambiente><xsl:value-of select="//Intestazione/Versatore/Ambiente" /></ambiente>
			<userId><xsl:value-of select="//Intestazione/Versatore/UserID" /></userId>

			<xsl:apply-templates select="//Intestazione/Chiave" />

			<tipologia><xsl:value-of select="//Intestazione/TipologiaUnitaDocumentaria" /></tipologia>
			
			<xsl:if test="//ProfiloUnitaDocumentaria">
				<oggetto><xsl:value-of select="//ProfiloUnitaDocumentaria/Oggetto" /></oggetto>
				<data><xsl:value-of select="//ProfiloUnitaDocumentaria/Data" /></data>
			</xsl:if>
	
			<xsl:if test="//ProfiloArchivistico/FascicoloPrincipale/*">
				<xsl:apply-templates select="//ProfiloArchivistico/FascicoloPrincipale" />
			</xsl:if>
			
			<xsl:if test="//ProfiloArchivistico/FascicoliSecondari/FascicoloSecondario/*" >
				<xsl:apply-templates
					select="//ProfiloArchivistico/FascicoliSecondari/FascicoloSecondario" />
			</xsl:if>

			<xsl:apply-templates select="//DocumentiCollegati" />

			<xsl:apply-templates select="//DocumentoPrincipale" />
			<xsl:apply-templates select="//Allegati/Allegato" />
			<xsl:apply-templates select="//Annessi/Annesso" />
			<xsl:apply-templates select="//Annotazioni/Annotazione" />
		</unitaDocumentaria>
	</xsl:template>

	<xsl:template match="Chiave|ChiaveCollegamento|Riferimento">
		<numeroPG><xsl:value-of select="Numero" /></numeroPG>
		<annoPG><xsl:value-of select="Anno" /></annoPG>
		<registroPG><xsl:value-of select="TipoRegistro" /></registroPG>
	</xsl:template>

	<xsl:template match="FascicoloPrincipale">
		<xsl:if test="*" >
			<fascicoloPrincipale>
				<xsl:if test="Classifica">
					<codiceTitolario><xsl:value-of select="Classifica" /></codiceTitolario>
				</xsl:if>
				<xsl:apply-templates select="Fascicolo" />
				<xsl:apply-templates select="SottoFascicolo" />
			</fascicoloPrincipale>
		</xsl:if>
	</xsl:template>

	<xsl:template match="FascicoloSecondario">
		<xsl:if test="*">
			<fascicoloSecondario>
				<xsl:if test="Classifica">
					<codiceTitolario><xsl:value-of select="Classifica" /></codiceTitolario>
				</xsl:if>
				<xsl:apply-templates select="Fascicolo" />
				<xsl:apply-templates select="SottoFascicolo" />
			</fascicoloSecondario>
		</xsl:if>
	</xsl:template>

	<xsl:template match="Fascicolo">
		<xsl:call-template name="fascicolo" />
	</xsl:template>

	<xsl:template match="SottoFascicolo">
		<xsl:if test="*">
			<sottoFascicolo>
				<xsl:call-template name="fascicolo" />
			</sottoFascicolo>
		</xsl:if>
	</xsl:template>

	<xsl:template name="fascicolo">
		<codiceFascicolo><xsl:value-of select="Identificativo" /></codiceFascicolo>
		<descrizioneFascicolo><xsl:value-of select="Oggetto" /></descrizioneFascicolo>
	</xsl:template>

	<xsl:template match="DocumentoCollegato">
		<xsl:if test="*">
			<documentoCollegato>
				<xsl:apply-templates select="ChiaveCollegamento" />
				<descrizioneCollegamento><xsl:value-of select="DescrizioneCollegamento" /></descrizioneCollegamento>
			</documentoCollegato>
		</xsl:if>
	</xsl:template>

	<xsl:template match="DocumentoPrincipale">
		<documento>
			<xsl:call-template name="documento" />
		</documento>
	</xsl:template>
	
	<xsl:template match="Allegato">
		<allegato>
			<xsl:call-template name="documento" />
		</allegato>
	</xsl:template>
	
	<xsl:template match="Annotazione">
		<annotazione>
			<xsl:call-template name="documento" />
		</annotazione>
	</xsl:template>
	
	<xsl:template match="Annesso">
		<annesso>
			<xsl:call-template name="documento" />
		</annesso>
	</xsl:template>

	<xsl:template name="documento">
		<docNum><xsl:value-of select="IDDocumento" /></docNum>
		<typeId><xsl:value-of select="TipoDocumento" /></typeId>
		<xsl:if test="ProfiloDocumento/*">
			<description><xsl:value-of select="ProfiloDocumento/Descrizione" /></description>
			<authorId><xsl:value-of select="ProfiloDocumento/Autore" /></authorId>
		</xsl:if>

		<xsl:if test="DatiSpecifici">
			<datiSpecifici>
				<xsl:for-each select="DatiSpecifici/*">
					<xsl:element name="{name()}">
						<xsl:value-of select="." />
					</xsl:element>
				</xsl:for-each>
			</datiSpecifici>
		</xsl:if>
		
		<xsl:if test="DatiFiscali">
			<datiFiscali>
				<xsl:if test="DatiFiscali/Denominazione">
					<denominazione><xsl:value-of select="DatiFiscali/Denominazione" /></denominazione>
				</xsl:if>
				<xsl:if test="DatiFiscali/Nome">
					<nome><xsl:value-of select="DatiFiscali/Nome" /></nome>
				</xsl:if>
				<xsl:if test="DatiFiscali/Cognome">
					<cognome><xsl:value-of select="DatiFiscali/Cognome" /></cognome>
				</xsl:if>
				<xsl:if test="DatiFiscali/CF">
					<cf><xsl:value-of select="DatiFiscali/CF" /></cf>
				</xsl:if>
				<xsl:if test="DatiFiscali/PIVA">
					<pIVA><xsl:value-of select="DatiFiscali/PIVA" /></pIVA>
				</xsl:if>
				<dataEmissione><xsl:value-of select="DatiFiscali/DataEmissione" /></dataEmissione>
				<numeroProgressivo><xsl:value-of select="DatiFiscali/NumeroProgressivo" /></numeroProgressivo>
				<registro><xsl:value-of select="DatiFiscali/Registro" /></registro>
				<periodoFiscale><xsl:value-of select="DatiFiscali/PeriodoFiscale" /></periodoFiscale>
				<dataTermineEmissione><xsl:value-of select="DatiFiscali/DataTermineEmissione" /></dataTermineEmissione>
			</datiFiscali>
		</xsl:if>

		<tipoStruttura><xsl:value-of select="StrutturaOriginale/TipoStruttura" /></tipoStruttura>

		<xsl:apply-templates select="StrutturaOriginale/Componenti/Componente" />

	</xsl:template>

	<xsl:template match="Componente">
		<componente>
			<xsl:call-template name="componente" />
			<xsl:apply-templates select="SottoComponenti/SottoComponente" />
		</componente>
	</xsl:template>

	<xsl:template match="SottoComponente">
		<xsl:if test="*" >
			<sottoComponente>
				<xsl:call-template name="componente" />
			</sottoComponente>
		</xsl:if>
	</xsl:template>

	<xsl:template name="componente">
		<id><xsl:value-of select="ID" /></id>
		<ordinePresentazione><xsl:value-of select="OrdinePresentazione" /></ordinePresentazione>
		<tipoComponente><xsl:value-of select="TipoComponente" /></tipoComponente>
		<tipoSupportoComponente><xsl:value-of select="TipoSupportoComponente" /></tipoSupportoComponente>
		<xsl:if test="Riferimento/*">
			<riferimento>
				<xsl:apply-templates select="Riferimento" />
			</riferimento>
		</xsl:if>
		<xsl:if test="TipoRappresentazioneComponente">
			<tipoRappresentazioneComponente><xsl:value-of select="TipoRappresentazioneComponente" /></tipoRappresentazioneComponente>
		</xsl:if>
		<xsl:if test="NomeComponente">
			<nomeComponente><xsl:value-of select="NomeComponente" /></nomeComponente>
		</xsl:if>
		<xsl:if test="FormatoFileVersato">
			<formatoFileVersato><xsl:value-of select="FormatoFileVersato" /></formatoFileVersato>
		</xsl:if>
		<xsl:if test="HashVersato">
			<hashVersato><xsl:value-of select="HashVersato" /></hashVersato>
		</xsl:if>
		<xsl:if test="UrnVersato">
			<urnVersato><xsl:value-of select="UrnVersato" /></urnVersato>
		</xsl:if>
		<xsl:if test="IDComponenteVersato">
			<idComponenteVersato><xsl:value-of select="IDComponenteVersato" /></idComponenteVersato>
		</xsl:if>
		<utilizzoDataFirmaPerRifTemp><xsl:value-of select="UtilizzoDataFirmaPerRifTemp" /></utilizzoDataFirmaPerRifTemp>
		<xsl:if test="RiferimentoTemporale">
			<riferimentoTemporale><xsl:value-of select="RiferimentoTemporale" /></riferimentoTemporale>
		</xsl:if>
		<xsl:if test="DescrizioneRiferimentoTemporale">
			<descrizioneRiferimentoTemporale><xsl:value-of select="DescrizioneRiferimentoTemporale" /></descrizioneRiferimentoTemporale>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>