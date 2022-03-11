<link rel="stylesheet" href="/static/protocollo/css/protocollo.css" crossorigin="anonymous">
<link rel="stylesheet" href="/static/protocollo/css/plugin.css" crossorigin="anonymous">
<link rel="stylesheet" href="/static/protocollo/css/jquery-ui.css">
<link rel="stylesheet" href="/static/protocollo/css/bootstrap-datepicker3.css" />
<link rel="stylesheet" href="/static/protocollo/css/bootstrap-multiselect.css" />
<link rel="stylesheet" href="/static/protocollo/css/wickedpicker.css" >

<form action='complete' method='POST' enctype='multipart/form-data' id='protoForm'>
  <div>
    <a id="apriDocumento" class="btn btn-default openDoc" onclick="viewDoc(ritornaNomeFilePreview(),docnumVerifica)" role="button" href="#" title="Apri documento"><i class="glyphicon glyphicon-new-window"></i>&nbsp;Apri</a>
    <button type="button" id="creaEtichetta" style="display: none;" class="btn btn-primary btn-default"><i class="glyphicon glyphicon-qrcode "></i>&nbsp;Etichetta di protocollo</button>
    <button type="button" id="creaStampigliatura" style="display: none;" class="btn btn-primary btn-default" data-toggle="modal" data-target="#stampigliatura" disabled><i class="glyphicon glyphicon-open-file">&nbsp;</i>&nbsp;Stampigliatura di protocollo</button>
    <hr>
    <div class="btn-group btn-group-justified" role="group" aria-label="...">
      <div class="btn-group" role="group">
        <button id="entrata" type="button" class="btn btn-default btn-primary" data-toggle="modal" data-target="#versoDocumento">Entrata</button>
      </div>
      <div class="btn-group" role="group">
        <button id="interna" type="button" class="btn btn-default" data-toggle="modal" data-target="#versoDocumento">Interna</button>
      </div>
      <div class="btn-group" role="group">
        <button id="uscita" type="button" class="btn btn-default" data-toggle="modal" data-target="#versoDocumento">Uscita</button>
      </div>
      <input type="hidden" id="versoDocumento-id" name="protocollo.versoDocumento" value="">
    </div>

    <div class="row">
      <div class="col-xs-12">&nbsp;</div>
    </div>
    
    <div class="row form-group">
      <div id="registroEmergenza" class="col-xs-12 checkbox">
        <label><input type="checkbox" id="checkbox1" name="protocollo.registroEmergenza" value="true" class="">Registro di emergenza</label>
      </div>
      <div id="registroEmergenzaContent">
        <div class="col-xs-4">
          <label class="control-label">*Data di protocollazione</label>
          <div class="input-group">
            <input type="text" class="form-control" id="data_pg_emergenza-id" name="protocollo.data_pg_emergenza">
          </div>
        </div>
        <div class="col-xs-4">
          <label class="control-label">*Ora di protocollazione</label>
          <div class="input-group">
            <input type="text" class="form-control timepicker" id="ora_pg_emergenza-id" name="protocollo.ora_pg_emergenza" value="">
          </div>
        </div>
        <div class="col-xs-4">
          <label class="control-label">*Numero</label>
          <div class="input-group">
            <input type="text" class="form-control" id="num_pg_emergenza-id" name="protocollo.num_pg_emergenza" value="">
          </div>
        </div>
      </div>
    </div>
    
    <div class="row">
      <div class="col-xs-12">&nbsp;</div>
    </div>
    
    <div class="form-group">
      <label>*Oggetto</label>
      <textarea class="col-md-12 form-control" id="oggetto-id" name="protocollo.oggetto"></textarea>
    </div>
        
    <div class="row">
      <div class="col-xs-12">&nbsp;</div>
    </div>

    <div class="form-group">
      <label>Note</label>
      <textarea class="col-md-12 form-control" id="note-id" name="protocollo.note"></textarea>
    </div>

    <div class="row">
      <div class="col-xs-12">&nbsp;</div>
    </div>

    <div class="row form-group">
      <div class="col-md-2">
        <label class="control-label">*Data</label>
        <div class="input-group">
          <input type="text" class="form-control" id="data_proto-id" name="protocollo.data_proto">
        </div>
      </div>
      <div class="col-md-2">
        <label class="control-label">*Ora</label>
        <input type="text" class="form-control timepicker" id="ora_proto-id" name="protocollo.ora_proto" value="">
      </div>
      <div style="display: none">
        <select id="riservatezza-id" class="form-control" name="protocollo.riservatezza">
          <option value="0">Riservato</option>
          <option value="4">Non Riservato</option>
          <option value="1" selected>Ufficio</option>
          <option value="2">Pubblico</option>
        </select>
      </div>
      <div class="col-md-8">
        <label class="control-label">*Progetto</label>
        <div class="input-group">
          <input type="text" id="classificaAutocomplete" class="form-control">
          <input type="hidden" id="classificaAutocomplete-id" name="protocollo.classificaId">
          <input type="hidden" id="classificaAutocomplete-descrizione" name="protocollo.classificaDescrizione">
        </div>
        <p id="selection"></p>
      </div>
    </div>

    <div class="row">
      <div class="col-xs-12">&nbsp;</div>
    </div>

    <div id="rubricaIpa" class="panel panel-default">
      <div class="panel-heading">
        <div id="rubricaIpaTitle" class="col-md-12"><b>Mittenti</b></div>
        <hr>
        <div class="panel-title row">
          <div class="col-md-3">
            <div class="form-check">
              <input name="gruppo1" type="radio" id="radio1" value="rub" checked>
              <label for="radio1">Rubrica</label>
              <span>
                <button id="btnAddContacts" style="margin-top: -2px;" type="button" class="btn btn-info btn-xs" data-toggle="modal" data-target="#rubrica">Nuovo</button>
              </span>
            </div>
            <div class="form-check">
              <input name="gruppo1" type="radio" id="radio2" value="ipa">
              <label for="radio2">IPA</label>
            </div>
          </div>
          <div id="rubrica-input" class="col-md-3">
            <input type="text" id="rubricaAutocomplete" placeholder="" class="form-control">
            <input type="hidden" id="rubricaAutocomplete-id" name="protocollo.rubrica">
          </div>
          <div id="ipa-input" class="col-md-3" display="none">
            <input type="text" id="ipaAutocomplete" placeholder="Amministrazione, AOO o Ufficio" class="form-control">
            <input type="hidden" id="ipaAutocomplete-id" name="protocollo.ipa">
          </div>
          <div class="col-md-2">
            <button id="btnSelectContacts" type="button" class="btn btn-info btn-block" disabled="disabled">Seleziona</button>
          </div>
          <div class="row">
            <div class="col-xs-12">&nbsp;</div>
          </div>
          <div id="mailDest" class="col-md-6" style="display: none;">
            <span class="small pull-right">
              <i class="glyphicon glyphicon-envelope"></i><a href=""></a>
            </span>
          </div>
        </div>
      </div>
      <div style="background-color: #e0e0e0;" class="panel-body">
        <div id="rubricaIpaNoElement" class="text-center">Non ci sono elementi nella lista</div>
        <div class="panel panel-default" id="rubricaIpaSelezionato" style="display:none;">
          <ul id="rubricaIpaUl" class="list-group">
          </ul>
        </div>
      </div>
    </div>

    <div id="protocolloMittente" class="panel panel-default" style="display:none;">
      <div style="height:50px;" class="panel-heading">
        <div id="protocolloMittenteTitle" class="col-md-12"><b>Protocollo Mittente</b>
          <span>
            <button id="btnAddProtocolloMittente" style="margin-top: -2px;" type="button" class="btn btn-info btn-xs" data-toggle="modal" data-target="#protocolloMittenteModal">Aggiungi</button>
          </span>
        </div>
      </div>
      <div style="background-color: #e0e0e0;" class="panel-body">
        <div id="protocolloMittenteNoElement" class="text-center">Non ci sono elementi nella lista</div>
        <div class="panel panel-default" id="protocolloMittenteSelezionato" style="display:none;">
          <ul id="protocolloMittenteUl" class="list-group">
          </ul>
        </div>
      </div>
    </div>

    <div id="mittente" class="panel panel-default">
      <div class="panel-heading">
        <div class="col-md-12"><b>Mittente</b></div>
        <hr>
        <div class="panel-title row">
          <div class="col-md-3">
            <input type="text" id="mittentiAutocomplete" placeholder="Unita' Organizzativa" class="form-control">
            <input type="hidden" id="mittentiAutocomplete-id" name="protocollo.mittente">
          </div>
          <div class="col-md-3">
            <input type="text" id="mittentiPersonaAutocomplete" placeholder="Utente" class="form-control" disabled="disabled">
            <input type="hidden" id="mittentiPersonaAutocomplete-id" name="protocollo.mittenteUtente">
          </div>
          <div class="col-md-4">
            <select class="form-control" name="protocollo.mittenteEmail" id="selectEmailUser">
              <option value=""></option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <div id="destinatario" class="panel panel-default">
      <div class="panel-heading">
        <div class="col-md-12"><b>Destinatari</b></div>
        <hr>
        <div class="panel-title row">
          <div class="col-md-6">
            <input type="text" id="destinatariAutocomplete" placeholder="Unita' Organizzativa" class="form-control">
            <input type="hidden" id="destinatariAutocomplete-id" name="protocollo.destinatario">
          </div>
          <div class='col-md-4'>
            <select class="form-control" name="protocollo.destinatariUtenti" id="destinatariPersone-id" multiple="multiple">
            </select>
          </div>
          <div class="col-md-2">
            <button id="btnSelectDestinatario" type="button" class="btn btn-info btn-block" disabled="disabled">Seleziona</button>
          </div>
        </div>
      </div>
      <div style="background-color: #e0e0e0;" class="panel-body">
        <div id="destinatariNoElement" class="text-center">Non ci sono elementi nella lista</div>
        <div class="panel panel-default" id="destinatariSelezionato" style="display:none;">
          <ul id="destinatariUl" class="list-group">
          </ul>
        </div>
      </div>
    </div>

    <#if documento.getProperty('FIRMATARIO')??>
    <div class="row-fluid">
      <div class="col-xs-3">
        <label>Firma Digitale</label>
        <div class="btn-group" role="group" aria-label="...">
        <#if documento.getProperty('FIRMATARIO')=="<Firmatario />">
          <div class="btn-group" role="group">
            <button style="cursor:auto" type="button" class="btn btn-xs btn-primary">&nbsp;NO&nbsp;</button>
          </div>
          <div class="btn-group" role="group">
            <button style="cursor:auto" type="button" disabled="disabled" class="btn btn-xs btn-default">&nbsp;SI&nbsp;</button>
          </div>
        <#else>
          <div class="btn-group" role="group">
            <button style="cursor:auto" type="button" disabled="disabled" class="btn btn-xs btn-default">&nbsp;NO&nbsp;</button>
          </div>
          <div class="btn-group" role="group">
            <button style="cursor:auto" type="button" class="btn btn-xs btn-primary">&nbsp;SI&nbsp;</button>
          </div>
        </#if>
        </div>
      </div>
      <div class="col-xs-2">
      <#if documento.getProperty('FIRMATARIO')=="<Firmatario />">
        <a type="button" class="btn btn-xs btn-primary" disabled="disabled">&nbsp;Verifica Firme&nbsp;</a>
      <#else>
        <a type="button" class="btn btn-xs btn-primary verificaFirmaDivClass" href="/AppDoc/verificaFirmaDocumenti?docNum=${jsonData.docNum}">&nbsp;Verifica Firme&nbsp;</a>
      </#if>
      </div>
    </div>
    <#else>
    <div class="row-fluid">
      <div class="col-xs-3">
        <label>Firma Digitale</label>
        <div class="btn-group" role="group" aria-label="...">
          <div class="btn-group" role="group">
            <button style="cursor:auto" type="button" class="btn btn-xs btn-primary">&nbsp;NO&nbsp;</button>
          </div>
          <div class="btn-group" role="group">
            <button style="cursor:auto" type="button" disabled="disabled" class="btn btn-xs btn-default">&nbsp;SI&nbsp;</button>
          </div>
        </div>
      </div>
      <div class="col-xs-2">
        <a type="button" class="btn btn-xs btn-primary" disabled="disabled">&nbsp;Verifica Firme&nbsp;</a>
      </div>
    </div>
    </#if>
    <div class="row">
      <div class="col-xs-12">&nbsp;</div>
    </div>
    <div class="row">
      <div class="col-xs-12">&nbsp;</div>
    </div>
    <button type="button" id="salvaProtocollo" class="btn btn-primary btn-small">Salva</button>
    <button type="button" id="checkRaccomandata" class="btn btn-primary btn-small" disabled>Verifica dati Raccomandata</button>
    <button type="button" id="protocolla" class="btn btn-primary btn-small" disabled>Protocolla</button>
    <button type="button" id="inoltraProtocollo" class="btn btn-primary btn-small">Inoltra a Protocollo</button>
    <button style="display: none;" type="button" id="aggiornaProtocollo" class="btn btn-primary btn-small">Aggiorna</button>
    <button style="display: none;" type="button" id="annullaProtocollo" class="btn btn-danger btn-small" data-toggle="modal" data-target="#annullaProto">Annulla Protocollazione</button>
    <button type="button" id="form_annulla" class="btn btn-dafault btn-small">Annulla</button>
    <br><br>
    <div id="warningNearSubmit" class="bs-example warning" style="display:none;">
      <div class="alert alert-danger div-alert-warning"></div>
    </div>
  </div>
