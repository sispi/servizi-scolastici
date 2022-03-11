--13_D_PROFILO_FIRMATARI_DELTA_SOLR_p.sql
--02_D_PERMESSI_ACL_DELTA_SOLR_p.sql
--03_D_PERMESSI_ASSEGNAZIONI_DELTA_SOLR_p.sql
--04_Competenti_step1_DELTA_solr_p.sql
--04_Competenti_step2_delta_solr_p.sql
--05_D_PERMESSI_COMPET_DELTA_STEP1_SOLR_p.sql
--05_D_PERMESSI_COMPET_DELTA_STEP2_SOLR_p.sql
--05_D_PERMESSI_COMPET_DELTA_STEP3_SOLR_p.sql
--05_D_PERMESSI_COMPET_DELTA_STEP4_SOLR_p.sql
--05_D_PERMESSI_ALTRI_DELTA_STEP5_SOLR_p.sql
--06_profilo_corrispondenti_delta_step0_solr_p.sql
--06_profilo_corrispondenti_delta_Step1_solr_p.sql
--06_profilo_corrispondenti_Dett_delta_Step2_solr_P.sql
--07_D_FIRMATARIO_DELTA_SOLR_p.sql
--09_d_permessi_tot_delta_solr_p.sql
--10_d_profilo_compon_DELTA_Step3_solr_p.sql
--14_Create_PILOTA_DELTA_SOLR_p.sql
DECLARE
	cnt NUMBER;
BEGIN

SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'SOLR_PROFILE';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE SOLR_PROFILE purge';

	END IF;

	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'SOLR_FIELDS';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE SOLR_FIELDS purge';

	END IF;

	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_STEP00_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_STEP00_DELTA_SOLR purge';

	END IF;
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_STEP01_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_STEP01_DELTA_SOLR purge';

	END IF;
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_STEP02_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_STEP02_DELTA_SOLR purge';

	END IF;
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_STEP03_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_STEP03_DELTA_SOLR purge';

	END IF;
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_STEP04_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_STEP04_DELTA_SOLR purge';

	END IF;
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_DELTA_SOLR purge';

	END IF;

END;
/




CREATE TABLE D_PROFILO_STEP00_DELTA_SOLR AS (
	SELECT
		lk_utenti_creazione,
		fk_documento_esteso,
		sequ_long_id,
		lk_utenti_aggiornamento,
		fk_original_docid,
		fk_document_id,
		proto.desc_filename_originale,
		( desc_filename ),
		( desc_note ),
		( dttm_creazione ),
		( desc_provenienza ),
		( dttm_aggiornamento ),
		( nume_protocollo ),
		( data_protocollo ),
		( flag_firmato_01 ),
		desc_oggetto,
		( flag_annullato_012 ),
		( data_annullamento ),
		( desc_nota_annullamento ),
		( desc_provv_annullamento ),
		( flag_modalita ),
		( desc_proto_esterno ),
		( data_documento ),
		( fk_ufficio_protocollo ),
		( fk_registri ),
		( desc_firmatario ),
		fk_aoo
	FROM
		D_PROFILO_DOC_PROTO proto
	);
