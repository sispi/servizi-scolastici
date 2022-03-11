
$(document).ready(function () {

    $("#protoForm").submit(function(e){
        e.preventDefault();
    });

    //gestione arrivo da pagina creazione UD
    
    let url_string = window.location.href;
    var url = new URL(url_string);
    var paramFromProtoUd = url.searchParams.get("verso");
    if(paramFromProtoUd=="E"){
        showEntrataInfo();
    }
    if(paramFromProtoUd=="I"){
        showInternaInfo();
    }
    if(paramFromProtoUd=="U"){
        showUscitaInfo();
    }
    if(!paramFromProtoUd){
        //nel caso in cui non sia settato il tipo di protocollazione presento la pagina su entrata
        if (!tipoProtocollazione_DOC) {
            showEntrataInfo();
        }
    }

    //se il documento è protocollato e in uscita
    if (numPg && tipoProtocollazione_DOC == "U") {
        $("#mailDest").show();
    }
    //se il documento è protocollato
    if (numPg) {

        $('#inviaMailProtocollo').show();
        popolateSelectMittente();
        $('#modal-docnum-input').val(docnumVerifica);
        $('#modal-codaoo-input').val(currentCodiceAOO);
        $('#modal-codente-input').val(currentCodiceAmm);
        $('#checkbox-allegati').prop('checked', true);
        var ckboxMittente = $('#checkbox-allegati');
        $('input').on('click', function () {
            if (ckboxMittente.is(':checked')) {
                $('#checkbox-allegati').val("true");
            } else {
                $('#checkbox-allegati').val("false");
            }
        });

        $('#riservatezza-id').prop("disabled", true);
        $('#creaEtichetta').show();
        $('#entrata').prop("disabled", true);
        $('#interna').prop("disabled", true);
        $('#uscita').prop("disabled", true);
        $('#salvaDati').hide();
        $('#protocolla').hide();
        $('#inoltraProtocollo').hide();
        $('#aggiornaProtocollo').show();
        $('#annullaProtocollo').show();
        $('#data_pg').html(getDataOraToIso(dataPg));

        if (archiveType=="PAPER") {
            $('#allPages').attr("disabled", true);
        }
        if (statoArchivistico=="4") {
            $('#newVersionDoc').attr("disabled", true);
        }

        var ckboxStampigliatura = $('#allPages');
        $('input').on('click', function () {
            if (ckboxStampigliatura.is(':checked')) {
                $("#allPages").val("true");
            } else {
                $("#allPages").val("false");
            }
        });
        var ckboxNewVersion = $('#newVersionDoc');
        $('input').on('click', function () {
            if (ckboxNewVersion.is(':checked')) {
                $("#newVersionDoc").val("apply");
            } else {
                $("#newVersionDoc").val("open");
            }
        });

    }
    //fine se il documento è protocollato


    $('#destinatariPersone-id').hide();

    if (tipoProtocollazione_DOC == "E") { //gestione elementi da recuperare e visualizzare a seconda del tipo di protocollazione
        showEntrataInfo();
        //recupero info protocollo mittente
        if (dataPgMittente_DOC) {
            $('#protocolloMittente').show();
            $("#protocolloMittenteNoElement").html("");
            btnSelectNewProtocolloMittenteClick();
            listaProtocolloMittenteSelezionato.push(parseItemProtocolloMittente());
            dataPgMittente_DOC = "";//setto vuota la variabile così da poter gestire gli if in btnSelectNewProtocolloMittenteClick e in parseItemProtocolloMittente
        }
        if (listaMittentiRecuperati!=null){
            for(i=0;i<listaMittentiRecuperati.length;i++) {
                var utente = listaMittentiRecuperati[i];
                if(utente!=null){
                    selectMittentiRecuperati(utente);
                }
            }
        }
        if (listaDestinatariRecuperati!=null) {
            for(i=0;i<listaDestinatariRecuperati.length;i++) {
                var utente = listaDestinatariRecuperati[i];
                if(utente!=null){
                    selectDestinatariRecuperati(utente);
                }
            }
        }
    }
    if (tipoProtocollazione_DOC == "I") {
        showInternaInfo();
        if (listaMittentiRecuperati!=null){
            for(i=0;i<listaMittentiRecuperati.length;i++) {
                var utente = listaMittentiRecuperati[i];
                if(utente!=null){
                    addDestinatarioPersonaRecuperato(utente);
                }
            }
        }
        if (listaDestinatariRecuperati!=null) {
            for(i=0;i<listaDestinatariRecuperati.length;i++) {
                var utente = listaDestinatariRecuperati[i];
                if(utente!=null){
                    selectDestinatariRecuperati(utente);
                }
            }
        }
    }
    if (tipoProtocollazione_DOC == "U") {
        showUscitaInfo();
        if (listaMittentiRecuperati!=null){
            for(i=0;i<listaMittentiRecuperati.length;i++) {
                var utente = listaMittentiRecuperati[i];
                if(utente!=null){
                    addDestinatarioPersonaRecuperato(utente);
                }
            }
        }
        if (listaDestinatariRecuperati!=null) {
            for(i=0;i<listaDestinatariRecuperati.length;i++) {
                var utente = listaDestinatariRecuperati[i];
                if(utente!=null){
                    selectMittentiRecuperati(utente);//attenzione, in realtà in questo caso sono destinatari
                }
            }
        }
    }

    
    if($('#aggiornaProtocollo').is(':visible')){ //richiamo la funzione solo se in aggiornamento
        recuperaIdListaIniziale();
    }

    if (classifica) {
        getDescrizioneClassifica();
    }

    if (riservatezza) {
        $('#riservatezza-id').val(riservatezza);
    }
    $('#oggetto-id').val(oggettoProtocollazione_DOC);
    $('#note-id').val(note_DOC);
    $('#num_pg_emergenza-id').val(numPgEmergenza_DOC);
    if (dataPgEmergenza_DOC) {
        $('#data_pg_emergenza-id').val(getDataToIso(dataPgEmergenza_DOC));
        $('#ora_pg_emergenza-id').val(getOraToIso(dataPgEmergenza_DOC));
    } else {
        $('#data_pg_emergenza-id').val("");
        $('#ora_pg_emergenza-id').val("");
    }
    if (dataPg) {
        $('#data_proto-id').val(getDataToIso(dataPg));
        $('#ora_proto-id').val(getOraToIso(dataPg));
    } else {
        let today = new Date().toISOString().slice(0, 10);
        $('#data_proto-id').val(today);
        let time = new Date().toString().substr(16,8);
        $('#ora_proto-id').val(time);
    }

    popolateSelect();


    if ($('#ora_pg_emergenza-id').val()) {
        $("#registroEmergenzaContent").show();
        $("#checkbox1").attr("checked", "checked");
    } else {
        $("#registroEmergenzaContent").hide();
    }

    // azzero i campi se il check Registro Emergenza non è spuntato
    var ckbox = $('#checkbox1');
    $('input').on('click', function () {
        if (ckbox.is(':checked')) {

        } else {
            $("#data_pg_emergenza-id").val("");
            $("#ora_pg_emergenza-id").val("");
            $("#num_pg_emergenza-id").val("");
        }
    });

    $('#entrata, #interna, #uscita').click(function () {
        if (this.id == 'entrata') {
            $('#modalOkVerso').click(function () {
                showEntrataInfo();
                clearModalInfo();
                resetListaMittenti();
            });
        }
        if (this.id == 'interna') {
            $('#modalOkVerso').click(function () {
                showInternaInfo();
                clearModalInfo();
                resetListaMittenti();
            });
        }
        if (this.id == 'uscita') {
            $('#modalOkVerso').click(function () {
                showUscitaInfo();
                clearModalInfo();
                resetListaMittenti();
            });
        }
    });

    $('#selectEmailUser').change(function () {
        if(this.value){
            listaMittenteSelezionato[0].indirizzoTelematico = this.value;
        }else{
            delete listaMittenteSelezionato[0].indirizzoTelematico;
        }
    });

    $('#btnAddContacts').click(function () {

        $('#btnContactsPF').trigger("click");
        $("#modal-cf-input").removeAttr("disabled");
        $("#modal-piva-input").removeAttr("disabled");
        $("#modal-aooUfficio-input").removeAttr("disabled");
        $("#modal-amministrazione-input").removeAttr("disabled");
        $("#modal-indirizzoPostale-input").removeAttr("disabled");
        $("#modal-aooUfficio-input").removeAttr("disabled");
        $("#modal-pec-input").removeAttr("disabled");
        $('#warningNearModal').hide();
        $(".div-alert-warning > p").remove();
        $('.cfCheck').css("color", "grey");
        $('.pivaCheck').css("color", "grey");
        $('.aooCheck').css("color", "grey");
        $('.denomCheck').css("color", "grey");
        $('.indirizzoPostaleCheck').css("color", "grey");
        $('.ammCheck').css("color", "grey");
        $("#btnContactsPF").removeAttr("disabled");
        $("#btnContactsPG").removeAttr("disabled");
        $("#btnContactsPA").removeAttr("disabled");
        $("#modal-progressivo-input").val(""); //setto il progressivo vuoto quando è un nuovo inserimento
        $("#modal-tipologia-input").val("Persona"); //setto la tipologia a PF perchè quando apro il modale il tasto selezionato è Persona Fisica
        $("#modal-codice-input").val("");
        //pulisco i campi del modale eventualmente inseriti
        $("#modal-amministrazione-input, #modal-aooUfficio-input, #modal-denominazione-input, #modal-piva-input, #modal-cf-input, #modal-indirizzoPostale-input, #modal-pec-input, #modal-peo-input, #modal-fax-input").val("");

        $('#warningNearModal').show();
        $('.div-alert-warning').append('<p><b>Attenzione</b> - se il nominativo non viene salvato in Rubrica, verranno recuperati solo i dati necessari alla Protocollazione!</p>');
    });

    $('#btnAddProtocolloMittente').click(function () {
        $("#modal-progressivoPm-input").val(""); //setto il progressivo vuoto quando è un nuovo inserimento
        $("#btnSelezionaPg").attr("disabled", "disabled");
        //pulisco i campi del modale eventualmente inseriti
        $("#modal-numPg-input, #modal-dataPg-input, #modal-classificaPg-input, #modal-fascicoloPg-input").val("");
    });

    $('#btnContactsPF').click(function () {
        $("#modal-tipologia-input").val("Persona");
        $("#btnContactsPF").removeClass("btn-default").addClass("btn-primary");
        $("#btnContactsPG, #btnContactsPA").removeClass("btn-primary").addClass("btn-default");
        $("#label-pec-input").html("Indirizzo PEC");
        //pulisco i campi del modale eventualmente inseriti e disabilito i tasti seleziona e salva rubrica
        $("#modal-amministrazione-input, #modal-aooUfficio-input, #modal-denominazione-input, #modal-piva-input, #modal-cf-input, #modal-indirizzoPostale-input, #modal-pec-input, #modal-peo-input, #modal-fax-input").val("");
        //hide & show rubrica modale
        $("#modal-denominazione, #modal-cf, #modal-indirizzoPostale, #modal-pec, #modal-peo, #modal-fax").show();
        $("#modal-denominazione label").text("*Denominazione");
        $("#modal-piva, #modal-amministrazione, #modal-aooUfficio").hide();
        $('#warningNearModal').hide();
        $(".div-alert-warning > p").remove();
    });

    $('#btnContactsPG').click(function () {
        $("#modal-tipologia-input").val("PersonaGiuridica");
        $("#btnContactsPG").removeClass("btn-default").addClass("btn-primary");
        $("#btnContactsPF, #btnContactsPA").removeClass("btn-primary").addClass("btn-default");
        $("#label-pec-input").html("Indirizzo PEC");
        //pulisco i campi del modale eventualmente inseriti e disabilito i tasti seleziona e salva rubrica
        $("#modal-amministrazione-input, #modal-aooUfficio-input, #modal-denominazione-input, #modal-piva-input, #modal-cf-input, #modal-indirizzoPostale-input, #modal-pec-input, #modal-peo-input, #modal-fax-input").val("");
        //hide & show rubrica modale
        $("#modal-denominazione, #modal-piva, #modal-indirizzoPostale, #modal-pec").show();
        $("#modal-denominazione label").text("*Ragione Sociale");
        $("#modal-cf, #modal-peo, #modal-fax, #modal-amministrazione, #modal-aooUfficio").hide();
        $('#warningNearModal').hide();
        $(".div-alert-warning > p").remove();
    });

    $('#btnContactsPA').click(function () {
        $("#modal-tipologia-input").val("Amministrazione");
        $("#btnContactsPA").removeClass("btn-default").addClass("btn-primary");
        $("#label-pec-input").html("*Indirizzo PEC");
        $("#btnContactsPF, #btnContactsPG").removeClass("btn-primary").addClass("btn-default");
        //pulisco i campi del modale eventualmente inseriti e disabilito i tasti seleziona e salva rubrica
        $("#modal-amministrazione-input, #modal-aooUfficio-input, #modal-denominazione-input, #modal-piva-input, #modal-cf-input, #modal-indirizzoPostale-input, #modal-pec-input, #modal-peo-input, #modal-fax-input").val("");
        //hide & show rubrica modale
        $("#modal-indirizzoPostale, #modal-pec, #modal-amministrazione, #modal-aooUfficio").show();
        $("#modal-denominazione, #modal-cf, #modal-peo, #modal-fax, #modal-piva").hide();
        $('#warningNearModal').hide();
        $(".div-alert-warning > p").remove();
    });

    $('#classificaAutocomplete').blur(function () {
        if (!($('#classificaAutocomplete').val())) {
            $('#classificaAutocomplete-id').val("");
            $('#classificaAutocomplete-descrizione').val("");
        }
    });

    $('#mittentiAutocomplete').blur(function () {
        if (!($('#mittentiAutocomplete').val())) {
            $('#mittentiAutocomplete-id').val("");
            $('#selectEmailUser').val("");
            listaMittenteSelezionato = [];
            listaMittenteSelezionato.push({});
        }
    });
    $('#mittentiAutocomplete').keyup(function () {
        if (!this.value) {
            $('#mittentiAutocomplete').val("");
            $('#mittentiAutocomplete-id').val("");
            $("#mittentiPersonaAutocomplete").attr("disabled", "disabled");
            $("#mittentiPersonaAutocomplete").val("");
            $("#mittentiPersonaAutocomplete-id").val("");
        }
    });

    $('#mittentiPersonaAutocomplete').blur(function () {
        if (!($('#mittentiPersonaAutocomplete').val())) {
            $('#mittentiPersonaAutocomplete-id').val("");
            $('#selectEmailUser').val("");
            listaMittenteSelezionato = [];
            listaMittenteSelezionato.push({});
            /*delete listaMittenteSelezionato[0].persona;
            listaMittenteSelezionato[0].id=listaMittenteSelezionato[0].codiceUO;*/
        }
    });
    $('#mittentiPersonaAutocomplete').keyup(function () {
        if (!this.value) {
            $('#mittentiAutocomplete').val("");
            $('#mittentiAutocomplete-id').val("");
            $("#mittentiPersonaAutocomplete").attr("disabled", "disabled");
            $("#mittentiPersonaAutocomplete").val("");
            $("#mittentiPersonaAutocomplete-id").val("");
        }
    });

    $('#modal-denominazione-input').keyup(function () {
        $('.denomCheck').css("color", "gray");
    });
    $('#modal-amministrazione-input').keyup(function () {
        $('.ammCheck').css("color", "gray");
    });
    $('#modal-indirizzoPostale').keyup(function () {
        $('.indirizzoPostaleCheck').css("color", "gray");
    });
    $('#modal-pec-input, #modal-peo-input').keyup(function () {
        $('.emailCheck').css("color", "gray");
    });
    $('#modal-amministrazione-input').keyup(function () {
        $('.ammCheck').css("color", "gray");
    });
    $('#modal-aooUfficio-input').keyup(function () {
        $('.aooCheck').css("color", "gray");
    });


    $('#modal-numPg-input').keyup(function () {
        if ( ($(this).val()) && !($('#modal-dataPg-input').val()) ){
            $("#btnSelezionaPg").attr("disabled", "disabled");
        }
        if ( ($(this).val()) && $('#modal-dataPg-input').val() ){
            $("#btnSelezionaPg").removeAttr("disabled");
        }
        if ( !($(this).val()) &&  $('#modal-dataPg-input').val() ){
            $("#btnSelezionaPg").attr("disabled", "disabled");
        }
        if ( !($(this).val()) &&  !($('#modal-dataPg-input').val()) ){
            $("#btnSelezionaPg").attr("disabled", "disabled");
        }
    });
    $('#modal-dataPg-input').on("keyup change", function () {
        if ( ($(this).val()) && !($('#modal-numPg-input').val()) ){
            $("#btnSelezionaPg").attr("disabled", "disabled");
        }
        if ( ($(this).val()) && $('#modal-numPg-input').val() ){
            $("#btnSelezionaPg").removeAttr("disabled");
        }
        if ( !($(this).val()) &&  $('#modal-numPg-input').val() ){
            $("#btnSelezionaPg").attr("disabled", "disabled");
        }
        if ( !($(this).val()) &&  !($('#modal-numPg-input').val()) ){
            $("#btnSelezionaPg").attr("disabled", "disabled");
        }
    });


    $('#motivoAnnullamento').keyup(function () {
        if ( ($(this).val()) && !($('#riferimentoAnnullamento').val()) ){
            $("#modalOkAnnullamento").attr("disabled", "disabled");
        }
        if ( ($(this).val()) && $('#riferimentoAnnullamento').val() ){
            $("#modalOkAnnullamento").removeAttr("disabled");
        }
        if ( !($(this).val()) &&  $('#riferimentoAnnullamento').val() ){
            $("#modalOkAnnullamento").attr("disabled", "disabled");
        }
        if ( !($(this).val()) &&  !($('#riferimentoAnnullamento').val()) ){
            $("#modalOkAnnullamento").attr("disabled", "disabled");
        }
    });
    $('#riferimentoAnnullamento').keyup(function () {
        if ( ($(this).val()) && !($('#motivoAnnullamento').val()) ){
            $("#modalOkAnnullamento").attr("disabled", "disabled");
        }
        if ( ($(this).val()) && $('#motivoAnnullamento').val() ){
            $("#modalOkAnnullamento").removeAttr("disabled");
        }
        if ( !($(this).val()) &&  $('#motivoAnnullamento').val() ){
            $("#modalOkAnnullamento").attr("disabled", "disabled");
        }
        if ( !($(this).val()) &&  !($('#motivoAnnullamento').val()) ){
            $("#modalOkAnnullamento").attr("disabled", "disabled");
        }
    });


    $('#destinatariAutocomplete').focusin(function () {
        //if (!this.value) {
        resetMultiOptions();
        //}
    });

    $('#checkbox1').click(function () {
        if (!$(this).is(':checked')) {
            $("#registroEmergenzaContent").hide();
        } else {
            $("#registroEmergenzaContent").show();
        }
    });

    $("#radio1").prop("checked", true);

    $('input:radio[name="gruppo1"]').change(
        function () {
            if (this.checked && this.value == 'rub') {
                $("#btnAddContacts").removeAttr("disabled");
                $("#rubrica-input").show();
                $("#ipa-input").hide();
                $("#ipaAutocomplete").val("");
                $("#ipaAutocomplete-id").val("");
            }
            if (this.checked && this.value == 'ipa') {
                $("#btnAddContacts").attr("disabled", "disabled");
                $("#ipa-input").show();
                $("#rubrica-input").hide();
                $("#rubricaAutocomplete").val("");
                $("#rubricaAutocomplete-id").val("");
            }
        });

    $("#classificaAutocomplete").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "/docer/v1/solr/select?fl=CLASSIFICA,DES_TITOLARIO&sort=classifica_sort%20asc,name%20asc&fq=type:titolario&fq=ENABLED:true",
                data: {
                    q: "name:*" + request.term
                },
                success: function (data) {
                    response(data.data);
                }
            });
        },
        minLength: 1,
        response: function (event, ui) {
            if (ui.content.length > 0) {
                ui.content.forEach(function (singoloElemento) {
                    console.log("singoloElemento:" + singoloElemento);
                    //singoloElemento.name = unescape(singoloElemento.DES_TITOLARIO); //aggiunto per caratteri speciali
                    singoloElemento.value = singoloElemento.CLASSIFICA + " (" + singoloElemento.DES_TITOLARIO + ")";
                    singoloElemento.label = singoloElemento.CLASSIFICA + " (" + singoloElemento.DES_TITOLARIO + ")";
                });
            }
        },
        select: function (event, ui) {
            console.log("select:" + ui);
            $("#classificaAutocomplete-id").val(ui.item.CLASSIFICA);
            $("#classificaAutocomplete-descrizione").val(ui.item.CLASSIFICA + " (" + ui.item.DES_TITOLARIO + ")");
            classificaDetail = ui.item;
            listaClassificaSelezionata = classificaDetail;
        }
    });

    $("#mittentiAutocomplete").autocomplete({
        source: function (request, response) {
            $.ajax({
                //url: "/docer/v1/solr/select?database=false&fq=type:group&fq=GRUPPO_STRUTTURA:true&global=true&fl=type,sid,name:%5Bexpression%20expr=%27GROUP_NAME%2b%5C%27%20(%5C%27%2bsid%2b%5C%27)%5C%27%27%5D",
                url: "/docer/v1/solr/select?database=false&sort=name%20asc,name%20asc&fq=GRUPPO_STRUTTURA:true&fl=sid:%5Bexpression%20expr=%27id%27%5D,name:%5Bexpression%20expr=%27display_name%27%5D,GROUP_ID,GROUP_NAME,COD_AOO&rows=50&fq=type:group",
                data: {
                    q: request.term
                },
                success: function (data) {
                    response(data.data);
                }
            });
        },
        minLength: 1,
        response: function (event, ui) {
            if (ui.content.length > 0) {
                ui.content.forEach(function (singoloElemento) {
                    //console.log("singoloElemento:" + singoloElemento);
                    //singoloElemento.value = singoloElemento.name;
                    //singoloElemento.label = singoloElemento.name;
                    singoloElemento.value = singoloElemento.GROUP_NAME;
                    singoloElemento.label = singoloElemento.GROUP_NAME;
                    //singoloElemento.label = singoloElemento.name; ( descrizione + (id) )
                });
            }
        },
        select: function (event, ui) {
            console.log("select:" + ui);
            $("#mittentiAutocomplete-id").val(ui.item.GROUP_ID);
            mittPers(ui.item.GROUP_ID);
            $("#mittentiPersonaAutocomplete").removeAttr("disabled");

            mittenteDetail = ui.item;
            listaMittenteSelezionato[0] = mergeMittenteSelezionato(listaMittenteSelezionato[0], parseItemMittente(mittenteDetail));
        }


    });

    $("#destinatariAutocomplete").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "/docer/v1/solr/select?database=false&sort=name%20asc,name%20asc&fq=GRUPPO_STRUTTURA:true&fl=sid:%5Bexpression%20expr=%27id%27%5D,name:%5Bexpression%20expr=%27display_name%27%5D,GROUP_ID,GROUP_NAME,COD_AOO&rows=50&fq=type:group",
                data: {
                    q: request.term
                },
                success: function (data) {
                    response(data.data);
                }

            });
        },
        minLength: 1,
        response: function (event, ui) {
            if (ui.content.length > 0) {
                ui.content.forEach(function (singoloElemento) {
                    console.log("singoloElemento:" + singoloElemento);
                    singoloElemento.value = singoloElemento.GROUP_NAME;
                    singoloElemento.label = singoloElemento.GROUP_NAME;
                    //singoloElemento.label = singoloElemento.name; ( descrizione + (id) )
                });
            }
        },
        select: function (event, ui) {
            $("#destinatariAutocomplete-id").val(ui.item.GROUP_ID);
            $("#btnSelectDestinatario").removeAttr("disabled");
            destinatarioDetailList.push(parseItemDestinatariAddCompetente(ui.item));
            popolateSelectCodice();
        }
    });

    $("#rubricaAutocomplete").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "/docer/v1/solr/select?database=false&fq=TIPO_RUBRICA:*&fq=type:RUBRICA&fl=ID:INDIRIZZO_POSTALE,ID:FAX,ID:INDIRIZZO_PEO,ID:INDIRIZZO_PEC,ID:CODICE_FISCALE,ID:PARTITA_IVA,COD_RUBRICA,DES_RUBRICA,TIPO_RUBRICA,CODICE_FISCALE,PARTITA_IVA,INDIRIZZO_PEC,INDIRIZZO_PEO,INDIRIZZO_POSTALE,TELEFONO,FAX,AOO_UFFICIO&sort=DES_RUBRICA%20asc&emptyItem=false",
                data: {
                    q: request.term
                },
                success: function (data) {
                    response(data.data);
                }
            });
        },
        minLength: 1,
        response: function (event, ui) {
            if (ui.content.length > 0) {
                ui.content.forEach(function (singoloElemento) {
                    console.log("singoloElemento:" + singoloElemento);
                    if(singoloElemento.TIPO_RUBRICA=="Persona"){
                        icon = "PF - ";
                    }
                    if(singoloElemento.TIPO_RUBRICA=="PersonaGiuridica"){
                        icon = "PG - ";
                    }
                    if(singoloElemento.TIPO_RUBRICA=="Amministrazione"){
                        icon = "PA - ";
                    }
                    singoloElemento.value = icon + singoloElemento.DES_RUBRICA + " ("+ singoloElemento.ID +")";
                    singoloElemento.label = icon + singoloElemento.DES_RUBRICA + " ("+ singoloElemento.ID +")";
                });
            }
        },
        select: function (event, ui) {
            console.log("select:" + ui);
            $("#rubricaAutocomplete-id").val(ui.item.COD_RUBRICA);
            $("#btnSelectContacts").removeAttr("disabled");
            ipaDetail = ui.item;
        }
    });

    $("#ipaAutocomplete").autocomplete({
        source: function (request, response) {
            $.ajax({
                url: "/docer/v1/solr/select?database=false&rows=100&fq=type:(ipa_amm%20ipa_aoo%20ipa_ou)&fl=indirizzo_pec,indirizzo_postale,sid,name,des_amm,cod_amm,ipatype,display_name&sort=score%20desc",
                data: {
                    q: request.term
                },
                success: function (data) {
                    response(data.data);
                }
            });
        },
        minLength: 1,
        response: function (event, ui) {
            if (ui.content.length > 0) {
                ui.content.forEach(function (singoloElemento) {
                    console.log("singoloElemento:" + singoloElemento);
                    singoloElemento.value = singoloElemento.name;
                    singoloElemento.label = singoloElemento.name;
                });
            }
        },
        select: function (event, ui) {
            console.log("select:" + ui);
            $("#ipaAutocomplete-id").val(ui.item.cod_amm);
            $("#btnSelectContacts").removeAttr("disabled");
            ipaDetail = ui.item;
        }
    });

    $('#btnSelectContacts').click(function () {
        if (ipaDetail != null) {
            var mittenteTrovato = false;
            for(i=0;i<listaMittentiSelezionati.length;i++) {
                var mittente = listaMittentiSelezionati[i];
                if(mittente.COD_RUBRICA==ipaDetail.COD_RUBRICA){
                    mittenteTrovato=true;
                }
            }
            if (mittenteTrovato) {
                alert("l'Id selezionato risulta gia' inserito");
                $("#rubricaAutocomplete").val("");
                $("#rubricaAutocomplete-id").val("");
                $("#ipaAutocomplete").val("");
                $("#ipaAutocomplete-id").val("");
            } else {
                listaMittentiSelezionati.push(parseItem(ipaDetail));
                //listaMittentiSelezionati.push(ipaDetail);
                //btnSelectContactsClick(ipaDetail);
                btnSelectContactsClick(parseItem(ipaDetail));
            }

            $("#btnSelectContacts").attr("disabled", "disabled");
            $("#rubricaIpaNoElement").html("");
        }

        // se protocollo in Entrata e il mittente è una IPA o Amministrazione abilito il box protocolloMittente per poter aggiungere eventuali metadati relativi l'IPA
        if ($("#versoDocumento-id").val()=="E") {
            if ($(".glyphicon-home")[0]){ //se è una IPA o Amministrazione l'icona nella pagina accanto al nome è glyphicon-home
                $('#protocolloMittente').show();
            }
        }

    });

    $('#btnSelezionaRubrica').click(function () {

        $('#warningNearModal').hide();
        $(".div-alert-warning > p").remove();

        $("#label-amministrazione-input > b").remove();
        $("#label-amministrazione-input").css("color","#000");
        $("#label-aooUfficio-input > b").remove();
        $("#label-aooUfficio-input").css("color","#000");
        $("#label-denominazione-input > b").remove();
        $("#label-denominazione-input").css("color","#000");
        $("#label-cf-input > b").remove();
        $("#label-cf-input").css("color","#000");
        $("#label-piva-input > b").remove();
        $("#label-piva-input").css("color","#000");
        $("#label-indirizzoPostale-input > b").remove();
        $("#label-indirizzoPostale-input").css("color","#000");
        $("#label-pec-input > b").remove();
        $("#label-pec-input").css("color","#000");
        $("#label-peo-input > b").remove();
        $("#label-peo-input").css("color","#000");
        $("#label-fax-input > b").remove();
        $("#label-fax-input").css("color","#000");

        var errore = false;

        if (!checkRequiredInput()){
            errore = true;
        }
        if (!checkCf()){
            errore = true;
        }
        if (!checkPiva()){
            errore = true;
        }
        if (!checkEmail()){
            errore = true;
        }
        if (!errore) {
            var progressivo = $("#modal-progressivo-input").val();
            if (progressivo) { //nel caso in cui arrivo dalla modifica
                var htmlElement = $("#mittente"+progressivo);
                addMittente(htmlElement, progressivo);
                closeClearRubrica();
                $("#rubricaIpaNoElement").html("");
            } else {
                if (!checkId()){
                    errore = true;
                }
                if (!errore) {
                    $('#warningNearModal').hide();
                    btnSelectNewContactsClick();
                    closeClearRubrica();
                } else {
                    $('#warningNearModal').show();
                }
                $("#rubricaIpaNoElement").html("");
            }

            // se protocollo in Entrata e il mittente è una IPA o Amministrazione abilito il box protocolloMittente per poter aggiungere eventuali metadati relativi l'IPA
            if ($("#versoDocumento-id").val()=="E") {
                if ($(".glyphicon-home")[0]){ //se è una IPA o Amministrazione l'icona nella pagina accanto al nome è glyphicon-home
                    $('#protocolloMittente').show();
                }
            }

        } else {
            $('#warningNearModal').show();
        }

    });

    $('#btnSelezionaPg').click(function () {
        var progressivo = $("#modal-progressivoPm-input").val();
        if (progressivo) { //nel caso in cui arrivo dalla modifica
            var htmlElement = $("#protocolloMittente"+progressivo);
            addProtocolloMittente(htmlElement, progressivo);
            //svuoto l'unico elemento della lista e rimetto i valori modificati nella listaProtocolloMittenteSelezionato
            listaProtocolloMittenteSelezionato.pop();
            listaProtocolloMittenteSelezionato.push(parseItemProtocolloMittente());
            closeClearProtocolloMittente();
            $("#protocolloMittenteNoElement").html("");
        } else {
            btnSelectNewProtocolloMittenteClick();
            listaProtocolloMittenteSelezionato.push(parseItemProtocolloMittente());
            closeClearProtocolloMittente();
            $("#protocolloMittenteNoElement").html("");
        }
    });

    $('#btnSelectDestinatario').click(function () {
        if ($('#destinatariPersone-id').val()!="") {   //se ho gli utenti
            var listaUtentiSelezionatiMulti = $('#destinatariPersone-id').val();
            //var ciccio = $("#destinatariPersone-id option:selected").text();


            for (i=0;i<listaUtentiSelezionatiMulti.length;i++) {
                var utentiMultiDetail = listaUtentiSelezionatiMulti[i];
                //if ID presente
                var destinatarioTrovato = false;
                for(j=0;j<listaDestinatariSelezionati.length;j++) {
                    var destinatario = listaDestinatariSelezionati[j];
                    if(destinatario.id==utentiMultiDetail.split("*").shift()){
                        destinatarioTrovato=true;
                    }
                }
                if (destinatarioTrovato) {
                    //alert("Uno o piu' Nominativi selezionati risultano gia' inseriti, verranno aggiunti solo eventuali nuovi nominativi selezionati");
                } else {
                    listaDestinatariSelezionati.push(parseItemDestinatari(utentiMultiDetail));
                    btnSelectDestinatarioClickMulti(utentiMultiDetail);
                }
                //fine if ID presente
                $("#destinatariNoElement").html("");
            }
        } else {
            for (i=0;i<destinatarioDetailList.length;i++) {
                var destinatarioDetail = destinatarioDetailList[i];
                var destinatarioTrovato = false;
                for(j=0;j<listaDestinatariSelezionati.length;j++) {
                    var destinatario = listaDestinatariSelezionati[j];
                    if (untitaOrganizzativaDetail != null) {
                        if(destinatario.id==destinatarioDetail.USER_ID){
                            destinatarioTrovato=true;
                        }
                    } else {
                        if(destinatario.id==destinatarioDetail.GROUP_ID){
                            destinatarioTrovato=true;
                        }
                    }
                }
                if (destinatarioTrovato) {
                    alert("l'Unita' Organizzativa selezionata risulta gia' inserita");
                    destinatarioDetailList = [];
                } else {
                    listaDestinatariSelezionati.push(parseItemDestinatari(destinatarioDetail));
                    btnSelectDestinatarioClick(destinatarioDetail);
                    resetMultiOptions();
                }
                $("#destinatariNoElement").html("");
            }
        }
    });

    $('#aggiornaProtocollo').click(function () {
        recuperaIdListaFinale();
        //se è stato modificato Oggetto, mittenti o destinatari faccio annullamento parziale
        if ( (listaMittentiIniziali.toLocaleString() != listaMittentiFinali.toLocaleString()) || (listaDestinatariIniziali.toLocaleString() != listaDestinatariFinali.toLocaleString()) || ($("#oggetto-id").val() != oggettoProtocollazione_DOC) ){
            $('#alertAggiornaModal').modal('show');
            $("#alertAggiornaModalMessage").addClass("alert-warning");
            $(".aggiornaModalMessage").html("Procedendo con l'azione richiesta si eseguirà un annullamento parziale della protocollazione. Procedere con l'annullamento parziale?");
        } else {
            //altrimenti salvo
            $("#salvaDati").trigger("click");
        }
    });

}); //*** chiusura $(document).ready ***