</form>

<!-- Modal verso-->
<div class="modal fade" id="versoDocumento" tabindex="-1" role="dialog" aria-labelledby="versoDocumentoLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" id="versoDocumentoLabel"></h4>
      </div>
      <div class="modal-body">
        <div class="alert alert-warning">
          <span>Cambiando il verso del documento si perderanno gli elementi inseriti per il verso attuale. Cambiare verso del documento?</span>
        </div>
      </div>
      <div class="modal-footer">
        <button id="modalOkVerso" type="button" class="btn btn-primary" data-dismiss="modal">OK</button>
        <button id="modalAnnullaVerso" type="button" class="btn btn-primary" data-dismiss="modal">Annulla</button>
      </div>
    </div>
  </div>
</div>
<!-- fine Modal verso-->

<!-- Modal stampigliatura-->
<div class="modal fade" id="stampigliatura" tabindex="-1" role="dialog" aria-labelledby="stampigliaturaLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h4 style="float: left;" class="modal-title" id="stampigliaturaLabel">Stampigliatura</h4>
      </div>
      <div class="modal-body">
        <div class="row">&nbsp;</div>
        <div class="col-xs-7">
          <div class="col-xs-12 checkbox">
            <label><input type="checkbox" id="allPages" value="false">Applica a tutte le pagine</label>
          </div>
          <div class="col-xs-12 checkbox">
            <label><input type="checkbox" id="newVersionDoc" value="open">Crea nuova versione documento</label>
          </div>
        </div>
        <div class="col-xs-5">
          <label class="control-label">Posizione</label>
          <div class="">
            <select id="posizioneStampigliatura" class="form-control"  >
              <option value="alto">In alto</option>
              <option value="basso">In basso</option>
              <option value="sinistra">Sinistra</option>
              <option value="destra">Destra</option>
            </select>
          </div>
        </div>
        <div class="row">&nbsp;</div>
      </div>
      <div class="modal-footer">
          <button id="modalOkStampigliatura" type="button" class="btn btn-primary" data-dismiss="modal">OK</button>
          <button id="modalAnnullaStampigliatura" type="button" class="btn btn-primary" data-dismiss="modal">Annulla</button>
      </div>
    </div>
  </div>
