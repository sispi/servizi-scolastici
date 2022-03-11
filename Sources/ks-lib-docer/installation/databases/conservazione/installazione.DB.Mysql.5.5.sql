/*
installare DB SERVER MYSQL5.5

Accedere alla linea di comando di MySQL...
Come root.

mysql -uroot -p
*/

create database CCD;
use CCD;
CREATE USER sqluser IDENTIFIED BY 'sqluserpw'; 
grant usage on *.* to sqluser@localhost identified by 'sqluserpw'; 
grant all privileges on CCD.* to sqluser@localhost;

# DROP TABLE CCD_JOB;
CREATE TABLE CCD_JOB
(
  ID_JOB              	BIGINT NOT NULL AUTO_INCREMENT,
  CHIAVE_DOC            VARCHAR(200) COMMENT 'Identificativo univoco del documento da conservare',
  STATO               	VARCHAR(3) NOT NULL COMMENT 'X: annullato, A: attesa di essere elaborato, WA: in esecuzione, E: errore, C: consegnato',
	# X: annullato
	# A: attesa di essere elaborato
	# WA: in esecuzione
	# E: errore
	# C: consegnato
  COD_ENTE            VARCHAR(100) COMMENT 'Ente del documento (stringa)',
  COD_AOO             VARCHAR(100) COMMENT 'AOO del documento (stringa)',
  DT_INS              DATETIME COMMENT 'data inserimento del record',
  DT_ULT_MOD          DATETIME COMMENT 'data ultima modifica del record',
  DT_CHIAMATA         DATETIME COMMENT 'data chiamata del ws invocato',
  # DATI DELLA CHIAMATA PARERLIB
  WEB_SERVICE					VARCHAR(10) COMMENT 'Web Server invocato. Valori possibili: VSYNC (VersamentoSync), AASYNC (AggiuntaAllegatiSync), MMDSYNC (ModificaMetaDatiSync), VSSYNC (VerificaStatoSync)',
  # SEGUONO PARAMETRI PER LA CHIAMATA DIRETTA ALLA BL CONSERVAZIONE DI DOCER
  docId               			BIGINT NOT NULL,		# ID Documento in DocER
  XML_PROFILO_DOC_PRINCIPALE 	MEDIUMTEXT, 			# XML Profilo documento principale (stringa)
  XML_PROFILO_DOC_ALLEGATI		MEDIUMTEXT,				# XML Profilo allegati (stringa)
  LISTA_URI 					TEXT,					# Lista URI documento principali e allegati (stringa)
  TIPO_DOC						VARCHAR(100),			# Tipo Documento (stringa)
  APP_CHIAMANTE 				VARCHAR(50),			# Applicazione Chiamante (stringa)	
  TIPO_CONSERVAZIONE 			VARCHAR(30),			# Tipo di Conservazione (stringa)
  FORZA_COLLEGAMENTO 			VARCHAR(5), 			# Forza Collegamento (booleano)
  FORZA_ACCETTAZIONE 			VARCHAR(5), 			# Forza Accettazione (booleano)
  FORZA_CONSERVAZIONE 			VARCHAR(5), 			# Forza Conservazione (booleano)
  DATA_REGISTRAZIONE 			VARCHAR(50), 			# Forza Conservazione (booleano),
  ERRCODE	        			VARCHAR(15) COMMENT 'Codice di errore/warning',
  ERRMESSAGE		         	TEXT COMMENT 'descrizione del errore/warning',
  PRIMARY KEY (ID_JOB)
);

	
# DROP TABLE CCD_JOB_LOG;
CREATE TABLE CCD_JOB_LOG
(
  ID_LOG			  	BIGINT  NOT NULL AUTO_INCREMENT,
  ID_JOB              	BIGINT,
  CHIAVE_DOC            VARCHAR(200) COMMENT 'Identificativo univoco del documento da conservare',
  docId              	BIGINT NOT NULL,
  ESITO					VARCHAR(4) COMMENT '0000: Esito POSITIVO, 0001: Esito positivo, ma con WARNING, 0002: NEGATIVO',
	# 0000: Esito POSITIVO
	# 0001: Esito positivo, ma con WARNING
	# 0002: NEGATIVO
  DT_INS              	DATETIME COMMENT 'data inserimento del record',
  DT_CHIAMATA         	DATETIME COMMENT 'data chiamata del ws invocato',
  DT_RISPOSTA         	DATETIME COMMENT 'data risposta del ws invocato',
  ERRCODE				VARCHAR(15) COMMENT 'Codice di errore/warning',
  MESSAGE				TEXT COMMENT 'descrizione del errore/warning',
  # DATI DELLA CHIAMATA DIRETTA ALLA BL CONSERVAZIONE DI DOCER
  XML_PROFILO_DOC_PRINCIPALE 	MEDIUMTEXT, 	# XML Profilo documento principale (stringa)
  XML_PROFILO_DOC_ALLEGATI		MEDIUMTEXT,		# XML Profilo allegati (stringa)
  LISTA_URI 					TEXT,			# Lista URI documento principali e allegati (stringa)
  TIPO_DOC						VARCHAR(100),	# Tipo Documento (stringa)
  APP_CHIAMANTE 				VARCHAR(50),	# Applicazione Chiamante (stringa)	
  TIPO_CONSERVAZIONE 			VARCHAR(30),	# Tipo di Conservazione (stringa)
  FORZA_COLLEGAMENTO 			VARCHAR(5), 	# Forza Collegamento (booleano)
  FORZA_ACCETTAZIONE 			VARCHAR(5), 	# Forza Accettazione (booleano)
  FORZA_CONSERVAZIONE 			VARCHAR(5), 	# Forza Conservazione (booleano)
  DATA_REGISTRAZIONE 			VARCHAR(50), 	# Forza Conservazione (booleano) 
  # DATI DELLA CHIAMATA PARERLIB
  WEB_SERVICE		VARCHAR(10) COMMENT 'Web Server invocato. Valori possibili: VSYNC (VersamentoSync), AASYNC (AggiuntaAllegatiSync), MMDSYNC (ModificaMetaDatiSync), VSSYNC (VerificaStatoSync)',
  XML_RICHIESTA		MEDIUMTEXT COMMENT 'xml di chiamata al ws invocato di SACER generato da BL Conservazione e eseguito dalla ParerLib',
  XML_ESITO			MEDIUMTEXT COMMENT 'xml di esito del ws invocato di SACER', 
  PRIMARY KEY (ID_LOG)
);