var untitaOrganizzativaDetail = null;
var progressivoDestinatari = 0;
var progressivoMittenti = 0;
var progressivoProtocolloMittente = 0;
var classificaDetail = null;
var mittenteDetail = null;
var mittentePersonaDetail = null;
var datiRecuperatiDetail = null;
var ipaDetail = null;
var protocolloMittenteDetail = null;
var destinatarioAutocompleteInit = false;
var mittenteAutocompleteInit = false;
//var email = null;
//LISTE
var destinatarioDetailList = [];
var listaMittentiSelezionati = [];
var listaMittenteSelezionato = [];
listaMittenteSelezionato.push({});
var listaDestinatariSelezionati = [];
var listaClassificaSelezionata = {};
var listaProtocolloMittenteSelezionato = [];
var listaProtocolloMittente = {};
//liste per gestione tasto Aggiorna
var listaMittentiIniziali = [];
var listaDestinatariIniziali = [];
var listaMittentiFinali = [];
var listaDestinatariFinali = [];
var destinatario = [];
var listaNomiMittenti = [];
var listaDocumento = [];


function checkRequiredInput() {

    var esitoCheck = true;

    if($("#modal-tipologia-input").val()=="Persona"){
        if( $("#modal-cf-input").val() ) {
            identificativo = $("#modal-cf-input").val();
        } else if ( $("#modal-pec-input").val() ) {
            identificativo = $("#modal-pec-input").val();
        } else if ( $("#modal-peo-input").val() ) {
            identificativo = $("#modal-peo-input").val();
        } else if ( $("#modal-fax-input").val() ) {
            identificativo = $("#modal-fax-input").val();
        } else {
            identificativo = "";
        }
        if( $("#modal-denominazione-input").val()=="" || identificativo=="" ) {
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Denominazione e uno tra CF, PEC, PEO, FAX obbligatori!</p>');
            esitoCheck = false;
        }
    }
    if($("#modal-tipologia-input").val()=="PersonaGiuridica"){
        if( $("#modal-piva-input").val() ) {
            identificativo = $("#modal-piva-input").val();
        } else if ( $("#modal-pec-input").val() ) {
            identificativo = $("#modal-pec-input").val();
        } else {
            identificativo = "";
        }
        if( $("#modal-denominazione-input").val()=="" || identificativo=="" ) {
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Ragione Sociale e uno tra PIVA o PEC obbligatori!</p>');
            esitoCheck = false;
        }
    }
    if($("#modal-tipologia-input").val()=="Amministrazione"){
        if( $("#modal-aooUfficio-input").val() && $("#modal-pec-input").val() ) {
            identificativo = "ok";
        } else {
            identificativo = "";
        }
        if( $("#modal-amministrazione-input").val()=="" || identificativo=="" ) {
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Amministrazione, Ufficio e PEC obbligatori!</p>');
            esitoCheck = false;
        }
    }

    return esitoCheck;
}


