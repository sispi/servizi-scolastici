<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" />

<xsl:template match="/">
		<notification>
			<saveOrUpdate>
				<record uid="{@uid}">
					<commonFields>
						<creation-timestamp><xsl:value-of select="//fascicolo/record/CREATION" /></creation-timestamp>
						<last-update-timestamp><xsl:value-of select="//fascicolo/record/LUSTUPDATE" /></last-update-timestamp>
						<codice-ente><xsl:value-of select="//fascicolo/record/COD_ENTE" /></codice-ente>
						<latitudine>0.0</latitudine>
						<longitudine>0.0</longitudine>
						<descrizione><xsl:value-of select="//documenti/record[1]/DES_ENTE" /></descrizione>
						<keywords />
						<!-- 
						<relazioni>
							<relazione autorizzazione="true" tipoId="codiceFiscale" tipoRelazione="titolare">BSNLCU74A01H501M</relazione>
						</relazioni>
						-->
						<xsl:value-of select="//fascicolo/record/relazoni" />
					</commonFields>
					<EntitaDocumentale>
						<Fascicolo>
							<numero><xsl:value-of select="//fascicolo/record/NUM_FASCICOLO" /></numero>
							<classifica><xsl:value-of select="//fascicolo/record/CLASSIFICA" /></classifica>
							<anno><xsl:value-of select="//fascicolo/record/ANNO_FASCICOLO" /></anno>
							<progressivo><xsl:value-of select="//fascicolo/record/PROGR_FASCICOLO" /></progressivo>
							<areaOrganizzativaOmogenea>
								<codice>AOO11</codice>
								<denominazione>des AOO 11</denominazione>
							</areaOrganizzativaOmogenea>
						</Fascicolo>
					</EntitaDocumentale>
				</record>
			</saveOrUpdate>
		</notification>
</xsl:template>


<xsl:template match="record" mode="document" >
	<notification>
		<saveOrUpdate>
			<record uid="{@uid}">
				<commonFields>
					<creation-timestamp><xsl:value-of select="//fascicolo/record/CREATION" /></creation-timestamp>
					<last-update-timestamp><xsl:value-of select="//fascicolo/record/LUSTUPDATE" /></last-update-timestamp>
					<codice-ente><xsl:value-of select="//fascicolo/record/COD_ENTE" /></codice-ente>
					<latitudine>0.0</latitudine>
					<longitudine>0.0</longitudine>
					<descrizione><xsl:value-of select="//documenti/record[1]/DES_ENTE" /></descrizione>
					<!-- <relazioni>-->
				<!-- <relazioni><relazione autorizzazione="true" tipoId="codiceFiscale" tipoRelazione="titolare">BSNLCU74A01H501M</relazione></relazioni></commonFields> -->
				
					<EntitaDocumentale>
						<Documento>
							<numero><xsl:value-of select="NUM_FASCICOLO" /></numero>
							<classifica><xsl:value-of select="CLASSIFICA" /></classifica>
							<tipologiaDocumentaria>
								<codice>DOCUMENTO</codice>
								<denominazione>Documento Generico</denominazione>
							</tipologiaDocumentaria>
							<idFascicolo>F540</idFascicolo>
							<registrazioni />
							<allegati><allegato documentoPrincipale="true">
									<nomeFile>test.txt</nomeFile>
									<descrizione>aaa</descrizione>
									<url>http://kdm.docer.it/download/D787/787.pdf</url>
								</allegato></allegati>
						</Documento>
					</EntitaDocumentale>
							
				</commonFields>
			</record>
		</saveOrUpdate>
	</notification></xsl:template></xsl:stylesheet>