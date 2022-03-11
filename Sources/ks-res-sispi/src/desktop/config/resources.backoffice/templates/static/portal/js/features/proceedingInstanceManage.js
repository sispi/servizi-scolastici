import { ProceedingsInstanceJson } from '/static/portal/json/proceedings-instance-json.js';
import { LastActivityJson } from '/static/portal/json/activities-json.js';
import { ProceedingDetail } from '/static/portal/js/components/proceeding-detail.js?no-cache';
import { InstancePayments } from '/static/portal/js/components/instance-payments.js?no-cache';
import { InstanceDocuments } from '/static/portal/js/components/instance-documents.js?no-cache';
import { Timeline } from '/static/portal/js/components/timeline.component.js?no-cache';

Vue.component('App', { template: `<div class="container">
    <h2>{{ portalInstance.proceeding.title }}</h2>
    <div class="row">
        <div class="col-md-4">
            <p>
                <i class="fa fa-briefcase"></i>
                <strong>Stato: </strong> 
                <span v-if="bpmInstance.status === 1">Attivo</span>
                <span v-else-if="bpmInstance.status === 2">Completato</span>
                <span v-else-if="bpmInstance.status === 3">Rigettata</span>
            </p>
        </div>
        <div class="col-md-4">
            <p>
                <i class="fa fa-dot-circle"></i>
                <strong>Incaricato: </strong> {{ portalInstance.proceeding.accountableStaff }}
            </p>
        </div>
    </div>
    <custom-template></custom-template>
    <ul class="nav nav-tabs" id="myTab" role="tablist">
        <li class="nav-item">
            <a class="nav-link active" id="tab1-tab" data-toggle="tab" href="#tab1" role="tab" aria-controls="tab1" aria-selected="true">
                Attività <span class="badge badge-danger" v-if="tasksNotCompleted > 0">{{ tasksNotCompleted }}</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="tab2-tab" data-toggle="tab" href="#tab2" role="tab" aria-controls="tab2" aria-selected="false">
                Dettaglio
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="tab3-tab" data-toggle="tab" href="#tab3" role="tab" aria-controls="tab3" aria-selected="false">
                Pagamenti
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" id="tab3-tab" data-toggle="tab" href="#tab4" role="tab" aria-controls="tab4" aria-selected="false">
                Documenti
            </a>
        </li>
    </ul>
    <div class="tab-content" id="myTabContent">
        <div class="tab-pane p-4 fade show active" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
            <!--<my-all-activities :instance="bpmInstance"></my-all-activities>-->
            <instance-timeline :instance="bpmInstance" :user="user"></instance-timeline>
        </div>
        <div class="tab-pane p-4 fade" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
            <my-proceeding-detail :proceeding="portalInstance.proceeding"></my-proceeding-detail>
        </div>
        <div class="tab-pane p-4 fade" id="tab3" role="tabpanel" aria-labelledby="tab3-tab">
            <instance-payments :instance="bpmInstance"></instance-payments>
        </div>
        <div class="tab-pane p-4 fade" id="tab4" role="tabpanel" aria-labelledby="tab4-tab">
            <instance-documents :proceeding="portalInstance.proceeding" :bpmInstance="bpmInstance"></instance-documents>
        </div>
    </div>
    <div v-if="portalInstance.proceeding.activeCommunication == true" class="row">
        <div class="col-md-12">
            <button type="button" class="btn btn-secondary btn-sm btn-block" @click="goToChat" v-if="bpmInstance.status === 1">
                <i class="fa fa-comments" aria-hidden="true"></i> Richiedi assistenza
            </button>
        </div>
    </div>
</div>
`,
    data() {
        return {
            proceedingInstance: ProceedingsInstanceJson(),
            lastActivity: LastActivityJson(),
            //task: data("data-task"),
            portalInstance: data("data-instance"),
            bpmInstance: null,
            tasks: null,
            tasksNotCompleted: 0,
            value: true,
            user: null
        }
    },
    methods: {
        goToChat: function(){
            location.href = '/portal/features/chat?id=' + this.bpmInstance.id;
        }
    },
    components: {
        'my-proceeding-detail': ProceedingDetail(),
        'instance-payments': InstancePayments(),
        'instance-documents': InstanceDocuments(),
        'instance-timeline': Timeline(),
        'custom-template': async () => {
            const instance = data("data-instance");
            const customTemplate = instance.proceeding.customTemplate;
            const showCustomTemplate = instance.proceeding.showCustomTemplate;
            if(customTemplate != null && showCustomTemplate){
                const url = '/static/proceedingTemplate/' + customTemplate + '?no-cache';
                const template = await import(url);
                return template.CustomTemplate();
            } else {
                const whiteTemplate = {
                    template: ``
                };
                return whiteTemplate;
            }
        }
    },
    created(){
        this.user = username;
        this.bpmInstance = data("data-bpm-instance");
        const tasks = this.bpmInstance.tasks;
        tasks.forEach(task => {
            if (task.status != 'Completed'){
                this.tasksNotCompleted++;
            }
        });
    }
});
new Vue({ el: "#app" });
var bodyRecuperato = $.parseHTML(data("data-instance").proceeding.body)[0].textContent;
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