</div>
<!-- fine Modal stampigliatura-->

<!-- Modal annullaProtocollo-->
<div class="modal fade" id="annullaProto" tabindex="-1" role="dialog" aria-labelledby="annullaProtoLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h4 style="float: left;" class="modal-title" id="annullaProtoLabel">Conferma annullamento</h4>
      </div>
      <div class="modal-body">
        <div class="col-xs-12">
          <p>Inserire la motivazione di annullamento</p>
          <textarea class="col-xs-12 form-control" id="motivoAnnullamento"></textarea>
        </div>
        <div class="row">&nbsp;</div>
      </div>
      <div class="modal-footer">
        <button id="modalAnnullaAnnullamento" type="button" class="btn btn-default" data-dismiss="modal">Chiudi</button>
        <button id="modalOkAnnullamento" type="button" class="btn btn-primary" data-dismiss="modal">Conferma</button>
      </div>
    </div>
  </div>
</div>
<!-- fine Modal annullaProtocollo-->

<!-- Modal save-->
<div class="modal fade" id="successSave" tabindex="-1" role="dialog" aria-labelledby="successSaveLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" id="successSaveLabel"></h4>
      </div>
      <div class="modal-body">
        <div id="alertModalMessage" class="alert">
          <span class="modalMessage"></span>
        </div>
      </div>
      <div class="modal-footer">
        <button id="modalOkSave" type="button" class="btn btn-primary" data-dismiss="modal">OK</button>
      </div>
    </div>
  </div>