CREATE TABLE D_PROFILO_STEP01_DELTA_SOLR AS (
	SELECT
		max( proto.lk_utenti_creazione ) lk_utenti_creazione,
		max( proto.fk_documento_esteso ) fk_documento_esteso,
		proto.sequ_long_id,
		max( proto.lk_utenti_aggiornamento ) AS lk_utenti_aggiornamento,
		proto.sequ_long_id AS DOCNUM_PRINC,
		proto.sequ_long_id AS DOCNUM,
		proto.fk_original_docid AS PATH,
		max( proto.fk_document_id ) AS PATH_ANNESSO,
		( SELECT TEXT_VALORE FROM d_properties WHERE codi_nome = 'amministrazione.codice' ) AS cod_ente,
		aoo.codi_codice AS COD_AOO,
		max( aoo.desc_nome ) AS descr_aoo,
		max( proto.desc_filename_originale ) AS DOCNAME,
		max( proto.desc_filename ) AS DOCNAME_ANNESSO,
		max( proto.desc_note ) AS ABSTRACT,
		max( proto.dttm_creazione ) AS CREATION_DATE,
		max( proto.desc_provenienza ) AS APP_VERSANTE,
		'PRINCIPALE' AS TIPO_COMPONENTE,
		max( proto.dttm_aggiornamento ) AS UPDATE_DATE,
		max( proto.nume_protocollo ) AS NUM_PG,
		max( proto.data_protocollo ) AS DATA_PG,
	CASE

			WHEN max( proto.flag_firmato_01 ) = 1 THEN
			'FD'
			WHEN max( proto.desc_firmatario ) IS NOT NULL
			AND max( proto.desc_firmatario ) <> '' THEN
				'FE' ELSE NULL
				END AS TIPO_FIRMA,
			max( proto.desc_oggetto ) AS OGGETTO,
		CASE

				WHEN max( proto.flag_annullato_012 ) = 1 THEN
				'ANNULLATO' ELSE ' '
			END AS ANNULLATO,
			max( proto.data_annullamento ) AS DATA_ANNULL,
			max( proto.desc_nota_annullamento ) AS MOTIVO_ANNULL,
			max( proto.desc_provv_annullamento ) AS PROV_ANNULL,
			to_char( max( proto.data_protocollo ), 'YYYY' ) AS ANNO,
			max( proto.flag_modalita ) AS TIPO_PROTOCOLLAZIONE,
			max( proto.desc_proto_esterno ) AS NUM_PG_MITTENTE,
		CASE

				WHEN max( proto.flag_modalita ) = 'I' THEN
				max( proto.data_documento ) ELSE NULL
			END AS DATA_PG_MITTENTE,
			max( proto.flag_firmato_01 ) flag_firmato_01,
			max( proto.fk_ufficio_protocollo ) AS fk_ufficio_protocollo,
			max( proto.fk_registri ) AS fk_registri,
			max( proto.desc_firmatario ) desc_firmatario
		FROM
			D_PROFILO_STEP00_DELTA_SOLR proto
			INNER JOIN d_aree_organizzative aoo ON proto.fk_aoo = aoo.sequ_long_id
		GROUP BY
			proto.sequ_long_id,
			proto.fk_original_docid,
			aoo.codi_codice
		);
	CREATE TABLE D_PROFILO_STEP02_DELTA_SOLR AS (
		SELECT
			concat( proto.DOCNUM_PRINC, 0 ) AS DOCNUM_RICALCOLATO,
			proto.DOCNUM_PRINC,
			proto.DOCNUM,
			proto.PATH,
			max( proto.PATH_ANNESSO ) AS PATH_ANNESSO,
--max(coalesce(est.codi_nome,'--') )      as TYPE_ID,
			' ' AS TYPE_ID,
			max( proto.cod_ente ) AS COD_ENTE,
			proto.COD_AOO AS COD_AOO,
			max( proto.descr_aoo ) AS descr_aoo,
			max( proto.DOCNAME ) AS DOCNAME,
			max( proto.DOCNAME_ANNESSO ) AS DOCNAME_ANNESSO,
			max( proto.ABSTRACT ) AS ABSTRACT,
			max( proto.CREATION_DATE ) AS CREATION_DATE,
			max( proto.APP_VERSANTE ) AS APP_VERSANTE,
			'PRINCIPALE' AS TIPO_COMPONENTE,
			max( proto.UPDATE_DATE ) AS UPDATE_DATE,
			max( ute.codi_user_id ) AS AUTHOR_ID,
			max( ute1.codi_user_id ) AS TYPIST_ID,
			max( reg.codi_nome ) AS REGISTRO,
			max( proto.NUM_PG ) AS NUM_PG,
			max( proto.DATA_PG ) AS DATA_PG,
			max( proto.TIPO_FIRMA ) AS TIPO_FIRMA,
			max( proto.OGGETTO ) AS OGGETTO,
			' ' AS desc_tag,
			max( proto.ANNULLATO ) AS ANNULLATO,
			max( proto.DATA_ANNULL ) AS DATA_ANNULL,
			max( proto.MOTIVO_ANNULL ) AS MOTIVO_ANNULL,
			max( proto.PROV_ANNULL ) AS PROV_ANNULL,
			max( proto.ANNO ) AS ANNO,
			max( proto.TIPO_PROTOCOLLAZIONE ) AS TIPO_PROTOCOLLAZIONE,
			max( proto.NUM_PG_MITTENTE ) AS NUM_PG_MITTENTE,
			max( proto.DATA_PG_MITTENTE ) AS DATA_PG_MITTENTE,
			max( tit.codi_campo_ordinamento ) AS CLASSIFICA,
			max( tit2.codi_campo_ordinamento ) AS FASCICOLAZIONE,
			max( proto.flag_firmato_01 ) flag_firmato_01,
			max( proto.fk_ufficio_protocollo ) AS fk_ufficio_protocollo,
			max( proto.fk_registri ) AS fk_registri,
			max( proto.desc_firmatario ) desc_firmatario
		FROM
			D_PROFILO_STEP01_DELTA_SOLR proto
			INNER JOIN D_REGISTRI reg ON reg.sequ_long_id = coalesce( proto.fk_registri, 0 )
			INNER JOIN D_UTENTI ute ON coalesce( proto.lk_utenti_creazione, 0 ) = ute.sequ_long_id LEFT OUTER
			JOIN D_PROFILI_TAG tag ON proto.sequ_long_id = tag.fk_profili LEFT OUTER
			JOIN D_ELENCO_TAG eltag ON tag.fk_tag = eltag.sequ_long_id -- LEFT OUTER JOIN  D_DOCUMENTI_ESTESI      est    ON      coalesce(proto.fk_documento_esteso,0) = est.sequ_long_id
			LEFT OUTER
			JOIN D_TITOLARIO_DOC_PROTO titdoc ON coalesce( proto.sequ_long_id, 0 ) = titdoc.fk_profilo_doc_proto LEFT OUTER
			JOIN D_TITOLARIO tit ON titdoc.fk_titolario = tit.sequ_long_id LEFT OUTER
			JOIN D_COPIE_DOCPROTO copie ON coalesce( proto.sequ_long_id, 0 ) = copie.fk_original_docproto LEFT OUTER
			JOIN D_FOLDER fol ON copie.fk_folder = fol.sequ_long_id LEFT OUTER
			JOIN D_PRATICHE pra ON pra.sequ_long_id = fol.fk_pratica LEFT OUTER
			JOIN D_TITOLARIO tit2 ON pra.fk_titolario = tit2.sequ_long_id LEFT OUTER
			JOIN D_UTENTI ute1 ON coalesce( proto.lk_utenti_aggiornamento, 0 ) = ute1.sequ_long_id
		GROUP BY
			proto.DOCNUM_PRINC,
			DOCNUM,
			proto.PATH,
			proto.COD_AOO
		);
	CREATE TABLE D_PROFILO_STEP03_DELTA_SOLR AS (
		SELECT
			concat( alleg.sequ_long_id, 1 ) AS DOCNUM_RICALCOLATO,--allegato
			alleg.sequ_long_id AS DOCNUM,
			alleg.fk_document_id AS PATH,
			' ' AS PATH_ANNESSO,
			' ' AS TYPE_ID,
			( SELECT TEXT_VALORE FROM d_properties WHERE codi_nome = 'amministrazione.codice' ) AS cod_ente,
			alleg.desc_nome AS DOCNAME,
			' ' AS DOCNAME_ANNESSO,
			' ' AS ABSTRACT,
			alleg.dttm_creazione AS CREATION_DATE,
			' ' AS APP_VERSANTE,
			'ALLEGATO' AS TIPO_COMPONENTE,
			alleg.dttm_aggiornamento AS UPDATE_DATE,
			' ' AS REGISTRO,
			0 AS NUM_PG,
			' ' AS DATA_PG,
			' ' AS TIPO_FIRMA,
			' ' AS OGGETTO,
			'-' AS desc_tag,
			' ' AS ANNULLATO,
			' ' AS DATA_ANNULL,
			' ' AS MOTIVO_ANNULL,
			' ' AS PROV_ANNULL,
			' ' ANNO,
			' ' AS TIPO_PROTOCOLLAZIONE,
			' ' AS NUM_PG_MITTENTE,
			' ' AS DATA_PG_MITTENTE,
			' ' AS CLASSIFICA,
			' ' AS FASCICOLAZIONE,
			alleg.lk_utenti_creazione,
			alleg.lk_utenti_aggiornamento,
			alleg.fk_profilo_doc_proto
		FROM
			d_allegati_doc_proto alleg
		);
	CREATE TABLE D_PROFILO_STEP04_DELTA_SOLR AS (
		SELECT
			DOCNUM_RICALCOLATO,--allegato
			proto.sequ_long_id AS DOCNUM_PRINC,
			DOCNUM,
			PATH,
			PATH_ANNESSO,
			TYPE_ID,
			COD_ENTE,
			aoo.codi_codice AS COD_AOO,
			aoo.desc_nome AS descr_aoo,
			DOCNAME,
			DOCNAME_ANNESSO,
			ABSTRACT,
			CREATION_DATE,
			APP_VERSANTE,
			TIPO_COMPONENTE,
			UPDATE_DATE,
			ute.codi_user_id AS AUTHOR_ID,
			ute1.codi_user_id AS TYPIST_ID,
			REGISTRO,
			NUM_PG,
			DATA_PG,
			TIPO_FIRMA,
			OGGETTO,
			desc_tag,
			ANNULLATO,
			DATA_ANNULL,
			MOTIVO_ANNULL,
			PROV_ANNULL,
			ANNO,
			TIPO_PROTOCOLLAZIONE,
			NUM_PG_MITTENTE,
			DATA_PG_MITTENTE,
			CLASSIFICA,
			FASCICOLAZIONE,
			proto.flag_firmato_01 AS flag_firmato_01,
			proto.fk_ufficio_protocollo,
			proto.fk_registri,
			proto.desc_firmatario
		FROM
			D_PROFILO_STEP03_DELTA_SOLR alleg
			INNER JOIN d_profilo_doc_proto proto ON ( alleg.fk_profilo_doc_proto = proto.sequ_long_id )
			INNER JOIN d_utenti ute ON alleg.lk_utenti_creazione = ute.sequ_long_id
			INNER JOIN d_aree_organizzative aoo ON proto.fk_aoo = aoo.sequ_long_id LEFT OUTER
			JOIN d_utenti ute1 ON alleg.lk_utenti_aggiornamento = ute1.sequ_long_id
		);


	CREATE TABLE D_PROFILO_DELTA_SOLR AS (
		SELECT
			proto.DOCNUM_RICALCOLATO,
			proto.DOCNUM_PRINC,
			proto.DOCNUM,
			proto.PATH,
			( proto.PATH_ANNESSO ) AS PATH_ANNESSO,
			proto.TYPE_ID,
			proto.COD_ENTE,
			proto.COD_AOO AS COD_AOO,
			proto.descr_aoo AS descr_aoo,
			( proto.DOCNAME ) AS DOCNAME,
			( proto.DOCNAME_ANNESSO ) AS DOCNAME_ANNESSO,
			( proto.ABSTRACT ) AS ABSTRACT,
			( proto.CREATION_DATE ) AS CREATION_DATE,
			( proto.APP_VERSANTE ) AS APP_VERSANTE,
			proto.TIPO_COMPONENTE,
			( proto.UPDATE_DATE ) AS UPDATE_DATE,
			( proto.AUTHOR_ID ) AS AUTHOR_ID,
			( proto.TYPIST_ID ) AS TYPIST_ID,
			( proto.REGISTRO ) AS REGISTRO,
			( proto.NUM_PG ) AS NUM_PG,
			( proto.DATA_PG ) AS DATA_PG,
			( proto.TIPO_FIRMA ) AS TIPO_FIRMA,
			( proto.OGGETTO ) AS OGGETTO,
			proto.desc_tag AS desc_tag,
			( proto.ANNULLATO ) AS ANNULLATO,
			( proto.DATA_ANNULL ) AS DATA_ANNULL,
			( proto.MOTIVO_ANNULL ) AS MOTIVO_ANNULL,
			( proto.PROV_ANNULL ) AS PROV_ANNULL,
			( proto.ANNO ) AS ANNO,
			( proto.TIPO_PROTOCOLLAZIONE ) AS TIPO_PROTOCOLLAZIONE,
			( proto.NUM_PG_MITTENTE ) AS NUM_PG_MITTENTE,
			( proto.DATA_PG_MITTENTE ) AS DATA_PG_MITTENTE,
			proto.CLASSIFICA,
			proto.FASCICOLAZIONE,
			( proto.flag_firmato_01 ) flag_firmato_01,
			( proto.fk_ufficio_protocollo ) AS fk_ufficio_protocollo,
			( proto.fk_registri ) AS fk_registri,
			( proto.desc_firmatario ) desc_firmatario
		FROM
			D_PROFILO_STEP02_DELTA_SOLR proto UNION ALL
		SELECT
			DOCNUM_RICALCOLATO,
			DOCNUM_PRINC,
			DOCNUM,
			PATH,
			PATH_ANNESSO,
			TYPE_ID,
			COD_ENTE,
			COD_AOO,
			descr_aoo,
			DOCNAME,
			DOCNAME_ANNESSO,
			ABSTRACT,
			CREATION_DATE,
			APP_VERSANTE,
			TIPO_COMPONENTE,
			UPDATE_DATE,
			AUTHOR_ID,
			TYPIST_ID,
			REGISTRO,
			NULL AS num_pg,
			NULL data_pg,
			TIPO_FIRMA,
			OGGETTO,
			desc_tag,
			ANNULLATO,
			NULL AS DATA_ANNULL,
			MOTIVO_ANNULL,
			PROV_ANNULL,
			ANNO,
			TIPO_PROTOCOLLAZIONE,
			NUM_PG_MITTENTE,
			NULL AS DATA_PG_MITTENTE,
			CLASSIFICA,
			FASCICOLAZIONE,
			flag_firmato_01,
			fk_ufficio_protocollo,
			fk_registri,
			desc_firmatario
		FROM
			D_PROFILO_STEP04_DELTA_SOLR alleg
		);

	--------------------- ACL -----------------------
	-------------------------------------------------


	--02_D_PERMESSI_ACL_DELTA_SOLR_p.sql
