import { MandatoryModule } from '/static/portal/js/components/mandatory-module.js?no-cache';
import { Payments } from '/static/portal/js/components/payments.js?no-cache';
import { PaymentsModule } from '/static/portal/js/components/payments-module.js?no-cache';
import { ProceedingsInstanceJson } from '/static/portal/json/proceedings-instance-json.js';
import { Attachments } from '/static/portal/js/components/attachments.js?no-cache';
import { Result } from '/static/portal/js/components/result.js?no-cache';
import { Send } from '/static/portal/js/components/send.js?no-cache';

Vue.component('App', { template: `<div class="container">
    <h2>{{ proceedingsInstance.agencyProceedings.title }}</h2>
    <h3>Servizio della sezione {{ proceedingsInstance.agencyProceedings.agencyService.service.name }}</h3>
    <p class="lead">
        {{ proceedingsInstance.status.descriptionForInstance }}
    </p>
    
    <ul class="nav nav-tabs" id="myTab" role="tablist">
        <li class="nav-item" v-for="step in proceedingsInstance.agencyProceedings.steps" v-if="step.template=='attachments'">
            <a class="nav-link active" id="tab1-tab" data-toggle="tab" href="#tab1" role="tab" aria-controls="tab1" aria-selected="true">
                Allegati
            </a>
        </li>
        <li class="nav-item" v-for="step in proceedingsInstance.agencyProceedings.steps" v-if="step.template=='paymentModules'">
            <a class="nav-link" id="tab2-tab" data-toggle="tab" href="#tab2" role="tab" aria-controls="tab2" aria-selected="false">
                Moduli di pagamento
            </a>
        </li>
        <li class="nav-item" v-for="step in proceedingsInstance.agencyProceedings.steps" v-if="step.template=='payments'">
            <a class="nav-link" id="tab3-tab" data-toggle="tab" href="#tab3" role="tab" aria-controls="tab3" aria-selected="false">
                Pagamenti
            </a>
        </li>
        <li class="nav-item" v-for="step in proceedingsInstance.agencyProceedings.steps" v-if="step.template=='mandatoryModules'">
            <a class="nav-link" id="tab4-tab" data-toggle="tab" href="#tab4" role="tab" aria-controls="tab4" aria-selected="false">
                Moduli obbligatori
            </a>
        </li>
        <li class="nav-item" v-for="step in proceedingsInstance.agencyProceedings.steps" v-if="step.template=='result'">
            <a class="nav-link" id="tab5-tab" data-toggle="tab" href="#tab5" role="tab" aria-controls="tab5" aria-selected="false">
                Riepilogo
            </a>
        </li>
        <li class="nav-item" v-for="step in proceedingsInstance.agencyProceedings.steps" v-if="step.template=='send'">
            <a class="nav-link" id="tab6-tab" data-toggle="tab" href="#tab6" role="tab" aria-controls="tab6" aria-selected="false">
                Invia
            </a>
        </li>
    </ul>
    <div class="tab-content" id="myTabContent">
        <div class="tab-pane p-4 fade show active" id="tab1" role="tabpanel" aria-labelledby="tab1-tab">
            <my-attachments v-bind="{proceedingsInstanceId: proceedingsInstance.id}">
            </my-attachments>
        </div>
        <div class="tab-pane p-4 fade" id="tab2" role="tabpanel" aria-labelledby="tab2-tab">
            <my-payments-module v-bind="{proceedingsInstanceId: proceedingsInstance.id}">
            </my-payments-module>
        </div>
        <div class="tab-pane p-4 fade" id="tab3" role="tabpanel" aria-labelledby="tab3-tab">
            <my-payments v-bind="{proceedingsInstanceId: proceedingsInstance.id}">
            </my-payments>
        </div>
        <div class="tab-pane p-4 fade" id="tab4" role="tabpanel" aria-labelledby="tab4-tab">
            <my-mandatory-module v-bind="{proceedingsInstanceId: proceedingsInstance.id}">
            </my-mandatory-module>
        </div>
        <div class="tab-pane p-4 fade" id="tab5" role="tabpanel" aria-labelledby="tab5-tab">
            <my-result v-bind="{proceedingsInstanceId: proceedingsInstance.id}">
            </my-result>
        </div>
        <div class="tab-pane p-4 fade" id="tab6" role="tabpanel" aria-labelledby="tab6-tab">
            <my-send v-bind="{proceedingsInstanceId: proceedingsInstance.id}">
            </my-send>
        </div>
    </div>
</div>
`, 
data() { 
    return {
        proceedingsInstance: ProceedingsInstanceJson()
    };
},
methods: { },
components: {
    'my-mandatory-module': MandatoryModule(),
    'my-payments': Payments(),
    'my-payments-module': PaymentsModule(),
    'my-attachments': Attachments(),
    'my-result': Result(),
    'my-send': Send(),
}
});

new Vue({ el: "#app" });