function recuperaIdListaIniziale() {
    if (tipoProtocollazione_DOC == "E") {
        for(i=0;i<listaMittentiSelezionati.length;i++) {
            var mittentiIniziali = listaMittentiSelezionati[i].ID;
            listaMittentiIniziali.push(mittentiIniziali);
        }
        for(i=0;i<listaDestinatariSelezionati.length;i++) {
            var destinatariIniziali = listaDestinatariSelezionati[i].id;
            listaDestinatariIniziali.push(destinatariIniziali);
        }
    }
    if (tipoProtocollazione_DOC == "I") {
        for(i=0;i<listaMittenteSelezionato.length;i++) {
            var mittenteIniziale = listaMittenteSelezionato[i].id;
            listaMittentiIniziali.push(mittenteIniziale);
        }
        for(i=0;i<listaDestinatariSelezionati.length;i++) {
            var destinatariIniziali = listaDestinatariSelezionati[i].id;
            listaDestinatariIniziali.push(destinatariIniziali);
        }
    }
    if (tipoProtocollazione_DOC == "U") {
        for(i=0;i<listaMittenteSelezionato.length;i++) {
            var mittenteIniziale = listaMittenteSelezionato[i].id;
            listaMittentiIniziali.push(mittenteIniziale);
        }
        for(i=0;i<listaMittentiSelezionati.length;i++) {
            var destinatariIniziali = listaMittentiSelezionati[i].ID;
            listaDestinatariIniziali.push(destinatariIniziali);
        }
    }
}

function recuperaIdListaFinale() {
    if (tipoProtocollazione_DOC == "E") {
        for(i=0;i<listaMittentiSelezionati.length;i++) {
            var mittentiFinali = listaMittentiSelezionati[i].ID;
            listaMittentiFinali.push(mittentiFinali);
        }
        for(i=0;i<listaDestinatariSelezionati.length;i++) {
            var destinatariFinali = listaDestinatariSelezionati[i].id;
            listaDestinatariFinali.push(destinatariFinali);
        }
    }
    if (tipoProtocollazione_DOC == "I") {
        for(i=0;i<listaMittenteSelezionato.length;i++) {
            var mittenteFinale = listaMittenteSelezionato[i].id;
            listaMittentiFinali.push(mittenteFinale);
        }
        for(i=0;i<listaDestinatariSelezionati.length;i++) {
            var destinatariFinali = listaDestinatariSelezionati[i].id;
            listaDestinatariFinali.push(destinatariFinali);
        }
    }
    if (tipoProtocollazione_DOC == "U") {
        for(i=0;i<listaMittenteSelezionato.length;i++) {
            var mittenteFinale = listaMittenteSelezionato[i].id;
            listaMittentiFinali.push(mittenteFinale);
        }
        for(i=0;i<listaMittentiSelezionati.length;i++) {
            var destinatariFinali = listaMittentiSelezionati[i].ID;
            listaDestinatariFinali.push(destinatariFinali);
        }
    }
}

function showEntrataInfo() {
    $("#versoDocumento-id").val("E");
    $("#rubricaIpaTitle").text("*Mittente").css({
        "font-weight": "bold"
    });
    $("#entrata").removeClass("btn-default").addClass("btn-primary");
    $("#interna, #uscita").removeClass("btn-primary").addClass("btn-default");
    $("#destinatario").show();
    $("#mittente").hide();
    $("#ipa-input").hide();
    $("#rubricaIpa").show();
    $('#mittentiAutocomplete').val("");
    $('#mittentiAutocomplete-id').val("");
    $('#mittentiPersonaAutocomplete').val("");
    $('#mittentiPersonaAutocomplete-id').val("");
    $('#selectEmailUser').val("").show();
}

function showUscitaInfo() {
    $("#versoDocumento-id").val("U");
    $("#rubricaIpaTitle").text("*Destinatari").css({
        "font-weight": "bold"
    });
    $("#uscita").removeClass("btn-default").addClass("btn-primary");
    $("#interna, #entrata").removeClass("btn-primary").addClass("btn-default");
    $("#mittente").show();
    $("#destinatario").hide();
    $("#rubricaIpa").show();
    $("#ipa-input").hide();
    $("#mittente").insertBefore("#rubricaIpa");
    $('#mittentiAutocomplete').val("");
    $('#mittentiAutocomplete-id').val("");
    $('#mittentiPersonaAutocomplete').val("");
    $('#mittentiPersonaAutocomplete-id').val("");
    $('#selectEmailUser').val("").show();
}

function showInternaInfo() {
    $("#versoDocumento-id").val("I");
    $("#interna").removeClass("btn-default").addClass("btn-primary");
    $("#entrata, #uscita").removeClass("btn-primary").addClass("btn-default");
    $("#mittente, #destinatario").show();
    $("#rubricaIpa").hide();
    $('#mittentiAutocomplete').val("");
    $('#mittentiAutocomplete-id').val("");
    $('#mittentiPersonaAutocomplete').val("");
    $('#mittentiPersonaAutocomplete-id').val("");
    $('#selectEmailUser').val("").hide();
}

/*function valorizzaMezzo() {
    $('form .rubricaIpaSelezionatoResultOption').each(function (index) {
        var value = $(this).val();
        if (value) {
            var itemIdElement = $(this).closest("li").find(".itemId").val();
            for (i=0;i<listaMittentiSelezionati.length;i++){
                var tmpElement = listaMittentiSelezionati[i];
                if (itemIdElement == tmpElement.id) {
                    listaMittentiSelezionati[i].mezzo = value;
                }
            }
        }
    });
}*/


//PARSE ITEM
//rimappo Autocomplete
function parseItem(item) {
    var result = {};
    if (item.COD_RUBRICA) {
        result = parseItemRubrica(item);
    } else {
        result = parseItemIpa(item);
    }
    return result;
}

function parseItemRubrica(item) { //KS6

    var result = {};
    result.ID = item.ID ? item.ID : "";
    result.COD_RUBRICA = item.COD_RUBRICA;
    result.DES_RUBRICA = item.DES_RUBRICA ? item.DES_RUBRICA : "";
    result.INDIRIZZO_PEC = item.INDIRIZZO_PEC ? item.INDIRIZZO_PEC : "";
    result.INDIRIZZO_POSTALE = item.INDIRIZZO_POSTALE ? item.INDIRIZZO_POSTALE : "";
    result.TIPO_RUBRICA = item.TIPO_RUBRICA ? item.TIPO_RUBRICA : "";
    result.riferimentoMezzo = "";

    if(item.TIPO_RUBRICA=="Persona"){
        result.CODICE_FISCALE = item.CODICE_FISCALE ? item.CODICE_FISCALE : "";
        result.INDIRIZZO_PEO = item.INDIRIZZO_PEO ? item.INDIRIZZO_PEO : "";
        result.FAX = item.FAX ? item.FAX : "";
    }
    if(item.TIPO_RUBRICA=="PersonaGiuridica"){
        result.PARTITA_IVA = item.PARTITA_IVA ? item.PARTITA_IVA : "";
    }
    if(item.TIPO_RUBRICA=="Amministrazione"){
        result.AOO_UFFICIO = item.AOO_UFFICIO ? item.AOO_UFFICIO : "";
    }

    return result;
}

function parseItemIpa(item) {
    var result = {};

    result.ID = item.cod_amm;
    result.COD_RUBRICA = item.cod_amm;
    result.DES_RUBRICA = item.des_amm ? item.des_amm : "";
    result.INDIRIZZO_PEC = item.indirizzo_pec ? item.indirizzo_pec : "";
    result.INDIRIZZO_POSTALE = item.indirizzo_postale ? item.indirizzo_postale : "";
    result.TIPO_RUBRICA = "Amministrazione";
    result.riferimentoMezzo = "";

    return result;
}


function parseItemProtocolloMittente() {
    var result = {};
    if (dataPgMittente_DOC == "") {
        var dataPg = $("#modal-dataPg-input").val();
        result.dataPgMittente = getIsoDate(dataPg);
        result.codAoo = codAooVerifica;
        result.numPgMittente = $("#modal-numPg-input").val();
        result.classificaMittente = $("#modal-classificaPg-input").val();
        result.fascicoloMittente = $("#modal-fascicoloPg-input").val();
        result.type = "PM";
    } else {
        result.dataPgMittente = dataPgMittente_DOC;
        result.codAoo = codAooVerifica;
        result.numPgMittente = numPgMittente_DOC;
        result.classificaMittente = classificaMittente_DOC;
        result.fascicoloMittente = fascicoloMittente_DOC;
        result.type = "PM";
    }
    return result;
}

function parseItemDestinatariAddCompetente(item) {
    var result = {};
    result.COD_AOO = item.COD_AOO;
    result.GROUP_ID = item.GROUP_ID;
    result.GROUP_NAME = item.GROUP_NAME;
    result.label = item.label;
    result.name = item.name;
    result.sid = item.sid;
    result.value = item.value;
    if (listaDestinatariSelezionati.length==0) {
        result.competente = true;
    } else {
        result.competente = false;
    }
    return result;
}

function parseItemDestinatari(item) { // KS6 parse destinatari autocomplete

    var result = {};

    result.codiceAOO = currentCodiceAOO;
    result.codiceAmm = currentCodiceAmm;
    result.codiceUO = item.GROUP_ID ? item.GROUP_ID : $('#destinatariAutocomplete-id').val();
    if (listaDestinatariSelezionati.length==0) {
        result.competente = true;
    } else {
        result.competente = false;
    }
    result.denominazioneAOO = currentDenominazioneAOO;
    result.denominazioneAmm = currentDenominazioneAmm;
    result.denominazioneUO = item.GROUP_NAME ? item.GROUP_NAME : $('#destinatariAutocomplete').val();
    //result.indirizzoPostale = result.indirizzoPostale ? result.indirizzoPostale : null;
    //result.indirizzoTelematico = result.indirizzoTelematico ? result.indirizzoTelematico : null;
    //result.mezzo = result.mezzo ? result.mezzo : null;
    //result.riferimentoMezzo = result.riferimentoMezzo ? result.riferimentoMezzo : null;
    result.tipoCorrispondente = "Amministrazione";
    var itemString = JSON.stringify(item);
    if (itemString.includes("*")) {
        result.persona = {};
        result.persona.identificativo = item.split("*").shift();
        result.persona.denominazione = item.split("*").pop();
        //result.persona.indirizzoPostale = result.persona.indirizzoPostale ? result.persona.indirizzoPostale : null;
        //result.persona.indirizzoTelematico = result.persona.indirizzoTelematico ? result.persona.indirizzoTelematico : null;
        //result.persona.mezzo = result.persona.mezzo ? result.persona.mezzo : null;
        //result.persona.riferimentoMezzo = result.persona.riferimentoMezzo ? result.persona.riferimentoMezzo : null;
        result.persona.tipoCorrispondente = "Persona";
        result.id = item.split("*").shift();
    } else {
        result.id = item.GROUP_ID ? item.GROUP_ID : $('#destinatariAutocomplete-id').val();
    }
    return result;

}
/*
function parseItemClassifica(item) {
    var result = {};
    result.id = item.CLASSIFICA;
    result.name = item.DES_TITOLARIO;
    return result;
}*/

function parseItemMittente(item) {
    var result = {};
    result.codiceAOO = currentCodiceAOO;
    result.codiceAmm = currentCodiceAmm;
    result.codiceUO = item.GROUP_ID;
    result.competente = false;
    result.denominazioneAOO = currentDenominazioneAOO;
    result.denominazioneAmm = currentDenominazioneAmm;
    result.denominazioneUO = item.GROUP_NAME;
    result.id = item.GROUP_ID;
    result.tipoCorrispondente = "Amministrazione";
    return result;
}

function parseItemMittentePersona(item) {
    var result = {};
    result.id = item.USER_ID;
    result.persona = {};
    result.persona.denominazione = item.FULL_NAME;
    result.persona.identificativo = item.USER_ID;
    result.persona.tipoCorrispondente = "Persona";
    return result;
}

function parseItemMittentePersonaRecuperato(listaMittentiRecuperati) {
    var result = {};

    result.codiceAOO = listaMittentiRecuperati.codiceAOO ? listaMittentiRecuperati.codiceAOO : "";
    result.codiceAmm = listaMittentiRecuperati.codiceAmm ? listaMittentiRecuperati.codiceAmm : "";
    result.codiceUO = listaMittentiRecuperati.codiceUO ? listaMittentiRecuperati.codiceUO : "";
    result.competente = listaMittentiRecuperati.competente ? listaMittentiRecuperati.competente : false;
    result.denominazioneAOO = listaMittentiRecuperati.denominazioneAOO ? listaMittentiRecuperati.denominazioneAOO : "";
    result.denominazioneAmm = listaMittentiRecuperati.denominazioneAmm ? listaMittentiRecuperati.denominazioneAmm : "";
    result.denominazioneUO = listaMittentiRecuperati.denominazioneUO ? listaMittentiRecuperati.denominazioneUO : "";
    if(listaMittentiRecuperati.indirizzoTelematico) {
        result.indirizzoTelematico = listaMittentiRecuperati.indirizzoTelematico ? listaMittentiRecuperati.indirizzoTelematico : "";
    }
    result.tipoCorrispondente = "Amministrazione";
    if(listaMittentiRecuperati.persona) {
        result.persona = {};
        result.persona.identificativo = listaMittentiRecuperati.persona.identificativo ? listaMittentiRecuperati.persona.identificativo : "";
        result.persona.denominazione = listaMittentiRecuperati.persona.denominazione ? listaMittentiRecuperati.persona.denominazione : "";
        result.persona.tipoCorrispondente = "Persona";
        result.id = listaMittentiRecuperati.persona.identificativo ? listaMittentiRecuperati.persona.identificativo : "";
    }else{
        result.id = listaMittentiRecuperati.codiceUO ? listaMittentiRecuperati.codiceUO : "";
    }
    return result;
}



//parse dati listaMittenti per salvataggio/protocollo
function parseItemListaMittenti(listaMittentiSelezionati) {

    var result = {};
    if(listaMittentiSelezionati.TIPO_RUBRICA=="Amministrazione"){
        result.codiceRubrica = listaMittentiSelezionati.COD_RUBRICA;
        result.denominazioneAmm = listaMittentiSelezionati.DES_RUBRICA;
        result.denominazioneUO = listaMittentiSelezionati.AOO_UFFICIO;
        result.indirizzoPostale = listaMittentiSelezionati.INDIRIZZO_POSTALE;
        result.indirizzoTelematico = listaMittentiSelezionati.INDIRIZZO_PEC;
        result.mezzo = listaMittentiSelezionati.riferimentoMezzo ? listaMittentiSelezionati.riferimentoMezzo : "-- mezzo --";
        result.tipoCorrispondente = listaMittentiSelezionati.TIPO_RUBRICA;
    } else {
        result.codiceRubrica = listaMittentiSelezionati.COD_RUBRICA;
        result.denominazione = listaMittentiSelezionati.DES_RUBRICA;
        result.identificativo = listaMittentiSelezionati.ID;
        result.indirizzoPostale = listaMittentiSelezionati.INDIRIZZO_POSTALE;
        result.tipoCorrispondente = listaMittentiSelezionati.TIPO_RUBRICA;
        result.mezzo = listaMittentiSelezionati.riferimentoMezzo ? listaMittentiSelezionati.riferimentoMezzo : "-- mezzo --";
        if ( listaMittentiSelezionati.riferimentoMezzo == "PEC" ) {
            result.indirizzoTelematico = listaMittentiSelezionati.INDIRIZZO_PEC;
        }
        if ( listaMittentiSelezionati.riferimentoMezzo == "PEO" ) {
            result.indirizzoTelematico = listaMittentiSelezionati.INDIRIZZO_PEO;
        }
        if ( listaMittentiSelezionati.riferimentoMezzo == "FAX" ) {
            result.indirizzoTelematico = listaMittentiSelezionati.FAX;
        }
    }

    return result;
}