--13_D_PROFILO_FIRMATARI_DELTA_SOLR_p.sql
--03_D_PERMESSI_ASSEGNAZIONI_DELTA_SOLR_p.sql
--04_Competenti_step1_DELTA_solr_p.sql
--04_Competenti_step2_delta_solr_p.sql
-- D_PERMESSI_COMPET_DELTA_STEP1_SOLR
-- D_PERMESSI_DELTA_ALTRI_SOLR

DECLARE cnt NUMBER;


BEGIN

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'DES_ACL';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE DES_ACL purge';
	END IF;

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'D_PERMESSI_ACL_DELTA_SOLR';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_ACL_DELTA_SOLR purge';
	END IF;

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'D_COMPETENTI_DELTA_STEP1_SOLR';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE D_COMPETENTI_DELTA_STEP1_SOLR purge';
	END IF;

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'D_PROFILO_FIRMATARI_DELTA_SOLR';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_FIRMATARI_DELTA_SOLR purge';
	END IF;

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'D_COMPETENTI_DELTA_SOLR';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE D_COMPETENTI_DELTA_SOLR purge';
	END IF;

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'D_PERMESSI_ASSEGN_DELTA_SOLR';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_ASSEGN_DELTA_SOLR purge';
	END IF;

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'D_PERMESSI_COMPET_DELTA_STEP1_SOLR';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_COMPET_DELTA_STEP1_SOLR purge';
	END IF;

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'D_PERMESSI_COMPET_DELTA_STEP2_SOLR';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_COMPET_DELTA_STEP2_SOLR purge';
	END IF;

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'D_PERMESSI_COMPET_DELTA_STEP3_SOLR';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_COMPET_DELTA_STEP3_SOLR purge';
	END IF;

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'D_PERMESSI_COMPET_DELTA_SOLR';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_COMPET_DELTA_SOLR purge';
	END IF;

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'D_PERMESSI_DELTA_ALTRI_SOLR';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_DELTA_ALTRI_SOLR purge';
	END IF;

	--D_PERMESSI_TOT_DELTA_SOLR

	SELECT COUNT(*) INTO cnt FROM user_tables WHERE table_name = 'D_PERMESSI_TOT_DELTA_SOLR';
	IF cnt <> 0 THEN
		EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_TOT_DELTA_SOLR purge';
	END IF;


END;
/

/*create table DES_ACL
as(

     select desc_nome , fk_original_docproto
    from (
    select
                trim(desc_nome) desc_nome
                ,fk_original_docproto
          from d_copie_docproto copie
                INNER JOIN d_folder fol ON copie.fk_folder = fol.sequ_long_id
                INNER JOIN d_acl acl ON fol.fk_acl = acl.sequ_long_id
         )  des
        group by desc_nome , fk_original_docproto);*/


create table D_PERMESSI_ACL_DELTA_SOLR
as(
	select distinct

			cast(fk_original_docproto as varchar2(100)) AS DOCNUM_RICALCOLATO,
			trim(desc_nome) AS IDENTIFICATIVO,
			'GRUPPO' AS TIPO,
			'READ' AS DIRITTO

	from d_copie_docproto copie
	INNER JOIN d_folder fol ON copie.fk_folder = fol.sequ_long_id
	INNER JOIN d_acl acl ON fol.fk_acl = acl.sequ_long_id );



/*create table D_PERMESSI_ACL_DELTA_SOLR
as(

SELECT     proto.DOCNUM_RICALCOLATO
           ,'GRUPPO' AS TIPO
           , DES_ACL.desc_nome AS IDENTIFICATIVO
           ,'READ' AS DIRITTO

  FROM   D_PROFILO_DELTA_SOLR proto
         INNER JOIN  DES_ACL      ON proto.docnum_princ =  DES_ACL.fk_original_docproto
group by DOCNUM_RICALCOLATO, proto.docnum,docnum_princ ,cod_aoo,tipo_componente,DES_ACL.desc_nome
);*/

create table D_COMPETENTI_DELTA_STEP1_SOLR as
(
         select
             FK_PROFILO
            ,uff.codi_ufficio
           ,max(uff.DESC_DESCRIZIONE) as DESC_DESCRIZIONE
           ,max(uff.fk_aoo)         as fk_aoo
           ,max(flag_tipo)      as flag_tipo
           ,max(uff.indi_email) as indi_email
           ,max(uff.indi_comune) as indi_comune
           ,max(uff.indi_toponimo)  as indi_toponimo
           ,max( uff.sequ_long_id)   as sequ_long_id
     from  D_COMPETENTI  mitt_dest
           inner join  D_UFFICI   uff            on MITT_DEST.FK_UFFICI = UFF.SEQU_LONG_ID
          group by FK_PROFILO  , uff.codi_ufficio
     );

create index idx_COMPETENTI_DELTAstep1_SOLR on D_COMPETENTI_DELTA_step1_SOLR(fk_profilo );

------------------------------------