</div>
<!-- fine Modal save-->

<!-- Modal pulisci-->
<div class="modal fade" id="alertClearModal" tabindex="-1" role="dialog" aria-labelledby="alertClearModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" id="alertClearModalLabel"></h4>
      </div>
      <div class="modal-body">
        <div id="alertClearModalMessage" class="alert">
          <span class="clearModalMessage"></span>
        </div>
      </div>
      <div class="modal-footer">
        <button id="modalOkClear" type="button" class="btn btn-primary" data-dismiss="modal">OK</button>
      </div>
    </div>
  </div>
</div>
<!-- fine Modal pulisci-->

<!-- Modal annullamento parziale-->
<div class="modal fade" id="alertAggiornaModal" tabindex="-1" role="dialog" aria-labelledby="alertAggiornaModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" id="alertAggiornaModalLabel">Richiesta conferma</h4>
      </div>
      <div class="modal-body">
        <div id="alertAggiornaModalMessage" class="alert">
          <span class="aggiornaModalMessage"></span>
        </div>
      </div>
      <div class="modal-footer">
        <button id="modalOkAggiorna" type="button" class="btn btn-primary" data-dismiss="modal">Si</button>
        <button id="modalNoAggiorna" type="button" class="btn btn-primary" data-dismiss="modal">No</button>
      </div>
    </div>
  </div>