//PARSE ITEM CARICAMENTO PAGINA
function parseItemProtocolloMittenteVuoto() { //setto il result con i valori "" perchè se non mando a solr "protocolloMittente" lascia i vecchi dati
    var result = {};
    result.dataPgMittente = "";
    result.codAoo = "";
    result.numPgMittente = "";
    result.classificaMittente = "";
    result.fascicoloMittente = "";
    result.type = "";
    return result;
}

function parseItemMittentiRecuperati(listaMittentiRecuperati) {
    var result = {};
    if (listaMittentiRecuperati.COD_ENTE) {
        result = parseItemMittentiRecuperatiIpa(listaMittentiRecuperati);
    } else {
        result = parseItemMittentiRecuperatiRubrica(listaMittentiRecuperati);
    }
    return result;
}

function parseItemMittentiRecuperatiRubrica(listaMittentiRecuperati) {
    var result = {};

    //se arrivo da recupero dati
    if(listaMittentiRecuperati.tipoCorrispondente == "Persona") {
        result.ID = listaMittentiRecuperati.identificativo ? listaMittentiRecuperati.identificativo : "";
        result.COD_RUBRICA = listaMittentiRecuperati.codiceRubrica ? listaMittentiRecuperati.codiceRubrica : "";
        result.DES_RUBRICA = listaMittentiRecuperati.denominazione ? listaMittentiRecuperati.denominazione : "";
        result.INDIRIZZO_POSTALE = listaMittentiRecuperati.indirizzoPostale ? listaMittentiRecuperati.indirizzoPostale : "";
        result.TIPO_RUBRICA = listaMittentiRecuperati.tipoCorrispondente ? listaMittentiRecuperati.tipoCorrispondente : "";
        result.riferimentoMezzo = listaMittentiRecuperati.mezzo ? listaMittentiRecuperati.mezzo : "";
        //quando recupero il mittente ho solo indirizzoTelematico, inserisco il valore in FAX, PEC o PEO a seconda del mezzo recuperato
        if(listaMittentiRecuperati.mezzo=="FAX"){
            result.FAX = listaMittentiRecuperati.indirizzoTelematico ? listaMittentiRecuperati.indirizzoTelematico : "";
            result.INDIRIZZO_PEC = "";
            result.INDIRIZZO_PEO = "";
        }
        if(listaMittentiRecuperati.mezzo=="PEC"){
            result.FAX = "";
            result.INDIRIZZO_PEC = listaMittentiRecuperati.indirizzoTelematico ? listaMittentiRecuperati.indirizzoTelematico : "";
            result.INDIRIZZO_PEO = "";
        }
        if(listaMittentiRecuperati.mezzo=="PEO"){
            result.FAX = "";
            result.INDIRIZZO_PEC = "";
            result.INDIRIZZO_PEO = listaMittentiRecuperati.indirizzoTelematico ? listaMittentiRecuperati.indirizzoTelematico : "";
        }
        //se ho selezionato Brevimano o Altro inserisco l'identificativo come CF se passa il controllo regex
        var $regexcf=/^[A-Za-z]{6}[0-9]{2}[A-Za-z]{1}[0-9]{2}[A-Za-z]{1}[0-9]{3}[A-Za-z]{1}$/;
        if (listaMittentiRecuperati.identificativo && listaMittentiRecuperati.identificativo.match($regexcf)) {
            result.CODICE_FISCALE = listaMittentiRecuperati.identificativo ? listaMittentiRecuperati.identificativo : "";
        }
    }
    if(listaMittentiRecuperati.tipoCorrispondente == "PersonaGiuridica") {
        result.ID = listaMittentiRecuperati.identificativo ? listaMittentiRecuperati.identificativo : "";
        result.COD_RUBRICA = listaMittentiRecuperati.codiceRubrica ? listaMittentiRecuperati.codiceRubrica : "";
        result.DES_RUBRICA = listaMittentiRecuperati.denominazione ? listaMittentiRecuperati.denominazione : "";
        result.INDIRIZZO_POSTALE = listaMittentiRecuperati.indirizzoPostale ? listaMittentiRecuperati.indirizzoPostale : "";
        result.TIPO_RUBRICA = listaMittentiRecuperati.tipoCorrispondente ? listaMittentiRecuperati.tipoCorrispondente : "";
        result.riferimentoMezzo = listaMittentiRecuperati.mezzo ? listaMittentiRecuperati.mezzo : "";
        if(listaMittentiRecuperati.mezzo=="PEC"){
            result.INDIRIZZO_PEC = listaMittentiRecuperati.indirizzoTelematico ? listaMittentiRecuperati.indirizzoTelematico : "";
        }
        //se ho selezionato Brevimano o Altro inserisco l'identificativo come PIVA se passa il controllo regex (PIVA straniera non passa il controllo regex)
        var $regexpiva=/^[0-9]{11}$/;
        if (listaMittentiRecuperati.identificativo && listaMittentiRecuperati.identificativo.match($regexpiva)) {
            result.PARTITA_IVA = listaMittentiRecuperati.identificativo ? listaMittentiRecuperati.identificativo : "";
        }
    }
    if (listaMittentiRecuperati.tipoCorrispondente == "Amministrazione") {
        result.ID = listaMittentiRecuperati.indirizzoTelematico ? listaMittentiRecuperati.indirizzoTelematico : "";
        result.COD_RUBRICA = listaMittentiRecuperati.codiceRubrica ? listaMittentiRecuperati.codiceRubrica : "";
        result.DES_RUBRICA = listaMittentiRecuperati.denominazioneAmm ? listaMittentiRecuperati.denominazioneAmm : "";
        result.AOO_UFFICIO = listaMittentiRecuperati.denominazioneUO ? listaMittentiRecuperati.denominazioneUO : "";
        result.INDIRIZZO_PEC = listaMittentiRecuperati.indirizzoTelematico ? listaMittentiRecuperati.indirizzoTelematico : "";
        result.INDIRIZZO_POSTALE = listaMittentiRecuperati.indirizzoPostale ? listaMittentiRecuperati.indirizzoPostale : "";
        result.TIPO_RUBRICA = listaMittentiRecuperati.tipoCorrispondente ? listaMittentiRecuperati.tipoCorrispondente : "";
        result.riferimentoMezzo = listaMittentiRecuperati.mezzo ? listaMittentiRecuperati.mezzo : "";
    }

    //se arrivo da modale
    if (listaMittentiRecuperati.TIPO_RUBRICA == "Persona") {
        result.COD_RUBRICA = listaMittentiRecuperati.COD_RUBRICA ? listaMittentiRecuperati.COD_RUBRICA : "";
        result.DES_RUBRICA = listaMittentiRecuperati.DES_RUBRICA ? listaMittentiRecuperati.DES_RUBRICA : "";
        result.CODICE_FISCALE = listaMittentiRecuperati.CODICE_FISCALE ? listaMittentiRecuperati.CODICE_FISCALE : "";
        result.FAX = listaMittentiRecuperati.FAX ? listaMittentiRecuperati.FAX : "";
        result.ID = listaMittentiRecuperati.ID ? listaMittentiRecuperati.ID : "";
        result.INDIRIZZO_PEC = listaMittentiRecuperati.INDIRIZZO_PEC ? listaMittentiRecuperati.INDIRIZZO_PEC : "";
        result.INDIRIZZO_PEO = listaMittentiRecuperati.INDIRIZZO_PEO ? listaMittentiRecuperati.INDIRIZZO_PEO : "";
        result.INDIRIZZO_POSTALE = listaMittentiRecuperati.INDIRIZZO_POSTALE ? listaMittentiRecuperati.INDIRIZZO_POSTALE : "";
        result.TIPO_RUBRICA = listaMittentiRecuperati.TIPO_RUBRICA ? listaMittentiRecuperati.TIPO_RUBRICA : "";
        result.riferimentoMezzo = listaMittentiRecuperati.riferimentoMezzo ? listaMittentiRecuperati.riferimentoMezzo : "";
        if (listaMittentiRecuperati.riferimentoMezzo=="PEC" && listaMittentiRecuperati.INDIRIZZO_PEC==""){
            result.riferimentoMezzo = "-- mezzo --";
        }
        if (listaMittentiRecuperati.riferimentoMezzo=="PEO" && listaMittentiRecuperati.INDIRIZZO_PEO==""){
            result.riferimentoMezzo = "-- mezzo --";
        }
        if (listaMittentiRecuperati.riferimentoMezzo=="FAX" && listaMittentiRecuperati.FAX==""){
            result.riferimentoMezzo = "-- mezzo --";
        }
    }
    if (listaMittentiRecuperati.TIPO_RUBRICA == "PersonaGiuridica") {
        result.COD_RUBRICA = listaMittentiRecuperati.COD_RUBRICA ? listaMittentiRecuperati.COD_RUBRICA : "";
        result.DES_RUBRICA = listaMittentiRecuperati.DES_RUBRICA ? listaMittentiRecuperati.DES_RUBRICA : "";
        result.PARTITA_IVA = listaMittentiRecuperati.PARTITA_IVA ? listaMittentiRecuperati.PARTITA_IVA : "";
        result.ID = listaMittentiRecuperati.ID ? listaMittentiRecuperati.ID : "";
        result.INDIRIZZO_PEC = listaMittentiRecuperati.INDIRIZZO_PEC ? listaMittentiRecuperati.INDIRIZZO_PEC : "";
        result.INDIRIZZO_POSTALE = listaMittentiRecuperati.INDIRIZZO_POSTALE ? listaMittentiRecuperati.INDIRIZZO_POSTALE : "";
        result.TIPO_RUBRICA = listaMittentiRecuperati.TIPO_RUBRICA ? listaMittentiRecuperati.TIPO_RUBRICA : "";
        result.riferimentoMezzo = listaMittentiRecuperati.riferimentoMezzo ? listaMittentiRecuperati.riferimentoMezzo : "";
        if (listaMittentiRecuperati.riferimentoMezzo=="PEC" && listaMittentiRecuperati.INDIRIZZO_PEC==""){
            result.riferimentoMezzo = "-- mezzo --";
        }
    }
    //se arrivo da modale
    if (listaMittentiRecuperati.TIPO_RUBRICA == "Amministrazione") {
        result.COD_RUBRICA = listaMittentiRecuperati.COD_RUBRICA ? listaMittentiRecuperati.COD_RUBRICA : "";
        result.DES_RUBRICA = listaMittentiRecuperati.DES_RUBRICA ? listaMittentiRecuperati.DES_RUBRICA : "";
        result.INDIRIZZO_PEC = listaMittentiRecuperati.INDIRIZZO_PEC ? listaMittentiRecuperati.INDIRIZZO_PEC : "";
        result.INDIRIZZO_POSTALE = listaMittentiRecuperati.INDIRIZZO_POSTALE ? listaMittentiRecuperati.INDIRIZZO_POSTALE : "";
        result.TIPO_RUBRICA = listaMittentiRecuperati.TIPO_RUBRICA ? listaMittentiRecuperati.TIPO_RUBRICA : "";
        result.riferimentoMezzo = listaMittentiRecuperati.riferimentoMezzo ? listaMittentiRecuperati.riferimentoMezzo : "";
        result.ID = listaMittentiRecuperati.ID ? listaMittentiRecuperati.ID : "";
        result.AOO_UFFICIO = listaMittentiRecuperati.AOO_UFFICIO ? listaMittentiRecuperati.AOO_UFFICIO : "";
    }

    return result;
}

function parseItemMittentiRecuperatiIpa(listaMittentiRecuperati) {
    var result = {};
    result.id = listaMittentiRecuperati.COD_ENTE;
    result.email = listaMittentiRecuperati.IndirizzoTelematico ? listaMittentiRecuperati.IndirizzoTelematico : "";
    result.indirizzo = listaMittentiRecuperati.indirizzoPostale ? listaMittentiRecuperati.indirizzoPostale : "";
    result.ipa = {};
    result.ipa.codAmm = listaMittentiRecuperati.COD_ENTE ? listaMittentiRecuperati.COD_ENTE : "";
    result.ipa.desAmm = listaMittentiRecuperati.DES_ENTE ? listaMittentiRecuperati.DES_ENTE : "";
    result.ipa.email = listaMittentiRecuperati.IndirizzoTelematico ? listaMittentiRecuperati.IndirizzoTelematico : "";
    result.ipa.indirizzo = listaMittentiRecuperati.indirizzoPostale ? listaMittentiRecuperati.indirizzoPostale : "";
    result.ipa.ipaCod = listaMittentiRecuperati.COD_ENTE ? listaMittentiRecuperati.COD_ENTE : "";
    result.ipa.ipaDes = listaMittentiRecuperati.DES_ENTE ? listaMittentiRecuperati.DES_ENTE : "";
    result.ipa.ipaType = "amm";
    result.ipa.name = listaMittentiRecuperati.DES_ENTE ? listaMittentiRecuperati.DES_ENTE : "";
    result.ipa.props = {};
    result.ipa.props.PARENTIDS = "";
    result.ipa.props.PHYSICAL_PATH = "";
    result.ipa.props.VIRTUAL_PATH = "";
    result.ipa.props.cod_amm = listaMittentiRecuperati.COD_ENTE ? listaMittentiRecuperati.COD_ENTE : "";
    result.ipa.props.des_amm = listaMittentiRecuperati.DES_ENTE ? listaMittentiRecuperati.DES_ENTE : "";
    result.ipa.props.display_name = listaMittentiRecuperati.DES_ENTE ? listaMittentiRecuperati.DES_ENTE : "";
    result.ipa.props.indirizzo_pec = listaMittentiRecuperati.IndirizzoTelematico ? listaMittentiRecuperati.IndirizzoTelematico : "";
    result.ipa.props.indirizzo = listaMittentiRecuperati.indirizzoPostale ? listaMittentiRecuperati.indirizzoPostale : "";
    result.ipa.props.ipatype = "amm";
    result.ipa.props.name = listaMittentiRecuperati.DES_ENTE ? listaMittentiRecuperati.DES_ENTE : "";
    result.ipa.props.sid = listaMittentiRecuperati.COD_ENTE ? listaMittentiRecuperati.COD_ENTE : "";
    result.mezzo = listaMittentiRecuperati.mezzo ? listaMittentiRecuperati.mezzo : "";
    result.type = "PA";
    return result;
}


function parseItemDestinatariRecuperati(listaDestinatariRecuperati) {
    var result = {};
    result.codiceAOO = listaDestinatariRecuperati.codiceAOO;
    result.codiceAmm = listaDestinatariRecuperati.codiceAmm;
    result.codiceUO = listaDestinatariRecuperati.codiceUO;
    result.competente = listaDestinatariRecuperati.competente;
    result.denominazioneAOO = listaDestinatariRecuperati.denominazioneAOO;
    result.denominazioneAmm = listaDestinatariRecuperati.denominazioneAmm;
    result.denominazioneUO = listaDestinatariRecuperati.denominazioneUO;
    result.tipoCorrispondente = listaDestinatariRecuperati.tipoCorrispondente;
    if(listaDestinatariRecuperati.persona){
        result.id = listaDestinatariRecuperati.persona.identificativo;
        result.persona = {};
        result.persona.denominazione = listaDestinatariRecuperati.persona.denominazione;
        result.persona.identificativo = listaDestinatariRecuperati.persona.identificativo;
        result.persona.tipoCorrispondente = listaDestinatariRecuperati.persona.tipoCorrispondente;
    } else {
        result.id = listaDestinatariRecuperati.codiceUO;
    }
    return result;
}



function getIsoDate(dataString, oraString) {
    var result = "";
    if(dataString) {
        var parts = dataString.match(/(\d+)/g);
        var dataObject = new Date(parts[0], parts[1]-1, parts[2]);
        if (oraString) {
            parts = oraString.match(/(\d+)/g);
            dataObject.setHours(parts[0]);
            dataObject.setMinutes(parts[1]);
            dataObject.setSeconds(parts[2]);
        }
        result = dataObject.toISOString();
    }
    return result;
}
function getDataToIso(isoDate) {
    date = new Date(isoDate);
    year = date.getFullYear();
    month = date.getMonth()+1;
    dt = date.getDate();
    if (dt < 10) {
        dt = '0' + dt;
    }
    if (month < 10) {
        month = '0' + month;
    }
    var anno = year+'-' + month + '-'+dt;
    return anno;
}
function getOraToIso(isoDate) {
    var data = isoDate;
    var ora = (new Date(data).getHours()<10?'0':'') + new Date(data).getHours();
    ora+=":";
    ora+= (new Date(data).getMinutes()<10?'0':'') + new Date(data).getMinutes();
    ora+=":";
    ora+= (new Date(data).getSeconds()<10?'0':'') + new Date(data).getSeconds();
    //console.log("ora: " +ora);
    return ora;
}
function getDataOraToIso(isoDate) {
    var data = isoDate;
    var anno = new Date(data).toLocaleDateString();
    var ora = (new Date(data).getHours()<10?'0':'') + new Date(data).getHours();
    ora+=":";
    ora+= (new Date(data).getMinutes()<10?'0':'') + new Date(data).getMinutes();
    ora+=":";
    ora+= (new Date(data).getSeconds()<10?'0':'') + new Date(data).getSeconds();
    var annoOra = anno + " " + ora;
    //console.log("annoOra: " +annoOra);
    return annoOra;
}


