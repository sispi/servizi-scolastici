CONN &&USER_SCHEMA/&&SCHEMA_PASSWORD@&DB_ALIAS;

-- DROP TABLE CCD_JOB;
CREATE TABLE CCD_JOB
(
  ID_JOB              	INTEGER NOT NULL,
  CHIAVE_DOC            VARCHAR2(200),
  STATO               	VARCHAR2(3) NOT NULL,
	-- X: annullato
	-- A: attesa di essere elaborato
	-- WA: in esecuzione
	-- E: errore
	-- C: consegnato
  COD_ENTE            VARCHAR2(100), -- Ente del documento (stringa)
  COD_AOO             VARCHAR2(100), -- AOO del documento (stringa)
  DT_INS              DATE,
  DT_ULT_MOD          DATE,
  DT_CHIAMATA         DATE,
  -- DATI DELLA CHIAMATA PARERLIB
  WEB_SERVICE					VARCHAR2(10),
  -- SEGUONO PARAMETRI PER LA CHIAMATA DIRETTA ALLA BL CONSERVAZIONE DI DOCER
  docId               			INTEGER NOT NULL,		-- ID Documento in DocER
  XML_PROFILO_DOC_PRINCIPALE 	CLOB, 			-- XML Profilo documento principale (stringa)
  XML_PROFILO_DOC_ALLEGATI		CLOB,				-- XML Profilo allegati (stringa)
  LISTA_URI 					CLOB,					-- Lista URI documento principali e allegati (stringa)
  TIPO_DOC						VARCHAR2(100),			-- Tipo Documento (stringa)
  APP_CHIAMANTE 				VARCHAR2(50),			-- Applicazione Chiamante (stringa)	
  TIPO_CONSERVAZIONE 			VARCHAR2(30),			-- Tipo di Conservazione (stringa)
  FORZA_COLLEGAMENTO 			VARCHAR2(5), 			-- Forza Collegamento (booleano)
  FORZA_ACCETTAZIONE 			VARCHAR2(5), 			-- Forza Accettazione (booleano)
  FORZA_CONSERVAZIONE 			VARCHAR2(5), 			-- Forza Conservazione (booleano)
  DATA_REGISTRAZIONE 			VARCHAR2(20) 			-- Forza Conservazione (booleano)
  ERRCODE				        VARCHAR2(15),
  ERRMESSAGE				    VARCHAR2(4000),
);
ALTER TABLE CCD_JOB ADD (CONSTRAINT CCD_JOB_PK  PRIMARY KEY (ID_JOB));
CREATE INDEX I_CCD_JOB_DOCAPP ON CCD_JOB (ID_DOC_APP_CHIAMANTE);
CREATE INDEX I_CCD_JOB_STATO ON CCD_JOB (STATO);
CREATE INDEX I_CCD_JOB_docId ON CCD_JOB (docId);

CREATE SEQUENCE S_CCD_JOB
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;
	