Create table D_PROFILO_FIRMATARI_DELTA_SOLR  as

 select
  proto.docnum_ricalcolato,
  PROTO.docnum                              as id
   ,coalesce(PROTO.TYPE_id,cast('Documento' as varchar(100) )) as  TYPE
   ,cast('-' as varchar(100) ) as GROUP_ID
   ,cast ('-' as varchar(100) ) as    PARENT_GROUP_ID
   ,PROTO.COD_ENTE as DES_ENTE
   ,coalesce(proto.descr_aoo,' ' ) as descr_aoo
   ,PROTO.DOCNUM_PRINC
   ,PROTO.DOCNUM
   ,PROTO.PATH
   ,PROTO.PATH_ANNESSO
   ,PROTO.TYPE_ID
   ,PROTO.COD_ENTE
   ,coalesce(PROTO.COD_AOO,'-') as cod_aoo
   ,PROTO.DOCNAME
    ,PROTO.DOCNAME_ANNESSO
   ,proto.ABSTRACT
   ,PROTO.CREATION_DATE
   ,PROTO.APP_VERSANTE
  -- ,PROTO.DOC_HASH
   ,PROTO.TIPO_COMPONENTE
   ,PROTO.UPDATE_DATE
   ,PROTO.AUTHOR_ID
   ,PROTO.TYPIST_ID
   ,PROTO.REGISTRO
   ,PROTO.NUM_PG
   ,PROTO.DATA_PG
   ,PROTO.OGGETTO
   ,PROTO.DESC_TAG
   ,PROTO.ANNULLATO
   ,PROTO.DATA_ANNULL
   ,PROTO.MOTIVO_ANNULL
   ,PROTO.PROV_ANNULL
   ,PROTO.ANNO
   ,proto.TIPO_PROTOCOLLAZIONE
   ,proto.NUM_PG_MITTENTE
   ,PROTO.DATA_PG_MITTENTE
   ,PROTO.CLASSIFICA
   ,PROTO.FASCICOLAZIONE
   ,cast('-' as varchar(100))    as ACL_EXPLICIT
   ,cast('-' as varchar(100))    as mittenti
   ,cast('-' as varchar(100))    as destinatari
   ,coalesce(firm.DESC_FIRMATARIO, cast ('-' as varchar(100))) || '|&&&|' || coalesce(proto.DESC_FIRMATARIO, cast ('-' as varchar(100)) ) as firmatari
   ,cast('-' as varchar(100)) as ASSEGNATARI
   ,case when PROTO.TIPO_PROTOCOLLAZIONE  = 'I' THEN
         cast('mittenti' as varchar(100))
         when PROTO.TIPO_PROTOCOLLAZIONE  = 'U' THEN
         cast('destinatari' as varchar(100))
    else cast(null as varchar(100))  ---PROTO.TIPO_PROTOCOLLAZIONE
    END CORR_TYPE
  from
      D_PROFILO_DELTA_SOLR  proto
        left outer  join   D_FIRMATARI firm        on  (proto.docnum_ricalcolato = firm.FK_DOCPROTO)
        where proto.docnum_ricalcolato is not null ;


CREATE INDEX IDX_PROF_FIRMATARI_delta_SOLR ON D_PROFILO_FIRMATARI_DELTA_SOLR (DOCNUM_RICALCOLATO);
ALTER TABLE D_PROFILO_FIRMATARI_DELTA_SOLR ADD PRIMARY KEY (docnum_ricalcolato);

-------------------------------------------------------------------------------

create table D_COMPETENTI_DELTA_SOLR as
  (      SELECT com.FK_PROFILO   AS idprofilo, --corrisponde al docnum_princ
           cast('COMPETENTE' as varchar2(100) ) AS motivo,
           CASE
              WHEN max(com.flag_tipo) = 0 THEN 'U'
              WHEN max(com.flag_tipo) = 1 THEN 'I'
           END
            AS tipo,
            Com.codi_ufficio        AS codice,
            max( com.desc_descrizione)     AS descrizione,
           (select cast(text_valore  as varchar2(100) ) from d_properties where codi_nome  ='amministrazione.codice') as ente,
             (proto.cod_aoo) cod_aoo,
            max(proto.descr_aoo )  descr_aoo ,
           TRIM(max(coalesce( com.indi_comune,cast('-' as varchar2(100) )))|| ' ' ||TRIM(coalesce(max( com.indi_toponimo),cast('-' as varchar2(100) )))) AS indirizzo
          ,max(coalesce( com.indi_email,cast('-' as varchar2(100) ))) AS email,
           CASE
              WHEN max(com.flag_tipo) = 0 THEN 'MITTENTI'
              WHEN max(com.flag_tipo) = 1 THEN 'DESTINATARI'
           END
              AS mezzo,
          com.sequ_long_id as sequ_long_id_uff,
         max(PROTO.FK_UFFICIO_PROTOCOLLO) FK_UFFICIO_PROTOCOLLO,
         max(PROTO.FK_REGISTRI ) FK_REGISTRI
      ,max( proto.docnum_ricalcolato)  as docnum_ricalcolato
      ,proto.DOCNUM
      ,fk_profilo as  DOCNUM_PRINC
     ,max(proto.PATH) PATH
     ,max(proto.TYPE_ID) TYPE_ID
      ,(select cast(text_valore  as varchar2(100) ) from d_properties where codi_nome  ='amministrazione.codice') as cod_ente
      ,(proto.TIPO_COMPONENTE) as TIPO_COMPONENTE
     FROM  D_PROFILO_DELTA_SOLR       proto
           inner join D_COMPETENTI_DELTA_STEP1_SOLR  com on   (com.fk_profilo)=( proto.DOCNUM_princ)
      group by FK_PROFILO,docnum,proto.cod_aoo,codi_ufficio,sequ_long_id ,tipo_componente
      );
  create index idx_COMPETENTI_DELTA_SOLR on D_COMPETENTI_delta_SOLR(docnum_ricalcolato );

---------------------

create table  D_PERMESSI_ASSEGN_DELTA_SOLR as
 select *
from
(
WITH perm_ute  as
    (
   select
     max(coalesce(ut.codi_user_id,cast('-' as varchar2(100)) )) as codi_user_id
    ,max( ass.fk_utente_assegnatario)  fk_utente_assegnatario
    ,ass.fk_profilo_doc
    ,ut.sequ_long_id
    ,max(ass.dttm_creazione) dttm_creazione
  from   d_utenti ut
     INNER JOIN  d_assegnazioni ass  on    ass.fk_utente_assegnatario  = ut.sequ_long_id
   where   coalesce(ass.fk_ufficio_assegnatario,0) = 0 -- is null
    and ass.FLAG_RIFIUTATA_CANCELLATA = 0
    and ass.codi_tipo != 7
    group by   ass.fk_profilo_doc,ut.sequ_long_id
    ),
    perm_uff as
     (
    select ----permessi assegnazioni ufficio
        ass.fk_profilo_doc
            ,trim(coalesce(uff.codi_ufficio,cast('-' as CHAR )))  AS      IDENTIFICATIVO
            ,cast('READ' as VARCHAR2(100) )            as DIRITTO
            ,max(ass.dttm_creazione) dttm_creazione
     from       d_assegnazioni ass
    INNER JOIN  d_uffici uff ON ass.fk_ufficio_assegnatario = uff.sequ_long_id
    where   ass.fk_utente_assegnatario is null
     and    ass.FLAG_RIFIUTATA_CANCELLATA = 0
     and    ass.codi_tipo != 7
     group by  ass.fk_profilo_doc ,uff.codi_ufficio
     )
---
select ----permessi assegnazioni ufficio
      max(proto.DOCNUM_RICALCOLATO)  as DOCNUM_RICALCOLATO
    , proto.DOCNUM     --sequ_long_id
    , docnum_princ
    , tipo_componente
    ,  cast('GRUPPO' as varchar2(100))          as TIPO
    , ass.IDENTIFICATIVO
    , cast('READ' as varchar2(100))              as DIRITTO
    ,(proto.cod_aoo)  cod_aoo
    ,max(cod_ente) as  cod_ente
    from        D_PROFILO_DELTA_SOLR   proto
    INNER JOIN  perm_uff ass ON  proto.DOCNUM_princ = ass.fk_profilo_doc
      group by  proto.DOCNUM ,proto.docnum_princ,cod_aoo,tipo_componente,ass.identificativo
UNION all
    select ----permessi assegnazioni utente
     max(proto.DOCNUM_RICALCOLATO)  as DOCNUM_RICALCOLATO
    ,proto.DOCNUM     --sequ_long_id
    , docnum_princ
    , tipo_componente
    , cast('UTENTE' as varchar2(100))                as TIPO
    , trim(coalesce(codi_user_id,cast('-' as varchar2(100))))  IDENTIFICATIVO
    ,cast('READ'  as varchar2(100))   as DIRITTO
    ,(proto.COD_AOO) cod_aoo
    ,max(cod_ente) cod_ente
from  D_PROFILO_DELTA_SOLR   proto
    INNER JOIN  perm_ute  ass   ON     proto.DOCNUM_princ                = ass.fk_profilo_doc
    group by  proto.DOCNUM ,proto.docnum_princ,cod_aoo,tipo_componente ,trim(coalesce(codi_user_id,cast('-' as varchar2(100) )))
---2018-12-10 aggiunto per gestire le nuove assegnaziono che non comportano una modifica sulla d_profilo_doc_proto
union all
select ----permessi assegnazioni ufficio
      max(proto.DOCNUM_RICALCOLATO)  as DOCNUM_RICALCOLATO
    , proto.DOCNUM     --sequ_long_id
    , docnum_princ
    , tipo_componente
    ,  cast('GRUPPO' as varchar2(100))          as TIPO
    , ass.IDENTIFICATIVO
    , cast('READ' as varchar2(100))              as DIRITTO
    ,(proto.cod_aoo)  cod_aoo
    ,max(cod_ente) as  cod_ente
    from        D_PROFILO_FIRMATARI_DELTA_SOLR   proto
    INNER JOIN  perm_uff ass ON  proto.DOCNUM_princ = ass.fk_profilo_doc
    --and ass.dttm_creazione > (select max_data_elab_int from d_pilota_delta_solr)
      group by  proto.DOCNUM ,proto.docnum_princ,cod_aoo,tipo_componente,ass.identificativo
UNION all
    select ----permessi assegnazioni utente
     max(proto.DOCNUM_RICALCOLATO)  as DOCNUM_RICALCOLATO
    ,proto.DOCNUM     --sequ_long_id
    , docnum_princ
    , tipo_componente
    , cast('UTENTE' as varchar2(100))                as TIPO
    , trim(coalesce(codi_user_id,cast('-' as varchar2(100))))  IDENTIFICATIVO
    ,cast('READ'  as varchar2(100))   as DIRITTO
    ,(proto.COD_AOO) cod_aoo
    ,max(cod_ente) cod_ente
from  D_PROFILO_FIRMATARI_DELTA_SOLR   proto
    INNER JOIN  perm_ute  ass   ON     proto.DOCNUM_princ                = ass.fk_profilo_doc
    -- and ass.dttm_creazione >(select max_data_elab_int from d_pilota_delta_solr)
    group by  proto.DOCNUM ,proto.docnum_princ,cod_aoo,tipo_componente ,trim(coalesce(codi_user_id,cast('-' as varchar2(100) )))
)sel1;    -- group by DOCNUM_ricalcolato, DOCNUM ,docnum_princ,cod_aoo,tipo_componente;
create index i_permAss_delta_solr on  D_PERMESSI_ASSEGN_DELTA_SOLR (docnum_ricalcolato) ;