function clearModalInfo() {
    // svuoto e nascondo pannello rubricaIpa
    $("#rubricaIpaSelezionato").hide().val("");
    $(".rubricaIpaSelezionatoResultRemove").trigger("click");
    $("#rubricaIpaNoElement").show();
    // svuoto e nascondo pannello destinatari
    $("#destinatariSelezionato").hide().val("");
    $(".destinatariSelezionatoResultRemove").trigger("click");
    $("#destinatariNoElement").show();
    resetMultiOptions();
    // svuoto e nascondo pannello mittentiPersona
    $("#mittentiPersonaAutocomplete").attr("disabled", "disabled").val("");
    $("#mittentiPersonaAutocomplete-id").val("");
    $("#btnAddContacts").removeAttr("disabled");
    $("#rubrica-input").show();
    $("#ipa-input").hide();
    $("#ipaAutocomplete").val("");
    $("#ipaAutocomplete-id").val("");
    //setto il valore del radio button su rubrica
    $('input:radio[name=gruppo1]')[0].checked = true;
    // svuoto e nascondo pannello protocolloMittente
    $('#protocolloMittente').hide();
    $('#protocolloMittenteUl').empty();
    $('#protocolloMittenteSelezionato').hide();
    $("#btnAddProtocolloMittente").removeAttr("disabled");
    $("#protocolloMittenteNoElement").html("Non ci sono elementi nella lista");
    $('.div-alert-warning > p').remove();
    $('#warningNearSubmit').hide();
    //svuote le liste
    listaMittentiSelezionati = [];
    listaMittenteSelezionato = [];
    listaMittenteSelezionato.push({});
    listaDestinatariSelezionati = [];
    listaClassificaSelezionata = {};
    listaProtocolloMittenteSelezionato = [];
    progressivoProtocolloMittente = 0;
    listaProtocolloMittente = {};
    listaMittentiRecuperati = [];
    listaDestinatariRecuperati = [];
}

function closeClearRubrica() {
    $('#rubrica').modal('toggle');
    $("#modal-amministrazione-input, #modal-aooUfficio-input, #modal-denominazione-input, #modal-piva-input, #modal-cf-input, #modal-indirizzoPostale-input, #modal-pec-input, #modal-peo-input, #modal-fax-input").val("");
}
function closeClearProtocolloMittente() {
    $('#protocolloMittenteModal').modal('toggle');
    $("#modal-numPg-input, #modal-dataPg-input, #modal-classificaPg-input, #modal-fascicoloPg-input").val("");
}


//rimuove contatto rubricaIpa
function btnRemoveContantsClick(elemento) {
    removeElementRubricaIpaForSaveButton(elemento);
    showRubricaIpaAndProtocolloMittente();
}
//rimuove contato IPA
/*function btnRemoveRubricaClick(elemento) {
    removeElementRubricaIpaForSaveButton(elemento);
    $(elemento).closest("li").remove();
    showRubricaIpaAndProtocolloMittente();
}*/
function removeElementRubricaIpaForSaveButton(elemento) {
    var idToRemove = $(elemento).closest("li").find(".rubricaIpaSelezionatoResultInput").val();
    listaMittentiSelezionati = listaMittentiSelezionati.filter(function(item) {
        if(item.COD_RUBRICA){
            return item.COD_RUBRICA !== idToRemove;
        }else{
            return item.ID !== idToRemove;
        }
    });
    resetListaMittenti();

    if(listaMittentiSelezionati.length>0 && $("#versoDocumento-id").val()=="E"){ //se Entrata limito a 1 elemento i mittenti
        $("#btnAddContacts, #btnSelectContacts, #rubricaAutocomplete, #radio1, #radio2").attr("disabled", "disabled");
    } else {
        $("#btnAddContacts, #btnSelectContacts, #rubricaAutocomplete, #radio1, #radio2").removeAttr("disabled");
    }
}
function showRubricaIpaAndProtocolloMittente(){
    if ($('#rubricaIpaUl li').length == 0) {
        $("#rubricaIpaNoElement").show();
        $("#rubricaIpaNoElement").html("Non ci sono elementi nella lista");
        $("#rubricaIpaSelezionato").hide();
    }
    if ($("#versoDocumento-id").val()=="E") {
        if ($(".glyphicon-home")[0] == undefined){
            $('#protocolloMittente').hide();
            $('#protocolloMittenteUl').empty();
            $('#protocolloMittenteSelezionato').hide();
            $("#btnAddProtocolloMittente").removeAttr("disabled");
            $("#protocolloMittenteNoElement").html("Non ci sono elementi nella lista");
        }
    }
}
function resetListaMittenti() {
    listaTemp = listaMittentiSelezionati;
    progressivoMittenti = 0;
    listaMittentiSelezionati = [];
    $(rubricaIpaUl).empty();

    for(i=0;i<listaTemp.length;i++) {
        var utente = listaTemp[i];
        selectMittentiRecuperati(utente);
    }
    //aggiunto if per corretta visualizzazione nel cambio verso protocollo
    $myList = $('#rubricaIpaUl')
    if ( $myList.children().length === 0 ){
        $('#rubricaIpaSelezionato').hide();
        $('#protocolloMittenteNoElement').show();
        $("#protocolloMittenteNoElement").html("Non ci sono elementi nella lista");
    }
}

function removeElementUoForSaveButton(elemento) {
    var idToRemove = $(elemento).closest("li").find(".destinatariSelezionatoResultInput").val();
    listaDestinatariSelezionati = listaDestinatariSelezionati.filter(function(item) {
        if (idToRemove.includes("*")) { //gestione destinatari persona
            idToRemove = idToRemove.split("*").shift();
        }
        return item.id !== idToRemove;
    });
}

function btnRemoveDestinatariClick(elemento) {
    removeElementUoForSaveButton(elemento);
    $(elemento).closest("li").remove();
    if ($('#destinatariUl li').length == 0) {
        $("#destinatariNoElement").show();
        $("#destinatariNoElement").html("Non ci sono elementi nella lista");
        $("#destinatariSelezionato").hide();
    }
}

function btnRemoveProtocolloMittenteClick(elemento) {
    $(elemento).closest("li").remove();
    $("#btnAddProtocolloMittente").removeAttr("disabled");

    if ($('#protocolloMittenteUl li').length == 0) {
        $("#protocolloMittenteNoElement").html("Non ci sono elementi nella lista");
        $("#protocolloMittenteSelezionato").hide();
        //listaProtocolloMittenteSelezionato = [];//BUG - se lascio la lista vuota SOLR lascia i vecchi valori, bisogna quindi settare i valori ""
        listaProtocolloMittenteSelezionato.pop();//levo l'elemento dalla lista
        listaProtocolloMittenteSelezionato.push(parseItemProtocolloMittenteVuoto()); //aggiungo l'elemento ma con i valori "" perchè se non mando a solr "protocolloMittente" lascia i vecchi dati
    }
}

// 1. MODIFICA NOMINATIVO
function btnEditContantsClick(elemento) {

    $('#warningNearSubmit').hide();

    var idProgressivo = $(elemento).find("span").data("progressivo");
    var utente = listaMittentiSelezionati[idProgressivo];

    if ((utente.TIPO_RUBRICA) == "Persona") {
        $('#btnContactsPF').trigger("click");
        $("#btnContactsPF, #btnContactsPG, #btnContactsPA").attr("disabled", "disabled");
        $("#rubrica").modal(
            $("#modal-progressivo-input").val(idProgressivo),
            $("#modal-denominazione-input").val(utente.DES_RUBRICA).removeAttr("disabled"),
            $("#modal-cf-input").val(utente.CODICE_FISCALE).removeAttr("disabled"),
            $("#modal-indirizzoPostale-input").val(utente.INDIRIZZO_POSTALE).removeAttr("disabled"),
            $("#modal-fax-input").val(utente.FAX).removeAttr("disabled"),
            $("#modal-pec-input").val(utente.INDIRIZZO_PEC).removeAttr("disabled"),
            $("#modal-peo-input").val(utente.INDIRIZZO_PEO).removeAttr("disabled"),
            $('.cfCheck').css("color", "grey"),
            $('.denomCheck').css("color", "grey"),
            $('.indirizzoPostaleCheck').css("color", "grey"),
            $("#modal-riferimentoMezzo-input").val(utente.riferimentoMezzo),
            $("#modal-codice-input").val(utente.COD_RUBRICA),
        );
    }
    if ((utente.TIPO_RUBRICA) == "PersonaGiuridica") {
        $('#btnContactsPG').trigger("click");
        $("#btnContactsPF, #btnContactsPG, #btnContactsPA").attr("disabled", "disabled");
        $("#rubrica").modal(
            $("#modal-progressivo-input").val(idProgressivo),
            $("#modal-denominazione-input").val(utente.DES_RUBRICA).removeAttr("disabled"),
            $("#modal-indirizzoPostale-input").val(utente.INDIRIZZO_POSTALE).removeAttr("disabled"),
            $("#modal-piva-input").val(utente.PARTITA_IVA).removeAttr("disabled"),
            $("#modal-pec-input").val(utente.INDIRIZZO_PEC).removeAttr("disabled"),
            $('.pivaCheck').css("color", "grey"),
            $('.denomCheck').css("color", "grey"),
            $('.indirizzoPostaleCheck').css("color", "grey"),
            $("#modal-riferimentoMezzo-input").val(utente.riferimentoMezzo),
            $("#modal-codice-input").val(utente.COD_RUBRICA),
        );
    }
    if ((utente.TIPO_RUBRICA) == "Amministrazione") {
        $('#btnContactsPA').trigger("click");
        $("#btnContactsPF, #btnContactsPG, #btnContactsPA").attr("disabled", "disabled");
        $("#rubrica").modal(
            $("#modal-progressivo-input").val(idProgressivo),
            $("#modal-amministrazione-input").val(utente.DES_RUBRICA).removeAttr("disabled"),
            $("#modal-indirizzoPostale-input").val(utente.INDIRIZZO_POSTALE).removeAttr("disabled"),
            //$("#modal-aooUfficio-input").val(utente.COD_RUBRICA).attr("disabled", "disabled"),
            $("#modal-aooUfficio-input").val(utente.AOO_UFFICIO).removeAttr("disabled"),
            $("#modal-pec-input").val(utente.INDIRIZZO_PEC).removeAttr("disabled"),
            $('.aooCheck').css("color", "grey"),
            $('.ammCheck').css("color", "grey"),
            $("#modal-riferimentoMezzo-input").val(utente.riferimentoMezzo),
            $("#modal-codice-input").val(utente.COD_RUBRICA),
        );
    }

    if (utente.COD_RUBRICA) { //se ho il COD_RUBRICA faccio la query in rubrica per recuperare le informazioni mancanti
        popolateRubricaFromCode(utente.COD_RUBRICA);
    }

}

function addMezzoValue(elemento) {
    $('form .rubricaIpaSelezionatoResultOption').each(function (index) {
        var value = $(this).val();
        if (value) {
            var itemIdElement = $(this).closest("li").find(".itemId").val();
            for (i=0;i<listaMittentiSelezionati.length;i++){
                var tmpElement = listaMittentiSelezionati[i];
                if(tmpElement.COD_RUBRICA) {
                    if (itemIdElement == tmpElement.COD_RUBRICA) {
                        listaMittentiSelezionati[i].riferimentoMezzo = value;
                    }
                } else {
                    if (itemIdElement == tmpElement.ID) {
                        listaMittentiSelezionati[i].riferimentoMezzo = value;
                    }
                }

            }
        }
    });
}


function addCompetenteValue(elemento) {
    if ($('input[name="'+elemento.name+'"]').is(':checked') ) {
        var value = true;
    } else {
        var value = false;
    }
    var idToChange = $(elemento).closest("li").find(".destinatariSelezionatoResultInput").val();
    for (i=0;i<listaDestinatariSelezionati.length;i++){
        var tmpElement = listaDestinatariSelezionati[i];
        if (idToChange.includes("*")) { //gestione destinatari persona
            idToChange = idToChange.split("*").shift();
        }
        if (idToChange == tmpElement.id) {
            listaDestinatariSelezionati[i].competente = value;
        }
    }
}

function btnEditProtocolloMittenteClick(elemento) {
    var idProgressivo = $(elemento).find("span").data("progressivo");
    var utente = listaProtocolloMittente[idProgressivo];
    //disabilito il tasto Aggiungi se un protocollo mittente è stato inserito
    $("#btnAddProtocolloMittente").attr("disabled", "disabled");
    $("#protocolloMittenteModal").modal(
        $("#modal-progressivoPm-input").val(idProgressivo),
        $("#modal-numPg-input").val(utente.numero),
        $("#modal-dataPg-input").val(utente.data),
        $("#modal-classificaPg-input").val(utente.classifica),
        $("#modal-fascicoloPg-input").val(utente.fascicolo),
    );
}

function btnSelectNewContactsClick(elemento) {
    $("#rubricaIpaSelezionato").show();

    var progressivo = progressivoMittenti;
    progressivoMittenti = progressivoMittenti + 1;
    var htmlElement = $("#ipaTemplate").clone();
    htmlElement.removeClass("hidden");
    htmlElement.removeAttr("id");
    htmlElement.attr("id","mittente"+progressivo);
    addMittente(htmlElement, progressivo);
    //remove
    htmlElement.find(".rubricaIpaSelezionatoResultRemove").on("click", function () {
        btnRemoveContantsClick(this);
    });
    //edit
    htmlElement.find(".rubricaIpaSelezionatoResult").on("click", function () {
        btnEditContantsClick(this);
    });

    htmlElement.find(".rubricaIpaSelezionatoResultOption").on("change", function () {
        addMezzoValue(this);
    });

}

function selectMittentiRecuperati(utente) {
    $("#rubricaIpaNoElement").hide();
    $("#rubricaIpaSelezionato").show();

    var progressivo = progressivoMittenti;
    progressivoMittenti = progressivoMittenti + 1;
    var htmlElement = $("#ipaTemplate").clone();
    htmlElement.removeClass("hidden");
    htmlElement.removeAttr("id");
    htmlElement.attr("id","mittente"+progressivo);
    addMittenteRecuperato(htmlElement, progressivo, utente);

    // se protocollo in Entrata e il mittente è una IPA o Amministrazione abilito il box protocolloMittente per poter aggiungere eventuali metadati relativi l'IPA
    if ($("#versoDocumento-id").val()=="E") {
        if ($(".glyphicon-home")[0]){ //se è una IPA o Amministrazione l'icona nella pagina accanto al nome è glyphicon-home
            $('#protocolloMittente').show();
        }
    }

    //remove
    htmlElement.find(".rubricaIpaSelezionatoResultRemove").on("click", function () {
        btnRemoveContantsClick(this);
    });
    //edit
    htmlElement.find(".rubricaIpaSelezionatoResult").on("click", function () {
        btnEditContantsClick(this);
    });

    htmlElement.find(".rubricaIpaSelezionatoResultOption").on("change", function () {
        addMezzoValue(this);
    });

}

function selectDestinatariRecuperati(utente) { //prova A
    $("#destinatariNoElement").hide();
    $("#destinatariSelezionato").show();
    progressivoDestinatari = progressivoDestinatari + 1;
    var progressivo = progressivoDestinatari;
    var htmlElement = $("#destinatariTemplate").clone();
    htmlElement.removeClass("hidden");
    htmlElement.removeAttr("id");
    htmlElement.attr("id","destinatario"+progressivo);
    addDestinatarioRecuperato(htmlElement, progressivo, utente);
    //remove
    htmlElement.find(".destinatariSelezionatoResultRemove").on("click", function () {
        btnRemoveDestinatariClick(this);
    });
    htmlElement.find(".destinatariSelezionatoResultRadioInput").on("click", function () {
        addCompetenteValue(this);
    });

}

function btnSelectNewProtocolloMittenteClick(elemento) {
    $("#protocolloMittenteSelezionato").show();
    progressivoProtocolloMittente = progressivoProtocolloMittente + 1;
    var progressivo = progressivoProtocolloMittente;
    var htmlElement = $("#protocolloMittenteTemplate").clone();
    htmlElement.removeClass("hidden");
    htmlElement.removeAttr("id");
    htmlElement.attr("id","protocolloMittente"+progressivo);
    addProtocolloMittente(htmlElement, progressivo);
    //remove
    htmlElement.find(".protocolloMittenteSelezionatoResultRemove").on("click", function () {
        btnRemoveProtocolloMittenteClick(this);
    });
    //edit
    htmlElement.find(".protocolloMittenteSelezionatoResult").on("click", function () {
        btnEditProtocolloMittenteClick(this);
    });
    $("#protocolloMittenteUl").append(htmlElement);
    //disabilito il tasto Aggiungi se un protocollo mittente è stato inserito
    $("#btnAddProtocolloMittente").attr("disabled", "disabled");
}

