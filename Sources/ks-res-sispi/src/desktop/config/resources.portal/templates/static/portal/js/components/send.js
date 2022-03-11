import { AttachmentsJson } from '/static/portal/json/attachments-json.js';
import { PaymentsModuleJson } from '/static/portal/json/payments-module-json.js';
import { PaymentsJson } from '/static/portal/json/payments-json.js';
import { UserJson } from '/static/portal/json/user-json.js';
import { MandatoryModules } from '/static/portal/json/mandatory-modules-json.js';
import { CheckProceedingDates } from '/static/utilities/proceeding.utilities.js?no-cache';

export function Send() {
    var send = {
        template: `
    <div class="container step-container">
        <h4>Invio pratica</h4>
        <h5>Avvia la pratica cliccando sul tasto qui di seguito.</h5>
        <p v-if="isComplete()" class="text-danger">
            <strong>Attenzione! </strong>Per inviare la pratica bisogna completare tutte le sezioni.
        </p>
        <button type="button" class="btn btn-primary btn-lg" :disabled="isComplete()" v-on:click="send">INVIA ORA</button>
        <div class="mt-3">
            <div class="row">
                <div class="col-md-6">
                    <button type="button" class="btn btn-danger btn-block">Annulla</button>
                </div>
                <div class="col-md-6">
                    <button type="button" class="btn btn-primary btn-block" :disabled="!canSendProceeding()">Salva la Pratica</button>
                </div>
            </div>
        </div>
    </div>
    `,
        props: ['stepId', 'description', 'proceedingsInstanceId'],
        data() {
            return {
                number: +this.stepId + 3,
                attachments: AttachmentsJson(),
                paymentsModule: PaymentsModuleJson(),
                payments: PaymentsJson(),
                user: UserJson(),
                modules: MandatoryModules(),
                documentCount: 0,
                buttonDisabled: true
            }
        },
        methods: {
            documentNumber: function(){
                this.paymentsModule.forEach(element => {
                    if (element.agencyModule.moduleTypeId == 3 && element.proceedingsInstance.status.code != 'INTEGRATION') {
                        this.documentCount++;
                    }
                });
                return this.documentCount;
            },
            completePaymentsModule: function(){
                var isCompletePaymentsModule = true;
                if(this.documentNumber() == 0){
                    isCompletePaymentsModule = true;
                } else {
                    this.paymentsModule.forEach(paymentModule => {
                        if (paymentModule.uploadDate == null) {
                            isCompletePaymentsModule = false;
                        }
                    });
                }
                return isCompletePaymentsModule;
            },
            completeMandatoryModules: function() {
                var isCompleteMandatoryModules = true;
                this.modules.forEach(module => {
                    if(module.uploadDate == null){
                        isCompleteMandatoryModules = false;
                    }
                });
                return isCompleteMandatoryModules;
            },
            isPayd: function(){
                var isPayd = true;
                this.payments.forEach(payment => {
                    if (payment.esitoTransazione != 'OK') {
                        isPayd = false;
                    }
                });
                return isPayd;
            },
            isComplete: function(){
                console.log("pagato: " + this.isPayd());
                console.log("moduli di pagamento: " + this.completePaymentsModule());
                console.log("moduli obbligatori: " + this.completeMandatoryModules());
                if(this.completePaymentsModule() && this.isPayd() && this.completeMandatoryModules()){
                    return false;
                }
                return true;
            },
            send: function(){
                alert("Pratica inviata");
            },
            checkProceedingDates: function(proceeding) {
                return CheckProceedingDates(proceeding);
            },
            canSendProceeding: function() {
                return this.isComplete && this.checkDates() && element.proceedingsInstance.agencyProceedings.sendIfExpired;
                //&& element.proceedingsInstance.agencyProceedings.uniqueInstance
            }
        }
    }
    return send;
}