</div>
<!-- fine Modal annullamento parziale-->

<!-- Modal protocollo mittente-->
<div class="modal fade" id="protocolloMittenteModal" tabindex="-1" role="dialog" aria-labelledby="protocolloMittenteModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-body">
        <div id="modal-numPg" class="form-group">
          <label>*Num PG</label>
          <input id="modal-numPg-input" type="text" class="form-control col-md-12">
        </div>
        <div id="modal-dataPg" class="form-group">
          <label>*Data Pg</label>
          <input id="modal-dataPg-input" type="text" class="form-control col-md-12">
        </div>
        <div id="modal-classificaPg" class="form-group">
          <label>Classifica</label>
          <input id="modal-classificaPg-input" type="text" class="form-control col-md-12">
        </div>
        <div id="modal-fascicoloPg" class="form-group">
          <label>Fascicolo</label>
          <input id="modal-fascicoloPg-input" type="text" class="form-control col-md-12">
        </div>
        <input type="hidden" id="modal-progressivoPm-input">
      </div>
      <br><br>
      <div class="modal-footer">
        <button id="btnAnnullaPg" type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
        <button id="btnSelezionaPg" type="button" class="btn btn-primary" disabled>Seleziona</button>
      </div>
    </div>
  </div>
</div>
<!-- fine Modal protocollo mittente-->

<!-- Modal rubrica-->
<div class="modal fade" id="rubrica" tabindex="-1" role="dialog" aria-labelledby="rubricaLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <div class="btn-group btn-group-justified" role="group" aria-label="...">
          <div class="btn-group" role="group">
            <button id="btnContactsPF" type="button" class="btn btn-default btn-primary">Persona Fisica</button>
          </div>
          <div class="btn-group" role="group">
            <button id="btnContactsPG" type="button" class="btn btn-default">Persona Giuridica</button>
          </div>
          <div class="btn-group" role="group">
            <button id="btnContactsPA" type="button" class="btn btn-default">Pubblica Amministrazione</button>
          </div>
        </div>
      </div>
      <div class="modal-body">
        <div id="warningNearModal" class="bs-example warning" style="display:none;">
          <div class="alert alert-danger div-alert-warning"></div>
        </div>
        <div id="successNearModal" class="bs-example success" style="display:none;">
          <div class="alert alert-success div-alert-success"></div>
        </div>
        <div style="display:none;" id="modal-amministrazione" class="form-group">
          <label>*Amministrazione</label>
          <input id="modal-amministrazione-input" type="text" class="form-control col-md-12 ammCheck">
        </div>
        <div style="display:none;" id="modal-aooUfficio" class="form-group">
          <label>*AOO o Ufficio</label>
          <input id="modal-aooUfficio-input" type="text" class="form-control col-md-12 aooCheck">
        </div>
        <div id="modal-denominazione" class="form-group">
          <label>*Denominazione (es. Nome, Cognome)</label>
          <input id="modal-denominazione-input" type="text" class="form-control col-md-12 denomCheck">
        </div>
        <div style="display:none;" id="modal-piva" class="form-group">
          <label>Partita IVA</label>&emsp;<label><input type="checkbox" id="pivaStraniera"> Straniera</label>
          <input id="modal-piva-input" type="text" class="form-control col-md-12 pivaCheck">
        </div>
        <div id="modal-cf" class="form-group">
          <label>Codice Fiscale</label>
          <input id="modal-cf-input" type="text" class="form-control col-md-12 cfCheck">
        </div>
        <div id="modal-indirizzoPostale" class="form-group">
          <label>Indirizzo Postale (es. Piazza Venezia, 1, 00186 Roma, RM)</label>
          <input id="modal-indirizzoPostale-input" type="text" class="form-control col-md-12 indirizzoPostaleCheck">
        </div>
        <div id="modal-pec" class="form-group">
          <label>Indirizzo PEC</label>
          <input id="modal-pec-input" type="text" class="form-control col-md-12 emailCheck">
        </div>
        <div id="modal-peo" class="form-group">
          <label>Indirizzo PEO</label>
          <input id="modal-peo-input" type="text" class="form-control col-md-12 emailCheck">
        </div>
        <div id="modal-fax" class="form-group">
          <label>FAX</label>
          <input id="modal-fax-input" type="number" class="form-control col-md-12">
        </div>
        <input type="hidden" id="modal-tipologia-input" name="protocollo.tipologia">
        <input type="hidden" id="modal-progressivo-input">
      </div>
      <br><br>
      <div class="modal-footer">
        <button id="btnAnnullaRubrica" type="button" class="btn btn-default" data-dismiss="modal">Annulla</button>
        <button id="btnSelezionaRubrica" type="button" class="btn btn-primary" disabled>Seleziona</button>
        <button id="btnSalvaRubrica" type="button" class="btn btn-primary" disabled>Salva in Rubrica</button>
      </div>
    </div>
  </div>
