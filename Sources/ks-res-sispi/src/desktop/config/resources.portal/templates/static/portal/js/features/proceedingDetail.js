import { CreateFavoriteProceeding } from '/static/portal/js/services/favoriteProceeding.service.js?no-cache';
import { IsPresentFavoriteProceeding } from '/static/portal/js/services/favoriteProceeding.service.js?no-cache';
import { canCreateInstance } from '/static/utilities/proceeding.utilities.js?no-cache';
import { FormatDateAndTime } from '/static/utilities/date.utilities.js?no-cache';
import { findCompilingInstance, CreateInstance } from '/static/portal/js/services/instance.service.js?no-cache';
import { FindUoByGroupId } from '/static/portal/js/services/docer.service.js?no-cache';

Vue.component('App', { template: `<div class="container">
    <div v-if="!serverError">
        <div class="alert alert-info" role="alert" v-if="!compilation.canCompile">
            {{compilation.message}}
        </div>
        <h2>{{ proceeding.title }}</h2>
        <h3>Servizio della sezione {{ proceeding.service.name }}</h3>
        <p class="lead">
            Informazioni su come avviare la pratica, documenti necessari ed eventuali costi
        </p>
        <div class="action-container">
            <div class="alert alert-danger" role="alert" v-if="!savedAsFavorite">
                Errore durante il salvataggio come procedimento preferito.
            </div>
            <div v-if="username === 'guest'">Per compilare questo procedimento devi autenticarti.</div>
            <div v-else>
                <button class="btn btn-primary btn-xs btn-icon" v-on:click="addFavorite(proceeding.id)" v-if="!isFavoriteProceeding(proceeding.id)">
                    <i class="fa fa-star" aria-hidden="true"></i>
                    <span>Aggiungi a preferiti</span>
                </button>
                <div class="mb-3"></div>
                <div v-if="proceeding.isOnline">
                    <form action="proceedingsInstance.html" method="GET">
                        <div v-if="checkIfStartable == true" class="row">
                            <div class="col-md-6">
                                <i class="fa fa-check success" aria-hidden="true"></i> Disponibile per la compilazione online
                            </div>
                            <div class="col-md-6">
                                <button type="button" v-if="compilation.canCompile === true" 
                                        v-on:click="createInstance(proceeding.id)" 
                                        class="btn btn-primary float-right">
                                    <span>Compila ora</span>
                                </button>

                                <button type="button" v-if="compilation.canCompile === false" 
                                        v-on:click="navigateToInstance(compilation)" 
                                        class="btn btn-secondary float-right">
                                    <span>Vedi istanza</span>
                                </button>

                            </div>
                        </div>
                        <div v-else class="row">
                            <div class="col-md-12">
                                <i class="fa fa-ban" aria-hidden="true"></i> Il servizio potrà essere compilato dal <b>{{ formatDate(proceeding.startDate) }}</b> al <b>{{ formatDate(proceeding.endDate) }}</b>
                            </div>
                        </div>
                    </form>
                    <div class="clearfix"></div>
                </div>
                <div v-else><i class="fa fa-check warning" aria-hidden="true"></i> Non disponibile per la compilazione online: segui le istruzioni su come presentare la pratica</div>
            </div>
        </div>
        <div id="accordionDiv1" class="collapse-div mt-3" role="tablist">
            <div class="collapse-header" id="headingA1">
                <button data-toggle="collapse" data-target="#accordion1" aria-expanded="true" aria-controls="accordion1">
                    Informazioni
                </button>
            </div>
            <div id="accordion1" class="collapse show" role="tabpanel" aria-labelledby="headingA1" data-parent="#accordionDiv1">
                <div class="collapse-body">
                    <span class="lead">Dettagli relativi al procedimento</span>
                    <br><br>
                    <p v-if="proceeding.startDate">
                        <strong>Periodo avvio procedimento</strong> <br>
                        <span>dal {{ formatDate(proceeding.startDate) }} al {{ formatDate(proceeding.endDate) }}</span>
                    </p>
                    <p v-if="proceeding.title">
                        <strong>Denominazione procedimento</strong> <br>
                        <span>{{ proceeding.title }}</span>
                    </p>
                    <p v-if="proceeding.body">
                        <strong>Descrizione procedimento</strong> <br>
                        <div id="editor">
            
                        </div>
                    </p>
                    <p v-if="proceeding.accountableOffice">
                        <strong>Ufficio responsabile di istruttoria</strong> <br>
                        <span>{{ proceeding.accountableOffice }}</span>
                    </p>
                    <p v-if="proceeding.accountableStaff">
                        <strong>Responsabile del Procedimento</strong> <br>
                        <span>{{ proceeding.accountableStaff }}</span>
                    </p>
                    <p v-if="proceeding.operatorStaff">
                        <strong>Operatore</strong> <br>
                        <span>{{ proceeding.operatorStaff }}</span>
                    </p>
                    <p v-if="proceeding.applicantRequirement">
                        <strong>Requisiti del soggetto che presenta domanda di avvio del procedimento</strong> <br>
                        <span>{{ proceeding.applicantRequirement }}</span>
                    </p>
                    <p v-if="proceeding.costs">
                        <strong>Costi</strong> <br>
                        <span>{{ proceeding.costs }}</span>
                    </p>
                    <p v-if="proceeding.attachments">
                        <strong>Documentazione da allegare alla domanda</strong> <br>
                        <span>{{ proceeding.attachments }}</span>
                    </p>
                    <p v-if="proceeding.howToSubmit">
                        <strong>Modalità di presentazione della domanda</strong> <br>
                        <span>{{ proceeding.howToSubmit }}</span>
                    </p>
                    <p v-if="proceeding.timeNeeded">
                        <strong>Tempi di conclusione del procedimento</strong> <br>
                        <span>{{ proceeding.timeNeeded }}</span>
                    </p>
                    <p v-if="proceeding.uo">
                        <strong>Informazioni di contatto</strong> <br>
                        <span>{{ uo.GROUP_NAME }}: {{ uo.EMAIL_ADDRESS }}</span>
                    </p>
                </div>
            </div>
        </div>
    </div>
    <div v-else class="alert alert-danger" role="alert">
        Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
    </div>
</div>
`,
    data() {
        const user = guest;
        var saving = false;
        const m = {
            userinfo: data("data-user")
        }
        return {
            username: user,
            proceeding: null,
            savedAsFavorite: true,
            checkIfStartable: null,
            serverError: false,
            compilation: {
                canCompile: null,
                proceedingId: null,
                sent: false,
                bpmId: null,
                message: null
            },
            uo: null,
            saving: false,
            model: m
        };
    },
    mounted: function () {
        var now = new Date().toISOString();
        if( (now > this.proceeding.startDate) && (now < this.proceeding.endDate) ){
            this.checkIfStartable = true;
        }else{
            this.checkIfStartable = false;
        }
    },
    methods: {
        addFavorite: function(proceedingId){
            const response = CreateFavoriteProceeding(proceedingId);
            if(response.status === 'success'){
                location.reload();
            } else {
                this.savedAsFavorite = false;
            }
        },
        canCreateInstance: function() {
            this.canCreateInstanceValue = canCreateInstance(this.proceeding);
            return this.canCreateInstanceValue;
        },
        isFavoriteProceeding: function(proceedingId){
            const result = IsPresentFavoriteProceeding(proceedingId);
            if(result.status === 'success'){
                return true;
            } else {
                return false;
            }
        },
        formatDate: function(date){
            return FormatDateAndTime(date);
        },
        createInstance: function(proceedingId) {
            const portalInstance = {
                'proceedingId': proceedingId,
                'model': JSON.stringify(this.model)
            };
            if(!this.saving) {
                this.saving = true;
                const portalResponse = CreateInstance(portalInstance);
                if (portalResponse.status === "success") {
                    //istanza salvata correttamente nel portale
                    location.href = "/portal/features/update-instance-start?upid=" + portalResponse.data.id;
                } else {
                    //errore durante la creazione
                    this.saving = false;
                    window.alert('Abbiamo riscontrato un errore. Riprova.');
                }
            }
        },
        navigateToInstance: function(proceeding) {
            if(proceeding.sent !== true) {
                location.href = "/portal/features/update-instance-start?upid=" + proceeding.proceedingId;
            } else if(proceeding.bpmId !== null) {
                location.href = "/portal/features/proceedingInstanceManage?id=" + proceeding.bpmId;
            } else {
                location.href = "/portal/features/myPractices";
            }
        }
    },
    created(){
        this.proceeding = data("data-proceeding");
        if(this.proceeding.uo != null){
            const response = FindUoByGroupId(this.proceeding.uo);
            if(response.status === 'success'){
                this.uo = response.data;
            } else {
                this.serverError = true;
            }
        }
        if(this.proceeding.uniqueInstance){
            const response = findCompilingInstance('UNIQUE', this.proceeding.id);
            console.log('response UNIQUE', response);
            if(response.status === 'success'){
                if(response.data === null || response.data === ''){
                    this.compilation = {
                        canCompile: true,
                        proceedingId: null,
                        sent: false,
                        bpmId: null,
                        message: null
                    };
                } else {
                    this.compilation = {
                        canCompile: false,
                        proceedingId: response.data.id,
                        sent: response.data.sent,
                        bpmId: response.data.bpmInstanceId,
                        message: 'Non puoi creare una nuova compilazione di questa istanza poiché può essere compilata solamente una volta.'
                    };
                }
            } else {
                this.serverError = true;
            }
        } else if(this.proceeding.singleInstance){
            const response = findCompilingInstance('SINGLE', this.proceeding.id);
            console.log('response SINGLE', response);
            if(response.status === 'success'){
                if(response.data === null || response.data === ''){
                    this.compilation = {
                        canCompile: true,
                        proceedingId: null,
                        sent: false,
                        bpmId: null,
                        message: null
                    };
                } else {
                    this.compilation = {
                        canCompile: false,
                        proceedingId: response.data.id,
                        sent: response.data.sent,
                        bpmId: response.data.bpmInstanceId,
                        message: 'Non puoi creare una nuova compilazione di questa istanza. Prima di compilarne un\'altra bisogna terminare quella attiva.'
                    };
                }
            } else {
                this.serverError = true;
            }
        } else {
            this.compilation = {
                canCompile: true,
                proceedingId: null,
                sent: false,
                bpmId: null,
                message: null
            };
        }
    }
});
new Vue({ el: "#app" });

var bodyRecuperato = $.parseHTML(data("data-proceeding").body)[0].textContent;
let editor;
ClassicEditor.create( document.querySelector( '#editor' ) )
    .then( newEditor => {
        editor = newEditor;

        if(bodyRecuperato){
            editor.data.set(bodyRecuperato);
            editor.isReadOnly = true;
            //disabilito inserimento immagini
            $('.ck-file-dialog-button').hide();
            $('.ck.ck-editor__top.ck-reset_all').hide();
            $('.ck.ck-content.ck-editor__editable.ck-rounded-corners.ck-editor__editable_inline.ck-blurred').css("border","none");
            //$('.ck.ck-editor__main').disable='true';
        }
    } )
    .catch( error => {
        console.error( error );
    } );