--------------------------------------------------



create table D_PERMESSI_COMPET_DELTA_STEP1_SOLR  as
(
  SELECT ---permessi competenti ufficio
  distinct
           DOCNUM_RICALCOLATO
      ,    DOCNUM  as idprofilo
          ,com.idprofilo   docnum_princ
          ,tipo_componente
         ,cast('GRUPPO'      as varchar2(100) )          as TIPO
        ,codice                           as IDENTIFICATIVO
        ,cast('READ' as varchar2(100))                    as DIRITTO
        ,cast('anlpa' as varchar2(100))                   as COD_ENTE
        ,com.cod_aoo              as COD_AOO
    from   d_competenti_DELTA_solr  com
       INNER JOIN D_UFFICI UFF ON com.sequ_long_id_uff = uff.sequ_long_id
      );
--
CREATE INDEX IDX_PERMESSI_COMPET_delta_STEP1_SOLR ON D_PERMESSI_COMPET_DELTA_STEP1_SOLR
(DOCNUM_RICALCOLATO, idprofilo,docnum_princ, tipo_componente, IDENTIFICATIVO,COD_AOO );

create table D_PERMESSI_COMPET_DELTA_STEP2_SOLR
as
(
 SELECT distinct ---permessi ufficio protocollo
         DOCNUM_RICALCOLATO
        ,DOCNUM as  idprofilo
        ,com.idprofilo   docnum_princ
        ,tipo_componente
        ,cast('GRUPPO' as varchar2(100))            as TIPO
        ,uff.codi_ufficio    as IDENTIFICATIVO
        ,cast('EDIT' as varchar2(100))             as DIRITTO
        ,cod_ente              as COD_ENTE
        ,com.cod_aoo        as COD_AOO
    from       d_competenti_delta_solr  com
    INNER JOIN d_uffici uff ON com.fk_ufficio_protocollo = uff.sequ_long_id
      );
--
CREATE INDEX IDX_PERMESSI_COMPET_DELTA_STEP2_SOLR ON D_PERMESSI_COMPET_DELTA_STEP2_SOLR
(DOCNUM_RICALCOLATO, idprofilo,docnum_princ, tipo_componente, IDENTIFICATIVO,COD_AOO );

create table D_PERMESSI_COMPET_DELTA_STEP3_SOLR  as
(
   -----permessi Registri
   SELECT      DOCNUM_RICALCOLATO
           ,DOCNUM                                              as  idprofilo
          ,com.idprofilo                                        as  docnum_princ
          ,tipo_componente
          ,cast('GRUPPO'  as varchar2(100))                               as TIPO
          , trim(com.cod_aoo)||'-'||registri.codi_nome           as IDENTIFICATIVO
           ,cast('READ' as varchar2(100))                                                      as DIRITTO
           ,cod_ente                                as COD_ENTE
           ,com.cod_aoo                                          as COD_AOO
    from   d_competenti_DELTA_solr com
    INNER JOIN d_registri registri ON com.fk_registri = registri.sequ_long_id
      );
--
CREATE INDEX IDX_PERMESSI_COMPET_DELTA_STEP3_SOLR ON D_PERMESSI_COMPET_DELTA_STEP3_SOLR
(DOCNUM_RICALCOLATO, idprofilo,docnum_princ, tipo_componente, IDENTIFICATIVO,COD_AOO );

create table D_PERMESSI_COMPET_DELTA_SOLR  as
(

    select
      *
 FROM
 (
  SELECT ------permessi competenti ufficio
          DOCNUM_RICALCOLATO,idprofilo ,docnum_princ,tipo_componente,TIPO
       ,IDENTIFICATIVO,DIRITTO,COD_ENTE, COD_AOO
    from    D_PERMESSI_COMPET_DELTA_STEP1_SOLR
   UNION all
   ---permessi ufficio protocollo
    SELECT   DOCNUM_RICALCOLATO,idprofilo ,docnum_princ,tipo_componente,TIPO
            ,IDENTIFICATIVO,DIRITTO,COD_ENTE, COD_AOO
    from   D_PERMESSI_COMPET_DELTA_STEP2_SOLR
UNION all
------permessi Registri
     SELECT
         DOCNUM_RICALCOLATO,idprofilo ,docnum_princ,tipo_componente,TIPO
       ,IDENTIFICATIVO,DIRITTO,COD_ENTE, COD_AOO
    from   D_PERMESSI_COMPET_DELTA_STEP3_SOLR
      ) query1
      --group by  DOCNUM_RICALCOLATO, idprofilo,docnum_princ, tipo_componente, IDENTIFICATIVO,COD_AOO
    )
    ;
---
CREATE INDEX IDX_D_PERMESSI_COMPET_DELTA_SOLR ON D_PERMESSI_COMPET_DELTA_SOLR
(DOCNUM_RICALCOLATO);

create table D_PERMESSI_DELTA_ALTRI_SOLR  as
select
        *