// 1. INSERIMENTO NOMINATIVO DA MODALE
// 2. MODIFICA NOMINATIVO DA MODALE
function addMittente(htmlElement, progressivo){

    TIPO_RUBRICA = $("#modal-tipologia-input").val();

    var utente = {};
    utente.COD_RUBRICA = $("#modal-codice-input").val();
    utente.INDIRIZZO_PEC = $("#modal-pec-input").val();
    utente.INDIRIZZO_POSTALE = $("#modal-indirizzoPostale-input").val();
    utente.TIPO_RUBRICA = $("#modal-tipologia-input").val();
    utente.riferimentoMezzo = $("#modal-riferimentoMezzo-input").val();
    tipoInvio = utente.riferimentoMezzo ? utente.riferimentoMezzo : "-- mezzo --";

    if(TIPO_RUBRICA=="Persona"){
        utente.DES_RUBRICA = $("#modal-denominazione-input").val();
        utente.CODICE_FISCALE = $("#modal-cf-input").val();
        utente.INDIRIZZO_PEO = $("#modal-peo-input").val();
        utente.FAX = $("#modal-fax-input").val();
        if($("#modal-cf-input").val()){
            utente.ID = $("#modal-cf-input").val();
        } else if($("#modal-pec-input").val()){
            utente.ID = $("#modal-pec-input").val();
        } else if($("#modal-peo-input").val()){
            utente.ID = $("#modal-peo-input").val();
        } else if($("#modal-fax-input").val()){
            utente.ID = $("#modal-fax-input").val();
        }
    }
    if(TIPO_RUBRICA=="PersonaGiuridica"){
        utente.DES_RUBRICA = $("#modal-denominazione-input").val();
        utente.PARTITA_IVA = $("#modal-piva-input").val();
        if($("#modal-piva-input").val()){
            utente.ID = $("#modal-piva-input").val();
        } else if($("#modal-pec-input").val()){
            utente.ID = $("#modal-pec-input").val();
        }
    }
    if(TIPO_RUBRICA=="Amministrazione"){
        utente.AOO_UFFICIO = $("#modal-aooUfficio-input").val();
        utente.DES_RUBRICA = $("#modal-amministrazione-input").val();
        if($("#modal-pec-input").val()){
            utente.ID = $("#modal-pec-input").val();
        } else if($("#modal-amministrazione-input").val()){
            utente.ID = $("#modal-amministrazione-input").val();
        }
    }

    var mittenteTrovato = false;
    for(i=0;i<listaMittentiSelezionati.length;i++) {
        var mittente = listaMittentiSelezionati[i];
        if(mittente.COD_RUBRICA==utente.COD_RUBRICA){
            mittenteTrovato=true;
        }
    }
    listaMittentiSelezionati[progressivo] = utente;

    if ($("#btnContactsPF").hasClass("btn-primary")) {
        var icona = "glyphicon glyphicon-user";
        if (utente.CODICE_FISCALE) {
            htmlElement.find(".rubricaIpaSelezionatoResultNameInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".cf").val(utente.CODICE_FISCALE);
        }
        if (utente.INDIRIZZO_PEO) {
            htmlElement.find(".rubricaIpaSelezionatoResultPeo").html("<span class=\"indirizzoTelematico\">(PEO)</span>" + utente.INDIRIZZO_PEO);
            htmlElement.find(".rubricaIpaSelezionatoResultPeoInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".peo").val(utente.INDIRIZZO_PEO);
        }
        if (utente.FAX) {//nascosto nell'html
            htmlElement.find(".rubricaIpaSelezionatoResultFaxInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".fax").val(utente.FAX);
        }
    }
    if ($("#btnContactsPG").hasClass("btn-primary")) {
        var icona = "glyphicon glyphicon-pencil";
        if (utente.PARTITA_IVA) {
            htmlElement.find(".rubricaIpaSelezionatoResultNameInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".piva").val(utente.PARTITA_IVA);
        }
    }
    if ($("#btnContactsPA").hasClass("btn-primary")) {
        var icona = "glyphicon glyphicon-home";
        if (utente.AOO_UFFICIO) {
            htmlElement.find(".rubricaIpaSelezionatoResultNameInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".aooUfficio").val(utente.AOO_UFFICIO);
        }
    }

    htmlElement.find(".rubricaIpaSelezionatoResult").html("<a style=\"color:blue;cursor:pointer;\"><span data-progressivo=\"" + progressivo + "\" style=\"color:black\" class=\""+icona+"\"></span> " + utente.DES_RUBRICA + "</a>");
    htmlElement.find(".rubricaIpaSelezionatoResultInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".id");
    htmlElement.find(".rubricaIpaSelezionatoResultName").html(utente.ID); //campo visibile sotto il nominativo
    if (utente.DES_RUBRICA) {//nascosto nell'html
        htmlElement.find(".rubricaIpaSelezionatoResultDenominazioneInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".denominazione").val(utente.DES_RUBRICA);
    }
    if (utente.COD_RUBRICA) {
        htmlElement.find(".rubricaIpaSelezionatoResultInput").val(utente.COD_RUBRICA);
    } else {
        htmlElement.find(".rubricaIpaSelezionatoResultInput").val(utente.ID);
    }
    if (utente.INDIRIZZO_PEC) {
        htmlElement.find(".rubricaIpaSelezionatoResultPec").html("<span class=\"indirizzoTelematico\">(PEC)</span>" + utente.INDIRIZZO_PEC);
        htmlElement.find(".rubricaIpaSelezionatoResultPecInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".pec").val(utente.INDIRIZZO_PEC);
    }
    if (utente.INDIRIZZO_POSTALE) {
        htmlElement.find(".rubricaIpaSelezionatoResultIndirizzoPostale").html(utente.INDIRIZZO_POSTALE);
        htmlElement.find(".rubricaIpaSelezionatoResultIndirizzoPostaleInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".indirizzoPostale").val(utente.INDIRIZZO_POSTALE);
    }
    htmlElement.find(".rubricaIpaSelezionatoResultIdInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".progressivo").val(progressivo);
    htmlElement.find(".rubricaIpaSelezionatoResultTipologiaInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".tipologia").val(utente.TIPO_RUBRICA);
    //select
    htmlElement.find(".rubricaIpaSelezionatoResultOption").attr("name", "mittentiProtocollo$$$" + progressivo + ".tipoInvio");
    htmlElement.find(".rubricaIpaSelezionatoResultOption").empty().append(new Option("-- mezzo --", "-- mezzo --")); //resetto la select per l'edit
    if(utente.INDIRIZZO_PEC){
        htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("PEC", "PEC"));
    }
    if(typeof utente.INDIRIZZO_PEO !== 'undefined'){
        if(utente.INDIRIZZO_PEO){
            htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("PEO", "PEO"));
        }
    }
    if(utente.INDIRIZZO_POSTALE){
        htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Posta", "Posta"));
    }
    if (typeof utente.FAX !== 'undefined'){
        if(utente.FAX){
            htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("FAX", "FAX"));
        }
    }
    htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Brevimano", "Brevimano"));
    htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Altro", "Altro"));
    //fine select
    htmlElement.find(".rubricaIpaSelezionatoResultOption").val(tipoInvio); //inserisco il tipo invio esistente nella select

    if (!mittenteTrovato) {//se esiste non lo aggiungo alla lista
        $("#rubricaIpaUl").append(htmlElement);
    }
    resetListaMittenti();
    $("#modal-riferimentoMezzo-input").val("-- mezzo --");//imposto il campo di default per nuovi inserimenti
    if(listaMittentiSelezionati.length>0 && $("#versoDocumento-id").val()=="E"){ //se Entrata limito a 1 elemento i mittenti
        $("#btnAddContacts, #btnSelectContacts, #rubricaAutocomplete, #radio1, #radio2").attr("disabled", "disabled");
    } else {
        $("#btnAddContacts, #btnSelectContacts, #rubricaAutocomplete, #radio1, #radio2").removeAttr("disabled");
    }
}


// 1. INSERIMENTO NOMINATIVO DA MODALE
// 2. MODIFICA NOMINATIVO DA MODALE
function addMittenteRecuperato(htmlElement, progressivo, utente){

    INDIRIZZO_POSTALE = utente.INDIRIZZO_POSTALE ? utente.INDIRIZZO_POSTALE : utente.indirizzoPostale;
    TIPO_RUBRICA = utente.TIPO_RUBRICA ? utente.TIPO_RUBRICA : utente.tipoCorrispondente;
    COD_RUBRICA = utente.COD_RUBRICA ? utente.COD_RUBRICA : utente.codiceRubrica;
    if(TIPO_RUBRICA=="Persona"){
        DES_RUBRICA = utente.DES_RUBRICA ? utente.DES_RUBRICA : utente.denominazione;
        CODICE_FISCALE = utente.CODICE_FISCALE ? utente.CODICE_FISCALE : "";
        if(utente.mezzo == "PEC"){
            INDIRIZZO_PEC = utente.INDIRIZZO_PEC ? utente.INDIRIZZO_PEC : utente.indirizzoTelematico;
            INDIRIZZO_PEO = utente.INDIRIZZO_PEO ? utente.INDIRIZZO_PEO : "";
            FAX = utente.FAX ? utente.FAX : "";
        } else if(utente.mezzo == "PEO"){
            INDIRIZZO_PEO = utente.INDIRIZZO_PEO ? utente.INDIRIZZO_PEO : utente.indirizzoTelematico;
            INDIRIZZO_PEC = utente.INDIRIZZO_PEC ? utente.INDIRIZZO_PEC : "";
            FAX = utente.FAX ? utente.FAX : "";
        } else if(utente.mezzo == "FAX"){
            FAX = utente.FAX ? utente.FAX : utente.indirizzoTelematico;
            INDIRIZZO_PEC = utente.INDIRIZZO_PEC ? utente.INDIRIZZO_PEC : "";
            INDIRIZZO_PEO = utente.INDIRIZZO_PEO ? utente.INDIRIZZO_PEO : "";
        } else {
            FAX = utente.FAX ? utente.FAX : "";
            INDIRIZZO_PEC = utente.INDIRIZZO_PEC ? utente.INDIRIZZO_PEC : "";
            INDIRIZZO_PEO = utente.INDIRIZZO_PEO ? utente.INDIRIZZO_PEO : "";
        }
        if($("#modal-cf-input").val()){
            ID = $("#modal-cf-input").val();
        } else if($("#modal-pec-input").val()){
            ID = $("#modal-pec-input").val();
        } else if($("#modal-peo-input").val()){
            ID = $("#modal-peo-input").val();
        } else if($("#modal-fax-input").val()){
            ID = $("#modal-fax-input").val();
        }else{
            ID = utente.ID ? utente.ID : utente.identificativo;
        }
    }
    if(TIPO_RUBRICA=="PersonaGiuridica"){
        DES_RUBRICA = utente.DES_RUBRICA ? utente.DES_RUBRICA : utente.denominazione;
        PARTITA_IVA = utente.PARTITA_IVA ? utente.PARTITA_IVA : "";
        INDIRIZZO_PEC = utente.INDIRIZZO_PEC ? utente.INDIRIZZO_PEC : utente.indirizzoTelematico;

        if($("#modal-piva-input").val()){
            ID = $("#modal-piva-input").val(); //dall'autocomplete (utente.) non ho questa informazione
        } else if($("#modal-pec-input").val()){
            ID = $("#modal-pec-input").val();
        }else{
            ID = utente.ID ? utente.ID : utente.identificativo;
        }
    }
    if(TIPO_RUBRICA=="Amministrazione"){
        DES_RUBRICA = utente.DES_RUBRICA ? utente.DES_RUBRICA : utente.denominazioneAmm;
        AOO_UFFICIO = utente.AOO_UFFICIO ? utente.AOO_UFFICIO : utente.denominazioneUO;
        INDIRIZZO_PEC = utente.INDIRIZZO_PEC ? utente.INDIRIZZO_PEC : utente.indirizzoTelematico;
        ID = utente.ID ? utente.ID : utente.indirizzoTelematico;
    }
    if (utente.mezzo){
        tipoInvio = utente.mezzo ? utente.mezzo : "-- mezzo --";
    }
    if (utente.riferimentoMezzo){
        tipoInvio = utente.riferimentoMezzo ? utente.riferimentoMezzo : "-- mezzo --";
        if (tipoInvio=="PEO" && $('#modal-peo-input').val()==""){
            tipoInvio = "-- mezzo --";
            $('#modal-riferimentoMezzo-input').val("");
        }
        if (tipoInvio=="PEC" && $('#modal-pec-input').val()==""){
            tipoInvio = "-- mezzo --";
            $('#modal-riferimentoMezzo-input').val("");
        }
        if (tipoInvio=="FAX" && $('#modal-fax-input').val()==""){
            tipoInvio = "-- mezzo --";
            $('#modal-riferimentoMezzo-input').val("");
        }
    }

    if (TIPO_RUBRICA == "Persona") {
        var icona = "glyphicon glyphicon-user";
        if (CODICE_FISCALE) {
            htmlElement.find(".rubricaIpaSelezionatoResultNameInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".cf").val(CODICE_FISCALE);
        }
        if (INDIRIZZO_PEO) {
            htmlElement.find(".rubricaIpaSelezionatoResultPeo").html("<span class=\"indirizzoTelematico\">(PEO)</span>" + INDIRIZZO_PEO);
            htmlElement.find(".rubricaIpaSelezionatoResultPeoInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".peo").val(INDIRIZZO_PEO);
        }
        if (FAX) {//nascosto nell'html
            htmlElement.find(".rubricaIpaSelezionatoResultFaxInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".fax").val(FAX);
        }
    }
    if (TIPO_RUBRICA == "PersonaGiuridica") {
        var icona = "glyphicon glyphicon-pencil";
        if (PARTITA_IVA) {
            htmlElement.find(".rubricaIpaSelezionatoResultNameInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".piva").val(PARTITA_IVA);
        }
    }
    if (TIPO_RUBRICA == "Amministrazione") {
        var icona = "glyphicon glyphicon-home";
        if (AOO_UFFICIO) {
            htmlElement.find(".rubricaIpaSelezionatoResultNameInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".aooUfficio").val(AOO_UFFICIO);
        }
    }

    htmlElement.find(".rubricaIpaSelezionatoResult").html("<a style=\"color:blue;cursor:pointer;\"><span data-progressivo=\"" + progressivo + "\" style=\"color:black\" class=\""+icona+"\"></span> " + DES_RUBRICA + "</a>");
    htmlElement.find(".rubricaIpaSelezionatoResultInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".id");
    htmlElement.find(".rubricaIpaSelezionatoResultName").html(ID); //campo visibile sotto il nominativo
    if (DES_RUBRICA) {//nascosto nell'html
        htmlElement.find(".rubricaIpaSelezionatoResultDenominazioneInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".denominazione").val(DES_RUBRICA);
    }
    if (COD_RUBRICA) {
        htmlElement.find(".rubricaIpaSelezionatoResultInput").val(COD_RUBRICA);
    } else {
        htmlElement.find(".rubricaIpaSelezionatoResultInput").val(ID);
    }
    if (INDIRIZZO_PEC) {
        htmlElement.find(".rubricaIpaSelezionatoResultPec").html("<span class=\"indirizzoTelematico\">(PEC)</span>" + INDIRIZZO_PEC);
        htmlElement.find(".rubricaIpaSelezionatoResultPecInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".pec").val(INDIRIZZO_PEC);
    }
    if (INDIRIZZO_POSTALE) {
        htmlElement.find(".rubricaIpaSelezionatoResultIndirizzoPostale").html(INDIRIZZO_POSTALE);
        htmlElement.find(".rubricaIpaSelezionatoResultIndirizzoPostaleInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".indirizzoPostale").val(INDIRIZZO_POSTALE);
    }
    htmlElement.find(".rubricaIpaSelezionatoResultIdInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".progressivo").val(progressivo);
    htmlElement.find(".rubricaIpaSelezionatoResultTipologiaInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".tipologia").val(TIPO_RUBRICA);
    //select
    htmlElement.find(".rubricaIpaSelezionatoResultOption").attr("name", "mittentiProtocollo$$$" + progressivo + ".tipoInvio");
    htmlElement.find(".rubricaIpaSelezionatoResultOption").empty().append(new Option("-- mezzo --", "-- mezzo --")); //resetto la select per l'edit
    if (INDIRIZZO_PEC){
        htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("PEC", "PEC"));
    }
    if (typeof INDIRIZZO_PEO !== 'undefined'){
        if(INDIRIZZO_PEO){
            htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("PEO", "PEO"));
        }
    }
    if (INDIRIZZO_POSTALE){
        htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Posta", "Posta"));
    }
    if (typeof FAX !== 'undefined'){
        if(FAX){
            htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("FAX", "FAX"));
        }
    }
    htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Brevimano", "Brevimano"));
    htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Altro", "Altro"));
    //fine select
    htmlElement.find(".rubricaIpaSelezionatoResultOption").val(tipoInvio); //inserisco il tipo invio esistente
    listaMittentiSelezionati.push(parseItemMittentiRecuperati(utente));
    $("#rubricaIpaUl").append(htmlElement);

    if(listaMittentiSelezionati.length>0 && $("#versoDocumento-id").val()=="E"){ //se Entrata limito a 1 elemento i mittenti
        $("#btnAddContacts, #btnSelectContacts, #rubricaAutocomplete, #radio1, #radio2").attr("disabled", "disabled");
    } else {
        $("#btnAddContacts, #btnSelectContacts, #rubricaAutocomplete, #radio1, #radio2").removeAttr("disabled");
    }
}


//gestione elementi lista destinatari
function btnSelectDestinatarioClick(elemento) {
    $("#destinatariSelezionato").show();
    progressivoDestinatari = progressivoDestinatari + 1;
    var progressivo = progressivoDestinatari;
    var htmlElement = $("#destinatariTemplate").clone();
    htmlElement.removeClass("hidden");
    htmlElement.removeAttr("id");

    //se ho selezionato un utente lo inserisco nella lista
    if (untitaOrganizzativaDetail != null) {
        htmlElement.find(".destinatariSelezionatoResult").html("<span style=\"color:black\" class=\"glyphicon glyphicon-calendar\"></span> " + untitaOrganizzativaDetail);
        htmlElement.find(".destinatariSelezionatoResultInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".id").val(elemento.USER_ID);
        htmlElement.find(".destinatariSelezionatoResultName").html("<span style=\"color:black\" class=\"glyphicon glyphicon-user\"></span> " + elemento.FULL_NAME);
        htmlElement.find(".destinatariSelezionatoResultNameInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".idUtente").val(elemento.USER_ID);
    } else { //se ho solo l'UO
        htmlElement.find(".destinatariSelezionatoResult").html("<span style=\"color:black\" class=\"glyphicon glyphicon-calendar\"></span> " + elemento.GROUP_NAME);
        htmlElement.find(".destinatariSelezionatoResultInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".id").val(elemento.GROUP_ID);
        // setto idUtente = ""
        htmlElement.find(".destinatariSelezionatoResultName").html("");
        htmlElement.find(".destinatariSelezionatoResultNameInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".idUtente").val("");
    }

    if (elemento.competente==true){
        htmlElement.find(".destinatariSelezionatoResultRadioInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".radio").attr("checked",true).val(true);
    } else {
        htmlElement.find(".destinatariSelezionatoResultRadioInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".radio").attr("checked",false).val(false);
    }
    htmlElement.find(".destinatariSelezionatoResultIdInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".progressivo").val(progressivo);
    //remove
    htmlElement.find(".destinatariSelezionatoResultRemove").on("click", function () {
        btnRemoveDestinatariClick(this);
    });
    htmlElement.find(".destinatariSelezionatoResultRadioInput").on("click", function () {
        addCompetenteValue(this);
    });

    $("#destinatariUl").append(htmlElement);
    destinatarioDetailList = [];
    untitaOrganizzativaDetail = null;
    //azzero e disabilito i campi unità organizzativa e utente
    $("#destinatariAutocomplete").val("");
    $("#destinatariAutocomplete-id").val("");
}
//fine gestione elementi lista destinatari


function btnSelectDestinatarioClickMulti(elemento) {

    var descrizioneUO = $('#destinatariAutocomplete').val();
    var idUo = $('#destinatariAutocomplete-id').val();
    var untitaOrganizzativaDetail = $('#destinatariPersone-id').val();
    var nomeDaIdPersona = elemento.split("*").pop();
    var idDaIdPersona = elemento.split("*").shift();
    //nomeDaIdPersona = nomeDaIdPersona.toLowerCase().replace(/\b[a-z]/g, function(letter) {
    //    return letter.toUpperCase();
    //});

    $("#destinatariSelezionato").show();
    progressivoDestinatari = progressivoDestinatari + 1;
    var progressivo = progressivoDestinatari;
    var htmlElement = $("#destinatariTemplate").clone();
    htmlElement.removeClass("hidden");
    htmlElement.removeAttr("id");

    htmlElement.find(".destinatariSelezionatoResult").html("<span style=\"color:black\" class=\"glyphicon glyphicon-calendar\"></span> " + descrizioneUO);
    htmlElement.find(".destinatariSelezionatoResultInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".id").val(elemento);
    htmlElement.find(".destinatariSelezionatoResultName").html("<span style=\"color:black\" class=\"glyphicon glyphicon-user\"></span> " + nomeDaIdPersona);
    htmlElement.find(".destinatariSelezionatoResultNameInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".idUtente").val(idDaIdPersona);
    htmlElement.find(".destinatariSelezionatoResultIdInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".progressivo").val(progressivo);
    if (listaDestinatariSelezionati.length==1){ //se c'è solo un elemento è quello che sto inserento e metto true il check
        htmlElement.find(".destinatariSelezionatoResultRadioInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".radio").attr("checked",true).val(true);
    } else {
        htmlElement.find(".destinatariSelezionatoResultRadioInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".radio").attr("checked",false).val(false);
    }
    //remove
    htmlElement.find(".destinatariSelezionatoResultRemove").on("click", function () {
        btnRemoveDestinatariClick(this);
    });
    htmlElement.find(".destinatariSelezionatoResultRadioInput").on("click", function () {
        addCompetenteValue(this);
    });

    $("#destinatariUl").append(htmlElement);
}

function addDestinatarioPersonaRecuperato(utente) {
    indirizzoTelematico = utente.indirizzoTelematico;
    COD_UO = utente.codiceUO;
    DES_UO = utente.denominazioneUO;
    if(utente.persona){
        persona_id = utente.persona.identificativo;
        persona_name = utente.persona.denominazione;
    } else {
        persona_id = "";
        persona_name = "";
    }
    $("#mittentiAutocomplete").val(DES_UO);
    $("#mittentiAutocomplete-id").val(COD_UO);
    if (persona_id) {
        $("#mittentiPersonaAutocomplete").removeAttr("disabled");
        $("#mittentiPersonaAutocomplete").val(persona_id);
        $("#mittentiPersonaAutocomplete-id").val(persona_name);
    }
    //il valore recuperato dell'email lo inserisco dall'if dentro la funzione successRecuperoDatiUser
    listaMittenteSelezionato[0] = parseItemMittentePersonaRecuperato(utente);
}

//destinatari recuperati all'apertura pagina
function addDestinatarioRecuperato(htmlElement, progressivo, utente) {
    GROUP_ID = utente.codiceUO;
    GROUP_NAME = utente.denominazioneUO;
    if(utente.persona){
        USER_ID = utente.persona.identificativo;
        FULL_NAME = utente.persona.denominazione;
    }
    //se ho selezionato un utente lo inserisco nella lista
    if (utente.persona) {
        htmlElement.find(".destinatariSelezionatoResult").html("<span style=\"color:black\" class=\"glyphicon glyphicon-calendar\"></span> " + GROUP_NAME);
        htmlElement.find(".destinatariSelezionatoResultInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".id").val(USER_ID);
        htmlElement.find(".destinatariSelezionatoResultName").html("<span style=\"color:black\" class=\"glyphicon glyphicon-user\"></span> " + FULL_NAME);
        htmlElement.find(".destinatariSelezionatoResultNameInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".idUtente").val(USER_ID);
    } else { //se ho solo l'UO
        htmlElement.find(".destinatariSelezionatoResult").html("<span style=\"color:black\" class=\"glyphicon glyphicon-calendar\"></span> " + GROUP_NAME);
        htmlElement.find(".destinatariSelezionatoResultInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".id").val(GROUP_ID);
    }
    htmlElement.find(".destinatariSelezionatoResultIdInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".progressivo").val(progressivo);


    if (utente.competente==true){
        htmlElement.find(".destinatariSelezionatoResultRadioInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".radio").attr("checked",true).val(true);
    } else {
        htmlElement.find(".destinatariSelezionatoResultRadioInput").attr("name", "destinatariProtocollo$$$" + progressivo + ".radio").attr("checked",false).val(false);
    }


    listaDestinatariSelezionati.push(parseItemDestinatariRecuperati(utente));
    //listaDestinatariSelezionati.push(utente);

    $("#destinatariUl").append(htmlElement);
    destinatarioDetailList = [];
    untitaOrganizzativaDetail = null;
}

function addProtocolloMittente(htmlElement, progressivo){
    //if (dataPgMittente_DOC == "") {
    if (!dataPgMittente_DOC) {
        numeroPgM = $("#modal-numPg-input").val();
        dataPgM = $("#modal-dataPg-input").val();
        classificaPgM = $("#modal-classificaPg-input").val();
        fascicoloPgM = $("#modal-fascicoloPg-input").val();
    } else {
        numeroPgM = numPgMittente_DOC;
        dataPgM = getDataToIso(dataPgMittente_DOC);
        classificaPgM = classificaMittente_DOC;
        fascicoloPgM = fascicoloMittente_DOC;
    }

    var protocolloMittente = {};
    protocolloMittente.numero = numeroPgM;
    protocolloMittente.data = dataPgM;
    protocolloMittente.classifica = classificaPgM;
    protocolloMittente.fascicolo = fascicoloPgM;
    listaProtocolloMittente[progressivo] = protocolloMittente;

    htmlElement.find(".protocolloMittenteSelezionatoResult").html("<a style=\"color:blue;cursor:pointer;\"><span data-progressivo=\"" + progressivo + "\">Protocollo N.</span> " + numeroPgM + "</a>");
    htmlElement.find(".protocolloMittenteSelezionatoResultInput").attr("name", "protocollo.numPg");
    htmlElement.find(".protocolloMittenteSelezionatoResultInput").val(numeroPgM);
    htmlElement.find(".protocolloMittenteSelezionatoResultData").html(" del <b>"+dataPgM+"</b>");
    htmlElement.find(".protocolloMittenteSelezionatoResultDataInput").attr("name", "protocollo.dataPg");
    htmlElement.find(".protocolloMittenteSelezionatoResultDataInput").val(dataPgM);
    htmlElement.find(".protocolloMittenteSelezionatoResultClassificaInput").attr("name", "protocollo.classificaPg");
    htmlElement.find(".protocolloMittenteSelezionatoResultClassificaInput").val(classificaPgM);
    htmlElement.find(".protocolloMittenteSelezionatoResultFascicoloInput").attr("name", "protocollo.fascicoloPg");
    htmlElement.find(".protocolloMittenteSelezionatoResultFascicoloInput").val(fascicoloPgM);
}

//gestione elementi lista Autocomplete IPA e Rubrica
// 1. INSERIMENTO NOMINATIVO DA AUTOCOMPLETE
// 2. MODIFICA NOMINATIVO DA AUTOCOMPLETE
function btnSelectContactsClick(elemento) {

    $("#rubricaIpaSelezionato").show();

    var progressivo = progressivoMittenti;
    progressivoMittenti = progressivoMittenti + 1;
    var htmlElement = $("#ipaTemplate").clone();
    htmlElement.removeClass("hidden");
    htmlElement.removeAttr("id");

    //se rubrica
    if ($('#ipaAutocomplete-id').val() == "") {

        if(elemento.TIPO_RUBRICA=="Persona"){
            var icona = "glyphicon glyphicon-user";
        }
        if(elemento.TIPO_RUBRICA=="PersonaGiuridica"){
            var icona = "glyphicon glyphicon-pencil";
        }
        if(elemento.TIPO_RUBRICA=="Amministrazione"){
            // non cambiare icona glyphicon-home, serve per la visualizzazione del box Protocollo Mittente aggiuntivo in caso di IPA
            var icona = "glyphicon glyphicon-home";
        }
        htmlElement.find(".rubricaIpaSelezionatoResult").html("<a style=\"color:blue;cursor:pointer;\"><span data-progressivo=\"" + progressivo + "\" style=\"color:black\" class=\""+icona+"\"></span> " + elemento.DES_RUBRICA + "</a>");
        htmlElement.find(".rubricaIpaSelezionatoResultInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".id").val(elemento.COD_RUBRICA);
        if(elemento.INDIRIZZO_PEC){
            htmlElement.find(".rubricaIpaSelezionatoResultPec").html("<span class=\"indirizzoTelematico\">(PEC)</span>" + elemento.INDIRIZZO_PEC);
            htmlElement.find(".rubricaIpaSelezionatoResultPecInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".pec").val(elemento.INDIRIZZO_PEC);
        }
        if(elemento.INDIRIZZO_PEO){
            htmlElement.find(".rubricaIpaSelezionatoResultPeo").html("<span class=\"indirizzoTelematico\">(PEO)</span>" + elemento.INDIRIZZO_PEO);
            htmlElement.find(".rubricaIpaSelezionatoResultPeoInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".peo").val(elemento.INDIRIZZO_PEO);
        }
        if(elemento.INDIRIZZO_POSTALE){
            htmlElement.find(".rubricaIpaSelezionatoResultIndirizzoPostale").html(elemento.INDIRIZZO_POSTALE);
            htmlElement.find(".rubricaIpaSelezionatoResultIndirizzoPostaleInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".indirizzoPostale").val(elemento.INDIRIZZO_POSTALE);
        }
        if(elemento.FAX){
            htmlElement.find(".rubricaIpaSelezionatoResultFaxInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".fax").val(elemento.FAX);
        }
        htmlElement.find(".rubricaIpaSelezionatoResultName").html(elemento.ID);//campo visibile sotto il nominativo
        htmlElement.find(".rubricaIpaSelezionatoResultIdInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".progressivo").val(progressivo);
        //select
        htmlElement.find(".rubricaIpaSelezionatoResultOption").attr("name", "mittentiProtocollo$$$" + progressivo + ".tipoInvio");
        if(elemento.INDIRIZZO_PEC){
            htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("PEC", "PEC"));
        }
        if(elemento.INDIRIZZO_PEO){
            htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("PEO", "PEO"));
        }
        if(elemento.INDIRIZZO_POSTALE){
            htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Posta", "Posta"));
        }
        if(elemento.FAX){
            htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("FAX", "FAX"));
        }
        htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Brevimano", "Brevimano"));
        htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Altro", "Altro"));
        //fine select
    }

    //se ipa
    if ($('#rubricaAutocomplete-id').val() == "") {
        // non cambiare icona glyphicon-home, serve per la visualizzazione del box Protocollo Mittente aggiuntivo in caso di IPA
        htmlElement.find(".rubricaIpaSelezionatoResult").html("<span data-progressivo=\"" + progressivo + "\" style=\"color:black\" class=\"glyphicon glyphicon-home\"></span> " + elemento.DES_RUBRICA );
        htmlElement.find(".rubricaIpaSelezionatoResultInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".id").val(elemento.COD_RUBRICA);
        htmlElement.find(".rubricaIpaSelezionatoResultPec").html("<span class=\"indirizzoTelematico\">(PEC)</span>" + elemento.INDIRIZZO_PEC);
        htmlElement.find(".rubricaIpaSelezionatoResultPecInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".pec").val(elemento.INDIRIZZO_PEC);
        htmlElement.find(".rubricaIpaSelezionatoResultName").html(elemento.COD_RUBRICA);
        //select
        htmlElement.find(".rubricaIpaSelezionatoResultOption").attr("name", "mittentiProtocollo$$$" + progressivo + ".tipoInvio");
        if(elemento.INDIRIZZO_PEC){
            htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("PEC", "PEC"));
        }
        if(elemento.INDIRIZZO_POSTALE){
            htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Posta", "Posta"));
        }
        htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Brevimano", "Brevimano"));
        htmlElement.find(".rubricaIpaSelezionatoResultOption").append(new Option("Altro", "Altro"));
        //fine select
    }

    htmlElement.find(".rubricaIpaSelezionatoResultIdInput").attr("name", "mittentiProtocollo$$$" + progressivo + ".progressivo");
    htmlElement.find(".rubricaIpaSelezionatoResultIdInput").val(progressivo);
    htmlElement.find(".rubricaIpaSelezionatoResult").on("click", function () {
        btnEditContantsClick(this);
    });

    //remove
    //if ($('#ipaAutocomplete-id').val() == "") {
    htmlElement.find(".rubricaIpaSelezionatoResultRemove").on("click", function () {
        btnRemoveContantsClick(this);
    });

    htmlElement.find(".rubricaIpaSelezionatoResultOption").on("change", function () {
        addMezzoValue(this);
    });

    //}
    //if ($('#rubricaAutocomplete-id').val() == "") {
    //  htmlElement.find(".rubricaIpaSelezionatoResultRemove").on("click", function () {
    //     btnRemoveRubricaClick(this);
    // });
    //}

    $("#rubricaIpaUl").append(htmlElement);

    ipaDetail = null;

    //azzero e disabilito i campi unità organizzativa utente e rubrica
    $("#ipaAutocomplete").val("");
    $("#ipaAutocomplete-id").val("");
    $("#rubricaAutocomplete").val("");
    $("#rubricaAutocomplete-id").val("");

    if(listaMittentiSelezionati.length>0 && $("#versoDocumento-id").val()=="E"){ //se Entrata limito a 1 elemento i mittenti
        $("#btnAddContacts, #btnSelectContacts, #rubricaAutocomplete, #radio1, #radio2").attr("disabled", "disabled");
    } else {
        $("#btnAddContacts, #btnSelectContacts, #rubricaAutocomplete, #radio1, #radio2").removeAttr("disabled");
    }
}
//fine gestione elementi lista IPA e Rubrica

//gestione elementi lista protocolloMittente
function btnSelectProtocolloMittenteClick(elemento) {
    $("#protocolloMittenteSelezionato").show();
    progressivoProtocolloMittente = progressivoProtocolloMittente + 1;
    var progressivo = progressivoProtocolloMittente;
    var htmlElement = $("#protocolloMittenteTemplate").clone();
    htmlElement.removeClass("hidden");
    htmlElement.removeAttr("id");

    htmlElement.find(".protocolloMittenteSelezionatoResult").html("<span style=\"color:black\" class=\"glyphicon glyphicon-user\"></span> " + elemento.numero);
    htmlElement.find(".protocolloMittenteSelezionatoResultInput").attr("name", "protocollo.numPg");
    htmlElement.find(".protocolloMittenteSelezionatoResultInput").val(elemento.numero);
    htmlElement.find(".protocolloMittenteSelezionatoResultDataInput").html(" del " + elemento.data);
    htmlElement.find(".protocolloMittenteSelezionatoResultDataInput").attr("name", "protocollo.data");
    htmlElement.find(".protocolloMittenteSelezionatoResultDataInput").val(elemento.data);
    htmlElement.find(".protocolloMittenteSelezionatoResultIdInput").attr("name", "protocollo.progressivo");
    htmlElement.find(".protocolloMittenteSelezionatoResultIdInput").val(progressivo);
    htmlElement.find(".protocolloMittenteSelezionatoResultClassificaInput").attr("name", "protocollo.classificaPg");
    htmlElement.find(".protocolloMittenteSelezionatoResultClassificaInput").val(classifica);
    htmlElement.find(".protocolloMittenteSelezionatoResultFascicoloInput").attr("name", "protocollo.fascicoloPg");
    htmlElement.find(".protocolloMittenteSelezionatoResultFascicoloInput").val(fascicolo);
    //remove
    htmlElement.find(".protocolloMittenteSelezionatoResultRemove").on("click", function () {
        btnRemoveProtocolloMittenteClick(this);
    });

    $("#protocolloMittenteUl").append(htmlElement);
    protocolloMittenteDetail = null;
}
//fine gestione elementi lista protocolloMittente


//FUNCTION AUTOCOMPLETE
function mittPers(idDestinatario) {
    if (mittenteAutocompleteInit) {
        $("#mittentiPersonaAutocomplete").autocomplete("destroy");
        $("#mittentiPersonaAutocomplete").val("");
        $("#mittentiPersonaAutocomplete-id").val("");
    }
    $("#mittentiPersonaAutocomplete").autocomplete({
        source: function (request, response) {
            $.ajax({
                //autocompletionService?fq=type:user&global=true&fl=type,sid,name:[expression%20expr="USER_ID%2b%27%20(%27%2bsid%2b%27)%27"]
                url: "/docer/v1/solr/select?database=false&fl=USER_ID,FULL_NAME&fq=type:user&sort=sid%20asc&rows=100",
                data: {
                    fq: "groups:(" + idDestinatario + "@group)",
                    q: request.term
                },
                success: function (data) {
                    response(data.data);
                }
            });
        },
        minLength: 1,
        response: function (event, ui) {
            if (ui.content.length > 0) {
                ui.content.forEach(function (singoloElemento) {
                    singoloElemento.value = singoloElemento.USER_ID;
                    singoloElemento.label = singoloElemento.FULL_NAME;
                });
            }
        },
        select: function (event, ui) {
            $("#mittentiPersonaAutocomplete-id").val(ui.item.USER_ID);
            //$("#mittentiPersonaAutocomplete").val(ui.item.FULL_NAME + " ("+ui.item.USER_ID+")" );
            mittentePersonaDetail = ui.item;
            listaMittenteSelezionato[0] = mergeMittenteSelezionato(listaMittenteSelezionato[0], parseItemMittentePersona(mittentePersonaDetail));
        }
    });
    mittenteAutocompleteInit = true;
}

function mergeMittenteSelezionato(select1, select2) {
    var risultato = {};
    for(chiave in select1) {
        risultato[chiave] = select1[chiave];
    }
    for(chiave in select2) {
        risultato[chiave] = select2[chiave];
    }
    return risultato;
}


function checkId() {
    var esitoId = true;

    if ( $("#modal-tipologia-input").val()== "PF" ) {
        if ($("#modal-cf-input").val()=="") {
            var idInseritoDenom = $("#modal-denominazione-input").val();
            $(".denomCheck").each(function(index){
                for(i=0;i<listaMittentiSelezionati.length;i++) {
                    var mittente = listaMittentiSelezionati[i];
                    if(mittente.id==idInseritoDenom){
                        esitoId = false;
                        $('.div-alert-warning').append('<p>Id: '+idInseritoDenom+ ' esistente!</p>');
                        $('.denomCheck').css("color", "red");
                    }
                }
            });
        } else {
            var idInseritoCf = $("#modal-cf-input").val();
            $(".cfCheck").each(function(index){
                for(i=0;i<listaMittentiSelezionati.length;i++) {
                    var mittente = listaMittentiSelezionati[i];
                    if(mittente.id==idInseritoCf){
                        esitoId = false;
                        $('.div-alert-warning').append('<p>Id: '+idInseritoCf+ ' esistente!</p>');
                        $('.cfCheck').css("color", "red");
                    }
                }
            });
        }
    }
    if ( $("#modal-tipologia-input").val()== "PG" ) {
        if ($("#modal-piva-input").val()=="") {
            var idInseritoDenom = $("#modal-denominazione-input").val();
            $(".denomCheck").each(function(index){
                for(i=0;i<listaMittentiSelezionati.length;i++) {
                    var mittente = listaMittentiSelezionati[i];
                    if(mittente.id==idInseritoDenom){
                        esitoId = false;
                        $('.div-alert-warning').append('<p>Id: '+idInseritoDenom+ ' esistente!</p>');
                        $('.denomCheck').css("color", "red");
                    }
                }
            });
        } else {
            var idInseritoPiva = $("#modal-piva-input").val();
            $(".pivaCheck").each(function(index){
                for(i=0;i<listaMittentiSelezionati.length;i++) {
                    var mittente = listaMittentiSelezionati[i];
                    if(mittente.id==idInseritoPiva){
                        esitoId = false;
                        $('.div-alert-warning').append('<p>Id: '+idInseritoPiva+ ' esistente!</p>');
                        $('.pivaCheck').css("color", "red");
                    }
                }
            });
        }
    }
    if ( $("#modal-tipologia-input").val()== "PA" ) {
        var idInserito = $("#modal-aooUfficio-input").val();
        $(".aooCheck").each(function(index){
            for(i=0;i<listaMittentiSelezionati.length;i++) {
                var mittente = listaMittentiSelezionati[i];
                if(mittente.id==idInserito){
                    esitoId = false;
                    $('.div-alert-warning').append('<p>Id: '+idInserito+ ' esistente!</p>');
                    $('.aooCheck').css("color", "red");
                }
            }
        });
    }
    return esitoId;
}

function checkCf() {
    var $regexcf=/^[A-Za-z]{6}[0-9]{2}[A-Za-z]{1}[0-9]{2}[A-Za-z]{1}[0-9]{3}[A-Za-z]{1}$/;
    var esitoCf = true;
    var cf = document.getElementById('modal-cf-input');
    if (!cf.disabled) {
        $(".cfCheck").each(function(index){
            if ($(this).val() && !$(this).val().match($regexcf)) {
                esitoCf = false;
                $('.div-alert-warning').append('<p>Codice Fiscale: <b>'+ $(this).val() +'</b> non corretto!</p>');
                $('.cfCheck').css("color", "red");
            }
        });
    }
    return esitoCf;
}

function checkPiva() {
    var $regexpiva=/^[0-9]{11}$/;
    var esitoPiva = true;
    var piva = document.getElementById('modal-piva-input');
    if (!piva.disabled) {
        if( !($('#pivaStraniera').is(':checked')) ){ //se PIVA non straniera
            $(".pivaCheck").each(function(index){
                if ($(this).val() && !$(this).val().match($regexpiva)) {
                    esitoPiva = false;
                    $('.div-alert-warning').append('<p>PIVA: <b>'+ $(this).val() +'</b> non corretto!</p>');
                    $('.pivaCheck').css("color", "red");
                }
            });
        }
    }
    return esitoPiva;
}

function checkEmail() {
    var $regexmail=/^([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})$/;
    var esitoEmail = true;

    $(".emailCheck").each(function(index){
        if ($(this).val() && !$(this).val().match($regexmail)) {
            esitoEmail = false;
            $('.div-alert-warning').append('<p>Formato Email: <b>'+ $(this).val() +'</b> non corretto!</p>');
            $('.emailCheck').css("color", "red");
        }
    });
    return esitoEmail;
}

function popolateSelect(){
    $.ajax({
        url: "/docer/v1/report?output.type=map&output.columns=mailbox&base64=cXVlcnk9U0VMRUNUICogRlJPTSBtYWlsQWNjb3VudHMgd2hlcmUgaXNfZW5hYmxlZD0xICZkYXRhc291cmNlLmRlZmF1bHQ9amJwbTY45ded3b76ab34c9a5052242dd337aa7f",
        data: {
            username: utenteLoggato
        },
        success: successRecuperoDatiUser,
        error: errorRecuperoDatiUser
    });
}
function successRecuperoDatiUser(data, textStatus, jqXHR){
    for(chiave in data.data) {
        var valore = data.data[chiave];
        //$("#selectEmailUser").append(new Option(chiave));
        $("#selectEmailUser").append(new Option(valore.mailbox, valore.mailbox));
    }
    if (tipoProtocollazione_DOC != "E") {
        if(typeof indirizzoTelematico !== 'undefined'){
            if(indirizzoTelematico){
                $("#selectEmailUser").val(indirizzoTelematico);
            }
        }
    }
}
function errorRecuperoDatiUser(jqXHR, textStatus, errorThrown){
    console.log("data error " + errorThrown);
}



function popolateSelectMittente(){
    $.ajax({
        url: "/docer/v1/report?output.type=map&output.columns=mailbox&base64=cXVlcnk9U0VMRUNUICogRlJPTSBtYWlsQWNjb3VudHMgd2hlcmUgaXNfZW5hYmxlZD0xICZkYXRhc291cmNlLmRlZmF1bHQ9amJwbTY45ded3b76ab34c9a5052242dd337aa7f",
        data: {
            username: utenteLoggato
        },
        success: successRecuperoDatiUserMittente,
        error: errorRecuperoDatiUserMittente
    });
}

function successRecuperoDatiUserMittente(data, textStatus, jqXHR){
    for(chiave in data.data) {
        var valore = data.data[chiave];
        $("#selectEmailUserMittente").append(new Option(valore.mailbox, valore.mailbox));
    }
}

function errorRecuperoDatiUserMittente(jqXHR, textStatus, errorThrown){
    console.log("data error " + errorThrown);
}

function popolateRubricaFromCode(codice){
    $.ajax({
        url: "/docer/v1/solr/select?database=false&fq=TIPO_RUBRICA:*&fq=type:RUBRICA&fl=ID:INDIRIZZO_POSTALE,ID:FAX,ID:INDIRIZZO_PEO,ID:INDIRIZZO_PEC,ID:CODICE_FISCALE,ID:PARTITA_IVA,COD_RUBRICA,DES_RUBRICA,TIPO_RUBRICA,CODICE_FISCALE,PARTITA_IVA,INDIRIZZO_PEC,INDIRIZZO_PEO,INDIRIZZO_POSTALE,TELEFONO,FAX,AOO_UFFICIO&emptyItem=false",
        data: {
            q: codice
        },
        success: successRecuperoDatiRubrica,
        error: errorRecuperoDatiRubrica
    });
}
function successRecuperoDatiRubrica(data, textStatus, jqXHR){
    utente = data.data[0];
    if(utente){
        if(!$("#modal-amministrazione-input").val()) {
            $("#modal-amministrazione-input").val(utente.DES_RUBRICA);
        } else if ( $("#modal-amministrazione-input").val() != utente.DES_RUBRICA) {
            $("#modal-amministrazione-input").val( $("#modal-amministrazione-input").val() );
            $("#label-amministrazione-input").append(" <b>(<i>Amministrazione salvata:</i> "+ utente.DES_RUBRICA +")</b>" ).css("color", "red");
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Attenzione - L\'Amministrazione non coincide con quello salvato in rubrica!</p>');
        } else {
            $("#modal-amministrazione-input").val($("#modal-amministrazione-input").val());
        }

        if(!$("#modal-aooUfficio-input").val()) {
            $("#modal-aooUfficio-input").val(utente.AOO_UFFICIO);
        } else if ( $("#modal-aooUfficio-input").val() != utente.AOO_UFFICIO) {
            $("#modal-aooUfficio-input").val( $("#modal-aooUfficio-input").val() );
            $("#label-aooUfficio-input").append(" <b>(<i>Aoo / Ufficio salvato:</i> "+ utente.AOO_UFFICIO +")</b>" ).css("color", "red");
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Attenzione - L\'AOO/Ufficio non coincide con quello salvato in rubrica!</p>');
        } else {
            $("#modal-aooUfficio-input").val($("#modal-aooUfficio-input").val());
        }

        if(!$("#modal-denominazione-input").val()) {
            $("#modal-denominazione-input").val(utente.DES_RUBRICA);
        } else if ( $("#modal-denominazione-input").val() != utente.DES_RUBRICA) {
            $("#modal-denominazione-input").val( $("#modal-denominazione-input").val() );
            $("#label-denominazione-input").append(" <b>(<i>Nominativo salvato:</i> "+ utente.DES_RUBRICA +")</b>" ).css("color", "red");
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Attenzione - Il nominativo non coincide con quello salvato in rubrica!</p>');
        } else {
            $("#modal-denominazione-input").val($("#modal-denominazione-input").val());
        }

        if(!$("#modal-piva-input").val()) {
            $("#modal-piva-input").val(utente.PARTITA_IVA);
        } else if ( $("#modal-piva-input").val() != utente.PARTITA_IVA) {
            $("#modal-piva-input").val( $("#modal-piva-input").val() );
            $("#label-piva-input").append(" <b>(<i>Partita Iva salvata:</i> "+ utente.PARTITA_IVA +")</b>" ).css("color", "red");
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Attenzione - La Partita Iva non coincide con quella salvato in rubrica!</p>');
        } else {
            $("#modal-piva-input").val($("#modal-piva-input").val());
        }

        if(!$("#modal-cf-input").val()) {
            $("#modal-cf-input").val(utente.CODICE_FISCALE);
        } else if ( $("#modal-cf-input").val() != utente.CODICE_FISCALE) {
            $("#modal-cf-input").val( $("#modal-cf-input").val() );
            $("#label-cf-input").append(" <b>(<i>Codice Fiscale salvato:</i> "+ utente.CODICE_FISCALE +")</b>" ).css("color", "red");
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Attenzione - Il codice fiscale non coincide con quello salvato in rubrica!</p>');
        } else {
            $("#modal-cf-input").val($("#modal-cf-input").val());
        }

        if(!$("#modal-indirizzoPostale-input").val()) {
            $("#modal-indirizzoPostale-input").val(utente.INDIRIZZO_POSTALE);
        } else if ( $("#modal-indirizzoPostale-input").val() != utente.INDIRIZZO_POSTALE) {
            $("#modal-indirizzoPostale-input").val( $("#modal-indirizzoPostale-input").val() );
            $("#label-indirizzoPostale-input").append(" <b>(<i>Indirizzo salvato:</i> "+ utente.INDIRIZZO_POSTALE +")</b>" ).css("color", "red");
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Attenzione - l\'indirizzo postale non coincide con quello salvato in rubrica!</p>');
        } else {
            $("#modal-indirizzoPostale-input").val($("#modal-indirizzoPostale-input").val());
        }

        if(!$("#modal-pec-input").val()) {
            $("#modal-pec-input").val(utente.INDIRIZZO_PEC);
        } else if ( $("#modal-pec-input").val() != utente.INDIRIZZO_PEC) {
            $("#modal-pec-input").val( $("#modal-pec-input").val() );
            $("#label-pec-input").append(" <b>(<i>Pec salvata:</i> "+ utente.INDIRIZZO_PEC +")</b>" ).css("color", "red");
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Attenzione - l\'indirizzo PEC non coincide con quello salvato in rubrica!</p>');
        } else {
            $("#modal-pec-input").val($("#modal-pec-input").val());
        }

        if(!$("#modal-peo-input").val()) {
            $("#modal-peo-input").val(utente.INDIRIZZO_PEO);
        } else if ( $("#modal-peo-input").val() != utente.INDIRIZZO_PEO) {
            $("#modal-peo-input").val( $("#modal-peo-input").val() );
            $("#label-peo-input").append(" <b>(<i>Peo salvata:</i> "+ utente.INDIRIZZO_PEO +")</b>" ).css("color", "red");
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Attenzione - l\'indirizzo PEO non coincide con quello salvato in rubrica!</p>');
        } else {
            $("#modal-peo-input").val($("#modal-peo-input").val());
        }

        if(!$("#modal-fax-input").val()) {
            $("#modal-fax-input").val(utente.FAX);
        } else if ( $("#modal-fax-input").val() != utente.FAX) {
            $("#modal-fax-input").val( $("#modal-fax-input").val() );
            $("#label-fax-input").append(" <b>(<i>Fax salvato:</i> "+ utente.FAX +")</b>" ).css("color", "red");
            $('#warningNearModal').show();
            $('.div-alert-warning').append('<p>Attenzione - Il numero FAX non coincide con quello salvato in rubrica!</p>');
        } else {
            $("#modal-fax-input").val($("#modal-fax-input").val());
        }
    } else {
        $('#warningNearModal').show();
        $('.div-alert-warning').append('<p><b>Attenzione</b> - il nominativo non risulta essere salvato in Rubrica, sono stati recuperati solo i dati relativi al Tipo mezzo selezionato!</p>');
    }
}
function errorRecuperoDatiRubrica(jqXHR, textStatus, errorThrown){
    console.log("data error " + errorThrown);
}

function getDescrizioneClassifica(){
    $.ajax({
        url: "/docer/v1/solr/select?database=false&fq=type:titolario&wt=json&fl=CLASSIFICA,text:name",
        data: {
            q: "CLASSIFICA:("+classifica+")"
        },
        success: successRecuperoDescrizioneClassifica,
        error: errorRecuperoDatiDescrizioneClassifica
    });
}
var descrizioneClassifica = "";
function successRecuperoDescrizioneClassifica(data, textStatus, jqXHR){
    if(data.data.length>0){
        descrizioneClassifica = data.data[0].text;
    }else{
        descrizioneClassifica = "descrizione mancante";
    }
        $('#classificaAutocomplete').val(classifica + " (" + descrizioneClassifica +")");
        $('#classificaAutocomplete-id').val(classifica);
        $('#classificaAutocomplete-descrizione').val(descrizioneClassifica);    
}
function errorRecuperoDatiDescrizioneClassifica(jqXHR, textStatus, errorThrown){
    console.log("data error " + errorThrown);
}

function popolateSelectCodice(){
    var idDestinatario = $("#destinatariAutocomplete-id").val();
    $.ajax({
        url: "/docer/v1/solr/select?database=false&fl=USER_ID,FULL_NAME&fq=type:user&sort=sid%20asc&rows=100",
        data: {
            fq: "groups:(" + idDestinatario + "@group)",
            q: "*"
        },
        success: successRecuperoDatiCodice,
        error: errorRecuperoDatiCodice
    });
}

var listaRecuperoDati = [];
function successRecuperoDatiCodice(data, textStatus, jqXHR){
    listaRecuperoDati = data.data;//recupero tutte le info della query
    for(chiave in data.data) {
        var valore = data.data[chiave];
        $("#destinatariPersone-id").append(new Option(valore.FULL_NAME, valore.USER_ID+"*"+valore.FULL_NAME));
    }
    //$(function() {
    $('#destinatariPersone-id').multiselect({
        nonSelectedText: 'Utenti',
        nSelectedText: 'utenti selezionati!',
        allSelectedText: 'tutti selezionati',
        numberDisplayed: 2,
        selectAllText: 'Seleziona tutti',
        includeSelectAllOption: true,
        enableFiltering: true,//barra ricerca
        maxHeight: 200,
        includeSelectAllOption: true,
        buttonWidth: '100%'
    });
    //});
}
function errorRecuperoDatiCodice(jqXHR, textStatus, errorThrown){
    console.log("data error " + errorThrown);
}

function resetMultiOptions() {
    //svuoto e distruggo multiselct
    $('#destinatariPersone-id').multiselect('destroy');
    $("#destinatariPersone-id").empty();
    //svuoto i destinatari
    $("#destinatariAutocomplete").val("");
    $("#destinatariAutocomplete-id").val("");
    //disabilito il tasto per aggiungere i destinatari
    $("#btnSelectDestinatario").attr("disabled", "disabled");
    //svuoto la lista Destanatari
    destinatarioDetailList = [];
}