import { PaymentsModuleJson } from '/static/portal/json/payments-module-json.js';
import { ProceedingFooter } from '/static/portal/js/components/proceeding-footer.js?no-cache';



export function PaymentsModule() {
    var paymentsModule = {
        template: `
    <div class="container step-container">
        <h4>Moduli di pagamento</h4>
        <h5>Attivazione dei <strong>moduli di pagamento</strong> per la pratica richiesta</h5>
        <div class="card-wrapper card-space" v-for="paymentModule in paymentsModule" v-if="paymentModule.agencyModule.moduleTypeId == 3 && paymentModule.proceedingsInstance.status.code != 'INTEGRATION'">
            <div class="card card-bg card-big border-bottom-card">
                <div class="card-header">
                    <h5>{{ paymentModule.agencyModule.name }}</h5>
                    <small class="text-muted" v-if="paymentModule.uploadDate != null">File caricato: <i>{{paymentModule.documentFile}}</i></small>
                </div>
                <div class="card-body">
                    <small class="text-muted" v-if="paymentModule.uploadDate != null">Caricato il {{paymentModule.uploadDate}}.</small>
                    <small class="text-muted" v-if="paymentModule.uploadDate == null">Non ancora caricato.</small>
                    <small class="text-muted" v-if="paymentModule.acquisitionDate != null">Acquisito il {{paymentModule.acquisitionDate}}.</small>
                    <small class="text-muted" v-if="paymentModule.acquisitionDate == null">Non ancora acquisito dall'ente.</small>
                    <div class="row">
                        <div class="col-md-12" v-if="paymentModule.uploadDate == null">
                            <button type="button" class="btn btn-primary" v-if="paymentModule.htmlForm != null && paymentModule.acquisitionDate == null">
                                Compila Online
                            </button>
                            <button type="button" class="btn btn-primary" 
                                v-if="(paymentModule.agencyModule.digitalSign == null || paymentModule.agencyModule.digitalSign != true) && paymentModule.htmlForm == null && paymentModule.acquisitionDate == null">
                                Inserisci
                            </button>
                            <button type="button" class="btn btn-primary" 
                                v-if="(paymentModule.documentSize > 0 || paymentModule.documentBlob!=null) && paymentModule.agencyModule.digitalSign == true && paymentModule.acquisitionDate == null">
                                Inserisci doc firmato
                            </button>
                        </div>
                        <div class="col-md-12" v-if="paymentModule.uploadDate != null">
                            <button type="button" class="btn btn-primary" 
                                v-if="paymentModule.documentBlob != null && paymentModule.documentBlob != 0 && paymentModule.documentSize > 0">
                                Scarica <i class="fa fa-download"></i>
                            </button>
                            <button type="button" class="btn btn-primary" 
                                v-if="paymentModule.documentSize > 0 && paymentModule.agencyModule.digitalSign == true && paymentModule.htmlForm != null">
                                Scarica per firma <i class="fa fa-download"></i>
                            </button>
                            <button type="button" class="btn btn-danger">Elimina</button>
                            <p v-if="(paymentModule.documentSize > 0 || paymentModule.documentBlob!=null) && paymentModule.signed == false && paymentModule.agencyModule.digitalSign == true && paymentModule.acquisitionDate == null">
                                Si prega di caricare un documento firmato.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div v-if="documentNumber() == 0">
            <span class="lead">Al momento non ci sono documenti in questa sezione.</span>
        </div>
        <my-proceeding-footer></my-proceeding-footer>
    </div>
            `,
        props: ['stepId', 'description', 'proceedingsInstanceId'],
        data() {
            return {
                number: +this.stepId + 3,
                paymentsModule: PaymentsModuleJson(),
                documentCount: 0
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
            }
        },
        components: {
            'my-proceeding-footer': ProceedingFooter(),
        }
    }
    return paymentsModule;
}