FROM
(
   SELECT   DOCNUM_RICALCOLATO
        , docnum
       , DOCNUM_PRINC
       , case when (docnum = DOCNUM_PRINC) then
        cast( 'PRINCIPALE'       as varchar2(100) )
          else
        cast( 'ALLEGATO'       as varchar2(100) )
          end TIPO_COMPONENTE
          ,cast( 'GRUPPO'       as varchar2(100) )                  as TIPO
          ,uff.codi_ufficio    as IDENTIFICATIVO
          ,cast( 'EDIT'  as varchar2(100) )             as DIRITTO
          ,cod_ente          as COD_ENTE
         ,cod_aoo        as COD_AOO
    from   d_profilo_DELTA_solr proto
         INNER JOIN d_uffici uff ON proto.fk_ufficio_protocollo = uff.sequ_long_id
UNION all
 SELECT   DOCNUM_RICALCOLATO
         , docnum
         , DOCNUM_PRINC
          , case when (docnum = DOCNUM_PRINC) then
             cast( 'PRINCIPALE'       as varchar2(100) )
           else
             cast( 'ALLEGATO'       as varchar2(100) )
           end TIPO_COMPONENTE
          ,cast( 'GRUPPO'       as varchar2(100) )                  as TIPO
          ,trim( coalesce(aoo.codi_codice,cast('-' as varchar2(100) ) ) || registri.codi_nome ) as IDENTIFICATIVO
          ,  cast( 'READ'  as varchar2(100) )             as DIRITTO
          , cod_ente           as COD_ENTE
         ,cod_aoo        as COD_AOO
 from d_profilo_delta_solr  proto
     INNER JOIN d_registri registri ON proto.fk_registri = registri.sequ_long_id
     INNER JOIN d_aree_organizzative aoo ON registri.fk_aoo = aoo.sequ_long_id
 )sel1;
 --group by  DOCNUM_RICALCOLATO,docnum, docnum_princ, tipo_componente,COD_AOO;


 ---------------------------------------



CREATE TABLE D_PERMESSI_TOT_DELTA_SOLR AS

SELECT
docnum_ricalcolato,
identificativo,

CASE
		WHEN max(tipo) = 'GRUPPO' THEN 'G'
		ELSE 'U'
END TIPO ,

CASE
		WHEN min(diritto) = 'EDIT' THEN 'NORMAL'
		ELSE 'READ'
END DIRITTO

FROM
	(
	SELECT
		docnum_ricalcolato,
		identificativo,
		tipo,
		diritto
	FROM
		D_PERMESSI_ACL_DELTA_SOLR

	UNION ALL

	SELECT
		docnum_ricalcolato,
		identificativo,
		tipo,
		diritto
	FROM
		D_PERMESSI_ASSEGN_DELTA_SOLR

	UNION ALL

	SELECT
		docnum_ricalcolato,
		identificativo,
		tipo,
		diritto
	FROM
		D_PERMESSI_COMPET_DELTA_SOLR

	UNION ALL
	SELECT
		docnum_ricalcolato,
		identificativo,
		tipo,
		diritto
	FROM
		D_PERMESSI_DELTA_ALTRI_SOLR
	) select1 group by DOCNUM_RICALCOLATO, IDENTIFICATIVO ;

---crea indici
--CREATE INDEX idx_d_permessi_tot_delta_solr ON D_PERMESSI_TOT_DELTA_SOLR ( docnum, docnum_princ, tipo_componente, cod_aoo );
CREATE INDEX idx_d_permessi_tot_2_delta_solr ON D_PERMESSI_TOT_DELTA_SOLR ( docnum_ricalcolato );
-- 62 secondi


------------------- PROFILI --------------------------
---------------------------------------------



DECLARE cnt NUMBER;
BEGIN
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_CORRISP_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_CORRISP_DELTA_SOLR purge';

	END IF;
--D_CORRISP_MITTDEST_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_CORRISP_MITTDEST_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_CORRISP_MITTDEST_DELTA_SOLR purge';

	END IF;
--D_FIRMATARI_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_FIRMATARI_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_FIRMATARI_DELTA_SOLR purge';

	END IF;
--D_COMP_MITDEST_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_COMP_MITDEST_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_COMP_MITDEST_DELTA_SOLR purge';

	END IF;

	--D_CORR_MITDEST_DETT_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_CORR_MITDEST_DETT_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_CORR_MITDEST_DETT_DELTA_SOLR purge';

	END IF;

	--D_PILOTA_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'SOLR_PILORA';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE SOLR_PILOTA purge';

	END IF;



END;
/


CREATE TABLE D_PROFILO_CORRISP_DELTA_SOLR AS WITH CORR AS (
	SELECT
		FK_DOC_PROTO,
		max( indi_codice_ente ) AS ente_eff,
		max( coalesce( indi_indirizzo, cast( '-' AS VARCHAR2 ( 100 ) ) ) ) AS indirizzo,
		max( coalesce( indi_comune, cast( '-' AS VARCHAR2 ( 100 ) ) ) ) AS comune,
		max( coalesce( INDI_EMAIL, cast( '-' AS VARCHAR2 ( 100 ) ) ) ) AS email,
		max( coalesce( codi_codice, cast( 'GRUPPO' AS VARCHAR2 ( 100 ) ) ) ) AS CODICE,
		max( PERS_DESCRIZIONE ) AS Descrizione,
		max( FK_MEZZI_SPEDIZIONE ) AS fk_mezzi_spedizione
	FROM
		D_MITTDEST_DOC_PROTO
		INNER JOIN D_PROFILO_DELTA_SOLR proto ON ( FK_DOC_PROTO = proto.DOCNUM_princ )
	GROUP BY
		FK_DOC_PROTO,
		fk_aoo
	) SELECT
	proto.docnum_ricalcolato,
	proto.DOCNUM AS id,
	cast( 'documento' AS VARCHAR2 ( 100 ) ) AS TYPE -----cast('GRUPPO' as text)  as GROUP_ID
	,
	coalesce( codice, cast( 'GRUPPO' AS VARCHAR2 ( 100 ) ) ) AS GROUP_ID --- vediV_CORRISPONDENTI.sqldalle viste passate da FEST
	,
	cast( '-' AS VARCHAR2 ( 100 ) ) AS PARENT_GROUP_ID,
	cod_ente AS DES_ENTE,
	proto.descr_aoo AS DES_AOO,
	proto.DOCNUM_PRINC,
	proto.DOCNUM,
	cod_ente AS COD_ENTE,
	proto.cod_aoo AS COD_AOO,
	proto.DOCNAME AS DOCNAME,
	proto.ABSTRACT,
	proto.CREATION_DATE,
	proto.APP_VERSANTE ---,proto.DOC_HASH
	,
	proto.TIPO_COMPONENTE,
	proto.UPDATE_DATE,
	proto.AUTHOR_ID,
	proto.TYPIST_ID,
	proto.REGISTRO,
	proto.NUM_PG,
	proto.DATA_PG,
	email,
	proto.TIPO_PROTOCOLLAZIONE AS tipo --- aggiunti per step 2
	,
	coalesce( codice, cast( 'GRUPPO' AS VARCHAR2 ( 100 ) ) ) AS codice,
	Descrizione,
	cod_ente AS ente,
	proto.cod_aoo AOO,
	CORR.INDIRIZZO AS indirizzo,
	corr.comune AS comune --,cast('-' as  varchar)   as fk_mezzi_spedizione
	,
	fk_mezzi_spedizione,
CASE

		WHEN PROTO.TIPO_PROTOCOLLAZIONE = 'I' THEN
		cast( 'MITTENTI' AS VARCHAR2 ( 100 ) )
		WHEN PROTO.TIPO_PROTOCOLLAZIONE = 'U' THEN
		cast( 'DESTINATARI' AS VARCHAR2 ( 100 ) )
	END CORR_TYPE
FROM
	D_PROFILO_DELTA_SOLR proto
	INNER JOIN CORR ON ( proto.DOCNUM_princ = CORR.fk_doc_proto )
WHERE
	PROTO.TIPO_PROTOCOLLAZIONE IN ( 'I', 'U' );-- verificare se va commentato o meno
---
CREATE INDEX idx_profcorrisp_solr ON d_profilo_corrisp_delta_solr ( DOCNUM_ricalcolato );
CREATE TABLE D_CORRISP_MITTDEST_DELTA_SOLR AS SELECT
docnum_ricalcolato,
max( id ) AS docnum,
max( DOCNUM_PRINC ) AS docnum_princ,
max( COD_AOO ) AS COD_AOO,
max( tipo_componente ) AS tipo_componente,
cast( 'CORRISPONDENTE' AS VARCHAR2 ( 100 ) ) AS T_CORR,
max( codice ) AS codice --da D_MITTDEST_DOC_PROTO.codi_codice
,
max( Descrizione ) AS descrizione,
max( cod_ente ) AS ENTE,
max( CORR_TYPE ) AS Tipo,
max( indirizzo ) AS indirizzo,
max( comune ) AS comune,
max( email ) AS email,
max( fk_mezzi_spedizione ) AS fk_mezzi_spedizione
FROM
	d_profilo_corrisp_delta_solr
