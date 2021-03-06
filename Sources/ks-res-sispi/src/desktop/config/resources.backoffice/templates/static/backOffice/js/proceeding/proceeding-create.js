import { CreateProceeding } from '/static/portal/js/services/proceeding.service.js?no-cache';
import { DatetimeLocalFormat } from '/static/utilities/date.utilities.js?no-cache';
var App = Vue.component('App', {
    template: `
<div class="container">
    <h2>Crea un nuovo Procedimento</h2>
    <div class="alert alert-danger alert-dismissible fade show" role="alert" id="alertError" v-if="resError">
        Errore durante la creazione del procedimento.
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <div class="alert alert-warning alert-dismissible fade show" role="alert" v-if="errors.length">
        Questi campi sono obbligatori:
        <ul>
            <li v-for="error in errors">{{ error }}</li>
        </ul>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
    <div class="form-group">
        <label for="service">Servizio</label>
        <input @change="checkValidation('service',serviceName)" type="text" class="form-control is-invalid" id="service" placeholder="Servizio padre" v-model="serviceName" readonly>
        <div class="invalid-feedback">* Campo obbligatorio.</div>
    </div>
    <div class="form-group">
        <label for="title">Titolo</label>
        <input @change="checkValidation('title',title)" type="text" class="form-control is-invalid" id="title" placeholder="Titolo" v-model="title">
        <div class="invalid-feedback">* Campo obbligatorio.</div>
    </div>
    <textarea style="display:none;" class="form-control" id="body" rows="3" v-model="body"></textarea>
    <div class="form-group">
        <label for="body">Descrizione</label>
        <div id="editor">
        
        </div>
    </div>
    <div class="form-group">
        <label>Codice BPM</label>
        <select2 id="bpmCode" v-on:changed="checkValidationSelect2($event.id)" placeholder="Cerca o crea il codice" :value="bpmCode" :label="label" v-on:changed="bpmCode=$event.values" class="form-control is-invalid" multiple="true" maximum-selection-length="1"  
        :url="url" :processResults="processResults"></select2>
        <div id="bpmCodeLabel" class="invalid-feedback">* Campo obbligatorio.</div>
    </div>
    <div class="form-group">
        <label for="applicantRequirement">Requisiti del Richiedente</label>
        <input type="text" class="form-control" id="applicantRequirement" placeholder="Requisiti del Richiedente" v-model="applicantRequirement">
    </div>
    <div class="form-group">
        <label for="costs">Costi</label>
        <input type="text" class="form-control" id="costs" placeholder="Costi" v-model="costs">
    </div>
    <div class="form-group">
        <label for="attachments">Allegati</label>
        <input type="text" class="form-control" id="attachments" placeholder="Allegati" v-model="attachments">
    </div>
    <div class="form-group">
        <label for="howToSubmit">A chi presentare la richiesta</label>
        <input type="text" class="form-control" id="howToSubmit" placeholder="A chi presentare la richiesta" v-model="howToSubmit">
    </div>
    <div class="form-group">
        <label for="timeNeeded">Tempi di conclusione</label>
        <input type="text" class="form-control" id="timeNeeded" placeholder="Tempi di conclusione" v-model="timeNeeded">
    </div>
    <div class="form-group">
        <label for="accountableStaff">Personale Responsabile</label>
        <input type="text" class="form-control" id="accountableStaff" placeholder="Personale Responsabile" v-model="accountableStaff">
    </div>
    <div class="form-group">
        <label for="accountableOffice">Ufficio Responsabile</label>
        <input type="text" class="form-control" id="accountableOffice" placeholder="Ufficio Responsabile" v-model="accountableOffice">
    </div>
    <div class="form-group">
        <label for="operatorStaff">Operatore</label>
        <input type="text" class="form-control" id="operatorStaff" placeholder="Operatore" v-model="operatorStaff">
    </div>
    <div class="form-group">
        <label for="startDate">Data di Inizio</label>
        <input @change="checkValidation('startDate',startDate)" type="datetime-local" class="form-control is-invalid" id="startDate" v-model="startDate">
        <div class="invalid-feedback">* Campo obbligatorio.</div>
    </div>
    <div class="form-group">
        <label for="endDate">Data di Fine</label>
        <input disabled @change="checkValidation('endDate',endDate)" type="datetime-local" class="form-control is-invalid" id="endDate" v-model="endDate">
        <div class="invalid-feedback">* Campo obbligatorio.</div>
    </div>
    <div class="form-group">
        <label for="version">Versione</label>
        <input type="number" class="form-control" id="version" placeholder="Versione" min="1" v-model="version">
    </div>
    <div class="form-group">
        <label for="uo">Unit?? Operativa</label>
        <select2 id="uoId" placeholder="Cerca l'unit?? operativa" :value="uoId" :label="label" v-on:changed="uoId=$event.values" class="form-control" multiple="true" maximum-selection-length="1"  
        :url="url2" :processResults="uoResults" style="border: none;"></select2>
    </div>
    <div class="form-check">
        <input @change="showHideCustomInput()" type="checkbox" class="form-check-input" id="showCustomTemplate" v-model="showCustomTemplate">
        <label class="form-check-label" for="showCustomTemplate">Mostra Custom Template</label>
    </div>
    <div id="inputCustomTemplate" class="form-group">
        <input type="text" class="form-control" id="customTemplate" placeholder="Nome del Custom Template" v-model="customTemplate">
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" id="isActive" v-model="isActive">
        <label class="form-check-label" for="isActive">Attivo</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" id="isOnline" v-model="isOnline">
        <label class="form-check-label" for="isOnline">Online</label>
    </div>
    <div class="form-check">
        <input class="form-check-input" type="radio" name="instanceType" id="multipleInstance" value="multipleInstance" v-model="instanceType" checked>
        <label class="form-check-label" for="multipleInstance">Multi Istanza</label>
    </div>
    <div class="form-check">
        <input class="form-check-input" type="radio" name="instanceType" id="uniqueInstance" value="uniqueInstance" v-model="instanceType">
        <label class="form-check-label" for="uniqueInstance">Istanza Unica</label>
    </div>
    <div class="form-check disabled">
        <input class="form-check-input" type="radio" name="instanceType" id="singleInstance" value="singleInstance" v-model="instanceType">
        <label class="form-check-label" for="singleInstance">Istanza Singola</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" id="sendIfExpired" v-model="sendIfExpired">
        <label class="form-check-label" for="sendIfExpired">Invia se scaduto</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" id="filingPlan" v-model="filingPlan">
        <label class="form-check-label" for="filingPlan">Piano di Archiviazione</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" id="updating" v-model="updating">
        <label class="form-check-label" for="updating">In Aggiornamento</label>
    </div>
    <div class="form-check">
        <input type="checkbox" class="form-check-input" id="activeCommunication" v-model="activeCommunication">
        <label class="form-check-label" for="activeCommunication">Comunicazione Attiva</label>
    </div>
    <button type="submit" class="btn btn-primary" v-on:click="createProceeding">Salva</button>
    <button type="button" class="btn btn-secondary" v-on:click="goBack">Annulla</button>
</div>
`,
    data(){
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        var serviceId = null;
        var serviceName = null;
        if(urlParams.has('serviceId')){
            serviceId = urlParams.get('serviceId');
            if(urlParams.has('serviceName')){
                serviceName = urlParams.get('serviceName');
            }
        }
        return {
            serviceName: serviceName,
            service: serviceId,
            title: null,
            body: null,
            proceedingBpmCode: null,
            applicantRequirement: null,
            costs: null,
            attachments: null,
            howToSubmit: null,
            timeNeeded: null,
            accountableStaff: null,
            accountableOffice: null,
            operatorStaff: null,
            isActive: false,
            isOnline: false,
            multipleInstance: false,
            uniqueInstance: false,
            singleInstance: false,
            sendIfExpired: false,
            filingPlan: false,
            startDate: null,
            endDate: null,
            updating: false,
            activeCommunication: false,
            version: null,
            uoId: [],
            services: data("data-serviceList"),
            uos: [],
            errors: [],
            resError: false,
            bpmCode: [],
            label: null,
            customTemplate: null,
            showCustomTemplate: false,
            instanceType: 'multipleInstance'
        }
    },
    created() {
        this.bpmCode.push({ id: null, text: null });
        this.uoId.push({ id: null, text: null });
    },
    mounted: function () {
        if(this.serviceName){
            $('#service').removeClass("is-invalid").addClass("is-valid");
        }

        if(this.showCustomTemplate){
            $('#inputCustomTemplate').show();
        }else{
            $('#inputCustomTemplate').hide();
        }

        $('#bpmCodeLabel').show();

        var formatToday = this.datetimeLocalFormat(new Date());
        $('#startDate').attr('min', formatToday);
        $('#endDate').attr('min', formatToday);

    },
    methods: {
        datetimeLocalFormat: function(isoDate){
            return DatetimeLocalFormat(isoDate);
        },
        processResults : function(data) {
            var data= $.map(data.data, function (item) {
                return { id: item.id, text: item.deployment.processId };
            });

            return { results: data };
        },
        uoResults : function(data) {
            var data= $.map(data.data, function (item) {
                return { id: item.GROUP_ID, text: item.GROUP_NAME };
            });

            return { results: data };
        },
        url: function(params) {
            var query ="/bpm/v1/configurations?runnableOnly=true&processId=";

            if (!params.term)
                return query + "__NULL__";
            else
                return query + params.term;
        },
        url2: function(params) {
            var query ="/docer/v1/gruppi?q=GRUPPO_STRUTTURA:true&fq=name:";

            if (!params.term)
                return query + "__NULL__";
            else
                return query + params.term + "*";
        },
        createProceeding: function(){
            //recupero il body dall'editor avanzato
            this.body = editor.getData();

            if(this.service && this.title && this.bpmCode[0].id){
                let sDate = null;
                let eDate = null;
                if(this.startDate){
                    sDate = new Date(this.startDate);
                }
                if(this.endDate){
                    eDate = new Date(this.endDate);
                }
                if(this.instanceType === 'multipleInstance'){
                    this.multipleInstance = true;
                    this.uniqueInstance = false;
                    this.singleInstance = false;
                } else if(this.instanceType === 'uniqueInstance'){
                    this.multipleInstance = false;
                    this.uniqueInstance = true;
                    this.singleInstance = false;
                } else if(this.instanceType === 'singleInstance'){
                    this.multipleInstance = false;
                    this.uniqueInstance = false;
                    this.singleInstance = true;
                }
                const proceeding = {
                    'serviceId': this.service,
                    'title': this.title,
                    'body': this.body,
                    'configurationId': this.bpmCode[0].id,
                    'processId': this.bpmCode[0].text,
                    'applicantRequirement': this.applicantRequirement,
                    'costs': this.costs,
                    'attachments': this.attachments,
                    'howToSubmit': this.howToSubmit,
                    'timeNeeded': this.timeNeeded,
                    'accountableStaff': this.accountableStaff,
                    'accountableOffice': this.accountableOffice,
                    'operatorStaff': this.operatorStaff,
                    'isActive': this.isActive,
                    'isOnline': this.isOnline,
                    'multipleInstance': this.multipleInstance,
                    'uniqueInstance': this.uniqueInstance,
                    'sendIfExpired': this.sendIfExpired,
                    'filingPlan': this.filingPlan,
                    'startDate': sDate,
                    'endDate': eDate,
                    'updating': this.updating,
                    'activeCommunication': this.activeCommunication,
                    'version': this.version,
                    'uoId': this.uoId[0].id,
                    'customTemplate': this.customTemplate,
                    'showCustomTemplate': this.showCustomTemplate,
                    'singleInstance': this.singleInstance
                };
                const res = CreateProceeding(proceeding);
                if(res.status === 'success'){
                    const message = 'Il procedimento ' + this.title + ' ?? stato creato.'
                    location.href = "/backOffice/proceeding/proceeding?success=" + message;
                } else {
                    const message = 'Errore durante la creazione del procedimento.'
                    location.href = "/backOffice/proceeding/proceeding?error=" + message;
                }
            } else {
                this.errors = [];
                if (!this.service) {
                    this.errors.push('Servizio');
                }
                if (!this.title) {
                    this.errors.push('Titolo');
                }
                if (!this.proceedingBpmCode) {
                    this.errors.push('Codice BPM');
                }
            }
        },
        goBack: function(){
            window.history.back();
        },
        checkValidation: function (id, value) {
            if(value){
                $('#'+id).removeClass("is-invalid").addClass("is-valid");
            }else{
                $('#'+id).removeClass("is-valid").addClass("is-invalid");
            }

            if(this.startDate){
                $('#endDate').removeAttr("disabled");
                $('#endDate').attr('min', this.startDate);
            }
            if(this.startDate > this.endDate){
                this.endDate = "";
                $('#endDate').removeClass("is-valid").addClass("is-invalid");
            }

        },
        checkValidationSelect2: function (id) {
            if( $.isNumeric(id) ){
                $('#bpmCodeLabel').hide();
                $('.select2-container--default .select2-selection--multiple').css({
                    "border": "solid #28a745 1px"
                });
            }else{
                $('#bpmCodeLabel').show();
                $('.select2-container--default .select2-selection--multiple').css({
                    "border": "solid #dc3545 1px"
                });
            }
        },
        showHideCustomInput: function () {
            if(this.showCustomTemplate){
                $('#inputCustomTemplate').show();
            }else{
                $('#inputCustomTemplate').hide();
                this.customTemplate="";
            }
        },
    }
});
new Vue({ el: "#app" });

let editor;
ClassicEditor.create( document.querySelector( '#editor' ) )
    .then( newEditor => {
        editor = newEditor;
        //disabilito inserimento immagini
        $('.ck-file-dialog-button').hide();
    } )
    .catch( error => {
        console.error( error );
    } );