</div>
<!-- fine Modal rubrica-->

<!-- verifica firma -->
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-front ui-draggable ui-resizable" tabindex="-1" role="dialog" aria-describedby="verificaFirmaDiv" aria-labelledby="ui-id-1" style="position: absolute; height: auto; width: 900px; top: 872px; left: 124.5px; display: none; z-index: 1000;">
  <div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><span id="ui-id-1" class="ui-dialog-title">Verifica firma</span>
    <button type="button" class="ui-dialog-titlebar-close">x</button>
  </div>
  <div id="verificaFirmaDiv" style="width: auto; min-height: 0px; max-height: none; height: 386px;" class="ui-dialog-content ui-widget-content"></div>
  <div class="ui-resizable-handle ui-resizable-n" style="z-index: 90;"></div>
  <div class="ui-resizable-handle ui-resizable-e" style="z-index: 90;"></div>
  <div class="ui-resizable-handle ui-resizable-s" style="z-index: 90;"></div>
  <div class="ui-resizable-handle ui-resizable-w" style="z-index: 90;"></div>
  <div class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se" style="z-index: 90;"></div>
  <div class="ui-resizable-handle ui-resizable-sw" style="z-index: 90;"></div>
  <div class="ui-resizable-handle ui-resizable-ne" style="z-index: 90;"></div>
  <div class="ui-resizable-handle ui-resizable-nw" style="z-index: 90;"></div>
</div>

<!-- lista IPA -->
<li id="ipaTemplate" class="list-group-item row-fluid clearfix hidden" style="border-left: 2px solid #02918D;">
  <div class="row col-xs-9 p-default">
    <div class="row-fluid">
      <div class="col-xs-7">
        <span style="color:blue" class="list-group-item-heading rubricaIpaSelezionatoResult"></span>
        <input class="rubricaIpaSelezionatoResultInput itemId" type="hidden">
        <br>
        <span style="font-size: 13px; margin-left: 18px;" class="list-group-item-text rubricaIpaSelezionatoResultName"></span>
        <input class="rubricaIpaSelezionatoResultNameInput" type="hidden">
        <br>
        <span style="color: #888888; font-style: italic; font-size: 13px; margin-left: 16px;" class="list-group-item-heading rubricaIpaSelezionatoResultIndirizzoPostale"></span>
        <input class="rubricaIpaSelezionatoResultIndirizzoPostaleInput" type="hidden">
      </div>
      <div class="col-xs-5">
        <span style="font-size: 13px;" class="list-group-item-text pull-right rubricaIpaSelezionatoResultPec"></span>
        <input class="rubricaIpaSelezionatoResultPecInput" type="hidden">
        <span style="font-size: 13px;" class="list-group-item-text pull-right rubricaIpaSelezionatoResultPeo"></span>
        <input class="rubricaIpaSelezionatoResultPeoInput" type="hidden">
      </div>
      <div class="col-xs-2">
        <input class="rubricaIpaSelezionatoResultIdInput" type="hidden">
        <input class="rubricaIpaSelezionatoResultTipologiaInput" type="hidden">
        <input class="rubricaIpaSelezionatoResultDenominazioneInput" type="hidden">
        <input class="rubricaIpaSelezionatoResultFaxInput" type="hidden">
      </div>
    </div>
  </div>
  <div class="col-xs-2">
    <select style="font-size: 13px;" class="form-control rubricaIpaSelezionatoResultOption">
      <option value="">-- mezzo --</option>
    </select>
  </div>
  <div class="col-xs-1 pull-right">
    <button class="btn btn-danger pull-right rubricaIpaSelezionatoResultRemove"><i class="glyphicon glyphicon-remove"></i></button>
  </div>
</li>