GROUP BY
	docnum_ricalcolato;
---
CREATE INDEX idx_corrisp_MITTDEST_delta_solr ON D_CORRISP_MITTDEST_DELTA_SOLR ( docnum_ricalcolato );
CREATE TABLE D_CORR_MITDEST_DETT_DELTA_SOLR --D_CORR_MITTDEST_dettaglio_SOLR --mail gentile Nelle select sql viene presa la‎:D_CORR_MITDEST_dett_delta_SOLR
AS (
	SELECT DISTINCT
		docnum_ricalcolato,
		docnum,
		docnum_princ,
		mit_solr.cod_aoo,
		'CORRISPONDENTE' AS T_CORR,
		mit_solr.tipo AS TIPO,
		mit_solr.tipo_componente -- ,codi_codice as CODICE
		,
		codice,
		Descrizione,
		concat( coalesce( comune, '-' ), coalesce( indirizzo, '-' ) ) AS indirizzo,
		EMAIL,
		mit_solr.fk_mezzi_spedizione,
		coalesce( MEZ.DESC_NOME, '-' ) AS DESC_NOME,
		mit_solr.ente
	FROM
		D_CORRISP_MITTDEST_DELTA_SOLR mit_solr LEFT outer
		JOIN d_mezzi_spedizione mez ON ( mit_solr.fk_mezzi_spedizione = mez.sequ_long_id )
	);
--
CREATE INDEX idx_MITTDEST_dett_d_SOLR ON D_CORR_MITDEST_DETT_DELTA_SOLR ( docnum, docnum_princ, cod_aoo );
CREATE INDEX idx_MITTDEST_dett2_D_SOLR ON D_CORR_MITDEST_DETT_DELTA_SOLR ( docnum_ricalcolato );
-------------------------
CREATE TABLE D_FIRMATARI_DELTA_SOLR AS SELECT
docnum_ricalcolato,
max( sequ_long_id ) AS sequ_long_id,
max( aoo ) AS aoo,
max( docnum_princ ) AS docnum_princ,
LISTAGG ( firm_conc, '|&&&|' ) WITHIN GROUP ( ORDER BY firm_conc ) firmatario
FROM
	(
	SELECT
		proto.docnum_ricalcolato,
		max( proto.docnum ) AS sequ_long_id,
		cast( 'FIRMATARIO' AS VARCHAR2 ( 100 ) ) AS motivo,
		cast( 'F' AS VARCHAR2 ( 100 ) ) AS tipo,
		cast( '--' AS VARCHAR2 ( 100 ) ) AS codice,
		fir.desc_firmatario AS descrizione,
		cast( 'anlpa' AS VARCHAR2 ( 100 ) ) AS ente,
		( proto.cod_aoo ) AS aoo,
		max( DESC_LOCALITY ) AS indirizzo,
		max( DESC_CA_EMAIL ) AS email,
		cast( 'FD' AS VARCHAR2 ( 100 ) ) AS mezzo,
		proto.docnum_princ,
		cast( 'FIRMATARIO' AS VARCHAR2 ( 100 ) ),
		cast( ' $ ' AS VARCHAR2 ( 100 ) ),
		cast( 'F' AS VARCHAR2 ( 100 ) ),
		cast( ' $ ' AS VARCHAR2 ( 100 ) ),
		cast( '--' AS VARCHAR2 ( 100 ) ),
		cast( ' $ ' AS VARCHAR2 ( 100 ) ),
		fir.desc_firmatario,
		cast( ' $ ' AS VARCHAR2 ( 100 ) ),
		( SELECT cast( text_valore AS VARCHAR2 ( 100 ) ) FROM d_properties WHERE codi_nome = 'amministrazione.codice' ),
		cast( ' $ ' AS VARCHAR2 ( 100 ) ),
		proto.cod_aoo,
		cast( ' $ ' AS VARCHAR2 ( 100 ) ),
		max( DESC_LOCALITY ),
		max( DESC_CA_EMAIL ),
		cast( ' $ ' AS VARCHAR2 ( 100 ) ),
		cast( 'FD' AS VARCHAR2 ( 100 ) ) AS firm_conc
	FROM
		D_PROFILO_DELTA_SOLR proto,
		d_firmatari fir
	WHERE
		proto.docnum_princ = fir.fk_docproto
	GROUP BY
		proto.docnum_ricalcolato,
		proto.docnum,
		fir.desc_firmatario,
		proto.cod_aoo,
		proto.docnum_princ
	) select1
GROUP BY
	docnum_ricalcolato;
----'FIRMATARIO' NF viene gestito diversamente quando  si concatena sulla tabella finale
-- si prende direttamente dalla pofilo_solr
CREATE INDEX idx_Firmatari_delta_solr ON D_FIRMATARI_DELTA_SOLR ( docnum_ricalcolato );
------------------------------------------------------------------------------------------------
CREATE TABLE D_COMP_MITDEST_DELTA_SOLR AS (
	SELECT
		DOCNUM_RICALCOLATO,
		docnum,
		max( docnum_princ ) AS docnum_princ,
		( COD_AOO ) AS COD_AOO,
		tipo_componente,
		cast( 'documento' AS VARCHAR2 ( 100 ) ) AS TYPE,
		cast( 'GROUP' AS VARCHAR2 ( 100 ) ) AS GROUP_ID,
		LISTAGG ( coalesce( codice, '-' ), '|&&&|' ) WITHIN GROUP (ORDER BY codice) AS codice,
		LISTAGG ( coalesce( descrizione, '-' ), '|&&&|' ) WITHIN GROUP (ORDER BY descrizione) AS descrizione,
		max( cod_ente ) AS ENTE,
		max( TIPO ) AS tipo,
		LISTAGG ( cast( coalesce( sequ_long_id_uff, 0 ) AS VARCHAR2(100) ), '|&&&|' ) WITHIN GROUP (ORDER BY sequ_long_id_uff) AS id_uff,
		LISTAGG ( cast( coalesce( FK_UFFICIO_PROTOCOLLO, 0 ) AS VARCHAR2(100) ), '|&&&|' ) WITHIN GROUP (ORDER BY FK_UFFICIO_PROTOCOLLO) AS FK_UFFICIO_PROTOCOLLO,
		LISTAGG ( coalesce( email, '-' ), '|&&&|' ) WITHIN GROUP (ORDER BY email) AS email
	FROM
		d_competenti_DELTA_solr
	GROUP BY
		DOCNUM_RICALCOLATO,
		docnum,
		COD_AOO,
		codice,
		sequ_long_id_uff,
		tipo_componente
	);
------
CREATE INDEX idx_docnum_delta_solr ON D_COMP_MITDEST_DELTA_SOLR ( DOCNUM_RICALCOLATO );



---------------- FINALE ----------------
----------------------------------------


DECLARE cnt NUMBER;

BEGIN
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_STEP00_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_STEP00_DELTA_SOLR purge';

	END IF;
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_STEP01_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_STEP01_DELTA_SOLR purge';

	END IF;
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_STEP02_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_STEP02_DELTA_SOLR purge';

	END IF;
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_STEP03_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_STEP03_DELTA_SOLR purge';

	END IF;
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_STEP04_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_STEP04_DELTA_SOLR purge';

	END IF;
--W_PROFILO_DETT_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'W_PROFILO_DETT_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE W_PROFILO_DETT_SOLR purge';

	END IF;

END;
/



/*CREATE TABLE SOLR_PILOTA AS (
	SELECT
		cast( 'D_PROFILO_DELTA_SOLR' AS VARCHAR2 ( 100 ) ) nome_table,
		max( CREATION_DATE ) CREATION_DATE,
		max( UPDATE_DATE ) UPDATE_DATE,
		max( coalesce( UPDATE_DATE, CREATION_DATE ) ) MAX_DATA_ELAB_INT
	FROM
		D_PROFILO_DELTA_SOLR
	);*/