COMMENT ON COLUMN CCD_JOB.ID_DOC_APP_CHIAMANTE IS 'Identificativo univoco del applicativo chiamante del documento da conservare';
COMMENT ON COLUMN CCD_JOB.STATO IS 'X: annullato, A: attesa di essere elaborato, WA: in esecuzione, E: errore, C: consegnato';
COMMENT ON COLUMN CCD_JOB.DT_INS IS 'data inserimento del record';
COMMENT ON COLUMN CCD_JOB.DT_ULT_MOD IS 'data ultima modifica del record';
COMMENT ON COLUMN CCD_JOB.DT_CHIAMATA  IS 'data chiamata del ws invocato';
COMMENT ON COLUMN CCD_JOB.WEB_SERVICE IS 'Web Server invocato. Valori possibili: VSYNC (VersamentoSync), AASYNC (AggiuntaAllegatiSync), MMDSYNC (ModificaMetaDatiSync), VSSYNC (VerificaStatoSync)';

	
-- DROP TABLE CCD_JOB_LOG;
CREATE TABLE CCD_JOB_LOG
(
  ID_LOG			  	INTEGER NOT NULL,
  ID_JOB              	INTEGER,
  CHIAVE_DOC            VARCHAR2(200)
  docId              	INTEGER NOT NULL,
  ESITO					VARCHAR2(4),
	-- 0000: Esito POSITIVO
	-- 0001: Esito positivo, ma con WARNING
	-- 0002: NEGATIVO
  DT_INS              	DATE,
  DT_CHIAMATA         	DATE,
  DT_RISPOSTA         	DATE,
  ERRCODE				VARCHAR2(15),
  MESSAGE				VARCHAR2(4000),
  -- DATI DELLA CHIAMATA DIRETTA ALLA BL CONSERVAZIONE DI DOCER
  XML_PROFILO_DOC_PRINCIPALE 	CLOB, 			-- XML Profilo documento principale (stringa)
  XML_PROFILO_DOC_ALLEGATI		CLOB,			-- XML Profilo allegati (stringa)
  LISTA_URI 					CLOB,			-- Lista URI documento principali e allegati (stringa)
  TIPO_DOC						VARCHAR2(100),	-- Tipo Documento (stringa)
  APP_CHIAMANTE 				VARCHAR2(50),	-- Applicazione Chiamante (stringa)	
  TIPO_CONSERVAZIONE 			VARCHAR2(30),	-- Tipo di Conservazione (stringa)
  FORZA_COLLEGAMENTO 			VARCHAR2(5), 	-- Forza Collegamento (booleano)
  FORZA_ACCETTAZIONE 			VARCHAR2(5), 	-- Forza Accettazione (booleano)
  FORZA_CONSERVAZIONE 			VARCHAR2(5), 	-- Forza Conservazione (booleano)
  DATA_REGISTRAZIONE 			VARCHAR2(20), 	-- Forza Conservazione (booleano) 
  -- DATI DELLA CHIAMATA PARERLIB
  WEB_SERVICE		VARCHAR2(10),
  XML_RICHIESTA		CLOB,
  XML_ESITO			CLOB 
);
ALTER TABLE CCD_JOB_LOG ADD (CONSTRAINT CCD_JOB_LOG_PK  PRIMARY KEY (ID_LOG));
CREATE INDEX I_CCD_JOB_LOG_ID_JOB ON CCD_JOB_LOG (ID_JOB);
CREATE INDEX I_CCD_JOB_LOG_DOCAPP ON CCD_JOB_LOG (ID_DOC_APP_CHIAMANTE);
CREATE INDEX I_CCD_JOB_LOG_docId ON CCD_JOB_LOG (docId);
CREATE INDEX I_CCD_JOB_LOG_ESITO ON CCD_JOB_LOG (ESITO);
CREATE INDEX I_CCD_JOB_LOG_ERRCODE ON CCD_JOB_LOG (ERRCODE);
CREATE INDEX I_CCD_JOB_LOG_MESSAGE ON CCD_JOB_LOG (MESSAGE);

CREATE SEQUENCE S_CCD_JOB_LOG
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;

COMMENT ON COLUMN CCD_JOB_LOG.ID_DOC_APP_CHIAMANTE IS 'Identificativo univoco del applicativo chiamante del documento da conservare';
COMMENT ON COLUMN CCD_JOB_LOG.ESITO IS  '0000: Esito POSITIVO, 0001: Esito positivo, ma con WARNING, 0002: NEGATIVO';
COMMENT ON COLUMN CCD_JOB_LOG.DT_INS IS 'data inserimento del record';
COMMENT ON COLUMN CCD_JOB_LOG.DT_CHIAMATA IS  'data chiamata del ws invocato';
COMMENT ON COLUMN CCD_JOB_LOG.DT_RISPOSTA IS 'data risposta del ws invocato';
COMMENT ON COLUMN CCD_JOB_LOG.ERRCODE IS 'Codice di errore/warning';
COMMENT ON COLUMN CCD_JOB_LOG.MESSAGE IS 'descrizione del errore/warning';
COMMENT ON COLUMN CCD_JOB_LOG.WEB_SERVICE IS  'Web Server invocato. Valori possibili: VSYNC (VersamentoSync), AASYNC (AggiuntaAllegatiSync), MMDSYNC (ModificaMetaDatiSync), VSSYNC (VerificaStatoSync)';
COMMENT ON COLUMN CCD_JOB_LOG.XML_RICHIESTA IS 'xml di chiamata al ws invocato di SACER generato da BL Conservazione e eseguito dalla ParerLib';
COMMENT ON COLUMN CCD_JOB_LOG.XML_ESITO IS 'xml di esito del ws invocato di SACER';