<!-- lista protocollo Mittente -->
<li id="protocolloMittenteTemplate" class="list-group-item row-fluid clearfix hidden" style="border-left: 2px solid #02918D;">
  <div class="row col-xs-9 p-default">
    <div class="row-fluid">
      <div class="col-xs-12">
        <span style="color:blue" class="list-group-item-heading protocolloMittenteSelezionatoResult"></span>
        <input class="protocolloMittenteSelezionatoResultInput" type="hidden">
        <span class="list-group-item-text protocolloMittenteSelezionatoResultData"></span>
        <input class="protocolloMittenteSelezionatoResultDataInput" type="hidden">
      </div>
      <div class="col-xs-10">
        <span class="list-group-item-text protocolloMittenteSelezionatoResultName"></span>
        <input class="protocolloMittenteSelezionatoResultNameInput" type="hidden">
      </div>
      <div class="col-xs-2">
        <input class="protocolloMittenteSelezionatoResultIdInput" type="hidden">
        <input class="protocolloMittenteSelezionatoResultClassificaInput" type="hidden">
        <input class="protocolloMittenteSelezionatoResultFascicoloInput" type="hidden">
      </div>
    </div>
  </div>
  <div class="col-xs-1 pull-right">
    <button class="btn btn-danger pull-right protocolloMittenteSelezionatoResultRemove"><i class="glyphicon glyphicon-remove"></i></button>
  </div>
</li>

<!-- lista destinatari -->
<li id="destinatariTemplate" class="list-group-item row-fluid clearfix hidden" style="border-left: 2px solid #02918D;">
  <div class="row col-xs-9 p-default">
    <div class="row-fluid">
      <div class="col-xs-10">
        <span style="color:blue" class="list-group-item-heading destinatariSelezionatoResult"></span>
        <input class="destinatariSelezionatoResultInput" type="hidden">
      </div>
      <div class="col-xs-10">
        <span class="list-group-item-text destinatariSelezionatoResultName"></span>
        <input class="destinatariSelezionatoResultNameInput" type="hidden">
      </div>
      <div class="col-xs-2">
        <input class="destinatariSelezionatoResultIdInput" type="hidden">
      </div>
    </div>
  </div>
  <div class="col-xs-1 pull-right">
    <button class="btn btn-danger pull-right destinatariSelezionatoResultRemove"><i class="glyphicon glyphicon-remove"></i></button>
  </div>
</li>

<script type="text/javascript" src="/static/protocollo/js/bootstrap-datepicker-proto.min.js"></script>
<script type="text/javascript" src="/static/protocollo/js/bootstrap-datepicker.it.min.js"></script>
<script type="text/javascript" src="/static/protocollo/js/wickedpicker.js"></script>
<script type="text/javascript" src="/static/protocollo/js/protocollo.js"></script>
<script type="text/javascript" src="/static/protocollo/js/wait.js"></script>
<script type="text/javascript" src="/static/protocollo/js/bootstrap-multiselect.js"></script>
<script type="text/javascript" src="/static/protocollo/js/openfile.js"></script>

  <#assign documentoPrincipale=documento>
  
  <#assign docOgg=documentoPrincipale.OGGETTO_PROTOCOLLAZIONE!"">
    <#if docOgg?has_content>
      <#assign docOgg=docOgg?js_string>
    <#else>
      <#assign docOgg="">
    </#if>

  <#assign docAbstract=documentoPrincipale.ABSTRACT!"">
    <#if docAbstract?has_content>
      <#assign docAbstract=docAbstract?js_string>
    <#else>
      <#assign docAbstract="">
    </#if>
   
  <#assign docOggPG=documentoPrincipale.OGGETTO_PG!"">
    <#if docOggPG?has_content>
      <#assign  docOggPG=docOggPG?js_string>
    <#else>
      <#assign docOggPG="">
    </#if>