CREATE TABLE SOLR_FIELDS AS (
	SELECT
		TO_NUMBER(select1.docnum) as ID,
		select1.fieldname as NAME,
		select1.valore as VALUE
	FROM
		(
		SELECT
			DOCNUM_RICALCOLATO as docnum,
			'ACL_EXPLICIT' as fieldname,
			TIPO || IDENTIFICATIVO || ':' || DIRITTO AS valore
		FROM D_PERMESSI_TOT_DELTA_SOLR


		UNION ALL

		SELECT
			DOCNUM_RICALCOLATO as docnum,
			'ASSEGNATARI_SS',
			IDENTIFICATIVO
		FROM D_PERMESSI_ASSEGN_DELTA_SOLR

		UNION ALL

		SELECT
			DOCNUM_RICALCOLATO as docnum,
			MEZZO,
			'type:OU;codice:' || CODICE || ';descrizione:' || DESCRIZIONE
		FROM D_COMPETENTI_DELTA_SOLR

		UNION ALL

		SELECT
			DOCNUM_RICALCOLATO as docnum,
			'COD_UO',
			CODICE
		FROM D_COMPETENTI_DELTA_SOLR
		WHERE MEZZO = 'MITTENTI'

		UNION ALL

		SELECT
			DOCNUM_RICALCOLATO as docnum,
			CORR_TYPE,
			'type:PF;descrizione:' || DESCRIZIONE || ';indirizzo:' || INDIRIZZO || ' ' || COMUNE || ';email:' || EMAIL || ';mezzo:' || ' ' || FK_MEZZI_SPEDIZIONE

		FROM D_PROFILO_CORRISP_DELTA_SOLR

		UNION ALL

		SELECT
			FK_DOCPROTO || '0',
			'FIRMATARIO',
			'type:PF;descrizione:' || DESC_FIRMATARIO
		FROM D_FIRMATARI


	) select1 );

create index idx_ID on SOLR_FIELDS("ID" );


create table SOLR_PROFILE as (

SELECT
TO_NUMBER( DOCNUM_RICALCOLATO ) as ID,
DOCNUM_RICALCOLATO SID,
COD_ENTE as COD_ENTE ,
COD_AOO as COD_AOO,

NVL(DOCNAME, DOCNAME_ANNESSO) DESCRIPTION,

'documento' TYPE,

cast(NULL as varchar2(255)) as PARENT_SID,
cast(NULL as varchar2(255)) as PARENT_TYPE,
cast(NULL as INTEGER) as ACL_PARENT_ID,

CREATION_DATE,
CREATION_DATE DATA_DOCUMENTO,
UPDATE_DATE,
UPDATE_DATE as TIMESTAMP,

cast(NULL as varchar2(255)) as TYPE_ID,
TIPO_COMPONENTE,
cast( DOCNUM_PRINC as varchar(100)) || '0' DOCNUM_PRINC,
TO_NUMBER ( cast( DOCNUM_PRINC as varchar(100)) || '1' ) RELATED,

--trim(CLASSIFICA) CLASSIFICA,
--ANNO ANNO_FASCICOLO,
--FASCICOLAZIONE PROGR_FASCICOLO,

NVL(PATH,PATH_ANNESSO) || '/' || NVL(DOCNAME, DOCNAME_ANNESSO)  PATH ,
ABSTRACT,
AUTHOR_ID,
TYPIST_ID,
AUTHOR_ID CREATOR,
TYPIST_ID MODIFIER,

CASE
		WHEN TIPO_PROTOCOLLAZIONE = 'I' THEN 'E'
		ELSE TIPO_PROTOCOLLAZIONE
END TIPO_PROTOCOLLAZIONE,

TIPO_FIRMA,

'PROTOCOLLO_PG' REGISTRO_PG,
NUM_PG,
DATA_PG,
OGGETTO OGGETTO_PG,
trim(ANNULLATO) ANNULLATO_PG ,
DATA_ANNULL D_ANNULL_PG,
MOTIVO_ANNULL M_ANNULL_PG,
PROV_ANNULL P_ANNULL_PG,
--E_ANNULL_PG,

NUM_PG_MITTENTE,
DATA_PG_MITTENTE

FROM D_PROFILO_DELTA_SOLR

WHERE
REGISTRO = 'REGISTRO UFFICIALE' AND TIPO_COMPONENTE = 'PRINCIPALE'

 and COD_ENTE = 'anlpa'
 and COD_AOO = 'ANPAL'

 or

 TIPO_COMPONENTE <> 'PRINCIPALE'

 and COD_ENTE = 'anlpa'
 and COD_AOO = 'ANPAL'

 and exists (select 1

							from D_PROFILO_DELTA_SOLR dp2
							where cast( dp2.DOCNUM_PRINC as varchar(100)) || '0'  = dp2.DOCNUM_RICALCOLATO
							and dp2.REGISTRO = 'REGISTRO UFFICIALE'
							AND dp2.TIPO_COMPONENTE = 'PRINCIPALE')

);

ALTER TABLE "SOLR_PROFILE" ADD PRIMARY KEY (ID);


DECLARE
	cnt NUMBER;
BEGIN
	--D_COMPETENTI_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_COMPETENTI_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_COMPETENTI_DELTA_SOLR purge';

	END IF;

	--D_PROFILO_FIRMATARI_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_FIRMATARI_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_FIRMATARI_DELTA_SOLR purge';

	END IF;

	--D_COMPETENTI_DELTA_STEP1_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_COMPETENTI_DELTA_STEP1_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_COMPETENTI_DELTA_STEP1_SOLR purge';

	END IF;


	--D_CORRISP_MITTDEST_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_CORRISP_MITTDEST_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_CORRISP_MITTDEST_DELTA_SOLR purge';

	END IF;

	--D_CORR_MITDEST_DETT_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_CORR_MITDEST_DETT_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_CORR_MITDEST_DETT_DELTA_SOLR purge';

	END IF;

	--D_PERMESSI_ACL_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PERMESSI_ACL_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_ACL_DELTA_SOLR purge';

	END IF;

	--D_PERMESSI_ASSEGN_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PERMESSI_ASSEGN_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_ASSEGN_DELTA_SOLR purge';

	END IF;

	--D_PERMESSI_COMPET_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PERMESSI_COMPET_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_COMPET_DELTA_SOLR purge';

	END IF;

	--D_PERMESSI_COMPET_DELTA_STEP1_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PERMESSI_COMPET_DELTA_STEP1_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_COMPET_DELTA_STEP1_SOLR purge';

	END IF;

	--D_PERMESSI_COMPET_DELTA_STEP2_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PERMESSI_COMPET_DELTA_STEP2_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_COMPET_DELTA_STEP2_SOLR purge';

	END IF;

	--D_PERMESSI_COMPET_DELTA_STEP3_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PERMESSI_COMPET_DELTA_STEP3_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_COMPET_DELTA_STEP3_SOLR purge';

	END IF;

	--D_PERMESSI_DELTA_ALTRI_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PERMESSI_DELTA_ALTRI_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_DELTA_ALTRI_SOLR purge';

	END IF;

	--D_PERMESSI_TOT_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PERMESSI_TOT_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PERMESSI_TOT_DELTA_SOLR purge';

	END IF;

	--D_PROFILO_CORRISP_DELTA_SOLR
	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_CORRISP_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_CORRISP_DELTA_SOLR purge';

	END IF;

	SELECT
		COUNT( * ) INTO cnt
	FROM
		user_tables
	WHERE
		table_name = 'D_PROFILO_DELTA_SOLR';
	IF
		cnt <> 0 THEN
			EXECUTE IMMEDIATE 'DROP TABLE D_PROFILO_DELTA_SOLR purge';

	END IF;


END;
/

---------------------- CHECK ----------------
	--------------------------------------

	SELECT
COD_AOO,TIPO_COMPONENTE AS TIPO,
count( * ) NUM
FROM
	SOLR_PROFILE
GROUP BY
	TIPO_COMPONENTE, COD_AOO

	UNION

	SELECT null, NAME, count(*)

	FROM SOLR_FIELDS
	group by NAME;