<script>
  var docnumVerifica = "${documentoPrincipale.DOCNUM!""}";
  var codAooVerifica = "${documentoPrincipale.COD_AOO!""}";
  var dataOra = "${documentoPrincipale.CREATED!""}";
  var dataDocumento = "${documentoPrincipale.DATA_DOCUMENTO!""}";
  var tipoFirma = "${documentoPrincipale.TIPO_FIRMA!""}";
  var riservatezza = "${documentoPrincipale.RISERVATEZZA!""}";
  var classifica = "${documentoPrincipale.CLASSIFICA!""}";
  var desTitolario_DOC = "${jsonData.classifica.get('name')!""}";
  var oggettoProtocollazione_DOC = "${docOgg}";
  var note_DOC =  "${docAbstract}";
  var numPg = "${documentoPrincipale.NUM_PG!""}";
  var dataPg = "${documentoPrincipale.DATA_PG!""}";
  var numPgEmergenza_DOC = "${documentoPrincipale.NUM_PG_EMERGENZA!""}";
  var dataPgEmergenza_DOC = "${documentoPrincipale.DATA_PG_EMERGENZA!""}";
  var tipoProtocollazione_DOC = "${documentoPrincipale.TIPO_PROTOCOLLAZIONE!""}";
  var numPgMittente_DOC = "${documentoPrincipale.NUM_PG_MITTENTE!""}";
  var dataPgMittente_DOC = "${documentoPrincipale.DATA_PG_MITTENTE!""}";
  var fascicoloMittente_DOC = "${documentoPrincipale.FASCICOLO_MITTENTE!""}";
  var classificaMittente_DOC = "${documentoPrincipale.CLASSIFICA_MITTENTE!""}";
  var archiveType = "${documentoPrincipale.ARCHIVE_TYPE!""}";
  var docname = "${documentoPrincipale.DOCNAME!""}";
  var statoArchivistico = "${documentoPrincipale.STATO_ARCHIVISTICO!""}";
  var statoBusiness = ${isEnabledInoltro?c};
  var utenteLoggato = "${user!""}";
  var estensioniSupportate = "html,txt";
  if (numPg!=""){
      if ("${docOggPG}"!=""){
         oggettoProtocollazione_DOC="${docOggPG}";
      }
      if (dataDocumento==""){
         dataDocumento= "${documentoPrincipale.CREATED!""}";
      }
  }


  var hash = "${documentoPrincipale.FILE_HASH!""}";
  var content_type = "${documentoPrincipale.content_type!""}";
  var ragioneSocialeEnte = "${documentoPrincipale.propRagioneSocialeEnte!""}";
  var indirizzoENTE = "${documentoPrincipale.propIndirizzoCompletoEnte!""}";
  var urlRaccomandataOnline = "${documentoPrincipale.propurRESTService!""}";


  var listaMittentiRecuperati = [];
  var singoloMittente = {};
  <#list documentoPrincipale.MITTENTI as mittente>
    singoloMittente = {};
    <#list mittente?keys as prop>
      <#if prop!="UO" && prop!="AOO">
        singoloMittente["${prop}"] = "${mittente[prop]!""}";
      <#elseif prop=="UO">
        singoloMittente.UO = {};
        singoloMittente.UO.indirizzoPostale = "${mittente[prop]["indirizzoPostale"]!""}";
        singoloMittente.UO.IndirizzoTelematico = "${mittente[prop]["IndirizzoTelematico"]!""}";
        singoloMittente.UO.COD_UO = "${mittente[prop]["COD_UO"]!""}";
        singoloMittente.UO.tipo = "${mittente[prop]["tipo"]!""}";
        singoloMittente.UO.DES_UO = "${mittente[prop]["DES_UO"]?js_string!""}";
      <#elseif prop=="AOO">
        singoloMittente.AOO = {};
        singoloMittente.AOO.COD_AOO = "${mittente[prop]["COD_AOO"]!""}";
        singoloMittente.AOO.DES_AOO = "${mittente[prop]["DES_AOO"]?js_string!""}";
        singoloMittente.AOO.indirizzoTelematico = "${mittente[prop]["indirizzoTelematico"]!""}";
      </#if>
    </#list>
    listaMittentiRecuperati.push(singoloMittente);
  </#list>

  var listaDestinatariRecuperati = [];
  var singoloDestinatario = {};
  <#list documentoPrincipale.DESTINATARI as destinatario>
    singoloDestinatario = {};
    <#list destinatario?keys as prop>
      <#if prop!="UO" && prop!="AOO">
        singoloDestinatario["${prop}"] = "${destinatario[prop]!""}";
      <#elseif prop=="UO">
        singoloDestinatario.UO = {};
        singoloDestinatario.UO.indirizzoPostale = "${destinatario[prop]["indirizzoPostale"]!""}";
        singoloDestinatario.UO.IndirizzoTelematico = "${destinatario[prop]["IndirizzoTelematico"]!""}";
        singoloDestinatario.UO.COD_UO = "${destinatario[prop]["COD_UO"]!""}";
        singoloDestinatario.UO.tipo = "${destinatario[prop]["tipo"]!""}";
        singoloDestinatario.UO.DES_UO = "${destinatario[prop]["DES_UO"]?js_string!""}";
      <#elseif prop=="AOO">
        singoloDestinatario.AOO = {};
        singoloDestinatario.AOO.COD_AOO = "${destinatario[prop]["COD_AOO"]!""}";
        singoloDestinatario.AOO.DES_AOO = "${destinatario[prop]["DES_AOO"]?js_string!""}";
        singoloDestinatario.AOO.indirizzoTelematico = "${destinatario[prop]["indirizzoTelematico"]!""}";
      </#if>
    </#list>
    listaDestinatariRecuperati.push(singoloDestinatario);
  </#list>

  documentoJava = {};
  <#list documento?keys as prop>
    <#if prop!='mittenti' && prop!='destinatari'>
      documentoJava["${prop}"] = "${(documento[prop]?js_string)!}";
    </#if>
  </#list>

</script>
